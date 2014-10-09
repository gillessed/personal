package com.gillessed.scanlater;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.List;

import com.gillessed.scanlater.ui.BubbleUpdateListener;
import com.gillessed.scanlater.ui.Globals;
import com.gillessed.scanlater.ui.ScanlaterFonts;
import com.gillessed.scanlater.ui.ScanlaterImagePanel;

public class Bubble implements Serializable {
	private static final long serialVersionUID = 6174229170919337799L;
	
	private static final int HOVER_BORDER = 16;
	public static final int HOVER_NO = 0;
	public static final int HOVER_YES = 1;
	public static final int HOVER_T = 2;
	public static final int HOVER_B = 3;
	public static final int HOVER_L = 4;
	public static final int HOVER_R = 5;
	
	private Polygon clearPolygon;
	private Rectangle2D.Double textRectangle;
	private String textString;
	private int fontSize;
	private int fontStyle;
	private Color textColour;
	private Color backgroundColour;
	private Point2D.Double midPoint;
	private boolean hovered;
	
	private BubbleUpdateListener updateListener;
	
	public Bubble(List<Point2D.Double> clearPolygonPoints, int fontSize, int fontStyle, Color textColour, Color backgroundColour) {
		this.fontSize = fontSize;
		this.fontStyle = fontStyle;
		this.textColour = textColour;
		this.backgroundColour = backgroundColour;
		this.textString = "";
		this.hovered = false;
		
		midPoint = new Point2D.Double(0, 0);
		for(Point2D.Double point : clearPolygonPoints) {
			midPoint.x += point.x;
			midPoint.y += point.y;
		}
		midPoint.x /= clearPolygonPoints.size();
		midPoint.y /= clearPolygonPoints.size();
		
		double distance = 0;
		for(Point2D.Double point : clearPolygonPoints) {
			double dx = midPoint.x - point.x;
			double dy = midPoint.y - point.y;
			distance += Math.sqrt(dx * dx + dy * dy);
		}
		distance /= clearPolygonPoints.size();
		
		createPolygon(clearPolygonPoints);
		
		textRectangle = new Rectangle2D.Double(midPoint.x - distance, midPoint.y - distance, distance * 2, distance * 2);
	}
	
	private void createPolygon(List<Point2D.Double> clearPolygonPoints) {
		int x[] = new int[clearPolygonPoints.size()];
		int y[] = new int[clearPolygonPoints.size()];
		for(int i = 0; i < clearPolygonPoints.size(); i++) {
			x[i] = (int)clearPolygonPoints.get(i).x;
			y[i] = (int)clearPolygonPoints.get(i).y;
		}
		clearPolygon = new Polygon(x, y, clearPolygonPoints.size());
	}
	
	public void setFontStyle(int fontStyle) {
		this.fontStyle = fontStyle;
		if(updateListener != null) {
			updateListener.bubbleUpdated(this);
		}
	}
	
	public int getFontStyle() {
		return fontStyle;
	}
	
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
		if(updateListener != null) {
			updateListener.bubbleUpdated(this);
		}
	}
	
	public int getFontSize() {
		return fontSize;
	}

	public String getTextString() {
		return textString;
	}

	public void setTextString(String textString) {
		this.textString = textString;
		if(updateListener != null) {
			updateListener.bubbleUpdated(this);
		}
	}

	public Color getTextColour() {
		return textColour;
	}

	public void setTextColour(Color textColour) {
		this.textColour = textColour;
		if(updateListener != null) {
			updateListener.bubbleUpdated(this);
		}
	}

	public Color getBackgroundColour() {
		return backgroundColour;
	}

	public void setBackgroundColour(Color backgroundColour) {
		this.backgroundColour = backgroundColour;
		if(updateListener != null) {
			updateListener.bubbleUpdated(this);
		}
	}
	
	public void draw(Graphics2D g) {
		g.setColor(backgroundColour);
		g.fillPolygon(clearPolygon);
		
		if(hovered) {
			g.setColor(Color.blue);
		} else {
			if(this == Globals.instance().getSelectedBubble()) {
				g.setColor(Color.red);
			} else {
				g.setColor(Color.green);
			}
		}
		g.drawPolygon(clearPolygon);

		
		if(hovered) {
			g.setColor(new Color(0, 0, 200));
		} else {
			if(this == Globals.instance().getSelectedBubble()) {
				g.setColor(new Color(200, 0, 0));
			} else {
				g.setColor(new Color(0, 200, 0));
			}
		}
		Stroke temp = g.getStroke();
		g.setStroke(Globals.DASHED_STROKE);
		g.drawRect((int)textRectangle.x, (int)textRectangle.y, (int)textRectangle.width, (int)textRectangle.height);
		g.setStroke(temp);
		
		g.setColor(textColour);
		g.setFont(ScanlaterFonts.getFont(fontSize, fontStyle));
		g.drawString(textString, (int)textRectangle.x + 5, (int)textRectangle.y + 15);
	}

	public BubbleUpdateListener getUpdateListener() {
		return updateListener;
	}

	public void setUpdateListener(BubbleUpdateListener updateListener) {
		this.updateListener = updateListener;
	}

	public boolean isVisible(ScanlaterImagePanel imagePanel, Point displacement, double scale, int dx) {
		boolean visible = true;
		int npoints = clearPolygon.npoints;
		for(int i = 0; i < npoints; i++) {
			double x = clearPolygon.xpoints[i];
			double y = clearPolygon.ypoints[i];
			x += dx;
			x *= scale;
			y *= scale;
			x += displacement.x;
			y += displacement.y;
			if(x < 0 || x > imagePanel.getWidth() || y < 0 || y > imagePanel.getHeight()) {
				visible = false;
				break;
			}
		}
		return visible;
	}

	public Point2D.Double getMidPoint() {
		return new Point2D.Double(midPoint.x, midPoint.y);
	}
	
	public int containsPoint(double x, double y) {
		double top = textRectangle.y;
		double bottom = textRectangle.y + textRectangle.height;
		double left = textRectangle.x;
		double right = textRectangle.x + textRectangle.width;
		if(x > left - HOVER_BORDER && x < left + HOVER_BORDER) {
			if(y > top - HOVER_BORDER && y < bottom + HOVER_BORDER) {
				return HOVER_L;
			}
		} else if(x > right - HOVER_BORDER && x < right + HOVER_BORDER) {
			if(y > top - HOVER_BORDER && y < bottom + HOVER_BORDER) {
				return HOVER_R;
			}
		} else if(x > left && x < right) {
			if(y > top - HOVER_BORDER && y < top + HOVER_BORDER) {
				return HOVER_T;
			} else if(y > bottom - HOVER_BORDER && y < bottom + HOVER_BORDER) {
				return HOVER_B;
			} else if(y > top && y < bottom) {
				return HOVER_YES;
			}
		}
		
		return HOVER_NO;
	}

	public boolean isHovered() {
		return hovered;
	}

	public void setHovered(boolean hovered) {
		this.hovered = hovered;
	}
}
