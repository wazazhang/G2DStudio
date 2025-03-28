package com.g2d.display;

import com.g2d.Graphics2D;


/**
 * 用于快速渲染的根节点，
 * 该节点不会Clip任何区域，该节点也不处理任何触碰事件。
 */
public abstract class DisplayObjectLeaf extends DisplayObject
{
	void onUpdate(DisplayObjectContainer parent) 
	{
		super.onUpdate(parent);
	}
	
	final void updateBefore(DisplayObjectContainer parent) {}
	final void updateAfter(DisplayObjectContainer parent) {}
	
	public void onRender(Graphics2D g) 
	{
		if (visible) 
		{
			startFrameRecorder();
			g.pushTransform();
			g.pushComposite();
			try {
				g.translate(x, y);
				this.render(g);
			} finally {
				g.popComposite();
				g.popTransform();
				endFrameRecorder();
			}
		}
	}
	
	protected void renderBefore(Graphics2D g){}
	protected void renderAfter(Graphics2D g) {}
	protected void renderInteractive(Graphics2D g){}
	protected void renderDebug(Graphics2D g) {}
	

}
