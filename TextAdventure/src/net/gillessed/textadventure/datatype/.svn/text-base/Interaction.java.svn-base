package net.gillessed.textadventure.datatype;

import javax.swing.Icon;

import net.gillessed.textadventure.deletelistener.DeleteListener;
import net.gillessed.textadventure.utils.IconUtils;

public class Interaction extends DataType {

	private static final long serialVersionUID = -705302237752247186L;
	
	private Event eventToTrigger;
	private DeleteListener eventListener;

	public Interaction(DataType parent) {
		super(parent);
		
		eventListener = new DeleteListener() {
			private static final long serialVersionUID = 8164885921775920978L;

			@Override
			public void deleted(DataType deleted) {
				setEventToTrigger(null);
			}
		};
	}

	public Event getEventToTrigger() {
		return eventToTrigger;
	}

	public void setEventToTrigger(Event eventToTrigger) {
		if(this.eventToTrigger != null) {
			this.eventToTrigger.removeDeleteListener(eventListener);
		}
		this.eventToTrigger = eventToTrigger;
		if(eventToTrigger != null) {
			eventToTrigger.addDeleteListener(eventListener);
		}
	}
	
	@Override
	public Icon getIcon(int size) {
		return IconUtils.INTERACTIONS_ICON(size);
	}
}
