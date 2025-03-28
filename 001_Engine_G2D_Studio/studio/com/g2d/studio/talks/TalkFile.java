package com.g2d.studio.talks;

import javax.swing.ImageIcon;

import com.g2d.studio.StudioConfig;
import com.g2d.studio.fileobj.FileObject;
import com.g2d.studio.io.File;
import com.g2d.studio.res.Res;

public class TalkFile extends FileObject
{
	TalkFile(File file) {
		super(file.getName().substring(0, file.getName().length() - StudioConfig.TALK_SUFFIX.length()), file);
	}
	
	@Override
	public ImageIcon getListIcon(boolean update) {
		return new ImageIcon(Res.img_talk);
	}
	
	@Override
	public String getSaveListArgs() {
		return "";
	}
	
	
}
