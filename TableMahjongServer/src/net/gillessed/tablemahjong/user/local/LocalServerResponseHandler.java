package net.gillessed.tablemahjong.user.local;

import net.gillessed.tablemahjong.mahjongupdate.MahjongUpdate;
import net.gillessed.tablemahjong.server.logging.Logger;
import net.gillessed.tablemahjong.server.packet.Packet;
import net.gillessed.tablemahjong.threadspace.MainThreadSpace;
import net.gillessed.tablemahjong.user.AbstractServerResponseHandler;
import net.gillessed.tablemahjong.user.User;

import org.json.JSONException;

public class LocalServerResponseHandler  extends AbstractServerResponseHandler {

	public LocalServerResponseHandler(MainThreadSpace mainThreadSpace, User user) {
		super(mainThreadSpace, user);
	}

	@Override
	public void handle(Packet packet) {
		String action;
		switch(packet.getPacketType()) {
		case LOGOUT:
			mainThreadSpace.removeUser(user.getName());
			break;
		case KILL:
			mainThreadSpace.removeUser(user.getName());
			mainThreadSpace.getTableMahjongServer().kill();
			break;
		case CHAT:
			mainThreadSpace.updateChat(packet);
			break;
		case CREATEROOMACTION:
			Logger.getLogger().dev("SERVER - CreateRoomAction");
			action = packet.getMessageString("action");
			if("create".equals(action)) {
				mainThreadSpace.createCreateGameThreadSpace(packet);
			} else if("kill".equals(action)) {
				mainThreadSpace.killCreateGameThreadSpace(packet);
			} else if("finalize".equals(action)) {
				mainThreadSpace.finalizeCreateGameThreadSpace(packet);
			} else if("join".equals(action)) {
				mainThreadSpace.addUserToCreateGameThreadSpace(packet, user);
			} else if("leave".equals(action)) {
				mainThreadSpace.removeUserFromCreateGameThreadSpace(packet, user);
			} else if("start".equals(action)) {
				try {
					mainThreadSpace.startGame(packet);
				} catch (JSONException e) {
					throw new RuntimeException(e);
				}
			}
			break;
		case GAMEROOMACTION:
			Logger.getLogger().dev("SERVER - GameRoomAction");
			action = packet.getMessageString("action");
			if("message".equals(action)) {
				mainThreadSpace.getGameThreadSpaces().get(packet.getMessageString("room")).notify(user.getName());
			}
			if("steal-chosen".equals(action)) {
				mainThreadSpace.getGameThreadSpaces().get(packet.getMessageString("room")).stealChosen(packet.getMessage());
			}
			break;
		case MAHJONGACTION:
			action = packet.getMessageString("action");
			Logger.getLogger().dev("SERVER - MahjongAction: " + action);
			if("event".equals(action)) {
				try {
					mainThreadSpace.getGameThreadSpaces()
						.get(packet.getMessageString("room"))
						.event(new MahjongUpdate(packet.getMessage().getJSONObject("event")), user.getName());
				} catch (JSONException e) {
					throw new RuntimeException(e);
				}
			}
			break;
		case DEBUG:
			try {
				mainThreadSpace.debug(packet);
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
			break;
		case SIMULATE:
				mainThreadSpace.simulate(packet);
			break;
		default:
			Logger.getLogger().error("Unkown packet type in packet " + packet.toString());
			break;
		}
	}
}
