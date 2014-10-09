package net.gillessed.tablemahjong.threadspace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.gillessed.tablemahjong.server.TableMahjongServer;
import net.gillessed.tablemahjong.server.logging.Logger;
import net.gillessed.tablemahjong.server.packet.Packet;
import net.gillessed.tablemahjong.server.packet.Packet.PacketType;
import net.gillessed.tablemahjong.user.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainThreadSpace implements ThreadSpace {
	
	private final String uuid;
	private final Map<String, User> users;
	private final Map<String, CreateGameThreadSpace> createGameThreadSpaces;
	private final Map<String, GameThreadSpace> gameThreadSpaces;
	private final TableMahjongServer tableMahjongServer;
	
	public MainThreadSpace(String uuid, TableMahjongServer tableMahjongServer) {
		this.uuid = uuid;
		this.tableMahjongServer = tableMahjongServer;
		users = new HashMap<String, User>();
		createGameThreadSpaces = new HashMap<String, CreateGameThreadSpace>();
		gameThreadSpaces = new HashMap<String, GameThreadSpace>();
	}
	
	public synchronized void updateMainChat(String message, String username) {
		for(User user : users.values()) {
			JSONObject obj = new JSONObject();
			try {
				obj.put("room", uuid);
				obj.put("message", message);
				obj.put("from", username);
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
			Packet messagePacket = new Packet(PacketType.CHAT, Packet.SUCCESS, obj);
			user.sendPacket(messagePacket);
		}
	}
	
	public String getUUID() {
		return uuid;
	}

	public Map<String, User> getUsers() {
		return users;
	}

	public synchronized void addUser(User user) {

		users.put(user.getName(), user);
		user.getUserUpdaterThread().start();
		
    	JSONObject object = new JSONObject();
    	JSONArray usernamesJSON = new JSONArray();
    	JSONArray createGameRoomsJSON = new JSONArray();
    	try {
    		for(User u : users.values()) {
    			JSONObject userJSON = new JSONObject();
    			userJSON.put("username", u.getName());
    			usernamesJSON.put(userJSON);
    		}
    		for(CreateGameThreadSpace cgts : createGameThreadSpaces.values()) {
    			if(cgts.isFinalized()) {
	    			JSONObject cgrJSON = new JSONObject();
	    			cgrJSON.put("room", cgts.getUUID());
	    			cgrJSON.put("bean", cgts.getBean().getJSON());
	    			createGameRoomsJSON.put(cgrJSON);
    			}
    		}
			object.put("main-room-uuid", uuid);
			object.put("user-array", usernamesJSON);
			object.put("open-challenges", createGameRoomsJSON);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
    	Packet roomJoin = new Packet(PacketType.MAINROOMACTION, Packet.ATTEMPT, object);
    	user.sendPacket(roomJoin);
    	
		for(User u : users.values()) {
			if(u != user) {
				JSONObject obj = new JSONObject();
				try {
					obj.put("room", "main-room");
					obj.put("action", "join");
					obj.put("username", user.getName());
				} catch (JSONException e) {
					throw new RuntimeException(e);
				}
				roomJoin = new Packet(PacketType.MAINROOMACTION, Packet.SUCCESS, obj);
				u.sendPacket(roomJoin);
			}
		}
	}
	
	public synchronized void removeUser(String username) {

		User userToRemove = users.remove(username);
		userToRemove.getUserUpdaterThread().kill();
		
		Logger.getLogger().info("Logout suceeded from " + username);
    	
		for(User u : users.values()) {
			JSONObject obj = new JSONObject();
			try {
				obj.put("username", username);
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
			Packet logoutPacket = new Packet(PacketType.LOGOUT, Packet.ATTEMPT, obj);
			u.sendPacket(logoutPacket);
		}
	}

	public void kill() {
		for(User u : users.values()) {
			JSONObject obj = new JSONObject();
			Packet logoutPacket = new Packet(PacketType.KILL, Packet.SUCCESS, obj);
			u.sendPacket(logoutPacket);
			u.getUserUpdaterThread().kill();
		}
	}

	public TableMahjongServer getTableMahjongServer() {
		return tableMahjongServer;
	}

	public void updateChat(Packet packet) {
		JSONObject obj = packet.getMessage();
		try {
			String room = obj.getString("room");
			String message = obj.getString("message");
			String username = obj.getString("from");
			if(uuid.equals(room)) {
				updateMainChat(message, username);
			}
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public void createCreateGameThreadSpace(Packet packet) {
		User creator = users.get(packet.getMessageString("creator"));
		Logger.getLogger().info("Game created by " + creator.getName());
		CreateGameThreadSpace createGameThreadSpace = new CreateGameThreadSpace(this, UUID.randomUUID().toString(), creator);
		createGameThreadSpaces.put(createGameThreadSpace.getUUID(), createGameThreadSpace);
		
		CreateGameRoomBean bean = new CreateGameRoomBean(creator.getDefaultGameOptions());
		
		JSONObject obj = new JSONObject();
		try {
			obj.put("action", "open-window");
			obj.put("room", createGameThreadSpace.getUUID());
			obj.put("bean", bean.getJSON());
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		creator.sendPacket(new Packet(PacketType.CREATEROOMACTION, Packet.SUCCESS, obj));
	}

	public void finalizeCreateGameThreadSpace(Packet packet) {
		try {
			String uuid = packet.getMessage().getString("room");
			CreateGameRoomBean bean = new CreateGameRoomBean(packet.getMessage().getJSONObject("bean"));
			createGameThreadSpaces.get(uuid).setBean(bean);
			createGameThreadSpaces.get(uuid).setFinalized(true);
			JSONObject obj = new JSONObject();
			obj.put("action", "finalize");
			obj.put("room", uuid);
			obj.put("bean", bean.getJSON());
			Packet finalizePacket = new Packet(PacketType.CREATEROOMACTION, Packet.SUCCESS, obj);
			for(User u : users.values()) {
				u.sendPacket(finalizePacket);
			}
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public void killCreateGameThreadSpace(Packet packet) {
		String room = packet.getMessageString("room");
		CreateGameThreadSpace createGameThreadSpace = createGameThreadSpaces.remove(room);
		if(createGameThreadSpace.isFinalized()) {
			JSONObject obj = new JSONObject();
			try {
				CreateGameRoomBean bean = new CreateGameRoomBean(packet.getMessage().getJSONObject("bean"));
				obj.put("action", "kill");
				obj.put("room", room);
				obj.put("bean", bean.getJSON());
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
			Packet killPacket = new Packet(PacketType.CREATEROOMACTION, Packet.SUCCESS, obj);
			for(User u : users.values()) {
				u.sendPacket(killPacket);
			}
		}
	}

	public void addUserToCreateGameThreadSpace(Packet packet, User user) {
		String uuid = packet.getMessageString("room");
		createGameThreadSpaces.get(uuid).addUser(user);
	}

	public void removeUserFromCreateGameThreadSpace(Packet packet, User user) {
		String uuid = packet.getMessageString("room");
		createGameThreadSpaces.get(uuid).removeUser(user);
	}

	public void debug(Packet packet) throws JSONException {
		Logger logger = Logger.getLogger();
		JSONArray debugInfo;
		debugInfo = packet.getMessage().getJSONArray("debug-info");
		logger.info("*** Start Server Debug ***");
		for(int i = 0; i < debugInfo.length(); i++) {
			String debugString = debugInfo.getString(i);
			if("main-thread-space".equalsIgnoreCase(debugString)) {
				logger.info("=== Main Thread Space: ===");
				logger.info("   === Users (" + users.size() + "): ===");
				for(User u : users.values()) {
					logger.info("      " + u.getName());
				}
				logger.info("   === # of Create Game Thread Spaces (" + createGameThreadSpaces.size() + "): ===");
				for(CreateGameThreadSpace sp : createGameThreadSpaces.values()) {
					logger.info("      " + sp.getUUID());
				}
				logger.info("   === # of Game Thread Spaces (" + gameThreadSpaces.size() + "): ===");
				for(GameThreadSpace sp : gameThreadSpaces.values()) {
					logger.info("      " + sp.getUUID());
				}
			} else if("create-game-thread-spaces".equalsIgnoreCase(debugString)) {
				for(CreateGameThreadSpace sp : createGameThreadSpaces.values()) {
					sp.debug();
				}
			} else if("game-thread-spaces".equalsIgnoreCase(debugString)) {
				for(GameThreadSpace sp : gameThreadSpaces.values()) {
					sp.debug();
				}
			}
		}
		logger.info("*** End Server Debug ***");
	}

	public void startGame(Packet packet) throws JSONException {
		String roomUuid = packet.getMessageString("room");
		CreateGameThreadSpace cgt = createGameThreadSpaces.remove(roomUuid);
		cgt.sendCloseMessage();
		List<User> players = new ArrayList<User>();
		for(int i = 1; i <= 4; i++) {
			String player = (String)packet.getMessage().get("player"+i);
			players.add(users.get(player));
		}
		List<User> joiners = new ArrayList<User>();
		JSONArray joinersJSON = packet.getMessage().getJSONArray("joiners");
		for(int i = 0; i < joinersJSON.length(); i++) {
			joiners.add(users.get(joinersJSON.getString(i)));
		}
		CreateGameRoomBean bean = new CreateGameRoomBean(packet.getMessage().getJSONObject("bean"));
		for(User u : users.values()) {
			JSONObject obj = new JSONObject();
			obj.put("bean", bean.getJSON());
			obj.put("room", cgt.getUUID());
			obj.put("action", "transition");
			u.sendPacket(new Packet(PacketType.CREATEROOMACTION, Packet.SUCCESS, obj));
		}
		GameThreadSpace space = new GameThreadSpace(UUID.randomUUID().toString(), players, joiners, bean);
		gameThreadSpaces.put(space.getUUID(), space);
		space.start();
		for(User u : users.values()) {
			JSONObject obj = new JSONObject();
			obj.put("bean", bean.getJSON());
			obj.put("room", cgt.getUUID());
			obj.put("action", "transition");
			u.sendPacket(new Packet(PacketType.GAMEROOMACTION, Packet.SUCCESS, obj));
		}
	}

	public void simulate(Packet packet) {
		String type = packet.getMessageString("type");
		if("game".equals(type)) {
			String creator = packet.getMessageString("creator");
			User gameCreator = users.get(creator);
			
			List<String> tempPlayers = new ArrayList<String>();
			tempPlayers.add(creator);
			tempPlayers.add("DEBUG-player1");
			tempPlayers.add("DEBUG-player2");
			tempPlayers.add("DEBUG-player3");

			List<String> tempObservers= new ArrayList<String>();
			tempObservers.add("DEBUG-observer1");
			tempObservers.add("DEBUG-observer2");
			tempObservers.add("DEBUG-observer3");
			
			JSONObject obj = new JSONObject();
			try {
				obj.put("action", "create");
				obj.put("room", UUID.randomUUID().toString());
				obj.put("status", "player");
				obj.put("players", tempPlayers);
				obj.put("observers", tempObservers);
				CreateGameRoomBean bean = new CreateGameRoomBean(creator,
						"Japanese Rules", 
						20000,
						"hi", "started");
				obj.put("bean", bean.getJSON());
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
			gameCreator.sendPacket(new Packet(PacketType.GAMEROOMACTION, Packet.SUCCESS, obj));
			
			gameCreator.sendPacket(packet);
		}
	}

	public Map<String, CreateGameThreadSpace> getCreateGameThreadSpaces() {
		return Collections.unmodifiableMap(createGameThreadSpaces);
	}

	public Map<String, GameThreadSpace> getGameThreadSpaces() {
		return Collections.unmodifiableMap(gameThreadSpaces);
	}
}
