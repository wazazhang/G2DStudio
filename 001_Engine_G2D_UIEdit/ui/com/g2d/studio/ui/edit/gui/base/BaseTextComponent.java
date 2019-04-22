package com.g2d.studio.ui.edit.gui.base;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.g2d.BufferedImage;
import com.g2d.Color;
import com.g2d.Font;
import com.g2d.Graphics2D;
import com.g2d.annotation.Property;
import com.g2d.annotation.TextProperty;
import com.g2d.display.ui.UIComponent;
import com.g2d.studio.ui.edit.UIEditFileNode;
import com.g2d.studio.ui.edit.UIFontStyleEditor;
import com.g2d.studio.ui.edit.UIImageFont;
import com.g2d.studio.ui.edit.UIFontStyleEditor.UIEditFontStyle;
import com.g2d.studio.ui.edit.gui.SavedComponent;
import com.g2d.util.Drawing;
import com.g2d.util.Drawing.TextAnchor;

public class BaseTextComponent extends UIComponent implements SavedComponent
{
	@Property("文字颜色")
	public Color textColor = Color.WHITE;

	@Property("文字边颜色")
	public Color textBorderColor = Color.BLACK;

	@Property("文字边透明度%")
	public float textBorderAlpha = 100.0f;

	@Property("text")
	@TextProperty
	public String text = getClass().getSimpleName();

	@Property("文字类型")
	public Font textFont = null;

	@Property("文字尺寸")
	public Integer textFontSize = null;

	@Property("文字对齐")
	public TextAnchor text_anchor = TextAnchor.C_C;
	@Property("文字偏移")
	public int text_offset_x;
	@Property("文字偏移")
	public int text_offset_y;

	@Property("文字样式")
	public UIEditFontStyle FontStyle;

	@Property("图片字库")
	public UIImageFont ImageFont;
	
	/**文字是否抗锯齿*/
//	@Property("文字是否抗锯齿")
	public boolean	enable_antialiasing	 = true;
	

	protected void renderText(Graphics2D g, String text)
	{
		if (ImageFont != null)
		{
			ImageFont.render(g, text
					, text_offset_x,
					text_offset_y, 
					getWidth(), 
					getHeight(),
					text_anchor);
		}
		else if (FontStyle != null)
		{
			if (FontStyle.getBuffer(text, getFontSize()) != null)
			{
				FontStyle.render(g, 
						text_offset_x, 
						text_offset_y, 
						getWidth(),
						getHeight(), 
						text_anchor);
			}
		}
		else
		{
			Font oldf = g.getFont();
			try {
				if (textFont != null) {
					g.setFont(textFont);
				}
				if (textFontSize != null && textFontSize > 0) {
					g.setFontSize(textFontSize);
				}
				if (enable_antialiasing) {
					g.setFontAntialiasing(true);
				}
				textBorderColor.setAlpha(Math.min(Math.abs(textBorderAlpha)/100f, 1f));
				g.setColor(textColor);
				boolean flag = g.setFontAntialiasing(enable_antialiasing);
				Drawing.drawStringBorder(g, text,
						text_offset_x, 
						text_offset_y,
						getWidth(),
						getHeight(),
						text_anchor, textBorderColor);
				g.setFontAntialiasing(flag);
				increaseFrameRenderTime(1);
			} finally {
				g.setFont(oldf);
			}
		}
	}
	
	public int getFontSize() {
		if (textFont != null) {
			return textFont.getSize();
		}
		if (textFontSize != null && textFontSize > 0) {
			return textFontSize;
		}
		return 0;
	}
	
	@Override
	public void onWrite(UIEditFileNode edit, Element e, Document doc)
			throws Exception {
		if (this.FontStyle != null) {
			e.setAttribute("FontStyle", this.FontStyle.TemplateName);
		}
	}

	@Override
	public void onRead(UIEditFileNode edit, Element e, Document doc)
			throws Exception {
		if (e.hasAttribute("FontStyle")) {
			this.FontStyle = UIFontStyleEditor.readXML(
					e.getAttribute("FontStyle"), this.text, this.getFontSize());
		}
	}
}
