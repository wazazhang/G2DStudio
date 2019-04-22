import java.io.File;

import com.cell.CUtil;
import com.cell.io.CFile;
import com.cell.util.Properties;


public class MakeBeginEnd 
{
	public static void main(String args[])
	{
		try {
			File input = new File(args[0]);
			String prefix = args[1];
			String suffix = args[2];
			File output = input;
			boolean appendline = true;

			Properties pp = new Properties(CUtil.arrayToString(args, "\n"), ":");
			String _o = pp.get("-o");
			String _n = pp.get("-n");
			if (_o != null) {
				output = new File(_o);
			}
			if (_n != null) {
				appendline = Boolean.parseBoolean(_n);
			}
			
			String text = CFile.readText(input);
			if (appendline) {
				text = prefix + "\n" + text + "\n" + suffix;
			} else {
				text = prefix + text + suffix;
			}
			CFile.writeText(output, text);
		} catch (Exception e) {
			e.printStackTrace();
			printUsage();
		}
	}
	
	public static void printUsage()
	{
		System.out.println(usage());
	}
	
	public static String usage()
	{
		String usage = 
			"将原始文件加开头和结尾标识\n" +
			"MakeBeginEnd src prefix suffix [-o:outfile] [-n:appendline]\n";
		return usage;
	}


}
