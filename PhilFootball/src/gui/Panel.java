package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.JPanel;

import brain.Brain;
import brain.Brain.WinDirection;
import football.Jump;
import football.PhilFootBall;

@SuppressWarnings("serial")
public class Panel extends JPanel implements MouseListener, MouseMotionListener {
	
	private static final double BORDER = 25;
	
	private PhilFootBall game;
	
	private int mx;
	private int my;
	private MouseMode mouseMode;
	
	private boolean player1Human;
	private boolean player2Human;
	private Brain p1;
	private Brain p2;
	private boolean hasJumped;
	
	private int turn;

	public Panel(PhilFootBall game, Brain p1, Brain p2) {
		this.game = game;
		mouseMode = MouseMode.WAIT;
		setPreferredSize(new Dimension(500, 630));
		addMouseListener(this);
		addMouseMotionListener(this);
		if(p1 == null) {
			player1Human = true;
		} else {
			this.p1 = p1;
			player1Human = false;
		}
		if(p2 == null) {
			player2Human = true;
		} else {
			this.p2 = p2;
			player2Human = false;
		}
		turn = 0;
		hasJumped = false;
	}
	
	public void start() {
		nextMove();
	}
	
	private Point getBoardCoords(int screenX, int screenY) {
		double gap = (getWidth() - 2 * BORDER) / game.getWidth();
		double tx = (screenX - BORDER) / gap;
		double ty;
		if(BORDER > screenY) {
			ty = (screenY - BORDER) / gap - 1;
		} else {
			ty = (screenY - BORDER) / gap;
		}
		return new Point((int)tx, (int)ty);
	}
	
	@Override
	protected void paintComponent(Graphics g2) {
		Graphics2D g = (Graphics2D)g2;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.setColor(new Color(255, 180, 0));
		g.fillRect(0, 0, getWidth(), getHeight());
		
		double gap = (getWidth() - 2 * BORDER) / game.getWidth();
		
		g.setColor(Color.black);
		for(int i = 0; i < game.getHeight(); i++) {
			g.drawLine((int)(BORDER + gap / 2), (int)(BORDER + gap * (i + 0.5)),
					(int)(getWidth() - BORDER - gap / 2), (int)(BORDER + gap * (i + 0.5)));
		}
		for(int i = 0; i < game.getWidth(); i++) {
			g.drawLine((int)(BORDER + gap * (i + 0.5)), (int)(BORDER + gap / 2), 
					(int)(BORDER + gap * (i + 0.5)), (int)(BORDER + gap * (game.getHeight() - 0.5)));
		}
		g.fillOval((int)(BORDER + gap * game.getWidth() / 2 - 3), (int)(BORDER + gap * game.getHeight() / 2 - 3), 7, 7);
		
		double radius = gap / 2.2;
		
		for(int i = 0; i < game.getWidth(); i++) {
			for(int j = 0; j < game.getHeight(); j++) {
				if(game.getState(i, j) == 1) {
					g.fillOval((int)(BORDER + gap * (i + 0.5) - radius), (int)(BORDER + gap * (j + 0.5) - radius),
							(int)(2 * radius), (int)(2 * radius));
				}
			}
		}
		
		Point ballCoords = game.getBallCoords();
		if(ballCoords.x >= 0 && ballCoords.x < game.getWidth() && ballCoords.y >= 0 && ballCoords.y < game.getHeight()) {
			g.setColor(Color.white);
			g.fillOval((int)(BORDER + gap * (ballCoords.x + 0.5) - radius), (int)(BORDER + gap * (ballCoords.y + 0.5) - radius),
					(int)(2 * radius), (int)(2 * radius));
		}
		
		if(mouseMode == MouseMode.ADD_STONE) {
			Point next = getBoardCoords(mx, my);
			if(next.x >= 0 && next.x < game.getWidth() && next.y >= 0 && next.y < game.getHeight()) {
				if(game.getState(next.x, next.y) == 0) {
					if(next.x == ballCoords.x && next.y == ballCoords.y) {
						drawPossibleJumps(g, false);
					} else {
						g.setColor(new Color(0, 0, 0, 150));
						g.fillOval((int)(BORDER + gap * (next.x + 0.5) - radius), (int)(BORDER + gap * (next.y + 0.5) - radius),
								(int)(2 * radius), (int)(2 * radius));
					}
				}
			}
		} else if(mouseMode == MouseMode.JUMP) {
			Point next = getBoardCoords(mx, my);
			if(isPossibleJump(next.x, next.y) != null && next.x >= 0 && next.x < game.getWidth() && next.y >= -1 && next.y <= game.getHeight()) {
				g.setColor(new Color(255, 255, 255, 150));
				g.fillOval((int)(BORDER + gap * (next.x + 0.5) - radius), (int)(BORDER + gap * (next.y + 0.5) - radius),
						(int)(2 * radius), (int)(2 * radius));
			}
			drawPossibleJumps(g, true);
		}
		
		if(mouseMode == MouseMode.WIN) {
			g.setColor(Color.black);
			g.setFont(new Font("ARIAL", Font.BOLD, 32));
			String draw = "Player " + (game.isWin() + 1) + " wins!";
			int length = g.getFontMetrics().stringWidth(draw);
			g.drawString(draw, getWidth() / 2 - length / 2, getHeight() / 2 + 16);
		} else {
			g.setColor(Color.black);
			g.setFont(new Font("ARIAL", Font.BOLD, 14));
			String stateString = "";
			switch(mouseMode) {
			case ADD_STONE:
				stateString = "Place Stone"; break;
			case JUMP:
				stateString = "Jump"; break;
			case WAIT:
				stateString = "Wait"; break;
			default:
				stateString = ""; break;
			}
			if(turn == 0) {
				g.drawString("Player 1 (" + stateString + ") ^", 10, 22);
			} else if(turn == 1) {
				g.drawString("Player 2 (" + stateString + ") v", 10, 22);
			}
		}
	}

	private Jump isPossibleJump(int x, int y) {
		List<Jump> possibleJumps = game.getPossibleJumps();
		Point ballCoords = game.getBallCoords();
		for(Jump jump : possibleJumps) {
			int tx = ballCoords.x + jump.length * jump.dir.x;
			int ty = ballCoords.y + jump.length * jump.dir.y;
			if(tx >= 0 && tx < game.getWidth() && ty >= -1 && ty <= game.getHeight() && tx == x && ty == y) {
				return jump;
			}
		}
		return null;
	}

	private void drawPossibleJumps(Graphics2D g, boolean full) {
		g.setColor(new Color(255, 0, 0, (full ? 255 : 150)));
		List<Jump> possibleJumps = game.getPossibleJumps();
		Point ballCoords = game.getBallCoords();
		double gap = (getWidth() - 2 * BORDER) / game.getWidth();
		double radius = gap / 4;
		for(Jump jump : possibleJumps) {
			int x = ballCoords.x + jump.length * jump.dir.x;
			int y = ballCoords.y + jump.length * jump.dir.y;
			g.fillRect((int)(BORDER + gap * (x + 0.5) - radius), (int)(BORDER + gap * (y + 0.5) - radius),
					(int)(2 * radius), (int)(2 * radius));
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		double gap = (getWidth() - 2 * BORDER) / game.getWidth();
		int x = (int)((e.getX() - BORDER) / gap);
		int y = (int)((e.getY() - BORDER) / gap);
		
		if(mouseMode == MouseMode.ADD_STONE) {
			if(x >= 0 && x < game.getWidth() && y >= 0 && y < game.getHeight()) {
				if(game.getState(x, y) == 0 && !game.isBall(x, y)) {
					game.addStone(x, y);
					turn = 1 - turn;
					nextMove();
				} else if(game.isBall(x, y)) {
					mouseMode = MouseMode.JUMP;
				}
			}
		} else if(mouseMode == MouseMode.JUMP) {
			if(x >= 0 && x < game.getWidth() && y >= -1 && y <= game.getHeight()) {
				if(game.isBall(x, y)) {
					if(hasJumped){
						turn = 1 - turn;
						hasJumped = false;
						nextMove();
					} else {
						mouseMode = MouseMode.ADD_STONE;
					}
				} else {
					Point boardCoords = getBoardCoords(e.getX(), e.getY());
					Jump dir = isPossibleJump(boardCoords.x, boardCoords.y);
					if(dir != null) {
						game.jumpBall(dir);
						if(game.isWin() >= 0) {
							mouseMode = MouseMode.WIN;
						} else {
							hasJumped = true;	
						}
					}
				}
			}
		} else if(mouseMode == MouseMode.WIN) {
			mouseMode = MouseMode.WAIT;
			turn = 0;
			hasJumped = false;
			game.reset();
			nextMove();
		}
	}

	private void nextMove() {
		if((!player1Human) && (!player2Human)) {
			while(mouseMode != MouseMode.WIN) {
				p1.makeMove(game, WinDirection.UP);
				if(game.isWin() >= 0) {
					mouseMode = MouseMode.WIN;
				}
			}
		} else {
			if(turn == 0) {
				if(player1Human) {
					mouseMode = MouseMode.ADD_STONE;
				} else {
					mouseMode = MouseMode.WAIT;
					p1.makeMove(game, WinDirection.UP);
					if(game.isWin() >= 0) {
						mouseMode = MouseMode.WIN;
					} else {
						turn = 1 - turn;
						nextMove();
					}
				}
			} else if(turn == 1) {
				if(player2Human) {
					mouseMode = MouseMode.ADD_STONE;
				} else {
					mouseMode = MouseMode.WAIT;
					p2.makeMove(game, WinDirection.DOWN);
					if(game.isWin() >= 0) {
						mouseMode = MouseMode.WIN;
					} else {
						turn = 1 - turn;
						nextMove();
					}

				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
}
