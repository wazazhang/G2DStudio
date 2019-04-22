package com.cell.rpg.display;

import com.g2d.Graphics2D;


public class UnitNode extends Node
{
	private static final long serialVersionUID = 1L;

	public int		cur_anim 	= 0;
	public float	scale_x		= 1;
	public float	scale_y		= 1;
	public int		blend_mode	= Graphics2D.BLEND_MODE_NORMAL;
	
	public UnitNode(String cpj_name, String cpj_sprite_id)
	{
		super(cpj_name, cpj_sprite_id, Type.SPRITE);
	}
}
