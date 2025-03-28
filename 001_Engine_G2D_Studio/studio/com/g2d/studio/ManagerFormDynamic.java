package com.g2d.studio;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.KeyStroke;

import com.g2d.studio.Studio.ProgressForm;
import com.g2d.studio.swing.G2DWindowToolBar;



@SuppressWarnings("serial")
public abstract class ManagerFormDynamic extends ManagerForm implements ActionListener
{
	final protected G2DWindowToolBar tool_bar = new G2DWindowToolBar(this, false, false, true, true,false);

	public ManagerFormDynamic(Studio studio, ProgressForm progress, String title, Image icon) 
	{
		super(studio, progress, title, icon);
		this.tool_bar.save_s.setToolTipText("保存所选 Ctrl + S");
		this.tool_bar.save_s.registerKeyboardAction(
				this, 
				KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK), 
				JComponent.WHEN_IN_FOCUSED_WINDOW);  
		this.add(tool_bar, BorderLayout.NORTH);		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == tool_bar.save) {
				saveAll(new SaveProgressForm());
			} else if (e.getSource() == tool_bar.save_s) {
				saveSingle();
			}
		} catch (Throwable e1) {
			e1.printStackTrace();
		}
	}

}
