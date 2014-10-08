package com.gillessed.player;

import com.gillessed.magic.state.GameState;

public interface Action {
	public void perform(GameState gameState);
}
