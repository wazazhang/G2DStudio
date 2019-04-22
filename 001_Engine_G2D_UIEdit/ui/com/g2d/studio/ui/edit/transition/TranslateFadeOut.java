package com.g2d.studio.ui.edit.transition;

import com.g2d.Graphics2D;
import com.g2d.action.Action;
import com.g2d.annotation.Property;

@Property("位移特效")
public class TranslateFadeOut extends UIFadeAction
{
	@Property("目标位置")
	public float ToOffsetX;
	@Property("目标位置")
	public float ToOffsetY;

	public TranslateFadeOut() {
		this(0.0f, -800, 10);
	}
	public TranslateFadeOut(float toX, float toY, int time) { 
		super(time);
		this.ToOffsetX = toX;
		this.ToOffsetY = toY;
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
		tx = (ToOffsetX) * (getTimerPercent());
		ty = (ToOffsetY) * (getTimerPercent());
		g.translate(tx, ty);
	}
}
