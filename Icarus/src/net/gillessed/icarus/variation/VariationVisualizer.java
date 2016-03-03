package net.gillessed.icarus.variation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import net.gillessed.icarus.geometry.Point;
import net.gillessed.icarus.geometry.ViewRectangle;

class Line {

	public static final Set<Point> allPoints = new HashSet<Point>();

	private Point[] points;
	public Line(int nPoints, double xStart, double yStart, double xEnd, double yEnd) {
		points = new Point[nPoints];
		Point vector = new Point((xEnd - xStart) / (nPoints - 1), (yEnd - yStart) / (nPoints - 1));
		for(int i = 0; i < nPoints; i++) {
			points[i] = new Point(xStart + vector.getX() * i, yStart + vector.getY() * i);
			allPoints.add(points[i]);
		}
	}
	public Line(int nPoints, Point start, double xEnd, double yEnd) {
		points = new Point[nPoints];
		Point vector = new Point((xEnd - start.getX()) / (nPoints - 1), (yEnd - start.getY()) / (nPoints - 1));
		for(int i = 0; i < nPoints; i++) {
			if(i == 0) {
				points[i] = start;
			} else {
				points[i] = new Point(start.getX() + vector.getX() * i, start.getY() + vector.getY() * i);
				allPoints.add(points[i]);
			}
		}
	}
	public Line(int nPoints, Point start, Point end) {
		points = new Point[nPoints];
		Point vector = new Point((end.getX() - start.getX()) / (nPoints - 1), (end.getY() - start.getY()) / (nPoints - 1));
		for(int i = 0; i < nPoints; i++) {
			if(i == 0) {
				points[i] = start;
			} else if(i == nPoints - 1) {
				points[i] = end;
			} else {
				points[i] = new Point(start.getX() + vector.getX() * i, start.getY() + vector.getY() * i);
				allPoints.add(points[i]);
			}
		}
	}
	public Point getStart() {
		return points[0];
	}
	public Point getEnd() {
		return points[points.length - 1];
	}
	public Point[] getPoints() {
		return points;
	}
}

@SuppressWarnings("serial")
public class VariationVisualizer extends JPanel {

	public static void main(String args[]) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(512,512);
		VariationVisualizer v = new VariationVisualizer();
		f.getContentPane().setLayout(new GridLayout(1,1));
		f.getContentPane().add(v);
		f.setVisible(true);
		v.start();
	}

	private final static int LINE_DENSITY = 20;
	private final static int POINT_DENSITY = 5;


	private final ViewRectangle view = new ViewRectangle(3, 1, this);
	private final Timer timer;
	private final Variation variation;

	private final int left = -2;
	private final int right = -left;
	private final int top = left;
	@SuppressWarnings("unused")
	private final int bottom = right;
	private final double lineDistance;

	public VariationVisualizer() {
		timer = new Timer(50, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				VariationVisualizer.this.repaint();
			}
		});

		variation = new Heart();

		lineDistance = (double)(right - left) / (double)LINE_DENSITY;
	}

	public void start() {
		timer.start();
	}

	private Point getPointCoord(int i, int j) {
		return new Point(left + lineDistance * i, top + lineDistance * j);
	}

	@Override
	protected void paintComponent(Graphics g2) {

		Line.allPoints.clear();

		Graphics2D g = (Graphics2D)g2;
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());

		//Generate Points
		Line[][] Xgrid = new Line[LINE_DENSITY][LINE_DENSITY + 1];
		Line[][] Ygrid = new Line[LINE_DENSITY + 1][LINE_DENSITY];
		Point startPoint = getPointCoord(0, 0);
		Point start1Point = getPointCoord(1, 0);
		Line startLine = new Line(POINT_DENSITY, startPoint.getX(), startPoint.getY(), start1Point.getX(), start1Point.getY());
		Xgrid[0][0] = startLine;
		Line lastLine = startLine;
		for(int i = 1; i < LINE_DENSITY; i++) {
			Point endPoint = getPointCoord(i + 1, 0);
			Line xLine = new Line(POINT_DENSITY, lastLine.getEnd(), endPoint.getX(), endPoint.getY());
			Xgrid[i][0] = xLine;
			lastLine = xLine;
		}
		for(int i = 0; i < LINE_DENSITY + 1; i++) {
			Point nextPoint;
			if(i < LINE_DENSITY) {
				nextPoint = Xgrid[i][0].getStart();
			} else {
				nextPoint = Xgrid[i - 1][0].getEnd();
			}
			Point endPoint = getPointCoord(i, 1);
			Line yLine = new Line(POINT_DENSITY, nextPoint, endPoint.getX(), endPoint.getY());
			Ygrid[i][0] = yLine;
			lastLine = yLine;
		}
		for(int y = 1; y < LINE_DENSITY; y++) {
			for(int x = 0; x < LINE_DENSITY; x++) {
				Line yLine1 = Ygrid[x][y - 1];
				Line yLine2 = Ygrid[x + 1][y - 1];
				Line xLine = new Line(POINT_DENSITY, yLine1.getEnd(), yLine2.getEnd());
				Xgrid[x][y] = xLine;
			}
			for(int x = 0; x < LINE_DENSITY + 1; x++) {
				Point nextPoint;
				if(x < LINE_DENSITY) {
					nextPoint = Xgrid[x][y].getStart();
				} else {
					nextPoint = Xgrid[x - 1][y].getEnd();
				}
				Point endPoint = getPointCoord(x, y + 1);
				Line yLine = new Line(POINT_DENSITY, nextPoint, endPoint.getX(), endPoint.getY());
				Ygrid[x][y] = yLine;
				lastLine = yLine;
			}
		}
		for(int x = 0; x < LINE_DENSITY; x++) {
			Line yLine1 = Ygrid[x][LINE_DENSITY - 1];
			Line yLine2 = Ygrid[x + 1][LINE_DENSITY - 1];
			Line xLine = new Line(POINT_DENSITY, yLine1.getEnd(), yLine2.getEnd());
			Xgrid[x][LINE_DENSITY] = xLine;
		}

		//Draw original
		g.setColor(Color.gray.darker());
		for(int i = 0; i < LINE_DENSITY; i++) {
			for(int j = 0; j < LINE_DENSITY + 1; j++) {
				if(Xgrid[i][j] != null) {
					drawLine(Xgrid[i][j], g);
				}
			}
		}
		for(int i = 0; i < LINE_DENSITY + 1; i++) {
			for(int j = 0; j < LINE_DENSITY; j++) {
				if(Ygrid[i][j] != null) {
					drawLine(Ygrid[i][j], g);
				}
			}
		}

		//Transform
		for(Point p : Line.allPoints) {
			Point t = variation.calculate(p.getX(), p.getY());
			p.setX(t.getX());
			p.setY(t.getY());
		}

		//Draw transformed
		g.setColor(Color.white);
		for(int i = 0; i < LINE_DENSITY; i++) {
			for(int j = 0; j < LINE_DENSITY + 1; j++) {
				if(Xgrid[i][j] != null) {
					drawLine(Xgrid[i][j], g);
				}
			}
		}
		for(int i = 0; i < LINE_DENSITY + 1; i++) {
			for(int j = 0; j < LINE_DENSITY; j++) {
				if(Ygrid[i][j] != null) {
					drawLine(Ygrid[i][j], g);
				}
			}
		}
	}

	private void drawLine(Line l, Graphics2D g) {
		for(int i = 0; i < l.getPoints().length - 1; i++) {
			Point start = l.getPoints()[i];
			Point end = l.getPoints()[i+1];
			g.drawLine(view.changeX(start.getX()),
					view.changeY(start.getY()),
					view.changeX(end.getX()),
					view.changeY(end.getY()));
		}
	}
}
