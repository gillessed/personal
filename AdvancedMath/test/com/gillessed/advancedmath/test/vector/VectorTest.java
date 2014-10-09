package com.gillessed.advancedmath.test.vector;

import junit.framework.TestCase;

import org.junit.Test;

import com.gillessed.advancedmath.vector.Vector;

public class VectorTest extends TestCase {
	
	@Test
	public void test() {
		Vector v1 = new Vector(3, 1.0, 2.0, 3.0);
		Vector v2 = new Vector(3, 2.0, 4.0, -2.0);
		assertEquals(4.0, v1.dot(v2));
		assertEquals(new Vector(3, 3.0, 6.0, 1.0 ), v1.add(v2));
	}
}
