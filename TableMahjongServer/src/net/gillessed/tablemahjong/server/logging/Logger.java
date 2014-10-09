package net.gillessed.tablemahjong.server.logging;

import java.util.Stack;

public class Logger {
	private final static Logger logger = new Logger(Level.INFO);

	public static Logger getLogger() {
		return logger;
	}
	
	private Level level;
	private Stack<Appender> appenders;
	
	public Logger(Level level) {
		this.level = level;
		appenders = new Stack<Appender>();
	}
	
	public void pushAppender(Appender appender) {
		appenders.push(appender);
	}
	
	public void popAppender() {
		appenders.pop();
	}
	
	public void setLevel(Level level) {
		this.level = level;
	}

	public void info(String message) {
		if(level.isOn(Level.INFO)) {
			for(Appender a : appenders) {
				a.log("INFO - " + message);
			}
		}
	}
	public void dev(String message) {
		if(level.isOn(Level.DEV)) {
			for(Appender a : appenders) {
				a.log("DEV - " + message);
			}
		}
	}
	public void debug(String message) {
		if(level.isOn(Level.DEBUG)) {
			for(Appender a : appenders) {
				a.log("DEBUG - " + message);
			}
		}
	}
	public void warn(String message) {
		if(level.isOn(Level.WARN)) {
			for(Appender a : appenders) {
				a.log("WARN - " + message);
			}
		}
	}
	public void error(String message) {
		if(level.isOn(Level.ERROR)) {
			for(Appender a : appenders) {
				a.log("ERROR - " + message);
			}
		}
	}
	public void fatal(String message) {
		if(level.isOn(Level.FATAL)) {
			for(Appender a : appenders) {
				a.log("FATAL - " + message);
			}
		}
	}
}
