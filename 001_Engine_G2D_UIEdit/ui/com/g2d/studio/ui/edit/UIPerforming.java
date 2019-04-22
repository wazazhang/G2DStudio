package com.g2d.studio.ui.edit;

import java.awt.BorderLayout;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JRootPane;

import com.cell.CIO;
import com.cell.CMath;
import com.cell.CUtil;
import com.cell.io.CFile;
import com.g2d.BufferedImage;
import com.g2d.awt.util.AbstractDialog;
import com.g2d.awt.util.AbstractTaskFrame;
import com.g2d.awt.util.TextEditorDialog;
import com.g2d.java2d.impl.AwtEngine;
import com.g2d.studio.SaveProgressBar;

public class UIPerforming 
{
	private static UIPerforming g_instance;
	public static UIPerforming newInstance() {
		g_instance = new UIPerforming();
		return g_instance;
	}

//	--------------------------------------------------------------------
	
	public static void UseImage(File f)
	{
		if (g_instance != null) {
			g_instance.useImage(f);
		}
	}
	
	public static void UseAtlas(File f, String img, int tileCount, int tileID)
	{
		if (g_instance != null) {
			g_instance.useAtlas(f, img, tileCount, tileID);
		}
	}
	
	
//	--------------------------------------------------------------------
	
	static private class AtlasImageUsing
	{
		final public String imagesName;
		final public int tileCount;
		final public AtomicInteger use_count = new AtomicInteger();
		final public LinkedHashMap<Integer, AtomicInteger> tiles = 
				new LinkedHashMap<Integer, AtomicInteger>();
		
		public AtlasImageUsing(String imagesName, int tileCount) {
			this.imagesName = imagesName;
			this.tileCount = tileCount;
		}
		public void useTile(int tileID) {
			use_count.incrementAndGet();
			AtomicInteger im = tiles.get(tileID);
			if (im == null) {
				im = new AtomicInteger();
				tiles.put(tileID, im);
			}
			im.incrementAndGet();
		}
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(
			"\t<\"" + imagesName + "\" count=" + use_count + ">\n");
			for (int i=0; i<tileCount; i++) {
				sb.append("\t\t\""+imagesName+"\".tile["+i+"] = " + tiles.get(i)).append("\n");
			}
			return sb.toString();
		}
	}
	
	static private class AtlasUsing
	{
		final public File xmlfile;
		final public AtomicInteger use_count = new AtomicInteger();
		final public LinkedHashMap<String, AtlasImageUsing> images = 
				new LinkedHashMap<String, AtlasImageUsing>();
		
		public AtlasUsing(File xmlfile) {
			this.xmlfile = xmlfile;
		}
		public void useImage(String img, int tileCount, int tileID) {
			use_count.incrementAndGet();
			AtlasImageUsing im = images.get(img);
			if (im == null) {
				im = new AtlasImageUsing(img, tileCount);
				images.put(img, im);
			}
			im.useTile(tileID);
		}
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(
			"<\"" + xmlfile + "\" count=" + use_count + ">\n");
			for (AtlasImageUsing e : images.values()) {
				sb.append(e).append("\n");
			}
			return sb.toString();
		}
	}

//	--------------------------------------------------------------------
	
	private LinkedHashMap<File, AtomicInteger> images = new LinkedHashMap<File, AtomicInteger>();
	private LinkedHashMap<File, AtlasUsing> atlas = new LinkedHashMap<File, AtlasUsing>();
	
	private void useImage(File f)
	{
		if (f != null && f.exists()) {
			AtomicInteger count = images.get(f);
			if (count != null) {
				count.incrementAndGet();
			} else {
				images.put(f, new AtomicInteger(1));
			}
		}
	}
	
	private void useAtlas(File f, String img, int tileCount, int tileID)
	{
		if (f != null && f.exists()) {
			AtlasUsing att = atlas.get(f);
			if (att == null) {
				att = new AtlasUsing(f);
				atlas.put(f, att);
			}
			att.useImage(img, tileCount, tileID);
		}
	}
	
	public String getInfo() 
	{
		HashMap<String, File> used_files = new HashMap<String, File>();
		StringBuilder sb = new StringBuilder();
		sb.append("[图片容量]\n");
		{
			long total_save = 0;
			long total_range = 0;
			long total_range_gl = 0;
			for (Map.Entry<File, AtomicInteger> e : images.entrySet()) {
				try {
					if (e.getKey().exists()) {
						byte[] data = CIO.loadData(e.getKey().getPath());
						BufferedImage buff = AwtEngine.getEngine().createImage(
								new ByteArrayInputStream(data));
						long pow 		= CMath.ccNextPOT(buff.getWidth());
						long poh 		= CMath.ccNextPOT(buff.getHeight());
						total_save		+= data.length;
						total_range 	+= (buff.getWidth()*buff.getHeight()*4);
						total_range_gl 	+= (pow*poh*4);
					}
				} catch (Throwable e1) {
					e1.printStackTrace();
				}
			}
			sb.append("文件占用	= " + CUtil.toBytesSizeString(total_save) + "\n");
			sb.append("内存占用	= " + CUtil.toBytesSizeString(total_range) + "\n");
			sb.append("纹理占用	= " + CUtil.toBytesSizeString(total_range_gl) + "\n");
		}
		sb.append("\n");
		sb.append("[图片使用次数]\n");
		{
			for (Map.Entry<File, AtomicInteger> e : images.entrySet()) {
				String count = e.getValue().toString();
				sb.append(count).append("	= ").append(e.getKey()).append("\n");
				try {
					used_files.put(e.getKey().getCanonicalPath(), e.getKey());
				} catch (Exception err) {
				}
			}
		}
		sb.append("\n");
		sb.append("[图集使用次数]\n");
		{
			for (Map.Entry<File, AtlasUsing> e : atlas.entrySet()) {
				if (e.getKey().exists()) {
					sb.append(e.getValue()).append("\n");
				}
				try {
					used_files.put(e.getKey().getCanonicalPath(), e.getKey());
				} catch (Exception err) {
				}
			}
		}
		sb.append("[未使用文件]\n");
		ArrayList<File> allres_files = CFile.listAllChildren(UIEdit.getInstance().resdir, null);
		{
			for (File file : allres_files) {
				try {
					file = file.getCanonicalFile();
					if (!used_files.containsKey(file.getCanonicalPath())){
						sb.append(file).append("\n");
					}
				} catch (Exception err) {
				}
			}
		}
		return sb.toString();
	}
	
	public void performingUserTemplates(Collection<UITemplate> templates)
	{
		new PerformingUserTemplatesTask(templates).start();
	}
	

	private class PerformingUserTemplatesTask extends AbstractTaskFrame
	{
		private static final long serialVersionUID = 1L;
		
		Collection<UITemplate> templates;
		
		public PerformingUserTemplatesTask(Collection<UITemplate> templates)		
		{
			super.setTitle("分析中...");
			this.templates = templates;
		}

		public void run()
		{
			try 
			{
				progress.setMaximum(templates.size());
				for (UITemplate uu : templates) {
					try {
						progress.setPrefix("["+uu.getFullName()+"] ");
						UIEditFileNode fn = new UIEditFileNode(uu.getUserTemplate());
						fn.loadFile();
						fn = null;
						System.gc();
					} catch (Throwable e) {
						e.printStackTrace();
					} finally {
						progress.increment();
					}
				}
				progress.setPrefix("");
			}
			catch (Throwable e) {
				e.printStackTrace();
			} 
			finally {
				try {
					TextEditorDialog d = new TextEditorDialog();
					d.setAlwaysOnTop(false);
					d.setText(UIPerforming.this.getInfo());
					d.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			this.dispose();
			g_instance = null;
		}
		
	}
	

}
