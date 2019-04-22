package com.cell.rpg.particle;

import com.g2d.Graphics2D;

public enum ParticleBlend {

	NORMAL,
	SCREEN,;
	
	public void setBlend(Graphics2D g) {
		switch(this) {
		case NORMAL:
			g.setBlendMode(0);
			break;
		case SCREEN:
			g.setBlendMode(3);
			break;
		}
	}
}
