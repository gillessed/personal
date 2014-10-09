package net.gillessed.textadventure.editor.gui.propertypanel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.datatype.Event;
import net.gillessed.textadventure.datatype.Response;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

@SuppressWarnings("serial")
public class ResponseCreatorPanel extends CreatorPanel<Response> {

	private static final String NO_NEXT_EVENT = "- No Next Event -";
	
	private final JTextField titleTextField;
	private final JTextArea textTextArea;
	private final JComboBox<Object> eventComboBox;
	
	public ResponseCreatorPanel(Response model, final ResponseAdderPanel responseAdderPanel) {
		super(model, responseAdderPanel);
		
		FormLayout layout = new FormLayout("5dlu, pref, 5dlu, fill:pref:grow, 10dlu, 80dlu, 5dlu", 
				"5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu");
		CellConstraints cc = new CellConstraints();
		setLayout(layout);

		titleTextField = new JTextField(60);
		titleTextField.setText(model.getName());
		add(new JLabel("Title: "), cc.xy(2,2));
		add(titleTextField, cc.xyw(4,2,3));
		
		textTextArea = new JTextArea(10, 50);
		textTextArea.setText(model.getDescription());
		JScrollPane descriptionScrollPane = new JScrollPane(textTextArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(new JLabel("Text: "), cc.xy(2,4));
		add(descriptionScrollPane, cc.xyw(4,4,3));
		
		List<Object> nextEventSelections = new ArrayList<Object>();
		nextEventSelections.add(NO_NEXT_EVENT);
		nextEventSelections.addAll(DataType.getAllEvents((Event)model.getParent()));
		eventComboBox = new JComboBox<Object>(nextEventSelections.toArray(new Object[0]));
		if(model.getNextEvent() != null) {
			eventComboBox.setSelectedItem(model.getNextEvent());
		}
		add(new JLabel("Next Event: "), cc.xyw(2,6,1));
		add(eventComboBox, cc.xyw(4,6,1));
		add(deleteButton, cc.xy(6,6));
	}

	@Override
	public void save() {
		model.setName(titleTextField.getText());
		model.setDescription(textTextArea.getText());
		Object nextEvent = eventComboBox.getSelectedItem();
		if(nextEvent.equals(NO_NEXT_EVENT)) {
			model.setNextEvent(null);
		} else {
			model.setNextEvent((Event)nextEvent);
		}
	}

	@Override
	public void delete() {}

	@Override
	public Response getModel() {
		return model;
	}

}
