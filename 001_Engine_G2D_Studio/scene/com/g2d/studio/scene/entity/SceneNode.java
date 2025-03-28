package com.g2d.studio.scene.entity;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTree;

import com.cell.rpg.scene.Scene;
import com.cell.rpg.template.TemplateNode;
import com.g2d.awt.util.Tools;
import com.g2d.studio.Studio;
import com.g2d.studio.cpj.CPJIndex;
import com.g2d.studio.cpj.CPJResourceType;
import com.g2d.studio.cpj.entity.CPJFile;
import com.g2d.studio.cpj.entity.CPJWorld;
import com.g2d.studio.gameedit.dynamic.DynamicNode;
import com.g2d.studio.gameedit.dynamic.IDynamicIDFactory;
import com.g2d.studio.scene.editor.SceneEditor;
import com.g2d.studio.swing.G2DTreeNodeGroup.NodeMenu;

final public class SceneNode extends DynamicNode<Scene>
{
	private CPJIndex<CPJWorld> world_index;
	transient CPJWorld world_display;
	transient AtomicReference<SceneEditor> world_editor = new AtomicReference<SceneEditor>();	
	
	public SceneNode(IDynamicIDFactory<?> factory, 
			String name,
			CPJIndex<CPJWorld> world_index) throws Exception
	{
		super(factory, factory.idf().createID(), name);
		this.world_index = world_index;
		this.bind_data.scene_node = new com.cell.rpg.display.SceneNode(
				world_index.cpj_file_name,
				world_index.set_object_name);
		this.bind_data.scene_node.width		= world_index.getObject().getSetObject().Width;
		this.bind_data.scene_node.height	= world_index.getObject().getSetObject().Height;
	}

	public SceneNode(Scene scene) {
		super(scene, Integer.parseInt(scene.getID()), scene.name);
		this.world_index = new CPJIndex<CPJWorld>(
				CPJResourceType.WORLD, 
				scene.scene_node.cpj_project_name, 
				scene.scene_node.cpj_object_id);
		System.out.println("load a scene : " + scene.name + "   (" + scene.getID() + ")");
//		try {
//			CPJWorld res_world = Studio.getInstance().getCPJResourceManager().getNode(world_index);
//			this.bind_data.scene_node.width		= res_world.getSetObject().Width;
//			this.bind_data.scene_node.height	= res_world.getSetObject().Height;
//		} catch(Exception err) {
//			err.printStackTrace();
//			throw new RuntimeException("场景\""+scene.name+"("+scene.id+")\"读取错误！", err);
//		}
	}
	
	@Override
	protected Scene newData(IDynamicIDFactory<?> factory, String name, Object... args) {
		return new Scene(getIntID(), name);
	}
	
	public void setResource(CPJWorld cpj) {
		this.world_index = Studio.getInstance().getCPJResourceManager()
				.getNodeIndex(cpj);
		this.bind_data.scene_node = new com.cell.rpg.display.SceneNode(
				world_index.cpj_file_name, world_index.set_object_name);
		this.bind_data.scene_node.width = world_index.getObject()
				.getSetObject().Width;
		this.bind_data.scene_node.height = world_index.getObject()
				.getSetObject().Height;
		resetResource(cpj.getCPJFile());
		getIcon(true);
	}
	
	public String getResourceName() {
		return world_index.set_object_name;
	}
	
	@Override
	public boolean setName(String name) {
		if(super.setName(name)){
			getData().name = name;
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected ImageIcon createIcon() {
		if (getWorldDisplay()!=null) {
			ImageIcon ico = getWorldDisplay().getIcon(false);
			if (ico != null) {
				return Tools.createIcon(Tools.combianImage(60, 40, ico.getImage()));
			}
		}
		return null;
	}
	
	public CPJWorld getWorldDisplay() {
		if (world_display==null) {
			System.out.println("load world display : " + world_index.set_object_name);
			this.world_display = Studio.getInstance().getCPJResourceManager().getNode(world_index);
			if (this.world_display != null && world_display.getSetObject() != null) {
				this.bind_data.scene_node.width		= world_display.getSetObject().Width;
				this.bind_data.scene_node.height	= world_display.getSetObject().Height;
			}
		}
		return world_display;
	}

	public void saveSnapshot(BufferedImage image)
	{
		if (getWorldDisplay()!=null) {
			getWorldDisplay().saveSnapshot(image);
			getIcon(true);
		}
	}
	
	public void resetResource(CPJFile cpj) {
		if (cpj.getName().equals(world_index.cpj_file_name)) {
			SceneEditor editor = this.world_editor.get();
			if (editor != null) {
				editor.setVisible(false);
				editor.dispose();
				world_editor.set(null);
			}
			if (world_display != null) {
				this.world_display = null;
			}
		}
	}
	
	public SceneEditor getSceneEditor() {
		onOpenEdit();
		synchronized (world_editor) {
			if (world_editor.get() == null) {
				SceneEditor se = new SceneEditor(this);
				se.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						world_editor.set(null);
					}
				});
				world_editor.set(se);
			} else {
			}
			return world_editor.get();
		}
	}
	
	@Override
	public Component getListComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		return null;
	}
	

}
