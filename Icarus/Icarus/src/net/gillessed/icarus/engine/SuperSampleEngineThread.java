package net.gillessed.icarus.engine;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.gillessed.icarus.Global;
import net.gillessed.icarus.event.ProgressChangeEvent;
import net.gillessed.icarus.geometry.BlurKernel;
import net.gillessed.icarus.swingui.color.ColorProvider;

public class SuperSampleEngineThread extends EngineThread {

	private FractalEngine fractalEngine;

	private final int[][] frequencies;
	private final short[][] colors;
	private final float[][] alpha;
	private final ColorProvider colorProvider;
	private final double logBase = Double.parseDouble(Global.getProperty(Global.LOG_BASE));
	
	private Color[][] pixels;
	
	public SuperSampleEngineThread(FractalEngine engine, EngineThread nextThread) {
		super(engine, nextThread, FractalEngine.SUPERSAMPLING);
		fractalEngine = engine;
		
		frequencies = fractalEngine.getFrequencies();
		colors = fractalEngine.getColors();
		alpha = fractalEngine.getAlpha();
		pixels = fractalEngine.getPixels();
		colorProvider = fractalEngine.getFlameModel().getColorProvider();
	}

	@Override
	public void run() {
		// Log density correction
		int maximum = 0;
		for(int i = 0; i < fractalEngine.getSuperSampleWidth(); i++)
		{
			for(int j = 0; j < fractalEngine.getSuperSampleHeight(); j++)
			{
				if(frequencies[i][j] > maximum)
				{
					maximum = frequencies[i][j];
				}
				augmentProgress();
			}
		}
		float color = 0, temp = 0;
		float logmax = (float)Math.log(maximum / logBase);
		for(int i = 0; i < fractalEngine.getSuperSampleWidth(); i++)
		{
			for(int j = 0; j < fractalEngine.getSuperSampleHeight(); j++)
			{
				temp = frequencies[i][j];
				if(temp > 0)
				{
					color = (float)Math.log(temp / logBase) / logmax;
				}
				else
				{
					color = 0;
				}
				alpha[i][j] = color;
				augmentProgress();
			}
		}
		
		// Supersampling and gamma correction
		for(int i = 0; i < fractalEngine.getPixelWidth(); i++) {
			for(int j = 0; j < fractalEngine.getPixelHeight(); j++) {
				List<Color> colorsToAverage = new ArrayList<Color>();
				double alphaSum = 0;
				for(int _i = 0; _i < FractalEngine.SUPERSAMPLE_COUNT; _i++) {
					for(int _j = 0; _j < FractalEngine.SUPERSAMPLE_COUNT; _j++) {
						int superX = i * FractalEngine.SUPERSAMPLE_COUNT + _i;
						int superY = j * FractalEngine.SUPERSAMPLE_COUNT + _j;
						colorsToAverage.add(colorProvider.getColor((int)colors[superX][superY]));
						alphaSum += alpha[superX][superY];
					}
				}
				Color colorAverage = colorAverage(colorsToAverage);
				
				double alpha = Math.pow(alphaSum / (double)(FractalEngine.SUPERSAMPLE_COUNT * FractalEngine.SUPERSAMPLE_COUNT), 1 / fractalEngine.getFlameModel().getGamma());
				
				Color cAlpha = new Color(
						colorAverage.getRed(),
						colorAverage.getGreen(),
						colorAverage.getBlue(),
						(int)(alpha * 255));
				
				pixels[i][j] = cAlpha;
				augmentProgress();
			}
		}
		
		//Blur filter TODO: make this work better :/
		
		if(Boolean.parseBoolean(Global.getProperty(Global.BLUR_ON))) {
			Color[][] blurredPixels = new Color[fractalEngine.getPixelWidth()][fractalEngine.getPixelHeight()];
			double maxKernelWidth = 11;
			double minKernelWidth = 3;
			int minMid = ((int)(minKernelWidth+1) - 1) / 2;
			
			for(int i = minMid; i < fractalEngine.getPixelWidth() - minMid; i++) {
				for(int j = minMid; j < fractalEngine.getPixelHeight() - minMid; j++) {
					double kernelSize = (double)maxKernelWidth * Math.pow((1 - (double)pixels[i][j].getAlpha() / 255), 3) + 0.1;
					int binCount = (int)kernelSize;
					int mid = (binCount - 1) / 2;
					if(i - mid <= 0) mid = i;
					if(j - mid <= 0) mid = j;
					if(i + mid >= fractalEngine.getPixelWidth()) mid = fractalEngine.getPixelWidth() - i - 1;
					if(j + mid >= fractalEngine.getPixelHeight()) mid = fractalEngine.getPixelHeight() - j - 1;
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
							red += blurMatrix[mid + ii][mid + jj] * (double)c.getRed();
							green += blurMatrix[mid + ii][mid + jj] * (double)c.getGreen();
							blue += blurMatrix[mid + ii][mid + jj] * (double)c.getBlue();
							alpha += blurMatrix[mid + ii][mid + jj] * (double)c.getAlpha();
						}
					}
					blurredPixels[i][j] = new Color((int)red,(int)green,(int)blue,(int)alpha);
				}
			}
			
			for(int i = 0; i < fractalEngine.getPixelWidth(); i++) {
				for(int j = 0; j < fractalEngine.getPixelHeight(); j++) {
					pixels[i][j] = blurredPixels[i][j];
				}
			}
		}
		
		fractalEngine.fireProgressChangeEvent(new ProgressChangeEvent(this, 100, true, false));
	}
	
	private Color colorAverage(List<Color> colors) {
		double r = 0;
		double g = 0;
		double b = 0;
		double l = 0;
		for(Color c : colors) {
				l++;
				r += c.getRed();
				g += c.getGreen();
				b += c.getBlue();
		}
		if(l == 0) {
			return Color.black;
		}
		Color retColor = new Color((int)(r/l),(int)(g/l),(int)(b/l));
		return retColor;
	}

	@Override
	public double getProgressTotal() {
		if(progressTotal == 0) {
			double sw = fractalEngine.getSuperSampleWidth();
			double sh = fractalEngine.getSuperSampleHeight();
			double pw = fractalEngine.getPixelWidth();
			double ph = fractalEngine.getPixelHeight();
			progressTotal = sw * sh + 
			sw * sh +
			pw * ph;
		}
		return progressTotal;
	}

}
