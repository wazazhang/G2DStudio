package com.g2d.studio.gameedit.dynamic.util;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuListener;

import com.cell.CMath;
import com.cell.CUtil;
import com.cell.gfx.game.CSprite;
import com.cell.rpg.display.UnitNode;
import com.cell.rpg.template.TEffect;
import com.cell.rpg.template.TAvatar.FramePosition;
import com.cell.rpg.template.TAvatar.ParticleEmitter;
import com.cell.util.Pair;
import com.g2d.Font;
import com.g2d.Graphics2D;
import com.g2d.Tools;
import com.g2d.awt.util.AbstractFrame;
import com.g2d.cell.CellSprite;
import com.g2d.display.DisplayObject;
import com.g2d.display.DisplayObjectContainer;
import com.g2d.display.Sprite;
import com.g2d.display.event.MouseMoveEvent;
import com.g2d.display.event.MouseWheelEvent;
import com.g2d.display.particle.Layer;
import com.g2d.display.particle.ParticleDisplay;
import com.g2d.display.particle.ParticleSystem;
import com.g2d.display.ui.Button;
import com.g2d.display.ui.Container;
import com.g2d.display.ui.ImageButton;
import com.g2d.display.ui.Label;
import com.g2d.display.ui.Pan;
import com.g2d.display.ui.Panel;
import com.g2d.display.ui.ToggleButton;
import com.g2d.display.ui.ToggleButton.ImageToggleButton;
import com.g2d.display.ui.UIComponent;
import com.g2d.display.ui.layout.UILayout;
import com.g2d.display.ui.layout.UILayout.ImageStyle;
import com.g2d.editor.DisplayObjectPanel;
import com.g2d.editor.DisplayObjectPanel.ObjectStage;
import com.g2d.editor.property.ObjectPropertyDialog;
import com.g2d.java2d.impl.AwtEngine;
import com.g2d.studio.Studio;
import com.g2d.studio.StudioConfig;
import com.g2d.studio.cpj.CPJIndex;
import com.g2d.studio.cpj.CPJResourceSelectDialog;
import com.g2d.studio.cpj.CPJResourceType;
import com.g2d.studio.cpj.entity.CPJSprite;
import com.g2d.studio.gameedit.ObjectSelectDialogInteger;
import com.g2d.studio.gameedit.dynamic.DAvatar;
import com.g2d.studio.gameedit.dynamic.DEffect;
import com.g2d.studio.res.Res;
import com.g2d.studio.swing.G2DList;
import com.g2d.studio.swing.G2DListItem;
import com.g2d.studio.swing.G2DListItemData;
import com.g2d.util.Drawing;


@SuppressWarnings("serial")
public class AvatarEditor extends AbstractFrame implements ActionListener
{	
	static private com.g2d.Color DEFAULT_BACK_COLOR = new com.g2d.Color(0,0,0,0xFF);
	
	ReentrantLock 		lock 			= new ReentrantLock();

	PartList	part_list 		= new PartList();
	EffectList	effect_list		= new EffectList();
	ActionList	action_list		= new ActionList();
	
	JButton		btn_save		= new JButton(new ImageIcon(Res.icons_tool_bar[2]));
	
	JButton		btn_change_body	= new JButton("更换主角");
	JButton		btn_add_part	= new JButton("添加部件");
	JButton		btn_remove_part	= new JButton("删除部件");
	JButton		btn_move_up		= new JButton("部件置后");
	JButton		btn_move_down	= new JButton("部件置前");
	JMenu		men_part_blend	= new JMenu("部件渲染");
	JMenuItem	men_part_blend_NORMAL = new JMenuItem("NORMAL");
	JMenuItem	men_part_blend_SCREEN = new JMenuItem("SCREEN");
	
	JButton		btn_add_effect	= new JButton("添加粒子");
	JButton		btn_del_effect	= new JButton("删除粒子");
	JButton		btn_up_effect	= new JButton("粒子置前");
	JButton		btn_dw_effect	= new JButton("粒子置后");
	
	JButton		btn_change_bg	= new JButton("更换背景色");
	JSpinner 	btn_flare 		= new JSpinner(new SpinnerNumberModel(100f, 0f, 100f, 10f));
	
	ObjectStage 		stage 			= new ObjectStage(DEFAULT_BACK_COLOR);
	DisplayObjectPanel	stage_view 		= new DisplayObjectPanel(stage);
	AvatarLayer			avatar_layer	= new AvatarLayer();
	FrameLayer			frame_layer		= new FrameLayer();
	DAvatar 			current_avatar;
	ImageToggleButton	chk_auto_play;
	ImageToggleButton	chk_show_body;

	CellSprite body_spr;
	int current_animate = 0;
	int current_frame = 0;
	Hashtable<CPJIndex<CPJSprite>, PartLayer> avatar_map = new Hashtable<CPJIndex<CPJSprite>, PartLayer>();
	HashMap<ParticleEmitter, ParticleLayer> particle_map = new HashMap<ParticleEmitter, ParticleLayer>();
	float master_scale = 1;
	float master_flare = 1;
	
	public AvatarEditor(DAvatar avatar) 
	{
		super.setSize(960, 600);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		// Tools
		{
			JToolBar left_tools = new JToolBar();
			left_tools.setFloatable(false);
			
			btn_save.addActionListener(this);
			btn_change_body.addActionListener(this);
			btn_add_part.addActionListener(this);
			btn_remove_part.addActionListener(this);
			btn_move_up.addActionListener(this);
			btn_move_down.addActionListener(this);
			btn_change_bg.addActionListener(this);
			btn_change_bg.setBackground(AwtEngine.unwrap(stage.back_color));
			btn_add_effect.addActionListener(this);
			btn_del_effect.addActionListener(this);
			btn_up_effect.addActionListener(this);
			btn_dw_effect.addActionListener(this);
						
			btn_flare.setToolTipText("Flare");
			JMenuBar blend_menu = new JMenuBar();
			{
				men_part_blend_NORMAL.addActionListener(this);
				men_part_blend_SCREEN.addActionListener(this);
				men_part_blend.add(men_part_blend_NORMAL);
				men_part_blend.add(men_part_blend_SCREEN);
				blend_menu.add(men_part_blend);
			}
			left_tools.add(btn_save);
			left_tools.addSeparator();
			left_tools.add(btn_change_body);
			left_tools.add(btn_add_part);
			left_tools.add(btn_remove_part);
			left_tools.add(btn_move_up);
			left_tools.add(btn_move_down);
//			left_tools.add(blend_menu);
			left_tools.addSeparator();
			left_tools.add(btn_add_effect);
			left_tools.add(btn_del_effect);
//			left_tools.add(btn_up_effect);
//			left_tools.add(btn_dw_effect);
			left_tools.addSeparator();
			left_tools.add(btn_change_bg);
			left_tools.add(new JLabel("Flare:"));
			left_tools.add(btn_flare);
			left_tools.addSeparator();
			
			this.add(left_tools, BorderLayout.NORTH);
		}
		
		

		JSplitPane split_h1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JSplitPane split_h2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JSplitPane split_h3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		// part list
		{
			JScrollPane js = new JScrollPane(part_list);
			js.setPreferredSize(new Dimension(180, 200));
			js.setMinimumSize(new Dimension(180, 200));
			split_h3.setTopComponent(js);
		}
		// effect list
		{
			JScrollPane js = new JScrollPane(effect_list);
			js.setPreferredSize(new Dimension(180, 200));
			js.setMinimumSize(new Dimension(180, 200));
			split_h3.setBottomComponent(js);
		}
		// action list
		{
			JScrollPane js = new JScrollPane(action_list);
			js.setPreferredSize(new Dimension(200, 200));
			js.setMinimumSize(new Dimension(150, 200));
			split_h2.setRightComponent(js);
		}
		// stage
		{
			JPanel main = new JPanel(new BorderLayout());
			main.add(stage_view, BorderLayout.CENTER);
//			main.add(utilbar, BorderLayout.SOUTH);
			stage_view.getStage().addChild(avatar_layer);
			stage_view.getStage().addChild(frame_layer);
			stage_view.getCanvas().setFPS(StudioConfig.DEFAULT_FPS);
			stage_view.getSimpleCanvas().addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					part_list.clearSelection();
				}
			});
			split_h2.setLeftComponent(main);
		}
		split_h3.setResizeWeight(0.5f);
		split_h2.setResizeWeight(1);
		split_h1.setLeftComponent(split_h3);
		split_h1.setRightComponent(split_h2);
		
		super.add(split_h1);

		this.setAvatar(avatar);
		
		this.stage_view.start();
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				stage_view.stop();
			}
		});
	}
	
	private ParticleEmitter getSelectedEmitter() {
		EmitterInfo pe = effect_list.getSelectedItem();
		if (pe != null) {
			return pe.getData();
		}
		return null;
	}
	
	private ParticleLayer getSelectedEffect() {
		EmitterInfo pe = effect_list.getSelectedItem();
		if (pe != null) {
			return particle_map.get(pe.getData());
		}
		return null;
	}
	
	private PartInfo getSelectedPart() {
		PartInfo key = part_list.getSelectedItem();
		return key;
	}
	
	private CellSprite getSelectedPartSprite() {
		PartInfo key = part_list.getSelectedItem();
		CellSprite selected = key == null ? null : avatar_map.get(key.cpj);
		return selected;
	}
	
	private boolean isAutoPlay() {
		return chk_auto_play.isChecked();
	}

	private boolean isShowBody() {
		return chk_show_body.isChecked();
	}
	
	private void setAvatar(DAvatar avatar)
	{
//		synchronized(lock)
		{
			this.setTitle("AVATAR: "+avatar.getListName());
			this.current_avatar = avatar;
			CPJSprite cpj = Studio.getInstance().getCPJResourceManager().getNode(avatar.getBody());
			if (cpj != null)
			{			
				this.particle_map.clear();
				this.avatar_map.clear();
				
				this.avatar_layer.clearData();
				this.avatar_layer.setBody(cpj);
				
				this.action_list.resetAvatarBody();
				this.action_list.setSelectedIndex(current_animate);
				
				this.frame_layer.reset();
				
				this.part_list.resetList();
				this.effect_list.resetList();
				
				this.part_list.repaint();
				this.effect_list.repaint();
			}
		}
		this.repaint();
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (btn_change_bg == e.getSource()) 
		{
			Color color = JColorChooser.showDialog(this, "选择背景色", btn_change_bg.getBackground());
			if (color != null) {
				btn_change_bg.setBackground(color);
				DEFAULT_BACK_COLOR = AwtEngine.wrap(color);
				stage.back_color = DEFAULT_BACK_COLOR;
			}
		}
		else if (btn_flare == e.getSource())
		{
			master_flare -= 0.1f;
			if (master_flare < 0) {
				master_flare = 1;
			}
		}
		else if (current_avatar != null) 
		{
			if (e.getSource() == btn_save)
			{
				Studio.getInstance().getObjectManager().saveObject(current_avatar);
			} 
			else if (e.getSource() == btn_change_body)
			{
				CPJSprite spr = new CPJResourceSelectDialog<CPJSprite>(this, 
						CPJResourceType.ACTOR).showDialog();
				if (spr != null) {
					current_avatar.setBody(Studio.getInstance().getCPJResourceManager().getNodeIndex(spr));
				}
			} 

			// parts
			doAction_Part(e);
			// effects
			doAction_Effect(e);
			
			setAvatar(current_avatar);
		}
	}
	
	private void doAction_Part(ActionEvent e)
	{
		if (e.getSource() == btn_add_part) 
		{
			CPJSprite spr = new CPJResourceSelectDialog<CPJSprite>(this, 
					CPJResourceType.AVATAR).showDialog();
			if (spr != null) {
				current_avatar.addAvatarPart(Studio.getInstance().getCPJResourceManager().getNodeIndex(spr));
			}
		}
		else if (getSelectedPart() != null)		
		{
			PartInfo pinfo = getSelectedPart();
			
			if (e.getSource() == btn_remove_part)
			{
				current_avatar.removeAvatarPart(pinfo.node);
			} 
			else if (e.getSource() == btn_move_up) 
			{
				current_avatar.moveAvatarPart(pinfo.node, -1);
			}
			else if (e.getSource() == btn_move_down) 
			{
				current_avatar.moveAvatarPart(pinfo.node, 1);
			}
			else if (e.getSource() == men_part_blend_NORMAL) 
			{
				pinfo.node.blend_mode = Graphics2D.BLEND_MODE_NORMAL;
			}
			else if (e.getSource() == men_part_blend_SCREEN) 
			{
				pinfo.node.blend_mode = Graphics2D.BLEND_MODE_SCREEN;
			}
		}
	}
	
	private void doAction_Effect(ActionEvent e)
	{
		if (e.getSource() == btn_add_effect)
		{
			DEffect de = new ObjectSelectDialogInteger<DEffect>(
					this, DEffect.class, 8, 0, null).showDialog();
			if (de != null) {
				current_avatar.addAvatarEffect(de);
			}
		}
		else if (getSelectedEffect()!=null) 
		{
			if (e.getSource() == btn_del_effect)
			{
				current_avatar.removeAvatarEffect(getSelectedEffect().data);
			} 
			else if (e.getSource() == btn_up_effect)
			{
				getSelectedEffect().data.priority += 1;
			}
			else if (e.getSource() == btn_dw_effect)
			{
				getSelectedEffect().data.priority -= 1;
			}
		}
	}
	
	private FramePosition findNearParticleKeyFrame(int anim)
	{
		int frame_count = body_spr.cspr.getFrameCount(anim);
		int particlecount = current_avatar.getData().particles.size();
		for (int pi=0; pi<particlecount; pi++) {
			ParticleEmitter em = current_avatar.getData().particles.get(pi);
			for (int fi=0; fi<frame_count; fi++) {
				FramePosition fp = em.findFirstPos(anim, frame_count-1);
				if (fp != null) {
					return fp;
				}
			}
		}
		return null;
	}
	
//	--------------------------------------------------------------------------------------------------------
	
	class PartInfo extends G2DListItemData<UnitNode>
	{
		final CPJIndex<CPJSprite> cpj;
		final UnitNode node;
		final PartLayer layer ;
		String info;
		public PartInfo(CPJIndex<CPJSprite> cpj, UnitNode node, PartLayer layer) {
			super(node);
			this.cpj = cpj;
			this.node = node;
			this.layer = layer;
			StringBuffer sb = new StringBuffer();
			sb.append("<html><body>");
			sb.append("<p>" +
					"<font color=\"#000000\">" + cpj.toString() + " </font> " +
					"<font color=\"#ff0000\">" + "z=" + (int)(node.z) + "</font> " +
					"</p>");
			sb.append("</body></html>");
			this.info = sb.toString();
		}
		@Override
		public String toString() 
		{
			return info;
		}
		@Override
		public String getListName() {
			return info;
		}
		@Override
		public ImageIcon getListIcon(boolean update) {
			return null;
		}
		@Override
		public Component getListComponent(JList list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
			return null;
		}
	}
	
	
	
	class PartList extends G2DList<PartInfo>
	{
		public PartList()
		{
			this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//			this.setCellRenderer(new ListRender());
		}
		
		void resetList() 
		{
			Vector<PartInfo> infos = new Vector<PartInfo>();
			ArrayList<Pair<CPJIndex<CPJSprite>, UnitNode>> parts = current_avatar.getParts();
			for (Pair<CPJIndex<CPJSprite>, UnitNode> e : parts) {
				PartLayer ly = avatar_layer.addPart(e.getKey(), e.getValue());
				PartInfo pi = new PartInfo(e.getKey(), e.getValue(), ly);
				infos.add(pi);
			}
//			this.setListData(infos);
			this.setListItems(infos);
		}
		
//		class ListRender extends DefaultListCellRenderer
//		{
//			private static final long serialVersionUID = 1L;
//
//			public ListRender() {
//		         setOpaque(true);
//		     }
//			
//			public Component getListCellRendererComponent(
//					JList list, 
//					Object value,
//					int index, 
//					boolean isSelected,
//					boolean cellHasFocus) 
//			{
//				DefaultListCellRenderer render = (DefaultListCellRenderer)super.getListCellRendererComponent(
//						list, value, index, isSelected, cellHasFocus);
//				if (value instanceof CPJIndex) {
//					CPJIndex<?> item = (CPJIndex) value;
//					render.setText(item.toString() + " " + 0);
//				}
//				return render;
//			}
//
//		}

	}
	
	class ActionInfo
	{
		final String name;
		final int anim;
		final int frame_count;
		
		boolean k = false;
		String info;
		
		public ActionInfo(String name, int anim, int frame_count)
		{
			this.name = name;
			this.anim = anim;
			this.frame_count = frame_count;
			FramePosition fp = findNearParticleKeyFrame(anim);
			if (fp != null) {
				k = true;
			}
			StringBuffer sb = new StringBuffer();
			sb.append("<html><body>");
			sb.append("<p>" +
					"<font color=\"#008000\">" + anim + "</font> " +
					"<font color=\"#000000\">" + name + "</font> " +
					"<font color=\"#0000ff\">" + frame_count + "</font> " +
					"<font color=\"#ff0000\">" + (k?"(K)":"") + "</font> " +
					"</p>");
			sb.append("</body></html>");
			info = sb.toString();
		}
		
		@Override
		public String toString()
		{				
			return info;
		}
	}
	
	class ActionList extends JList<ActionInfo> implements ListSelectionListener
	{
		public ActionList() 
		{
			this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.addListSelectionListener(this);
		}
		
		void resetAvatarBody() 
		{
			int acount = body_spr.cspr.getAnimateCount();
			Vector<ActionInfo> actions = new Vector<ActionInfo>(acount);
			for (int a = 0; a < acount; a++) {
				String aname = body_spr.cspr.getAnimateName(a);
				actions.add(new ActionInfo(aname, a, body_spr.cspr.getFrameCount(a)));
			}
			current_animate = Math.min(acount, current_animate);
			current_frame = Math.min(acount, current_frame);
			setListData(actions);
		}
		
		@Override
		public void valueChanged(ListSelectionEvent e) 
		{
			ActionInfo name = this.getSelectedValue();
			if (name != null) {
				current_animate = body_spr.cspr.getAnimateIndex(name.name);
			}				
			frame_layer.reset();
		}
		
		
	}
	
	class EmitterInfo extends G2DListItemData<ParticleEmitter> implements ActionListener
	{
		String info;
		EmitterInfo(ParticleEmitter e)
		{
			super(e);
			StringBuffer sb = new StringBuffer();
			sb.append("<html><body><p>");
			sb.append("<font color=\"#000000\">EFFECT: " + getData().effectID + "</font> ");
			if (e.sync_anim) {
				sb.append("<font color=\"#00ff00\"> B</font> ");
			}
			sb.append("</p></body></html>");
			this.info = sb.toString();
		}
		@Override
		public String getListName() {
			return info;
		}
		@Override
		public ImageIcon getListIcon(boolean update) {
			return null;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof EmitterInfo) {
				return getData().equals(((EmitterInfo)obj).getData());
			}
			return false;
		}
		
		@Override
		public void onRightClicked(G2DList<?> list, MouseEvent e) {
			JPopupMenu pop = new JPopupMenu();
			JMenuItem menu_info = new JMenuItem("属性");
			menu_info.addActionListener(this);
			pop.add(menu_info);
			pop.show(list, e.getX(), e.getY());
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			ObjectPropertyDialog.showDialog(AvatarEditor.this, getData());
		}
	}
	
	class EffectList extends G2DList<EmitterInfo> implements ListSelectionListener
	{
		public EffectList() 
		{
			this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.addListSelectionListener(this);
//			Studio.getInstance().getObjectManager().getObject(type, id);
		}
		
		void resetList() 
		{
			if (current_avatar.getData().particles != null) {
				Vector<EmitterInfo> listdata = new Vector<AvatarEditor.EmitterInfo>();
				for (ParticleEmitter em : current_avatar.getData().particles) {
					ParticleLayer pl = new ParticleLayer(em);
					pl.setSpawnTarget(avatar_layer);
					avatar_layer.avatar.addChild(pl);
					particle_map.put(em, pl);
					listdata.add(new EmitterInfo(em));
				}
				this.setListItems(listdata);
			} else {
				this.setListItems(new EmitterInfo[0]);
			}			
		}
		
		@Override
		public void valueChanged(ListSelectionEvent e) 
		{

		}

		
	}
	
	class PartLayer extends CellSprite
	{
		public int blend_mode = Graphics2D.BLEND_MODE_NORMAL;
		public PartLayer(CSprite spr) {
			super(spr);
		}
		@Override
		public void render(Graphics2D g) {
			g.pushBlendMode();
			g.setBlendMode(blend_mode);
			super.render(g);
			g.popBlendMode();
		}
	}
	
	class ParticleLayer extends ParticleDisplay
	{
		DEffect effect;
		ParticleEmitter data;

		float dx = 0;
		float dy = 0;
		float dz = 0;
		
		public ParticleLayer(ParticleEmitter emitter) {
			this.data = emitter;
			this.effect = Studio.getInstance().getObjectManager().getObject(DEffect.class, emitter.effectID);
			if (effect != null) {
				effect.loadAppearance();
				setData(effect.getData().particles);
			}
		}
		
		@Override
		public void update() 
		{				
			super.update();
			
			this.z = data.priority;
			
			if (data.sync_anim) 
			{
				this.dx = 0;
				this.dy = 0;
				this.dz = data.priority;
				this.visible = true;
			}
			else
			{
				FramePosition fp = data.findFirstPos(
						current_animate,
						current_frame);
				if (fp != null) {
					this.dx = fp.x;
					this.dy = fp.y;
					this.dz = fp.z;
					this.visible = !fp.hide;
				} else {
					this.dx = 0;
					this.dy = 0;
					this.dz = 0;
					this.visible = false;
				}
			}
			setAnimateFrame(data.sync_anim, current_animate, current_frame);
		}
				
		@Override
		protected boolean addParticleNode(SingleObject node, Layer layer)
		{
			if (this.visible) {
				node.pos.y += dy;
				node.pos.x += dx;
				node.z = this.dz;
				node.main_scale = master_scale;
				super.addParticleNode(node, layer);
				return true;
			}
			return false;
		}
	}
	
	class AvatarLayer extends Sprite
	{			
		Sprite avatar = new Sprite();
		
		public AvatarLayer() 
		{
			enable 				= true;
			enable_input		= true;
			enable_focus		= true;
			enable_mouse_wheel	= true;
			this.avatar.x = 200;
			this.avatar.y = 200;
			this.addChild(avatar);
		}
		
		public void setBody(CPJSprite cpj) 
		{
			CSprite cspr = cpj.createCSprite();
			PartLayer pspr = new PartLayer(cspr);
			avatar.addChild(pspr);
			body_spr = pspr;
		}
		
		public PartLayer addPart(CPJIndex<CPJSprite> index, UnitNode u)
		{
			CPJSprite spr = Studio.getInstance().getCPJResourceManager().getNode(index);
			if (spr != null) {
				CSprite cspr = spr.createCSprite();
				PartLayer pspr = new PartLayer(cspr);
				pspr.user_data = index;
				pspr.blend_mode = u.blend_mode;
				pspr.z = u.z;
				avatar.addChild(pspr);
				avatar_map.put(index, pspr);
				return pspr;
			}
			return null;
		}

		public void clearData() {
			avatar.clearChilds();
		}
		
		private void setParticlePos(int mouseBtn) {
			if (mouseBtn == com.g2d.display.event.MouseEvent.BUTTON_RIGHT) {
				ParticleLayer pl = getSelectedEffect();
				if (pl != null) {
					pl.data.setCurrentOffset(
							current_animate,
							current_frame,
							avatar.getMouseX(), 
							avatar.getMouseY());
				}
			}
		}
		
		@Override
		protected void onMouseWheelMoved(MouseWheelEvent event) {
		}
		@Override
		protected void onMouseDown(com.g2d.display.event.MouseEvent event) {
			setParticlePos(event.mouseButton);
		}
		@Override
		protected void onMouseUp(com.g2d.display.event.MouseEvent event) {
			setParticlePos(event.mouseButton);
		}
		@Override
		protected void onMouseDraged(MouseMoveEvent event) {
			setParticlePos(event.mouseButton);
		}
		
		@Override
		public void update() 
		{
			int pw = this.getStage().getWidth();
			int ph = this.getStage().getHeight();
			setLocalBounds(0, 0, pw, ph - frame_layer.getHeight());

			if (getRoot().isMouseWheelDown()) {
//				master_scale += 0.1f;
			}
			else if (getRoot().isMouseWheelUP()) {
//				master_scale -= 0.1f;
			}
			if (isCatchedMouse()) {
				if (getRoot().isMouseHold(com.g2d.display.event.MouseEvent.BUTTON_LEFT)) {
					avatar.x = getMouseX();
					avatar.y = getMouseY();
				}
			}
			
			master_scale = Math.min(master_scale,  10.0f);
			master_scale = Math.max(master_scale, -10.0f);
			avatar.scale_x = avatar.scale_y = master_scale;
			
			master_flare = ((Double)btn_flare.getValue()).floatValue();
			master_flare = Math.min(master_flare, 100.0f);
			master_flare = Math.max(master_flare,   0.0f);
			stage.back_color.setAlpha(master_flare/100.0f);
			
			sort();
			avatar.sort();
		}
		
		@Override
		public void render(com.g2d.Graphics2D g) 
		{
			try {
				body_spr.getSprite().setCurrentAnimate(current_animate);
			} catch (Exception e) {}
			int banim = body_spr.getSprite().getCurrentAnimate();
			int bfram = body_spr.getSprite().getCurrentFrame();
			int banimc = body_spr.getSprite().getAnimateCount();
			int bframc = body_spr.getSprite().getFrameCount(banim); 
			
			CellSprite selected = getSelectedPartSprite();
			Vector<CellSprite> list = new Vector<CellSprite>(avatar_map.values());
			list.add(0, body_spr);
			
			int i=0;
			for (CellSprite cspr : list) 
			{	
				int anim = 0, fram = 0, animc = 0, framc = 0; 
				boolean error = false;
				try{
					cspr.getSprite().setCurrentFrame(
							body_spr.getSprite().getCurrentAnimate(), 
							body_spr.getSprite().getCurrentFrame());
					anim = cspr.getSprite().getCurrentAnimate();
					fram = cspr.getSprite().getCurrentFrame();
					animc = cspr.getSprite().getAnimateCount();
					framc = cspr.getSprite().getFrameCount(anim); 
					
					if (selected == cspr) {
						cspr.alpha = (float)Math.abs(Math.sin(timer/10f));
					} else {
						cspr.alpha = 1f;
					}
//					cspr.z = cspr.getSprite().
					if (anim == banim && fram == bfram && animc == banimc && framc == bframc) {
					} else {
						error = true;
					}
				}
				catch (Exception err) {
					error = true;
				}
				
				if (error) {
					g.setColor(com.g2d.Color.RED);
				} else if (selected == cspr) {
					g.setColor(com.g2d.Color.WHITE);
				} else {
					g.setColor(com.g2d.Color.GREEN);
				}
				
				com.g2d.util.Drawing.drawStringBorder(g, 
						CUtil.snapStringRightSize("资源:" + cspr.user_data, 20, ' ') + "   " +
						CUtil.snapStringRightSize("动画:" + anim+"/"+animc, 10, ' ') + "   " +
						CUtil.snapStringRightSize("帧:" + fram+"/"+framc, 10, ' ')   + "   " + 
						(body_spr==cspr ? "身体" : ""), 
						local_bounds.x+8, local_bounds.y+8 + i*20, 0);
				i++;
			}

			g.setColor(com.g2d.Color.WHITE);
			com.g2d.util.Drawing.drawStringBorder(g, 
					(int)(master_scale*100)+"%", 
					local_bounds.width - 8, 8,
					com.g2d.util.Drawing.TEXT_ANCHOR_RIGHT);
			
			if (isAutoPlay()) 
			{
				body_spr.getSprite().nextCycFrame();
				current_frame = body_spr.getSprite().getCurrentFrame();
			}
			else
			{
				body_spr.getSprite().setCurrentFrame(current_animate, current_frame);
//				if (getRoot().isKeyHold(KeyEvent.VK_CONTROL)) {
//					ParticleLayer pl = getSelectedEffect();
//					if (pl != null) {
//						g.drawString("EFFECT", getMouseX(), getMouseY());
//					}
//				}
			}
			body_spr.visible_content = isShowBody();
		}
	
	}
	
	final float FrameLayerSize = 0.4f;

	final UILayout ui_z_b = new UILayout(
			com.g2d.Tools.readImage("/com/g2d/studio/res/ui/avatar/z_b.png"),
			UILayout.ImageStyle.IMAGE_STYLE_BACK_4_CENTER, 0);
	final UILayout ui_z_h = new UILayout(
			com.g2d.Tools.readImage("/com/g2d/studio/res/ui/avatar/z_h.png"),
			UILayout.ImageStyle.IMAGE_STYLE_BACK_4_CENTER, 0);
	final UILayout ui_z_d = new UILayout(
			com.g2d.Tools.readImage("/com/g2d/studio/res/ui/avatar/z_d.png"),
			UILayout.ImageStyle.IMAGE_STYLE_BACK_4_CENTER, 0);
	final UILayout ui_z_u = new UILayout(
			com.g2d.Tools.readImage("/com/g2d/studio/res/ui/avatar/z_u.png"),
			UILayout.ImageStyle.IMAGE_STYLE_BACK_4_CENTER, 0);

	final UILayout flu = Res.ui_layout_mu715.deriveImage(
			UILayout.ImageStyle.IMAGE_STYLE_ALL_9,
			10);
	final UILayout fld = Res.ui_layout_mu714.deriveImage(
			UILayout.ImageStyle.IMAGE_STYLE_ALL_9,
			10);
	final UILayout flku = Res.ui_layout_mu171.deriveImage(
			UILayout.ImageStyle.IMAGE_STYLE_ALL_9,
			10);
	final UILayout flkd = Res.ui_layout_mu172.deriveImage(
			UILayout.ImageStyle.IMAGE_STYLE_ALL_9,
			10);
	final UILayout flkh = Res.ui_layout_mu175.deriveImage(
			UILayout.ImageStyle.IMAGE_STYLE_ALL_9,
			10);
	
	
	
	class FrameLayer extends Pan
	{
		Label lbl_status = new Label("选择粒子(鼠标右键)调整位置");
		
		Panel panel = new Panel();
				
		public FrameLayer() 
		{
			this.setCustomLayout(Res.ui_layout_mu18.deriveImage(
					ImageStyle.IMAGE_STYLE_ALL_9, 10));
			
			UILayout check_bg = Res.ui_layout_mu805.deriveImage(
					ImageStyle.IMAGE_STYLE_ALL_9, 4);
			
			chk_show_body = new ImageToggleButton(
					Res.ui_layout_mu218.getSrcImage(), 
					Res.ui_layout_mu211.getSrcImage());
			chk_show_body.setCustomLayout(check_bg, check_bg);
			chk_show_body.setBounds(2, 2, 26, 26);
			chk_show_body.setChecked(true);
			chk_show_body.tip = ("显示身体");

			chk_auto_play = new ImageToggleButton(
					Res.ui_layout_mu709.getSrcImage(), null);
			chk_auto_play.setCustomLayout(check_bg, check_bg);
			chk_auto_play.setBounds(32, 2, 26, 26);
			chk_auto_play.setChecked(true);
			chk_auto_play.tip = ("自动播放");
			
			lbl_status.setCustomLayout(UILayout.createBlankRect());
			panel.setCustomLayout(Res.ui_layout_mu73.deriveImage(
					ImageStyle.IMAGE_STYLE_ALL_9, 10, 8));
			panel.EnableMouseRightScroll = true;
			panel.EnableMouseWheelScroll = false;
			
			this.addComponent(chk_auto_play);
			this.addComponent(chk_show_body);
			this.addComponent(lbl_status);
			this.addComponent(panel);
		}
	
		@Override
		public void update() 
		{
			int pw = this.getStage().getWidth();
			int ph = this.getStage().getHeight();
			setLocalBounds(0, 0, pw, (int)(ph*FrameLayerSize));
			this.y = ph - getHeight();
			this.lbl_status.visible = !isAutoPlay();
			this.lbl_status.setBounds(0, getHeight() - 20, getWidth(), 20);
			this.panel.setBounds(0, 40, getWidth(), getHeight() - 40 - 20);
		}
		
		@Override
		public void render(Graphics2D g)
		{
			super.render(g);
		}
	
		synchronized public void reset()
		{
			panel.getContainer().clearChilds();
			int sx = 0;
			int sy = 0;
			int sw = 40;
			int sh = 40;
			int framecount = body_spr.cspr.getFrameCount(current_animate);
			if (framecount > 0) {
				for (int i=0; i<framecount; i++) {
					FrameButton gbtn = new FrameButton(i);
					gbtn.setSize(sw, sh);
					gbtn.setLocation(sx + i*sw, sy);
					panel.addContainerComponent(gbtn);
				}
				sy += 44;
			}
			if (current_avatar.getData().particles != null) 
			{
				int particlecount = current_avatar.getData().particles.size();
				for (int pi=0; pi<particlecount; pi++) 
				{
					ParticleEmitter em = current_avatar.getData().particles.get(pi);
					for (int fi=0; fi<framecount; fi++) 
					{
						FramePosition fp = em.getPos(current_animate, fi);
						FrameKeyButton fk = new FrameKeyButton(em, current_animate, fi, fp);
						fk.setSize(sw, sh);
						fk.setLocation(sx + fi*sw, sy);
						panel.addContainerComponent(fk);
					}
					
					sy += 44;
				}
			}
			
		}
		
		class FrameButton extends Button
		{
			final int frame;
			
			public FrameButton(int f) 
			{
				super("");
				this.frame = f;
				this.setCustomLayout(flu, fld);
			}
			
			@Override
			public void update()
			{

			}
			
			@Override
			protected void onMouseClick(com.g2d.display.event.MouseEvent event) {
				if (!isAutoPlay()) {
					current_frame = frame;
				}
			}
			
			@Override
			protected int renderLayout(Graphics2D g, boolean isDown) 
			{
				return super.renderLayout(g, current_frame == frame);
			}
			
			@Override
			public void render(Graphics2D g) 
			{
				super.render(g);
				
				if (isAutoPlay()) {
					g.setAlpha(Math.abs((float)Math.sin(timer*0.2f)));
				} else {
					g.setAlpha(1);
				}
				
				Font font = g.getFont();
				g.setFontSize(20);
				g.setFontAntialiasing(true);
				g.setColor(com.g2d.Color.WHITE);
				com.g2d.util.Drawing.drawStringBorder(g, 
						frame+"", 
						0, 0, getWidth(), getHeight(),
						Drawing.TEXT_ANCHOR_HCENTER | Drawing.TEXT_ANCHOR_VCENTER);	
				g.setFont(font);
				
			}
		}
		
		class FrameKeyButton extends Button implements com.g2d.display.ui.event.ActionListener
		{
			final ParticleEmitter pe;
			final int anim;
			final int frame;
			
			final ImageToggleButton btn_z_h = new ImageToggleButton();
			final Button btn_z_u = new Button("");
			final Button btn_z_d = new Button("");
//			final Button btn_blend = new Button("");

			FramePosition fp;
			
			public FrameKeyButton(ParticleEmitter pe, int anim, int frame, FramePosition fp) 
			{
				super("");
				this.pe = pe;
				this.fp = fp;
				this.anim = anim;
				this.frame = frame;
				this.setCustomLayout(flku, flkd);
				
				this.addChild(btn_z_h);
				this.addChild(btn_z_u);
				this.addChild(btn_z_d);
				
				btn_z_h.setChecked((fp == null) || fp.hide);
				
				btn_z_h.setCustomLayout(ui_z_h, ui_z_h.deriveGrayImage());
				btn_z_d.setCustomLayout(ui_z_d, ui_z_d.deriveGrayImage());
				btn_z_u.setCustomLayout(ui_z_u, ui_z_u.deriveGrayImage());
				btn_z_h.setSize(ui_z_h.getSrcImage().getWidth(), ui_z_h.getSrcImage().getHeight());
				btn_z_d.setSize(ui_z_h.getSrcImage().getWidth(), ui_z_h.getSrcImage().getHeight());
				btn_z_u.setSize(ui_z_h.getSrcImage().getWidth(), ui_z_h.getSrcImage().getHeight());
				btn_z_h.addEventListener(this);
				btn_z_d.addEventListener(this);
				btn_z_u.addEventListener(this);
				btn_z_h.tip = "隐藏(HIDE)";
				btn_z_d.tip = "置后(Z-)";
				btn_z_u.tip = "置前(Z+)";
				
//				btn_blend.setCustomLayout(ui_z_b);
//				btn_blend.setSize(ui_z_b.getSrcImage().getWidth(), ui_z_b.getSrcImage().getHeight());
//				btn_blend.addEventListener(this);
//				btn_blend.tip = "BLEND";
//				this.addChild(btn_blend);
				
			}
			
			@Override
			public void added(DisplayObjectContainer parent)
			{
				super.added(parent);				
				
				int h0 = getHeight()-btn_z_h.getHeight()-2;
				int h1 = getHeight()-btn_z_h.getHeight()-btn_z_h.getHeight()-4;
				
				btn_z_h.setLocation(btn_z_h.getWidth()*0+2, h0);
				btn_z_d.setLocation(btn_z_h.getWidth()*1+4, h0);
				btn_z_u.setLocation(btn_z_h.getWidth()*2+6, h0);

//				btn_blend.setLocation(btn_z_h.getWidth()*0+2, h1);

				
			}
			
			@Override
			public void update() 
			{
				super.update();
				
				if (!isAutoPlay() && current_frame == frame && getSelectedEmitter() == pe) {
					btn_z_h.visible = true;
					btn_z_d.visible = true;
					btn_z_u.visible = true;
//					btn_blend.visible = true;
				} else {
					btn_z_h.visible = false;
					btn_z_d.visible = false;
					btn_z_u.visible = false;
//					btn_blend.visible = false;
				}
			}
			
			@Override
			public void itemAction(UIComponent item, com.g2d.display.ui.event.ActionEvent event) {
				ParticleLayer pl = particle_map.get(pe);
				if (item == btn_z_h) {
					fp = pl.data.setCurrentPosHide(anim, frame, btn_z_h.isChecked());
				}
				if (item == btn_z_u) {
					fp = pl.data.setCurrentPosZ(anim, frame, +1.0f);
				}
				if (item == btn_z_d) {
					fp = pl.data.setCurrentPosZ(anim, frame, -1.0f);
				}
//				if (item == btn_blend) {
//					
//				}
			}
			
			@Override
			protected void onMouseDown(com.g2d.display.event.MouseEvent event) 
			{
				effect_list.setSelectedValue(new EmitterInfo(pe), true);
				if (!isAutoPlay()) {
					current_frame = frame;
				}
			}
			
			@Override
			protected int renderLayout(Graphics2D g, boolean isDown) 
			{
				int ret = super.renderLayout(g, current_frame == frame);
				if (current_frame == frame && getSelectedEmitter() == pe) {
					ret += flkh.render(g, 0, 0, getWidth(), getHeight());
				}
				return ret;
			}
			
			@Override
			public void render(Graphics2D g) 
			{
				super.render(g);
				if (fp != null) {
					Font font = g.getFont();
					g.setFontSize(10);
					g.setColor(com.g2d.Color.yellow);
					com.g2d.util.Drawing.drawStringBorder(g, 
							"K:"+(fp.hide?"h":"v")+":"+((int)fp.z), 
							2, 2, getWidth(), getHeight(),
							Drawing.TEXT_ANCHOR_LEFT | Drawing.TEXT_ANCHOR_TOP);	
					g.setFont(font);
				}
			}
		}
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
