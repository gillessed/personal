package net.gillessed.tablemahjong.mahjonggame;

import net.gillessed.tablemahjong.mahjonggame.MahjongGame.Wind;

import org.json.JSONException;
import org.json.JSONObject;

public class MahjongPlayer {
	private final Wind associatedHand;
	private final String name;
	private int money;

	public MahjongPlayer(String name, int money, Wind associatedHand) {
		this.name = name;
		this.money = money;
		this.associatedHand = associatedHand;
	}
	public MahjongPlayer(JSONObject source)  throws JSONException {
		associatedHand = Wind.getWind(source.getString("wind"));
		name = source.getString("name");
		money = source.getInt("money");
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getMoney() {
		return money;
	}
	public String getName() {
		return name;
	}
	public Wind getAssociatedHand() {
		return associatedHand;
	}
	public JSONObject toJSON() throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("wind", associatedHand.toString());
		obj.put("name" , name);
		obj.put("money", money);
		return obj;
	}
}
