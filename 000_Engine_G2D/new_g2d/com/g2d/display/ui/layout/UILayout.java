package com.g2d.display.ui.layout;


import java.awt.AlphaComposite;

import com.cell.DObject;
import com.cell.gfx.game.CSprite;
import com.cell.io.IOCloneable;
import com.g2d.BufferedImage;
import com.g2d.Color;
import com.g2d.Graphics2D;
import com.g2d.Image;
import com.g2d.Tools;
import com.g2d.geom.Rectangle;
import com.g2d.util.Drawing;

public class UILayout extends DObject implements IOCloneable
{	
	public static enum ImageStyle
	{
		NULL,
		COLOR,
		SPRITE,
		IMAGE_STYLE_ALL_9,
		IMAGE_STYLE_ALL_8,
		IMAGE_STYLE_H_012, 
		IMAGE_STYLE_V_036,
		IMAGE_STYLE_HLM, 
		IMAGE_STYLE_VTM,
		IMAGE_STYLE_BACK_4,
		IMAGE_STYLE_BACK_4_CENTER,
		;
		
		public static ImageStyle getStyle(String style) {
			return ImageStyle.valueOf(style);
		}
	}
	
//	------------------------------------------------------------------------------------------------------------------------------

	public static UILayout createRect(Color backcolor)
	{
		UILayout rect = new UILayout();
		rect.BackColor = backcolor;
		rect.BorderColor = backcolor;
		rect.BorderSize = 0;
		return rect;
	}
	public static UILayout createRect(Color backcolor, Color borderColor, int borderSize)
	{
		UILayout rect = new UILayout();
		rect.BackColor = backcolor;
		rect.BorderColor = borderColor;
		rect.BorderSize = borderSize;
		return rect;
	}
	public static UILayout createRect()
	{
		return createRect(new Color(0,0,0,0));
	}

	public static UILayout createBlankRect()
	{
		UILayout rect = createRect();
		rect.Style = ImageStyle.NULL;
		return rect;
	}

//	------------------------------------------------------------------------------------------------------------------------------
	
	//border
	public int 				BorderSize	= 1;
	
	private int				ClipSize = 1;
	// color layout
	private Color 			BackColor	= new Color(0xffc0c0c0);//0xffc0c0c0;
	private Color 			BorderColor	= new Color(0xff000000);//0xff000000;
	
	// image layout
	private ImageStyle 		Style		= ImageStyle.COLOR;
	
	private BufferedImage	BorderT;
	private BufferedImage	BorderB;
	private BufferedImage	BorderL;
	private BufferedImage	BorderR;
	private BufferedImage	BackImage;
	private BufferedImage	BorderTL;
	private BufferedImage	BorderTR;
	private BufferedImage	BorderBL;
	private BufferedImage	BorderBR;

	private BufferedImage	srcImage;
	
	private Rectangle		srcRect;
	
	private CSprite			sprite;
	
	private boolean			fillRepeat = false;
//	------------------------------------------------------------------------------------------------------------------------------

	public UILayout(){}
	
	public UILayout(ImageStyle style){
		this.Style = style;
		this.BorderSize = 0;
		this.ClipSize = 0;
	}
	
	/**
	 * @see {@link #setImages(BufferedImage[], ImageStyle)}
	 */
	public UILayout(BufferedImage[] images, ImageStyle style){
		setImages(images, style);
	}
	
	/**
	 * @see {@link #setImages(Image, ImageStyle, int)}
	 */
	public UILayout(BufferedImage src, ImageStyle style, int clipsize){
		setImages(src, style, clipsize, clipsize>>1);
	}

	/**
	 * @see {@link #setImages(Image, ImageStyle, int, int)}
	 */
	public UILayout(BufferedImage src, ImageStyle style, int clipsize, int bordersize){
		setImages(src, style, clipsize, bordersize);
	}

	public UILayout(UILayout set){
		this.set(set);
	}

	public UILayout clone() {
		UILayout ret = new UILayout(this);
		return ret;
	}

	public UILayout deriveImage(ImageStyle style, int clipsize) {
		UILayout ret = new UILayout(getSrcImage(), style, clipsize);
		return ret;
	}
	
	public UILayout deriveImage(ImageStyle style, int clipsize, int bordersize) {
		UILayout ret = new UILayout(getSrcImage(), style, clipsize, bordersize);
		return ret;
	}
	
	public UILayout deriveGrayImage() {
		UILayout ret = new UILayout(
				Tools.createTurngrey(getSrcImage()), 
				getStyle(), ClipSize, BorderSize);
		return ret;
	}
	
	@Override
	public String toString() {
		return "[UI]\n" + Style + "\n" + BackColor;
	}
	
	public CSprite getSprite() {
		return sprite;
	}
	
	public void setSprite(CSprite spr) {
		this.sprite = spr.clone();
		this.Style = ImageStyle.SPRITE;
	}
	
	public void setSpriteAnim(int anim) {
		this.sprite.setCurrentAnimate(anim);
	}
	
	public BufferedImage getSrcImage() {
		return srcImage;
	}
	public Rectangle getSrcRect() {
		return srcRect;
	}
	
	public Color getBackColor() {
		return BackColor;
	}

	public Color getBorderColor() {
		return BorderColor;
	}

	public void setStyle(ImageStyle style) {
		this.Style = style;
	}
	
	public ImageStyle getStyle() {
		return Style;
	}
	
	public Rectangle getPreferredSize()
	{
		Rectangle ret = new Rectangle();
		switch(Style)
		{
		case IMAGE_STYLE_ALL_9:
		case IMAGE_STYLE_ALL_8:
			ret.width  = BorderTL.getWidth()  + BorderT.getWidth()  + BorderTR.getWidth();
			ret.height = BorderTL.getHeight() + BorderL.getHeight() + BorderBL.getHeight();
			return ret;

		case IMAGE_STYLE_H_012:
			ret.width = BorderTL.getWidth() + BorderT.getWidth()
					+ BorderTR.getWidth();
			ret.height = BorderTL.getHeight();
			return ret;
			
		case IMAGE_STYLE_V_036:
			ret.width  = BorderTL.getWidth();
			ret.height = BorderTL.getHeight() + BorderL.getHeight() + BorderBL.getHeight();
			return ret;
			
		case IMAGE_STYLE_HLM:
			ret.width  = BorderTL.getWidth()  + BorderT.getWidth();
			ret.height = BorderTL.getHeight() + BorderL.getHeight() + BorderBL.getHeight();
			return ret;
		case IMAGE_STYLE_VTM:
			ret.width  = BorderTL.getWidth()  + BorderT.getWidth()  + BorderTR.getWidth();
			ret.height = BorderTL.getHeight() + BorderL.getHeight();
			return ret;

		case IMAGE_STYLE_BACK_4:
		case IMAGE_STYLE_BACK_4_CENTER:
			ret.width  = BackImage.getWidth();
			ret.height = BackImage.getHeight();
			return ret;
		case SPRITE:
			if (sprite != null) {
				ret.width = (int)sprite.getVisibleWidth();
				ret.height = (int)sprite.getVisibleHeight();
				return ret;
			}
			break;
		}
		return null;
	}
	
//	------------------------------------------------------------------------------------------------------------------------------

	public BufferedImage getBorderT() {
		return BorderT;
	}
	public BufferedImage getBorderB() {
		return BorderB;
	}
	public BufferedImage getBorderL() {
		return BorderL;
	}
	public BufferedImage getBorderR() {
		return BorderR;
	}
	public BufferedImage getBackImage() {
		return BackImage;
	}
	public BufferedImage getBorderTL() {
		return BorderTL;
	}
	public BufferedImage getBorderTR() {
		return BorderTR;
	}
	public BufferedImage getBorderBL() {
		return BorderBL;
	}
	public BufferedImage getBorderBR() {
		return BorderBR;
	}
	public boolean isRepeatFill() {
		return fillRepeat;
	}
	
	
	public void set(UILayout set)
	{
//		body
		BackColor		= set.BackColor;
		BackImage		= set.BackImage;
//		BackAlphaColor 	= set.BackAlphaColor;
		if (set.sprite != null) {
			sprite		= set.sprite.clone();
		} else {
			sprite		= set.sprite;
		}
		
//		border
		BorderSize		= set.BorderSize;
		BorderColor		= set.BorderColor;
		
		BorderT			= set.BorderT;
		BorderB			= set.BorderB;
		BorderL			= set.BorderL;
		BorderR			= set.BorderR;
		
		BorderTL		= set.BorderTL;
		BorderTR		= set.BorderTR;
		BorderBL		= set.BorderBL;
		BorderBR		= set.BorderBR;
		
		Style			= set.Style;
		srcImage 		= set.srcImage;
		srcRect			= set.srcRect;
		
		fillRepeat		= set.fillRepeat;
	}

	public void setRepeatFill(boolean c) {
		fillRepeat = c;
	}

	public void setBackColor(Color color) {
		BackColor = color;
	}

	public void setBorderColor(Color color) {
		BorderColor = color;
	}

	public void setImages(Image src, ImageStyle style, int clipsize) {
		setImages(src, style, clipsize, clipsize >> 1, null);
	}

	public void setImages(Image src, ImageStyle style, int clipsize, int bordersize) {
		setImages(src, style, clipsize, bordersize, null);
	}

	public void setImages(Image src, ImageStyle style, int clipsize,
			Rectangle srcRect) {
		setImages(src, style, clipsize, clipsize >> 1, srcRect);
	}

	public void setImages(
			Image src, 
			ImageStyle style, 
			int clipsize, 
			int bordersize, 
			Rectangle srcRect)
	{
		this.srcRect = srcRect;
		this.BorderSize = bordersize;
		this.ClipSize = clipsize;
		if (src instanceof BufferedImage) {
			this.srcImage = (BufferedImage)src;
			clipImages(style, clipsize, clipsize, clipsize, clipsize);
		} else if (src != null) {
			this.srcImage = Tools.combianImage(src.getWidth(), src.getHeight(), src);
			clipImages(style, clipsize, clipsize, clipsize, clipsize);
		}
	}
	

	private void clipImages(
			ImageStyle style, 
			int L, int R, 
			int T, int B)
	{
		BufferedImage src = srcImage;
		if (srcRect != null) {
			src = (BufferedImage)srcImage.subImage(
					srcRect.x, 
					srcRect.y, 
					srcRect.width, 
					srcRect.height);
		}
		
		BufferedImage BorderTL	= null;
		BufferedImage BorderT	= null;
		BufferedImage BorderTR 	= null;
		BufferedImage BorderL  	= null;
		BufferedImage BackImage = null;
		BufferedImage BorderR  	= null;
		BufferedImage BorderBL	= null;
		BufferedImage BorderB	= null;
		BufferedImage BorderBR	= null;

		if (src != null)
		{
			int W = src.getWidth();
			int H = src.getHeight();

			switch(style)
			{
			case IMAGE_STYLE_ALL_9:
				BackImage 	= Tools.subImage(src, L, T, W - L - R, H - T - B);
				
			case IMAGE_STYLE_ALL_8:
				BorderTL 	= Tools.subImage(src, 0, 0, L, T);
				BorderT 	= Tools.subImage(src, L, 0, W - L - R, T);
				BorderTR 	= Tools.subImage(src, W - R, 0, R, T);
				BorderL 	= Tools.subImage(src, 0, T, L, H - T - B);
				BorderR 	= Tools.subImage(src, W - R, T, R, H - T - B);
				BorderBL 	= Tools.subImage(src, 0, H - B, L, B);
				BorderB 	= Tools.subImage(src, L, H - B, W - L - R, B);
				BorderBR 	= Tools.subImage(src, W - R, H - B, R, B);
//				IImage m = new CImage(src).newInstance();
//				int rgb[] = new int[1];
//				m.getRGB(rgb, 0, 1, R, T, 1, 1);
//				BackColor = new Color(rgb[0]);
				BackColor	= new Color(src.getRGB(
						src.getWidth()/2,
						src.getHeight()/2));
				break;
				
			case IMAGE_STYLE_H_012:
				BorderTL	= Tools.subImage(src, 0, 0, L, H);
				BorderT		= Tools.subImage(src, L, 0, W - R - L, H);
				BorderTR 	= Tools.subImage(src, W - R, 0, R, H);
				break;
				
			case IMAGE_STYLE_V_036:
				BorderTL 	= Tools.subImage(src, 0, 0, W, T);
				BorderL 	= Tools.subImage(src, 0, T, W, H - T - B);
				BorderBL 	= Tools.subImage(src, 0, H - B, W, B);
				break;
				
			case IMAGE_STYLE_HLM:
				BorderTL 	= Tools.subImage(src, 0, 0, L, T);
				BorderT 	= Tools.subImage(src, L, 0, W - L, T);
				BorderL 	= Tools.subImage(src, 0, T, L, H - T - B);
				BackImage 	= Tools.subImage(src, L, T, W - L, H - T - B);
				BorderBL 	= Tools.subImage(src, 0, H - B, L, B);
				BorderB 	= Tools.subImage(src, L, H - B, W - L, B);
				break;
			case IMAGE_STYLE_VTM:
				BorderTL 	= Tools.subImage(src, 0, 0, L, T);
				BorderT 	= Tools.subImage(src, L, 0, W - L - R, T);
				BorderTR 	= Tools.subImage(src, W - R, 0, R, T);
				BorderL 	= Tools.subImage(src, 0, T, L, H - T);
				BackImage 	= Tools.subImage(src, L, T, W - L - R, H - T);
				BorderR 	= Tools.subImage(src, W - R, T, R, H - T);
				break;

			case IMAGE_STYLE_BACK_4:
			case IMAGE_STYLE_BACK_4_CENTER:
				BackImage 	= src;
				break;
			}
			Style = style;
			
			setImages(new BufferedImage[] {
							BorderTL, BorderT,   BorderTR, 
							BorderL,  BackImage, BorderR, 
							BorderBL, BorderB,   BorderBR, }, 
							style);
		}
	}
	

	/**
	 * @param images
	 * <pre>
	 * Rect
	 * 
	 * TopLeft----------Top---------TopRight 
	 * |                                   | 
	 * Left         Background         Right 
	 * |                                   | 
	 * BottomLeft-----Bottom-----BottomRight 
	 * 
	 * images[0] images[1] images[2]         
	 * images[3] images[4] images[5]         
	 * images[6] images[7] images[8]         
	 * 
	 * 0,1,2 可以用来横向平铺
	 * 0,3,6 可以用来纵向平铺
	 * 4 可以用来缩放
	 * </pre>
	 */
	private void setImages(BufferedImage[] images, ImageStyle style)
	{
		if (images == null)
			return;

		BorderTL = images[0]; BorderT   = images[1]; BorderTR = images[2];
		BorderL  = images[3]; BackImage = images[4]; BorderR  = images[5];
		BorderBL = images[6]; BorderB   = images[7]; BorderBR = images[8];
		
//		if (images[0] != null)BorderTL	= new TexturePaint(images[0], new Rectangle(images[0].getWidth(), images[0].getHeight()));
//		if (images[1] != null)BorderT	= new TexturePaint(images[1], new Rectangle(images[1].getWidth(), images[1].getHeight()));
//		if (images[2] != null)BorderTR	= new TexturePaint(images[2], new Rectangle(images[2].getWidth(), images[2].getHeight()));//
//		if (images[3] != null)BorderL	= new TexturePaint(images[3], new Rectangle(images[3].getWidth(), images[3].getHeight()));
//		if (images[4] != null)BackImage	= new TexturePaint(images[4], new Rectangle(images[4].getWidth(), images[4].getHeight()));
//		if (images[5] != null)BorderR	= new TexturePaint(images[5], new Rectangle(images[5].getWidth(), images[5].getHeight()));//
//		if (images[6] != null)BorderBL	= new TexturePaint(images[6], new Rectangle(images[6].getWidth(), images[6].getHeight()));
//		if (images[7] != null)BorderB	= new TexturePaint(images[7], new Rectangle(images[7].getWidth(), images[7].getHeight()));
//		if (images[8] != null)BorderBR	= new TexturePaint(images[8], new Rectangle(images[8].getWidth(), images[8].getHeight()));//
		
		
		Style		= style;
	}
	
//	---------------------------------------------------------------------------------------------------------------
	
	public int getTopClip() {
		if (BorderTL != null) {
			return BorderTL.getHeight();
		}
		return 0;
	}

	public int getBottomClip() {
		if (BorderBL != null) {
			return BorderBL.getHeight();
		}
		return 0;
	}
	
	
	public int getLeftClip() {
		if (BorderTL != null) {
			return BorderTL.getWidth();
		}
		return 0;
	}

	public int getRightClip() {
		if (BorderTR != null) {
			return BorderTR.getWidth();
		}
		return 0;
	}
	
	
	public int render(Graphics2D g, int x, int y, int W, int H)
	{
		if (Style == ImageStyle.NULL) return 0;
		
		g.pushPaint();
		g.translate(x, y);
		try {
			switch(Style)
			{
			case COLOR:
				return render0(g, W, H);
			case SPRITE:
				return renderSprite(g, W, H);
			case IMAGE_STYLE_ALL_8:
				return renderAll8(g, W, H);
			case IMAGE_STYLE_ALL_9:
				return renderAll9(g, W, H);
			case IMAGE_STYLE_H_012:
				return renderH012(g, W, H);
			case IMAGE_STYLE_V_036:
				return renderV036(g, W, H);
				
			case IMAGE_STYLE_HLM:
				return renderHLM(g, W, H);
			case IMAGE_STYLE_VTM:
				return renderVTM(g, W, H);
				
			case IMAGE_STYLE_BACK_4:
				return renderBack4(g, W, H);
			case IMAGE_STYLE_BACK_4_CENTER:
				return renderBack4Center(g, W, H);
			default:
				return 0;
			}
		} catch(Exception err){
			return 0;
		} finally {
			g.popPaint();
			g.translate(-x, -y);
		}
	}

	protected int renderSprite(Graphics2D g, int W, int H)
	{
		if (sprite != null)
		{
			sprite.render(g, W/2, H/2);
			sprite.nextCycFrame();
		}
		return 2;
	}
	protected int render0(Graphics2D g, int W, int H) 
	{
		int width  = W - (BorderSize<<1);
		int height = H - (BorderSize<<1);
		
		if ((width > 0) && (height > 0))		{
			g.setColor(BackColor);
			g.fillRect(BorderSize, BorderSize, width, height);
		}
		
		g.setColor(BorderColor);
		g.drawRect(0, 0, W-1, H-1);
		return 2;
	}
	
	protected int render0123_5678(Graphics2D g, int W, int H)
	{
		int time = 0;
		time += Drawing.drawRoundImage(g, BorderL,
					0, 
					BorderTL.getHeight(),
					BorderL.getWidth(), 
					H - BorderTL.getHeight() - BorderBL.getHeight(),
					fillRepeat);
			
		time += Drawing.drawRoundImage(g, BorderR,
					W - BorderR.getWidth(), 
					BorderTR.getHeight(), 
					BorderR.getWidth(), 
					H - BorderTR.getHeight() - BorderBR.getHeight(),
					fillRepeat);
			
		time += Drawing.drawRoundImage(g, BorderT,
					BorderTL.getWidth(), 
					0, 
					W-BorderTL.getWidth()-BorderTR.getWidth(),
					BorderT.getHeight(),
					fillRepeat);
			
		time += Drawing.drawRoundImage(g, BorderB,
					BorderBL.getWidth(), 
					H - BorderB.getHeight(), 
					W - BorderBL.getWidth() - BorderBR.getWidth(), 
					BorderB.getHeight(),
					fillRepeat);

		g.drawImage(BorderTL, 
				0, 0);
		g.drawImage(BorderBL, 0, 
				H - BorderBL.getHeight());
		g.drawImage(BorderTR, 
				W - BorderTR.getWidth(), 0);
		g.drawImage(BorderBR, 
				W - BorderBR.getWidth(),
				H - BorderBR.getHeight());
		time += 4;
		
		return time;
	}
	
	
	protected int renderHLM(Graphics2D g, int W, int H)
	{
		int time = 0;
		int mx = BorderL.getWidth();
		int my = BorderT.getHeight();
		int mw = W - BorderL.getWidth() - BorderL.getWidth();
		int mh = H - BorderT.getHeight() - BorderB.getHeight();
		
		if ( (mw > 0) && (mh > 0))
		{
			time += Drawing.drawRoundImage(g, BackImage, mx, my, mw, mh,
					fillRepeat);
		}
		
		time += render012356(g, W, H);
		return time;
	}
	
	protected int render012356(Graphics2D g, int W, int H)
	{
		int time = 0;
		time += Drawing.drawRoundImage(g, BorderL,
				0, 
				BorderTL.getHeight(),
				BorderL.getWidth(), 
				H - BorderTL.getHeight() - BorderBL.getHeight(),
				fillRepeat);
		
		time += Drawing.drawRoundImage(g, BorderL,
				W - BorderL.getWidth(), 
				BorderTL.getHeight(), 
				BorderL.getWidth(), 
				H - BorderTL.getHeight() - BorderBL.getHeight(), 
				Graphics2D.TRANS_MIRROR,
				fillRepeat
				);
		
		time += Drawing.drawRoundImage(g, BorderT,
				BorderTL.getWidth(), 
				0, 
				W-BorderTL.getWidth()-BorderTL.getWidth(),
				BorderT.getHeight(),
				fillRepeat);
		
		time += Drawing.drawRoundImage(g, BorderB,
				BorderBL.getWidth(), 
				H - BorderB.getHeight(), 
				W - BorderBL.getWidth() - BorderBL.getWidth(), 
				BorderB.getHeight(),
				fillRepeat);


		g.drawImage(BorderTL, 
				0, 0);
		g.drawImage(BorderBL, 0, 
				H - BorderBL.getHeight());
		g.drawImage(BorderTL, 
				W - BorderTL.getWidth(), 0, 
				Graphics2D.TRANS_MIRROR);
		g.drawImage(BorderBL, 
				W - BorderBL.getWidth(),
				H - BorderBL.getHeight(), 
				Graphics2D.TRANS_MIRROR);
		time += 4;
		
		return time;

	}
	
	protected int renderVTM(Graphics2D g, int W, int H)
	{
		int time = 0;
		int mx = BorderL.getWidth();
		int my = BorderT.getHeight();
		int mw = W - BorderL.getWidth() - BorderR.getWidth();
		int mh = H - BorderT.getHeight() - BorderT.getHeight();
		
		if ( (mw > 0) && (mh > 0))
		{
			time += Drawing.drawRoundImage(g, BackImage, mx, my, mw, mh,
					fillRepeat);
		}
		
		time += render012345(g, W, H);
		return time;
	}

	
	protected int render012345(Graphics2D g, int W, int H) 
	{
		int time = 0;
		time += Drawing.drawRoundImage(g, BorderL,
				0, 
				BorderTL.getHeight(),
				BorderL.getWidth(), 
				H - BorderTL.getHeight() - BorderTL.getHeight(),
				fillRepeat);
		
		time += Drawing.drawRoundImage(g, BorderR,
				W - BorderR.getWidth(), 
				BorderTR.getHeight(), 
				BorderR.getWidth(), 
				H - BorderTR.getHeight() - BorderTR.getHeight(),
				fillRepeat);
		
		time += Drawing.drawRoundImage(g, BorderT,
				BorderTL.getWidth(), 
				0, 
				W-BorderTL.getWidth()-BorderTR.getWidth(),
				BorderT.getHeight(),
				fillRepeat);
		
		time += Drawing.drawRoundImage(g, BorderT,
				BorderTL.getWidth(), 
				H - BorderT.getHeight(), 
				W - BorderTL.getWidth() - BorderTR.getWidth(), 
				BorderT.getHeight(), 
				Graphics2D.TRANS_MIRROR_ROT180,
				fillRepeat);

		g.drawImage(BorderTL, 
				0, 0);
		g.drawImage(BorderTL, 0, 
				H - BorderTL.getHeight(),
				Graphics2D.TRANS_MIRROR_ROT180);
		g.drawImage(BorderTR, 
				W - BorderTR.getWidth(), 0);
		g.drawImage(BorderTR, 
				W - BorderTR.getWidth(),
				H - BorderTR.getHeight(),
				Graphics2D.TRANS_MIRROR_ROT180);
		time += 4;
		
		return time;
	}
	
	protected int renderAll8(Graphics2D g, int W, int H)
	{
		int time = 0;
		int mx = BorderL.getWidth();
		int my = BorderT.getHeight();
		int mw = W - BorderL.getWidth() - BorderR.getWidth();
		int mh = H - BorderT.getHeight() - BorderB.getHeight();
		
		if ( (mw > 0) && (mh > 0))
		{
			g.setColor(BackColor);
			g.fillRect(mx, my, mw, mh);
			time += 1;
		}
		
		time += render0123_5678(g, W, H);
		
		return time;
	}
	
	protected int renderAll9(Graphics2D g, int W, int H)
	{
		int time = 0;
		int mx = BorderL.getWidth();
		int my = BorderT.getHeight();
		int mw = W - BorderL.getWidth() - BorderR.getWidth();
		int mh = H - BorderT.getHeight() - BorderB.getHeight();
		
		if ( (mw > 0) && (mh > 0))
		{
			time += Drawing.drawRoundImage(g, BackImage, mx, my, mw, mh,
					fillRepeat);
		}

		time += render0123_5678(g, W, H);
		
		return time;
	}
	
	protected int renderH012(Graphics2D g, int W, int H)
	{
		int time = 0;
		int bl = BorderTL.getWidth();
		int br = BorderTR.getWidth();
		//画血条时可能会用到，当血条长度小于左右块长度�?
		if (bl + br >= W) {
			float clip_scale = W / (float)(bl + br);
			int wl = (int)(bl*clip_scale);
			int wr = (int)(br*clip_scale);
			g.drawImage(BorderTL, 
					0, 0, wl, H);
			g.drawImage(BorderTR, 
					wl, 0, wr, H);
			time += 2;
		} else {
			time += Drawing.drawRoundImageH(g, BorderT, bl, 0, W - bl - br, H,
					fillRepeat);
			g.drawImage(BorderTL,    0, 0, bl, H);
			g.drawImage(BorderTR, W-br, 0, br, H);
			time += 2;
		}
		return time;
	}
	
	protected int renderV036(Graphics2D g, int W, int H)
	{
		int time = 0;
		int bt = BorderTL.getHeight();
		int bb = BorderBL.getHeight();
		//画血条时可能会用到，当血条长度小于左右块长度�?
		if (bt + bb >= H) {
			float clip_scale = H / (float)(bt + bb);
			int ht = (int)(bt*clip_scale);
			int hb = (int)(bb*clip_scale);
			g.drawImage(BorderTL, 
					0, 0, W, ht);
			g.drawImage(BorderBL, 
					0, ht, W, hb);
			time += 2;
		} else {
			time += Drawing.drawRoundImageV(g, BorderL, 0, bt, W, H - bt - bb,
					fillRepeat);
			g.drawImage(BorderTL, 0,    0, W, bt);
			g.drawImage(BorderBL, 0, H-bb, W, bb);
			time += 2;
		}
		return time;
	}
	
	protected int renderBack4(Graphics2D g, int W, int H)
	{
		g.drawImage(BackImage, 0, 0, W, H);
		return 1;
	}
	
	protected int renderBack4Center(Graphics2D g, int W, int H)
	{
		g.drawImage(BackImage, 
				((W - BackImage.getWidth()) >> 1),
				((H - BackImage.getHeight()) >> 1));
		return 1;
	}
	

	//private void drawPaintRect(Graphics2D g, BufferedImage paint, int x, int y, int W, int H) 
	//{
	//	Drawing.drawRoundImage(g, paint, x, y, W, H);
	//}
	
	//private void drawPaintRect(Graphics2D g, BufferedImage paint, int x, int y, int W, int H, int trans) 
	//{
	//	Drawing.drawRoundImage(g, paint, x, y, W, H, trans);
	//}
}
