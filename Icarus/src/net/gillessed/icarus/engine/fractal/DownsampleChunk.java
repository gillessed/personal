package net.gillessed.icarus.engine.fractal;

import java.awt.Color;

/**
 * This is a result class for downsampling a subset of
 * the supersampled iteration aggregation..
 *
 * @author gcole
 */
public class DownsampleChunk {
    private final Color[][] colorRaster;
    private final int startX;
    private final int endX;
    private final int startY;
    private final int endY;

    public DownsampleChunk(Color[][] colorRaster,
            int startX,
            int endX,
            int startY,
            int endY) {
        this.colorRaster = colorRaster;
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
    }

    public Color[][] getColorRaster() {
        return colorRaster;
    }

    public int getStartX() {
        return startX;
    }

    public int getEndX() {
        return endX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndY() {
        return endY;
    }
}
