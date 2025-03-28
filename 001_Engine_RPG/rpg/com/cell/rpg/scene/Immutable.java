package com.cell.rpg.scene;

import com.cell.CUtil;
import com.cell.rpg.display.UnitNode;
import com.cell.rpg.instance.zones.ability.InstanceZoneUnitVisible;
import com.cell.rpg.scene.ability.ActorTransport;
import com.cell.rpg.scene.ability.ImmutableDisplay;

public class Immutable extends SceneSprite
{
	final UnitNode		display_node;
	
	public Immutable(String id, String cpj_name, String set_name) 
	{	
		super(id);
		display_node = new UnitNode(cpj_name, set_name);
	}

	public UnitNode getDisplayNode() {
		return display_node;
	}
	
	public Class<?>[] getSubAbilityTypes()
	{
		return static_abilities;
	}

	private static Class<?>[] static_abilities = new Class<?>[]{				
		ActorTransport.class,
		ImmutableDisplay.class,
		InstanceZoneUnitVisible.class,
	};
	
	static {
		SceneAbilityManager sam = SceneAbilityManager.getManager();
		if (sam != null) {
			Class<?>[] set = sam.getSceneImmutableExtAbilities();
			if (set != null) {
				static_abilities = CUtil.link(
						static_abilities, set);
			}
		}
	}
//	@Override
//	public Class<com.cell.rpg.scene.script.entity.Immutable> getTriggerObjectType() {
//		return com.cell.rpg.scene.script.entity.Immutable.class;
//	}
	
}
