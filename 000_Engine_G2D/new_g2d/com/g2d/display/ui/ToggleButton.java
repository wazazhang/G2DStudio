package com.g2d.display.ui;

import com.g2d.BufferedImage;
import com.g2d.Graphics2D;
import com.g2d.Image;
import com.g2d.display.event.MouseEvent;
import com.g2d.display.ui.layout.UILayout;


public abstract class ToggleButton extends BaseButton 
{	
	boolean is_checked = false;
	
	public ToggleButton() {}
	
	public boolean isChecked() {
		return this.is_checked;
	}
	
	public void setChecked(boolean checked) {
		this.is_checked = checked;
	}
	
	@Override
	protected void onMouseClick(MouseEvent event) {
		super.onMouseClick(event);
		this.setChecked(!is_checked);
	}
	
	public void render(Graphics2D g) 
	{
		super.render(g);
		
		
		renderLayout(g);
	}
	
	protected int renderLayout(Graphics2D g) 
	{
		UILayout rect = layout;
		if (custom_layout != null) {
			rect = custom_layout;
		}
		if (is_checked) {
			if (custom_layout_down != null) {
				rect = custom_layout_down;
			}
		} 
		return rect.render(g, 0, 0, getWidth(), getHeight());
	}
	
	
	public static class ImageToggleButton extends ToggleButton
	{
		public Image	checked_image;
		public Image	unchecked_image;
		
		public ImageToggleButton(Image checked, Image unchecked)
		{			
			checked_image = checked;
			unchecked_image = unchecked;
		}
		public ImageToggleButton()
		{			
		}
		public void render(Graphics2D g) 
		{
			super.render(g);
		}
		
		protected int renderLayout(Graphics2D g) 
		{
			UILayout rect = layout;
			Image image = null;
			if (is_checked) {
				image = checked_image;
				if (custom_layout_down != null) {
					rect = custom_layout_down;
				}
			} else {
				image = unchecked_image;
				if (custom_layout != null) {
					rect = custom_layout;
				}
			}
			int ret = rect.render(g, 0, 0, getWidth(), getHeight());
			if (image != null) {
				g.drawImage(image, 
						getWidth()/2  - image.getWidth()/2, 
						getHeight()/2 - image.getHeight()/2);
				ret += 1;
			}
			return ret;
		}
	}
}
