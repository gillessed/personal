package com.gillessed.spacepenguin.gui;

public class Pair<A, B> {
	private A a;
	private B b;
	public Pair() {
		a = null;
		b = null;
	}
	
	public Pair(A a, B b) {
		this.a = a;
		this.b = b;
	}
	
	public A getFirst() {
		return a;
	}
	
	public void setFirst(A a) {
		this.a = a;
	}
	
	public B getSecond() {
		return b;
	}
	
	public void setSecond(B b) {
		this.b = b;
	}
	
	public Pair<B, A> flip() {
		return new Pair<B, A>(getSecond(), getFirst());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Pair)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		Pair<Object, Object> o = (Pair<Object, Object>)obj;
		return a.equals(o.getFirst()) && b.equals(o.getSecond());
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result += a.hashCode() * 31;
		result += b.hashCode() * 31;
		return result;
	}
}
