package com.g2d.editor.property;

import java.lang.reflect.Field;

import com.g2d.awt.util.TextEditorDialog;


/**
 * @author WAZA
 * 负责在JTable里更改颜色
 */
public class PopupCellEditHtml extends PopupCellEdit<String>
{
	TextEditorDialog dialog;
	
	public PopupCellEditHtml(String value) 
	{
		// Set up the dialog that the button brings up.
		dialog = new TextEditorDialog();
		dialog.setText(value+"");
	}

	@Override
	public void setValue(Field field, String value, ObjectPropertyEdit comp) {
		super.setValue(field, value, comp);
		if (value != null) {
			setText(value+"");
		}
	}
	
	@Override
	public void onOpenEditor(String value) 
	{
		dialog.setText(value+"");
		// 
		String result = dialog.showDialog();
		if (result != null) {
			current_value = result;
		}
	}
	
	
}
