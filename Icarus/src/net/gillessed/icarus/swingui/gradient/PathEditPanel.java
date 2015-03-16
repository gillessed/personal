package net.gillessed.icarus.swingui.gradient;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.Timer;

import net.gillessed.icarus.geometry.Point;
import net.gillessed.icarus.geometry.TransformPath;
import net.gillessed.icarus.geometry.Triangle;
import net.gillessed.icarus.swingui.FlameModelContainer;
import net.gillessed.icarus.swingui.transform.TransformShowPanel;

@SuppressWarnings("serial")
public class PathEditPanel extends TransformShowPanel {
	
	private final Object lock = new Object();
	
	private Point vertexSelectedDragging;
	private boolean mouseIn;
	private String currentAction;
	private int mx;
	private int my;
	
	private TransformPath currentPath;
	
	private final Set<Point> hasPath;
	
	private final MouseListener mouseListener = new MouseListener() {
		@Override
		public void mouseReleased(MouseEvent e) {
			vertexSelectedDragging = null;
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			if(e.getButton() == MouseEvent.BUTTON1 && currentPath == null) {
				for(TransformPath tp : transformPaths) {
					Point p = tp.checkMouse(e.getX(), e.getY(), PathEditPanel.this);
					if(p != null) {
						vertexSelectedDragging = p;
						break;
					}
				}
			}
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			mouseIn = false;
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			mouseIn = true;
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			synchronized(lock) {
				if(e.getClickCount() > 1) {
					if(e.getButton() == MouseEvent.BUTTON1 && currentPath != null) {
						transformPaths.add(currentPath);
						currentPath = null;
					} else if(e.getButton() == MouseEvent.BUTTON3 && currentPath == null) {
						for(TransformPath tp : transformPaths) {
							Point p = tp.checkMouse(e.getX(), e.getY(), PathEditPanel.this);
							if(p != null) {
								tp.getPoints().remove(p);
								if(tp.getPoints().isEmpty()) {
									transformPaths.remove(tp);
									hasPath.remove(tp.getTrianglePoint());
								}
								break;
							}
							 p = tp.checkMouseOnTPoint(e.getX(), e.getY(), PathEditPanel.this);
							if(p != null) {
								transformPaths.remove(tp);
								hasPath.remove(tp.getTrianglePoint());
								break;
							}
						}
					}
				} else if(e.getClickCount() == 1 && currentPath == null) {
					if(e.getButton() == MouseEvent.BUTTON1) {
						for(Triangle tr : triangles) {
							int ret = tr.checkMouse(e.getX(), e.getY());
							if(ret > 0 && !hasPath.contains(tr.getVertex(ret))) {
								hasPath.add(tr.getVertex(ret));
								currentPath = new TransformPath(tr.getVertex(ret), triangles.indexOf(tr), ret);
								break;
							}
						}
					}
				} else if(e.getClickCount() == 1 && currentPath != null) {
					if(e.getButton() == MouseEvent.BUTTON1) {
						double px = viewRectangle.reverseX(e.getX());
						double py = viewRectangle.reverseY(e.getY());
						currentPath.getPoints().add(new Point(px, py));
					}
				}
			}
		}
	};
	
	private final MouseMotionListener mouseMotionListener = new MouseMotionListener() {
		@Override
		public void mouseMoved(MouseEvent e) {
			mx = e.getX();
			my = e.getY();
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			if(vertexSelectedDragging != null) {
				vertexSelectedDragging.setX(reverseX(e.getX()));
				vertexSelectedDragging.setY(reverseY(e.getY()));
			}
		}
	};

	private final List<TransformPath> transformPaths;
	private final List<Triangle> triangleBuffer;
	
	public PathEditPanel(FlameModelContainer flameContainerModel, List<TransformPath> transformPaths) {
		super(flameContainerModel, false);
		this.transformPaths = transformPaths;
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseMotionListener);
		hasPath = new HashSet<Point>();
		for(TransformPath tp : transformPaths) {
			hasPath.add(tp.getTrianglePoint());
		}
		currentAction = "Select a vertex";
		vertexSelectedDragging = null;
		mouseIn = false;
		final Timer timer = new Timer(30, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		timer.start();
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent e) {
				timer.stop();
			}
		});
		triangleBuffer = new ArrayList<>();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.white);
		g.drawString(currentAction,10,20);
		synchronized(lock) {
			for(TransformPath tp : transformPaths) {
				drawPath(tp, (Graphics2D)g, false);
			}
			if(currentPath != null) {
				drawPath(currentPath, (Graphics2D)g, true);
			}
		}
		synchronized(triangleBuffer) {
			for(Triangle tr : triangleBuffer) {
				tr.draw((Graphics2D)g, false);
			}
		}
	}
	
	private void drawPath(TransformPath tp, Graphics2D g, boolean isCurrentPath) {
		g.setColor(Color.white);
		int px = changeX(tp.getTrianglePoint().getX());
		int py = changeY(tp.getTrianglePoint().getY());
		g.drawOval(px - tp.getRad(), py - tp.getRad(), tp.getRad() * 2, tp.getRad() * 2);
		if(!isCurrentPath || tp.getPoints().size() >= 1) {
			g.drawLine(px, py,
					changeX(tp.getPoints().get(0).getX()),
					changeY(tp.getPoints().get(0).getY()));
			g.drawOval(changeX(tp.getPoints().get(0).getX()) - tp.getRad(),
					changeY(tp.getPoints().get(0).getY()) - tp.getRad(), tp.getRad() * 2, tp.getRad() * 2);
		}
		for(int i = 0; i < tp.getPoints().size() - 1; i++) {
			int px1 = changeX(tp.getPoints().get(i).getX());
			int py1 = changeY(tp.getPoints().get(i).getY());
			int px2 = changeX(tp.getPoints().get(i + 1).getX());
			int py2 = changeY(tp.getPoints().get(i + 1).getY());
			g.drawOval(px2 - tp.getRad(), py2 - tp.getRad(), tp.getRad() * 2, tp.getRad() * 2);
			g.drawLine(px1, py1, px2, py2);
		}
		if(!isCurrentPath) {
			int px1 = changeX(tp.getPoints().get(tp.getPoints().size() - 1).getX());
			int py1 = changeY(tp.getPoints().get(tp.getPoints().size() - 1).getY());
			g.drawLine(px1, py1, px, py);
		} else if(mouseIn) {
			int px1, py1;
			if(tp.getPoints().size() > 0) {
				px1 = changeX(tp.getPoints().get(tp.getPoints().size() - 1).getX());
				py1 = changeY(tp.getPoints().get(tp.getPoints().size() - 1).getY());
			} else {
				px1 = px; py1 = py;
			}
			g.drawLine(px1, py1, mx, my);
			g.drawOval(mx - tp.getRad(), my - tp.getRad(), tp.getRad() * 2, tp.getRad() * 2);
		}
 	}

	public TransformPath getCurrentPath() {
		return currentPath;
	}

	public void setCurrentPath(TransformPath currentPath) {
		this.currentPath = currentPath;
	}
	
	public void addTriangleToBuffer(Triangle t) {
		synchronized(triangleBuffer) {
			triangleBuffer.add(t);
		}
	}
	
	public void clearTriangleBuffer() {
		synchronized(triangleBuffer) {
			triangleBuffer.clear();
		}
	}
}
