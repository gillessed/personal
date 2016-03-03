package net.gillessed.icarus.swingui.transform;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.Function;
import net.gillessed.icarus.event.FlameModificationListener;
import net.gillessed.icarus.event.NewFlameListener;
import net.gillessed.icarus.event.FunctionEvent;
import net.gillessed.icarus.event.FunctionListener;
import net.gillessed.icarus.geometry.Triangle;
import net.gillessed.icarus.geometry.ViewRectangle;
import net.gillessed.icarus.swingui.FlameModelContainer;

public class TransformShowPanel extends JPanel {
	private static final long serialVersionUID = -3233829205235124908L;

	public static Color[] colors = new Color[12];
	static{
		colors[0] = Color.red;
		colors[1] = Color.blue;
		colors[2] = Color.green;
		colors[3] = Color.yellow;
		colors[4] = Color.magenta;
		colors[5] = Color.cyan;
		colors[6] = Color.red.darker();
		colors[7] = Color.blue.darker();
		colors[8] = Color.green.darker();
		colors[9] = Color.yellow.darker();
		colors[10] = Color.magenta.darker();
		colors[11] = Color.cyan.darker();
	}

	private final NewFlameListener flameChangeListener = new NewFlameListener() {
		@Override
		public void newFlame(FlameModel flameModel) {
			update();
			repaint();
			flameModelContainer.getFlameModel().addFunctionListener(new FunctionListener() {

				@Override
				public void functionRemoved(FunctionEvent e) {
					update();
					repaint();
				}

				@Override
				public void functionAdded(FunctionEvent e) {
					update();
					repaint();
				}
			});
		}
	};

	private final FlameModificationListener flameModificationListener = new FlameModificationListener() {
        @Override
        public void flameModified() {
            update();
            repaint();
        }
    };

	protected final FlameModelContainer flameModelContainer;
	protected final ViewRectangle viewRectangle = new ViewRectangle(2, 1.0, this);
	protected final boolean drawTriangleData;
	protected final Triangle baseTriangle;
	protected final List<Triangle> triangles;
	protected int lastTouched;
	protected int triangleDragged;
	protected int moveState;

	public TransformShowPanel(FlameModelContainer model, boolean drawTriangleData) {
		this.flameModelContainer = model;
		this.flameModelContainer.addNewFlameListener(flameChangeListener);
		this.flameModelContainer.addFlameModificationListener(flameModificationListener);
		this.drawTriangleData = drawTriangleData;
		triangleDragged = -1;
		baseTriangle = new Triangle(null, this);
		triangles = new ArrayList<>();
	}

	private final void update() {
		int i = 0;
		triangles.clear();
		for(Function v : flameModelContainer.getFlameModel().getFunctions()) {
			Triangle t = new Triangle(v.getAffineTransform(), TransformShowPanel.this);
			t.setColor(colors[i]);
			i++;
			triangles.add(t);
		}
		lastTouched = 0;
		triangles.get(0).setLastTouched(true);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.black);
		g2.fillRect(0, 0, getWidth(), getHeight());

		g2.setFont(new Font("Arial",Font.PLAIN,16));

		baseTriangle.draw(g2, false);

		Collections.reverse(triangles);
		for(Triangle t : triangles) {
			t.draw(g2, drawTriangleData);
		}
		Collections.reverse(triangles);
	}

	public ViewRectangle getViewRectangle() {
		return viewRectangle;
	}

	public int changeX(double x) {
		return viewRectangle.changeX(x);
	}

	public int changeY(double y) {
		return viewRectangle.changeY(y);
	}

	public double reverseX(int x) {
		return viewRectangle.reverseX(x);
	}

	public double reverseY(int y) {

		return viewRectangle.reverseY(y);
	}

	public void randomizeTriangles() {
		Random r = new Random();
		for(Triangle t : triangles) {
			t.setA(r.nextInt(getWidth()), r.nextInt(getHeight()));
			t.setB(r.nextInt(getWidth()), r.nextInt(getHeight()));
			t.setC(r.nextInt(getWidth()), r.nextInt(getHeight()));
		}
	}

	public void normallyRandomizeTriangles() {
		Random r = new Random();
		int ax, ay, bx, by, cx, cy;
		double ab, bc, ca;
		double area;
		for(Triangle t : triangles) {
			do {
				ax = r.nextInt(getWidth());
				ay = r.nextInt(getHeight());
				bx = r.nextInt(getWidth());
				by = r.nextInt(getHeight());
				cx = r.nextInt(getWidth());
				cy = r.nextInt(getHeight());
				area = area(ax, ay, bx, by, cx, cy);
				ab = dist(ax, ay, bx, by);
				bc = dist(bx, by, cx, cy);
				ca = dist(cx, cy, ax, ay);
			} while((area < 800 || area > 7000) || ab > 200 || bc > 200 || ca > 200);
			t.setA(ax, ay);
			t.setB(bx, by);
			t.setC(cx, cy);
		}
	}

	public double area(double ax, double ay, double bx, double by, double cx, double cy) {
		double ab = dist(ax, ay, bx, by);
		double bc = dist(bx, by, cx, cy);
		double ca = dist(cx, cy, ax, ay);
		double s = (ab + bc + ca) / 2;
		return Math.sqrt(s * (s - ab) * (s - bc) * (s - ca));
	}

	public double dist(double x1, double y1, double x2, double y2) {
		double dx = x1 - x2;
		double dy = y1 - y2;
		return Math.sqrt(dx*dx + dy*dy);
	}

	public List<Triangle> getTriangles() {
		return triangles;
	}
}
