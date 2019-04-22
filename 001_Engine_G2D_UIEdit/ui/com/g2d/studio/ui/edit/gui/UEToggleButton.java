package com.g2d.studio.ui.edit.gui;

import com.g2d.Graphics2D;
import com.g2d.annotation.Property;
import com.g2d.display.event.MouseEvent;

public class UEToggleButton extends UEButton implements SavedComponent
{
	@Property("是否按下")
	public boolean isChecked = false;
	
	public UEToggleButton() {}
	
	@Override
	protected void onMouseClick(MouseEvent event) {
		super.onMouseClick(event);
		this.isChecked = (!isChecked);
	}
	
	protected int renderLayout(Graphics2D g) 
	{
		return super.renderLayout(g, isChecked);
	}
	
	protected int renderText(Graphics2D g, boolean isDown) 
	{
		return super.renderText(g, isChecked);
	}
}
