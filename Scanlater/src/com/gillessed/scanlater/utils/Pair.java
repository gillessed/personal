package com.gillessed.scanlater.utils;

public class Pair <S, T> {
	private S s;
	private T t;
	
	public Pair(S s, T t) {
		this.s = s;
		this.t = t;
	}
	
	public Pair() {
		this(null, null);
	}

	public S getFirst() {
		return s;
	}

	public void setFirst(S s) {
		this.s = s;
	}

	public T getSecond() {
		return t;
	}

	public void setSecond(T t) {
		this.t = t;
	}
	
	public Pair<T, S> swap() {
		return new Pair<T, S>(t, s);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Pair)) {
			return false;
		}
		
		Pair<?,?> pair = (Pair<?,?>)obj;
		
		boolean first = (s == null) ? (pair.getFirst() == null) : (s.equals(pair.getFirst()));
		boolean second = (t == null) ? (pair.getSecond() == null) : (t.equals(pair.getSecond()));
		
		return first && second;
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		if(s != null) {
			result += s.hashCode() * 31;
		}
		if(t != null) {
			result += t.hashCode() * 31;
		}
		return result;
	}
	
	@Override
	public String toString() {
		String ret = "Pair<";
		if(s == null) {
			ret += "null"; 
		} else {
			ret += s.toString();
		}
		ret += ",";
		if(t == null) {
			ret += "null"; 
		} else {
			ret += t.toString();
		}
		ret += ">";
		return ret;
	}
}
