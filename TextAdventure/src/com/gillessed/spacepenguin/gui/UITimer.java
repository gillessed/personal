package com.gillessed.spacepenguin.gui;

import javax.swing.JPanel;


public class UITimer extends Thread {
	private static final long SCREEN_REPAINT_PAUSE = (long)(1000.0 / 60.0);
	
	private long lastDraw = 0;

	private JPanel panel;
	
	public UITimer(JPanel panel) {
		setDaemon(true);
		this.panel = panel;
	}

	@Override
	public void run() {
		while(true) {
			if(lastDraw != 0) {
				while((System.currentTimeMillis() - lastDraw) < SCREEN_REPAINT_PAUSE) {
					try {
						Thread.sleep(SCREEN_REPAINT_PAUSE - (System.currentTimeMillis() - lastDraw));
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
			panel.repaint();
			lastDraw = System.currentTimeMillis();
		}
	}
}
