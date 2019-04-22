package com.cell.rpg.quest;


public abstract class QuestAbilityManager 
{
//		-------------------------------------------------------------------------
	static private QuestAbilityManager manager;
	static public void setManager(QuestAbilityManager m) {
		manager = m;
	}
	static public QuestAbilityManager getManager() {
		return manager;
	}
//		-------------------------------------------------------------------------
	/**扩展任务自身能力*/
	public Class<?>[] getQuestExtAbilities() {
		return null;
	}

	/**扩展任务接取条件*/
	public Class<?>[] getQuestTagExtAbilities() {
		return null;
	}

	/**扩展任务奖励*/
	public Class<?>[] getQuestResultExtAbilities() {
		return null;
	}

}
