package com.cell.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.cell.CIO;
import com.cell.CObject;
import com.cell.util.FileFilters;

public class CFile 
{
	public static ArrayList<File> listAllChildren(java.io.File parent, FileFilters pattern) {
		ArrayList<File> ret = new ArrayList<File>();
		listAllChildren(parent, ret, pattern);
		return ret;
	}
	public static void listAllChildren(java.io.File parent, ArrayList<File> files, FileFilters pattern) {
		if (parent.isDirectory()) {
			for (File f : parent.listFiles()) {
				listAllChildren(f, files, pattern);
			}
		} else if (parent.isFile()) {
			if (pattern == null || pattern.acceptFile(parent)) {
				files.add(parent);
			}
		}
	}
	
	public static boolean isChild(java.io.File parent, java.io.File child) {
		File fp = child.getParentFile();
		while (fp != null) {
			if (fp.equals(parent)) {
				return true;
			}
			fp = fp.getParentFile();
		}
		return false;
	}
	
	public static String readText(java.io.File file, String encoding)
	{
		try{
			FileInputStream fis = new FileInputStream(file);
			return CIO.readAllText(fis, encoding);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean appendText(java.io.File file, String text, String encoding)
	{
		return appendData(file, CIO.stringEncode(text, encoding));
	}
	public static boolean appendText(java.io.File file, String text)
	{
		return appendData(file, CIO.stringEncode(text, CObject.ENCODING));
	}
	
	public static boolean writeText(java.io.File file, String text, String encoding)
	{
		return writeData(file, CIO.stringEncode(text, encoding));
	}
	
	public static String readText(java.io.File file)
	{
		return readText(file, CObject.ENCODING);
	}
	
	public static boolean writeText(java.io.File file, String text)
	{
		return writeText(file, text, CObject.ENCODING);
	}
	
	public static boolean copy(java.io.File src, java.io.File dst) 
	{
		byte[] data = readData(src);
		return writeData(dst, data);
	}
	
	public static byte[] readData(java.io.File file)
	{
		if (file.exists()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				return CIO.readStream(fis);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					fis.close();
				} catch (Exception e2) {
				}
			}
		}
		return null;
	}
	
	public static byte[] readData(java.io.File file, final int file_start, final int length)
	{
		if (file.exists()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				fis.skip(file_start);
				byte[] ret = new byte[length];
				readData(fis, ret, 0, length);
				return ret;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					fis.close();
				} catch (Exception e2) {
				}
			}
		}
		return null;
	}
	
	public static int readData(FileInputStream fis, byte[] dst, int start, int length) throws Exception
	{
		int readed = 0;
		while (readed < length) {
			int rpos = start + readed;
			int rlen = Math.min(length - readed, fis.available());
			if (rlen == 0) {
				break; 
			}
			int rd = fis.read(dst, rpos, rlen);
			if (rd < 0) {
				return -1;
			}
			readed += rd;
		}
		return readed;
	}
	
	public static boolean createFile(java.io.File file) {
		try {
			file = file.getCanonicalFile();
			if (file.getParentFile() != null && !file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean writeData(java.io.File file, byte[] data)
	{
		try {
			if (createFile(file)) {
				FileOutputStream fos = new FileOutputStream(file);
				try {
					fos.write(data);
					fos.flush();
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					fos.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean appendData(java.io.File file, byte[] data) 
	{
		try {
			if (createFile(file)) {
				FileOutputStream fos = new FileOutputStream(file, true);
				try {
					fos.write(data);
					fos.flush();
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					fos.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean copyFile(java.io.File src, java.io.File dst)
	{
		byte[] data = readData(src);
		return writeData(dst, data);
	}
	
	
	/**
	 * 删除目录下指定后缀的所有文件
	 * @param png
	 * @param suffix
	 * @throws IOException 
	 */
	static public void deleteFiles(File png, String suffix) throws IOException {
		File[] list = png.listFiles();
		if (list != null) {
			for (File _jpg : list) {
				if (_jpg.getName().toLowerCase().endsWith(suffix)) {
					_jpg.getCanonicalFile().delete();
				}
			}
		}
	}
	
	/**
	 * 删除目录下指定后缀的所有文件
	 * @param png
	 * @param suffix
	 * @throws IOException 
	 */
	static public void deleteFilesRecursion(File png, String suffix) throws IOException {
		File[] list = png.listFiles();
		if (list != null) {
			for (File _jpg : list) {
				if (_jpg.isDirectory()) {
					deleteFilesRecursion(_jpg, suffix);
				}
				else if (_jpg.getName().toLowerCase().endsWith(suffix)) {
					_jpg.getCanonicalFile().delete();
				}
			}
		}
	}
	
	/**
	 * 递归删除file，如果file是个目录，则递归其子目录全部删除
	 * @param file
	 */
	static public void deleteIfExists(File file) {
		if (file != null && file.exists()) {
			if (file.isDirectory()) {
				for (File sub : file.listFiles()) {
					deleteIfExists(sub);
				}
			}
			file.delete();
		}
	}

	static public String getExtension(File f) {
		if (f.isDirectory()) {
			return f.getName();
		}
		String ret = f.getName();
		int ld = ret.lastIndexOf('.');
		return ret.substring(ld + 1);
	}
	
	static public String getNoneExtension(File f) {
		if (f.isDirectory()) {
			return f.getName();
		}
		String ret = f.getName();
		int ld = ret.lastIndexOf('.');
		return ret.substring(0, ld);
	}
}













