package com.g2d.display.event;


public interface MouseMoveListener extends EventListener
{
	public void mouseDragged(MouseMoveEvent e);

	public void mouseDraggedEnd(MouseMoveEvent e);
}
