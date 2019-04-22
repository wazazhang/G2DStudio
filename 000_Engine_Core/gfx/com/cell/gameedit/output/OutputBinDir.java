package com.cell.gameedit.output;


import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.cell.CIO;
import com.cell.j2se.CAppBridge;


/**
 * 如何将编辑器资源解析成单位
 * @author WAZA
 */
public class OutputBinDir extends OutputBin
{
	final public String root;
	final public String file_name;
	final public String path;
	
	public OutputBinDir(String file) throws Exception {
		this.path 		= file.replace('\\', '/');
		this.root		= path.substring(0, path.lastIndexOf("/")+1);
		this.file_name	= path.substring(root.length());
		loadFromBin(CIO.getInputStream(file));
	}
	
	@Override
	public String getResRoot() {
		return root;
	}
	
	@Override
	public String getPath() {
		return path;
	}
	
	public byte[] loadRes(String path, AtomicReference<Float> percent)
	{
		byte[] data = CIO.loadData(root + path);
		if (data == null) {
			System.err.println("SetResource : read error : " + root + path);
		} 
		return data;
	}
}



