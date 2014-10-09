package net.gillessed.tablemahjong.mahjonggame.swingui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import net.gillessed.tablemahjong.collections.Ordering;
import net.gillessed.tablemahjong.mahjonggame.MahjongGame.Wind;
import net.gillessed.tablemahjong.mahjonggame.MahjongHand;
import net.gillessed.tablemahjong.mahjonggame.MahjongTile;

public class MahjongHandUI extends AbstractMahjongUIComponent<MahjongHand> {
	
	private final MahjongDiscardZoneUI discardZoneUI;
	private int orientation;
	private int x, y;
	private final MahjongTileDragger tileDragger;
	private final Ordering<MahjongTile> handOrdering;
	private final Set<MahjongTile> orderingDiff;

	public MahjongHandUI(MinSizeComponent container, MahjongGameUI parent, MahjongHand model) {
		super(container, parent, model);
		orientation = 0;
		tileDragger = new MahjongTileDragger(this);
		discardZoneUI = new MahjongDiscardZoneUI(this, tileDragger);
		handOrdering = new Ordering<MahjongTile>(getModel().getTiles().getList());
		orderingDiff = new HashSet<MahjongTile>();
	}
	
	private void populateOrdering() {
		synchronized(handOrdering) {
			orderingDiff.clear();
			for(MahjongTile tile : getModel().getTiles().getList()) {
				if(!handOrdering.getMap().containsKey(tile)) {
					orderingDiff.add(tile);
				}
			}
			for(MahjongTile tile : orderingDiff) {
				handOrdering.add(tile, 0);
			}
			orderingDiff.clear();
			for(MahjongTile tile : handOrdering.getMap().keySet()) {
				if(!getModel().getTiles().getList().contains(tile)) {
					orderingDiff.add(tile);
				}
			}
			for(MahjongTile tile : orderingDiff) {
				handOrdering.remove(tile);
			}
			orderingDiff.clear();
		}
	}

	@Override
	public synchronized void draw(Graphics g) {
		populateOrdering();
		
		Graphics2D g2 = (Graphics2D) g;
		calcOrientation();
		
		int handOffset = getParent().getUIProperty(MahjongGameUI.HAND_OFFSET);
		int tileWidth = getParent().getUIProperty(MahjongGameUI.DEFAULT_IMAGE_WIDTH);
		int tileHeight = getParent().getUIProperty(MahjongGameUI.DEFAULT_IMAGE_HEIGHT);
		int pickupOverlay = getParent().getUIProperty(MahjongGameUI.PICKUP_OVERLAY);
		int tileZoneInset = getParent().getUIProperty(MahjongGameUI.TILE_ZONE_INSET);
		int sqSize = getParent().getUIProperty(MahjongGameUI.CENTER_SQUARE_SIZE);
		int tileZoneY = 0;
		
		switch(orientation) {
		case 0:
			x = getContainer().getWidth() / 2;
			y = getContainer().getHeight() - handOffset;
			tileZoneY = y - ((getContainer().getHeight() + sqSize) / 2 + tileZoneInset);
			break;
		case 1:
			x = handOffset;
			y = getContainer().getHeight() / 2;
			tileZoneY = ((getContainer().getWidth() - sqSize) / 2 - tileZoneInset) - x;
			break;
		case 2:
			x = getContainer().getWidth() / 2;
			y = handOffset;
			tileZoneY = ((getContainer().getHeight() - sqSize) / 2 - tileZoneInset) - y;
			break;
		case 3:
			x = getContainer().getWidth() - handOffset;
			y = getContainer().getHeight() / 2;
			tileZoneY = x - ((getContainer().getWidth() + sqSize) / 2 + tileZoneInset);
			break;
		}
		
		int tileZoneHeight = tileZoneY - tileHeight / 2 - tileZoneInset;

		double rotationAngle = (orientation) * Math.PI * 0.5;
		g2.translate(x, y);
		g2.rotate(rotationAngle);
		
		int fullHandStartX = -(15 * tileWidth + pickupOverlay) / 2;
		g2.setStroke(new BasicStroke(5.0f));
		g2.setColor(new Color(15, 100, 15));
		discardZoneUI.setDimensions(new Rectangle(fullHandStartX + tileZoneInset,
				-tileZoneY,
				 -fullHandStartX + sqSize / 2 - 2 * tileZoneInset, 
				 tileZoneHeight));
		discardZoneUI.draw(g2);
		
		int handSize = getModel().getTiles().getList().size();
		if(getModel().isHasPickup()) {
			handSize--;
		}
		int startX = -((2 + handSize) * tileWidth + pickupOverlay) / 2;
		
		g2.setColor(new Color(0,255,0));
		int pickupOverlayY = pickupOverlay + tileHeight / 2;
		g2.fillRect(startX, -pickupOverlayY, 2 * pickupOverlay + tileWidth, 2 * pickupOverlayY);

		int handStartX = (int)(startX + pickupOverlay + (2.5d) * tileWidth);
		int handEndX = handStartX + handSize * tileWidth;
		
		int disp = isCloseEnoughToDisplace(startX + pickupOverlay, handStartX - tileWidth / 2, handEndX - tileWidth / 2);
		tileDragger.setDisplace(disp);
		MahjongTileUI pickupTileUI = null;
		if(getModel().isHasPickup() && (
				tileDragger.isDragging() &&
				!(handOrdering.get(0).equals(tileDragger.getTile())))) {
			pickupTileUI = UIResources.getMahjongTileUI(handOrdering.get(0));
		} else if(getModel().isHasPickup() && (
				tileDragger.isDragging() &&
				!(handOrdering.get(0).equals(tileDragger.getTile())) &&
				!(tileDragger.getUIIndex() == 0))) {
			pickupTileUI = UIResources.getMahjongTileUI(handOrdering.get(tileDragger.getUIIndex()));
		}
		if(pickupTileUI != null) {
			pickupTileUI.setX(startX + tileWidth / 2 + pickupOverlay);
			pickupTileUI.setY(0);
			pickupTileUI.draw(g, getParent(), getContainer());
		}
		for(int i = (getModel().isHasPickup() ? 1 : 0); i < handSize; i++) {
			if(i == tileDragger.getUIIndex()) {
				tileDragger.getDraggedTileUI().draw(g, getParent(), getContainer());
			} else if (i == tileDragger.getDisplace() && i > 0) {
				MahjongTileUI tileUI = UIResources.getMahjongTileUI(handOrdering.get(i));
				tileUI.setX(handStartX + tileDragger.getUIIndex() * tileWidth);
				tileUI.setY(0);
				tileUI.draw(g, getParent(), getContainer());
			} else {
				MahjongTileUI tileUI = UIResources.getMahjongTileUI(handOrdering.get(i));
				tileUI.setX(handStartX + i * tileWidth);
				tileUI.setY(0);
				tileUI.draw(g, getParent(), getContainer());
			}
			i++;
		}
		
		g2.rotate(-rotationAngle);
		g2.translate(-x, -y);
	}
	
	private int isCloseEnoughToDisplace(int pickupStartX, int handStartX, int handEndX) {
		if(tileDragger.isDragging()) {
			int mx = tileDragger.getDraggedTileUI().getX();
			int my = tileDragger.getDraggedTileUI().getY();
			int tileWidth = getParent().getUIProperty(MahjongGameUI.DEFAULT_IMAGE_WIDTH);
			int tileHeight = getParent().getUIProperty(MahjongGameUI.DEFAULT_IMAGE_HEIGHT);
			if(mx > handStartX && mx < handEndX && my > -tileHeight / 2 && my < tileHeight / 2) {
				return (mx - handStartX - tileWidth / 2) / tileWidth;
			} else if(getModel().isHasPickup() && mx > pickupStartX && mx < pickupStartX + tileWidth && my > -tileHeight / 2 && my < tileHeight / 2) {
				return 0;
			} else {
				return MahjongTileDragger.NO_SELECTED;
			}
		} else {
			return MahjongTileDragger.NO_SELECTED;
		}
	}

	public void calcOrientation() {
		Wind wind = getModel().getWind();
		if(getParent().isObserver()) {
			// South should be at the bottom of the screen if observer
			orientation = Wind.South.getDistanceTo(wind);
		} else {
			// If not observer, player's own hand should be at the bottom of the screen
			orientation = getParent().getSelfWind().getDistanceTo(wind);
		}
	}

	public void mouseMoved(MouseEvent e) {
		Point temp = getTranformedCoords(e.getX(), e.getY());
		for(MahjongTile tile : getModel().getTiles().getList()) {
			UIResources.getMahjongTileUI(tile).mouseMoved(getParent(), temp.x, temp.y);
		}
	}
	
	public Point getTranformedCoords(int sx, int sy) {
		int tx = sx - x;
		int ty = sy - y;
		int rx = tx;
		int ry = ty;
		switch(orientation) {
			case 0: break;
			case 1: rx = ty; ry = tx; break;
			case 2: rx = -tx; ry = -ty; break;
			case 3: rx = -ty; ry = tx; break;
		}
		return new Point(rx, ry);
	}

	public void mouseClicked(MouseEvent e) {
		MahjongTile tileHovered = null;
		for(MahjongTile tile : getModel().getTiles().getList()) {
			if(UIResources.getMahjongTileUI(tile).isHovered()) {
				tileHovered = tile;
			}
		}
		if(tileHovered != null) {
			tileDragger.newClick(tileHovered, e, handOrdering.getMap().get(tileHovered));
		}
	}

	public void mouseDragged(MouseEvent e) {
		if(tileDragger.isDragging()) {
			tileDragger.dragEvent(e);
			discardZoneUI.dragEvent(e);
		}
	}

	public void mouseReleased(MouseEvent e) {
		if(tileDragger.isDragging()) {
			if(!getParent().isControlsFrozen()) {
				int disp = tileDragger.getDisplace();
				if(disp != MahjongTileDragger.NO_SELECTED) {
					handOrdering.swap(handOrdering.get(disp), tileDragger.getTile());
				}
				discardZoneUI.mouseReleased();
			}
			tileDragger.releaseEvent(e);
		}
	}

	public void mouseReleased() {
		tileDragger.releaseEvent(null);
	}

	public MahjongTileDragger getTileDragger() {
		return tileDragger;
	}
}
