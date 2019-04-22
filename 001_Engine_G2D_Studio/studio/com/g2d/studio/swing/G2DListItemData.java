package com.g2d.studio.swing;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JList;

public class G2DListItemData<T> implements G2DListItem
{
	final public T data;
	final private String name;

	public G2DListItemData(T data) {
		this(data, data + "");
	}

	public G2DListItemData(T data, String name) {
		this.data = data;
		this.name = name;
	}

	@Override
	public Component getListComponent(JList list, Object value, int index,
			boolean isSelected, boolean cellHasFocus) {
		return null;
	}

	@Override
	public ImageIcon getListIcon(boolean update) {
		return null;
	}

	@Override
	public String getListName() {
		return name;
	}

	@Override
	public String toString() {
		return getListName();
	}

	public T getData() {
		return data;
	}

	@Override
	public void onDoubleClicked(G2DList<?> list, MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRightClicked(G2DList<?> list, MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClicked(G2DList<?> list, MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSelected(G2DList<?> list) {
		// TODO Auto-generated method stub

	}	
	
}
