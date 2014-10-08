package com.gillessed.magic.state;

import java.util.List;

import com.gillessed.magic.target.Target;
import com.gillessed.player.Player;

/**
 * These listeners will be called whenever there is an event that
 * could trigger and ability. The listener itself should check
 * the appropriate criteria.
 */
public interface EventListener extends StackObject {
	/**
	 * This function checks if the given event can
	 * actually trigger this ability. 
	 */
	public boolean doesEvenTriggerAction(Event event, GameState gameState);
	
	/**
	 * This should return the player controlling the card with the ability.
	 */
	public Player getController();
	
	/**
	 * This should return true if it is the player's choice whether
	 * or not to activate this ability.
	 */
	public boolean isConditional();
	
	/**
	 * If targets must be selected, this should return a list
	 * of what kinds of targets this ability must target.
	 */
	public List<Target> getTargets();
	
	/**
	 * This resolves the given ability after it is popped from the stack.
	 */
	public void resolve(GameState gameState);
}
