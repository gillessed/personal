package com.gillessed.spacepenguin.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import com.gillessed.spacepenguin.gui.renderproperties.RenderProperties;
import com.gillessed.spacepenguin.gui.renderproperties.RenderSettings;
import com.gillessed.spacepenguin.gui.renderproperties.SPCardinal;
import com.gillessed.spacepenguin.gui.renderproperties.SPDimension;
import com.gillessed.spacepenguin.gui.renderproperties.SPPositioning;
import com.gillessed.spacepenguin.panel.Screen;

public abstract class RenderTarget implements MouseListener, MouseMotionListener, KeyListener, MouseWheelListener {
	
	public enum RenderState {
		NONE,
		IDLE,
		HOVER,
		PRESSED,
		ALL
	}
	
	protected final Rectangle margin;
	protected final Rectangle padding;
	protected final Rectangle inner;
	
	private RenderTarget parent;
	protected final List<RenderTarget> childrenToAdd;
	protected final List<RenderTarget> children;
	private String id;
	private final List<String> classes;
	private final RenderProperties renderProperties;
	private RenderSettings renderSettings;
	protected Rectangle lastDraw;
	protected RenderState state;
	protected boolean visible;
	
	public RenderTarget(String id) {
		this.id = id;
		renderProperties = new RenderProperties();
		children = new ArrayList<RenderTarget>();
		childrenToAdd = new ArrayList<RenderTarget>();
		classes = new ArrayList<String>();
		this.state = RenderState.IDLE;
		margin = new Rectangle();
		padding = new Rectangle();
		inner = new Rectangle();
		visible = true;
	}

	public RenderTarget getParent() {
		return parent;
	}

	public Screen getScreen() {
		RenderTarget t = getParent();
		while(t != null && !(t instanceof Screen)) {
			t = t.getParent();
		}
		if(t == null) {
			return null;
		} else {
			return (Screen)t;
		}
	}

	public void setParent(RenderTarget parent) {
		this.parent = parent;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
		if(renderSettings != null) {
			renderSettings.updateSettings(this);
		}
	}

	public Rectangle getLastDraw() {
		return lastDraw;
	}

	public void setLastDraw(Rectangle lastDraw) {
		this.lastDraw = lastDraw;
	}

	public RenderProperties getRenderProperties() {
		return renderProperties;
	}
	
	public void setRenderSettings(RenderSettings renderSettings) {
		this.renderSettings = renderSettings;
	}
	
	public void addClass(String clazz) {
		classes.add(clazz);
		if(renderSettings != null) {
			renderSettings.updateSettings(this);
		}
	}
	
	public List<String> getClasses() {
		return classes;
	}
	
	public void removeClass(String clazz) {
		classes.remove(clazz);
		if(renderSettings != null) {
			renderSettings.updateSettings(this);
		}
	}
	
	public void addChild(RenderTarget target) {
		if(target == null) {
			System.out.println("hi?");
		}
		childrenToAdd.add(target);
	}
	
	public void setupChild(RenderTarget target) {
		if(target.getParent() == null) {
			children.add(target);
			target.setParent(this);
			renderSettings.childAdded(target);
		} else {
			throw new RuntimeException("Render target \"" + target.getId() + "\" already has a parent.");
		}
	}
	
	public void childAdded(RenderTarget child) {
		if(getParent() != null) {
			getParent().childAdded(child);
		} else {
			throw new RuntimeException ("Child " + child.getId() + " has no top-level (e.g. Screen) parent on add.");
		}
	}
	
	public void removeChild(RenderTarget target) {
		childRemoved(target);
		children.remove(target);
		target.setParent(null);
	}
	
	public void childRemoved(RenderTarget child) {
		if(getParent() != null) {
			getParent().childRemoved(child);
		} else {
			throw new RuntimeException ("Child " + child.getId() + " has no top-level (e.g. Screen) parent on remove.");
		}
	}

	public synchronized RenderState getState() {
		return state;
	}

	public synchronized void setState(RenderState state) {
		this.state = state;
	}

	public Graphics getGraphics() {
		if(getParent() != null) {
			return getParent().getGraphics();
		} else {
			throw new RuntimeException("Error. Child with id [" + id + "] does not have a parent and is not a screen.");
		}
	}
	
	public Point getRenderForPositioning(SPPositioning pos) {
		Point p = new Point();
		Rectangle rct = getParent().getLastDraw();
		switch(pos.layout) {
		case FLOW:
			if(rct == null) {
				p.x = getParent().getInner().x;
				p.y = getParent().getInner().y;
			} else {
				p.x = rct.x;
				p.y = rct.y;
			}
			break;
		case LEFT_X:
			p.x = getParent().getInner().x + pos.x;
			if(rct == null) {
				p.y = getParent().getInner().y;
			} else {
				p.y = rct.y + rct.height;
			}
			p.y += pos.y;
			break;
		case MIDDLE_X:
			p.x = getParent().getInner().x + getParent().inner.width / 2 - inner.width / 2 + pos.x;
			if(rct == null) {
				p.y = getParent().getInner().y;
			} else {
				p.y = rct.y + rct.height;
			}
			p.y += pos.y;
			break;
		case RELATIVE_TR:
			p.x = getParent().inner.x + getParent().inner.width - pos.x;
			p.y = getParent().inner.y + pos.y;
			break;
		case RELATIVE_TL:
			p.x = getParent().inner.x + pos.x;
			p.y = getParent().inner.y + pos.y;
			break;
		case RELATIVE_BL:
			p.x = getParent().inner.x + pos.x;
			p.y = getParent().inner.y + getParent().inner.height - pos.y - inner.height;
			break;
		case RELATIVE_BR:
			p.x = getParent().inner.x + getParent().inner.width - pos.x - inner.width;
			p.y = getParent().inner.y + getParent().inner.height - pos.y - inner.height;
			break;
		case ABSOLUTE:
			p.x = pos.x;
			p.y = pos.y;
			break;
		default:
			throw new RuntimeException("Layout " + pos.layout.toString() + " not implemented yet!");
		}
		return p;
	}
	
	public Rectangle draw(Graphics2D g) {
		if(getScreen().isUpdate()) {
			update(g);
		}
		return margin;
	}
	
	public void update(Graphics2D g) {
		RenderProperties rp = getRenderProperties();
		g.setFont(rp.getFont(getState()));
		SPDimension d = getRenderProperties().getDimensions(getState());
		if(d == null) {
			Dimension dim = getInnerDimension(g);
			inner.width = dim.width;
			inner.height = dim.height;
		} else {
			inner.width = d.x;
			if(inner.width == SPDimension.WIDTH) {
				inner.width = getParent().getInner().width;
			}
			inner.height = d.y;
			if(inner.height == SPDimension.HEIGHT) {
				inner.height = getParent().getInner().height;
			}
		}
		
		Point p = getRenderForPositioning(rp.getPositioning(getState()));
		margin.x = p.x;
		margin.y = p.y;
		
		SPCardinal margin_p = rp.getMargin(getState());
		SPCardinal padding_p = rp.getPadding(getState());
		padding.x = margin.x + margin_p.left;
		padding.y = margin.y + margin_p.top;
		padding.width = padding_p.left + inner.width + padding_p.right;
		padding.height = padding_p.top + inner.height + padding_p.bottom;
		inner.x = padding.x + padding_p.left;
		inner.y = padding.y + padding_p.top;
		margin.width = margin_p.left + padding.width + margin_p.right;
		margin.height = margin_p.top + padding.height + margin_p.bottom;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		for(RenderTarget rt : children) {
			rt.mouseDragged(e);
		}
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		for(RenderTarget rt : children) {
			if(rt.isVisible()) {
				rt.mouseMoved(e);
			}
		}
	}
	@Override
	final public void mouseClicked(MouseEvent e) {}
	
	@Override
	public void mousePressed(MouseEvent e) {
		for(RenderTarget rt : children) {
			if(rt.isVisible()) {
				rt.mousePressed(e);
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		for(RenderTarget rt : children) {
			rt.mouseReleased(e);
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		for(RenderTarget rt : children) {
			if(rt.isVisible()) {
				rt.mouseEntered(e);
			}
		}
	}
	@Override
	public void mouseExited(MouseEvent e) {
		for(RenderTarget rt : children) {
			if(rt.isVisible()) {
				rt.mouseExited(e);
			}
		}
	}
	@Override
	public void keyPressed(KeyEvent e) {
		for(RenderTarget rt : children) {
			if(rt.isVisible()) {
				rt.keyPressed(e);
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		for(RenderTarget rt : children) {
			if(rt.isVisible()) {
				rt.keyReleased(e);
			}
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {
		for(RenderTarget rt : children) {
			if(rt.isVisible()) {
				rt.keyTyped(e);
			}
		}
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		for(RenderTarget rt : children) {
			if(rt.isVisible()) {
				rt.mouseWheelMoved(e);
			}
		}
	}
	
	protected abstract Dimension getInnerDimension(Graphics2D g);

	public void setup() {
		for(RenderTarget child : childrenToAdd) {
			setupChild(child);
		}
		for(RenderTarget child : children) {
			child.setup();
		}
	}

	public Rectangle getMargin() {
		return margin;
	}

	public Rectangle getPadding() {
		return padding;
	}

	public Rectangle getInner() {
		return inner;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
