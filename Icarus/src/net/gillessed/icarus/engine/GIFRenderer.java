package net.gillessed.icarus.engine;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.engine.gif.GIFEngine;
import net.gillessed.icarus.geometry.ViewRectangle;

/**
 * This class will render the gif to a file in a separate
 * thread.
 *
 * @author Gregory Cole
 */

public class GIFRenderer {

    private final ExecutorService renderService;

	public void renderGIF(final List<FlameModel> flameModel,
	        final int pixelWidth,
	        final int pixelHeight,
	        final ViewRectangle viewRectangle,
	        final File targetFile,
	        final int frameRate,
	        final ProgressUpdater updater,
	        final Callback<Void> callback) throws Exception {
	    final ScheduledExecutorService updaterService = Executors.newScheduledThreadPool(1);
        final GIFEngine gifEngine = new GIFEngine(flameModel, pixelWidth, pixelHeight, viewRectangle, targetFile, frameRate);
        final Runnable updaterCallable = new Runnable() {
            @Override
            public void run() {
                updater.updateProgress(gifEngine.getProgress());
            }
        };
        final Callable<Void> task = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                gifEngine.run();
                updater.stop();
                updaterService.shutdownNow();
                if(callback != null) {
                	callback.callback(null);
                }
                return null;
            }
        };
        renderService.submit(task);
        updaterService.scheduleAtFixedRate(updaterCallable, 100, 100, TimeUnit.MILLISECONDS);
	}

	private GIFRenderer() {
	    renderService = Executors.newCachedThreadPool();
	}

	private static final GIFRenderer instance = new GIFRenderer();
	public static GIFRenderer get() {
	    return instance;
	}
}
