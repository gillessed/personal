package net.gillessed.icarus.swingui.function;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import net.gillessed.icarus.Function;
import net.gillessed.icarus.variation.Variation;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class FunctionEditPanel extends JPanel {
	
	private static class FunctionTableModel extends AbstractTableModel {
		
		private static final long serialVersionUID = 746730680180987870L;
		private Function model;
		
		public FunctionTableModel(Function model) {
			this.model = model;
		}
		
		@Override
		public int getColumnCount() {
			return 2;
		}
		
		@Override
		public int getRowCount() {
			if(model == null || model.getVariations() == null) {
				return 0;
			} else {
				return model.getVariations().size();
			}
		}
		
		@Override
		public Object getValueAt(int row, int col) {
			if(col == 0) {
				return model.getVariations().get(row);
			} else if (col == 1) {
				return model.getVariations().get(row).getWeight();
			} else {
				return null;
			}
		}
		
		public void setModel(Function model) {
			this.model = model;
			fireTableDataChanged();
		}
		
		@Override
		public Class<?> getColumnClass(int col) {
			if(col == 0) {
				return Variation.class;
			} else if (col == 1) {
				return Double.class;
			} else {
				throw new IllegalArgumentException(col + " must be between 0 and 1 inclusive");
			}
		}
		
		@Override
		public String getColumnName(int col) {
			if(col == 0) {
				return "Variation";
			} else if (col == 1) {
				return "Weight";
			} else {
				throw new IllegalArgumentException(col + " must be between 0 and 1 inclusive");
			}
		}
		
		@Override
		public boolean isCellEditable(int row, int col) {
			if(col == 0) { 
				return false; 
			} else {
				return true;
			}
		}
		
		@Override
		public void setValueAt(Object aValue, int row, int col) {
			if(col == 1) {
				if(aValue != null)	{
					model.getVariations().get(row).setWeight((Double)aValue);
					fireTableCellUpdated(row, col);
				}
			}
		}
	}
	private static final long serialVersionUID = -3504219495523818682L;
	private Function model;
	private final JTextField probabilityField;
	private final JTable functionTable;
	private final FunctionTableModel functionTableModel;
	
	public FunctionEditPanel(Function model) {
		super();
		this.model = model;

		CellConstraints cc = new CellConstraints();
		FormLayout layout = new FormLayout("10dlu, fill:pref:grow, 30dlu, pref, 10dlu",
											"10dlu, pref, 10dlu, pref, 10dlu, pref, 10dlu");
		this.setLayout(layout);
		setBorder(BorderFactory.createLineBorder(new Color(100,100,150)));
		add(new JLabel("Probability:"), cc.xy(2, 2));
		add(new JLabel("Color:"), cc.xy(2, 4));
		
		probabilityField = new JTextField(5);
		add(probabilityField, cc.xy(4, 2));
		
		functionTableModel = new FunctionTableModel(model);
		functionTable = new JTable(functionTableModel);
		functionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane tableScrollPane = new JScrollPane(functionTable);
		tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		tableScrollPane.setPreferredSize(new Dimension(300,200));
		tableScrollPane.setBorder(BorderFactory.createBevelBorder(1));
		add(tableScrollPane, cc.xyw(2, 6, 3));
	}
	
	public void applyChanges() {
		model.setProbability(Double.parseDouble(probabilityField.getText()));
	}
	
	public void setModel(Function model) {
		this.model = model;
		functionTableModel.setModel(model);
		probabilityField.setText(Double.toString(model.getProbability()));
		repaint();
	}
	
	public Function getModel() {
		return model;
	}
	
	public void updateTable() {
		functionTableModel.fireTableDataChanged();
		repaint();
	}
}
