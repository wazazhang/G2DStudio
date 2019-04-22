package com.g2d.awt.util.celledit;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import com.cell.gameedit.StreamTiles;
import com.cell.gameedit.object.ImagesSet;
import com.cell.gameedit.output.OutputXmlDir;
import com.cell.gfx.IImage;
import com.g2d.cell.CellSetResource;
import com.g2d.java2d.impl.AwtEngine;

public class CellTilesResource extends CellSetResource
{
	final public File xmlfile;
	
	public CellTilesResource(File xmlfile) throws Exception 
	{
		super(new OutputXmlDir(xmlfile.getPath()));
		this.xmlfile = xmlfile;
	}
	
	
	public CellTileIndex findTile(String imgName, int tileIndex) 
	{
		CellTilesImages st = (CellTilesImages)getImages(imgName);
		if (st != null) {
			File imgFile = new File(
					xmlfile.getParent(), 
					st.getImagesSet().Name+"."+st.getImagesSet().Extention);
			if (st.getImage(tileIndex)!= null) {
				return new CellTileIndex(this, 
						xmlfile,
						st.getImagesSet().Name, 
						imgFile, 
						tileIndex, 
						st.getClipRect(tileIndex), 
						AwtEngine.unwrap(st.srcImage));
			}
		}
		return null;
	}
	
	public IImage getImage(String imgName, int tileIndex) {
		StreamTiles st = getImages(imgName);
		if (st != null) {
			IImage img = st.getImage(tileIndex);
			return img;
		}
		return null;
	}
	
	public Rectangle getImageRect(String imgName, int tileIndex) {
		StreamTiles st = getImages(imgName);
		if (st != null) {
			return st.getClipRect(tileIndex);
		}
		return null;
	}

	@Override
	protected StreamTiles getStreamImage(ImagesSet img) throws IOException {
		return new CellTilesImages(img, this);
	}
}
