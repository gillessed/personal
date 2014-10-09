package net.gillessed.tablemahjong.server.packet;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class Packet {
	
	private static Map<String, PacketType> packetMap = new HashMap<String, PacketType>();
	
	public static int ATTEMPT = 100;
	public static int SUCCESS = 101;
	public static int FAILURE = 102;
	
	public enum PacketType {
		LOGIN("login"),
		CHAT("chat"),
		MAINROOMACTION("mainroomaction"),
		CREATEROOMACTION("createroomaction"),
		GAMEROOMACTION("gameroomaction"),
		MAHJONGACTION("mahjongaction"),
		LOGOUT("logout"),
		KILL("kill"),
		DEBUG("debug"),
		SIMULATE("simulate");
		
		private final String name;

		PacketType(String name) {
			this.name = name;
			packetMap.put(name, this);
		}
		
		public static PacketType get(String name) {
			if(packetMap.containsKey(name)) {
				return packetMap.get(name);
			}
			return null;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	private final PacketType packetType;
	private final int statusCode;
	private final JSONObject message;
	
	public Packet(PacketType packetType, int statusCode, JSONObject message) {
		this.packetType = packetType;
		this.statusCode = statusCode;
		this.message = message;
	}
	
	public Packet(String definition) {
		try {
			JSONObject obj = new JSONObject(definition);
			packetType = PacketType.get(obj.getString("packet-type"));
			statusCode = obj.getInt("status-code");
			message = obj.getJSONObject("message");
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public String toString() {
		try {
			JSONObject obj = new JSONObject();
			obj.put("packet-type", getPacketType());
			obj.put("status-code", getStatusCode());
			obj.put("message", message);
			return obj.toString();
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getMessageString(String key) {
		try {
			return message.getString(key);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
	
	public int getMessageInt(String key) {
		try {
			return message.getInt(key);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public int getStatusCode() {
		return statusCode;
	}

	public PacketType getPacketType() {
		return packetType;
	}

	public JSONObject getMessage() {
		return message;
	}
}
