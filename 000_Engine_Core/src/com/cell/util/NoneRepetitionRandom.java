package com.cell.util;

import java.util.Random;

public class NoneRepetitionRandom 
{
	final private int min;
	final private int max;
	final private int count;
	final private int[] rdlist;
	private int randomIndex = 0;
	private Random random;
	
	public NoneRepetitionRandom(int min, int max) 
	{
		this(min, max, System.currentTimeMillis());
	}
	
	public NoneRepetitionRandom(int min, int max, long seed) 
	{
		this.random = new Random(seed);
		this.min = Math.min(max, min);
		this.max = Math.max(max, min);
		this.count = this.max - this.min;
		this.rdlist = new int[count];
		for (int i=0; i<rdlist.length; i++) {
			rdlist[i] = min + i;
		}
		reindex();
	}
	
	private void reindex() 
	{
		for (int si=0; si<rdlist.length; si++) {
			int di = Math.abs(random.nextInt()%rdlist.length);
			int tp = rdlist[si];
			rdlist[si] = rdlist[di];
			rdlist[di] = tp;
		}
	}
	
	synchronized public int nextInt() 
	{
		int i = randomIndex%rdlist.length;
		int ret = rdlist[i];
		randomIndex++;
		if (randomIndex == rdlist.length) {
			reindex();
		}
		return ret;
	}
	
}
