package net.gillessed.textadventure.editor.gui.custom;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.editor.gui.EditorFrame;
import net.gillessed.textadventure.utils.IconUtils;

@SuppressWarnings("serial")
public class UniverseTreeCellRenderer extends DefaultTreeCellRenderer {
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,row, hasFocus);
		
		Object userObject = ((DefaultMutableTreeNode)value).getUserObject();
		
		if(userObject instanceof DataType) {
			setIcon(((DataType)userObject).getIcon(16));
		} else {
			switch((String)userObject) {
			case EditorFrame.WORLDS_NODE_NAME:
				setIcon(IconUtils.WORLDS_ICON_16);
				break;
			case EditorFrame.EVENTS_NODE_NAME:
				setIcon(IconUtils.EVENTS_ICON_16);
				break;
			case EditorFrame.ENEMIES_NODE_NAME:
				setIcon(IconUtils.ENEMIES_ICON_16);
				break;
			case EditorFrame.ITEMS_NODE_NAME:
				setIcon(IconUtils.ITEM_ICON_16);
				break;
			case EditorFrame.SKILLS_NODE_NAME:
				setIcon(IconUtils.SKILLS_ICON_16);
				break;
			case EditorFrame.CHARACTERS_NODE_NAME:
				setIcon(IconUtils.CHARACTERS_ICON_16);
				break;
			case EditorFrame.VARIABLES_NODE_NAME:
				setIcon(IconUtils.VARIABLE_ICON_16);
				break;
			case EditorFrame.FRIENDLY_AREAS_NODE_NAME:
				setIcon(IconUtils.FRIENDLY_AREA_ICON_16);
				break;
			case EditorFrame.ENEMY_AREAS_NODE_NAME:
				setIcon(IconUtils.ENEMY_AREA_ICON_16);
				break;
			case EditorFrame.SHOPS_NODE_NAME:
				setIcon(IconUtils.SHOP_ICON_16);
				break;
			case EditorFrame.INTERACTIONS_NODE_NAME:
				setIcon(IconUtils.INTERACTIONS_ICON_16);
				break;
			}
		}
		
		return this;
	}
}
