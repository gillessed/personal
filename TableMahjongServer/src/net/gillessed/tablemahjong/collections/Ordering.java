package net.gillessed.tablemahjong.collections;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Ordering <K> {
	
	public final Map<K, Integer> objects;
	
	public Ordering() {
		objects = new TreeMap<K, Integer>();
	}
	
	public Ordering(List<K> source) {
		objects = new HashMap<K, Integer>();
		int i = 0;
		for(K k : source) {
			objects.put(k, i);
			i++;
		}
	}
	
	public void add(K obj) {
		objects.put(obj, objects.values().size());
	}
	
	public void add(K obj, int value) {
		if(value > objects.values().size() + 1) {
			throw new IllegalArgumentException("Value [" + value
					+ "]cannot exceed the size of the ordering [" + (objects.values().size() + 1) + "].");
		}
		for(Entry<K, Integer> pair : objects.entrySet()) {
			if(pair.getValue() >= value) {
				objects.put(pair.getKey(), pair.getValue() + 1);
			}
		}
		objects.put(obj, value);
	}
	
	public boolean remove(K obj) {
		if(objects.containsKey(obj)) {
			int value = objects.remove(obj);
			for(K k : objects.keySet()) {
				int oldValue = objects.get(k);
				if(objects.get(k) > value) {
					objects.put(k, oldValue--);
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	public boolean swap(K obj1, K obj2) {
		if(objects.containsKey(obj1) && objects.containsKey(obj2)) {
			int value1 = objects.get(obj1);
			int value2 = objects.get(obj2);
			objects.put(obj1, value2);
			objects.put(obj2, value1);
			return true;
		} else {
			return false;
		}
	}
	
	public Map<K, Integer> getMap() {
		return Collections.unmodifiableMap(objects);
	}
	
	public K get(Integer i) {
		for(Entry<K, Integer> pair : objects.entrySet()) {
			if(pair.getValue() == i) {
				return pair.getKey();
			}
		}
		return null;
	}
}
