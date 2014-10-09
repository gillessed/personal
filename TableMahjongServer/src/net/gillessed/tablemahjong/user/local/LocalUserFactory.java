package net.gillessed.tablemahjong.user.local;

import net.gillessed.tablemahjong.server.packet.Packet;
import net.gillessed.tablemahjong.threadspace.MainThreadSpace;
import net.gillessed.tablemahjong.user.User;
import net.gillessed.tablemahjong.user.UserFactory;

public class LocalUserFactory implements UserFactory {

	@Override
	public User createUser(Packet loginPacket, MainThreadSpace mainThreadSpace) {
		String username = loginPacket.getMessageString("username");
		return new LocalUser(username, mainThreadSpace);
	}

}
