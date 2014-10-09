package net.gillessed.textadventure.editor.gui.editorpanel;

import java.awt.Dimension;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import net.gillessed.textadventure.datatype.Shop;
import net.gillessed.textadventure.editor.gui.EditorFrame;
import net.gillessed.textadventure.editor.gui.propertypanel.ItemBundleAdderPanel;

@SuppressWarnings("serial")
public class ShopEditorPanel extends DataEditorPanel<Shop> {
	
	private final JSpinner buySpinner;
	private final JSpinner sellSpinner;
	private final ItemBundleAdderPanel itemBundleAdderPanel;
	
	public ShopEditorPanel(Shop model, EditorFrame editorFrame) {
		super(model, editorFrame, 6, 0);
		
		generateNameDescFields();
		
		buySpinner = new JSpinner(new SpinnerNumberModel(model.getBuyFactor(), 0, Double.MAX_VALUE, 0.1d));
		buySpinner.setPreferredSize(new Dimension(160, 20));
		getPropertyPanel().addProperty("Buy Factor: ", buySpinner);
		
		sellSpinner = new JSpinner(new SpinnerNumberModel(model.getSellFactor(), 0, Double.MAX_VALUE, 0.1d));
		sellSpinner.setPreferredSize(new Dimension(160, 20));
		getPropertyPanel().addProperty("Sell Factor: ", sellSpinner);
		
		itemBundleAdderPanel = new ItemBundleAdderPanel(model.getInventory(), this, "Inventory", "Amount");
		getPropertyPanel().addSubPanel(itemBundleAdderPanel);
		
		generateSaveDeletePanel(true);
	}

	@Override
	public void save() {
		saveNameAndDescription();
		model.setBuyFactor(((SpinnerNumberModel)buySpinner.getModel()).getNumber().doubleValue());
		model.setSellFactor(((SpinnerNumberModel)sellSpinner.getModel()).getNumber().doubleValue());
		itemBundleAdderPanel.save(true);
	}

	@Override
	public void delete() {
		model.deleted();
	}
}
