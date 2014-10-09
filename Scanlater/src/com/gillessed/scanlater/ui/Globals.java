package com.gillessed.scanlater.ui;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.gillessed.scanlater.Bubble;
import com.gillessed.scanlater.Project;
import com.gillessed.scanlater.ui.undo.UndoStack;

public class Globals {

	public static final int CIRCLE_RADIUS = 16;
	public static final int PAGE_MARGIN = 20;
	public static String SAVE_FILE = "translation.trf";
	public static Stroke DASHED_STROKE = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
	public static Stroke THICK_STROKE = new BasicStroke(3);
	
	public enum Mode {
		STANDBY,
		DRAG,
		NEW_BUBBLE,
		HOVER,
		HOVER_T,
		HOVER_B,
		HOVER_L,
		HOVER_R,
		MOVE,
		MOVE_T,
		MOVE_B,
		MOVE_L,
		MOVE_R
	}
	
	private static Globals mouse;
	
	public static Globals instance() {
		if(mouse == null) {
			mouse = new Globals();
		}
		return mouse;
	}
	
	public boolean saveUpToDate;
	public int selectedFontSize;
	public int selectedFontStyle;
	private Mode mode;
	private Hover hover;
	private Selector selector;
	private final Point2D.Double mouseMoint;
	private Bubble selectedBubble;
	private List<BubbleListener> bubbleListeners;
	private UndoStack undoStack;
	private Project project;
	private BubbleUpdateListener bubbleUpdateListener;
	private ScanlaterImagePanel scanlaterImagePanel;
	
	public Globals() {
		selectedFontSize = 12;
		selectedFontStyle = Font.PLAIN;
		undoStack = new UndoStack();
		mode = Mode.STANDBY;
		selector = new Selector();
		mouseMoint = new Point2D.Double();
		bubbleListeners = new ArrayList<>();
		saveUpToDate = true;
		hover = new Hover(null);
	}
	
	public void setMode(Mode mode, Component c) {
		this.mode = mode;
		switch(mode) {
		case STANDBY: c.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); break;
		case DRAG: c.setCursor(new Cursor(Cursor.HAND_CURSOR)); break;
		case NEW_BUBBLE: c.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); break;
		case HOVER: c.setCursor(new Cursor(Cursor.MOVE_CURSOR)); break;
		case HOVER_T: c.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR)); break;
		case HOVER_B: c.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR)); break;
		case HOVER_L: c.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR)); break;
		case HOVER_R: c.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR)); break;
		case MOVE: c.setCursor(new Cursor(Cursor.MOVE_CURSOR)); break;
		case MOVE_T: c.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR)); break;
		case MOVE_B: c.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR)); break;
		case MOVE_L: c.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR)); break;
		case MOVE_R: c.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR)); break;
		}
	}

	public Mode mouseMode() {
		return mode;
	}
	
	public void setMousePoint(double x, double y) {
		mouseMoint.x = x;
		mouseMoint.y = y;
	}
	
	public void setMousePoint(Point2D.Double point) {
		this.mouseMoint.x = point.x;
		this.mouseMoint.y = point.y;
	}
	
	public Point2D.Double getMousePoint() {
		return new Point2D.Double(mouseMoint.x, mouseMoint.y);
	}
	
	public void newSelector() {
		selector = new Selector();
	}
	
	public Selector getSelector() {
		return selector;
	}
	
	public void addBubbleListener(BubbleListener bubbleListener) {
		bubbleListeners.add(bubbleListener);
	}
	
	public void setSelectedBubble(Bubble bubble) {
		if(this.selectedBubble != null) {
			this.selectedBubble.setUpdateListener(null);
		}
		this.selectedBubble = bubble;
		if(this.selectedBubble != null) {
			this.selectedBubble.setUpdateListener(bubbleUpdateListener);
		}
		if(!Globals.instance().IsBubbleVisible(this.selectedBubble)) {
			Globals.instance().focusOnBubble(this.selectedBubble);
		}
		for(BubbleListener bl : bubbleListeners) {
			bl.bubbleSelected(selectedBubble);
		}
	}

	public boolean hasSelectedBubble() {
		return selectedBubble != null;
	}
	
	public Bubble getSelectedBubble() {
		return selectedBubble;
	}
	
	public void setProject(Project project) {
		this.project = project;
	}

	public UndoStack getUndoStack() {
		return undoStack;
	}
	
	public Project getProject() {
		return project;
	}

	public void setBubbleUpdateListener(BubbleUpdateListener bubbleUpdateListener) {
		this.bubbleUpdateListener = bubbleUpdateListener;
	}

	public void setImagePanel(ScanlaterImagePanel scanlaterImagePanel) {
		this.scanlaterImagePanel = scanlaterImagePanel;
	}
	
	public ScanlaterImagePanel getImagePanel() {
		return scanlaterImagePanel;
	}
	
	public boolean IsBubbleVisible(Bubble bubble) {
		if(scanlaterImagePanel == null) {
			return false;
		} else {
			return project.isBubbleVisible(bubble, scanlaterImagePanel);
		}
	}
	
	public void focusOnBubble(Bubble bubble) {
		project.focusOnBubble(bubble, scanlaterImagePanel);
	}
	
	public void newHover(Bubble bubble) {
		hover = new Hover(bubble);
	}
	
	public Hover getHover() {
		return hover;
	}

	public boolean isHovering() {
		return (mode == Mode.HOVER) ||
				(mode == Mode.HOVER_T) ||
				(mode == Mode.HOVER_B) ||
				(mode == Mode.HOVER_L) ||
				(mode == Mode.HOVER_R);
	}
}
