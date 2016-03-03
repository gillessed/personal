package net.gillessed.icarus.swingui.explorer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.engine.fractal.FractalEngine;
import net.gillessed.icarus.geometry.ViewRectangle;

public class ExplorerFlamePanel extends JPanel {
	private static final long serialVersionUID = 4537620802485175864L;

	private FlameModel flameModel;
	private BufferedImage dbImage;

	private final ComponentListener resizeListener = new ComponentListener() {

		@Override
		public void componentHidden(ComponentEvent e) {}
		@Override
		public void componentMoved(ComponentEvent e) {}
		@Override
		public void componentResized(ComponentEvent e) {
			repaint();
		}
		@Override
		public void componentShown(ComponentEvent e) {}

	};

	public ExplorerFlamePanel() {
		this.flameModel = null;
		addComponentListener(resizeListener);
	}

	/**
	 * This functions runs the fractal engine and does everything.
	 */
	public void runFractalAlgorithm() throws Exception {
		int pixelWidth = getWidth();
		int pixelHeight = getHeight();
		ViewRectangle viewRectangle = new ViewRectangle(ViewRectangle.DEFAULT_VALUE, (double) pixelHeight / (double)pixelWidth, this);
        FractalEngine fractalEngine = new FractalEngine(flameModel, pixelWidth, pixelHeight, viewRectangle, 1);
        setImage(fractalEngine.run());
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		BufferedImage image = getImage();
		if(image != null) {
			int imageLeft = getWidth() / 2 - image.getWidth() / 2;
			int imageTop = getHeight() / 2 - image.getHeight() / 2;
			g.drawImage(image, imageLeft, imageTop, this);
		}
	}

	public synchronized void removeImage() {
        setImage(null);
	}

	public synchronized void setFlameModel(FlameModel flameModel) {
		this.flameModel = flameModel;
		try {
			runFractalAlgorithm();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public synchronized FlameModel getFlameModel() {
		return flameModel;
	}

	public synchronized BufferedImage getImage() {
	    return dbImage;
	}

	public synchronized void setImage(BufferedImage image) {
	    this.dbImage = image;
	    repaint();
	}
}
