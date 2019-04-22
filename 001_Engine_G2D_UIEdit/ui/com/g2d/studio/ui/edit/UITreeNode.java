package com.g2d.studio.ui.edit;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.cell.CUtil;
import com.cell.reflect.Parser;
import com.g2d.BufferedImage;
import com.g2d.Color;
import com.g2d.Engine;
import com.g2d.Graphics2D;
import com.g2d.Tools;
import com.g2d.action.Action;
import com.g2d.annotation.Property;
import com.g2d.annotation.TextProperty;
import com.g2d.awt.util.AbstractDialog;
import com.g2d.display.DisplayObject;
import com.g2d.display.DragResizeObject;
import com.g2d.display.InteractiveObject;
import com.g2d.display.event.MouseMoveEvent;
import com.g2d.display.ui.UIComponent;
import com.g2d.display.ui.layout.UILayout;
import com.g2d.display.ui.layout.UILayout.ImageStyle;
import com.g2d.editor.property.ObjectPropertyListener;
import com.g2d.editor.property.ObjectPropertyPanel;
import com.g2d.java2d.impl.AwtEngine;
import com.g2d.studio.res.Res;
import com.g2d.studio.swing.G2DTreeNode;
import com.g2d.studio.swing.G2DTreeNodeGroup.NodeMenu;
import com.g2d.studio.ui.edit.UIFontStyleEditor.UIEditFontStyle;
import com.g2d.studio.ui.edit.UILayoutManager.UIEditImageAtlas;
import com.g2d.studio.ui.edit.UILayoutManager.UIEditLayout;
import com.g2d.studio.ui.edit.UILayoutManager.UIEditSprite;
import com.g2d.studio.ui.edit.gui.SavedComponent;
import com.g2d.studio.ui.edit.gui.UECanvas;
import com.g2d.studio.ui.edit.gui.UEFileNode;
import com.g2d.studio.ui.edit.gui.UERoot;
import com.g2d.studio.ui.edit.transition.UIFadeAction;


public class UITreeNode extends G2DTreeNode<UITreeNode> 
implements ObjectPropertyListener
{
	private static UITemplate copyed_node_template;
	private static byte[] copyed_node_xml_data;
	final private UITemplate template;
	private ObjectPropertyPanel opp;
	private UIComponent display;
	private BufferedImage icon;
	private UIEditFileNode edit;
	private BufferedImage img_lock = AwtEngine.wrap_awt(Res.icon_lock);
	
	public UITreeNode(UIEditFileNode edit, UITemplate template, String name) throws Exception
	{
		this.edit = edit;
		this.template = template;
		DisplayAdapter adapter = new DisplayAdapter();
		this.display = template.createDisplay(edit);
		this.getDisplay().name = name;
		this.getDisplay().debugDraw = adapter;
		this.getDisplay().enable = true;
		this.getDisplay().enable_drag = true;
		this.getDisplay().enable_drag_resize = true;
		this.getDisplay().enable_key_input = true;
		this.getDisplay().enable_focus = true;
		this.getDisplay().edit_mode = adapter;
		this.getDisplay().addEventListener(adapter);
		this.getDisplay().setSorter(adapter);
		this.getDisplay().setAttribute(UITreeNode.class.getSimpleName(), this);
		this.opp = new ObjectPropertyPanel(getDisplay(), 100, 200, false, UIPropertyPanel.getAdapters(edit));
		this.opp.addObjectPropertyListener(this);
		this.setAllowsChildren(getDisplay() instanceof com.g2d.display.ui.Container);
	}
	public ObjectPropertyPanel getProperty() {
		return opp;
	}
	@Override
	public void onFieldChanged(Object object, Field field) {
		getIcon(true);
		edit.getTree().repaint();
		edit.dataChanged();
	}
	
	@Override
	protected ImageIcon createIcon() {
		UILayout uc = getDisplay().getCustomLayout();
		if (uc == null) {
			uc = getDisplay().getLayout();
		}
		if (uc != null) {
			icon = Engine.getEngine().createImage(getDisplay().getWidth(), getDisplay().getHeight());
			Graphics2D g = icon.createGraphics();
			uc.render(g, 0, 0, getDisplay().getWidth(), getDisplay().getHeight());
			g.dispose();
			icon = Tools.combianImage(40, 22, icon);
			if (display.lock) {
				g = icon.createGraphics();
				g.drawImage(img_lock, 1, 1);
				g.dispose();
			}
			return new ImageIcon(AwtEngine.unwrap(icon));
		}
		return null;
	}
	
	@Override
	public String getName() {
		return this.display.name;
	}
	
	public String pathInfo() {
		return "<" + template.getFullName() + " " + getName() + ">";
	}
	
	@Override
	public void onClicked(JTree tree, MouseEvent e) {
		edit.onSelectTreeNode(this);
		edit.start();
	}
	@Override
	public void onRightClicked(JTree tree, MouseEvent e) {
		new UIMenu(tree, this).show(tree, e.getX(), e.getY());
	}
	public boolean isSelected() {
		return edit.getTree().getSelectedNode() == this;
	}

	
	class DisplayAdapter implements 
	com.g2d.display.event.MouseListener, 
	com.g2d.display.event.MouseMoveListener,
	com.g2d.display.event.KeyListener,
	com.g2d.display.event.MouseDragResizeListener,
	com.g2d.display.ui.UIComponent.DebugModeDraw,
	com.g2d.display.InteractiveObject.EditModeSelect,
	Comparator<DisplayObject>
	{		
		boolean check = false;
		
		@Override
		public boolean isSelected(InteractiveObject obj) {
			if (!UIEdit.getInstance().isToolAutoSelect()) {
				return edit.getSelectedUINode() == UITreeNode.this;
			} else {
				return true;
			}
		}
		
		@Override
		public boolean isDragEnable(InteractiveObject obj) {
			return UIEdit.getInstance().isToolDragMove();
		}
		
		@Override
		public int compare(DisplayObject o1, DisplayObject o2) {
			if (o1 != null && o2!=null) {
				UITreeNode t1 = ((UIComponent)o1).getAttribute(UITreeNode.class.getSimpleName());
				UITreeNode t2 = ((UIComponent)o2).getAttribute(UITreeNode.class.getSimpleName());
				TreeNode p1 = t1.getParent();
				TreeNode p2 = t2.getParent();
				if (p1 == p2 && p1!=null) {
					return p1.getIndex(t1) - p2.getIndex(t2);
				}
			}
			return 0;
		}
		
		@Override
		public void mouseClick(com.g2d.display.event.MouseEvent e) {
			
		}
		@Override
		public void mouseDown(com.g2d.display.event.MouseEvent e) {
			edit.onSelectTreeNode(UITreeNode.this);
			check = true;
		}
		@Override
		public void mouseUp(com.g2d.display.event.MouseEvent e) {
			opp.refresh();
		}

		@Override
		public void mouseDragged(MouseMoveEvent e) {
			if (UIEdit.getInstance().isToolGridEnable()) {
				UIEdit.getLayoutManager().gridPos(getDisplay());
			}
		}	
		@Override
		public void mouseDraggedEnd(MouseMoveEvent e) {
			edit.dataChanged();
		}
		@Override
		public void onDragResizeEnd(InteractiveObject object) {
			edit.dataChanged();
		}

		@Override
		public void onDragResizeRunning(InteractiveObject object, DragResizeObject dr) 
		{
			if (UIEdit.getInstance().isToolGridEnable()) 
			{
				UIEdit.getLayoutManager().gridSize(dr.start_drag_bounds);
			}
		}

		@Override
		public void onDragResizeStart(InteractiveObject object, int drag_type) {}
		
		
		@Override
		public void keyDown(com.g2d.display.event.KeyEvent e) {}
		@Override
		public void keyTyped(com.g2d.display.event.KeyEvent e) {}
		@Override
		public void keyUp(com.g2d.display.event.KeyEvent e) {}
		
		
		
		@Override
		public void render(com.g2d.Graphics2D g, UIComponent ui) 
		{
			if (UIEdit.getInstance().isToolShowBounds() && 
				edit.getTree().getSelectedNode() == UITreeNode.this)
			{
				int s1 = 3;
				int s2 = 6;
				int s4 = 12;
				int w = ui.getWidth();
				int h = ui.getHeight();
				g.pushClip();
				g.setClip(-s2, -s2, w+s4, h+s4);
				
				if (UIEdit.getInstance().isToolDragMove())
				{
					float alpha = 0.5f;
					if (!ui.lock) {
						alpha = 0.5f + (float)Math.sin(ui.timer / 5.0f)/2;
					}
					g.setColor(new Color(alpha, alpha, alpha, 1));
					g.drawRect(0, 0, w-1, h-1);
					
					g.fillRect( -s1,  -s1, s2, s2);
					g.fillRect(w-s1,  -s1, s2, s2);
					g.fillRect( -s1, h-s1, s2, s2);
					g.fillRect(w-s1, h-s1, s2, s2);
					
					g.fillRect(w/2-s1,  -s1, s2, s2);
					g.fillRect(w/2-s1, h-s1, s2, s2);
					g.fillRect( -s1, h/2-s1, s2, s2);
					g.fillRect(w-s1, h/2-s1, s2, s2);
				}
				else
				{
					g.setColor(new Color(0.5f, 0.5f, 1f, 1f));
					g.drawRect(-1, -1, w+1, h+1);
				}
				g.popClip();
				if (ui.lock) {
					g.drawImage(img_lock, w, h);
				}
			}
		}
	}

	
//	---------------------------------------------------------------------------------------------------------------

	class UIMenu extends NodeMenu<UITreeNode>
	{
		final UITree tree;

		protected JMenuItem refresh	= new JMenuItem("刷新");
		protected JMenuItem add 	= new JMenuItem("添加");
//		protected JMenuItem copy 	= new JMenuItem("复制");
		protected JMenuItem cut 	= new JMenuItem("剪贴");
		protected JMenuItem paste 	= new JMenuItem("粘贴");
		protected JMenuItem lock	= new JMenuItem("锁定/解锁");
		
		public UIMenu(JTree tree, UITreeNode node) 
		{
			super(node);
			this.tree = (UITree)tree;
			super.remove(open);
			super.remove(rename);
			cut.addActionListener(this);
			add.addActionListener(this);
			paste.addActionListener(this);
			refresh.addActionListener(this);
			lock.addActionListener(this);

			if (getDisplay() instanceof com.g2d.display.ui.Container) {
				super.insert(paste, 1);
				paste.setEnabled(copyed_node_xml_data != null);
			}

			if (UITreeNode.this.getParent() != null) 
			{
				super.insert(cut, 1);
				super.insert(copy, 1);
			}
			
			if (getDisplay() instanceof com.g2d.display.ui.Container) {
				super.insert(add, 1);
			}
			super.insert(rename, 1);
			super.insert(refresh, 1);

			super.add(lock);
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			try {
				if (e.getSource() == refresh)
				{
					tree.reload(node);
				}
				else if (e.getSource() == rename)
				{
					String name = JOptionPane.showInputDialog(
							AbstractDialog.getTopWindow(getInvoker()), "输入名字！",
							node.getDisplay().name);
					if (name != null && name.length() > 0) {
						node.getDisplay().name = name;
					}
					tree.reload(node);
				}
				else if (e.getSource() == add) 
				{
					UITemplate type = UIEdit.getInstance().getSelectedTemplate();
					if (type != null) {
						String name = JOptionPane.showInputDialog(
								AbstractDialog.getTopWindow(getInvoker()), "输入名字！",
								"");
						if (name != null && name.length() > 0) {
							try {
								createChild(type, name);
							} catch (Exception err) {
								JOptionPane.showMessageDialog(this, err.getMessage());
							}
						}
					}
				}
				else if (e.getSource() == copy)
				{
					UITreeNode.this.copy(edit);
				}
				else if (e.getSource() == cut)
				{
					UITreeNode.this.copy(edit);
					removeFromParent2();
				}
				else if (e.getSource() == paste)
				{
					UITreeNode pasted = paste(edit);
					if (pasted != null) {
						addChild(pasted);
					}
				}
				else if (e.getSource() == delete) 
				{
					TreeNode parent = node.getParent();
					this.node.removeFromParent();
					this.node.getDisplay().removeFromParent();
					if (tree!=null) {
						tree.reload(parent);
					}
				}
				else if (e.getSource() == lock)
				{
					this.node.switchLock();
				}
			} catch (Exception err) {
				JOptionPane.showMessageDialog(this, err.getMessage());
			}
			
		}
	}
	
	public void removeFromParent2() {
		((UITreeNode)getParent()).removeChild(this);
	}
	
	public UITreeNode removeChild(UITreeNode tr) {
		try {
			super.remove(tr);
			this.getDisplay().removeChild(tr.getDisplay());
			return tr;
		} catch (Exception e) {}
		return null;
	}
	
	public void clearChilds() {
		while (getChildCount()>0) {
			removeChild((UITreeNode)getFirstChild());
		}
	}
	public UITreeNode addChild(UITreeNode node) {
		if (getDisplay() instanceof com.g2d.display.ui.Container) {
			com.g2d.display.ui.Container pc = (com.g2d.display.ui.Container) getDisplay();
			if (pc.addComponent(node.getDisplay())) {
				this.add(node);
				edit.getTree().reload(this);
				edit.getTree().expand(node);
				return node;
			}
		}
		return null;
	}
	
	public UITreeNode createChild(UITemplate template, String name) throws Exception
	{
		if (getDisplay() instanceof com.g2d.display.ui.Container) {
			UITreeNode node = new UITreeNode(edit, template, name);
			if (!edit.checkChilds(node.getDisplay())) {
				throw new Exception(name + " 文件递归错误！");
			}
			com.g2d.display.ui.Container pc = (com.g2d.display.ui.Container) getDisplay();
			if (pc.addComponent(node.getDisplay())) {
				this.add(node);
				edit.getTree().reload(this);
				edit.getTree().expand(node);
				return node;
			}
		}
		throw new Exception(
				"["+getDisplay().getClass().getSimpleName()+ "]" +
				" 不能添加子节点 [" + template.getUIType().getSimpleName() + "]");
	}
	
	
	public void copy(UIEditFileNode edit) throws Exception
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		this.toXMLStream(edit, os);
		copyed_node_template = template;
		copyed_node_xml_data = os.toByteArray();
		
//		UIComponent ui = ret.getDisplay();
//		Field[] fields = ui.getClass().getFields();
//		for (Field f : fields) {
//			try {
//				if (f.getAnnotation(Property.class) != null) {
//					Object attr = f.get(this.getDisplay());
//					f.set(ui, CIO.cloneObject(attr));
//				}
//			} catch (Exception err) {
//				err.printStackTrace();
//			}
//		}
//		((SavedComponent)ui).readComplete(edit);
//		
//		for (int i = 0; i < getChildCount(); i++) {
//			UITreeNode cn = (UITreeNode) getChildAt(i);
//			cn = cn.copy(edit);
//			ret.addChild(cn);
//		}
	}
	
	public UITreeNode paste(UIEditFileNode edit) throws Exception
	{
		if (copyed_node_xml_data != null) {
			ByteArrayInputStream is = new ByteArrayInputStream(copyed_node_xml_data);
			UITreeNode ret = new UITreeNode(edit, copyed_node_template, "");
			ret.fromXMLStream(edit, is);
			return ret;
		}
		return null;
	}
	
	public void fromXMLStream(UIEditFileNode edit, InputStream is) throws Exception
	{
		try {
			this.clearChilds();
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(is);
			Element e = doc.getDocumentElement();
			if (e != null) {
				this.getDisplay().disable_layout = null;
				this.getDisplay().custom_layout = null;
				readInternal(edit, doc, e, this, new Stack<String>());
			} else {
				throw new Exception("格式错误!");
			}
		} catch (SAXParseException err) {
			throw new Exception("** Parsing error" + ", line "
					+ err.getLineNumber() + ", uri " + err.getSystemId(), err);
		} catch (SAXException e) {
			throw e;
		}
	}
	
	public void toXMLStream(UIEditFileNode edit, OutputStream os) throws Exception
	{
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element e = doc.createElement(getDisplay().getClass().getCanonicalName());
			doc.appendChild(e);
			writeInternal(edit, doc, e, this);
			doc.normalizeDocument();
			doc.normalize();
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(
					new DOMSource(doc), 
					new StreamResult(os));
		} catch (SAXException e) {
			Exception x = e.getException();
			((x == null) ? e : x).printStackTrace();
			throw e;
		}
	}

	private static void readInternal(
			UIEditFileNode edit, 
			Document doc, 
			Element e, 
			UITreeNode ui,
			Stack<String> trace) throws Exception
	{	
		trace.push(ui.pathInfo());
		try {
			readFields(edit, e, ui.getDisplay());
			if (ui.getDisplay() instanceof SavedComponent) {
				((SavedComponent)ui.getDisplay()).onRead(edit, e, doc);
//				((SavedComponent)ui.getDisplay()).readComplete(edit);
			}
			
			NodeList list = e.getChildNodes();
			for (int i=0; i<list.getLength(); i++)
			{
				if (list.item(i) instanceof Element) 
				{
					Element ev = (Element)list.item(i);
					
					if (ev.getNodeName().equals("layout")) 
					{
						UILayout layout = readLayout(edit, ev);
						ui.getDisplay().custom_layout = layout;
					}
					else if (ev.getNodeName().equals("disable_layout")) 
					{
						UILayout layout = readLayout(edit, ev);
						ui.getDisplay().disable_layout = layout;
					}
					else if (ev.getNodeName().equals("childs"))
					{
						NodeList childs = ev.getChildNodes();
						for (int c=0; c<childs.getLength(); c++) {
							if (childs.item(c) instanceof Element) {
								Element child = (Element)childs.item(c);
								readChildNode(edit, doc, child, ui, trace);
							}
						}
					}
					else if (ui.getDisplay() instanceof UERoot)
					{
						UERoot root = (UERoot)ui.getDisplay();
						if (ev.getNodeName().equals("fadeIn"))
						{
							UIFadeAction fadeIn = readTransition(edit, ev);
							root.fadeIn = fadeIn;
						}
						else if (ev.getNodeName().equals("fadeOut"))
						{
							UIFadeAction fadeOut = readTransition(edit, ev);
							root.fadeOut = fadeOut;
						}
					}
				}
			}
			
			ui.getIcon(true);

		} catch (Exception err) {
			throw new Exception("读入节点错误：" + ui.getName() + "\n" + err.getMessage(),
					err);
		} finally {
			trace.pop();
		}
	}

	private static void readChildNode(UIEditFileNode edit, 
			Document doc, 
			Element child, 
			UITreeNode ui,
			Stack<String> trace) throws Exception
	{
		Class<?> type = Class.forName(child.getNodeName());
		if (UEFileNode.class.isAssignableFrom(type)) {
			String fileName = child.getAttribute("fileName");
			UITemplate template = UIEdit.getInstance().getUserTemplate(fileName);
			if (template != null && template.getUserTemplate() != null) {
				if (template.getUserTemplate().equals(edit.getFile())) {
					throw new Exception("[" + ui.getName()
							+ "]加载文件[" + fileName
							+ "]递归错误！\n" + 
							CUtil.arrayToString(trace, "-"));
				}
				UITreeNode tn = ui.createChild(template, fileName);
				readInternal(edit, doc, child, tn, trace);
			} else {
				throw new Exception("[" + ui.getName()
						+ "]加载文件[" + fileName
						+ "]失败！\n" + 
						CUtil.arrayToString(trace, "-"));
			}
		} else {
			UITemplate template = UIEdit.getInstance().getTemplate(type);
			UITreeNode tn = ui.createChild(template, "");
			readInternal(edit, doc, child, tn, trace);
		}
	
	}
	
	private static void writeInternal(UIEditFileNode edit, Document doc, Element e, UITreeNode ui) throws Exception
	{
		writeFields(edit, e, ui.getDisplay());
		if (ui.getDisplay() instanceof SavedComponent) {
//			((SavedComponent)ui.getDisplay()).writeBefore(edit);
			((SavedComponent)ui.getDisplay()).onWrite(edit, e, doc);
		}
		if (ui.getDisplay().getCustomLayout() != null) {
			Element rect = doc.createElement("layout");
			writeLayout(edit, ui.getDisplay().getCustomLayout(), rect);
			e.appendChild(rect);
		}
		if (ui.getDisplay().disable_layout != null) {
			Element rect = doc.createElement("disable_layout");
			writeLayout(edit, ui.getDisplay().disable_layout, rect);
			e.appendChild(rect);
		}
		if (ui.getDisplay() instanceof UERoot)
		{
			UERoot root = (UERoot)ui.getDisplay();
			Element fadeIn = doc.createElement("fadeIn");
			Element fadeOut = doc.createElement("fadeOut");
			writeTransition(edit, root.fadeIn, fadeIn);
			writeTransition(edit, root.fadeOut, fadeOut);
			e.appendChild(fadeIn);
			e.appendChild(fadeOut);
		}
		if (ui.getChildCount() > 0)
		{
			Element childs = doc.createElement("childs");
			for (int i=0; i<ui.getChildCount(); i++) {
				UITreeNode cn = (UITreeNode)ui.getChildAt(i);
				Element child = doc.createElement(
						cn.getDisplay().getClass().getCanonicalName());
				writeInternal(edit, doc, child, cn);
				childs.appendChild(child);
			}
			e.appendChild(childs);
			
		}
		
	}

	
	private static void readFields(UIEditFileNode edit, Element e, UIComponent ui) 
	{
		ui.name 				= e.getAttribute("name");
		ui.clip_local_bounds 	= Boolean.parseBoolean(e.getAttribute("clipbounds"));
		ui.local_bounds.width	= Integer.parseInt(e.getAttribute("width"));
		ui.local_bounds.height	= Integer.parseInt(e.getAttribute("height"));

		Field[] fields = ui.getClass().getFields();
		for (Field f : fields) {
			try {
				if (UILayout.class.isAssignableFrom(f.getType())) {
					continue;
				}
				if (Action.class.isAssignableFrom(f.getType())) {
					continue;
				}
				if (UIEditFontStyle.class.isAssignableFrom(f.getType())) {
					continue;
				}
				if (f.getAnnotation(TextProperty.class) != null) {
					UILanguages.PutTextProperty(edit, ui, e, f);
					if (UILanguages.Translate(edit, ui, f)) {
						continue;
					}
				}
				if (f.getAnnotation(Property.class) != null) {
					if (e.hasAttribute(f.getName())) {
						String attrvalue = e.getAttribute(f.getName());
						Object attr = Parser.stringToObjectP(
								attrvalue,
								f.getType());
						if (attr != null) {
							f.set(ui, attr);
						}
					}
				}
			} catch (Exception err) {
				//err.printStackTrace();
				//System.err.println("解析字段错误: " + f.getName() + "@" + ui.name + "(" + ui.getClass().getSimpleName() + ") : " + err.getMessage());
			}
		}

		UIEdit.getLayoutManager().setEffect(ui);
	}
	
	private static void writeFields(UIEditFileNode edit, Element e, UIComponent ui) 
	{
		e.setAttribute("name", ui.name);
		e.setAttribute("clipbounds", ui.clip_local_bounds+"");
		e.setAttribute("width", ui.getWidth()+"");
		e.setAttribute("height", ui.getHeight()+"");
		
		Field[] fields = ui.getClass().getFields();
		for (Field f : fields) {
			try {
				if (UILayout.class.isAssignableFrom(f.getType())) {
					continue;
				}
				if (Action.class.isAssignableFrom(f.getType())) {
					continue;
				}
				if (UIEditFontStyle.class.isAssignableFrom(f.getType())) {
					continue;
				}
				if (f.getAnnotation(Property.class) != null) {
					Object attr = f.get(ui);
					String satr = Parser.objectToString(attr, f.getType());
					e.setAttribute(f.getName(), satr);
				}
			} catch (Exception err) {
				err.printStackTrace();
			}
		}
	}

	public static UILayout readLayout(UIEditFileNode edit, Element rect) throws Exception
	{
		if (rect == null) {
			return null;
		}
		try
		{
			UILayout layout;
			ImageStyle style = Enum.valueOf(ImageStyle.class, rect.getAttribute("style"));
			if (style == ImageStyle.SPRITE) 
			{
				UIEditSprite uisprite = Parser.stringToObject(
						rect.getAttribute("sprite"), 
						UIEditSprite.class);
				layout = UIEdit.getLayoutManager().createLayoutSprite(uisprite);
			}
			else if (rect.hasAttribute("img") && 
					style != ImageStyle.NULL && 
					style != ImageStyle.COLOR) 
			{
				UIEditImageAtlas atlas = Parser.stringToObject(
						rect.getAttribute("atlas"), 
						UIEditImageAtlas.class);
				layout = UIEdit.getLayoutManager().createLayout(
						rect.getAttribute("img"), 
						atlas,
						style,
						Integer.parseInt(rect.getAttribute("clip")));
				String repeatFill = rect.getAttribute("repeat");
				if (repeatFill != null) {
					layout.setRepeatFill(Boolean.parseBoolean(repeatFill));
				}
			} 
			else 
			{
				if (style == ImageStyle.COLOR || style == ImageStyle.NULL) {
					layout = new UILayout(style);
				} else {
					layout = new UILayout(ImageStyle.NULL);
				}
			}
			layout.setBackColor(new Color(rect.getAttribute("bgc")));
			layout.setBorderColor(new Color(rect.getAttribute("bdc")));
			return layout;
		} catch (Exception e) {
			e.printStackTrace();
			UILayout layout = new UILayout(ImageStyle.COLOR);
			layout.setBackColor(Color.RED);
			return layout;
		}
	}

	public static void writeLayout(UIEditFileNode edit, UILayout layout, Element rect) 
	{
		rect.setAttribute("style", layout.getStyle().toString());
		rect.setAttribute("bgc", layout.getBackColor().toHexString());
		rect.setAttribute("bdc", layout.getBorderColor().toHexString());
		if (layout instanceof UIEditLayout) {
			UIEditLayout imgrect = (UIEditLayout) layout;
			if (imgrect.getStyle() == ImageStyle.SPRITE) {
				if (imgrect.uisprite != null) {
					rect.setAttribute("sprite", Parser.objectToString(imgrect.uisprite));
				}
			} else {
				rect.setAttribute("img", imgrect.srcImageName() + "");
				rect.setAttribute("clip", imgrect.clip_border + "");
				if (imgrect.getAtlas() != null) {
					rect.setAttribute("atlas", Parser.objectToString(imgrect.getAtlas()));
				}
				rect.setAttribute("repeat", imgrect.isRepeatFill()+"");
			}
		}
	}

	public static void writeTransition(UIEditFileNode edit, Action action, Element e)
	{
		if (action != null)
		{
			Element ee = e.getOwnerDocument().createElement(action.getClass().getCanonicalName());
			Field[] fields = action.getClass().getFields();
			for (Field f : fields) {
				try {
					if (f.getAnnotation(Property.class) != null) {
						Object attr = f.get(action);
						String satr = Parser.objectToString(attr, f.getType());
						ee.setAttribute(f.getName(), satr);
					}
				} catch (Exception err) {
					err.printStackTrace();
				}
			}
			e.appendChild(ee);
		}
	}
	
	public static UIFadeAction readTransition(UIEditFileNode edit, Element ev) 
	{
		NodeList list = ev.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			if (list.item(i) instanceof Element)
			{
				Element ee = (Element) list.item(i);
				try {
					String type = ee.getNodeName();
					Class<?> clazz = Class.forName(type);
					UIFadeAction action = (UIFadeAction) clazz.newInstance();
					Field[] fields = action.getClass().getFields();
					for (Field f : fields) {
						try {
							if (f.getAnnotation(Property.class) != null) {
								if (ee.hasAttribute(f.getName())) {
									String attrvalue = ee.getAttribute(f
											.getName());
									Object attr = Parser.stringToObject(
											attrvalue, f.getType());
									if (attr != null) {
										f.set(action, attr);
									}
								}
							}
						} catch (Exception err) {
							err.printStackTrace();
						}
					}
					return action;
				} catch (Exception err) {
				}
			}
		}
		return null;
	}
	
	public static Element findXmlChild(Element e, String child_name)
	{
		NodeList list = e.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i) instanceof Element) {
				Element ev = (Element) list.item(i);
				if (ev.getNodeName().equals(child_name)) {
					return ev;
				}
			}
		}
		return null;
	}

	public String getClassName() {
		return template.getFullName();
	}
	
	public UIComponent getDisplay() {
		return display;
	}
	
	public void switchLock()
	{
		this.display.lock = !this.display.lock;
		this.getIcon(true);
		edit.repaint();
	}
	
}
