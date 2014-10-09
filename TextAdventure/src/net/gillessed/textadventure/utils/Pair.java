package net.gillessed.textadventure.utils;

import java.io.Serializable;

public class Pair<S, T> implements Serializable {
	private static final long serialVersionUID = -2672053366992565494L;
	
	private S first;
	private T second;
	public Pair() {
		first = null;
		second = null;
	}
	public Pair(S first, T second) {
		this.first = first;
		this.second = second;
	}
	public S getFirst() {
		return first;
	}
	public void setFirst(S first) {
		this.first = first;
	}
	public T getSecond() {
		return second;
	}
	public void setSecond(T second) {
		this.second = second;
	}
}
