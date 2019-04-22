package com.g2d.studio.gameedit.template;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.cell.rpg.template.TAvatar;
import com.cell.rpg.template.TUnit;
import com.cell.rpg.template.TemplateNode;
import com.cell.rpg.template.ability.UnitAvatar;
import com.cell.xls.XLSFile;
import com.cell.xls.XLSFullRow;
import com.g2d.awt.util.Tools;
import com.g2d.studio.Studio;
import com.g2d.studio.cpj.CPJIndex;
import com.g2d.studio.cpj.CPJResourceSelectDialog;
import com.g2d.studio.cpj.CPJResourceType;
import com.g2d.studio.cpj.entity.CPJFile;
import com.g2d.studio.cpj.entity.CPJSprite;
import com.g2d.studio.gameedit.ObjectAdapters;
import com.g2d.studio.gameedit.ObjectSelectDialog;
import com.g2d.studio.gameedit.ObjectViewer;
import com.g2d.studio.gameedit.dynamic.DAvatar;
import com.g2d.studio.gameedit.template.util.XLSObjectViewer;

final public class XLSUnit extends XLSTemplateNode<TUnit>
{
	private CPJSprite spr_cpj_sprite;
	private CPJSprite spr_avt_sprite;
	
	public XLSUnit(XLSFullRow xls_row, TemplateNode data) {
		super(xls_row, data);
//		if (template_data.getDisplayNode()!=null) {
//			CPJIndex<CPJSprite> spr_index = Studio.getInstance().getCPJResourceManager().getNode(
//					CPJResourceType.ACTOR, 
//					template_data.getDisplayNode().cpj_project_name,
//					template_data.getDisplayNode().cpj_object_id);
//			if (spr_index != null) {
//				cpj_sprite = spr_index.getObject();
//			}
//		}
	}
	
	@Override
	protected TUnit newData(XLSFullRow xls_row) {
		return new TUnit(getIntID(), xls_row.desc);
	}
	
	public CPJSprite getDisplaySpr() {
		 CPJSprite avt = getAvatarBody();
		 CPJSprite cpj = getCPJSprite();
		 if (avt != null) {
			 return avt;
		 } else {
			 return cpj;
		 }
	}
	
	private CPJSprite getAvatarBody()
	{
		UnitAvatar uavatar = this.getData().getAbility(UnitAvatar.class);
		if (uavatar != null) {
			DAvatar davatar = Studio.getInstance().getObjectManager().getObject(
					DAvatar.class, uavatar.avatar_id);
			if (davatar != null) {
				if (spr_avt_sprite == null) {
					CPJIndex<CPJSprite> body_index = Studio
							.getInstance()
							.getCPJResourceManager()
							.getNode(CPJResourceType.ACTOR, 
									davatar.getData().body.cpj_project_name,
									davatar.getData().body.cpj_object_id);
					if (body_index != null) {
						spr_avt_sprite = Studio.getInstance().getCPJResourceManager()
								.getNode(body_index);
						return spr_avt_sprite;
					}
				}
			} else {
				spr_avt_sprite = null;
			}
		} else {
			spr_avt_sprite = null;
		}
		return spr_avt_sprite;
	}
	
	private CPJSprite getCPJSprite() {
		if (spr_cpj_sprite == null && template_data.getDisplayNode()!=null) {
			CPJIndex<CPJSprite> spr_index = Studio.getInstance().getCPJResourceManager().getNode(
					CPJResourceType.ACTOR, 
					template_data.getDisplayNode().cpj_project_name,
					template_data.getDisplayNode().cpj_object_id);
			if (spr_index != null) {
				spr_cpj_sprite = spr_index.getObject();
			}
		}
		return spr_cpj_sprite;	
	}
	
	private void setCPJSprite(CPJSprite sprite) {
		template_data.setDisplayNode(sprite.parent.getName(), sprite.getName());
		this.spr_cpj_sprite = sprite;
	}
	
	@Override
	public ImageIcon getIcon(boolean update) {
		if (icon_file==null) {
			icon_file = Studio.getInstance().getIconManager().getIcon("0");
		}
		return super.getIcon(update);
	}
	
	@Override
	public ImageIcon createIcon() {
		CPJSprite spr = getDisplaySpr();
		if (spr != null) {
			ImageIcon icom = spr.getIcon(true);
			if (icom != null) {
				return Tools.createIcon(
						Tools.combianImage(20, 20, 
								icom.getImage()));
			}
		}
		return super.createIcon();
	}

	public ObjectViewer<?> getEditComponent(){
		onOpenEdit();
		if (edit_component==null) {
			edit_component = new NPCObjectViewer();
		}
		return edit_component;
	}
	
	public void resetResource(CPJFile cpj) {
		if (template_data.getDisplayNode() != null && 
			template_data.getDisplayNode().cpj_project_name.equals(cpj.getName())) {
			spr_cpj_sprite = null;
			spr_avt_sprite = null;
			getCPJSprite();
			getAvatarBody();
		}
	}
	
//	-----------------------------------------------------------------------------------------------------------------
	
	class NPCObjectViewer extends XLSObjectViewer<XLSUnit> implements ActionListener
	{
		private static final long serialVersionUID = 1L;
		
		JPanel	page_properties;
		JButton	set_binding		= new JButton("绑定资源");
		JButton	set_avatar		= new JButton("设置AVATAR");
		
		public NPCObjectViewer() 
		{
			super(XLSUnit.this,
					new ObjectAdapters.UnitBattleTeamNodeAdapter(),
					new ObjectAdapters.ItemListIDSelectAdapter(),
					new ObjectAdapters.ShopItemListIDSelectAdapter()
			);
			if (spr_cpj_sprite!=null) {
				set_binding.setIcon(spr_cpj_sprite.getIcon(false));
				set_binding.setText("(" + spr_cpj_sprite.name + ")" + "绑定资源");
			}
			if (spr_avt_sprite!=null) {
				set_avatar.setIcon(spr_avt_sprite.getIcon(false));
				set_avatar.setText("(" + spr_avt_sprite.name + ")" + "绑定资源");
			}
			page_properties = new JPanel();
			page_properties.setLayout(new GridLayout(1, 2));
			{
				set_binding.addActionListener(this);
				page_properties.add(set_binding);
				
				set_avatar.addActionListener(this);
				page_properties.add(set_avatar);
			}
			table.insertTab("属性", null, page_properties, null, 0);
			table.setSelectedIndex(0);
		}
		

		public void actionPerformed(ActionEvent e)
		{
			if (set_binding==e.getSource()) {
				CPJSprite spr = new CPJResourceSelectDialog<CPJSprite>(this,
						CPJResourceType.ACTOR).showDialog();
				if (spr != null) {
					setCPJSprite(spr);
					XLSUnit.this.getIcon(true);
					set_binding.setIcon(spr_cpj_sprite.getIcon(false));
					set_binding.setText("(" + spr_cpj_sprite.name + ")" + "绑定资源");
				}
			}
			else if (set_avatar == e.getSource()) {
				DAvatar davatar = new ObjectSelectDialog<DAvatar>(this, DAvatar.class, 8).showDialog();
				if (davatar!=null) {
					UnitAvatar tavatar = new UnitAvatar();
					tavatar.avatar_id = davatar.getIntID();
					getData().addAbility(tavatar);
					getAvatarBody();
					XLSUnit.this.getIcon(true);
					set_avatar.setIcon(spr_avt_sprite.getIcon(false));
					set_avatar.setText("(" + spr_avt_sprite.name + ")" + "绑定资源");
				}
			}
			Studio.getInstance().getObjectManager().getPage(XLSUnit.class).repaint();
		}
		
	}
	
		
	
	
	
	
	
	
	
	
	
	
}
