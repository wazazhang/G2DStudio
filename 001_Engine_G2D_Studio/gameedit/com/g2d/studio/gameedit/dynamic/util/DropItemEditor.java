package com.g2d.studio.gameedit.dynamic.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.cell.CUtil;
import com.cell.rpg.template.TItemList.UnitDropItems.DropItemNode;
import com.cell.rpg.template.TItemList.UnitDropItems.DropItems;
import com.g2d.awt.util.AbstractDialog;
import com.g2d.awt.util.AbstractOptionDialog;
import com.g2d.editor.property.ObjectPropertyEdit;
import com.g2d.editor.property.PropertyCellEdit;
import com.g2d.studio.Studio;
import com.g2d.studio.gameedit.template.XLSItem;
import com.g2d.studio.res.Res;
import com.g2d.studio.swing.G2DList;
import com.g2d.studio.swing.G2DListItem;

public class DropItemEditor extends AbstractDialog implements PropertyCellEdit<DropItems> , ActionListener
{
	private static final long serialVersionUID = 1L;
	
	JLabel 						edit_label 	= new JLabel();
	
	//
	G2DList<Node>				value_list	= new G2DList<Node>();
	Vector<Node>				node_list	= new Vector<Node>();
	JLabel						total		= new JLabel();
	
	JButton						value_add	= new JButton("添加");
	JButton						value_edt	= new JButton("编辑");
	JButton						value_del	= new JButton("移除");
	JButton 					ok			= new JButton("确定");
	
	ObjectPropertyEdit editor;
	
	public DropItemEditor(Component owner, DropItems src, ObjectPropertyEdit editor) 
	{
		super(owner);
		super.setIconImage(Res.icon_edit);
		super.setTitle("掉落道具编辑器");
		super.setSize(260, 400);
		
		this.editor = editor;
		this.add(total, BorderLayout.NORTH);
		this.add(new JScrollPane(value_list), BorderLayout.CENTER);
		{
			JPanel bp = new JPanel(new FlowLayout());
			bp.add(ok);
			bp.add(value_del);
			bp.add(value_edt);
			bp.add(value_add);
			this.add(bp, BorderLayout.SOUTH);
			
			value_add.addActionListener(this);
			value_edt.addActionListener(this);
			value_del.addActionListener(this);
			ok.addActionListener(this);
		}

		if (src!=null) {
			for (DropItemNode node : src) {
				XLSItem item = Studio.getInstance().getObjectManager().getObject(XLSItem.class, node.titem_id+"");
				if (item != null) {
					node_list.add(new Node(item, node));
				}
			}
			resetList(null);
		}
		
		value_list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int si = value_list.getSelectedIndex();
					Node node = (Node)value_list.getModel().getElementAt(si);
					new EditNodeDialog("编辑道具", node).showDialog();
					resetList(node);
				}
			}
		});
	}
	
	protected void resetList(Node edit_node)
	{
		double total_percent = 0;
		
		if (!node_list.isEmpty()) {
			for (Node e : node_list) {
				if (!e.equals(edit_node)) {
					total_percent += e.value.drop_rate_percent;
				}
			}
//			if (total_percent > max) {
//				double div = max / total_percent;
//				for (Node e : node_list) {
//					if (!e.equals(edit_node)) {
//						e.value.drop_rate_percent *= div;
//						e.value.drop_rate_percent = Math.round(e.value.drop_rate_percent * 10000) / 10000;
//						e.value.drop_rate_percent = Math.max(0, e.value.drop_rate_percent);
//					}
//				}
//			}
			total_percent = 0;
			for (Node e : node_list) {
				total_percent += e.value.drop_rate_percent;
			}
		}
		total.setText("全部几率 : " + total_percent + "%");
		
		value_list.setListData(node_list);
		value_list.repaint(100);
		
		
	}
	
	@Override
	public Component getComponent(ObjectPropertyEdit panel) {		
		StringBuffer sb = new StringBuffer();
		for (Node node : node_list) {
			sb.append(node.item.getName()+", ");
		}
		edit_label.setText(sb.toString());
//		edit_label.setText(node_list.size()+"个道具 : ");	
		return edit_label;
	}
	
	@Override
	public DropItems getValue() {
		Vector<DropItemNode> values = new Vector<DropItemNode>(node_list.size());		
		for (Node e : node_list) {
			values.add(e.value);
		}
		DropItems ret = new DropItems();
		ret.addAll(values);
		return ret;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == value_add) {
			Node node = new EditNodeDialog("选择道具", null).showDialog();
			if (node != null) {
				node_list.add(node);
				resetList(node);
				value_list.setSelectedValue(node, true);
			}
		}
		else if (e.getSource() == value_edt) {
			try{
				int si = value_list.getSelectedIndex();
				Node node = (Node)value_list.getModel().getElementAt(si);
				new EditNodeDialog("编辑道具", node).showDialog();
				resetList(node);
			}catch(Exception er){}
		}
		else if (e.getSource() == value_del) {
			try{
				int si = value_list.getSelectedIndex();
				Node node = (Node)value_list.getModel().getElementAt(si);
				node_list.remove(node);
				resetList(null);
			}catch(Exception er){}
		}
		else if (e.getSource() == ok) {
			editor.fireEditingStopped();
			this.setVisible(false);
		}
	}

	@Override
	public void commitValue() {}
	
	
	class Node extends JLabel implements G2DListItem
	{
		private static final long serialVersionUID = 1L;
		
		XLSItem			item;
		DropItemNode	value;
		
		public Node(XLSItem item, DropItemNode value) 
		{
			setFocusable(true);
			this.item		= item;
			this.value		= value;			
		}
		
		@Override
		public ImageIcon getListIcon(boolean update) {
			return item.getIcon(update);
		}
	
		@Override
		public String getListName() {
			String count = "";
			if (value.min_count != value.max_count) {
				count = " x" + value.min_count + "~" + value.max_count;
			}
			else if (value.min_count != 0) {
				count = " x" + value.min_count;
			}
			return CUtil.snapStringRightSize(
					"(" + value.drop_rate_percent + "%"+count+") ", 16, ' ') + item.getName();
		}
		
		@Override
		public Component getListComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			return null;
		}
		
		@Override
		public String toString() {
			return getName();
		}		
		
		@Override
		public void onDoubleClicked(G2DList<?> list, MouseEvent e) {}

		@Override
		public void onRightClicked(G2DList<?> list, MouseEvent e) {}

		@Override
		public void onClicked(G2DList<?> list, MouseEvent e) {}

		@Override
		public void onSelected(G2DList<?> list) {}
		
	}
	
	class EditNodeDialog extends AbstractOptionDialog<Node> implements MouseListener
	{
		private static final long serialVersionUID = 1L;

//		ObjectSelectList<XLSItem> 	item_list 	= new ObjectSelectList<XLSItem>(XLSItem.class, 10);
		G2DList<XLSItem> item_list = new G2DList<XLSItem>();
		JSpinner percent = new JSpinner(new SpinnerNumberModel(100.d, 0.d,
				100.d, 1.d));
		Node edit_node = null;
		JSpinner max_count = new JSpinner(
				new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
		JSpinner min_count = new JSpinner(
				new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
		
		public EditNodeDialog(String title, Node node) 
		{
			super(DropItemEditor.this);
			super.setIconImage(Res.icon_edit);
			super.setTitle(title);
			super.setSize(260, 400);
			super.setLocation(
					DropItemEditor.this.getLocation().x + DropItemEditor.this.getWidth(),
					DropItemEditor.this.getLocation().y);

			this.edit_node = node;
			
			this.item_list.setListData(
					Studio.getInstance().getObjectManager().getObjects(XLSItem.class));
			
			if (edit_node != null) {
				item_list.setSelectedValue(edit_node.item, true);
				percent.setValue(edit_node.value.drop_rate_percent);
				min_count.setValue(edit_node.value.min_count);
				max_count.setValue(edit_node.value.max_count);
			} else {
				percent.setValue(100.d /*/ (node_list.size()+1)*/);
			}
			percent.setPreferredSize(new Dimension(100, 24));
			min_count.setMaximumSize(new Dimension(50, 24));
			max_count.setMaximumSize(new Dimension(50, 24));
			min_count.setPreferredSize(new Dimension(50, 24));
			max_count.setPreferredSize(new Dimension(50, 24));
			
			
			
			JPanel num_panel = new JPanel(new BorderLayout());
			num_panel.add(percent, BorderLayout.CENTER);
			num_panel.add(new JLabel("百分比"), BorderLayout.WEST);

			JPanel count_pane = new JPanel(new FlowLayout());
			count_pane.add(new JLabel("Min"));
			count_pane.add(min_count);
			count_pane.add(new JLabel("~"));
			count_pane.add(new JLabel("Max"));
			count_pane.add(max_count);
			num_panel.add(count_pane, BorderLayout.SOUTH);
			
			
			this.add(num_panel, BorderLayout.NORTH);
			this.add(new JScrollPane(item_list), BorderLayout.CENTER);
			
			item_list.addMouseListener(this);
			
		}

		@Override
		protected boolean checkOK() {
			XLSItem titem = item_list.getSelectedItem();
			if (titem==null) {
				JOptionPane.showMessageDialog(this, "未选择道具类型！");
				return false;
			}
			return true;
		}
		
		@Override
		protected Node getUserObject(ActionEvent e) {
			try {
				XLSItem titem = item_list.getSelectedItem();
				if (titem!=null) {
					Number perc = (Number)percent.getValue();
					int min_c = (Integer)min_count.getValue();
					int max_c = (Integer)max_count.getValue();
					DropItemNode value = new DropItemNode(titem.getIntID(), perc.floatValue());
					value.min_count = min_c;
					value.max_count = max_c;
					if (edit_node!=null) {
						edit_node.item = titem;
						edit_node.value = value;
						return edit_node;
					} else {
						Node node = new Node(titem, value);
						return node;
					}
				}
			} catch (Exception er) {
				er.printStackTrace();
			}
			return null;
		}
		
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				Node node = getUserObject(null);
				if (node != null) {
					node_list.add(node);
					resetList(node);
					value_list.setSelectedValue(node, true);
				}
			}
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		

	}
	
	
}
