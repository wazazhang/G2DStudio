package com.g2d.studio.gameedit.dynamic.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.g2d.awt.util.AbstractOptionDialog;
import com.g2d.studio.gameedit.dynamic.IDynamicIDFactory;

public class IdNameDialog extends AbstractOptionDialog<Integer>
{
	final private IDynamicIDFactory<?> factor;
	
	JPanel center = new JPanel(null);

	JLabel lbl_id   = new JLabel("ID");
	JLabel lbl_name = new JLabel("名字");
	
	JTextField txt_id   = new JTextField("");
	JTextField txt_name = new JTextField("");
	
	public IdNameDialog(IDynamicIDFactory<?> factor, String tip) {
		super();
		super.setTitle(tip);
		this.factor = factor;
		init2();
	}
	
	public IdNameDialog(Component owner, IDynamicIDFactory<?> factor, String tip) {
		super(owner);
		super.setTitle(tip);
		this.factor = factor;
		init2();
	}
	
	public void setEnableName(boolean en) {
		lbl_name.setVisible(en);
		txt_name.setVisible(en);
	}
	
	public void setEnableID(boolean en) {
		lbl_id.setVisible(en);
		txt_id.setVisible(en);
	}
	
	public void setDefaultID(int id) {
		txt_id.setText(id+"");
	}
	
	public void setDefaultName(String name) {
		txt_name.setText(name);
	}
	
	private void init2()
	{
		super.setSize(300, 150);
		center.setMinimumSize(new Dimension(300, 150));
		{
			lbl_id	.setSize(50,  30);
			txt_id	.setSize(200, 30);
			lbl_name.setSize(50,  30);
			txt_name.setSize(200, 30);
			
			lbl_id	.setLocation(4,  4);
			txt_id	.setLocation(58, 4);
			lbl_name.setLocation(4,  40);
			txt_name.setLocation(58, 40);

			center.add(txt_id);
			center.add(txt_name);
			center.add(lbl_id);
			center.add(lbl_name);

			txt_id.setText(factor.idf().peekID()+"");
		}
		this.add(center, BorderLayout.CENTER);
	}
	
	@Override
	public void setVisible(boolean b) {
		txt_name.requestFocus();
		super.setVisible(b);
	}
	
	@Override
	protected boolean checkOK()
	{
		if (txt_name.isVisible()) {
			if (txt_name.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "名字不能为空！");
				return false;
			}
		}
		try {
			Integer newID = Integer.parseInt(txt_id.getText());
			if (!factor.idf().containsID(newID)) {
				return true;
			} else {
				JOptionPane.showMessageDialog(this, "此ID被占用！");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "非法的ID格式！");
		}
		return false;
	}

	@Override
	protected Integer getUserObject(ActionEvent e)
	{
		return Integer.parseInt(txt_id.getText());
	}
	
	public String getInputName() 
	{
		return txt_name.getText();
	}
	
}
