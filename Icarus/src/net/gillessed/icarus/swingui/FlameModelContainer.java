package net.gillessed.icarus.swingui;

import java.util.HashSet;
import java.util.Set;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.event.FlameModificationListener;
import net.gillessed.icarus.event.NewFlameListener;

/**
 * This is a wrapper around the model for propagating UI events.
 *
 * @author gillessed
 */
public class FlameModelContainer {
	private FlameModel flameModel;
	private final Set<NewFlameListener> flameChangeListeners;
	private final Set<FlameModificationListener> flameModificationListeners;

	public FlameModelContainer() {
		flameChangeListeners = new HashSet<>();
		flameModificationListeners = new HashSet<>();
	}

	public synchronized FlameModel getFlameModel() {
		return flameModel;
	}

	public synchronized void setFlameModel(FlameModel flameModel) {
		if(this.flameModel != null) {
			this.flameModel.clearFunctionListeners();
		}
		this.flameModel = flameModel;
		for(NewFlameListener listener : flameChangeListeners) {
			listener.newFlame(flameModel);
		}
	}

	public synchronized void addNewFlameListener(NewFlameListener flameChangeListener) {
		flameChangeListeners.add(flameChangeListener);
	}

	public synchronized void removeFlameChangeListener(NewFlameListener flameChangeListener) {
		flameChangeListeners.remove(flameChangeListener);
	}

	public synchronized void flameModified() {
	    for(FlameModificationListener listener : flameModificationListeners) {
	        listener.flameModified();
	    }
	}

	public synchronized void addFlameModificationListener(FlameModificationListener flameModificationListener) {
	    flameModificationListeners.add(flameModificationListener);
	}

    public synchronized void removeFlameModificationListener(FlameModificationListener flameModificationListener) {
        flameModificationListeners.remove(flameModificationListener);
    }
}
