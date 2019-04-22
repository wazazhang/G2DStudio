package com.cell.rpg.scene;

import com.cell.CUtil;
import com.cell.rpg.instance.zones.ability.InstanceZoneUnitVisible;

public class Effect extends SceneUnit
{
	final public int	template_effect_id;
	
	public Effect(String id, int template_effect_id) 
	{	
		super(id);
		this.template_effect_id = template_effect_id;
	}

	public Class<?>[] getSubAbilityTypes()
	{
		return static_abilities;
	}

	private static Class<?>[] static_abilities = new Class<?>[] {
		InstanceZoneUnitVisible.class,
		};
	
	static {
		SceneAbilityManager sam = SceneAbilityManager.getManager();
		if (sam != null) {
			Class<?>[] set = sam.getSceneEffectExtAbilities();
			if (set != null) {
				static_abilities = CUtil.link(
						static_abilities, set);
			}
		}
	}
	
//	
//	@Override
//	public Class<? extends com.cell.rpg.scene.script.entity.SceneUnit> getTriggerObjectType() {
//		return null;
//	}

}
