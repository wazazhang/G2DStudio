package com.g2d.awt.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Window;

import javax.swing.JRootPane;

import com.g2d.studio.SaveProgressBar;

public abstract class AbstractTaskFrame extends AbstractDialog implements Runnable
{
	private static final long serialVersionUID = 1L;
	
	protected SaveProgressBar progress = new SaveProgressBar();
	
	public AbstractTaskFrame(Component owner) {
		super(owner);
		init();
	}

	public AbstractTaskFrame(Window owner) {
		super(owner);
		init();
	}

	public AbstractTaskFrame() {
		super(null);
		init();
	}
	
	private void init() {
		super.setSize(500, 90);
		super.setLayout(new BorderLayout());
		super.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		super.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		super.setAlwaysOnTop(false);
		
		this.progress.setStringPainted(true);
		this.add(progress, BorderLayout.CENTER);
	}
	
	public void start() {
		new Thread(this).start();
		setVisible(true);
		setCenter();
	}
		
	

}
