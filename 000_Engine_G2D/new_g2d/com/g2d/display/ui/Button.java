package com.g2d.display.ui;
import com.g2d.Color;
import com.g2d.Font;
import com.g2d.Graphics2D;
import com.g2d.annotation.Property;
import com.g2d.annotation.TextProperty;
import com.g2d.util.Drawing;
import com.g2d.util.Drawing.TextAnchor;

/**
 * 用于显示基本文字的按钮
 * @author WAZA
 */
public class Button extends BaseButton 
{
	/**文字颜色*/
	@Property("文字颜色")
	public Color 				unfocusTextColor	= new Color(0xffffFF00);

	@Property("文字边颜色")
	public Color				textBorderColor		= Color.BLACK;
	@Property("文字边透明度%")
	public float				textBorderAlpha		= 100.0f;
	
	/**文字颜色(获得鼠标后)*/
	@Property("文字颜色(获得鼠标后)")
	public Color 				focusTextColor		= new Color(0xffffffff);
	
	/**text*/
	@TextProperty
	@Property("text")
	public String 				text 				= getClass().getSimpleName();
	
	@TextProperty
	@Property("textDown")
	public String 				textDown;
	
	@Property("文字类型")
	public Font textFont = null;
	
	/**text_anchor*/
	@Property("文字对齐")
	public TextAnchor text_anchor = TextAnchor.C_C;

	@Property("text_offset_x")
	public int text_offset_x;
	@Property("text_offset_y")
	public int text_offset_y;

	@Property("textSize")
	public int textSize;

	/**文字是否抗锯齿*/
//	@Property("文字是否抗锯齿")
	public boolean	enable_antialiasing	 = true;
	
	
	public Button(String text, int width, int height) {
		super(width, height);
		this.text = text;
	}
	
	public Button(String text) {
		super() ;
		this.text = text;
	}
	
	public Button() {
		super();
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public int getTextSize() {
		if (textSize > 0) {
			return textSize;
		}
		if (textFont != null) {
			return textFont.getSize();
		}
		return 0;
	}
	
	@Override
	public void render(Graphics2D g) {
		super.render(g);
		renderText(g, isOnDragged());
	}
	
	protected int renderText(Graphics2D g, boolean isDown)
	{
		Font oldf = g.getFont();
		boolean flag = g.getFontAntialiasing();
		int offsety = isDown?1:0;
		String txt = text;
		if (isDown && textDown!=null && !textDown.isEmpty()) {
			txt = textDown;
		}
		try {
			if (textSize > 0) {
				g.setFontSize(textSize);
			}
			else if (textFont != null) {
				g.setFont(textFont);
			}
			if (getRoot()!=null && isCatchedMouse()) {
				g.setColor(focusTextColor);
			}else{
				g.setColor(unfocusTextColor);
			}
			g.setFontAntialiasing(enable_antialiasing);
			textBorderColor.setAlpha(Math.min(Math.abs(textBorderAlpha)/100f, 1f));
			Drawing.drawStringBorder(
					g, txt,
					text_offset_x, 
					text_offset_y+offsety,
					getWidth(),
					getHeight(), 
					text_anchor,
					textBorderColor);
		} finally {
			g.setFont(oldf);
			g.setFontAntialiasing(flag);
		}
		return 1;
	}
}
