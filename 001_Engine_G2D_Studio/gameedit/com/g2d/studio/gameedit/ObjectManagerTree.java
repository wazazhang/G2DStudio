package com.g2d.studio.gameedit;

import java.awt.BorderLayout;
import java.awt.Image;

import com.cell.rpg.RPGObject;
import com.g2d.studio.ManagerFormDynamic;
import com.g2d.studio.Studio;
import com.g2d.studio.Studio.ProgressForm;
import com.g2d.studio.gameedit.entity.IProgress;
import com.g2d.studio.gameedit.entity.ObjectNode;

@SuppressWarnings("serial")
public class ObjectManagerTree<T extends ObjectNode<D>, D extends RPGObject> extends ManagerFormDynamic
{	
	final public ObjectTreeView<T, D> tree_view;
	
	public ObjectManagerTree(Studio studio, ProgressForm progress, Image icon, ObjectTreeView<T, D> tree_view) 
	{
		super(studio, progress, tree_view.getTitle(), icon);
		this.tree_view = tree_view;
		this.add(tool_bar, BorderLayout.NORTH);
		this.add(tree_view, BorderLayout.CENTER);
	}


	@Override
	public void saveAll(IProgress progress) throws Throwable {
		tree_view.saveAll(progress);
	}

	@Override
	public void saveSingle() throws Throwable {
		tree_view.saveSingle();
	}
	
	protected void initToolbars(ObjectManager manager) {}

}
