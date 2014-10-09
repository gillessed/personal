package com.gillessed.raytracer.scenenode;

import java.util.ArrayList;
import java.util.List;

import com.gillessed.raytracer.algebra.Intersection;
import com.gillessed.raytracer.algebra.Matrix4x4;
import com.gillessed.raytracer.algebra.Point3;


public class GeometryNode extends SceneNode {

	public GeometryNode(String name) {
		super(name);
	}

	@Override
	public List<GeometryNode> flattenGeometryNodes() {
		List<GeometryNode> ret = new ArrayList<>();
		ret.add(this);
		for(SceneNode node : children) {
			List<GeometryNode> nodeRet = node.flattenGeometryNodes();
			ret.addAll(nodeRet);
		}
		return ret;
	}

	@Override
	public void preCalculateTransformation(Matrix4x4 parentTransformation) {
		transformation = Matrix4x4.multiply(parentTransformation, transformation);
		transformationInverse = transformation.invert();
		//TODO calculate the transformed bounding box
		for(SceneNode node : children) {
			node.preCalculateTransformation(transformation);
		}
	}
	
	@Override
	public List<Intersection> getIntersections(Point3 eye, Point3 pixel) {
		List<Intersection> ret = getIntersectionsThis(transformationInverse.multiply(eye),
				transformationInverse.multiply(pixel));
		for(SceneNode node : children) {
			List<Intersection> nodeRet = node.getIntersections(eye, pixel);
			ret.addAll(nodeRet);
		}
		return ret; 
	}

	public List<Intersection> getIntersectionsThis(Point3 eye, Point3 pixel) {
		return null;
	}
}
