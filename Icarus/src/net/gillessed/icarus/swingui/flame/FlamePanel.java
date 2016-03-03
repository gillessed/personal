package net.gillessed.icarus.swingui.flame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.engine.Callback;
import net.gillessed.icarus.engine.FlameRenderer;
import net.gillessed.icarus.engine.ProgressBarUpdater;
import net.gillessed.icarus.engine.ProgressUpdater;
import net.gillessed.icarus.event.NewFlameListener;
import net.gillessed.icarus.geometry.ViewRectangle;
import net.gillessed.icarus.swingui.FlameModelContainer;

public class FlamePanel extends JPanel {
	private static final long serialVersionUID = 5834137707867738051L;
	
	private FlameModelContainer flameModelContainer;
	private BufferedImage dbImage;
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
	
	private final NewFlameListener flameChangeListener = new NewFlameListener() {
		@Override
		public void newFlame(FlameModel flameModel) {
			dbImage = null;
			repaint();
		}
	};
	
	public FlamePanel(FlameModelContainer flameModelContainer, JProgressBar monitor) {
		this.flameModelContainer = flameModelContainer;
		this.flameModelContainer.addNewFlameListener(flameChangeListener);
		this.monitor = monitor;
		addComponentListener(resizeListener);
	}

	public void resizeImage() {
		repaint();
	}

	/**
	 * This functions runs the fractal engine and does everything.
	 */
	public void runFractalAlgorithm() throws Exception {
		pixelWidth = getWidth();
		pixelHeight = getHeight();
		ViewRectangle viewRectangle = new ViewRectangle(ViewRectangle.DEFAULT_VALUE, (double) pixelHeight / (double)pixelWidth, this);

		ProgressUpdater updater = new ProgressBarUpdater(monitor);
		Callback<BufferedImage> callback = new Callback<BufferedImage>() {
            @Override
            public void callback(BufferedImage image) throws Exception {
                setImage(image);
                repaint();
            }
        };
		FlameRenderer.get().renderFlame(flameModelContainer.getFlameModel(),
		        pixelWidth,
		        pixelHeight,
		        viewRectangle,
		        updater,
		        callback
		        );
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(0, 0, getWidth(), getHeight());
		BufferedImage image = getImage();
		if(image != null) {
			int imageLeft = getWidth() / 2 - image.getWidth() / 2;
			int imageTop = getHeight() / 2 - image.getHeight() / 2;
			g.setColor(Color.white);
			g.fillRect(imageLeft - 1, imageTop - 1, image.getWidth() + 2, image.getHeight() + 2);
			g.drawImage(image, imageLeft, imageTop, this);
		}
	}

	public synchronized BufferedImage getImage() {
	    return dbImage;
	}

	public synchronized void setImage(BufferedImage image) {
	    this.dbImage = image;
	}
}
