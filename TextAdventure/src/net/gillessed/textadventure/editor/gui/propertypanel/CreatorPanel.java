package net.gillessed.textadventure.editor.gui.propertypanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.editor.gui.editorpanel.ModelEditor;
import net.gillessed.textadventure.utils.IconUtils;

@SuppressWarnings("serial")
public abstract class CreatorPanel<T extends DataType> extends ModelEditor<T> {

	protected T model;
	protected final JButton deleteButton;

	public CreatorPanel(T model, @SuppressWarnings("rawtypes") final AdderPanel parent) {
		this.model = model;
		
		deleteButton = new JButton("Delete");
		deleteButton.setIcon(IconUtils.DELETE_ICON_16);
		deleteButton.addActionListener(new ActionListener() {
			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.delete((CreatorPanel)CreatorPanel.this);
			}
		});
	}
	
	@Override
	public void delete() {}

	@Override
	public T getModel() {
		return model;
	}

}
