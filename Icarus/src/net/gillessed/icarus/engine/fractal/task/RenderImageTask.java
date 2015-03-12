package net.gillessed.icarus.engine.fractal.task;

import java.awt.Color;
import java.awt.image.BufferedImage;

import net.gillessed.icarus.engine.api.AbstractTask;

/**
 * This task render a partial amount of the pixels to the
 * image we want to generate.
 * @author gcole
 */
public class RenderImageTask extends AbstractTask<Void> {

    private final BufferedImage image;
    private final int startX;
    private final int endX;
    private final int startY;
    private final int endY;

    public RenderImageTask(String name, BufferedImage image, int startX, int endX, int startY, int endY) {
        super(name);
        this.image = image;
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
    }

    @Override
    public Void call() throws Exception {
        Color[][] pixels = getSingleResultForTask(DownsampleAggregationTask.class);
        int dx = startX - endX;
        int dy = startY - endY;
        setMaxProgress(dx * dy);

        for(int i = startX; i < endX; i++) {
            for(int j = startY; j < endY; j++) {
                Color c = pixels[i][j];
                double alpha = c.getAlpha() / 255.0;
                int[] data = new int[] {(int)(c.getRed() * alpha), (int)(c.getGreen() * alpha), (int)(c.getBlue() * alpha), 255};
                image.getRaster().setPixel(i, j, data);
            }
            incrementProgress(dy);
        }

        return null;
    }

}
