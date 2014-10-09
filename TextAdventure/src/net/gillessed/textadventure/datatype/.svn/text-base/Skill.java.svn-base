package net.gillessed.textadventure.datatype;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;

import net.gillessed.textadventure.dataenums.CreatureProperties;
import net.gillessed.textadventure.dataenums.ItemProperties;
import net.gillessed.textadventure.utils.IconUtils;
import net.gillessed.textadventure.utils.ListenerList;

public class Skill extends DataType {
	private static final long serialVersionUID = 4418478462586746278L;
	
	private long hpCost;
	private long mpCost;
	private long level;
	private boolean standard;
	private final Map<ItemProperties, Integer> itemProperties;
	private final Map<CreatureProperties, Integer> creatureProperties;
	private final List<Attack> attacks;
	
	public Skill(DataType parent) {
		super(parent);
		creatureProperties = new HashMap<CreatureProperties, Integer>();
		for(CreatureProperties p : CreatureProperties.values()) {
			creatureProperties.put(p, 0);
		}
		itemProperties = new HashMap<ItemProperties, Integer>();
		for(ItemProperties p : ItemProperties.values()) {
			itemProperties.put(p, 0);
		}
		attacks = new ListenerList<Attack>();
	}
	public List<Attack> getAttacks() {
		return attacks;
	}
	public Map<CreatureProperties, Integer> getCreatureProperties() {
		return creatureProperties;
	}
	public Map<ItemProperties, Integer> getItemProperties() {
		return itemProperties;
	}
	public long getHpCost() {
		return hpCost;
	}
	public void setHpCost(long hpCost) {
		this.hpCost = hpCost;
	}
	public long getMpCost() {
		return mpCost;
	}
	public void setMpCost(long mpCost) {
		this.mpCost = mpCost;
	}
	public boolean isStandard() {
		return standard;
	}
	public void setStandard(boolean standard) {
		this.standard = standard;
	}
	public long getLevel() {
		return level;
	}
	public void setLevel(long level) {
		this.level = level;
	}
	
	@Override
	public Icon getIcon(int size) {
		return IconUtils.SKILLS_ICON(size);
	}
}
