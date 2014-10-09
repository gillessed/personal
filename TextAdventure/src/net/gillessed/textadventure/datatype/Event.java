package net.gillessed.textadventure.datatype;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import net.gillessed.textadventure.utils.IconUtils;
import net.gillessed.textadventure.utils.ListenerList;

public class Event extends DataType {
	private static final long serialVersionUID = 4502311900394033922L;

	private final List<Condition> conditions;
	private final List<Response> responses;
	private final List<EventEffect<? extends DataType>> eventEffects;
	
	public Event(DataType parent) {
		super(parent);
		conditions = new ListenerList<>();
		eventEffects = new ListenerList<>();
		responses = new ArrayList<>();
	}

	public List<Condition> getConditions() {
		return conditions;
	}
	
	public List<EventEffect<? extends DataType>> getEventEffects() {
		return eventEffects;
	}
	
	public List<Response> getResponses() {
		return responses;
	}
	
	@Override
	public Icon getIcon(int size) {
		return IconUtils.EVENTS_ICON(size);
	}
}
