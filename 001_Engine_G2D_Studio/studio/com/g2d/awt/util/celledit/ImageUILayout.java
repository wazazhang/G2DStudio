package com.g2d.awt.util.celledit;

import java.io.File;

import com.cell.gfx.game.CSprite;
import com.g2d.BufferedImage;
import com.g2d.display.ui.layout.UILayout;
import com.g2d.geom.Rectangle;


public class ImageUILayout extends UILayout
{
	final public File 		image_file;
	
	final public File		cpj_file;
	final public String		cpj_imagesName;
	final public int		cpj_tileID;
	final public String		cpj_spriteName;

	final public ImageStyle clip_style;
	final public int 		clip_border;
	
	
	public ImageUILayout(
			BufferedImage 	image, 
			CSprite			sprite,
			File 			image_file, 
			File 			cpj_file, 
			String 			cpj_imagesName, 
			String			cpj_spriteName,
			int 			cpj_tileID,
			Rectangle 		region,
			ImageStyle 		style,
			int 			clip_border) 
	{
		this.image_file		= image_file;

		this.cpj_file		= cpj_file;
		this.cpj_imagesName	= cpj_imagesName;
		this.cpj_spriteName	= cpj_spriteName;
		this.cpj_tileID		= cpj_tileID;
		
		this.clip_style		= style;
		this.clip_border	= clip_border;
		
		if (style == ImageStyle.SPRITE) {
			setSprite(sprite);
			setSpriteAnim(cpj_tileID);
		} else {
			setImages(image, clip_style, clip_border, region);
		}
	}
	
	public ImageUILayout(ImageUILayout src) 
	{
		this.image_file		= src.image_file;

		this.cpj_file		= src.cpj_file;
		this.cpj_imagesName	= src.cpj_imagesName;
		this.cpj_spriteName	= src.cpj_spriteName;
		this.cpj_tileID		= src.cpj_tileID;
		
		this.clip_style		= src.clip_style;
		this.clip_border	= src.clip_border;
		//this.src_rect		= src.src_rect;
		set(src);
	}
	
	@Override
	public ImageUILayout clone() {
		ImageUILayout ret = new ImageUILayout(this);
		return ret;
	}
	
//	public BufferedImage srcImage() {
//		return image;
//	}
	
	public File srcImageFile() {
		return image_file;
	}
	
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		if (image_file!=null) 
		{
			sb.append(image_file.getName());
		}
		else if (cpj_file!=null) 
		{
			if (getStyle() == ImageStyle.SPRITE) {
				sb.append("#"+cpj_file.getName()+"|"+cpj_imagesName+"|"+cpj_spriteName+"|"+cpj_tileID);
			}
			else {
				sb.append("#"+cpj_file.getName()+"|"+cpj_imagesName+"|"+cpj_tileID);
			}
		}
		sb.append(clip_style + "\n");
		sb.append(clip_border);
		return sb.toString();
	}
	
//	@Override
//	protected void onRead(MarkedHashtable data) {
//		super.onRead(data);
//		image = Tools.readImage(UILayoutManager.getInstance().save_path + "/" + image_name);
//		setImages(image, clip_style, clip_border);
//	}
//	
//	@Override
//	protected void onWrite(MarkedHashtable data) {
//		super.onWrite(data);
//		Tools.writeImage(UILayoutManager.getInstance().save_path + "/" + image_name, image);
//	}
	

	
	
	
}
