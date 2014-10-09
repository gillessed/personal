package net.gillessed.tablemahjong.user;

import java.util.Map;

import net.gillessed.tablemahjong.server.ResponseHandler;
import net.gillessed.tablemahjong.server.packet.Packet;
import net.gillessed.tablemahjong.threadspace.CreateGameThreadSpace;
import net.gillessed.tablemahjong.threadspace.MainThreadSpace;

public abstract class AbstractUser implements User {

	protected String name;
	protected MainThreadSpace mainThreadSpace;
	protected Map<String, CreateGameThreadSpace> createGameThreadSpace;
	protected Map<String, CreateGameThreadSpace> gameThreadSpace;
	protected UserUpdaterThread userUpdaterThread;
	protected ResponseHandler responseHandler;
	
	public AbstractUser(String name, MainThreadSpace mainThreadSpace) {
		this.name = name;
		this.mainThreadSpace = mainThreadSpace;
		initResponseHandler();
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public UserUpdaterThread getUserUpdaterThread() {
		return userUpdaterThread;
	}

	@Override
	public void setUserUpdaterThread(UserUpdaterThread userUpdaterThread) {
		this.userUpdaterThread = userUpdaterThread;
	}
	
	@Override
	public boolean isPlayingGame() {
		return false;
	}
	
	@Override
	public synchronized void sendPacket(Packet packet) {
		userUpdaterThread.sendPacket(packet);
	}
	
	public abstract void initResponseHandler();

	@Override
	public ResponseHandler getResponseHandler() {
		return responseHandler;
	}
}
