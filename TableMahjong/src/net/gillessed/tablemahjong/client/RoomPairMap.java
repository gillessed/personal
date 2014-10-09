package net.gillessed.tablemahjong.client;

import java.util.ArrayList;
import java.util.List;

import net.gillessed.tablemahjong.threadspace.CreateGameRoomBean;

public class RoomPairMap {
	private final List<String> uuids;
	private final List<CreateGameRoomBean> beans;
	public RoomPairMap() {
		uuids = new ArrayList<String>();
		beans = new ArrayList<CreateGameRoomBean>();
	}
	public boolean put(String uuid, CreateGameRoomBean bean) {
		if(getUuids().contains(uuid)) {
			return false;
		}
		getUuids().add(uuid);
		getBeans().add(bean);
		return true;
	}
	public CreateGameRoomBean get(String uuid) {
		int index = getUuids().indexOf(uuid);
		if(index < 0) return null;
		return getBeans().get(index);
	}
	public CreateGameRoomBean remove(String uuid) {
		if(!getUuids().contains(uuid)) {
			return null;
		}
		int index = getUuids().indexOf(uuid);
		getUuids().remove(index);
		return getBeans().remove(index);
	}
	public List<String> getUuids() {
		return uuids;
	}
	public List<CreateGameRoomBean> getBeans() {
		return beans;
	}
	public int size() {
		if(uuids.size() != beans.size()) {
			throw new RuntimeException("Your RoomPairMap is in an inconsistent state. uuids: " + uuids.size() + "; beans:" + beans.size());
		}
		return uuids.size();
	}
	public boolean isEmpty() {
		return size() == 0;
	}
}
