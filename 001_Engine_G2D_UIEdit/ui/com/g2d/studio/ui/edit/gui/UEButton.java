package com.g2d.studio.ui.edit.gui;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.g2d.BufferedImage;
import com.g2d.Color;
import com.g2d.Graphics2D;
import com.g2d.Image;
import com.g2d.annotation.Property;
import com.g2d.annotation.TextProperty;
import com.g2d.awt.util.Tools;
import com.g2d.display.ui.Button;
import com.g2d.studio.res.Res;
import com.g2d.studio.ui.edit.UIEdit;
import com.g2d.studio.ui.edit.UIEditConfig;
import com.g2d.studio.ui.edit.UIEditFileNode;
import com.g2d.studio.ui.edit.UIFontStyleEditor;
import com.g2d.studio.ui.edit.UIFontStyleEditor.UIEditFontStyle;
import com.g2d.studio.ui.edit.UIImageFont;
import com.g2d.studio.ui.edit.UILayoutManager.UIEditImageAtlas;
import com.g2d.studio.ui.edit.UIPerforming;
import com.g2d.studio.ui.edit.UITreeNode;
import com.g2d.util.Drawing.TextAnchor;

public class UEButton extends Button implements SavedComponent
{
	public static BufferedImage mouse_catched_image = Tools.wrap_awt(Res.img_light64);

	@Property("按下图片文本")
	public String imageTextDown;

	@Property("图片文本")
	public String imageTextUp;

	public Image imageDownText;
	public Image imageUpText;
	
	@Property("按下图集")
	public UIEditImageAtlas imageAtlasDown;
	@Property("图片集")
	public UIEditImageAtlas imageAtlasUp;

	@Property("文字样式")
	public UIEditFontStyle FontStyle;
	
	@Property("图片字库")
	public UIImageFont ImageFont;
	
	public UEButton() {
		this.mouse_catched_mask = mouse_catched_image;
		this.enable_antialiasing = true;
		this.unfocusTextColor = new Color(UIEditConfig.UI_DEFAULT_TEXT_COLOR);
		this.focusTextColor = new Color(UIEditConfig.UI_DEFAULT_TEXT_COLOR_FOCUS);
	}
	
	@Override
	protected int renderLayout(Graphics2D g, boolean isDown) {
		if (imageAtlasDown != null) {
			imageDown = imageAtlasDown.update();
		} else {
			imageDown = imageDownText;
		}
		if (imageAtlasUp != null) {
			imageUp = imageAtlasUp.update();
		} else {
			imageUp = imageUpText;
		}
		return super.renderLayout(g, isDown);
	}

	@Override
	protected int renderText(Graphics2D g, boolean isDown)
	{
		if (ImageFont != null)
		{
			ImageFont.render(g, text
					, text_offset_x,
					text_offset_y, 
					getWidth(), 
					getHeight(),
					text_anchor);
			return 1;
		}
		else if (FontStyle != null)
		{
			if (FontStyle.getBuffer(text, getTextSize()) != null)
			{
				FontStyle.render(g, 
						text_offset_x, 
						text_offset_y, 
						getWidth(),
						getHeight(), 
						text_anchor);
			}
			return 1;
		}
		else 
		{
			return super.renderText(g, isDown);
		}
	}
	
	@Override
	public void onRead(UIEditFileNode edit, Element e, Document doc) throws Exception 
	{
		if (imageTextDown != null && !imageTextDown.isEmpty()) {
			imageDownText = UIEdit.getLayoutManager().getImageBuffer(imageTextDown);
		}
		if (imageTextUp != null && !imageTextUp.isEmpty()) {
			imageUpText = UIEdit.getLayoutManager().getImageBuffer(imageTextUp);
		}
		custom_layout_down = UITreeNode.readLayout(edit, 
				UITreeNode.findXmlChild(e, "layout_down"));
		
		if (e.hasAttribute("FontStyle")) {
			this.FontStyle = UIFontStyleEditor.readXML(
					e.getAttribute("FontStyle"), this.text, this.textSize);
		}
	}

	@Override
	public void onWrite(UIEditFileNode edit, Element e, Document doc) throws Exception
	{
		if (custom_layout_down != null) {
			Element rect = doc.createElement("layout_down");
			UITreeNode.writeLayout(edit, custom_layout_down, rect);
			e.appendChild(rect);
		}
		if (this.FontStyle != null) {
			e.setAttribute("FontStyle", this.FontStyle.TemplateName);
		}
	}

//	@Override
//	public void readComplete(UIEditFileNode edit) {}
//	@Override
//	public void writeBefore(UIEditFileNode edit) {}
}
