package net.gillessed.tablemahjong.mahjonggame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.gillessed.tablemahjong.mahjonggame.MahjongGame.Wind;
import net.gillessed.tablemahjong.mahjongupdate.MahjongUpdate;
import net.gillessed.tablemahjong.mahjongupdate.MahjongUpdate.UpdateTargetType;
import net.gillessed.tablemahjong.mahjongupdate.TileHolder;

public abstract class MahjongRound {
	public enum Stage {
		Starting,
		Pickup,
		Discard,
		Done;
	}
	
	protected final List<MahjongUpdate> events;
	protected UpdateTargetType type;
	
	protected TileHolder wallTileHolder;
	protected Map<Wind, MahjongHand> hands;
	protected  Set<MahjongPlayer> players;
	protected final Map<String, TileHolder> tileHolders;
	protected final int roundNumber;
	protected final int bonusNumber;
	protected final Wind prevailingWind;
	protected final Wind dealer;
	
	protected Stage stage;
	protected Wind currentWind;
	
	public MahjongRound(int roundNumber, int bonusNumber) {
		this.roundNumber = roundNumber;
		this.bonusNumber = bonusNumber;
		tileHolders = new HashMap<String, TileHolder>();
		dealer = Wind.East;
		if(roundNumber >= 1 && roundNumber <= 4) {
			prevailingWind = Wind.East;
		} else if(roundNumber >= 5 && roundNumber <= 8){
			prevailingWind = Wind.South;
		} else {
			throw new RuntimeException("Round number should be within 1 and 8!");
		}
		players = new HashSet<MahjongPlayer>();
		hands = new HashMap<Wind, MahjongHand>();
		events = new ArrayList<MahjongUpdate>();
		setStage(Stage.Starting);
		currentWind = dealer;
		setup();
	}
	
	public MahjongRound(int roundNumber, int bonusNumber, String wallTileHolderUUID, List<MahjongTile> mahjongTiles) {
		this(roundNumber, bonusNumber);
		wallTileHolder = new TileHolder(wallTileHolderUUID);
		for(MahjongTile mt : mahjongTiles) {
			wallTileHolder.addMahjongTile(mt);
		}
		registerTileHolder(wallTileHolder);
	}
	
	public Wind getCurrentWind() {
		return currentWind;
	}
	protected MahjongTile removeWallTop() {
		return wallTileHolder.getList().remove(wallTileHolder.getList().size() - 1);
	}
	protected void registerTileHolder(TileHolder th) {
		tileHolders.put(th.getUuid(), th);
	}
	public void setHands(Map<Wind, MahjongHand> hands) {
		this.hands = hands;
	}
	public Map<Wind, MahjongHand> getHands() {
		return hands;
	}
	public Set<MahjongPlayer> getPlayers() {
		return players;
	}
	public List<MahjongTile> getWall() {
		return wallTileHolder.getList();
	}
	public int getRevisionNumber() {
		return events.size();
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Stage getStage() {
		return stage;
	}
	 
	protected MahjongTile removeWallTile(String tileToTransfer) {
		MahjongTile tileToRemove = null;
		for(MahjongTile tile : wallTileHolder.getList()) {
			if(tile.getDescription().equals(tileToTransfer)) {
				tileToRemove = tile;
			}
		}
		wallTileHolder.removeMahjongTile(tileToRemove);
		return tileToRemove;
	}

	public int getRoundNumber() {
		return roundNumber;
	}

	public int getBonusNumber() {
		return bonusNumber;
	}

	public Wind getPrevailingWind() {
		return prevailingWind;
	}
	
	public void setPlayers(Set<MahjongPlayer> players) {
		this.players = players;
	}

	public UpdateTargetType getType() {
		return type;
	}

	public void setType(UpdateTargetType type) {
		this.type = type;
	}
	
	public void performMahjongUpdate(MahjongUpdate mahjongUpdate) {
		TileHolder fromTileHolder = tileHolders.get(mahjongUpdate.getFromUUID());
		TileHolder toTileHolder = tileHolders.get(mahjongUpdate.getToUUID());
		MahjongTile tile = fromTileHolder.getTile(mahjongUpdate.getDescription());
		fromTileHolder.removeMahjongTile(tile);
		toTileHolder.addMahjongTile(tile);
	}
	
	public abstract void fireUpdate(MahjongUpdate mahjongUpdate);
	
	public abstract void setup();
}
