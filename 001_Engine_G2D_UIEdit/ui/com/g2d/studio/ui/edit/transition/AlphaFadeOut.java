package com.g2d.studio.ui.edit.transition;

import com.g2d.Graphics2D;
import com.g2d.action.Action;
import com.g2d.annotation.Property;

@Property("渐变特效")
public class AlphaFadeOut extends UIFadeAction
{
	@Property("渐变到Alpha")
	public float ToAlpha;

	public AlphaFadeOut() {
		this(10);
	}
	public AlphaFadeOut(int time) { 
		this(0, time);
	}
	public AlphaFadeOut(float alpha, int time) { 
		super(time);
		this.ToAlpha = alpha;
	}
	
	@Override
	public void onEnd() {}
	
	@Override
	public void onStart() {}
	
	@Override
	public void onUpdate(Graphics2D g)
	{
		float alpha = (1 - ToAlpha) * (1 - getTimerPercent());
		g.setAlpha(alpha);
	}
}
