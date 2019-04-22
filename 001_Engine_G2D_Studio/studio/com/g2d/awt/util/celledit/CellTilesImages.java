package com.g2d.awt.util.celledit;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.cell.gameedit.object.ImagesSet;
import com.g2d.BufferedImage;
import com.g2d.Engine;
import com.g2d.cell.CellSetResource;
import com.g2d.cell.CellStreamTiles;

public class CellTilesImages extends CellStreamTiles
{
	final public BufferedImage srcImage;
	
	public CellTilesImages(ImagesSet img, CellSetResource res) throws IOException
	{
		super(img, res);

		String image_extentions = getImageExtentions();
		byte[] idata = set.getOutput().loadRes(
				img.getName() + "." + image_extentions, null);
		srcImage = Engine.getEngine().createImage(
				new ByteArrayInputStream(idata));
		for (int i = 0; i < images.length; i++) {
			if (img.getClipW(i) > 0 && img.getClipH(i) > 0) {
				images[i] = srcImage.subImage(
						img.getClipX(i), 
						img.getClipY(i),
						img.getClipW(i), 
						img.getClipH(i));
			}
		}
		run();
	}
	
	
	@Override
	protected void initImages() throws Throwable {
		
	}
}
