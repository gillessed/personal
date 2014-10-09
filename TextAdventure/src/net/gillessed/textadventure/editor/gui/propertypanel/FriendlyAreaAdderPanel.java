package net.gillessed.textadventure.editor.gui.propertypanel;

import java.util.List;

import javax.swing.JPanel;

import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.datatype.FriendlyArea;
import net.gillessed.textadventure.datatype.World;

@SuppressWarnings("serial")
public class FriendlyAreaAdderPanel extends AdderPanel<FriendlyArea, FriendlyAreaChooserPanel> {

	private final FriendlyArea currentArea;
	private final World world;

	public FriendlyAreaAdderPanel(FriendlyArea currentArea, World world, List<FriendlyArea> model, JPanel parent, String title) {
		super(model, parent, title);
		this.currentArea = currentArea;
		this.world = world;
		createGUI();
	}
	
	@Override
	protected FriendlyAreaChooserPanel getCreatorPanel(FriendlyArea s, int index) {
		return new FriendlyAreaChooserPanel(s, currentArea, world, this);
	}

	@Override
	protected FriendlyArea generateNewElement(int index) {
		List<FriendlyArea> allOtherAreas = DataType.getAllFriendlyAreas(world, currentArea);
		if(allOtherAreas.size() >= 1) {
			return allOtherAreas.get(0);
		} else {
			return null;
		}
	}
}
