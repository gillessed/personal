package net.gillessed.tablemahjong.mahjonggame;

import org.json.JSONException;
import org.json.JSONObject;



public class MahjongTile {
	public enum TileType {
		Stick,
		Circle,
		TenThousand,
		Dragon,
		Wind
	};
	private final TileType type;
	private final String value;
	private final String description;
	public MahjongTile(String description) {
		this.description = description;
		String[] descs = description.split("-");
		String descType = descs[0];
		value = descs[1];
		if(descType.equals("bamboo")) {
			type = TileType.Stick;
		} else if(descType.equals("pin")) {
			type = TileType.Circle;
		} else if(descType.equals("man")) {
			type = TileType.TenThousand;
		} else if(descType.equals("dragon")) {
			type = TileType.Dragon;
		} else if(descType.equals("wind")) {
			type = TileType.Wind;
		} else {
			throw new RuntimeException("Tiles must have a type. This one doesn't conform: " + description);
		}
	}
	public MahjongTile(JSONObject obj) throws JSONException {
		this(obj.getString("description"));
	}
	public String getDescription() {
		return description;
	}
	public JSONObject toJSON() throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("description", description);
		return obj;
	}
	public TileType getType() {
		return type;
	}
	public String getValue() {
		return value;
	}
	@Override
	public String toString() {
		return description;
	}
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof MahjongTile) && (((MahjongTile)obj).getDescription().equals(description));
	}
	@Override
	public int hashCode() {
		return description.hashCode();
	}
}
