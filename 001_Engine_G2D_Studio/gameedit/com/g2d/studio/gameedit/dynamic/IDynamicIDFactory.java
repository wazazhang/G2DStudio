package com.g2d.studio.gameedit.dynamic;

import com.cell.rpg.RPGFactory;
import com.cell.util.IDFactoryInteger;

public interface IDynamicIDFactory<T extends DynamicNode<?>> extends RPGFactory
{
//	public Integer createID() ;
//	
//	public boolean resetID(DynamicNode<?> obj, int newID);
//
//	public boolean containsID(int newID);
//	
//	public Integer peekID();
	
	public IDFactoryInteger<T> idf();
}
