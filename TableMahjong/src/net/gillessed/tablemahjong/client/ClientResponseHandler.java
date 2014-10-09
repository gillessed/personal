package net.gillessed.tablemahjong.client;

import net.gillessed.tablemahjong.mahjonggame.MahjongGame.Wind;
import net.gillessed.tablemahjong.mahjongupdate.MahjongUpdate;
import net.gillessed.tablemahjong.server.logging.Logger;
import net.gillessed.tablemahjong.server.packet.Packet;
import net.gillessed.tablemahjong.threadspace.CreateGameRoomBean;

import org.json.JSONException;
import org.json.JSONObject;

public class ClientResponseHandler {

	private final MainRoom mainRoom;

	public ClientResponseHandler(MainRoom mainRoom) {
		this.mainRoom = mainRoom;
	}
	
	public void handle(Packet packet) {
		
		if(packet.getStatusCode() == Packet.FAILURE) {
			Logger.getLogger().error("Packet " + packet.toString() + " returned status FAILURE");
		}
		
		JSONObject data = packet.getMessage();
		String room, action, username, message, uuid;
		CreateGameRoomBean cgrbean;
		try {
			switch(packet.getPacketType()) {
			case MAINROOMACTION:
				room = data.getString("room");
				action = data.getString("action");
				username = data.getString("username");
				Logger.getLogger().dev("CLIENT - Main Room Action: " + action);
				if("join".equals(action) && "main-room".equals(room)) {
					mainRoom.addUser(username);
				}
				break;
			case LOGOUT:
				username = data.getString("username");
				mainRoom.removeUser(username);
				break;
			case KILL:
				mainRoom.close(false);
				break;
			case CHAT:
				username = data.getString("from");
				message = data.getString("message");
				uuid = data.getString("room");
				if(mainRoom.getUuid().equals(uuid)) {
					mainRoom.updateChatFromServer(username, message);
				}
				break;
			case CREATEROOMACTION:
				action = data.getString("action");
				uuid = data.getString("room");
				cgrbean = new CreateGameRoomBean(data.getJSONObject("bean"));
				Logger.getLogger().dev("CLIENT - Create Game Room Action: " + action);
				if("open-window".equals(action)) {
					mainRoom.createCreateGameRoom(uuid, cgrbean);
				} else if("finalize".equals(action)) {
					mainRoom.addRoomToGameList(uuid, cgrbean);
				} else if("kill".equals(action)) {
					mainRoom.removeGameFromGameList(uuid);
					if(mainRoom.getJoinGameRooms().containsKey(uuid)) {
						mainRoom.getJoinGameRooms().get(uuid).killFromServer();
					}
				} else if("join".equals(action)) {
					int size = data.getInt("size");
					mainRoom.joinCreateGameRoomFromServer(uuid, cgrbean, size);
				} else if("user-joined".equals(action)) {
					username = data.getString("username");
					mainRoom.addUserToCreateGameRoom(uuid, username);
				} else if("user-left".equals(action)) {
					username = data.getString("username");
					mainRoom.removeUserFromCreateGameRoom(uuid, username);
				} else if("size-update".equals(action)) {
					int size = data.getInt("size");
					mainRoom.updateJoinGameRoomSize(uuid, size);
				} else if("close".equals(action)) {
					if(mainRoom.getJoinGameRooms().containsKey(uuid)) {
						mainRoom.removeBeanFromList(new CreateGameRoomBean(data.getJSONObject("bean")));
						mainRoom.getJoinGameRooms().get(uuid).gameStarted();
					}
				} else if("transition".equals(action)) {
					mainRoom.removeGameFromGameList(uuid);
				}
				break;
			case GAMEROOMACTION:
				action = data.getString("action");
				uuid = data.getString("room");
				Logger.getLogger().dev("CLIENT - Game Room Action: " + action);
				if("create".equals(action)) {
					mainRoom.createGameRoom(packet);
				}else if("create-round".equals(action)) {
					mainRoom.getGameRooms().get(uuid).createRound(packet);
				} else if("start".equals(action)) {
					cgrbean = new CreateGameRoomBean(data.getJSONObject("bean"));
				} else if("transition".equals(action)) {
					cgrbean = new CreateGameRoomBean(data.getJSONObject("bean"));
					mainRoom.addRoomToGameList(uuid,cgrbean);
				} else if("message".equals(action)) {
					mainRoom.getGameRooms().get(uuid).message(data.getString("message"));
				} else if("steal-notice".equals(action)) {
					mainRoom.getGameRooms().get(uuid).stealNotice(Wind.values()[data.getInt("wind")]);
				} else if("freeze".equals(action)) {
					mainRoom.getGameRooms().get(uuid).freezeUsers(data);
				}
				break;
			case MAHJONGACTION:
				Logger.getLogger().dev("CLIENT - Mahjong Action");
				uuid = data.getString("room");
				action = data.getString("action");
				if("event-update".equals(action)) {
					mainRoom.getGameRooms().get(uuid).serverUpdate(new MahjongUpdate((data.getJSONObject("event"))));
				}
				break;
			default:
				Logger.getLogger().error("CLIENT - Unkown packet type in packet " + packet.toString());
				break;
			}
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
}
