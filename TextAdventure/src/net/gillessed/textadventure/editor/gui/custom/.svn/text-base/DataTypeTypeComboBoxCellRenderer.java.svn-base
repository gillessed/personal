package net.gillessed.textadventure.editor.gui.custom;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import net.gillessed.textadventure.datatype.EnemyArea;
import net.gillessed.textadventure.datatype.FriendlyArea;
import net.gillessed.textadventure.datatype.Interaction;
import net.gillessed.textadventure.datatype.PlayableCharacter;
import net.gillessed.textadventure.datatype.Shop;
import net.gillessed.textadventure.utils.IconUtils;

@SuppressWarnings("serial")
public class DataTypeTypeComboBoxCellRenderer extends DefaultListCellRenderer {
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected,cellHasFocus);
		if(Shop.class.equals(value)) {
			setText("Shop");
			setIcon(IconUtils.SHOP_ICON_16);
		} else if(FriendlyArea.class.equals(value)) {
			setText("Friendly Area");
			setIcon(IconUtils.FRIENDLY_AREA_ICON_16);
		} else if(EnemyArea.class.equals(value)) {
			setText("Enemy Area");
			setIcon(IconUtils.ENEMY_AREA_ICON_16);
		} else if(Interaction.class.equals(value)) {
			setText("Interaction");
			setIcon(IconUtils.INTERACTIONS_ICON_16);
		} else if(PlayableCharacter.class.equals(value)) {
			setText("Character");
			setIcon(IconUtils.CHARACTERS_ICON_16);
		}
		return this;
	}
}
