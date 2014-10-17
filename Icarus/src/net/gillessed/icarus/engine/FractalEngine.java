package net.gillessed.icarus.engine;


import java.awt.Color;
import java.awt.image.BufferedImage;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.Global;
import net.gillessed.icarus.Icarus;
import net.gillessed.icarus.geometry.Symmetry;
import net.gillessed.icarus.geometry.ViewRectangle;

public class FractalEngine extends AbstractEngine {
	public static final int SUPERSAMPLE_COUNT = 
		Integer.parseInt(Global.getProperty(Global.SUPERSAMPLE_COUNT));
	
	public static final String IDLE = "Idle";
	public static final String FRACTAL_ALGORITHM = "Fractal Algorithm";
	public static final String SUPERSAMPLING = "Supersampling";
	public static final String RENDERING_IMAGE = "Rendering Image";

	private final FlameModel flameModel;
	
	private final int[][] frequencies;
	private final double[][] colors;
	private final float[][] alpha;
	
	private final Color[][] pixels;
	private final int superSampleWidth;
	private final int superSampleHeight;
	private final int pixelHeight;
	private final int pixelWidth;
	private final FractalEngineThread fet;
	private final SuperSampleEngineThread sset;
	private final RenderingEngineThread ret;
	private final ViewRectangle viewRectangle;
	private final BufferedImage canvas;

	private final Symmetry symmetry;
	
	/**
	 * Constructs an instance of the fractal engine that draws the fractals.
	 * @param engineMonitor The object that will monitor the progress changes on the thread.
	 * @param flameModel The flame we wish to render.
	 * @param pixelWidth The width of the image we render.
	 * @param pixelHeight The height of the image we render.
	 * @param viewRectangle The rectangle representing the rectangle in space we are drawing.
	 * @param canvas The image we will draw on.
	 */
	public FractalEngine(EngineMonitor engineMonitor, FlameModel flameModel, int pixelWidth, int pixelHeight, ViewRectangle viewRectangle, BufferedImage canvas) {
		super(engineMonitor, IDLE);
		this.flameModel = flameModel;
		this.viewRectangle = viewRectangle;
		this.canvas = canvas;
		
		this.pixelWidth = pixelWidth;
		this.pixelHeight = pixelHeight;
		this.symmetry = Icarus.symmetryMap.get(flameModel.getSymmetry());
		superSampleWidth = pixelWidth * SUPERSAMPLE_COUNT;
		superSampleHeight = pixelHeight * SUPERSAMPLE_COUNT;
		
		pixels = new Color[pixelWidth][pixelHeight];
		
		frequencies = new int[superSampleWidth][superSampleHeight];
		colors = new double[superSampleWidth][superSampleHeight];
		alpha = new float[superSampleWidth][superSampleHeight];
		
		clearArrays();
		
		ret = new RenderingEngineThread(this, null);
		sset = new SuperSampleEngineThread(this, ret);
		fet = new FractalEngineThread(this, sset);
		initialThread = fet;
	}

	public FlameModel getFlameModel() {
		return flameModel;
	}
	
	public void clearArrays() {
		for(int i = 0; i < getPixelWidth(); i++) {
			for(int j = 0; j < getPixelHeight(); j++) {
				pixels[i][j] = new Color(255,255,255);
			}
		}
		for(int i = 0; i < getSuperSampleWidth(); i++) {
			for(int j = 0; j < getSuperSampleHeight(); j++) {
				frequencies[i][j] = 1;
				colors[i][j] = 0;
				alpha[i][j] = 0;
			}
		}
	}

	public ViewRectangle getViewRectangle() {
		return viewRectangle;
	}

	public BufferedImage getCanvas() {
		return canvas;
	}

	public int getSuperSampleWidth() {
		return superSampleWidth;
	}

	public int getSuperSampleHeight() {
		return superSampleHeight;
	}

	public int getPixelHeight() {
		return pixelHeight;
	}

	public int getPixelWidth() {
		return pixelWidth;
	}

	public Color[][] getPixels() {
		return pixels;
	}

	public int[][] getFrequencies() {
		return frequencies;
	}

	public float[][] getAlpha() {
		return alpha;
	}

	public double[][] getColors() {
		return colors;
	}

	public Symmetry getSymmetry() {
		return symmetry;
	}
}
