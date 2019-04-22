package com.g2d.studio.gameedit;

import java.awt.Component;

import com.g2d.editor.property.PropertyCellEdit;
import com.g2d.studio.gameedit.entity.ObjectNode;

public class ObjectSelectDialogString<T extends ObjectNode<?>> extends ObjectSelect<T> implements PropertyCellEdit<String>
{
	private static final long serialVersionUID = 1L;
	
	public ObjectSelectDialogString(
			Component owner,
			Class<T> type,
			int wcount,
			Object default_value, 
			ObjectSelectFilter<T> filter) 
	{
		super(owner, type, wcount, default_value, filter);
	}
	
	@Override
	public String getValue() {
		T obj = getSelectedObject();
		if (obj != null) {
			return obj.getID();
		}
		return null;
	}

	@Override
	public void commitValue() {}
	
}
