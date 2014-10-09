package net.gillessed.textadventure.editor.gui.propertypanel;

import java.util.List;

import javax.swing.JPanel;

import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.datatype.EnemyArea;
import net.gillessed.textadventure.datatype.World;

@SuppressWarnings("serial")
public class EnemyAreaAdderPanel extends AdderPanel<EnemyArea, EnemyAreaChooserPanel> {

	private final EnemyArea currentArea;
	private final World world;


	public EnemyAreaAdderPanel(EnemyArea currentArea, World world, List<EnemyArea> model, JPanel parent, String title) {
		super(model, parent, title);
		this.currentArea = currentArea;
		this.world = world;
		createGUI();
	}
	
	@Override
	protected EnemyAreaChooserPanel getCreatorPanel(EnemyArea s, int index) {
		return new EnemyAreaChooserPanel(s, currentArea, world, this);
	}

	@Override
	protected EnemyArea generateNewElement(int index) {
		List<EnemyArea> allOtherAreas = DataType.getAllEnemyAreas(world, currentArea);
		if(allOtherAreas.size() >= 1) {
			return allOtherAreas.get(0);
		} else {
			return null;
		}
	}
}
