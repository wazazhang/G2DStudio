package com.g2d.studio.item.property;

import java.awt.Component;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import com.cell.rpg.item.anno.ItemType;
import com.g2d.editor.property.ObjectPropertyEdit;
import com.g2d.editor.property.PropertyCellEdit;
import com.g2d.studio.swing.G2DListItemData;

@SuppressWarnings("serial")
public class ItemPropertySavedTypeSelectComboBox extends JComboBox implements PropertyCellEdit<Integer>
{
	JLabel cell_edit_component = new JLabel();
	
	public ItemPropertySavedTypeSelectComboBox(Integer dv) {
		Vector<G2DListItemData<ItemType>> datas = ItemPropertySavedTypeSelectDialog.createG2DListData();
		G2DListItemData<ItemType> dnode = null;
		for (G2DListItemData<ItemType> node : datas) {
			super.addItem(node);
			if (dv != null) {
				if (node.getData().value() == dv) {
					dnode = node;
				}
			}
		}
		if (dnode != null) {
			super.setSelectedItem(dnode);
		}
	}
	
	@Override
	public Component getComponent(ObjectPropertyEdit panel) {
		G2DListItemData<ItemType> node = (G2DListItemData<ItemType>)getSelectedItem();
		if (node != null) {
			cell_edit_component.setText(node.getListName());
		} else {
			cell_edit_component.setText("");
		}
		return cell_edit_component;
	}
	
	@Override
	public Integer getValue() {
		G2DListItemData<ItemType> node = (G2DListItemData<ItemType>)getSelectedItem();
		if (node != null) {
			return node.getData().value();
		}
		return null;
	}
	@Override
	public void commitValue() {}
	
}
