package net.gillessed.tablemahjong.mahjonggame.swingui;

import java.awt.Graphics;

public interface MahjongUIComponent<E> {
	public void draw(Graphics g);
	public E getModel();
	public MinSizeComponent getContainer();
	public MahjongGameUI getParent();
}
