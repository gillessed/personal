package com.gillessed.spacepenguin.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import com.gillessed.spacepenguin.panel.Screen;
import com.gillessed.spacepenguin.panel.TargetListener;

public class Panel extends JPanel {
	private static final long serialVersionUID = -8720623298070512878L;
	
	private Screen target;
	private final UITimer timer;
	
	public Panel(int width, int height) {
		setPreferredSize(new Dimension(width, height));
		timer = new UITimer(this);
		timer.start();
	}
	
	public void targetInit(Screen target) {
		synchronized(this) {
			setTarget(target);
			target.addTargetListener(new TargetListener() {
				@Override
				public void changeTarget(Screen oldTarget, Screen newTarget) {
					oldTarget.removeTargetListener(this);
					removeMouseListener(oldTarget);
					removeMouseMotionListener(oldTarget);
					removeKeyListener(oldTarget);
					removeComponentListener(oldTarget);
					newTarget.addTargetListener(this);
					newTarget.setup();
					setTarget(newTarget);
				}
			});
		}
	}
	
	public void setTarget(Screen target) {
		synchronized(this) {
			this.target = target;
			target.setPanel(this);
			addMouseListener(target);
			addMouseMotionListener(target);
			addKeyListener(target);
			addComponentListener(target);
			addMouseWheelListener(target);
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		synchronized(this) {
			if(target != null) {
				target.draw(g2);
			}
		}
	}
}
