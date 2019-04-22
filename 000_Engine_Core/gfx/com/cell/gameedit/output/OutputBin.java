package com.cell.gameedit.output;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.cell.CUtil;
import com.cell.gameedit.object.ImagesSet;
import com.cell.gameedit.object.MapSet;
import com.cell.gameedit.object.SpriteSet;
import com.cell.gameedit.object.WorldSet;
import com.cell.gameedit.object.WorldSet.WaypointObject;
import com.cell.gfx.IGraphics.ImageAnchor;
import com.cell.gfx.IGraphics.ImageTrans;
import com.cell.gfx.game.CCD;
import com.cell.io.LittleIODeserialize;
import com.cell.io.LittleIOSerialize;
import com.cell.io.TextDeserialize;
import com.cell.util.EnumManager;

abstract public class OutputBin extends BaseOutput
{
	final static public int VERSION_VALUE_1001		= 0x01000001;
	final static public int VERSION_VALUE_1002		= 0x01000002;
	final static public int VERSION_VALUE_2000		= 0x02000000;
	
	final static public byte[] VERSION 			= new byte[]{0,0,0,0};
	final static public byte[] CE_HEAD_START 	= new byte[]{'C', 'E', 'F', 'S'};
	final static public byte[] CE_IMG_START 	= new byte[]{'C', 'E', 'I', 'M'};
	final static public byte[] CE_SPR_START 	= new byte[]{'C', 'E', 'S', 'P'};
	final static public byte[] CE_MAP_START 	= new byte[]{'C', 'E', 'M', 'P'};
	final static public byte[] CE_WORLD_START 	= new byte[]{'C', 'E', 'W', 'D'};
	
	private byte[] version = new byte[VERSION.length];
	private long filesize;
	private String	image_type;
	private boolean	image_tile;
	private boolean	image_group;
	

	public int getVersionValue() {
		int version_value = 0;
		for (int i = 0; i < VERSION.length; i++) {
			version_value |= (((int) version[i]) << (i * 8));
		}
		return version_value;
	}
	
	public void loadFromBin(InputStream is) throws Exception 
	{
		byte[] head_trunk = new byte[4];
		is.read(head_trunk);
		if (!CUtil.arrayEquals(head_trunk, CE_HEAD_START)) {
			throw new Exception("Unknow File Type : CE_HEAD_START");
		}
		is.read(version);
		filesize 	= LittleIODeserialize.getLong(is);
		image_group = LittleIODeserialize.getBoolean(is);
		image_tile	= LittleIODeserialize.getBoolean(is);
		image_type	= LittleIODeserialize.getString(is);
		{
			// ///////////////////////////////////////////
			{
				is.read(head_trunk);
				if (!CUtil.arrayEquals(head_trunk, CE_IMG_START)) {
					throw new Exception("Unknow File Type : CE_IMG_START");
				}
				int imgSize = LittleIODeserialize.getInt(is);
				for (int i=0; i<imgSize; i++) {
					ImagesSet im = readImagesSet(is);
					super.ImgTable.put(im.Name, im);
				}
			}
			// ///////////////////////////////////////////
			{
				is.read(head_trunk);
				if (!CUtil.arrayEquals(head_trunk, CE_SPR_START)) {
					throw new Exception("Unknow File Type : CE_SPR_START");
				}
				int sprSize = LittleIODeserialize.getInt(is);
				for (int i=0; i<sprSize; i++) {
					SpriteSet im = readSpriteSet(is);
					super.SprTable.put(im.Name, im);
				}
			}
			// ///////////////////////////////////////////
			{
				is.read(head_trunk);
				if (!CUtil.arrayEquals(head_trunk, CE_MAP_START)) {
					throw new Exception("Unknow File Type : CE_MAP_START");
				}
				int mapSize = LittleIODeserialize.getInt(is);
				for (int i=0; i<mapSize; i++) {
					MapSet im = readMapSet(is);
					super.MapTable.put(im.Name, im);
				}
			}
			// ///////////////////////////////////////////
			{
				is.read(head_trunk);
				if (!CUtil.arrayEquals(head_trunk, CE_WORLD_START)) {
					throw new Exception("Unknow File Type : CE_WORLD_START");
				}
				int wdSize = LittleIODeserialize.getInt(is);
				for (int i=0; i<wdSize; i++) {
					WorldSet im = readWorldSet(is);
					super.WorldTable.put(im.Name, im);
				}
			}
			// ///////////////////////////////////////////
		}
	}
	
	
//	------------------------------------------------------------------------------------------------
	
	public long getFileSize() {
		return filesize;
	}
	
	@Override
	public String getImageExtentions() {
		return image_type;
	}
	
	@Override
	public boolean isGroup() {
		return image_group;
	}
	
	@Override
	public boolean isTile() {
		return image_tile;
	}
	
	@Override
	public void dispose() 
	{}

	@Override
	public String getResRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return null;
	}

	private ImagesSet readImagesSet(InputStream bis) throws Exception 
	{
		ImagesSet im = new ImagesSet(
				LittleIODeserialize.getInt(bis), 
				LittleIODeserialize.getString(bis));
		im.Count = LittleIODeserialize.getInt(bis);
		
		im.ClipsX = new int[im.Count];
		im.ClipsY = new int[im.Count];
		im.ClipsW = new int[im.Count];
		im.ClipsH = new int[im.Count];
		im.ClipsKey = new String[im.Count];
		for (int i=0; i<im.Count; i++) {
			if (LittleIODeserialize.getBoolean(bis)) {
				im.ClipsX[i] = LittleIODeserialize.getInt(bis);
				im.ClipsY[i] = LittleIODeserialize.getInt(bis);
				im.ClipsW[i] = LittleIODeserialize.getInt(bis);
				im.ClipsH[i] = LittleIODeserialize.getInt(bis);
				im.ClipsKey[i] = LittleIODeserialize.getString(bis);
			}
		}
		
		im.Extention 	= LittleIODeserialize.getString(bis);
		im.IsTiles		= LittleIODeserialize.getBoolean(bis);
		im.ImageInfo	= LittleIODeserialize.getString(bis);
		
		im.AppendData	= LittleIODeserialize.getString(bis);
		
		return im;
	}

	private SpriteSet readSpriteSet(InputStream bis) throws Exception 
	{
		SpriteSet im = new SpriteSet(
				LittleIODeserialize.getInt(bis), 
				LittleIODeserialize.getString(bis));
		im.ImagesName = LittleIODeserialize.getString(bis);
		if (getVersionValue() >= VERSION_VALUE_1001)
		{
			im.ComplexMode = LittleIODeserialize.getBoolean(bis);
		}
		{
			int partCount = LittleIODeserialize.getInt(bis);
			
			im.PartX 		= new float	[partCount];
			im.PartY 		= new float	[partCount];
			im.PartZ 		= new float	[partCount];
			im.PartTileID 	= new int	[partCount];
			im.PartTileTrans= new byte	[partCount];
			
			im.PartAlpha	= new float	[partCount];
			im.PartRotate	= new float	[partCount];
			im.PartScaleX	= new float	[partCount];
			im.PartScaleY	= new float	[partCount];
			im.PartAnchorX	= new float	[partCount];
			im.PartAnchorY	= new float	[partCount];
			
			for (int i=0; i<partCount; i++)
			{
				im.PartX[i]			= LittleIODeserialize.getFloat(bis);
				im.PartY[i]			= LittleIODeserialize.getFloat(bis);
				im.PartZ[i]			= LittleIODeserialize.getFloat(bis);
				im.PartTileID[i]	= LittleIODeserialize.getInt(bis);
				im.PartTileTrans[i]	= LittleIODeserialize.getByte(bis);
				
				im.PartAlpha[i]		= LittleIODeserialize.getFloat(bis);
				im.PartRotate[i]	= LittleIODeserialize.getFloat(bis);
				im.PartScaleX[i]	= LittleIODeserialize.getFloat(bis);
				im.PartScaleY[i]	= LittleIODeserialize.getFloat(bis);
				im.PartAnchorX[i]	= LittleIODeserialize.getFloat(bis);
				im.PartAnchorY[i]	= LittleIODeserialize.getFloat(bis);
			}
			
			im.Parts = new short[LittleIODeserialize.getInt(bis)][];
			for (int i=0; i<im.Parts.length; i++) {
				im.Parts[i] = new short[LittleIODeserialize.getInt(bis)];
				for (int j=0; j<im.Parts[i].length; j++) {
					im.Parts[i][j] = LittleIODeserialize.getShort(bis);
				}
			}
		}
		{
			int cdCount = LittleIODeserialize.getInt(bis);
			im.BlocksMask	= new int	[cdCount];
			im.BlocksX1		= new float	[cdCount];
			im.BlocksY1		= new float	[cdCount];
			im.BlocksW		= new float	[cdCount];
			im.BlocksH		= new float	[cdCount];
			
			for (int i=0; i<cdCount; i++) 
			{
				im.BlocksMask[i]	= LittleIODeserialize.getInt(bis);
				im.BlocksX1[i]		= LittleIODeserialize.getFloat(bis);
				im.BlocksY1[i]		= LittleIODeserialize.getFloat(bis);
				im.BlocksW[i]		= LittleIODeserialize.getFloat(bis);
				im.BlocksH[i]		= LittleIODeserialize.getFloat(bis);
			}
			
			im.Blocks = new short[LittleIODeserialize.getInt(bis)][];
			for (int i=0; i<im.Blocks.length; i++) {
				im.Blocks[i] = new short[LittleIODeserialize.getInt(bis)];
				for (int j=0; j<im.Blocks[i].length; j++) {
					im.Blocks[i][j] = LittleIODeserialize.getShort(bis);
				}
			}
		}
		{
			im.AnimateCount = LittleIODeserialize.getInt(bis);
			im.AnimateNames = new String[im.AnimateCount];
			im.FrameAnimate = new short[im.AnimateCount][];
			im.FrameCDMap = new short[im.AnimateCount][];
			im.FrameCDAtk = new short[im.AnimateCount][];
			im.FrameCDDef = new short[im.AnimateCount][];
			im.FrameCDExt = new short[im.AnimateCount][];
			im.FrameAlpha = new float[im.AnimateCount][];
			im.FrameDatas = new String[im.AnimateCount][];
			for (int i=0; i<im.AnimateCount; i++) {
				im.AnimateNames[i] = LittleIODeserialize.getString(bis);
				int frameCount = LittleIODeserialize.getInt(bis);
				im.FrameAnimate[i] = new short[frameCount];
				im.FrameCDMap[i] = new short[frameCount];
				im.FrameCDAtk[i] = new short[frameCount];
				im.FrameCDDef[i] = new short[frameCount];
				im.FrameCDExt[i] = new short[frameCount];
				im.FrameAlpha[i] = new float[frameCount];
				im.FrameDatas[i] = new String[frameCount];
				for (int j=0; j<frameCount; j++) {
					im.FrameAnimate[i][j]	= LittleIODeserialize.getShort(bis);
					im.FrameCDMap[i][j]		= LittleIODeserialize.getShort(bis);
					im.FrameCDAtk[i][j]		= LittleIODeserialize.getShort(bis);
					im.FrameCDDef[i][j]		= LittleIODeserialize.getShort(bis);
					im.FrameCDExt[i][j]		= LittleIODeserialize.getShort(bis);
					im.FrameAlpha[i][j]		= LittleIODeserialize.getFloat(bis);
					im.FrameDatas[i][j]		= LittleIODeserialize.getString(bis);
				}
			}
		}

		im.AppendData	= LittleIODeserialize.getString(bis);
		
		return im;
	}

	private MapSet readMapSet(InputStream bis) throws Exception 
	{
		MapSet im = new MapSet(
				LittleIODeserialize.getInt(bis), 
				LittleIODeserialize.getString(bis));
		
		im.ImagesName	= LittleIODeserialize.getString(bis);
		
		im.XCount		= LittleIODeserialize.getInt(bis);
		im.YCount		= LittleIODeserialize.getInt(bis);
		im.CellW		= LittleIODeserialize.getInt(bis);
		im.CellH		= LittleIODeserialize.getInt(bis);
		im.LayerCount	= LittleIODeserialize.getInt(bis);
		
		int blockCount = LittleIODeserialize.getInt(bis);
		im.BlocksType	= new int[blockCount];
		im.BlocksMask	= new int[blockCount];
		im.BlocksX1		= new int[blockCount];
		im.BlocksY1		= new int[blockCount];
		im.BlocksX2		= new int[blockCount];
		im.BlocksY2		= new int[blockCount];
		im.BlocksW		= new int[blockCount];
		im.BlocksH		= new int[blockCount];
		for (int i=0; i<blockCount; i++) {
			im.BlocksType[i]	= LittleIODeserialize.getInt(bis);
			im.BlocksMask[i]	= LittleIODeserialize.getInt(bis);
			im.BlocksX1[i]		= LittleIODeserialize.getInt(bis);
			im.BlocksY1[i]		= LittleIODeserialize.getInt(bis);
			im.BlocksX2[i]		= LittleIODeserialize.getInt(bis);
			im.BlocksY2[i]		= LittleIODeserialize.getInt(bis);
			im.BlocksW[i]		= LittleIODeserialize.getInt(bis);
			im.BlocksH[i]		= LittleIODeserialize.getInt(bis);
		}

		im.TerrainTile = new int[im.LayerCount][im.YCount][im.XCount];
		im.TerrainFlip = new int[im.LayerCount][im.YCount][im.XCount];
		im.TerrainFlag = new int[im.LayerCount][im.YCount][im.XCount];
		for (int i=0; i<im.LayerCount; i++) {
			for (int y=0; y<im.YCount; y++) {
				for (int x=0; x<im.XCount; x++) {
					im.TerrainTile[i][y][x] = LittleIODeserialize.getInt(bis);
					im.TerrainFlip[i][y][x] = LittleIODeserialize.getInt(bis);
					im.TerrainFlag[i][y][x] = LittleIODeserialize.getInt(bis);
				}
			}
		}

		im.AppendData	= LittleIODeserialize.getString(bis);
		
		return im;
	}

	private WorldSet readWorldSet(InputStream bis) throws Exception 
	{
		WorldSet im = new WorldSet(
				LittleIODeserialize.getInt(bis), 
				LittleIODeserialize.getString(bis));
		
		im.GridXCount	= LittleIODeserialize.getInt(bis);
		im.GridYCount	= LittleIODeserialize.getInt(bis);
		im.GridW		= LittleIODeserialize.getInt(bis);
		im.GridH		= LittleIODeserialize.getInt(bis);
		im.Width		= LittleIODeserialize.getInt(bis);
		im.Height		= LittleIODeserialize.getInt(bis);

		im.Terrian = new int[im.GridXCount][im.GridYCount];
		for (int x=0; x<im.Terrian.length; x++) {
			for (int y=0; y<im.Terrian[x].length; y++) {
				im.Terrian[x][y] = LittleIODeserialize.getInt(bis);
			}
		}
//		if (false)
		{
			{
				int sprsCount = LittleIODeserialize.getInt(bis);
				for (int i=0; i<sprsCount; i++) 
				{
					WorldSet.SpriteObject o = new WorldSet.SpriteObject();
					
					o.Index		= LittleIODeserialize.getInt(bis);
					o.UnitName	= LittleIODeserialize.getString(bis);
					o.SprID		= LittleIODeserialize.getString(bis);
					o.ImagesID	= LittleIODeserialize.getString(bis);
					
					o.Anim		= LittleIODeserialize.getInt(bis);
					o.Frame		= LittleIODeserialize.getInt(bis);
					o.X			= LittleIODeserialize.getInt(bis);
					o.Y			= LittleIODeserialize.getInt(bis);
					o.Priority	= LittleIODeserialize.getInt(bis);
	
					o.Data		= LittleIODeserialize.getString(bis);
					
					im.Sprs.put(o.Index, o);
				}
			}
			{
				int mapsCount = LittleIODeserialize.getInt(bis);
				for (int i=0; i<mapsCount; i++) 
				{
					WorldSet.MapObject o = new WorldSet.MapObject();
					
					o.Index		= LittleIODeserialize.getInt(bis);
					o.UnitName	= LittleIODeserialize.getString(bis);
					o.MapID		= LittleIODeserialize.getString(bis);
					o.ImagesID	= LittleIODeserialize.getString(bis);
					
					o.X			= LittleIODeserialize.getInt(bis);
					o.Y			= LittleIODeserialize.getInt(bis);
					o.Priority	= LittleIODeserialize.getInt(bis);
	
					o.Data		= LittleIODeserialize.getString(bis);
					
					im.Maps.put(o.Index, o);
				}
			}
			{
				int imgsCount = LittleIODeserialize.getInt(bis);
				for (int i=0; i<imgsCount; i++) 
				{
					WorldSet.ImageObject o = new WorldSet.ImageObject();
					
					o.Index		= LittleIODeserialize.getInt(bis);
					o.UnitName	= LittleIODeserialize.getString(bis);
					o.ImagesID	= LittleIODeserialize.getString(bis);
					
					o.TileID	= LittleIODeserialize.getInt(bis);
					o.X			= LittleIODeserialize.getInt(bis);
					o.Y			= LittleIODeserialize.getInt(bis);
					o.Priority	= LittleIODeserialize.getInt(bis);
					
					o.Anchor	= EnumManager.getEnum(ImageAnchor.class, 
									LittleIODeserialize.getInt(bis));
					o.Trans		= EnumManager.getEnum(ImageTrans.class, 
									LittleIODeserialize.getInt(bis));
	
					o.Data		= LittleIODeserialize.getString(bis);
	
					im.Imgs.put(o.Index, o);
				}
			}
			{
				int wpCount = LittleIODeserialize.getInt(bis);
				for (int i=0; i<wpCount; i++)
				{
					WorldSet.WaypointObject o = new WorldSet.WaypointObject();
					
					o.Index	= LittleIODeserialize.getInt(bis);
					o.X		= LittleIODeserialize.getInt(bis);
					o.Y		= LittleIODeserialize.getInt(bis);
					o.Data	= LittleIODeserialize.getString(bis);
					
					int nextCount = LittleIODeserialize.getInt(bis);
					for (int j=0; j<nextCount; j++) {
						int nid = LittleIODeserialize.getInt(bis);
						o.Nexts.put(nid, null);
					}
					
					im.WayPoints.put(o.Index, o);
				}
				for (WorldSet.WaypointObject o : im.WayPoints.values()) {
					for (Integer nid : o.Nexts.keySet()) {
						o.Nexts.put(nid, im.WayPoints.get(nid));
					}
				}
			}
			{
				int rgCount = LittleIODeserialize.getInt(bis);
				for (int i=0; i<rgCount; i++)
				{			
					WorldSet.RegionObject o = new WorldSet.RegionObject();
					
					o.Index	= LittleIODeserialize.getInt(bis);
					o.X		= LittleIODeserialize.getInt(bis);
					o.Y		= LittleIODeserialize.getInt(bis);
					o.W		= LittleIODeserialize.getInt(bis);
					o.H		= LittleIODeserialize.getInt(bis);
					o.Data	= LittleIODeserialize.getString(bis);
					
					im.Regions.put(o.Index, o);
				}
			}
			{
				int evCount = LittleIODeserialize.getInt(bis);
				for (int i=0; i<evCount; i++)
				{
					WorldSet.EventObject o = new WorldSet.EventObject();
					
					o.Index		= LittleIODeserialize.getInt(bis);
					o.ID		= LittleIODeserialize.getLong(bis);
					o.EventName	= LittleIODeserialize.getString(bis);
					o.EventFile	= LittleIODeserialize.getString(bis);
					o.X			= LittleIODeserialize.getInt(bis);
					o.Y			= LittleIODeserialize.getInt(bis);
					o.Data		= LittleIODeserialize.getString(bis);
					
					im.Events.put(o.Index, o);
				}
			}
		}
		
		im.Data	= LittleIODeserialize.getString(bis);
		
		return im;
	}
//	////////////////////////////////////////////////////////////////////////
//	// 
//	////////////////////////////////////////////////////////////////////////
	public static class Convert
	{
		final private byte[] version = new byte[VERSION.length];
		final private int versionValue;
		
		public Convert(String ver)
		{
			String[] versions = CUtil.splitString(ver, ".", 4);
			for (int i = 0; i < VERSION.length; i++) {
				version[i] = Byte.parseByte(versions[i]);
			}
			int b0 = version[0];
			int b1 = version[1];
			int b2 = version[2];
			int b3 = version[3];
				
			versionValue = 
					((b0 << 24) & 0xFF000000)|
					((b1 << 16) & 0x00FF0000) |
					((b2 <<  8) & 0x0000FF00) |
					(b3 & 0x000000FF);;
		}
		
		public byte[] saveToBin(BaseOutput xml) throws Exception
		{
			if (xml.isEmpty()){return null;}
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			try {
				bos.write(OutputBin.CE_HEAD_START);
				bos.write(version);
				LittleIOSerialize.putLong(bos, 0);
				LittleIOSerialize.putBoolean(bos, xml.isGroup());
				LittleIOSerialize.putBoolean(bos, xml.isTile());
				LittleIOSerialize.putString(bos, xml.getImageExtentions());
				
				// ///////////////////////////////////////////
				bos.write(OutputBin.CE_IMG_START);
				LittleIOSerialize.putInt(bos, xml.ImgTable.size());
				for (ImagesSet im : xml.ImgTable.values()) {
					writeImagesSet(bos, im);
				}
				// ///////////////////////////////////////////
				bos.write(OutputBin.CE_SPR_START);
				LittleIOSerialize.putInt(bos, xml.SprTable.size());
				for (SpriteSet im : xml.SprTable.values()) {
					writeSpriteSet(bos, im);
				}
				// ///////////////////////////////////////////
				bos.write(OutputBin.CE_MAP_START);
				LittleIOSerialize.putInt(bos, xml.MapTable.size());
				for (MapSet im : xml.MapTable.values()) {
					writeMapSet(bos, im);
				}
				// ///////////////////////////////////////////
				bos.write(OutputBin.CE_WORLD_START);
				LittleIOSerialize.putInt(bos, xml.WorldTable.size());
				for (WorldSet im : xml.WorldTable.values()) {
					writeWorldSet(bos, im);
				}
				// ///////////////////////////////////////////
			} finally {
				bos.close();
			}
			byte[] data = bos.toByteArray();
			ByteBuffer bb = ByteBuffer.wrap(data);
			bb.order(ByteOrder.LITTLE_ENDIAN);
			bb.position(OutputBin.CE_HEAD_START.length + OutputBin.VERSION.length);
			bb.putLong(data.length);
			bb.flip();
			return data;
		}
		
		
		private void writeImagesSet(OutputStream bos, ImagesSet im) throws Exception 
		{
			LittleIOSerialize.putInt	(bos, im.Index);
			LittleIOSerialize.putString	(bos, im.Name);
			LittleIOSerialize.putInt	(bos, im.Count);
			
			for (int i=0; i<im.Count; i++) {
				if (im.ClipsW[i] == 0 || im.ClipsH[i] == 0) {
					LittleIOSerialize.putBoolean(bos, false);
				} else {
					LittleIOSerialize.putBoolean(bos, true);
					LittleIOSerialize.putInt(bos, im.ClipsX[i]);
					LittleIOSerialize.putInt(bos, im.ClipsY[i]);
					LittleIOSerialize.putInt(bos, im.ClipsW[i]);
					LittleIOSerialize.putInt(bos, im.ClipsH[i]);
					LittleIOSerialize.putString(bos, im.ClipsKey[i]);
				}
			}
			
			LittleIOSerialize.putString	(bos, im.Extention);
			LittleIOSerialize.putBoolean(bos, im.IsTiles);
			LittleIOSerialize.putString	(bos, im.ImageInfo);
			if (versionValue >= VERSION_VALUE_1002)
			{
				LittleIOSerialize.putInt	(bos, im.TotalW);
				LittleIOSerialize.putInt	(bos, im.TotalH);
				LittleIOSerialize.putInt	(bos, im.SplitSize);
			}
			LittleIOSerialize.putString	(bos, im.AppendData);
		}
		
		private void writeSpriteSet(OutputStream bos, SpriteSet im) throws Exception 
		{
			LittleIOSerialize.putInt	(bos, im.Index);
			LittleIOSerialize.putString	(bos, im.Name);
			LittleIOSerialize.putString	(bos, im.ImagesName);
			if (versionValue >= VERSION_VALUE_1001)
			{
				LittleIOSerialize.putBoolean	(bos, im.ComplexMode);
			}
			{
				int partCount = im.PartX.length;
				LittleIOSerialize.putInt	(bos, partCount);
				for (int i=0; i<partCount; i++) {
					if (versionValue >= VERSION_VALUE_2000)
					{
						LittleIOSerialize.putFloat	(bos, im.PartX[i]);
						LittleIOSerialize.putFloat	(bos, im.PartY[i]);
						LittleIOSerialize.putFloat	(bos, im.PartZ[i]);
					}
					else
					{
						LittleIOSerialize.putShort	(bos, (short)im.PartX[i]);
						LittleIOSerialize.putShort	(bos, (short)im.PartY[i]);
						LittleIOSerialize.putShort	(bos, (short)im.PartZ[i]);
					}
					LittleIOSerialize.putInt	(bos, im.PartTileID[i]);
					LittleIOSerialize.putByte	(bos, im.PartTileTrans[i]);
					
					LittleIOSerialize.putFloat	(bos, im.PartAlpha[i]);
					LittleIOSerialize.putFloat	(bos, im.PartRotate[i]);
					LittleIOSerialize.putFloat	(bos, im.PartScaleX[i]);
					LittleIOSerialize.putFloat	(bos, im.PartScaleY[i]);
					LittleIOSerialize.putFloat	(bos, im.PartAnchorX[i]);
					LittleIOSerialize.putFloat	(bos, im.PartAnchorY[i]);
				}
				LittleIOSerialize.putInt(bos, im.Parts.length);
				for (int i=0; i<im.Parts.length; i++) {
					LittleIOSerialize.putInt(bos, im.Parts[i].length);
					for (int j=0; j<im.Parts[i].length; j++) {
						LittleIOSerialize.putShort(bos, im.Parts[i][j]);
					}
				}
			}
			{
				int cdCount = im.BlocksMask.length;
				LittleIOSerialize.putInt(bos, cdCount);
				for (int i=0; i<cdCount; i++) {
					LittleIOSerialize.putInt	(bos, im.BlocksMask[i]);
					if (versionValue >= VERSION_VALUE_2000)
					{
						LittleIOSerialize.putFloat	(bos, im.BlocksX1[i]);
						LittleIOSerialize.putFloat	(bos, im.BlocksY1[i]);
						LittleIOSerialize.putFloat	(bos, im.BlocksW[i]);
						LittleIOSerialize.putFloat	(bos, im.BlocksH[i]);
					}
					else
					{
						LittleIOSerialize.putShort	(bos, (short)im.BlocksX1[i]);
						LittleIOSerialize.putShort	(bos, (short)im.BlocksY1[i]);
						LittleIOSerialize.putShort	(bos, (short)im.BlocksW[i]);
						LittleIOSerialize.putShort	(bos, (short)im.BlocksH[i]);
					}
				}
				LittleIOSerialize.putInt(bos, im.Blocks.length);
				for (int i=0; i<im.Blocks.length; i++) {
					LittleIOSerialize.putInt(bos, im.Blocks[i].length);
					for (int j=0; j<im.Blocks[i].length; j++) {
						LittleIOSerialize.putShort(bos, im.Blocks[i][j]);
					}
				}
			}
			{
				LittleIOSerialize.putInt(bos, im.AnimateCount);
				for (int i=0; i<im.AnimateCount; i++) {
					LittleIOSerialize.putString(bos, im.AnimateNames[i]);
					int frameCount = im.FrameAnimate[i].length;
					LittleIOSerialize.putInt(bos, frameCount);
					for (int j=0; j<frameCount; j++) {
						LittleIOSerialize.putShort(bos, im.FrameAnimate[i][j]);
						LittleIOSerialize.putShort(bos, im.FrameCDMap[i][j]);
						LittleIOSerialize.putShort(bos, im.FrameCDAtk[i][j]);
						LittleIOSerialize.putShort(bos, im.FrameCDDef[i][j]);
						LittleIOSerialize.putShort(bos, im.FrameCDExt[i][j]);
						LittleIOSerialize.putFloat(bos, im.FrameAlpha[i][j]);
						LittleIOSerialize.putString(bos, im.FrameDatas[i][j]);
					}
				}
			}
	
			LittleIOSerialize.putString	(bos, im.AppendData);
		}
	
		private void writeMapSet(OutputStream bos, MapSet im) throws Exception 
		{
			LittleIOSerialize.putInt	(bos, im.Index);
			LittleIOSerialize.putString	(bos, im.Name);
			LittleIOSerialize.putString	(bos, im.ImagesName);
			
			LittleIOSerialize.putInt	(bos, im.XCount);
			LittleIOSerialize.putInt	(bos, im.YCount);
			LittleIOSerialize.putInt	(bos, im.CellW);
			LittleIOSerialize.putInt	(bos, im.CellH);
			LittleIOSerialize.putInt	(bos, im.LayerCount);
			
			int blockCount = im.BlocksType.length;
			LittleIOSerialize.putInt(bos, blockCount);
			for (int i=0; i<blockCount; i++) {
				LittleIOSerialize.putInt	(bos, im.BlocksType[i]);
				LittleIOSerialize.putInt	(bos, im.BlocksMask[i]);
				LittleIOSerialize.putInt	(bos, im.BlocksX1[i]);
				LittleIOSerialize.putInt	(bos, im.BlocksY1[i]);
				LittleIOSerialize.putInt	(bos, im.BlocksX2[i]);
				LittleIOSerialize.putInt	(bos, im.BlocksY2[i]);
				LittleIOSerialize.putInt	(bos, im.BlocksW[i]);
				LittleIOSerialize.putInt	(bos, im.BlocksH[i]);
			}
	
			for (int i=0; i<im.LayerCount; i++) {
				for (int y=0; y<im.YCount; y++) {
					for (int x=0; x<im.XCount; x++) {
						LittleIOSerialize.putInt	(bos, im.TerrainTile[i][y][x]);
						LittleIOSerialize.putInt	(bos, im.TerrainFlip[i][y][x]);
						LittleIOSerialize.putInt	(bos, im.TerrainFlag[i][y][x]);
					}
				}
			}
	
			LittleIOSerialize.putString	(bos, im.AppendData);
			
		}
		
		private void writeWorldSet(OutputStream bos, WorldSet im) throws Exception 
		{
			LittleIOSerialize.putInt	(bos, im.Index);
			LittleIOSerialize.putString	(bos, im.Name);
			
			LittleIOSerialize.putInt	(bos, im.GridXCount);
			LittleIOSerialize.putInt	(bos, im.GridYCount);
			LittleIOSerialize.putInt	(bos, im.GridW);
			LittleIOSerialize.putInt	(bos, im.GridH);
			LittleIOSerialize.putInt	(bos, im.Width);
			LittleIOSerialize.putInt	(bos, im.Height);
			
			for (int x=0; x<im.Terrian.length; x++) {
				for (int y=0; y<im.Terrian[x].length; y++) {
					LittleIOSerialize.putInt(bos, im.Terrian[x][y]);
				}
			}
	//		if (false)
			{
				{
					int sprsCount = im.Sprs.size();
					LittleIOSerialize.putInt(bos, sprsCount);
					for (WorldSet.SpriteObject o : im.Sprs.values()) 
					{
						LittleIOSerialize.putInt	(bos, o.Index);
						LittleIOSerialize.putString	(bos, o.UnitName);
						LittleIOSerialize.putString	(bos, o.SprID);
						LittleIOSerialize.putString	(bos, o.ImagesID);
						
						LittleIOSerialize.putInt	(bos, o.Anim);
						LittleIOSerialize.putInt	(bos, o.Frame);
						LittleIOSerialize.putInt	(bos, o.X);
						LittleIOSerialize.putInt	(bos, o.Y);
						LittleIOSerialize.putInt	(bos, o.Priority);
		
						LittleIOSerialize.putString	(bos, o.Data);
					}
				}
				{
					int mapsCount = im.Maps.size();
					LittleIOSerialize.putInt(bos, mapsCount);
					for (WorldSet.MapObject o : im.Maps.values()) 
					{
						LittleIOSerialize.putInt	(bos, o.Index);
						LittleIOSerialize.putString	(bos, o.UnitName);
						LittleIOSerialize.putString	(bos, o.MapID);
						LittleIOSerialize.putString	(bos, o.ImagesID);
						
						LittleIOSerialize.putInt	(bos, o.X);
						LittleIOSerialize.putInt	(bos, o.Y);
						LittleIOSerialize.putInt	(bos, o.Priority);
		
						LittleIOSerialize.putString	(bos, o.Data);
					}
				}
				{
					int imgsCount = im.Imgs.size();
					LittleIOSerialize.putInt(bos, imgsCount);
					for (WorldSet.ImageObject o : im.Imgs.values()) 
					{
						LittleIOSerialize.putInt	(bos, o.Index);
						LittleIOSerialize.putString	(bos, o.UnitName);
						LittleIOSerialize.putString	(bos, o.ImagesID);
						
						LittleIOSerialize.putInt	(bos, o.TileID);
						LittleIOSerialize.putInt	(bos, o.X);
						LittleIOSerialize.putInt	(bos, o.Y);
						LittleIOSerialize.putInt	(bos, o.Priority);
						LittleIOSerialize.putInt	(bos, o.Anchor.value);
						LittleIOSerialize.putInt	(bos, o.Trans.value);
		
						LittleIOSerialize.putString	(bos, o.Data);
					}
				}
				{
					int wpCount = im.WayPoints.size();
					LittleIOSerialize.putInt(bos, wpCount);
					for (WorldSet.WaypointObject o : im.WayPoints.values())
					{
						LittleIOSerialize.putInt	(bos, o.Index);
						LittleIOSerialize.putInt	(bos, o.X);
						LittleIOSerialize.putInt	(bos, o.Y);
						LittleIOSerialize.putString	(bos, o.Data);
						
						int nextCount = o.Nexts.size();
						LittleIOSerialize.putInt(bos, nextCount);
						for (WaypointObject n : o.Nexts.values()) {
							LittleIOSerialize.putInt(bos, n.Index);
						}
					}
				}
				{
					int rgCount = im.Regions.size();
					LittleIOSerialize.putInt(bos, rgCount);
					for (WorldSet.RegionObject o : im.Regions.values())
					{
						LittleIOSerialize.putInt	(bos, o.Index);
						LittleIOSerialize.putInt	(bos, o.X);
						LittleIOSerialize.putInt	(bos, o.Y);
						LittleIOSerialize.putInt	(bos, o.W);
						LittleIOSerialize.putInt	(bos, o.H);
						LittleIOSerialize.putString	(bos, o.Data);
					}
				}
				{
					int evCount = im.Events.size();
					LittleIOSerialize.putInt(bos, evCount);
					for (WorldSet.EventObject o : im.Events.values())
					{
						LittleIOSerialize.putInt	(bos, o.Index);
						LittleIOSerialize.putLong	(bos, o.ID);
						LittleIOSerialize.putString	(bos, o.EventName);
						LittleIOSerialize.putString	(bos, o.EventFile);
						LittleIOSerialize.putInt	(bos, o.X);
						LittleIOSerialize.putInt	(bos, o.Y);
						LittleIOSerialize.putString	(bos, o.Data);
					}
				}
			}
	
			LittleIOSerialize.putString	(bos, im.Data);
			
		}
		
	}
}
