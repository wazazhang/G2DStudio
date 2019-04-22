package com.g2d.studio.ui.edit.gui;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.g2d.Color;
import com.g2d.Font;
import com.g2d.Graphics2D;
import com.g2d.Image;
import com.g2d.Tools;
import com.g2d.annotation.Property;
import com.g2d.annotation.TextProperty;
import com.g2d.display.ui.Guage;
import com.g2d.display.ui.UIComponent;
import com.g2d.display.ui.layout.UILayout;
import com.g2d.studio.ui.edit.UIEditConfig;
import com.g2d.studio.ui.edit.UIEditFileNode;
import com.g2d.studio.ui.edit.UITreeNode;
import com.g2d.studio.ui.edit.gui.base.BaseTextComponent;
import com.g2d.util.Drawing;
import com.g2d.util.Drawing.TextAnchor;

public class UEGauge extends BaseTextComponent implements SavedComponent
{
	@Property("自定控件样式-上层")
	public UILayout custom_layout_up;
	protected UILayout	layout_up;

	@Property("max")
	public double gaugeMax;
	@Property("min")
	public double gaugeMin;
	@Property("value")
	public double gaugeValue;

	@Property("显示为百分比")
	public boolean showPercent = true;

	@Property("上层显示方向")
	public RenderOrientation render_orientation = RenderOrientation.LEFT_2_RIGHT;
	
	public UEGauge() {
		this.gaugeMax = 100;
		this.gaugeMin = 0;
		this.gaugeValue = 50;
		this.textColor = new Color(UIEditConfig.UI_DEFAULT_TEXT_COLOR);
	}

	public void setLayout(UILayout down, UILayout up) {
		this.layout = down;
		this.layout_up = up;
	}
	
	public double getMax() {
		return gaugeMax;
	}

	public double getMin() {
		return gaugeMin;
	}

	public double getValue() {
		return gaugeValue;
	}

	public void setRange(double min, double max) {
		this.gaugeMax = Math.max(max, min);
		this.gaugeMin = Math.min(max, min);
		this.gaugeValue = Math.max(gaugeValue, min);
		this.gaugeValue = Math.min(gaugeValue, max);
	}

	public void setMax(double max) {
		this.gaugeMax = max;
		this.gaugeValue = Math.min(gaugeValue, max);
	}

	public void setValue(double value) {
		value = Math.max(value, gaugeMin);
		value = Math.min(value, gaugeMax);
		this.gaugeValue = value;
	}

	public double getPercent() {
		double sw = (gaugeMax - gaugeMin);
		double sx = (gaugeValue - gaugeMin);
		return (int) (sx / sw * 100);
	}

	public void render(Graphics2D g) 
	{
		setRange(gaugeMin, gaugeMax);
		
		super.render(g);
		
		UILayout up = custom_layout_up != null? custom_layout_up : layout_up;
		if (up != null)
		{
			int sx = 0;
			int sy = 0;
			int sw = (int)(getWidth() * getPercent()/100f);
			int sh = (int)(getHeight());
			switch(render_orientation)
			{
			case LEFT_2_RIGHT:
				break;
			case RIGTH_2_LEFT:
				sx = getWidth() - sw;
				break;
			case TOP_2_BOTTOM:
				sw = (int)(getWidth());
				sh = (int)(getHeight() * getPercent()/100f);
				break;
			case BOTTOM_2_TOP:
				sw = (int)(getWidth());
				sh = (int)(getHeight() * getPercent()/100f);
				sy = getHeight() - sh;
				break;
			}
			up.render(g, sx, sy, sw, sh);
		}
		super.renderText(g, showPercent?((int)getPercent())+"%":text);
	}


	
	@Override
	public void onRead(UIEditFileNode edit, Element e, Document doc) throws Exception {
		custom_layout_up = UITreeNode.readLayout(edit, 
				UITreeNode.findXmlChild(e, "custom_layout_up"));
	}

	@Override
	public void onWrite(UIEditFileNode edit, Element e, Document doc) throws Exception {
		if (custom_layout_up != null) {
			Element rect = doc.createElement("custom_layout_up");
			UITreeNode.writeLayout(edit, custom_layout_up, rect);
			e.appendChild(rect);
		}
	}
	
	public static enum RenderOrientation
	{
		LEFT_2_RIGHT,
		RIGTH_2_LEFT,
		TOP_2_BOTTOM,
		BOTTOM_2_TOP,
	}
}
