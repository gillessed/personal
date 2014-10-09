package net.gillessed.textadventure.player.gameobjects;

import java.io.Serializable;

import net.gillessed.textadventure.datatype.EnemyArea;
import net.gillessed.textadventure.datatype.Event;
import net.gillessed.textadventure.datatype.FriendlyArea;
import net.gillessed.textadventure.datatype.Universe;
import net.gillessed.textadventure.datatype.World;

public class Game implements Serializable {
	private static final long serialVersionUID = 3231929204150848965L;
	
	private final Universe universe;
	private final String gameName;
	
	private World currentWorld;
	private FriendlyArea currentFriendlyArea;
	private EnemyArea currentEnemyArea;
	private Event currentEvent;
	
	public Game(Universe universe, String gameName) {
		this.universe = universe;
		this.gameName = gameName;
		
		currentFriendlyArea = universe.getFirstArea();
		currentWorld = universe.getFirstWorld();
		currentEnemyArea = null;
		currentEvent = universe.getFirstEvent();
	}

	public Universe getUniverse() {
		return universe;
	}

	public String getGameName() {
		return gameName;
	}

	public FriendlyArea getCurrentFriendlyArea() {
		return currentFriendlyArea;
	}

	public void setCurrentFriendlyArea(FriendlyArea currentFriendlyArea) {
		this.currentFriendlyArea = currentFriendlyArea;
	}

	public EnemyArea getCurrentEnemyArea() {
		return currentEnemyArea;
	}

	public void setCurrentEnemyArea(EnemyArea currentEnemyArea) {
		this.currentEnemyArea = currentEnemyArea;
	}

	public World getCurrentWorld() {
		return currentWorld;
	}

	public void setCurrentWorld(World currentWorld) {
		this.currentWorld = currentWorld;
	}

	public Event getCurrentEvent() {
		return currentEvent;
	}

	public void setCurrentEvent(Event currentEvent) {
		this.currentEvent = currentEvent;
	}
}
