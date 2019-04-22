package com.cell.util.zip;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.cell.CUtil;
import com.cell.io.CFile;
import com.cell.io.LittleIODeserialize;
import com.cell.io.LittleIOSerialize;
import com.cell.util.FileFilters;
import com.cell.util.zip.ZipUtil.Compressor;

/**<pre>
static class ZBF
{
	final int head = CUtil.string4CharToIntLittle("MFZB");
	s64 total_size;
	s32 block_size;
	ZBT[] trunks = new ZBT[block_count]; 
}
static class ZBT
{
	s32 uncompressed_size;
	s32 compressed_size;
	byte[] zip_data;
}
*/
public class ZipBigFile
{
	final Compressor compressor = new Compressor();
	
	public ZipBigFile() {}
	
	public void zip(final File src, final File out, final int block_size) throws Exception
	{
		System.out.println("compress big zip file : " + 
				src.getPath() + " - " + 
				src.length() + "(bytes)");
		final long total_size = src.length();
		byte[] block_buffer = new byte[block_size];
		FileInputStream  fis = new FileInputStream(src);
		FileOutputStream fos = new FileOutputStream(out);
		ByteArrayOutputStream baos = new ByteArrayOutputStream(block_size);
		try {
			LittleIOSerialize.putInt	(fos, CUtil.string4CharToIntLittle("MFZB"));
			LittleIOSerialize.putLong	(fos, total_size);
			LittleIOSerialize.putInt	(fos, block_size);
			for (long readed = 0; readed < total_size; ) {
				final int rd = CFile.readData(fis, block_buffer, 0, block_size);
				if (rd > 0) {
					readed += rd;
					baos.reset();
					compressor.compress(block_buffer, 0, rd, baos);
					byte[] zip_data = baos.toByteArray();
					int uncompressed_size = rd;
					int compressed_size = zip_data.length;
					LittleIOSerialize.putInt(fos, uncompressed_size);
					LittleIOSerialize.putInt(fos, compressed_size);
					fos.write(zip_data);
				}
				else if (rd < 0) {
					throw new Exception("ends of src file");
				}
			}
			fos.flush();
		} finally {
			fis.close();
			fos.close();
		}
		System.out.println("	done : " + 
				out.getPath() + " - " + 
				out.length() + "(bytes)");
	}
	
	public void unzip(final File src, final File out) throws Exception
	{
		System.out.println("uncompress big zip file : " + 
				src.getPath() + " - " + 
				src.length() + "(bytes)");
		
		FileInputStream  fis = new FileInputStream(src);
		FileOutputStream fos = new FileOutputStream(out);
		try {
			LittleIODeserialize.getInt(fis);
			final long total_size = LittleIODeserialize.getLong(fis);
			final int  block_size = LittleIODeserialize.getInt(fis);
			byte[] comp_buffer = new byte[block_size];
			ByteArrayOutputStream baos = new ByteArrayOutputStream(block_size);
			for (long readed = 0; readed < total_size; ) {
				baos.reset();
				int uncompressed_size = LittleIODeserialize.getInt(fis);
				int compressed_size   = LittleIODeserialize.getInt(fis);
				CFile.readData(fis, comp_buffer, 0, compressed_size);
				compressor.decompress(comp_buffer, 0, compressed_size, baos);
				byte[] unzip_data = baos.toByteArray();
				if (uncompressed_size == unzip_data.length) {
					fos.write(unzip_data);
				} else {
					throw new Exception("bad of src file");
				}
				readed += uncompressed_size;
			}
			fos.flush();
		} finally {
			fis.close();
			fos.close();
		}
		System.out.println("	done : " + 
				out.getPath() + " - " + 
				out.length() + "(bytes)");
	}

	public static void main(String[] args) throws Exception
	{
		if (args.length == 5) {
			String cmd = args[0].toLowerCase();
			File src = new File(args[1]);
			File dst = new File(args[2]);
			FileFilters pattern = new FileFilters(args[3]);
			int block_size_m = Integer.parseInt(args[4]);
			if (pattern.acceptFile(src)) {
				if (cmd.equals("z")) {
					new ZipBigFile().zip(src, dst, block_size_m*1024*1024);
				} else if (cmd.equals("u")) {
					new ZipBigFile().unzip(src, dst);
				}
			}
		}
		
	}
}
