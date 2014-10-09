package net.gillessed.icarus.event;

import net.gillessed.icarus.Function;

public class FunctionEvent {
	private final Object source;
	private final Function child;

	public FunctionEvent(Object source, Function child) {
		this.source = source;
		this.child = child;
	}

	public Object getSource() {
		return source;
	}

	public Function getChild() {
		return child;
	}
}
