
package com.cell.gfx.game;


import com.cell.CMath;
import com.cell.gfx.IGraphics;

/**
 * a camera scrollable on a map system, view world unit on screen.</br>
 * @author yifeizhang
 * @since 2006-11-29 
 * @version 1.0
 */
public class CCameraDirect extends CUnit 
{
	protected int WindowBW;
	protected int WindowBH;
	private int WorldW;
	private int WorldH;
//	-----------------------------------------------------------------------------------
	
	protected int X;
	protected int Y;
	
	protected CMap 	Map;
	
	protected int CellW;
	protected int CellH;

	protected int BufW;
	protected int BufH;
	protected int BufBW;
	protected int BufBH;
	
	protected int MapW;
	protected int MapH;
	protected int MapBW;
	protected int MapBH;
	
//	-----------------------------------------------------------------------------------

//	-----------------------------------------------------------------------------------

	/**
	 * Construct a camera based in specify map.</br>
	 * @param windowX screen window x </br>�1�7
	 * @param windowY screen window y </br>�1�7
	 * @param windowW screen window width </br>
	 * @param windowH screen window height </br>
	 * @param map one map can be used </br>
	 * @param isBackBuffer is back buffer mode, </br>
	 * true : can run game faster use more heap memory, </br>
	 * false : run game slowly not use memory buffer.</br>
	 * @param backColor 
	 */
	public CCameraDirect(int windowW, int windowH, CMap map)
	{
		Map = map;
		
		CellW = map.CellW;
		CellH = map.CellH;
		
		MapBW = Map.getXCount();
		MapBH = Map.getYCount();
		MapW = Map.getWidth();
		MapH = Map.getHeight();
		
		setSize(windowW, windowH);
	}

	public void setSize(int windowW, int windowH) 
	{
		WorldW = windowW;
		WorldH = windowH;
		
		if(WorldW > MapW)WorldW = MapW;
		if(WorldH > MapH)WorldH = MapH;
		WindowBW = WorldW / CellW + 2;
		WindowBH = WorldH / CellH + 2;
	}
	/**
	 * get camera size width</br>�1�7
	 * @return width
	 */
	public int getWidth(){
		return WorldW;
	}
	/**
	 * get camera size height</br>
	 * @return height
	 */
	public int getHeight(){
		return WorldH;
	}
	
	/**
	 * set position within map</br>�1�7
	 * @param x x 
	 * @param y y
	 */
	public void setPos(int x,int y)
	{
		X = x;
		Y = y;
		if (!Map.IsCyc) {
			X = Math.max(0, X);
			Y = Math.max(0, Y);
			X = Math.min(Map.getWidth() - getWidth(),  X);
			Y = Math.min(Map.getHeight()- getHeight(), Y);
		}
	}

	
	/**
	 * move camera within map</br>�1�7
	 * @param px offset x
	 * @param py offset y
	 */
	public void mov(int x, int y)
	{
		setPos(X+x, Y+y);
	}

	
	//	----------------------------------------------------------------------------------------

	
	/**
	 * draw world units on graphics surface
	 * @param g graphics surface
	 */
	public void render(IGraphics g) {
		if (!Visible)return;
		
		g.pushClip();
    	g.clipRect(0,0,WorldW,WorldH);
		{
			int bx = CMath.cycMod(CMath.cycNum(X, 0, MapW), CellW);
			int by = CMath.cycMod(CMath.cycNum(Y, 0, MapH), CellH);
			int dx = CMath.cycNum(X, 0, MapW) % CellW;
			int dy = CMath.cycNum(Y, 0, MapH) % CellH;

			int cpx = -dx;
			int cpy = -dy;

			for (int y = 0; y < WindowBH; y++) {
				for (int x = 0; x < WindowBW; x++) {
					Map.renderCell(g, 
							cpx + x * CellW, 
							cpy + y * CellH,
							CMath.cycNum(bx, x, MapBW),
							CMath.cycNum(by, y, MapBH));
				}
			}
		}
		
		g.popClip();
	}

	
}