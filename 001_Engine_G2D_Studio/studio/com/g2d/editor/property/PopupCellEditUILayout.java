package com.g2d.editor.property;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.cell.gfx.game.CCD;
import com.cell.gfx.game.CSprite;
import com.g2d.Engine;
import com.g2d.awt.util.AbstractFrame;
import com.g2d.awt.util.celledit.CellSpriteChooser;
import com.g2d.awt.util.celledit.CellSpriteIndex;
import com.g2d.awt.util.celledit.CellTileIndex;
import com.g2d.awt.util.celledit.CellTilesChooser;
import com.g2d.awt.util.celledit.CellTilesResource;
import com.g2d.awt.util.celledit.ImageUILayout;
import com.g2d.display.ui.UIComponent;
import com.g2d.display.ui.layout.UILayout;
import com.g2d.display.ui.layout.UILayout.ImageStyle;
import com.g2d.editor.Util;
import com.g2d.geom.Rectangle;
import com.g2d.java2d.impl.AwtEngine;
import com.g2d.studio.swing.G2DWindowToolBar;

/**
 * @author WAZA
 * 负责在JTable里更改颜色
 */
public class PopupCellEditUILayout extends PopupCellEdit<UILayout>
{
	public static File uilayout_root;
	public static UILayoutEditAdapter uilayout_checker;
	public static com.g2d.Color back_color = new com.g2d.Color(0xff000000);
	private static File last_uilayout_root;
	private static File last_cpj_root;
	
	UIComponent object;
	
	public PopupCellEditUILayout()
	{
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (current_value!=null) {
			com.g2d.Graphics2D g2d = AwtEngine.wrap((Graphics2D)g);
			g2d.setColor(back_color);
			current_value.render(g2d, 0, 0, getWidth(), getHeight());
		}
	}

	@Override
	public void setValue(Field field, UILayout value, ObjectPropertyEdit comp) {
		super.setValue(field, value, comp);
		
	}
	
	@Override
	public void onOpenEditor(UILayout value) 
	{
		try {
			object = (UIComponent)sender.getObject();
			MakeLayoutForm dialog = new MakeLayoutForm(value);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public class MakeLayoutForm extends AbstractFrame implements ActionListener
	{
		private static final long serialVersionUID = 1L;

		JButton tool_changesrc = new JButton("更换图片");
		JButton tool_changeatlas = new JButton("更换图集");
		JButton tool_changesprite = new JButton("更换精灵");
		JButton tool_cleansrc = new JButton("清除图片");
		G2DWindowToolBar tools;

		BufferedImage 		image;
		CSprite				sprite;
		File 				image_file;
		File 				cpj_file;
		String				cpj_imageName;
		String				cpj_spriteName;
		int					cpj_spriteAnim;
		int					cpj_tileID;
		CellTilesResource 	cpj_res;
		Rectangle			imageRegion;
		UILayout.ImageStyle image_style = ImageStyle.NULL;

		// left pan
		JPanel pan1 = new JPanel();
		SrcCanvas src_canvas;

		// left bot
		JPanel pan3 = new JPanel();
		DstView dstview;

		// right pan
		JPanel pan2 = new JPanel();
		JLabel lbl_broadsize = new JLabel("切割尺寸");
		JSpinner spin_broadsize = new JSpinner();
		ButtonGroup group_styles;
		JLabel lbl_styles = new JLabel("切割样式");

		// area split
		JSplitPane lsplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pan1,
				pan3);
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, lsplit,
				pan2);

		
		JLabel lbl_border_color 	= new JLabel("BorderColor");
		JLabel lbl_back_color 		= new JLabel("BackColor");
		JButton btn_border_color 	= new JButton("更换边颜色");
		JButton btn_back_color 		= new JButton("更换背景色");
		
		
		JRadioButton chk_preferredSize = new JRadioButton("调整为首选尺寸");
		JRadioButton chk_repeatFill = new JRadioButton("平铺");
		
		JButton ok = new JButton("确定");
		JButton cancel = new JButton("取消");

		public MakeLayoutForm() throws Exception{
			this(null);
		}
		
		@SuppressWarnings("deprecation")
		public MakeLayoutForm(UILayout rect) throws Exception
		{
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			
			tools				= new G2DWindowToolBar(this, false, false, false, false,false);
			
//			object 				= src;
			src_canvas 			= new SrcCanvas() ;
			dstview				= new DstView();
			
			setSize(800,600);
			setLocation(
					AbstractFrame.getScreenWidth()/2 - getWidth()/2,
					AbstractFrame.getScreenHeight()/2 - getHeight()/2);

			if (rect instanceof ImageUILayout) 
			{
				ImageUILayout srect = (ImageUILayout)rect;
				
				image 			= AwtEngine.unwrap(srect.getSrcImage());	
				image_file 		= srect.srcImageFile();
				sprite			= srect.getSprite();
				
				cpj_file		= srect.cpj_file;
				cpj_imageName	= srect.cpj_imagesName;
				cpj_spriteName	= srect.cpj_spriteName;
				cpj_spriteAnim	= srect.cpj_tileID;
				cpj_tileID		= srect.cpj_tileID;
				
				imageRegion		= srect.getSrcRect();
				
				spin_broadsize.setValue(srect.clip_border);
			} 
			
			if (rect != null) {
				lbl_back_color.setForeground(
						AwtEngine.unwrap(rect.getBackColor()));
				lbl_border_color.setForeground(
						AwtEngine.unwrap(rect.getBorderColor()));
				image_style = rect.getStyle();
				chk_repeatFill.setSelected(rect.isRepeatFill());
			} else {
//				changeSrcImage();
				UILayout srect = new UILayout();
				lbl_back_color.setForeground(
						AwtEngine.unwrap(srect.getBackColor()));
				lbl_border_color.setForeground(
						AwtEngine.unwrap(srect.getBorderColor()));
				chk_repeatFill.setSelected(false);
			}
			
			
			// left
			{
			
				{
					pan1.setLayout(new BorderLayout());	
					
					if (image != null) {
						src_canvas.setSize(
								image.getWidth(null), 
								image.getHeight(null));
						src_canvas.setPreferredSize(new Dimension(
								image.getWidth(null), 
								image.getHeight(null)));
					}
					src_canvas.setCursor(
							Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
					pan1.add(new JScrollPane(src_canvas));
					pan1.setMinimumSize(new Dimension(220, 220));
				}
				
				{
					pan3.setLayout(null);
					pan3.setMinimumSize(new Dimension(220, 220));
					pan3.add(dstview);
				}
			}
			
			// right
			{
				int sx = 10, sy = 10;
				
				pan2.setLayout(null);
				lbl_broadsize.setBounds (sx, sy+=20, 100, 20);
				spin_broadsize.setBounds(sx, sy+=20, 100, 20);
				
				spin_broadsize.addChangeListener(new SpinChanged());
				spin_broadsize.setValue(8);			
				pan2.add(lbl_broadsize);
				pan2.add(spin_broadsize);
				if (rect instanceof ImageUILayout) {
					spin_broadsize.setValue(((ImageUILayout)rect).clip_border);
				} 
				
				sy += 20;
				
				{
					lbl_styles.setBounds (sx, sy+=20, 200, 20);
					pan2.add(lbl_styles);
					
					group_styles = Util.createButtonsFromEnum(
							UILayout.ImageStyle.class, JRadioButton.class);
					Enumeration<AbstractButton> btns = group_styles.getElements();
					int i=0;
					while(btns.hasMoreElements()){
						JRadioButton style = (JRadioButton)btns.nextElement();
						style.setBounds(sx, sy+=20, 240, 20);
						style.setActionCommand(style.getText());
						style.addActionListener(new StyleChanged());
						if (style.getText().equals(image_style.toString())) {
							style.setSelected(true);
						}
						pan2.add(style);
						i++;
					}
					
					lbl_border_color.setBounds(sx, sy+=20, 200, 20);
					btn_border_color.setBounds(sx + 200, sy, 200, 20);
					lbl_back_color.setBounds(sx, sy+=20, 200, 20);
					btn_back_color.setBounds(sx + 200, sy, 200, 20);
					
					pan2.add(lbl_border_color);
					pan2.add(btn_border_color);
					pan2.add(lbl_back_color);
					pan2.add(btn_back_color);
					btn_border_color.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e) {
							changeColor(lbl_border_color);
						}
					});
					btn_back_color.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e) {
							changeColor(lbl_back_color);
						}
					});
				}
				
				sy +=20;
				sy+=20;
				
				chk_preferredSize.setBounds(sx, sy, 200, 20);
				pan2.add(chk_preferredSize);

				sy+=24;
				
				chk_repeatFill.setBounds(sx, sy, 200, 20);
				pan2.add(chk_repeatFill);
				
				sy+=20;
				sy+=20;
				
				ok.setBounds	(sx    , sy, 100, 24);
				ok.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						MakeLayoutForm.this.setVisible(false);
						MakeLayoutForm.this.onOkClose();
					}
				});
				cancel.setBounds(sx+110, sy, 100, 24);
				cancel.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						MakeLayoutForm.this.setVisible(false);
					}
				});
				pan2.add(ok);
				pan2.add(cancel);
				
			}

			tools.add(tool_changesrc);
			tools.add(tool_changeatlas);
			tools.add(tool_changesprite);
			tools.add(tool_cleansrc);
			tool_changesrc.addActionListener(this);
			tool_changeatlas.addActionListener(this);
			tool_changesprite.addActionListener(this);
			tool_cleansrc.addActionListener(this);
			
			tools.setFloatable(false);
			
			this.add(tools, BorderLayout.NORTH);
			this.add(split, BorderLayout.CENTER);

			chk_repeatFill.addActionListener(this);
		}
		
		class StyleChanged implements ActionListener
		{
			public void actionPerformed(ActionEvent e) {
				System.out.println(e.getActionCommand());
				image_style = UILayout.ImageStyle.getStyle(e.getActionCommand());
				src_canvas.repaint();
				dstview.repaint();
			}
		}
		
		class SpinChanged implements ChangeListener
		{
			public void stateChanged(ChangeEvent e) {
				try
				{
					int size = Integer.parseInt(spin_broadsize.getValue().toString());
					
					if (size<1)
					{
						spin_broadsize.setValue(1);
					}
					if (image != null) {
						if ((image_style == UILayout.ImageStyle.IMAGE_STYLE_H_012||
							image_style == UILayout.ImageStyle.IMAGE_STYLE_ALL_8 || 
							image_style == UILayout.ImageStyle.IMAGE_STYLE_ALL_9)) {
							if (size > image.getWidth(null) / 2 - 1) {
								size = image.getWidth(null) / 2 - 1;
								spin_broadsize.setValue(size);
							}
						} else if ((image_style == UILayout.ImageStyle.IMAGE_STYLE_V_036|| 
								image_style == UILayout.ImageStyle.IMAGE_STYLE_ALL_8 ||
								image_style == UILayout.ImageStyle.IMAGE_STYLE_ALL_9)) {
							if (size > image.getHeight(null) / 2 - 1) {
								size = image.getHeight(null) / 2 - 1;
								spin_broadsize.setValue(size);
							}
						}
					}
					src_canvas.repaint();
					dstview.repaint();
					
					
				}
				catch (Exception err) {
					err.printStackTrace();
				}
				
			}
			
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == tool_changesrc) {
				changeSrcImage();
			}
			else if (e.getSource() == tool_changeatlas) {
				changeAtlasImage();
			}
			else if (e.getSource() == tool_changesprite) {
				changeSprite();
			}
			else if (e.getSource() == tool_cleansrc) {
				cleanSrcImage();
			}
			else if (e.getSource() == chk_repeatFill) {
				src_canvas.repaint();
				dstview.repaint();
			}
		}
		
		public void changeStyle(ImageStyle c_style)
		{
			Enumeration<AbstractButton> btns = group_styles.getElements();
			while(btns.hasMoreElements()){
				JRadioButton style = (JRadioButton)btns.nextElement();
				if (style.getText().equals(c_style.toString())) {
					style.setSelected(true);
				} else {
					style.setSelected(false);
				}
			}
			image_style = c_style;
		}

		public void changeColor(final JLabel lbl)
		{
			final JColorChooser colorChooser = new JColorChooser();
			JDialog dialog = JColorChooser.createDialog(
					this, 
					"Pick a Color",
					true, // modal
					colorChooser, 
					new ActionListener(){
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							lbl.setForeground(colorChooser.getColor());
							dstview.repaint();
						}
					}, // OK button handler
					null); // no CANCEL button handler
			dialog.setVisible(true);
		}
		
		public void cleanSrcImage() 
		{
			image 			= null;
			sprite			= null;
			image_file 		= null;
			cpj_file 		= null;
			cpj_imageName 	= null;
			cpj_spriteName 	= null;
			cpj_tileID 		= 0;
			imageRegion 	= null;
			
			src_canvas.repaint();
			dstview.repaint();
		}
		
		public void changeSrcImage()
		{
			java.awt.FileDialog fd = new FileDialog(this);
			fd.setLocation(getLocation());
			if (image_file != null) {
				fd.setDirectory(image_file.getPath());
			}
			else if (last_uilayout_root != null) {
				fd.setDirectory(last_uilayout_root.getPath());
			}
			else if (uilayout_root != null) {
				fd.setDirectory(uilayout_root.getPath());
			}
			fd.setTitle("选择原图片");
			fd.setMode(FileDialog.LOAD);
			fd.setVisible(true);
			try {
				String file = fd.getDirectory() + fd.getFile();
				File dfile = new File(file).getCanonicalFile();
				if (fd.getFile() != null && !fd.getFile().isEmpty()) {
					if (uilayout_checker != null) {
						if (!uilayout_checker.checkFile(PopupCellEditUILayout.this, dfile)) {
							return;
						}
					}
					System.out.println("You chose to open this file: " + file);
					last_uilayout_root = dfile;
					image_file = dfile;
					FileInputStream fis = new FileInputStream(dfile);
					image = AwtEngine.unwrap(Engine.getEngine().createImage(fis));
					src_canvas.setSize(
							image.getWidth(null), 
							image.getHeight(null));
					cpj_file 		= null;
					cpj_imageName 	= null;
					cpj_tileID 		= 0;
					cpj_res			= null;
					imageRegion 	= null;
					src_canvas.repaint();
					dstview.repaint();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void changeAtlasImage() 
		{
			File root = last_cpj_root;
			if (cpj_file != null) {
				root = cpj_file.getParentFile();
			}
			CellTileIndex idx = CellTilesChooser.showDialog(this, root);
			if (idx != null) {
				if (uilayout_checker != null) {
					if (!uilayout_checker.checkFile(
							PopupCellEditUILayout.this, 
							idx.imageFile)) {
						return;
					}
				}
				image 			= idx.image;
				image_file 		= null;
				cpj_file		= idx.res.xmlfile;
				cpj_imageName	= idx.imagesName;
				cpj_tileID		= idx.tileID;
				cpj_res			= idx.res;
				imageRegion		= AwtEngine.wrap(idx.tileRect);

				last_cpj_root	= cpj_file.getParentFile();
				
				src_canvas.setSize(
						idx.tileRect.width, 
						idx.tileRect.height);
				src_canvas.repaint();
				dstview.repaint();
			}
		}
		
		public void changeSprite()
		{
			File root = last_cpj_root;
			if (cpj_file != null) {
				root = cpj_file.getParentFile();
			}
			CellSpriteIndex idx = CellSpriteChooser.showDialog(this, root);
			if (idx != null) {
				if (uilayout_checker != null) {
					if (!uilayout_checker.checkFile(
							PopupCellEditUILayout.this, 
							idx.xml_file)) {
						return;
					}
				}
				
				image 			= null;
				image_file 		= null;
				sprite			= idx.sprite;
				cpj_file		= idx.res.xmlfile;
				cpj_imageName	= idx.imagesName;
				cpj_spriteName	= idx.spriteName;
				cpj_spriteAnim	= idx.anim;
				cpj_res			= idx.res;
				imageRegion		= null;
				last_cpj_root	= cpj_file.getParentFile();
				
				changeStyle(ImageStyle.SPRITE);
				
				src_canvas.setSize(
						(int)idx.sprite.getVisibleWidth(), 
						(int)idx.sprite.getVisibleHeight());
				src_canvas.repaint();
				dstview.repaint();
			}
		}
		
		public void onOkClose()
		{
			current_value = getUILayout();
			try {
				current_field.set(object, current_value);
				if (chk_preferredSize.isSelected()) {
					Rectangle psize = current_value.getPreferredSize();
					if (psize != null) {
						object.setSize(new com.g2d.geom.Dimension(
								psize.width, 
								psize.height));
					}
				}
				current_value.setRepeatFill(chk_repeatFill.isSelected());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		public UILayout getUILayout() 
		{
			try
			{
				if (image != null)
				{
					int bordersize = Integer.parseInt(spin_broadsize.getValue().toString());
					ImageUILayout image_layout = null;
					if (uilayout_checker != null) {
						image_layout = uilayout_checker.createUILayout(
								PopupCellEditUILayout.this,
								AwtEngine.wrap_awt(image), 
								null,
								image_file,
								cpj_file,
								cpj_imageName,
								null,
								cpj_tileID,
								cpj_res,
								imageRegion,
								image_style, 
								bordersize);
					}
					if (image_layout == null) {
						image_layout = new ImageUILayout(
								AwtEngine.wrap_awt(image), 
								null,
								image_file,
								cpj_file,
								cpj_imageName,
								null,
								cpj_tileID,
								imageRegion,
								image_style, 
								bordersize);
					}
					image_layout.setRepeatFill(chk_repeatFill.isSelected());
					return image_layout;
				}
				else if (sprite != null) 
				{
					ImageUILayout image_layout = null;
					if (uilayout_checker != null) {
						image_layout = uilayout_checker.createUILayout(
								PopupCellEditUILayout.this,
								null, 
								sprite,
								null,
								cpj_file,
								cpj_imageName,
								cpj_spriteName,
								cpj_spriteAnim,
								cpj_res,
								null,
								ImageStyle.SPRITE, 
								0);
					}
					if (image_layout == null) {
						image_layout = new ImageUILayout(
								null, 
								sprite,
								null,
								cpj_file,
								cpj_imageName,
								cpj_spriteName,
								cpj_spriteAnim,
								null,
								ImageStyle.SPRITE, 
								0);
					}
					return image_layout;
				}
				else 
				{
					UILayout rect = new UILayout(image_style);
					rect.setBackColor(
							AwtEngine.wrap(lbl_back_color.getForeground()));
					rect.setBorderColor(
							AwtEngine.wrap(lbl_border_color.getForeground()));
					return rect;
				}
			} catch (Exception e) {
				spin_broadsize.setValue(1);
				e.printStackTrace();
			}
			return UILayout.createBlankRect();
		}
		
		class SrcCanvas extends JPanel
		{
			private static final long serialVersionUID = 1L;
			
			public void update(Graphics g) {
				paint(g);
			}
			
			public void paint(Graphics g) 
			{
				super.paint(g);
				
				if (sprite != null) 
				{
					CCD bounds = sprite.getVisibleBounds();
					int w = (int)bounds.getWidth();
					int h = (int)bounds.getHeight();
					setMinimumSize(new Dimension(w, h));
					g.clearRect(0, 0, w, h);
					try {
						sprite.render(
								AwtEngine.wrap((java.awt.Graphics2D)g), 
								-sprite.getVisibleLeft(), 
								-sprite.getVisibleTop());
					} catch (Exception e) {
						spin_broadsize.setValue(1);
						e.printStackTrace();
					}
				}
				else if (image != null)
				{
					int w = image.getWidth();
					int h = image.getHeight();
					if (imageRegion != null) {
						w = imageRegion.width;
						h = imageRegion.height;
					}
					setMinimumSize(new Dimension(w, h));
					g.clearRect(0, 0, w, h);
					
					try
					{
						int bs = Integer.parseInt(spin_broadsize.getValue().toString());
						if (imageRegion == null) {
							g.drawImage(image, 0, 0, this);
						} else {
							g.drawImage(image, 0, 0, w, h,
									imageRegion.x, imageRegion.y, 
									imageRegion.x + w, imageRegion.y + h, 
									null);
						}
						
						g.setColor(new Color(0xffffffff, true));
						switch(image_style)
						{
						case IMAGE_STYLE_ALL_8:
						case IMAGE_STYLE_ALL_9:
							g.drawLine(0, bs, w, bs);
							g.drawLine(0, h-bs, w, h-bs);
							g.drawLine(bs, 0, bs, h);
							g.drawLine(w-bs, 0, w-bs, h);
							break;
						case IMAGE_STYLE_H_012:
							g.drawLine(bs, 0, bs, h);
							g.drawLine(w-bs, 0, w-bs, h);
							break;
						case IMAGE_STYLE_V_036:
							g.drawLine(0, bs, w, bs);
							g.drawLine(0, h-bs, w, h-bs);
							break;
						case IMAGE_STYLE_BACK_4:
							break;
						case IMAGE_STYLE_HLM:
							g.drawLine(0, bs, w, bs);
							g.drawLine(0, h-bs, w, h-bs);
							g.drawLine(bs, 0, bs, h);
							break;
						case IMAGE_STYLE_VTM:
							g.drawLine(0, bs, w, bs);
							g.drawLine(bs, 0, bs, h);
							g.drawLine(w-bs, 0, w-bs, h);
							break;
						}
					}
					catch (Exception e) {
						spin_broadsize.setValue(1);
						e.printStackTrace();
					}
				}else{
					g.clearRect(0, 0, getWidth(), getHeight());
				}
			}
			
		};
		
		class DstView extends JInternalFrame 
		{
			private static final long serialVersionUID = 1L;
			
			public DstView() 
			{
				super();
				
				if (image != null) {
					this.setSize(image.getWidth(), image.getHeight());
				}
				else if (sprite != null) {
					this.setSize(
							(int)sprite.getVisibleWidth(), 
							(int)sprite.getVisibleHeight());
				} else {
					this.setSize(200, 200);
				}
				
				this.setVisible(true);
				this.setResizable(true);
			}
			
			public void paint(Graphics dg) 
			{
				super.paint(dg);
				
				Graphics2D g = (Graphics2D)dg;
				
				g.clearRect(0, 0, getWidth(), getHeight());
				try{
					com.g2d.Graphics2D g2d = AwtEngine.wrap((Graphics2D)dg);
					getUILayout().render(g2d, 0, 0, getWidth(), getHeight());
				}catch (Exception e) {
					//e.printStackTrace();
				}
			}
			
		}
		

	}
	
	public static interface UILayoutEditAdapter
	{
		public boolean checkFile(
				PopupCellEditUILayout edit, 
				File file);
		
		public ImageUILayout createUILayout(
				PopupCellEditUILayout edit, 
				com.g2d.BufferedImage image,
				CSprite sprite,
				File image_file, 
				File cpj_file, 
				String cpj_imagesName, 
				String cpj_spriteName, 
				int cpj_tileID,
				CellTilesResource cpj_res,
				com.g2d.geom.Rectangle region,
				ImageStyle style,
				int clip_border) ;
			
	}
	
	
}
