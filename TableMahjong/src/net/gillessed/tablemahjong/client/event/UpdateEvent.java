package net.gillessed.tablemahjong.client.event;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;


public class UpdateEvent {
	public enum UpdateType {
		USERJOIN("user"),
		USERQUIT("user"),
		CHAT("message"),
		KILL(),
		CREATEGAME,
		FINALIZEGAME,
		JOINCREATEGAME,
		QUITCREATEGAME,
		SIZEUPATE,
		STARTGAME,
		GAMEACTION,
		QUITGAME,
		STEALMESSAGE,
		MESSAGE,
		FREEZE,
		NEWROUND;
		
		private final List<String> dataKeys;
		
		UpdateType(String... data) {
			dataKeys = new ArrayList<String>();
			for(String s : data) {
				getDataKeys().add(s);
			}
		}

		public List<String> getDataKeys() {
			return dataKeys;
		}
	}
	private final UpdateType type;
	private final JSONObject eventData;
	public UpdateEvent(UpdateType type, JSONObject eventData) {
		this.type = type;
		this.eventData = eventData;
	}
	public UpdateType getType() {
		return type;
	}
	public JSONObject getEventData() {
		return eventData;
	}
}
