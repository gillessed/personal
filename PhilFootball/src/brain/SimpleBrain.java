package brain;

import java.awt.Point;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import football.PhilFootBall;

public class SimpleBrain implements Brain {
	private int folds;
	private WinDirection dir;

	public SimpleBrain(int folds) {
		this.folds = folds;
	}
	
	public int evaluate(PhilFootBall game) {
		int ballStart = game.getHeight() / 2;
		int diff = game.getBallCoords().y - ballStart;
		if(dir == WinDirection.UP) {
			return -diff;
		} else {
			return diff;
		}
	}
	
	@Override
	public void makeMove(PhilFootBall game, WinDirection dir) {
		this.dir = dir;
		GameTree gameTree = new GameTree(game);
		minMax(gameTree, folds, true);
		gameTree.getLimitChild(true).getMove().apply(game);
	}
	
	private void minMax(GameTree gameTree, int currentFold, boolean max) {
		if(currentFold == 0) {
			gameTree.setValue(evaluate(gameTree.getGame()));
		} else {
			Set<Point> points = gameTree.getGame().getStonesWithinTwoSpaces();
			for(Point p : points) {
				Move move = new MoveStone(p);
				PhilFootBall copy = gameTree.getGame().copy();
				move.apply(copy);
				gameTree.addChild(new GameTree(copy, move));
			}
			
			List<MoveJump> allJumps = getAllJumps(gameTree.getGame().copy());
			
			for(GameTree child : gameTree.getChildren()) {
				minMax(child, currentFold - 1, !max);
			}
			gameTree.setValue(gameTree.getLimitChild(max).getValue());
		}
	}

	private List<MoveJump> getAllJumps(PhilFootBall game) {
		Stack<GameTree> gameTrees = new Stack<>();
		
	}
}
