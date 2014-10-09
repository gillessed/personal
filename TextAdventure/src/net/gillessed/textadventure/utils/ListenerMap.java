package net.gillessed.textadventure.utils;

import java.util.HashMap;
import java.util.Map;

import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.deletelistener.DeleteListener;

public class ListenerMap<K, T extends DataType> extends HashMap<K, T> {
	private static final long serialVersionUID = 4891613119029123240L;

	private final DeleteListener deleteListener;
	
	public ListenerMap(DeleteListener deleteListener){
		this.deleteListener = deleteListener;
	}
	
	@Override
	public T put(K key, T value) {
		value.addDeleteListener(deleteListener);
		return super.put(key, value);
	}
	
	public void putAll(Map<? extends K,? extends T> m) {
		for(Map.Entry<? extends K, ? extends T> e : m.entrySet()) {
			e.getValue().addDeleteListener(deleteListener);
		}
	}
	
	@Override
	public T remove(Object key) {
		T t = super.remove(key);
		if(t != null) {
			t.removeDeleteListener(deleteListener);
		}
		return t;
	}
	
	public boolean removeValue(Object value) {
		if(value == null) {
			return false;
		}
		K toRemove = null;
		for(Map.Entry<? extends K, ? extends T> e : entrySet()) {
			if(value.equals(e.getValue())) {
				toRemove = e.getKey(); 
			}
		}
		if(toRemove == null) {
			return false;
		} else {
			remove(toRemove);
			return true;
		}
	}
	
	@Override
	public void clear() {
		for(Map.Entry<? extends K, ? extends T> e : entrySet()) {
			e.getValue().removeDeleteListener(deleteListener);
		}
		super.clear();
	}
}
