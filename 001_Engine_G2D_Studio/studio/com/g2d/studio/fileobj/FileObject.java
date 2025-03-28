package com.g2d.studio.fileobj;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JList;

import com.g2d.studio.io.File;
import com.g2d.studio.swing.G2DList;
import com.g2d.studio.swing.G2DListItem;

abstract public class FileObject implements G2DListItem
{
	final private String 		name;

	final private File			file;
	
	public FileObject(String name, File file) {
		this.name = name;
		this.file = file;
	}
	
	public File getFile() {
		return file;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public String getListName() {
		return name;
	}
	
	@Override
	public Component getListComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		return null;
	}
	
	abstract public String getSaveListArgs();

	public FileObject clone() {
		return new WrapObject(this);
	}
	
	@Override
	public ImageIcon getListIcon(boolean update) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDoubleClicked(G2DList<?> list, MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRightClicked(G2DList<?> list, MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClicked(G2DList<?> list, MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSelected(G2DList<?> list) {
		// TODO Auto-generated method stub
		
	}

	static class WrapObject extends FileObject
	{
		FileObject src;
		
		public WrapObject(FileObject src) {
			super(src.getName(), src.getFile());
			this.src = src;
		}
		
		@Override
		public ImageIcon getListIcon(boolean update) {
			return src.getListIcon(update);
		}
		
		@Override
		public FileObject clone() {
			return src.clone();
		}
		
		@Override
		public String getSaveListArgs() {
			return src.getSaveListArgs();
		}
		
		public File getFile() {
			return src.getFile();
		}
		
		public String getName() {
			return src.getName();
		}

		@Override
		public String getListName() {
			return src.getListName();
		}
		
		@Override
		public Component getListComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			return null;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof WrapObject) {
				return ((WrapObject) obj).src.equals(src);
			}
			if (obj instanceof FileObject) {
				return obj.equals(src);
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return src.hashCode();
		}
	}
}

