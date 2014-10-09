package net.gillessed.textadventure.editor.gui.propertypanel;

import java.util.List;

import javax.swing.JPanel;

import net.gillessed.textadventure.datatype.EnemyChance;

@SuppressWarnings("serial")
public class EnemyChanceAdderPanel extends AdderPanel<EnemyChance, EnemyChanceCreatorPanel>{

	private final String chooserPanelTitle;

	public EnemyChanceAdderPanel(List<EnemyChance> model, JPanel parent, String title, String chooserPanelTitle) {
		super(model, parent, title);
		this.chooserPanelTitle = chooserPanelTitle;
		createGUI();
	}

	@Override
	protected EnemyChanceCreatorPanel getCreatorPanel(EnemyChance s, int index) {
		return new EnemyChanceCreatorPanel(s, this, chooserPanelTitle);
	}

	@Override
	protected EnemyChance generateNewElement(int index) {
		return new EnemyChance();
	}

}
