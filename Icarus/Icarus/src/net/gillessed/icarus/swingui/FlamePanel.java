package net.gillessed.icarus.swingui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.engine.EngineMonitor;
import net.gillessed.icarus.engine.FractalEngine;
import net.gillessed.icarus.event.ProgressChangeEvent;
import net.gillessed.icarus.event.ProgressListener;
import net.gillessed.icarus.geometry.ViewRectangle;

public class FlamePanel extends JPanel implements EngineMonitor {
	public static final String DONE = "Done";
	private static final long serialVersionUID = 5834137707867738051L;
	private final List<ProgressListener> progressListeners;
	private final List<ProgressListener> progressListenersToRemove;
	private ViewRectangle viewRectangle;
	private FlameModel flameModel;
	private BufferedImage dbImage;
	private Graphics dbg;
	private FractalEngine fractalEngine;
	private int pixelWidth;
	private int pixelHeight;
	private final JProgressBar monitor;
	
	private final ComponentListener resizeListener = new ComponentListener() {

		@Override
		public void componentHidden(ComponentEvent e) {}
		@Override
		public void componentMoved(ComponentEvent e) {}
		@Override
		public void componentResized(ComponentEvent e) {
			resizeImage();
		}
		@Override
		public void componentShown(ComponentEvent e) {}
		
	};
	
	public FlamePanel(JProgressBar monitor, int initialQuality, double initialGamma) {
		this(null, monitor, initialQuality, initialGamma);
	}
	public FlamePanel(FlameModel flameModel, JProgressBar monitor, int initialQuality, double initialGamma) {
		this.monitor = monitor;
		this.setFlameModel(flameModel);
		
		progressListeners = new ArrayList<ProgressListener>();
		progressListenersToRemove = new ArrayList<ProgressListener>();
		setQuality(initialQuality);
		setGamma(initialGamma);
		
		addComponentListener(resizeListener);
	}
	
	public void resizeImage() {
		if(!isThreadRunning()) {
			pixelWidth = getWidth();
			pixelHeight = getHeight();
			viewRectangle = new ViewRectangle(ViewRectangle.DEFAULT_VALUE, (double) pixelHeight / (double)pixelWidth, this);
			if(fractalEngine == null) {
				dbImage = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_4BYTE_ABGR);
				dbg = dbImage.getGraphics();
				dbg.setColor(Color.black);
				dbg.fillRect(0, 0, pixelWidth, pixelHeight);
			}
			repaint();
		}
	}
	
	/**
	 * This functions runs the fractal engine and does everything.
	 */
	public void runFractalAlgorithm() {
		pixelWidth = getWidth();
		pixelHeight = getHeight();
		viewRectangle = new ViewRectangle(ViewRectangle.DEFAULT_VALUE, (double) pixelHeight / (double)pixelWidth, this);
		dbImage = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_4BYTE_ABGR);
		dbg = dbImage.getGraphics();
		dbg.setColor(Color.black);
		dbg.fillRect(0, 0, pixelWidth, pixelHeight);
		fractalEngine = new FractalEngine(this, flameModel, pixelWidth, pixelHeight, viewRectangle, dbImage);
		fractalEngine.run();
	}
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(0, 0, getWidth(), getHeight());
		if(dbImage != null) {
			int imageLeft = getWidth() / 2 - dbImage.getWidth() / 2;
			int imageTop = getHeight() / 2 - dbImage.getHeight() / 2;
			g.setColor(Color.white);
			g.fillRect(imageLeft - 1, imageTop - 1, dbImage.getWidth() + 2, dbImage.getHeight() + 2);
			g.drawImage(dbImage, imageLeft, imageTop, this);
		}
	}
	public void setFlameModel(FlameModel flameModel) {
		this.flameModel = flameModel;
	}
	public FlameModel getFlameModel() {
		return flameModel;
	}
	public void setQuality(int quality) {
		flameModel.setQuality(quality);
	}
	public int getQuality() {
		return flameModel.getQuality();
	}
	public void setGamma(double gamma) {
		flameModel.setGamma(gamma);
	}
	public double getGamma() {
		return flameModel.getGamma();
	}
	public void addProgressListener(ProgressListener progressListener) {
		progressListener.setHolder(this);
		progressListeners.add(progressListener);
	}
	public void removeProgressListener(ProgressListener progressListener) {
		progressListeners.remove(progressListener);
		progressListener.setHolder(null);
	}
	public void setRemoveProgressListener(ProgressListener progressListener) {
		progressListenersToRemove.add(progressListener);
	}
	public void fireProgressChangeEvent(ProgressChangeEvent e) {
		for(ProgressListener pl : progressListeners) {
			pl.progressChangeEventPerformed(e);
		}
		for(ProgressListener pl : progressListenersToRemove) {
			progressListeners.remove(pl);
		}
		progressListenersToRemove.clear();
	}
	public boolean isThreadRunning() {
		if(fractalEngine == null) return false;
		return fractalEngine.isRunning();
	}
	@Override
	public void setThreadState(String threadState) {
		monitor.setString(getProgress() + "% - " + threadState);
	}
	public String getThreadState() {
		if(fractalEngine == null) return "Idle";
		return fractalEngine.getThreadState();
	}
	public int getProgress() {
		if(fractalEngine == null) {
			return 0;
		}
		return fractalEngine.getProgress();
	}
}
