package net.gillessed.icarus.variation;

import net.gillessed.icarus.geometry.Point;

/**
 * A variation is simply a (possibly non-linear) transformation on a point.
 */
public abstract class Variation {
	/**
	 * This is weight given to this specific function in a variation.
	 */
	private double weight;
	
	/**
	 * This is the name of the transformation
	 */
	private final String name;

	public Variation() {
		this.name = getClass().getSimpleName();
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public double getWeight() {
		return weight;
	}
	@Override
	public String toString() {
		return getName();
	}
	public String getName() {
		return name;
	}
	protected double getR(double x, double y) {
		double r = Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
		return r;
	}
	protected double getTheta(double x, double y) {
		double theta = Math.atan(x/y);
		return theta;
	}
	protected double getPhi(double x, double y) {
		double phi = Math.atan(y/x);
		return phi;
	}
	protected double getRandom(double x, double y) {
		double random = (int)(Math.random() * 2);
		return random;
	}
	protected double getOmega(double x, double y) {
		double omega = (int)(Math.random() * 2) * Math.PI;
		return omega;
	}
	
	/**
	 * This calculate the transformation performed on x and y.
	 */
	public abstract Point calculate(double inx, double iny);
	
	/**
	 * This is needed to create all the different copies of the variations from the original set.
	 * @return A copy of the variation, created by reflection.
	 */
	public Variation createCopy() {
		try {
			return getClass().newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
