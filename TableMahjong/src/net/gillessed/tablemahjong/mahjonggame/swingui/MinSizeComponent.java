package net.gillessed.tablemahjong.mahjonggame.swingui;

import javax.swing.JComponent;

public class MinSizeComponent {
	
	private final JComponent component;
	private MahjongGameUI mahjongGameUI;
	
	public MinSizeComponent(JComponent component) {
		this.component = component;
	}

	public JComponent getComponent() {
		return component;
	}
	
	public int getWidth() {
		int superWidth = component.getWidth();
		if(mahjongGameUI == null) return superWidth;
		return Math.max(superWidth, mahjongGameUI.getUIProperty(MahjongGameUI.MIN_SIZE));
	}
	
	public int getHeight() {
		int superHeight =  component.getHeight();
		if(mahjongGameUI == null) return superHeight;
		return Math.max(superHeight, mahjongGameUI.getUIProperty(MahjongGameUI.MIN_SIZE));
	}

	public MahjongGameUI getMahjongGameUI() {
		return mahjongGameUI;
	}

	public void setMahjongGameUI(MahjongGameUI mahjongGameUI) {
		this.mahjongGameUI = mahjongGameUI;
	}
}
