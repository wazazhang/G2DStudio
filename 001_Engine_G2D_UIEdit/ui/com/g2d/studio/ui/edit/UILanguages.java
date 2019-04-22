package com.g2d.studio.ui.edit;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import org.w3c.dom.Element;

import com.cell.CIO;
import com.cell.CMath;
import com.cell.CUtil;
import com.cell.io.CFile;
import com.cell.util.Pair;
import com.cell.util.Properties;
import com.g2d.BufferedImage;
import com.g2d.awt.util.AbstractTaskFrame;
import com.g2d.awt.util.TextEditorDialog;
import com.g2d.display.ui.UIComponent;
import com.g2d.java2d.impl.AwtEngine;

public class UILanguages 
{
	private static final String LanguageFileName = "language.properties";

	public static File[] ListLanguageFiles()
	{
		Vector<File> files = new Vector<File>();
		for (File dir : UIEdit.getInstance().languagedir.listFiles()) {
			if (dir.isFile()) {
				if (dir.getName().endsWith(".properties")) {
					files.add(dir);
				}
			}
			else if (dir.isDirectory()) {
				File lanfile = new File(dir, LanguageFileName);
				if (lanfile.exists()) {
					files.add(lanfile);
				}
			}
		}
		return CUtil.toArray(files, File.class);
	}

//	--------------------------------------------------------------------
//	Translater
//	--------------------------------------------------------------------

	
	private static LanguagePack lanpak;
	
	public static File GetCurrentLanguageFile() 
	{
		if (lanpak != null) {
			return lanpak.file;
		}
		return null;
	}

	public static void LoadLanguageFile(Collection<UITemplate> templates, File lanfile)
	{
		if (lanfile!=null) {
			lanpak = new LanguagePack(lanfile);
			lanpak.start(templates);
		} else {
			lanpak = null;
		}
	}

	public static boolean Translate(
			UIEditFileNode edit,
			UIComponent uinode, 
			Field f)
	{
		if (lanpak != null) {
			return lanpak.translate(edit, uinode, f);
		}
		return false;
	}

	private static class LanguagePack
	{
		final private File file;
		final private Properties lmap;
	
		private LanguagePack(File propFile)
		{
			String alltext = CFile.readText(propFile);
			this.file = propFile;
			this.lmap = new Properties(alltext, "=");
		}
		
		private void start(Collection<UITemplate> templates) 
		{
			new TranslateTaskFrame(templates).start();
		}
		
		private boolean translate(
				UIEditFileNode edit,
				UIComponent uinode, 
				Field f)
		{
			String key = edit.getEditName() + "." + uinode.name + "." + f.getName();
			String value = lmap.get(key);
			if (value != null) {
				try {
					f.set(uinode, value);
					return true;
				} catch (Exception e) {}
			}
			return false;
		}
		
		private class TranslateTaskFrame extends AbstractTaskFrame
		{
			private static final long serialVersionUID = 1L;
			
			final Collection<UITemplate> templates;
			
			public TranslateTaskFrame(Collection<UITemplate> templates)		
			{
				super.setTitle("翻译文本信息...");
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
							fn.saveFile(false);
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
				this.dispose();
				collector=null;
			}
			
		}

	}

//	--------------------------------------------------------------------
//	Collector
//	--------------------------------------------------------------------

	private static LanguageCollector collector;

	public static void GenLanguageFile(Collection<UITemplate> templates)
	{
		collector = new LanguageCollector();
		collector.start(templates);
	}
	
	public static void PutTextProperty(
			UIEditFileNode edit,
			UIComponent uinode, 
			Element e, 
			Field f)
	{
		if (collector != null) {
			collector.putTextProperty(edit, uinode, e, f);
		}
	}

	private static class LanguageCollector
	{

		private TreeMap<String, Vector<Pair<String, String>>> all_files = 
				new TreeMap<String, Vector<Pair<String, String>>>();
		
		public LanguageCollector() 
		{
		}
		
		private void start(Collection<UITemplate> templates)
		{
			new GenLanguageTaskFrame(templates).start();
		}
		
		synchronized private void putTextProperty(
				UIEditFileNode edit,
				UIComponent uinode, 
				Element e, 
				Field f)
		{
			String txt = e.getAttribute(f.getName());
			if (txt != null && !txt.isEmpty()) {
				putField(edit, uinode.getName() + "." + f.getName(), txt);
			}
		}

		synchronized private void putField(UIEditFileNode edit, String key, String value) 
		{
			Vector<Pair<String, String>> fm = all_files.get(edit.getEditName());
			if (fm == null) {
				fm = new Vector<Pair<String, String>>();
				all_files.put(edit.getEditName(), fm);
			}
			fm.add(new Pair<String, String>(key, value));
		}
		
		synchronized private String getInfo() 
		{
			StringBuilder sb = new StringBuilder();
			String commentLine = "###############################################\n";
			
			sb.append(commentLine);
			sb.append("# Language : " + Locale.getDefault().getLanguage() + "\n");
			sb.append("# Country  : " + Locale.getDefault().getCountry() + "\n");
			sb.append("# Date     : " + CUtil.timeYYMMDDHHMMSS(System.currentTimeMillis()) + "\n");
			sb.append(commentLine);
			sb.append("\n");
			sb.append("\n");
			
			for (String edit_file : all_files.keySet())
			{
				Vector<Pair<String, String>> fm = all_files.get(edit_file);
				if (!fm.isEmpty())
				{
					sb.append(commentLine);
					sb.append("# " + edit_file + "\n");
					sb.append(commentLine);
					sb.append(edit_file + "\n");
					for (Pair<String, String> e : fm) {
						String key = e.getKey();
						String value = e.getValue();
						if (value != null && !value.isEmpty()) {
							if (value.indexOf('\n') >= 0) {
								value = CUtil.replaceString(value, "\r", "");
								value = CUtil.replaceString(value, "\n", "\\\n");
								value = "\\\n" + value;
							}
							sb.append(edit_file+"."+key + "=" + value + "\n");
						}
					}
					sb.append("\n");
					sb.append("\n");
				}
			}
			return sb.toString();
		}

		synchronized private void saveToLanguageFile() 
		{
			File path = new File(
					UIEdit.getInstance().languagedir.getPath(), 
					LanguageFileName);
			CFile.writeText(path, getInfo());
		}

		private class GenLanguageTaskFrame extends AbstractTaskFrame
		{
			private static final long serialVersionUID = 1L;
			
			final Collection<UITemplate> templates;
			
			public GenLanguageTaskFrame(Collection<UITemplate> templates)		
			{
				super.setTitle("检索文本信息...");
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
					saveToLanguageFile();
				}
				catch (Throwable e) {
					e.printStackTrace();
				} 
				this.dispose();
				collector=null;
			}
			
		}

	}
	
//	--------------------------------------------------------------------
	
	

	

}
