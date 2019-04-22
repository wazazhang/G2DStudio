package com.cell.util;

import com.cell.CUtil;

public class Args {

	public static String argToJavaString(String text)
	{
		text = CUtil.replaceString(text, "\\\\", "\\");
		text = CUtil.replaceString(text, "\\n", "\n");
		text = CUtil.replaceString(text, "\\t", "\t");
		return text;
	}
}
