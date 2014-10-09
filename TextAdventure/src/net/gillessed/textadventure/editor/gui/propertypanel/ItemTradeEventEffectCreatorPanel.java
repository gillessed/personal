package net.gillessed.textadventure.editor.gui.propertypanel;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import net.gillessed.textadventure.datatype.ItemTradeEventEffect;

@SuppressWarnings("serial")
public class ItemTradeEventEffectCreatorPanel extends CreatorPanel<ItemTradeEventEffect> {

	private final JSpinner goldSpinner;
	private final ItemBundleAdderPanel itemBundleAdderPanel;
	
	public ItemTradeEventEffectCreatorPanel(ItemTradeEventEffect model, EventEffectAdderPanel parent) {
		super(model, parent);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel goldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		goldSpinner = new JSpinner(new SpinnerNumberModel(model.getGold(), Long.MIN_VALUE, Long.MAX_VALUE, 1L));
		goldPanel.add(new JLabel("Gold: "));
		goldPanel.add(goldSpinner);
		add(goldPanel);
		
		itemBundleAdderPanel = new ItemBundleAdderPanel(model.getItemBundles(), parent.getParentPanel(), "Items", "Amount");
		add(itemBundleAdderPanel);

		JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		deletePanel.add(deleteButton);
		add(deletePanel);
	}

	@Override
	public void save() {
		model.setGold(((SpinnerNumberModel)goldSpinner.getModel()).getNumber().longValue());
		itemBundleAdderPanel.save(true);
	}
}
