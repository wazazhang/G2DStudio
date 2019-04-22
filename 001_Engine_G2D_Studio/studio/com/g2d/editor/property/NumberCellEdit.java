package com.g2d.editor.property;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.cell.reflect.Parser;

public class NumberCellEdit extends JSpinner implements 
PropertyCellEdit<Object>
{
	final Class<?> type;
	final Object src_value;
	
	public NumberCellEdit(Class<?> type, Object data) {
		super(new SpinnerNumberModel(new Double(0), new Double(-Double.MAX_VALUE), new Double(Double.MAX_VALUE), new Double(1)));
		this.type = type;
		this.src_value = data;
		if (data != null) {
			this.setValue(data);
		}
	}
	
	@Override
	public void commitValue() {
		try {
			commitEdit();
		} catch (Exception e) {}
	}
	
	@Override
	public void setValue(Object value) {
		super.setValue(Parser.castNumber(value, Double.class));
	}
	
	public Object getValue() {
		if (type != null) {
			Object rvalue = super.getValue();
			Object ret = Parser.castNumber(rvalue, type);
			return ret;
		}
		return 0;
	}

	public Component getComponent(ObjectPropertyEdit panel) {
		return this;
	}
}
