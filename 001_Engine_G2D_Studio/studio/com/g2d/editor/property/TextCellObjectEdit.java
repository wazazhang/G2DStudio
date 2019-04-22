package com.g2d.editor.property;

import java.awt.Component;

import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.cell.reflect.Parser;

public class TextCellObjectEdit<T> extends JTextField implements PropertyCellEdit<T>
{
	private static final long serialVersionUID = 1L;
	private T value;
	
	public TextCellObjectEdit(T value) {
		super.setBorder(new EmptyBorder(1, 1, 1, 1));
		super.setText(Parser.objectToString(value));
	}
	
	public T getValue() {
		return value;
	}
	
	public Component getComponent(ObjectPropertyEdit panel) {
		return this;
	}

	@Override
	public void commitValue() {}
}
