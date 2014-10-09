package net.gillessed.textadventure.datatype;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.Icon;

import net.gillessed.textadventure.deletelistener.DeleteListener;
import net.gillessed.textadventure.utils.IconUtils;
import net.gillessed.textadventure.utils.ListenerList;

public class FriendlyArea extends DataType { 
	private static final long serialVersionUID = 8010993214630312764L;
	
	private World parent;
	private final List<Event> immediateEvents;
	private final List<FriendlyArea> connectedFriendlyAreas;
	private final List<EnemyArea> connectedEnemyAreas;
	private final Set<Shop> shops;
	private final Set<Interaction> interactions;
	private boolean isSpacePort;
	
	public FriendlyArea(DataType parent) {
		super(parent);
		shops = new HashSet<Shop>();
		interactions = new HashSet<Interaction>();
		immediateEvents = new ListenerList<Event>();
		connectedFriendlyAreas = new ListenerList<FriendlyArea>();
		connectedEnemyAreas = new ListenerList<EnemyArea>();
	}
	
	public List<Event> getImmediateEvents() {
		return immediateEvents;
	}
	
	public List<FriendlyArea> getConnectedFriendlyAreas() {
		return connectedFriendlyAreas;
	}
	
	public List<EnemyArea> getConnectedEnemyAreas() {
		return connectedEnemyAreas;
	}
	
	public Set<Shop> getShops() {
		return Collections.unmodifiableSet(shops);
	}
	
	public void addShop(final Shop shop) {
		shops.add(shop);
		shop.addDeleteListener(new DeleteListener() {
			private static final long serialVersionUID = 7223763786700097427L;

			@Override
			public void deleted(DataType deleted) {
				shops.remove(shop);
			}
		});
	}
	
	public Set<Interaction> getInteractions() {
		return Collections.unmodifiableSet(interactions);
	}
	
	public void addInteraction(final Interaction interaction) {
		interactions.add(interaction);
		interaction.addDeleteListener(new DeleteListener() {
			private static final long serialVersionUID = 5549675880975179312L;

			@Override
			public void deleted(DataType deleted) {
				interactions.remove(interaction);
			}
		});
	}
	
	public boolean isSpacePort() {
		return isSpacePort;
	}
	
	public void setSpacePort(boolean isSpacePort) {
		this.isSpacePort = isSpacePort;
	}

	public World getParent() {
		return parent;
	}

	public void setParent(World parent) {
		this.parent = parent;
	}
	
	@Override
	public void deleted() {
		Set<Shop> tempShops = new HashSet<>(shops);
		for(Shop s : tempShops) {
			s.deleted();
		}
		Set<Interaction> tempInteractions = new HashSet<>(interactions);
		for(Interaction i : tempInteractions) {
			i.deleted();
		}
		super.deleted();
	}
	
	@Override
	public Icon getIcon(int size) {
		return IconUtils.FRIENDLY_AREAS_ICON(size);
	}
}
