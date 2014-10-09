package net.gillessed.icarus.engine;

import java.awt.image.BufferedImage;
import java.util.concurrent.Semaphore;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.event.ProgressChangeEvent;

public class MultipleRenderThread extends EngineThread {

	private final Semaphore tickSemaphore;
	private final Semaphore finalSemaphore;
	private final GifEngine gifEngine;

	public MultipleRenderThread(GifEngine engine, EngineThread nextThread) {
		super(engine, nextThread, GifEngine.RENDERING_FRACTALS);
		gifEngine = engine;
		finalSemaphore = new Semaphore(1 - gifEngine.getTicks());
		tickSemaphore = new Semaphore(3);
	}

	@Override
	public void run() {
		final Object lock = new Object();
		for(int tick = 0; tick < gifEngine.getTicks(); tick++) {
			final BufferedImage canvas = new BufferedImage(gifEngine.getPixelWidth(),
					gifEngine.getPixelHeight(), BufferedImage.TYPE_INT_RGB);
			final int finalTick = tick;
			EngineMonitor monitor = new EngineMonitor() {
				
				@Override
				public void setThreadState(String threadState) {}
				
				@Override
				public FlameModel getFlameModel() {
					return gifEngine.getFlameModels().get(finalTick);
				}
				
				@Override
				public void fireProgressChangeEvent(ProgressChangeEvent e) {
					if(e.isEngineDone()) {
						synchronized(lock) {
							gifEngine.getCompletedRenders()[finalTick] = canvas;
							tickSemaphore.release();
							finalSemaphore.release();
							augmentProgress();
						}
					}
				}
			};
			try {
				tickSemaphore.acquire();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			FractalEngine fe = new FractalEngine(monitor, gifEngine.getFlameModels().get(tick),
					gifEngine.getPixelWidth(), gifEngine.getPixelHeight(), gifEngine.getViewRectangle(), canvas);
			fe.run();
		}
		try {
			finalSemaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gifEngine.fireProgressChangeEvent(new ProgressChangeEvent(this, 100, true, false));
	}

	@Override
	public double getProgressTotal() {
		return gifEngine.getTicks();
	}

}
