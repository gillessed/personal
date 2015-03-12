package net.gillessed.icarus.engine.fractal.task;

import java.awt.image.BufferedImage;

import net.gillessed.icarus.engine.api.AbstractTask;

/**
 * This is the final task to return the image as the result
 * of the engine's work.
 *
 * @author gcole
 */
public final class GetImageTask extends AbstractTask<BufferedImage> {

    private final BufferedImage image;

    public GetImageTask(BufferedImage image) {
        super("Get Image Task");
        this.image = image;

    }

    @Override
    public BufferedImage call() throws Exception {
        return image;
    }

}
