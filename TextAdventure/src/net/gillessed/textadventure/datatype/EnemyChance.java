package net.gillessed.textadventure.datatype;

import javax.swing.Icon;

import net.gillessed.textadventure.deletelistener.DeleteListener;

public class EnemyChance extends DataType {
	private static final long serialVersionUID = 444469113453509090L;
	
	private double enemyAmount;
	private EnemyDescription enemy;
	private final DeleteListener enemyListener;
	public EnemyChance() {
		super(null);
		enemyAmount = 0;
		enemyListener = new DeleteListener() {
			private static final long serialVersionUID = -4198887943036790648L;

			@Override
			public void deleted(DataType deleted) {
				setEnemy(null);
			}
		};
	}

	public EnemyDescription getEnemy() {
		return enemy;
	}

	public void setEnemy(EnemyDescription enemy) {
		if(this.enemy != null) {
			this.enemy.removeDeleteListener(enemyListener);
		}
		this.enemy = enemy;
		if(enemy != null) {
			enemy.addDeleteListener(enemyListener);
		}
	}

	public double getEnemyAmount() {
		return enemyAmount;
	}

	public void setEnemyAmount(double enemyAmount) {
		this.enemyAmount = enemyAmount;
	}

	@Override
	public Icon getIcon(int size) {
		return null;
	}
}
