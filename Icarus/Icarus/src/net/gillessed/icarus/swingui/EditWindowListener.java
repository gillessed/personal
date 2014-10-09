package net.gillessed.icarus.swingui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JMenuItem;

public class EditWindowListener implements WindowListener {

	private final List<JMenuItem> editMenuItems;

	public EditWindowListener(List<JMenuItem> editMenuItems) {
		this.editMenuItems = editMenuItems;
	}
	
	@Override
	public void windowOpened(WindowEvent paramWindowEvent) {}

	@Override
	public void windowClosing(WindowEvent paramWindowEvent) {}

	@Override
	public void windowClosed(WindowEvent paramWindowEvent) {
		for(JMenuItem menuItem : editMenuItems) {
			menuItem.setEnabled(true);
		}
	}
	@Override
	public void windowIconified(WindowEvent paramWindowEvent) {}
	@Override
	public void windowDeiconified(WindowEvent paramWindowEvent) {}
	@Override
	public void windowActivated(WindowEvent paramWindowEvent) {}
	@Override
	public void windowDeactivated(WindowEvent paramWindowEvent) {}
	
}
