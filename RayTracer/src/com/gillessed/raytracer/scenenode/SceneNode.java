package com.gillessed.raytracer.scenenode;

import java.util.ArrayList;
import java.util.List;

import com.gillessed.raytracer.algebra.Intersection;
import com.gillessed.raytracer.algebra.Matrix4x4;
import com.gillessed.raytracer.algebra.Point3;
import com.gillessed.raytracer.algebra.Vector3;

public class SceneNode {
	protected final String name;
	protected Matrix4x4 transformation;
	protected Matrix4x4 transformationInverse;
//	protected boolean difference;
	protected List<SceneNode> children;
	
	public SceneNode(String name) {
		this.name = name;
//		difference = false;
	}

	public String getName() {
		return name;
	}
	
	public void addChild(SceneNode child) {
		children.add(child);
	}
	
	public void removeChild(SceneNode child) {
		children.remove(child);
	}
	
	public void translate(Vector3 v) {
		Matrix4x4 transform = Matrix4x4.translate(v);
		transformation.multiply(transform);
	}
	
	public void scale(Vector3 v) {
		Matrix4x4 scale = Matrix4x4.scale(v);
		transformation.multiply(scale);
	}
	
	public void rotateX(double angle) {
		Matrix4x4 rot = Matrix4x4.rotateX(angle);
		transformation.multiply(rot);
	}
	
	public void rotateY(double angle) {
		Matrix4x4 rot = Matrix4x4.rotateX(angle);
		transformation.multiply(rot);
	}
	
	public void rotateZ(double angle) {
		Matrix4x4 rot = Matrix4x4.rotateX(angle);
		transformation.multiply(rot);
	}
	
	public Matrix4x4 getTransformation() {
		return transformation;
	}
	
	public Matrix4x4 getInverse() {
		return transformation.invert();
	}
	
	public List<GeometryNode> flattenGeometryNodes() {
		List<GeometryNode> ret = new ArrayList<>();
		for(SceneNode node : children) {
			List<GeometryNode> nodeRet = node.flattenGeometryNodes();
			ret.addAll(nodeRet);
		}
		return ret;
	}
	
	public void preCalculateTransformation(Matrix4x4 parentTransformation) {
		transformation = Matrix4x4.multiply(parentTransformation, transformation);
		transformationInverse = transformation.invert();
		for(SceneNode node : children) {
			node.preCalculateTransformation(transformation);
		}
	}
	
	public List<Intersection> getIntersections(Point3 eye, Point3 pixel) {
		List<Intersection> ret = new ArrayList<>();
		for(SceneNode node : children) {
			List<Intersection> nodeRet = node.getIntersections(eye, pixel);
			ret.addAll(nodeRet);
		}
		return ret; 
	}
}
