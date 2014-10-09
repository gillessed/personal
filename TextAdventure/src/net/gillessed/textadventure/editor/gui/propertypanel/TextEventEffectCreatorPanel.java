package net.gillessed.textadventure.editor.gui.propertypanel;

import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.gillessed.textadventure.datatype.TextEventEffect;

@SuppressWarnings("serial")
public class TextEventEffectCreatorPanel extends CreatorPanel<TextEventEffect> {
	
	private JTextArea textArea;
	
	public TextEventEffectCreatorPanel(TextEventEffect model, final EventEffectAdderPanel parent) {
		super(model, parent);
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		textArea = new JTextArea(5, 40);
		if(model.getDescription() != null) {
			textArea.setText(model.getDescription());
		}
		JScrollPane descriptionScrollPane = new JScrollPane(textArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(new JLabel("Text: "));
		add(descriptionScrollPane);
		add(Box.createHorizontalStrut(5));
		add(deleteButton);
	}

	@Override
	public void save() {
		model.setDescription(textArea.getText());
	}
}
