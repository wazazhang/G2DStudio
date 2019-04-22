package com.cell.rpg.particle;

import com.cell.gfx.game.CSprite;
import com.g2d.BufferedImage;

public interface ParticleDisplayResource 
{
	public BufferedImage getEffectImage(
			String cpj_project_name, 
			String cpj_sprite_name, 
			int index);
	
	public CSprite getEffectSprite(
			String cpj_project_name, 
			String cpj_sprite_name);
}
