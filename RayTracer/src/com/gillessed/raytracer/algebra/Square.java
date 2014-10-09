package com.gillessed.raytracer.algebra;

public class Square {
	public Point3[] vertices = new Point3[4];
	public Vector3 normal;
	public double d;
	
	public static Square[] constructSurfaces(Point3 v1, Point3 v2, Point3 v3, Point3 v4, 
			Point3 v5, Point3 v6, Point3 v7, Point3 v8) {
		Square[] surfaces = new Square[6];
		
		//-xz
		surfaces[0].vertices[0] = v6;
		surfaces[0].vertices[1] = v7;
		surfaces[0].vertices[2] = v2;
		surfaces[0].vertices[3] = v1;
		surfaces[0].normal = new Vector3(0, -1, 0);
		
		//-xy
		surfaces[1].vertices[0] = v1;
		surfaces[1].vertices[1] = v2;
		surfaces[1].vertices[2] = v3;
		surfaces[1].vertices[3] = v4;
		surfaces[1].normal = new Vector3(0, 0, -1);
		
		//-yz
		surfaces[2].vertices[0] = v1;
		surfaces[2].vertices[1] = v4;
		surfaces[2].vertices[2] = v5;
		surfaces[2].vertices[3] = v6;
		 surfaces[2].normal = new Vector3(-1, 0, 0);
		
		//xz
		surfaces[3].vertices[0] = v4;
		surfaces[3].vertices[1] = v3;
		surfaces[3].vertices[2] = v8;
		surfaces[3].vertices[3] = v5;
		surfaces[3].normal = new Vector3(0, 1, 0);
		
		//xy
		surfaces[4].vertices[0] = v8;
		surfaces[4].vertices[1] = v7;
		surfaces[4].vertices[2] = v6;
		surfaces[4].vertices[3] = v5;
		surfaces[4].normal = new Vector3(0, 0, 1);
		
		//yz
		surfaces[5].vertices[0] = v8;
		surfaces[5].vertices[1] = v3;
		surfaces[5].vertices[2] = v2;
		surfaces[5].vertices[3] = v7;
		surfaces[5].normal = new Vector3(1, 0, 0);
	  
		for(int i = 0; i < 6; i++) {
			surfaces[i].d = - surfaces[i].normal.dot(surfaces[i].vertices[0]);
		}
		return surfaces;
	}	
}
