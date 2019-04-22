package com.g2d.awt.util.celledit;

import java.io.File;

public class CellAtlasIndex
{
	final public CellTilesResource res;
	final public File xml_file;
	final public String imagesName;

	public CellAtlasIndex(CellTilesResource res, File xml_file,
			String imagesName) {
		this.res = res;
		this.xml_file = xml_file;
		this.imagesName = imagesName;
	}

}
