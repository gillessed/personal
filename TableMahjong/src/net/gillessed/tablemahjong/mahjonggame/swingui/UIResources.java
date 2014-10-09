package net.gillessed.tablemahjong.mahjonggame.swingui;

import java.util.HashMap;
import java.util.Map;

import net.gillessed.tablemahjong.mahjonggame.MahjongTile;

public class UIResources {
	private static Map<String, MahjongTileUI> mahjongTileUIs;
	static {
		mahjongTileUIs = new HashMap<String, MahjongTileUI>();
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 4; j++) {
				String bamboo = "bamboo-" + (i+1) + "-" + (j+1);
				mahjongTileUIs.put(bamboo, new MahjongTileUI(bamboo));
				String pin = "pin-" + (i+1) + "-" + (j+1);
				mahjongTileUIs.put(pin, new MahjongTileUI(pin));
				String man = "man-" + (i+1) + "-" + (j+1);
				mahjongTileUIs.put(man, new MahjongTileUI(man));
			}
		}
		for(int i = 0; i < 4; i++) {
			mahjongTileUIs.put("wind-east-" + (i+1), new MahjongTileUI("wind-east-" + (i+1)));
			mahjongTileUIs.put("wind-north-" + (i+1), new MahjongTileUI("wind-north-" + (i+1)));
			mahjongTileUIs.put("wind-west-" + (i+1), new MahjongTileUI("wind-west-" + (i+1)));
			mahjongTileUIs.put("wind-south-" + (i+1), new MahjongTileUI("wind-south-" + (i+1)));
		}
		for(int i = 0; i < 4; i++) {
			mahjongTileUIs.put("dragon-chun-" + (i+1), new MahjongTileUI("dragon-chun-" + (i+1)));
			mahjongTileUIs.put("dragon-green-" + (i+1), new MahjongTileUI("dragon-green-" + (i+1)));
			mahjongTileUIs.put("dragon-haku-" + (i+1), new MahjongTileUI("dragon-haku-" + (i+1)));
		}
	}
	
	public static MahjongTileUI getMahjongTileUI(MahjongTile tile) {
		if(tile == null) {
			throw new RuntimeException("tile should never be null here!");
		}
		return getMahjongTileUI(tile.getDescription());
	}
	
	public static MahjongTileUI getMahjongTileUI(String desc) {
		return mahjongTileUIs.get(desc);
	}
}
