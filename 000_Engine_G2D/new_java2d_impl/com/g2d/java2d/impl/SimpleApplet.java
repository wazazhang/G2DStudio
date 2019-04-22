package com.g2d.java2d.impl;

import javax.swing.JApplet;

import com.g2d.display.Stage;

public class SimpleApplet extends JApplet
{	
	private SimpleCanvas canvas;
	
	public SimpleApplet(int width, int height) 
	{
		this.canvas = new SimpleCanvas(width, height);
		this.setLayout(null);
		this.add(canvas);
	}
	
	public void start(int fps, Class<? extends Stage> state_name) {
		 canvas.start(fps, state_name);
	}

	public SimpleCanvas getCanvas() {
		return canvas;
	}
	
}