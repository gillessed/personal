package net.gillessed.icarus.engine.fractal;

/**
 * Class to store the result of an iterated function system
 * to be processed and turned into an image.
 *
 * @author gcole
 */
public class IterationHistogram {
    private final int[][] frequencies;
    private final double[][] colors;

    public IterationHistogram(int width, int height) {
        frequencies = new int[width][height];
        colors = new double[width][height];
    }

    public int[][] getFrequencies() {
        return frequencies;
    }

    public double[][] getColors() {
        return colors;
    }

    public void update(int x, int y, double color) {
        double currentColor = colors[x][y];
        int currentFrequency = frequencies[x][y];
        //TODO: feels like this can be optimized
        colors[x][y] = (currentColor * currentFrequency + color) / (currentFrequency + 1d);
        frequencies[x][y]++;
    }

    public void set(int x, int y, int frequency, double color) {
        frequencies[x][y] = frequency;
        colors[x][y] = color;
    }
}
