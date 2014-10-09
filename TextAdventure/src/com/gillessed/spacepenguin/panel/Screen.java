package com.gillessed.spacepenguin.panel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import com.gillessed.spacepenguin.gui.RenderTarget;
import com.gillessed.spacepenguin.gui.renderproperties.FileRenderSettings;
import com.gillessed.spacepenguin.gui.renderproperties.RenderProperties;
import com.gillessed.spacepenguin.gui.renderproperties.RenderSettings;

public class Screen extends RenderTarget implements ComponentListener {

	private final List<TargetListener> targetListeners;
	private final Map<String, RenderTarget> tree;
	protected JPanel panel;
	protected Point mousePosition;
	private boolean update; 
	
	public Screen(String id, String... renderSettingsFile) {
		super(id);
		tree = new HashMap<String, RenderTarget>();
		targetListeners = new ArrayList<TargetListener>();

		RenderSettings properties = new RenderSettings() {};
		for(int i = 0; i < renderSettingsFile.length; i++) {
			try {
				RenderSettings tempProperties = new FileRenderSettings(renderSettingsFile[i]);
				properties.agglomerateRules(tempProperties);
			} catch (IOException e) {
				System.err.println("Cannot find property file " + renderSettingsFile);
			}
		}
		setRenderSettings(properties);
		update = true;
	}
	
	public Screen(String id, RenderSettings renderSettings) {
		super(id);
		tree = new HashMap<String, RenderTarget>();
		targetListeners = new ArrayList<TargetListener>();
		setRenderSettings(renderSettings);
	}
	
	public void addTargetListener(TargetListener tl) {
		targetListeners.add(tl);
	}
	
	public void removeTargetListener(TargetListener tl) {
		targetListeners.remove(tl);
	}

	@Override
	public Rectangle draw(Graphics2D g) {
		RenderProperties rp = getRenderProperties();
		g.setColor(rp.getBackgroundColor(getState()));
		g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
		lastDraw = null;
		for(RenderTarget rt : children) {
			if(rt.isVisible()) {
				lastDraw = rt.draw(g);
			}
		}
		setUpdate(false);
		return null;
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
		inner.x = 0;
		inner.y = 0;
		inner.width = panel.getWidth();
		inner.height = panel.getHeight();
	}
	
	@Override
	public void childAdded(RenderTarget child) {
		tree.put(child.getId(), child);
	}
	
	@Override
	public Graphics getGraphics() {
		if(panel != null) {
			return panel.getGraphics();
		} else {
			return null;
		}
	}

	@Override
	public void componentHidden(ComponentEvent e) {}
	@Override
	public void componentMoved(ComponentEvent e) {}
	@Override
	public void componentResized(ComponentEvent e) {
		inner.x = 0;
		inner.y = 0;
		inner.width = panel.getWidth();
		inner.height = panel.getHeight();
		padding.x = 0;
		padding.y = 0;
		padding.width = panel.getWidth();
		padding.height = panel.getHeight();
		margin.x = 0;
		margin.y = 0;
		margin.width = panel.getWidth();
		margin.height = panel.getHeight();
		setUpdate(true);
	}
	@Override
	public void componentShown(ComponentEvent e) {}

	@Override
	protected Dimension getInnerDimension(Graphics2D g) {
		if(panel == null) {
			return new Dimension();
		} else {
			return new Dimension(panel.getWidth(), panel.getHeight());
		}
	}
	
	public void switchTarget(Screen screen) {
		while(targetListeners.size() > 0) {
			targetListeners.get(0).changeTarget(this, screen);
		}
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}
}
