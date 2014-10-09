package net.gillessed.tablemahjong.user.local;

import net.gillessed.tablemahjong.threadspace.MainThreadSpace;
import net.gillessed.tablemahjong.user.AbstractUser;

import org.json.JSONException;
import org.json.JSONObject;

public class LocalUser extends AbstractUser {

	public LocalUser(String name, MainThreadSpace mainThreadSpace) {
		super(name, mainThreadSpace);
	}

	@Override
	public void initResponseHandler() {
		responseHandler = new LocalServerResponseHandler(mainThreadSpace, this);
	}

	@Override
	public JSONObject getDefaultGameOptions() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("creator", name);
			obj.put("rule-set", "japanese");
			obj.put("starting-money", 25000);
			obj.put("description", "");
			obj.put("status", "Open");
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return obj;
	}
}
