package com.gillessed.advancedmath.mathpanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;

import com.gillessed.advancedmath.field.RationalNumber;
import com.gillessed.advancedmath.field.RationalNumberField;
import com.gillessed.advancedmath.function.field.FieldFunction;


public class NaturalNumberGraphPane extends MathPanel {
	private static final long serialVersionUID = 1L;
	
	private FieldFunction<RationalNumber> function;
	@SuppressWarnings("unused")
	private Map<String, RationalNumber> parameters;
	private RationalNumberField rationalNumberField;
	private final int limit;
	
	public NaturalNumberGraphPane(Dimension size, int limit) {
		this.limit = limit;
		rationalNumberField = new RationalNumberField();
		parameters = new HashMap<String, RationalNumber>();
		setPreferredSize(size);
	}
	
	public void redraw(String newFunction) {
		function = new FieldFunction<RationalNumber>(rationalNumberField, newFunction);
		repaint();
	}

	@Override
	protected void paintPanel(Graphics inG) {
		Graphics2D g = (Graphics2D) inG;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if(function != null) {
			g.setColor(Color.red);
			
			for(int i = 1; i < limit; i++) {
				
			}
			
//			double previousValue;
//			parameters.clear();
//			parameters.put("x", new RealNumber(convertXFromScreen(0)));
//			previousValue = function.evaluate(parameters).getValue();
//			for(int i = 1; i < getWidth(); i++) {
//				parameters.clear();
//				parameters.put("x", new RealNumber(convertXFromScreen(i)));
//				double value = function.evaluate(parameters).getValue();
//				g.drawLine(i,convertYForScreen(previousValue),i+1,convertYForScreen(value));
//				previousValue = value;
//			}
		}
	}
	
}
