package com.cell.gameedit.output;


import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.cell.CUtil;
import com.cell.gameedit.OutputLoader;
import com.cell.gameedit.object.ImagesSet;
import com.cell.gameedit.object.MapSet;
import com.cell.gameedit.object.SpriteSet;
import com.cell.gameedit.object.TableSet;
import com.cell.gameedit.object.WorldSet;
import com.cell.io.TextDeserialize;


/**
 * 如何将编辑器资源解析成单位
 * @author WAZA
 */
abstract public class BaseOutput implements OutputLoader
{
	final LinkedHashMap<String, ImagesSet> ImgTable = new LinkedHashMap<String, ImagesSet>();
	final LinkedHashMap<String, SpriteSet> SprTable = new LinkedHashMap<String, SpriteSet>();
	final LinkedHashMap<String, MapSet> MapTable = new LinkedHashMap<String, MapSet>();
	final LinkedHashMap<String, WorldSet> WorldTable = new LinkedHashMap<String, WorldSet>();
	final LinkedHashMap<String, TableSet> TableGroups = new LinkedHashMap<String, TableSet>();
	
	public boolean isEmpty() {
		return ImgTable.isEmpty() && SprTable.isEmpty() && MapTable.isEmpty() && WorldTable.isEmpty() && TableGroups.isEmpty();
	}
	
	@Override
	final public Map<String, ImagesSet> getImgTable() {
		return ImgTable;
	}
	
	@Override
	final public Map<String, MapSet> getMapTable() {
		return MapTable;
	}
	
	@Override
	final public Map<String,SpriteSet> getSprTable() {
		return SprTable;
	}
	
	@Override
	final public Map<String, TableSet> getTableGroups() {
		return TableGroups;
	}
	
	@Override
	final public Map<String, WorldSet> getWorldTable() {
		return WorldTable;
	}
	
//	------------------------------------------------------------------------------------------------

	/**
	 * input "{1234},{5678}"
	 * return [1234][5678]
	 */
	final protected static String[] getArray2D(String text)
	{
		text = text.replace('{', ' ');
		String[] texts = CUtil.splitString(text, "},");
		for (int i=texts.length-1; i>=0; --i) {
			texts[i] = texts[i].trim();
		}
		return texts;
	}
	
	/**
	 * input 3,123,4,5678
	 * return [123] [5678]
	 * @param text
	 * @return
	 */
	final protected static String[] getArray1D(String text)
	{
		StringReader reader = new StringReader(text);
		ArrayList<String> list = new ArrayList<String>();
		try{
			while(true){
				String line = TextDeserialize.getString(reader);
				list.add(line);
			}
		}catch (Exception e) {}
		return list.toArray(new String[list.size()]);
	}
	
	/**
	 * input 3,123,4,5678
	 * return [123] [5678]
	 * @param text
	 * @return
	 */
	public static String getArray1DLines(String text)
	{
		StringReader reader = new StringReader(text);
		StringBuilder ret = new StringBuilder();
		try{
			while(true){
				String line = TextDeserialize.getString(reader);
				ret.append(line+"\n");
			}
		}catch (Exception e) {}
		return ret.toString();
	}
	
//	------------------------------------------------------------------------------------------------



}



