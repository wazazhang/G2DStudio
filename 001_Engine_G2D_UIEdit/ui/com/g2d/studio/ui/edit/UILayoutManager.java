package com.g2d.studio.ui.edit;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.cell.CUtil;
import com.cell.gameedit.StreamTiles;
import com.cell.gfx.IImage;
import com.cell.gfx.game.CSprite;
import com.cell.io.CFile;
import com.cell.reflect.IObjectStringParser;
import com.cell.reflect.Parser;
import com.cell.rpg.io.RPGObjectMap;
import com.cell.rpg.particle.ParticleDisplayResource;
import com.cell.rpg.template.TEffect;
import com.g2d.BufferedImage;
import com.g2d.Color;
import com.g2d.Font;
import com.g2d.Tools;
import com.g2d.action.Action;
import com.g2d.awt.util.AbstractDialog;
import com.g2d.awt.util.AbstractOptionDialog;
import com.g2d.awt.util.celledit.CellAtlasChooser;
import com.g2d.awt.util.celledit.CellAtlasIndex;
import com.g2d.awt.util.celledit.CellTileIndex;
import com.g2d.awt.util.celledit.CellTilesChooser;
import com.g2d.awt.util.celledit.CellTilesResource;
import com.g2d.awt.util.celledit.ImageUILayout;
import com.g2d.display.DisplayObject;
import com.g2d.display.DisplayObjectContainer;
import com.g2d.display.particle.Layer;
import com.g2d.display.particle.OriginShape;
import com.g2d.display.particle.ParticleData;
import com.g2d.display.particle.ParticleDisplay;
import com.g2d.display.ui.Button;
import com.g2d.display.ui.UIComponent;
import com.g2d.display.ui.layout.UILayout;
import com.g2d.display.ui.layout.UILayout.ImageStyle;
import com.g2d.editor.property.ObjectPropertyEdit;
import com.g2d.editor.property.ObjectPropertyPanel;
import com.g2d.editor.property.PopupCellEdit;
import com.g2d.editor.property.PopupCellEditUILayout;
import com.g2d.geom.Rectangle;
import com.g2d.java2d.impl.AwtEngine;
import com.g2d.java2d.impl.AwtGraphics2D;
import com.g2d.studio.res.Res;
import com.g2d.studio.swing.G2DList;
import com.g2d.studio.swing.G2DListItemData;
import com.g2d.studio.ui.edit.gui.SavedComponent;
import com.g2d.studio.ui.edit.gui.UEButton;
import com.g2d.studio.ui.edit.gui.UECanvas;
import com.g2d.studio.ui.edit.gui.UECheckBox;
import com.g2d.studio.ui.edit.gui.UEGauge;
import com.g2d.studio.ui.edit.gui.UEImageBox;
import com.g2d.studio.ui.edit.gui.UELabel;
import com.g2d.studio.ui.edit.gui.UERoot;
import com.g2d.studio.ui.edit.gui.UEScrollPan;
import com.g2d.studio.ui.edit.gui.UETextBox;
import com.g2d.studio.ui.edit.gui.UETextBoxHtml;
import com.g2d.studio.ui.edit.gui.UETextInput;
import com.g2d.studio.ui.edit.gui.UETextInputMultiline;
import com.g2d.studio.ui.edit.gui.UEToggleButton;
import com.g2d.studio.ui.edit.transition.*;

public class UILayoutManager extends com.g2d.display.ui.layout.UILayoutManager implements ParticleDisplayResource
{
	public static ImageIcon icon_close12 = new ImageIcon(Res.icon_close12);
	public static SimpleLayoutManager simple_layout = new UIEditSimpleLayoutManager();
	private Font default_font = null;
	
	private UILayout ui_default = new UILayout(ImageStyle.COLOR);
	private UILayout ui_null = new UILayout(ImageStyle.NULL);
	
	private UILayout ui_root = new UILayout();
	private UILayout ui_btn_d = new UILayout();
	private UILayout ui_btn_u = new UILayout();
	private UILayout ui_checkbox = new UILayout();
	private UILayout ui_label = new UILayout();
	private UILayout ui_canvas = new UILayout();
	private UILayout ui_imagebox = new UILayout();
	private UILayout ui_gauge_u = new UILayout();
	private UILayout ui_gauge_d = new UILayout();
	private UILayout ui_textinput = new UILayout();
	private UILayout ui_textbox = new UILayout();
	private UILayout ui_scrollpan = new UILayout();
	
	private BufferedImage img_checkbox_checked = null;
	private BufferedImage img_checkbox_unchecked = null;
	
	private HashMap<File, CellTilesResource> cpj_map = new HashMap<File, CellTilesResource>();
	private HashMap<File, UIEditImage> image_map = new HashMap<File, UIEditImage>();
	private UIEdit edit;
	
	public UILayoutManager(UIEdit edit)
	{
		this.edit = edit;
		Parser.registerObjectStringParser(UIEditImageAtlas.class, new UIEditImageAtlasParser());
		Parser.registerObjectStringParser(UIEditSprite.class, new UIEditSpriteParser());
		Parser.registerObjectStringParser(UIImageFont.class, new UIImageFont.UIImageFontParser());
		com.g2d.editor.Util.initParser();
		
		
		DisplayObjectContainer.FOCUS_TO_TOP = false;
		CellTilesChooser.filechooser_root = edit.resdir;
		PopupCellEditUILayout.uilayout_root = edit.resdir;
		PopupCellEditUILayout.uilayout_checker = new PopupCellEditUILayoutChecker();
		
		initFont();
		
		initLayout(ui_root, 		UIEditConfig.UI_DEFAULT_LAYOUT_ROOT);
		initLayout(ui_btn_d, 		UIEditConfig.UI_DEFAULT_LAYOUT_BUTTON_D);
		initLayout(ui_btn_u, 		UIEditConfig.UI_DEFAULT_LAYOUT_BUTTON_U);
		initLayout(ui_gauge_d, 		UIEditConfig.UI_DEFAULT_LAYOUT_GAUGE_D);
		initLayout(ui_gauge_u, 		UIEditConfig.UI_DEFAULT_LAYOUT_GAUGE_U);
		initLayout(ui_label, 		UIEditConfig.UI_DEFAULT_LAYOUT_LABEL);
		initLayout(ui_canvas, 		UIEditConfig.UI_DEFAULT_LAYOUT_CANVAS);
		initLayout(ui_imagebox, 	UIEditConfig.UI_DEFAULT_LAYOUT_IMAGEBOX);
		initLayout(ui_checkbox, 	UIEditConfig.UI_DEFAULT_LAYOUT_CHECKBOX);
		initLayout(ui_textinput,	UIEditConfig.UI_DEFAULT_LAYOUT_TEXTINPUT);
		initLayout(ui_textbox, 		UIEditConfig.UI_DEFAULT_LAYOUT_TEXTBOX);	
		initLayout(ui_scrollpan, 	UIEditConfig.UI_DEFAULT_LAYOUT_SCROLLPAN);		
		
		img_checkbox_checked 	= Tools.readImage(UIEditConfig.UI_DEFAULT_IMAGE_CHECKBOX_CHECKED);
		img_checkbox_unchecked 	= Tools.readImage(UIEditConfig.UI_DEFAULT_IMAGE_CHECKBOX_UNCHECKED);
		
		setInstance(this);
	}
	
	private void initLayout(UILayout layout, String cfg) 
	{
		String[] split = CUtil.splitString(cfg, ",", true);
		ImageStyle style = ImageStyle.valueOf(split[1]);
		if (style == ImageStyle.COLOR || style == ImageStyle.NULL) {
			layout.setStyle(style);
		} else {
			layout.setImages(Tools.readImage(split[0]), style,
					Integer.parseInt(split[2]));
		}
	}
	
	private void initFont() 
	{
		if (UIEditConfig.UI_DEFAULT_FONT != null) 
		{
			String[] split = CUtil.splitString(
					UIEditConfig.UI_DEFAULT_FONT.toLowerCase(),
					",", true);
			String name = split[0].trim();
			if (name.endsWith(".ttf")) 
			{
				try {
					File ffile = new File(UIEdit.getInstance().workdir, name);
					java.awt.Font jfont = java.awt.Font.createFont(
							java.awt.Font.TRUETYPE_FONT, ffile);
					AwtEngine.getEngine().getGE().registerFont(jfont);
					default_font = AwtEngine.wrap(jfont.deriveFont(
							Integer.parseInt(split[1]), 
							Integer.parseInt(split[2])));
					if (UIEditConfig.UI_FONT_FAMILY_FIXED)
					{
						AwtGraphics2D.setGlobalFont(jfont);
					}
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, 
							"注册字体失败: " + UIEditConfig.UI_DEFAULT_FONT);
				}
			} else {
				default_font = Parser.stringToObject(name, Font.class);
			}
		} else {
			
		}
	}
	
	public Font getDefaultFont() {
		return default_font;
	}
	
	
	public void setLayout(UIComponent ui)
	{
		if (ui instanceof SavedComponent) 
		{
			if (ui instanceof UERoot) {
				ui.setLayout(ui_root);
				ui.setSize(100, 30);
			}
			else if (ui instanceof UEButton || ui instanceof UEToggleButton) {
				((Button)ui).setLayout(ui_btn_u, ui_btn_d);
				ui.setSize(100, 30);
			}
			else if (ui instanceof UEGauge) {
				((UEGauge)ui).setLayout(ui_gauge_d, ui_gauge_u);
				ui.setSize(100, 24);
			}
			else if (ui instanceof UECheckBox) {
				((UECheckBox)ui).imageChecked 	= img_checkbox_checked;
				((UECheckBox)ui).imageUnchecked = img_checkbox_unchecked;
				ui.setLayout(ui_checkbox);
				ui.setSize(120, 24);
			}
			else if (ui instanceof UECanvas) {
				ui.setLayout(ui_canvas);	
				ui.setSize(200, 200);
			}
			else if (ui instanceof UEScrollPan) {
				ui.setLayout(ui_scrollpan);	
				ui.setSize(200, 200);
			}
			else if (ui instanceof UELabel) {
				ui.setLayout(ui_label);
				ui.setSize(100, 30);
			}
			else if (ui instanceof UETextInput || ui instanceof UETextInputMultiline) {
				ui.setLayout(ui_textinput);
				ui.setSize(100, 100);
			}
			else if (ui instanceof UETextBox || ui instanceof UETextBoxHtml) {
				ui.setLayout(ui_textbox);
				ui.setSize(100, 30);
			}
			else if (ui instanceof UEImageBox) {
				ui.setLayout(ui_imagebox);
				ui.setSize(100, 100);
			}
			else {
				ui.setLayout(ui_default);
			}
			if (edit.isToolGridEnable()) 
			{
				gridSize(ui.local_bounds);
			}
		}
		else
		{
			simple_layout.setLayout(ui);
		}
	}

//	@Override
//	public ImageUILayout createUILayout(
//			PopupCellEditUILayout edit,
//			BufferedImage image, 
//			File image_file,
//			ImageStyle style, 
//			int clip_border, 
//			Rectangle subImage)
//	{
//		UIEditImage img = new UIEditImage(image_file, image);
//		image_map.put(img.getFile(), img);
//		return new UIEditLayout(img, style, clip_border, subImage);
//	}
	static public UIImageFont dialogLoadImageFont(UIImageFont font)
	{
		File dir = CellAtlasChooser.filechooser_root;
		if (font != null)
		{
			File dfile = new File(
					UIEdit.getInstance().resdir,
					font.cpj_fileName);
			try {
				CellAtlasIndex cti = new CellAtlasChooser(new CellTilesResource(
						dfile), UIEdit.getInstance()).showDialog();
				if (cti != null) {
					UIImageFont ret = new UIImageFont(
							UIEdit.getInstance().getResSubName(cti.res.xmlfile),
							cti.imagesName);
					return ret;
				}
			} catch(Exception err) {
				JOptionPane.showMessageDialog(UIEdit.getInstance(), err.getMessage());
			}
		}
		else 
		{
			CellAtlasIndex cti = CellAtlasChooser.showDialog(UIEdit.getInstance(), dir);
			if (cti != null) {
				if (CFile.isChild(UIEdit.getInstance().resdir, cti.res.xmlfile)) {
					UIImageFont ret = new UIImageFont(
							UIEdit.getInstance().getResSubName(cti.res.xmlfile),
							cti.imagesName);
					return ret;
				} else {
					JOptionPane.showMessageDialog(UIEdit.getInstance(),
							"此文件目录不存在于工作空间！");
				}
			}
		}
		return null;
	
	}
	
	
	static public UIEditImage dialogLoadImage(String path)
	{
		if (path == null) {
			path = PopupCellEditUILayout.uilayout_root.getPath();
		}
		AtomicReference<File> outpath = new AtomicReference<File>();
		BufferedImage img = com.g2d.awt.util.Tools.dialogLoadImage(
				UIEdit.getInstance(), path, outpath);
		if (img != null) {
			if (CFile.isChild(UIEdit.getInstance().resdir, outpath.get())) {
				return new UIEditImage(outpath.get(), img);
			} else {
				JOptionPane.showMessageDialog(UIEdit.getInstance(), 
						"此文件目录不存在于工作空间！");
			}
		}
		return null;
	}
	
	static public UIEditImage dialogLoadImageAtlas(String path, UIEditImageAtlas atlas)
	{
		File dir = CellTilesChooser.filechooser_root;
		if (path != null) {
			dir = new File(UIEdit.getInstance().resdir, path);
		}
		if (atlas != null)
		{
			File dfile = new File(
					UIEdit.getInstance().resdir,
					atlas.cpj_fileName);
			try {
				CellTileIndex cti = new CellTilesChooser(new CellTilesResource(
						dfile), UIEdit.getInstance()).showDialog();
				UIEditImage ret = new UIEditImage(cti);
				return ret;
			} catch(Exception err) {
				JOptionPane.showMessageDialog(UIEdit.getInstance(), err.getMessage());
			}
		}
		else 
		{
			CellTileIndex cti = CellTilesChooser.showDialog(UIEdit.getInstance(), dir);
			if (cti != null) {
				if (CFile.isChild(UIEdit.getInstance().resdir, cti.imageFile)) {
					UIEditImage ret = new UIEditImage(cti);
					return ret;
				} else {
					JOptionPane.showMessageDialog(UIEdit.getInstance(),
							"此文件目录不存在于工作空间！");
				}
			}
		}
		return null;
	}
	
	public BufferedImage getAtlasBuffer(UIEditImageAtlas subpath) {
		UIEditImage img = getImageAtlas(subpath);
		if (img != null) {
			return img.getDisplayImage();
		}
		return null;
	}
	
	public BufferedImage getImageBuffer(String subpath)
	{
		UIEditImage img = loadImage(subpath);
		if (img != null) {
			return img.getDisplayImage();
		}
		return null;
	}
	
	public UIEditImage loadImage(String subpath)
	{
		if (subpath!=null && !subpath.isEmpty()) {
			File file = new File(edit.resdir.getPath(), subpath);
			UIPerforming.UseImage(file);
			UIEditImage ret = image_map.get(file);
			if (ret == null) {
				BufferedImage img = Tools.readImage(file.getPath());
				if (img != null) {
					ret = new UIEditImage(file, img);
					image_map.put(ret.getFile(), ret);
				}
			}
			return ret;
		}
		return null;
	}
		
	public UIEditImage loadImageAtlasRes(UIEditImageAtlas subpath, CellTilesResource res)
	{
		res = updateCpjRes(subpath.cpj_fileName, res);
		CellTileIndex idx = res.findTile(subpath.cpj_imageName, subpath.cpj_tileID);
		if (idx != null) {		
			UIPerforming.UseImage(
					idx.imageFile);
			UIPerforming.UseAtlas(
					res.xmlfile, 
					idx.imagesName, 
					res.getImages(idx.imagesName).getCount(),
					idx.tileID);
			return new UIEditImage(idx);
		} else {
			return null;
		}
	}
	
	public UIEditImage getImageAtlas(UIEditImageAtlas subpath)
	{
		 return loadImageAtlasRes(subpath, null);
	}

	public CellTilesResource getCpjRes(String xml_path, CellTilesResource res)
	{
		res = updateCpjRes(xml_path, res);
		return res;
	}
	
	private CellTilesResource updateCpjRes(String cpj_fileName, CellTilesResource res) {
		File file = new File(edit.resdir.getPath(), cpj_fileName);
		if (res != null) {
			cpj_map.put(file, res);
		} else {
			res = cpj_map.get(file);
		}
		if (res == null) {
			try {
				res = new CellTilesResource(file);
				cpj_map.put(file, res);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return res;
	}
	
	public UIEditSprite getUISpriteRes(UIEditSprite spr, CellTilesResource res)
	{
		res = updateCpjRes(spr.cpj_fileName, res);
		StreamTiles st = res.getImages(spr.cpj_imageName);
		if (st != null) {
			CSprite cspr = res.getSprite(spr.cpj_spriteName, st);
			if (cspr != null) {
				spr.csprite = cspr.clone();
				spr.csprite.setCurrentAnimate(spr.cpj_animID);
				return spr;
			}
		}
		return null;
	}
	public UIEditSprite getUISprite(UIEditSprite spr)
	{
		 return getUISpriteRes(spr, null);
	}
	
//	public BufferedImage putImage(String subpath, BufferedImage buff)
//	{
//		image_map.put(subpath, buff);
//		try {
//			File file = new File(edit.resdir, subpath);
//			if (!file.exists()) {
//				Tools.writeImage(file.getPath(), "png", buff);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return image_map.get(subpath);
//	}
	
	public void gridPos(UIComponent ui) 
	{
		int gw = edit.getGridSize();
		int gw2 = gw/2;
		
		int pw = ui.getIntX() % gw;
		int ph = ui.getIntY() % gw;
		if (pw < gw2) {
			ui.x = ui.x - pw;
		} else {
			ui.x = ui.x - pw + gw;
		}
		if (ph < gw2) {
			ui.y = ui.y - ph;
		} else {
			ui.y = ui.y - ph + gw;
		}
	}
	
	public void gridSize(Rectangle rect) 
	{
		int gw = edit.getGridSize();
		rect.x = ((int)rect.x)/gw*gw;
		rect.y = ((int)rect.y)/gw*gw;

		if (rect.width <= gw) {
			rect.width  = gw;
		} else {
			rect.width  = rect.width/gw*gw;
		}
		if (rect.height <= gw) {
			rect.height = gw;
		} else {
			rect.height = rect.height/gw*gw;
		}
	}
	
	
	public UITemplate[] getTemplates() {
		UITemplate[] templates = new UITemplate[] {
				new UITemplate(ui_btn_u, 	UEButton.class, 			"Button"),
				new UITemplate(ui_btn_u, 	UEToggleButton.class, 		"ToggleButton"),
				new UITemplate(ui_checkbox, UECheckBox.class, 			"CheckBox"),
				new UITemplate(ui_imagebox, UEImageBox.class, 			"ImageBox"),
				new UITemplate(ui_label, 	UELabel.class, 				"Label"),
				new UITemplate(ui_canvas, 	UECanvas.class, 			"Canvas"),
				new UITemplate(ui_textinput,UETextInput.class, 			"TextInput"),
				new UITemplate(ui_textinput,UETextInputMultiline.class, "TextInputMultiline"),
				new UITemplate(ui_textbox, 	UETextBox.class, 			"TextBox"),
				new UITemplate(ui_textbox, 	UETextBoxHtml.class, 		"TextBoxHtml"),
				new UITemplate(ui_gauge_d, 	UEGauge.class, 				"Gauge"),
				new UITemplate(ui_scrollpan,UEScrollPan.class, 			"ScrollPan"),
		};
		return templates;
	}
	
	public UILayout createLayoutSprite(UIEditSprite uisprite) throws Exception
	{
		try {
			UIEditSprite spr = getUISprite(uisprite);
			if (spr != null) {
				return UIEditLayout.createFromSprite(spr);
			} else {
				JOptionPane.showMessageDialog(edit, 
						"读取图片失败 - " + uisprite, 
						uisprite.cpj_fileName, 
						JOptionPane.ERROR_MESSAGE);
				return new UILayout();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"创建样式失败 - " + e.getMessage() + "\n" + 
					"   uisprite : " + uisprite);
			return UILayout.createRect(Color.RED);
		}
	}
	
	public UILayout createLayout(
			String image_file, 
			UIEditImageAtlas atlas,
			ImageStyle style,
			int clip_border) throws Exception
	{
		try {
			UIEditImage buf = null;
			if (atlas != null) {
				buf = getImageAtlas(atlas);
				if (buf == null) {
					JOptionPane.showMessageDialog(edit, 
							"读取图片失败 - " + atlas, 
							image_file, JOptionPane.ERROR_MESSAGE);
					return new UILayout();
				}
			} else {
				buf = loadImage(image_file);
				if (buf == null) {
					JOptionPane.showMessageDialog(edit, 
							"读取图片失败 - " + image_file, 
							image_file, JOptionPane.ERROR_MESSAGE);
					return new UILayout();
				}
			}
			return UIEditLayout.createFromImage(buf, style, clip_border);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"创建样式失败 - " + e.getMessage() + "\n" + 
					"      style : " + style + "\n" + 
					" image_file : " + image_file + "\n" + 
					"      atlas : " + atlas + "\n" + 
					"clip_border : " +clip_border);
			return UILayout.createRect(Color.RED);
		}
	}

	public void setEffect(UIComponent ui) {
		if (ui.uiEffect != null) {
			DisplayObject spr = createEffectNode(ui);
			ui.setUIEffect(spr);
		} else {
			ui.setUIEffect(null);
		}
	}
	
	private DisplayObject createEffectNode(UIComponent ui) {
		String uiEffect = ui.uiEffect;
		if (uiEffect != null && !uiEffect.isEmpty()) {
			String[] kv = CUtil.splitString(uiEffect, "|");
			if (kv.length > 0) {
				uiEffect = kv[0];
			}
			File teffect_dir = UIEdit.getInstance().getSubFile(
					UIEditConfig.G2D_SAVE_PATH + "/objects/teffect.obj");
			File teffect_xml = new File(teffect_dir, uiEffect+".xml");
			if (teffect_xml.exists()) {
				TEffect effect = RPGObjectMap.readNode(
						new ByteArrayInputStream(CFile.readData(teffect_xml)),
						teffect_xml.getPath(),
						TEffect.class);
				if (effect != null) {
					effect.setResource(this);
					UIEffect ret = new UIEffect(effect.particles);
					ui.uiEffect = effect.getID() + "|" + effect.name;
					return ret;
				}
			}
		}
		return null;
	}
	

	private CellTilesResource getEffectRes(String cpj_project_name) {
		try
		{
			File path = UIEdit.getInstance().getSubFile(
					UIEditConfig.G2D_EFFECTS_PATH + 
					"/" +cpj_project_name + 
					"/output/effect.xml");
			CellTilesResource resource = new CellTilesResource(path);
			
			return resource;
		} catch (Exception err) {
			err.printStackTrace();
		}
		return null;
	}
	
	public BufferedImage getEffectImage(
			String cpj_project_name, 
			String cpj_sprite_name, 
			int index)
	{
		try {
			CellTilesResource res = getEffectRes(cpj_project_name);
			if (res != null) {
				IImage image = res.getImage(
						res.SprTable.get(cpj_sprite_name).ImagesName,
						index);
				return Tools.createImage(image);
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		return null;
	}
	
	public CSprite getEffectSprite(
			String cpj_project_name, 
			String cpj_sprite_name) {
		try
		{
			CellTilesResource res = getEffectRes(cpj_project_name);
			if (res != null) {
				CSprite sprite = res.getSprite(cpj_sprite_name);
				return sprite;
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		return null;
	}
	
	
	public class PopupCellEditUILayoutChecker implements PopupCellEditUILayout.UILayoutEditAdapter
	{

		@Override
		public boolean checkFile(PopupCellEditUILayout edit, File file) {
			if (CFile.isChild(UIEdit.getInstance().resdir, file)) {
				return true;
			} else {
				JOptionPane.showMessageDialog(null, "此文件目录不存在于工作空间！");
				return false;
			}
		}
		
		@Override
		public ImageUILayout createUILayout(
				PopupCellEditUILayout edit, 
				BufferedImage image, 
				CSprite sprite,
				File image_file,
				File cpj_file, 
				String cpj_imagesName, 
				String cpj_spriteName, 
				int cpj_tileID,
				CellTilesResource cpj_res,
				Rectangle region, 
				ImageStyle style,
				int clip_border) 
		{
			if (style == ImageStyle.SPRITE) {
				UIEditSprite uispr = getUISpriteRes(new UIEditSprite(
						UIEdit.getInstance().getResSubName(cpj_file),
						cpj_imagesName,
						cpj_spriteName,
						cpj_tileID), cpj_res);
				return UIEditLayout.createFromSprite(uispr);
			}
			else {
				UIEditImage uiimg = null;
				if (cpj_file != null) {
					uiimg = loadImageAtlasRes(new UIEditImageAtlas(
							UIEdit.getInstance().getResSubName(cpj_file),
							cpj_imagesName,
							cpj_tileID
							), cpj_res);
				} else {
					uiimg = loadImage(UIEdit.getInstance().getResSubName(image_file));
				}
				return UIEditLayout.createFromImage(uiimg, style, clip_border);
			}
		}
		
	}
	
//	public void putLayout(ImageUILayout layout)
//	{
//		try {
//			File file = new File(edit.resdir, layout.image_file.getName());
//			if (!file.exists()) {
//				Tools.writeImage(file.getPath(), "png", layout.getSrcImage());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	public static class UIEditImageAtlas implements Serializable
	{
		private static final long serialVersionUID = 1L;
		
		public String 	cpj_fileName;
		public String 	cpj_imageName;
		public int		cpj_tileID;
		
		transient public boolean need_update = true;
		transient public BufferedImage image_buffer = null;
		
		
		public UIEditImageAtlas(
				String cpj_fileName, 
				String cpj_imageName,
				int cpj_tileID) 
		{
			this.cpj_fileName 	= cpj_fileName;
			this.cpj_imageName 	= cpj_imageName;
			this.cpj_tileID 	= cpj_tileID;
		}
		public UIEditImageAtlas() {}
		@Override
		public String toString() {
			return Parser.objectToString(this);
		}
		public BufferedImage update() {
			if (need_update || image_buffer == null) {
				image_buffer = UIEdit.getLayoutManager().getAtlasBuffer(this);
				need_update = false;
			}
			return image_buffer;
		}
		final protected Object writeReplace() throws ObjectStreamException {
			return this;
		}
		final protected Object readResolve() throws ObjectStreamException {
			need_update = true;
			return this;
		}
	}
	
	public static class UIEditImageAtlasParser implements IObjectStringParser
	{
		@Override
		public Object parseFrom(String str, Class<?> return_type) {
			while(str.startsWith("#")) {
				str = str.substring(1);
			}
			String[] kv = CUtil.splitString(str, "|");
			if (kv.length >= 3) {
				UIEditImageAtlas ret = new UIEditImageAtlas();
				ret.cpj_fileName	= kv[0];
				ret.cpj_imageName	= kv[1];
				ret.cpj_tileID		= Integer.parseInt(kv[2]);
				return ret;
			}
			return null;
		}
		@Override
		public String toString(Object obj) {
			UIEditImageAtlas ua = (UIEditImageAtlas)obj;
			return "#" + ua.cpj_fileName + "|" + ua.cpj_imageName+"|" + ua.cpj_tileID;
		}
	}
	
	public static class UIEditSprite implements Serializable
	{
		private static final long serialVersionUID = 1L;
		
		public String 	cpj_fileName;
		public String 	cpj_imageName;
		public String 	cpj_spriteName;
		public int		cpj_animID;
		
		public CSprite	csprite;
		
		public UIEditSprite(
				String cpj_fileName, 
				String cpj_imageName,
				String cpj_spriteName,
				int cpj_animID) 
		{
			this.cpj_fileName 	= cpj_fileName;
			this.cpj_imageName 	= cpj_imageName;
			this.cpj_spriteName = cpj_spriteName;
			this.cpj_animID 	= cpj_animID;
		}
		public UIEditSprite() {}
		@Override
		public String toString() {
			return Parser.objectToString(this);
		}
	}
	public static class UIEditSpriteParser implements IObjectStringParser
	{
		@Override
		public Object parseFrom(String str, Class<?> return_type) 
		{
			while(str.startsWith("@")) {
				str = str.substring(1);
			}
			String[] kv = CUtil.splitString(str, "|");
			if (kv.length >= 3) {
				UIEditSprite ret = new UIEditSprite();
				ret.cpj_fileName	= kv[0];
				ret.cpj_imageName	= kv[1];
				ret.cpj_spriteName	= kv[2];
				ret.cpj_animID		= Integer.parseInt(kv[3]);
				return ret;
			}
			return null;
		}
		
		@Override
		public String toString(Object obj) 
		{
			UIEditSprite ua = (UIEditSprite)obj;
			return "@" + ua.cpj_fileName + 
					"|" + ua.cpj_imageName+
					"|" + ua.cpj_spriteName+
					"|" + ua.cpj_animID;
		}
	}
	
	public static class UIEditImage
	{
		private String 				name;
		
		private File 				file;
		
		private UIEditImageAtlas 	atlas;
		
		private BufferedImage 		srcImage;
		private Rectangle			imageRegion;
		private BufferedImage 		buffer;
		
		public UIEditImage(File file, BufferedImage srcImage)
		{
			this.file 		= file;
			this.name 		= UIEdit.getInstance().getResSubName(file);
			
			this.srcImage  	= srcImage;
			this.buffer 	= srcImage;
		}
		
		public UIEditImage(CellTileIndex cti)
		{
			this.atlas		= new UIEditImageAtlas(
					UIEdit.getInstance().getResSubName(cti.res.xmlfile),
					cti.imagesName,
					cti.tileID);
			
			this.srcImage  	= AwtEngine.wrap_g2d(cti.image);
			this.buffer		= AwtEngine.wrap_g2d(cti.imageRegion());
			this.imageRegion= AwtEngine.wrap(cti.tileRect);
		}
		public UIEditImageAtlas getAtlas() {
			return atlas;
		}
		public String getName() {
			return name;
		}
		public File getFile() {
			return file;
		}
		public BufferedImage getDisplayImage() {
			return buffer;
		}
	}

	public static class UIEditLayout extends ImageUILayout
	{
		final public UIEditImage 	uiimg;
		final public UIEditSprite 	uisprite;
		
		public static UIEditLayout createFromSprite(UIEditSprite sprite)
		{
			return new UIEditLayout(
					null, 
					sprite,
					null, 
					sprite.csprite,
					null, 
					new File(UIEdit.getInstance().resdir, sprite.cpj_fileName),
					sprite.cpj_imageName,
					sprite.cpj_spriteName,
					sprite.cpj_animID,
					null, 
					ImageStyle.SPRITE,
					0);
		}
		
		public static UIEditLayout createFromImage(
				UIEditImage uiimg, 
				ImageStyle style, 
				int clipsize) 
		{
			if (uiimg.getAtlas() != null) {
				UIEditImageAtlas atlas = uiimg.getAtlas();
				return new UIEditLayout(
						uiimg, 
						null,
						uiimg.srcImage, 
						null,
						null, 
						new File(UIEdit.getInstance().resdir, atlas.cpj_fileName),
						atlas.cpj_imageName,
						null,
						atlas.cpj_tileID,
						uiimg.imageRegion, 
						style, clipsize);
			} else {
				return new UIEditLayout(
						uiimg, 
						null,
						uiimg.srcImage, 
						null,
						uiimg.getFile(), 
						null, 
						null, 
						null,
						0,
						null, 
						style, clipsize);
			}
		}
		
		private UIEditLayout(
				UIEditImage 	uiimg,
				UIEditSprite	uisprite,
				BufferedImage 	image, 
				CSprite			sprite,
				File 			image_file, 
				File 			cpj_file, 
				String 			cpj_imagesName, 
				String 			cpj_spriteName, 
				int 			cpj_tileID,
				Rectangle 		region,
				ImageStyle 		style,
				int 			clip_border) 
		{
			super(image, sprite, image_file, 
					cpj_file, 
					cpj_imagesName,
					cpj_spriteName,
					cpj_tileID, 
					region, 
					style, 
					clip_border);
			this.uiimg = uiimg;
			this.uisprite = uisprite;
		}
		
		private UIEditLayout(UIEditLayout o) {
			super(o) ;
			this.uiimg 		= o.uiimg;
			this.uisprite 	= o.uisprite;
		}
		@Override
		public UIEditLayout clone() {
			UIEditLayout ret = new UIEditLayout(this);
			return ret;
		}
		public String srcImageName() {
			return uiimg.getName();
		}
		public UIEditImageAtlas getAtlas() {
			return uiimg.getAtlas();
		}
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			if (uiimg != null) {
				if (uiimg.getAtlas() != null) {
					sb.append(uiimg.getAtlas() + "\n");
				} else {
					sb.append(uiimg.getName() + "\n");
				}
			} else {
				sb.append(uisprite.toString() + "\n");
			}
			sb.append(clip_style + "\n");
			sb.append(clip_border);
			return sb.toString();
		}
		
	}
	
	public static class UIEffect extends ParticleDisplay
	{
		public UIEffect(ParticleData data) 
		{
			super(data);
		}
		
		@Override
		public void update() 
		{
			if (UIEdit.getInstance().isToolShowEffect()) {
				super.update();
				Rectangle pbounds = getParent().local_bounds;
				for (Layer layer : getData()) {
					if (layer.origin_shape instanceof OriginShape.Ring) {
						OriginShape.Ring r = (OriginShape.Ring)layer.origin_shape;
						if (r.radius1 > r.radius2) {
							r.radius1 = pbounds.width/2;
						} else {
							r.radius2 = pbounds.width/2;
						}
					}
					else if (layer.origin_shape instanceof OriginShape.Rectangle) {
						OriginShape.Rectangle r = (OriginShape.Rectangle)layer.origin_shape;
						r.x = - pbounds.width/2;
						r.y = - pbounds.height/2;
						r.w = pbounds.width;
						r.h = pbounds.height;
					}
				}
				this.x = pbounds.width/2;
				this.y = pbounds.height/2;
			}
		}
	}
	
	public static class UIEffectSelecter extends PopupCellEdit<String>
	{
		private static final long serialVersionUID = 1L;

		public UIEffectSelecter(Object obj) {
			if (obj != null) {
				this.current_value = obj.toString();
			}
		}

		@Override
		public void setValue(Field field, String value, ObjectPropertyEdit comp) {
			super.setValue(field, value, comp);
			setText(value);
		}
		
		@Override
		public void onOpenEditor(String value) 
		{
			File teffect_dir = UIEdit.getInstance().getSubFile(
					UIEditConfig.G2D_SAVE_PATH+
					"/objects/teffect.obj");
			java.awt.FileDialog fd = new java.awt.FileDialog(UIEdit.getInstance());
			fd.setLocation(getLocation());
			fd.setDirectory(teffect_dir.getPath());
			fd.setTitle("选择资源");
			fd.setMode(FileDialog.LOAD);
			fd.setVisible(true);
			try {
				if (fd.getFile() != null && !fd.getFile().isEmpty()) {
					this.current_value = CUtil.replaceString(fd.getFile(), ".xml", "");
					this.setText(current_value);
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.current_value = null;
			UIEdit.getLayoutManager().setEffect((UIComponent)sender.getObject());
		}
		
		
	}


	public static class UIActionEditor extends AbstractOptionDialog<UIFadeAction> implements ListSelectionListener
	{
		private JSplitPane split = new JSplitPane();
		private JList<TypeItem> types = new JList<TypeItem>();
		
		public UIActionEditor(boolean isIN)
		{
			if (isIN)
			{
				types.setListData(new TypeItem[] {
						new TypeItem(AlphaFadeIn.class),
						new TypeItem(DropFadeIn.class),
						new TypeItem(ScaleFadeIn.class),
						new TypeItem(TranslateFadeIn.class),
				});
			}
			else
			{
				types.setListData(new TypeItem[] {
						new TypeItem(AlphaFadeOut.class),
						new TypeItem(ScaleFadeOut.class),
						new TypeItem(TranslateFadeOut.class),
				});
			}
			types.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			types.addListSelectionListener(this);
			types.setMinimumSize(new Dimension(200, 300));
			split.setLeftComponent(types);
			add(split, BorderLayout.CENTER);
		} 
		
		@Override
		public void valueChanged(ListSelectionEvent e) {
			TypeItem item = types.getSelectedValue();
			if (item != null)
			{
				split.setRightComponent(item.PropertyPanel);
			}
			else 
			{
				split.setRightComponent(new JPanel());
			}
		}
		
		@Override
		protected boolean checkOK() 
		{
			TypeItem item = types.getSelectedValue();
			return item != null;
		}
		
		@Override
		protected UIFadeAction getUserObject(ActionEvent e) {
			TypeItem item = types.getSelectedValue();
			return item.Instance;
		}
		
		private static class TypeItem
		{
			public UIFadeAction Instance;
			
			public Class<?> ActionType;
			
			public ObjectPropertyPanel PropertyPanel;
			
			public TypeItem(Class<?> ty)
			{
				try {
					this.ActionType = ty;
					this.Instance = (UIFadeAction)ty.newInstance();
					this.PropertyPanel = new ObjectPropertyPanel(Instance);
				} catch (Exception e) {}
			}
			
			@Override
			public String toString() 
			{
				return Instance.toString();
			}
		}
	}
	
	static class UIEditSimpleLayoutManager extends SimpleLayoutManager
	{
		public UIEditSimpleLayoutManager() {
			super.initLayout();
		}
	}

}
