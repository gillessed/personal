package com.gillessed.advancedmath.mathpanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;

import com.gillessed.advancedmath.field.RealNumber;
import com.gillessed.advancedmath.field.RealNumberField;
import com.gillessed.advancedmath.function.field.FieldFunction;


public class GraphPane extends MathPanel {

	private static final long serialVersionUID = 1L;
	private FieldFunction<RealNumber> function;
	private Map<String, RealNumber> parameters;

	private RealNumberField realNumberField;
	
	public GraphPane(Dimension size) {
		realNumberField = new RealNumberField();
		parameters = new HashMap<String, RealNumber>();
		setPreferredSize(size);
	}
	
	public void redraw(String newFunction) {
		function = new FieldFunction<RealNumber>(realNumberField, newFunction);
		repaint();
	}

	@Override
	public void paintPanel(Graphics inG) {
		Graphics2D g = (Graphics2D) inG;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if(function != null) {
			g.setColor(Color.red);
			double previousValue;
			parameters.clear();
			parameters.put("x", new RealNumber(convertXFromScreen(0)));
			previousValue = function.evaluate(parameters).getValue();
			for(int i = 1; i < getWidth(); i++) {
				parameters.clear();
				parameters.put("x", new RealNumber(convertXFromScreen(i)));
				double value = function.evaluate(parameters).getValue();
				g.drawLine(i,convertYForScreen(previousValue),i+1,convertYForScreen(value));
				previousValue = value;
			}
		}
	}
}
