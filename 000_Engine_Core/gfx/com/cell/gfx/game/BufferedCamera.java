package com.cell.gfx.game;

import com.cell.CMath;
import com.cell.gfx.IGraphics;
import com.cell.gfx.IImage;

/**
 * a camera scrollable on a map system, view world unit on screen.</br>
 * @author yifeizhang
 * @since 2006-11-29 
 * @version 1.0
 */
public abstract class BufferedCamera 
{
	private boolean cycmap = false;
	
	final private int WindowW;
	final private int WindowH;
	final private int CellW;
	final private int CellH;
	
	private int WindowBW;
	private int WindowBH;
	private int WorldW;
	private int WorldH;
	
//	-----------------------------------------------------------------------------------
	
	private int X;
	private int Y;
	
	private IImage 		BackBuffer;
	private IGraphics 	bg;


	private int BufW;
	private int BufH;
	private int BufBW;
	private int BufBH;
	
	final private int MapW;
	final private int MapH;
	final private int MapBW;
	final private int MapBH;
	
	private int vBufX;
	private int vBufY;
	private int vBufBX;
	private int vBufBY;
	
	private int vMapX;
	private int vMapY;
	private int vMapBX;
	private int vMapBY;
	
	private int vnT;
	private int vnB;
	private int vnL;
	private int vnR;
	
//	-----------------------------------------------------------------------------------

	public int getXCount() {
		return MapBW;
	}
	public int getYCount() {
		return MapBH;
	}
	
	public BufferedCamera(
			int windowW, int windowH, 
			int cellW, int cellH,
			int mapW, int mapH,
			boolean isCyc)
	{
		this.cycmap = isCyc;
		
		this.WindowW = windowW;
		this.WindowH = windowH;
		
		this.CellW = cellW;
		this.CellH = cellH;
		
		WorldW = windowW;
		WorldH = windowH;
		
		MapBW = CMath.roundMod(mapW, CellW);
		MapBH = CMath.roundMod(mapH, CellH);
		MapW = mapW;
		MapH = mapH;
				
		int divx = windowW % CellW;
		int divy = windowH % CellH;
		WorldW = windowW - divx;
		WorldH = windowH - divy;
		
		if(divx!=0)WorldW += CellW;
		if(divy!=0)WorldH += CellH;

		if(WorldW + CellW > MapW)WorldW = MapW - CellW;
		if(WorldH + CellH > MapH)WorldH = MapH - CellH;
		WindowBW = WorldW / CellW;
		WindowBH = WorldH / CellH;
		
		BufBW = WindowBW + 1;
		BufBH = WindowBH + 1;
		BufW = BufBW*CellW;
		BufH = BufBH*CellH;
		
		BackBuffer = createBuffer(BufW, BufH);
		bg = BackBuffer.createGraphics();		
		
		vBufX = 0;
		vBufY = 0;
		vBufBX = 0;
		vBufBY = 0;
		
		vMapX = 0;
		vMapY = 0;
		vMapBX = 0;
		vMapBY = 0;
		
		vnT = 0;
		vnB = CellH;
		vnL = 0;
		vnR = CellW;
		
		// fill buffer
//		for (int y = 0; y < BufBH; y++) {
//			for (int x = 0; x < BufBW; x++) {
//				int ddx = x * CellW;
//				int ddy = y * CellH;
//				bg.translate(ddx, ddy);
//				renderCell(bg, x, y);
//				bg.translate(-ddx, -ddy);
//			}
//		}
	}

	abstract protected IImage createBuffer(int w, int h); 
	
	public void clean() {
		if (BackBuffer != null) {
			BackBuffer.dispose();
		}
	}
	
	/**
	 * get x within map </br>
	 * @return x coordinate
	 */
	public int getX(){
		return X;
	}
	/**
	 * get y within map</br>�1�7
	 * @return y coordinate
	 */
	public int getY(){
		return Y;
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
	
	public int getCellW() {
		return CellW;
	}
	
	public int getCellH() {
		return CellH;
	}
	
	/**
	 * set position within map</br>�1�7
	 * @param x x 
	 * @param y y
	 */
	public void setPos(int x,int y){
		move(x - X,y - Y);
	}

	/**
	 * move camera within map</br>�1�7
	 * @param px offset x
	 * @param py offset y
	 */
	public void move(int x, int y)
	{
		int dstX = X + x;
		int dstY = Y + y;
		int oldX = X;
		int oldY = Y;
		
		if (!cycmap) {
			if (dstX < 0) {
				x = 0 - X;
			} else if (dstX + WorldW > MapW) {
				x = MapW - (X + WorldW);
			}
			if (dstY < 0) {
				y = 0 - Y;
			} else if (dstY + WorldH > MapH) {
				y = MapH - (Y + WorldH);
			}
		}

		X += x;
		Y += y;
		
		
		{
			if (x != 0)		
			{
				int dx = (x<0?-1:1);
				int dw = Math.abs(x);
				int xCount = (dw/CellW+1)<BufBW?(dw/CellW+1):BufBW;
//				refpos on dest
				vMapX = CMath.cycNum(X,0,MapW);
				vBufX = CMath.cycNum(X,0,BufW);
				vMapBX = CMath.cycNum(vMapX/CellW,0,MapBW);
				vBufBX = CMath.cycNum(vBufX/CellW,0,BufBW);
				vnL = vMapX%CellW;
				vnR = CellW-vnL;
//				fill back buffer
				if (xCount != 0 && (oldX / CellW != X / CellW))
				{
					int mbx = CMath.cycNum(vMapBX,(dx>0?WindowBW:0),MapBW);
					int mby = CMath.cycNum(vMapBY,0,MapBH);
					int bbx = CMath.cycNum(vBufBX,(dx>0?WindowBW:0),BufBW);
					int bby = CMath.cycNum(vBufBY,0,BufBH);
					for(int nx=0;nx<xCount;nx++)
					{
						for(int ny=0;ny<BufBH;ny++)
						{
							int ddx = CMath.cycNum(bbx,-dx*nx,BufBW)*CellW;
							int ddy = CMath.cycNum(bby,ny,BufBH)*CellH;
							bg.translate(ddx, ddy);
							renderCell(bg, 
									CMath.cycNum(mbx,-dx*nx,MapBW), //
									CMath.cycNum(mby,ny,MapBH) //
									);
							bg.translate(-ddx, -ddy);
						}

					}
				}
			}

			if (y != 0) 
			{
				int dy = (y < 0 ? -1 : 1);
				int dh = Math.abs(y);
				int yCount = (dh/CellH+1)<BufBH?(dh/CellH+1):BufBH;
//				refpos				
				vMapY = CMath.cycNum(Y,0,MapH);
				vBufY = CMath.cycNum(Y,0,BufH);
				vMapBY = CMath.cycNum(vMapY/CellH,0,MapBH);
				vBufBY = CMath.cycNum(vBufY/CellH,0,BufBH);
				vnT = vMapY%CellH;
				vnB = CellH-vnT;
//				fill back buffer
				if (yCount != 0 && (oldY / CellH != Y / CellH))
				{
					int mby = CMath.cycNum(vMapBY,(dy>0?WindowBH:0),MapBH);
					int mbx = CMath.cycNum(vMapBX,0,MapBW);
					int bby = CMath.cycNum(vBufBY,(dy>0?WindowBH:0),BufBH);
					int bbx = CMath.cycNum(vBufBX,0,BufBW);
					for(int ny=0;ny<yCount;ny++)
					{
						for(int nx=0;nx<BufBW;nx++)
						{
							int ddx = CMath.cycNum(bbx,nx,BufBW)*CellW;
							int ddy = CMath.cycNum(bby,-dy*ny,BufBH)*CellH;
							bg.translate(ddx, ddy);
							renderCell(bg, 
									CMath.cycNum(mbx,nx,MapBW), //
									CMath.cycNum(mby,-dy*ny,MapBH) //
									);
							bg.translate(-ddx, -ddy);
						}
					}
				}
			}
		}
		
	}

	public void resetBuffer()
	{
		vMapX = CMath.cycNum(X,0,MapW);
		vMapY = CMath.cycNum(Y,0,MapH);
		vMapBX = CMath.cycNum(vMapX/CellW,0,MapBW);
		vMapBY = CMath.cycNum(vMapY/CellH,0,MapBH);
		
		vBufX = CMath.cycNum(X,0,BufW);
		vBufY = CMath.cycNum(Y,0,BufH);
		vBufBX = CMath.cycNum(vBufX/CellW,0,BufBW);
		vBufBY = CMath.cycNum(vBufY/CellH,0,BufBH);
		
		vnL = vMapX%CellW;
		vnR = CellW-vnL;
		vnT = vMapY%CellH;
		vnB = CellH-vnT;
		
		for(int ny=0;ny<BufBH;ny++){
			for(int nx=0;nx<BufBW;nx++){
				int ddx = CMath.cycNum(vBufBX, nx, BufBW)*CellW;
				int ddy = CMath.cycNum(vBufBY, ny, BufBH)*CellH;
				bg.translate(ddx, ddy);
				renderCell(bg, 
						CMath.cycNum(vMapBX,nx,MapBW), 
						CMath.cycNum(vMapBY,ny,MapBH)
						);
				bg.translate(-ddx, -ddy);
			}
		}
	}

	public void resetBufferMapBlock(int bx,int by){

		int sbx = vMapX/CellW;
		int sby = vMapY/CellH;
		int dbx = vBufX/CellW;
		int dby = vBufY/CellH;
		
		// offset
		int offsetx = bx - sbx;
		int offsety = by - sby;
		if(Math.abs(offsetx)>=BufBW)return;
		if(Math.abs(offsety)>=BufBH)return;
		
		dbx = CMath.cycNum(dbx,offsetx,BufBW);
		dby = CMath.cycNum(dby,offsety,BufBH);
		int ddx = dbx*CellW;
		int ddy = dby*CellH;
		bg.translate(ddx, ddy);
		renderCell(bg, bx, by);	
		bg.translate(-ddx, -ddy);
	}
	//	----------------------------------------------------------------------------------------

	
	/**
	 * draw world units on graphics surface
	 * @param g graphics surface
	 */
	public void render(IGraphics g)
	{
		g.pushClip();
    	g.clipRect(0,0,WindowW,WindowH);
		{
			int w1 = BufW-vBufX<=WorldW?BufW-vBufX:WorldW;
			int h1 = BufH-vBufY<=WorldH?BufH-vBufY:WorldH;
			int w2 = WorldW - w1;
			int h2 = WorldH - h1;
			
			if (w1 > 0 && h1 > 0) {
				g.drawRegion(BackBuffer, vBufX, vBufY, w1, h1, 0, 0, 0);
			}
			if (w2 > 0 && h2 > 0) {
				g.drawRegion(BackBuffer, 0, 0, w2, h2, 0, w1, h1);
			}
			if (w1 > 0 && h2 > 0) {
				g.drawRegion(BackBuffer, vBufX, 0, w1, h2, 0, 0, h1);
			}
			if (w2 > 0 && h1 > 0) {
				g.drawRegion(BackBuffer, 0, vBufY, w2, h1, 0, w1, 0);
			}
		}
		
//    	int bx = CMath.cycNum(X,0,MapW)/CellW;
//		int by = CMath.cycNum(Y,0,MapH)/CellH;
//		int dx = CMath.cycNum(X,0,MapW)%CellW;
//		int dy = CMath.cycNum(Y,0,MapH)%CellH;
//		
//		g.translate(-dx, -dy);
//		for(int y=0;y<WindowBH;y++)
//		{
//			for(int x=0;x<WindowBW;x++)
//			{
//				int ddx = x*CellW;
//				int ddy = y*CellH;
//				g.translate(ddx, ddy);
//				renderCell(g, 
//						CMath.cycNum(bx,x,MapBW), 
//						CMath.cycNum(by,y,MapBH)
//						);
//				g.translate(-ddx, -ddy);
//			}
//		}
//		g.translate(dx, dy);
		
		g.popClip();
	}

	
	/**
	 * draw back buffer directly (DEBUG)</br>
	 * @param g graphics surface
	 * @param x x
	 * @param y y
	 * @param c camera position debug color
	 */
	public void renderDebugBackBuffer(IGraphics g, int c) 
	{
		g.drawImage(BackBuffer, 0, 0, 0);
		g.setColor(c);
		int w1 = (BufW-vBufX<=WorldW)?(BufW-vBufX):WorldW;
		int h1 = (BufH-vBufY<=WorldH)?(BufH-vBufY):WorldH;
		int w2 = WorldW - w1;
		int h2 = WorldH - h1;
		if (w1 > 0 && h1 > 0) {
			g.drawRect(vBufX, vBufY, w1-1, h1-1);
		}
		if (w2 > 0 && h2 > 0) {
			g.drawRect(0, 0, w2-1, h2-1);
		}
		if (w1 > 0 && h2 > 0) {
			g.drawRect(vBufX, 0, w1-1, h2-1);
		}
		if (w2 > 0 && h1 > 0) {
			g.drawRect(0, vBufY, w2-1, h1-1);
		}
	}
	

	/**
	 * @param g
	 * @param cellX 原地图格X
	 * @param cellY 原地图格Y
	 */
	abstract protected void renderCell(IGraphics g, int cellX, int cellY);
	
}
