package net.gillessed.tablemahjong.threadspace;

import java.util.ArrayList;
import java.util.List;

import net.gillessed.tablemahjong.server.logging.Logger;
import net.gillessed.tablemahjong.server.packet.Packet;
import net.gillessed.tablemahjong.server.packet.Packet.PacketType;
import net.gillessed.tablemahjong.user.User;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateGameThreadSpace implements ThreadSpace {
	
	private final String uuid;
	private final User creator;
	private final List<User> users;
	private final MainThreadSpace parent;
	private CreateGameRoomBean bean;
	private boolean finalized;
	
	public CreateGameThreadSpace(MainThreadSpace parent, String uuid, User creator) {
		this.parent = parent;
		users = new ArrayList<User>();
		this.uuid = uuid;
		this.creator = creator;
		finalized = false;
	}
	
	public String getUUID() {
		return uuid;
	}

	public void setBean(CreateGameRoomBean bean) {
		this.bean = bean;
	}

	public CreateGameRoomBean getBean() {
		return bean;
	}

	public void setFinalized(boolean finalized) {
		this.finalized = finalized;
	}

	public boolean isFinalized() {
		return finalized;
	}

	public User getCreator() {
		return creator;
	}

	public List<User> getUsers() {
		return users;
	}

	public MainThreadSpace getParent() {
		return parent;
	}

	public void addUser(User user) {
		users.add(user);
		JSONObject obj = new JSONObject();
		try {
			obj.put("bean", getBean().getJSON());
			obj.put("username", user.getName());
			obj.put("room", getUUID());
			obj.put("action", "user-joined");
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		creator.sendPacket(new Packet(PacketType.CREATEROOMACTION, Packet.SUCCESS, obj));

		obj = new JSONObject();
		try {
			obj.put("bean", getBean().getJSON());
			obj.put("size", users.size());
			obj.put("room", getUUID());
			obj.put("action", "join");
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		user.sendPacket(new Packet(PacketType.CREATEROOMACTION, Packet.SUCCESS, obj));
		
		sendSizeUpdate();
	}

	public void removeUser(User user) {
		users.remove(user);
		JSONObject obj = new JSONObject();
		try {
			obj.put("bean", getBean().getJSON());
			obj.put("username", user.getName());
			obj.put("room", getUUID());
			obj.put("action", "user-left");
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		creator.sendPacket(new Packet(PacketType.CREATEROOMACTION, Packet.SUCCESS, obj));
		
		sendSizeUpdate();
	}

	private void sendSizeUpdate() {
		JSONObject obj;
		for(User u : users) {
			obj = new JSONObject();
			try {
				obj.put("bean", getBean().getJSON());
				obj.put("size", users.size());
				obj.put("room", getUUID());
				obj.put("action", "size-update");
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
			u.sendPacket(new Packet(PacketType.CREATEROOMACTION, Packet.SUCCESS, obj));
		}
	}

	public void debug() {
		Logger logger = Logger.getLogger();
		logger.info("=== " + uuid + " ===");
		logger.info("   === Creator: ===");
		logger.info("      " + creator.getName());
		logger.info("   === Joiners (" + users.size() + "): ===");
		for(User u : users) {
			logger.info("      " + u.getName());
		}
		logger.info("   === Finalized: ===");
		logger.info("      " + finalized);
		if(finalized) {
			bean.debug();
		}
	}

	public void sendCloseMessage() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("room", uuid);
			obj.put("action", "close");
			obj.put("bean", bean.getJSON());
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		for(User u : users) {
			u.sendPacket(new Packet(PacketType.CREATEROOMACTION, Packet.SUCCESS, obj));
		}
	}
}
