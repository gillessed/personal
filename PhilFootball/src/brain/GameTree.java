package brain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import football.PhilFootBall;

/**
 * 
 *
 */
public class GameTree {
	private List<GameTree> children;
	private int value;
	private Move move;
	private PhilFootBall game;
	public GameTree(PhilFootBall phil) {
		this.game = phil;
		children = new ArrayList<>();
		value = 0;
	}
	
	public GameTree(PhilFootBall phil, Move move) {
		this(phil);
		this.move = move;
	}
	
	public void addChild(GameTree tree) {
		children.add(tree);
	}
	
	public PhilFootBall getGame() {
		return game;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public Move getMove() {
		return move;
	}
	
	public GameTree getLimitChild(boolean max) {
		if(children.isEmpty()) {
			throw new RuntimeException("Shouldn't get here");
		}
		GameTree limTree = children.get(0);
		int lim = limTree.getValue();
		for(int i = 1; i < children.size(); i++) {
			if((max && children.get(i).getValue() > lim) || (!max && children.get(i).getValue() < lim)) {
				limTree = children.get(i);
				lim = limTree.getValue();
			}
		}
		
		List<GameTree> limitTrees = new ArrayList<>();
		for(GameTree child : children) {
			if(child.getValue() == lim) {
				limitTrees.add(child);
			}
		}
		
		return limitTrees.get((int)(Math.random() * limitTrees.size()));
	}

	public List<GameTree> getChildren() {
		return Collections.unmodifiableList(children);
	}
}
