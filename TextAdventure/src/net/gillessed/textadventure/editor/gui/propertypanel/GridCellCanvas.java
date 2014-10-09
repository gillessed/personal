package net.gillessed.textadventure.editor.gui.propertypanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import net.gillessed.textadventure.dataenums.Direction;
import net.gillessed.textadventure.datatype.EnemyArea;
import net.gillessed.textadventure.datatype.FriendlyArea;
import net.gillessed.textadventure.datatype.GridCell;
import net.gillessed.textadventure.utils.RepaintQueue;

@SuppressWarnings("serial")
public class GridCellCanvas extends JPanel {
	
	public enum Mode {
		NONE,
		FRIENDLY,
		ENEMY
	};

	public static final int EXIT_ARC = 3;
	public static final int EXIT_WIDTH = 5;
	public static final int EXIT_INSET = 3;
	public static final int MARGIN = 5;
	public static final int MAX_WIDTH = 20;
	public static final int MAX_HEIGHT = 20;
	public static final int CELL_SIZE = 24;

	private Mode mode;
	private int hoveredX;
	private int hoveredY;
	private Direction hoveredDirection;
	private final Map<Direction, Point> pointMap;
	
	private final Set<GridCell> model;
	private final int width;
	private final int height;
	private final RepaintQueue repaintQueue;

	public GridCellCanvas(final Set<GridCell> model) {
		this.model = model;
		pointMap = new HashMap<>();
		width = MARGIN * 2 + MAX_WIDTH * CELL_SIZE;
		height = MARGIN * 2 + MAX_HEIGHT * CELL_SIZE;
		setPreferredSize(new Dimension(width, height));
		setBorder(BorderFactory.createLineBorder(Color.black));
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				setMouse(e.getX(), e.getY());
				repaintRequest();
			}
		});
		repaintQueue = new RepaintQueue(this);
		repaintQueue.start();
	}
	
	protected void paintComponent(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.gray.brighter());
		for(int i = 0; i < MAX_WIDTH; i++) {
			for(int j = 0; j < MAX_HEIGHT; j++) {
				g.drawRect(MARGIN + i * CELL_SIZE, MARGIN + j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
			}
		}

		Stroke str = ((Graphics2D)g).getStroke();
		((Graphics2D)g).setStroke(new BasicStroke(3));
		for(GridCell gridCell : model) {
			int x = gridCell.getLocation().x;
			int y = gridCell.getLocation().y;
			g.setColor(Color.gray.brighter());
			int cx = MARGIN + x * CELL_SIZE;
			int cy = MARGIN + y * CELL_SIZE;
			g.fillRect(cx, cy, CELL_SIZE, CELL_SIZE);
			g.setColor(Color.black);
			g.drawRect(cx, cy, CELL_SIZE, CELL_SIZE);
			if(gridCell.getLocation().x == hoveredX && gridCell.getLocation().y == hoveredY) {
				if(mode == Mode.FRIENDLY) {
					g.setColor(new Color(0, 255, 0, 150));
					drawExit(cx, cy, hoveredDirection, g);
				} else if (mode == Mode.ENEMY) {
					g.setColor(new Color(255, 0, 0, 150));
					drawExit(cx, cy, hoveredDirection, g);
				}
			}
			for(Map.Entry<Direction, FriendlyArea> entry : gridCell.getFriendlyExits().entrySet()) {
				if(!(entry.getKey() == hoveredDirection && gridCell.getLocation().x == hoveredX &&
						gridCell.getLocation().y == hoveredY && mode == Mode.FRIENDLY)) {
					g.setColor(Color.green);
					drawExit(cx, cy, entry.getKey(), g);
				}
			}
			for(Map.Entry<Direction, EnemyArea> entry : gridCell.getEnemyExits().entrySet()) {
				if(!(entry.getKey() == hoveredDirection && gridCell.getLocation().x == hoveredX &&
						gridCell.getLocation().y == hoveredY && mode == Mode.ENEMY)) {
					g.setColor(Color.red);
					drawExit(cx, cy, entry.getKey(), g);
				}
			}
		}
		((Graphics2D)g).setStroke(str);
	}
	
	private void drawExit(int cx, int cy, Direction direction, Graphics g) {
		Rectangle drawRect = null;
		switch(direction) {
		case NORTH:
			drawRect = new Rectangle(cx + EXIT_INSET,
					cy + EXIT_INSET,
					CELL_SIZE - EXIT_INSET * 2,
					EXIT_WIDTH);
			break;
		case SOUTH:
			drawRect = new Rectangle(cx + EXIT_INSET,
					cy + CELL_SIZE - EXIT_INSET - EXIT_WIDTH,
					CELL_SIZE - EXIT_INSET * 2,
					EXIT_WIDTH);
			break;
		case WEST:
			drawRect = new Rectangle(cx + EXIT_INSET,
					cy + EXIT_INSET,
					EXIT_WIDTH,
					CELL_SIZE - EXIT_INSET * 2);
			break;
		case EAST:
			drawRect = new Rectangle(cx + CELL_SIZE - EXIT_INSET * 2 - 1,
					cy + EXIT_INSET,
					EXIT_WIDTH,
					CELL_SIZE - EXIT_INSET * 2);
			break;
		}
		g.fillRoundRect(drawRect.x, drawRect.y, drawRect.width, drawRect.height,
				EXIT_ARC, EXIT_ARC);
	}
	
	public void repaintRequest() {
		repaintQueue.addRepaintRequest();
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	public void setMouse(int mx, int my) {
		hoveredX = (mx - GridCellCanvas.MARGIN) / GridCellCanvas.CELL_SIZE;
		hoveredY = (my - GridCellCanvas.MARGIN) / GridCellCanvas.CELL_SIZE;
		pointMap.put(Direction.NORTH, new Point(MARGIN + CELL_SIZE * hoveredX + CELL_SIZE / 2,
				MARGIN + CELL_SIZE * hoveredY));
		pointMap.put(Direction.SOUTH, new Point(MARGIN + CELL_SIZE * hoveredX + CELL_SIZE / 2,
				MARGIN + CELL_SIZE * (hoveredY + 1)));
		pointMap.put(Direction.WEST, new Point(MARGIN + CELL_SIZE * hoveredX,
				MARGIN + CELL_SIZE * hoveredY + CELL_SIZE / 2));
		pointMap.put(Direction.EAST, new Point(MARGIN + CELL_SIZE * (hoveredX + 1),
				MARGIN + CELL_SIZE * hoveredY + CELL_SIZE / 2));
		double minDistance = Double.MAX_VALUE;
		for(Map.Entry<Direction, Point> entry : pointMap.entrySet()) {
			double newDist = Point.distance(mx, my,
					pointMap.get(entry.getKey()).x, pointMap.get(entry.getKey()).y);
			if(newDist < minDistance) {
				minDistance = newDist;
				hoveredDirection = entry.getKey();
			}
		}
	}
}
