package com.gillessed.magic.state;

public abstract class Card implements StackObject {
	protected String name;
	protected Color color;
	private CardCollection holder;
	
	public Card(String name, Color color) {
		this.name = name;
		this.color = color;
	}
	
	public abstract void cast(GameState gameState);
	
	@Override
	public abstract void resolve(GameState gameState);

	public CardCollection getHolder() {
		return holder;
	}

	public void setHolder(CardCollection holder) {
		this.holder = holder;
	}
}
