package net.gillessed.tablemahjong.mahjonggame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.gillessed.tablemahjong.rules.MahjongRules;
import net.gillessed.tablemahjong.threadspace.GameThreadSpace;


public class MahjongGame {
	private static final Map<String, Wind> windMap = new HashMap<String, Wind>();
	public enum Wind {
		East("East"),
		North("North"),
		West("West"),
		South("South");
		private final String name;
		Wind(String name) {
			this.name = name;
			windMap.put(name, this);
		}
		public static Wind getNextWind(Wind wind) {
			return Wind.values()[(wind.ordinal() + 1) % 4];
		}
		@Override
		public String toString() {
			return name;
		}
		public static Wind getWind(String windName) {
			return windMap.get(windName);
		}
		public int getDistanceTo(Wind wind) {
			return (ordinal() - wind.ordinal() + 4) % 4;
		}
	}
	
	protected final MahjongRules ruleSet;
	protected final Set<String> playerNames;
	protected final int startingMoney;
	protected final List<ServerMahjongRound> mahjongRounds;

	private final GameThreadSpace gameThreadSpace;
	
	private int currentRoundNum;
	private int currentBonusNum;

	public MahjongGame(GameThreadSpace gameThreadSpace, MahjongRules ruleSet, Set<String> playerNames, int startingMoney) {
		this.ruleSet = ruleSet;
		this.playerNames = playerNames;
		this.startingMoney = startingMoney;
		this.gameThreadSpace = gameThreadSpace;
		mahjongRounds = new ArrayList<ServerMahjongRound>();
		currentRoundNum = 0;
		currentBonusNum = 0;
	}
	
	public Set<String> getPlayerNames() {
		return playerNames;
	}
	
	public int getStartingMoney() {
		return startingMoney;
	}
	
	public MahjongRules getRuleSet() {
		return ruleSet;
	}
	
	public List<? extends ServerMahjongRound> getMahjongRounds() {
		return Collections.unmodifiableList(mahjongRounds);
	}

	public ServerMahjongRound getCurrentMahjongRound() {
		synchronized(mahjongRounds) {
			if(currentRoundNum >= 1 && currentRoundNum <= 8) {
				return mahjongRounds.get(currentRoundNum - 1);
			} else {
				return null;
			}
		}
	}

	public ServerMahjongRound getPreviousMahjongRound() {
		if(currentRoundNum >= 2 && currentRoundNum <= 8) {
			return mahjongRounds.get(currentRoundNum - 2);
		} else {
			return null;
		}
	}
	
	public void startNewRound(boolean isBonus) {
		ServerMahjongRound previousRound = getPreviousMahjongRound();
		if(isBonus) {
			currentBonusNum++;
		} else {
			currentRoundNum++;
			currentBonusNum = 1;
		}
		Set<Wind> chosenWinds = new HashSet<Wind>();
		Set<MahjongPlayer> players = new HashSet<MahjongPlayer>();
		Random rand = new Random();
		if(currentRoundNum == 1) {
			for(String playerName : playerNames) {
				int r = (int)(rand.nextInt(4));
				while(chosenWinds.contains(Wind.values()[r])) {
					r = (int)(rand.nextInt(4));
				}
				chosenWinds.add(Wind.values()[r]);
				players.add(new MahjongPlayer(playerName, getStartingMoney(), Wind.values()[r]));
			}
			
		} else {
			for(MahjongPlayer oldPlayer : previousRound.getPlayers()) {
				if(isBonus) {
					players.add(new MahjongPlayer(oldPlayer.getName(), oldPlayer.getMoney(), oldPlayer.getAssociatedHand()));
				} else {
					players.add(new MahjongPlayer(oldPlayer.getName(), oldPlayer.getMoney(),
							Wind.getNextWind(oldPlayer.getAssociatedHand())));
				}
			}
		}
		ServerMahjongRound round = new ServerMahjongRound(gameThreadSpace,
				currentRoundNum, currentBonusNum, players);
		synchronized(mahjongRounds) {
			mahjongRounds.add(round);
		}
	}
}
