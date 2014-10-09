package net.gillessed.tablemahjong.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.gillessed.tablemahjong.client.event.UpdateEvent;
import net.gillessed.tablemahjong.client.event.UpdateEvent.UpdateType;
import net.gillessed.tablemahjong.server.packet.Packet;
import net.gillessed.tablemahjong.server.packet.Packet.PacketType;
import net.gillessed.tablemahjong.swingui.PlayerSelectionComboBoxModel;
import net.gillessed.tablemahjong.threadspace.CreateGameRoomBean;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateGameRoom extends Updatable {
	public static final String PLAYER2 = "player2";
	public static final String PLAYER3 = "player3";
	public static final String PLAYER4 = "player4";
	
	public final static int CREATED = 1;
	public final static int FINALIZED = 2;
	
	private final String uuid;
	private final CreateGameRoomBean bean;
	private final MainRoom parent;
	private final List<String> joiners;
	private int state;
	
	private final Map<String,PlayerSelectionComboBoxModel> playerModels;
	
	public CreateGameRoom(MainRoom parent, String uuid, CreateGameRoomBean bean) {
		super();
		this.parent = parent;
		this.uuid = uuid;
		this.bean = bean;
		state = CREATED;
		
		playerModels = new HashMap<String,PlayerSelectionComboBoxModel>();
		getPlayerModels().put(PLAYER2, new PlayerSelectionComboBoxModel(PLAYER2));
		getPlayerModels().put(PLAYER3, new PlayerSelectionComboBoxModel(PLAYER3));
		getPlayerModels().put(PLAYER4, new PlayerSelectionComboBoxModel(PLAYER4));
		
		joiners = new ArrayList<String>();
	}
	public CreateGameRoomBean getBean() {
		return bean;
	}
	public String getUuid() {
		return uuid;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getState() {
		return state;
	}
	public void finalizeToServer() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("action", "finalize");
			obj.put("room", uuid);
			obj.put("bean", bean.getJSON());
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		parent.getClientUpdaterThread().sendPacket(new Packet(PacketType.CREATEROOMACTION, Packet.ATTEMPT, obj));
		state = FINALIZED;
	}
	public boolean isReady() {
		return state == FINALIZED;
	}
	public void addJoiner(String username) {
		if(!joiners.contains(username)) {
			joiners.add(username);
		}
		if (getPlayer2().isEmpty()) {
			getPlayer2().addPlayer(username);
		} else if (getPlayer3().isEmpty()) {
			getPlayer3().addPlayer(username);
		} else if (getPlayer4().isEmpty()) {
			getPlayer4().addPlayer(username);
		} else {
			getPlayer2().addPlayer(username);
			getPlayer3().addPlayer(username);
			getPlayer4().addPlayer(username);
		}
		fireUpdateEvent(new UpdateEvent(UpdateType.JOINCREATEGAME, getIsReadyJSON()));
	}
	private JSONObject getIsReadyJSON() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("is-ready", isReady());
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return obj;
	}
	public PlayerSelectionComboBoxModel getPlayer2() {
		return getPlayerModels().get(PLAYER2);
	}
	public PlayerSelectionComboBoxModel getPlayer3() {
		return getPlayerModels().get(PLAYER3);
	}
	public PlayerSelectionComboBoxModel getPlayer4() {
		return getPlayerModels().get(PLAYER4);
	}
	public Map<String,PlayerSelectionComboBoxModel> getPlayerModels() {
		return playerModels;
	}
	public boolean isMagic() {
		for(PlayerSelectionComboBoxModel ps : playerModels.values()) if(!ps.isMagic()) return false;
		return true;
	}
	public int getPlayersJoined() {
		int size = 0;
		if(!getPlayer2().isEmpty()) {
			size++;
		}
		if(!getPlayer3().isEmpty()) {
			size++;
		}
		if(!getPlayer4().isEmpty()) {
			size++;
		}
		if(size == 3) {
			size += getPlayer2().getSize() - 1;
		}
		return size;
	}
	public void removeJoiner(String username) {
		if(joiners.contains(username)) {
			joiners.remove(username);
		}
		for(PlayerSelectionComboBoxModel ps : playerModels.values()) {
			if(ps.contains(username)) {
				ps.removePlayer(username);
			}
		}
		fireUpdateEvent(new UpdateEvent(UpdateType.QUITCREATEGAME, getIsReadyJSON()));
	}
	public List<String> getJoiners() {
		return joiners;
	}
}
