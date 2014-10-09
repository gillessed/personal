package net.gillessed.tablemahjong.rules;

import net.gillessed.tablemahjong.mahjonggame.MahjongHand;


public class JapaneseMahjongRules extends AbstractMahjongRules {

	public JapaneseMahjongRules() {
		super("Japanese Rules", 20000);
	}

	@Override
	public int score(MahjongHand mh) {
		return 12000;
	}
}
