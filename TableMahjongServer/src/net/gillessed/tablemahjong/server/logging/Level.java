package net.gillessed.tablemahjong.server.logging;

public enum Level {
	INFO(1),
	DEV(2),
	DEBUG(3),
	WARN(4),
	ERROR(5),
	FATAL(6);

	private final int levelInt;
	Level(int levelInt) {
		this.levelInt = levelInt;
	}
	
	public boolean isOn(Level info) {
		return levelInt >= info.getLevelInt();
	}

	public int getLevelInt() {
		return levelInt;
	}
}
