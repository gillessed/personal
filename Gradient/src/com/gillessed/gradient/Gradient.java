package com.gillessed.gradient;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This is the data class for a gradient. A gradient here
 * is a group of linear functions along the color line, giving
 * a list of colors which is continuous.
 * 
 * @author Gregory Cole
 */

public class Gradient implements Serializable {
	private static final long serialVersionUID = 7339395943786910762L;
	public static final int DEFAULT_SIZE = 512;
	private final int size;
	private Color[] colorPoints;
	private SortedMap<Integer, Color> colorMap;
	private final String name;
	
	/**
	 * Kept to serialize the class
	 */
	private Color startAnchor;
	private Color endAnchor;
	private SortedMap<Integer, Color> middleAnchors;
	
	public Gradient(Color startAndEnd, SortedMap<Integer, Color> middleAnchors, int size, String name) {
		this(startAndEnd, startAndEnd, middleAnchors, size, name);
	}
	public Gradient(Color startAnchor, Color endAnchor, SortedMap<Integer, Color> middleAnchors, int size, String name) {
		this.startAnchor = startAnchor;
		this.endAnchor = endAnchor;
		this.middleAnchors = middleAnchors;
		this.size = size;
		this.name = name;
		recalculateGradient();
	}
	public void recalculateGradient() {
		colorMap = new TreeMap<Integer, Color>();
		colorMap.put(0, startAnchor);
		colorMap.putAll(middleAnchors);
		colorMap.put(size - 1, endAnchor);
		colorPoints = new Color[size];
		int currentPoint;
		int nextPoint;
		List<Integer> anchorPoints = new ArrayList<Integer>();
		List<Color> anchors = new ArrayList<Color>();
		for(Entry<Integer, Color> entry : colorMap.entrySet()) {
			anchorPoints.add(entry.getKey());
			anchors.add(entry.getValue());
		}
		for(int i = 0; i < colorMap.size() - 1; i++) {
			Color currentColor = anchors.get(i);
			Color nextColor = anchors.get(i+1);
			currentPoint = anchorPoints.get(i);
			nextPoint = anchorPoints.get(i+1);
			for(int j = currentPoint; j < nextPoint; j++) {
				double percentage = (double)(j - currentPoint) / (double)(nextPoint - currentPoint);
				
				int red = currentColor.getRed() + (int)((double)(nextColor.getRed() - currentColor.getRed()) * percentage);
				int green = currentColor.getGreen() + (int)((double)(nextColor.getGreen() - currentColor.getGreen()) * percentage);
				int blue = currentColor.getBlue() + (int)((double)(nextColor.getBlue() - currentColor.getBlue()) * percentage);
				colorPoints[j] = new Color(red, green, blue);
			}
			colorPoints[nextPoint] = nextColor;
		}
	}
	public void printToDataStream(ObjectOutputStream oos) throws IOException {
		oos.writeObject(startAnchor);
		oos.writeObject(endAnchor);
		oos.writeInt(getMiddleAnchors().size());
		for(Entry<Integer, Color> entry : colorMap.entrySet()) {
			oos.writeInt(entry.getKey());
			oos.writeObject(entry.getValue());
		}
		oos.writeInt(size);
		oos.writeObject(name);
	}
	public Color getColor(int point) {
		return colorPoints[point];
	}
	public int getSize() {
		return size;
	}
	public SortedMap<Integer, Color> getColorMap() {
		return colorMap;
	}
	public String getName() {
		return name;
	}
	@Override
	public String toString() {
		return name;
	}
	public SortedMap<Integer, Color> getMiddleAnchors() {
		return middleAnchors;
	}
	public void copyGradient(Gradient g) {
		newGradient(g.getStartAnchor(), g.getEndAnchor(), g.getMiddleAnchors());
	}
	public void newGradient(Color startAnchor, Color endAnchor, SortedMap<Integer, Color> middleAnchors) {
		this.startAnchor = startAnchor;
		this.endAnchor = endAnchor;
		this.middleAnchors = middleAnchors;
		recalculateGradient();
	}
	public Color getStartAnchor() {
		return startAnchor;
	}
	public Color getEndAnchor() {
		return endAnchor;
	}
}
