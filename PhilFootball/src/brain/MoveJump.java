package brain;

import java.util.List;

import football.Jump;
import football.PhilFootBall;

public class MoveJump implements Move {

	private List<Jump> jumps;

	public MoveJump(List<Jump> jumps) {
		this.jumps = jumps;
	}
	
	@Override
	public void apply(PhilFootBall game) {
		for(Jump jump : jumps) {
			game.jumpBall(jump);
		}
	}
}
