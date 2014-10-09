package com.gillessed.gradient.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.gillessed.gradient.Gradient;
import com.gillessed.gradient.GradientProvider;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * This class is generically usable panel capable of creating new
 * gradients and editing and removing existing gradients.
 * @author Gregory Cole
 */
public class GradientEditorPanel extends JPanel {
	private static final long serialVersionUID = 7849642361223070526L;
	
	private final JButton addGradient;
	private final JButton removeGradient;
	private final DefaultListModel<Gradient> gradientListModel;
	private final List<Gradient> gradients;
	private final JList<Gradient> gradientList;
	private final GradientPointEditPanel pointEditPanel;

	private final Component parent;
	
	private boolean containsName(String name) {
		for(Gradient gr : gradients) {
			if(name.equals(gr.getName())) {
				return false;
			}
		}
		return true;
	}
	
	private final ActionListener addListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String name = JOptionPane.showInputDialog(parent, "Pick a name for your gradient:", "Gradient Name", JOptionPane.INFORMATION_MESSAGE);	
			while(!containsName(name)) {
				name = JOptionPane.showInputDialog(parent, "Pick a different name for your gradient:", "Gradient Name", JOptionPane.INFORMATION_MESSAGE);	
			}
			Gradient g = new Gradient(Color.white, new TreeMap<Integer, Color>(), Gradient.DEFAULT_SIZE, name);
			gradients.add(g);
			try {
				gradientProvider.addGradient(g);
			} catch (IOException e1) {
				throw new RuntimeException(e1);
			}
			gradientListModel.addElement(g);
			if(gradients.size() > 1) {
				removeGradient.setEnabled(true);
			}
		}
	};
	private final ActionListener removeListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			int selected = gradientList.getSelectedIndex();
			Gradient g = gradients.get(selected);
			gradients.remove(g);
			gradientListModel.removeElement(g);
			try {
				gradientProvider.removeGradient(g);
			} catch (IOException e1) {
				throw new RuntimeException(e1);
			}
			if(selected >= gradients.size()) {
				selected = gradients.size() - 1;
			}
			gradientList.setSelectedIndex(selected);
			if(gradients.size() == 1) {
				removeGradient.setEnabled(false);
			}
		}
	};
	private final ListSelectionListener listListener = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if(!e.getValueIsAdjusting()) {
				gradientProvider.save();
				int selected = gradientList.getSelectedIndex();
				pointEditPanel.setModel(gradients.get(selected >= 0 ? selected : 0));
			}
		}
	};
	
	private final GradientProvider gradientProvider;
	
	public GradientEditorPanel(Component parent, GradientProvider gradientProvider) {
		this.parent = parent;
		this.gradientProvider = gradientProvider;
		gradients = new ArrayList<Gradient>();
		for(Gradient gr : gradientProvider.getGradientList()) {
			gradients.add(gr);
		}
		setPreferredSize(new Dimension(840,500));
		
		gradientListModel = new DefaultListModel<Gradient>();
		for(Gradient gr : gradients) {
			gradientListModel.addElement(gr);
		}
		gradientList = new JList<Gradient>(gradientListModel);
		gradientList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		GradientListRenderer flr = new GradientListRenderer();
		gradientList.setCellRenderer(flr);
		gradientList.setBorder(BorderFactory.createBevelBorder(1));
		gradientList.setSelectedIndex(0);
		gradientList.addListSelectionListener(listListener);
		
		CellConstraints cc = new CellConstraints();
		FormLayout layout = new FormLayout("10dlu, pref, pref, 10dlu, pref, 10dlu",
											"10dlu, pref, 10dlu, pref, 10dlu, pref, 10dlu,");
		JPanel panel = new JPanel(layout);

		panel.add(new JLabel("Variations"), cc.xy(2, 2));
		panel.add(new JLabel("Details"), cc.xy(5, 2));
		
		JScrollPane scrollPane = new JScrollPane(gradientList);
		scrollPane.setPreferredSize(new Dimension(150,300));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		panel.add(scrollPane, cc.xyw(2, 4, 2));
		
		if(gradientListModel.isEmpty()) {
			pointEditPanel = new GradientPointEditPanel(null);
		} else {
			pointEditPanel = new GradientPointEditPanel(gradientListModel.get(0));
		}
		panel.add(pointEditPanel, cc.xy(5, 4));
		
		JPanel plusMinusPanel = new JPanel();
		addGradient = new JButton("+");
		addGradient.addActionListener(addListener);
		plusMinusPanel.add(addGradient);
		
		removeGradient = new JButton("-");
		removeGradient.addActionListener(removeListener);
		plusMinusPanel.add(removeGradient);
		if(gradients.size() == 0 || gradients.size() == 1) {
			removeGradient.setEnabled(false);
		}
		
		panel.add(plusMinusPanel, cc.xy(3,6));
		
		add(panel);
	}
}
