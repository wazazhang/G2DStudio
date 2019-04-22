package com.g2d.studio.ui.edit.gui;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.cell.reflect.Parser;
import com.g2d.Color;
import com.g2d.action.Action;
import com.g2d.annotation.Property;
import com.g2d.display.ui.Container;
import com.g2d.display.ui.layout.UILayout;
import com.g2d.display.ui.layout.UILayout.ImageStyle;
import com.g2d.studio.ui.edit.UIEdit;
import com.g2d.studio.ui.edit.UIEditFileNode;
import com.g2d.studio.ui.edit.UILayoutManager.UIEditImageAtlas;
import com.g2d.studio.ui.edit.UILayoutManager.UIEditLayout;
import com.g2d.studio.ui.edit.UILayoutManager.UIEditSprite;
import com.g2d.studio.ui.edit.transition.*;

public class UERoot extends Container implements SavedComponent
{
	@Property("淡入效果")
	public UIFadeAction  fadeIn;
	@Property("淡出效果")
	public UIFadeAction fadeOut;

	private boolean opened = true;
	
	public UERoot() 
	{		
//		fadeIn  = new AlphaFadeIn(0.0f, 10);
//		fadeOut = new AlphaFadeOut(0.0f, 10);
//		fadeIn  = new TranslateFadeIn(0.0f, -800, 10);
//		fadeOut = new TranslateFadeOut(0.0f, -800, 10);
//		fadeIn  = new ScaleFadeIn(0.0f, 10);
		this.setSize(640, 480);
	}
	
	public void open() {
		if (runningActions() == 0) {
			if (fadeIn != null) {
				super.runAction(fadeIn);
			}
			this.opened = true;
			this.visible = true;
		}
	}

	public void close() {
		if (runningActions() == 0) {
			if (fadeOut != null) {
				super.runAction(fadeOut);
			}
			this.opened = false;
		}
	}
	
	@Override
	protected void onActionStoped(Action act) {
		if (fadeOut == act) {
			this.visible = false;
		} else {
			this.visible = true;
		}
	}
	
	public void switchOpen() {
		if (opened) {
			close();
		} else {
			open();
		}
	}
	
	@Override
	public void onRead(UIEditFileNode edit, Element e, Document doc) throws Exception {}
	@Override
	public void onWrite(UIEditFileNode edit, Element e, Document doc) throws Exception{}

}
