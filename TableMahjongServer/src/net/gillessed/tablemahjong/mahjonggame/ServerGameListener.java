package net.gillessed.tablemahjong.mahjonggame;

import net.gillessed.tablemahjong.mahjongupdate.MahjongUpdate;
import net.gillessed.tablemahjong.mahjongupdate.UpdateTarget;
import net.gillessed.tablemahjong.server.packet.Packet;
import net.gillessed.tablemahjong.server.packet.Packet.PacketType;
import net.gillessed.tablemahjong.threadspace.GameThreadSpace;
import net.gillessed.tablemahjong.user.User;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerGameListener implements UpdateTarget {

	private final GameThreadSpace gameThreadSpace;
	private String userFilter;

	public ServerGameListener(GameThreadSpace gameThreadSpace) {
		this.gameThreadSpace = gameThreadSpace;
		userFilter = null;
	}


	@Override
	public void update(MahjongUpdate mahjongUpdate) {
		for(User u : gameThreadSpace.getPlayers()) {
			if(!(userFilter != null && userFilter.equals(u.getName()))) {
				u.sendPacket(new Packet(PacketType.MAHJONGACTION, Packet.ATTEMPT, makeUpdateObject(mahjongUpdate)));
			}
		}
		for(User u : gameThreadSpace.getObservers()) {
			if(!(userFilter != null && userFilter.equals(u.getName()))) {
				u.sendPacket(new Packet(PacketType.MAHJONGACTION, Packet.ATTEMPT, makeUpdateObject(mahjongUpdate)));
			}
		}
		userFilter = null;
	}
	
	public JSONObject makeUpdateObject(MahjongUpdate e) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("action", "event-update");
			obj.put("room", gameThreadSpace.getUUID());
			obj.put("event", e.toJSON());
		} catch (JSONException excep) {
			throw new RuntimeException(excep);
		}
		return obj;
	}

	public void setUserFilter(String userFilter) {
		this.userFilter = userFilter;
	}
}
