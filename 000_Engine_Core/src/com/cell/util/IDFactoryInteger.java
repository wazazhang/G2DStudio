package com.cell.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class IDFactoryInteger<T> implements Iterable<T>
{
	final AtomicInteger			id_index 	= new AtomicInteger();
	final HashMap<Integer, T> 	spawned_id	= new HashMap<Integer, T>();
	
	public IDFactoryInteger() {}
	
	synchronized public boolean storeID(Integer id, T obj) {
		if (!spawned_id.containsKey(id)) {
			spawned_id.put(id, obj);
			id_index.set(id);
			return true;
		}
		return false;
	}
	
	synchronized public Integer peekID () {
		int baseid = id_index.get();
		while (true) {
			int lid = (++baseid);
			if (!spawned_id.containsKey(lid)) {
				return lid;
			}
		}
	}
	
	synchronized public Integer createID () {
		while (true) {
			int lid = id_index.incrementAndGet();
			if (!spawned_id.containsKey(lid)) {
				return lid;
			}
		}
	}

	synchronized public boolean swapID(int oldID, int newID) {
		if (!spawned_id.containsKey(newID) && spawned_id.containsKey(oldID)) {
			T obj = spawned_id.remove(oldID);
			spawned_id.put(newID, obj);
			return true;
		}
		return false;
	}
	
	synchronized public T killID(Integer id) {
		return spawned_id.remove(id);
	}
	
	synchronized public T get(Integer id) {
		return spawned_id.get(id);
	}

	synchronized public boolean containsID(int id) {
		return spawned_id.containsKey(id);
	}
	
	@Override
	public Iterator<T> iterator() {
		return spawned_id.values().iterator();
	}
	
}
