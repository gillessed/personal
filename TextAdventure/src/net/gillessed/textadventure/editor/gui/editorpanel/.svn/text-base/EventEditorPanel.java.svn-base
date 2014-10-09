package net.gillessed.textadventure.editor.gui.editorpanel;

import net.gillessed.textadventure.datatype.Event;
import net.gillessed.textadventure.editor.gui.EditorFrame;
import net.gillessed.textadventure.editor.gui.propertypanel.ConditionAdderPanel;
import net.gillessed.textadventure.editor.gui.propertypanel.EventEffectAdderPanel;
import net.gillessed.textadventure.editor.gui.propertypanel.ResponseAdderPanel;

@SuppressWarnings("serial")
public class EventEditorPanel extends DataEditorPanel<Event> {
	
	private final ConditionAdderPanel conditionAdderPanel;
	private final EventEffectAdderPanel eventEffectAdderPanel;
	private final ResponseAdderPanel responseAdderPanel;
	
	public EventEditorPanel(Event model, EditorFrame editorFrame) {
		super(model, editorFrame, 8, 0);
		
		generateNameDescFields();
		
		conditionAdderPanel = new ConditionAdderPanel(model.getConditions(), this, "Event Conditions");
		getPropertyPanel().addSubPanel(conditionAdderPanel);
		
		eventEffectAdderPanel = new EventEffectAdderPanel(model.getEventEffects(), model, this);
		getPropertyPanel().addSubPanel(eventEffectAdderPanel);
		
		responseAdderPanel = new ResponseAdderPanel(model.getResponses(), model, this);
		getPropertyPanel().addSubPanel(responseAdderPanel);
		
		generateSaveDeletePanel(true);
	}

	@Override
	public void save() {
		saveNameAndDescription();
		conditionAdderPanel.save(true);
		eventEffectAdderPanel.save(true);
		responseAdderPanel.save(true);
	}

	@Override
	public void delete() {
		model.deleted();
	}

}
