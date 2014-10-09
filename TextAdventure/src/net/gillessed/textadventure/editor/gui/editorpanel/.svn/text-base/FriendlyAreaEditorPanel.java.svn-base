package net.gillessed.textadventure.editor.gui.editorpanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;

import net.gillessed.textadventure.datatype.FriendlyArea;
import net.gillessed.textadventure.datatype.Interaction;
import net.gillessed.textadventure.datatype.Shop;
import net.gillessed.textadventure.datatype.World;
import net.gillessed.textadventure.editor.gui.EditorFrame;
import net.gillessed.textadventure.editor.gui.propertypanel.EnemyAreaAdderPanel;
import net.gillessed.textadventure.editor.gui.propertypanel.EventAdderPanel;
import net.gillessed.textadventure.editor.gui.propertypanel.FriendlyAreaAdderPanel;
import net.gillessed.textadventure.utils.IconUtils;

@SuppressWarnings("serial")
public class FriendlyAreaEditorPanel extends DataEditorPanel<FriendlyArea>{

	private final JCheckBox isSpacePortCheckBox;
	
	private final JButton newShopButton;
	private final JButton newInteractionButton;
	private final EventAdderPanel eventAdderPanel;
	private final FriendlyAreaAdderPanel friendlyAreaAdderPanel;
	private final EnemyAreaAdderPanel enemyAreaAdderPanel;

	public FriendlyAreaEditorPanel(FriendlyArea model, EditorFrame editorFrame) {
		super(model, editorFrame, 7, 2);
		
		generateNameDescFields();
		isSpacePortCheckBox = new JCheckBox();
		isSpacePortCheckBox.setSelected(model.isSpacePort());
		getPropertyPanel().addProperty("Is Space Port: ", isSpacePortCheckBox);

		eventAdderPanel = new EventAdderPanel(null, model.getImmediateEvents(), this, "Immediate Events");
		getPropertyPanel().addSubPanel(eventAdderPanel);
		
		friendlyAreaAdderPanel = new FriendlyAreaAdderPanel(model, (World)model.getParent(), model.getConnectedFriendlyAreas(), this, "Connected Friendly Areas");
		getPropertyPanel().addSubPanel(friendlyAreaAdderPanel);
		
		enemyAreaAdderPanel = new EnemyAreaAdderPanel(null, (World)model.getParent(), model.getConnectedEnemyAreas(), this, "Connected Enemy Areas");
		getPropertyPanel().addSubPanel(enemyAreaAdderPanel);
		
		generateSaveDeletePanel(true);
		
		newShopButton = new JButton("New Shop");
		newShopButton.setIcon(IconUtils.SHOP_ICON_16);
		newShopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Shop newShop = new Shop(FriendlyAreaEditorPanel.this.model);
				FriendlyAreaEditorPanel.this.model.
					addShop(newShop);
				FriendlyAreaEditorPanel.this.editorFrame.
					newDataSelected(newShop);
			}
		});
		addNewButton(newShopButton);
		
		newInteractionButton = new JButton("New Interaction");
		newInteractionButton.setIcon(IconUtils.INTERACTIONS_ICON_16);
		newInteractionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Interaction newInteraction = new Interaction(FriendlyAreaEditorPanel.this.model);
				FriendlyAreaEditorPanel.this.model.
					addInteraction(newInteraction);
				FriendlyAreaEditorPanel.this.editorFrame.
					newDataSelected(newInteraction);
			}
		});
		addNewButton(newInteractionButton);
	}

	@Override
	public void save() {
		saveNameAndDescription();
		model.setSpacePort(isSpacePortCheckBox.isSelected());
		eventAdderPanel.save(false);
		friendlyAreaAdderPanel.save(false);
		enemyAreaAdderPanel.save(false);
	}

	@Override
	public void delete() {
		model.deleted();
	}

}
