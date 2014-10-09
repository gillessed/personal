package net.gillessed.tablemahjong.rules;

import net.gillessed.tablemahjong.mahjonggame.MahjongHand;

public class HongKongMahjongRules extends AbstractMahjongRules {
	
	public HongKongMahjongRules() {
		super("Hong Kong Rules", 50);
	}

	@Override
	public int score(MahjongHand mh) {
		return 10;
	}
}
