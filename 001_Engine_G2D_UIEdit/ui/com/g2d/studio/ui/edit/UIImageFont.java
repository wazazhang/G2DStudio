package com.g2d.studio.ui.edit;

import java.io.ObjectStreamException;
import java.io.Serializable;

import com.cell.CUtil;
import com.cell.gameedit.StreamTiles;
import com.cell.gfx.IImage;
import com.cell.reflect.IObjectStringParser;
import com.cell.reflect.Parser;
import com.g2d.BufferedImage;
import com.g2d.Graphics2D;
import com.g2d.awt.util.celledit.CellTilesResource;
import com.g2d.studio.ui.edit.UILayoutManager.UIEditImageAtlas;
import com.g2d.util.Drawing.TextAnchor;

public class UIImageFont implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public String 	cpj_fileName;
	public String 	cpj_imageName;
	
	private boolean need_update = true;
	private CellTilesResource res = null;
	private StreamTiles tiles;
	
	public UIImageFont(
			String cpj_fileName, 
			String cpj_imageName) 
	{
		this.cpj_fileName 	= cpj_fileName;
		this.cpj_imageName 	= cpj_imageName;
	}
	public UIImageFont() {}
	@Override
	public String toString() {
		return Parser.objectToString(this);
	}
	final protected Object writeReplace() throws ObjectStreamException {
		return this;
	}
	final protected Object readResolve() throws ObjectStreamException {
		need_update = true;
		return this;
	}
	
	public boolean render(
			Graphics2D g,
			String text, 
			float x, float y, float w, float h, 
			TextAnchor text_anchor)
	{
		if (need_update || res == null) {
			need_update = false;	
			res = UIEdit.getLayoutManager().getCpjRes(cpj_fileName, res);
			if (res != null)
			{
				tiles = res.getImages(cpj_imageName);
			}
		}
		if (tiles != null) {
			g.pushTransform();
			g.translate(x, y);
			try {
				int totalW = 0;
				int totalH = 0;
				IImage[] ts = new IImage[text.length()];
				for (int i = text.length() - 1; i >= 0; --i) {
					ts[i] = tiles.getImageWithKey(text.charAt(i) + "");
					if (ts[i] != null) {
						totalW += ts[i].getWidth();
						totalH = Math.max(totalH, ts[i].getHeight());
					}
				}
				
				switch (text_anchor) {
				case L_T:
					break;
				case C_T:
					g.translate(w / 2 - totalW / 2, 0);
					break;
				case R_T:
					g.translate(w - totalW, 0);
					break;
				case L_C:
					g.translate(0, h / 2 - totalH / 2);
					break;
				case C_C:
					g.translate(w / 2 - totalW / 2, h / 2 - totalH / 2);
					break;
				case R_C:
					g.translate(w - totalW, h / 2 - totalH / 2);
					break;
				case L_B:
					g.translate(0, h - totalH);
					break;
				case C_B:
					g.translate(w / 2 - totalW / 2, h - totalH);
					break;
				case R_B:
					g.translate(w - totalW, h - totalH);
					break;
				}
				
				int sx = 0;
				for (int i = 0; i < ts.length; i++) {
					if (ts[i] != null) {
						g.drawImage(ts[i], sx, totalH - ts[i].getHeight(), 0);
						sx += ts[i].getWidth();
					}
				}
			} finally {
				g.popTransform();
			}
			return true;
		}
		return false;
	}
	
	public static class UIImageFontParser implements IObjectStringParser
	{
		@Override
		public Object parseFrom(String str, Class<?> return_type) {
			while(str.startsWith("^")) {
				str = str.substring(1);
			}
			String[] kv = CUtil.splitString(str, "|");
			if (kv.length >= 2) {
				UIImageFont ret = new UIImageFont();
				ret.cpj_fileName	= kv[0];
				ret.cpj_imageName	= kv[1];
				return ret;
			}
			return null;
		}
		@Override
		public String toString(Object obj) {
			UIImageFont ua = (UIImageFont)obj;
			return "^" + ua.cpj_fileName + "|" + ua.cpj_imageName;
		}
	}
	
}
