package com.g2d.studio.ui.edit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.tree.TreeNode;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import com.cell.CUtil;
import com.cell.io.CFile;
import com.g2d.display.DisplayObject;
import com.g2d.display.DisplayObjectContainer;
import com.g2d.editor.DisplayObjectPanel;
import com.g2d.studio.swing.G2DTree;
import com.g2d.studio.ui.edit.UITreeNode.UIMenu;
import com.g2d.studio.ui.edit.gui.UEFileNode;
import com.g2d.studio.ui.edit.gui.UERoot;

public class UIEditFileNode extends JPanel
{
	private static final long serialVersionUID = 1L;
	private UIStage g2d_stage;
	private UIPropertyPanel ui_property_panel;
	private UITreeNode tree_root;
	private UITree tree;
	private DisplayObjectPanel stage_view;
	private File last_saved_file;
	private boolean data_changed = false;
	private boolean ignore_undo_redo = false;
	private int[] selected_path;
	private Vector<UndoRedoInfo> undo_list = new Vector<UndoRedoInfo>();
	private Vector<UndoRedoInfo> redo_list = new Vector<UndoRedoInfo>();
	private JToolBar tabComp = new JToolBar();
	private JLabel tabCompText = new JLabel();
	private JButton tabCompClose = new JButton(UILayoutManager.icon_close12);
	
	public UIEditFileNode(File gui_xml) throws Exception 
	{
		super(new BorderLayout());
		this.last_saved_file = gui_xml;
		
		addComponentListener(new ComponentListener(){
			public void componentHidden(ComponentEvent e) {
				stop();
			}
			public void componentMoved(ComponentEvent e) {}
			public void componentResized(ComponentEvent e) {}
			public void componentShown(ComponentEvent e) {
				start();
			}
		});
		
		JSplitPane hsplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		{
			JSplitPane vsplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			{
				tree_root = new UITreeNode(this, UIEdit.getInstance().getTemplate(UERoot.class), "root");
				tree = new UITree(this, tree_root);
				JScrollPane treescroll = new JScrollPane(tree);
				treescroll.setPreferredSize(new Dimension(UIEdit.SPLIT_VIEW_MIN_SIZE, getHeight()/3));
				treescroll.setMinimumSize(new Dimension(UIEdit.SPLIT_VIEW_MIN_SIZE, 300));
				vsplit.setTopComponent(treescroll);
			}{
				ui_property_panel = new UIPropertyPanel(UIEdit.SPLIT_VIEW_MIN_SIZE, 300);
				ui_property_panel.setPreferredSize(new Dimension(UIEdit.SPLIT_VIEW_MIN_SIZE, 300));
				vsplit.setBottomComponent(ui_property_panel);
			}
			hsplit.setRightComponent(vsplit);
		}
		hsplit.setResizeWeight(1);
		{
			this.g2d_stage = new UIStage(this);
			this.stage_view = new DisplayObjectPanel(g2d_stage);
			if (UIEdit.getLayoutManager().getDefaultFont() != null) {
				this.stage_view
						.getSimpleCanvas()
						.getCanvasAdapter()
						.setDefaultFont(
								UIEdit.getLayoutManager().getDefaultFont());
			}
			
			DropTarget dt = new DropTarget(
					stage_view.getSimpleCanvas(), 
					DnDConstants.ACTION_REFERENCE,
					g2d_stage, true);
			this.stage_view.getSimpleCanvas().setDropTarget(dt);		
			this.stage_view.getCanvas().setFPS(30);
			this.g2d_stage.setRootUI(tree_root.getDisplay());

			hsplit.setLeftComponent(stage_view);
		}
		this.add(hsplit, BorderLayout.CENTER);
		
		tabCompText.setText(getPageTitle());
		tabCompText.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					UIEdit.getInstance().selectFileTabble(UIEditFileNode.this);
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					UIEdit.getInstance().showPageMenu(UIEditFileNode.this, e.getXOnScreen(), e.getYOnScreen());
				}
			}
		});
		tabCompClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tryClose();
			}
		});
		tabCompClose.setMaximumSize(new Dimension(10,10));
		tabComp.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				UIEdit.getInstance().selectFileTabble(UIEditFileNode.this);
			}
		});
		tabComp.setFloatable(false);
		tabComp.add(tabCompText);
		tabComp.add(tabCompClose);
	}
	
	public void tryClose() {
		if (data_changed) {
			int result = JOptionPane.showConfirmDialog(UIEditFileNode.this, getTitle() + "\n关闭当前保存文件？");
			if (result == JOptionPane.OK_OPTION) {
				saveFile(false);
				UIEdit.getInstance().removeFileTabble(UIEditFileNode.this);
			} else if (result == JOptionPane.NO_OPTION) {
				UIEdit.getInstance().removeFileTabble(UIEditFileNode.this);
			}
		} else {
			UIEdit.getInstance().removeFileTabble(UIEditFileNode.this);
		}
		UIEdit.getInstance().revalidate();
	}
	
	public void start() {
		stage_view.start();
		UIEdit.getInstance().revalidate();
	}

	public void stop() {
		stage_view.stop();
		UIEdit.getInstance().revalidate();
	}

	public void runTransition() {
		g2d_stage.runTransition();
	}
	
	
	public String getEditName() {
		if (last_saved_file != null) {
			return UIEdit.getInstance().getFileSubName(last_saved_file);
		} else {
			return null;
		}
	}
	
	public File getFile() {
		return last_saved_file;
	}
	
	public Component getTabComponent() {
		return tabComp;
	}
	
	public UIStage getUIStage() {
		return g2d_stage;
	}

	public java.awt.Color getPageColor() {
		if (last_saved_file == null) {
			return java.awt.Color.red;
		} else if (data_changed) {
			return java.awt.Color.red;
		} else {
			return java.awt.Color.black;
		}
	}
	public String getPageTitle() {
		StringBuffer sb = new StringBuffer();
		sb.append("<html><body>");
		sb.append("<p>");
		if (last_saved_file == null) {
			sb.append("<font color=ff0000>(unamed)</font>");
		} else if (data_changed) {
			sb.append("<font color=ff0000> * " + last_saved_file.getName() + "</font>");
		} else {
			sb.append("<font color=000000>" + last_saved_file.getName() + "</font>");
		}
		sb.append("</p>");
		sb.append("</body></html>");
		return sb.toString();
	}
	public String getTitle() {
		if (last_saved_file == null) {
			return "(unamed)";
		} else {
			return last_saved_file.getName();
		}
	}
	
	public UITreeNode getSelectedUINode() {
		Object value = tree.getSelectedNode();
		if (value instanceof UITreeNode){
			return (UITreeNode)value;
		}
		return null;
	}
	
	public UITree getTree() {
		return tree;
	}
	
	public UITreeNode getTreeRoot() {
		return tree_root;
	}
	
	public void onSelectTreeNode(UITreeNode node) {
		tree.setSelectionNode(node);
		ui_property_panel.setCompoment(node);
		if (node != null) {
			selected_path = G2DTree.getIndexPathFromNode(tree.getSelectedNode());
		}
	}

	public boolean checkChilds()
	{
		if (last_saved_file == null) {
			return true;
		}
		return checkContainsFile(last_saved_file, tree_root.getDisplay());
	}
	
	public boolean checkChilds(DisplayObjectContainer obj)
	{
		if (last_saved_file == null) {
			return true;
		}
		return checkContainsFile(last_saved_file, obj);
	}
	
	public boolean checkContainsFile(File file, DisplayObjectContainer obj)
	{
		if (obj instanceof UEFileNode) {
			UEFileNode utfile = (UEFileNode)obj;
			if (utfile.getFile().equals(file)) {
				return false;
			}
		}
		for (DisplayObject dc : obj.getChilds()) {
			if (dc instanceof DisplayObjectContainer) {
				if (!checkContainsFile(file, (DisplayObjectContainer)dc)) {
					return false;
				}
			}
		}
		return true;
	}

	public void saveFile(boolean saveAs)
	{
		if (!checkChilds()) {
			JOptionPane.showMessageDialog(this,
					"不能保存：节点递归错误！", 
					"不能保存！", 
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		try {
			if (last_saved_file == null || saveAs) {
				String name = JOptionPane.showInputDialog(this, "输入名字！", "");
				if (name != null && name.length() > 0) {
					File file = new File(UIEdit.getInstance().workdir, name + ".gui.xml");
					if (file.exists()) {
						JOptionPane.showMessageDialog(this,
								"不能保存！\"" + file.getName() + "\"文件已存在！", 
								"不能保存！", 
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					last_saved_file = file;
				} else {
					return;
				}
			}
			if (last_saved_file != null) {
				CFile.writeData(last_saved_file, this.toSaveBinary());
				data_changed = false;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		tabCompText.setText(getPageTitle());
	}
	public void loadFile() throws Throwable
	{
		last_saved_file = last_saved_file.getCanonicalFile();
		File xmlfile = last_saved_file;
		FileInputStream fis = new FileInputStream(xmlfile);
		last_saved_file = xmlfile;
		try {
			tree_root.fromXMLStream(this, fis);
		} catch (Throwable err) {
			tree_root.clearChilds();
			throw err;
		} finally {
			fis.close();
		}
		tree.reload();
		onSelectTreeNode(tree_root);
		tabCompText.setText(getPageTitle());
		undo_list.clear();
		undo_list.add(new UndoRedoInfo(toSaveBinary()));
	}

	private byte[] toSaveBinary() throws Exception {
		ByteArrayOutputStream fos = new ByteArrayOutputStream();
		tree_root.toXMLStream(this, fos);
		fos.flush();
		return fos.toByteArray();
	}
	
	private void dataRestore(UndoRedoInfo info) {
		ByteArrayInputStream fis = new ByteArrayInputStream(info.data);
		try {
			tree_root.fromXMLStream(this, fis);
		} catch (Throwable err) {
			tree_root.clearChilds();
		}
		tree.reload();
		TreeNode selected = G2DTree.getNodeFromIndexPath(tree_root, selected_path);
		if (selected instanceof UITreeNode) {
			onSelectTreeNode((UITreeNode)selected);
		} else {
			onSelectTreeNode(tree_root);
		}
		data_changed = true;
	}
	
	public void dataChanged()  {
		try {
			if (!ignore_undo_redo) {
				byte[] binary = this.toSaveBinary();
				if (undo_list.isEmpty() || !Arrays.equals(binary, undo_list.lastElement().data)) {
					data_changed = true;
					undo_list.add(new UndoRedoInfo(binary));
					redo_list.clear();
				}
			}
			tabCompText.setText(getPageTitle());
			UIEdit.getInstance().dataChanged(this);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public boolean hasUndo() {
		return undo_list.size() > 1;
	}

	public boolean hasRedo() {
		return redo_list.size() > 0;
	}
	
	public int undoCount() {
		return undo_list.size() - 1;
	}

	public int redoCount() {
		return redo_list.size();
	}

	public void undo() {
		try {
			ignore_undo_redo = true;
			if (hasUndo()) {
				redo_list.add(undo_list.remove(undo_list.size() - 1));
				dataRestore(undo_list.get(undo_list.size() - 1));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			ignore_undo_redo = false;
		}
	}

	public void redo() {
		try {
			ignore_undo_redo = true;
			if (hasRedo()) {
				UndoRedoInfo data = redo_list.remove(redo_list.size() - 1);
				undo_list.add(data);
				dataRestore(data);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			ignore_undo_redo = false;
		}
	}

	class UndoRedoInfo {
		final public String text;
		final public byte[] data;

		public UndoRedoInfo(byte[] data) {
			this.text = CUtil.timeToString(System.currentTimeMillis());
			this.data = data;
		}
	}
	//	private class FileNodeMenu extends JPopupMenu implements ActionListener
//	{
//		private static final long serialVersionUID = 1L;
//
//		protected JMenuItem hide = new JMenuItem("关闭");
//
//		public FileNodeMenu()
//		{
//			add(hide);
//			hide.addActionListener(this);
//		}
//		
//		@Override
//		public void actionPerformed(ActionEvent e) 
//		{
//			if (e.getSource() == hide) {
//				UIEdit.getInstance().removeFileTabble(UIEditFileNode.this);
//			} 
//		}
//		
////		@Override
////		public void show(Component invoker, int x, int y) {
////			if (invoker instanceof G2DTree) {
////				this.tree = (G2DTree)invoker;
////			}
////			super.show(invoker, x, y);
////		}
//	}
	
	
	
	
}
