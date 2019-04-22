package com.g2d.studio.ui.edit.transition;

import com.cell.CMath;
import com.g2d.Graphics2D;
import com.g2d.action.Action;
import com.g2d.annotation.Property;

@Property("下落特效")
public class DropFadeIn extends UIFadeAction
{
	@Property("初始高度")
	public float FromOffsetY;
	@Property("重力")
	public float Gravity;
	
	private float direction;
	private float speed;
	private float curY;
	
	public DropFadeIn() {
		this(-800f, 4f, 30);
	}
	public DropFadeIn(float fromY, float gravity, int time) { 
		super(time);
		this.FromOffsetY = fromY;
		this.Gravity = Math.abs(gravity);
	}
	
	@Override
	public void onEnd() {}
	
	@Override
	public void onStart() {
		direction = CMath.getDirect(0 - FromOffsetY);
		curY = FromOffsetY;
		speed = direction * Gravity;
	}
	
	@Override
	public void onUpdate(Graphics2D g)
	{
		curY += (speed);
		speed += (Gravity);
		g.translate(0, curY);
		if (curY * direction > 0) {
			speed = -speed / 4.0f;
			curY = 0;
			if (Math.abs(speed) < Gravity) {
				stop();
			}
		}
	}
}
