package com.cell.tools.m3z;

import java.awt.EventQueue;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import com.cell.CObject;
import com.cell.CUtil;
import com.cell.io.CFile;
import com.cell.j2se.CAppBridge;
import com.cell.util.FileFilters;
import com.cell.util.FileMake;
import com.cell.util.Properties;

public class M3ZConvert
{
	
	
	public static void convert(
			File srcFile, 
			float scale,
			boolean yflip,
			String[] fmts, 
			String newExt, 
			File workdir,
			boolean removeTemp,
			boolean needPOW,
			boolean needSQR) throws Throwable
	{
		M3ZPacker m3z = new M3ZPacker(srcFile);
		
		System.out.println(
		"----------------------------------------------------------------------------------------------------------------\n" +
		"- Convert M3Z file from : " + srcFile.getPath() + "\n" +
		"----------------------------------------------------------------------------------------------------------------"
		);	

		try {
			for (String type : fmts) {
				if (m3z.addTrunk(type, scale, yflip, workdir, removeTemp, needPOW, needSQR) == false) {
					System.out.println("ignore type : " + type);
				}
			}
			M3ZHeader hd = m3z.toHeader();
			String outname = srcFile.getName();
			outname = outname.substring(0, outname.lastIndexOf('.')) + newExt;
			
			File output = new File(srcFile.getParentFile(), outname);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				hd.write(baos);
			} finally {
				baos.close();
			}
			CFile.writeData(output, baos.toByteArray());
			System.out.println("create m3z file : " + output + " << " + CUtil.arrayToString(fmts));
			System.out.println("create m3z file : " + hd);
		} finally {
			if (removeTemp) {
				m3z.clean();
			}
		}
	}

	public static void batch(
			File srcDir, 
			FileFilters p,
			FileMake h, 
			float scale, 
			boolean yflip,
			String newExt,
			String fmts[],
			File workdir,
			boolean removeTemp,
			boolean needPOW,
			boolean needSQR) throws Exception	
	{
		ArrayList<File> files = CFile.listAllChildren(srcDir, p);
		if (h != null) {
			files = h.getNewerList(files);
		}
		for (File s : files) {
			try {
				convert(s.getCanonicalFile(), 
						scale, yflip,
						fmts, 
						newExt, 
						workdir,
						removeTemp,
						needPOW,
						needSQR);
			} catch (Throwable e) {
				e.printStackTrace();
				System.err.println("囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧");
				System.err.println("Can not convert image:");
				System.err.println(s.toString());
				System.err.println("囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧囧");
				while(true){
					Thread.sleep(1);
				}
			}
		}
	}
	
	public static void main(String ... args) throws Throwable
	{
		CAppBridge.initNullStorage();
		M3ZHeader.printConsts();
		
		if (args.length >= 1)
		{
			Properties pp = new Properties(CUtil.arrayToString(args, "\n"), ":");
			String _c = pp.get("-c");
			String _w = pp.get("-w");
			String _rt = pp.get("-rt");
			boolean removeTemp = false;
			if (_rt != null) {
				removeTemp = Boolean.parseBoolean(_rt);
			}
			if (_c != null) {
				M3ZPacker.readCmdLines(new FileInputStream(new File(_c)));
			}
			File workdir = new File(".").getCanonicalFile();
			if (_w != null) {
				workdir = new File(_w).getCanonicalFile();
			}
			
			if (Arrays.binarySearch(args, "-ui") >= 0)		
			{
				final File wdr = workdir;
				final boolean rt = removeTemp;
				EventQueue.invokeLater(new Runnable() {
			        public void run() {
			        	new M3ZConvertUI(wdr, rt).setVisible(true);
			        }
			    });
				return;
			}
			else
			{			
				String _f = pp.get("-f");
				String _e = pp.get("-e");
				String _d = pp.get("-d");
				String _i = pp.get("-i");
				String _scale = pp.get("-scale");
				float scale = (_scale != null) ? Float.parseFloat(_scale) : 1.0f;
				boolean yflip = (pp.get("-yflip") != null);
				boolean pow = (pp.get("-pow") != null);
				boolean sqr = (pp.get("-sqr") != null);

				if (_e == null) {
					_e = ".m3z";
				}
				
				String[] fmts = CUtil.splitString(_f, ",");
				if(_i != null) 
				{
					convert(new File(_i), scale, yflip, fmts, _e, workdir, removeTemp, pow, sqr);
					return;
				}
				else if(_d != null) 
				{
					String _h = pp.get("-h");
					String _p = pp.get("-p");
					String __p = pp.get("--p");
					FileFilters pattern = null;
					if (_p != null) {
						pattern = new FileFilters(CFile.readText(new File(_p)));
					}
					if (__p != null) {
						pattern = new FileFilters(__p);
					}
					FileMake history = null;
					if (_h != null) {
						history = new FileMake(_h);
					}
					batch(new File(_d), pattern, history, scale, yflip, _e, fmts, workdir, removeTemp, pow, sqr);
					return;
				}
			}
		}
		printUsage();
	}
	
	public static void printUsage()
	{
		System.out.println(usage());
	}
	
	public static String usage()
	{
		String usage = 
			"1. open converter ui.\n" +
			"   usage: -ui\n" +
			"\n" +
			"2. convert single image to compressed texture with m3z format.\n" +
			"   usage: -f:<format> -i:<inputFile>\n" +
			"\n" +
			"3. convert all image to compressed texture with m3z format.\n" +
			"   usage: -f:<format> -d:<inputdir> [-p:<FileFilters>] [--p:<Filters>] [-h:<FileMake:[dst_replace]>]\n" +
			"\n" +
			"- addition command:\n" +
			"        [-w:<workdir>]" +
			"        [-c:<cmdlines>]" +
			"        [-scale:<scale>]" +
			"        [-e:<new extention>]\n" +
			"        [-yflip:]" +
			"\n" +
			"- args description:\n" +
			"	-f	   : eg \"ETC1,ETCA\" or \"ETC1,A8\" or \"PVR4\" \n" +
			"            PVR4 means contains compressed PVR4 texture data.\n" +
			"            PVR1,PVRA means contains dual compressed PVR4bpp(RGB) and PVR4bpp(Alpha).\n" +
			"            ETC1,ETCA means contains dual compressed ETC1(RGB) and ETC1(Alpha).\n" +
			"            PKM1,PKMA means contains dual compressed ETC1(RGB) and ETC1(Alpha).\n" +
			"            ETC1,A8 means contains dual ETC1 and A8 alpha channel.\n" +
			"	-c	   : eg PVRTexTool -fOGLPVRTC4 -p -i<in> -o<out>\n" +
			"	-rt	   : et -rt:true remove temp files.\n" +
			"	-pow   : pow src img to pow2.\n" +
			"	-sqr   : make src width == height.\n" +
			"	-h	   : FileMake\n" + FileMake.usage(
			"			 ") + "\n" +
			"	-p	   : FileFilters\n" + FileFilters.usage(
			"			 ");
		return usage;
	}


}
