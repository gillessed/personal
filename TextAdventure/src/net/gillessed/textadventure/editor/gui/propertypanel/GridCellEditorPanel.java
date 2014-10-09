package net.gillessed.textadventure.editor.gui.propertypanel;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;

import net.gillessed.textadventure.dataenums.Direction;
import net.gillessed.textadventure.datatype.EnemyArea;
import net.gillessed.textadventure.datatype.FriendlyArea;
import net.gillessed.textadventure.datatype.GridCell;
import net.gillessed.textadventure.datatype.World;
import net.gillessed.textadventure.editor.gui.propertypanel.GridCellCanvas.Mode;
import net.gillessed.textadventure.utils.IconUtils;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

@SuppressWarnings("serial")
public class GridCellEditorPanel extends JPanel {
	
	private final MouseAdapter toggleCellMouseAdapter = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			int mx = e.getX();
			int my = e.getY();
			int cx = (mx - GridCellCanvas.MARGIN) / GridCellCanvas.CELL_SIZE;
			int cy = (my - GridCellCanvas.MARGIN) / GridCellCanvas.CELL_SIZE;
			GridCell oldCell = null;
			Point test = new Point(cx, cy);
			for(GridCell testCell : model) {
				if(testCell.getLocation().equals(test)) {
					oldCell = testCell;
					break;
				}
			}
			if(oldCell == null) {
				GridCell gridCell = new GridCell(null);
				gridCell.setLocation(test);
				for(GridCell existingCell : model) {
					int ex = existingCell.getLocation().x;
					int ey = existingCell.getLocation().y;
					if(ex == cx && ey == cy - 1) {
						gridCell.getGridCells().put(Direction.NORTH, existingCell);
						existingCell.getGridCells().put(Direction.SOUTH, gridCell);
					} else if(ex == cx && ey == cy + 1) {
						gridCell.getGridCells().put(Direction.SOUTH, existingCell);
						existingCell.getGridCells().put(Direction.NORTH, gridCell);
					} else if(ex == cx - 1 && ey == cy) {
						gridCell.getGridCells().put(Direction.WEST, existingCell);
						existingCell.getGridCells().put(Direction.EAST, gridCell);
					} else if(ex == cx + 1 && ey == cy) {
						gridCell.getGridCells().put(Direction.EAST, existingCell);
						existingCell.getGridCells().put(Direction.WEST, gridCell);
					}
				}
				model.add(gridCell);
			} else {
				oldCell.getGridCells().clear();
				model.remove(oldCell);
				oldCell.deleted();
			}
			gridCellCanvas.repaintRequest();
		}
	};
	
	private final MouseAdapter friendlyAreaMouseAdapter = new MouseAdapter() {
		@Override
		public void mouseClicked(final MouseEvent e) {
			new Thread() {
				@Override
				public void run() {
					addOrRemoveArea(e.getX(), e.getY(), Mode.FRIENDLY);
				}
			}.start();
		}
	};
	
	private final MouseAdapter enemyAreaMouseAdapter = new MouseAdapter() {
		@Override
		public void mouseClicked(final MouseEvent e) {
			new Thread() {
				@Override
				public void run() {
					addOrRemoveArea(e.getX(), e.getY(), Mode.ENEMY);
				}
			}.start();
		}
	};
	
	public void addOrRemoveArea(int mx, int my, Mode mode) {
		int hoveredX = (mx - GridCellCanvas.MARGIN) / GridCellCanvas.CELL_SIZE;
		int hoveredY = (my - GridCellCanvas.MARGIN) / GridCellCanvas.CELL_SIZE;
		pointMap.put(Direction.NORTH, new Point(GridCellCanvas.MARGIN + GridCellCanvas.CELL_SIZE * hoveredX + GridCellCanvas.CELL_SIZE / 2,
				GridCellCanvas.MARGIN + GridCellCanvas.CELL_SIZE * hoveredY));
		pointMap.put(Direction.SOUTH, new Point(GridCellCanvas.MARGIN + GridCellCanvas.CELL_SIZE * hoveredX + GridCellCanvas.CELL_SIZE / 2,
				GridCellCanvas.MARGIN + GridCellCanvas.CELL_SIZE * (hoveredY + 1)));
		pointMap.put(Direction.WEST, new Point(GridCellCanvas.MARGIN + GridCellCanvas.CELL_SIZE * hoveredX,
				GridCellCanvas.MARGIN + GridCellCanvas.CELL_SIZE * hoveredY + GridCellCanvas.CELL_SIZE / 2));
		pointMap.put(Direction.EAST, new Point(GridCellCanvas.MARGIN + GridCellCanvas.CELL_SIZE * (hoveredX + 1),
				GridCellCanvas.MARGIN + GridCellCanvas.CELL_SIZE * hoveredY + GridCellCanvas.CELL_SIZE / 2));
		double minDistance = Double.MAX_VALUE;
		Direction hoveredDirection = null;
		for(Map.Entry<Direction, Point> entry : pointMap.entrySet()) {
			double newDist = Point.distance(mx, my,
					pointMap.get(entry.getKey()).x, pointMap.get(entry.getKey()).y);
			if(newDist < minDistance) {
				minDistance = newDist;
				hoveredDirection = entry.getKey();
			}
		}
		GridCell cell = null;
		for(GridCell existingCell : model) {
			if(existingCell.getLocation().x == hoveredX && hoveredY == existingCell.getLocation().y) {
				cell = existingCell;
				break;
			}
		}
		if(cell == null) return;
		if(mode == Mode.FRIENDLY) {
			FriendlyArea oldArea = cell.getFriendlyExits().get(hoveredDirection);
			if(oldArea == null) {
				FriendlyArea newFriendlyArea = (FriendlyArea)AreaDialog.showAreaChooser(Mode.FRIENDLY,
						(World)currentArea.getParent(), null);
				if(newFriendlyArea != null) {
					cell.getFriendlyExits().put(hoveredDirection, newFriendlyArea);
				}
			} else {
				cell.getFriendlyExits().removeValue(oldArea);
			}
		} else if(mode == Mode.ENEMY) {
			EnemyArea oldArea = cell.getEnemyExits().get(hoveredDirection);
			if(oldArea == null) {
				EnemyArea newEnemyArea = (EnemyArea)AreaDialog.showAreaChooser(Mode.ENEMY,
						(World)currentArea.getParent(), currentArea);
				if(newEnemyArea != null) {
					cell.getEnemyExits().put(hoveredDirection, newEnemyArea);
				}
			} else {
				cell.getEnemyExits().removeValue(oldArea);
			}
		}
		gridCellCanvas.repaintRequest();
	}
	
	private final ButtonGroup buttonGroup;
	private final JToggleButton toggleCellStateButton;
	private final JToggleButton toggleFriendlyStateButton;
	private final JToggleButton toggleEnemyStateButton;
	private final GridCellCanvas gridCellCanvas;
	private Set<GridCell> model;
	private MouseAdapter currentMouseAdapter;
	private final Map<Direction, Point> pointMap;
	private final EnemyArea currentArea;

	public GridCellEditorPanel(Set<GridCell> model, EnemyArea currentArea) {
		this.model = model;
		this.currentArea = currentArea;
		pointMap = new HashMap<>();
		FormLayout layout = new FormLayout("10dlu, pref, 10dlu, pref, fill:pref:grow",
				"10dlu, 20dlu, 5dlu, 20dlu, 5dlu, 20dlu, fill:pref:grow, 10dlu");
		CellConstraints cc = new CellConstraints();
		setLayout(layout);
		
		buttonGroup = new ButtonGroup();
		
		toggleCellStateButton = new JToggleButton(IconUtils.TOGGLE_CELL_ICON_32);
		toggleCellStateButton.setText("Cell on/off");
		toggleCellStateButton.setSelected(true);
		toggleCellStateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(toggleCellStateButton.isSelected()) {
					gridCellCanvas.removeMouseListener(currentMouseAdapter);
					gridCellCanvas.addMouseListener(toggleCellMouseAdapter);
					gridCellCanvas.setMode(Mode.NONE);
					currentMouseAdapter = toggleCellMouseAdapter;
				}
			}
		});
		buttonGroup.add(toggleCellStateButton);
		add(toggleCellStateButton, cc.xy(2, 2));

		toggleFriendlyStateButton = new JToggleButton(IconUtils.FRIENDLY_AREA_ICON_32);
		toggleFriendlyStateButton.setText("Friendly Area");
		toggleFriendlyStateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(toggleFriendlyStateButton.isSelected()) {
					gridCellCanvas.removeMouseListener(currentMouseAdapter);
					gridCellCanvas.addMouseListener(friendlyAreaMouseAdapter);
					gridCellCanvas.setMode(Mode.FRIENDLY);
					currentMouseAdapter = friendlyAreaMouseAdapter;
				}
			}
		});
		buttonGroup.add(toggleFriendlyStateButton);
		add(toggleFriendlyStateButton, cc.xy(2, 4));

		toggleEnemyStateButton = new JToggleButton(IconUtils.ENEMY_AREA_ICON_32);
		toggleEnemyStateButton.setText("Enemy Area");
		toggleEnemyStateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(toggleEnemyStateButton.isSelected()) {
					gridCellCanvas.removeMouseListener(currentMouseAdapter);
					gridCellCanvas.addMouseListener(enemyAreaMouseAdapter);
					gridCellCanvas.setMode(Mode.ENEMY);
					currentMouseAdapter = enemyAreaMouseAdapter;
				}
			}
		});
		buttonGroup.add(toggleEnemyStateButton);
		add(toggleEnemyStateButton, cc.xy(2, 6));
		
		gridCellCanvas = new GridCellCanvas(model);
		add(gridCellCanvas, cc.xywh(4, 2, 1, 6));
		
		setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.black, 1), "Grid Cells"));
		
		currentMouseAdapter = toggleCellMouseAdapter;
		gridCellCanvas.addMouseListener(toggleCellMouseAdapter);
	}
	
	public void save() {
		
	}
}
