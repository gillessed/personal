package com.gillessed.advancedmath.test.field;

import com.gillessed.advancedmath.field.RationalNumber;
import com.gillessed.advancedmath.field.RationalNumberField;

import junit.framework.TestCase;

public class RationalNumberTest extends TestCase {
	
	private RationalNumberField rationalNumbers = new RationalNumberField();
	
	public void testPrint() {
		RationalNumber a = new RationalNumber(10,6);
		RationalNumber b = new RationalNumber(10,102);
		RationalNumber c = new RationalNumber(25,5);
		RationalNumber d = new RationalNumber(1,1);
		RationalNumber e = new RationalNumber(234,2345432);
		a.println();
		b.println();
		c.println();
		d.println();
		e.println();
		assertEquals(3,a.getPrintSize());
		assertEquals(4,b.getPrintSize());
		assertEquals(1,c.getPrintSize());
		assertEquals(1,d.getPrintSize());
		assertEquals(11,e.getPrintSize());
	}
	public void testAdd() {
		RationalNumber a = new RationalNumber(10,6);
		RationalNumber b = new RationalNumber(10,102);
		RationalNumber c = new RationalNumber(25,5);
		RationalNumber d = new RationalNumber(1,1);
		assertEquals(new RationalNumber(1080,612), rationalNumbers.add(a, b));
		assertEquals(new RationalNumber(6,1), rationalNumbers.add(c, d));
	}
}
