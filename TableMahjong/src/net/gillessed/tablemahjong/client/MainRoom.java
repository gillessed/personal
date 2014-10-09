package net.gillessed.tablemahjong.client;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.gillessed.tablemahjong.client.event.UpdateEvent;
import net.gillessed.tablemahjong.client.event.UpdateEvent.UpdateType;
import net.gillessed.tablemahjong.client.event.UpdateListener;
import net.gillessed.tablemahjong.client.swingui.GameTableModel;
import net.gillessed.tablemahjong.server.logging.Logger;
import net.gillessed.tablemahjong.server.packet.Packet;
import net.gillessed.tablemahjong.server.packet.Packet.PacketType;
import net.gillessed.tablemahjong.threadspace.CreateGameRoomBean;

import org.json.JSONException;
import org.json.JSONObject;

public class MainRoom extends Updatable { 
	
   private final static SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
	
	private ClientUpdaterThread clientUpdaterThread;
	
	private final String uuid;
	private final String selfUsername;
	private final List<String> usernames;
	private final List<String> chat;
	
	private final GameTableModel gameTableModel;

	private final boolean isLocalServer;
	
	private final Map<String, GameRoom> gameRooms;
	private final Map<String, CreateGameRoom> createGameRooms;
	private final Map<String, JoinGameRoom> joinGameRooms;
	
	public MainRoom(String uuid, List<String> usernames, String selfUsername, boolean isLocalServer, RoomPairMap openRooms) {
		super();
		this.uuid = uuid;
		this.usernames = usernames;
		this.selfUsername = selfUsername;
		this.isLocalServer = isLocalServer;
		
		chat = new ArrayList<String>();
		gameTableModel = new GameTableModel(openRooms);
		createGameRooms = new HashMap<String, CreateGameRoom>();
		joinGameRooms = new HashMap<String, JoinGameRoom>();
		gameRooms = new HashMap<String, GameRoom>();
		
		updateListeners = new ArrayList<UpdateListener>();
	}
	public void updateChatToServer(String message) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("room", uuid);
			obj.put("from", getSelfUsername());
			obj.put("message", message);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		clientUpdaterThread.sendPacket(new Packet(PacketType.CHAT, Packet.ATTEMPT, obj));
	}
	public void updateChatFromServer(String username, String message) {
		String finalMessage = "[";
		Date date = new Date();
		finalMessage += format.format(date).toString();
		finalMessage += "] ";
		finalMessage += username;
		finalMessage += ": ";
		finalMessage += message;
		finalMessage += "\n";
		chat.add(finalMessage);
		JSONObject obj = new JSONObject();
		try {
			obj.put("message", finalMessage);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		fireUpdateEvent(new UpdateEvent(UpdateType.CHAT, obj));
	}
	public void addUser(String username) {
		usernames.add(username);
		JSONObject obj = new JSONObject();
		try {
			obj.put(UpdateType.USERJOIN.getDataKeys().get(0), username);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		fireUpdateEvent(new UpdateEvent(UpdateType.USERJOIN, obj));
	}
	public void removeUser(String username) {
		usernames.remove(username);
		JSONObject obj = new JSONObject();
		try {
			obj.put(UpdateType.USERQUIT.getDataKeys().get(0), username);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		fireUpdateEvent(new UpdateEvent(UpdateType.USERQUIT, obj));
	}
	public List<String> getUsernames() {
		return usernames;
	}
	public List<String> getChat() {
		return chat;
	}
	public String getUuid() {
		return uuid;
	}
	public void setClientUpdaterThread(ClientUpdaterThread clientUpdaterThread) {
		this.clientUpdaterThread = clientUpdaterThread;
	}
	public ClientUpdaterThread getClientUpdaterThread() {
		return clientUpdaterThread;
	}
	public boolean isLocalServer() {
		return isLocalServer;
	}
	private void logout() {
		if(isLocalServer) {
			sendKillToServer();
		} else {
			JSONObject obj = new JSONObject();
			try {
				obj.put("username", getSelfUsername());
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
			Packet logoutPacket = new Packet(PacketType.LOGOUT, Packet.ATTEMPT, obj);
			clientUpdaterThread.sendPacket(logoutPacket);
		}
		clientUpdaterThread.kill();
	}
	public void close(boolean expected) {
		logout();
		JSONObject obj = new JSONObject();
		try {
			obj.put("expected", expected);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		fireUpdateEvent(new UpdateEvent(UpdateType.KILL, obj));
	}
	public void sendKillToServer() {
		JSONObject obj = new JSONObject();
		Packet logoutPacket = new Packet(PacketType.KILL, Packet.ATTEMPT, obj);
		clientUpdaterThread.sendPacket(logoutPacket);
	}
	public void addCreateGameRoomToServer() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("action", "create");
			obj.put("creator", getSelfUsername());
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		Packet logoutPacket = new Packet(PacketType.CREATEROOMACTION, Packet.ATTEMPT, obj);
		clientUpdaterThread.sendPacket(logoutPacket);
	}
	public void createCreateGameRoom(String roomUuid, CreateGameRoomBean bean) {
		CreateGameRoom createGameRoom = new CreateGameRoom(this, roomUuid, bean);
		getCreateGameRooms().put(roomUuid, createGameRoom);
		JSONObject obj = new JSONObject();
		try {
			obj.put("room", roomUuid);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		fireUpdateEvent(new UpdateEvent(UpdateType.CREATEGAME, obj));
	}
	public Map<String, CreateGameRoom> getCreateGameRooms() {
		return createGameRooms;
	}
	public String getSelfUsername() {
		return selfUsername;
	}
	public void addRoomToGameList(String roomUuid, CreateGameRoomBean cgrbean) {
		gameTableModel.put(roomUuid, cgrbean);
	}
	public GameTableModel getGameTableModel() {
		return gameTableModel;
	}
	public void removeGameFromGameList(String room) {
		gameTableModel.remove(room);
	}
	public void joinCreateGameRoomToServer(String roomUuid, CreateGameRoomBean bean) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("action", "join");
			obj.put("room", roomUuid);
			obj.put("bean", bean);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		clientUpdaterThread.sendPacket(new Packet(PacketType.CREATEROOMACTION, Packet.ATTEMPT, obj));
	}
	public void joinCreateGameRoomFromServer(String roomUuid, CreateGameRoomBean bean, int size) {
		JoinGameRoom jgrroom = new JoinGameRoom(roomUuid, this, bean, -1);
		joinGameRooms.put(roomUuid, jgrroom);
		JSONObject obj = new JSONObject();
		try {
			obj.put("room", roomUuid);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		fireUpdateEvent(new UpdateEvent(UpdateType.JOINCREATEGAME, obj));
	}
	public Map<String, JoinGameRoom> getJoinGameRooms() {
		return joinGameRooms;
	}
	public void addUserToCreateGameRoom(String roomUuid, String username) {
		createGameRooms.get(roomUuid).addJoiner(username);
	}
	public void updateJoinGameRoomSize(String roomUuid, int size) {
		joinGameRooms.get(roomUuid).setSize(size);
	}
	public void removeUserFromCreateGameRoom(String roomUuid, String username) {
		createGameRooms.get(roomUuid).removeJoiner(username);
	}
	public void debug() {
		Logger logger = Logger.getLogger();
		logger.info("=== Main Room: ===");
		logger.info("   === Users (" + usernames.size() + "): ===");
		for(String u : usernames) {
			logger.info("      " + u);
		}
		logger.info("   === # of Create Game Rooms (" + createGameRooms.size() + "): ===");
		for(CreateGameRoom sp : createGameRooms.values()) {
			logger.info("      " + sp.getUuid());
		}
		logger.info("   === # of Join Game Rooms (" + joinGameRooms.size() + "): ===");
		for(JoinGameRoom sp : joinGameRooms.values()) {
			logger.info("      " + sp.getUuid());
		}
		logger.info("   === # of Game Rooms (" + getGameRooms().size() + "): ===");
		for(JoinGameRoom sp : joinGameRooms.values()) {
			logger.info("      " + sp.getUuid());
		}
	}
	
	public void createGameRoom(Packet packet) throws JSONException {
		JSONObject message = packet.getMessage();
		String roomUuid = message.getString("room");
		String status = packet.getMessageString("status");
		GameRoom gameRoom = new GameRoom(this,
				new CreateGameRoomBean(message.getJSONObject("bean")),
				message.getString("room"),
				message.getJSONArray("players"),
				message.getJSONArray("observers"),
				"player".equals(status) ? false : true);
		getGameRooms().put(roomUuid, gameRoom);
		JSONObject obj = new JSONObject();
		try {
			obj.put("room", roomUuid);
			obj.put("status", packet.getMessageString("status"));
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		fireUpdateEvent(new UpdateEvent(UpdateType.STARTGAME, obj));
	}
	
	public Map<String, GameRoom> getGameRooms() {
		return gameRooms;
	}
	public void removeBeanFromList(CreateGameRoomBean createGameRoomBean) {
		
	}
}
