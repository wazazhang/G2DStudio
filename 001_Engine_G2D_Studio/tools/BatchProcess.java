import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;

import com.cell.CIO;
import com.cell.CUtil;
import com.cell.io.CFile;
import com.cell.util.FileFilters;
import com.cell.util.FileMake;


public class BatchProcess 
{
	public static void batch(String command, File file, File workdir)
	{
		try {
			String ext = file.getName().substring(file.getName().lastIndexOf(".")+1);
			String pre = file.getName().substring(0, file.getName().lastIndexOf("."));
			command = CUtil.replaceString(command, "<path>", file.getPath());
			command = CUtil.replaceString(command, "<parent>", file.getParent());
			command = CUtil.replaceString(command, "<name>", file.getName());
			command = CUtil.replaceString(command, "<pre>", pre);
			command = CUtil.replaceString(command, "<ext>", ext);
			System.out.print(command);
			
			Process p = null;
			if (workdir != null) {
				p = Runtime.getRuntime().exec(command, CUtil.getEnv(), workdir);
			} else {
				p = Runtime.getRuntime().exec(command, CUtil.getEnv());
			}
//			p.getOutputStream().write("\n".getBytes());
			p.waitFor();
			System.out.println(" | exit code(" + p.exitValue() + ")");
//			byte[] data = CIO.readStream(p.getInputStream());
//			System.out.println(new String(data));
			String pout = CIO.readAllText(p.getInputStream(), "GBK");
			String perr = CIO.readAllText(p.getErrorStream(), "GBK");
			if (!pout.isEmpty()) {
				System.out.println(pout.trim());
			}
			if (!perr.isEmpty()) {
				System.err.println(perr.trim());
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String ... args) throws Exception
	{
		if (args.length >= 3)
		{
			File workdir = null;
			if (!args[0].isEmpty()) {
				workdir = new File(args[0]).getCanonicalFile();
			}
			FileFilters pattern = new FileFilters(CFile.readText(new File(args[1])));
			FileMake history = new FileMake(args[2]);
			String[] cmds = Arrays.copyOfRange(args, 3, args.length);
			for (int i=0; i<cmds.length; i++) {
				cmds[i] = "\"" + cmds[i] + "\"";
			}
			String cmd = CUtil.arrayToString(cmds, " ");

			ArrayList<File> files = CFile.listAllChildren(new File("."), pattern);
			files = history.getNewerList(files);
			for (File f : files) {
				 batch(cmd, f.getCanonicalFile(), workdir);
			}
		}
		else
		{
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
			"usage: <WorkingDir> <RegexFile> <FileMake:[dst_replace]> command ...\n" +
			"	[FileMake]\n" + FileMake.usage("\t") + "\n" +
			"	[RegexFile]\n" + FileFilters.usage("\t") + "\n" +
			"	Envrioment Var : <path>,<parent>,<name><pre><ext>";
		return usage;
	}

}
