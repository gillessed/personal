package brain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import football.Jump;
import football.PhilFootBall;

public class MoveJump implements Move {

	private List<Jump> jumps;

	public MoveJump(List<Jump> jumps) {
		this.jumps = jumps;
	}
	
	public MoveJump() {
	    jumps = Collections.emptyList();
    }

    public MoveJump addJump(Jump jump) {
	    List<Jump> jumps = new ArrayList<>();
	    jumps.addAll(this.jumps);
	    jumps.add(jump);
	    return new MoveJump(jumps);
	}
	
	@Override
	public void apply(PhilFootBall game) {
		for(Jump jump : jumps) {
			game.jumpBall(jump);
		}
	}
}
