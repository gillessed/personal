package com.gillessed.spacepenguin.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public abstract class PressableRenderTarget extends RenderTarget {

	private final List<MouseListener> actionListeners;
	
	public PressableRenderTarget(String id) {
		super(id);
		actionListeners = new ArrayList<MouseListener>();
	}
	
	public void addMouseListener(MouseListener al) {
		actionListeners.add(al);
	}
	
	public void removeActionListener(MouseListener al) {
		actionListeners.remove(al);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(covered(e.getX(), e.getY())) {
			state = RenderState.HOVER;
		} else {
			if(state != RenderState.PRESSED) {
				state = RenderState.IDLE;
			}
		}
		super.mouseMoved(e);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(state == RenderState.HOVER) {
			setState(RenderState.PRESSED);
			for(MouseListener al : actionListeners) {
				al.mousePressed(e);
			}
		}
		super.mousePressed(e);
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(state == RenderState.PRESSED) {
			if(covered(e.getX(), e.getY())) {
				state = RenderState.HOVER;
			} else {
				state = RenderState.IDLE;
			}
			for(MouseListener al : actionListeners) {
				al.mouseReleased(e);
			}
		}
		super.mouseReleased(e);
	}
	
	private boolean covered(int x, int y) {
		return x >= padding.x
				&& x <= padding.x + padding.width
				&& y >= padding.y
				&& y <= padding.y + padding.height;
	}
}
