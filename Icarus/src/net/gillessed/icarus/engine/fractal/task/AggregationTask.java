package net.gillessed.icarus.engine.fractal.task;

import java.util.List;

import net.gillessed.icarus.engine.api.AbstractTask;
import net.gillessed.icarus.engine.fractal.IterationHistogram;

/**
 * This task aggregates multiple Iterated Function System histograms
 * into a single one.
 *
 * @author gcole
 */
public final class AggregationTask extends AbstractTask<IterationHistogram> {

    private final int superSampleWidth;
    private final int superSampleHeight;

    public AggregationTask(int superSampleWidth, int superSampleHeight) {
        super("Aggregation Task");
        this.superSampleWidth = superSampleWidth;
        this.superSampleHeight = superSampleHeight;
    }

    @Override
    public IterationHistogram doWork() throws Exception {
        setMaxProgress(superSampleWidth * superSampleHeight);
        List<IterationHistogram> histograms = getResultsForTask(IterationTask.class);
        IterationHistogram aggregation = new IterationHistogram(superSampleWidth, superSampleHeight);
        for(int x = 0; x < superSampleWidth; x++) {
            for(int y = 0; y < superSampleHeight; y++) {
                int frequencyTotal = 0;
                double colorTotal = 0;
                for(IterationHistogram histogram : histograms) {
                    int frequency = histogram.getFrequencies()[x][y];
                    frequencyTotal += frequency;
                    colorTotal += histogram.getColors()[x][y] * frequency;
                }
                aggregation.set(x, y, frequencyTotal, colorTotal / frequencyTotal);
            }
            incrementProgress(superSampleHeight);
        }
        return aggregation;
    }
}
