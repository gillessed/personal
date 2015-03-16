package net.gillessed.icarus;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.gillessed.icarus.event.FunctionEvent;
import net.gillessed.icarus.event.FunctionListener;
import net.gillessed.icarus.swingui.gradient.ColorProvider;
import net.gillessed.icarus.variation.Variation;

/**
 * Model for holding the different parameters of the fractal flame
 * including the affine transforms, and the different functions
 * used.
 */
public class FlameModel {
	/**
	 * A default initial quality value for any newly created flame models.
	 */
	public final static int INITIAL_QUALITY = 6;

	/**
	 * A default initial gamma value for any newly created flame models.
	 */
	public final static double INITIAL_GAMMA = 2.2;
	
	/**
	 * This is the corresponding file where the flame is saved. If it is null, the flame has not been saved yet.
	 */
	private File file;
	private int quality;
	private double gamma;
	private boolean blur;
	private String name;
	private String symmetry;
	private ColorProvider colorProvider;
	private final List<Function> functions;
	private final List<FunctionListener> listeners;
	private final List<Variation> variationDefinitions;
	
	public FlameModel(ColorProvider colorProvider) {
		this(null, colorProvider, Icarus.symmetryDefault);
	}
	
	public FlameModel(String name, ColorProvider colorProvider, String symmetry) {
		this.variationDefinitions = Icarus.variationList();
		this.symmetry = symmetry;
		this.name = name;
		this.colorProvider = colorProvider;
		functions = new ArrayList<Function>();
		Function initialFunction = new Function(variationDefinitions);
		functions.add(initialFunction);
		listeners = new ArrayList<FunctionListener>();
		file = null;
		quality = INITIAL_QUALITY;
		gamma = INITIAL_GAMMA;
	}
	
	public FlameModel(String name, ColorProvider colorProvider, String symmetry, List<Function> functions) {
		this.symmetry = symmetry;
		this.name = name;
		this.functions = functions;
		this.colorProvider = colorProvider;
		this.variationDefinitions = Icarus.variationList();
		listeners = new ArrayList<FunctionListener>();
		file = null;
		quality = INITIAL_QUALITY;
		gamma = INITIAL_GAMMA;
	}
	
	/**
	 * Use this function only when you are loading a flame from a file.
	 */
	public void clearFunctions() {
		functions.clear();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("FlameModel [name=");
		sb.append(name);
		sb.append("; quality=");
		sb.append(quality);
		sb.append("; gamma= ");
		sb.append(gamma);
		sb.append("]");
		return sb.toString();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Function> getFunctions() {
		return functions;
	}
	
	public void addFunctionListener(FunctionListener l) {
		listeners.add(l);
	}
	
	public void removeFunctionListener(FunctionListener l) {
		listeners.remove(l);
	}
	
	public void clearFunctionListeners() {
		listeners.clear();
	}
	
	public void addFunction(Function function) {
		functions.add(function);
		for(FunctionListener l : listeners) {
			l.functionAdded(new FunctionEvent(this, function));
		}
	}
	
	public void removeFunction(Function function) {
		boolean removed = functions.remove(function);
		if(removed) {
			for(FunctionListener l : listeners) {
				l.functionRemoved(new FunctionEvent(this, function));
			}
		}
	}
	
	public List<Variation> getVariationDefinitions() {
		return variationDefinitions;
	}
	
	public void setFile(File file) {
		this.file = file;
	}
	
	public File getFile() {
		return file;
	}
	
	public void setQuality(int quality) {
		this.quality = quality;
	}
	
	public int getQuality() {
		return quality;
	}
	
	public void setGamma(double gamma) {
		this.gamma = gamma;
	}
	
	public double getGamma() {
		return gamma;
	}
	
	public boolean isBlur() {
		return blur;
	}
	
	public void setBlur(boolean blur) {
		this.blur = blur;
	}
	
	public void setColorProvider(ColorProvider colorProvider) {
		this.colorProvider = colorProvider;
	}
	
	public ColorProvider getColorProvider() {
		return colorProvider;
	}
	
	public String getSymmetry() {
		return symmetry;
	}
	
	public void setSymmetry(String symmetry) {
		this.symmetry = symmetry;
	}
	
	public FlameModel cloneFlame() {
		List<Function> functions = new ArrayList<Function>();
		for(Function f : this.functions) {
			functions.add(f.copyFunction());
		}
		FlameModel newFlame = new FlameModel(getName(), colorProvider, symmetry, functions);
		newFlame.setGamma(getGamma());
		newFlame.setQuality(getQuality());
		return newFlame;
	}
}
