package com.gillessed.scanlater.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import javax.swing.JPanel;

import com.gillessed.scanlater.Bubble;
import com.gillessed.scanlater.Project;
import com.gillessed.scanlater.ui.undo.CreateBubbleAction;
import com.gillessed.scanlater.utils.Pair;

public class ScanlaterImagePanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
	
	private static final long serialVersionUID = -4420482999191926288L;

	private ImagePanelRepaintTimer timer;
	
	private Project project;
	private Point displacement;
	private double scale;
	
	public ScanlaterImagePanel(Project project, Point displacement, double scale) {
		this.project = project;
		Globals.instance().setImagePanel(this);
		timer = new ImagePanelRepaintTimer(this);
		timer.start();
		setPreferredSize(new Dimension(500, 800));
		this.displacement = displacement;
		this.scale = scale;
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
	}
	
	@Override
	protected void paintComponent(Graphics g1) {
		Graphics2D g = (Graphics2D)g1;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(new Color(20, 20, 20));
		g.fillRect(0, 0, getWidth(), getHeight());
		if(project != null) {
			AffineTransform transform = g.getTransform();
			g.translate(displacement.x, displacement.y);
			g.scale(scale, scale);
			project.draw(g, getSize());
			g.setTransform(transform);
		}
		if(Globals.instance().mouseMode() == Globals.Mode.NEW_BUBBLE) {
			Globals.instance().getSelector().draw(g);
		}
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(project != null) {
			if(Globals.instance().mouseMode() == Globals.Mode.DRAG) {
				Point2D.Double m = Globals.instance().getMousePoint();
				displacement.x += e.getX() - m.x;
				displacement.y += e.getY() - m.y;
				Globals.instance().setMousePoint(e.getX(), e.getY());
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Globals.instance().setMousePoint(e.getX(), e.getY());
		if(Globals.instance().mouseMode() == Globals.Mode.NEW_BUBBLE) {
			Globals.instance().getSelector().mouseMoved(e.getX(), e.getY());
		} else if(Globals.instance().mouseMode() == Globals.Mode.STANDBY ||
				Globals.instance().isHovering()) {
			Pair<Bubble, Integer> result = Globals.instance().getProject().bubbleContainsMouse(e.getX(), e.getY(), displacement, scale);
			if(result.getSecond() != Bubble.HOVER_NO) {
				Globals.instance().newHover(result.getFirst());
				switch(result.getSecond()) {
				case Bubble.HOVER_YES: Globals.instance().setMode(Globals.Mode.HOVER, this); break;
				case Bubble.HOVER_T: Globals.instance().setMode(Globals.Mode.HOVER_T, this); break;
				case Bubble.HOVER_B: Globals.instance().setMode(Globals.Mode.HOVER_B, this); break;
				case Bubble.HOVER_L: Globals.instance().setMode(Globals.Mode.HOVER_L, this); break;
				case Bubble.HOVER_R: Globals.instance().setMode(Globals.Mode.HOVER_R, this); break;
				}
			} else if(result.getFirst() != Globals.instance().getHover().getHoveredBubble()) {
				Globals.instance().getHover().finishHover();
				Globals.instance().setMode(Globals.Mode.STANDBY, this);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(project != null) {
			if(e.getButton() == MouseEvent.BUTTON3) {
				if(Globals.instance().mouseMode() == Globals.Mode.STANDBY ||
						Globals.instance().isHovering()) {
					Globals.instance().setMode(Globals.Mode.DRAG, this);
					Globals.instance().setMousePoint(e.getX(), e.getY());
					Globals.instance().getHover().finishHover();
				}
			} else if(e.getButton() == MouseEvent.BUTTON1) {
				if(Globals.instance().isHovering()) {
					Bubble hoveredBubble = Globals.instance().getHover().getHoveredBubble();
					if(hoveredBubble != null) {
						Globals.instance().setSelectedBubble(hoveredBubble);
					}
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(project != null) {
			if(Globals.instance().mouseMode() == Globals.Mode.DRAG) {
				Globals.instance().setMode(Globals.Mode.STANDBY, this);
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(project != null) {
			if(Globals.instance().mouseMode() == Globals.Mode.NEW_BUBBLE) {
				if(Globals.instance().getSelector().isComplete()) {
					Bubble newBubble = project.completedSelected(Globals.instance().getSelector().getPoints(), displacement, scale);
					if(newBubble != null) {
						Globals.instance().setSelectedBubble(newBubble);
						Globals.instance().getUndoStack().addAction(new CreateBubbleAction(newBubble, project.getPageForBubble(newBubble)));
					}
					Globals.instance().setMode(Globals.Mode.STANDBY, this);
				} else {
					Globals.instance().getSelector().addPoint(new Point(e.getX(), e.getY()));
				}
			}
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(project != null) {
			if(e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
				if(Globals.instance().mouseMode() == Globals.Mode.STANDBY ||
						Globals.instance().mouseMode() == Globals.Mode.DRAG ||
						Globals.instance().isHovering()) {
					double beforeScale = scale;
					scale *= Math.pow(0.9, e.getWheelRotation());
					double x = e.getX() - displacement.x;
					double y = e.getY() - displacement.y;
					x *= scale / beforeScale;
					y *= scale / beforeScale;
					x += displacement.x;
					y += displacement.y;
					displacement.x += (e.getX() - x);
					displacement.y += (e.getY() - y);
					Globals.instance().getHover().finishHover();
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

	public void save() throws IOException {
		File saveFile = new File(project.getDirectory().getAbsolutePath() + File.separatorChar + Globals.SAVE_FILE);
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(saveFile));
		os.writeObject(project);
		os.writeObject(displacement);
		os.writeDouble(scale);
		os.close();
		
		PrintWriter wr = new PrintWriter(new FileWriter(new File("previous")));
		wr.println(project.getDirectory().getAbsolutePath());
		wr.close();
	}
	
	public void setValues(Point displacement, double scale) {
		this.displacement = new Point(displacement);
		this.scale = scale;
	}
	
	public Point getDisplacement() {
		return displacement;
	}
	
	public double getScale() {
		return scale;
	}

	public void setDisplacement(Point displacement) {
		this.displacement = new Point(displacement);
	}
}
