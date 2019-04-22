package com.g2d.cell.game;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.cell.gameedit.SetResource;
import com.cell.gameedit.object.MapSet;
import com.cell.gfx.IImages;
import com.cell.gfx.game.CCameraDirect;
import com.cell.gfx.game.CMap;
import com.g2d.Graphics2D;

public class SceneMap
{
//	------------------------------------------------------------------------------------------------------------------------------------
//	resource
	
	transient protected CCameraDirect	 	camera;
	transient protected CMap	 			map;
	
	transient protected SetResource			set_resource;
	transient protected MapSet	 			set_map;
	
//	------------------------------------------------------------------------------------------------------------------------------------
	
	public void init(
			SetResource set, 
			String mapID,
			int windowW, 
			int windowH)
	{
		this.set_resource 	= set;
		this.set_map		= set.MapTable.get(mapID);
		
		IImages tiles = set.getImages(set_map.ImagesName);
		this.map = set.createMapFromSet(set_map, tiles, false);
		
		this.camera = new CCameraDirect(windowW, windowH, map);
	}
		
	public SceneImage clone() {
		throw new NotImplementedException();
	}
	
	public SetResource getSetResource() {
		return set_resource;
	}
	
	public void render(Graphics2D g) {
		this.camera.render(g);
	}

	public void setCameraPos(int x, int y) {
		this.camera.setPos(x, y);
	}
	public void setCameraSize(int w, int h) {
		this.camera.setSize(w, h);
	}


}
