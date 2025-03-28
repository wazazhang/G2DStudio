package com.g2d.display.ui;

import java.text.AttributedString;

import com.g2d.Color;
import com.g2d.Engine;
import com.g2d.Graphics2D;
import com.g2d.annotation.Property;
import com.g2d.font.TextAttribute;
import com.g2d.text.MultiTextLayout;



public class TextPan extends UIComponent
{	
	transient int text_draw_x;
	transient int text_draw_y;
	transient MultiTextLayout atext = Engine.getEngine().createMultiTextLayout();
	
	/**文字颜色*/
	@Property("文字颜色")
	public Color 						textColor			= new Color(0xffffffff);

	@Property("阴影偏移X")
	public int text_shadow_x = 0;
	@Property("阴影偏移Y")
	public int text_shadow_y = 0;
	@Property("阴影透明度")
	public float text_shadow_alpha = 1f;
	@Property("阴影颜色")
	public Color text_shadow_dcolor = new Color(0);
	@Property("文字尺寸")
	public int text_size = 22;
	
	private int old_text_size = 22;
	
	/**文字是否抗锯齿*/
//	@Property("文字是否抗锯齿")
	public boolean	enable_antialiasing	 = true;
	
	public TextPan() 
	{
		this("");
	}
	
	public TextPan(String text)
	{
		this(text, 100, 100);
	}
	
	public TextPan(String text, int w, int h)
	{
		enable_key_input	= false;
		enable_mouse_wheel	= false;
		
		setText(text);
		setSize(w, h);
	}

//	public MultiTextLayout getTextLayout() {
//		return atext;
//	}
	
	public void setText(String text)
	{
		if (text==null)return;
		AttributedString astr = new AttributedString(text);
		if (text.length() > 0) {
			astr.addAttribute(TextAttribute.SIZE, text_size);
		}
		this.atext.setText(astr);
	}
	
//	public void setText(AttributedString atext) {
//		this.atext.setText(atext);
//	}
//	
//	public void appendLine(String text) {
//		this.atext.appendText(text);
//	}
//	
	public String getText() {
		return this.atext.getText();
	}
//	
//	public void appendText(String text) {
//		this.atext.appendText(text);
//	}
	
	public void update() 
	{
		super.update();
		atext.setShowCaret(false);
		atext.setWidth((getWidth()-layout.BorderSize*2));
		atext.setAntialias(enable_antialiasing);
		text_draw_x = layout.BorderSize;
		text_draw_y = layout.BorderSize;
		text_shadow_dcolor.setAlpha(text_shadow_alpha);
		
		if (old_text_size != text_size) {
			old_text_size = text_size;
			setText("");
			setText(getText());
		}
	}
	
	
	public void render(Graphics2D g) 
	{
		super.render(g);
		g.setColor(textColor);
		boolean flag = g.setFontAntialiasing(enable_antialiasing);
		try {
			atext.drawText(g, 
					text_draw_x, text_draw_y, 
					0, 0, 
					getWidth()-layout.BorderSize*2,
					getHeight()-layout.BorderSize*2, 
					text_shadow_x, 
					text_shadow_y,
					text_shadow_alpha,
					text_shadow_dcolor.getARGB());
			increaseFrameRenderTime(1);
		} finally {
			g.setFontAntialiasing(flag);
		}
	}

	
	
}
