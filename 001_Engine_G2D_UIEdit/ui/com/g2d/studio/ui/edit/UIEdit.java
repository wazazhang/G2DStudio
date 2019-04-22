package com.g2d.studio.ui.edit;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import com.cell.CIO;
import com.cell.CObject;
import com.cell.CUtil;
import com.cell.io.CFile;
import com.cell.j2se.CAppBridge;
import com.cell.j2se.CStorage;
import com.cell.net.http.HttpQueryListener;
import com.cell.net.http.HttpRequester;
import com.cell.net.http.HttpResponse;
import com.g2d.Color;
import com.g2d.awt.util.AbstractFrame;
import com.g2d.awt.util.Tools;
import com.g2d.awt.util.celledit.CellTilesList;
import com.g2d.editor.property.PopupCellEditUILayout;
import com.g2d.java2d.impl.AwtEngine;
import com.g2d.studio.Studio;
import com.g2d.studio.res.Res;
import com.g2d.studio.swing.G2DTree;
import com.g2d.studio.swing.G2DTreeNode;
import com.g2d.studio.swing.G2DWindowToolBar;
import com.g2d.studio.ui.edit.gui.UERoot;

public class UIEdit extends AbstractFrame implements ActionListener
{	
	final public static int SPLIT_VIEW_MIN_SIZE = 320;
	private static UIEdit instance;
	private static UILayoutManager manager;
	
	public static UIEdit getInstance() {
		return instance;
	}
	public static UILayoutManager getLayoutManager() {
		return manager;
	}

	final public File workdir;
	final public File resdir;
	final public File languagedir;
	final public static AtomicReference<Color> backcolor = new AtomicReference<Color>(Color.BLACK);
	
	private JButton tool_undo	 		= new JButton(new ImageIcon(Res.undo));
	private JButton tool_redo	 		= new JButton(new ImageIcon(Res.redo));
	private JButton tool_zoom_in	 		= new JButton(new ImageIcon(Res.plus));
	private JButton tool_zoom_out	 		= new JButton(new ImageIcon(Res.minus));

	private JCheckBox tool_grid 			= new JCheckBox("按格对齐", false); // Tools.createIcon(Res.icon_grid),
	private JCheckBox tool_auto_select 		= new JCheckBox("自动选取", true);
	private JCheckBox tool_not_drag_move 	= new JCheckBox("不可拖动", false);
	private JCheckBox tool_show_bounds	 	= new JCheckBox("显示额外信息", true);
	private JCheckBox tool_show_effect	 	= new JCheckBox("显示特效", true);
	private JCheckBox chk_render_time	 	= new JCheckBox("追踪效能", false);
	private JButton tool_backcolor	 		= new JButton("背景颜色");
	private JSpinner tool_grid_size 		= new JSpinner(new SpinnerNumberModel(8, 2, 100, 1));
	private JButton tool_run_transition		= new JButton(new ImageIcon(Res.icon_camera));
	
	private G2DWindowToolBar tools;
	
	private JToolBar bar_status;
	private JProgressBar bar_save_progress = new JProgressBar();

	private JButton tool_refresh_template = new JButton(Tools.createIcon(Res.icon_refresh));
	private JButton tool_performing_template = new JButton(Tools.createIcon(Res.icon_event));
	private JButton tool_font_template = new JButton(Tools.createIcon(Res.icon_action));
	private UITemplateList templates;
	
	private JTabbedPane filesTabb = new JTabbedPane();

	private JMenuItem 	menu_item_language		= new JMenuItem("生成语言模板");
	private JMenu 		menu_load_lang			= new LoadLanguageMenu();
	
	public UIEdit(File cfg) throws Exception 
	{
		cfg = cfg.getCanonicalFile();
		UIEditConfig.load	(cfg.getPath());
		Locale.setDefault	(Locale.SIMPLIFIED_CHINESE);
		this.workdir 		= cfg.getParentFile();
		this.resdir	 		= new File(workdir, "res");
		this.languagedir	= new File(workdir, "languages");
		instance 			= this;
		manager 			= new UILayoutManager(this);
		
		this.setTitle(workdir.getPath());
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		this.setSize(
				AbstractFrame.getScreenWidth() - 200, 
				AbstractFrame.getScreenHeight() - 200);
		this.setCenter();
	
		
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		{
			tool_refresh_template.setToolTipText("刷新模板");
			tool_refresh_template.addActionListener(this);
			tool_performing_template.setToolTipText("分析");
			tool_performing_template.addActionListener(this);
			
			JToolBar temptoolbar = new JToolBar();
			temptoolbar.add(new JLabel("标准控件"));
			temptoolbar.add(tool_refresh_template);
			temptoolbar.add(tool_performing_template);
			temptoolbar.setFloatable(false);
			
			templates = new UITemplateList(this);
			JScrollPane tempscroll = new JScrollPane(templates);
			JPanel tppan = new JPanel(new BorderLayout());
			tppan.add(temptoolbar, BorderLayout.NORTH);
			tppan.add(tempscroll, BorderLayout.CENTER);
			tempscroll.setPreferredSize(new Dimension(UIEdit.SPLIT_VIEW_MIN_SIZE, 300));
			tempscroll.setMinimumSize(new Dimension(UIEdit.SPLIT_VIEW_MIN_SIZE, 300));
			split.setLeftComponent(tppan);
		}
		{
			FilesTabbedAdapter adapter = new FilesTabbedAdapter();
			filesTabb.addChangeListener(adapter);
			filesTabb.addMouseListener(adapter);
			filesTabb.addPropertyChangeListener(adapter);
		}
		split.setRightComponent(filesTabb);
		{
			tools = new G2DWindowToolBar(this, true, true, true, false, true);
			tools.setFloatable(false);
			{
				tools.add(tool_undo);
				tools.add(tool_redo);
				tool_undo.setToolTipText("撤销 Ctrl + Z");
				tool_redo.setToolTipText("重做 Ctrl + Y");
				tool_undo.setEnabled(false);
				tool_redo.setEnabled(false);		
				tool_undo.addActionListener(this);
				tool_redo.addActionListener(this);
				tool_undo.registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK),
						JComponent.WHEN_IN_FOCUSED_WINDOW);
				tool_redo.registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_MASK),
						JComponent.WHEN_IN_FOCUSED_WINDOW);
			}
			tools.addSeparator();
			{
				tools.add(tool_zoom_in);
				tools.add(tool_zoom_out);
				tool_zoom_in.setToolTipText("放大 Ctrl + 滚轮向上");
				tool_zoom_out.setToolTipText("缩小 Ctrl + 滚轮向下");
				tool_zoom_in.addActionListener(this);
				tool_zoom_out.addActionListener(this);
			}
			tools.addSeparator();
			tools.add(tool_auto_select);
			tools.add(tool_not_drag_move);
			tools.add(tool_show_bounds);
			tools.add(tool_show_effect);
			tools.addSeparator();
			tools.add(tool_grid);
			tools.add(tool_grid_size);
			tools.addSeparator();
			tools.add(tool_backcolor);
			tools.addSeparator();
			tools.add(chk_render_time);
			tools.add(tool_run_transition);
			tools.save_to.setToolTipText("另存为");
			tool_run_transition.setToolTipText("运行");
			tools.addSeparator();
			tools.add(tool_font_template);
			tool_font_template.setToolTipText("文字样式模板");
			
			tool_grid_size.setValue(8);
			tool_grid_size.setPreferredSize(new Dimension(50, 25));
			tool_grid_size.setToolTipText("对其到网格");
			
			tool_backcolor.addActionListener(this);
			tool_run_transition.addActionListener(this);
			tool_font_template.addActionListener(this);
			
			this.tools.save.setToolTipText("保存  Ctrl + S");
			this.tools.save.registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK),
					JComponent.WHEN_IN_FOCUSED_WINDOW);
		}
		{
			bar_status = new JToolBar();
			bar_status.setFloatable(false);
			bar_status.add(bar_save_progress);
			
		}
		
		{
			JMenu menu = new JMenu("工具");
			{
				menu_item_language.addActionListener(this);
				menu.add(menu_item_language);
				menu.add(menu_load_lang);
			}
			JMenuBar jb = new JMenuBar();
			jb.setUI(null);
			jb.add(menu);
			tools.addSeparator();
			tools.add(jb);
			tools.addSeparator();
		}
		
		this.add(tools, 		BorderLayout.NORTH);
		this.add(split, 		BorderLayout.CENTER);
		this.add(bar_status, 	BorderLayout.SOUTH);

		new UIFontStyleEditor();
		
		this.addWindowListener(new WindowListener() {
		    public void windowOpened(WindowEvent e) {}
		    public void windowClosing(WindowEvent e) {
				int option = JOptionPane.showConfirmDialog(UIEdit.this, "确定要退出?", "确定要退出", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					UIEdit.this.dispose();
 				}
			}
		    public void windowClosed(WindowEvent e){}
		    public void windowIconified(WindowEvent e){}
		    public void windowDeiconified(WindowEvent e){}
		    public void windowActivated(WindowEvent e){}
		    public void windowDeactivated(WindowEvent e){}
		});
	}
	
	public UITemplateList getTemplates() {
		return templates;
	}

	public UITemplate getTemplate(Class<?> type) {
		return templates.getTemplate(type);
	}
	
	public UITemplate getUserTemplate(String fileName) {
		return templates.getTemplateFileName(fileName);
	}
	
	public UITemplate getSelectedTemplate() {
		if (templates.getSelectedNode() instanceof UITemplate) {
			UITemplate ut = (UITemplate)templates.getSelectedNode();
			if (ut != null) {
				return ut;
			}
		}
		return null;
	}

	public InputStream getStream(String sub_file) {
		return CIO.getInputStream(workdir.getPath() + "/" + sub_file);
	}
	
	public File getSubFile(String sub_file) {
		return new File(workdir, sub_file);
	}

	public boolean isToolGridEnable() {
		return tool_grid.isSelected();
	}
	
	public boolean isToolAutoSelect() {
		return tool_auto_select.isSelected();
	}
	
	public boolean isToolDragMove() {
		return !tool_not_drag_move.isSelected();
	}

	public boolean isToolShowBounds() {
		return tool_show_bounds.isSelected();
	}
	
	public boolean isToolShowEffect() {
		return tool_show_effect.isSelected();
	}
	
	public boolean isDebugRenderTime() {
		return chk_render_time.isSelected();
	}
	
	public int getGridSize() {
		return ((Number)tool_grid_size.getValue()).intValue();
	}

	// tool 
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == tools.new_) {
			newFile();
		} else if (e.getSource() == tools.save) {
			saveFile(false);
		} else if (e.getSource() == tools.save_to) {
			saveFile(true);
		} else if (e.getSource() == tools.load) {
			loadFile();
		} else if (e.getSource() == tool_backcolor) {
			setBackColor();
		} else if (e.getSource() == tool_refresh_template) {
			templates.reloadUserDefine();
		} else if (e.getSource() == tool_performing_template) {
			templates.performingImages();
		} else if (e.getSource() == menu_item_language) {
			templates.genLanguage();
		} else if (e.getSource() == tool_run_transition) {
			UIEditFileNode node = getCurrentFileNode();
			if (node != null) {
				node.runTransition();
			}
		} else if (e.getSource() == tool_font_template) {
			UIFontStyleEditor.getInstance().ShowTemplateEditor();
		} else if (e.getSource() == tool_undo) {
			UIEditFileNode fn = getCurrentFileNode();
			if (fn != null) {
				fn.undo();
				refreshUndo(fn);
			}
		} else if (e.getSource() == tool_redo) {
			UIEditFileNode fn = getCurrentFileNode();
			if (fn != null) {
				fn.redo();
				refreshUndo(fn);
			}
		} else if (e.getSource() == tool_zoom_in) {
			UIEditFileNode fn = getCurrentFileNode();
			if (fn != null) {
				fn.getUIStage().zoomIn();
			}
		} else if (e.getSource() == tool_zoom_out) {
			UIEditFileNode fn = getCurrentFileNode();
			if (fn != null) {
				fn.getUIStage().zoomOut();
			}
		}
	}
	
	private void refreshUndo(UIEditFileNode fn) {
		tool_redo.setEnabled(fn.hasRedo());
		tool_undo.setEnabled(fn.hasUndo());
		fn.start();
		//tool_redo.setToolTipText(fn.redoCount()+"");
		//tool_undo.setToolTipText(fn.undoCount()+"");
	}
	
	public void setBackColor() {
		java.awt.Color jcolor = JColorChooser.showDialog(this, "选择背景色",
				new java.awt.Color(backcolor.get().getARGB(), false));
		if (jcolor != null) {
			backcolor.set(new Color(jcolor.getRGB()));
			PopupCellEditUILayout.back_color = backcolor.get();
			CellTilesList.back_color = backcolor.get();
		}
	}
	
	public int getCurrentFileIndex() {
		return filesTabb.getSelectedIndex();
	}
	public UIEditFileNode getCurrentFileNode() {
		int index = filesTabb.getSelectedIndex();
		if (index >= 0) {
			return (UIEditFileNode)filesTabb.getComponentAt(index);
		}
		return null;
	}
	public int getFileIndex(UIEditFileNode fn) {
		for (int i = 0; i < filesTabb.getComponentCount(); i++) {
			if (filesTabb.getComponentAt(i) == fn) {
				return i;
			}
		}
		return -1;
	}
	public UIEditFileNode getFileTabble(File xmlfile) {
		for (int i=filesTabb.getTabCount()-1; i>=0; --i) {
			UIEditFileNode nd = (UIEditFileNode)filesTabb.getComponentAt(i);
			if (xmlfile.equals(nd.getFile())) {
				return nd;
			}
		}
		return null;
	}
	public ArrayList<UIEditFileNode> getOpendFiles(){
		return getOpendFiles(0, null);
	}
	public ArrayList<UIEditFileNode> getOpendFiles(int d, UIEditFileNode fn) {
		ArrayList<UIEditFileNode> ret = new ArrayList<UIEditFileNode>();
		if (d == 0) {
			for (int i = filesTabb.getTabCount() - 1; i >= 0; --i) {
				UIEditFileNode nd = (UIEditFileNode) filesTabb.getComponentAt(i);
				ret.add(nd);
			}
		} else if (d < 0) {
			int index = filesTabb.indexOfComponent(fn);
			for (int i = index - 1; i >= 0; --i) {
				UIEditFileNode nd = (UIEditFileNode) filesTabb.getComponentAt(i);
				ret.add(nd);
			}
		} else if (d > 0) {
			int index = filesTabb.indexOfComponent(fn);
			for (int i = index + 1; i < filesTabb.getTabCount(); ++i) {
				UIEditFileNode nd = (UIEditFileNode) filesTabb.getComponentAt(i);
				ret.add(nd);
			}
		}
		return ret;
	}
	public boolean existOpened(File xmlfile) {
		try {
			xmlfile = xmlfile.getCanonicalFile();
			for (int i=filesTabb.getTabCount()-1; i>=0; --i) {
				UIEditFileNode nd = (UIEditFileNode)filesTabb.getComponentAt(i);
				if (xmlfile.equals(nd.getFile())) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public UIEditFileNode selectFileTabble(File xmlfile) {
		UIEditFileNode fn = getFileTabble(xmlfile);
		if (fn != null) {
			filesTabb.setSelectedComponent(fn);
			refreshUndo(fn);
		}
		return fn;
	}
	public void selectFileTabble(UIEditFileNode uefn) {
		filesTabb.setSelectedComponent(uefn);
		refreshUndo(uefn);
	}
	public void addFileTabble(UIEditFileNode uefn) {
		filesTabb.addTab(uefn.getPageTitle(), uefn);
		int index = filesTabb.indexOfComponent(uefn);
		filesTabb.setTabComponentAt(index, uefn.getTabComponent());
		filesTabb.setSelectedComponent(uefn);
		refreshUndo(uefn);
	}
	public void removeFileTabble(UIEditFileNode uefn) {
		int index = filesTabb.indexOfComponent(uefn);
		if (index >= 0) {
			uefn.stop();
			filesTabb.removeTabAt(index);
		}
		if (index < filesTabb.getTabCount()) {
			UIEditFileNode newIndex = (UIEditFileNode)filesTabb.getComponentAt(index);
			this.selectFileTabble(newIndex);
		}
	}
	public void loadFileTabble(File xmlfile) {
		try {
			if (!existOpened(xmlfile)) {
				UIEditFileNode fn = new UIEditFileNode(xmlfile);
				fn.loadFile();
				addFileTabble(fn);
			} else {
				selectFileTabble(xmlfile);
//				JOptionPane.showMessageDialog(this, 
//						"已经打开\"" + xmlfile.getName() + "\"!");
			}
		} catch (Throwable e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(this, "非法的格式!\n" + e1.getMessage());
		}
	}
	
	private void newFile() {
		try {
			UIEditFileNode added = new UIEditFileNode(null);
			addFileTabble(added);
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(this, 
					"非法的格式!\n" + e1.getMessage());
		}
	}
	public void dataChanged(UIEditFileNode fn) {
		int index = getFileIndex(fn);
		if (index >= 0) {
			//filesTabb.setBackgroundAt(index, fn.getPageColor());
			filesTabb.setTitleAt(index, fn.getPageTitle());
		}
		if (fn == getCurrentFileNode()) {
			refreshUndo(fn);
		}
	}
	private void saveFile(boolean saveAs)
	{
		UIEditFileNode fn = getCurrentFileNode();
		if (fn != null) {
			fn.saveFile(saveAs);
			filesTabb.setTitleAt(getCurrentFileIndex(), fn.getPageTitle());
			templates.reloadUserDefine();
		}
	}
	
	private void loadFile()
	{
		try {
			java.awt.FileDialog fd = new FileDialog(this, 
					"打开文件",
					FileDialog.LOAD);
			fd.setLocation(getLocation());
			fd.setDirectory(workdir.getCanonicalPath());
			fd.setFilenameFilter(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					if (name.toLowerCase().endsWith(".gui.xml")) {
						return true;
					}
					return false;
				}
			});
			fd.setVisible(true);
			if (fd.getFile() != null) {
				try {
					String file = fd.getDirectory() + fd.getFile();
					File xmlfile = new File(file).getCanonicalFile();
					loadFileTabble(xmlfile);
				} catch (Throwable e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(this, 
							"非法的格式!\n" + e.getMessage());
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	
	}

	
	public UERoot loadFileNode(UIEditFileNode parent, File file) throws Exception
	{
		if (CFile.isChild(workdir, file)) {
			File xmlfile = file.getCanonicalFile();
			FileInputStream fis = new FileInputStream(xmlfile);
			try {
				UITreeNode tree_root = new UITreeNode(parent,
						getTemplate(UERoot.class), 
						xmlfile.getName());
				tree_root.fromXMLStream(parent, fis);
				tree_root.removeAllChildren();
				return (UERoot)tree_root.getDisplay();
			} finally {
				fis.close();
			}
		}
		return null;
	}
	
	public void showPageMenu(UIEditFileNode node, int x, int y) {
		java.awt.Point sp = this.getLocationOnScreen();
		new PageMenu(node).show(this, x - sp.x, y - sp.y);
		this.revalidate();
	}
	
	private class LoadLanguageMenu extends JMenu implements MenuListener, ActionListener
	{
		private static final long serialVersionUID = 1L;
		private HashMap<JMenuItem, File> submap = new HashMap<JMenuItem, File>();
		
		private LoadLanguageMenu() 
		{
			super("读取语言文件");
			this.addMenuListener(this);
		}
		
		private void reload() 
		{
			super.removeAll();
			submap.clear();
			File[] files = UILanguages.ListLanguageFiles();
			
			for (File file : files)
			{
				String name = file.getPath().substring(UIEdit.getInstance().languagedir.getPath().length());
				JCheckBoxMenuItem item = new JCheckBoxMenuItem(name);
				item.addActionListener(this);
				item.setSelected(file.equals(UILanguages.GetCurrentLanguageFile()));
				submap.put(item, file);
				super.add(item);
			}
		}
		@Override
		public void menuSelected(MenuEvent e) {
			reload();
		}
		
		@Override
		public void menuCanceled(MenuEvent arg0) {}
		@Override
		public void menuDeselected(MenuEvent arg0) {}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			File lanfile = submap.get(e.getSource());
			if (lanfile != null) {
				templates.loadLanguage(lanfile);
			}
		}
		
	}

	public class PageMenu extends JPopupMenu implements ActionListener
	{ 
		final private JMenuItem info;
		final private JMenuItem close = new JMenuItem("关闭");
		final private JMenuItem close_left = new JMenuItem("左边全部关闭");
		final private JMenuItem close_right = new JMenuItem("右边全部关闭");
		final private JMenuItem close_other = new JMenuItem("除此之外全部关闭");
		final private JMenuItem close_all = new JMenuItem("全部关闭");
		final private UIEditFileNode node;

		public PageMenu(UIEditFileNode node)
		{
			this.node = node;
			this.info = new JMenuItem(node.getTitle());
			this.info.setText(node.getTitle());
			this.info.setEnabled(false);
			
			add(info);
			add(close);
			add(close_left);
			add(close_right);
			add(close_other);
			add(close_all);
			
			close.addActionListener(this);
			close_left.addActionListener(this);
			close_right.addActionListener(this);
			close_other.addActionListener(this);
			close_all.addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == close) {
				node.tryClose();
			} else if (e.getSource() == close_left) {
				for (UIEditFileNode open : getOpendFiles(-1, node)) {
					open.tryClose();
				}
			} else if (e.getSource() == close_right) {
				for (UIEditFileNode open : getOpendFiles(1, node)) {
					open.tryClose();
				}
			} else if (e.getSource() == close_other) {
				ArrayList<UIEditFileNode> list = getOpendFiles();
				list.remove(node);
				for (UIEditFileNode open : list) {
					open.tryClose();
				}
			} else if (e.getSource() == close_all) {
				for (UIEditFileNode open : getOpendFiles()) {
					open.tryClose();
				}
			}
		}
		
	}
	private class FilesTabbedAdapter implements MouseListener, ChangeListener, PropertyChangeListener
	{
		@Override
		public void stateChanged(ChangeEvent e) {
			
		}
		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void propertyChange(PropertyChangeEvent evt) {}
	}
	
	public static String formatFileName(String fn) 
	{ 
		fn = CUtil.replaceString(fn, "\\", "/");
		while (fn.startsWith("/")) {
			fn = fn.substring(1);
		}
		return fn;
	}
	
	public String getFileSubName(File fn) {
		try {
			return formatFileName(CUtil.replaceString(
					fn.getCanonicalPath(), 
					workdir.getCanonicalPath(), 
					""));
		} catch (Exception e) {}
		return fn.getPath();
	}
	
	public String getResSubName(File fn) {
		try {
			return formatFileName(CUtil.replaceString(
					fn.getCanonicalPath(), 
					resdir.getCanonicalPath(), 
					""));
		} catch (Exception e) {}
		return fn.getPath();
	}
	
    public static void setDefaultSize(int size) {
        java.util.Set<Object> keySet = UIManager.getLookAndFeelDefaults().keySet();
        Object[] keys = keySet.toArray(new Object[keySet.size()]);
        for (Object key : keys) {
            if (key != null && key.toString().toLowerCase().contains("font")) {
                System.out.println(key);
                Font font = UIManager.getDefaults().getFont(key);
                if (font != null) {
                    font = font.deriveFont((float)size);
                    UIManager.put(key, font);
                }
            }
        }

    }

	static public void main(final String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
            public void run() {
				CObject.initSystem(new CStorage("ui_edit"), new CAppBridge(Studio.class.getClassLoader(), Studio.class));
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");          
					//setDefaultSize(24);
				} catch (Exception err) {}	
				try {
					if (args.length > 0) {
						File cfg = new File(args[0]).getCanonicalFile();
						if (cfg.exists()) {
							new AwtEngine();
							UIEdit frm = new UIEdit(cfg);
							frm.addWindowListener(new WindowAdapter() {
								@Override
								public void windowClosed(WindowEvent e) {
									System.exit(0);
								}
							});
							frm.setVisible(true);
							if (args.length > 1 && args[1].equals("waza")) {
							} else {
								//run_secure();
							}
							return;
						} else {
							JOptionPane.showMessageDialog(null, 
									"无法找到工程文件: " + args[0]);
							return;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "工作空间配置不正确!");
				System.exit(1);
            }
        });
	}

	static private void run_secure()
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						String url = "http://192.168.1.81:80/GameEditor.txt";
						HttpResponse rsp = new HttpRequester().sendGet(url);
						if (rsp.getContent().trim().equals("684f9089eb046ca4c0a76c869c8ce889")) {
							Thread.sleep(60000);
							continue;
						}
					} catch (Exception err) {
						System.err.println(err.getMessage());
					}
					System.exit(1);
				}
			}
		}).start();
		 
		
	}
	
}
