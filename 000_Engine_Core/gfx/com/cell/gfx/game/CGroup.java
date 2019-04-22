package com.cell.gfx.game;

import java.io.Serializable;

import com.cell.CMath;
import com.cell.CObject;

/**
 * @author yifeizhang
 * @since 2006-11-30 
 * @version 1.0
 */
public class CGroup extends CObject implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected short Frames[][];
	
	protected int SubIndex;
	protected int SubCount;
	
	protected float w_left = 0;
	protected float w_top = 0;
	protected float w_bottom = 1;
	protected float w_right = 1;
	
	protected float w_width = 0;
	protected float w_height = 0;
	
	/**
	 * @param left
	 * @param top
	 * @param right
	 * @param botton 
	 */
	protected void fixArea(float left, float top, float right, float botton) {
		if (left < w_left)
			w_left =  left;
		if (top < w_top)
			w_top =  top;
		if (right > w_right)
			w_right =  right;
		if (botton > w_bottom)
			w_bottom =  botton;
		
		w_width = (w_right - w_left);
		w_height = (w_bottom - w_top);
	}
	
	/**
	 * fast detect 2 collides's area specify frame, 
	 * area is every part within a frame's max scope. 
	 * </br>
	 * @param c1
	 * @param index1
	 * @param x1
	 * @param y1
	 * @param c2
	 * @param index2
	 * @param x2
	 * @param y2
	 * @return 
	 */
	static public boolean touchArea(
			CGroup c1,int x1,int y1,
			CGroup c2,int x2,int y2){
		if(CMath.intersectRect(
				x1 + c1.w_left,  
				y1 + c1.w_top, 
				x1 + c1.w_right,  
				y1 + c1.w_bottom, 
				x2 + c2.w_left, 
				y2 + c2.w_top, 
				x2 + c2.w_right, 
				y2 + c2.w_bottom)){
			return true;
		}
		return false;
	}
	
	/**
	 * set frame sequence, frames[frame id][part id] = groupted object. </br> 
	 * e.g. : animates's image id ; collides's CCD object ;</br>
	 * @param frames frames[frame id][part id]
	 */
	public void setFrames(short[][] frames){
		Frames = frames;
	}
	
	public short[][] getFrames(){
		return Frames;
	}
	
	/**
	 * set part sequence specify frame index</br>
	 * @param frame frames[frame id][part id]
	 * @param index frame id
	 */
	public void setComboFrame(short[] frame,int index){
		Frames[index] = frame;
	}
	
	/**
	 * get frames count</br>
	 * @return count
	 */
	public int getCount(){
		return Frames.length;
	}
	
	/**
	 * get frames count</br>
	 * @return count
	 */
	public int getComboFrameCount(int index){
		return Frames[index].length;
	}
}
