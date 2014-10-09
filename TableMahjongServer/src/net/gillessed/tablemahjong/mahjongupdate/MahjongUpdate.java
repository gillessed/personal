package net.gillessed.tablemahjong.mahjongupdate;

import net.gillessed.tablemahjong.mahjonggame.MahjongTile;

import org.json.JSONException;
import org.json.JSONObject;

public class MahjongUpdate {
	
	public enum UpdateTargetType {
		BOTH,
		SERVER,
		UI,
		NONE
	}
	
	private String tileDescription;
	private String fromUUID;
	private String toUUID;

	public MahjongUpdate(MahjongTile tile, TileHolder from, TileHolder to) {
		tileDescription = tile.getDescription();
		fromUUID = from.getUuid();
		toUUID = to.getUuid();
	}

	public MahjongUpdate(JSONObject obj) {
		try {
			tileDescription = obj.getString("tileDescription");
			fromUUID = obj.getString("fromUUID");
			toUUID = obj.getString("toUUID");
		} catch(JSONException e) {
			throw new RuntimeException(e);
		}
	}
	
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("tileDescription", tileDescription);
			obj.put("fromUUID", fromUUID);
			obj.put("toUUID", toUUID);
		} catch(JSONException e) {
			throw new RuntimeException(e);
		}
		return obj;
	}
	
	public String getFromUUID() {
		return fromUUID;
	}
	
	public String getToUUID() {
		return toUUID;
	}
	
	public String getDescription() {
		return tileDescription;
	}
}
