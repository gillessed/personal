package football;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents the board state of a single game. 
 */
public class PhilFootBall {
	
	private final int width;
	private final int height;
	
	private int ballX;
	private int ballY;
	
	private int boardState[][];
	
	public PhilFootBall() {
		width = 15;
		height = 19;
		boardState = new int[width][height];
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				boardState[i][j] = 0;
			}
		}
		
		ballX = width / 2;
		ballY = height / 2;
	}
	
	public PhilFootBall(PhilFootBall toCopy) {
		width = toCopy.getWidth();
		height = toCopy.getHeight();
		
		boardState = new int[width][height];
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				boardState[i][j] = toCopy.getState(i, j);
			}
		}
		
		Point ball = toCopy.getBallCoords();
		ballX = ball.x;
		ballY = ball.y;
	}
	
	public void reset() {
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				boardState[i][j] = 0;
			}
		}
		
		ballX = width / 2;
		ballY = height / 2;
	}
	
	public int getState(int x, int y) {
		return boardState[x][y];
	}
	
	public void addStone(int x, int y) {
		boardState[x][y] = 1;
	}
	
	public Point getBallCoords() {
		return new Point(ballX, ballY);
	}
	
	/**
	 * This will jump the ball once in a given direction.
	 */
	public void jumpBall(Jump jump) {
		for(int i = 1; i < jump.length; i++) {
			boardState[ballX + i * jump.dir.x][ballY + i * jump.dir.y] = 0;
		}
		ballX += jump.length * jump.dir.x;
		ballY += jump.length * jump.dir.y;
		
		if(ballX < 0 || ballX >= width) {
			throw new RuntimeException("Ball x coord is off.");
		}
	}
	
	/**
	 * This gets a list of possible jumps for the ball given the
	 * current board position.
	 */
	public List<Jump> getPossibleJumps() {
		List<JumpDirection> dirs = new ArrayList<>();
		if(ballX > 1) {
			if(ballY > 0 && boardState[ballX - 1][ballY - 1] == 1) {
				dirs.add(JumpDirection.UP_LEFT);
			} if(boardState[ballX - 1][ballY] == 1) {
				dirs.add(JumpDirection.LEFT);
			} if(ballY < height - 1 && boardState[ballX - 1][ballY + 1] == 1) {
				dirs.add(JumpDirection.DOWN_LEFT);
			}
		}
		if(ballX < width - 2) {
			if(ballY > 0 && boardState[ballX + 1][ballY - 1] == 1) {
				dirs.add(JumpDirection.UP_RIGHT);
			} if(boardState[ballX + 1][ballY] == 1) {
				dirs.add(JumpDirection.RIGHT);
			} if(ballY < height - 1 && boardState[ballX + 1][ballY + 1] == 1) {
				dirs.add(JumpDirection.DOWN_RIGHT);
			}
		}
		if(ballY > 0 && boardState[ballX][ballY - 1] == 1) {
			dirs.add(JumpDirection.UP);
		}
		if(ballY < height - 1 && boardState[ballX][ballY + 1] == 1) {
			dirs.add(JumpDirection.DOWN);
		}
		List<Jump> jumps = new ArrayList<>();
		for(JumpDirection dir : dirs) {
			int length = 1;
			while(ballX + length * dir.x >= 0 && ballX + length * dir.x < width &&
					ballY + length * dir.y >= 0 && ballY + length * dir.y < height &&
					boardState[ballX + length * dir.x][ballY + length * dir.y] == 1) {
				length++;
			}
			int x = ballX + length * dir.x;
			int y = ballY + length * dir.y;
			if(x >= 0 && x < width && y >= -1 && y <= height) {
				Jump jump = new Jump(dir, length);
				jumps.add(jump);
			}
		}
		return jumps;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int isWin() {
		if(ballY <= 0) return 0;
		if(ballY >= height - 1) return 1;
		return -1;
	}
	
	public boolean isBall(int x, int y) {
		return x == ballX && y == ballY;
	}
	
	public Set<Point> getStonesWithinTwoSpaces() {
		Set<Point> points = new HashSet<Point>();
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				if(boardState[i][j] == 1) {
					for(int x = -2; x <= 2; x++) {
						for(int y = -2; y <= 2; y++) {
							if(i + x >= 0 && i + x < width && y + j >= 0 && y + j < height) {
								points.add(new Point(i + x, y + j));
							}
						}
					}
				}
			}
		}
		for(JumpDirection dir : JumpDirection.values()) {
			int x = ballX + dir.x;
			int y = ballY + dir.y;
			if(x >= 0 && x < width && y >= 0 && y < height) {
				points.add(new Point(x, y));
			}
		}
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				if(boardState[i][j] == 1) {
					points.remove(new Point(i, j));
				}
			}
		}
		return points;
	}

	public PhilFootBall copy() {
		return new PhilFootBall(this);
	}
}