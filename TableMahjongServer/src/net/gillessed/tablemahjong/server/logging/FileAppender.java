package net.gillessed.tablemahjong.server.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileAppender implements Appender {

	private File f;
	private PrintWriter wr;
	
	public FileAppender(String fileName) {
		f = new File(fileName);
		try {
			wr = new PrintWriter(new FileWriter(f));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void log(String message) {
		wr.append(message);
	}
}
