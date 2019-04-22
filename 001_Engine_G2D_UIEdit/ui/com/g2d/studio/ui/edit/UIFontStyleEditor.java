package com.g2d.studio.ui.edit;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.cell.CIO;
import com.cell.CUtil;
import com.cell.io.CFile;
import com.cell.security.MD5;
import com.g2d.BufferedImage;
import com.g2d.Graphics2D;
import com.g2d.Tools;
import com.g2d.annotation.Property;
import com.g2d.awt.util.AbstractOptionDialog;
import com.g2d.display.ui.UIComponent;
import com.g2d.util.Drawing;
import com.g2d.util.Drawing.TextAnchor;

public class UIFontStyleEditor 
{
	private static UIFontStyleEditor instance;
	public static UIFontStyleEditor getInstance() {
		return instance;
	}
	
	private File editorExe;
	private File fontSavePath;
	private LinkedHashMap<String, File> templateFiles = new LinkedHashMap<String, File>();
	
	public UIFontStyleEditor() 
	{
		instance = this;
		fontSavePath = UIEdit.getInstance().getSubFile("/res/fonts");
		if (UIEditConfig.EXT_TEXT_STYLE_TOOLS_PATH!=null)
		{
//			File exe = UIEdit.getInstance().getSubFile(UIEditConfig.EXT_TEXT_STYLE_TOOLS_PATH);
//			if (exe.exists() && exe.canExecute())
//			{
//				try {
//					for (File template : new File(editorExe.getParentFile(), "templates/style").listFiles())
//					{
//						if (template.getName().toLowerCase().endsWith(".xml"))
//						{
//							templateFiles.put(template.getName(), template);
//						}
//					}
//					editorExe = exe.getCanonicalFile();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
		}
	}
	
	public BufferedImage RefreshTextStyle(String templateName, String text, int textSize)
	{
//		命令行参数约定： 
//		文字内容 -t:美峰数码 
//		文字尺寸 -s:20 
//		输出路径 -o:绝对路径+文件名 
//		XML模板ID -n:xx.xml 
//		是否命令行 -c:true/false
		if (editorExe != null) {
			String pngName = CUtil.replaceString(templateName, ".xml", "") + 
					"_" + textSize +
					"_" + MD5.getMD5(text, "UTF-8");
			String fileName = fontSavePath.getAbsolutePath() + 
					"/" + pngName + ".png";
			String cmdline = editorExe.getPath();
			cmdline += " -t:"+text;
			cmdline += " -s:"+32;
			cmdline += " -o:"+fileName;
			cmdline += " -n:"+templateName;
			cmdline += " -c:true";
			cmdline = CUtil.replaceString(cmdline, "\\", "/");
			System.out.println(cmdline);
			try {
				Process process = Runtime.getRuntime().exec(
						cmdline, 
						CUtil.getEnv(), 
						editorExe.getParentFile());
				process.waitFor();
			} catch (Exception e) {
				e.printStackTrace();
			}	
			return Tools.readImage(fileName);
		}
		return null;
	}
	
	public UIEditFontStyle ShowSelectStyleTemplates(UIEditFontStyle obj)
	{
		if (editorExe != null) {
			SelectStypeDialog dialog = new SelectStypeDialog();
			String template = dialog.showDialog();
			if (template != null)
			{
				UIEditFontStyle ret = new UIEditFontStyle();
				ret.TemplateName = template;
				return ret;
			}
			return obj;
		}
		return null;
	}
	
	public void ShowTemplateEditor()
	{
		try {
			if (editorExe != null) {
				Runtime.getRuntime().exec(
						editorExe.getPath(), 
						CUtil.getEnv(), 
						editorExe.getParentFile());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	private class SelectStypeDialog extends AbstractOptionDialog<String>
	{
		JList<String> list = new JList<String>();
		
		public SelectStypeDialog() 
		{
			super.setTitle("选择样式模板");
			super.setSize(800, 600);
			this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.list.setListData(templateFiles.keySet().toArray(new String[templateFiles.size()]));
			this.add(list, BorderLayout.CENTER);
		}
		
		@Override
		protected String getUserObject(ActionEvent e) {
			return list.getSelectedValue();
		}
		
		@Override
		protected boolean checkOK() {
			return true;
		}
	}
	
	public static UIEditFontStyle readXML(String templateName, String text, int textSize)
	{
		UIEditFontStyle ret = new UIEditFontStyle();
		ret.TemplateName = templateName;
		ret.getBuffer(text, textSize);
		return ret;
	}
	
	public static class UIEditFontStyle
	{
		public String TemplateName;
		private BufferedImage Buffer;
		
		private int LastFontSize;
		private String LastText;
		
		@Override
		public String toString()
		{
			return TemplateName+"";
		}
		
		public BufferedImage getBuffer(String text, int size)
		{
			if (Buffer == null || LastFontSize != size || !text.equals(LastText))
			{
				LastFontSize = size;
				LastText = text;
				Buffer = instance.RefreshTextStyle(TemplateName, text, size);
			}
			return Buffer;
		}
		
		public void render(Graphics2D g, int x, int y, int w, int h, TextAnchor ta)
		{
			int iw = Buffer.getWidth();
			int ih = Buffer.getHeight();
			switch(ta)
			{
			case L_T:
				g.drawImage(Buffer, x, y);
				return;
			case C_T:
				g.drawImage(Buffer, w / 2 - iw / 2 + x, y);
				return;
			case R_T:
				g.drawImage(Buffer, w - iw + x, y);
				return;
			case L_C:
				g.drawImage(Buffer, x, h / 2 - ih / 2 + y);
				return;
			case C_C:
				g.drawImage(Buffer, w / 2 - iw / 2 + x, h / 2 - ih / 2 + y);
				return;
			case R_C:
				g.drawImage(Buffer, w - iw + x, h / 2 - ih / 2 + y);
				return;
			case L_B:
				g.drawImage(Buffer, x, h - ih + y);
				return;
			case C_B:
				g.drawImage(Buffer, w / 2 - iw / 2 + x, h - ih + y);
				return;
			case R_B:
				g.drawImage(Buffer, w - iw + x, h - ih + y);
				return;
			}
			
		}
	}
}
