package net.gillessed.icarus.engine;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import net.gillessed.icarus.event.ProgressChangeEvent;
import net.gillessed.icarus.fileIO.AnimatedGifEncoder;

public class GifRenderThread extends EngineThread{

	private final GifEngine gifEngine;
	
	public GifRenderThread(GifEngine engine, EngineThread nextThread) {
		super(engine, nextThread, GifEngine.RENDERING_GIF);
		this.gifEngine = engine;
	}

	@Override
	public void run() {
		try {
			FileOutputStream fos = new FileOutputStream(gifEngine.getTargetFile());
			AnimatedGifEncoder encoder = new AnimatedGifEncoder();
			encoder.setFrameRate(gifEngine.getFrameRate());
			encoder.start(fos);
			for(int i = 0; i < gifEngine.getTicks(); i++) {
				encoder.addFrame(gifEngine.getCompletedRenders()[i]);
				augmentProgress();
			}
			encoder.setRepeat(0);
			encoder.finish();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		gifEngine.fireProgressChangeEvent(new ProgressChangeEvent(this, 100, true, true));
	}

	@Override
	public double getProgressTotal() {
		return gifEngine.getTicks();
	}

}
