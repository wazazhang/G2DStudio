package com.cell.tools.m3z;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import com.cell.CIO;
import com.cell.CUtil;
import com.cell.io.CFile;
import com.cell.tools.m3z.M3ZHeader.Trunk;
import com.tools.TextureConvert;

public class M3ZPacker
{
	final private BufferedImage srcImage;
	final private File 			srcImageFile;
	final private boolean		srcHasAlpha;

	private ArrayList<Trunk> trunks = new ArrayList<Trunk>();
	private HashSet<File> alltemps = new HashSet<File>();

	public M3ZPacker(File srcImageFile) throws Exception {
		initStatic();
		this.srcImageFile = srcImageFile;
		this.srcImage = ImageIO.read(srcImageFile);
		if (srcImage == null) {
			throw new Exception("Can't read image file !!! " + srcImageFile);
		}
		this.srcHasAlpha = TextureConvert.hasAlpha(srcImage);
	}
	
	/**
	 * @param type
	 * @param scale
	 * @param dir
	 * @param removeTemp
	 * @param needPow 需要单向扩展2的冥
	 * @param needSqr 需要双向扩展2的冥
	 * @return
	 * @throws Throwable
	 */
	public boolean addTrunk(
			String type,
			float scale, 
			boolean yflip, 
			File dir,
			boolean removeTemp,
			boolean needPow, 
			boolean needSqr) throws Throwable 
	{
		int ftype = CUtil.string4CharToIntLittle(type);		
		String cmd = cmdlines.get(ftype);
		
		// remove temp file
		// 尝试得到输出文件名并不执行转换脚本
		if (ftype == M3ZHeader.TYPE_ETCA || 
			ftype == M3ZHeader.TYPE_PVRA ||  
			ftype == M3ZHeader.TYPE_PKMA || 
			ftype == M3ZHeader.TYPE_A8) {
			if (!srcHasAlpha) {
				File cmd_input = srcImageFile;
				File cmd_output = new File(
						cmd_input.getParentFile(), 
						cmd_input.getName() + "." + type.toLowerCase());
				cmd_output = execCmd(cmd, cmd_input, cmd_output, dir, false);
				if (!cmd_output.equals(srcImageFile)) {
					alltemps.add(cmd_output);
				}
				return false;
			}
		}
		
		Trunk trunk = new Trunk();
		trunk.type = ftype;
		byte cdata[] = null;
		
		TextureConvert.ConvertInfo dimg = new TextureConvert.ConvertInfo();
		
		if (cmd == null || cmd.isEmpty()) 
		{
			type = "PNG";
			trunk.hasAlpha = srcHasAlpha;
			if (resize(false, false, scale, yflip, srcImageFile, dimg)) 
			{
				trunk.realPixelW = trunk.pixelW = dimg.outputImage.getWidth();
				trunk.realPixelH = trunk.pixelH = dimg.outputImage.getHeight();
				cdata = CFile.readData(dimg.outputFile);
				alltemps.add(dimg.outputFile);
			}
			else
			{
				trunk.realPixelW = trunk.pixelW = srcImage.getWidth();
				trunk.realPixelH = trunk.pixelH = srcImage.getHeight();
				cdata = CFile.readData(srcImageFile);
			}
		}
		else
		{
			// 需要扩展到2的冥
			if (ftype == M3ZHeader.TYPE_ATC_E || 
				ftype == M3ZHeader.TYPE_ATC_I ||
				ftype == M3ZHeader.TYPE_ATC_RGB  ||
				ftype == M3ZHeader.TYPE_ETC1 || 
				ftype == M3ZHeader.TYPE_ETCA ||
				ftype == M3ZHeader.TYPE_PKM1 ||
				ftype == M3ZHeader.TYPE_PKMA ) 
			{
				needPow = true;
			} 
			if (ftype == M3ZHeader.TYPE_PVR2 ||
				ftype == M3ZHeader.TYPE_PVR4 ||
				ftype == M3ZHeader.TYPE_PVR1 || 
				ftype == M3ZHeader.TYPE_PVRA)
			{
				needSqr = true;
			}
			
			File cmd_input = srcImageFile;
			
			// resize and extends pow2
			if (resize(needPow, needSqr, scale, yflip, cmd_input, dimg))
			{
				cmd_input = dimg.outputFile;
				alltemps.add(dimg.outputFile);

				trunk.pixelW = dimg.outputImage.getWidth();
				trunk.pixelH = dimg.outputImage.getHeight();
				trunk.realPixelW = dimg.usedPixelW;
				trunk.realPixelH = dimg.usedPixelH;
			}
			else
			{
				trunk.realPixelW = trunk.pixelW = dimg.outputImage.getWidth();
				trunk.realPixelH = trunk.pixelH = dimg.outputImage.getHeight();
			}

			// RGB + Alpha 分层要求
			if (ftype == M3ZHeader.TYPE_PVR1 || 
				ftype == M3ZHeader.TYPE_PKM1)
			{
				if (genmask("1RGB", cmd_input, dimg))
				{
					cmd_input = dimg.outputFile;
					alltemps.add(dimg.outputFile);
				}
			}
			if (ftype == M3ZHeader.TYPE_PVRA || 
				ftype == M3ZHeader.TYPE_PKMA)
			{
				if (genmask("1AAA", cmd_input, dimg))
				{
					cmd_input = dimg.outputFile;
					alltemps.add(dimg.outputFile);
				}
			}
			
			trunk.hasAlpha = TextureConvert.hasAlpha(dimg.outputImage);

			
			// 执行转换工具
			{
				File cmd_output = new File(
						cmd_input.getParentFile(), 
						cmd_input.getName() + "." + type.toLowerCase());
				cmd_output = execCmd(cmd, cmd_input, cmd_output, dir, true);
				if (!cmd_output.equals(srcImageFile)) {
					alltemps.add(cmd_output);
				}
				cdata = CFile.readData(cmd_output);
				if (cdata == null) {
					throw new Exception("Can not convert image : " + cmd_input);
				}
			}
		}
	
		trunk.fileSize 	= cdata.length;
		trunk.fileData 	= cdata;
		trunks.add(trunk);
		
		return true;
	}
	
	private static boolean genmask(String channel, File cmd_input, TextureConvert.ConvertInfo out) throws Throwable
	{
		File rgbf = new File(
				cmd_input.getParentFile(),
				CFile.getNoneExtension(cmd_input) + "_" + channel + "." + CFile.getExtension(cmd_input));
		TextureConvert.convert(cmd_input, rgbf,
				null, false, false, 1, false, channel, "png", out);
		return true;
	}
	
	private static boolean resize(
			boolean needPow,
			boolean needSqr,
			float scale,
			boolean yflip,
			File cmd_input, 
			TextureConvert.ConvertInfo out) throws Throwable
	{
		File srcImageFile = cmd_input;
		if (needPow)
		{
			File srcImagePowFile = new File( 
					srcImageFile.getParentFile(),
					CFile.getNoneExtension(srcImageFile)+ ".pow");
			TextureConvert.convert(
						srcImageFile, 
						srcImagePowFile, 
						null, true, false, scale, yflip, "ARGB", "png", out);
			return true;
		}
		else if (needSqr)
		{
			File srcImageSqrFile = new File( 
					srcImageFile.getParentFile(),
					CFile.getNoneExtension(srcImageFile)+ ".sqr");
			TextureConvert.convert(
					srcImageFile, 
					srcImageSqrFile, 
					null, true, true, scale, yflip, "ARGB", "png", out);
			return true;
		}
		else if (scale != 1)
		{
			File af = new File( 
					srcImageFile.getParentFile(),
					CFile.getNoneExtension(srcImageFile)+ ".scale");
			TextureConvert.convert(srcImageFile, af,
					null, false, false, scale, yflip, "ARGB", "png", out);
			return true;
		}
		else if (yflip)
		{
			File yf = new File( 
					srcImageFile.getParentFile(),
					CFile.getNoneExtension(srcImageFile)+ ".yflip");
			TextureConvert.convert(srcImageFile, yf,
					null, false, false, scale, yflip, "ARGB", "png", out);
			return true;
		}
		return false;
	}
	
	public M3ZHeader toHeader() 
	{
		M3ZHeader header = new M3ZHeader();			// 4
		header.srcWidth 	= srcImage.getWidth();	// 4
		header.srcHeight	= srcImage.getHeight();	// 4
		header.srcHasAlpha	= srcHasAlpha; 			// 1;
		header.trunks 		= trunks.toArray(new Trunk[trunks.size()]);
		return header;
	}

	public void clean() {
		for (File file : alltemps) {
			if (file.exists()) {
				file.delete();
			}
		}
	}

//	-----------------------------------------------------------------------------------
	
	
	
	
//	-----------------------------------------------------------------------------------
	
	private static HashMap<Integer, String> cmdlines = null;
	private static void initStatic() {
		if (cmdlines == null) {
			cmdlines = new HashMap<Integer, String>();
			readCmdLines(CIO.getInputStream("/com/cell/tools/m3z/cmdlines.txt"));
		}
	}
	
	public static void readCmdLines(InputStream is) 
	{
		if (cmdlines == null) {
			cmdlines = new HashMap<Integer, String>();
		}
		String[] lines = CIO.readAllLine(is);
		for (String line : lines) {
			String[] kv = CUtil.splitString(line, "=", 2, true);
			if (kv.length==2) {
				Integer key = CUtil.string4CharToIntLittle(kv[0]);
				cmdlines.put(key, kv[1]);
				System.out.println(kv[0]+" = " + kv[1]);
			}
		}
	}
	
	private static File execCmd(String cmd_out, File in, File out, File dir, boolean runexe) throws Throwable
	{
		File outfile = out;
		String command = "";
		{
			String in_ext = in.getName().substring(in.getName().lastIndexOf(".")+1);
			String in_pre = in.getName().substring(0, in.getName().lastIndexOf("."));
			cmd_out = CUtil.replaceString(cmd_out, "<dir>", 	in.getParent());
			cmd_out = CUtil.replaceString(cmd_out, "<in_path>", in.getPath());
			cmd_out = CUtil.replaceString(cmd_out, "<in_name>", in.getName());
			cmd_out = CUtil.replaceString(cmd_out, "<in_pre>", 	in_pre);
			cmd_out = CUtil.replaceString(cmd_out, "<in_ext>", 	in_ext);
			cmd_out = CUtil.replaceString(cmd_out, "<out>", 	out.getPath());
			
			int ld = cmd_out.lastIndexOf("->");
			command = cmd_out.substring(0, ld).trim();
			String of = cmd_out.substring(ld+2).trim();
			if (!of.isEmpty()) {
				outfile = new File(of);
			}
		}
		if (runexe)
		{
			System.out.println("cmd convert to : >>> " + outfile.getName() + " <<<");
			if (!command.isEmpty()) 
			{
				Map<String, String> map = System.getenv();
				String[] ev = new String[map.size()];
				int index = 0;
				for (Entry<String, String> e : map.entrySet()) {
					if (e.getKey().toUpperCase().equals("PATH")) {
						ev[index++] = e.getKey() + "=" + e.getValue()+";"+dir.getCanonicalPath();
					} else {
						ev[index++] = e.getKey() + "=" + e.getValue();
					}
				}
				dir = dir.getCanonicalFile();
				System.out.print(dir.getPath() + " :> " + command);
				Process p = Runtime.getRuntime().exec(command, ev, dir);
				p.waitFor();
				System.out.println(" | exit code(" + p.exitValue() + ")");
				Thread.yield();
				String pout = CIO.readAllText(p.getInputStream(), "GBK");
				String perr = CIO.readAllText(p.getErrorStream(), "GBK");
				if (!pout.isEmpty()) {
					System.out.println(pout.trim());
				}
				if (!perr.isEmpty()) {
					System.err.println(perr.trim());
				}
			}
		}
		return outfile;
	}
	
	
	
	
	
	
	
	
	
}
