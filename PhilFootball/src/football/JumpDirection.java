package football;

public enum JumpDirection {
	UP(0, -1),
	UP_RIGHT(1, -1),
	RIGHT(1, 0),
	DOWN_RIGHT(1, 1),
	DOWN(0, 1),
	DOWN_LEFT(-1, 1),
	LEFT(-1, 0),
	UP_LEFT(-1, -1);
	
	public int x;
	public int y;
	
	private JumpDirection(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
