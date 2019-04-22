package com.g2d.studio.ui.edit;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.tree.TreeNode;

import com.g2d.Color;
import com.g2d.Graphics2D;
import com.g2d.display.DisplayObject;
import com.g2d.display.DisplayObjectContainer;
import com.g2d.display.event.MouseEvent;
import com.g2d.display.ui.Menu;
import com.g2d.display.ui.Panel;
import com.g2d.display.ui.ScrollBar;
import com.g2d.display.ui.UIComponent;
import com.g2d.display.ui.Menu.MenuItem;
import com.g2d.display.ui.Menu.MenuItemButton;
import com.g2d.display.ui.layout.UILayout;
import com.g2d.display.ui.layout.UILayoutManager;
import com.g2d.display.ui.layout.UILayout.ImageStyle;
import com.g2d.editor.DisplayObjectPanel;
import com.g2d.geom.Rectangle;
import com.g2d.studio.swing.G2DTree;
import com.g2d.studio.ui.edit.gui.UERoot;
import com.g2d.studio.ui.edit.transition.ScaleFadeIn;
import com.g2d.util.Drawing;
import com.g2d.util.Drawing.TextAnchor;
import com.g2d.util.Profiling;

public class UIStage extends DisplayObjectPanel.ObjectStage implements DropTargetListener {
	private UIEditFileNode edit;
	private UERoot root;
	private Panel edit_panel;
	private int zome_show_time = 0;
	private int zome_show_time_max = 50;
	private float last_mouse_x;
	private float last_mouse_y;
	private float last_screen_x;
	private float last_screen_y;

	public UIStage(UIEditFileNode edit) {
		super(Color.black);
		this.edit = edit;
		this.edit_panel = new Panel();
		this.edit_panel.setLayout(new UILayout(ImageStyle.NULL));
		this.addChild(edit_panel);
		edit.getTree().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_L) {
					lockSelectedComponent();
				}
			}
		});
	}

	@Override
	public void added(DisplayObjectContainer parent) {
		super.added(parent);
	}

	public void setRootUI(UIComponent root) {
		this.root = (UERoot) root;
		this.edit_panel.addContainerComponent(root);
	}

	public void runTransition() {
		this.root.switchOpen();
	}

	public void zoomIn() {
		this.zome_show_time = zome_show_time_max;
		this.getRoot().setScreenScale(Math.min(this.getRoot().getScreenScale() + 0.1f, 100.0f));
	}

	public void zoomOut() {
		this.zome_show_time = zome_show_time_max;
		this.getRoot().setScreenScale(Math.max(this.getRoot().getScreenScale() - 0.1f, 1f));
	}

	public ArrayList<UITreeNode> getComponentsByPos(float screenX, float screenY) {
		ArrayList<UITreeNode> nodes = new ArrayList<UITreeNode>();
		for (TreeNode tn : G2DTree.getAllNodes(edit.getTreeRoot())) {
			if (tn instanceof UITreeNode) {
				UITreeNode uitn = (UITreeNode) tn;
				if (uitn.getDisplay().getScreenBounds().contains(screenX, screenY)) {
					nodes.add(uitn);
				}
			}
		}
		return nodes;
	}

	public void lockSelectedComponent() {
		UITreeNode un = (UITreeNode) edit.getTree().getSelectedNode();
		if (un != null) {
			un.getDisplay().lock = !un.getDisplay().lock;
			un.getIcon(true);
			edit.repaint();
			edit.dataChanged();
		}
	}

	@Override
	public void update() {
		back_color = UIEdit.backcolor.get();
		UITreeNode un = (UITreeNode) edit.getTree().getSelectedNode();
		if (un != null) {
			DisplayObject display = un.getDisplay();
			if (getRoot().isKeyDown(KeyEvent.VK_L)) {
				lockSelectedComponent();
			} else if (isKeyRepeat(KeyEvent.VK_UP)) {
				display.y--;
				un.getProperty().refresh();
				edit.dataChanged();
			} else if (isKeyRepeat(KeyEvent.VK_DOWN)) {
				display.y++;
				un.getProperty().refresh();
				edit.dataChanged();
			} else if (isKeyRepeat(KeyEvent.VK_LEFT)) {
				display.x--;
				un.getProperty().refresh();
				edit.dataChanged();
			} else if (isKeyRepeat(KeyEvent.VK_RIGHT)) {
				display.x++;
				un.getProperty().refresh();
				edit.dataChanged();
			}
		}
		if (getRoot().isKeyHold(KeyEvent.VK_CONTROL)) {
			if (getRoot().isMouseWheelUP()) {
				//getRoot().setScreenOffest(this.last_screen_x, this.last_screen_y);
				zoomIn();
			}
			if (getRoot().isMouseWheelDown()) {
				//getRoot().setScreenOffest(this.last_screen_x, this.last_screen_y);
				zoomOut();
			}
		}
		if (getRoot().isMouseDown(MouseEvent.BUTTON_RIGHT)) {
			this.last_mouse_x = getMouseX();
			this.last_mouse_y = getMouseY();
			this.last_screen_x = getRoot().getScreenOffestX();
			this.last_screen_y = getRoot().getScreenOffestY();
		}
		if (getRoot().isMouseHold(MouseEvent.BUTTON_RIGHT)) {
			float offsetX = last_mouse_x - getMouseX();
			float offsetY = last_mouse_y - getMouseY();
			getRoot().setScreenOffest(
					Math.max(last_screen_x + offsetX, 0),
					Math.max(last_screen_y + offsetY, 0));
		}
		if (getRoot().isMouseUp(MouseEvent.BUTTON_RIGHT)) {
			if (getMouseX() == last_mouse_x && getMouseY() == last_mouse_y) {
				ArrayList<UITreeNode> nodes = getComponentsByPos(this.getMouseX(), this.getMouseY());
				if (nodes.size() > 0) {
					new ObjectsMenu(nodes).show(this, getMouseX(), getMouseY());
				}
			}
		}
	}

	@Override
	public void render(Graphics2D g) {
		super.render(g);
	}

	@Override
	protected void renderChilds(Graphics2D g) {
		super.renderChilds(g);
	}

	@Override
	protected void renderDebug(Graphics2D g) {
		super.renderDebug(g);
		if (root.x < 0) {
			root.x = 0;
		}
		if (root.y < 0) {
			root.y = 0;
		}
		edit_panel.setSize(getRoot().getStageWidth(), getRoot().getStageHeight());
	}

	@Override
	public void onRenderScreen(Graphics2D g) {
		super.onRenderScreen(g);
		try {
			g.pushTransform();
			g.pushFont();
			g.setFont("Consolas");
			{
				String info = "Mouse(" + getMouseX() + ", " + getMouseY() + ")";
				g.setColor(Color.WHITE);
				Drawing.drawStringBorder(g, info, getWidth() - g.getStringWidth(info),
						getHeight() - g.getStringHeight() - 1, Drawing.TEXT_ANCHOR_LEFT | Drawing.TEXT_ANCHOR_TOP,
						Color.BLACK);
			}
			if (zome_show_time-- > 0) {
				g.setColor(Color.WHITE);
				float alpha = zome_show_time / (zome_show_time_max / 2f);
				alpha = Math.min(alpha, 1.0f);
				alpha = Math.max(alpha, 0.0f);
				g.setAlpha(alpha);
				Drawing.drawStringBorder(g, (int) (this.getRoot().getScreenScale() * 100) + "%", 1, 1, TextAnchor.L_T);
				g.setAlpha(1);
			}
			if (UIEdit.getInstance().isToolGridEnable() && UIEdit.getInstance().isToolShowBounds()) {
				// float alpha = 0.5f + (float) Math.sin(this.timer / 5.0f) / 2;
				Color c1 = new Color(1, 1, 1, 1f);
				Color c2 = new Color(0, 0, 0, 1f);
				int gw = UIEdit.getInstance().getGridSize();
				for (int x = 0; x < getWidth(); x += gw) {
					for (int y = 0; y < getHeight(); y += gw) {
						if ((x / gw) % 2 == 0 || (y / gw) % 2 == 0) {
							g.setColor(c1);
						} else {
							g.setColor(c2);
						}
						g.fillRect(x, y, 1, 1);
					}
				}
			}
			if (UIEdit.getInstance().isDebugRenderTime()) {
				UITreeNode un = (UITreeNode) edit.getTree().getSelectedNode();
				if (un != null) {
					UIComponent display = un.getDisplay();
					String info = display.getClass().getSimpleName() + "(" + un.getName() + ")";
					info += " - CurrentDrawCall(" + display.getFrameRenderTime() + ")";
					info += " - TotalDrawCall(" + Profiling.getAllNodeRenderTime(display) + ")";
					g.setColor(Color.WHITE);
					Drawing.drawStringBorder(g, info, 1, getHeight() - g.getStringHeight()- 1,
							Drawing.TEXT_ANCHOR_LEFT | Drawing.TEXT_ANCHOR_TOP, Color.BLACK);
				}
			}
		} finally {
			g.popFont();
			g.popTransform();
		}
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		if (G2DTree.dragged_tree_node instanceof UITemplate) {
			UITemplate ut = (UITemplate) G2DTree.dragged_tree_node;
			UITreeNode ct = edit.getSelectedUINode();
			if (ct == null) {
				ct = (UITreeNode) edit.getTree().getRoot();
			}
			try {
				UITreeNode uc = ct.createChild(ut, "");
				if (uc == null) {
					JOptionPane.showMessageDialog(edit, ct.getDisplay().getClass().getSimpleName() + "不能添加子节点！");
				} else {
					double tx = ct.getDisplay().screenToLocalX(dtde.getLocation().x);
					double ty = ct.getDisplay().screenToLocalY(dtde.getLocation().y);
					uc.getDisplay().setLocation(tx, ty);
					if (UIEdit.getInstance().isToolGridEnable()) {
						UIEdit.getInstance().getLayoutManager().gridPos(ct.getDisplay());
					}
				}
				edit.dataChanged();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(edit, e.getMessage());
			}

		}
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
	}

	class ObjectsMenu extends Menu {
		public ObjectsMenu(ArrayList<UITreeNode> nodes) {
			super(300);
			this.setLayout(UILayout.createRect(new Color(0, 0, 0, 0xA0)));
			for (int i = nodes.size() - 1; i >= 0; --i) {
				UITreeNode obj = nodes.get(i);
				MenuItemButton item = new MenuItemButton(obj.getName() + " - " + obj.getClassName());
				item.setLayout(
						UILayout.createRect(new Color(0xA0, 0xA0, 0xA0, 0x80), new Color(0x80, 0x80, 0x80, 0xFF), 2));
				item.text_anchor = Drawing.TEXT_ANCHOR_LEFT | Drawing.TEXT_ANCHOR_VCENTER;
				item.text_font = "Consolas";
				item.setUserData(obj);
				addMenuItem(item);
				UILayoutManager.getInstance().setLayout(item);
			}
		}

		@Override
		protected void onClickMenuItem(MouseEvent e, MenuItem item) {
			if (item.getUserData() instanceof UITreeNode) {
				UITreeNode tn = (UITreeNode) item.getUserData();
				edit.onSelectTreeNode(tn);
			}
		}
	}
}
