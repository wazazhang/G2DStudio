package com.g2d.util;


import com.g2d.Color;
import com.g2d.Graphics2D;
import com.g2d.Image;
import com.g2d.Paint;
import com.g2d.Tools;
import com.g2d.display.ui.layout.UILayout;
import com.g2d.geom.Rectangle;
import com.g2d.geom.Shape;
import com.g2d.text.MultiTextLayout;

public class Drawing 
{
	final static public void fillPaintRect(Graphics2D g, Paint paint, int x, int y, int width, int height) 
	{
		g.pushPaint();
		g.translate(x, y);
		try {
			g.setPaint(paint);
			g.fillRect(0, 0, width, height);
		} finally {
			g.translate(-x, -y);
			g.popPaint();
		}
	}	
	
	final static public void fillPaint(Graphics2D g, Paint paint, int x, int y, Shape shape) 
	{
		g.pushPaint();
		g.translate(x, y);
		try {
			g.setPaint(paint);
			g.fill(shape);
		} finally {
			g.translate(-x, -y);
			g.popPaint();
		}
	}

	final static public int drawRoundImage(Graphics2D g, Image src, int x,
			int y, int width, int height, int trans, boolean continually) 
	{
		int time = 0;
		g.pushClip();
		g.clipRect(x, y, width, height);
		if (continually) {
			int w = src.getWidth();
			int h = src.getHeight();
			for (int dx = 0; dx < width;) {
				for (int dy = 0; dy < height;) {
					g.drawImage(src, x + dx, y + dy, trans);
					dy += h;
					time++;
				}
				dx += w;
			}
		} else {
			g.drawImage(src, x, y, width, height, trans);
			time++;
		}
		g.popClip();
		return time;
	}

	final static public int drawRoundImage(Graphics2D g, Image src, int x,
			int y, int width, int height, boolean continually) {
		return drawRoundImage(g, src, x, y, width, height,
				Graphics2D.TRANS_NONE, continually);
	}

	final static public int drawRoundImageH(Graphics2D g, Image src, int x,
			int y, int width, int height, boolean continually) {
		int time = 0;
		g.pushClip();
		g.clipRect(x, y, width, height);
		if (continually) {
			int w = src.getWidth();
			for (int dx = 0; dx < width;) {
				g.drawImage(src, x + dx, y, w, height);
				dx += w;
				time++;
			}
		} else {
			g.drawImage(src, x, y, width, height);
			time++;
		}
		g.popClip();
		return time;
	}

	final static public int drawRoundImageV(Graphics2D g, Image src, int x,
			int y, int width, int height, boolean continually) {
		int time = 0;
		g.pushClip();
		g.clipRect(x, y, width, height);
		if (continually) {
			int h = src.getHeight();
			for (int dy = 0; dy < height;) {
				g.drawImage(src, x, y + dy, width, h);
				dy += h;
				time++;
			}
		} else {
			g.drawImage(src, x, y, width, height);
			time++;
		}
		g.popClip();
		return time;
	}

//	-----------------------------------------------------------------------------------------------------------------------------
//	draw string
//	-----------------------------------------------------------------------------------------------------------------------------
	
	final static public int TEXT_ANCHOR_LEFT 	= 0x00;
	final static public int TEXT_ANCHOR_RIGHT 	= 0x01;
	final static public int TEXT_ANCHOR_HCENTER = 0x02;
	final static public int TEXT_ANCHOR_TOP 	= 0x00;
	final static public int TEXT_ANCHOR_BOTTON 	= 0x10;
	final static public int TEXT_ANCHOR_VCENTER = 0x20;
	
	public static enum TextAnchor
	{
		L_T, C_T, R_T,
		L_C, C_C, R_C,
		L_B, C_B, R_B,;
		
		public static int toAnchorValue(TextAnchor ta) {
			switch(ta) {
			case L_T: return TEXT_ANCHOR_LEFT    | TEXT_ANCHOR_TOP;
			case C_T: return TEXT_ANCHOR_HCENTER | TEXT_ANCHOR_TOP;
			case R_T: return TEXT_ANCHOR_RIGHT   | TEXT_ANCHOR_TOP;
			case L_C: return TEXT_ANCHOR_LEFT    | TEXT_ANCHOR_VCENTER;
			case C_C: return TEXT_ANCHOR_HCENTER | TEXT_ANCHOR_VCENTER;
			case R_C: return TEXT_ANCHOR_RIGHT   | TEXT_ANCHOR_VCENTER;
			case L_B: return TEXT_ANCHOR_LEFT    | TEXT_ANCHOR_BOTTON;
			case C_B: return TEXT_ANCHOR_HCENTER | TEXT_ANCHOR_BOTTON;
			case R_B: return TEXT_ANCHOR_RIGHT   | TEXT_ANCHOR_BOTTON;
			}
			return 0;
		}
	};
	

	
	final static public Rectangle drawString(Graphics2D g, String src, int x, int y)
	{
		Rectangle rect = g.getFont().getStringSize(src, g);
		rect.setBounds(0, 0, rect.width, rect.height);
		g.drawString(src, x, y);
		return rect;
	}
	
	final static public Rectangle drawString(Graphics2D g, String src, int x, int y, int anchor)
	{
		Rectangle rect = g.getFont().getStringSize(src, g);
		rect.setBounds(0, 0, rect.width, rect.height);

		if ((anchor & TEXT_ANCHOR_RIGHT) != 0) {
			x -= rect.width;
		} else if ((anchor & TEXT_ANCHOR_HCENTER) != 0) {
			x -= (rect.width >> 1);
		}

		if ((anchor & TEXT_ANCHOR_BOTTON) != 0) {
			y -= rect.height;
		} else if ((anchor & TEXT_ANCHOR_VCENTER) != 0) {
			y -= (rect.height >> 1);
		}
		
		g.drawString(src, x, y);
		
		return rect;
	}
	
	final static public Rectangle drawString(Graphics2D g, String src, int x, int y, int w, int h, int anchor)
	{
		Rectangle rect = g.getFont().getStringSize(src, g);
		rect.setBounds(0, 0, rect.width, rect.height);
		
		if ((anchor & TEXT_ANCHOR_RIGHT) != 0) {
			x += w - rect.width;
		} else if ((anchor & TEXT_ANCHOR_HCENTER) != 0) {
			x += ((w - rect.width) >> 1);
		}

		if ((anchor & TEXT_ANCHOR_BOTTON) != 0) {
			y += h - rect.height;
		} else if ((anchor & TEXT_ANCHOR_VCENTER) != 0) {
			y += ((h - rect.height) >> 1);
		}
		
		g.drawString(src, x, y);
		
		return rect;
	}
	
//	-----------------------------------------------------------------------------------------------------------------------------
//	draw string border
//	-----------------------------------------------------------------------------------------------------------------------------
	
	final static public Rectangle drawStringBorder(Graphics2D g, String src, 
			int x, int y, int w, int h, TextAnchor anchor)
	{
		return drawStringBorder(g, src, 
				x, y, w, h, 
				TextAnchor.toAnchorValue(anchor),
				Color.BLACK);
	}
	
	final static public Rectangle drawStringBorder(Graphics2D g, String src,
			int x, int y, TextAnchor anchor)
	{
		return drawStringBorder(g, src, x, y, 
				TextAnchor.toAnchorValue(anchor), Color.BLACK);
	}
	
	final static public Rectangle drawStringBorder(Graphics2D g, String src, 
			int x, int y, TextAnchor anchor, 
			Color back_color)
	{
		return drawStringBorder(g, src, x, y, 
				TextAnchor.toAnchorValue(anchor), back_color);
	}
	
	final static public Rectangle drawStringBorder(Graphics2D g, String src,
			int x, int y, int w, int h, TextAnchor anchor, Color back_color)
	{
		return drawStringBorder(g, src, x, y, w, h, 
				TextAnchor.toAnchorValue(anchor), back_color);
	}
	
	
	
	
	
	final static public Rectangle drawStringBorder(Graphics2D g, String src, int x, int y, int w, int h, int anchor)
	{
		return drawStringBorder(g, src, x, y, w, h, anchor, Color.BLACK);
	}
	
	final static public Rectangle drawStringBorder(Graphics2D g, String src, int x, int y, int anchor)
	{
		return drawStringBorder(g, src, x, y, anchor, Color.BLACK);
	}
	
	final static public Rectangle drawStringBorder(Graphics2D g, String src, int x, int y, int anchor, Color back_color)
	{
		if ((anchor & TEXT_ANCHOR_RIGHT) != 0) {
			x -= 1;
		} else if ((anchor & TEXT_ANCHOR_HCENTER) != 0) {
		} else {
			x += 1;
		}
		if ((anchor & TEXT_ANCHOR_BOTTON) != 0) {
			y -= 1;
		} else if ((anchor & TEXT_ANCHOR_VCENTER) != 0) {
		} else {
			y += 1;
		}
		Color c = g.getColor();
		{
			g.setColor(back_color);
			drawString(g, src, x-1, y-1, anchor);
			drawString(g, src, x-1, y-0, anchor);
			drawString(g, src, x-1, y+1, anchor);
			drawString(g, src, x-0, y-1, anchor);
			drawString(g, src, x-0, y+1, anchor);
			drawString(g, src, x+1, y-1, anchor);
			drawString(g, src, x+1, y-0, anchor);
			drawString(g, src, x+1, y+1, anchor);
		}
		g.setColor(c);
		return drawString(g, src, x, y, anchor);
	}

	final static public Rectangle drawStringBorder(Graphics2D g, String src, int x, int y, int w, int h, int anchor, Color back_color)
	{
		if ((anchor & TEXT_ANCHOR_RIGHT) != 0) {
			x -= 1;
		} else if ((anchor & TEXT_ANCHOR_HCENTER) != 0) {
		} else {
			x += 1;
		}
		if ((anchor & TEXT_ANCHOR_BOTTON) != 0) {
			y -= 1;
		} else if ((anchor & TEXT_ANCHOR_VCENTER) != 0) {
		} else {
			y += 1;
		}
		if (back_color.getAlpha() > 0)
		{
			Color c = g.getColor();
			g.setColor(back_color);
			drawString(g, src, x-1, y-1, w, h, anchor);
			drawString(g, src, x-1, y-0, w, h, anchor);
			drawString(g, src, x-1, y+1, w, h, anchor);
			drawString(g, src, x-0, y-1, w, h, anchor);
			drawString(g, src, x-0, y+1, w, h, anchor);
			drawString(g, src, x+1, y-1, w, h, anchor);
			drawString(g, src, x+1, y-0, w, h, anchor);
			drawString(g, src, x+1, y+1, w, h, anchor);
			g.setColor(c);
		}
		return drawString(g, src, x, y, w, h, anchor);
	}
	

//	-----------------------------------------------------------------------------------------------------------------------------
//	draw string shadow
//	-----------------------------------------------------------------------------------------------------------------------------
	
	
	final static public Rectangle drawStringShadow(Graphics2D g, String src, int x, int y, int anchor)
	{
		return drawStringShadow(g, src, x, y, anchor, Color.BLACK);
	}

	final static public Rectangle drawStringShadow(Graphics2D g, String src, int x, int y, int w, int h, int anchor)
	{
		return drawStringShadow(g, src, x, y, w, h, anchor, Color.BLACK);
	}
	
	final static public Rectangle drawStringShadow(Graphics2D g, String src, int x, int y, int anchor, Color back_color)
	{
		if ((anchor & TEXT_ANCHOR_RIGHT) != 0) {
			x -= 1;
		}
		if ((anchor & TEXT_ANCHOR_BOTTON) != 0) {
			y -= 1;
		}
		Color c = g.getColor();
		{
			g.setColor(back_color);
			drawString(g, src, x-0, y+1, anchor);
			drawString(g, src, x+1, y+1, anchor);
		}
		g.setColor(c);
		return drawString(g, src, x, y, anchor);
	}

	final static public Rectangle drawStringShadow(Graphics2D g, String src, int x, int y, int w, int h, int anchor, Color back_color)
	{
		if ((anchor & TEXT_ANCHOR_RIGHT) != 0) {
			x -= 1;
		}
		if ((anchor & TEXT_ANCHOR_BOTTON) != 0) {
			y -= 1;
		}
		Color c = g.getColor();
		{
			g.setColor(back_color);
			drawString(g, src, x-0, y+1, w, h, anchor);
			drawString(g, src, x+1, y+1, w, h, anchor);
		}
		g.setColor(c);
		return drawString(g, src, x, y, w, h, anchor);
	}
	

//	-----------------------------------------------------------------------------------------------------------------------------
//	draw advance string
//	-----------------------------------------------------------------------------------------------------------------------------
	
	final static public void drawString(Graphics2D g, MultiTextLayout src, int x, int y, int anchor)
	{
		if ((anchor & TEXT_ANCHOR_RIGHT) != 0) {
			x -= src.getWidth();
		} else if ((anchor & TEXT_ANCHOR_HCENTER) != 0) {
			x -= (src.getWidth() >> 1);
		}

		if ((anchor & TEXT_ANCHOR_BOTTON) != 0) {
			y -= src.getHeight();
		} else if ((anchor & TEXT_ANCHOR_VCENTER) != 0) {
			y -= (src.getHeight() >> 1);
		}
		
		src.drawText(g, x, y);
		
	}
	
	final static public void drawString(Graphics2D g, MultiTextLayout src, int x, int y, int w, int h, int anchor)
	{
		if ((anchor & TEXT_ANCHOR_RIGHT) != 0) {
			x += w - src.getWidth();
		} else if ((anchor & TEXT_ANCHOR_HCENTER) != 0) {
			x += ((w - src.getWidth()) >> 1);
		}

		if ((anchor & TEXT_ANCHOR_BOTTON) != 0) {
			y += h - src.getHeight();
		} else if ((anchor & TEXT_ANCHOR_VCENTER) != 0) {
			y += ((h - src.getHeight()) >> 1);
		}
		
		src.drawText(g, x, y, 0, 0, w, h);
	}
	
	final static public void drawStringShwdow(Graphics2D g, MultiTextLayout src, int x, int y, int w, int h, int anchor)
	{
		if ((anchor & TEXT_ANCHOR_RIGHT) != 0) {
			x += w - src.getWidth();
		} else if ((anchor & TEXT_ANCHOR_HCENTER) != 0) {
			x += ((w - src.getWidth()) >> 1);
		}

		if ((anchor & TEXT_ANCHOR_BOTTON) != 0) {
			y += h - src.getHeight();
		} else if ((anchor & TEXT_ANCHOR_VCENTER) != 0) {
			y += ((h - src.getHeight()) >> 1);
		}

		src.drawText(g, x, y, 0, 0, w, h, 1, 1, 1f, 0);
	}
	
	
	
	
	
	
	
	
	
	/**<pre>
	 * 将图片向外渐隐扩展显示。
	 * 显示范围vtimer递增
	 *   |---- keep_time ----|
	 *   |---- cycle_time ------------|
	 *   ^                   ^        ^
	 *   1                   2        3
	 * 1~2阶段：
	 *   从原尺寸过渡到目标尺寸为（src_size -> src_size+out_size），
	 *   alpha度从1过度到0
	 * 2~3阶段：
	 *   不显示
	 * @param g
	 * @param image
	 * @param src_w 原尺寸
	 * @param src_h 原尺寸
	 * @param out_w 放大多少尺寸
	 * @param out_h 放大多少尺寸
	 * @param vtimer 当前时间（帧）
	 * @param keep_time  显示保持时间（帧）
	 * @param cycle_time 显示循环时间（帧）必须大于keep_time
	 */
	public static void drawImageOutShadow(
			Graphics2D g, 
			Image image,
			int src_w, int src_h, 
			int out_w, int out_h,
			int vtimer, 
			int keep_time, 
			int cycle_time)
	{
		int dtime = vtimer % cycle_time;
		if (dtime < keep_time) {
			float alpha = ((float)(dtime)/(float)keep_time);
			int tw = src_w + (int)(out_w * alpha);
			int th = src_h + (int)(out_h * alpha);
			g.pushComposite();
			try {
				Tools.setAlpha(g, 1 - alpha);
				g.drawImage(image, (src_w-tw)>>1, (src_h-th)>>1, tw, th);
			} finally {
				g.popComposite();
			}
		}
	}
	

	/**
	 * @see #drawImageOutShadow(Graphics2D, Image, int, int, int, int, int, int, int)
	 * @param g
	 * @param image
	 * @param src_w
	 * @param src_h
	 * @param out_w
	 * @param out_h
	 * @param vtimer
	 * @param keep_time
	 * @param cycle_time
	 */
	public static void drawUILayoutOutShadow(
			Graphics2D g, 
			UILayout image,
			int src_w, int src_h, 
			int out_w, int out_h,
			int vtimer, 
			int keep_time, 
			int cycle_time)
	{
		int dtime = vtimer % cycle_time;
		if (dtime < keep_time) {
			float alpha = ((float)(dtime)/(float)keep_time);
			int tw = src_w + (int)(out_w * alpha);
			int th = src_h + (int)(out_h * alpha);
			g.pushComposite();
			try {
				Tools.setAlpha(g, 1 - alpha);
				image.render(g, (src_w-tw)>>1, (src_h-th)>>1, tw, th);
			} finally {
				g.popComposite();
			}
		}
	}

	public static enum ImageAnchor
	{
		L_T, C_T, R_T, L_C, C_C, R_C, L_B, C_B, R_B,
	}

	
	final static public void drawImageAnchor(Graphics2D g, Image src, int x, int y, int width, int height, ImageAnchor anchor)
	{
		int dx = 0;
		int dy = 0;
		switch (anchor) {
		case L_T: 
			break;
		case C_T:
			dx = (width - src.getWidth())>>1;
			break;
		case R_T:
			dx = (width - src.getWidth());
			break;
		case L_C:
			dy = (height - src.getHeight())>>1;
			break;
		case C_C:
			dx = (width - src.getWidth())>>1;
			dy = (height - src.getHeight())>>1;
			break;
		case R_C:
			dx = (width - src.getWidth());
			dy = (height - src.getHeight())>>1;
			break;
		case L_B:
			dy = (height - src.getHeight());
			break;
		case C_B:
			dx = (width - src.getWidth())>>1;
			dy = (height - src.getHeight());
			break;
		case R_B:
			dx = (width - src.getWidth());
			dy = (height - src.getHeight());
			break;
		}
		g.drawImage(src, x+dx, y+dy);
	}
	
	final static public void drawResize9(Graphics2D g, int x, int y, int w, int h, int bsize)
	{
		g.pushTransform();
		g.translate(x, y);
		
		int s1 = bsize;
		int s2 = bsize<<1;
		int s4 = bsize<<2;
		g.pushClip();
		g.setClip(-s2, -s2, w+s4, h+s4);
		{
			g.drawRect(0, 0, w-1, h-1);
			
			g.fillRect( -s1,  -s1, s2, s2);
			g.fillRect(w-s1,  -s1, s2, s2);
			g.fillRect( -s1, h-s1, s2, s2);
			g.fillRect(w-s1, h-s1, s2, s2);
			
			g.fillRect(w/2-s1,  -s1, s2, s2);
			g.fillRect(w/2-s1, h-s1, s2, s2);
			g.fillRect( -s1, h/2-s1, s2, s2);
			g.fillRect(w-s1, h/2-s1, s2, s2);
		}
		g.popClip();
	
		g.popTransform();
	}
}
