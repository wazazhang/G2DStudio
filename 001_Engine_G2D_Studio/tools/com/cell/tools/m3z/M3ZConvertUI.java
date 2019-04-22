package com.cell.tools.m3z;

import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import com.cell.CUtil;
import com.cell.j2se.CAppBridge;
import com.g2d.awt.util.AbstractFrame;

public class M3ZConvertUI extends AbstractFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;

	final String[] formats = new String[] { 
			"PVR4",
			"PVR2", 
			"PVR1,PVRA", 
			"ETC1,ETCA", 
			"ETC1", 
			"ETCA", 
			"ATCI",
			"ATCE", 
			"ATC3", };
	final File 		workDir;
	final boolean 	removeTemp;
	
	JButton		btnLoadPNG 	= new JButton("Load Image");
	JTextField	txtLoadPNG	= new JTextField("");

	JLabel		lblScale	= new JLabel("scale Image");
	JSpinner 	numScale 	= new JSpinner(new SpinnerNumberModel(1.0f, -1024, 1024, 0.5f));
	
	JLabel		lblFormats	= new JLabel("choose formats");
	JComboBox	cbxFormats	= new JComboBox(formats);

	JLabel		lblNewExt	= new JLabel("save file extension");
	JTextField	txtNewExt	= new JTextField(".m3z");
	
	JButton		btnConvert 	= new JButton("Convert");
	
	File 		curFile;
	
	public M3ZConvertUI(File workDir, boolean removeTemp) 
	{
		super.setTitle("m3z converter");
		super.setSize(800, 480);
		super.setCenter();
		super.setResizable(false);
		super.setLayout(null);
		
		this.removeTemp = removeTemp;
		this.workDir = workDir;
		
		btnLoadPNG.setBounds(10, 10, 200, 32);
		btnLoadPNG.addActionListener(this);
		this.add(btnLoadPNG);
		
		txtLoadPNG.setBounds(10, 50, 770, 32);
		txtLoadPNG.setEditable(false);
		this.add(txtLoadPNG);
		
		lblFormats.setBounds(10, 100, 200, 32);
		this.add(lblFormats);
		cbxFormats.setBounds(10, 132, 200, 32);
		cbxFormats.setEditable(true);
		this.add(cbxFormats);
		
		lblNewExt.setBounds(250, 100, 200, 32);
		this.add(lblNewExt);
		txtNewExt.setBounds(250, 132, 200, 32);
		this.add(txtNewExt);
		
		lblScale.setBounds(10, 200, 200, 32);
		this.add(lblScale);
		numScale.setBounds(10, 232, 200, 32);
		this.add(numScale);
		
		btnConvert.setBounds(
				getWidth()-200-20, 
				getHeight()-32-40, 200, 32);
		btnConvert.addActionListener(this);
		this.add(btnConvert);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == btnLoadPNG) {
			chooseImage();
		}
		else if (arg0.getSource() == btnConvert) {
			runConvert();
		}
	}
	
	private String[] getSelectedFormats()
	{
		String selected = cbxFormats.getSelectedItem().toString();
		String[] fmts = CUtil.splitString(selected, ",");
		return fmts;
	}
	
	private void chooseImage() {
		try {
			java.awt.FileDialog fd = new FileDialog(this, 
					"load a png file",
					FileDialog.LOAD);
			fd.setLocation(getLocation());
			fd.setVisible(true);
			if (fd.getFile() != null) {
				curFile = new File(fd.getDirectory(), fd.getFile()).getCanonicalFile();
				txtLoadPNG.setText(curFile.getCanonicalPath());
			}
		} catch (Throwable e1) {
			e1.printStackTrace();
		}
	}
	
	private void runConvert()
	{
		try {
			if (curFile != null) {
				String newExt = txtNewExt.getText();
				String[] fmts = getSelectedFormats();
				if (newExt.isEmpty()) {
					newExt = ".m3z";
				}
				float scale = ((Number)numScale.getValue()).floatValue();
				M3ZConvert.convert(curFile, scale, false, fmts, newExt, workDir, removeTemp, true, false);
			} else {
				JOptionPane.showMessageDialog(this,
						"There is no input image !!!");
			}
		} catch (Throwable e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(this, 
					CUtil.getStackTrace(e1),
					e1.getMessage(),
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
}
