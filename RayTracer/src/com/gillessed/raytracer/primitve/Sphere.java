package com.gillessed.raytracer.primitve;

import java.util.ArrayList;
import java.util.List;

import com.gillessed.raytracer.algebra.Intersection;
import com.gillessed.raytracer.algebra.Point3;
import com.gillessed.raytracer.algebra.Roots;
import com.gillessed.raytracer.algebra.Square;
import com.gillessed.raytracer.algebra.Vector3;

public class Sphere extends Primitive {
	public Sphere() {
		Point3 v1 = new Point3(-1, -1, -1);
		Point3 v2 = new Point3(1, -1, -1);
		Point3 v3 = new Point3(1, 1, -1);
		Point3 v4 = new Point3(-1, 1, -1);
		Point3 v5 = new Point3(-1, 1, 1);
		Point3 v6 = new Point3(-1, -1, 1);
		Point3 v7 = new Point3(1, -1, 1);
		Point3 v8 = new Point3(1, 1, 1);
		Square.constructSurfaces(v1, v2, v3, v4, v5, v6, v7, v8);
	}

	@Override
	public List<Intersection> getIntersectionsThis(Point3 eye, Point3 pixel) {
		Vector3 pme = pixel.sub(eye);
		Vector3 emc = pixel.sub(new Point3(0, 0, 0));

		double a = pme.dot(pme);
		double b = 2*emc.dot(pme);
		double c = emc.dot(emc) - 1;

		double[] roots = new double[2];
		int n_roots = Roots.quadraticRoots(roots, a, b, c);
		List<Intersection> ret = new ArrayList<>();
		for(int i = 0; i < n_roots; i++) {
			Intersection intersection = new Intersection();
			intersection.tValue = roots[i];
			intersection.rawIntersection = eye.add(pme.mult(roots[i]));
			intersection.rawNormal = intersection.rawIntersection.sub(new Point3(0, 0, 0));
			double theta_x = normals[i].project_plane(xz_plane).angle_to(sphere_left) / (2 * M_PI);
			if(theta_x < 0) {
				theta_x += 1;
			}
			double theta_y = abs(normals[i].angle_to(sphere_up)) / M_PI;
			if(theta_y < 0) {
				theta_y += 1;
			}
			surface_points[i] = Point2D(theta_x, theta_y);
			}
		return n_roots;
		}
	}
}
