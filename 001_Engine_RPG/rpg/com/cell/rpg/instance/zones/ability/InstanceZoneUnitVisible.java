package com.cell.rpg.instance.zones.ability;

import com.cell.rpg.ability.AbstractAbility;
import com.cell.rpg.struct.InstanceZoneScriptCode;
import com.g2d.annotation.Property;


@Property("[副本单位能力] 单位的显示条件")
public class InstanceZoneUnitVisible extends AbstractAbility 
{
	private static final long serialVersionUID = 1L;

	/** 掉落道具列表ID */
	@Property("脚本")
	public InstanceZoneScriptCode		dst_value		= new InstanceZoneScriptCode();

	@Override
	final public boolean isMultiField() {
		return false;
	}
}
