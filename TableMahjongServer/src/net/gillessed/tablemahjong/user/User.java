package net.gillessed.tablemahjong.user;

import net.gillessed.tablemahjong.server.ResponseHandler;
import net.gillessed.tablemahjong.server.packet.Packet;

import org.json.JSONObject;

public interface User {
	public String getName();
	public UserUpdaterThread getUserUpdaterThread();
	public void setUserUpdaterThread(UserUpdaterThread userUpdaterThread);
	public boolean isPlayingGame();
	public void sendPacket(Packet roomJoin);
	public ResponseHandler getResponseHandler();
	public JSONObject getDefaultGameOptions();
}
