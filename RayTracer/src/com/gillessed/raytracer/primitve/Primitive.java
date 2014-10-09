package com.gillessed.raytracer.primitve;

import java.util.List;

import com.gillessed.raytracer.algebra.Intersection;
import com.gillessed.raytracer.algebra.Point3;
import com.gillessed.raytracer.algebra.Square;

public abstract class Primitive {
	protected Square[] boundingBox;
	
	public Square[] getBoundingBox() {
		return boundingBox;
	}
	
	public abstract List<Intersection> getIntersectionsThis(Point3 eye, Point3 pixel);
}
