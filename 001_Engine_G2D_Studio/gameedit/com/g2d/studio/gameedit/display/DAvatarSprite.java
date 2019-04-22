package com.g2d.studio.gameedit.display;

import java.util.ArrayList;

import com.cell.gfx.game.CSprite;
import com.cell.rpg.display.Node;
import com.cell.rpg.display.UnitNode;
import com.cell.rpg.template.TAvatar;
import com.cell.rpg.template.TAvatar.FramePosition;
import com.cell.rpg.template.TAvatar.ParticleEmitter;
import com.g2d.Graphics2D;
import com.g2d.cell.CellSprite;
import com.g2d.display.DisplayObject;
import com.g2d.display.DisplayObjectContainer;
import com.g2d.display.Sprite;
import com.g2d.display.particle.Layer;
import com.g2d.display.particle.ParticleDisplay;
import com.g2d.display.particle.ParticleDisplay.SingleObject;
import com.g2d.studio.Studio;
import com.g2d.studio.cpj.CPJIndex;
import com.g2d.studio.cpj.CPJResourceType;
import com.g2d.studio.cpj.entity.CPJSprite;
import com.g2d.studio.gameedit.dynamic.DAvatar;
import com.g2d.studio.gameedit.dynamic.DEffect;

public class DAvatarSprite extends Sprite
{
	private DAvatar davatar;
	private TAvatar tavatar;

	private CellSprite body_spr;
	private ArrayList<CellSprite> parts = new ArrayList<CellSprite>();
	private ArrayList<ParticleLayer> effects = new ArrayList<ParticleLayer>();
	
	public DAvatarSprite(DAvatar avt) 
	{
		this.davatar = avt;
		this.tavatar = avt.getData();
		
		{
			CPJIndex<CPJSprite> body_index = Studio
					.getInstance()
					.getCPJResourceManager()
					.getNode(CPJResourceType.ACTOR, 
							tavatar.body.cpj_project_name,
							tavatar.body.cpj_object_id);
			if (body_index != null) {
				CPJSprite body = Studio.getInstance().getCPJResourceManager()
						.getNode(body_index);
				body_spr = body.createCellSprite();
				addChild(body_spr);
			}	
		}
		for (Node s : tavatar.body.sub_nodes) {
			UnitNode sub = (UnitNode)s;
			CPJIndex<CPJSprite> sub_index = Studio
					.getInstance()
					.getCPJResourceManager()
					.getNode(CPJResourceType.AVATAR,
							sub.cpj_project_name,
							sub.cpj_object_id);
			if (sub_index != null) {
				CPJSprite subcpj = Studio.getInstance().getCPJResourceManager()
						.getNode(sub_index);
				PartUnit subspr = new PartUnit(subcpj.createCSprite());
				subspr.z = sub.z;
				subspr.blend_mode = sub.blend_mode;
				parts.add(subspr);
				addChild(subspr);
			}
		}
		if (avt.getData().particles != null) {
			for (ParticleEmitter pe : avt.getData().particles) {
				ParticleLayer pl = new ParticleLayer(pe);
				effects.add(pl);
				addChild(pl);
			}
		}
		
	}

	public void setAnimate(int anim, int frame) 
	{
		if (body_spr != null)
		{
			body_spr.cspr.setCurrentFrame(anim, frame);
			for (CellSprite cs : parts) {
				try {
					cs.cspr.setCurrentFrame(anim, frame);
				} catch (Exception e) {}
			}
		}
	}
	
	public void setParticleSpawnLayer(DisplayObjectContainer dc) {
		for (ParticleLayer pl : effects) {
			pl.setSpawnTarget(dc);
		}
	}
	
	@Override
	public void update() {
		sort();
	}
	
	@Override
	public void render(com.g2d.Graphics2D g) 
	{
		super.render(g);
	}
	
	class PartUnit extends CellSprite
	{
		public int blend_mode = Graphics2D.BLEND_MODE_NORMAL;
		public PartUnit(CSprite spr) {
			super(spr);
		}
		@Override
		public void render(Graphics2D g) {
			g.pushBlendMode();
			g.setBlendMode(blend_mode);
			super.render(g);
			g.popBlendMode();
		}
	}

	class ParticleLayer extends ParticleDisplay
	{
		DEffect effect;
		ParticleEmitter data;
		
		float dx = 0;
		float dy = 0;
		float dz = 0;
		
		public ParticleLayer(ParticleEmitter emitter) {
			this.data = emitter;
			this.effect = Studio.getInstance().getObjectManager().getObject(
					DEffect.class, 
					emitter.effectID);
			if (effect != null) {
				effect.loadAppearance();
				setData(effect.getData().particles);
			}
		}

		@Override
		public void update() 
		{				
			super.update();
			
			this.z = data.priority;
			
			if (data.sync_anim) {
				this.dx = 0;
				this.dy = 0;
				this.dz = data.priority;
				this.visible = true;
			}
			else
			{
				FramePosition fp = data.findFirstPos(
						body_spr.cspr.getCurrentAnimate(),
						body_spr.cspr.getCurrentFrame());
				if (fp != null) {
					this.dx = fp.x;
					this.dy = fp.y;
					this.dz = fp.z;
					this.visible = !fp.hide;
				}
				else
				{
					this.dx = 0;
					this.dy = 0;
					this.dz = 0;
					this.visible = false;
				}
			}
			
			
			setAnimateFrame(data.sync_anim, 
					body_spr.cspr.getCurrentAnimate(), 
					body_spr.cspr.getCurrentFrame());
		}
				
		@Override
		protected boolean addParticleNode(SingleObject node, Layer layer)
		{
			if (this.visible) {
				node.pos.y += dy;
				node.pos.x += dx;
				node.priority = (int)(dz*2);
				super.addParticleNode(node, layer);
				return true;
			}
			return false;
		}
	}
	


}
