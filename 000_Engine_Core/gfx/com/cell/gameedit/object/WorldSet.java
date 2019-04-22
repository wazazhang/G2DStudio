package com.cell.gameedit.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import com.cell.gameedit.SetObject;
import com.cell.gfx.IGraphics;

public class WorldSet implements SetObject
{
	private static final long serialVersionUID = 1L;
	
	final public int Index;
	final public String Name;
	public int GridXCount;
	public int GridYCount;
	public int GridW;
	public int GridH;
	public int Width;
	public int Height;

	public Map<Integer, SpriteObject> 	Sprs 		= new LinkedHashMap<Integer, SpriteObject>();
	public Map<Integer, MapObject> 		Maps 		= new LinkedHashMap<Integer, MapObject>();
	public Map<Integer, ImageObject> 	Imgs 		= new LinkedHashMap<Integer, ImageObject>();
	
	public Map<Integer, WaypointObject> WayPoints 	= new LinkedHashMap<Integer, WaypointObject>();
	public Map<Integer, RegionObject> 	Regions 	= new LinkedHashMap<Integer, RegionObject>();
	public Map<Integer, EventObject> 	Events	 	= new LinkedHashMap<Integer, EventObject>();
	
	public String Data;
	public int[][] Terrian;

	
	public WorldSet(int index, String name) {
		this.Index = index;
		this.Name = name;
	}
	
	
	public int getIndex() {
		return Index;
	}

	public String getName() {
		return Name;
	}

	public int getTerrainCell(int grid_x, int grid_y) {
		return Terrian[grid_x][grid_y];
	}
	

	public static class MapObject implements Serializable 
	{
		private static final long serialVersionUID = 1L;

		public int Index;
		public String UnitName;
		public String MapID;
		public String ImagesID;
		public int X;
		public int Y;
		public int Priority;
		public String Data;

	}

	public static class SpriteObject implements Serializable 
	{
		private static final long serialVersionUID = 1L;

		public int Index;
		public String UnitName;
		public String SprID;
		public String ImagesID;
		public int Anim;
		public int Frame;
		public int X;
		public int Y;
		public int Priority;
		public String Data;

	}
	

	
    public static final int TRANS_NONE = 0;
    public static final int TRANS_ROT90 = 1;
    public static final int TRANS_ROT180 = 2;
    public static final int TRANS_ROT270 = 3;
    public static final int TRANS_MIRROR = 4;
    public static final int TRANS_MIRROR_ROT90 = 5;
    public static final int TRANS_MIRROR_ROT180 = 6;
    public static final int TRANS_MIRROR_ROT270 = 7;

	public static class ImageObject implements Serializable 
	{
		private static final long serialVersionUID = 1L;

		public int Index;
		public String UnitName;
		public String ImagesID;
		public int TileID;
		public IGraphics.ImageAnchor Anchor;
		public IGraphics.ImageTrans Trans;
		public int X;
		public int Y;
		public int Priority;
		public String Data;
		
	}
	
	
	public static class WaypointObject implements Serializable 
	{
		private static final long serialVersionUID = 1L;

		public int Index;
		public int X;
		public int Y;
		public String Data;

		public Map<Integer, WaypointObject> Nexts = 
				new LinkedHashMap<Integer, WaypointObject>();

	}

	public static class RegionObject implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public int Index;
		public int X;
		public int Y;
		public int W;
		public int H;
		public String Data;

	}
	
	public static class EventObject implements Serializable 
	{
		private static final long serialVersionUID = 1L;

		public int Index;
		public long ID;
		public String EventName;
		public String EventFile;
		public int X;
		public int Y;
		public String Data;
	}
}
