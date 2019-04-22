package com.g2d.studio.ui.edit.transition;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.g2d.action.Action;
import com.g2d.annotation.Property;
import com.g2d.studio.ui.edit.UIEditFileNode;

public abstract class UIFadeAction extends Action
{
	public UIFadeAction(int time)
	{
		super(time);
	}

	@Override
	public String toString() {
		Property prop = getClass().getAnnotation(Property.class);
		if (prop != null && prop.value().length > 0) {
			return prop.value()[0];
		}
		return super.toString();
	}
	
	public void onRead(UIEditFileNode edit, Element e, Document doc)
			throws Exception {
	}

	public void onWrite(UIEditFileNode edit, Element e, Document doc)
			throws Exception {
	}
}
