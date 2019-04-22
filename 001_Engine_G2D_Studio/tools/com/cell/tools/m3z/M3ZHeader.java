package com.cell.tools.m3z;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.cell.CUtil;
import com.cell.io.LittleIODeserialize;
import com.cell.io.LittleIOSerialize;

/**
 * M3Z是morefuntek纹理压缩打包格式，
 * "@左应@章世宁@张翼飞"
 * @author zhangyifei
 */
public class M3ZHeader 
{
	public static final int HEADER 			= CUtil.string4CharToIntLittle("MF3Z");
	
	public static final int TYPE_PNG		= CUtil.string4CharToIntLittle("PNG");
	public static final int TYPE_JPG		= CUtil.string4CharToIntLittle("JPG");
	public static final int TYPE_BMP		= CUtil.string4CharToIntLittle("BMP");
	
	public static final int TYPE_PVR4		= CUtil.string4CharToIntLittle("PVR4");
	public static final int TYPE_PVR2		= CUtil.string4CharToIntLittle("PVR2");
	public static final int TYPE_PVR1		= CUtil.string4CharToIntLittle("PVR1");
	public static final int TYPE_PVRA		= CUtil.string4CharToIntLittle("PVRA");

	public static final int TYPE_PKM1		= CUtil.string4CharToIntLittle("PKM1");
	public static final int TYPE_PKMA		= CUtil.string4CharToIntLittle("PKMA");
	
	public static final int TYPE_ATC_E		= CUtil.string4CharToIntLittle("ATCE");
	public static final int TYPE_ATC_I		= CUtil.string4CharToIntLittle("ATCI");
	public static final int TYPE_ATC_RGB	= CUtil.string4CharToIntLittle("ATC3");
	
	public static final int TYPE_ETC1		= CUtil.string4CharToIntLittle("ETC1");
	public static final int TYPE_ETCA		= CUtil.string4CharToIntLittle("ETCA");

	public static final int TYPE_RGBA8		= CUtil.string4CharToIntLittle("RGBA");
	public static final int TYPE_A8			= CUtil.string4CharToIntLittle("A8");
	
	static public void printConsts()
	{
		System.out.println("---------------------------------------------");
		System.out.println("HEADER       = 0x" + CUtil.toHexU32(HEADER));
		System.out.println("TYPE_PNG     = 0x" + CUtil.toHexU32(TYPE_PNG));
		System.out.println("TYPE_JPG     = 0x" + CUtil.toHexU32(TYPE_JPG));
		System.out.println("TYPE_BMP     = 0x" + CUtil.toHexU32(TYPE_BMP));
		System.out.println("");
		System.out.println("TYPE_PVR4    = 0x" + CUtil.toHexU32(TYPE_PVR4));
		System.out.println("TYPE_PVR2    = 0x" + CUtil.toHexU32(TYPE_PVR2));
		System.out.println("TYPE_PVR1    = 0x" + CUtil.toHexU32(TYPE_PVR1));
		System.out.println("TYPE_PVRA    = 0x" + CUtil.toHexU32(TYPE_PVRA));
		System.out.println("");
		System.out.println("TYPE_PKM1    = 0x" + CUtil.toHexU32(TYPE_PKM1));
		System.out.println("TYPE_PKMA    = 0x" + CUtil.toHexU32(TYPE_PKMA));
		System.out.println("");
		System.out.println("TYPE_ATC_E   = 0x" + CUtil.toHexU32(TYPE_ATC_E));
		System.out.println("TYPE_ATC_I   = 0x" + CUtil.toHexU32(TYPE_ATC_I));
		System.out.println("TYPE_ATC_RGB = 0x" + CUtil.toHexU32(TYPE_ATC_RGB));
		System.out.println("");
		System.out.println("TYPE_ETC1    = 0x" + CUtil.toHexU32(TYPE_ETC1));
		System.out.println("TYPE_ETCA    = 0x" + CUtil.toHexU32(TYPE_ETCA));
		System.out.println("");
		System.out.println("TYPE_RGBA8   = 0x" + CUtil.toHexU32(TYPE_RGBA8));
		System.out.println("TYPE_A8      = 0x" + CUtil.toHexU32(TYPE_A8));
		System.out.println("---------------------------------------------");
	}
	
	/**文件头*/
	final public int header = HEADER;
	/**版本*/
	public byte[] 	version = new byte[] { 0, 1, 0, 0 };
	/**原始图片宽*/
	public int 		srcWidth;
	/**原始图片高*/
	public int 		srcHeight;
	/**原始图片是否包含半透明*/
	public boolean	srcHasAlpha;
	/**扩展数据*/
	public String	extUTFData = "";
	/**纹理块*/
	public Trunk[]	trunks = new Trunk[]{};
	
	
	static public class Trunk
	{
		/**类型*/
		public int 			type;
		
		/**是否包含半透明*/
		public boolean		hasAlpha;
		/**二的冥宽*/
		public int 			pixelW;
		/**二的冥高*/
		public int 			pixelH;
		/**实际像素点宽*/
		public int			realPixelW;
		/**实际像素点高*/
		public int			realPixelH;

		/**扩展数据*/
		public String 		extUTFData = "";
		
		/**数据段尺寸*/
		public int 			fileSize;
		
		/**数据段*/
		public byte[] 		fileData;

	}
	
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append("M3ZHeader:");
		sb.append("\n      srcWidth = " + srcWidth);
		sb.append("\n     srcHeight = " + srcHeight);
		sb.append("\n   srcHasAlpha = " + srcHasAlpha);
		sb.append("\n    trunkCount = " + trunks.length);
		for (Trunk trunk : trunks) {
		sb.append("\n        [trunk]");
		sb.append("\n          type = " + CUtil.intLittle4String4Char(trunk.type));
		sb.append("\n      hasAlpha = " + trunk.hasAlpha);
		sb.append("\n        pixelW = " + trunk.pixelW);
		sb.append("\n        pixelH = " + trunk.pixelH);
		sb.append("\n    realPixelW = " + trunk.realPixelW);
		sb.append("\n    realPixelH = " + trunk.realPixelH);
		sb.append("\n      fileSize = " + trunk.fileSize);
		}
		return sb.toString();
	}
	
	public void write(OutputStream o) throws IOException
	{
		LittleIOSerialize.putInt		(o, header);
		o.write(version, 0, 4);
		LittleIOSerialize.putInt		(o, srcWidth);
		LittleIOSerialize.putInt		(o, srcHeight);
		LittleIOSerialize.putBoolean	(o, srcHasAlpha);
		LittleIOSerialize.putString		(o, extUTFData);
		LittleIOSerialize.putInt		(o, trunks.length);
		for (int i = 0; i < trunks.length; i++) 
		{
			Trunk tk = trunks[i];
			LittleIOSerialize.putInt		(o, tk.type);
			LittleIOSerialize.putBoolean	(o, tk.hasAlpha);
			LittleIOSerialize.putInt		(o, tk.pixelW);
			LittleIOSerialize.putInt		(o, tk.pixelH);
			LittleIOSerialize.putInt		(o, tk.realPixelW);
			LittleIOSerialize.putInt		(o, tk.realPixelH);
			LittleIOSerialize.putString		(o, tk.extUTFData);
			LittleIOSerialize.putInt		(o, tk.fileSize);
			o.write(tk.fileData, 0, tk.fileSize);
		}
	}
	
	public void read(InputStream in) throws IOException 
	{
		int hd = LittleIODeserialize.getInt(in);
		if (hd != header) {
			throw new IOException("file header error : expect is [MF3Z]");
		}
		in.read(version, 0, 4);
		srcWidth	= LittleIODeserialize.getInt(in);
		srcHeight	= LittleIODeserialize.getInt(in);
		srcHasAlpha	= LittleIODeserialize.getBoolean(in);
		extUTFData	= LittleIODeserialize.getString(in);
		trunks		= new Trunk[LittleIODeserialize.getInt(in)];
		for (int i = 0; i < trunks.length; i++) 
		{
			Trunk tk = trunks[i] = new Trunk();
			tk.type			= LittleIODeserialize.getInt	(in);
			tk.hasAlpha		= LittleIODeserialize.getBoolean(in);
			tk.pixelW		= LittleIODeserialize.getInt	(in);
			tk.pixelH		= LittleIODeserialize.getInt	(in);
			tk.realPixelW	= LittleIODeserialize.getInt	(in);
			tk.realPixelH	= LittleIODeserialize.getInt	(in);
			tk.extUTFData	= LittleIODeserialize.getString	(in);
			tk.fileSize		= LittleIODeserialize.getInt	(in);
			tk.fileData		= new byte[tk.fileSize];
			in.read(tk.fileData, 0, tk.fileSize);
		}
	}
}
