package net.gillessed.icarus.engine;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.gillessed.icarus.event.ProgressChangeEvent;

public class RenderingEngineThread extends EngineThread {
	
	private FractalEngine fractalEngine;
	private BufferedImage dbImage;
	private Graphics dbg;
	private Color[][] pixels;
	
	public RenderingEngineThread(FractalEngine engine, EngineThread nextThread) {
		super(engine, nextThread, FractalEngine.RENDERING_IMAGE);
		fractalEngine = engine;
		dbImage = fractalEngine.getCanvas();
		dbg = dbImage.getGraphics();
		pixels = fractalEngine.getPixels();
	}

	public void run() {
		dbg.setColor(Color.black);
		dbg.fillRect(0, 0, dbImage.getWidth(), dbImage.getHeight());
		
		// Drawing to the image
		for(int i = 0; i < fractalEngine.getPixelWidth(); i++) {
			for(int j = 0; j < fractalEngine.getPixelHeight(); j++) {
				dbg.setColor(pixels[i][j]);
				dbg.fillRect(i,j,1,1);
				augmentProgress();
			}
		}
		engine.fireProgressChangeEvent(new ProgressChangeEvent(this, 100, true, false));
	}

	@Override
	public double getProgressTotal() {
		if(progressTotal == 0) {
			progressTotal = fractalEngine.getPixelWidth() * fractalEngine.getPixelHeight();
		}
		return progressTotal;
	}
}
