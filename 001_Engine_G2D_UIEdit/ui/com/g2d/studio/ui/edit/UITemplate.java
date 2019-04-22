package com.g2d.studio.ui.edit;

import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.g2d.BufferedImage;
import com.g2d.Engine;
import com.g2d.Graphics2D;
import com.g2d.display.ui.UIComponent;
import com.g2d.display.ui.layout.UILayout;
import com.g2d.display.ui.layout.UILayout.ImageStyle;
import com.g2d.java2d.impl.AwtEngine;
import com.g2d.studio.swing.G2DTreeNode;
import com.g2d.studio.ui.edit.gui.UEFileNode;

public class UITemplate extends G2DTreeNode<UITemplate>
{
	final public UILayout rect;
	
	final private String name;
	final private String path;
	
	private final Class<?> 	uiType;
	private final File 		userFile;
	
	public UITemplate(UILayout rect, Class<?> uiType, String name) {
		this.name = name;
		this.path = name;
		this.rect = rect;
		this.uiType = uiType;
		this.userFile = null;
	}
	public UITemplate(File file) {
		this.name = file.getName();
		this.path = UIEdit.getInstance().getFileSubName(file);
		this.rect = new UILayout(ImageStyle.NULL);
		this.uiType = UEFileNode.class;
		this.userFile = file;
	}
	@Override
	protected ImageIcon createIcon() {
		if (userFile == null) {
			BufferedImage sic = Engine.getEngine().createImage(40, 30);
			Graphics2D g = sic.createGraphics();
			g.setClip(0, 0, 40, 30);
			rect.render(g, 0, 0, 40, 30);
			g.dispose();
			return new ImageIcon(AwtEngine.unwrap(sic));
		}
		return null;
	}
	
	public File getUserTemplate() {
		return userFile;
	}

	public Class<?> getUIType() {
		return uiType;
	}
	
	public UIComponent createDisplay(UIEditFileNode edit) throws Exception {
		if (userFile != null) {
			UEFileNode fn = new UEFileNode(userFile);
			fn.load(edit);
			return fn;
		} else {
			return (UIComponent) getUIType().newInstance();
		}
	}
	
	@Override
	public void onDoubleClicked(JTree tree, MouseEvent e) {
		if (userFile != null) {
			UIEdit.getInstance().loadFileTabble(userFile);
		}
	}
	
	@Override
	public String getName() {
		return name;
	}
	public String getFullName() {
		return path;
	}
	
	public static class UITemplateGroup extends DefaultMutableTreeNode
	{
		private static final long serialVersionUID = 1L;
		
		final public File dir;
		
		public UITemplateGroup(File dir) {
			super(dir.getName(), true);
			this.dir = dir;
		}
		private UITemplateGroup(String name) {
			super(name, true);
			this.dir = null;
		}
		
		public static UITemplateGroup createRootNode(String name) {
			return new UITemplateGroup("用户控件");
		}
	}
}
