package com.gillessed.raytracer;

import java.awt.image.BufferedImage;
import java.util.List;

import com.gillessed.raytracer.algebra.Colour;
import com.gillessed.raytracer.algebra.Light;
import com.gillessed.raytracer.algebra.Point3;
import com.gillessed.raytracer.algebra.Vector3;
import com.gillessed.raytracer.scenenode.SceneNode;

public class Raytracer {
	public BufferedImage render(SceneNode node, int width, int height, Point3 eye, Vector3 view, 
			Vector3 up, double fov, Colour ambient, List<Light> lights) {
		
		return null;
	}
}
