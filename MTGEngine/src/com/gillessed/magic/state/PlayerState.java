package com.gillessed.magic.state;

import java.util.ArrayList;
import java.util.List;

import com.gillessed.magic.permanent.Permanent;

public class PlayerState {
	private final List<Card> deck;
	private CardCollection hand;
	private CardCollection library;
	private CardCollection graveyard;
	private CardCollection exiled;
	private List<Permanent> battlefield;
	private int lifeTotal;
	private int poisonCounters;
	private int mulliganCount = 0;
	private boolean isAlive;
	
	public PlayerState(List<Card> deck) {
		this.deck = deck;
	}
	
	public void reset() {
		hand = new CardCollection();
		library = new CardCollection(deck);
		graveyard = new CardCollection();
		exiled = new CardCollection();
		battlefield = new ArrayList<Permanent>();
		lifeTotal = 20;
		poisonCounters = 0;
		isAlive = true;
		hand.addCards(library.takeN(7));
	}
	
	public void mulligan() {
		mulliganCount++;
		library.addCards(hand.takeAll());
		hand.addCards(library.takeN(7 - mulliganCount));
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public int getPoisonCounters() {
		return poisonCounters;
	}

	public void setPoisonCounters(int poisonCounters) {
		this.poisonCounters = poisonCounters;
	}

	public int getLifeTotal() {
		return lifeTotal;
	}

	public void setLifeTotal(int lifeTotal) {
		this.lifeTotal = lifeTotal;
	}

	public List<Permanent> getBattlefield() {
		return battlefield;
	}

	public void setBattlefield(List<Permanent> battlefield) {
		this.battlefield = battlefield;
	}

	public CardCollection getExiled() {
		return exiled;
	}

	public void setExiled(CardCollection exiled) {
		this.exiled = exiled;
	}

	public CardCollection getGraveyard() {
		return graveyard;
	}

	public void setGraveyard(CardCollection graveyard) {
		this.graveyard = graveyard;
	}

	public CardCollection getLibrary() {
		return library;
	}

	public void setLibrary(CardCollection library) {
		this.library = library;
	}

	public CardCollection getHand() {
		return hand;
	}

	public void setHand(CardCollection hand) {
		this.hand = hand;
	}
}
