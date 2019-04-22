package com.g2d.studio.ui.edit;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.lang.reflect.Field;

import javax.swing.JPanel;
import javax.swing.table.DefaultTableCellRenderer;

import com.cell.reflect.Parser;
import com.g2d.BufferedImage;
import com.g2d.display.ui.UIComponent;
import com.g2d.editor.property.CellEditAdapter;
import com.g2d.editor.property.ObjectPropertyEdit;
import com.g2d.editor.property.PopupCellEditText;
import com.g2d.editor.property.PropertyCellEdit;
import com.g2d.editor.property.TextCellEdit;
import com.g2d.editor.property.TextCellObjectEdit;
import com.g2d.studio.ui.edit.UIFontStyleEditor.UIEditFontStyle;
import com.g2d.studio.ui.edit.UILayoutManager.UIActionEditor;
import com.g2d.studio.ui.edit.UILayoutManager.UIEditImage;
import com.g2d.studio.ui.edit.UILayoutManager.UIEditImageAtlas;
import com.g2d.studio.ui.edit.UILayoutManager.UIEffectSelecter;
import com.g2d.studio.ui.edit.gui.UEButton;
import com.g2d.studio.ui.edit.gui.UECheckBox;
import com.g2d.studio.ui.edit.gui.UEImageBox;
import com.g2d.studio.ui.edit.gui.UERoot;
import com.g2d.studio.ui.edit.gui.UETextBox;
import com.g2d.studio.ui.edit.gui.UETextBoxHtml;
import com.g2d.studio.ui.edit.gui.base.BaseTextComponent;
import com.g2d.studio.ui.edit.transition.UIFadeAction;

public class UIPropertyPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	public UIPropertyPanel(int mw, int mh) {
		super(new CardLayout());
		this.setMinimumSize(new Dimension(mw, mh));
	}
	
	public void setCompoment(UITreeNode node) {
		this.removeAll();
		if (node != null) {
			node.getProperty().setVisible(false);
			this.add(node.getProperty(), BorderLayout.CENTER);
			node.getProperty().refresh();
			node.getProperty().setVisible(true);
		}
	}

	public static CellEditAdapter<?>[] getAdapters(UIEditFileNode edit) {
		return new CellEditAdapter<?>[]{
				new UIPropertyPanel.UEComponentAttributeAdapter(edit),
				new UIPropertyPanel.UEImageBoxAdapter(edit),
				new UIPropertyPanel.UEButtonAdapter(edit),
				new UIPropertyPanel.UECheckBoxAdapter(edit),
				new UIPropertyPanel.UETextBoxAdapter(edit),
				new UIPropertyPanel.UETextBoxHtmlAdapter(edit),
				new UIPropertyPanel.UEEffectAdapter(edit),
				new UIPropertyPanel.UERootTransitionAdapter(edit),
				new UIPropertyPanel.UIFontStyleAdapter(edit),
		};
	};
	
	abstract public static class UEAdapter<T> implements CellEditAdapter<T>
	{
		final protected UIEditFileNode edit;
		
		public UEAdapter(UIEditFileNode edit) {
			this.edit = edit;
		}
		
		@Override
		public boolean fieldChanged(Object edit_object, Object field_value,
				Field field) {
			return false;
		}

		@Override
		public String getCellText(Object edit_object, Field field,
				Object field_src_value) {
			return null;
		}

		@Override
		public Object getCellValue(Object edit_object,
				PropertyCellEdit<?> field_edit, Field field,
				Object field_src_value) {
			return null;
		}
		
		@Override
		public PropertyCellEdit<?> getCellEdit(ObjectPropertyEdit owner, Object editObject, Object fieldValue, Field field) 
		{
			return null;
		}
	
		@Override
		public Component getCellRender(ObjectPropertyEdit owner, Object editObject, Object fieldValue, Field field, DefaultTableCellRenderer src) 
		{
			return null;
		}
	
	}
	
	public static class UEImageBoxAdapter extends UEAdapter<UEImageBox>
	{
		public UEImageBoxAdapter(UIEditFileNode edit) {
			super(edit);
		}
		
		@Override
		public Class<UEImageBox> getType() {
			return UEImageBox.class;
		}

		@Override
		public PropertyCellEdit<?> getCellEdit(ObjectPropertyEdit owner, Object editObject, Object fieldValue, Field field) 
		{
			UEImageBox ib = (UEImageBox)editObject;
			
			if (field.getName().equals("imagePath")) 
			{
				UIEditImage bf = UILayoutManager.dialogLoadImage(ib.imagePath);
				if (bf != null) {
					ib.image = bf.getDisplayImage();
					ib.imagePath = bf.getName();
					return new TextCellEdit(bf.getName());
				}
			}
			else if (field.getName().equals("imageAtlas")) 
			{
				String path = null;
				if (ib.imageAtlas != null) {
					path = ib.imageAtlas.cpj_fileName;
				}
				UIEditImage bf = UILayoutManager.dialogLoadImageAtlas(path, ib.imageAtlas);
				if (bf != null) {
					((UEImageBox)editObject).image = bf.getDisplayImage();
					((UEImageBox)editObject).imageAtlas = bf.getAtlas();
					return new TextCellObjectEdit<UIEditImageAtlas>(
							((UEImageBox)editObject).imageAtlas);
				}
			}
			return super.getCellEdit(owner, editObject, fieldValue, field);
		}
	}
	
	public static class UEComponentAttributeAdapter extends UEAdapter<UIComponent>
	{
		public UEComponentAttributeAdapter(UIEditFileNode edit) {
			super(edit);
		}

		@Override
		public Class<UIComponent> getType() 
		{
			return UIComponent.class;
		}

		@Override
		public PropertyCellEdit<?> getCellEdit(ObjectPropertyEdit owner, Object editObject, Object fieldValue, Field field) 
		{
			if (field.getName().equals("Attributes")) 
			{
				PopupCellEditText tedDialog = new PopupCellEditText(fieldValue+"");
				return tedDialog;
			}
			return super.getCellEdit(owner, editObject, fieldValue, field);
		}
	}
	
	public static class UEButtonAdapter extends UEAdapter<UEButton>
	{
		public UEButtonAdapter(UIEditFileNode edit) {
			super(edit);
		}
		
		@Override
		public Class<UEButton> getType() 
		{
			return UEButton.class;
		}
		@Override
		public boolean fieldChanged(Object edit_object, Object field_value, Field field) {

			UEButton btn = (UEButton)edit_object;
			
			if (field.getName().equals("imageTextDown") || 
				field.getName().equals("imageTextUp")) 
			{
				BufferedImage bf = UIEdit.getLayoutManager().getImageBuffer(field_value + "");
				if (bf != null) {
					if (field.getName().equals("imageTextDown")) {
						btn.imageDownText = bf;
					}
					if (field.getName().equals("imageTextUp")) {
						btn.imageUpText = bf;
					}
				}
			}
			return super.fieldChanged(edit_object, field_value, field);
		}
		@Override
		public PropertyCellEdit<?> getCellEdit(ObjectPropertyEdit owner, Object editObject, Object fieldValue, Field field) 
		{
			UEButton btn = (UEButton)editObject;
			
			if (field.getName().equals("imageTextDown") || 
				field.getName().equals("imageTextUp")) 
			{
				String path = null;
				if (field.getName().equals("imageTextDown")) {
					path = btn.imageTextDown;
				}
				else if (field.getName().equals("imageTextUp")) {
					path = btn.imageTextUp;
				}
				UIEditImage bf = UILayoutManager.dialogLoadImage(path);
				if (bf != null) {
					
					if (field.getName().equals("imageTextDown")) {
						btn.imageDownText = bf.getDisplayImage();
						btn.imageTextDown = bf.getName();
					}
					if (field.getName().equals("imageTextUp")) {
						btn.imageUpText = bf.getDisplayImage();
						btn.imageTextUp = bf.getName();
					}
					return new TextCellEdit(bf.getName());
				}
			}
			if (field.getName().equals("imageAtlasDown") || 
				field.getName().equals("imageAtlasUp")) 
			{
				UIEditImageAtlas atlas = null;
				String path = null;
				if (field.getName().equals("imageAtlasDown") && btn.imageAtlasDown != null) {
					path = btn.imageAtlasDown.cpj_fileName;
					atlas = btn.imageAtlasDown;
				}
				else if (field.getName().equals("imageAtlasUp") && btn.imageAtlasUp != null) {
					path = btn.imageAtlasUp.cpj_fileName;
					atlas = btn.imageAtlasUp;
				}
				UIEditImage bf = UILayoutManager.dialogLoadImageAtlas(path, atlas);
				if (bf != null) {
					if (field.getName().equals("imageAtlasDown")) {
						btn.imageAtlasDown = bf.getAtlas();
						return new TextCellObjectEdit<UIEditImageAtlas>(btn.imageAtlasDown);
					}
					if (field.getName().equals("imageAtlasUp")) {
						btn.imageAtlasUp = bf.getAtlas();
						return new TextCellObjectEdit<UIEditImageAtlas>(btn.imageAtlasUp);
					}
				}
			}
			return super.getCellEdit(owner, editObject, fieldValue, field);
		}
	
	}

	public static class UETextBoxAdapter extends UEAdapter<UETextBox>
	{
		public UETextBoxAdapter(UIEditFileNode edit) {
			super(edit);
		}
		
		@Override
		public Class<UETextBox> getType() 
		{
			return UETextBox.class;
		}
		
		@Override
		public PropertyCellEdit<?> getCellEdit(ObjectPropertyEdit owner, Object editObject, Object fieldValue, Field field) 
		{
			if (field.getName().equals("Text")) 
			{
				PopupCellEditText tedDialog = new PopupCellEditText(fieldValue+"");
				return tedDialog;
			}
			return super.getCellEdit(owner, editObject, fieldValue, field);
		}
	
	}

	public static class UETextBoxHtmlAdapter extends UEAdapter<UETextBoxHtml>
	{
		public UETextBoxHtmlAdapter(UIEditFileNode edit) {
			super(edit);
		}

		@Override
		public Class<UETextBoxHtml> getType() 
		{
			return UETextBoxHtml.class;
		}

		@Override
		public PropertyCellEdit<?> getCellEdit(ObjectPropertyEdit owner, Object editObject, Object fieldValue, Field field) 
		{
			if (field.getName().equals("HtmlText")) 
			{
				PopupCellEditText tedDialog = new PopupCellEditText(fieldValue+"");
				return tedDialog;
			}
			return super.getCellEdit(owner, editObject, fieldValue, field);
		}
	}
	

	public static class UEEffectAdapter extends UEAdapter<UIComponent>
	{
		public UEEffectAdapter(UIEditFileNode edit) {
			super(edit);
		}

		@Override
		public Class<UIComponent> getType() {
			return UIComponent.class;
		}
		
		@Override
		public PropertyCellEdit<?> getCellEdit(ObjectPropertyEdit owner, Object editObject, Object fieldValue, Field field) 
		{
			if (field.getName().equals("uiEffect")) 
			{
				UIEffectSelecter tedDialog = new UIEffectSelecter(fieldValue);
				return tedDialog;
			}
			return super.getCellEdit(owner, editObject, fieldValue, field);
		}
		
		@Override
		public boolean fieldChanged(
				Object edit_object, 
				Object field_value,
				Field field)
		{
			UIComponent ui = (UIComponent)edit_object;
			if (field_value == null) {
				ui.uiEffect = null;
			}
			UIEdit.getLayoutManager().setEffect(ui);
			return super.fieldChanged(edit_object, field_value, field);
		}

	}
	
	

	public static class UECheckBoxAdapter extends UEAdapter<UECheckBox>
	{
		public UECheckBoxAdapter(UIEditFileNode edit) 
		{
			super(edit);
		}
		
		@Override
		public Class<UECheckBox> getType() 
		{
			return UECheckBox.class;
		}
		
		@Override
		public boolean fieldChanged(Object edit_object, Object field_value, Field field)
		{
			UECheckBox btn = (UECheckBox)edit_object;
			
			if (field.getName().equals("imagePathChecked") || 
				field.getName().equals("imagePathUnchecked")) 
			{
				BufferedImage bf = UIEdit.getLayoutManager().getImageBuffer(field_value + "");
				if (bf != null) {
					if (field.getName().equals("imagePathChecked")) {
						btn.imageChecked = bf;
					}
					if (field.getName().equals("imagePathUnchecked")) {
						btn.imageUnchecked = bf;
					}
				}
			}
			return super.fieldChanged(edit_object, field_value, field);
		}
		
		@Override
		public PropertyCellEdit<?> getCellEdit(ObjectPropertyEdit owner, Object editObject, Object fieldValue, Field field) 
		{
			UECheckBox btn = (UECheckBox)editObject;
			
			if (field.getName().equals("imagePathChecked") || 
				field.getName().equals("imagePathUnchecked")) 
			{
				String path = null;
				if (field.getName().equals("imagePathChecked")) {
					path = btn.imagePathChecked;
				}
				else if (field.getName().equals("imagePathUnchecked")) {
					path = btn.imagePathUnchecked;
				}
				UIEditImage bf = UILayoutManager.dialogLoadImage(path);
				if (bf != null) {
					if (field.getName().equals("imagePathChecked")) {
						btn.imageChecked = bf.getDisplayImage();
						btn.imagePathChecked = bf.getName();
					}
					if (field.getName().equals("imagePathUnchecked")) {
						btn.imageUnchecked = bf.getDisplayImage();
						btn.imagePathUnchecked = bf.getName();
					}
					return new TextCellEdit(bf.getName());
				}
			}
			if (field.getName().equals("imageAtlasChecked") || 
				field.getName().equals("imageAtlasUnchecked")) 
			{
				UIEditImageAtlas atlas = null;
				String path = null;
				if (field.getName().equals("imageAtlasChecked") && btn.imageAtlasChecked != null) {
					path = btn.imageAtlasChecked.cpj_fileName;
					atlas = btn.imageAtlasChecked;
				}
				else if (field.getName().equals("imageAtlasUnchecked") && btn.imageAtlasUnchecked != null) {
					path = btn.imageAtlasUnchecked.cpj_fileName;
					atlas = btn.imageAtlasUnchecked;
				}
				UIEditImage bf = UILayoutManager.dialogLoadImageAtlas(path, atlas);
				if (bf != null) {
					if (field.getName().equals("imageAtlasChecked")) {
						btn.imageAtlasChecked = bf.getAtlas();
						return new TextCellObjectEdit<UIEditImageAtlas>(btn.imageAtlasChecked);
					}
					if (field.getName().equals("imageAtlasUnchecked")) {
						btn.imageAtlasUnchecked = bf.getAtlas();
						return new TextCellObjectEdit<UIEditImageAtlas>(btn.imageAtlasUnchecked);
					}
				}
			}
			return super.getCellEdit(owner, editObject, fieldValue, field);
		}
	
	}


	public static class UERootTransitionAdapter extends UEAdapter<UERoot>
	{
		public UERootTransitionAdapter(UIEditFileNode edit) 
		{
			super(edit);
		}
		
		@Override
		public Class<UERoot> getType() 
		{
			return UERoot.class;
		}
		
		@Override
		public PropertyCellEdit<?> getCellEdit(ObjectPropertyEdit owner, Object editObject, Object fieldValue, Field field) 
		{
			UERoot root = (UERoot)editObject;
			if (field.getName().equals("fadeIn")) {
				UIActionEditor editor = new UIActionEditor(true);
				UIFadeAction action = editor.showDialog();
				if (action != null) {
					root.fadeIn = action;
				}
			} else if (field.getName().equals("fadeOut")) {
				UIActionEditor editor = new UIActionEditor(false);
				UIFadeAction action = editor.showDialog();
				if (action != null) {
					root.fadeOut = action;
				}
			}
			return super.getCellEdit(owner, editObject, fieldValue, field);
		}
	
	}
	
	public static class UIFontStyleAdapter extends UEAdapter<UIComponent>
	{
//		ShowSelectStyleTemplates()
		
		public UIFontStyleAdapter(UIEditFileNode edit) 
		{
			super(edit);
		}
		
		@Override
		public Class<UIComponent> getType() 
		{
			return UIComponent.class;
		}
		
		@Override
		public PropertyCellEdit<?> getCellEdit(
				ObjectPropertyEdit owner,
				Object editObject, 
				Object fieldValue, 
				Field field) 
		{
			if (UIImageFont.class.isAssignableFrom(field.getType()))
			{
				UIImageFont ft = (UIImageFont)fieldValue;
				UIImageFont bf = UILayoutManager.dialogLoadImageFont(ft);
				if (bf != null) {
					try {
						field.set(editObject, bf);
						fieldValue = bf;
						TextCellEdit text_edit = new TextCellEdit();
						String txt = Parser.objectToString(bf);
						text_edit.setText(txt);
						return text_edit;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			else if (UIEditFontStyle.class.isAssignableFrom(field.getType()))
			{
				UIEditFontStyle ret = UIFontStyleEditor.getInstance().
						ShowSelectStyleTemplates((UIEditFontStyle)fieldValue);
				if (ret != null)
				{
					try {
						field.set(editObject, ret);
						fieldValue = ret;
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (editObject instanceof BaseTextComponent)
					{
						BaseTextComponent base = (BaseTextComponent)editObject;
						ret.getBuffer(base.text, base.getFontSize());
					}
					else if (editObject instanceof UEButton)
					{
						UEButton base = (UEButton)editObject;
						ret.getBuffer(base.text, base.getTextSize());
					}
					TextCellEdit text_edit = new TextCellEdit();
					String txt = Parser.objectToString(ret);
					text_edit.setText(txt);
					return text_edit;
				}
			}
			return super.getCellEdit(owner, editObject, fieldValue, field);
		}
	}
}
