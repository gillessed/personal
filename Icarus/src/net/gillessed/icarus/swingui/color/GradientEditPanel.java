package net.gillessed.icarus.swingui.color;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.Function;

import com.gillessed.gradient.Gradient;

public class GradientEditPanel extends JPanel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	private static final int PADDING = 10;
	private static final int TOPPADDING = 50;
	private Gradient gradient;
	private final FlameModel flameModel;
	private final List<FunctionValuePointer> pointers;
	private boolean dragging;
	private FunctionValuePointer draggedPointer;

	public GradientEditPanel(Gradient gradient, FlameModel flameModel) {
		this.gradient = gradient;
		this.flameModel = flameModel;
		pointers = new ArrayList<FunctionValuePointer>();
		for(Function f : flameModel.getFunctions()) {
			getPointers().add(new FunctionValuePointer(f, gradient));
		}
		dragging = false;
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0,0,getWidth(),getHeight());
		
		if(gradient != null) {
			for(int i = PADDING;  i < getWidth() - PADDING; i++) {
				g.setColor(gradient.getColor((i - PADDING) % gradient.getSize()));
				g.drawLine(i, PADDING + TOPPADDING, i, getHeight() - PADDING);
			}
		}
		for(FunctionValuePointer fvp : getPointers()) {
			fvp.draw(g, PADDING, TOPPADDING, getHeight());
		}
	}

	public void setGradient(Gradient gradient) {
		this.gradient = gradient;
		for(FunctionValuePointer fvp : pointers) {
			fvp.setGradient(gradient);
		}
	}

	public Gradient getGradient() {
		return gradient;
	}
	
	@Override
	public int getWidth() {
		return Gradient.DEFAULT_SIZE + PADDING * 2;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for(FunctionValuePointer fvp : getPointers()) {
			if(fvp.mouseOn(e.getX(), e.getY())) {
				dragging = true;
				draggedPointer = fvp;
				break;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		dragging = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(dragging) {
			draggedPointer.setValue(e.getX());
			repaint();
		}
	}

	public List<FunctionValuePointer> getPointers() {
		return pointers;
	}
	
	public void applyChanges() {
		flameModel.getColorProvider().setGradient(gradient);
		for(FunctionValuePointer fvp : pointers) {
			fvp.applyChanges();
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}
}
