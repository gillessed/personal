package net.gillessed.icarus.engine.fractal.task;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.gillessed.icarus.Global;
import net.gillessed.icarus.engine.api.AbstractTask;
import net.gillessed.icarus.engine.fractal.DownsampleChunk;

/**
 * This class downsamples a portion of the supersampled
 * iterated function system aggregation.
 *
 * @author gcole
 */
public class DownsampleTask extends AbstractTask<DownsampleChunk> {

    public static final int SUPERSAMPLE_COUNT =
            Integer.parseInt(Global.getProperty(Global.SUPERSAMPLE_COUNT));

    private int startX;
    private int endX;
    private int startY;
    private int endY;

    public DownsampleTask(String name, int startX, int endX, int startY, int endY) {
        super(name);

        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
    }

    @Override
    public DownsampleChunk call() throws Exception {
        int dx = endX - startX;
        int dy = endY - startY;
        setMaxProgress(dx * dy);
        Color[][] supersample = getSingleResultForTask(BlurTask.class);
        Color[][] colorChunk = new Color[dx][dy];
        for(int i = startX; i < endX; i++) {
            for(int j = startY; j < endY; j++) {
                List<Color> colorsToAverage = new ArrayList<Color>();
                for(int _i = 0; _i < SUPERSAMPLE_COUNT; _i++) {
                    for(int _j = 0; _j < SUPERSAMPLE_COUNT; _j++) {
                        int superX = i * SUPERSAMPLE_COUNT + _i;
                        int superY = j * SUPERSAMPLE_COUNT + _j;
                        colorsToAverage.add(supersample[superX][superY]);
                    }
                }

                colorChunk[i - startX][j - startY] = colorAverage(colorsToAverage);
            }
            incrementProgress(endY - startY);
        }
        return new DownsampleChunk(colorChunk, startX, endX, startY, endY);
    }

    /**
     * Utility method that returns the average of
     * all the colors in {@code colors}.
     */
    private Color colorAverage(List<Color> colors) {
        double r = 0;
        double g = 0;
        double b = 0;
        double l = 0;
        double a = 0;
        for(Color c : colors) {
                l++;
                r += c.getRed();
                g += c.getGreen();
                b += c.getBlue();
                a += c.getAlpha();
        }
        if(l == 0) {
            return Color.black;
        } else {
           return new Color((int)(r/l),(int)(g/l),(int)(b/l),(int)(a/l));
        }
    }
}
