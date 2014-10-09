package net.gillessed.tablemahjong.stealnotice;

import java.util.Map;

import net.gillessed.tablemahjong.mahjonggame.MahjongGame.Wind;
import net.gillessed.tablemahjong.mahjonggame.ServerMahjongRound;
import net.gillessed.tablemahjong.server.packet.Packet;
import net.gillessed.tablemahjong.server.packet.Packet.PacketType;
import net.gillessed.tablemahjong.user.User;

import org.json.JSONException;
import org.json.JSONObject;


public class NoticeThread extends Thread {
	
	private final ServerMahjongRound round;
	private final Map<Wind, User> userWindMap;
	private final Object waiter;

	public NoticeThread(ServerMahjongRound round, Map<Wind, User> userWindMap) {
		this.round = round;
		this.userWindMap = userWindMap;
		setDaemon(true);
		waiter = new Object();
	}
	
	@Override
	public void run() {
		for(int i = 1; i < 4; i++) {
			Wind wind = Wind.values()[(round.getCurrentWind().ordinal() + i) % 4];
			JSONObject obj = new JSONObject();
			try {
				obj.put("room", round.getGameThreadSpace().getUUID());
				obj.put("action", "steal-notice");
				obj.put("wind", wind.ordinal());
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
			userWindMap.get(wind).sendPacket(new Packet(PacketType.GAMEROOMACTION, Packet.ATTEMPT, obj));
			try {
				synchronized (waiter) {
					waiter.wait();
				}
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		round.freezeUsers(false);
	}

	public Object getWaiter() {
		return waiter;
	};
	
	public void notifyWaiter() {
		synchronized (waiter) {
			waiter.notify();
		}
	}
};