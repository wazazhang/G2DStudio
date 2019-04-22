package com.g2d.cell;

import java.io.ByteArrayInputStream;

import com.cell.CMath;
import com.cell.gameedit.StreamTiles;
import com.cell.gameedit.object.ImagesSet;
import com.cell.gfx.IImage;
import com.cell.gfx.IPalette;
import com.g2d.Engine;
import com.g2d.Image;

public class CellStreamTiles extends StreamTiles
{
	public CellStreamTiles(ImagesSet img, CellSetResource res) {
		super(img, res);
	}

	/***
	 * 是否单独输出每张图
	 * @return
	 */
	public boolean isTile() {
		return set.getOutput().isTile();
	}
	
	/**
	 * 是否输出整图
	 * @return
	 */
	public boolean isGroup() {
		return set.getOutput().isGroup();
	}
	
	/**
	 * 获得导出图片文件类型
	 * @return
	 */
	public String getImageExtentions() {
		return set.getOutput().getImageExtentions();
	}
	
	@Override
	protected void initImages() throws Throwable
	{
		// 导出图片格式为整图
		if (!img.IsTiles && img.SplitSize > 0) 
		{
			int m_uTotalW = (int)CMath.ccNextPOT(img.TotalW);
			int m_uTotalH = (int)CMath.ccNextPOT(img.TotalH);
			int SplitSize = img.SplitSize;
			int PartCountX = m_uTotalW / SplitSize;
			int PartCountY = m_uTotalH / SplitSize;
			IImage[][] imgs = new IImage[PartCountX][PartCountY];
			for (int x = 0; x < PartCountX; x++) {
				for (int y = 0; y < PartCountY; y++) {
					byte[] idata = set.getOutput().loadRes(
							img.getName() + "_" + x + "_" + y + "."	+ img.Extention, null);
					if (idata != null) {
						imgs[x][y] = Engine.getEngine().createImage(new ByteArrayInputStream(idata));
					}
				}
			}
			for (int i = 0; i < images.length; i++) {
				if (img.getClipW(i) > 0 && img.getClipH(i) > 0) {
					int px = img.getClipX(i) / SplitSize;
					int py = img.getClipY(i) / SplitSize;
					IImage src = imgs[px][py];
					images[i] = src.subImage(
							img.getClipX(i) % SplitSize, 
							img.getClipY(i) % SplitSize, 
							img.getClipW(i), 
							img.getClipH(i));
				}
			}
		}
		else if (!img.IsTiles)
		{
//			System.out.println("initImages group : " + img.getName());
			byte[] idata = set.getOutput().loadRes(
					img.getName() + "." + img.Extention, null);
			if (idata != null)
			{
				Image src = Engine.getEngine().createImage(new ByteArrayInputStream(idata));
				IPalette palette = this.getPalette();
				if (palette != null) {
					src.setPalette(palette);
				}
				for (int i = 0; i < images.length; i++) {
					if (img.getClipW(i) > 0 && img.getClipH(i) > 0) {
						images[i] = src.subImage(
								img.getClipX(i), 
								img.getClipY(i), 
								img.getClipW(i), 
								img.getClipH(i));
					}
				}
			}
		}
		// 导出图片格式为碎图
		else
		{
//			System.out.println("initImages tiles : " + img.getName());
			IPalette palette = this.getPalette();
			for (int i = 0; i < images.length; i++) {
				if (img.getClipW(i) > 0 && img.getClipH(i) > 0) {
					byte[] idata = set.getOutput().loadRes(
							img.getName() + "/" + i + "." + img.Extention, null);
					images[i] = Engine.getEngine().createImage(new ByteArrayInputStream(idata));
					if (palette != null) {
						images[i].setPalette(palette);
					}
				}
			}
		}
	}
}
