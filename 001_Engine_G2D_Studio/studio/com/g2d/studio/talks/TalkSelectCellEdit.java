package com.g2d.studio.talks;

import java.awt.Component;

import javax.swing.JLabel;

import com.g2d.editor.property.ObjectPropertyEdit;
import com.g2d.editor.property.PropertyCellEdit;
import com.g2d.studio.Studio;

public class TalkSelectCellEdit extends TalkSelectDialog implements PropertyCellEdit<String>
{
	private static final long serialVersionUID = 1L;
	
	JLabel	edit_label 	= new JLabel();
	
	public TalkSelectCellEdit(Component owner, TalkFile dv) {
		super(owner, dv);
	}
	
	public TalkSelectCellEdit(Component owner, String dv) {
		super(owner, Studio.getInstance().getTalkManager().getTalk(dv));
	}
	
	
	@Override
	public Component getComponent(ObjectPropertyEdit panel) {
		TalkFile talk = getSelectedObject();
		if (talk!=null) {
			edit_label.setText(talk.getName());	
			edit_label.setIcon(talk.getListIcon(false));
		} else {
			edit_label.setText("");	
			edit_label.setIcon(null);
		}
		return edit_label;
	}

	@Override
	public String getValue() {
		TalkFile talk = getSelectedObject();
		if (talk!=null) {
			return talk.getName();
		}
		return null;
	}
	

	@Override
	public void commitValue() {}
}
