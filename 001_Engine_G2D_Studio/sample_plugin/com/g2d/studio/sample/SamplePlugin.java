package com.g2d.studio.sample;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.cell.mysql.MySQLDriver;
import com.cell.rpg.item.ItemPropertyManager;
import com.cell.rpg.quest.QuestAbilityManager;
import com.cell.rpg.scene.SceneAbilityManager;
import com.cell.rpg.scene.graph.SceneGraphAstar;
import com.cell.rpg.scene.graph.SceneGraphNode;
import com.cell.util.Properties;
import com.cell.xls.XLSColumns;
import com.cell.xls.XLSFullRow;
import com.cell.xstream.XStreamAdapter;
import com.g2d.studio.Studio;
import com.g2d.studio.StudioPlugin;
import com.g2d.studio.cell.gameedit.Builder;
import com.g2d.studio.io.File;
import com.g2d.studio.sample.builder.SampleBuilder;
import com.g2d.studio.sample.entity.TPlayer;
import com.g2d.studio.sample.item.EatItemPropertyManager;
import com.g2d.studio.sample.quest.EatQuestAbilityManager;
import com.g2d.studio.sample.scene.EatSceneAbilityManager;
import com.g2d.studio.scene.entity.SceneNode;

public class SamplePlugin implements StudioPlugin
{
	public SamplePlugin() {}

	@Override
	public void init(Studio g2d, String[] args) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void openSceneScriptEditor(SceneNode scene_node) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void genSceneGraph(ArrayList<SceneGraphNode.SceneGraphPath> paths) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public ItemPropertyManager createItemPropertyManager() {
		return new EatItemPropertyManager();
	}

	@Override
	public SceneAbilityManager createSceneAbilityManager() {
		return new EatSceneAbilityManager();
	}

	@Override
	public QuestAbilityManager createQuestAbilityManager() {
		return new EatQuestAbilityManager();
	}
	
	@Override
	public Class<?> getPlayerClass() {
		return TPlayer.class;
	}

	@Override
	public Class<?> getPetClass() {
		return TPlayer.class;
	}

	@Override
	public Class<?> getNpcClass() {
		return TPlayer.class;
	}

	@Override
	public String getPersistanceManager() {
		return XStreamAdapter.class.getCanonicalName();
	}

	@Override
	public String getSQLDriver() {
		return MySQLDriver.class.getCanonicalName();
	}

	@Override
	public Builder createResourceBuilder(File g2d_root) throws IOException {
		return new SampleBuilder(g2d_root.getPath());
	}
	
	// --------------------------------------------------------------------------------------
	
	private class SimpleXLSDataSource implements XLSDataSource
	{
		@Override
		public Collection<XLSFullRow> LoadAllRows() {
			return new ArrayList<XLSFullRow>();
		}
		@Override
		public XLSColumns GetColumns() {
			return new XLSColumns();
		}
	}

	@Override
	public XLSDataSource GetItemXLS() {
		return new SimpleXLSDataSource();
	}

	@Override
	public XLSDataSource GetPetXLS() {
		return new SimpleXLSDataSource();
	}

	@Override
	public XLSDataSource GetPlayerXLS() {
		return new SimpleXLSDataSource();
	}

	@Override
	public XLSDataSource GetShopItemXLS() {
		return new SimpleXLSDataSource();
	}

	@Override
	public XLSDataSource GetSkillXLS() {
		return new SimpleXLSDataSource();
	}

	@Override
	public XLSDataSource GetUnitXLS() {
		return new SimpleXLSDataSource();
	}

}
