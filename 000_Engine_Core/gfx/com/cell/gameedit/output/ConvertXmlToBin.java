package com.cell.gameedit.output;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import com.cell.gameedit.object.ImagesSet;
import com.cell.gameedit.object.MapSet;
import com.cell.gameedit.object.SpriteSet;
import com.cell.gameedit.object.WorldSet;
import com.cell.gameedit.object.WorldSet.WaypointObject;
import com.cell.io.CFile;
import com.cell.io.LittleIODeserialize;
import com.cell.io.LittleIOSerialize;
import com.cell.util.FileFilters;
import com.cell.util.FileMake;

/**
 * @author zhangyifei
 *
 */
public class ConvertXmlToBin 
{
	public static byte[] xmlToBin(String file, String version) throws Exception
	{
		OutputXmlDir src = new OutputXmlDir(file);
		return new OutputBin.Convert(version).saveToBin(src);
	}
	
//	////////////////////////////////////////////////////////////////////////
//	// 
//	////////////////////////////////////////////////////////////////////////

	public static void batch(File input_dir, FileFilters pattern, FileMake fm, String version, String newExt)
	{
		ArrayList<File> files = CFile.listAllChildren(input_dir, pattern);
		if (fm != null) {
			files = fm.getNewerList(files);
		}
		for (File file : files) {
			try {
				String path = file.getCanonicalPath();
				String newPath = path.substring(0, path.lastIndexOf('.')) + "."	+ newExt;
				byte[] bin = xmlToBin(path, version);
				if (bin != null) {
					File output = new File(newPath);
					CFile.writeData(output, bin);
					System.out.println("Transfer: " + file + " -> " + output.getName() + " OK!");
				}else{
					System.out.println("Ignore: " + file);
				}
				// validate
				//OutputBinDir out = new OutputBinDir(output.getCanonicalPath());
				//out.toString();
			} catch (Throwable e) {
				System.err.println("Transfer Error: " + file + " : " + e.getMessage());
//				e.printStackTrace();
			}
		}
	}
	
	public static void main(String ... args)
	{
		if (args.length >= 1)
		{
			FileFilters pattern = null;
			if (args.length>=2 && !args[1].isEmpty()) {
				pattern = new FileFilters(CFile.readText(new File(args[1])));
			} else {
				pattern = new FileFilters("+.xml");
			}
			FileMake makefile = null;
			if (args.length>=3 && !args[2].isEmpty()) {
				makefile = new FileMake(args[2]);
			}
			String version = "0.0.0.0";
			if (args.length >= 4) {
				version = args[3];
			}
			String newExt = "bin";
			if (args.length >= 5) {
				newExt = args[4];
			}
			batch(new File(args[0]), pattern, makefile, version, newExt);
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
			"*************************************************\n" +
			"* Convert CellGameEdit output.xml -> output.xml.bin *\n" +
			"*************************************************\n" +
			"usage: <InputDir> [RegexFile] [FileMake] [Version] [NewExtName]\n" +
			"	[RegexFile]\n" + FileFilters.usage("\t") + "\n" +
			"	[FileMake]\n" + FileMake.usage("\t");
		return usage;
	}
	
	
	
	
	
	
	
	
	
	
	
}
