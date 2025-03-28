package com.g2d.studio.gameedit;

import com.cell.rpg.RPGObject;
import com.g2d.studio.Studio.ProgressForm;
import com.g2d.studio.gameedit.dynamic.DynamicNode;
import com.g2d.studio.gameedit.dynamic.IDynamicIDFactory;
import com.g2d.studio.io.File;

public abstract class ObjectTreeViewTemplateDynamic<T extends DynamicNode<D>, D extends RPGObject> 
extends ObjectTreeView<T, D> implements IDynamicIDFactory<T>
{
	private static final long serialVersionUID = 1L;
	
	public ObjectTreeViewTemplateDynamic(
			String		title, 
			Class<T>	node_type, 
			Class<D>	data_type, 
			File		objects_dir,
			ProgressForm progress) 
	{
		super(title, node_type, data_type, ObjectManager.toListFile(objects_dir, data_type), progress);
		getTreeRoot().loadList(progress);
		reload();
		getTree().setDragEnabled(true);
		saveListFile();
	}
	
//	@Override
//	public Integer createID() {
//		return node_index.createID();
//	}
//
	public boolean resetID(T obj, int newID) {
		if (obj.resetID(this, newID)) {
			this.repaint();
			return true;
		}
		return false;
	}
//	
//	@Override
//	public Integer peekID() {
//		return node_index.peekID();
//	}
//	
//	@Override
//	public boolean containsID(int newID) {
//		return node_index.containsID(newID);
//	}
//	---------------------------------------------------------------------------------------------------------------------------
	
}
