package com.g2d.studio.quest;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;

import com.cell.rpg.quest.Quest;
import com.cell.rpg.quest.QuestItem;
import com.cell.util.IDFactoryInteger;
import com.g2d.awt.util.AbstractDialog;
import com.g2d.awt.util.Tools;
import com.g2d.studio.Studio;
import com.g2d.studio.gameedit.ObjectViewer;
import com.g2d.studio.gameedit.dynamic.DynamicNode;
import com.g2d.studio.gameedit.dynamic.IDynamicIDFactory;
import com.g2d.studio.gameedit.dynamic.util.IdNameDialog;
import com.g2d.studio.icon.IconFile;
import com.g2d.studio.quest.items.QuestItemNode;
import com.g2d.studio.res.Res;
import com.g2d.studio.swing.G2DList;
import com.g2d.studio.swing.G2DTreeNodeGroup.NodeMenu;

public class QuestNode extends DynamicNode<Quest>
{
	private static final long serialVersionUID = 1L;

	transient IconFile 	icon_file;
	
	final private QuestItemManager item_manager;
	
	public QuestNode(IDynamicIDFactory<QuestNode> factory, int id, String name) throws Exception {
		super(factory, id, name);
		item_manager = new QuestItemManager();
	}

	@Override
	protected Quest newData(IDynamicIDFactory<?> factory, String name, Object... args) {
		return new Quest(getIntID(), name);
	}
	
	public QuestNode(Quest data) {
		super(data, data.getIntID(), data.name);
		item_manager = new QuestItemManager();
	}
	
	@Override
	public ImageIcon getIcon(boolean update) {
		if (icon_file==null || !icon_file.getName().equals(getData())) {
			resetIcon();
		} 
		return super.getIcon(update);
	}
	
	@Override
	protected ImageIcon createIcon() {
		icon_file = Studio.getInstance().getIconManager().getIcon(getData().icon_index);
		if (icon_file!=null) {
			return new ImageIcon(Tools.combianImage(20, 20, icon_file.getImage()));
		} else {
			return new ImageIcon(Tools.combianImage(20, 20, Res.icon_quest));
		}
	}
	
	@Override
	public void onRightClicked(JTree tree, MouseEvent e) {
		new QuestMenu(tree, this).show(tree, e.getX(), e.getY());
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
		onOpenEdit();
		if (edit_component == null) {
			edit_component = new QuestEditor(this);
		}
		return edit_component;
	}
	
	public QuestItemManager getQuestItemManager() {
		return item_manager;
	}
	
//	---------------------------------------------------------------------------------------------------------------

	class QuestMenu extends NodeMenu<QuestNode>
	{
		public QuestMenu(JTree tree, QuestNode node) {
			super(node);
			super.remove(open);
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
				QuestNodeGroup group = (QuestNodeGroup)node.getParent();
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
			else if (e.getSource() == delete) 
			{
				if (JOptionPane.showConfirmDialog(tree,
						"确实要删除 \"" + node.getName() + "\" ！") == JOptionPane.YES_OPTION) {
					TreeNode parent = node.getParent();
					this.node.removeFromParent();
					if (tree != null) {
						tree.reload(parent);
					}
				}
			}
		}
	}
	
//	---------------------------------------------------------------------------------------------------------------

	public class QuestItemManager extends IDFactoryInteger<QuestItemNode> implements IDynamicIDFactory<QuestItemNode>
	{
		public QuestItemManager() 
		{
			Quest data = getData();
			
			for (QuestItem item : data.accept_condition.getItems()) {
				QuestItemNode node = new QuestItemNode(QuestNode.this, item);
				if(!super.storeID(node.getIntID(), node)){}
			}
			for (QuestItem item : data.accept_award.getItems()) {
				QuestItemNode node = new QuestItemNode(QuestNode.this, item);
				if(!super.storeID(node.getIntID(), node)){}
			}
			for (QuestItem item : data.complete_condition.getItems()) {
				QuestItemNode node = new QuestItemNode(QuestNode.this, item);
				if(!super.storeID(node.getIntID(), node)){}
			}
			for (QuestItem item : data.complete_award.getItems()) {
				QuestItemNode node = new QuestItemNode(QuestNode.this, item);
				if(!super.storeID(node.getIntID(), node)){}
			}
			for (QuestItem item : data.discard_action.getItems()) {
				QuestItemNode node = new QuestItemNode(QuestNode.this, item);
				if(!super.storeID(node.getIntID(), node)){}
			}
		}
		
		@Override
		public IDFactoryInteger<QuestItemNode> idf() {
			return this;
		}
		
//		@Override
//		public synchronized Integer createID() {
//			return super.createID();
//		}

		public synchronized QuestItemNode createQuestItem(String name, boolean is_award) {
			try {
				QuestItemNode node = new QuestItemNode(QuestNode.this, name, is_award);
				super.storeID(node.getIntID(), node);
				return node;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		public Vector<QuestItemNode> getAllNodes() {
			Vector<QuestItemNode> ret = new Vector<QuestItemNode>();
			synchronized(this) {
				for (QuestItemNode e : this) {
					ret.add(e);
				}
			}
			return ret;
		}
	}

//	---------------------------------------------------------------------------------------------------------------
	
	public static class QuestItemList extends G2DList<QuestItemNode>
	{
		public QuestItemList(int quest_id) {
			super(Studio.getInstance().getQuestManager().getQuest(quest_id).getQuestItemManager().getAllNodes());
		}
	}
	
	
//	---------------------------------------------------------------------------------------------------------------

}
