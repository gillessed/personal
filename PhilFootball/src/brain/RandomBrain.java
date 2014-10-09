package brain;

import java.awt.Point;
import java.util.List;
import java.util.Random;
import java.util.Set;

import football.Jump;
import football.PhilFootBall;

public class RandomBrain implements Brain {
	@Override
	public void makeMove(PhilFootBall game, WinDirection dir) {
		Random rand = new Random();
		List<Jump> jumps = game.getPossibleJumps();
		if(!jumps.isEmpty() && rand.nextDouble() > 0.8) {
			int next = rand.nextInt(jumps.size());
			game.jumpBall(jumps.get(next));
			if(game.isWin() >= 0) {
				return;
			}
			jumps = game.getPossibleJumps();
			while(!jumps.isEmpty()) {
				next = rand.nextInt(jumps.size() + 1);
				if(next == jumps.size()) break;
				game.jumpBall(jumps.get(next));
				if(game.isWin() >= 0) {
					return;
				}
				jumps = game.getPossibleJumps();
			}
		} else {
			Set<Point> points = game.getStonesWithinTwoSpaces();
			int next = rand.nextInt(points.size());
			Point point = null;
			for(Point p : points) {
				if(next == 0) {
					point = p;
					break;
				}
				next--;
			}
			game.addStone(point.x, point.y);
		}
	}
}
