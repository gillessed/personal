package com.gillessed.raytracer.algebra;

public class Roots {
	public static int quadraticRoots(double[] roots, double a, double b, double c) {
		double d;
		double q;

		if(a == 0) {
			if(b == 0) {
				return 0;
			} else {
				roots[0] = -c/b;
				return 1;
		    }
		} else {
			d = b * b - 4 * a * c;
		    if(d < 0) {
		    	return 0;
		    } else {
		    	q = -(b + Math.signum(b) * Math.sqrt(d)) / 2.0;
		    	roots[0] = q / a;
		    	if( q != 0 ) {
		    		roots[1] = c / q;
		    	} else {
		    		roots[1] = roots[0];
		    	}
		    	return 2;
		    }
		}
	}
}
