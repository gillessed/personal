package net.gillessed.tablemahjong.user;

import net.gillessed.tablemahjong.server.packet.Packet;
import net.gillessed.tablemahjong.threadspace.MainThreadSpace;

public interface UserFactory {
	public User createUser(Packet loginPacket, MainThreadSpace mainThreadSpace);
}
