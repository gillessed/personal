import java.util.ArrayList;
import java.util.List;

import net.gillessed.tablemahjong.mahjonggame.MahjongTile;
import net.gillessed.tablemahjong.stealnotice.StealNotice;


public class TestStealNotice {
	public static void main(String args[]) {
		MahjongTile discard = new MahjongTile("pin-4");
		List<MahjongTile> hand = new ArrayList<MahjongTile>(13);
		hand.add(new MahjongTile("pin-4"));
		hand.add(new MahjongTile("pin-5"));
		hand.add(new MahjongTile("man-3"));
		hand.add(new MahjongTile("pin-6"));
		hand.add(new MahjongTile("pin-4"));
		hand.add(new MahjongTile("bamboo-3"));
		hand.add(new MahjongTile("pin-3"));
		hand.add(new MahjongTile("dragon-chun"));
		hand.add(new MahjongTile("dragon-haku"));
		hand.add(new MahjongTile("pin-4"));
		hand.add(new MahjongTile("pin-9"));
		hand.add(new MahjongTile("pin-8"));
		hand.add(new MahjongTile("dragon-chun"));
		StealNotice sn = new StealNotice(discard, hand, true);
		System.out.println("Steals:");
		for(List<MahjongTile> set : sn.getChoices()) {
			for(MahjongTile mt : set) {
				System.out.print(mt.getDescription() + " ");
			}
			System.out.println();
		}
		System.out.println("Done");
	}
}
