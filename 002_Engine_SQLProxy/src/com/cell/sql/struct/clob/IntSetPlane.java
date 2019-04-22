package com.cell.sql.struct.clob;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashSet;

import com.cell.reflect.Parser;
import com.cell.sql.SQLStructCLOB;
/**
 * 适合表示短数组
 * @author WAZA
 *
 */
public class IntSetPlane extends LinkedHashSet<Integer> implements SQLStructCLOB
{
	private static final long serialVersionUID = 1L;

	public IntSetPlane() {}
	
	public IntSetPlane(int len) {
		super(len);
	}
	
	public IntSetPlane(Collection<Integer> another) {
		super(another);
	}

	@Override
	public void decode(String text) throws IOException {
		String[] ds = text.split(",");
		for (String d : ds) {
			add(Parser.stringToObject(d, Integer.class));
		}
	}
	
	@Override
	public String encode() throws IOException {
		StringBuilder sb = new StringBuilder();
		for (Integer b : this) {
			sb.append(b+",");
		}
		return sb.toString();
	}
	
}
