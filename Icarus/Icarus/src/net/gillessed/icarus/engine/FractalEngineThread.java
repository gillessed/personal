package net.gillessed.icarus.engine;


import java.awt.Dimension;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.Global;
import net.gillessed.icarus.event.ProgressChangeEvent;
import net.gillessed.icarus.geometry.Symmetry;
import net.gillessed.icarus.geometry.ViewRectangle;
import net.gillessed.threadpool.Callback;

public class FractalEngineThread extends EngineThread {
	
	private static final int BEGINNING_SKIP_COUNT = 
		Integer.parseInt(Global.getProperty(Global.BEGINNING_SKIP_COUNT));
	
	private final FlameModel flameModel;
	private FractalEngine fractalEngine;
	private final long quality;
	private final long threadCount = Long.parseLong(Global.getProperty(Global.THREAD_POOL_COUNT));
	
	public FractalEngineThread(FractalEngine engine, EngineThread superSampleThread) {
		super(engine, superSampleThread, FractalEngine.FRACTAL_ALGORITHM);
		localProgress = 0;
		fractalEngine = engine;
		flameModel = fractalEngine.getFlameModel();
		quality = (long)(Math.pow(10,flameModel.getQuality()));
	}

	@Override
	public void run() {
		fractalEngine.clearArrays();
		FractalEngineThreadPool threadPool = new FractalEngineThreadPool(threadCount,
		new Callback<Void>() {
			@Override
			public void callback(Void value) {
				fractalEngine.fireProgressChangeEvent(new ProgressChangeEvent(this, 100, true, false));
			}
		}, this, quality);
		threadPool.start();
	}

	@Override
	public double getProgressTotal() {
		if(progressTotal == 0){
			progressTotal = quality + threadCount * BEGINNING_SKIP_COUNT;
		}
		return progressTotal;
	}
	
	public void updateModel(int cx, int cy, short color) {
		fractalEngine.getColors()[cx][cy] = color;
		fractalEngine.getFrequencies()[cx][cy]++;
	}

	public FlameModel getFlameModel() {
		return flameModel;
	}

	public Symmetry getSymmetry() {
		return fractalEngine.getSymmetry();
	}

	public ViewRectangle getViewRectangle() {
		return fractalEngine.getViewRectangle();
	}

	public void calculated() {
		augmentProgress();
	}

	public Dimension getSuperSampleDimensions() {
		return new Dimension(fractalEngine.getSuperSampleWidth(),
				fractalEngine.getSuperSampleHeight());
	}
}
