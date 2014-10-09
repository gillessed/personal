package net.gillessed.tablemahjong.client;

import java.io.IOException;

import net.gillessed.tablemahjong.server.ResponseHandler;
import net.gillessed.tablemahjong.server.SocketStream;
import net.gillessed.tablemahjong.server.packet.Packet;
import net.gillessed.tablemahjong.server.packet.Packet.PacketType;
import net.gillessed.tablemahjong.server.utils.ServerUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ClientUpdaterThread extends Thread {
	
	private static final int THREAD_SLEEP_TIME = 100;
	
	private final SocketStream stream;
	private final ClientResponseHandler responseHandler;
	private boolean killed;
	private boolean useCustomHandler;
	private ResponseHandler customResponseHandler;
 
	public ClientUpdaterThread(SocketStream stream, MainRoom mainRoom) {
		this.stream = stream;
		killed = false;
		responseHandler = new ClientResponseHandler(mainRoom);
		mainRoom.setClientUpdaterThread(this);
		useCustomHandler = false;
		setDaemon(true);
	}
	
	public void sendDebugPacket(String... debugInfo) {
		JSONObject obj = new JSONObject();
		JSONArray infoArray = new JSONArray();
		for(String s : debugInfo) {
			infoArray.put(s);
		}
		try {
			obj.put("debug-info", infoArray);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		sendPacket(new Packet(PacketType.DEBUG, Packet.ATTEMPT, obj));
	}

	public void sendSimulatePacket(String type, String creator) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("type", type);
			obj.put("creator", creator);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		sendPacket(new Packet(PacketType.SIMULATE, Packet.ATTEMPT, obj));
	}
	
	public synchronized void sendPacket(Packet packet) {
		stream.getOut().println(packet.toString());
	}
	
	public void insertCustomHandler(ResponseHandler handler) {
		customResponseHandler = handler;
		useCustomHandler = true;
	}
	
	@Override
	public void run() {
		while(!killed) {
			try {
				Thread.sleep(THREAD_SLEEP_TIME);
				Packet response = ServerUtils.waitForResponse(stream);
				if(response != null) {
					if(useCustomHandler) {
						customResponseHandler.handle(response);
						customResponseHandler = null;
						useCustomHandler = false;
					} else {
						responseHandler.handle(response);
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public void kill() {
		killed = true;
	}
}
