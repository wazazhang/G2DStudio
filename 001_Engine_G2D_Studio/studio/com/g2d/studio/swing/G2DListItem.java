package com.g2d.studio.swing;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JTree;

public abstract interface G2DListItem 
{
	public ImageIcon	getListIcon(boolean update); 

	public String		getListName();
	
	public Component	getListComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus);
	
	public void onDoubleClicked(G2DList<?> list, MouseEvent e);
	
	public void onRightClicked(G2DList<?> list, MouseEvent e);

	public void onClicked(G2DList<?> list, MouseEvent e);
	
	public void onSelected(G2DList<?> list);

}
