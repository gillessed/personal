package net.gillessed.icarus.engine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.geometry.ViewRectangle;

public class GifEngine extends AbstractEngine {
	
	public static final String IDLE = "Idle";
	public static final String RENDERING_FRACTALS = "Rendering Fractals";
	public static final String RENDERING_GIF = "Rendering Gif";
	
	private final List<FlameModel> flameModels;
	private final int pixelWidth;
	private final int pixelHeight;
	private final ViewRectangle viewRectangle;
	private final File targetFile;
	private final int ticks;
	private final int frameRate;
	private final BufferedImage[] completedRenders;
	
	private final MultipleRenderThread mrt;
	private final GifRenderThread grt;

	public GifEngine(EngineMonitor engineMonitor, List<FlameModel> flameModels, int pixelWidth,
			int pixelHeight, ViewRectangle viewRectangle, File targetFile, int ticks, int frameRate) {
		super(engineMonitor, "Idle");
		this.flameModels = flameModels;
		this.pixelWidth = pixelWidth;
		this.pixelHeight = pixelHeight;
		this.viewRectangle = viewRectangle;
		this.targetFile = targetFile;
		this.ticks = ticks;
		this.frameRate = frameRate;
		completedRenders = new BufferedImage[ticks];
		
		grt = new GifRenderThread(this, null);
		mrt = new MultipleRenderThread(this, grt);
		initialThread = mrt;
	}

	public List<FlameModel> getFlameModels() {
		return flameModels;
	}

	public int getPixelWidth() {
		return pixelWidth;
	}

	public int getPixelHeight() {
		return pixelHeight;
	}

	public ViewRectangle getViewRectangle() {
		return viewRectangle;
	}

	public File getTargetFile() {
		return targetFile;
	}

	public int getTicks() {
		return ticks;
	}

	public int getFrameRate() {
		return frameRate;
	}

	public BufferedImage[] getCompletedRenders() {
		return completedRenders;
	}

}
