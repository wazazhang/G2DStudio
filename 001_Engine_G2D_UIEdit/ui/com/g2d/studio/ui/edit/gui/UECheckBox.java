package com.g2d.studio.ui.edit.gui;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.g2d.Color;
import com.g2d.Font;
import com.g2d.Graphics2D;
import com.g2d.Image;
import com.g2d.annotation.Property;
import com.g2d.annotation.TextProperty;
import com.g2d.display.event.MouseEvent;
import com.g2d.display.ui.UIComponent;
import com.g2d.display.ui.layout.UILayout;
import com.g2d.studio.ui.edit.UIEdit;
import com.g2d.studio.ui.edit.UIEditConfig;
import com.g2d.studio.ui.edit.UIEditFileNode;
import com.g2d.studio.ui.edit.UITreeNode;
import com.g2d.studio.ui.edit.UILayoutManager.UIEditImageAtlas;
import com.g2d.studio.ui.edit.gui.UEGauge.RenderOrientation;
import com.g2d.studio.ui.edit.gui.base.BaseTextComponent;
import com.g2d.util.Drawing;
import com.g2d.util.Drawing.ImageAnchor;
import com.g2d.util.Drawing.TextAnchor;

public class UECheckBox extends BaseTextComponent implements SavedComponent
{
	@Property("是否选中")
	public boolean checked = true;
	
	@Property("上层图片对其方式")
	public ImageAnchor imageAnchor = ImageAnchor.L_C;
	@Property("上层图片偏移X")
	public int imageOffsetX = 0;
	@Property("上层图片偏移Y")
	public int imageOffsetY = 0;
	
	@Property("选中图片")
	public String imagePathChecked;
	@Property("非选中图片")
	public String imagePathUnchecked;
	@Property("选中图集")
	public UIEditImageAtlas imageAtlasChecked;
	@Property("非选中图集")
	public UIEditImageAtlas imageAtlasUnchecked;

	
	public Image imageChecked;
	public Image imageUnchecked;
	protected Image renderImageC;
	protected Image renderImageU;
	
	public UECheckBox() 
	{
		this.text = getClass().getSimpleName();
		this.imageOffsetX = 6;
		this.textColor = new Color(UIEditConfig.UI_DEFAULT_TEXT_COLOR);
	}

	@Override
	protected void onMouseDown(MouseEvent event)
	{
		super.onMouseDown(event);
		this.checked = (!checked);
	}
	
	public void render(Graphics2D g) 
	{
		super.render(g);
		
		if (imageAtlasChecked != null) {
			renderImageC = imageAtlasChecked.update();
		} else {
			renderImageC = imageChecked;
		}
		if (imageAtlasUnchecked != null) {
			renderImageU = imageAtlasUnchecked.update();
		} else {
			renderImageU = imageUnchecked;
		}
		Image img_check = checked ? renderImageC : renderImageU;
		Drawing.drawImageAnchor(g,
				img_check, 
				imageOffsetX, 
				imageOffsetY, 
				getWidth(),
				getHeight(),
				imageAnchor);
		
		super.renderText(g, text);
	}
	
	
	
	@Override
	public void onRead(UIEditFileNode edit, Element e, Document doc) throws Exception 
	{
		super.onRead(edit, e, doc);
		if (imagePathChecked != null && !imagePathChecked.isEmpty()) {
			imageChecked = UIEdit.getLayoutManager().getImageBuffer(imagePathChecked);
		}
		if (imagePathUnchecked != null && !imagePathUnchecked.isEmpty()) {
			imageUnchecked = UIEdit.getLayoutManager().getImageBuffer(imagePathUnchecked);
		}
	}

	@Override
	public void onWrite(UIEditFileNode edit, Element e, Document doc) throws Exception
	{
		super.onWrite(edit, e, doc);
	}

	

	
	
}
