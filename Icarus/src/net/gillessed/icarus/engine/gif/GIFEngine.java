package net.gillessed.icarus.engine.gif;

import java.io.File;
import java.util.List;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.engine.api.AbstractEngine;
import net.gillessed.icarus.engine.gif.task.CompileGIFTask;
import net.gillessed.icarus.engine.gif.task.RenderImagesTask;
import net.gillessed.icarus.geometry.ViewRectangle;

public class GIFEngine extends AbstractEngine<Void> {
	public GIFEngine(List<FlameModel> flameModels,
			int pixelWidth,
			int pixelHeight,
			ViewRectangle viewRectangle,
			File targetFile, 
			int frameRate) {
		
        RenderImagesTask renderImagesTask = new RenderImagesTask(flameModels, pixelWidth, pixelHeight, viewRectangle);
        addTaskAndBarrier(renderImagesTask);

        CompileGIFTask compileGIFTask = new CompileGIFTask(frameRate, targetFile);
        addFinalTask(compileGIFTask);
	}
}
