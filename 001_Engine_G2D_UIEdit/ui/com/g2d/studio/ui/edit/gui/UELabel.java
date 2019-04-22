package com.g2d.studio.ui.edit.gui;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.g2d.Color;
import com.g2d.Graphics2D;
import com.g2d.display.ui.Label;
import com.g2d.studio.ui.edit.UIEdit;
import com.g2d.studio.ui.edit.UIEditConfig;
import com.g2d.studio.ui.edit.UIEditFileNode;
import com.g2d.studio.ui.edit.gui.base.BaseTextComponent;

public class UELabel extends BaseTextComponent implements SavedComponent {
	
	public UELabel() {
		this.textColor = new Color(UIEditConfig.UI_DEFAULT_TEXT_COLOR);
	}

	@Override
	public void render(Graphics2D g) {
		super.render(g);
		super.renderText(g, text);
	}

	@Override
	public void onRead(UIEditFileNode edit, Element e, Document doc) throws Exception {
		super.onRead(edit, e, doc);
	}

	@Override
	public void onWrite(UIEditFileNode edit, Element e, Document doc) throws Exception {
		super.onWrite(edit, e, doc);
	}

}