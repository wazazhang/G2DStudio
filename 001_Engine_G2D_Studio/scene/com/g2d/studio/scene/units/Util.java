package com.g2d.studio.scene.units;

import com.cell.rpg.ability.AbstractAbility;
import com.g2d.Color;
import com.g2d.Graphics2D;
import com.g2d.display.TextTip;
import com.g2d.geom.Rectangle;
import com.g2d.studio.scene.editor.SceneEditor;
import com.g2d.text.TextBuilder;
import com.g2d.util.Drawing;

public class Util 
{

	public static void drawScript(Graphics2D g, SceneEditor editor, SceneUnitTag<?> actor) 
	{
		if (editor.isShowScript()) 
		{
			int count = actor.getUnit().getAbilitiesCount();
			if (count > 0) {
				Rectangle rect = actor.getGameUnit().local_bounds;
				if (editor.getSelectedUnit() == actor) {
					g.setColor(Color.WHITE);
				} else {
					g.setColor(Color.YELLOW);
				}
				Drawing.drawStringBorder(g, count+"能力", 
						rect.x, rect.y, rect.width, rect.height, 
						Drawing.TEXT_ANCHOR_HCENTER | Drawing.TEXT_ANCHOR_TOP);
			}
		}
	}
	
	public static TextTip getTip(SceneEditor editor, SceneUnitTag<?> actor, TextTip tip) {
		if (editor.isShowScript()) {
			tip.backColor = new Color(0f,0f,0f,0.8f);
			tip.text_width = 400;
			StringBuilder sb = new StringBuilder("[color:ffff8000]"+actor.getListName()+"[color] ");
			if (actor instanceof SceneActor) {
				SceneActor sa = (SceneActor)actor;
				sb.append("[color:ff8080ff]"+sa.xls_unit.getName()+"[color]");
			}
			if (actor instanceof SceneImmutable) {
				SceneImmutable sa = (SceneImmutable)actor;
				sb.append("[color:ff8080ff]"+sa.cpj_spr.getName()+"[color]");
			}
			if (actor instanceof SceneEffect) {
				SceneEffect sa = (SceneEffect)actor;
				sb.append("[color:ff8080ff]"+sa.effect.template_effect_id+"[color]");
				sb.append("\n");
				sb.append("[color:ff808080]滚轮提升高度（shift加速）[color]");
			}
			sb.append("\n");
			for (AbstractAbility a : actor.getUnit().getAbilities()) {
				sb.append(" [color:ff00ff00]" + a + "[color]\n");
			}
			tip.setText(TextBuilder.buildScript(sb.toString()));
			return tip;
		}
		return null;
	}
}
