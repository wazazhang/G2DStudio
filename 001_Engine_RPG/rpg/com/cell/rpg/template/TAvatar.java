package com.cell.rpg.template;

import java.io.Serializable;
import java.util.TreeSet;
import java.util.Vector;

import com.cell.CUtil.ICompare;
import com.cell.rpg.anno.PropertyAdapter;
import com.cell.rpg.anno.PropertyType;
import com.cell.rpg.display.UnitNode;
import com.cell.rpg.struct.TPosition;
import com.g2d.annotation.Property;


public class TAvatar extends TemplateNode
{
	public UnitNode body;
	
	public TAvatar(int id, String name) {
		super(id, name);
	}
	
	@Override
	public Class<?>[] getSubAbilityTypes() {
		return new Class<?>[] {};
	}

	@Property("绑定的粒子")
	public Vector<ParticleEmitter> particles = new Vector<ParticleEmitter>();
	
	public static class FramePosition implements Comparable<FramePosition>
	{
		public int anim;
		public int frame;
		
		public float x;
		public float y;
		public float z;
		public boolean hide = false;
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof FramePosition) {
				FramePosition ot = (FramePosition)obj;
				if (this.anim != ot.anim) return false;
				if (this.frame != ot.frame) return false;
				return true;
			}
			return false;
		}
		
		@Override
		public int compareTo(FramePosition o) {
			if (this.anim != o.anim) {
				return this.anim - o.anim;
			} else {
				return frame - o.frame;
			}
		}
		
		
	}
	
	public static class ParticleEmitter implements Serializable 
	{
		private static final long serialVersionUID = 1L;

		public int effectID = -1;

		@Property("排序优先级")
		public int priority = 0;
		
		public TreeSet<FramePosition> pos = new TreeSet<FramePosition>();

		@Property("同步精灵动画")
		public boolean sync_anim = false;
		
		@Override
		public String toString() {
			return "EFFECT: "+effectID + " (p="+priority+")";
		}
		

		public FramePosition findFirstPos(int anim, int frame) {
			for (int i=frame; i>=0; --i) {
				for (FramePosition fp : pos) {
					if (fp.anim == anim && fp.frame == i) {
						return fp;
					}
				}
			}
			return null;
		}
		
		public FramePosition getPos(int anim, int frame) {
			for (FramePosition fp : pos) {
				if (fp.anim == anim && fp.frame == frame) {
					return fp;
				}
			}
			return null;
		}
		
		public void putPos(FramePosition fp)
		{
			if (pos.contains(fp)) {
				pos.remove(fp);
			}
			pos.add(fp);
		}
		
		public FramePosition setCurrentPosHide(int anim, int frame, boolean hide) {
			FramePosition fp = getPos(anim, frame);
			if (fp == null) {
				fp = new FramePosition();
				fp.anim = anim;
				fp.frame = frame;
			}
			fp.hide = hide;
			this.putPos(fp);
			return fp;
		}

		public FramePosition setCurrentPosZ(int anim, int frame, float z) {
			FramePosition fp = getPos(anim, frame);
			if (fp == null) {
				fp = new FramePosition();
				fp.anim = anim;
				fp.frame = frame;
			}
			fp.z = z;
			this.putPos(fp);
			return fp;
		}
		
		public FramePosition setCurrentOffset(int anim, int frame, float x, float y) {
			FramePosition fp = getPos(anim, frame);
			if (fp == null) {
				fp = new FramePosition();
				fp.anim = anim;
				fp.frame = frame;
			}
			fp.x = x;
			fp.y = y;
			this.putPos(fp);
			return fp;
		}
	}
}
