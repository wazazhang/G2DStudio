package com.g2d.display.particle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import com.cell.CUtil;
import com.cell.math.MathVector;
import com.cell.math.TVector;
import com.cell.math.Vector;
import com.cell.rpg.particle.ParticleAppearanceType.DisplayNodeSprite;
import com.g2d.Color;
import com.g2d.Graphics2D;
import com.g2d.display.DisplayObject;
import com.g2d.display.DisplayObjectContainer;
import com.g2d.display.DisplayObjectLeaf;
import com.g2d.display.particle.Layer.TimeNode;
import com.g2d.geom.AffineTransform;
import com.g2d.geom.Rectangle;
import com.g2d.geom.Shape;


public class ParticleDisplay extends com.g2d.display.particle.ParticleSystem
{
	public static Random 			random 			= new Random();
	
	private ParticleData 			data;
	
	private ArrayList<LayerObject> 	layers;

	private DisplayObjectContainer	particle_scene;
	
	public ParticleDisplay() {}
	
	public void setAnimateFrame(boolean sync, int anim, int frame) {
		if (layers != null) {
			for (LayerObject layer : layers) {
				layer.vanim = anim;
				layer.vframe = frame;
				layer.sync_vanim = sync;
			}
		}
	}
	
	public ParticleDisplay(ParticleData data) {
		setData(data);
	}
	
	public ParticleData getData() {
		return data;
	}
	
	public void setData(ParticleData data) {
		this.data = data;
		this.layers = new ArrayList<LayerObject>(data.size());
		for (Layer layer : data) {
			layers.add(new LayerObject(layer, this));
		}
		if (data.bounding_box != null) {
			this.local_bounds = data.bounding_box;
		} else {
			this.local_bounds.setBounds(getOriginBounds(data));
		}
	}
	
	public void setSpawnTarget(DisplayObjectContainer dst) {
		this.particle_scene = dst;
	}
	
	public void spawn() {
		if (layers != null) {
			for (LayerObject layer : layers) {
				layer.spawn();
			}
		}
	}

	@Override
	public void update() {
		if (data.bounding_box != null) {
			this.local_bounds = data.bounding_box;
		}	
	}
	
	public void render(Graphics2D g) {
		if (layers != null) {
			for (LayerObject layer : layers) {
				layer.update();
			}
		}
	}
	
	@Override
	protected void renderChilds(Graphics2D g) {
		super.renderChilds(g);
	}
	
	/**
	 * 将粒子节点放到父节点里显示
	 * @param parent
	 * @param node
	 */
	protected boolean addParticleNode(SingleObject node, Layer layer) {
		if (layer.is_local_coordinate) {
			node.x = 0;
			node.y = 0;
			this.addChild(node);
		} else {
			if (particle_scene != null) {
				node.x = this.convertCoordinateX(particle_scene, 0);
				node.y = this.convertCoordinateY(particle_scene, 0);
				particle_scene.addChild(node);
			}
			else if (getParent() != null) {
				node.x = this.x;
				node.y = this.y;
				getParent().addChild(node);
			}
		}
		return true;
	}
	
	protected SingleObject createParticleNode(LayerObject layer)
	{
		return new SingleObject(layer);
	}
	
//	------------------------------------------------------------------------------------------------------------------------
	/**
	 * Layer Display Object
	 */
	public static class LayerObject
	{
		final Queue<SingleObject>	idle_nodes = new LinkedList<SingleObject>();
		final ParticleDisplay 		display;
		final Layer 				layer;

		private boolean sync_vanim = false;
		private int vanim;
		private int vframe;
		private int vtimer = 0;
		
		public LayerObject(Layer layer, ParticleDisplay display) {
			this.layer = layer;
			this.display = display;
			for (int i = 0; i < layer.particles_capacity; i++) {
				SingleObject node = new SingleObject(this);
				idle_nodes.add(node);
			}
		}
		
		private void spawnAdd() {
			for (int i = 0; i < layer.particles_per_frame; i++) {
				SingleObject node = idle_nodes.poll();
				if (node != null) {
					node.begin();
					// spawn origin
					Vector origin_pos	= getOriginPosition(layer);
					// spawn speed direction acc
					Vector spawn_speed	= getSpawnSpeed(layer, origin_pos);
					Vector spawn_acc	= new TVector(0, 0);
					MathVector.moveTo(spawn_acc, 
							spawn_speed.getVectorX(), 
							spawn_speed.getVectorY(), 
							layer.spawn_acc + CUtil.getRandom(random, -layer.spawn_acc_range, layer.spawn_acc_range));
					
					// node
					node.age_time		= CUtil.getRandom(random, layer.particle_min_age, layer.particle_max_age);
					node.timer			= 0;
					
					node.speed			.setVectorX(spawn_speed.getVectorX());
					node.speed			.setVectorY(spawn_speed.getVectorY());
					node.acc			.setVectorX(spawn_acc.getVectorX());
					node.acc			.setVectorY(spawn_acc.getVectorY());
					
					node.damp			= layer.spawn_damp + CUtil.getRandom(random, -layer.spawn_damp_range, layer.spawn_damp_range);
					node.priority		= display.priority;
					node.pos.x			= origin_pos.getVectorX();
					node.pos.y			= origin_pos.getVectorY();
					if (display.addParticleNode(node, layer)){
					} else {
						idle_nodes.add(node);
					}
				} else {
					break;
				}
			}
		}
		
		public void spawn() {
			if (!layer.particles_continued) {
				spawnAdd();
			}
		}
		
		public void update() 
		{
			int timer = this.vtimer;
			this.vtimer ++;
			if (layer.particles_delay_frame>0) {
				if (timer%layer.particles_delay_frame!=0) {
					return;
				}
			}
			if (layer.particles_continued) {
				spawnAdd();
			}
		}
		
	}
	
//	-----------------------------------------------------------------------------------------------------------------------
//	Particle 
//	-----------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Single Particle
	 */
	static public class SingleObject extends DisplayObjectLeaf implements ParticleAffectNode
	{
		public float main_scale = 1;
		
		final LayerObject layer;
		
		/** 生命周期 */
		double			age_time;		

		// time line
		Color	tl_color		= Color.WHITE;
		float	tl_alpha		= 1;
		float	tl_size_x		= 1;
		float	tl_size_y		= 1;
		float	tl_spin			= 0;
		float	tl_end_size_x	= 1;
		float	tl_end_size_y	= 1;
		float	tl_end_spin		= 0;
		
		Vector	speed 			= new TVector(0, 0);
		Vector	acc				= new TVector(0, 0);
		float	damp			= 0f;
		
		ParticleAppearance appearance;
		
		public TVector pos = new TVector();
		
		public SingleObject(LayerObject layer) {
			this.layer = layer;
		}
		
//		public float 	getAlpha() {return this.tl_alpha;}
//		public float 	getSize() {return this.tl_size;}
//		public float 	getSpin() {return this.tl_spin;}
		public Vector 	getSpeed() {return this.speed;}
		public Vector	getAcc() {return acc;}
		public float 	getDamp() {return damp;}
		
		@Override
		protected boolean testCatchMouse(Graphics2D g) {
			return false;
		}
		
		@Override
		public void removed(DisplayObjectContainer parent) {
			layer.idle_nodes.add(this);
		}
		@Override
		public void added(DisplayObjectContainer parent) {}
		
		@Override
		public void update() 
		{
		}
		@Override
		public void onRender(Graphics2D g)
		{
			MathVector.move(pos, 
					speed.getVectorX(), 
					speed.getVectorY());
			MathVector.move(speed, acc.getVectorX(), acc.getVectorY());
			MathVector.scale(speed, damp);

			float timeline_position = (float)(timer / age_time);
			
			updateTimeLine(timeline_position);
			
			updateAffects(timeline_position);
			
			super.onRender(g);
			
			if (timer >= age_time) 
			{
				this.removeFromParent();
			}
		}
		@Override
		public void render(Graphics2D g) 
		{
			g.pushBlendMode();
			g.pushTransform();
			try {
				setAlpha(g, tl_alpha);
				g.translate(pos.x, pos.y);
				g.scale(main_scale, main_scale);
				g.scale(tl_size_x, tl_size_y);
				g.rotate(tl_spin);
				// g.scale(tl_end_size_x, tl_end_size_y);
				// g.rotate(tl_end_spin);
				try {
					if (appearance != null) {
						appearance.render(g, layer.layer);
					} else {
						g.setColor(tl_color);
						g.drawArc(-2, -2, 4, 4, 0, 360);
					}
				} catch (Exception e) {
					g.setColor(Color.RED);
					g.drawString(e+"", 0, 0);
				}
			} finally {
				g.popTransform();
				g.popBlendMode();
			}
			increaseFrameRenderTime(1);
		}
		
		private void begin()
		{
			if (layer.layer.appearance != null) {
				// sync and bind
				this.appearance = layer.layer.appearance.cloneDisplay();
				if (this.appearance instanceof DisplayNodeSprite) {
					DisplayNodeSprite spr = (DisplayNodeSprite)this.appearance;
					if (layer.sync_vanim) {
						spr.sprite_anim = layer.vanim;
						spr.sprite_frame = layer.vframe;
					} else {
						spr.sprite_frame = layer.vtimer;
					}
				}
			}
			tl_alpha = 1;
			tl_size_x = 1;
			tl_size_y = 1;
			tl_spin = 0;
			tl_end_size_x = 1;
			tl_end_size_y = 1;
			tl_end_spin = 0;
			pos.x = 0;
			pos.y = 0;
		}
		private void updateTimeLine(float timeline_position)
		{
			TimeNode start = null;
			for (TimeNode tn : layer.layer.timeline) {
				if (tn.enable_size) {
					if (timeline_position >= tn.getPosition()) {
						start = tn;
						tl_size_x = start.scale_x;
						tl_size_y = start.scale_y;
					} else if (start != null) {
						float tnpos = timeline_position - start.getPosition();
						float tnlen = tn.getPosition() - start.getPosition();
						float tnval = tnpos / tnlen;
						tl_size_x	+= (tn.scale_x - start.scale_x) * tnval;
						tl_size_y	+= (tn.scale_y - start.scale_y) * tnval;
						break;
					}
				}
			}
			start = null;
			for (TimeNode tn : layer.layer.timeline) {
				if (tn.end_size) {
					if (timeline_position >= tn.getPosition()) {
						start = tn;
						tl_end_size_x = start.scale_end_x;
						tl_end_size_y = start.scale_end_y;
					} else if (start != null) {
						float tnpos = timeline_position - start.getPosition();
						float tnlen = tn.getPosition() - start.getPosition();
						float tnval = tnpos / tnlen;
						tl_end_size_x	+= (tn.scale_end_x - start.scale_end_x) * tnval;
						tl_end_size_y	+= (tn.scale_end_y - start.scale_end_y) * tnval;
						break;
					}
				}
			}
			
			start = null;
			for (TimeNode tn : layer.layer.timeline) {
				if (tn.enable_spin) {
					if (timeline_position >= tn.getPosition()) {
						start = tn;
						tl_spin = start.spin;
					} else if (start != null) {
						float tnpos = timeline_position - start.getPosition();
						float tnlen = tn.getPosition() - start.getPosition();
						float tnval = tnpos / tnlen;
						tl_spin += (tn.spin - start.spin) * tnval;
						break;
					}
				}
			}
			
			start = null;
			for (TimeNode tn : layer.layer.timeline) {
				if (tn.end_spin) {
					if (timeline_position >= tn.getPosition()) {
						start = tn;
						tl_end_spin = start.spin_end;
					} else if (start != null) {
						float tnpos = timeline_position - start.getPosition();
						float tnlen = tn.getPosition() - start.getPosition();
						float tnval = tnpos / tnlen;
						tl_end_spin += (tn.spin_end - start.spin_end) * tnval;
						break;
					}
				}
			}
			
			start = null;
			for (TimeNode tn : layer.layer.timeline) {
				if (tn.enable_alpha) {
					if (timeline_position >= tn.getPosition()) {
						start = tn;
						tl_alpha = start.alpha;
					} else if (start != null) {
						float tnpos = timeline_position - start.getPosition();
						float tnlen = tn.getPosition() - start.getPosition();
						float tnval = tnpos / tnlen;
						tl_alpha += (tn.alpha - start.alpha) * tnval;
						break;
					}
				}
			}

		}
		
		private void updateAffects(float timeline_position)
		{
			for (ParticleAffect affect : layer.layer.affects) {
				affect.update(timeline_position, this);
			}
		}
		
	}

//	------------------------------------------------------------------------------------------------------------------------
	
	public static Rectangle getOriginBounds(ParticleData data) {
		int x1=0, y1=0, x2=0, y2=0;
		for (Layer layer : data) {
			Rectangle shape = getOriginShape(layer).getBounds();
			x1 = Math.min(x1, shape.x);
			y1 = Math.min(y1, shape.y);
			x2 = Math.max(x2, shape.x+shape.width);
			y2 = Math.max(y2, shape.y+shape.height);
		}
		return new Rectangle(x1, y1, x2-x1, y2-y1);
	}
	
	public static Shape getOriginShape(Layer layer)
	{
		// spawn shape
		Shape shape = layer.origin_shape.getShape();
				
		shape = AffineTransform.getScaleInstance(layer.origin_scale_x, layer.origin_scale_y)
				.createTransformedShape(shape);
		shape = AffineTransform.getRotateInstance(layer.origin_rotation_angle)
				.createTransformedShape(shape);
		shape = AffineTransform.getTranslateInstance(layer.origin_x, layer.origin_y)
				.createTransformedShape(shape);
		
		return shape;
	}
	
	public static Vector getOriginPosition(Layer layer)
	{
		// spawn origin
		Vector pos = layer.origin_shape.getPosition(random);
		MathVector.scale(pos, layer.origin_scale_x, layer.origin_scale_y);
		MathVector.rotate(pos, layer.origin_rotation_angle);
		MathVector.move(pos, layer.origin_x, layer.origin_y);
		return pos;
	}
	
	public static Vector getSpawnSpeed(Layer layer, Vector origin_pos)
	{
		Vector speed	= new TVector();
		if (!layer.spawn_orgin_angle) {
			MathVector.movePolar(speed, 
					layer.spawn_angle    + CUtil.getRandom(random, -layer.spawn_angle_range,    layer.spawn_angle_range), 
					layer.spawn_velocity + CUtil.getRandom(random, -layer.spawn_velocity_range, layer.spawn_velocity_range));
		} else {
			double degree = MathVector.getDegree(origin_pos.getVectorX(), origin_pos.getVectorY());
			MathVector.movePolar(speed, 
					degree, 
					layer.spawn_velocity + CUtil.getRandom(random, -layer.spawn_velocity_range, layer.spawn_velocity_range));
		}
		return speed;
	}
	
//	------------------------------------------------------------------------------------------------------------------------
	
}
