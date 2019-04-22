package com.g2d.awt.util;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.cell.CObject;
import com.cell.io.CFile;
import com.g2d.studio.swing.G2DWindowToolBar;

@SuppressWarnings("serial")
public class TextEditorDialog extends AbstractOptionDialog<String>
{
	protected JTextPane text_pane = new JTextPane();
	protected G2DWindowToolBar tools = new G2DWindowToolBar(this, 			false, false, true, false,false);

	public TextEditorDialog() 
	{	
		super.setSize(640, 480);
		super.add(tools, BorderLayout.NORTH);
		super.add(new JScrollPane(text_pane), BorderLayout.CENTER);
	}

	public void setText(String text) {
		text_pane.setText(text);
	}

	@Override
	protected boolean checkOK() {
		return true;
	}
	
	@Override
	protected String getUserObject(ActionEvent e) {
		return text_pane.getText();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == tools.save) {
			saveToFile();
		}
		super.actionPerformed(e);
	}
	
	public void saveToFile()
	{
		java.awt.FileDialog fd = new FileDialog(this, 
				"保存",
				FileDialog.SAVE);
		fd.setLocation(getLocation());
		fd.setVisible(true);
		if (fd.getFile() != null) {
			try {
				String file = fd.getDirectory() + fd.getFile();
				File xmlfile = new File(file).getCanonicalFile();
				CFile.writeText(xmlfile, text_pane.getText());
			} catch (Throwable e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, 
						"非法的格式!\n" + e.getMessage());
			}
		}
	}
}
