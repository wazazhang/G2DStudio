package com.g2d.display;


import com.cell.math.MathVector;
import com.cell.math.TVector;
import com.cell.math.Vector;
import com.g2d.Graphics2D;

public class Sprite extends InteractiveObject implements Vector
{	
	/**缩放比率*/
	public double		scale_x, scale_y;
	
	/**旋转参数(弧度)*/
	public double		rotate;
	
	/**旋转基准点*/
	public TVector		rotate_origin	= new TVector(0, 0);
	
	/**透明度(范围0.0~1.0)*/
	public float		alpha;
	
	
	
	public Sprite() 
	{
		scale_x			= 1;
		scale_y			= 1;
		rotate			= 0;
		alpha			= 1f;
		
		enable 			= false;
		enable_input	= false;
		enable_focus	= false;
	}
	
//	 ------------------------------------------------------------------------------------------------------------------------
//	 math util
	
	public void move(double dx, double dy){
		MathVector.move(this, dx, dy);
	}
	
	/**
	 * 通过极坐标来移动
	 * @param angle 弧度
	 * @param distance
	 */
	public void movePolar(double angle, double distance){
		MathVector.movePolar(this, angle, distance);
	}
	
	/**
	 * 向目标移动
	 * @param dx
	 * @param dy
	 * @return 是否到达目标点
	 */
	public boolean moveTo(double x, double y, double distance){
		return MathVector.moveTo(this, x, y, distance);
	}

//	 ------------------------------------------------------------------------------------------------------------------------
	
//	final protected void onUpdate(DisplayObjectContainer parent) {
//		super.onUpdate(parent);
//	}
	
	
	final public void onRender(Graphics2D g) {
		super.onRender(g);
	}

	@Override
	final protected void renderBefore(Graphics2D g) 
	{
		super.renderBefore(g);
		
		if (scale_x != 1 || scale_y != 1) {
			g.scale(scale_x, scale_y);
		}
		if (rotate != 0) {
			if (rotate_origin != null) {
				g.rotate(rotate, rotate_origin.x, rotate_origin.y);
			} else {
				g.rotate(rotate);
			}
		}
		if (alpha < 1f) {
			g.setAlpha(alpha);
		}
	}
	
	public void added(DisplayObjectContainer parent) {}
	public void removed(DisplayObjectContainer parent) {}
	public void render(Graphics2D g) {}
	public void update() {}

//	------------------------------------------------------------------
//	ACTION
//	------------------------------------------------------------------

	public float getX() {
		return (float)x;
	}
	public float getY() {
		return (float)y;
	}
	public float getScaleX() { 
		return (float)scale_x;
	}
	public float getScaleY() {
		return (float)scale_y;
	}
	public float getRotate() { 
		return (float)rotate;
	}
	public float getAlpha() { 
		return alpha;
	}
	
	public void setPos(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public void setScale(float sx, float sy) { 
		this.scale_x = sx;
		this.scale_y = sy;
	}
	public void setRotate(float angle) { 
		this.rotate = angle;
		this.rotate_origin = null;
	}
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	
	public void addPos(float x, float y) { 
		this.x += x;
		this.y += y;
	}
	public void addScale(float sx, float sy) { 
		this.scale_x += sx;
		this.scale_y += sy;
	}
	public void addRotate(float angle) {
		this.rotate += angle;
	}
	public void addAlpha(float alpha) { 
		this.alpha += alpha;
	}
	public void mulScale(float sx, float sy) {
		this.scale_x *= sx;
		this.scale_y *= sy;
	}
	public void mulAlpha(float alpha) { 
		this.alpha *= alpha;
	}


}
