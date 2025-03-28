package com.g2d.cell;

import java.util.concurrent.ConcurrentHashMap;

public abstract class CellSetResourceManager
{
//	--------------------------------------------------------------------------------------------------------------------
//	
	final protected ConcurrentHashMap<String, CellSetResource> set_resources = new ConcurrentHashMap<String, CellSetResource>();
	
	public CellSetResource getSet(String path) throws Exception {
		synchronized (set_resources) {
			if (set_resources.containsKey(path)) {
				return set_resources.get(path);
			} else {
				CellSetResource set = createSet(path);
				set_resources.put(path, set);
				return set;
			}
		}
	}
	
	protected abstract CellSetResource createSet(String path) throws Exception;
	
	public void dispose() {
		synchronized (set_resources) {
			for (CellSetResource res : set_resources.values()) {
				res.dispose();
			}
		}
	}
}
