package com.cell.filesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import com.cell.CIO;
import com.cell.io.CFile;
import com.cell.io.LittleIOSerialize;
import com.cell.io.NIOSerialize;
import com.cell.security.MD5;

public class FileSystemWriter extends FileSystem
{
	private AtomicInteger trunk_index = new AtomicInteger(0);
	
	private LinkedHashMap<Long, ArrayList<Entry>> indexer = new LinkedHashMap<Long, ArrayList<Entry>>();
	
	private float current_percent = 0;
	private FileEntry current_entry = null;
	private ReentrantLock current_lock = new ReentrantLock();
	
	public class Entry
	{
		final public FileEntry fe;
		final public String name;
		final public File src;
		
		private Entry(FileEntry fe, String name, File src) {
			super();
			this.fe = fe;
			this.name = name;
			this.src = src;
		}
	}
	
	public FileSystemWriter() {}
	
	public int getEntryCount() {
		int count = 0;
		for (java.util.Map.Entry<Long, ArrayList<Entry>> me : indexer.entrySet()) {
			count += me.getValue().size();
		}
		return count;
	}
	
	public Entry genEntry(String name, File data) throws Exception 
	{
		byte[] name_bytes = name.getBytes("UTF-8");
		long hash = hash_function.hash(name_bytes);
		byte[] fdata = CFile.readData(data);
		if (fdata != null) {
			FileEntry entry = new FileEntry();
			entry.hash		= hash;
			entry.key		= name;
			entry.key_size	= name_bytes.length;
			entry.key_md5 	= MD5.getMD5(name_bytes).toUpperCase();
			entry.f_size	= fdata.length;
			entry.f_start	= trunk_index.get();
			entry.f_date	= System.currentTimeMillis()/1000;
			entry.f_md5		= MD5.getMD5(fdata).toUpperCase();
			return new Entry(entry, name, data);
		}
		return null;
	}

	public FileEntry putEntry(Entry entry) throws Exception 
	{
		ArrayList<Entry> index = indexer.get(entry.fe.hash);
		if (index == null) {
			index = new ArrayList<Entry>();
			indexer.put(entry.fe.hash, index);
		}
		index.add(new Entry(entry.fe, entry.name, entry.src));
		trunk_index.addAndGet(entry.fe.f_size);
		return CIO.cloneObject(entry.fe);
	}
	
	public FileEntry put(String name, File data) throws Exception 
	{
		Entry entry = genEntry(name, data);
		if (entry == null) {
			throw new Exception("File can not gen entry : " + data.getCanonicalPath());
		}
		return putEntry(entry);
	}
	
	public FileEntry getSavingEntry() {
		synchronized (current_lock) {
			return CIO.cloneObject(current_entry);
		}
	}
	public float getSavingPercent() {
		synchronized (current_lock) {
			return current_percent;
		}
	}
	
	public void save(File file) throws Exception
	{
		if (CFile.createFile(file)) 
		{
			ArrayList<Entry> entrys = new ArrayList<Entry>();
			for (java.util.Map.Entry<Long, ArrayList<Entry>> me : indexer.entrySet()) {
				for (Entry e : me.getValue()) {
					entrys.add(e);
				}
			}

			float c_index = 0;
			float c_total = entrys.size();
					
			FileOutputStream fos = new FileOutputStream(file);
			try {
				fos.write(FileSystem.FS_HEAD_START);
				fos.write(FileSystem.VERSION);
				LittleIOSerialize.putLong(fos, 0);
				//////////////////////////////////////////////////
				fos.write(FileSystem.FS_ENTRY_START);
				LittleIOSerialize.putInt(fos, entrys.size());
				int entry_index = 0;
				for (Entry e : entrys) {
					e.fe.index = entry_index;
					e.fe.save(fos);
					entry_index ++;
				}
				fos.flush();
				//////////////////////////////////////////////////
				fos.write(FileSystem.FS_TRUNK_START);
				for (Entry e : entrys) {
					synchronized (current_lock) {
						current_entry = e.fe;
						current_percent = c_index / c_total;
					}
					byte[] fdata = CFile.readData(e.src);
					fos.write(fdata);
					fos.flush();
					c_index += 1;
				}
				//////////////////////////////////////////////////
				fos.write(FileSystem.FS_END);
				fos.flush();
				try {
					FileChannel fc = fos.getChannel();
					long fsize = fc.position();
					fc.position(8);
					ByteBuffer bb = ByteBuffer.allocate(8);
					bb.order(ByteOrder.LITTLE_ENDIAN);
					NIOSerialize.putLong(bb, fsize);
					bb.flip();
					fc.write(bb);
					fc.close();
				} catch (Exception e) {
					throw e;
				}
			} finally {
				fos.close();
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
}
