package com.cell.net.io;

public class HashFunction 
{
	// ELF Hash
	public static int elfHash(String str)
	{
		long hash = 0;
		long x = 0;
		char[] chars = str.toCharArray();
		for (char ch : chars) {
			hash = (hash << 4) + ch;
			if ((x = hash & 0xF0000000L) != 0) {
				hash ^= (x >> 24);
				hash &= ~x;
			}
		}
		return (int) (hash & 0x7FFFFFFF);
	}
	
	
	// BKDR Hash Function
	public static int bkdrHash(String str) 
	{
		long seed = 131; // 31 131 1313 13131 131313 etc..
		long hash = 0;
		char[] chars = str.toCharArray();
		for (char ch : chars) {
			hash = hash * seed + ch;
		}
		return (int) (hash & 0x7FFFFFFF);
	}
	
}
