package net.gillessed.icarus.swingui.help;

import java.awt.Container;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class HelpFrame {

	private final JDialog dialog;
	private final JEditorPane pane;
	
	public HelpFrame(JFrame parent) {
		dialog = new JDialog(parent);
		dialog.setSize(800,600);
		dialog.setTitle("How to use");
		dialog.setLocation(parent.getLocation().x, parent.getLocation().y);
		Container c = dialog.getContentPane();
		c.setLayout(new GridLayout(1,1));
	
		String url = "file://" + new File("resources" + File.separator + "help.html").getAbsolutePath();
		//TODO: make this work?
		try {
			pane = new JEditorPane(url);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		pane.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(pane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		c.add(scrollPane);
	}
	
	public void show() {
		dialog.setVisible(true);
	}

}
