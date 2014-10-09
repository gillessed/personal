package net.gillessed.textadventure.editor.gui.editorpanel;

import java.awt.Dimension;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import net.gillessed.textadventure.datatype.Variable;
import net.gillessed.textadventure.editor.gui.EditorFrame;

@SuppressWarnings("serial")
public class VariableEditorPanel extends DataEditorPanel<Variable> {

	private final JSpinner valueSpinner;
	
	public VariableEditorPanel(Variable model, EditorFrame editorFrame) {
		super(model, editorFrame, 4, 0);
		
		generateNameDescFields();
		
		valueSpinner = new JSpinner(new SpinnerNumberModel(model.getValue(), -Double.MAX_VALUE, Double.MAX_VALUE, 1));
		valueSpinner.setPreferredSize(new Dimension(160, 20));
		getPropertyPanel().addProperty("Starting Value: ", valueSpinner);
		
		generateSaveDeletePanel(true);
	}

	@Override
	public void save() {
		saveNameAndDescription();
		model.setValue(((SpinnerNumberModel)valueSpinner.getModel()).getNumber().doubleValue());
	}

	@Override
	public void delete() {
		model.deleted();
	}
}
