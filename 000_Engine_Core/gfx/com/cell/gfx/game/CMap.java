
package com.cell.gfx.game;

import java.io.Serializable;

import com.cell.CIO;
import com.cell.gfx.IGraphics;
import com.cell.gfx.IImage;
import com.cell.gfx.IImages;




/**
 * @author yifeizhang
 * @since 2006-11-30 
 * @version 1.0
 */
public class CMap extends CUnit implements Serializable
{
	private static final long serialVersionUID = 1L;
	
//	----------------------------------------------------------------------------------------------
	protected class MapLayer
	{
		int[][] MatrixTile;
		int[][] MatrixFlip;
		int[][] MatrixFlag;
	}
	
	protected int CellW;
	protected int CellH;
	protected int XCount;
	protected int YCount;

	protected IImages			Tiles;
	protected CCollides 		Collides;
	
	protected MapLayer[]		layers;
	
	protected boolean IsCyc 	= false;

	protected int Width;
	protected int Height;
//	----------------------------------------------------------------------------------------------

	public CMap(
			IImages tiles, 
			CCollides collides,
			int cellw, 
			int cellh, 
			int xcount, 
			int ycount,
			int layerCount,
			int[][][] tile_matrix, 
			int[][][] flip_matrix,
			int[][][] flag_matrix,
			boolean isCyc
			) {
		IsCyc 		= isCyc;
		
		Tiles 		= tiles;
		Collides 	= collides;
		
		CellW = cellw;
		CellH = cellh;
		XCount = xcount;
		YCount = ycount;

		layers = new MapLayer[layerCount];
		for (int i=0; i<layerCount; i++) {
			MapLayer ly = new MapLayer();
			ly.MatrixTile 	= CIO.cloneObject(tile_matrix[i]);
			ly.MatrixFlip 	= CIO.cloneObject(flip_matrix[i]);
			ly.MatrixFlag 	= CIO.cloneObject(flag_matrix[i]);
			layers[i] = ly;
		}
		
		Width 	= xcount * CellW;
		Height	= ycount * CellH;
	}

//	----------------------------------------------------------------------------------------------------------------
	
	public IImages getTiles() {
		return Tiles;
	}
	
	/**
	 * before added world call
	 * @param cyc
	 */
	public void setCyc(boolean cyc) {
		IsCyc = cyc;
	}
	
	public int getLayerCount() {
		return layers.length;
	}	
	
	public int getWidth() {
		return Width;
	}

	public int getHeight() {
		return Height;
	}

	public int getXCount() {
		return XCount;
	}

	public int getYCount() {
		return YCount;
	}

	public int getCellW() {
		return CellW;
	}

	public int getCellH() {
		return CellH;
	}

	public CCD getCD(int layer, int bx, int by) {
		return Collides.getCD(layers[layer].MatrixFlag[by][bx]);
	}

	public IImage getImage(int layer, int bx, int by) {
		return Tiles.getImage(layers[layer].MatrixTile[by][bx]);
	}

	public int getFlag(int layer, int bx, int by) {
		return layers[layer].MatrixFlag[by][bx];
	}

	public int getTile(int layer, int bx, int by) {
		return layers[layer].MatrixTile[by][bx];
	}

	public void putFlag(int layer, int bx, int by, int data) {
		layers[layer].MatrixFlag[by][bx] = data;
	}
	//	-------------------------------------------------------------------------------

	public void renderCell(IGraphics g, int layer, int x, int y, int cellX, int cellY)
	{
		Tiles.render(g, 
				layers[layer].MatrixTile[cellY][cellX], 
				x, y,
				layers[layer].MatrixFlip[cellY][cellX]);
	}
	
	public void renderCell(IGraphics g, int x, int y, int cellX, int cellY)
	{
		for (int layer=0; layer<layers.length; layer++) {
			Tiles.render(g, 
					layers[layer].MatrixTile[cellY][cellX], 
					x, y,
					layers[layer].MatrixFlip[cellY][cellX]);
		}
	}
}