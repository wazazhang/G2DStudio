package com.g2d.studio.gameedit;

import java.awt.Component;

import com.g2d.editor.property.PropertyCellEdit;
import com.g2d.studio.gameedit.entity.ObjectNode;


@SuppressWarnings("serial")
public class ObjectSelectCellEdit<T extends ObjectNode<?>> extends ObjectSelectDialogString<T> implements PropertyCellEdit<String>
{
	public ObjectSelectCellEdit(Component owner, Class<T> object_type) 
	{
		this(owner, object_type, null);
	}
	
	public ObjectSelectCellEdit(Component owner, Class<T> object_type, Object selected) 
	{
		this(owner, object_type, null, null);
	}
	
	public ObjectSelectCellEdit(Component owner, Class<T> object_type, Object selected, ObjectSelectFilter<T> filter) 
	{
		super(owner, object_type, 4, selected, filter);
	}
	
	

}
