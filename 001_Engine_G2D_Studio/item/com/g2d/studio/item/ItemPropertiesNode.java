package com.g2d.studio.item;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;

import com.cell.rpg.item.ItemProperties;
import com.g2d.awt.util.AbstractDialog;
import com.g2d.studio.Studio;
import com.g2d.studio.gameedit.ObjectViewer;
import com.g2d.studio.gameedit.dynamic.DynamicNode;
import com.g2d.studio.gameedit.dynamic.IDynamicIDFactory;
import com.g2d.studio.gameedit.dynamic.util.IdNameDialog;
import com.g2d.studio.swing.G2DTreeNodeGroup.NodeMenu;

public class ItemPropertiesNode extends DynamicNode<ItemProperties>
{
	private static final long serialVersionUID = 1L;

	public ItemPropertiesNode(
			IDynamicIDFactory<ItemPropertiesNode> factory, 
			int id, 
			String name) throws Exception {
		super(factory, id, name);
	}

	@Override
	protected ItemProperties newData(IDynamicIDFactory<?> factory, String name, Object... args) {
		return new ItemProperties(getIntID(), name);
	}
	
	public ItemPropertiesNode(ItemProperties data) {
		super(data, data.getIntID(), data.name);
	}
	
	@Override
	public void onRightClicked(JTree tree, MouseEvent e) {
		new ItemPropertiesMenu(tree, this).show(tree, e.getX(), e.getY());
	}
	
	@Override
	public boolean setName(String name) {
		if(super.setName(name)){
			getData().name = name;
			return true;
		}
		return false;
	}
	
	@Override
	public ObjectViewer<?> getEditComponent() {
		if (edit_component == null) {
			edit_component = new ItemPropertiesEditor(this);
		}
		return edit_component;
	}
	
//	---------------------------------------------------------------------------------------------------------------

	class ItemPropertiesMenu extends NodeMenu<ItemPropertiesNode>
	{
		public ItemPropertiesMenu(JTree tree, ItemPropertiesNode node) {
			super(node);
			super.remove(open);
			super.add(copy);
			super.add(resetid);
			super.remove(delete);
			super.add(delete);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if (e.getSource() == rename) 
			{
				String name = JOptionPane.showInputDialog(
						AbstractDialog.getTopWindow(getInvoker()), 
						"输入名字！",
						node.getData().name);
				if (name!=null && name.length()>0) {
					node.setName(name);
				}
				if (tree!=null) {
					tree.reload(node);
				}
			}
			else if (e.getSource() == resetid) 
			{
				ItemPropertiesGroup group = (ItemPropertiesGroup)node.getParent();
				IdNameDialog dialog = new IdNameDialog(
						group.view, group.view, "输入ID！");
				dialog.setEnableName(false);
				dialog.setDefaultID(node.getIntID());
				Integer newID = dialog.showDialog();
				if (newID != null) {
					try {
						group.view.resetID(node, newID);
					} catch (Exception err) {
						err.printStackTrace();
					}
				}
			}
			else if (e.getSource() == copy) 
			{
				Studio.getInstance().getItemManager().copyItemProperties(
						ItemPropertiesNode.this);
			}
			else if (e.getSource() == delete) 
			{
				if (JOptionPane.showConfirmDialog(tree,
						"确实要删除 \"" + node.getName() + "\" ！") == JOptionPane.YES_OPTION) {
					TreeNode parent = node.getParent();
					this.node.removeFromParent();
					if (tree!=null) {
						tree.reload(parent);
					}
				}
			}
		}
	}
	
//	---------------------------------------------------------------------------------------------------------------

	
}
