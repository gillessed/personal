package net.gillessed.tablemahjong.server.logging;

public class ConsoleAppender implements Appender {
	@Override
	public void log(String message) {
		System.err.println(message);
	}
}
