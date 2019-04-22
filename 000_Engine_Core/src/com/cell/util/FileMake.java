package com.cell.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.cell.CUtil;
import com.cell.io.CFile;

public class FileMake
{	
	ArrayList<Pair<String, String>> replace = new ArrayList<Pair<String, String>>();
	
	public FileMake(String arg) 
	{
		String[] rp = CUtil.splitString(arg, "|");
		for (String r : rp) {
			String kv[] = CUtil.splitString(r, ">");
			replace.add(new Pair<String, String>(kv[0].trim(), kv[1].trim()));
		}
	}

	public static String usage(String line_prefix)
	{
		return 
				line_prefix + "History Log file\n" +
				line_prefix + "eg: .cpp>.o|.c>.o";
	}
	
	/** 
	 * dst file is newer than history. 
	 * @param dst_regex 
	 * @return if (return > 0) means dst file is newer than src.
	 */
	public boolean isNewer(File dst) {
		try {
			if (!replace.isEmpty()) {
				File dir = dst.getParentFile();
				for (Pair<String, String> p : replace) {
					String new_name = CUtil.replaceString(
							dst.getName(),
							p.getKey(), 
							p.getValue());
					File new_file = new File(dir, new_name);
					if (!new_file.exists() || new_file.lastModified() < dst.lastModified()) {
						return true;
					}
				}
				return false;
			}
		} catch (Exception e) {}
		return true;
	}
	
	public ArrayList<File> getNewerList(File[] files) {
		ArrayList<File> ret = new ArrayList<File>();
		for (File file : files) {
			if (isNewer(file)) {
				ret.add(file);
			}
		}
		return ret;
	}
	
	public ArrayList<File> getNewerList(Collection<File> files) {
		return getNewerList(files.toArray(new File[files.size()]));
	}
	
}
