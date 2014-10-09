package net.gillessed.tablemahjong.threadspace;

import net.gillessed.tablemahjong.server.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateGameRoomBean {
	public static final int PARAMS = 5;
	public String creator;
	public String ruleSet;
	public int startingMoney;
	public String description;
	public String status;
	public CreateGameRoomBean(String creator, String ruleSet, int startingMoney, String description, String status) {
		this.creator = creator;
		this.ruleSet = ruleSet;
		this.startingMoney = startingMoney;
		this.description = description;
		this.status = status;
	}
	public CreateGameRoomBean(JSONObject obj) {
		try {
			creator = obj.getString("creator");
			ruleSet = obj.getString("rule-set");
			startingMoney = obj.getInt("starting-money");
			description = obj.getString("description");
			status = obj.getString("status");
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
	public JSONObject getJSON() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("creator", creator);
			obj.put("rule-set", ruleSet);
			obj.put("starting-money", startingMoney);
			obj.put("description", description);
			obj.put("status", status);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return obj;
	}
	public void debug() {
		Logger logger = Logger.getLogger();
		logger.info("   === Bean: ===");
		logger.info("      === ruleSet=\"" + ruleSet + "\" ===");
		logger.info("      === starting-money=\"" + startingMoney + "\" ===");
		logger.info("      === description=\"" + description + "\" ===");
	}
}
