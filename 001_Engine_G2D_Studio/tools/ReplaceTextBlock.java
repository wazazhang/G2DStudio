import java.io.File;
import java.io.IOException;

import com.cell.io.CFile;
import com.cell.util.Args;
import com.cell.util.StringUtil;


public class ReplaceTextBlock 
{
	public static String usage()
	{
		String usg = "将文本文件一块替换成另一块\n" +
				"[src file]     -源文本文件\n" +
				"[head]         -源文件头标识\n" +
				"[tail]         -源文件尾标识\n" +
				"[replace file] -替换内容文件\n" +
				"";
		return usg;
	}
	

	public static void main(String[] args) throws IOException
	{
		if (args.length >= 4) 
		{
			replaceFile(new File(args[0]), args[1], args[2], new File(args[3]));
		} else {
			System.out.println(usage());
		}
	}
	
	static private void replaceFile(File srcText, String head, String tail, File replaceFile) throws IOException
	{
		String src_text = CFile.readText(srcText);
		String replace  = CFile.readText(replaceFile);
		
		String dst_text = StringUtil.replaceStringRange(
				src_text, 
				Args.argToJavaString(head), 
				Args.argToJavaString(tail),
				false, replace);
		
		CFile.writeText(srcText, dst_text);
	}

}
