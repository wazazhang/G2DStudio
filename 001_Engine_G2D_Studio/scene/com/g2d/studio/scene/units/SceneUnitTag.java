package com.g2d.studio.scene.units;

import java.util.Vector;

import com.cell.rpg.scene.SceneUnit;
import com.g2d.Color;
import com.g2d.display.ui.Menu;
import com.g2d.editor.DisplayObjectEditor;
import com.g2d.game.rpg.Unit;
import com.g2d.geom.Shape;
import com.g2d.studio.swing.G2DListItem;
//import com.cell.rpg.scene.script.trigger.Event;

public interface SceneUnitTag<T extends SceneUnit> extends G2DListItem
{
//	---------------------------------------------------------------------------------------	
	public Unit				getGameUnit();
	public T				getUnit();
	public boolean			setUnitID(String id);
//	---------------------------------------------------------------------------------------
//	io

	public T 				onWrite();
	public void				onReadComplete(Vector<SceneUnitTag<?>> all);
	public void				onWriteReady(Vector<SceneUnitTag<?>> all);

//	---------------------------------------------------------------------------------------
//	snapshot
	public Color 			getSnapColor();
	public Shape 			getSnapShape();

//	public Class<? extends com.cell.rpg.scene.script.entity.SceneUnit>	
//							getEventType();
	
//	---------------------------------------------------------------------------------------
//	edit
	
	public Menu 					getEditMenu();	
	public DisplayObjectEditor<?>	getEditorForm();

	/**
	 * 当编辑器窗口被关闭时
	 */
	public void				onHideFrom();
	
	
}
