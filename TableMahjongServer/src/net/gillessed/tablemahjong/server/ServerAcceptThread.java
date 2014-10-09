package net.gillessed.tablemahjong.server;

import java.io.IOException;
import java.net.ServerSocket;

import net.gillessed.tablemahjong.server.logging.Logger;
import net.gillessed.tablemahjong.server.packet.Packet;
import net.gillessed.tablemahjong.threadspace.MainThreadSpace;
import net.gillessed.tablemahjong.user.User;
import net.gillessed.tablemahjong.user.UserLogin;
import net.gillessed.tablemahjong.user.UserUpdaterThread;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerAcceptThread extends Thread {
	
	private boolean killed;
	private ServerSocket serverSocket;
	private MainThreadSpace mainThreadSpace;
	private final UserLogin userLogin;
	
	public ServerAcceptThread(MainThreadSpace mainThreadSpace, ServerSocket serverSocket, UserLogin userLogin) {
		this.mainThreadSpace = mainThreadSpace;
		this.serverSocket = serverSocket;
		this.userLogin = userLogin;
		killed = false;
	}
	
	@Override
	public void run() {
        while(!killed) {
            try {
                SocketStream stream = new SocketStream(serverSocket.accept());
                Logger.getLogger().info("Receiving socket connection.");
            	ResponseTimer timer = new ResponseTimer();
            	timer.start();
            	while(!timer.timeout()) {
            		String login = stream.getIn().readLine();
            		if(login != null) {
                        Logger.getLogger().info("Received login credentials.");
                        Packet loginPacket = new Packet(login);
                        User loggingIn = userLogin.getUserFactory().createUser(loginPacket, mainThreadSpace);
                        boolean loginSuccess = userLogin.login(loggingIn);
                        if(loginSuccess) {
                            Logger.getLogger().info("Login succeeded from " + loggingIn.getName() + ".");
                        	Packet loginSucessPacket = new Packet(Packet.PacketType.LOGIN, Packet.SUCCESS, new JSONObject());
                        	stream.getOut().println(loginSucessPacket.toString());
                        	new UserUpdaterThread(loggingIn, stream);
                        	mainThreadSpace.addUser(loggingIn);
                        	
                        } else {
                            Logger.getLogger().info("Login failed from " + loggingIn.getName() + ".");
                        	JSONObject object = new JSONObject();
                        	try {
								object.put("failure-message", userLogin.getLoginFailure());
							} catch (JSONException e) {
								throw new RuntimeException(e);
							}
                        	Packet loginFailure = new Packet(Packet.PacketType.LOGIN, Packet.FAILURE, object);
                        	stream.getOut().println(loginFailure.toString());
                        }
            	        break;
            		}
            	}
            } catch (IOException e) {
//                Squish this for now
//                e.printStackTrace();
            }
        }
	}

	public void kill() {
		killed = true;
		try {
			serverSocket.close();
		} catch (IOException e) {
//          Squish this for now
//          e.printStackTrace();
		}
	}
}
