package net.gillessed.icarus.swingui;

import java.util.HashSet;
import java.util.Set;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.event.FlameChangeListener;

/**
 * This is a wrapper around the model for propagating UI events.
 * 
 * @author gillessed
 */
public class FlameModelContainer {
	private FlameModel flameModel;
	private final Set<FlameChangeListener> flameChangeListeners;

	public FlameModelContainer() {
		flameChangeListeners = new HashSet<>();
	}

	public synchronized FlameModel getFlameModel() {
		return flameModel;
	}

	public synchronized void setFlameModel(FlameModel flameModel) {
		if(this.flameModel != null) {
			this.flameModel.clearFunctionListeners();
		}
		this.flameModel = flameModel;
		for(FlameChangeListener listener : flameChangeListeners) {
			listener.flameChanged(flameModel);
		}
	}
	
	public synchronized void addFlameChangeListener(FlameChangeListener flameChangeListener) {
		flameChangeListeners.add(flameChangeListener);
	}
	
	public synchronized void removeFlameChangeListener(FlameChangeListener flameChangeListener) {
		flameChangeListeners.remove(flameChangeListener);
		
	}
}
