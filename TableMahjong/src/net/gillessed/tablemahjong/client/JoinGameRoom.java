package net.gillessed.tablemahjong.client;

import net.gillessed.tablemahjong.client.event.UpdateEvent;
import net.gillessed.tablemahjong.client.event.UpdateEvent.UpdateType;
import net.gillessed.tablemahjong.server.packet.Packet;
import net.gillessed.tablemahjong.server.packet.Packet.PacketType;
import net.gillessed.tablemahjong.threadspace.CreateGameRoomBean;

import org.json.JSONException;
import org.json.JSONObject;

public class JoinGameRoom extends Updatable {

	private final String uuid;
	private final CreateGameRoomBean bean;
	private final MainRoom parent;
	
	private int waitingForPlayers;
	
	public JoinGameRoom(String uuid, MainRoom parent, CreateGameRoomBean bean, int waitingForPlayers) {
		super();
		this.bean = bean;
		this.uuid = uuid;
		this.parent = parent;
		this.waitingForPlayers = waitingForPlayers;
	}
	public void setWaitingForPlayers(int waitingForPlayers) {
		this.waitingForPlayers = waitingForPlayers;
	}
	public int getWaitingForPlayers() {
		return waitingForPlayers;
	}
	public MainRoom getParent() {
		return parent;
	}
	public String getUuid() {
		return uuid;
	}
	public CreateGameRoomBean getBean() {
		return bean;
	}
	public String getMessage() {
		if(waitingForPlayers > 1) {
			return "Waiting for " + waitingForPlayers + " players to join.";
		} else if(waitingForPlayers == 1) {
			return "Waiting for 1 player to join.";
		} else if(waitingForPlayers == 0) {
			return "Waiting for creator to start game.";
		} else {
			return "Waiting for response from game creator";
		}
	}
	public void setSize(int size) {
		waitingForPlayers = Math.max(0, 3-size);
		try {
			JSONObject obj = new JSONObject();
			obj.put("size", size);
			obj.put("message", getMessage());
			fireUpdateEvent(new UpdateEvent(UpdateType.SIZEUPATE, obj));
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
	public void leave() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("room", getUuid());
			obj.put("action", "leave");
			obj.put("bean", bean);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		getParent().getClientUpdaterThread().sendPacket(new Packet(PacketType.CREATEROOMACTION, Packet.ATTEMPT, obj));
		parent.getJoinGameRooms().remove(getUuid());
	}
	public void killFromServer() {
		parent.getJoinGameRooms().remove(getUuid());
		fireUpdateEvent(new UpdateEvent(UpdateType.KILL, new JSONObject()));
	}
	public void gameStarted() {
		parent.getJoinGameRooms().remove(getUuid());
		fireUpdateEvent(new UpdateEvent(UpdateType.KILL, new JSONObject()));
	}
}
