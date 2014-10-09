package net.gillessed.textadventure.datatype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;

import net.gillessed.textadventure.dataenums.CreatureProperties;
import net.gillessed.textadventure.dataenums.ItemProperties;
import net.gillessed.textadventure.utils.IconUtils;

public class ItemDescription extends DataType {
	private static final long serialVersionUID = -5210837887410900803L;

	private boolean rare;
	private long value;
	private final List<StatusEffect> effects;
	private final Map<ItemProperties, Integer> itemProperties;
	private final Map<CreatureProperties, Integer> creatureProperties;
	
	public ItemDescription(DataType parent) {
		super(parent);
		setRare(false);
		itemProperties = new HashMap<ItemProperties, Integer>();
		for(ItemProperties p : ItemProperties.values()) {
			itemProperties.put(p, 0);
		}
		creatureProperties = new HashMap<CreatureProperties, Integer>();
		for(CreatureProperties p : CreatureProperties.values()) {
			creatureProperties.put(p, 0);
		}
		effects = new ArrayList<StatusEffect>();
		value = 1;
	}
	
	public Map<ItemProperties, Integer> getItemProperties() {
		return itemProperties;
	}
	
	public Map<CreatureProperties, Integer> getCreatureProperties() {
		return creatureProperties;
	}

	public List<StatusEffect> getEffects() {
		return effects;
	}

	public boolean isRare() {
		return rare;
	}

	public void setRare(boolean rare) {
		this.rare = rare;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	@Override
	public Icon getIcon(int size) {
		return IconUtils.ITEM_ICON(size);
	}
	
	@Override
	public String getTypeName() {
		return "Item";
	}
}
