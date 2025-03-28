package com.g2d.studio;

import java.util.Enumeration;

import com.cell.gameedit.OutputLoader;
import com.cell.gameedit.StreamTiles;
import com.cell.gameedit.object.ImagesSet;
import com.cell.gfx.IImages;
import com.g2d.cell.CellSetResource;



abstract public class StudioResource extends CellSetResource
{
	boolean is_load_resource = false;
	
	public StudioResource(OutputLoader output) throws Exception {
		super(output);
	}
	
	
	final public boolean isLoadImages()
	{
		return is_load_resource;
	}
	
	final public void initAllStreamImages()
	{
		if (!is_load_resource) {
			is_load_resource = true;
			for (ImagesSet ts : ImgTable.values()) {
				IImages images = getImages(ts);
				if (images instanceof StreamTiles) {
					((StreamTiles) images).run();
				}
			}
		}
	}
	
	final public void destoryAllStreamImages(){
		if (is_load_resource) {
			is_load_resource = false;
			if (resource_manager!=null) {
				for (Object obj : resource_manager.values()) {
					if (obj instanceof StreamTiles){
						StreamTiles stiles = (StreamTiles)obj;
						stiles.unloadAllImages();
					}
				}
				dispose();
			}
		}
	}
	

	

}
