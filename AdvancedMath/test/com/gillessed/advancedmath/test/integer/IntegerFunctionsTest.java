package com.gillessed.advancedmath.test.integer;

import com.gillessed.advancedmath.integer.IntegerFunctions;

import junit.framework.TestCase;

public class IntegerFunctionsTest extends TestCase {
	public void testGcd() {
		assertEquals(IntegerFunctions.gcd(1,1), 1);
		assertEquals(IntegerFunctions.gcd(0,1), 0);
		assertEquals(IntegerFunctions.gcd(1,0), 0);
		assertEquals(IntegerFunctions.gcd(1,32), 1);
		assertEquals(IntegerFunctions.gcd(2134,1), 1);
		assertEquals(IntegerFunctions.gcd(40,40), 40);
		assertEquals(IntegerFunctions.gcd(6,2), 2);
		assertEquals(IntegerFunctions.gcd(3,2), 1);
		assertEquals(IntegerFunctions.gcd(9,15), 3);
		assertEquals(IntegerFunctions.gcd(63,99), 9);
		assertEquals(IntegerFunctions.gcd(121,1100), 11);
		assertEquals(IntegerFunctions.gcd(22,4), 2);
		assertEquals(IntegerFunctions.gcd(-45,-55), 5);
	}
}
