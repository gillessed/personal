package com.gillessed.scanlater.ui;

public class ImagePanelRepaintTimer extends Thread {
	private static double FPS = 30;
	private ScanlaterImagePanel panel;

	public ImagePanelRepaintTimer(ScanlaterImagePanel panel) {
		this.panel = panel;
		setDaemon(true);
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep((long)(1 / FPS));
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			panel.repaint();
		}
	}
}
