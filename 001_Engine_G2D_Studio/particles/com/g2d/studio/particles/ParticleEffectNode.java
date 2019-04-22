package com.g2d.studio.particles;

import java.awt.Color;
import java.awt.event.MouseEvent;

import com.cell.rpg.template.TEffect;
import com.g2d.display.DisplayObjectContainer;
import com.g2d.display.Sprite;
import com.g2d.display.particle.Layer;
import com.g2d.display.particle.ParticleDisplay;

public class ParticleEffectNode extends Sprite
{
	TEffect		 	data;
	ParticleDisplay	particle;
	
	Color			back_color				= Color.BLACK;
	boolean			is_show_cross 			= false;
	boolean			is_show_spawn_region	= false;
	boolean			is_show_spawn_bounds	= false;
	
	public ParticleEffectNode(TEffect data) 
	{
		this.data = data;
		this.particle = new ParticleDisplay(data.particles);
		this.addChild(particle);
	}
	
	public void added(DisplayObjectContainer parent) {
		particle.setLocation(getWidth()/2, getHeight()/2);
	}
	
	public void removed(DisplayObjectContainer parent) {
	}

	public void update() 
	{
		if (getRoot().isMouseHold(MouseEvent.BUTTON1)) {
			particle.setLocation(getMouseX(), getMouseY());
			particle.spawn();
		}
		if (getRoot().isMouseDown(MouseEvent.BUTTON3)) {
			particle.setLocation(getMouseX(), getMouseY());
			particle.spawn();
		}
		if (getRoot().isMouseHold(MouseEvent.BUTTON3)) {
			particle.setLocation(getMouseX(), getMouseY());
		}
	}

	public void render(com.g2d.Graphics2D g) 
	{
		g.setColor(back_color.getRGB());
		g.fill(local_bounds);
		
		if (is_show_cross) {
			g.setColor(com.g2d.Color.GRAY);
			g.drawLine(0, particle.getIntY(), getWidth(), particle.getIntY());
			g.drawLine(particle.getIntX(), 0, particle.getIntX(), getHeight());
		}
	}
	
	@Override
	protected void renderAfter(com.g2d.Graphics2D g)
	{
		if (is_show_spawn_region) {
			g.setColor(com.g2d.Color.WHITE);
			for (Layer layer : particle.getData()) {
				com.g2d.geom.Shape shape = ParticleDisplay.getOriginShape(layer);
				double tx = particle.x;
				double ty = particle.y;
				try{
					g.translate(tx, ty);
					g.draw(shape);
				}finally{
					g.translate(-tx, -ty);
				}
			}
		}
		
		if (is_show_spawn_bounds) {
			g.setColor(com.g2d.Color.YELLOW);
			com.g2d.geom.Rectangle rect = ParticleDisplay.getOriginBounds(particle.getData());
			double tx = particle.x;
			double ty = particle.y;
			try{
				g.translate(tx, ty);
				g.draw(rect);
			}finally{
				g.translate(-tx, -ty);
			}
		}
	}

	
}

