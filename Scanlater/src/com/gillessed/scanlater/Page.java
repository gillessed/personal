package com.gillessed.scanlater;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gillessed.scanlater.ui.Globals;
import com.gillessed.scanlater.ui.ScanlaterFonts;
import com.gillessed.scanlater.utils.Pair;

public class Page implements Serializable {
	private static final long serialVersionUID = 6258686103790155871L;

	private transient boolean loadedImage;
	private transient BufferedImage pageImage;
	private int width;
	private int height;
	private final List<Bubble> bubbles;
	private final String filename;
	
	public Page(String filename) {
		this.filename = filename;
		width = 500;
		height = 800;
		bubbles = new ArrayList<>();
		loadedImage = false;
	}
	
	public synchronized void loadImage(BufferedImage pageImage) {
		this.pageImage = pageImage;
		this.width = pageImage.getWidth();
		this.height = pageImage.getHeight();
		loadedImage = true;
	}

	public synchronized void draw(Graphics2D g, Dimension viewerDimensions) {
		if(loadedImage) {
			AffineTransform transform = g.getTransform();
			Point2D topLeft = transform.transform(new Point2D.Double(0, 0), null);
			Point2D bottomRight = transform.transform(new Point2D.Double(width, height), null);
			if (topLeft.getX() < viewerDimensions.width && bottomRight.getX() > 0 &&
				    topLeft.getY() < viewerDimensions.height && bottomRight.getY() > 0) {
				g.drawImage(pageImage, 0, 0, null);
			}
			
			for(Bubble bubble : bubbles) {
				bubble.draw(g);
			}
		} else {
			g.setColor(Color.white);
			g.setFont(ScanlaterFonts.normalVeryLarge);
			FontMetrics fm = g.getFontMetrics();
			String message = "Loading...";
			int strWidth = fm.stringWidth(message);
			g.drawString(message, (width - strWidth) / 2,  height / 2);
			g.drawRect(0, 0, width, height);
		}
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getFilename() {
		return filename;
	}

	public boolean contains(double px, double py) {
		return (px > 0 && px < width && py > 0 && py < height);
	}
	
	public void addBubble(Bubble bubble) {
		bubbles.add(bubble);
	}
	
	public void removeBubble(Bubble bubble) {
		bubbles.remove(bubble);
	}
	
	public Bubble newBubble(List<Point> points, Point displacement, int dx, double scale) {
		List<Point2D.Double> transformedPoints = new ArrayList<>();
		for(Point point : points) {
			Point2D.Double tr = new Point2D.Double((point.x - displacement.x) / scale - dx, (point.y - displacement.y) / scale);
			transformedPoints.add(tr);
		}
		
		double r = 0;
		double g = 0;
		double b = 0;
		for(Point2D.Double point : transformedPoints) {
			Color colour = new Color(pageImage.getRGB((int)point.x, (int)point.y));
			r += colour.getRed();
			g += colour.getGreen();
			b += colour.getBlue();
		}
		
		r /= transformedPoints.size();
		g /= transformedPoints.size();
		b /= transformedPoints.size();
		
		Color backgroundColor = new Color((int)r, (int)g, (int)b);
		Color textColor;
		double mean = r + g + b;
		if(mean < 127) {
			textColor = Color.white;
		} else {
			textColor = Color.black;
		}
		
		Bubble bubble = new Bubble(transformedPoints, Globals.instance().selectedFontSize, Globals.instance().selectedFontStyle, textColor, backgroundColor);
		bubbles.add(bubble);
		return bubble;
	}

	public boolean containsBubble(Bubble bubble) {
		return bubbles.contains(bubble);
	}
	
	public Pair<Bubble, Integer> bubbleContainsMouse(int mx, int my, Point displacement, double scale, int dx) {
		double x = (mx - displacement.x) / scale - dx;
		double y = (my - displacement.y) / scale;
		Bubble result = null;
		Integer hover = Bubble.HOVER_NO;
		for(Bubble bubble : bubbles) {
			hover = bubble.containsPoint(x, y);
			if(hover != Bubble.HOVER_NO) {
				result = bubble;
				break;
			}
		}
		return new Pair<Bubble, Integer>(result, hover);
	}
}
