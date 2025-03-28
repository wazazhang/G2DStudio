
package com.cell.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.cell.CObject;
import com.cell.CUtil;


/**
 * BIG_ENDIAN
 * @author WAZA
 *
 */
public class BigIOSerialize extends IOutput
{
//	protected static byte[] lock_write = new byte[]{};
	
	public static void putString(OutputStream ostream, String string) throws IOException
	{
//		synchronized (lock_write) 
		{
			byte[] stringBytes = string.getBytes(CObject.getEncoding());
			putUnsignedShort(ostream, stringBytes.length);
			ostream.write(stringBytes);
		}
		
	}
	
	public static void putString(OutputStream ostream, String string, String charset) throws IOException
	{
//		synchronized (lock_write)
		{
			byte[] stringBytes = string.getBytes(charset);
			putUnsignedShort(ostream, stringBytes.length);
			ostream.write(stringBytes);
		}
	}
	
	public static void putByte(OutputStream ostream, byte value) throws IOException
	{	
//		synchronized (lock_write) 
		{
			ostream.write((int)value);
		}
	}
	
	public static void putUnsignedByte(OutputStream ostream, short value) throws IOException
	{
//		synchronized (lock_write)
		{
			ostream.write(value & 0x000000FF);
		}
	}
	
	public static void putBoolean(OutputStream ostream, boolean value) throws IOException
	{
//		synchronized (lock_write)
		{
			ostream.write(value? 1 : 0);
		}
	}
	
	public static void putShort(OutputStream ostream, short value) throws IOException
	{
//		synchronized (lock_write) 
		{
			ostream.write((value & 0x0000FF00) >> 8);
			ostream.write((value & 0x000000FF));
		}
	}
	
	public static void putUnsignedShort(OutputStream ostream, int value) throws IOException
	{
//		synchronized (lock_write) 
		{
			ostream.write((value & 0x0000FF00) >> 8);
			ostream.write((value & 0x000000FF));
		}
	}
	
	public static void putInt(OutputStream ostream, int value) throws IOException
	{
//		synchronized (lock_write) 
		{
			ostream.write((value & 0xFF000000) >> 24);
			ostream.write((value & 0x00FF0000) >> 16);
			ostream.write((value & 0x0000FF00) >> 8);
			ostream.write((value & 0x000000FF));
		}
	}
	
	public static void putUnsignedInt(OutputStream ostream, long value) throws IOException
	{
//		synchronized (lock_write) 
		{
			ostream.write((int)((value & 0x00000000FF000000) >> 24));
			ostream.write((int)((value & 0x0000000000FF0000) >> 16));
			ostream.write((int)((value & 0x000000000000FF00) >> 8));
			ostream.write((int)(value & 0x00000000000000FF));
		}
	}
	
	public static void putLong(OutputStream ostream, long value) throws IOException
	{
//		synchronized (lock_write) 
		{
			ostream.write((int)((value & 0xFF00000000000000L) >> 56));
			ostream.write((int)((value & 0x00FF000000000000L) >> 48));
			ostream.write((int)((value & 0x0000FF0000000000L) >> 40));
			ostream.write((int)((value & 0x000000FF00000000L) >> 32));
			ostream.write((int)((value & 0x00000000FF000000) >> 24));
			ostream.write((int)((value & 0x0000000000FF0000) >> 16));
			ostream.write((int)((value & 0x000000000000FF00) >> 8));
			ostream.write((int)((value & 0x00000000000000FF)));
		}
	}
	
	public static void putFloat(OutputStream ostream, float value) throws IOException
	{
		putInt(ostream, Float.floatToIntBits(value));
	}
	
	public static void putDouble(OutputStream ostream, double value) throws IOException
	{
		putDouble(ostream, Double.doubleToLongBits(value));
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	// put array
	
	public static void putStrings(OutputStream ostream, String[] value) throws IOException
	{
//		synchronized (lock_write) 
		{
			if (value != null)
			{
				BigIOSerialize.putUnsignedInt(ostream, value.length);
				for ( int i=0; i<value.length; ++i )
					BigIOSerialize.putString(ostream, value[i]);
			}
			else
			{
				BigIOSerialize.putUnsignedInt(ostream, 0);
			}
		}
	}
	
	public static void putStrings(OutputStream ostream, String[] value, String charset) throws IOException
	{
//		synchronized (lock_write) 
		{
			if (value != null)
			{
				BigIOSerialize.putUnsignedInt(ostream, value.length);
				for ( int i=0; i<value.length; ++i )
					BigIOSerialize.putString(ostream, value[i], charset);
			}
			else
			{
				BigIOSerialize.putUnsignedInt(ostream, 0);
			}
		}
	}
	
	public static void putBooleans(OutputStream ostream, boolean[] value) throws IOException
	{
//		synchronized (lock_write) 
		{
			if (value != null)
			{
				BigIOSerialize.putUnsignedInt(ostream, value.length);
				for ( int i=0; i<value.length; ++i )
					BigIOSerialize.putBoolean(ostream, value[i]);
			}
			else
			{
				BigIOSerialize.putUnsignedInt(ostream, 0);
			}
		}
	}	
	
	public static void putBytes(OutputStream ostream, byte[] value) throws IOException
	{
//		synchronized (lock_write)
		{
			if (value != null)
			{
				BigIOSerialize.putUnsignedInt(ostream, value.length);
				for ( int i=0; i<value.length; ++i )
					BigIOSerialize.putByte(ostream, value[i]);
			}
			else
			{
				BigIOSerialize.putUnsignedInt(ostream, 0);
			}
		}
	}	
	
	public static void putUnsignedBytes(OutputStream ostream, short[] value) throws IOException
	{
//		synchronized (lock_write)
		{
			if (value != null)
			{
				BigIOSerialize.putUnsignedInt(ostream, value.length);
				for ( int i=0; i<value.length; ++i )
					BigIOSerialize.putUnsignedByte(ostream, value[i]);
			}
			else
			{
				BigIOSerialize.putUnsignedInt(ostream, 0);
			}
		}
	}
	
	public static void putShorts(OutputStream ostream, short[] value) throws IOException
	{
//		synchronized (lock_write) 
		{
			if (value != null)
			{
				BigIOSerialize.putUnsignedInt(ostream, value.length);
				for ( int i=0; i<value.length; ++i )
					BigIOSerialize.putShort(ostream, value[i]);
			}
			else
			{
				BigIOSerialize.putUnsignedInt(ostream, 0);
			}		
		}
	}	
	
	public static void putUnsignedShorts(OutputStream ostream, int[] value) throws IOException
	{
//		synchronized (lock_write) 
		{
			if (value != null)
			{
				BigIOSerialize.putUnsignedInt(ostream, value.length);
				for ( int i=0; i<value.length; ++i )
					BigIOSerialize.putUnsignedShort(ostream, value[i]);
			}
			else
			{
				BigIOSerialize.putUnsignedInt(ostream, 0);
			}		
		}
	}
	
	public static void putInts(OutputStream ostream, int[] value) throws IOException
	{
//		synchronized (lock_write) 
		{
			if (value != null)
			{
				BigIOSerialize.putUnsignedInt(ostream, value.length);
				for ( int i=0; i<value.length; ++i )
					BigIOSerialize.putInt(ostream, value[i]);
			}
			else
			{
				BigIOSerialize.putUnsignedInt(ostream, 0);
			}		
		}
	}	
	
	public static void putUnsignedInts(OutputStream ostream, long[] value) throws IOException
	{
//		synchronized (lock_write) 
		{
			if (value != null)
			{
				BigIOSerialize.putUnsignedInt(ostream, value.length);
				for ( int i=0; i<value.length; ++i )
					BigIOSerialize.putUnsignedInt(ostream, value[i]);
			}
			else
			{
				BigIOSerialize.putUnsignedInt(ostream, 0);
			}		
		}
	}
	
	public static void putLongs(OutputStream ostream, long[] value) throws IOException
	{
//		synchronized (lock_write) 
		{
			if (value != null)
			{
				BigIOSerialize.putUnsignedInt(ostream, value.length);
				for ( int i=0; i<value.length; ++i )
					BigIOSerialize.putLong(ostream, value[i]);
			}
			else
			{
				BigIOSerialize.putUnsignedInt(ostream, 0);
			}		
		}
	}	
	
	
//	------------------------------------------------------------------------------------------------------------------------------------
	
	OutputStream ostream;
	
	public BigIOSerialize(OutputStream os)
	{
		ostream = os;
	}
	protected BigIOSerialize() {}

	public void putBoolean(boolean value) throws IOException {
		putBoolean(ostream, value);
	}


	public void putByte(byte value) throws IOException {
		putByte(ostream, value);
	}


	public void putInt(int value) throws IOException {
		putInt(ostream, value);
	}


	public void putLong(long value) throws IOException {
		putLong(ostream, value);
	}


	public void putShort(short value) throws IOException {
		putShort(ostream, value);
	}


	public void putString(String string, String charset) throws IOException {
		putString(ostream, string, charset);
	}

	public void putString(String string) throws IOException {
		putString(ostream, string);
	}


	public void putUByte(short value) throws IOException {
		putUnsignedByte(ostream, value);
	}


	public void putUInt(long value) throws IOException {
		putUnsignedInt(ostream, value);
	}


	public void putUShort(int value) throws IOException {
		putUnsignedShort(ostream, value);
	}

	public void putFloat(float value) throws IOException
	{
		putFloat(ostream, value);
	}
	
	public void putDouble(double value) throws IOException
	{
		putDouble(ostream, value);
	}
	
	
	
//	------------------------------------------------------------------------------------------------------------------------------------
	
	
	
	static public void main(String[] args)
	{
		try
		{
			long[] datas = new long[]{
					0x7f,
					0xff,
					0x7fff,
					0xffff,
					0x7fffffff,
					0x0ffffffffL,
					0x7fffffffffffffffL
			};
			
			ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
			BigIOSerialize.putByte(os, (byte)datas[0]);
			BigIOSerialize.putUnsignedByte(os, (short)datas[1]);
			BigIOSerialize.putShort(os, (short)datas[2]);
			BigIOSerialize.putUnsignedShort(os, (int)datas[3]);
			BigIOSerialize.putInt(os, (int)datas[4]);
			BigIOSerialize.putUnsignedInt(os, datas[5]);
			BigIOSerialize.putLong(os, datas[6]);
			
			System.out.println(CUtil.arrayToString(datas));
			
			
			ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
			datas[0]	= BigIODeserialize.getByte(is);
			datas[1] 	= BigIODeserialize.getUnsignedByte(is);
			datas[2]	= BigIODeserialize.getShort(is);
			datas[3]	= BigIODeserialize.getUnsignedShort(is);
			datas[4]	= BigIODeserialize.getInt(is);
			datas[5]	= BigIODeserialize.getUnsignedInt(is);
			datas[6]	= BigIODeserialize.getLong(is);
			
			System.out.println(CUtil.arrayToString(datas));
			
			
		}
		catch(Exception err){
			err.printStackTrace();
		}
		
		
		
	}

}
