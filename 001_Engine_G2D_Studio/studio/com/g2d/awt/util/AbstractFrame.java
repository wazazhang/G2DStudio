package com.g2d.awt.util;

import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JFrame;

public abstract class AbstractFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;

	public AbstractFrame() {
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public void setCenter()
	{
		setLocation(
				Toolkit.getDefaultToolkit().getScreenSize().width/2 - getWidth()/2,
				Toolkit.getDefaultToolkit().getScreenSize().height/2 - getHeight()/2);
	}
	
	public static void setCenter(Window frame) {
		frame.setLocation(
				Toolkit.getDefaultToolkit().getScreenSize().width/2 - frame.getWidth()/2,
				Toolkit.getDefaultToolkit().getScreenSize().height/2 -  frame.getHeight()/2);
	}
	
	public static int getScreenWidth() {
		return Toolkit.getDefaultToolkit().getScreenSize().width;
	}
	
	public static int getScreenHeight() {
		return Toolkit.getDefaultToolkit().getScreenSize().height;
	}
}
