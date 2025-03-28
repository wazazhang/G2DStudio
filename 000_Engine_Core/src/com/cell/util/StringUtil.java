package com.cell.util;






public class StringUtil
{	
	public static Double[] getDoubleObjArray(String str, String delimiter)
	{
		if ( (str==null) || str.isEmpty() )
			return null;

		String[] strs = str.split(delimiter);

		Double[] ret = new Double[strs.length];
		int i = 0;
		for ( String s : strs )
			ret[i++] = Double.parseDouble(s);

		return ret;
	}	
	
	public static double[] getDoubleArray(String str, String delimiter)
	{
		if ( (str==null) || str.isEmpty() )
			return null;

		String[] strs = str.split(delimiter);

		double[] ret = new double[strs.length];
		int i = 0;
		for ( String s : strs )
			ret[i++] = Double.parseDouble(s);

		return ret;
	}	
	
	public static Float[] getFloatObjArray(String str, String delimiter)
	{
		if ( (str==null) || str.isEmpty() )
			return null;

		String[] strs = str.split(delimiter);

		Float[] ret = new Float[strs.length];
		int i = 0;
		for ( String s : strs )
			ret[i++] = Float.parseFloat(s);

		return ret;
	}	
	
	public static float[] getFloatArray(String str, String delimiter)
	{
		if ( (str==null) || str.isEmpty() )
			return null;

		String[] strs = str.split(delimiter);

		float[] ret = new float[strs.length];
		int i = 0;
		for ( String s : strs )
			ret[i++] = Float.parseFloat(s);

		return ret;
	}	
	
	public static Long[] getLongObjArray(String str, String delimiter)
	{
		if ( (str==null) || str.isEmpty() )
			return null;

		String[] strs = str.split(delimiter);

		Long[] ret = new Long[strs.length];
		int i = 0;
		for ( String s : strs )
			ret[i++] = Long.parseLong(s);

		return ret;
	}	
	
	public static long[] getLongArray(String str, String delimiter)
	{
		if ( (str==null) || str.isEmpty() )
			return null;

		String[] strs = str.split(delimiter);

		long[] ret = new long[strs.length];
		int i = 0;
		for ( String s : strs )
			ret[i++] = Long.parseLong(s);

		return ret;
	}
	
	public static Integer[] getIntegerObjArray(String str, String delimiter)
	{
		if ( (str==null) || str.isEmpty() )
			return null;

		String[] strs = str.split(delimiter);

		Integer[] ret = new Integer[strs.length];
		int i = 0;
		for ( String s : strs )
			ret[i++] = Integer.parseInt(s);

		return ret;
	}	
	
	public static int[] getIntegerArray(String str, String delimiter)
	{
		if ( (str==null) || str.isEmpty() )
			return null;

		String[] strs = str.split(delimiter);

		int[] ret = new int[strs.length];
		int i = 0;
		for ( String s : strs )
			ret[i++] = Integer.parseInt(s);

		return ret;
	}
	
	public static Short[] getShortObjArray(String str, String delimiter)
	{
		if ( (str==null) || str.isEmpty() )
			return null;

		String[] strs = str.split(delimiter);

		Short[] ret = new Short[strs.length];
		int i = 0;
		for ( String s : strs )
			ret[i++] = Short.parseShort(s);

		return ret;
	}	
	
	public static short[] getShortArray(String str, String delimiter)
	{
		if ( (str==null) || str.isEmpty() )
			return null;

		String[] strs = str.split(delimiter);

		short[] ret = new short[strs.length];
		int i = 0;
		for ( String s : strs )
			ret[i++] = Short.parseShort(s);

		return ret;
	}
	
	public static Byte[] getByteObjArray(String str, String delimiter)
	{
		if ( (str==null) || str.isEmpty() )
			return null;

		String[] strs = str.split(delimiter);

		Byte[] ret = new Byte[strs.length];
		int i = 0;
		for ( String s : strs )
			ret[i++] = Byte.parseByte(s);

		return ret;
	}	
	
	public static byte[] getByteArray(String str, String delimiter)
	{
		if ( (str==null) || str.isEmpty() )
			return null;

		String[] strs = str.split(delimiter);

		byte[] ret = new byte[strs.length];
		int i = 0;
		for ( String s : strs )
			ret[i++] = Byte.parseByte(s);

		return ret;
	}	
	
	

	/**
	 * 得到范围内的内容
	 * @param src
	 * @param begin
	 * @param end
	 * @param include 是否包含 begin,end
	 * @return
	 */
	public static String getStringRange(String src, String begin, String end, boolean include)
	{
		int si = src.indexOf(begin);
		int ei = src.lastIndexOf(end);
		if (si >= 0 && ei > 0) {
			if (include) {
				return src.substring(si, ei + end.length());
			} else {
				return src.substring(si + begin.length(), ei);
			}
		} else {
			return null;
		}
	}
	
	/**
	 * 删除范围内的内容
	 * @param src
	 * @param begin
	 * @param end
	 * @param include 是否包含 begin,end
	 * @return
	 */
	public static String delStringRange(String src, String begin, String end, boolean include)
	{
		int si = src.indexOf(begin);
		int ei = src.lastIndexOf(end);
		if (si >= 0 && ei > 0) {
			if (include) {
				return src.substring(0, si) + src.substring(ei + end.length());
			} else {
				return src.substring(0, si + begin.length()) + src.substring(ei);
			}
		} else {
			return src;
		}
	}
	
	/**
	 * 替换范围内的内容
	 * @param src
	 * @param begin
	 * @param end
	 * @param include 是否包含 begin,end
	 * @return
	 */
	public static String replaceStringRange(
			String src,
			String begin, 
			String end, 
			boolean include,
			String replace)
	{
		int si = src.indexOf(begin);
		while (si >= 0) {
			int ei = src.indexOf(end, si + begin.length());
			if (include) {
				src = src.substring(0, si) + replace + src.substring(ei + end.length());
			} else {
				src = src.substring(0, si + begin.length()) + replace + src.substring(ei);
			}
			si = src.indexOf(begin, si + 1);
		}
		return src;
	}
	
	/**
	 * 删除begin,end
	 * @param src
	 * @param begin
	 * @param end
	 * @return
	 */
	public static String cleanStringRangeKV(String src, String begin, String end)
	{
		int si = src.indexOf(begin);
		int ei = src.lastIndexOf(end);
		if (si >= 0 && ei > 0) {
			return
			src.substring(0, si) + 
			src.substring(si + begin.length(), ei) + 
			src.substring(ei + end.length());
		} else {
			return src;
		}
	}
	
	
	
}; // class


