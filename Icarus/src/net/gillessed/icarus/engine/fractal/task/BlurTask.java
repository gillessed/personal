package net.gillessed.icarus.engine.fractal.task;

import java.awt.Color;

import net.gillessed.icarus.Global;
import net.gillessed.icarus.engine.api.AbstractTask;
import net.gillessed.icarus.geometry.BlurKernel;

/**
 * This tasks blurs the image with a dynamic Gaussian blur.
 * Still haven't figured out how to make this long very good yet.
 *
 * @author gcole
 */
public final class BlurTask extends AbstractTask<Color[][]> {

    private static final boolean BLUR_ON = Boolean.parseBoolean(Global.getProperty(Global.BLUR_ON));
    private final int superSampleWidth;
    private final int superSampleHeight;

    public BlurTask(int superSampleWidth, int superSampleHeight) {
        super("Blur Task");
        this.superSampleWidth = superSampleWidth;
        this.superSampleHeight = superSampleHeight;
    }

    @Override
    public Color[][] doWork() throws Exception {
        Color[][] pixels = getSingleResultForTask(LogDensityTask.class);
        Color[][] blurredPixels = new Color[superSampleWidth][superSampleHeight];
        if(BLUR_ON) {
            setMaxProgress(superSampleWidth * superSampleHeight);
            double maxKernelWidth = 11;
            double minKernelWidth = 3;
            int minMid = ((int)(minKernelWidth + 1) - 1) / 2;

            for(int i = 0; i < superSampleWidth; i++) {
                for(int j = 0; j < superSampleHeight; j++) {
                    blurredPixels[i][j] = pixels[i][j];
                }
            }
            
            for(int i = minMid; i < superSampleWidth - minMid; i++) {
                for(int j = minMid; j < superSampleHeight - minMid; j++) {
                    double kernelSize = maxKernelWidth * Math.pow((1 - (double)pixels[i][j].getAlpha() / 255), 3) + 0.1;
                    int binCount = (int)kernelSize;
                    int mid = (binCount - 1) / 2;
                    if(i - mid <= 0) mid = i;
                    if(j - mid <= 0) mid = j;
                    if(i + mid >= superSampleWidth) mid = superSampleWidth - i - 1;
                    if(j + mid >= superSampleHeight) mid = superSampleHeight - j - 1;
                    binCount = Math.max(mid * 2 + 1, (int)minKernelWidth);
                    mid = (binCount - 1) / 2;
                    double[][] blurMatrix = new BlurKernel(binCount, kernelSize).getBlurMatrix();
                    double red = 0;
                    double green = 0;
                    double blue = 0;
                    double alpha = 0;
                    for(int ii = -mid; ii <= mid; ii++) {
                        for(int jj = -mid; jj <= mid; jj++) {
                            Color c = pixels[i + ii][j + jj];
                            red += blurMatrix[mid + ii][mid + jj] * c.getRed();
                            green += blurMatrix[mid + ii][mid + jj] * c.getGreen();
                            blue += blurMatrix[mid + ii][mid + jj] * c.getBlue();
                            alpha += blurMatrix[mid + ii][mid + jj] * c.getAlpha();
                        }
                    }
                    blurredPixels[i][j] = new Color((int)red,(int)green,(int)blue,(int)alpha);
                    incrementProgress(1);
                }
            }
        } else {
            setMaxProgress(1);
            for(int i = 0; i < superSampleWidth; i++) {
                for(int j = 0; j < superSampleHeight; j++) {
                    blurredPixels[i][j] = pixels[i][j];
                }
            }
            incrementProgress(1);
        }
        return blurredPixels;
    }

}
