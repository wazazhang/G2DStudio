package com.g2d.studio;

import javax.swing.JProgressBar;

import com.g2d.studio.gameedit.entity.IProgress;

public class SaveProgressBar extends JProgressBar implements IProgress
{	
	private static final long serialVersionUID = 1L;
	
	private String prefix;

	public SaveProgressBar() {
		this.setStringPainted(true);
	}
	
	@Override
	public void setMaximum(String prefix, int total) 
	{
		this.prefix = prefix;
		super.setMaximum(total);			
		super.setValue(0);
		super.setString(prefix + " " + (0)+"/"+super.getMaximum());
	}
	
	@Override
	public void setValue(String prefix, int n) 
	{
		this.prefix = prefix;
		super.setValue(n);
		super.setString(prefix + " " + (n) + "/" + super.getMaximum());
	}
	
	public void setPrefix(String prefix) 
	{
		this.prefix = prefix;
		super.setString(prefix + " " + getValue() + "/" + super.getMaximum());
	}
	
	@Override
	public void increment() 
	{
		increment(1);
	}

	@Override
	public void increment(int count) 
	{
		super.setValue(super.getValue() + count);
		super.setString(prefix + " " + super.getValue() + "/"
				+ super.getMaximum());
	}
	
}
