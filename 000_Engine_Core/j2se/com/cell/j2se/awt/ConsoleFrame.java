package com.cell.j2se.awt;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class ConsoleFrame extends JFrame
{
	ConsoleListener listener;
	
	ConsolePane		console		= ConsolePane.getInstance();
	JTextField		cmd			= new JTextField();
	Vector<String>	cmd_list		= new Vector<String>();
	int				cmd_index	= 0;
	
	
	public ConsoleFrame(ConsoleListener l) 
	{
		this.setSize(640, 480);
		this.setLayout(new BorderLayout());
		
		this.add(console, BorderLayout.CENTER);
		this.add(cmd, BorderLayout.SOUTH);
		
		this.listener = l;
		
		cmd.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar()=='\n') {
					String c = cmd.getText();
					cmd_list.remove(c);
					cmd_list.add(c);
					cmd_index = cmd_list.size();
					System.out.println(c);
					try{
						listener.textInput(c);
					}catch (Exception err) {
						err.printStackTrace();
					}
//					System.out.println(CUtil.arrayToString(cmd_list, ","));
					cmd.setText("");
				}
			}
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					if (cmd_index > 0) {
						cmd_index--;
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					if (cmd_index < cmd_list.size()-1){
						cmd_index++;
					}
				}
				
				if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
					if (cmd_index < cmd_list.size() && cmd_index >= 0) {
						String c = cmd_list.get(cmd_index);
						cmd.setText(c);
					}
				}
				
			}
		});
		
	}
	
	public ConsoleFrame() 
	{
		this(new ConsoleListener(){public void textInput(String cmd) {}});
	}
	
	public void setConsoleListener(ConsoleListener listener) {
		this.listener = listener;
	}
	
	public static void main(String[] args)
	{
		ConsoleFrame console = new ConsoleFrame();
		console.setVisible(true);
		console.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	

	public static interface ConsoleListener
	{
		public void textInput(String cmd);
	}
	
	

}
