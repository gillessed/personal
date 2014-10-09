package net.gillessed.textadventure.datatype;

import java.util.List;

import javax.swing.Icon;

import net.gillessed.textadventure.deletelistener.DeleteListener;
import net.gillessed.textadventure.utils.IconUtils;
import net.gillessed.textadventure.utils.ListenerList;

public class Universe extends DataType {
	private static final long serialVersionUID = -1052930093954301770L;

	private final List<World> worlds;
	private final List<ItemDescription> items;
	private final List<EnemyDescription> enemies;
	private final List<Event> events;
	private final List<Skill> skills;
	private final List<PlayableCharacter> characters;
	private final List<Variable> variables;
	private Event firstEvent;
	private World firstWorld;
	private FriendlyArea firstArea;
	private PlayableCharacter startingCharacter;
	public Universe() {
		super(null);
		setName("Universe");
		worlds = new ListenerList<World>();
		items = new ListenerList<ItemDescription>();
		enemies = new ListenerList<EnemyDescription>();
		events = new ListenerList<Event>();
		skills = new ListenerList<Skill>();
		characters = new ListenerList<PlayableCharacter>();
		variables = new ListenerList<Variable>();
	}

	public List<ItemDescription> getItems() {
		return items;
	}

	public List<EnemyDescription> getEnemies() {
		return enemies;
	}
	
	public List<Event> getEvents() {
		return events;
	}

	public List<World> getWorlds() {
		return worlds;
	}

	public List<Skill> getSkills() {
		return skills;
	}

	public List<PlayableCharacter> getCharacters() {
		return characters;
	}

	public List<Variable> getVariables() {
		return variables;
	}

	public Event getFirstEvent() {
		return firstEvent;
	}
	
	public void setFirstEvent(Event firstEvent) {
		this.firstEvent = firstEvent;
		if(firstEvent != null) {
			firstEvent.addDeleteListener(new DeleteListener() {
				private static final long serialVersionUID = 2491534790088380201L;

				@Override
				public void deleted(DataType deleted) {
					setFirstEvent(null);
				}
			});
		}
	}

	public World getFirstWorld() {
		return firstWorld;
	}

	public void setFirstWorld(World firstWorld) {
		this.firstWorld = firstWorld;
		if(firstWorld != null) {
			firstWorld.addDeleteListener(new DeleteListener() {
				private static final long serialVersionUID = 6998125734217716364L;

				@Override
				public void deleted(DataType deleted) {
					setFirstWorld(null);
					setFirstArea(null);
				}
			});
		}
	}

	public FriendlyArea getFirstArea() {
		return firstArea;
	}

	public void setFirstArea(FriendlyArea firstArea) {
		this.firstArea = firstArea;
		if(firstArea != null) {
			firstArea.addDeleteListener(new DeleteListener() {
				private static final long serialVersionUID = 4109333277705763706L;

				@Override
				public void deleted(DataType deleted) {
					setFirstArea(null);
				}
			});
		}
	}

	public PlayableCharacter getStartingCharacter() {
		return startingCharacter;
	}

	public void setStartingCharacter(PlayableCharacter startingCharacter) {
		this.startingCharacter = startingCharacter;
		if(startingCharacter != null) {
			startingCharacter.addDeleteListener(new DeleteListener() {
				private static final long serialVersionUID = -5870869842116972867L;

				@Override
				public void deleted(DataType deleted) {
					setStartingCharacter(null);
				}
			});
		}
	}
	
	@Override
	public Icon getIcon(int size) {
		return IconUtils.UNIVERSE_ICON(size);
	}
}
