package com.g2d.studio.ui.edit;

import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.g2d.display.ui.layout.UILayout;
import com.g2d.display.ui.layout.UILayout.ImageStyle;
import com.g2d.studio.swing.G2DTree;
import com.g2d.studio.ui.edit.UITemplate.UITemplateGroup;
import com.g2d.studio.ui.edit.gui.UEFileNode;
import com.g2d.studio.ui.edit.gui.UERoot;

public class UITemplateList extends G2DTree
{
	private UIEdit edit;
	
	private DefaultMutableTreeNode template = new DefaultMutableTreeNode("基础控件", true);
	private HashMap<Class<?>, UITemplate> tempate_map = new HashMap<Class<?>, UITemplate>();
	
	private UITemplateGroup user_define = UITemplateGroup.createRootNode("用户控件");
	private HashMap<String, UITemplate> user_define_map = new HashMap<String, UITemplate>();
	private HashMap<File, TreeNode> user_file_map = new HashMap<File, TreeNode>();
	
	
	public UITemplateList(UIEdit edit)
	{
		super(new DefaultMutableTreeNode());
		super.setRootVisible(false);
		this.edit = edit;
		
		initTemplate();
		
		initUserDefine();

		reload();

//		expandAll();
		expand(template);
		expand(user_define);
		
		setDragEnabled(true);
		
	}
	
	public UITemplate getTemplate(Class<?> type) {
		return tempate_map.get(type);
	}

	public UITemplate getTemplateFileName(String fileName) {
		return user_define_map.get(fileName);
	}
	
	private void initTemplate() 
	{
		tempate_map.put(UERoot.class,
				new UITemplate(new UILayout(ImageStyle.NULL), UERoot.class, "root"));
		tempate_map.put(UEFileNode.class, 
				new UITemplate(new UILayout(ImageStyle.NULL), UEFileNode.class, "file"));
		for (UITemplate ut : UIEdit.getLayoutManager().getTemplates()) {
			template.add(ut);
			tempate_map.put(ut.getUIType(), ut);
		}
		getRoot().add(template);
	}
	
	private void initUserDefine() {
		initUserDefine(edit.workdir, user_define);
		getRoot().add(user_define);
	}
	
	private void initUserDefine(File dir, UITemplateGroup group) {
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				UITemplateGroup g = new UITemplateGroup(f);
				initUserDefine(f, g);
				if (g.getChildCount() > 0) {
					group.add(g);
					user_file_map.put(f, g);
				}
			}
			else if (f.getName().endsWith(".gui.xml")) {
				try {
					UITemplate ut = new UITemplate(f);
					group.add(ut);
					user_define_map.put(ut.getFullName(), ut);
					user_file_map.put(f, ut);
				} catch (Exception e) {
					System.err.println("load ui failed : " + f.getName());
					System.err.println(e.getMessage());
				}
			}
		}
	}
	
	public void reloadUserDefine() 
	{
		HashMap<File, Boolean> expanded = new HashMap<File, Boolean>();
		for (TreeNode tn : getAllNodes()) {
			if (tn instanceof UITemplateGroup) {
				UITemplateGroup g = (UITemplateGroup)tn;
				if (isExpanded(getTreePath(g))) {
					if (g.dir != null) {
						expanded.put(g.dir, true);
					}
				}
			}
		}

		user_define_map.clear();
		user_file_map.clear();
		user_define.removeAllChildren();
		initUserDefine();
		
		reload();
		
		expand(template);
		expand(user_define);
		
		try {
			for (File dir : expanded.keySet()) {
				TreeNode tn = user_file_map.get(dir);
				if (tn instanceof UITemplateGroup) {
					expand(tn);
				}
			}
		} catch (Exception e) {}
	}
	
	@Override
	protected boolean checkDrag(DropTarget evt_source, Transferable trans,
			Object src, Object dst, int position) {
		return false;
	}
	
	public void performingImages() {
		UIPerforming.newInstance().performingUserTemplates(user_define_map.values());
	}
	
	public void genLanguage() {
		UILanguages.GenLanguageFile(user_define_map.values());
	}
	
	public void loadLanguage(File langFile) {
		UILanguages.LoadLanguageFile(user_define_map.values(), langFile);
	}
}
