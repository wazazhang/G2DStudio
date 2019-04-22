package com.g2d.studio.sample;


import java.io.IOException;

import com.cell.mysql.MySQLDriver;
import com.cell.rpg.item.ItemPropertyManager;
import com.cell.rpg.quest.QuestAbilityManager;
import com.cell.rpg.scene.SceneAbilityManager;
import com.cell.xstream.XStreamAdapter;
import com.g2d.studio.StudioPlugin;
import com.g2d.studio.cell.gameedit.Builder;
import com.g2d.studio.io.File;
import com.g2d.studio.sample.builder.SampleBuilder;
import com.g2d.studio.sample.builder.SimpleBuilder;
import com.g2d.studio.sample.entity.TPlayer;
import com.g2d.studio.sample.item.EatItemPropertyManager;
import com.g2d.studio.sample.quest.EatQuestAbilityManager;
import com.g2d.studio.sample.scene.EatSceneAbilityManager;

public class SimplePlugin extends SamplePlugin
{
	@Override
	public Builder createResourceBuilder(File g2d_root) throws IOException {
		return new SimpleBuilder(g2d_root.getPath());
	}
	
	
}
