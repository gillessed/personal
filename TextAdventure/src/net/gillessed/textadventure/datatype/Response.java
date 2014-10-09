package net.gillessed.textadventure.datatype;

import javax.swing.Icon;

import net.gillessed.textadventure.deletelistener.DeleteListener;

public class Response extends DataType {
	private static final long serialVersionUID = -7336231527852895887L;

	private Event nextEvent;
	private final DeleteListener eventListener;
	
	public Response(Event parent) {
		super(parent);
		eventListener = new DeleteListener() {
			private static final long serialVersionUID = 7604406030136922993L;

			@Override
			public void deleted(DataType deleted) {
				setNextEvent(null);
			}
		};
	}

	public Event getNextEvent() {
		return nextEvent;
	}

	public void setNextEvent(Event nextEvent) {
		if(this.nextEvent != null) {
			this.nextEvent.removeDeleteListener(eventListener);
		}
		this.nextEvent = nextEvent;
		if(nextEvent != null) {
			nextEvent.addDeleteListener(eventListener);
		}
	}

	@Override
	public Icon getIcon(int size) {
		return null;
	}

}
