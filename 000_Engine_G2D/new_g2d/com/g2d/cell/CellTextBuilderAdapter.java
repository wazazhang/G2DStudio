package com.g2d.cell;


import com.cell.CUtil;
import com.cell.gameedit.SetResource;
import com.cell.gfx.game.CSprite;
import com.g2d.Tools;
import com.g2d.font.GraphicAttribute;
import com.g2d.font.GraphicsAttributeImage;
import com.g2d.font.GraphicsAttributeSprite;
import com.g2d.font.TextBuilderAdapter;
import com.g2d.text.Instruction;

public class CellTextBuilderAdapter implements TextBuilderAdapter
{
	final protected CellSetResourceManager res_manager;
	
	public CellTextBuilderAdapter(CellSetResourceManager res_manager) {
		this.res_manager = res_manager;
	}
	
	@Override
	public GraphicAttribute createGraphicAttribute(
			Instruction instruction,
			String key, 
			String value) 
	{
		try {
			switch (instruction) {
			case IMAGE: {
				return new GraphicsAttributeImage(Tools.readImage(value));
			}
			case SPRITE: {
				String[] ss = CUtil.splitString(value, "@");
				SetResource res = res_manager.getSet(ss[0]);
				CSprite sprite = res.getSprite(ss[1]);
				int animate = Integer.parseInt(ss[2]);
				return new GraphicsAttributeSprite(sprite, animate);
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
