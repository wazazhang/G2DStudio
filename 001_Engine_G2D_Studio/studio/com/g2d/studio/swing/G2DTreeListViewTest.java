package com.g2d.studio.swing;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JList;

import com.cell.j2se.CAppBridge;

@SuppressWarnings("serial")
public class G2DTreeListViewTest extends G2DTreeListView<com.g2d.studio.swing.G2DTreeListViewTest.Item>
{
	public G2DTreeListViewTest() 
	{
		super(new Group("root"));
		
		NodeGroup<Item> g1 = new Group("g1");
		NodeGroup<Item> g2 = new Group("g2");
		
		for (int i = 0; i < 10; i++) {
			g1.addItem(new Item("g1_"+i));
			g2.addItem(new Item("g2_"+i));
		}
		
		getRoot().add(g1);
		getRoot().add(g2);
		
		reload();
	}
	
	static class Group extends NodeGroup<Item>
	{
		public Group(String name) {
			super(name);
		}
		
		@Override
		protected G2DTreeNodeGroup<?> createGroupNode(String name) {
			return new Group(name);
		}
	}
	
	static class Item implements G2DListItem
	{
		final String name;
		
		public Item(String name) {
			this.name = name;
		}

		@Override
		public Component getListComponent(JList list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
			return null;
		}

		@Override
		public ImageIcon getListIcon(boolean update) {
			return null;
		}

		@Override
		public String getListName() {
			return name;
		}

		@Override
		public void onDoubleClicked(G2DList<?> list, MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRightClicked(G2DList<?> list, MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onClicked(G2DList<?> list, MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSelected(G2DList<?> list) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	static public void main(final String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
            public void run() {
            	CAppBridge.init();
				new Thread() {
					public void run() {
						JFrame f = new JFrame();
						f.add(new G2DTreeListViewTest());
						f.setSize(800, 600);
						f.setVisible(true);
						f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					}
				}.start();
            }
        });
	}
}
