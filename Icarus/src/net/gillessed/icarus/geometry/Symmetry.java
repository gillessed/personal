package net.gillessed.icarus.geometry;

import java.util.HashSet;

public class Symmetry extends HashSet<Transformation> {
	private static final long serialVersionUID = 6860218776707102136L;
	private String name;
	public Symmetry(String name) {
		super();
		this.name = name;
	}
	@Override
	public String toString() {
		return name;
	}
}
