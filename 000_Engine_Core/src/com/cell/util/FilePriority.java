package com.cell.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import com.cell.CUtil;

public class FilePriority 
{
	ArrayList<ArrayList<String>> extens = new ArrayList<ArrayList<String>>();
	
	/**
	 * 比如优先打包PVR，否则KTX,否则PNG
	 * pvr>ktx>png;pvr>ktx>jpg
	 * @param arg
	 */
	public FilePriority(String arg) 
	{
		String[] lines = CUtil.splitString(arg, ";");
		for (String line : lines) {
			String[] kv = CUtil.splitString(line, ">");
			if (kv.length>1) {
				ArrayList<String> e = new ArrayList<String>(kv.length);
				e.addAll(Arrays.asList(kv));
				extens.add(e);
			}
		}
	}
	
	public ArrayList<File> replace(ArrayList<File> files) {
//		ArrayList<File> ret = new ArrayList<File>(files.size());
		TreeMap<String, File> ret = new TreeMap<String, File>();
		for (File s : files) {
			File d = accept(s);
			ret.put(d.getPath(), d);
		}
		return new ArrayList<File>(ret.values());
	}
	
	public File accept(File file) {
		File dir = file.getParentFile();
		String fn = file.getName();
		for (ArrayList<String> e : extens) {
			String last = e.get(e.size()-1);
			if (fn.endsWith(last)) {
				for (int i=0; i<e.size()-1; i++) {
					String d = e.get(i);
					String replace = fn.substring(0, fn.length() - last.length());
					replace += d;
					File df = new File(dir, replace);
					if (df.exists()) {
						return df;
					}
				}
			}
		}
		return file;
	}
	
	public static String usage(String line_prefix)
	{
		return 
			line_prefix + "文件优选：\n" +
			line_prefix + "	比如优先打包PVR，否则KTX，否则PNG or JPG\n" +
			line_prefix + "	pvr>ktx>png;pvr>ktx>jpg\n";
	}
	
}
