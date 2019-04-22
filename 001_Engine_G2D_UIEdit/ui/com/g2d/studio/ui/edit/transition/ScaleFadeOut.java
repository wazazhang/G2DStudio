package com.g2d.studio.ui.edit.transition;

import com.g2d.Graphics2D;
import com.g2d.action.Action;
import com.g2d.annotation.Property;

@Property("缩放特效")
public class ScaleFadeOut extends UIFadeAction
{
	@Property("目标尺寸")
	public float ToScaleX;
	@Property("目标尺寸")
	public float ToScaleY;

	public ScaleFadeOut() {
		this(10);
	}
	public ScaleFadeOut(int time) {
		this(0, time);
	}
	public ScaleFadeOut(float to, int time) {
		this(to, to, time);
	}
	
	public ScaleFadeOut(float toX, float toY, int time) {
		super(time);
		this.ToScaleX = toX;
		this.ToScaleY = toY;
	}
	
	@Override
	public void onEnd() {}
	
	@Override
	public void onStart() {}
	
	@Override
	public void onUpdate(Graphics2D g)
	{
		g.translate(getOwner().getWidth()/2, getOwner().getHeight()/2);
		float scalex = (1 - ToScaleX) * (1 - getTimerPercent());
		float scaley = (1 - ToScaleY) * (1 - getTimerPercent());
		g.scale(scalex, scaley);
		g.translate(-getOwner().getWidth()/2, -getOwner().getHeight()/2);
	}
	
	
}
