package com.g2d.studio.ui.edit.gui;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.g2d.Color;
import com.g2d.Font;
import com.g2d.Graphics2D;
import com.g2d.annotation.Property;
import com.g2d.display.ui.TextBoxSingle;
import com.g2d.font.TextAttribute;
import com.g2d.studio.ui.edit.UIEditConfig;
import com.g2d.studio.ui.edit.UIEditFileNode;

public class UETextInput extends TextBoxSingle implements SavedComponent {
	@Property("文字类型")
	public Font textFont = null;

	@Property("文字尺寸")
	public Integer textFontSize = null;

	public UETextInput() {
		super("");
		this.enable_antialiasing = true;
		this.textColor = new Color(UIEditConfig.UI_DEFAULT_TEXT_COLOR);
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void render(Graphics2D g) {
		if (Text.length() > 0) {
			if (textFont != null) {
				mtext.putAttribute(TextAttribute.FONT, textFont, 0, Text.length());
			} else {
				mtext.putAttribute(TextAttribute.FONT, g.getFont(), 0, Text.length());
			}
			if (textFontSize != null && textFontSize > 0) {
				mtext.putAttribute(TextAttribute.SIZE, textFontSize, 0, Text.length());
			} else {
				mtext.putAttribute(TextAttribute.SIZE, g.getFontSize(), 0, Text.length());
			}
		}
		Font oldf = g.getFont();
		try {
			if (textFont != null) {
				g.setFont(textFont);
			}
			super.render(g);
		} finally {
			g.setFont(oldf);
		}
	}

	@Override
	public void onRead(UIEditFileNode edit, Element e, Document doc) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void onWrite(UIEditFileNode edit, Element e, Document doc) throws Exception {

	}

}
