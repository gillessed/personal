package net.gillessed.tablemahjong.server;

public class ResponseTimer {

	private static final long SERVER_LOGIN_TIMEOUT = 1000000;
	
	private final long millis;
	private long startTime;
	
	public ResponseTimer() {
		this(SERVER_LOGIN_TIMEOUT);
	}
	public ResponseTimer(long millis) {
		this.millis = millis;
	}
	
	public void start() {
		startTime = System.currentTimeMillis();
	}
	
	public boolean timeout() {
		return (System.currentTimeMillis() - startTime > millis);
	}
}
