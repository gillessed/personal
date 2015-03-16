package net.gillessed.icarus.engine.gif.task;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import net.gillessed.icarus.engine.api.AbstractTask;
import net.gillessed.icarus.fileIO.AnimatedGifEncoder;

public class CompileGIFTask extends AbstractTask<Void> {

	private final int frameRate;
	private final File targetFile;

	public CompileGIFTask(int frameRate, File targetFile) {
		super("Compile GIF Task");
		this.frameRate = frameRate;
		this.targetFile = targetFile;
	}

	@Override
	protected Void doWork() throws Exception {
		List<BufferedImage> renderedImages = getSingleResultForTask(RenderImagesTask.class);
		setMaxProgress(renderedImages.size());
		FileOutputStream fos = new FileOutputStream(targetFile);
		AnimatedGifEncoder encoder = new AnimatedGifEncoder();
		encoder.setFrameRate(frameRate);
		encoder.start(fos);
		for(int i = 0; i < renderedImages.size(); i++) {
			encoder.addFrame(renderedImages.get(i));
			incrementProgress(1);
		}
		encoder.setRepeat(0);
		encoder.finish();
		fos.close();
		return null;
	}
}
