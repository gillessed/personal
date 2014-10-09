package com.gillessed.raytracer.algebra;

import java.awt.geom.Point2D;

import com.gillessed.raytracer.scenenode.GeometryNode;

public class Intersection {
	public GeometryNode geometryNode;
	public double tValue;
	public Point3 rawIntersection;
	public Vector3 rawNormal;
	public Matrix4x4 transformation;
	public Matrix4x4 normalTransformation;
	public boolean hasNT;
	public Point2D.Double surfacePoint;
}
