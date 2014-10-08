package com.gillessed.magic.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CardCollection implements Iterable<Card> {
	private List<Card> cards;
	
	public CardCollection() {
		cards = new ArrayList<Card>();
	}
	
	public CardCollection(List<Card> cards) {
		this.cards = new ArrayList<Card>(cards);
		for(Card card : this.cards) {
			card.setHolder(this);
		}
	}
	
	public CardCollection(Card... cards) {
		this();
		for(Card card : cards) {
			this.cards.add(card);
		}
		for(Card card : this.cards) {
			card.setHolder(this);
		}
	}
	
	public List<Card> getCardList() {
		return Collections.unmodifiableList(cards);
	}
	
	public void addCard(Card card) {
		cards.add(card);
		card.setHolder(this);
	}
	
	public void addCards(List<Card> cards) {
		this.cards.addAll(cards);
		for(Card card : cards) {
			card.setHolder(this);
		}
	}
	
	public void addCards(Card... cards) {
		for(Card card : cards) {
			this.cards.add(card);
		}
		for(Card card : cards) {
			card.setHolder(this);
		}
	}
	
	public void removeCard(Card card) {
		cards.remove(card);
		card.setHolder(null);
	}
	
	public void removeCards(List<Card> cards) {
		cards.removeAll(cards);
		for(Card card : cards) {
			card.setHolder(null);
		}
	}
	
	public void removeCards(Card... cards) {
		for(Card card : cards) {
			this.cards.remove(card);
		}
		for(Card card : cards) {
			card.setHolder(null);
		}
	}
	
	public List<Card> takeN(int n) {
		List<Card> chunk = new ArrayList<Card>();
		for(int i = 0; i < n; i++) {
			Card card = cards.remove(0);
			card.setHolder(null);
			chunk.add(card);
		}
		return chunk;
	}
	
	public List<Card> takeAll() {
		return takeN(cards.size());
	}
	
	public void randomize() {
		List<Card> temp = new ArrayList<Card>();
		temp.addAll(cards);
		cards.clear();
		while(!temp.isEmpty()) {
			int rand = (int)(Math.random() * temp.size());
			Card card = temp.remove(rand);
			cards.add(card);
		}
	}
	
	public void shuffle(GameState gameState) {
		//TODO
	}

	@Override
	public Iterator<Card> iterator() {
		return cards.iterator();
	}
}
