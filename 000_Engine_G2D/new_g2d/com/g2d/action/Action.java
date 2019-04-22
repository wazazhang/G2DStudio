package com.g2d.action;

import com.g2d.Graphics2D;
import com.g2d.annotation.Property;
import com.g2d.display.DisplayObject;

public abstract class Action 
{
	@Property("持续时间(帧)")
	public int life_time;
	
	private int timer;
	private boolean end = false;
	private DisplayObject owner;
	
	
	public Action(int lifeTime) {
		this.life_time = lifeTime;
	}
	
	public int getLifeTime() {
		return life_time;
	}
	
	public DisplayObject getOwner() {
		return owner;
	}
	
	public int getTimer() {
		return timer;
	}
	
	public float getTimerPercent() {
		return ((float)timer) / ((float)life_time);
	}
	
	public void start(DisplayObject obj) {
		this.owner = obj;
		this.timer = 0;
		this.end = false;
		this.onStart();
	}
	public void stop() {
		end = true;
		onEnd();
	}
	public boolean isEnd() {
		return end;
	}
	public void update(Graphics2D g) {
		onUpdate(g);
		if (timer >= life_time) {
			stop();
		}
		timer++;
	}
	
	
	abstract protected void onEnd();
	
	abstract protected void onStart();
	
	abstract protected void onUpdate(Graphics2D g);
	
}
