package net.gillessed.textadventure.datatype;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.Icon;

import net.gillessed.textadventure.utils.IconUtils;
import net.gillessed.textadventure.utils.ListenerList;

public class EnemyArea extends DataType {
	private static final long serialVersionUID = 7387939327468158134L;
	
	private final Set<GridCell> gridCell;
	private final List<EnemyChance> enemyEncounterChances;
	
	public EnemyArea(DataType parent) {
		super(parent);
		gridCell = Collections.newSetFromMap(new ConcurrentHashMap<GridCell, Boolean>());
		enemyEncounterChances = new ListenerList<>();
	}

	public Set<GridCell> getGridCell() {
		return gridCell;
	}

	public List<EnemyChance> getEnemyEncounterChances() {
		return enemyEncounterChances;
	}
	
	@Override
	public Icon getIcon(int size) {
		return IconUtils.ENEMY_AREA_ICON(size);
	}
}
