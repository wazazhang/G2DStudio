package com.g2d.studio.ui.edit.transition;

import com.g2d.Graphics2D;
import com.g2d.action.Action;
import com.g2d.annotation.Property;

@Property("渐变特效")
public class AlphaFadeIn extends UIFadeAction
{
	@Property("从多少开始渐变")
	public float FromAlpha;

	public AlphaFadeIn() { 
		this(10);
	}
	public AlphaFadeIn(int time) { 
		this(0, time);
	}
	public AlphaFadeIn(float alpha, int time) { 
		super(time);
		this.FromAlpha = alpha;
	}
	
	@Override
	public void onEnd() {}
	
	@Override
	public void onStart() {}
	
	@Override
	public void onUpdate(Graphics2D g)
	{
		float alpha = (1 - FromAlpha) * getTimerPercent();
		g.setAlpha(alpha);
	}
}
