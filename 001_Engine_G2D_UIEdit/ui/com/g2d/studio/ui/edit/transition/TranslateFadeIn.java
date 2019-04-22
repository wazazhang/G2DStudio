package com.g2d.studio.ui.edit.transition;

import com.g2d.Graphics2D;
import com.g2d.action.Action;
import com.g2d.annotation.Property;

@Property("位移特效")
public class TranslateFadeIn extends UIFadeAction
{
	@Property("起始位置")
	public float FromOffsetX;
	@Property("起始位置")
	public float FromOffsetY;
	
	public TranslateFadeIn() {
		this(0.0f, -800, 10);
	}
	public TranslateFadeIn(float fromX, float fromY, int time) { 
		super(time);
		this.FromOffsetX = fromX;
		this.FromOffsetY = fromY;
	}
	
	@Override
	public void onEnd() {}
	
	@Override
	public void onStart() {}
	
	@Override
	public void onUpdate(Graphics2D g)
	{
		float tx = 0;
		float ty = 0;
		tx = (FromOffsetX) * (1 - getTimerPercent());
		ty = (FromOffsetY) * (1 - getTimerPercent());
		g.translate(tx, ty);
	}
}
