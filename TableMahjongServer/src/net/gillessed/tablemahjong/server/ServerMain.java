package net.gillessed.tablemahjong.server;

import java.io.IOException;

public class ServerMain {
	public static void main(String args[]) {
		TableMahjongServer server = new TableMahjongServer(2345, ServerType.ENTERPRISE);
		try {
			server.start();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
