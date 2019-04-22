package com.cell.filesystem;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

public class MurmurHash implements Hash 
{
	@Override
	public long hash(byte[] data) 
	{
		return MurmurHash64B(data, data.length, DEFAULT_SEED);
	}
	
	public static int DEFAULT_SEED = 0x00EE6B27EB;

	public static long MurmurHash64B (byte[] data)
	{
		return MurmurHash64B (data, data.length, DEFAULT_SEED);
	}
	public static long MurmurHash64B (byte[] data, int length)
	{
		return MurmurHash64B (data, length, DEFAULT_SEED);
	}
	public static long MurmurHash64B (byte[] data, int length, int seed)
	{
		final long m = 0xc6a4a7935bd1e995L;
		final int r = 47;

		long h = (seed&0xffffffffl)^(length*m);

		int length8 = length/8;

		for (int i=0; i<length8; i++) {
			final int i8 = i*8;
			long k = 0;
			k += (((long)data[i8+0]&0xff));
			k += (((long)data[i8+1]&0xff)<<8);
			k += (((long)data[i8+2]&0xff)<<16) ;
			k += (((long)data[i8+3]&0xff)<<24);
			k += (((long)data[i8+4]&0xff)<<32) ;
			k += (((long)data[i8+5]&0xff)<<40);
			k += (((long)data[i8+6]&0xff)<<48) ;
			k += (((long)data[i8+7]&0xff)<<56);
			
			k *= m;
			k ^= k >>> r;
			k *= m;
			
			h ^= k;
			h *= m; 
		}
		
		switch (length%8) {
		case 7: h ^= (long)(data[(length&~7)+6]&0xff) << 48;
		case 6: h ^= (long)(data[(length&~7)+5]&0xff) << 40;
		case 5: h ^= (long)(data[(length&~7)+4]&0xff) << 32;
		case 4: h ^= (long)(data[(length&~7)+3]&0xff) << 24;
		case 3: h ^= (long)(data[(length&~7)+2]&0xff) << 16;
		case 2: h ^= (long)(data[(length&~7)+1]&0xff) << 8;
		case 1: h ^= (long)(data[length&~7]&0xff);
		        h *= m;
		};
	 
		h ^= h >>> r;
		h *= m;
		h ^= h >>> r;

		return h;
	}
	
	///
	public static class HashTest
	{
		Random random = new Random();
		
		HashMap<Long, String> table = new HashMap<Long, String>();
		
		public boolean putHash(String test_str) throws Exception
		{
			long hash = MurmurHash64B(test_str.getBytes("UTF-8"), DEFAULT_SEED);
			String hash_str = Long.toHexString((0x100000000L | hash)).substring(1);
			System.out.println(table.size() + " : " + hash_str + " : " + test_str);
			if (table.containsKey(hash)) {
				String old_str = table.get(hash);
				System.err.println(
						"Conflict hash : " + old_str + " \n" +
						"                " + test_str);
				return false;
			} else {
				table.put(hash, test_str);
				return true;
			}
		}
		
		public void testNumberHash(int count) throws Exception
		{
			for (int i=0; i<count; i++) {
				String test_str = i + " - " + random.nextLong() + " - " + random.nextDouble();
				if (putHash(test_str)) {
					throw new Exception("Conflict hash");
				}
			}
		}
		
		public void testFilesHash(File file) throws Exception {
			if (file.isDirectory()) {
				for (File sub : file.listFiles()) {
					testFilesHash(sub);
				}
			} else {
				String test_str = file.getAbsolutePath();
				if (putHash(test_str)) {
					throw new Exception("Conflict hash");
				}
			}
		}

	}
	
	public static void main(String[] args) throws Exception
	{
//		HashTest table = new HashTest();
//		table.putHash("box.jpg");
//		table.putHash("box.png");
//		testFilesHash(new File("F:/"), table);
		System.out.println(MurmurHash64B("/uiedit/res/scrollv-back.png".getBytes()));
	}

}
