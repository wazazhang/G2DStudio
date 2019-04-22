package com.cell.filesystem;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import com.cell.io.LittleIODeserialize;
import com.cell.io.LittleIOSerialize;

public class FileEntry implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public long 	hash;		// 文件名HASH
	public int		index;		// HASH对应所在Entry位置
	
	public String	key;		// 文件名
	public int	 	key_size;	// 文件名长度
	public String 	key_md5;	// 文件名MD5

	public int		f_start;	// 文件内容开始位置
	public int		f_size;		// 文件内容尺寸
	public long		f_date;		// 文件日期（1970-1-1起始秒） 
	public String	f_md5;		// 文件内容MD5
	
	public void save(OutputStream os) throws Exception 
	{
		LittleIOSerialize.putLong	(os, hash);
		LittleIOSerialize.putInt	(os, index);
		LittleIOSerialize.putString	(os, key, "UTF-8");
		LittleIOSerialize.putInt	(os, key_size);
		LittleIOSerialize.putString	(os, key_md5, "UTF-8");
		LittleIOSerialize.putInt	(os, f_start);
		LittleIOSerialize.putInt	(os, f_size);
		LittleIOSerialize.putLong	(os, f_date);
		LittleIOSerialize.putString	(os, f_md5, "UTF-8");
	}
	
	public void load(InputStream is) throws Exception 
	{
		hash 		= LittleIODeserialize.getLong	(is);
		index	 	= LittleIODeserialize.getInt	(is);
		key			= LittleIODeserialize.getString	(is, "UTF-8");
		key_size 	= LittleIODeserialize.getInt	(is);
		key_md5 	= LittleIODeserialize.getString	(is, "UTF-8");
		f_start 	= LittleIODeserialize.getInt	(is);
		f_size 		= LittleIODeserialize.getInt	(is);
		f_date 		= LittleIODeserialize.getLong	(is);
		f_md5 		= LittleIODeserialize.getString	(is, "UTF-8");
	}
}