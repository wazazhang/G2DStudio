package com.cell.filesystem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.cell.CUtil;
import com.cell.filesystem.FileSystemReader.EntryData;
import com.cell.io.CFile;
import com.cell.util.FileFilters;
import com.cell.util.FilePriority;

public class FileSystem 
{
	final static public byte[] FS_HEAD_START 	= new byte[]{'M', 'F', 'F', 'S'};
	final static public byte[] FS_ENTRY_START 	= new byte[]{'M', 'F', 'E', 'T'};
	final static public byte[] FS_TRUNK_START 	= new byte[]{'M', 'F', 'T', 'K'};
	final static public byte[] FS_END 			= new byte[]{'M', 'F', 'E', 'D'};

	final static public byte[] VERSION 			= new byte[]{'0', '0', '0', '1'};
	
	protected Hash hash_function = new MurmurHash();
	
	public static String formatName(String name) {
		return CUtil.replaceString(name, "\\", "/");
	}
	
	/**
	 * 将目录中的文件打包
	 * @param pattern
	 * @param input_dir
	 * @param output_file
	 * @throws Exception
	 */
	public static void packFiles(
			FileFilters pattern, 
			FilePriority replacer,
			File input_dir, 
			File output_file, 
			String extName) throws Exception
	{
		createUpdate(pattern, replacer, input_dir, output_file, null, extName);
	}

	public static Map<String, FileEntry> listAllEntrys(
			File old_packs_dir,
			String mpqExt)
	{
		// 获取过往文件列表
		HashMap<String, FileEntry> old_pack_entrys = new HashMap<String, FileEntry>();
		if (old_packs_dir != null && old_packs_dir.exists()) {
			for (File file : old_packs_dir.listFiles()) {
				try {
					if (file.isFile() && file.getName().endsWith(mpqExt)) {
						System.out.println("---------------------------------------------------------------------------------------------");
						System.out.println("check old update file : " + file + " : BEGIN");
						System.out.println("---------------------------------------------------------------------------------------------");
						FileSystemReader fsr = new FileSystemReader(file);
						ArrayList<FileEntry> entrys = fsr.getEntrys();
						Collections.sort(entrys, new Comparator<FileEntry>() {
							@Override
							public int compare(FileEntry o1, FileEntry o2) {
								return CUtil.stringCompare(o1.key, o2.key);
							}
						});
						for (FileEntry e : fsr.getEntrys()) {
							FileEntry ooe = old_pack_entrys.get(e.key);
							if (ooe!=null) {
								if (ooe.f_date < e.f_date) {
									old_pack_entrys.put(e.key, e);
									System.out.println("\tUPDATE NEW" + 
											" : O=" + CUtil.timeYYMMDDHHMMSS(ooe.f_date*1000) +
											" < N=" + CUtil.timeYYMMDDHHMMSS(e.f_date*1000) +
											" : " + ooe.key);
								} else {
									System.out.println("\tIGNORE OLD" + 
											" : O=" + CUtil.timeYYMMDDHHMMSS(ooe.f_date*1000) +
											" > N=" + CUtil.timeYYMMDDHHMMSS(e.f_date*1000) +
											" : " + ooe.key);
								}
							} else {
								old_pack_entrys.put(e.key, e);
							}
						}
						fsr.close();
					}
				} catch (Throwable e) {
					System.err.println("check old update file : error : " + e.getMessage());
				}
			}	
			System.out.println("---------------------------------------------------------------------------------------------");
		}
		return old_pack_entrys;
	}
	
	/**
	 * 创建更新文件
	 * @param pattern
	 * @param input_dir
	 * @param output_file
	 * @param old_packs_dir
	 */
	public static void createUpdate(
			FileFilters pattern,
			FilePriority replacer,
			File input_dir, 
			File output_file, 
			File old_packs_dir,
			String mpqExt) throws Exception
	{
		Map<String, FileEntry> old_pack_entrys = listAllEntrys(
				old_packs_dir,
				mpqExt);
		
		// 获取当前目录中所有需要打包的文件
		String prefix = input_dir.getCanonicalPath();
		System.out.println("list pack files : " + prefix);
		FileSystemWriter fsw = new FileSystemWriter();
		ArrayList<File> files = CFile.listAllChildren(input_dir, pattern);
		if (replacer != null) {
			files = replacer.replace(files);
		}
		StringBuilder _dir = new StringBuilder();
		for (File f : files) {
			String fname = f.getCanonicalPath();
			fname = fname.substring(prefix.length());
			fname = formatName(fname);
			FileSystemWriter.Entry new_entry = fsw.genEntry(fname, f);
			FileEntry old_entry = old_pack_entrys.get(fname);
			if (old_entry == null) {
				fsw.putEntry(new_entry);
				System.out.println("ADD <- " + 
						new_entry.fe.f_md5 + " : " + 
						new_entry.fe.key);
			}
			else if (!old_entry.f_md5.equals(new_entry.fe.f_md5)) {
				fsw.putEntry(new_entry);
				System.out.println("UPDATE <- " + 
						new_entry.fe.f_md5 + " : " + 
						new_entry.fe.key + " : " +
						"old=" + old_entry.f_md5);
			}
			_dir.append(new_entry.fe.key + "\n");
		}
		if (fsw.getEntryCount() > 0) {
			System.out.println("creating pack file : " + output_file);
			fsw.save(output_file);
			System.out.println("nicely done!");
		} else {
			System.out.println("there is no update files to be found, done!");
		}
		CFile.writeText(new File(output_file.getParentFile(), ".dir"), _dir.toString(), "UTF-8");
	}
	
	/**
	 * 将打包的文件解包到目录
	 * @param input_file
	 * @param output_dir
	 * @throws Exception
	 */
	public static void unpackFiles(File input_file, File output_dir) throws Exception
	{
		FileSystemReader fsr = new FileSystemReader(input_file);
		for (FileEntry e : fsr.getEntrys()) {
			EntryData ed = fsr.getEntryData(e);
			if (ed != null) {
				File out_file = new File(output_dir, ed.entry.key);
				CFile.writeData(out_file, ed.data);
				System.out.println("EXTRACT -> " + e.f_md5 + " : " + e.key);
			} else {
				System.out.println("EXTRACT -> ERROR : " + e.key);
			}
		}
		fsr.close();
		System.out.println("done");
	}

	static public void main(String[] args)
	{
		try
		{
			HashMap<String, String> commands = new HashMap<String, String>(args.length);
			for (String cmd : args) {
				if (cmd.startsWith("-")) {
					commands.put(cmd.trim(), cmd.trim());
				}
			}

			////////////////////////////////////////////////////
			if (args.length==0)
			{
				printUsage();
			}
			////////////////////////////////////////////////////
			else if (args[0].equals("A")) 
			{
				FileFilters pattern = null;
				if (args.length>=4) {
					pattern = new FileFilters(CFile.readText(new File(args[3])));
				}
				FilePriority replacer = null;
				if (args.length >= 5) {
					replacer = new FilePriority(args[4]);
				}
				String ext = "mpq";
				if (args.length >= 6) {
					ext = args[5];
				}
				packFiles(pattern, replacer, new File(args[1]), new File(args[2]), ext);
			} 
			////////////////////////////////////////////////////
			else if (args[0].equals("E")) 
			{
				unpackFiles(new File(args[1]), new File(args[2]));
			} 
			////////////////////////////////////////////////////
			else if (args[0].equals("C")) 
			{
				String ext = "mpq";
				if (args.length >= 3) {
					ext = args[2];
				}
				listAllEntrys(new File(args[1]), ext);
			}
			////////////////////////////////////////////////////
			else if (args[0].equals("U")) 
			{
				FileFilters pattern = null;
				if (args.length >= 5) {
					pattern = new FileFilters(CFile.readText(new File(args[4])));
				}
				FilePriority replacer = null;
				if (args.length >= 6) {
					replacer = new FilePriority(args[5]);
				}
				String ext = "mpq";
				if (args.length >= 7) {
					ext = args[6];
				}
				String time_str = CUtil.timeYYMMDDHHMMSS(
						System.currentTimeMillis());
				int pindex = args[2].lastIndexOf('.');
				String out_name = args[2];
				File update_file = null;
				if (pindex >= 0) {
					update_file = new File(out_name.substring(0, pindex) + "_"
							+ time_str + out_name.substring(pindex));
				} else {
					update_file = new File(out_name + "_" + time_str);
				}
				createUpdate(pattern, replacer, new File(args[1]), update_file, new File(args[3]), ext);
			}
			////////////////////////////////////////////////////
			else
			{
				printUsage();
			}
		} 
		catch (Exception err) {
			printUsage();
			err.printStackTrace();
		}
	}

	public static void printUsage()
	{
		System.out.println(usage());
	}
	
	public static String usage()
	{
		String usage = 
				"A <输入目录> <输出文件> [匹配配置文件] [替换方案]\n" +
				"	将一个目录所有匹配的文件打包\n" +
				"\n" +
				"E <输入文件> <输出目录>\n" +
				"	将一个打包文件解开到目录\n" +
				"\n" +
				"C <过往文件目录> [MPQ后缀]\n" +
				"	测试\n" +
				"\n" +
				"U <输入文件> <输出目录> <过往文件目录> [匹配配置文件] [替换方案] [MPQ后缀]\n" +
				"	生成更新文件，此操作将和过往的文件比较差异，产生新的不同的文件\n" +
				"\n" +
				"	[匹配配置文件]\n" + 
					FileFilters.usage("\t")+ "\n" +
				"	[优选文件]\n" + 
					FilePriority.usage("\t");
		return usage;
	}
	
}
