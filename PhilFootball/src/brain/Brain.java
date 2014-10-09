package brain;

import football.PhilFootBall;

public interface Brain {
	public enum WinDirection {
		UP,
		DOWN;
	}
	public void makeMove(PhilFootBall game, WinDirection dir);
}
