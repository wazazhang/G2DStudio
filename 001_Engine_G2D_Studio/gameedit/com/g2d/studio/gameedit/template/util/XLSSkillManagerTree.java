package com.g2d.studio.gameedit.template.util;

import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.swing.JButton;

import com.cell.rpg.template.TSkill;
import com.g2d.awt.util.Tools;
import com.g2d.studio.SaveProgressForm;
import com.g2d.studio.Studio;
import com.g2d.studio.Studio.ProgressForm;
import com.g2d.studio.gameedit.ObjectManager;
import com.g2d.studio.gameedit.ObjectManagerTree;
import com.g2d.studio.gameedit.ObjectTreeView;
import com.g2d.studio.gameedit.template.XLSSkill;
import com.g2d.studio.res.Res;







public class XLSSkillManagerTree extends ObjectManagerTree<XLSSkill, TSkill> 
{
	private JButton					btn_refresh_;
	
//	private JProgressBar			progress_bar_ = new JProgressBar();
	
	
	private ObjectManagerTree<?, ?>	itemlist_form;
		
	
	public XLSSkillManagerTree(Studio studio, ProgressForm progress, Image icon, ObjectTreeView<XLSSkill, TSkill> tree_view) 
	{
		super(studio, progress, icon, tree_view);
		
//		progress_bar_.setStringPainted(true);
//		this.add(progress_bar_, BorderLayout.SOUTH);
	}
	
	
	
	@Override
	protected void initToolbars(ObjectManager manager) 
	{
		super.initToolbars(manager);
		
		
		btn_refresh_ = new JButton();
		btn_refresh_.setToolTipText("刷新");
		btn_refresh_.setIcon(Tools.createIcon(Res.icon_refresh));
		btn_refresh_.addActionListener(this);
		tool_bar.add(btn_refresh_);
		
		
		tool_bar.addSeparator();
	}	
	
	
	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();
		
		if (source == btn_refresh_)
		{
			new Thread(
					new Runnable() 
					{				
						@Override
						public void run() 
						{
							XLSSkillManagerTree.this.tree_view.refresh(new SaveProgressForm());					
						}
			}).start();
		}
		else 
		{
			super.actionPerformed(e);
		}
	}

}; // class



