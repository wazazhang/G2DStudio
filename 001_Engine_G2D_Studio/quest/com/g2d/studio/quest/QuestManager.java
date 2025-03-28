package com.g2d.studio.quest;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JButton;

import com.cell.rpg.quest.QuestAbilityManager;
import com.cell.rpg.quest.formula.TriggerUnitMethod;
import com.g2d.awt.util.Tools;
import com.g2d.studio.ManagerFormDynamic;
import com.g2d.studio.Studio;
import com.g2d.studio.Studio.ProgressForm;
import com.g2d.studio.gameedit.entity.IProgress;
import com.g2d.studio.io.File;
import com.g2d.studio.res.Res;

public class QuestManager extends ManagerFormDynamic
{
	private static final long serialVersionUID = 1L;
	
	QuestTreeView 			tree_view;

	JButton					btn_quest_group = new JButton();
	
//	QuestItemManager		quest_items;
	
	public QuestManager(Studio studio, ProgressForm progress) 
	{
		super(studio, progress, "任务管理器", Res.icon_quest);

		initDynamicClasses(studio);

		btn_quest_group.setText("任务编组");
		btn_quest_group.setIcon(Tools.createIcon(Res.icon_quest_group));
		btn_quest_group.addActionListener(this);
		tool_bar.add(btn_quest_group);
		
//		File items_dir	= new File(studio.project_save_path, "quests/questitems");
//		File items_list	= new File(items_dir, "questitems.list");
//		quest_items		= new QuestItemManager(studio, items_list);
		
		File quest_dir	= studio.project_save_path.getChildFile("quests");
		File quest_list	= quest_dir.getChildFile("quest.list");
		tree_view 		= new QuestTreeView(quest_list, progress);
		this.add(tree_view, BorderLayout.CENTER);
		
	}
	
	private void initDynamicClasses(Studio studio)
	{
		Class<?> player_class 	= studio.getPlugin().getPlayerClass();
		Class<?> pet_class 		= studio.getPlugin().getPetClass();
		Class<?> npc_class 		= studio.getPlugin().getNpcClass();
		QuestAbilityManager.setManager(
				studio.getPlugin().createQuestAbilityManager());
//		try {
//			player_class 	= studio.getPlugin().getPlayerClass();
			//Class.forName(StudioConfig.DYNAMIC_QUEST_PLAYER_CLASS);
//		} catch (Exception err) {
//			System.err.println("错误：未定义玩家类型");
//		}
//		try {
//			pet_class 		= Class.forName(StudioConfig.DYNAMIC_QUEST_PET_CLASS);
//		} catch (Exception err) {
//			System.err.println("错误：未定义宠物类型");
//		}
//		try {
//			npc_class 		= Class.forName(StudioConfig.DYNAMIC_QUEST_NPC_CLASS);
//		} catch (Exception err) {
//			System.err.println("错误：未定义NPC类型");
//		}
		TriggerUnitMethod.setTriggerUnitClass(
				player_class, 
				pet_class, 
				npc_class);
	}
	
	public Vector<QuestNode> getDependedQuests() {
		Vector<QuestNode> ret = new Vector<QuestNode>();
		for (QuestNode n : tree_view.getAllObject()) {
			if (!n.getData().is_transient_quest) {
				ret.add(n);
			}
		}
		return ret;
	}
	
	public Vector<QuestNode> getQuests() {
		return tree_view.getAllObject();
	}
	
	public QuestNode getQuest(int id) {
		return tree_view.getNode(id);
	}
	
//	public QuestItemManager getQuestItems() {
//		return quest_items;
//	}
	
	@Override
	public void saveAll(IProgress progress) throws Throwable {
		tree_view.saveAll(progress);
	}
	@Override
	public void saveSingle() throws Throwable {
		tree_view.saveSingle();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource() == btn_quest_group) {
			Studio.getInstance().getQuestGroupManager().setVisible(true);
		}
	}
	
}
