package net.gillessed.icarus.swingui.gradient;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.Function;
import net.gillessed.icarus.event.FlameModificationListener;
import net.gillessed.icarus.event.NewFlameListener;
import net.gillessed.icarus.event.FunctionEvent;
import net.gillessed.icarus.event.FunctionListener;
import net.gillessed.icarus.swingui.FlameModelContainer;

import com.gillessed.gradient.Gradient;

public class GradientEditPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final int PADDING = 10;
	private static final int TOPPADDING = 50;

	private final FlameModelContainer flameModelContainer;
	private final List<FunctionValuePointer> pointers;
	private boolean dragging;
	private FunctionValuePointer draggedPointer;

	private final NewFlameListener newFlameListener = new NewFlameListener() {
		@Override
		public void newFlame(FlameModel flameModel) {
			flameModel.addFunctionListener(functionListener);
			updatePointers();
			repaint();
		}
	};

	private final FlameModificationListener flameModificationListener = new FlameModificationListener() {
        @Override
        public void flameModified() {
            updatePointers();
            repaint();
        }
    };

	private final FunctionListener functionListener = new FunctionListener() {
		@Override
		public void functionRemoved(FunctionEvent e) {
			updatePointers();
			repaint();
		}

		@Override
		public void functionAdded(FunctionEvent e) {
			updatePointers();
			repaint();
		}
	};

	private final MouseListener mouseListener = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			for(FunctionValuePointer fvp : pointers) {
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
			for(FunctionValuePointer fvp : pointers) {
				fvp.applyChanges();
			}
			flameModelContainer.flameModified();
		}
	};

	private final MouseMotionListener mouseMotionListener = new MouseMotionAdapter() {
		@Override
		public void mouseDragged(MouseEvent e) {
			if(dragging) {
				draggedPointer.setValue(e.getX());
				repaint();
			}
		}
	};

	public GradientEditPanel(FlameModelContainer flameModelContainer) {
		this.flameModelContainer = flameModelContainer;
		this.flameModelContainer.addNewFlameListener(newFlameListener);
		this.flameModelContainer.addFlameModificationListener(flameModificationListener);
		pointers = new ArrayList<FunctionValuePointer>();
		dragging = false;
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseMotionListener);
	}

	public void updatePointers() {
		pointers.clear();
		for(Function f : flameModelContainer.getFlameModel().getFunctions()) {
			pointers.add(new FunctionValuePointer(f, flameModelContainer.getFlameModel().getColorProvider()));
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0,0,getWidth(),getHeight());

		ColorProvider gradient = flameModelContainer.getFlameModel().getColorProvider();
		for(int i = PADDING;  i < getWidth() - PADDING; i++) {
			g.setColor(gradient.getColor((i - PADDING) % gradient.getSize()));
			g.drawLine(i, PADDING + TOPPADDING, i, getHeight() - PADDING);
		}
			for(FunctionValuePointer fvp : pointers) {
			fvp.draw(g, PADDING, TOPPADDING, getHeight());
		}
	}

	@Override
	public int getWidth() {
		return Gradient.DEFAULT_SIZE + PADDING * 2;
	}
}
