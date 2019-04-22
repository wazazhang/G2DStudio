package com.cell.util.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterInputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import com.cell.CIO;
import com.cell.CUtil;
import com.cell.io.CFile;
import com.cell.io.LittleIOSerialize;
import com.cell.util.FileFilters;
import com.cell.util.FileMake;
import com.cell.util.Pair;

public class ZipUtil 
{
	static public ByteArrayOutputStream packFiles(ArrayList<File> files, long time)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zip_out = new ZipOutputStream(baos);
		try{
			for (File file : files) {
				byte[] data = CFile.readData(file);
				if (data != null) {
					ZipEntry entry = new ZipEntry(file.getName());
					entry.setTime(time);
					try{
						zip_out.putNextEntry(entry);
						zip_out.write(data);
					} catch(Exception err){
						err.printStackTrace();
					}
				}
			}
		} finally {
			try {
				zip_out.close();
			} catch (IOException e) {}
		}
		return baos;
	}
	
	static public ByteArrayOutputStream packFile(File file, long time)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zip_out = new ZipOutputStream(baos);
		try{
			byte[] data = CFile.readData(file);
			if (data != null) {
				ZipEntry entry = new ZipEntry(file.getName());
				entry.setTime(time);
				try{
					zip_out.putNextEntry(entry);
					zip_out.write(data);
				} catch(Exception err){
					err.printStackTrace();
				}
			}
		} finally {
			try {
				zip_out.close();
			} catch (IOException e) {}
		}
		return baos;
	}

	static public ByteArrayOutputStream packStreams(ArrayList<Pair<InputStream, String>> files, long time)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zip_out = new ZipOutputStream(baos);
		try{
			for (Pair<InputStream, String> file : files) {
				byte[] data = CIO.readStream(file.getKey());
				if (data != null) {
					ZipEntry entry = new ZipEntry(file.getValue());
					entry.setTime(time);
					try{
						zip_out.putNextEntry(entry);
						zip_out.write(data);
					} catch(Exception err){
						err.printStackTrace();
					}
				}
			}
		} finally {
			try {
				zip_out.close();
			} catch (IOException e) {}
		}
		return baos;
	}
	
	static public ByteArrayOutputStream packStream(InputStream is, String name, long time)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zip_out = new ZipOutputStream(baos);
		try{
			byte[] data = CIO.readStream(is);
			if (data != null) {
				ZipEntry entry = new ZipEntry(name);
				entry.setTime(time);
				try{
					zip_out.putNextEntry(entry);
					zip_out.write(data);
				} catch(Exception err){
					err.printStackTrace();
				}
			}
		} finally {
			try {
				zip_out.close();
			} catch (IOException e) {}
		}
		return baos;
	}
	
	/**
	 * 该方法将关闭刘
	 * @param input
	 * @return
	 */
	static public Map<String, ByteArrayInputStream> unPackFile(InputStream input)
	{
		HashMap<String, ByteArrayInputStream> inputs = new HashMap<String, ByteArrayInputStream>();
		
		ZipInputStream zip_in = new ZipInputStream(input);
		
		try{
			while (true) {
				try {
					ZipEntry entry =  zip_in.getNextEntry();
					if (entry != null) {
						ByteArrayInputStream bais = new ByteArrayInputStream(
								readBytes(zip_in, null));
						inputs.put(entry.getName(), bais);
					} else {
						break;
					}
				} catch (Throwable ex) {
					ex.printStackTrace();
					break;
				}
			}
		}finally{
			try {
				zip_in.close();
			} catch (IOException e) {}
		}
		
		return inputs;
	}
	
	final static String ALGORITHM = "PBEWithMD5AndDES";

	static public byte[] readBytes(ZipInputStream is) throws Exception {
		return readBytes(is, null);
	}
	
	static public byte[] readBytes(ZipInputStream is, String pswd) throws Exception {
		ByteArrayOutputStream data = new ByteArrayOutputStream(8192);
		if (pswd == null || pswd.isEmpty()) {
			byte[] buffer = new byte[8192];
			int bytesRead = 0;
		    while ((bytesRead = is.read(buffer)) != -1) {
		    	data.write(buffer, 0, bytesRead);
		    }
		} else {
			PBEKeySpec keySpec = new PBEKeySpec(pswd.toCharArray());
		    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		    SecretKey passwordKey = keyFactory.generateSecret(keySpec);
		    byte[] salt = new byte[8];
		    is.read(salt);
		    int iterations = 100;
		    PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterations);
		    Cipher cipher = Cipher.getInstance(ALGORITHM);
		    cipher.init(Cipher.DECRYPT_MODE, passwordKey, parameterSpec);
		    byte[] input = new byte[64];
		    int bytesRead;
		    while ((bytesRead = is.read(input)) != -1) {
		    	byte[] output = cipher.update(input, 0, bytesRead);
		    	if (output != null){
		    		data.write(output);
		    	}
		    }
		    byte[] output = cipher.doFinal();
		    if (output != null){
		    	data.write(output);
		    }
		    data.flush();
		}
	
		return data.toByteArray();
	}
	
	
	
	static public void zip(
			File src, 
			File out, 
			FileFilters pattern, 
			boolean verbos, boolean stream) throws Exception
	{
		src = src.getCanonicalFile();
		out = out.getCanonicalFile();
		if (stream) {
			new ZipTask().zipFilesStream(src, out, pattern, verbos);
		} else {
			new ZipTask().zipFiles(src, out, pattern, verbos);
		}
	}
	
	public static class Compressor
	{
		public int BufferSize = 10*1024*1024;
		
		public int compress(byte[] data, int off, int len, OutputStream os) throws IOException {
			int ret = 0;
			Deflater compresser = new Deflater();
			try {
				compresser.reset();
				compresser.setInput(data, off, len);
				compresser.finish();
				byte[] buf = new byte[BufferSize];
				while (!compresser.finished()) {
					int rd = compresser.deflate(buf);
					os.write(buf, 0, rd);
					ret += rd;
				}
			} finally {
				compresser.end();
			}
			return ret;
		}
		public int compress(InputStream in, OutputStream os) throws IOException {
			int ret = 0;
			DeflaterInputStream compresser = new DeflaterInputStream(in);
			try {
				byte[] buf = new byte[BufferSize];
				while (true) {
					int read = compresser.read(buf);
					if (read > 0) {
						ret += read;
						os.write(buf, 0, read);
					} else {
						break;
					}
				}
			} finally {
				compresser.close();
			}
			return ret;
		}
		public int compressGZ(InputStream in, OutputStream os) throws IOException {
			int ret = 0;
			GZIPOutputStream out = new GZIPOutputStream(os);
			try {
				byte[] buf = new byte[BufferSize];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
			} finally {
				out.flush();
				out.close();
			}
			return ret;
		}
		
		public int decompress(byte[] data, int off, int len, OutputStream os) throws IOException, DataFormatException {
			int ret = 0;
			Inflater decompresser = new Inflater();
			try {
				decompresser.reset();
				decompresser.setInput(data, off, len);
				byte[] buf = new byte[BufferSize];
				while (!decompresser.finished()) {
					int rd = decompresser.inflate(buf);
					os.write(buf, 0, rd);
					ret += rd;
				}
			} finally {
				decompresser.end();
			}
			return ret;
		}

	}
	
	
	static public void main(String[] args)
	{
		try
		{
			HashMap<String, String> commands = new HashMap<String, String>(args.length);
			for (String cmd : args) {
				if (cmd.startsWith("-")) {
					if (cmd.contains(":")) {
						String[] kv = CUtil.splitString(cmd, ":", 2, true);
						commands.put(kv[0], kv[1]);
					} else {
						commands.put(cmd.trim(), cmd.trim());
					}
				}
			}
			
			if (args[0].equals("B")) 
			{
				File src = new File(args[args.length-2]).getCanonicalFile();
				String out = args[args.length-3].trim();
				FileFilters pattern = new FileFilters(args[args.length-1]);
				
				FileMake history = commands.containsKey("-h")?new FileMake(commands.get("-h")):null;
				boolean verbos = commands.containsKey("-verbos");
				
				new ZipTask().zipAllFileToStream(src, out, pattern, history, verbos);
			} 
			else if (args[0].equals("G")) 
			{
				File src = new File(args[args.length-1]).getCanonicalFile();
				File out = new File(args[args.length-2]).getCanonicalFile();
				new ZipTask().compressFileToGZip(src, out);
			} 
			else if (args[0].equals("D")) 
			{
				File src = new File(args[args.length-1]).getCanonicalFile();
				File out = new File(args[args.length-2]).getCanonicalFile();
				new ZipTask().compressFileToZip(src, out);
			} 
			else if (args[0].equals("BC")) 
			{
				File src = new File(args[args.length-2]).getCanonicalFile();
				String out = args[args.length-3].trim();
				FileFilters pattern = new FileFilters(args[args.length-1]);
				
				FileMake history = commands.containsKey("-h")?new FileMake(commands.get("-h")):null;
				boolean verbos = commands.containsKey("-verbos");
				
				new ZipTask().compressAllFileToStream(src, out, pattern, history, verbos);
			} 
			else if (args[0].equals("A")) 
			{
				File src = new File(args[args.length-2]).getCanonicalFile();
				File out = new File(args[args.length-3]).getCanonicalFile();
				FileFilters pattern = new FileFilters(args[args.length-1]);
				boolean verbos = commands.containsKey("-verbos");
				if (commands.containsKey("-h")) {
					FileMake history = new FileMake(commands.get("-h"));
					new ZipTask().zipFileToStream(src, out, pattern, history, verbos);
				} else if (commands.containsKey("-os")) {
					new ZipTask().zipFilesStream(src, out, pattern, verbos);
				} else {
					new ZipTask().zipFiles(src, out, pattern, verbos);
				}
			} 
			else
			{
				printUsage();
			}
		} 
		catch (Exception err) {
			err.printStackTrace();
			printUsage();
		}
	}
	
	static private void printUsage()
	{
		System.out.println(usage());
	}
	
	public static String usage()
	{
		String usage = 
				"A [-开关] <输出文件> <输入文件> [文件匹配正则表达式]\n" +
				"	整体压缩，通常将很多文件压缩成一个文件。" +
				"B [-开关] <输出后缀> <输入文件> [文件匹配正则表达式]\n" +
				"	批量单个压缩，将每个文件压缩成ZIP。\n" +
				"G [-开关] <输出文件> <输入文件> \n" +
				"	单个压缩成GZIP格式。\n" +
				"D [-开关] <输出文件> <输入文件> \n" +
				"	单个压缩成Zip流格式。\n" +
				"BC [-开关] <输出后缀> <输入文件>\n" +
				"	批量单个压缩，将每个文件压缩成MG3Z格式。\n" +
				"	MG3Z流格式\n" +
				"		u32 : head(MG3Z)\n" +
				"		u32 : src_size\n" +
				"		bin : data\n" +
				"	[开关]\n" +
				"	-verbos       开关：输出详细信息\n" +
				"	-os           开关：流的形式输出\n" +
				"	-h:<compare>  开关：FileMake\n" + FileMake.usage("\t\t") + "\n" +
				"	[文件匹配正则表达式]\n" + 
				FileFilters.usage("\t");
		return usage;
	}
	
	static public class ZipTask
	{
		final public int BufferSize = 10*1024*1024;
		
		public void zipFileToStream(File src, File out, FileFilters pattern, FileMake history, boolean verbos) throws Exception
		{
			src = src.getCanonicalFile();
			out = out.getCanonicalFile();
			
			if (!src.isHidden() && src.isFile() && pattern.acceptFile(src) && history.isNewer(src)) 
			{
				String root = src.getParentFile().getCanonicalPath();
				out.createNewFile();
				FileOutputStream baos = new FileOutputStream(out);
				ZipOutputStream zos = new ZipOutputStream(baos);
				try {
					pushFileEntry(src, root, zos, verbos);
				} finally {
					baos.flush();
					zos.close();
					baos.close();
				}
			}
			
		}
		
		public void compressAllFileToStream(
				File dir,
				String out_suffix, 
				FileFilters pattern, 
				FileMake history, 
				boolean verbos) throws Exception
		{
			dir = dir.getCanonicalFile();
			
			if (!dir.isHidden() && dir.isDirectory()) 
			{
				long head = CUtil.string4CharToIntLittle("MG3Z");
				Compressor compressor = new Compressor();
				ArrayList<File> files = CFile.listAllChildren(dir, pattern);
				if (history != null) {
					files = history.getNewerList(files);
				}
				for (File sub : files) 
				{					
					String root = sub.getParentFile().getCanonicalPath();
					File out = new File(root, CFile.getNoneExtension(sub)+out_suffix);
					out.createNewFile();
					FileOutputStream zos = new FileOutputStream(out);
					FileInputStream fis = new FileInputStream(sub);
					try {
						LittleIOSerialize.putUnsignedInt(zos, head);
						LittleIOSerialize.putUnsignedInt(zos, sub.length());
						compressor.compress(fis, zos);
					} finally {
						zos.flush();
						zos.close();
					}
					System.out.println(
							"COMPRESS: " + sub.getPath() + " -> " + out_suffix + 
							"("+(100*out.length()/sub.length())+"%)");
				}
				
			}
		}
		
		public void compressFileToGZip(File sub, File out)
				throws Exception {
			sub = sub.getCanonicalFile();
			Compressor compressor = new Compressor();
			if (!out.exists()){
				out.createNewFile();
			}
			FileOutputStream zos = new FileOutputStream(out);
			FileInputStream fis = new FileInputStream(sub);
			try {
				compressor.compressGZ(fis, zos);
			} finally {
				zos.flush();
				zos.close();
			}
			System.out.println("GZIP: " + sub.getPath() + " -> "
					+ out.getPath() + "(" + (100 * out.length() / sub.length())
					+ "%)");
		}
		
		public void compressFileToZip(File sub, File out) throws Exception {
			sub = sub.getCanonicalFile();
			Compressor compressor = new Compressor();
			if (!out.exists()) {
				out.createNewFile();
			}
			FileOutputStream zos = new FileOutputStream(out);
			FileInputStream fis = new FileInputStream(sub);
			try {
				compressor.compress(fis, zos);
			} finally {
				zos.flush();
				zos.close();
			}
			System.out.println("Deflater: " + sub.getPath() + " -> "
					+ out.getPath() + "(" + (100 * out.length() / sub.length())
					+ "%)");
		}		
		
		public void zipAllFileToStream(
				File dir,
				String out_suffix, 
				FileFilters pattern, 
				FileMake history, 
				boolean verbos) throws Exception
		{
			dir = dir.getCanonicalFile();
			
			if (!dir.isHidden() && dir.isDirectory()) 
			{
				ArrayList<File> files = CFile.listAllChildren(dir, pattern);
				if (history != null) {
					files = history.getNewerList(files);
				}
				for (File sub : files) 
				{					
					String root = sub.getParentFile().getCanonicalPath();
					File out = new File(root, CFile.getNoneExtension(sub)+out_suffix);
					out.createNewFile();
					ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(out));
					try {
						pushFileEntry(sub, root, zos, verbos);
					} finally {
						zos.flush();
						zos.close();
					}
					System.out.println(
							"ZIP: " + sub.getPath() + " -> " + out_suffix + 
							"("+(100*out.length()/sub.length())+"%)");
				}
				
			}
		}
		
		/**
		 * 将src所有符合标准的文件都压缩到out
		 * @param src
		 * @param out
		 * @param parttern
		 * @throws Exception
		 */
		public void zipFiles(File src, File out, FileFilters pattern, boolean verbos) throws Exception
		{
			src = src.getCanonicalFile();
			out = out.getCanonicalFile();
			
			if (!src.isHidden()) {
				String root = "";
				if (src.isDirectory()) {
					root = src.getCanonicalPath();
				} else if (src.isFile()) {
					root = src.getParentFile().getCanonicalPath();
				}
				LinkedHashMap<String, byte[]> entrys = new LinkedHashMap<String, byte[]>();
				zipFiles(src, pattern, root, entrys, verbos);
				if (!entrys.isEmpty()) {				
					out.createNewFile();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ZipOutputStream zos = new ZipOutputStream(baos);
					for (Entry<String, byte[]> e : entrys.entrySet()) {
						ZipEntry ze = new ZipEntry(e.getKey());
						ze.setTime(0);
						zos.putNextEntry(ze);
						zos.write(e.getValue());
					}
					zos.close();
					CFile.writeData(out, baos.toByteArray());
				}
			}
		}

		/**
		 * 将src所有符合标准的文件都压缩到out
		 * @param src
		 * @param out
		 * @param parttern
		 * @throws Exception
		 */
		public void zipFilesStream(File src, File out, FileFilters pattern, boolean verbos) throws Exception
		{
			src = src.getCanonicalFile();
			out = out.getCanonicalFile();
			
			if (!src.isHidden()) {
				String root = "";
				if (src.isDirectory()) {
					root = src.getCanonicalPath();
				} else if (src.isFile()) {
					root = src.getParentFile().getCanonicalPath();
				}
				out.createNewFile();
				FileOutputStream baos = new FileOutputStream(out);
				ZipOutputStream zos = new ZipOutputStream(baos);
				zipFiles(src, pattern, root, zos, verbos);
				baos.flush();
				zos.close();
			}
		}
		
		public void zipFiles(File src, FileFilters pattern, String root, LinkedHashMap<String, byte[]> entrys, boolean verbos) throws Exception
		{
			if (!src.isHidden()) {
				if (src.isFile()) {
					if (pattern == null || pattern.acceptFile(src)) {
						pushFileEntry(src, root, entrys, verbos);
					}
				} else if (src.isDirectory()) {
					for (File sub : src.listFiles()) {
						zipFiles(sub, pattern, root, entrys, verbos);
					}
				}
			}
		}
		
		public void zipFiles(File src, FileFilters pattern, String root, ZipOutputStream zos, boolean verbos) throws Exception
		{
			if (!src.isHidden()) {
				if (src.isFile()) {
					if (pattern == null || pattern.acceptFile(src)) {
						pushFileEntry(src, root, zos, verbos);
					}
				} else if (src.isDirectory()) {
					for (File sub : src.listFiles()) {
						zipFiles(sub, pattern, root, zos, verbos);
					}
				}
			}
		}
		
		private void pushFileEntry(File src, String root, ZipOutputStream zos, boolean verbos) throws Exception
		{
			String name = src.getCanonicalPath();
			name = CUtil.replaceString(name, root, "", 1);
			name = name.replaceAll("\\\\", "/");
			while (name.startsWith("/")) {
				name = name.substring(1);
			}
			ZipEntry ze = new ZipEntry(name);
			ze.setTime(0);
			zos.putNextEntry(ze);
			FileInputStream fis = new FileInputStream(src);
			byte[] buffer = new byte[BufferSize];
			while (fis.available() > 0) {
				int readed = fis.read(buffer);
				zos.write(buffer, 0, readed);
			}
			try {
				fis.close();
			} catch (Exception err) {}
			if (verbos) {
				System.out.println("put : " + CUtil.snapStringRightSize(src.length() + "(bytes)", 22, ' ') + " "+  name);
			}
		}
		
		private void pushFileEntry(File src, String root, LinkedHashMap<String, byte[]> entrys, boolean verbos) throws Exception
		{
			String name = src.getCanonicalPath();
			name = CUtil.replaceString(name, root, "", 1);
			name = name.replaceAll("\\\\", "/");
			while (name.startsWith("/")) {
				name = name.substring(1);
			}
			byte[] data = CFile.readData(src);
			entrys.put(name, data);
			if (verbos) {
				System.out.println("put : " + CUtil.snapStringRightSize(data.length + "(bytes)", 22, ' ') + " "+  name);
			}
		}
		
	}
	
	
	public static class UnzipTask
	{
		/**
		 * @param is, 该方法不会关闭流
		 * @return
		 */
		public TreeMap<String, byte[]> unzipAll(InputStream is, String pswd) {
			try {
				TreeMap<String, byte[]> ret = new TreeMap<String, byte[]>();
				ZipInputStream zip_in = new ZipInputStream(is);
				for (ZipEntry e = zip_in.getNextEntry(); e != null; e = zip_in.getNextEntry()) {
					byte[] v = ZipUtil.readBytes(zip_in, pswd);
					ret.put(e.getName(), v);
				}
				return ret;
			} catch (Exception err) {
				err.printStackTrace();
			}
			return null;
		}
	}
	
	

	public static byte[] compress(byte[] data) 
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length);
		Deflater compresser = new Deflater();
		try {
			compresser.reset();
			compresser.setInput(data);
			compresser.finish();
			byte[] buf = new byte[1024];
			while (!compresser.finished()) {
				int i = compresser.deflate(buf);
				baos.write(buf, 0, i);
			}
		} finally {
			compresser.end();
		}
		return baos.toByteArray();
	}

	public static byte[] uncompress(byte[] data) throws DataFormatException 
	{
		Inflater decompresser = new Inflater();
		ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length<<2);
		try {
			decompresser.reset();
			decompresser.setInput(data);
			byte[] buf = new byte[1024];
			while (!decompresser.finished()) {
				int i = decompresser.inflate(buf);
				baos.write(buf, 0, i);
			}
		} finally {
			decompresser.end();
		}
		return baos.toByteArray();
	}
    
}
