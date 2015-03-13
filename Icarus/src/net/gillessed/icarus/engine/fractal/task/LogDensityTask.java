package net.gillessed.icarus.engine.fractal.task;

import java.awt.Color;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.Global;
import net.gillessed.icarus.engine.api.AbstractTask;
import net.gillessed.icarus.engine.fractal.IterationHistogram;
import net.gillessed.icarus.swingui.color.ColorProvider;

/**
 * This task performs a log density correction on the aggregated histogram,
 * and then performs a gamma correction on the alpha value, and lastly create
 * a unified {@code Color} value from the colour value and alpha value.
 *
 * @author gcole
 */
public final class LogDensityTask extends AbstractTask<Color[][]> {

    private static final double LOG_BASE = Double.parseDouble(Global.getProperty(Global.LOG_BASE));

    private final double gamma;
    private final ColorProvider colorProvider;
    private final int superSampleWidth;
    private final int superSampleHeight;

    public LogDensityTask(FlameModel flameModel, int superSampleWidth, int superSampleHeight) {
        super("Log Density Task");
        this.gamma = flameModel.getGamma();
        this.colorProvider = flameModel.getColorProvider();
        this.superSampleWidth = superSampleWidth;
        this.superSampleHeight = superSampleHeight;
    }

    @Override
    public Color[][] doWork() throws Exception {
        setMaxProgress(superSampleWidth * superSampleHeight * 2);
        IterationHistogram histogram = getSingleResultForTask(AggregationTask.class);
        Color[][] colours = new Color[superSampleWidth][superSampleHeight];

        // Log density correction
        int maximum = 0;
        for(int i = 0; i < superSampleWidth; i++) {
            for(int j = 0; j < superSampleHeight; j++) {
                if(histogram.getFrequencies()[i][j] > maximum) {
                    maximum = histogram.getFrequencies()[i][j];
                }
            }
            incrementProgress(superSampleHeight);
        }
        float logmax = (float)(Math.log(maximum) / Math.log(LOG_BASE));
        for(int i = 0; i < superSampleWidth; i++) {
            for(int j = 0; j < superSampleHeight; j++) {
                int value = histogram.getFrequencies()[i][j];
                float alpha;
                if(value > 0) {
                    alpha = (float)(Math.log(value) / Math.log(LOG_BASE)) / logmax;
                } else {
                    alpha = 0;
                }
                if(alpha < 0) {
                    alpha = 0;
                }
                double correctedAlpha = Math.pow(alpha, 1 / gamma);
                double colorValue = histogram.getColors()[i][j];
                Color colour = colorProvider.getColor((int)colorValue);
                colours[i][j] = new Color(
                        colour.getRed(),
                        colour.getGreen(),
                        colour.getBlue(),
                        (int)(correctedAlpha * 255));
            }
            incrementProgress(superSampleHeight);
        }

        return colours;
    }
}
