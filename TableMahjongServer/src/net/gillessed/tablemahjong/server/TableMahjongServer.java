package net.gillessed.tablemahjong.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.UUID;

import net.gillessed.tablemahjong.server.logging.Logger;
import net.gillessed.tablemahjong.threadspace.MainThreadSpace;
import net.gillessed.tablemahjong.user.UserLogin;
import net.gillessed.tablemahjong.user.local.LocalUserLogin;

public class TableMahjongServer {
	
	private final int port;
	private final MainThreadSpace mainThreadSpace;
	private ServerAcceptThread acceptThread;
	private ServerSocket serverSocket;
	private UserLogin userLogin;
	
	public TableMahjongServer(int port, ServerType type) {
		this.port = port;
		mainThreadSpace = new MainThreadSpace(UUID.randomUUID().toString(), this);
		switch(type) {
		case LOCAL:
			userLogin = new LocalUserLogin(this);
			break;
		case ENTERPRISE:
			
			break;
		}
	}
	
	public void start() throws IOException {
		Logger.getLogger().info("Starting server on port " + port + ".");
		serverSocket = new ServerSocket(port);
		acceptThread = new ServerAcceptThread(getMainThreadSpace(), serverSocket, userLogin);
		acceptThread.start();
	}
	
	public void kill() {
		Logger.getLogger().info("Shutting down server.");
		mainThreadSpace.kill();
		acceptThread.kill();
	}

	public MainThreadSpace getMainThreadSpace() {
		return mainThreadSpace;
	}
}
