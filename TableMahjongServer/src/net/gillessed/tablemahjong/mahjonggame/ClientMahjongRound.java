package net.gillessed.tablemahjong.mahjonggame;

import java.util.List;

import net.gillessed.tablemahjong.mahjongupdate.MahjongUpdate;
import net.gillessed.tablemahjong.mahjongupdate.MahjongUpdate.UpdateTargetType;
import net.gillessed.tablemahjong.mahjongupdate.UpdateTarget;
import net.gillessed.tablemahjong.server.logging.Logger;

public class ClientMahjongRound extends MahjongRound {

	private UpdateTarget UIListener;
	private UpdateTarget serverListener;
	
	public ClientMahjongRound(int roundNumber, int bonusNumber) {
		super(roundNumber, bonusNumber);
	}
	
	public ClientMahjongRound(int roundNumber, int bonusNumber, String wallTileHolderUUID, List<MahjongTile> mahjongTiles) {
		super(roundNumber, bonusNumber, wallTileHolderUUID, mahjongTiles);
	}

	public void setUIListener(UpdateTarget UIListener) {
		this.UIListener = UIListener;
	}

	public UpdateTarget getUIListener() {
		return UIListener;
	}

	public void setServerListener(UpdateTarget serverListener) {
		this.serverListener = serverListener;
	}

	public UpdateTarget getServerListener() {
		return serverListener;
	}
	
	@Override
	public void fireUpdate(MahjongUpdate mahjongUpdate) {
		switch(type) {
		case BOTH: 
			UIListener.update(mahjongUpdate);
			serverListener.update(mahjongUpdate);
			break;
		case UI:
			UIListener.update(mahjongUpdate);
			break;
		case SERVER:
			serverListener.update(mahjongUpdate);
			break;
		case NONE:
			break;
		}
	}

	@Override
	public void setup() {
		
	}
	
	public void updateFromServer(MahjongUpdate mahjongUpdate) {
		Logger.getLogger().dev("Client received MahjongUpdate: " + mahjongUpdate.toJSON().toString());
		setType(UpdateTargetType.UI);
		performMahjongUpdate(mahjongUpdate);
		fireUpdate(mahjongUpdate);
	}
}
