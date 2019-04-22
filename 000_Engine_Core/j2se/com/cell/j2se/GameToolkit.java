package com.cell.j2se;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class GameToolkit 
{

	public static Cursor createCustomCursor(Image cursor, Point hotSpot, String name, ImageObserver ob) {
		Dimension bestCursorSize = Toolkit.getDefaultToolkit().getBestCursorSize(cursor.getWidth(ob), cursor.getHeight(ob));
		BufferedImage bufferedImage = new BufferedImage(bestCursorSize.width, bestCursorSize.height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < bestCursorSize.width; x++)
			for (int y = 0; y < bestCursorSize.height; y++)
				bufferedImage.setRGB(x, y, 0);
		bufferedImage.getGraphics().drawImage(cursor, 0, 0, ob);
		return Toolkit.getDefaultToolkit().createCustomCursor(bufferedImage, hotSpot, name);
	}

//	static public class AppBridgeImpl extends CAppBridge
//	{
//		private IGame Game;
//		
//		public AppBridgeImpl(IGame game, ClassLoader classloader, Class<?> rootclass)
//		{
//			super(classloader, rootclass);
//			Game = game;
//		}
//	    
//	}

}
