package net.gillessed.icarus;


import java.util.ArrayList;
import java.util.List;

import net.gillessed.icarus.variation.Variation;

/**
 * This class holds a function which produces a sum of weighted variations given an input point.
 */
public class Function {
	/**
	 * This is the probability coefficient assigned to this particular variation of functions.
	 */
	private double probability;

	/**
	 * This is the probability that has been normalized so that the total of all of them
	 * will be equal to 1.
	 */
	private double normalizedProbability;

	/**
	 * This is the affine transform that will be used in conjunction with this variation.
	 */
	private AffineTransform affineTransform;

	/**
	 * This is the color integer assgined to this variation in the overall gradient
	 */
	private int color;

	/**
	 * This is the list of possible functions to apply after the affine transform.
	 */
	private final List<Variation> variations;

	public Function(List<Variation> variationDefinitions) {
		variations = new ArrayList<Variation>();
		for(Variation v : variationDefinitions) {
			variations.add(v.createCopy());
		}
		probability = 1.0;
		color = 0;
		setAffineTransform(new AffineTransform());
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public double getProbability() {
		return probability;
	}

	public List<Variation> getVariations() {
		return variations;
	}

	public Variation getVariation(Variation variationTemplate) {
	    for(Variation variation : variations) {
	        if(variation.getName().equals(variationTemplate.getName())) {
	            return variation;
	        }
	    }
	    throw new RuntimeException("No variation with name [" + variationTemplate.getName() + "] exists.");
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getColor() {
		return color;
	}

	public AffineTransform getAffineTransform() {
		return affineTransform;
	}

	public void setNormalizedProbability(double normalizedProbability) {
		this.normalizedProbability = normalizedProbability;
	}

	public double getNormalizedProbability() {
		return normalizedProbability;
	}

	public void setAffineTransform(AffineTransform affineTransform) {
		this.affineTransform = affineTransform;
	}

	public Function copyFunction() {
		Function newFunction = new Function(Icarus.variationList());
		newFunction.setColor(getColor());
		newFunction.setNormalizedProbability(getNormalizedProbability());
		newFunction.setProbability(probability);
		newFunction.setAffineTransform(affineTransform.copyAffineTransform());
		for(int i = 0; i < variations.size(); i++) {
			newFunction.getVariations().get(i).setWeight(variations.get(i).getWeight());
		}
		return newFunction;
	}
}
