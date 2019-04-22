package com.g2d.studio.gameedit.dynamic;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;

import com.cell.CIO;
import com.cell.rpg.particle.ParticleResourceType;
import com.cell.rpg.particle.ParticleAppearanceType.DisplayNodeImage;
import com.cell.rpg.particle.ParticleAppearanceType.DisplayNodeSprite;
import com.cell.rpg.template.TEffect;
import com.g2d.awt.util.Tools;
import com.g2d.display.particle.Layer;
import com.g2d.studio.Studio;
import com.g2d.studio.cpj.CPJIndex;
import com.g2d.studio.cpj.CPJResourceType;
import com.g2d.studio.cpj.CPJEffectImageSelectDialog.TileImage;
import com.g2d.studio.cpj.entity.CPJSprite;
import com.g2d.studio.gameedit.dynamic.util.EffectEditor;
import com.g2d.studio.gameedit.dynamic.util.EffectTreeView;
import com.g2d.studio.gameedit.dynamic.util.EffectTreeView.EffectGroup;
import com.g2d.studio.gameedit.ObjectViewer;
import com.g2d.studio.res.Res;

final public class DEffect extends DynamicNode<TEffect>
{
	final EffectTreeView factory ;
	
	public DEffect(IDynamicIDFactory<DEffect> f, int id, String name) throws Exception {
		super(f, id, name);
		this.factory = (EffectTreeView)f;
	}
	
	public DEffect(IDynamicIDFactory<DEffect> f, TEffect effect) {
		super(effect, Integer.parseInt(effect.getID()), effect.name);
		this.factory = (EffectTreeView)f;
	}
	
	@Override
	protected TEffect newData(IDynamicIDFactory<?> f, String name, Object... args) {
		return new TEffect(getIntID(), name);
	}
	
	@Override
	public boolean setName(String name) {
		if(super.setName(name)){
			getData().name = name;
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public ImageIcon getIcon(boolean update) {
		return Tools.createIcon(Res.icon_res_8);
	}
	
	@Override
	public void onRightClicked(JTree tree, MouseEvent e) {
		new EffectMenu().show(tree, e.getX(), e.getY());
	}
	
	@Override
	public ObjectViewer<?> getEditComponent() {
		onOpenEdit();
		if (edit_component == null) {
			edit_component = new EffectViewer();
		}
		return edit_component;
	}
	@Override
	public void onSave() {
		super.onSave();
		if (edit_component instanceof EffectViewer) {
			((EffectViewer)edit_component).editor.getData();
		}
	}
	
	public void loadAppearance()
	{
		for (Layer layer : getData().particles) {
			if (layer.appearance instanceof DisplayNodeImage) {
				DisplayNodeImage appearance = (DisplayNodeImage) layer.appearance;
				TileImage ret = new TileImage(
						appearance.cpj_project_name,
						appearance.cpj_sprite_name,
						appearance.cpj_image_id);
				BufferedImage buff = ret.getEffectImage();
				appearance.setImage(Tools.wrap_g2d(buff));
			}
			else if (layer.appearance instanceof DisplayNodeSprite) {
				DisplayNodeSprite appearance = (DisplayNodeSprite) layer.appearance;
				CPJResourceType res_type = CPJResourceType.EFFECT;
				ParticleResourceType res_type_p = appearance.res_type;
				if (res_type_p == ParticleResourceType.EFFECT) {
					res_type = CPJResourceType.EFFECT;
				}
				else if (res_type_p == ParticleResourceType.ACTOR) {
					res_type = CPJResourceType.ACTOR;
				}
				else if (res_type_p == ParticleResourceType.AVATAR) {
					res_type = CPJResourceType.AVATAR;
				}
				CPJIndex<CPJSprite> index = Studio.getInstance().getCPJResourceManager().getNode(
						res_type, 
						appearance.cpj_project_name, 
						appearance.cpj_sprite_name);
				if (index != null) {
					CPJSprite spr = Studio.getInstance().getCPJResourceManager().getNode(index);
					if (spr != null) {
						appearance.setSprite(spr.createCSprite());
					}
				}
			}
		}
	}
	
//	----------------------------------------------------------------------------------------------------------------------
	public class EffectMenu extends DynamicNodeMenu
	{
		protected JMenuItem 		clone	= new JMenuItem("复制");
		
		public EffectMenu() {
			super(DEffect.this);
			super.add(clone);
			super.remove(delete);
			super.add(delete);
			clone.addActionListener(this);
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == clone) {
				if (node.getParent() instanceof EffectGroup) {
					try {
						EffectGroup	root	= (EffectGroup)node.getParent();
						TEffect		src		= CIO.cloneObject(DEffect.this.getData());
						DEffect		effect	= new DEffect(factory, 
								factory.idf().createID(), src.name+" Copy");
						effect.getData().particles.clear();
						for (Layer layer : src.particles) {
							effect.getData().particles.addLayer(layer);
						}
						factory.addNode(root, effect);
						factory.getTree().reload(root);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			} 
			else {
				super.actionPerformed(e);
			}
		}
	}
	
	
	public class EffectViewer extends ObjectViewer<DEffect>
	{
		private static final long serialVersionUID = 1L;
		
		EffectEditor editor;
		
		public EffectViewer() 
		{
			super(DEffect.this);
			editor = new EffectEditor(getData());
			table.remove(page_object_panel);
			table.remove(page_abilities);
			table.addTab("粒子编辑器", editor);
		}
		
		@Override
		protected void appendPages(JTabbedPane table) {}
	}
}
