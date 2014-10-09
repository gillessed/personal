package net.gillessed.textadventure.editor.gui.editorpanel;

import javax.swing.JComboBox;

import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.datatype.Event;
import net.gillessed.textadventure.datatype.Interaction;
import net.gillessed.textadventure.editor.gui.EditorFrame;

@SuppressWarnings("serial")
public class InteractionEditorPanel extends DataEditorPanel<Interaction> {

	private final JComboBox<Event> eventChooserBox;

	public InteractionEditorPanel(Interaction model, EditorFrame editorFrame) {
		super(model, editorFrame, 5, 0);
		
		generateNameDescFields();
		
		eventChooserBox = new JComboBox<>(DataType.getAllEvents(null).toArray(new Event[0]));
		eventChooserBox.setSelectedItem(model);
		getPropertyPanel().addProperty("Event: ", eventChooserBox);
		
		generateSaveDeletePanel(true);
	}

	@Override
	public void save() {
		saveNameAndDescription();
		model.setEventToTrigger((Event)eventChooserBox.getSelectedItem());
	}

	@Override
	public void delete() {
		model.deleted();
	}

}
