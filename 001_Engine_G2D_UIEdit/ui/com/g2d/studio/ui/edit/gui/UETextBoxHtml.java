package com.g2d.studio.ui.edit.gui;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.g2d.Color;
import com.g2d.annotation.Property;
import com.g2d.annotation.TextProperty;
import com.g2d.display.ui.TextPan;
import com.g2d.studio.ui.edit.UIEditConfig;
import com.g2d.studio.ui.edit.UIEditFileNode;

public class UETextBoxHtml extends TextPan implements SavedComponent {
	@Property("高级文本")
	@TextProperty
	public String HtmlText;

	public UETextBoxHtml() {
		this.enable_antialiasing = true;
		this.textColor = new Color(UIEditConfig.UI_DEFAULT_TEXT_COLOR);
	}

	@Override
	public void onRead(UIEditFileNode edit, Element e, Document doc) throws Exception {
	}

	@Override
	public void onWrite(UIEditFileNode edit, Element e, Document doc) throws Exception {
	}

	@Override
	public void update() {
		super.setText(this.HtmlText);
		super.update();
	}
}
