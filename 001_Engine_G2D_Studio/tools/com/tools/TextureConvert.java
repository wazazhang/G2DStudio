package com.tools;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import com.cell.CUtil;
import com.cell.io.CFile;
import com.cell.util.FileFilters;
import com.cell.util.FileMake;
import com.cell.util.Properties;


public class TextureConvert 
{
	public static class ConvertInfo
	{
		public File inputFile;
		public File outputFile;
		
		public BufferedImage inputImage;
		public BufferedImage outputImage;
		
		public int usedPixelW;
		public int usedPixelH;
	}

	static int ccNextPOT(int x)
	{
		x = x - 1;
		x = x | (x >> 1);
		x = x | (x >> 2);
		x = x | (x >> 4);
		x = x | (x >> 8);
		x = x | (x >>16);
		return x + 1;
	}
	
	public static boolean hasAlpha(BufferedImage img) {
		for (int x=img.getWidth()-1; x>=0; --x) {
			for (int y=img.getHeight()-1; y>=0; --y) {
				int argb = img.getRGB(x, y);
				if ((argb&0xff000000) != 0xff000000) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean convert(
			File in, 
			File out, 
			String newExt,
			boolean pow2,
			boolean square,
			float scale, 
			boolean yflip,
			String channel,
			String fmt,
			ConvertInfo info) throws Throwable
	{
		ConvertInfo ret = info;
		ret.inputFile = in;
		ret.outputFile = out;
		
		channel = channel.toUpperCase();
		try {
			String ext = in.getName().substring(in.getName().lastIndexOf(".")+1);
			if (out == null) {
				out = new File(in.getPath());
			}
			if (newExt != null) {
				String path = out.getPath();
				String key = path.substring(0, path.length() - ext.length()) + newExt;
				out = new File(key);
			}
			BufferedImage image = ImageIO.read(in);
			int powW = (int)Math.ceil(image.getWidth() * scale);
			int powH = (int)Math.ceil(image.getHeight() * scale);
			ret.inputImage = image;
			ret.usedPixelW = powW;
			ret.usedPixelH = powH;
			if (pow2)
			{
				powW = ccNextPOT(powW);
				powH = ccNextPOT(powH);
			}
			if (square) 
			{
				powW = Math.max(powW, powH);
				powH = Math.max(powW, powH);
			}
			BufferedImage buf = new BufferedImage(
					powW, 
					powH, 
					BufferedImage.TYPE_INT_ARGB);
			{
				// draw image in buff
				Graphics2D g2d = (Graphics2D)buf.getGraphics();
				try 
				{
					if (yflip)
					{
						g2d.translate(0, powH);
						g2d.scale(scale, -scale);
					}
					else if (scale != 1.0f) 
					{
						g2d.scale(scale, scale);
					}
					g2d.drawImage(image, 0, 0, null);
				}
				finally {
					g2d.dispose();
				}
				// gen ARGB mask
				if (!channel.equals("ARGB"))
				{
					buf = filter(buf, channel);
				}
			}
			ImageIO.write(buf, fmt.toLowerCase(), out);
			System.out.println("TexturePow: " + in.getPath() + 
					" ("+image.getWidth()+","+image.getHeight()+")->("+buf.getWidth()+","+buf.getHeight()+")" +
					" " + out.getName());
			ret.outputImage = buf;
			return true;
		} catch (Throwable e) {
			System.err.println("Error: " + in.getPath());
			throw e;
		}
	}

	public static BufferedImage filter(BufferedImage src, String channel) throws Exception
	{
		if (channel.length() != 4)
		{
			throw new Exception(
				"Channel arg must be have 4 chars !!!\n" +			
				"	[-channel:ARGB or 0RGB or A000 or AAAA or 1RGB or 1AAA XAAA]\n" +
				"		0 is empty, 1 is 255, X is mix(ARGB)");
		}
		for (int y = src.getHeight() - 1; y >= 0; --y)
		{
			for (int x = src.getWidth() - 1; x >= 0; --x)		
			{
				int argb = src.getRGB(x, y);
				int a = (argb & 0xFF000000) >> 24;
				int r = (argb & 0x00FF0000) >> 16;
				int g = (argb & 0x0000FF00) >> 8;
				int b = (argb & 0x000000FF) >> 0;
				
				argb = 	((getChannel(channel.charAt(0), a, r, g, b) & 0xFF) << 24) | 
						((getChannel(channel.charAt(1), a, r, g, b) & 0xFF) << 16) | 
						((getChannel(channel.charAt(2), a, r, g, b) & 0xFF) << 8) | 
						((getChannel(channel.charAt(3), a, r, g, b) & 0xFF) << 0);
				
				src.setRGB(x, y, argb);
			}
		}
		return src;
	}
	
	final private static int full_x4 = 255 * 4;
	
	private static int getChannel(char ch, int a, int r, int g, int b)
	{
		switch (ch) {
		case 'A':
		case 'a':
			return a;
		case 'R':
		case 'r':
			return r;
		case 'G':
		case 'g':
			return g;
		case 'B':
		case 'b':
			return b;
		case '1':
			return 0xFF;
		case '0':
			return 0;
		case 'X':
		case 'x':
			return (a + r + g + b) / full_x4;
		default:
			return 0;
		}
	}
	
	public static void batch(
			File in, 
			FileFilters p,
			FileMake h, 
			String newExt,
			boolean pow2,
			boolean square,
			float scale,
			boolean yflip,
			String channel,
			String fmt) throws Exception	
	{
		ArrayList<File> files = CFile.listAllChildren(in, p);
		if (h != null) {
			files = h.getNewerList(files);
		}
		for (File s : files) {
			try {
				convert(s, null, newExt, pow2, square, scale, yflip, channel, fmt, new ConvertInfo());
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String ... args) throws Throwable
	{
		if (args.length >= 2)
		{
			Properties pp = new Properties(CUtil.arrayToString(args, "\n"), ":");
			String _i = pp.get("-i");
			String _f = pp.get("-f");
			String _o = pp.get("-o");
			String _e = pp.get("-e");
			String _d = pp.get("-d");
			String _p = pp.get("-p");
			String _h = pp.get("-h");
			String _c = pp.get("-channel");
			String _square = pp.get("-square");
			String _scale = pp.get("-scale");
			String _yflip = pp.get("-yflip");
			boolean square = false;
			boolean yflip = _yflip != null;
			float scale = 1.0f;
			
			if (_square != null) {
				square = Boolean.parseBoolean(_square);
			}
			if (_scale != null) {
				scale = Float.parseFloat(_scale);
			}
			if (_c == null || _c.trim().isEmpty()) {
				_c = "ARGB";
			}
			// convert single file
			if (_i != null) 
			{
				File out = _o != null ? new File(_o) : null;
				convert(new File(_i), out, _e, true, square, scale, yflip, _c, _f, new ConvertInfo());
			}
			// convert files from directory
			else if(_d != null) 
			{
				FileFilters pattern = 
						_p != null ? pattern = new FileFilters(CFile.readText(new File(_p))) : null;
				FileMake history = 
						_h != null ? history = new FileMake(_h) : null;
				batch(new File(_d), pattern, history, _e, true, square, scale, yflip, _c, _f);
			}
			else {
				printUsage();
			}
		} else {
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
			"usage: -f:<format> -i:<inputfile> [-o:<outputfile>] [-e:<new extention>]\n" +
			"       -f:<format> -d:<inputdir> [-p:<RegexFile>] [-h:<HistoryLog:[dst_replace]>] [-e:<new extention>]\n" +
			"	[-channel:ARGB or 0RGB or A000 or AAAA or 1RGB or 1AAA XAAA]\n" +
			"		0 is empty, 1 is 255, X is mix(ARGB)\n" +
			"	[-square:true]\n" +
			"	[-scale:1.0]\n" +
			"	[HistoryLog]\n" + FileMake.usage("\t") + "\n" +
			"	[RegexFile]\n" + FileFilters.usage("\t");
		return usage;
	}

}
