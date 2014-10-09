package net.gillessed.tablemahjong.server.logging;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class OptionPaneAppender implements Appender {

	private final JFrame frame;

	public OptionPaneAppender(JFrame frame) {
		this.frame = frame;
	}
	
	@Override
	public void log(String message) {
		JOptionPane.showMessageDialog(frame, message, "Logging", JOptionPane.INFORMATION_MESSAGE);
	}

}
