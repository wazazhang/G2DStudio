package com.cell.gameedit.object;

import com.cell.gameedit.SetObject;

public class SpriteSet implements SetObject
{
	private static final long serialVersionUID = 1L;
	
	final public int Index;
	final public String Name;
	
	public String ImagesName;
	
	public boolean ComplexMode;
	
//	<scene_part index="0" tile="0" x="-11" y="-37" trans="0" 
//	alpha="1" rotate="1" scaleX="1" scaleY="1" shearX="0" shearY="0" />
	public float[] PartX;
	public float[] PartY;
	public float[] PartZ;
	public int[] PartTileID;
	public byte[] PartTileTrans;
	public float[] PartAlpha;
	public float[] PartRotate;
	public float[] PartScaleX;
	public float[] PartScaleY;
	public float[] PartAnchorX;
	public float[] PartAnchorY;
	public short[][] Parts;

	public int[] BlocksMask;
	public float[] BlocksX1;
	public float[] BlocksY1;
	public float[] BlocksW;
	public float[] BlocksH;
	public short[][] Blocks;

	public int AnimateCount;
	public String[] AnimateNames;
	public short[][] FrameAnimate;
	public short[][] FrameCDMap;
	public short[][] FrameCDAtk;
	public short[][] FrameCDDef;
	public short[][] FrameCDExt;
	
	public float[][] FrameAlpha;
	
	public String[][] FrameDatas;

	/**String*/
	public String AppendData;
	
	
	public SpriteSet(int index, String name) {
		this.Index = index;
		this.Name = name;
	}
	
	
	public int getIndex() {
		return Index;
	}

	public String getName() {
		return Name;
	}

	// images[PartTileID[Parts[FrameAnimate[anim][frame]][subpart]]];
	public int getPartImageIndex(int anim, int frame, int subpart) {
		return PartTileID[Parts[FrameAnimate[anim][frame]][subpart]];
	}

	public int getPartTrans(int anim, int frame, int subpart) {
		return PartTileTrans[Parts[FrameAnimate[anim][frame]][subpart]];
	}

	public float getPartX(int anim, int frame, int subpart) {
		return PartX[Parts[FrameAnimate[anim][frame]][subpart]];
	}

	public float getPartY(int anim, int frame, int subpart) {
		return PartY[Parts[FrameAnimate[anim][frame]][subpart]];
	}

	public float getPartZ(int anim, int frame, int subpart) {
		return PartZ[Parts[FrameAnimate[anim][frame]][subpart]];
	}
}
