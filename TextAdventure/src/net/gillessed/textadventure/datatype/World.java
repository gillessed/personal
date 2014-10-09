package net.gillessed.textadventure.datatype;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Icon;

import net.gillessed.textadventure.deletelistener.DeleteListener;
import net.gillessed.textadventure.utils.IconUtils;

public class World extends DataType {
	private static final long serialVersionUID = -5078389929349671429L;
	
	private final Set<FriendlyArea> friendlyAreas;
	private final Set<EnemyArea> enemyAreas;
	public World(DataType parent) {
		super(parent);
		friendlyAreas = new HashSet<FriendlyArea>();
		enemyAreas = new HashSet<EnemyArea>();
	}
	
	public Set<FriendlyArea> getFriendlyAreas() {
		return Collections.unmodifiableSet(friendlyAreas);
	}
	
	public void addFriendlyArea(final FriendlyArea friendlyArea) {
		friendlyAreas.add(friendlyArea);
		friendlyArea.setParent(this);
		friendlyArea.addDeleteListener(new DeleteListener() {
			private static final long serialVersionUID = -875572359409779685L;

			@Override
			public void deleted(DataType deleted) {
				friendlyAreas.remove(friendlyArea);
				friendlyArea.setParent(null);
			}
		});
	}
	
	public Set<EnemyArea> getEnemyAreas() {
		return Collections.unmodifiableSet(enemyAreas);
	}
	
	public void addEnemyArea(final EnemyArea enemyArea) {
		enemyAreas.add(enemyArea);
		enemyArea.addDeleteListener(new DeleteListener() {
			private static final long serialVersionUID = 7627782573127716365L;

			@Override
			public void deleted(DataType deleted) {
				enemyAreas.remove(enemyArea);
			}
		});
	}
	
	@Override
	public void deleted() {
		Set<FriendlyArea> tempFriendlyAreas = new HashSet<FriendlyArea>();
		tempFriendlyAreas.addAll(friendlyAreas);
		for(FriendlyArea fa : tempFriendlyAreas) {
			fa.deleted();
		}
		Set<EnemyArea> tempEnemyAreas = new HashSet<EnemyArea>();
		tempEnemyAreas.addAll(enemyAreas);
		for(EnemyArea ea : tempEnemyAreas) {
			ea.deleted();
		}
		super.deleted();
	}
	
	@Override
	public Icon getIcon(int size) {
		return IconUtils.WORLDS_ICON(size);
	}
}
