package net.gillessed.textadventure.editor.gui.propertypanel;

import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JComboBox;

import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.datatype.Event;

@SuppressWarnings("serial")
public class EventChooserPanel extends CreatorPanel<Event> {

	private final JComboBox<Event> eventChooserBox;
	
	public EventChooserPanel(Event model, Event currentEvent, final EventAdderPanel eventAdderPanel) {
		super(model, eventAdderPanel);
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		eventChooserBox = new JComboBox<>(DataType.getAllEvents(currentEvent).toArray(new Event[0]));
		eventChooserBox.setSelectedItem(model);
		add(eventChooserBox);

		add(Box.createHorizontalStrut(5));
		add(deleteButton);
	}
	
	@Override
	public void save() {
		model = (Event)eventChooserBox.getSelectedItem();
	}

	@Override
	public void delete() {}

	@Override
	public Event getModel() {
		return model;
	}
}
