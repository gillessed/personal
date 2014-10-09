package net.gillessed.textadventure.editor.gui.propertypanel;

import java.util.List;

import javax.swing.JPanel;

import net.gillessed.textadventure.datatype.ItemBundle;

@SuppressWarnings("serial")
public class ItemBundleAdderPanel extends AdderPanel<ItemBundle, ItemBundleCreatorPanel>{

	private final String chooserPanelTitle;

	public ItemBundleAdderPanel(List<ItemBundle> model, JPanel parent, String title, String chooserPanelTitle) {
		super(model, parent, title);
		this.chooserPanelTitle = chooserPanelTitle;
		createGUI();
	}

	@Override
	protected ItemBundleCreatorPanel getCreatorPanel(ItemBundle s, int index) {
		return new ItemBundleCreatorPanel(s, this, chooserPanelTitle);
	}

	@Override
	protected ItemBundle generateNewElement(int index) {
		return new ItemBundle();
	}

}
