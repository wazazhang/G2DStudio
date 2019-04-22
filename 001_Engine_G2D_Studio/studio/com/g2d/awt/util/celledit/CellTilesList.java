package com.g2d.awt.util.celledit;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.cell.CMath;
import com.cell.CUtil;
import com.cell.gameedit.StreamTiles;
import com.cell.gfx.IImage;
import com.g2d.Color;
import com.g2d.Graphics2D;
import com.g2d.display.DisplayObject;
import com.g2d.display.Sprite;
import com.g2d.display.event.MouseEvent;
import com.g2d.display.ui.ImageBox;
import com.g2d.editor.DisplayObjectPanel;
import com.g2d.geom.Point;
import com.g2d.java2d.impl.AwtEngine;
import com.g2d.util.Drawing;
import com.g2d.util.Drawing.TextAnchor;

public class CellTilesList extends JPanel implements ListSelectionListener
{
	private static final long serialVersionUID = 1L;

	public static com.g2d.Color back_color = new com.g2d.Color(0xff000000);
	private CellTilesResource res;
	private ImageStage g2d_stage;
	private JList namelist;
	private Tile selected;

	private JToolBar tools;
	private JCheckBox chkShowKey 			= new JCheckBox("显示Key", false); // Tools.createIcon(Res.icon_grid),
	private JCheckBox chkShowTileID 		= new JCheckBox("显示Tile", false);
	
	public CellTilesList(CellTilesResource res) 
	{
		super(new BorderLayout());
		this.res = res;
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		Vector<String> names = new Vector<String>(res.ImgTable.keySet());
		CUtil.sort(names, CUtil.getStringCompare());
		{
			namelist = new JList(names);
			namelist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			namelist.addListSelectionListener(this);
			JScrollPane namescroll = new JScrollPane(namelist);
			namescroll.setMinimumSize(new Dimension(100, 200));
			split.setLeftComponent(namescroll);
		} {
			g2d_stage = new ImageStage();
			DisplayObjectPanel stage_view = new DisplayObjectPanel(g2d_stage);
			stage_view.getCanvas().setFPS(30);
			stage_view.start();
			split.setRightComponent(stage_view);
		}
		add(split, BorderLayout.CENTER);
		{
			tools = new JToolBar();
			tools.setFloatable(false);
			tools.add(chkShowKey);
			tools.add(chkShowTileID);
		}
		add(tools, BorderLayout.NORTH);
		namelist.setSelectedIndex(0);
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		String name = namelist.getSelectedValue().toString();
		g2d_stage.changeImages(name);
	}
	
	public CellTileIndex getSelectedTile() {
		if (selected != null) {
			return selected.getObject();
		}
		return null;
	}
	
	public CellAtlasIndex getSelectedAtlas() {
		return g2d_stage.getObject();
	}
	
//	class SubImage extends JLabel implements G2DListItem
//	{
//		private static final long serialVersionUID = 1L;
//		
//		final BufferedImage		image;
//		final ImageIcon 		icon;
//		final int 				index;
//		final Rectangle 		rect;
//		
//		public SubImage(IImage img, Rectangle rect, int index) {
//			this.image	= AwtEngine.unwrap(img);
//			this.icon	= Tools.createIcon(image);
//			this.index	= index;
//			this.rect	= rect;
//			this.setIcon(icon);
//		}
//		
//		@Override
//		public Component getListComponent(JList list, Object value, int index,
//				boolean isSelected, boolean cellHasFocus) {
//			return null;
//		}
//		
//		@Override
//		public ImageIcon getListIcon(boolean update) {
//			return icon;
//		}
//		
//		@Override
//		public String getListName() {
//			return "" + index;
//		}
//	}
//	
//	class ImageList extends G2DList<SubImage>
//	{
//		private static final long serialVersionUID = 1L;
//
//		public ImageList(StreamTiles tiles)
//		{	
//			super.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
//			super.setLayoutOrientation(JList.HORIZONTAL_WRAP);
//			super.setVisibleRowCount(-1);
//			
//			Vector<SubImage> items = new Vector<SubImage>();
//			for (int i=0; i<tiles.getCount(); i++) {
//				IImage tile = tiles.getImage(i);
//				if (tile != null) {
//					items.add(new SubImage(tile, tiles.getClipRect(i), i));
//				}
//			}
//			this.setListData(items);
//		}
//		
//	}
	

	private float masterScale = 1;
	
	class ImageStage extends DisplayObjectPanel.ObjectStage
	{
		Sprite root = new Sprite();
		String currentImageName;
		Point lastmousedown = null;
		
		public ImageStage() 
		{
			super(CellTilesList.back_color);
			root.enable 		= true;
			root.enable_input	= true;
			root.enable_focus	= true;
			root.enable_childs	= true;
			this.addChild(root);
		}

		public void changeImages(String name)
		{
			currentImageName = name;
			root.clearChilds();
			StreamTiles st = res.getImages(name);
			for (int i=0; i<st.getCount(); i++) {
				IImage img = st.getImage(i);
				if (img != null) {
					String key = st.getImagesSet().getClipKey(i);
					Rectangle clip = st.getClipRect(i);
					Tile tile = new Tile(img, clip, st, i, key);
					root.addChild(tile);
				}
			}
		}
		
		@Override
		public void update() 
		{
			if (getRoot().isMouseDown(MouseEvent.BUTTON_LEFT)) {
				double mx = root.getMouseX();
				double my = root.getMouseY();
				mx /= masterScale;
				my /= masterScale;
				for (DisplayObject ds : root.getChilds()) {
					Tile t = (Tile)ds;
					if (CMath.includeRectPoint2(
							(float)t.x, 
							(float)t.y, 
							(float)t.getWidth(),
							(float)t.getHeight(),
							(float)mx,
							(float)my)) {
						selected = t;
					}
				}
			}
			else if (getRoot().isMouseDown(MouseEvent.BUTTON_RIGHT)) {
				lastmousedown = new Point(
						(int)(root.x - getMouseX()),
						(int)(root.y - getMouseY()));
			}
			else if (getRoot().isMouseHold(MouseEvent.BUTTON_RIGHT)) {
				if (lastmousedown != null) {
					root.x = getMouseX() + lastmousedown.x;
					root.y = getMouseY() + lastmousedown.y;
				}
			}
			else if (getRoot().isMouseUp(MouseEvent.BUTTON_RIGHT)) {
				lastmousedown = null;
			}
			else if (getRoot().isMouseWheelDown()) {
				masterScale -= 0.1f;
				masterScale = Math.max(0.1f, masterScale);
			}
			else if (getRoot().isMouseWheelUP()) {
				masterScale += 0.1f;
				masterScale = Math.min(10f, masterScale);
			}
			root.scale_x = root.scale_y = masterScale;
		}
		
		@Override
		public void render(Graphics2D g) 
		{
			super.render(g);
		}
		
		@Override
		protected void renderAfter(Graphics2D g) 
		{
			super.renderAfter(g);
			if (currentImageName != null) {
				g.setColor(Color.WHITE);
				Drawing.drawStringBorder(g, "（右键拖拽）当前图集：" + currentImageName, 
						1, getHeight()-1, 
						Drawing.TEXT_ANCHOR_LEFT | Drawing.TEXT_ANCHOR_BOTTON);
			}
		}
		
		public CellAtlasIndex getObject()
		{
			if (currentImageName != null) {
				StreamTiles st = res.getImages(currentImageName);
				return new CellAtlasIndex(res, 
						res.xmlfile,
						st.getImagesSet().Name);
			}
			return null;
		}
		
	}
	
	class Tile extends ImageBox
	{
		Rectangle clip;
		CellTilesImages st;
		int tileID;
		String keyValue;
		
		public Tile(IImage img, Rectangle clip, StreamTiles st, int tileid, String key) 
		{
			super(img);
			this.st = (CellTilesImages)st;
			this.clip = clip;
			this.x = clip.x;
			this.y = clip.y;
			this.tileID = tileid;
			this.keyValue = key;
		}
		
		@Override
		public void render(Graphics2D g) {
			super.render(g);
			if (selected == this) {
				float alpha = 0.5f + (float)Math.sin(timer / 5.0f)/2;
				g.setColor(new Color(alpha, alpha, alpha, 1));
				Drawing.drawResize9(g, 0, 0, 
						background.getWidth(), 
						background.getHeight(), 
						3);
			}
			g.setColor(Color.WHITE);
			{
				if (chkShowTileID.isSelected()) {
					g.pushTransform();
					g.scale(1.0f/masterScale, 1.0f/masterScale);
					Drawing.drawStringBorder(g, tileID + "", 0, 0, TextAnchor.L_T);
					g.popTransform();
				}
				if (chkShowKey.isSelected()) {
					g.pushTransform();
					g.translate(0, clip.height);
					g.scale(1.0f/masterScale, 1.0f/masterScale);
					Drawing.drawStringBorder(g, keyValue + "", 0, 0, TextAnchor.L_B);
					g.popTransform();
				}
			}
		}
		
		public CellTileIndex getObject() {
			File imgFile = new File(res.xmlfile.getParent(), 
					st.getImagesSet().Name+"."+st.getImagesSet().Extention);
			return new CellTileIndex(res, res.xmlfile,
					st.getImagesSet().Name, 
					imgFile, 
					tileID, 
					clip, 
					AwtEngine.unwrap(st.srcImage));
		}
	}

}
