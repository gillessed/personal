package com.gillessed.magic.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.gillessed.magic.permanent.Permanent;
import com.gillessed.magic.state.Event.EventType;
import com.gillessed.player.Action;
import com.gillessed.player.Player;

public class GameState {
	private List<Player> players;
	private Map<Player, PlayerState> playerStates;
	private ModularInt currentPlayer;
	private Phase phase;
	private Phase nextPhase;
	private final Set<EventListener> eventListeners;
	private final Map<Player, List<EventListener>> triggerMap;
	private final Stack<EventListener> stack;
	
	public GameState(int startPlayer, List<Player> players, List<List<Card>> decks) {
		eventListeners = new HashSet<EventListener>();
		triggerMap = new HashMap<Player, List<EventListener>>();
		stack = new Stack<EventListener>();
		this.players = players;
		playerStates = new HashMap<Player, PlayerState>();
		for(int i = 0; i < players.size(); i++) {
			playerStates.put(players.get(i), new PlayerState(decks.get(i)));
		}
		this.currentPlayer = new ModularInt(startPlayer, players.size());
		phase = Phase.UNTAP;
		for(Player player : players) {
			triggerMap.put(player, new ArrayList<EventListener>());
		}
	}
	
	/**
	 * This function starts the game and performs the phase loops
	 * for players to perform actions.
	 */
	public void start() {
		boolean gameEnd = false;
		while(!gameEnd) {
			switch(phase) {
			case UNTAP:
				nextPhase = Phase.UPKEEP;
				performUntapPhase();
				break;
			case UPKEEP:
				nextPhase = Phase.DRAW;
				performUpkeepPhase();
				break;
			}
			phase = nextPhase;
		}
	}
	
	public void attachListener(EventListener eventListener) {
		eventListeners.add(eventListener);
	}

	public void unattachListener(EventListener eventListener) {
		eventListeners.remove(eventListener);
	}
	
	public void eventPerformed(Event event) {
		for(EventListener listener : eventListeners) {
			if(listener.doesEvenTriggerAction(event, this)) {
				triggerMap.get(listener.getController()).add(listener);
			}
		}
	}
	
	/**
	 * Once the active player is given priority, this will continue to 
	 * prompt players for actions until the stack is empty and all players
	 * have passed, which ends whatever phase it currently is.
	 */
	private void priorityRound() {
		do {
			ModularInt priorityOrder = currentPlayer.copy();
			ModularInt lastPlayer = priorityOrder.copy();
			do {
				Player player = players.get(priorityOrder.getValue());
				Action action = player.priorityGiven(this);
				if(action != null) {
					action.perform(this);
					lastPlayer = priorityOrder.copy();
				} else {
					priorityOrder.add(1);
				}
			} while(!priorityOrder.equals(lastPlayer));
			StackObject obj = stack.pop();
			obj.resolve(this);
		} while(!stack.isEmpty());
	}
	
	private void performUntapPhase() {
		// Phasing in and out
		List<Permanent> toPhaseIn = new ArrayList<Permanent>();
		List<Permanent> toPhaseOut = new ArrayList<Permanent>();
		for(Permanent permanent : playerStates.get(players.get(currentPlayer.getValue())).getBattlefield()) {
			if(permanent.isPhasing()) {
				if(permanent.isPhasedOut()) {
					toPhaseIn.add(permanent);
				} else {
					toPhaseOut.add(permanent);
				}
			}
		}
		for(Permanent permanent : toPhaseIn) {
			permanent.phaseIn(this);
		}
		for(Permanent permanent : toPhaseOut) {
			permanent.phaseOut(this);
		}
		for(Permanent permanent : toPhaseIn) {
			permanent.attachInPlayListeners(this);
		}
		for(Permanent permanent : toPhaseOut) {
			permanent.unattachInPlayListeners(this);
		}
		
		// Untap permanents
		List<Permanent> toUntap = players.get(currentPlayer.getValue()).untapPermanents(this);

		for(Permanent permanent : toUntap) {
			permanent.untap(this);
		}
		
		// All triggers are held here and will be resolved during the upkeep.
	}
	
	public void performUpkeepPhase() {
		// Beginning of upkeep tiggers
		Event event = new Event(EventType.BEGINNING_OF_UPKEEP);
		event.triggeringPlayer = players.get(currentPlayer.getValue());
		eventPerformed(event);
		
		// Put triggers onto stack
		ModularInt resolveOrder = currentPlayer.copy();
		do {
			Player player = players.get(resolveOrder.getValue());
			List<EventListener> order = player.resolveTriggers(triggerMap.get(player));
			for(EventListener listener : order) {
				stack.push(listener);
			}
			triggerMap.get(player).clear();
			resolveOrder.add(1);
		} while(!resolveOrder.equals(currentPlayer));
		
		// Active player gets priority
		priorityRound();
	}
}
