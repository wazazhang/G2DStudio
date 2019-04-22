package com.g2d.awt.util.celledit;

import java.io.File;

import com.cell.gfx.game.CSprite;

public class CellSpriteIndex 
{
	final public CellTilesResource res;

	final public File				xml_file;
	
	final public String 			imagesName;
	final public String 			spriteName;
	final public int 				anim;
	
	final public CSprite			sprite;
	
	public CellSpriteIndex(
			CellTilesResource res, 
			File xml_file,
			String imagesName,
			String spriteName,
			int anim,
			CSprite sprite)
	{
		this.res = res;
		this.xml_file = xml_file;
		this.imagesName = imagesName;
		this.spriteName = spriteName;
		this.anim = anim;
		this.sprite = sprite;
	}
	
}
