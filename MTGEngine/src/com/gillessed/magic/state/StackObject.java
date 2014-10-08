package com.gillessed.magic.state;

import com.gillessed.player.Player;

public interface StackObject {
	public Player getController();
	public void resolve(GameState gameState);
}
