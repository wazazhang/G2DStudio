package com.g2d.studio.quest.group;

import java.awt.Component;

import javax.swing.JLabel;

import com.g2d.editor.property.ObjectPropertyEdit;
import com.g2d.editor.property.PropertyCellEdit;
import com.g2d.studio.Studio;
import com.g2d.studio.swing.G2DList;
import com.g2d.studio.swing.G2DListSelectDialog;

@SuppressWarnings("serial")
public class QuestGroupSelectCellEdit extends G2DListSelectDialog<DQuestGroup> implements PropertyCellEdit<Integer>
{
	JLabel cell_edit_component = new JLabel();
	
	/**
	 * @param not_transient 是否排除所有临时任务
	 */
	public QuestGroupSelectCellEdit(Component owner, Integer dv) {
		super(owner, new QuestGroupList(), 
				Studio.getInstance().getQuestGroupManager().getQuestGroup(dv));
	}

	@Override
	public Component getComponent(ObjectPropertyEdit panel) {
		DQuestGroup node = getSelectedObject();
		cell_edit_component.setText(node+"");
		return cell_edit_component;
	}
	
	@Override
	public Integer getValue() {
		DQuestGroup node = getSelectedObject();
		if (node != null) {
			return node.getIntID();
		}
		return -1;
	}

	@Override
	public void commitValue() {}
	static public class QuestGroupList extends G2DList<DQuestGroup>
	{
		public QuestGroupList() {
			super(Studio.getInstance().getQuestGroupManager().getQuestGroups());
		}
	}

}
