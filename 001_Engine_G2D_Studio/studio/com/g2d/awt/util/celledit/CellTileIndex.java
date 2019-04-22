package com.g2d.awt.util.celledit;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

public class CellTileIndex 
{
	final public CellTilesResource res;
	
	final public File xml_file;
	
	final public String 			imagesName;
	
	final public File				imageFile;
	
	final public int 				tileID;
	
	final public Rectangle			tileRect;
	
	final public BufferedImage 		image;

	public CellTileIndex(
			CellTilesResource res,
			File xml_file,
			String imagesName, 
			File imageFile,
			int tileID,
			Rectangle tileRect,
			BufferedImage image) {
		super();
		this.res = res;
		this.xml_file = xml_file;
		this.imagesName = imagesName;
		this.imageFile = imageFile;
		this.tileID = tileID;
		this.tileRect = tileRect;
		this.image = image;
	}
	
	public BufferedImage imageRegion() {
		return image.getSubimage(
				tileRect.x,
				tileRect.y, 
				tileRect.width, 
				tileRect.height);
	}
	
	
	
}
