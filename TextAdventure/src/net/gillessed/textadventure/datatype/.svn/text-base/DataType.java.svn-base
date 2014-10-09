package net.gillessed.textadventure.datatype;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.Icon;

import net.gillessed.textadventure.deletelistener.DeleteListener;

public abstract class DataType implements Serializable {
	
	private static final long serialVersionUID = -4251943039706177724L;
	
	private static final Map<String, DataType> dataMap = new HashMap<String, DataType>();
	
	public static DataType getDataType(String uuid) {
		return dataMap.get(uuid);
	}
	
	public static void clearData() {
		dataMap.clear();
	}
	
	public static List<FriendlyArea> getAllFriendlyAreas() {
		List<FriendlyArea> friendlyAreas = new ArrayList<FriendlyArea>();
		for(DataType data : dataMap.values()) {
			if(data instanceof FriendlyArea) {
				friendlyAreas.add((FriendlyArea)data);
			}
		}
		Collections.sort(friendlyAreas, new Comparator<FriendlyArea>() {
			@Override
			public int compare(FriendlyArea o1, FriendlyArea o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return friendlyAreas;
	}

	public static List<Event> getAllEvents(Event exclude) {
		List<Event> events = new ArrayList<Event>();
		for(DataType data : dataMap.values()) {
			if(data instanceof Event && !(data.equals(exclude))) {
				events.add((Event)data);
			}
		}
		Collections.sort(events, new Comparator<Event>() {
			@Override
			public int compare(Event o1, Event o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return events;
	}
	
	public static List<DataType> getAllOfType(Class<? extends DataType> clazz) {
		List<DataType> dataList = new ArrayList<DataType>();
		for(DataType data : dataMap.values()) {
			if(clazz.isAssignableFrom(data.getClass())) {
				dataList.add(data);
			}
		}
		return dataList;
	}

	public static List<FriendlyArea> getAllFriendlyAreas(World parent, FriendlyArea currentArea) {
		List<FriendlyArea> dataList = new ArrayList<FriendlyArea>();
		dataList.addAll(parent.getFriendlyAreas());
		if(currentArea != null) {
			dataList.remove(currentArea);
		}
		return dataList;
	}

	public static List<EnemyArea> getAllEnemyAreas(World parent, EnemyArea currentArea) {
		List<EnemyArea> dataList = new ArrayList<EnemyArea>();
		dataList.addAll(parent.getEnemyAreas());
		if(currentArea != null) {
			dataList.remove(currentArea);
		}
		return dataList;
	}
	
	private List<DeleteListener> deleteListeners = new ArrayList<DeleteListener>();
	private boolean exists;
	private String uuid;
	private String name;
	private String description;
	private DataType parent;
	
	public DataType(DataType parent) {
		this.parent = parent;
		this.name = "New" + getClass().getSimpleName();
		uuid = UUID.randomUUID().toString();
		dataMap.put(uuid, this);
		exists = true;
	}
	
	public String getUUID() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean isExists() {
		return exists;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
	}
	
	@Override
	public String toString() {
		if(name != null) {
			return name;
		} else {
			return super.toString();
		}
	}
	
	public void addDeleteListener(DeleteListener deleteListener) {
		deleteListeners.add(deleteListener);
	}
	
	public void removeDeleteListener(DeleteListener deleteListener) {
		deleteListeners.remove(deleteListener);
	}
	
	public void deleted() {
		List<DeleteListener> temp = new ArrayList<>();
		temp.addAll(deleteListeners);
		for(DeleteListener deleteListener : temp) {
			deleteListener.deleted(this);
		}
		dataMap.remove(getUUID());
	}
	public DataType getParent() {
		return parent;
	}

	public void setParent(DataType parent) {
		this.parent = parent;
	}

	public String getTypeName() {
		String className = getClass().getSimpleName();
		StringBuilder typeNameSb = new StringBuilder(className.substring(0,1));
		for(int i = 1; i < className.length(); i++) {
			char ch = className.charAt(i);
			if(Character.isUpperCase(ch)) {
				typeNameSb.append(" ");
			}
			typeNameSb.append(ch);
		}
		return typeNameSb.toString();
	}
	public abstract Icon getIcon(int size);

}
