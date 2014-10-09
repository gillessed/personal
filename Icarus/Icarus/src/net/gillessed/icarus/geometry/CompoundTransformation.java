package net.gillessed.icarus.geometry;

import java.util.ArrayList;
import java.util.List;

public class CompoundTransformation implements Transformation {

	private List<Transformation> transformations;
	
	public CompoundTransformation(Transformation... transformations) {
		this.transformations = new ArrayList<Transformation>();
		for(Transformation t : transformations) {
			this.transformations.add(t);
		}
	}
	
	public CompoundTransformation(List<Transformation> transformations) {
		this.transformations = new ArrayList<Transformation>();
		this.transformations.addAll(transformations);
	}

	@Override
	public Point transform(Point z) {
		Point p = z;
		for(Transformation t : transformations) {
			p = t.transform(p);
		}
		return p;
	}
}
