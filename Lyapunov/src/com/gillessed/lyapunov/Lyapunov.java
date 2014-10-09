package com.gillessed.lyapunov;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Lyapunov {
	private static final int DEFAULT_LIMIT = 500;
	private String lyaunovExponent;
	private double[] min;
	private double[] max;
	
	private int limit;
	private int antiAlias;
	
	/**
	 * N-dimensional lyapunov fractal.
	 */
	public Lyapunov(String lyaunovExponent, double[] min, double[] max) {
		this.lyaunovExponent = lyaunovExponent;
		this.min = min;
		this.max = max;
		limit = DEFAULT_LIMIT;
		setAntiAlias(1);
	}
	
	/**
	 * Render 2d lyapunov fractal.
	 */
	public void render(Graphics g, int width, int height) {
		render(g, width, height, new double[]{});
	}
	
	
	/**
	 * Render 2d  axis cross-section lyapunov fractal.
	 */
	public void render(Graphics g, int width, int height, double[] otherCoords) {
		// Elongate String
		StringBuilder limitExponentBuilder = new StringBuilder("");
		while(limitExponentBuilder.length() < limit) {
			limitExponentBuilder.append(lyaunovExponent);
		}
		String limitExponent = limitExponentBuilder.toString().toUpperCase();
		int aliasWidth = antiAlias * width;
		int aliasHeight = antiAlias * width;
		double dx = (max[0] - min[0]) / aliasWidth;
		double dy = (max[1] - min[1]) / aliasHeight;
		
		// Calculate lamdas
		double[][] aliasValues = new double[aliasWidth][aliasHeight];
		for(int gx = 0; gx < aliasWidth; gx++) {
			for(int gy = 0; gy < aliasHeight; gy++) {
				double xn = 0.5;
				double sum = 0;
				for(int n = 0; n < limit; n++) {
					char ch = limitExponent.charAt(n);
					int co = (int)(ch - 65);
					double rn;
					if(co == 0) {
						rn = min[0] + gx * dx;
					} else if(co == 1){
						rn = min[1] + gy * dy;
					} else {
						rn = otherCoords[co - 2];
					}
					xn = rn * xn * (1 - xn);
					sum += Math.log(Math.abs(rn * (1 - 2 * xn)));
				}
				sum /= limit;
				aliasValues[gx][gy] = sum;
			}
		}

		// Average out for antialias
		double[][] values = new double[aliasWidth][aliasHeight];
		for(int gx = 0; gx < width; gx++) {
			for(int gy = 0; gy < height; gy++) {
				double mean = 0;
				for(int tx = 0; tx < antiAlias; tx++) {
					for(int ty = 0; ty < antiAlias; ty++) {
						mean += aliasValues[gx * antiAlias + tx][gy * antiAlias + ty];
					}
				}
				values[gx][gy] = mean / (antiAlias * antiAlias);
			}
		}
		
		// Eliminate infinities
		for(int gx = 0; gx < width; gx++) {
			for(int gy = 0; gy < height; gy++) {
				if(values[gx][gy] == Double.NEGATIVE_INFINITY) {
					values[gx][gy] = 0;
				} else if(values[gx][gy] == Double.POSITIVE_INFINITY) {
					values[gx][gy] = 0;
				}
			}
		}

		// Logarithmic Adjustment to enhnace low-intensity pixels
		for(int gx = 0; gx < width; gx++) {
			for(int gy = 0; gy < height; gy++) {
				if(values[gx][gy] < 0) {
					values[gx][gy] = -Math.log(Math.abs(-1 + values[gx][gy]));
				} else {
					values[gx][gy] = Math.log(1 + values[gx][gy]);
				}
			}
		}
		
		// Find min and max
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		for(int gx = 0; gx < width; gx++) {
			for(int gy = 0; gy < height; gy++) {
				double value = values[gx][gy];
				if(value < min) {
					min = value;
				} else if(value > max) {
					max = value;
				}
			}
		}
		
		for(int gx = 0; gx < width; gx++) {
			for(int gy = 0; gy < height; gy++) {
				double value = values[gx][gy];
				if(value < 0) {
					g.setColor(new Color(0, 0, 255 - (int)(255 * value / min)));
				} else {
					g.setColor(new Color((int)(255 * value / max), (int)(255 * value / max), 0));
				}
				g.fillRect(gx, gy, 1, 1);
			}
		}
	}
	
	public void renderToImage(File file, int width, int height, double[] otherCoords) throws IOException {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		render(g, width, height, otherCoords);
		ImageIO.write(image, "png", file);
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getAntiAlias() {
		return antiAlias;
	}

	public void setAntiAlias(int antiAlias) {
		this.antiAlias = antiAlias;
	}
}
