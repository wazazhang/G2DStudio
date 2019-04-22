package com.g2d.studio.ui.edit.transition;

import com.g2d.Graphics2D;
import com.g2d.action.Action;
import com.g2d.annotation.Property;

@Property("缩放特效")
public class ScaleFadeIn extends UIFadeAction
{
	@Property("起始尺寸")
	public float FromScaleX;
	@Property("起始尺寸")
	public float FromScaleY;

	public ScaleFadeIn() {
		this(10);
	}
	public ScaleFadeIn(int time) {
		this(0, time);
	}
	public ScaleFadeIn(float from, int time) {
		this(from, from, time);
	}
	public ScaleFadeIn(float fromX, float fromY, int time) {
		super(time);
		this.FromScaleX = fromX;
		this.FromScaleY = fromY;
	}
	
	
	@Override
	public void onEnd() {}
	
	@Override
	public void onStart() {}
	
	@Override
	public void onUpdate(Graphics2D g)
	{
		g.translate(getOwner().getWidth()/2, getOwner().getHeight()/2);
		float scalex = (1 - FromScaleX) * getTimerPercent();
		float scaley = (1 - FromScaleY) * getTimerPercent();
		g.scale(scalex, scaley);
		g.translate(-getOwner().getWidth()/2, -getOwner().getHeight()/2);
	}
	
	
}
