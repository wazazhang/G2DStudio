

import java.io.File;

import com.cell.io.CFile;
import com.cell.util.FileFilters;

public class CopyResource
{
	public static String usage()
	{
		String usage = "复制文件\n" +
				"[src]         -源目录\n" +
				"[dst]         -目标目录\n" +
				"<file filter> -文件包含排除";
		return usage;
	}
	
	public static void main(String[] args)
	{
		try {
			if (args.length >= 3) {
				File src = new File(args[0]).getCanonicalFile();
				File dst = new File(args[1]).getCanonicalFile();
				File ft = new File(args[2]);
				String regex = args[2];
				if (ft.isFile()) {
					regex = CFile.readText(ft.getCanonicalFile());
				}
				FileFilters filter = new FileFilters(regex);
				copy(src, dst, filter);
			} else {
				System.out.println(usage());
				System.out.println(FileFilters.usage("               "));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void copy(File src, File dst, FileFilters filter) throws Exception
	{
		if (src.isDirectory()) {
			for (File sf : src.listFiles()) {
				File df = new File(dst, sf.getName());
				copy(sf, df, filter);
			}
		} else {
			if (filter.acceptFile(src)) {
				CFile.copyFile(src, dst);
				System.out.println("copy " + src.getCanonicalPath() + " - " + dst.getCanonicalPath());
			}
		}
	}
	
}
