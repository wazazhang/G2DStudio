import java.io.File;
import java.io.IOException;

import com.cell.io.CFile;


public class AppendText 
{
	public static String usage() {
		String usage = 
				"增加文本到结尾(无差别)\n" +
				"TextAppend\n" +
				"[file]	-目录\n" +
				"[text]	-文本文件\n";
		return usage;
	}
	
	public static void main(String[] args) throws IOException
	{
		if (args.length >= 2) 
		{
			File file = new File(args[0]).getCanonicalFile();
			System.out.println((args[1].trim()));
			CFile.appendText(file, args[1].trim());
		} else {
			System.out.println(usage());
		}
	}
	
	


}
