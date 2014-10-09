package gui;

import javax.swing.JPanel;

public class RepaintThread extends Thread {
	protected JPanel panel;
	public RepaintThread(JPanel panel) {
		this.panel = panel;
	}
	@Override
	public void run() {
		while(true) {
			panel.repaint();
			try {
				Thread.sleep((long)(1000d / 60d));
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
