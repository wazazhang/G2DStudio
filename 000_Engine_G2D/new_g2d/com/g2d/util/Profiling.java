package com.g2d.util;

import com.g2d.display.DisplayObject;
import com.g2d.display.DisplayObjectContainer;

public class Profiling 
{

	public static int getAllNodeRenderTime(DisplayObject ui)
	{
		int time = ui.getFrameRenderTime();
		if (ui instanceof DisplayObjectContainer) {
			DisplayObjectContainer doc = (DisplayObjectContainer)ui;
			for (DisplayObject o : doc.getChilds()) {
				time += getAllNodeRenderTime(o);
			}
		}
		return time;
	} 
}
