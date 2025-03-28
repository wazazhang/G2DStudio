package com.g2d.studio.gameedit.template.util;

import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.swing.JButton;

import com.cell.rpg.template.TShopItem;
import com.g2d.awt.util.Tools;
import com.g2d.studio.SaveProgressForm;
import com.g2d.studio.Studio;
import com.g2d.studio.Studio.ProgressForm;
import com.g2d.studio.gameedit.ObjectManager;
import com.g2d.studio.gameedit.ObjectManagerTree;
import com.g2d.studio.gameedit.ObjectTreeView;
import com.g2d.studio.gameedit.dynamic.DShopItemList;
import com.g2d.studio.gameedit.template.XLSShopItem;
import com.g2d.studio.res.Res;

@SuppressWarnings("serial")
public class XLSShopItemManagerTree extends ObjectManagerTree<XLSShopItem, TShopItem>
{
	private JButton 				btn_open_item_list_;
	
	private JButton					btn_refresh_;
	
//	private JProgressBar			progress_bar_ = new JProgressBar();

	private ObjectManagerTree<?, ?>	itemlist_form;
	

	public XLSShopItemManagerTree(Studio studio, ProgressForm progress, Image icon, ObjectTreeView<XLSShopItem, TShopItem> tree_view) 
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
		
		btn_open_item_list_	= new JButton();
		itemlist_form	= manager.getPageForm(DShopItemList.class);
		tool_bar.addSeparator();
		{
			btn_open_item_list_.setText(
					itemlist_form.getTitle());
			btn_open_item_list_.setToolTipText(
					"打开\""+itemlist_form.getTitle()+"\"");
			btn_open_item_list_.setIcon(
					Tools.createIcon(itemlist_form.getIconImage()));
			btn_open_item_list_.addActionListener(this);
			
			int dw = 200, dh = 50;
			itemlist_form.setLocation(
					itemlist_form.getX() + dw, 
					itemlist_form.getY() + dh);
			itemlist_form.setSize(
					itemlist_form.getWidth()- dw, 
					itemlist_form.getHeight()-dh);
		}
		tool_bar.add(btn_open_item_list_);
	}	
	
	
	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();
		
		if (source == btn_open_item_list_) 
		{
			itemlist_form.setVisible(true);
		} 
		else if (source == btn_refresh_)
		{
			new Thread(
					new Runnable() 
					{				
						@Override
						public void run() 
						{
							XLSShopItemManagerTree.this.tree_view.refresh(new SaveProgressForm());					
						}
			}).start();
		}		
		else 
		{
			super.actionPerformed(e);
		}
	}
	
}; // class



