package net.gillessed.tablemahjong.rules;

import net.gillessed.tablemahjong.mahjonggame.MahjongHand;

public interface MahjongRules {
	public String getName();
	public int score(MahjongHand mh);
}
