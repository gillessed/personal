package net.gillessed.tablemahjong.mahjonggame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import net.gillessed.tablemahjong.mahjonggame.MahjongGame.Wind;
import net.gillessed.tablemahjong.mahjongupdate.MahjongUpdate;
import net.gillessed.tablemahjong.mahjongupdate.TileHolder;
import net.gillessed.tablemahjong.mahjongupdate.UpdateTarget;
import net.gillessed.tablemahjong.mahjongupdate.MahjongUpdate.UpdateTargetType;
import net.gillessed.tablemahjong.server.logging.Logger;
import net.gillessed.tablemahjong.server.packet.Packet;
import net.gillessed.tablemahjong.server.packet.Packet.PacketType;
import net.gillessed.tablemahjong.stealnotice.NoticeThread;
import net.gillessed.tablemahjong.threadspace.GameThreadSpace;
import net.gillessed.tablemahjong.user.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerMahjongRound extends MahjongRound {

	private UpdateTarget serverListener;
	private final GameThreadSpace gameThreadSpace;
	private NoticeThread noticeThread;
	
	public ServerMahjongRound(GameThreadSpace gameThreadSpace, int roundNumber, int bonusNumber, Set<MahjongPlayer> players) {
		super(roundNumber, bonusNumber);
		this.gameThreadSpace = gameThreadSpace;
		for(MahjongPlayer mp : players) {
			this.players.add(mp);
		}
	}
	
	public void sendStealNotice() {
		List<User> users = getGameThreadSpace().getPlayers();
		final Map<Wind, User> userWindMap = new HashMap<Wind, User>();
		for(MahjongPlayer player : players) {
			for(User user : users) {
				if(user.getName().equals(player.getName())) {
					userWindMap.put(player.getAssociatedHand(), user);
				}
			}
		}
		freezeUsers(true);
		noticeThread = new NoticeThread(this, userWindMap);
		noticeThread.start();
	}

	public void freezeUsers(boolean b) {
		for(User user : getGameThreadSpace().getPlayers()) {
			JSONObject obj = new JSONObject();
			try {
				obj.put("room", getGameThreadSpace().getUUID());
				obj.put("action", "freeze");
				obj.put("freeze", b);
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
			user.sendPacket(new Packet(PacketType.GAMEROOMACTION, Packet.ATTEMPT, obj));
		}
	}

	public void setServerListener(UpdateTarget serverListener) {
		this.serverListener = serverListener;
	}

	public UpdateTarget getServerListener() {
		return serverListener;
	}
	
	@Override
	public void setStage(Stage stage) {
		Stage previousStage = getStage();
		super.setStage(stage);
		if(stage == Stage.Pickup && previousStage == Stage.Discard) {
			sendStealNotice();
		} else if(stage == Stage.Discard && previousStage == Stage.Pickup) {
			getCurrentWind();
		}
	}
	
	public void stealChosen(JSONObject obj) {
		//TODO: This should check for is anything is stolen.
		noticeThread.notifyWaiter();
	}

	public GameThreadSpace getGameThreadSpace() {
		return gameThreadSpace;
	}


	@Override
	public void setup() {
		wallTileHolder = new TileHolder();
		registerTileHolder(wallTileHolder);
		createDeck();
	}
	
	/**
	 * Makes the wall, effectively reshuffling all the tiles back into it, and creates empty hands.
	 */
	public void createDeck() {
		List<MahjongTile> deck = new ArrayList<MahjongTile>();
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 4; j++) {
				deck.add(new MahjongTile("bamboo-" + (i+1) + "-" + (j+1)));
				deck.add(new MahjongTile("pin-" + (i+1) + "-" + (j+1)));
				deck.add(new MahjongTile("man-" + (i+1) + "-" + (j+1)));
			}
		}
		for(int i = 0; i < 4; i++) {
			deck.add(new MahjongTile("wind-east-" + (i+1)));
			deck.add(new MahjongTile("wind-north-" + (i+1)));
			deck.add(new MahjongTile("wind-west-" + (i+1)));
			deck.add(new MahjongTile("wind-south-" + (i+1)));
		}
		for(int i = 0; i < 4; i++) {
			deck.add(new MahjongTile("dragon-chun-" + (i+1)));
			deck.add(new MahjongTile("dragon-green-" + (i+1)));
			deck.add(new MahjongTile("dragon-haku-" + (i+1)));
		}
		wallTileHolder.getList().clear();
		Random r = new Random();
		int deckSize = deck.size();
		for(int i = 0; i < deckSize; i++) {
			MahjongTile mt = deck.remove(r.nextInt(deckSize - i));
			wallTileHolder.addMahjongTile(mt);
		}
		MahjongHand eastHand = new MahjongHand(this, Wind.East);
		hands.put(Wind.East, eastHand);
		MahjongHand northHand = new MahjongHand(this, Wind.North);
		hands.put(Wind.North, northHand);
		MahjongHand westHand = new MahjongHand(this, Wind.West);
		hands.put(Wind.West, westHand);
		MahjongHand southHand = new MahjongHand(this, Wind.South);
		hands.put(Wind.South, southHand);
	}
	
	/**
	 * Deals each of the 13 tiles to each of the four hands, and also includes the 
	 */
	public void deal() {
		MahjongHand eastHand = hands.get(Wind.East);
		for(int j = 0; j < 13; j++) {
			MahjongTile mt = removeWallTop();
			eastHand.put(mt, wallTileHolder);
		}
		MahjongHand northHand = hands.get(Wind.North);
		for(int j = 0; j < 13; j++) {
			MahjongTile mt = removeWallTop();
			northHand.put(mt, wallTileHolder);
		}
		MahjongHand westHand = hands.get(Wind.West);
		for(int j = 0; j < 13; j++) {
			MahjongTile mt = removeWallTop();
			westHand.put(mt, wallTileHolder);
		}
		MahjongHand southHand = hands.get(Wind.South);
		for(int j = 0; j < 13; j++) {
			MahjongTile mt = removeWallTop();
			southHand.put(mt, wallTileHolder);
		}
		hands.get(dealer).pickup(removeWallTop(), wallTileHolder);
		setStage(Stage.Discard);
	}

	@Override
	public void fireUpdate(MahjongUpdate mahjongUpdate) {
		switch(getType()) {
		case BOTH: 
			serverListener.update(mahjongUpdate);
			break;
		case SERVER:
			serverListener.update(mahjongUpdate);
			break;
		case UI:
		case NONE:
			break;
		}
	}

	public JSONObject getRoundJSON() {
		JSONObject roundJSONObject = new JSONObject();
		try {
			roundJSONObject.put("wall-tile-holder", wallTileHolder.getUuid());
			for(Entry<Wind, MahjongHand> mh : hands.entrySet()) {
				JSONObject handObj = new JSONObject();
				handObj.put("hand", mh.getValue().getTiles().getUuid());
				handObj.put("discards", mh.getValue().getDiscards().getUuid());
				roundJSONObject.put("hand-" + mh.getKey().toString(), handObj);
			}
			for(MahjongPlayer mp : players) {
				roundJSONObject.put("player-" + mp.getAssociatedHand().toString(), mp.toJSON());
			}
			JSONArray tileArray = new JSONArray();
			for(MahjongTile mt : wallTileHolder.getList()) {
				tileArray.put(mt.toJSON());
			}
			roundJSONObject.put("wall", tileArray);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return roundJSONObject;
	}
	
	public void updateFromClient(MahjongUpdate mahjongUpdate) {
		Logger.getLogger().dev("Server received MahjongUpdate: " + mahjongUpdate.toJSON().toString());
		setType(UpdateTargetType.SERVER);
		performMahjongUpdate(mahjongUpdate);
		fireUpdate(mahjongUpdate);
	}
}
