package net.gillessed.textadventure.editor.gui.propertypanel;

import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

@SuppressWarnings("serial")
public class PropertyPanel extends JPanel {

	private final FormLayout propertyEditorFormLayout;
	private int propertyRowCount;
	private int propertyColumnCount;
	private final int columns;
	protected final CellConstraints cc;
	
	public PropertyPanel(int rows, int colums) {
		this.columns = colums;
		cc = new CellConstraints();
		
		String columnSpec = "10dlu, ";
		for(int i = 0; i < colums; i++) {
			columnSpec += "pref, 10dlu, pref, 10dlu";
			if(i < colums - 1) {
				columnSpec += ", ";
			}
		}
		
		String rowSpec = "10dlu, ";
		for(int i = 0; i < rows; i++) {
			rowSpec += "pref, 10dlu";
			if(i < rows - 1) {
				rowSpec += ", ";
			}
		}
		
		propertyEditorFormLayout = new FormLayout(columnSpec, rowSpec);
		setLayout(propertyEditorFormLayout);

		propertyRowCount = 2;
		propertyColumnCount = 2;
	}
	
	public void addSubPanel(JComponent panel) {
		if(propertyColumnCount != 2) {
			propertyColumnCount = 2;
			propertyRowCount += 2;
		}
		add(panel, cc.xyw(2, propertyRowCount, columns * 4 - 1));
		propertyRowCount += 2;
	}
	
	public void addPropertyLabel(String name) {
		if(propertyColumnCount != 2) {
			propertyColumnCount = 2;
			propertyRowCount += 2;
		}
		JLabel propertyLabel = new JLabel(name);
		propertyLabel.setFont(new Font(propertyLabel.getFont().getName(),
				Font.PLAIN, propertyLabel.getFont().getSize() * 2));
		add(propertyLabel, cc.xyw(2, propertyRowCount, columns * 4 - 1));
		propertyRowCount += 2;
	}
	
	public void addProperty(String name, JComponent component) {
		add(new JLabel(name), cc.xy(propertyColumnCount, propertyRowCount));
		add(component, cc.xy(propertyColumnCount + 2, propertyRowCount));
		propertyColumnCount += 4;
		if((propertyColumnCount - 2) / 4 >= columns) {
			propertyColumnCount = 2;
			propertyRowCount += 2;
		}
	}
}
