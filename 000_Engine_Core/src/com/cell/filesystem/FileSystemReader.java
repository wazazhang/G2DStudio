package com.cell.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.cell.CIO;
import com.cell.CUtil;
import com.cell.io.LittleIODeserialize;

public class FileSystemReader extends FileSystem
{
	private FileInputStream fis;
	private FileChannel fchannel;

	private HashMap<Long, ArrayList<FileEntry>> indexer = new HashMap<Long, ArrayList<FileEntry>>();
	
	final private long trunk_start;
	
	public static class EntryData
	{
		public FileEntry entry;
		public byte[] data;
	}
	
	public FileSystemReader(File file) throws Exception 
	{
		fis = new FileInputStream(file);
		byte[] head_trunk = new byte[4];
		fis.read(head_trunk);
		if (CUtil.arrayEquals(head_trunk, FileSystem.FS_HEAD_START)) {
			fis.read(head_trunk);// version
			long total_size = LittleIODeserialize.getLong(fis);
			fis.read(head_trunk);// entry start
			if (CUtil.arrayEquals(head_trunk, FileSystem.FS_ENTRY_START)) {
				int entry_count = LittleIODeserialize.getInt(fis);
				for (int i=0; i<entry_count; i++) {
					FileEntry re = new FileEntry();
					re.load(fis);
					long rehash = hash_function.hash(re.key.getBytes("UTF-8"));
					if (rehash != re.hash) {
						//System.out.println("rehash : " + re.key + " : " + re.hash + " -> " + rehash);
						re.hash = rehash;
					}
					ArrayList<FileEntry> ets = indexer.get(rehash);
					if (ets == null) {
						ets = new ArrayList<FileEntry>(1);
						indexer.put(rehash, ets);
					}
					ets.add(re);
				}
			} else {
				close();
				throw new Exception("File Type Error !");
			}
			fis.read(head_trunk);
			fchannel = fis.getChannel();
			trunk_start = fchannel.position();
		} else {
			close();
			throw new Exception("File Type Error !");
		}
	}

	protected FileEntry findEntry(long hash, String name) {
		try {
			ArrayList<FileEntry> ets = indexer.get(hash);
			if (ets != null) {
				if (ets.size() == 1) {
					return ets.get(0);
				} else {
					for (FileEntry e : ets) {
						if (e.key.equals(name)) {
							return e;
						}
					}
				}
			}
		} catch (Exception e) {}
		return null;
	}
	
	public ArrayList<FileEntry> getEntrys() {
		ArrayList<FileEntry> entrys = new ArrayList<FileEntry>();
		for (java.util.Map.Entry<Long, ArrayList<FileEntry>> me : indexer.entrySet()) {
			for (FileEntry e : me.getValue()) {
				entrys.add(e);
			}
		}
		return CIO.cloneObject(entrys);
	}
	
	public EntryData getEntryData(FileEntry e) throws Exception 
	{
		if (e != null) 
		{
			ByteBuffer bd = ByteBuffer.allocate(e.f_size);
			bd.order(ByteOrder.LITTLE_ENDIAN);
			synchronized (fchannel) {
				fchannel.position(trunk_start+e.f_start);
				fchannel.read(bd);
			}
			bd.flip();
			byte[] data = bd.array();
			EntryData ed = new EntryData();
			ed.data = data;
			ed.entry = CIO.cloneObject(e);
			return ed;
		}
		return null;
	}
	
	public EntryData getData(String name) throws Exception {
		byte[] name_data = name.getBytes("UTF-8");
		long hash = hash_function.hash(name_data);
		FileEntry e = findEntry(hash, name);
		EntryData ed = getEntryData(e);
		if (ed != null) {
			if (e.key.equals(name)) {
				return ed;
			}
		}
		return null;
	}
	
	public void close() {
		try {
			fchannel.close();
		} catch (Exception e) {}
		try {
			fis.close();
		} catch (Exception e) {}
	}
	
	
	
	
	
	
	
	
}
