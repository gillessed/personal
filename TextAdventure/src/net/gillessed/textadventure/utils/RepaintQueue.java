package net.gillessed.textadventure.utils;

import javax.swing.JComponent;


public class RepaintQueue extends Thread {

	private static final int REPAINT_SPEED = 1000/25;
	
	private boolean repaintRequest;
	private boolean killed;

	private final JComponent repaintTarget;
	
	public RepaintQueue(JComponent repaintTarget) {
		this.repaintTarget = repaintTarget;
		repaintRequest = false;
		killed = false;
		setDaemon(true);
	}

	@Override
	public void run() {
		while(!killed) {
			try {
				Thread.sleep(REPAINT_SPEED);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			synchronized(this) {
				if(repaintRequest) {
					repaintTarget.repaint();
					repaintRequest = false;
				}
			}
		}
	}
	
	public void kill() {
		killed = true;
	}
	
	public synchronized void addRepaintRequest() {
		repaintRequest = true;
	}
}
