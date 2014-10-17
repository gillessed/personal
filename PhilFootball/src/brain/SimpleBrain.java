package brain;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import football.Jump;
import football.PhilFootBall;

public class SimpleBrain implements Brain {
	private int folds;

	public SimpleBrain(int folds) {
		this.folds = folds;
	}
	
	public int evaluate(PhilFootBall game, WinDirection dir) {
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
	    long time1 = System.currentTimeMillis();
	    System.out.println("Thinking of a move...");
		GameTree gameTree = new GameTree(game);
		minMax(gameTree, folds, true, dir);
		double time = (System.currentTimeMillis() - time1) / 1000.0;
		System.out.println("Found a move. Took " + time + " seconds.");
		gameTree.getLimitChild(true).getMove().apply(game);
	}
	
	private void minMax(GameTree gameTree, int currentFold, boolean max, WinDirection dir) {
		if(currentFold == 0) {
			gameTree.setValue(evaluate(gameTree.getGame(), dir));
		} else {
			Set<Point> points = gameTree.getGame().getStonesWithinTwoSpaces();
			for(Point p : points) {
				Move move = new MoveStone(p);
				PhilFootBall copy = gameTree.getGame().copy();
				move.apply(copy);
				gameTree.addChild(new GameTree(copy, move));
			}
			
			Map<PhilFootBall, MoveJump> jumpChildren = getJumpChildren(gameTree.getGame().copy());
			for(Map.Entry<PhilFootBall, MoveJump> entry : jumpChildren.entrySet()) {
			    gameTree.addChild(new GameTree(entry.getKey(), entry.getValue()));
			}
			
			for(GameTree child : gameTree.getChildren()) {
				minMax(child, currentFold - 1, !max, dir);
			}
			gameTree.setValue(gameTree.getLimitChild(max).getValue());
		}
	}

	private Map<PhilFootBall, MoveJump> getJumpChildren(PhilFootBall initialState) {
	    Map<PhilFootBall, MoveJump> jumpMap = new HashMap<>();
	    List<MoveJump> jumps = new ArrayList<>();
		Stack<PhilFootBall> states = new Stack<>();
		states.push(initialState);
		while(!states.isEmpty()) {
		    PhilFootBall state = states.pop();
		    if(state.isWin() == -1) {
    		    List<Jump> possibleJumps = state.getPossibleJumps();
    		    for(Jump jump : possibleJumps) {
                    PhilFootBall newState = state.copy();
                    newState.jumpBall(jump);
                    MoveJump previousJump;
        		    if(jumpMap.containsKey(state)) {
        		        previousJump = jumpMap.get(state);
        		    } else {
        		        previousJump = new MoveJump();
        		    }
        		    MoveJump newJump = previousJump.addJump(jump);
                    jumps.add(newJump);
                    jumpMap.put(newState, newJump);
                    states.push(newState);
    		    }
		    }
		}
		
		return jumpMap;
	}
}
