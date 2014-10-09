package net.gillessed.textadventure.editor.gui.propertypanel;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.editor.gui.editorpanel.ModelEditor;
import net.gillessed.textadventure.utils.IconUtils;

@SuppressWarnings("serial")
public abstract class AdderPanel<M extends DataType, P extends ModelEditor<M>> extends JPanel {
	
	protected static int ALREADY_CREATED = -2;
	
	private class AddButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			P newEffectCreatorPanel = getCreatorPanelWithBorder(generateNewElement(-1), -1);
			effectSubPanel.add(newEffectCreatorPanel);
			creatorPanels.add(newEffectCreatorPanel);
			if(canInsert == true) {
				JPanel insertPanel = getInsertPanel(insertButtons.size());
				effectSubPanel.add(insertPanel);
			}
			parent.validate();
			parent.repaint();
		}
	}
	
	private class InsertButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int listIndex = insertButtons.indexOf(e.getSource());
			P newCreatorPanel = getCreatorPanelWithBorder(generateNewElement(listIndex), listIndex);
			JPanel insertPanel = getInsertPanel(listIndex);
			
			if(listIndex >= creatorPanels.size()) {
				creatorPanels.add(newCreatorPanel);
			} else {
				creatorPanels.add(listIndex + 1, newCreatorPanel);
			}
			
			int componentIndex = listIndex * 2 + 1;
			if(componentIndex < 0) {
				throw new RuntimeException("This should never be negative!");
			}
			effectSubPanel.add(insertPanel, componentIndex + 1);
			effectSubPanel.add(newCreatorPanel, componentIndex + 1);
			parent.validate();
			parent.repaint();
		}
	}
	
	private final boolean canInsert;
	private final String title;
	protected final List<M> model;
	protected final List<P> creatorPanels; 
	protected final List<JPanel> insertPanels;
	protected final List<JButton> insertButtons;
	protected final JPanel parent;
	protected JPanel effectSubPanel;
	protected JPanel addPanel;
	protected JButton addNewButton;

	public AdderPanel(List<M> model, final JPanel parent, String title) {
		this(model, parent, title, false);
	}
	
	public AdderPanel(List<M> model, final JPanel parent, String title, boolean canInsert) {
		this.title = title;
		this.model = model;
		this.parent = parent;
		this.canInsert = canInsert;
		creatorPanels = new ArrayList<>();
		insertButtons  = new ArrayList<>();
		insertPanels = new ArrayList<>();
	}
	
	protected void createGUI() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		effectSubPanel = new JPanel();
		effectSubPanel.setLayout(new BoxLayout(effectSubPanel, BoxLayout.Y_AXIS));
		int index = 0;
		for(M t : model) {
			P newCreatorPanel = getCreatorPanelWithBorder(t, ALREADY_CREATED);
			effectSubPanel.add(newCreatorPanel);
			creatorPanels.add(newCreatorPanel);
			effectSubPanel.add(getInsertPanel(index));
			index++;
		}
		add(effectSubPanel);
		addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		addToAddPanel();
		addNewButton = new JButton("Add");
		addNewButton.setIcon(IconUtils.ADD_ICON_16);
		addNewButton.addActionListener(new AddButtonListener());
		addPanel.add(addNewButton);
		add(addPanel);
		setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.black, 1), title));
	}
	
	public void save(boolean deleted) {
		if(deleted) {
			for(P effectCreatorPanel : creatorPanels) {
				if(effectCreatorPanel.getModel() != null) {
					effectCreatorPanel.getModel().deleted();
				}
			}
		}
		model.clear();
		for(P effectCreatorPanel : creatorPanels) {
			effectCreatorPanel.save();
			if(effectCreatorPanel.getModel() != null) {
				model.add(effectCreatorPanel.getModel());
			}
		}
	}

	public void delete(P panel) {
		if(canInsert) {
			int index = creatorPanels.indexOf(panel);
			effectSubPanel.remove(insertPanels.get(index));
			insertPanels.remove(index);
			insertButtons.remove(index);
			deletedInsertPanel(index);
		}
		creatorPanels.remove(panel);
		effectSubPanel.remove(panel);
		if(panel.getModel() != null) {
			model.remove(panel.getModel());
		}
		parent.validate();
		parent.repaint();
	}
	
	public List<M> getModel() {
		return model;
	}
	
	private P getCreatorPanelWithBorder(M s, int index) {
		P t = getCreatorPanel(s, index);
		t.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 3, 3, 3),
				BorderFactory.createRaisedSoftBevelBorder()));
		return t;
	}
	
	private JPanel getInsertPanel(int index) {
		JPanel insertPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		addToInsertPanel(insertPanel, index);
		JButton insertButton = new JButton("Insert");
		insertButton.setIcon(IconUtils.ADD_ICON_16);
		insertButton.addActionListener(new InsertButtonListener());
		if(index + 1 >= insertButtons.size()) {
			insertButtons.add(insertButton);
			insertPanels.add(insertPanel);
		} else {
			insertButtons.add(index + 1, insertButton);
			insertPanels.add(index + 1, insertPanel);
		}
		insertPanel.add(insertButton);
		return insertPanel;
	}
	
	public JPanel getParentPanel() {
		return parent;
	}
	
	protected void deletedInsertPanel(int index) {}
	protected void addToAddPanel() {}
	protected void addToInsertPanel(JPanel insertPanel, int index) {}
	protected abstract P getCreatorPanel(M s, int index);
	protected abstract M generateNewElement(int index);
}
