package com.g2d.studio.swing;

import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class G2DDefaultTreeNode extends G2DTreeNode<G2DTreeNode<?>>
{
	ImageIcon	icon;
	String		name;
	Object 		data;
	
	public G2DDefaultTreeNode(ImageIcon icon, String name) {
		this(icon, name, name);
	}
	
	public G2DDefaultTreeNode(ImageIcon icon, String name, Object data) {
		this.icon = icon;
		this.name = name;
		this.data = data;
	}
	
	@Override
	protected ImageIcon createIcon() {
		return icon;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
