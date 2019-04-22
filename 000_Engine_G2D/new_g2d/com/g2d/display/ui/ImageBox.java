package com.g2d.display.ui;

import com.cell.gfx.IImage;
import com.g2d.Graphics2D;
import com.g2d.Tools;

public class ImageBox extends UIComponent
{
	
	transient public IImage	background;
	
	public ImageBox(){}
	
	public ImageBox(String path)
	{
		setImage(path);
	}
	
	public ImageBox(IImage img) {
		setImage(img);
		setSize(img.getWidth(), img.getHeight());
	}


	public void setImage(IImage img) {
		background = img;
	}
	
	public void setImage(String path) {
		if (path!=null) {
			try {
				background = Tools.readImage(path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public IImage getImage()
	{
		return background;
	}
	
	@Override
	public void render(Graphics2D g)
	{
		super.render(g);
		if (background!=null) {
			g.drawImage(background, 0, 0, getWidth(), getHeight(), 0);
		}
		
	}
	
}
