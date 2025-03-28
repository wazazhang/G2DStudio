package com.g2d.display.particle.affects;

import com.cell.math.MathVector;
import com.g2d.annotation.Property;
import com.g2d.display.particle.ParticleAffect;
import com.g2d.display.particle.ParticleAffectNode;


@Property("漩涡")
public class Vortex implements ParticleAffect {

	private static final long serialVersionUID = 1L;
	
	@Property("角度(0~360)")
	public float angle = 90f;
	
	@Property("力量")
	public float force = 1f;
	
	@Override
	public void update(float timelinePosition, ParticleAffectNode particle) {
		double degree = MathVector.getDegree(particle.getSpeed());
		MathVector.movePolar(particle.getSpeed(), degree + Math.toRadians(angle), force);
	}
	
	@Override
	public String toString() {
		return "[漩涡][角度="+angle+"][力量="+force+"]";
	}
}
