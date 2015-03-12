package net.gillessed.icarus.engine.fractal.task;

import java.awt.Color;
import java.util.List;

import net.gillessed.icarus.engine.api.AbstractTask;
import net.gillessed.icarus.engine.fractal.DownsampleChunk;

/**
 * This task aggregates the result of the downsample tasks.
 *
 * @author gcole
 */
public final class DownsampleAggregationTask extends AbstractTask<Color[][]> {

    private final int pixelWidth;
    private final int pixelHeight;

    public DownsampleAggregationTask(int pixelWidth, int pixelHeight) {
        super("Downsample Aggregation Task");
        this.pixelWidth = pixelWidth;
        this.pixelHeight = pixelHeight;
    }

    @Override
    public Color[][] call() throws Exception {
        List<DownsampleChunk> chunks = getResultsForTask(DownsampleTask.class);
        setMaxProgress(chunks.size());
        Color[][] colours = new Color[pixelWidth][pixelHeight];
        for(DownsampleChunk chunk : chunks) {
            for(int i = chunk.getStartX(); i < chunk.getEndX(); i++) {
                for(int j = chunk.getStartY(); j < chunk.getEndY(); j++) {
                    colours[i][j] = chunk.getColorRaster()[i - chunk.getStartX()][j - chunk.getStartY()];
                }
            }
        }
        return colours;
    }
}
