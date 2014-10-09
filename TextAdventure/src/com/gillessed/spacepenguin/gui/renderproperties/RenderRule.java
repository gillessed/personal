package com.gillessed.spacepenguin.gui.renderproperties;

import java.util.ArrayList;
import java.util.List;

import com.gillessed.spacepenguin.gui.RenderTarget.RenderState;

public class RenderRule {
	public List<String> typeNames;
	public List<String> classNames;
	public List<String> idNames;
	public RenderState state;
	
	public RenderProperties.Props property;
	public Object value;
	
	public RenderRule(RenderProperties.Props property, Object value) {
		this.value = value;
		this.property = property;
		typeNames = new ArrayList<String>();
		classNames = new ArrayList<String>();
		idNames = new ArrayList<String>();
	}
}
