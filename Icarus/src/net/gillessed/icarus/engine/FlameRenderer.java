package net.gillessed.icarus.engine;


import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.engine.fractal.FractalEngine;
import net.gillessed.icarus.geometry.ViewRectangle;

/**
 * This class will render the flame to a file in a separate
 * thread.
 *
 * @author Gregory Cole
 */

public final class FlameRenderer {

    private final ExecutorService renderService;

	public void renderFlame(final FlameModel flameModel,
	        final int pixelWidth,
	        final int pixelHeight,
	        final ViewRectangle viewRectangle,
	        final ProgressUpdater updater,
	        final Callback<BufferedImage> callback) throws Exception {
	    final ScheduledExecutorService updaterService = Executors.newScheduledThreadPool(1);
        final FractalEngine fractalEngine = new FractalEngine(flameModel, pixelWidth, pixelHeight, viewRectangle);
        final Runnable updaterCallable = new Runnable() {
            @Override
            public void run() {
                updater.updateProgress(fractalEngine.getProgress());
            }
        };
        final Callable<Void> task = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                BufferedImage canvas = fractalEngine.run();
                updaterService.shutdownNow();
                callback.callback(canvas);
                return null;
            }
        };
        renderService.submit(task);
        updaterService.scheduleAtFixedRate(updaterCallable, 100, 100, TimeUnit.MILLISECONDS);
	}

	private FlameRenderer() {
	    renderService = Executors.newCachedThreadPool();
	}

	private static final FlameRenderer instance = new FlameRenderer();
	public static FlameRenderer get() {
	    return instance;
	}
}
