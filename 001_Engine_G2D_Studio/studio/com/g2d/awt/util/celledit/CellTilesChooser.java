package com.g2d.awt.util.celledit;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.JOptionPane;

import com.g2d.awt.util.AbstractOptionDialog;

public class CellTilesChooser extends AbstractOptionDialog<CellTileIndex>
{
	private static final long serialVersionUID = 1L;

	public static File filechooser_root = null;
	private static File last_filechooser_root = null;
	
	private CellTilesResource res;
	private CellTilesList list;
	
	public CellTilesChooser(CellTilesResource res, Frame parent) throws Exception 
	{
		super(parent);
		super.setSize(800, 600);
		this.res = res;
		this.list = new CellTilesList(res);
		this.add(list, BorderLayout.CENTER);
		setTitle(res.getPath());
		setCenter();
	}
	
	@Override
	protected boolean checkOK() {
		return true;
	}
	
	@Override
	protected CellTileIndex getUserObject(ActionEvent e) {
		return list.getSelectedTile();
	}

	public static CellTileIndex showDialog(Frame parent, CellTilesResource res) {
		try {
			return new CellTilesChooser(res, parent).showDialog();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(parent, e.getMessage());
		}
		return null;
	}
	
	public static CellTileIndex showDialog(Frame parent, File defaultFile) 
	{
		java.awt.FileDialog fd = new FileDialog(parent);
		if (defaultFile != null && defaultFile.exists()) {
			fd.setDirectory(defaultFile.getPath());
		}
		else if (last_filechooser_root != null) {
			fd.setDirectory(last_filechooser_root.getPath());
		}
		else if (filechooser_root != null) {
			fd.setDirectory(filechooser_root.getPath());
		}
		fd.setFilenameFilter(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".xml");
			}
		});
		fd.setTitle("选择原图片");
		fd.setMode(FileDialog.LOAD);
		fd.setVisible(true);
		try {
			String file = fd.getDirectory() + fd.getFile();
			File dfile = new File(file).getCanonicalFile();
			if (fd.getFile() != null && !fd.getFile().isEmpty()) {
				System.out.println("You chose to open this file: " + file);
				last_filechooser_root = dfile;
				return new CellTilesChooser(
						new CellTilesResource(dfile), parent).showDialog();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(parent, e.getMessage());
		}
		return null;
	}
}
