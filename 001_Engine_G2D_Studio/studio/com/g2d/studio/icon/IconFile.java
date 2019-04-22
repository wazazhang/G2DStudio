package com.g2d.studio.icon;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import com.g2d.awt.util.Tools;
import com.g2d.studio.StudioConfig;
import com.g2d.studio.fileobj.FileObject;
import com.g2d.studio.io.File;

public class IconFile extends FileObject
{
	transient private BufferedImage	image;
	transient private ImageIcon 	icon;	
	
	IconFile(File file) {
		super(file.getName().substring(0, file.getName().length() - StudioConfig.ICON_SUFFIX.length()), file);
	}
	
	IconFile(String name, File file, BufferedImage image, ImageIcon icon) {
		super(name, file);
		this.image = image;
		this.icon  = icon;
	}
	
	public BufferedImage getImage() {
		if (image == null) {
			image = Tools.readImage(getFile().getPath());
		}
		return image;
	}
	
	@Override
	public ImageIcon getListIcon(boolean update) {
		if (icon == null) {
			icon = new ImageIcon(getImage());
		}
		return icon;
	}
	
	@Override
	public String getSaveListArgs() {
		if (getImage() != null) {
			return getImage().getWidth()+","+getImage().getHeight();
		} else {
			return "0,0";
		}
	}
}
