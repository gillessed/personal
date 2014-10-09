package net.gillessed.tablemahjong.swingui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import net.gillessed.tablemahjong.server.logging.Logger;

public class PlayerSelectionComboBoxModel implements ComboBoxModel<String> {
	
	private int selectedItem;
	private List<ListDataListener> listDataListeners;
	private List<String> players;
	private boolean magic;
	private final String name;
	
	public PlayerSelectionComboBoxModel(String name) {
		this.name = name;
		listDataListeners = new ArrayList<ListDataListener>();
		players = new ArrayList<String>();
		selectedItem = 0;
		magic = true;
	}
	
	@Override
	public void addListDataListener(ListDataListener lsl) {
		listDataListeners.add(lsl);
	}

	@Override
	public void removeListDataListener(ListDataListener lsl) {
		listDataListeners.remove(lsl);
	}

	@Override
	public String getElementAt(int index) {
		return players.get(index);
	}

	@Override
	public int getSize() {
		return players.size();
	}

	@Override
	public Object getSelectedItem() {
		if(players.size() == 0) {
			return null;
		} else { 
			return players.get(selectedItem);
		}
	}

	@Override
	public void setSelectedItem(Object item) {
		if(!(item instanceof String)) {
			throw new RuntimeException("item should be a string");
		}
		String str = (String)item;
		selectedItem = players.indexOf(str);
	}
	
	public boolean isEmpty() {
		return players.isEmpty();
	}
	
	public void addPlayer(String username) {
		Logger.getLogger().debug("Adding player \"" + username + "\" to " + name);
		String previouslySelected = (String)getSelectedItem();
		if(players.contains(username)) {
			Logger.getLogger().dev(name + " already contains " + username + ": " + players);
		} else {
			players.add(username);
		}
		if(players.size() == 1) {
			setSelectedItem(username);
		} else {
			setSelectedItem(previouslySelected);
		}
		setMagic(false);
		int index = players.indexOf(username);
		for(ListDataListener ldl : listDataListeners) {
			ldl.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index));
		}
		setMagic(true);
	}

	public void removePlayer(String username) {
		Logger.getLogger().debug("Removing player \"" + username + "\" from " + name);
		if(!(players.contains(username))) {
			throw new RuntimeException("You should never get into this state removing \"" + username + "\"");
		}
		String previouslySelected = (String)getSelectedItem();
		int index = players.indexOf(username);
		players.remove(username);
		if(selectedItem >= players.size()) {
			selectedItem = players.size() - 1;
		}
		if(players.size() > 1 && !username.equals(previouslySelected)) {
			setSelectedItem(previouslySelected);
		}
		setMagic(false);
		for(ListDataListener ldl : listDataListeners) {
			ldl.intervalRemoved(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index, index));
		}
		setMagic(true);
	}

	public void setMagic(boolean magic) {
		this.magic = magic;
	}

	public boolean isMagic() {
		return magic;
	}
	
	public boolean contains(String username) {
		return players.contains(username);
	}
}
