package com.gillessed.player;

import java.util.List;

import com.gillessed.magic.permanent.Permanent;
import com.gillessed.magic.state.EventListener;
import com.gillessed.magic.state.GameState;

public interface Player {
	public List<Permanent> untapPermanents(GameState gameState);
	public Action priorityGiven(GameState gameState);
	public List<EventListener> resolveTriggers(List<EventListener> list);
}
