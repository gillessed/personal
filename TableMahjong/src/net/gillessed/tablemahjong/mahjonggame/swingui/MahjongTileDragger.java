package net.gillessed.tablemahjong.mahjonggame.swingui;

import java.awt.Point;
import java.awt.event.MouseEvent;

import net.gillessed.tablemahjong.mahjonggame.MahjongTile;

public class MahjongTileDragger {
	public static final int NO_SELECTED = -1;
	
	private final MahjongHandUI mahjongHandUI;
	private int sx;
	private int sy;
	private boolean dragging;
	private int displace;
	private int UIIndex;

	private MahjongTile tile;


	public MahjongTileDragger(MahjongHandUI mahjongHandUI) {
		dragging = false;
		sx = sy = 0;
		this.mahjongHandUI = mahjongHandUI;
		displace = NO_SELECTED;
		UIIndex = NO_SELECTED;
	}
	
	public void newClick(MahjongTile tile, MouseEvent click, int UIIndex) {
		this.tile = tile;
		this.UIIndex = UIIndex;
		
		Point temp = mahjongHandUI.getTranformedCoords(click.getX(), click.getY());
		this.sx = temp.x;
		this.sy = temp.y;
		dragging = true;
		displace = NO_SELECTED;
	}
	
	public MahjongTile getTile() {
		return tile;
	}
	
	public boolean isDragging() {
		return dragging;
	}
	
	public void dragEvent(MouseEvent drag) {
		Point transformed = mahjongHandUI.getTranformedCoords(drag.getX(), drag.getY());
		UIResources.getMahjongTileUI(tile).setDragX(mahjongHandUI.getParent(), transformed.x - sx);
		UIResources.getMahjongTileUI(tile).setDragY(mahjongHandUI.getParent(), transformed.y - sy);
	}

	public void releaseEvent(MouseEvent e) {
		if(UIResources.getMahjongTileUI(tile) != null) {
			UIResources.getMahjongTileUI(tile).setDragX(mahjongHandUI.getParent(), 0);
			UIResources.getMahjongTileUI(tile).setDragY(mahjongHandUI.getParent(), 0);
		}
		tile = null;
		dragging = false;
		displace = NO_SELECTED;
		UIIndex = NO_SELECTED;
	}

	public synchronized void setDisplace(int displace) {
		this.displace = displace;
	}

	public synchronized int getDisplace() {
		return displace;
	}

	public int getUIIndex() {
		return UIIndex;
	}

	public void setUIIndex(int uIIndex) {
		UIIndex = uIIndex;
	}
	
	public MahjongTileUI getDraggedTileUI() {
		return UIResources.getMahjongTileUI(tile);
	}
}
