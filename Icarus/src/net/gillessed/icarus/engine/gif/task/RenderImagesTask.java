package net.gillessed.icarus.engine.gif.task;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.engine.api.AbstractTask;
import net.gillessed.icarus.engine.fractal.FractalEngine;
import net.gillessed.icarus.geometry.ViewRectangle;

/**
 * This class render the frames of frame of a fractal flame.
 * 
 * @author gillessed
 */
public final class RenderImagesTask extends AbstractTask<List<BufferedImage>> {

	private final List<FlameModel> flameModels;
	private final int pixelWidth;
	private final int pixelHeight;
	private final ViewRectangle viewRectangle;

	public RenderImagesTask(List<FlameModel> flameModels,
			int pixelWidth,
			int pixelHeight,
			ViewRectangle viewRectangle) {
		super("Render Images Task");
		this.flameModels = flameModels;
		this.pixelWidth = pixelWidth;
		this.pixelHeight = pixelHeight;
		this.viewRectangle = viewRectangle;
	}

	@Override
	protected List<BufferedImage> doWork() throws Exception {
		setMaxProgress(flameModels.size());
		List<BufferedImage> renderedImages = new ArrayList<BufferedImage>();
		for(FlameModel flameModel : flameModels) {
			FractalEngine fractalEngine = new FractalEngine(flameModel, pixelWidth, pixelHeight, viewRectangle);
			renderedImages.add(fractalEngine.run());
			incrementProgress(1);
		}
		return renderedImages;
	}

}
