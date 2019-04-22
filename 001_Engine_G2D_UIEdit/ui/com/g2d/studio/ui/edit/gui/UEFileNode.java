package com.g2d.studio.ui.edit.gui;

import java.io.File;

import javax.swing.JOptionPane;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.g2d.Graphics2D;
import com.g2d.display.ui.UIComponent;
import com.g2d.studio.ui.edit.UIEdit;
import com.g2d.studio.ui.edit.UIEditFileNode;

public class UEFileNode extends UIComponent implements SavedComponent
{
	private String 	fileName;
	private File 	file;
	private UERoot 	croot = null;
	
	public UEFileNode(File file) {
		this.fileName = UIEdit.getInstance().getFileSubName(file);
		this.file = file;
		this.enable_childs = false;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public File getFile() {
		return file;
	}
	
	@Override
	public void update() {
		super.update();
		if (croot != null) {
			croot.x = 0;
			croot.y = 0;
			this.setSize(
					croot.getWidth(),
					croot.getHeight());
		}
	}
	@Override
	public void render(Graphics2D g) {
		if (debugDraw != null) {
			debugDraw.render(g, this);
		}
	}
	
	@Override
	public void onRead(UIEditFileNode edit, Element e, Document doc) throws Exception {
		fileName = e.getAttribute("fileName");
		try {
			load(edit);
		} catch (Exception err) {
			err.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"无法打开文件["+fileName+"]\n"+err.getMessage());
		}
	}

	@Override
	public void onWrite(UIEditFileNode edit, Element e, Document doc) throws Exception{
		e.setAttribute("fileName", fileName);
	}
	
	public void load(UIEditFileNode edit) throws Exception {
		if (croot == null) {
			if (fileName != null) {
				file  = UIEdit.getInstance().getSubFile(fileName).getCanonicalFile();
				croot = UIEdit.getInstance().loadFileNode(edit, file);
				this.addChild(croot);
			}
		}
	}
}
