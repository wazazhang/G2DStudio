package com.g2d.studio.ui.edit.gui;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.g2d.studio.ui.edit.UIEditFileNode;

public interface SavedComponent {
	
	String getName();

	void onWrite(UIEditFileNode edit, Element e, Document doc) throws Exception;
	void onRead(UIEditFileNode edit, Element e, Document doc) throws Exception;
	
//	void readComplete(UIEditFileNode edit);
//	void writeBefore(UIEditFileNode edit);
}
