package net.gillessed.textadventure.datatype;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;

import net.gillessed.textadventure.dataenums.CreatureProperties;
import net.gillessed.textadventure.utils.IconUtils;
import net.gillessed.textadventure.utils.ListenerList;

public class EnemyDescription extends DataType {
	private static final long serialVersionUID = 2369624310556707315L;

	private final Map<CreatureProperties, Integer> creatureProperties;
	private long goldDrop;
	private long exp;
	private final List<ItemBundle> itemDrops;
	private final List<EventEffect<? extends DataType>> eventEffects;
	
	public EnemyDescription(DataType parent) {
		super(parent);
		creatureProperties = new HashMap<CreatureProperties, Integer>();
		for(CreatureProperties p : CreatureProperties.values()) {
			creatureProperties.put(p, 0);
		}
		itemDrops = new ListenerList<>();
		eventEffects = new ListenerList<>();
	}
	
	public List<EventEffect<? extends DataType>> getEventEffects() {
		return eventEffects;
	}
	
	@Override
	public Icon getIcon(int size) {
		return IconUtils.ENEMIES_ICON(size);
	}
	
	@Override
	public String getTypeName() {
		return "Enemy";
	}

	public Map<CreatureProperties, Integer> getCreatureProperties() {
		return creatureProperties;
	}

	public long getGoldDrop() {
		return goldDrop;
	}

	public void setGoldDrop(long goldDrop) {
		this.goldDrop = goldDrop;
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public List<ItemBundle> getItemDrops() {
		return itemDrops;
	}
}
