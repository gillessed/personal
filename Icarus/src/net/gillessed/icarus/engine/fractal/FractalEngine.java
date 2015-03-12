package net.gillessed.icarus.engine.fractal;

import java.awt.image.BufferedImage;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.Global;
import net.gillessed.icarus.engine.api.AbstractEngine;
import net.gillessed.icarus.engine.fractal.task.AggregationTask;
import net.gillessed.icarus.engine.fractal.task.BlurTask;
import net.gillessed.icarus.engine.fractal.task.DownsampleTask;
import net.gillessed.icarus.engine.fractal.task.GetImageTask;
import net.gillessed.icarus.engine.fractal.task.IterationTask;
import net.gillessed.icarus.engine.fractal.task.LogDensityTask;
import net.gillessed.icarus.engine.fractal.task.RenderImageTask;
import net.gillessed.icarus.engine.fractal.task.DownsampleAggregationTask;
import net.gillessed.icarus.geometry.ViewRectangle;

public class FractalEngine extends AbstractEngine<BufferedImage> {
    private static final int SUPERSAMPLE_COUNT =
        Integer.parseInt(Global.getProperty(Global.SUPERSAMPLE_COUNT));
    private static final long THREAD_COUNT =
            Long.parseLong(Global.getProperty(Global.THREAD_POOL_COUNT));

    private static final int DOWNSAMPLE_THREAD_SPLIT = 2;
    private static final int RENDER_THREAD_SPLIT = 2;

    public FractalEngine(FlameModel flameModel, int pixelWidth, int pixelHeight, ViewRectangle viewRectangle) {
        int superSampleWidth = SUPERSAMPLE_COUNT * pixelWidth;
        int superSampleHeight = SUPERSAMPLE_COUNT * pixelHeight;

        long quality = (long)(Math.pow(10, flameModel.getQuality()));

        for(int i = 0; i < THREAD_COUNT; i++) {
            IterationTask iterationTask = new IterationTask("Iteration Task " + i, flameModel, viewRectangle, superSampleWidth, superSampleHeight, quality / THREAD_COUNT);
            addTask(iterationTask);
        }
        addBarrier();

        AggregationTask aggregationTask = new AggregationTask(superSampleWidth, superSampleHeight);
        addTaskAndBarrier(aggregationTask);

        LogDensityTask logDensityTask = new LogDensityTask(flameModel, superSampleWidth, superSampleHeight);
        addTaskAndBarrier(logDensityTask);

        BlurTask blurTask = new BlurTask(superSampleWidth, superSampleHeight);
        addTaskAndBarrier(blurTask);

        int sdx = pixelWidth / DOWNSAMPLE_THREAD_SPLIT;
        int sdy = pixelHeight / DOWNSAMPLE_THREAD_SPLIT;
        for(int x = 0; x < DOWNSAMPLE_THREAD_SPLIT; x++) {
            for(int y = 0; y < DOWNSAMPLE_THREAD_SPLIT; y++) {
                int ex = (x + 1) * sdx;
                if(x == DOWNSAMPLE_THREAD_SPLIT - 1) {
                    ex = pixelWidth;
                }

                int ey = (y + 1) * sdy;
                if(y == DOWNSAMPLE_THREAD_SPLIT - 1) {
                    ey = pixelHeight;
                }

                DownsampleTask downsampleTask = new DownsampleTask("Downsample Task (" + x + "," + y + ")",
                        x * sdx, ex,
                        y * sdy, ey);
                addTask(downsampleTask);
            }
        }
        addBarrier();

        DownsampleAggregationTask sampleAggregationTask = new DownsampleAggregationTask(pixelWidth, pixelHeight);
        addTaskAndBarrier(sampleAggregationTask);

        BufferedImage image = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_ARGB);
        int rdx = pixelWidth / RENDER_THREAD_SPLIT;
        int rdy = pixelHeight / RENDER_THREAD_SPLIT;
        for(int x = 0; x < RENDER_THREAD_SPLIT; x++) {
            for(int y = 0; y < RENDER_THREAD_SPLIT; y++) {
                int ex = (x + 1) * rdx;
                if(x == RENDER_THREAD_SPLIT - 1) {
                    ex = pixelWidth;
                }

                int ey = (y + 1) * rdy;
                if(y == RENDER_THREAD_SPLIT - 1) {
                    ey = pixelHeight;
                }

                RenderImageTask renderImageTask = new RenderImageTask("Render Image Task (" + x + "," + y + ")",
                        image,
                        x * rdx, ex,
                        y * rdy, ey);
                addTask(renderImageTask);
            }
        }
        addBarrier();

        GetImageTask getImageTask = new GetImageTask(image);
        addFinalTask(getImageTask);
    }
}
