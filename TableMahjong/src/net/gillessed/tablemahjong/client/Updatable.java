package net.gillessed.tablemahjong.client;

import java.util.ArrayList;
import java.util.List;

import net.gillessed.tablemahjong.client.event.UpdateEvent;
import net.gillessed.tablemahjong.client.event.UpdateListener;

public abstract class Updatable {
	protected List<UpdateListener> updateListeners;
	
	public Updatable() {
		updateListeners = new ArrayList<UpdateListener>();
	}
	
	public void addUpdateListener(UpdateListener ul) {
		updateListeners.add(ul);
	}
	public void removeUpdateListener(UpdateListener ul) {
		updateListeners.remove(ul);
	}
	protected void fireUpdateEvent(UpdateEvent e) {
		for(UpdateListener ul : updateListeners) {
			ul.actionPerformed(e);
		}
	}
}
