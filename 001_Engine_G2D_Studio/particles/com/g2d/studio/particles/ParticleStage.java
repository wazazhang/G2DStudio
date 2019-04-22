package com.g2d.studio.particles;

import java.awt.Color;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.cell.CIO;
import com.cell.gfx.game.CSprite;
import com.cell.rpg.particle.ParticleAppearanceType.DisplayNodeSprite;
import com.g2d.display.DisplayObjectContainer;
import com.g2d.display.Stage;
import com.g2d.display.particle.Layer;
import com.g2d.display.particle.ParticleDisplay;
import com.g2d.geom.Point;
import com.g2d.studio.gameedit.dynamic.util.EffectEditor;
import com.g2d.util.Drawing;
import com.g2d.util.Drawing.TextAnchor;
import com.g2d.util.Profiling;

public class ParticleStage extends Stage
{
	ParticleViewer 	viewer;
	EffectEditor 	cur_edit;
	ParticleDisplay	particle;
	
	Color			back_color				= Color.BLACK;
	boolean			is_show_cross 			= true;
	boolean			is_show_spawn_region	= false;
	boolean			is_show_spawn_bounds	= false;
	boolean			is_show_spawn_bounding	= true;
	
	Point			start_bounding_box		= null;
	com.g2d.geom.Rectangle bounding_box = new com.g2d.geom.Rectangle();
	
	public ParticleStage(EffectEditor cur_edit, ParticleViewer viewer) 
	{
		this.viewer = viewer;
		this.cur_edit = cur_edit;
		this.particle = new ParticleDisplay(cur_edit.getData().particles);
		if (particle.getData().bounding_box != null) {
			this.bounding_box = CIO.cloneObject(particle.getData().bounding_box);
		}
		this.addChild(particle);

	}
	
	public void added(DisplayObjectContainer parent) {
		particle.setLocation(getWidth()/2, getHeight()/2);
	}
	
	public void removed(DisplayObjectContainer parent) {
	}

	public void update() 
	{
		cur_edit.getData();
		
		if (getRoot().isKeyHold(KeyEvent.VK_CONTROL)) 
		{
			if (getRoot().isMouseDown(MouseEvent.BUTTON1)) 
			{
				start_bounding_box = new Point(
						particle.getMouseX(), 
						particle.getMouseY());
				bounding_box.setBounds(
						start_bounding_box.x,
						start_bounding_box.y,
						1, 1);
			}
			else if (getRoot().isMouseUp(MouseEvent.BUTTON1)) 
			{
				particle.getData().bounding_box = CIO.cloneObject(bounding_box);
				start_bounding_box = null;
			}
			else if (start_bounding_box != null)
			{
				bounding_box.setBounds(
						Math.min(particle.getMouseX(), start_bounding_box.x),
						Math.min(particle.getMouseY(), start_bounding_box.y), 
						Math.abs(particle.getMouseX()- start_bounding_box.x),
						Math.abs(particle.getMouseY()- start_bounding_box.y));
			}
			
		}
		else 
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
			
			Layer layer = viewer.getSelectedLayer();
			if (layer != null && (layer.appearance instanceof DisplayNodeSprite))
			{
				DisplayNodeSprite spr = (DisplayNodeSprite)layer.appearance;
				if (getRoot().isMouseWheelUP()) {
					spr.sprite_anim--;
				}
				if (getRoot().isMouseWheelDown()) {
					spr.sprite_anim++;
				}
			}
		}
		
		
	}

	public void render(com.g2d.Graphics2D g) 
	{
		g.setColor(back_color.getRGB());
		g.fill(local_bounds);
		
		if (is_show_cross)
		{
			g.setColor(com.g2d.Color.GRAY);
			g.drawLine(0, particle.getIntY(), getWidth(), particle.getIntY());
			g.drawLine(particle.getIntX(), 0, particle.getIntX(), getHeight());
		}
	}
	
	@Override
	protected void renderAfter(com.g2d.Graphics2D g)
	{
		Layer cur_layer = viewer.getSelectedLayer();
		String info = "渲染次数(" + Profiling.getAllNodeRenderTime(this)+") ";
		if (cur_layer != null)
		{
			try {
				info += "当前层：" + cur_layer;
				if (cur_layer.appearance instanceof DisplayNodeSprite) {
					DisplayNodeSprite spr = (DisplayNodeSprite) cur_layer.appearance;
					CSprite cspr = spr.getSprite();
					if (cspr != null) {
						info += " Anim=" + spr.sprite_anim + "/" + cspr.getAnimateCount();
						info += " FrameCount=" + cspr.getFrameCount(spr.sprite_anim);
					}
				}
			} catch (Exception e) {}
		}
		g.setColor(com.g2d.Color.WHITE);
		Drawing.drawStringBorder(g, info, 
				0, 0, TextAnchor.L_T);
		
		if (is_show_spawn_region)
		{
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
		
		if (is_show_spawn_bounds) 
		{
			g.setColor(com.g2d.Color.GREEN);
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
		
		if (is_show_spawn_bounding)
		{
			g.setColor(com.g2d.Color.YELLOW);
			g.drawRect(
					particle.getIntX() + bounding_box.x, 
					particle.getIntY() + bounding_box.y,
					bounding_box.width,
					bounding_box.height);
		}

		if (!getRoot().isKeyHold(KeyEvent.VK_CONTROL)) 
		{
			g.setColor(com.g2d.Color.YELLOW);
			Drawing.drawStringBorder(g, "按住CTRL编辑边界盒", 
					0, getHeight(), TextAnchor.L_B);
		} 
		else
		{
			g.setColor(com.g2d.Color.YELLOW);
			Drawing.drawStringBorder(g, "编辑边界盒", 
					0, getHeight(), TextAnchor.L_B);
		}
		
	
	}

	
}
