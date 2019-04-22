package com.g2d.awt.util.celledit;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.cell.CUtil;
import com.cell.gameedit.object.SpriteSet;
import com.cell.gfx.game.CSprite;
import com.g2d.Color;
import com.g2d.Graphics2D;
import com.g2d.display.Sprite;
import com.g2d.display.event.MouseEvent;
import com.g2d.editor.DisplayObjectPanel;
import com.g2d.geom.Point;
import com.g2d.util.Drawing;

public class CellSpriteList extends JPanel implements ListSelectionListener
{
	private static final long serialVersionUID = 1L;
	
	private CellTilesResource res;
	
	private SpriteStage g2d_stage;
	private JList namelist;	
	private SpriteBox selected = null;
	
	public CellSpriteList(CellTilesResource res) 
	{
		super(new BorderLayout());
		this.res = res;
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		Vector<String> names = new Vector<String>(res.SprTable.keySet());
		CUtil.sort(names, CUtil.getStringCompare());
		{
			namelist = new JList(names);
			namelist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			namelist.addListSelectionListener(this);
			JScrollPane namescroll = new JScrollPane(namelist);
			namescroll.setMinimumSize(new Dimension(100, 200));
			split.setLeftComponent(namescroll);
		} {
			g2d_stage = new SpriteStage();
			DisplayObjectPanel stage_view = new DisplayObjectPanel(g2d_stage);
			stage_view.getCanvas().setFPS(30);
			stage_view.start();
			split.setRightComponent(stage_view);
		}
		add(split, BorderLayout.CENTER);
		
		namelist.setSelectedIndex(0);
	}
	
	public CellSpriteIndex getSelectedObject() {
		if (selected != null) {
			CellSpriteIndex idx = new CellSpriteIndex(
					res, 
					res.xmlfile, 
					selected.sprset.ImagesName, 
					selected.sprset.Name, 
					selected.cspr.getCurrentAnimate(),
					selected.cspr.clone());
			return idx;
		}
		return null;
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		String name = namelist.getSelectedValue().toString();
		g2d_stage.changeSprite(name);
	}
	
	class SpriteStage extends DisplayObjectPanel.ObjectStage
	{
		Sprite root = new Sprite();
		String currentSpriteName;
		Point lastmousedown = null;
		
		public SpriteStage() 
		{
			super(Color.black);
			root.enable 		= true;
			root.enable_input	= true;
			root.enable_focus	= true;
			root.enable_childs	= true;
			this.addChild(root);
		}

		public void changeSprite(String name)
		{
			currentSpriteName = name;
			root.clearChilds();
			CSprite cspr = res.getSprite(name);
			selected = new SpriteBox(cspr, name);
			root.addChild(selected);
		}
		
		@Override
		public void update() 
		{
			root.local_bounds.width = getRoot().getStageWidth();
			root.local_bounds.height = getRoot().getStageHeight();
			
			if (getRoot().isMouseDown(MouseEvent.BUTTON_RIGHT)) {
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
			if (currentSpriteName != null && selected != null) {
				g.setColor(Color.WHITE);
				int anim = selected.cspr.getCurrentAnimate();
				Drawing.drawStringBorder(g, 
						"（右键拖拽）当前精灵：" + currentSpriteName + "  " +
						"（滚轮切换动画）动画：" + selected.cspr.getAnimateName(anim) + 
						"[" + anim + "]", 
						1, getHeight()-1, 
						Drawing.TEXT_ANCHOR_LEFT | Drawing.TEXT_ANCHOR_BOTTON);
			}
		}
		
		
	}
	
	class SpriteBox extends Sprite
	{
		final CSprite cspr;
		final String sprname;
		final SpriteSet sprset;
		
		public SpriteBox(CSprite cspr, String name) {
			this.cspr = cspr;
			this.sprname = name;
			this.sprset = res.getSetSprite(name);
		}
		@Override
		public void update() 
		{
			this.x = getParent().getWidth()/2;
			this.y = getParent().getHeight()/2;
			if (getRoot().isMouseWheelDown()) {
				cspr.nextAnimate(1);
			}
			if (getRoot().isMouseWheelUP()) {
				cspr.nextAnimate(-1);
			}
		}
		@Override
		public void render(Graphics2D g) {
			cspr.render(g, 0, 0);
			cspr.nextCycFrame();
		}
	}


}
