package net.gillessed.tablemahjong.mahjonggame.swingui;

import java.awt.Graphics;

public abstract class AbstractMahjongUIComponent<E> implements MahjongUIComponent<E> {
	private final MinSizeComponent container;
	private final E model;
	private final MahjongGameUI parent;
	public AbstractMahjongUIComponent(MinSizeComponent container, MahjongGameUI parent, E model) {
		this.parent = parent;
		this.model = model;
		this.container = container;
	}
	public abstract void draw(Graphics g);
	public E getModel() {
		return model;
	}
	public MinSizeComponent getContainer() {
		return container;
	}
	public MahjongGameUI getParent() {
		return parent;
	}
}
