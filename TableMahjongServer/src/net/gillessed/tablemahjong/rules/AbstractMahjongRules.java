package net.gillessed.tablemahjong.rules;

import java.util.HashMap;
import java.util.Map;


public abstract class AbstractMahjongRules implements MahjongRules {
	
	private static Map<String, MahjongRules> rulesMap = new HashMap<String, MahjongRules>();
	
	static {
		rulesMap.put(new JapaneseMahjongRules().toString(), new JapaneseMahjongRules());
		rulesMap.put(new HongKongMahjongRules().toString(), new HongKongMahjongRules());
	}
	
	public static Map<String, MahjongRules> getMahjongRules() {
		return rulesMap;
	}
	
	final protected String name;
	private final int defaultStartingMoney;
	
	public AbstractMahjongRules(String name, int defaultStartingMoney) {
		this.name = name;
		this.defaultStartingMoney = defaultStartingMoney;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof MahjongRules) {
			MahjongRules rules = (MahjongRules)obj;
			if(name == null) return false;
			return getName().equals(rules.getName());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 17;
		int hashedCode = 31 + hash * getName().hashCode();
		return hashedCode;
	}

	public int getDefaultStartingMoney() {
		return defaultStartingMoney;
	}
}
