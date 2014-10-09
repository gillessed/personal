package net.gillessed.tablemahjong.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.gillessed.tablemahjong.client.event.UpdateEvent;
import net.gillessed.tablemahjong.client.event.UpdateEvent.UpdateType;
import net.gillessed.tablemahjong.mahjonggame.ClientMahjongRound;
import net.gillessed.tablemahjong.mahjonggame.MahjongGame.Wind;
import net.gillessed.tablemahjong.mahjonggame.MahjongHand;
import net.gillessed.tablemahjong.mahjonggame.MahjongPlayer;
import net.gillessed.tablemahjong.mahjonggame.MahjongTile;
import net.gillessed.tablemahjong.mahjongupdate.MahjongUpdate;
import net.gillessed.tablemahjong.server.packet.Packet;
import net.gillessed.tablemahjong.stealnotice.StealNotice;
import net.gillessed.tablemahjong.threadspace.CreateGameRoomBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GameRoom extends Updatable {
	
	private final String uuid;
	private final List<String> players;
	private final List<String> observers;
	private final List<String> chat;
	private ClientMahjongRound round;
	private CreateGameRoomBean bean;
	private boolean isObserver;
	
	public GameRoom(MainRoom parent, CreateGameRoomBean bean, String uuid, JSONArray playersJSON, JSONArray observersJSON, boolean isObserver) throws JSONException {
		this.bean = bean;
		this.uuid = uuid;
		players = new ArrayList<String>();
		observers = new ArrayList<String>();
		chat = new ArrayList<String>();
		
		for(int i = 0; i < playersJSON.length(); i++) {
			players.add(playersJSON.getString(i));
		}
		
		for(int i = 0; i < observersJSON.length(); i++) {
			observers.add(observersJSON.getString(i));
		}
	}

	public List<String> getPlayers() {
		return players;
	}

	public List<String> getObservers() {
		return observers;
	}

	public void setBean(CreateGameRoomBean bean) {
		this.bean = bean;
	}

	public CreateGameRoomBean getBean() {
		return bean;
	}

	public ClientMahjongRound getRound() {
		return round;
	}

	public String getUuid() {
		return uuid;
	}

	public List<String> getChat() {
		return chat;
	}

	public void setObserver(boolean isObserver) {
		this.isObserver = isObserver;
	}

	public boolean isObserver() {
		return isObserver;
	}
	
	public void setMahjongRound(ClientMahjongRound round) {
		this.round = round;
		fireUpdateEvent(new UpdateEvent(UpdateType.NEWROUND, null));
	}

	public void message(String message) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("message", message);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		fireUpdateEvent(new UpdateEvent(UpdateType.MESSAGE, obj));
	}

	public void serverUpdate(MahjongUpdate mahjongUpdate) {
		round.updateFromServer(mahjongUpdate);
	}

	public void stealNotice(Wind wind) {
		MahjongTile lastDiscard = round.getHands().get(round.getCurrentWind()).getLastDiscard();
		List<MahjongTile> handToCheck = round.getHands().get(wind).getTiles().getList();
		boolean isRunAllowed = (1 == ((round.getCurrentWind().ordinal() - wind.ordinal() + 4) % 4));
		StealNotice stealNotice = new StealNotice(lastDiscard, handToCheck, isRunAllowed);
		try {
			fireUpdateEvent(new UpdateEvent(UpdateType.STEALMESSAGE, stealNotice.toJSON()));
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public void freezeUsers(JSONObject freeze) {
		fireUpdateEvent(new UpdateEvent(UpdateType.FREEZE, freeze));
	}

	public void createRound(Packet packet) {
		try {
			int roundNumber = packet.getMessageInt("round-number");
			int bonusNumber = packet.getMessageInt("bonus-number");
			JSONObject tileHolderJSONObject;
				tileHolderJSONObject = packet.getMessage().getJSONObject("round");
			JSONArray wallArray = tileHolderJSONObject.getJSONArray("wall");
			List<MahjongTile> tiles = new ArrayList<MahjongTile>();
			for(int i = 0; i < wallArray.length(); i++) {
				tiles.add(new MahjongTile(wallArray.getJSONObject(i)));
			}
			ClientMahjongRound mahjongRound = new ClientMahjongRound(roundNumber, bonusNumber,
					tileHolderJSONObject.getString("wall-tile-holder"), tiles);
			Map<Wind, MahjongHand> mahjongHands = new HashMap<Wind, MahjongHand>();
			for(Wind wind : Wind.values()) {
				JSONObject handJSONObject = tileHolderJSONObject.getJSONObject("hand-" + wind.toString());
				MahjongHand mh = new MahjongHand(mahjongRound, wind,
						handJSONObject.getString("hand"), handJSONObject.getString("discards"));
				mahjongHands.put(wind, mh);
			}
			mahjongRound.setHands(mahjongHands);
			Set<MahjongPlayer> players = new HashSet<MahjongPlayer>();
			for(Wind wind : Wind.values()) {
				JSONObject playerJSONObject = tileHolderJSONObject.getJSONObject("player-" + wind.toString());
				players.add(new MahjongPlayer(playerJSONObject));
			}
			mahjongRound.setPlayers(players);
			setMahjongRound(mahjongRound);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
}
