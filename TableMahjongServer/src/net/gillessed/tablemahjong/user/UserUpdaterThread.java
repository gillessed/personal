package net.gillessed.tablemahjong.user;

import java.io.IOException;

import net.gillessed.tablemahjong.server.SocketStream;
import net.gillessed.tablemahjong.server.logging.Logger;
import net.gillessed.tablemahjong.server.packet.Packet;

public class UserUpdaterThread extends Thread {
	
	private SocketStream stream;
	private boolean killed;
	private User user;
	
	public UserUpdaterThread(User user, SocketStream stream) {
		this.user = user;
		this.stream = stream;
		killed = false;
		user.setUserUpdaterThread(this);
		setDaemon(true);
	}
	
	public SocketStream getStream() {
		return stream;
	}
	public synchronized void sendPacket(Packet packet) {
		stream.getOut().println(packet.toString());
	}
	
	@Override
	public void run() {
		while(!killed) {
			try {
				String input = stream.getIn().readLine();
				if(input != null) {
					Packet p = new Packet(input);
					user.getResponseHandler().handle(p);
				}
			} catch (IOException e) {
				Logger.getLogger().error("There has been an error in the socket streams.");
				stream.close();
				throw new RuntimeException(e);
			}
		}
		stream.close();
	}
	
	public void kill() {
		killed = true;
	}
}
