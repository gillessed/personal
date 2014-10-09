package net.gillessed.tablemahjong.server.utils;

import java.io.IOException;

import net.gillessed.tablemahjong.server.ResponseTimer;
import net.gillessed.tablemahjong.server.SocketStream;
import net.gillessed.tablemahjong.server.logging.Logger;
import net.gillessed.tablemahjong.server.packet.Packet;

public class ServerUtils {
	/**
	 * Waits for a reponse on the given input stream of the socket. If there is not reponse in a given
	 * time limit, the function will return null.
	 * @param stream The socket stream to wait on.
	 * @return The reponse of the server or client. Null if no reponse.
	 * @throws IOException
	 */
	public static Packet waitForResponse(SocketStream stream) throws IOException {
		boolean receivedResponse = false;
		String response = null;
		ResponseTimer timer = new ResponseTimer();
		timer.start();
		while(!timer.timeout() && !receivedResponse) {
			response = stream.getIn().readLine();
			if(response != null) {
				receivedResponse = true;
				Logger.getLogger().debug("ServerUtils - Got response from server: " + response);
			}
		}
		if(receivedResponse == false) {
			Logger.getLogger().debug("ServerUtils - Server response wait timed out.");
		}
		return new Packet(response);
	}
}
