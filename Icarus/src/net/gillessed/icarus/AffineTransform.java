package net.gillessed.icarus;

public class AffineTransform {
	
	/**
	 * This class holds an affine transform that is used by the fractal algorithm.
	 * Affine transforms take the form
	 * 		x[i+1] = x[i]*a + y[i]*b + c;
	 * 		y[i+1] = x[i]*d + y[i]*e + f
	 * where a,b,c,d,e,f are constants.
	 */
	private double a, b, c, d, e, f;
	public AffineTransform() {
		a = 1;
		b = 0;
		c = 0;
		d = 0;
		e = 1;
		f = 0;
	}
	public void setA(double a) {
		this.a = a;
	}
	public double getA() {
		return a;
	}
	public void setB(double b) {
		this.b = b;
	}
	public double getB() {
		return b;
	}
	public void setC(double c) {
		this.c = c;
	}
	public double getC() {
		return c;
	}
	public void setD(double d) {
		this.d = d;
	}
	public double getD() {
		return d;
	}
	public void setE(double e) {
		this.e = e;
	}
	public double getE() {
		return e;
	}
	public void setF(double f) {
		this.f = f;
	}
	public double getF() {
		return f;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("x = x*");
		sb.append(a);
		sb.append("+ y[i]*");
		sb.append(b);
		sb.append("+ ");
		sb.append(c);
		sb.append(";y = x*");
		sb.append(d);
		sb.append("+ y*");
		sb.append(e);
		sb.append("+ ");
		sb.append(f);
		return sb.toString();
	}
	public AffineTransform copyAffineTransform() {
		AffineTransform aff = new AffineTransform();
		aff.setA(a);
		aff.setB(b);
		aff.setC(c);
		aff.setD(d);
		aff.setE(e);
		aff.setF(f);
		return aff;
	}
}
