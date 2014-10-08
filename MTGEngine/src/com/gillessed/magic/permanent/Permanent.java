package com.gillessed.magic.permanent;

import java.util.ArrayList;
import java.util.List;

import com.gillessed.magic.state.Card;
import com.gillessed.magic.state.Event;
import com.gillessed.magic.state.Event.EventType;
import com.gillessed.magic.state.EventListener;
import com.gillessed.magic.state.GameState;
import com.gillessed.player.Player;

public class Permanent {
	
	private List<EventListener> inPlayListeners;
	
	private Player owner;
	private Player controller;
	private Card card;
	
	private boolean phasing;
	private boolean isPhasedOut;
	private boolean tapped;
	
	public Permanent(Card card, Player owner, Player controller) {
		this.card = card;
		this.owner = owner;
		this.controller = controller;
		inPlayListeners = new ArrayList<EventListener>();
	}
	
	public boolean isPhasing() {
		return phasing;
	}
	
	public void setPhasing(boolean phasing) {
		this.phasing = phasing;
	}
	
	public boolean isPhasedOut() {
		return isPhasedOut;
	}
	
	public void phaseOut(GameState gameState) {
		this.isPhasedOut = true;
		Event event = new Event(EventType.PHASES_OUT);
		event.triggeringPermanent = this;
		gameState.eventPerformed(event);
	}
	
	public void phaseIn(GameState gameState) {
		this.isPhasedOut = false;
		Event event = new Event(EventType.PHASES_IN);
		event.triggeringPermanent = this;
		gameState.eventPerformed(event);
	}
	
	public boolean isTapped() {
		return tapped;
	}
	
	public void tap(GameState gameState) {
		this.tapped = false;
		Event event = new Event(EventType.TAPS);
		event.triggeringPermanent = this;
		gameState.eventPerformed(event);
	}
	
	public void untap(GameState gameState) {
		this.tapped = false;
		Event event = new Event(EventType.UNTAPS);
		event.triggeringPermanent = this;
		gameState.eventPerformed(event);
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public Player getController() {
		return controller;
	}

	public void setController(Player controller) {
		this.controller = controller;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
	public void attachInPlayListeners(GameState gameState) {
		for(EventListener eventListener : inPlayListeners) {
			gameState.attachListener(eventListener);
		}
	}
	public void unattachInPlayListeners(GameState gameState) {
		for(EventListener eventListener : inPlayListeners) {
			gameState.unattachListener(eventListener);
		}
	}
}
