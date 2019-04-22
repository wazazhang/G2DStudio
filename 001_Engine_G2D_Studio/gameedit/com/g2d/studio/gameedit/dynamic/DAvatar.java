package com.g2d.studio.gameedit.dynamic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.cell.rpg.display.Node;
import com.cell.rpg.display.UnitNode;
import com.cell.rpg.template.TAvatar;
import com.cell.rpg.template.TAvatar.ParticleEmitter;
import com.cell.rpg.template.ability.UnitAvatar;
import com.cell.util.Pair;
import com.cell.util.PairEntry;
import com.g2d.awt.util.Tools;
import com.g2d.studio.Studio;
import com.g2d.studio.cpj.CPJIndex;
import com.g2d.studio.cpj.CPJResourceType;
import com.g2d.studio.cpj.entity.CPJSprite;
import com.g2d.studio.gameedit.ObjectViewer;
import com.g2d.studio.gameedit.dynamic.util.AvatarEditor;
import com.g2d.studio.res.Res;


final public class DAvatar extends DynamicNode<TAvatar>
{	
	private CPJIndex<CPJSprite> body;
	private LinkedHashMap<UnitNode, CPJIndex<CPJSprite>> avatars =
			new LinkedHashMap<UnitNode, CPJIndex<CPJSprite>>();
	
	public DAvatar(IDynamicIDFactory<DAvatar> factory, 
			int id, String name, CPJIndex<CPJSprite> body) throws Exception
	{
		super(factory, id, name);
		setBody(body);
//		setParts(avatars);
	}
	
	public DAvatar(TAvatar avatar) {
		super(avatar, Integer.parseInt(avatar.getID()), avatar.name);
		setBody(new CPJIndex<CPJSprite>(CPJResourceType.ACTOR, 
				avatar.body.cpj_project_name, 
				avatar.body.cpj_object_id));
		for (Node node : avatar.body.sub_nodes) {
			if (node instanceof UnitNode) {
				UnitNode un = (UnitNode)node;
				avatars.put(un, new CPJIndex<CPJSprite>(
						CPJResourceType.AVATAR, 
						node.cpj_project_name, 
						node.cpj_object_id));
			}
		}
	}
	
	@Override
	protected TAvatar newData(IDynamicIDFactory<?> factory, String name, Object... args) {
		return new TAvatar(getIntID(), name);
	}
	
	@Override
	protected ImageIcon createIcon() {
		try {
			CPJSprite spr = getAvatarBodySpr();
			if (spr != null) {
				ImageIcon icom = spr.getIcon(true);
				if (icom != null) {
					return Tools.createIcon(
							Tools.combianImage(20, 20, 
									icom.getImage()));
				}
			}
		} catch (Exception e) {}
		return Tools.createIcon(Res.icon_res_2);
	}
	
//	----------------------------------------------------------------------------------------------------
	@Override
	public boolean setName(String name) {
		if(super.setName(name)){
			getData().name = name;
			return true;
		} else {
			return false;
		}
	}
	
	public CPJIndex<CPJSprite> getBody() {
		return body;
	}
	
	public void setBody(CPJIndex<CPJSprite> body) {
		if (body!=null) {
			UnitNode old = bind_data.body;
			this.body = body;
			this.bind_data.body = new UnitNode(
					body.cpj_file_name,
					body.set_object_name);
			if (old!=null) {
				this.bind_data.body.sub_nodes.addAll(old.sub_nodes);
			}
		}
	}

	public CPJSprite getAvatarBodySpr()
	{
		CPJIndex<CPJSprite> body_index = Studio
				.getInstance()
				.getCPJResourceManager()
				.getNode(CPJResourceType.ACTOR, 
						getData().body.cpj_project_name,
						getData().body.cpj_object_id);
		if (body_index != null) {
			return Studio.getInstance().getCPJResourceManager()
					.getNode(body_index);
		}
		return null;
	}
	
//	----------------------------------------------------------------------------------------------------
	
	public ArrayList<Pair<CPJIndex<CPJSprite>, UnitNode>> getParts() {
		ArrayList<Pair<CPJIndex<CPJSprite>, UnitNode>> ret =
				new ArrayList<Pair<CPJIndex<CPJSprite>, UnitNode>>();
		synchronized(avatars) {
			for (Map.Entry<UnitNode, CPJIndex<CPJSprite>> e : avatars.entrySet()) {
				ret.add(new Pair<CPJIndex<CPJSprite>, UnitNode>(e.getValue(), e.getKey()));
			}
		}
		return ret;
	}
	
	private void resetParts() {			
		synchronized(avatars) {
			bind_data.body.sub_nodes.clear();
			for (Map.Entry<UnitNode, CPJIndex<CPJSprite>> e : avatars.entrySet()) {
				bind_data.body.sub_nodes.add(e.getKey());
			}
		}
	}
	
	public UnitNode addAvatarPart(CPJIndex<CPJSprite> part) {
		synchronized(avatars) {
			UnitNode v = new UnitNode(part.cpj_file_name, part.set_object_name);
			avatars.put(v, part);
			resetParts();
			return v;
		}
	}
//	public Pair<CPJIndex<CPJSprite>, UnitNode> removeAvatarPart(int index) {
//		synchronized(avatars) {
//			try {
//				return avatars.remove(index);
//			} finally {
//				resetParts();
//			}
//		}
//	}

	public boolean removeAvatarPart(UnitNode nd) {
		synchronized (avatars) {
			try {
				avatars.remove(nd);
			} finally {
				resetParts();
			}
			return false;
		}
	}
	
	public void addAvatarEffect(DEffect de) {
		synchronized(avatars) {
			if (getData().particles == null) {
				getData().particles = new Vector<TAvatar.ParticleEmitter>();
			}
			ParticleEmitter pe = new ParticleEmitter();
			pe.effectID = de.getIntID();
			getData().particles.add(pe);
		}
	}
	
	public void removeAvatarEffect(ParticleEmitter em) {
		synchronized(avatars) {
			if (getData().particles != null) {
				getData().particles.remove(em);
			}
		}
	}
	
	public void moveAvatarPart(UnitNode nd, int direct) {
		try {
			synchronized(avatars) {
				if (avatars.get(nd) != null) {
					nd.z += direct;
				}
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

//	----------------------------------------------------------------------------------------------------
	
	@Override
	public ObjectViewer<?> getEditComponent() {
		onOpenEdit();
		if (edit_component==null) {
			edit_component = new AvatarViewer();
		}
		return edit_component;
	}
	
	
//	----------------------------------------------------------------------------------------------------------------------

	@SuppressWarnings("serial")
	public class AvatarViewer extends ObjectViewer<DAvatar>
	{
		private static final long serialVersionUID = 1L;
		
		public AvatarViewer() {
			super(DAvatar.this);
		}
		
		@Override
		protected void appendPages(JTabbedPane table) {
			table.insertTab("预览", null, new ViewPanel(), "", 0);
		}
		
		class ViewPanel extends JPanel implements ActionListener
		{
			JButton btn_open_viewer = new JButton("预览");
			public ViewPanel() {
				btn_open_viewer.addActionListener(this);
				this.add(btn_open_viewer);
			}
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btn_open_viewer) {
					AvatarEditor editor = new AvatarEditor(DAvatar.this);
					editor.setCenter();
					editor.setVisible(true);
				}
			}
		}
	}
	
}
