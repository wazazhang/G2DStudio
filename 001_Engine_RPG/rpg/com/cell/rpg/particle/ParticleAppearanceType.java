package com.cell.rpg.particle;

import java.io.Serializable;

import com.cell.CMath;
import com.cell.gfx.game.CSprite;
import com.g2d.Color;
import com.g2d.Graphics2D;
import com.g2d.Image;
import com.g2d.annotation.Property;
import com.g2d.display.particle.Layer;
import com.g2d.display.particle.ParticleAppearance;

public enum ParticleAppearanceType
{
	IMAGE(DisplayNodeImage.class), 
	SPRITE(DisplayNodeSprite.class), 
	;

//	-----------------------------------------------------------------------------------------------
	
	final private Class<? extends ParticleAppearance> type;
	
	ParticleAppearanceType(Class<? extends ParticleAppearance> type) {
		this.type = type;
	}
	
	public Class<? extends ParticleAppearance> getType() {
		return type;
	}
	
//	-----------------------------------------------------------------------------------------------
	
	public static class DisplayNodeImage implements ParticleAppearance, Serializable
	{
		private static final long serialVersionUID = 1L;

		public ParticleResourceType res_type = ParticleResourceType.EFFECT;
		
		/** CPJ工程名 */
		public String	cpj_project_name;
		/** CPJ图片组名*/
		public String	cpj_images_name;
		/** CPJ图片组名*/
		public String	cpj_sprite_name;
		/** CPJ图片组图片编号*/
		public int		cpj_image_id;
		
		@Property("混合方法")
		public ParticleBlend blend = ParticleBlend.SCREEN;

		private transient Image image;
		
		public void setImage(Image src) {
			this.image = src;
		}

		synchronized public Image getImage() {
			return image;
		}

		synchronized public DisplayNodeImage cloneDisplay() {
			return this;
		}
		
		@Override
		public void render(Graphics2D g, Layer layer) {
			g.pushBlendMode();
			try  {
				if (blend != null) {
					blend.setBlend(g);
				}
				if (getImage() != null) {
					g.drawImage(getImage(), -getImage().getWidth() >> 1, -getImage().getHeight() >> 1);
				} else {
					g.setColor(Color.WHITE);
					g.drawArc(-2, -2, 4, 4, 0, 360);
				}
			} finally {
				g.popBlendMode();
			}
		}
	}
	
	public static class DisplayNodeSprite implements ParticleAppearance, Serializable
	{
		private static final long serialVersionUID = 1L;

		public ParticleResourceType res_type = ParticleResourceType.EFFECT;
		
		/** CPJ工程名 */
		public String	cpj_project_name;
		public String	cpj_images_name;
		/** CPJ图片组名*/
		public String	cpj_sprite_name;

//		@Property("精灵动画号")
		public int		sprite_anim;
		public int		sprite_frame;
		@Property("自动播放")
		public boolean	auto_play = false;
		@Property("混合方法")
		public ParticleBlend blend = ParticleBlend.NORMAL;

		transient private CSprite	sprite;
		transient private int		st_current_timer;

		public DisplayNodeSprite cloneDisplay() {
			DisplayNodeSprite ret = new DisplayNodeSprite();
			ret.sprite_anim 		= sprite_anim;
			ret.sprite_frame 		= sprite_frame;
			ret.auto_play			= auto_play;
			ret.sprite 				= sprite;
			ret.blend				= blend;
			return ret;
		}
		
		synchronized public void setSprite(CSprite src) {
			this.sprite = src;
		}

		synchronized public CSprite getSprite() {
			return sprite;
		}

		@Override
		public void render(Graphics2D g, Layer layer) {
			g.pushBlendMode();
			try {
				if (blend != null) {
					blend.setBlend(g);
				}
				if (sprite != null) {
					int acou = sprite.getAnimateCount();
					if (acou > 0) {
						int anim = sprite_anim = CMath.cycNum(sprite_anim, 0, acou);
						int fcou = sprite.getFrameCount(anim);
						if (fcou > 0) {
							int fram = CMath.cycNum(sprite_frame, st_current_timer, fcou);
							sprite.render(g, 0, 0, anim, fram);
						}
					}
					if (auto_play) {
						st_current_timer ++;
					}
				} else {
					g.setColor(Color.WHITE);
					g.drawArc(-2, -2, 4, 4, 0, 360);
				}
			} finally {
				g.popBlendMode();
			}
		}
	}
}
