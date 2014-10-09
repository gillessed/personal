package net.gillessed.textadventure.utils;

import java.util.ArrayList;
import java.util.Collection;

import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.deletelistener.DeleteListener;

public class ListenerList<T extends DataType> extends ArrayList<T> {
	private static final long serialVersionUID = 4891613119029123240L;

	private final DeleteListener deleteListener = new DeleteListener() {
		private static final long serialVersionUID = -5979431807278152590L;

		@Override
		public void deleted(DataType deleted) {
			remove(deleted);
		}
	};
	
	@Override
	public void add(int index, T element) {
		element.addDeleteListener(deleteListener);
		super.add(index, element);
	}
	
	@Override
	public boolean add(T e) {
		e.addDeleteListener(deleteListener);
		return super.add(e);
	}
	
	@Override
	public boolean addAll(Collection<? extends T> c) {
		for(T t : c) {
			t.addDeleteListener(deleteListener);
		}
		return super.addAll(c);
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		for(T t : c) {
			t.addDeleteListener(deleteListener);
		}
		return super.addAll(index, c);
	}
	
	@Override
	public T remove(int index) {
		T t = super.remove(index);
		if(t != null) {
			t.removeDeleteListener(deleteListener);
		}
		return t;
	}
	
	@Override
	public boolean remove(Object o) {
		boolean b = super.remove(o);
		if(b) {
			((DataType)o).removeDeleteListener(deleteListener);
		}
		return b;
	}
	
	@Override
	public T set(int index, T element) {
		if(index >= 0 && index < size()) {
			T t = get(index);
			if(t != null) {
				t.removeDeleteListener(deleteListener);
			}
			element.addDeleteListener(deleteListener);
		}
		return super.set(index, element);
	}
	
	@Override
	public void clear() {
		for(T t : this) {
			t.removeDeleteListener(deleteListener);
		}
		super.clear();
	}
}
