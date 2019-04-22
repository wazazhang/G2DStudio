package com.g2d.cell.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.cell.gameedit.SetResource;
import com.cell.gameedit.object.SpriteSet;
import com.cell.gameedit.object.WorldSet;
import com.cell.gfx.game.CSprite;
import com.g2d.BufferedImage;
import com.g2d.Graphics2D;
import com.g2d.Tools;
import com.g2d.display.DisplayObject;
import com.g2d.display.Sprite;
import com.g2d.game.rpg.Unit;

public class Scene extends com.g2d.game.rpg.Scene 
{
	public Scene() {
	}
	
	public Scene(WorldContainer world) {
		super(world);
	}
	
	@SuppressWarnings("deprecation")
	public Scene(SetResource resource, String worldname) 	{
		addChild(new WorldContainer(this, resource, resource.WorldTable.get(worldname)));
	}
	

//	-----------------------------------------------------------------------------------------------------------

	public static class WorldContainer extends com.g2d.game.rpg.SceneObjectLayer
	{
		protected ArrayList<WorldObjectMap> static_maps = new ArrayList<Scene.WorldObjectMap>();
		
		final protected SetResource			set_resource;
		final protected WorldSet	set_world;

		public WorldContainer(Scene scene, SetResource resource, WorldSet set_world) 
		{
			super(scene, 
					set_world.GridW, 
					set_world.GridH, 
					set_world.GridXCount, 
					set_world.GridYCount, 
					set_world.Terrian);

			this.set_resource 	= resource;
			this.set_world		= set_world;
			
			for (WorldSet.MapObject wmap : sortWorldObject(new ArrayList<WorldSet.MapObject>(set_world.Maps.values()))){
				WorldObjectMap cm = createWorldObject(resource, wmap);
				if (cm != null) {
					static_maps.add(cm);
					addChild(cm);
				}
			}
			
			for (WorldSet.SpriteObject wspr : sortWorldObject(new ArrayList<WorldSet.SpriteObject>(set_world.Sprs.values()))){
				Unit cs = createWorldObject(resource, wspr);
				if (cs != null) {
					addChild(cs);
				}
			}
			for (WorldSet.ImageObject wspr : sortWorldObject(new ArrayList<WorldSet.ImageObject>(set_world.Imgs.values()))){
				Unit cs = createWorldObject(resource, wspr);
				if (cs != null) {
					addChild(cs);
				}
			}
		}
		
		/**
		 * 自定义地图单元的加载顺序
		 * @param objects
		 * @return
		 */
		protected<T> List<T> sortWorldObject(List<T> objects) {
			return objects;
		}
		
		/**
		 * 返回自定义的地图单位实现
		 * @param set
		 * @param world_set
		 * @return
		 */
		protected Unit createWorldObject(SetResource set, WorldSet.SpriteObject world_set) {
			return new WorldObjectSprite(set, world_set);
		}
		protected Unit createWorldObject(SetResource set, WorldSet.ImageObject world_set) {
			return new WorldObjectImage(set, world_set);
		}

		protected WorldObjectMap createWorldObject(
				SetResource set, 
				WorldSet.MapObject world_set) {
			return new WorldObjectMap(set, world_set, 400, 300);
		}
		
		final public SetResource getSetResource(){
			return set_resource;
		}
		
		final public WorldSet getSetWorld() {
			return set_world;
		}

		public boolean isStreamingImages()
		{
			for (WorldSet.SpriteObject wspr : set_world.Sprs.values()){
				if (set_resource.isStreamingImages(wspr.ImagesID)){
					return true;
				}
			}
			return false;
		}
		
		public BufferedImage createMiniMap(double width, double height) 
		{
			if (isStreamingImages()) {
				return null;
			} else {
				BufferedImage buffer = Tools.createImage((int) width, (int) height);
				Graphics2D g2d = (Graphics2D) buffer.createGraphics();
				double scalew = width / getWidth();
				double scaleh = height / getHeight();
				g2d.scale(scalew, scaleh);
				renderStaticMaps(g2d, 0, 0, getWidth(), getHeight());
				Vector<DisplayObject> childs = getSortedChilds();
				for (DisplayObject d : childs) {
					d.onRender(g2d);
				}
				g2d.dispose();
				return buffer;
			}
		}
		
		public BufferedImage createScreenshot(int x, int y, double width, double height)
		{
			if (isStreamingImages()) {
				return null;
			} else {
				BufferedImage buffer = Tools.createImage((int) width, (int) height);
				Graphics2D g2d = (Graphics2D) buffer.createGraphics();
				for (WorldSet.SpriteObject wspr : set_world.Sprs.values()) {
					CSprite csprite = set_resource.getSprite(wspr.SprID);
					csprite.render(g2d, x+wspr.X, y+wspr.Y, wspr.Anim, wspr.Frame);
				}
				g2d.dispose();
				return buffer;
			}
		}
		
		public void renderStaticMaps(Graphics2D g, float cx, float cy, float cw, float ch) {
			if (static_maps.isEmpty() == false) {
				for (WorldObjectMap om : static_maps) {
					om.updateCamera(cx, cy, cw, ch);
				}
			}
		}
	}
	
	public static class WorldObjectMap extends Sprite
	{
		final protected WorldSet.MapObject map_obj;
		final protected SceneMap map = new SceneMap();
		
		public WorldObjectMap(SetResource set, WorldSet.MapObject obj, int windowW, int windowH) {
			this.map_obj = obj;
			this.map.init(set, obj.MapID, windowW, windowH);
			this.enable = false;
		}
		
		@Override
		public void render(Graphics2D g) {
			map.render(g);
		}
		
		public void updateCamera(float cx, float cy, float cw, float ch) {
			this.x = cx;
			this.y = cy;
			map.setCameraPos((int)cx, (int)cy);
			map.setCameraSize((int)cw, (int)ch);
		}
	}

	public static class WorldObjectSprite extends SceneSprite
	{
		final protected WorldSet.SpriteObject 		set_world_sprite;
		
		public WorldObjectSprite(SetResource set, WorldSet.SpriteObject world_set) 
		{
			set_world_sprite = world_set;
			super.init(set, world_set.SprID);
			setLocation(set_world_sprite.X, set_world_sprite.Y);
		}
		
		@Override
		public void loaded(SetResource set, CSprite cspr, SpriteSet spr) {
			super.loaded(set, cspr, spr);
//			while (set_world_sprite==null) {}
			csprite.setCurrentFrame(set_world_sprite.Anim, set_world_sprite.Frame);
		}
	}
	
	public static class WorldObjectImage extends SceneImage
	{
		final protected WorldSet.ImageObject 		set_world_img;
		
		public WorldObjectImage(SetResource set, WorldSet.ImageObject world_set) 
		{
			super(set, 
					world_set.ImagesID,
					world_set.TileID, 
					world_set.Anchor,
					world_set.Trans);
			set_world_img = world_set;
			setLocation(world_set.X, world_set.Y);
		}
		
	}
	
}
