package com.gillessed.spacepenguin.gui.renderproperties;

import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.gillessed.spacepenguin.gui.RenderTarget.RenderState;
import com.gillessed.spacepenguin.gui.renderproperties.SPPositioning.Layout;

public class RenderProperties {
	
	public enum Props {
		BACKGROUND_COLOR(SPColor.class),
		FOREGROUND_COLOR(SPColor.class),
		POSITIONING(SPPositioning.class),
		DIMENSION(SPDimension.class),
		FONT(SPFont.class),
		MARGIN(SPCardinal.class),
		PADDING(SPCardinal.class),
		BORDER(SPBorder.class);
		
		private Class<?> clazz;
		Props(Class<?> clazz) {
			this.clazz = clazz;
		}
		
		public Object parseValue(String[] str) {
			try {
				Constructor<?> constructor = clazz.getConstructor(String[].class);
				return constructor.newInstance((Object)str);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
			} catch (SecurityException e) {
				throw new RuntimeException(e);
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private Map<RenderState,Map<Props, Object>> propertyMap = new HashMap<RenderState, Map<RenderProperties.Props, Object>>();
	private static Map<Props, Object> defaultMap = new HashMap<RenderProperties.Props, Object>();
	static {
		defaultMap.put(Props.BACKGROUND_COLOR, new Color(0,0,0));
		defaultMap.put(Props.FOREGROUND_COLOR, new Color(200,200,200));
		defaultMap.put(Props.POSITIONING, new SPPositioning(Layout.FLOW, 0, 0));
		defaultMap.put(Props.DIMENSION, null);
		defaultMap.put(Props.FONT, new Font("helvetica", Font.PLAIN, 16));
		defaultMap.put(Props.MARGIN, new SPCardinal(0,0,0,0));
		defaultMap.put(Props.PADDING, new SPCardinal(0,0,0,0));
		defaultMap.put(Props.BORDER, new SPBorder(0, "SOLID", new SPColor(0, 0, 0)));
		for(Props p : Props.values()) {
			if(!defaultMap.containsKey(p)) {
				throw new RuntimeException("No default value defined for property " + p.toString());
			}
		}
	}
	
	public RenderProperties() {
		for(RenderState s : RenderState.values()) {
			propertyMap.put(s, new HashMap<RenderProperties.Props, Object>());
		}
	}
	
	private Object get(Props prop, RenderState state) {
		if(propertyMap.get(RenderState.ALL).containsKey(prop))
			return propertyMap.get(RenderState.ALL).get(prop);
		
		if(propertyMap.get(state).containsKey(prop))
			return propertyMap.get(state).get(prop);
		
		if(propertyMap.get(RenderState.NONE).containsKey(prop))
			return propertyMap.get(RenderState.NONE).get(prop);
		
		return defaultMap.get(prop);
	}
	
	public void set(Props prop, Object value, RenderState state) {
		propertyMap.get(state).put(prop, value);
	}
	
	public void remove(Props prop, RenderState state) {
		propertyMap.get(state).remove(prop);
	}
	
	public Color getBackgroundColor(RenderState state) {
		return (Color)get(Props.BACKGROUND_COLOR, state);
	}
	
	public Color getForegroundColor(RenderState state) {
		return (Color)get(Props.FOREGROUND_COLOR, state);
	}
	
	public SPPositioning getPositioning(RenderState state) {
		return (SPPositioning)get(Props.POSITIONING, state);
	}
	
	public SPDimension getDimensions(RenderState state) {
		return (SPDimension)get(Props.DIMENSION, state);
	}
	
	public Font getFont(RenderState state) {
		return (Font)get(Props.FONT, state);
	}
	
	public SPCardinal getMargin(RenderState state) {
		return (SPCardinal)get(Props.MARGIN, state);
	}
	
	public SPCardinal getPadding(RenderState state) {
		return (SPCardinal)get(Props.PADDING, state);
	}
	
	public SPBorder getBorder(RenderState state) {
		return (SPBorder)get(Props.BORDER, state);
	}
}
