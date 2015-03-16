package net.gillessed.icarus.swingui.help;

import java.awt.Container;
import java.awt.GridLayout;
import java.io.File;
import java.net.URL;

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
	
		try {
			URL url = new File("resources" + File.separator + "help.html").toURI().toURL();
			pane = new JEditorPane(url);
			pane.setEditable(false);
			
			JScrollPane scrollPane = new JScrollPane(pane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			c.add(scrollPane);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void show() {
		dialog.setVisible(true);
	}

}
