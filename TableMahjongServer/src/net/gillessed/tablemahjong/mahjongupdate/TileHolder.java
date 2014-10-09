package net.gillessed.tablemahjong.mahjongupdate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.gillessed.tablemahjong.mahjonggame.MahjongTile;

public class TileHolder {
	private final String uuid;
	private List<MahjongTile> tiles;
	
	public TileHolder() {
		tiles = new ArrayList<MahjongTile>();
		uuid = UUID.randomUUID().toString();
	}
	
	public TileHolder(String uuid) {
		tiles = new ArrayList<MahjongTile>();
		this.uuid = uuid;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public MahjongTile getTile(String tileDescription) {
		for(MahjongTile mt : tiles) {
			if(mt.getDescription().equals(tileDescription)) {
				return mt;
			}
		}
		return null;
	}
	
	public void addMahjongTile(MahjongTile tile) {
		tiles.add(tile);
	}
	
	public void removeMahjongTile(MahjongTile tile) {
		tiles.remove(tile);
	}
	
	public List<MahjongTile> getList() {
		return tiles;
	}
	
	public MahjongTile get(int i) {
		return tiles.get(i);
	}
}
