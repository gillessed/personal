package net.gillessed.textadventure.datatype;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;

import net.gillessed.textadventure.dataenums.CreatureProperties;
import net.gillessed.textadventure.utils.IconUtils;

public class PlayableCharacter extends DataType {
	private static final long serialVersionUID = 1090958102713804354L;

	private final Map<CreatureProperties, Integer> creatureProperties;
	
	public PlayableCharacter(DataType parent) {
		super(parent);
		creatureProperties = new HashMap<CreatureProperties, Integer>();
		for(CreatureProperties p : CreatureProperties.values()) {
			creatureProperties.put(p, 0);
		}
	}

	@Override
	public Icon getIcon(int size) {
		return IconUtils.CHARACTERS_ICON(size);
	}

	public Map<CreatureProperties, Integer> getCreatureProperties() {
		return creatureProperties;
	}
	
	@Override
	public String getTypeName() {
		return "Character";
	}
}
