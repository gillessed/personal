package net.gillessed.textadventure.editor.gui.propertypanel;

import java.util.List;

import javax.swing.JPanel;

import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.datatype.Event;

@SuppressWarnings("serial")
public class EventAdderPanel extends AdderPanel<Event, EventChooserPanel>{

	private final Event currentEvent;

	public EventAdderPanel(Event currentEvent, List<Event> model, JPanel parent, String title) {
		super(model, parent, title);
		this.currentEvent = currentEvent;
		createGUI();
	}

	@Override
	protected EventChooserPanel getCreatorPanel(Event s, int index) {
		return new EventChooserPanel(s, currentEvent, this);
	}

	@Override
	protected Event generateNewElement(int index) {
		List<Event> allOtherEvents = DataType.getAllEvents(currentEvent);
		if(allOtherEvents.size() >= 1) {
			return allOtherEvents.get(0);
		} else {
			return null;
		}
	}
}
