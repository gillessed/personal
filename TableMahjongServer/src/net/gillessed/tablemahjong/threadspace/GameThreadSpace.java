package net.gillessed.tablemahjong.threadspace;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.gillessed.tablemahjong.mahjonggame.MahjongGame;
import net.gillessed.tablemahjong.mahjonggame.ServerGameListener;
import net.gillessed.tablemahjong.mahjongupdate.MahjongUpdate;
import net.gillessed.tablemahjong.mahjongupdate.MahjongUpdate.UpdateTargetType;
import net.gillessed.tablemahjong.rules.AbstractMahjongRules;
import net.gillessed.tablemahjong.server.logging.Logger;
import net.gillessed.tablemahjong.server.packet.Packet;
import net.gillessed.tablemahjong.server.packet.Packet.PacketType;
import net.gillessed.tablemahjong.user.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GameThreadSpace implements ThreadSpace {
	
	private final String uuid;
	private final List<User> players;
	private final List<User> observers;
	private final CreateGameRoomBean bean;
	private final MahjongGame mahjongGame;
	
	private ConcurrentMap<String, Boolean> playerWaits;
	
	private ServerGameListener gameListener;
	
	public GameThreadSpace(String uuid, List<User> players, List<User> observers, CreateGameRoomBean bean) {
		this.uuid = uuid;
		this.players = players;
		this.observers = observers;
		this.bean = bean;
		bean.status = "started";
		Set<String> playerNames = new HashSet<String>();
		for(User user : players) {
			playerNames.add(user.getName());
		}
		gameListener = new ServerGameListener(this);
		mahjongGame = new MahjongGame(this, AbstractMahjongRules.getMahjongRules().get(bean.ruleSet), playerNames, bean.startingMoney);
		mahjongGame.startNewRound(false);
		mahjongGame.getCurrentMahjongRound().setType(UpdateTargetType.SERVER);
		mahjongGame.getCurrentMahjongRound().setServerListener(gameListener);
	}

	public void start() {
		for(User u : getPlayers()) {
			JSONObject obj = makeCreateObject("player");
			u.sendPacket(new Packet(PacketType.GAMEROOMACTION, Packet.SUCCESS, obj));
		}
		for(User u : getObservers()) {
			JSONObject obj = makeCreateObject("observer");
			u.sendPacket(new Packet(PacketType.GAMEROOMACTION, Packet.SUCCESS, obj));
		}
		if(!Boolean.parseBoolean(System.getProperty("debug", "false"))) {
			waitForConfirmFromPlayers("Begin game - east set, round 1. Make this more descriptive (maybe add in the bean settings).");
		} else {
			startRound();
		}
	}
	
	public void startRound() {
		for(User u : players) {
			JSONObject obj = makeGameObject();
			u.sendPacket(new Packet(PacketType.GAMEROOMACTION, Packet.SUCCESS, obj));
		}
		mahjongGame.getCurrentMahjongRound().deal();
	}
	
	public synchronized void waitForConfirmFromPlayers(String message) {
		playerWaits = new ConcurrentHashMap<String, Boolean>();
		for(User user : players) {
			playerWaits.put(user.getName(), false);
		}
		Thread waitThread = new Thread() {
			@Override
			public void run() {
				boolean stop;
				do { 
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					stop = true;
					for(Boolean b : playerWaits.values()) {
						if(b == false) {
							stop = false;
							break;
						}
					}
				} while (!stop);
				Logger.getLogger().info("Starting game!");
				startRound();
			}
		};
		waitThread.setDaemon(true);
		waitThread.start();
		for(User u : getPlayers()) {
			JSONObject obj = new JSONObject();
			try {
				obj.put("action", "message");
				obj.put("room", uuid);
				obj.put("message", message);
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
			u.sendPacket(new Packet(PacketType.GAMEROOMACTION, Packet.ATTEMPT, obj));
		}
	}
	
	private JSONObject makeCreateObject(String status) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("action", "create");
			obj.put("room", uuid);
			obj.put("bean", bean.getJSON());
			obj.put("status", status);
			obj.put("players", getNames(players));
			obj.put("observers", getNames(observers));
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return obj;
	}
	
	private JSONObject makeGameObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("action", "create-round");
			obj.put("room", uuid);
			obj.put("bean", bean.getJSON());
			obj.put("round", mahjongGame.getCurrentMahjongRound().getRoundJSON());
			obj.put("round-number", mahjongGame.getCurrentMahjongRound().getRoundNumber());
			obj.put("bonus-number", mahjongGame.getCurrentMahjongRound().getBonusNumber());
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return obj;
	}

	public String getUUID() {
		return uuid;
	}

	public void debug() {
		Logger logger = Logger.getLogger();
		logger.info("=== " + uuid + " ===");
		logger.info("   === Players (" + getPlayers().size() + "): ===");
		for(User u : getPlayers()) {
			logger.info("      " + u.getName());
		}
		logger.info("   === Observers (" + getObservers().size() + "): ===");
		for(User u : getObservers()) {
			logger.info("      " + u.getName());
		}
	}

	public CreateGameRoomBean getBean() {
		return bean;
	}

	public List<User> getPlayers() {
		return players;
	}

	public List<User> getObservers() {
		return observers;
	}

	public JSONArray getNames(List<User> users) {
		JSONArray namesJSON = new JSONArray();
		for(User u : users) {
			namesJSON.put(u.getName());
		}
		return namesJSON;
	}
	public void notify(String playerName) {
		Logger.getLogger().info("notified for message: " + playerName);
		playerWaits.put(playerName, true);
	}

	public void event(MahjongUpdate mahjongUpdate, String username) {
		gameListener.setUserFilter(username);
		mahjongGame.getCurrentMahjongRound().updateFromClient(mahjongUpdate);
	}

	public void stealChosen(JSONObject obj) {
		mahjongGame.getCurrentMahjongRound().stealChosen(obj);
	}
}
