package com.g2d.awt.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;

import com.g2d.util.Drawing.ImageAnchor;


public class Drawing 
{
	final static public void fillPaintRect(Graphics2D g, Paint paint, int x, int y, int width, int height) 
	{
		Paint op = g.getPaint();
		g.translate(x, y);
		try {
			g.setPaint(paint);
			g.fillRect(0, 0, width, height);
		} finally {
			g.translate(-x, -y);
			g.setPaint(op);
		}
	}	
	
	final static public void fillPaint(Graphics2D g, Paint paint, int x, int y, Shape shape) 
	{
		Paint op = g.getPaint();
		g.translate(x, y);
		try {
			g.setPaint(paint);
			g.fill(shape);
		} finally {
			g.translate(-x, -y);
			g.setPaint(op);
		}
	}
	
	
	
	final static public void drawRoundImage(Graphics2D g, Image src, int x, int y, int width, int height)
	{
		Shape rect = g.getClip();
		
		g.clipRect(x , y , width, height);
		
		int w = src.getWidth(null);
		int h = src.getHeight(null);
		
		for (int dx = 0; dx < width;) {
			for (int dy = 0; dy < height;) {
				g.drawImage(src, x + dx, y + dy, null);
				dy += h;
			}
			dx += w;
		}
		
		g.setClip(rect);
	}
	
	final static public void drawRoundImageH(Graphics2D g, Image src, int x, int y, int width, int height)
	{
		Shape rect = g.getClip();
		
		g.clipRect(x , y , width, height);
		
		int w = src.getWidth(null);
		
		for(int dx=0;dx<width;){
			g.drawImage(src, x+dx, y, w, height, null);
			dx += w;
		}
		
		g.setClip(rect);
	}
	
	final static public void drawRoundImageV(Graphics2D g, Image src, int x, int y, int width, int height)
	{
		Shape rect = g.getClip();
		
		g.clipRect(x , y , width, height);
		
		int h = src.getHeight(null);
		
		for(int dy=0;dy<height;){
			g.drawImage(src, x, y+dy, width, h, null);
			dy += h;
		}
		
		g.setClip(rect);
	}
	
	
	final static public int TEXT_ANCHOR_LEFT 	= 0x00;
	final static public int TEXT_ANCHOR_RIGHT 	= 0x01;
	final static public int TEXT_ANCHOR_HCENTER = 0x02;
	final static public int TEXT_ANCHOR_TOP 	= 0x00;
	final static public int TEXT_ANCHOR_BOTTON 	= 0x10;
	final static public int TEXT_ANCHOR_VCENTER = 0x20;
	
	final static public Rectangle drawString(Graphics2D g, String src, int x, int y)
	{
		Rectangle rect = g.getFont().getStringBounds(src, g.getFontRenderContext()).getBounds();
		g.drawString(src, (int)(x-rect.getX()), (int)(y - rect.getY()));
		return rect;
	}
	
	final static public Rectangle drawString(Graphics2D g, String src, int x, int y, int anchor)
	{
		Rectangle rect = g.getFont().getStringBounds(src, g.getFontRenderContext()).getBounds();
//		System.out.println("g.getFont()="+g.getFont());
//		System.out.println(src + "= "+ rect.toString());
		if ((anchor & TEXT_ANCHOR_RIGHT) != 0) {
			x -= rect.width;
		}else if ((anchor & TEXT_ANCHOR_HCENTER) != 0) {
			x -= (rect.width>>1);
		}
		
		if ((anchor & TEXT_ANCHOR_BOTTON) != 0) {
			y -= rect.height;
		}else if ((anchor & TEXT_ANCHOR_VCENTER) != 0) {
			y -= (rect.height>>1);
		}
		
		g.drawString(src, x-rect.x, y - rect.y);
		
		return rect;
	}
	
	final static public Rectangle drawString(Graphics2D g, String src, int x, int y, int w, int h, int anchor)
	{
		Rectangle rect = g.getFont().getStringBounds(src, g.getFontRenderContext()).getBounds();
		
		if ((anchor & TEXT_ANCHOR_RIGHT) != 0) {
			x += w - rect.width;
		}else if ((anchor & TEXT_ANCHOR_HCENTER) != 0) {
			x += ((w-rect.width)>>1);
		}
		
		if ((anchor & TEXT_ANCHOR_BOTTON) != 0) {
			y += h - rect.height;
		}else if ((anchor & TEXT_ANCHOR_VCENTER) != 0) {
			y += ((h-rect.height)>>1);
		}
		
		g.drawString(src, x-rect.x, y - rect.y);
		
		return rect;
	}
	
	final static public Rectangle drawStringBorder(Graphics2D g, String src, int x, int y, int w, int h, int anchor)
	{
		Color c = g.getColor();
		
		{
			g.setColor(Color.BLACK);
			drawString(g, src, x-1, y-1, w, h, anchor);
			drawString(g, src, x-1, y-0, w, h, anchor);
			drawString(g, src, x-1, y+1, w, h, anchor);
			drawString(g, src, x-0, y-1, w, h, anchor);
			drawString(g, src, x-0, y+1, w, h, anchor);
			drawString(g, src, x+1, y-1, w, h, anchor);
			drawString(g, src, x+1, y-0, w, h, anchor);
			drawString(g, src, x+1, y+1, w, h, anchor);
		}
		
		g.setColor(c);
		return drawString(g, src, x, y, w, h, anchor);
	}
	
	final static public Rectangle drawStringShadow(Graphics2D g, String src, int x, int y, int w, int h, int anchor)
	{
		Color c = g.getColor();
		
		{
			g.setColor(Color.BLACK);
			drawString(g, src, x-0, y+1, w, h, anchor);
			drawString(g, src, x+1, y+1, w, h, anchor);
		}
		
		g.setColor(c);
		return drawString(g, src, x, y, w, h, anchor);
	}
	
	final static public Rectangle drawStringBorder(Graphics2D g, String src, int x, int y, int anchor)
	{
		Color c = g.getColor();
		
		{
			g.setColor(Color.BLACK);
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
	
	final static public Rectangle drawStringShadow(Graphics2D g, String src, int x, int y, int anchor)
	{
		Color c = g.getColor();
		
		{
			g.setColor(Color.BLACK);
			drawString(g, src, x-0, y+1, anchor);
			drawString(g, src, x+1, y+1, anchor);
		}
		
		g.setColor(c);
		return drawString(g, src, x, y, anchor);
	}
	
	final static public void drawImageAnchor(Graphics2D g, Image src, ImageAnchor anchor, int x, int y, int width, int height)
	{
		int lx = 0;
		int cx = width / 2 - src.getWidth(null)/2;
		int rx = width - src.getWidth(null);
		
		int ty = 0;
		int cy = height / 2 - src.getHeight(null)/2;
		int by = height - src.getHeight(null);
		
		switch (anchor)
		{
		case L_T: g.drawImage(src, lx, ty, null); return;
		case C_T: g.drawImage(src, cx, ty, null); return;
		case R_T: g.drawImage(src, rx, ty, null); return;
		case L_C: g.drawImage(src, lx, cy, null); return;
		case C_C: g.drawImage(src, cx, cy, null); return;
		case R_C: g.drawImage(src, rx, cy, null); return;
		case L_B: g.drawImage(src, lx, by, null); return;
		case C_B: g.drawImage(src, cx, by, null); return;
		case R_B: g.drawImage(src, rx, by, null); return;
		}

	}
	
}
