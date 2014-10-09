package com.gillessed.gradient.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import com.gillessed.gradient.Gradient;

public class GradientPointEditPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final int PADDING = 16;
	private static int TOPHEIGHT = 50;
	
	private final GradientPointEditPanel selfReference = this;
	
	private Gradient model;
	private AnchorPointer startPointer;
	private AnchorPointer endPointer;
	private final List<AnchorPointer> anchorPointers = new ArrayList<AnchorPointer>();
	
	private AnchorPointer pointerToDrag;
	private boolean dragging;
	
	private final MouseListener mouseListener = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {
			if(model != null) { 
				for(AnchorPointer ap : anchorPointers) {
					ap.setSelected(false);
				}
				for(AnchorPointer ap : anchorPointers) {
					if(ap.isSelected(e.getX(), e.getY())) {
						ap.setSelected(true);
						if(ap != startPointer && ap != endPointer) {
							dragging = true;
							pointerToDrag = ap;
						}
						repaint();
						break;
					}
				}
				if(e.getButton() == MouseEvent.BUTTON3) {
					
					boolean selected = false;
					AnchorPointer apSelected = null;
					for(AnchorPointer ap : anchorPointers) {
						if(ap.isSelected(e.getX(), e.getY())) {
							selected = true;
							apSelected = ap;
							break;
						}
					}
					
					if(selected) {
						JPopupMenu popup = constructRecolorPopup(apSelected);
						popup.show(e.getComponent(), e.getX(), e.getY());
						repaint();
					} else if(e.getX() > PADDING && e.getX() < model.getSize() + PADDING){
						JPopupMenu popup = constructNewPointerPopup(e.getX());
						popup.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			}
		}
		private JPopupMenu constructNewPointerPopup(final int x) {
			JPopupMenu popup = new JPopupMenu();
			JMenuItem changeColor = new JMenuItem("New Pointer");
			changeColor.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Color colour = JColorChooser.showDialog(selfReference, "Choose new color", Color.black);
					AnchorPointer newPointer = new AnchorPointer(selfReference, colour, x, 20, TOPHEIGHT);
					List<AnchorPointer> newPointers = new ArrayList<AnchorPointer>();
					for(int i = 0; i < anchorPointers.size(); i++) {
						newPointers.add(anchorPointers.get(i));
						if(x > anchorPointers.get(i).getX() && x < anchorPointers.get(i + 1).getX()) {
							newPointers.add(newPointer);
						}
					}
					anchorPointers.clear();
					anchorPointers.addAll(newPointers);
					recalculateGradient();
				}
			});
			popup.add(changeColor);
			return popup;
		}
		private JPopupMenu constructRecolorPopup(final AnchorPointer p) {
			JPopupMenu popup = new JPopupMenu();
			
			JMenuItem changeColor = new JMenuItem("Change color");
			changeColor.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Color colour = JColorChooser.showDialog(selfReference, "Choose new color", p.getColor());
					p.setColor(colour);
					recalculateGradient();
				}
			});
			
			JMenuItem deletePoint = new JMenuItem("Delete point");
			if(anchorPointers.size() == 2) deletePoint.setEnabled(false);
			if(p == startPointer) deletePoint.setEnabled(false);
			if(p == endPointer) deletePoint.setEnabled(false);
			deletePoint.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					anchorPointers.remove(p);
					recalculateGradient();
					repaint();
				}
			});
			
			popup.add(changeColor);
			popup.add(deletePoint);
			return popup;
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			dragging = false;
			repaint();
		}
	};
	private final MouseMotionListener mouseMotionListener = new MouseMotionListener() {
		@Override
		public void mouseMoved(MouseEvent e) {
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			if(dragging) {
				pointerToDrag.setX(e.getX());
				recalculateGradient();
			}
		}
	};
	
	public void recalculateGradient() {
		Color startColor = startPointer.getColor();
		Color endColor = endPointer.getColor();
		SortedMap<Integer, Color> colorMap = new TreeMap<Integer, Color>();
		for(AnchorPointer ap : anchorPointers) {
			if(ap != startPointer && ap != endPointer) {
				colorMap.put(ap.getX() - PADDING, ap.getColor());
			}
		}
		Gradient newGradient = new Gradient(startColor, endColor, colorMap, model.getSize(), model.getName());
		setModelAfterDrag(newGradient);
	}
	
	public GradientPointEditPanel(Gradient model) {
		this.model = model;
		setBorder(BorderFactory.createLineBorder(Color.black));
		setPreferredSize(new Dimension(Gradient.DEFAULT_SIZE + 2 * PADDING, TOPHEIGHT + 50 + 2 * PADDING));
		createAnchorPointers();
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseMotionListener);
	}
	
	private void createAnchorPointers() {
		anchorPointers.clear();
		if(model != null) {
			startPointer = new AnchorPointer(this, model.getColor(0), PADDING, 20, TOPHEIGHT);
			anchorPointers.add(startPointer);
			for(Integer key : model.getMiddleAnchors().keySet()) {
				anchorPointers.add(new AnchorPointer(this, model.getColor(key), PADDING + key, 20, TOPHEIGHT));
			}
			endPointer = new AnchorPointer(this, model.getColor(model.getSize() - 1), PADDING + model.getSize() - 1, 20, TOPHEIGHT);
			anchorPointers.add(endPointer);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		for(AnchorPointer ap : anchorPointers) {
			ap.draw(g);
		}
		if(model != null) {
			for(int i = 0; i < model.getSize(); i++) {
				g.setColor(model.getColor(i));
				g.drawLine(PADDING + i, TOPHEIGHT + PADDING, PADDING + i, getHeight() - PADDING);
			}
		}
		g.setColor(Color.black);
		int size = model != null ? model.getSize() : Gradient.DEFAULT_SIZE;
		g.drawRect(PADDING - 1, TOPHEIGHT + PADDING - 1, size + 1, getHeight() - (2 * PADDING + TOPHEIGHT) + 2);
	}

	public void setModel(Gradient model) {
		this.model = model;
		createAnchorPointers();
		repaint();
	}

	public void setModelAfterDrag(Gradient model) {
		this.model.copyGradient(model);
		repaint();
	}

	public Gradient getModel() {
		return model;
	}
}
