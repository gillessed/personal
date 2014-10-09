package net.gillessed.tablemahjong.mahjonggame.swingui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import net.gillessed.tablemahjong.client.ClientUpdaterThread;
import net.gillessed.tablemahjong.client.swingui.GameRoomUI;
import net.gillessed.tablemahjong.server.packet.Packet;
import net.gillessed.tablemahjong.server.packet.Packet.PacketType;
import net.gillessed.tablemahjong.stealnotice.StealNotice;

import org.json.JSONException;
import org.json.JSONObject;

public class StealNoticeUI {

	private final GameRoomUI roomUI;
	private final MahjongGameUI gameUI;
	private final StealNotice model;
	
	private ActionListener stealListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			//TODO: get the chosen permutation and send response
			sendResponseToServer(null);
		}
	};
	
	public StealNoticeUI(GameRoomUI roomUI, StealNotice model) {
		this.roomUI = roomUI;
		this.model = model;
		gameUI = roomUI.getGamePane().getMahjongGameUI();
	}
	
	private void sendResponseToServer(List<String> descriptions) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("room", roomUI.getModel().getUuid());
			obj.put("action", "steal-chosen");
			obj.put("stealing", false);
		} catch (JSONException exp) {
			throw new RuntimeException(exp);
		}
		ClientUpdaterThread cut = roomUI.getUIParent().getModel().getClientUpdaterThread();
		Packet packet = new Packet(PacketType.GAMEROOMACTION, Packet.ATTEMPT, obj);
		cut.sendPacket(packet);
	}
}
