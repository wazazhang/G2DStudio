import java.io.File;
import java.io.IOException;

import com.cell.io.CFile;
import com.cell.util.Args;


public class TextAppend 
{
	public static String usage() {
		String usage = 
				"增加文本到结尾(替换符号)\n" +
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
			System.out.println(Args.argToJavaString(args[1].trim()));
			CFile.appendText(file, Args.argToJavaString(args[1].trim()));
		} else {
			System.out.println(usage());
		}
	}
	
	


}
