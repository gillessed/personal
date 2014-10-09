package net.gillessed.textadventure.editor.gui.propertypanel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.datatype.Event;
import net.gillessed.textadventure.datatype.EventEffect;
import net.gillessed.textadventure.editor.gui.custom.EventEffectFactory;
import net.gillessed.textadventure.editor.gui.custom.EventEffectFactoryVector;

@SuppressWarnings("serial")
public class EventEffectAdderPanel extends 
		AdderPanel<EventEffect<? extends DataType>, CreatorPanel<EventEffect<? extends DataType>>>{
	
	private final EventEffectFactoryVector eventEffectFactorVector;
	private List<JComboBox<EventEffectFactory>> newTypeComboBoxes;
	private JComboBox<EventEffectFactory> newTypeAddComboBox;
	private final Event parentEvent;

	public EventEffectAdderPanel(List<EventEffect<? extends DataType>> model, Event parentEvent, JPanel parent) {
		super(model, parent, "Event Effects", true);
		this.parentEvent = parentEvent;
		eventEffectFactorVector = new EventEffectFactoryVector();
		newTypeComboBoxes = new ArrayList<>();
		for(int i = 0; i < model.size(); i++) {
			newTypeComboBoxes.add(getNewComboBox());
		}
		newTypeAddComboBox = getNewComboBox();
		createGUI();
	}
	
	@Override
	protected void deletedInsertPanel(int index) {
		newTypeComboBoxes.remove(index);
	}
	
	private JComboBox<EventEffectFactory> getNewComboBox() {
		return new JComboBox<>(eventEffectFactorVector);
	}
	
	@Override
	protected void addToAddPanel() {
		addPanel.add(newTypeAddComboBox);
	}
	
	@Override
	protected void addToInsertPanel(JPanel insertPanel, int index) {
		JComboBox<EventEffectFactory> chooser = getNewComboBox();
		if(index >= newTypeComboBoxes.size()) {
			newTypeComboBoxes.add(chooser);
		} else {
			newTypeComboBoxes.add(index, chooser);
		}
		insertPanel.add(chooser);
	}

	@Override
	protected CreatorPanel<EventEffect<? extends DataType>> getCreatorPanel(EventEffect<? extends DataType> s, int index) {
		if(index == ALREADY_CREATED) {
			return eventEffectFactorVector.getFactory(s.getClass()).createCreatorPanel(s, this);
		} else if(index == -1) {
			return ((EventEffectFactory)newTypeAddComboBox.getSelectedItem()).createCreatorPanel(s, this);
		} else {
			return ((EventEffectFactory)newTypeComboBoxes.get(index).getSelectedItem()).createCreatorPanel(s, this);
		}
	}

	@Override
	protected EventEffect<? extends DataType> generateNewElement(int index) {
		if(index == -1) {
			return ((EventEffectFactory)newTypeAddComboBox.getSelectedItem()).createEventEffect(parentEvent);
		} else {
			return ((EventEffectFactory)newTypeComboBoxes.get(index).getSelectedItem()).createEventEffect(parentEvent);
		}
	}

}
