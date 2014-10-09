package net.gillessed.textadventure.editor.gui.editorpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.editor.gui.EditorFrame;
import net.gillessed.textadventure.editor.gui.propertypanel.PropertyPanel;
import net.gillessed.textadventure.utils.IconUtils;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

@SuppressWarnings("serial")
public abstract class DataEditorPanel<T extends DataType> extends ModelEditor<T> {
	
	private final FormLayout mainFormLayout;
	private final FormLayout newButtonFormLayout;
	protected PropertyPanel propertyEditorPanel;
	protected JPanel newButtonPanel;
	protected final CellConstraints cc;
	
	protected final T model;
	protected final EditorFrame editorFrame;

	protected JButton saveButton;
	protected JButton deleteButton;
	
	private int buttonCount;

	protected JTextField nameTextField;
	protected JTextArea descriptionTextArea;
	
	public DataEditorPanel(T model, EditorFrame editorFrame, int fieldRows, int newButtons) {
		this(model, editorFrame, 1, fieldRows, newButtons);
	}
	
	public DataEditorPanel(T model, EditorFrame editorFrame, int fieldColumns, int fieldRows, int newButtons) {
		this.model = model;
		this.editorFrame = editorFrame;
		
		cc = new CellConstraints();
		mainFormLayout = new FormLayout("fill:200dlu:grow, 200dlu", "fill:200dlu:grow");
		setLayout(mainFormLayout);
		
		propertyEditorPanel = new PropertyPanel(fieldRows, fieldColumns);
		JScrollPane scrollPane = new JScrollPane(propertyEditorPanel,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.setBorder(new TitledBorder(
				BorderFactory.createLineBorder(Color.black, 1, true), "Properties"));
		add(scrollPane, cc.xy(1, 1));

		String rowSpec = "10dlu, pref, 10dlu, ";
		for(int i = 0; i < newButtons; i++) {
			rowSpec += "pref, 10dlu";
			if(i < newButtons - 1) {
				rowSpec += ", ";
			}
		}
		newButtonFormLayout = new FormLayout("10dlu, fill:pref:grow, 10dlu", rowSpec);
		newButtonPanel = new JPanel(newButtonFormLayout);
		newButtonPanel.setBorder(new TitledBorder(
				BorderFactory.createLineBorder(Color.black, 1, true), "Data Type"));
		JPanel infoPanel = new JPanel(new BorderLayout());
		JLabel infoLabel = new JLabel(model.getTypeName());
		Font currentFont = infoLabel.getFont();
		infoLabel.setFont(new Font(currentFont.getName(), Font.PLAIN, 28));
		infoLabel.setIcon(model.getIcon(128));
		infoPanel.add(infoLabel, BorderLayout.CENTER);
		newButtonPanel.add(infoPanel, cc.xy(2, 2));
		add(newButtonPanel, cc.xy(2, 1));
		
		buttonCount = 4;
	}
	
	protected void addNewButton(JButton button) {
		newButtonPanel.add(button, cc.xy(2, buttonCount));
		buttonCount += 2;
	}

	public T getModel() {
		return this.model;
	}
	
	protected void generateSaveDeletePanel(boolean canDelete) {
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		saveButton = new JButton("Save");
		saveButton.setIcon(IconUtils.SAVEDATATYPE_ICON_16);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
				editorFrame.updateUI(false);
			}
		});
		buttonPanel.add(saveButton);
		
		deleteButton = new JButton("Delete");
		deleteButton.setIcon(IconUtils.DELETE_ICON_16);
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(JOptionPane.showConfirmDialog(
							DataEditorPanel.this,
							"Are you sure you want to delete " + getModel().getName() + "?",
							"Confirm",
							JOptionPane.YES_NO_OPTION)
						== JOptionPane.YES_OPTION) {
					delete();
					editorFrame.updateUI(true);
				}
			}
		});
		if(canDelete) {
			buttonPanel.add(deleteButton);
		}

		propertyEditorPanel.addSubPanel(buttonPanel);
	}
	
	protected void generateNameDescFields() {
		nameTextField = new JTextField(60);
		nameTextField.setText(model.getName());
		propertyEditorPanel.addProperty("Name: ", nameTextField);
		
		descriptionTextArea = new JTextArea(10, 24);
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setText(model.getDescription());
		JScrollPane descriptionScrollPane = new JScrollPane(descriptionTextArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		propertyEditorPanel.addProperty("Description: ", descriptionScrollPane);
	}
	
	protected void saveNameAndDescription() {
		model.setName(nameTextField.getText());
		model.setDescription(descriptionTextArea.getText());
	}
	
	protected PropertyPanel getPropertyPanel() {
		return propertyEditorPanel;
	}
}
