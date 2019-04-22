package com.cell.rpg.template.ability;

import java.io.ObjectStreamException;

import com.cell.rpg.ability.AbstractAbility;
import com.g2d.annotation.Property;


@Property("[单位能力] 单位掉落物品")
public class UnitItemDrop extends AbstractAbility 
{
	private static final long serialVersionUID = 1L;

	/** 掉落道具列表ID */
	@Property("掉落道具列表ID")
	public int item_list_id = -1;
	
	@Property("列表生效的概率,[0,10000)的万分比整数概率")
	public int probability = 10000;
	
	@Property("最少数量")
	public int min_count = 1;

	@Property("最大数量")
	public int max_count = 1;

	@Override
	final public boolean isMultiField() 
	{
		return true;
	}
	
	@Override
	protected Object readResolve() throws ObjectStreamException {
		min_count = Math.max(min_count, 1);
		max_count = Math.max(max_count, 1);
		return super.readResolve();
	}
}


