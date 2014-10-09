package net.gillessed.tablemahjong.server;

import net.gillessed.tablemahjong.server.packet.Packet;

public interface ResponseHandler {
	public void handle(Packet packet);
}
