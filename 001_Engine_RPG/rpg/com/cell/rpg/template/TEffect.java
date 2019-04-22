package com.cell.rpg.template;

import com.cell.rpg.particle.ParticleAppearanceType.DisplayNodeImage;
import com.cell.rpg.particle.ParticleAppearanceType.DisplayNodeSprite;
import com.cell.rpg.particle.ParticleDisplayResource;
import com.g2d.display.particle.Layer;
import com.g2d.display.particle.ParticleData;


public class TEffect extends TemplateNode
{	
	final public ParticleData particles = new ParticleData();
	
	public TEffect(int id, String name) {
		super(id, name);
	}

	public void setResource(ParticleDisplayResource res) {
		for (Layer layer : particles) {
			if (layer.appearance instanceof DisplayNodeImage) {
				DisplayNodeImage dni = (DisplayNodeImage)layer.appearance;
				dni.setImage(res.getEffectImage(
						dni.cpj_project_name, 
						dni.cpj_sprite_name, 
						dni.cpj_image_id));
			}
			else if (layer.appearance instanceof DisplayNodeSprite) {
				DisplayNodeSprite dns = (DisplayNodeSprite)layer.appearance;
				dns.setSprite(res.getEffectSprite(
						dns.cpj_project_name, 
						dns.cpj_sprite_name));
			}
		}
	}
	
	@Override
	public Class<?>[] getSubAbilityTypes() {
		return new Class<?>[] {};
	}
	
}
