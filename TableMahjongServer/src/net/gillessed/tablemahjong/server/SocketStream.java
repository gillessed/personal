package net.gillessed.tablemahjong.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class SocketStream { 
	private final Socket socket;
	private final PrintWriter out;
	private final BufferedReader in;
	public SocketStream(Socket socket) {
		try {
			this.socket = socket;
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public Socket getSocket() {
		return socket;
	}
	public PrintWriter getOut() {
		return out;
	}
	public BufferedReader getIn() {
		return in;
	}
	public void close() {
		try {
			out.close();
			in.close();
			socket.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
