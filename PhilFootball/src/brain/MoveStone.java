package brain;

import java.awt.Point;

import football.PhilFootBall;

public class MoveStone implements Move {
	
	private Point stone;

	public MoveStone(Point stone) {
		this.stone = stone;
	}

	@Override
	public void apply(PhilFootBall game) {
		game.addStone(stone.x, stone.y);
	}
}
