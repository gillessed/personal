package net.gillessed.tablemahjong.mahjonggame.swingui;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import net.gillessed.tablemahjong.mahjonggame.MahjongGame.Wind;
import net.gillessed.tablemahjong.mahjonggame.MahjongRound.Stage;
import net.gillessed.tablemahjong.mahjongupdate.MahjongUpdate.UpdateTargetType;

public class MahjongDiscardZoneUI {
	private int x1;
	private int x2;
	private int y1;
	private int y2;
	
	/**
	 * This flag tells us whether the player is hovering above the discard area with a tile in drag.
	 */
	private boolean dragged;
	
	private final MahjongHandUI mahjongHandUI;
	
	public MahjongDiscardZoneUI(MahjongHandUI mahjongHandUI, MahjongTileDragger tileDragger) {
		this.mahjongHandUI = mahjongHandUI;
		dragged = false;
	}
	
	public synchronized void draw(Graphics g) {
	}
	
	public synchronized void dragEvent(MouseEvent e) {
		Point temp = mahjongHandUI.getTranformedCoords(e.getX(), e.getY());
		dragged = (temp.x > x1 && temp.x < x2 && temp.y > y1 && temp.y < y2);
	}
	
	public synchronized void mouseReleased() {
		if(isCurrentHandDiscard() && mahjongHandUI.getTileDragger().isDragging() && dragged) {
			UIResources.getMahjongTileUI(mahjongHandUI.getTileDragger().getTile()).setHovered(mahjongHandUI.getParent(), false);
			mahjongHandUI.getParent().getModel().getRound().setType(UpdateTargetType.BOTH);
			mahjongHandUI.getModel().discard(mahjongHandUI.getTileDragger().getTile());
		}
		dragged = false;
	}
	
	public synchronized void setX1(int x1) {
		this.x1 = x1;
	}
	
	public synchronized void setX2(int x2) {
		this.x2 = x2;
	}
	
	public synchronized void setY1(int y1) {
		this.y1 = y1;
	}
	
	public synchronized void setY2(int y2) {
		this.y2 = y2;
	}
	
	public synchronized void setDimensions(Rectangle rect) {
		x1 = rect.x;
		x2 = rect.x + rect.width;
		y1 = rect.y;
		y2 = rect.y + rect.height;
	}
	
	public MahjongHandUI getMahjongHandUI() {
		return mahjongHandUI;
	}
	
	private boolean isCurrentHandDiscard() {
		Wind currentWind = mahjongHandUI.getParent().getModel().getRound().getCurrentWind();
		if(currentWind != null) {
			return (Stage.Discard.equals(mahjongHandUI.getParent().getModel().getRound().getStage()) &&
					currentWind.equals(mahjongHandUI.getModel().getWind()));
		} else {
			return false;
		}
				
	}
}
