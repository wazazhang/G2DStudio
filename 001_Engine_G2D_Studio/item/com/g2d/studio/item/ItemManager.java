package com.g2d.studio.item;

import java.awt.BorderLayout;
import java.util.Vector;

import com.cell.rpg.item.ItemProperties;
import com.cell.rpg.item.ItemPropertyTypes;
import com.g2d.studio.ManagerFormDynamic;
import com.g2d.studio.Studio;
import com.g2d.studio.Studio.ProgressForm;
import com.g2d.studio.gameedit.entity.IProgress;
import com.g2d.studio.io.File;
import com.g2d.studio.res.Res;

public class ItemManager extends ManagerFormDynamic
{
	private static final long serialVersionUID = 1L;

	ItemPropertiesTreeView	tree_view;
	
	public ItemManager(Studio studio, ProgressForm progress) throws Exception
	{
		super(studio, progress, "道具/技能能力管理器", Res.icons_bar[5]);
	
//		if (StudioConfig.DYNAMIC_ITEM_PROPERTY_MANAGER_CLASS != null) {
//			try {
//				Class<?> properties_type = Class.forName(StudioConfig.DYNAMIC_ITEM_PROPERTY_MANAGER_CLASS);
//				ItemPropertyManager manager = (ItemPropertyManager)properties_type.newInstance();
//				ItemPropertyTypes.setItemPropertyTypes(manager);
//			} catch (Throwable e) {
//				e.printStackTrace();
//			}
//		}
		ItemPropertyTypes.setItemPropertyTypes(
				studio.getPlugin().createItemPropertyManager());
	
		File item_properties_list_file = studio.project_save_path.getChildFile("item_properties/item_properties.list");
		this.tree_view = new ItemPropertiesTreeView(item_properties_list_file, progress);
		this.add(tree_view, BorderLayout.CENTER);
	}

	public ItemPropertiesNode getNode(int id) {
		return tree_view.getNode(id);
	}
	
	public Vector<ItemPropertiesNode> getAllNodes() {
		return tree_view.getAllObject();
	}
	
	public void saveAll(IProgress progress) throws Throwable
	{
		this.tree_view.saveAll(progress);
		
	}
	@Override
	public void saveSingle() throws Throwable {
		this.tree_view.saveSingle();
	}
//	-------------------------------------------------------------------------------------------------------------------------------
	

//	-------------------------------------------------------------------------------------------------------------------------------
	
	
	
	public void copyItemProperties(ItemPropertiesNode src) {
		try {
			ItemPropertiesGroup root = (ItemPropertiesGroup)src.getParent();
			int newID = tree_view.idf().createID();
			ItemProperties data = new ItemProperties(newID, src.getData());
			ItemPropertiesNode node = new ItemPropertiesNode(data);
			tree_view.addNode(root, node);
			tree_view.getTree().reload(root);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
}
