package net.gillessed.textadventure.datatype;

import java.awt.Point;

import javax.swing.Icon;

import net.gillessed.textadventure.dataenums.Direction;
import net.gillessed.textadventure.deletelistener.DeleteListener;
import net.gillessed.textadventure.utils.ListenerMap;

public class GridCell extends DataType {
	
	private static final long serialVersionUID = 2129017985844083886L;
	
	private Point location;
	
	private final ListenerMap<Direction, GridCell> gridCells;
	private final ListenerMap<Direction, FriendlyArea> friendlyExits;
	private final ListenerMap<Direction, EnemyArea> enemyExits;
	private final DeleteListener gridCellListener;
	private final DeleteListener friendlyAreaListener;
	private final DeleteListener enemyAreaListener;
	
	public GridCell(EnemyArea parent) {
		super(parent);
		gridCellListener = new DeleteListener() {
			private static final long serialVersionUID = 25462457233331L;

			@Override
			public void deleted(DataType deleted) {
				gridCells.removeValue(deleted);
			}
		};
		gridCells = new ListenerMap<Direction, GridCell>(gridCellListener);
		friendlyAreaListener = new DeleteListener() {
			private static final long serialVersionUID = 25462457233331L;

			@Override
			public void deleted(DataType deleted) {
				friendlyExits.removeValue(deleted);
			}
		};
		friendlyExits = new ListenerMap<Direction, FriendlyArea>(friendlyAreaListener);
		enemyAreaListener = new DeleteListener() {
			private static final long serialVersionUID = 25462457233331L;

			@Override
			public void deleted(DataType deleted) {
				enemyExits.removeValue(deleted);
			}
		};
		enemyExits = new ListenerMap<Direction, EnemyArea>(enemyAreaListener);
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}
	
	@Override
	public Icon getIcon(int size) {
		return null;
	}

	public ListenerMap<Direction, GridCell> getGridCells() {
		return gridCells;
	}

	public ListenerMap<Direction, FriendlyArea> getFriendlyExits() {
		return friendlyExits;
	}

	public ListenerMap<Direction, EnemyArea> getEnemyExits() {
		return enemyExits;
	}
}
