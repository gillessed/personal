package net.gillessed.icarus.geometry;

public class BlurKernel {
	
	private final double[][] blurMatrix;
	private final int size;
	
	public BlurKernel(int size, double kernelSize) {
		this.size = size;
		blurMatrix = new double[size][size];
		int mid = (size - 1) / 2;
		double lambda = kernelSize;
		double sum = 0;
		for(int i = -mid; i <= mid; i++) {
			for(int j = -mid; j <= mid; j++) {
				double u = (1/lambda) * (double)i;
				double w = (1/lambda) * (double)j;
				double d = u*u + w*w;
				double blur = 0;
				if(d < 1) {
					blur = (1 / lambda) * 3/4 * (1 - d);
				}
				sum += blur;
				blurMatrix[i+mid][j+mid] = blur;
			}
		}
		for(int i = -mid; i <= mid; i++) {
			for(int j = -mid; j <= mid; j++) {
				blurMatrix[i+mid][j+mid] /= sum;
			}
		}
	}
	
	public double[][] getBlurMatrix() {
		return blurMatrix;
	}
	public int getSize() {
		return size;
	}
}
