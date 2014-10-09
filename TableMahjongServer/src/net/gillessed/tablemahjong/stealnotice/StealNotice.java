package net.gillessed.tablemahjong.stealnotice;

import java.util.ArrayList;
import java.util.List;

import net.gillessed.tablemahjong.mahjonggame.MahjongTile;
import net.gillessed.tablemahjong.mahjonggame.MahjongTile.TileType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StealNotice {
	private List<List<MahjongTile>> choices;

	public StealNotice(MahjongTile lastDiscard, List<MahjongTile> handToCheck, boolean isRunAllowed) {
		choices = new ArrayList<List<MahjongTile>>();
		TileType tileType = lastDiscard.getType();
		List<MahjongTile> tripQuad = new ArrayList<MahjongTile>();
		tripQuad.add(lastDiscard);
		for(MahjongTile mt : handToCheck) {
			if(mt.getType() == tileType && mt.getValue().equals(lastDiscard.getValue())) {
				tripQuad.add(mt);
			}
		}
		if(tripQuad.size() == 3) {
			choices.add(tripQuad);
		} else if(tripQuad.size() == 4) {
			choices.add(tripQuad);
			List<MahjongTile> trip = new ArrayList<MahjongTile>();
			trip.addAll(tripQuad);
			trip.remove(trip.size() - 1);
			choices.add(trip);
		}
		MahjongTile[] runList = new MahjongTile[5];
		for(int i = 0; i < 5; i++) {
			runList[i] = null;
		}
		if(tileType != TileType.Dragon && tileType != TileType.Wind && isRunAllowed) {
			int discardTileValue = Integer.parseInt(lastDiscard.getValue());
			runList[2] = new MahjongTile(lastDiscard.getDescription());
			for(MahjongTile mt : handToCheck) {
				if(mt.getType() != TileType.Dragon && mt.getType() != TileType.Wind && mt.getType().equals(lastDiscard.getType())) {
					int mtValue = Integer.parseInt(mt.getValue());
					if(Math.abs(mtValue - discardTileValue) <= 2 && mtValue - discardTileValue != 0) {
						runList[mtValue - discardTileValue + 2] = new MahjongTile(mt.getDescription());
					}
				}
			}
			for(int i = 0; i <= 2; i++) {
				if(runList[i] != null && runList[i + 1] != null && runList[i + 2] != null) {
					List<MahjongTile> run = new ArrayList<MahjongTile>();
					run.add(new MahjongTile(runList[i].getDescription()));
					run.add(new MahjongTile(runList[i + 1].getDescription()));
					run.add(new MahjongTile(runList[i + 2].getDescription()));
					choices.add(run);
				}
			}
		}
	}

	public StealNotice(JSONObject source) throws JSONException {
		choices = new ArrayList<List<MahjongTile>>();
		JSONArray choicesJSON = source.getJSONArray("choices");
		for(int i = 0; i < choicesJSON.length(); i++) {
			JSONArray choiceJSON = choicesJSON.getJSONArray(i);
			List<MahjongTile> choice = new ArrayList<MahjongTile>();
			for(int j = 0; j < choiceJSON.length(); j++) {
				choice.add(new MahjongTile(choiceJSON.getJSONObject(j)));
			}
			choices.add(choice);
		}
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject obj = new JSONObject();
		JSONArray choicesJSON = new JSONArray();
		for(List<MahjongTile> choice : choices) {
			JSONArray choiceJSON = new JSONArray();
			for(MahjongTile mt : choice) {
				choiceJSON.put(mt.toJSON());
			}
			choicesJSON.put(choiceJSON);
		}
		obj.put("choices", choicesJSON);
		return obj;
	}

	public List<List<MahjongTile>> getChoices() {
		return choices;
	}
}
