package net.gillessed.tablemahjong.mahjonggame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.gillessed.tablemahjong.mahjonggame.MahjongGame.Wind;
import net.gillessed.tablemahjong.mahjonggame.MahjongRound.Stage;
import net.gillessed.tablemahjong.mahjongupdate.MahjongUpdate;
import net.gillessed.tablemahjong.mahjongupdate.TileHolder;


public class MahjongHand {
	private final TileHolder tiles;
	private final TileHolder discards;
	private final List<TileHolder> formedGroups;
	private final MahjongRound round;
	private final Wind wind;
	private boolean hasPickup;
	
	public MahjongHand(MahjongRound round, Wind wind) {
		this.round = round;
		this.wind = wind;
		tiles = new TileHolder();
		round.registerTileHolder(tiles);
		discards = new TileHolder();
		round.registerTileHolder(discards);
		formedGroups = new ArrayList<TileHolder>();
	}
	
	public MahjongHand(MahjongRound round, Wind wind, String tileUuid, String discardUuid) {
		this.round = round;
		this.wind = wind;
		tiles = new TileHolder(tileUuid);
		round.registerTileHolder(tiles);
		discards = new TileHolder(discardUuid);
		round.registerTileHolder(discards);
		formedGroups = new ArrayList<TileHolder>();
	}
	
	public synchronized void put(MahjongTile tile, TileHolder from) {
		tiles.addMahjongTile(tile);
		round.fireUpdate(new MahjongUpdate(tile, from, tiles));
	}
	
	public synchronized void pickup(MahjongTile tile, TileHolder from) {
		if(round.getStage() != Stage.Pickup && round.getStage() != Stage.Starting) {
			throw new RuntimeException("It is currently not pickup phase.");
		}
		tiles.addMahjongTile(tile);
		hasPickup = true;
		round.fireUpdate(new MahjongUpdate(tile, from, tiles));
		round.setStage(Stage.Discard);
	}
	public synchronized void discard(MahjongTile tile) {
		if(round.getStage() != Stage.Discard) {
			throw new RuntimeException("It is currently not discard phase.");
		}
		tiles.removeMahjongTile(tile);
		discards.addMahjongTile(tile);
		hasPickup = false;
		round.fireUpdate(new MahjongUpdate(tile, tiles, discards));
		round.setStage(Stage.Pickup);
	}
	public synchronized List<MahjongTile> getAllTiles() {
		List<MahjongTile> allTiles = new ArrayList<MahjongTile>();
		allTiles.addAll(tiles.getList());
		for(TileHolder groupsLists : formedGroups) {
			allTiles.addAll(groupsLists.getList());
		}
		allTiles.addAll(discards.getList());
		return Collections.unmodifiableList(allTiles);
	}
	public MahjongTile getTile(String description) {
		for(MahjongTile handTile : getAllTiles()) {
			if(handTile.getDescription().equals(description)) {
				return handTile;
			}
		}
		return null;
	}
	public Wind getWind() {
		return wind;
	}
	public TileHolder getTiles() {
		return tiles;
	}
	public TileHolder getDiscards() {
		return discards;
	}
	public MahjongTile getLastDiscard() {
		return discards.getList().get(discards.getList().size() - 1);
	}

	public boolean isHasPickup() {
		return hasPickup;
	}

	public void setHasPickup(boolean hasPickup) {
		this.hasPickup = hasPickup;
	}
}
