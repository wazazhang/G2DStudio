package com.g2d.display.ui;


import com.g2d.display.DisplayObject;
import com.g2d.display.event.MouseWheelEvent;
import com.g2d.display.ui.layout.UILayout;
import com.g2d.geom.Point2D;
import com.g2d.geom.Rectangle;

public class Panel extends UIComponent
{	
	public static int DEFAULT_SCROLL_BAR_SIZE = 12;
	
	ScrollBar.ScrollBarPair		scrollbar;
	
	/** 实际的容器 */
	protected PanelContainer	container;
	/** 视口 */
	protected Pan 				view_port;
	
	private Point2D.Double		last_mouse_right_down_mouse;
	private Point2D.Double		last_mouse_right_down_pos;
	
//	------------------------------------------------------------------------------------------------------------------------------
	public boolean EnableMouseWheelScroll = true;
	
	public boolean EnableMouseRightScroll = false;

	public Panel() 
	{
		enable_mouse_wheel = true;
		
		view_port 	= new Pan();
		container 	= new PanelContainer();
		scrollbar	= new ScrollBar.ScrollBarPair(DEFAULT_SCROLL_BAR_SIZE);

		super.addChild(scrollbar.v_scroll);		
		super.addChild(scrollbar.h_scroll);
		
		view_port.setCustomLayout(UILayout.createBlankRect());
		view_port.setLayout(UILayout.createBlankRect());
		view_port.enable_input = false;
		view_port.addChild(container);
		
		super.addChild(view_port);
	}
	
	/** call getContainer().addChild(DisplayObject child); */
	@Deprecated
	public boolean addChild(DisplayObject child) {
		throw new IllegalStateException("can not add a custom child component in " + getClass().getName() + " !");
	}
	/**  call getContainer().removeChild(DisplayObject child); */
	@Deprecated
	public boolean removeChild(DisplayObject child) {
		throw new IllegalStateException("can not remove a custom child component in " + getClass().getName() + " !");
	}
	
	public Container getContainer() {
		return container;
	}
	
	public void setContainer(PanelContainer container) {
		view_port.removeChild(this.container);
		this.container = container;
		view_port.addChild(this.container);
	}
	
	public boolean addContainerComponent(UIComponent o) {
		return this.container.addComponent(o);
	}

	public boolean removeContainerComponent(UIComponent o) {
		return this.container.removeComponent(o);
	}
//	------------------------------------------------------------------------------------------------------------------------------
	
	
	protected void onMouseWheelMoved(MouseWheelEvent event) {
		if (EnableMouseWheelScroll) {
			scrollbar.v_scroll.moveInterval(event.scrollDirection);
		}
	}

	public ScrollBar getVScrollBar() {
		return scrollbar.v_scroll;
	}
	public ScrollBar getHScrollBar() {
		return scrollbar.h_scroll;
	}
	
	public int getViewPortWidth() {
		return view_port.getWidth();
	}
	
	public int getViewPortHeight() {
		return view_port.getHeight();
	}
	
	public void setAutoScroll(boolean hScroll, boolean vbScroll) {
		setEnableHScrollBar(hScroll);
		setEnableVScrollBar(vbScroll);
	}
	
	public void setEnableVScrollBar(boolean enable) {
		if (enable) {
			if (super.contains(scrollbar.v_scroll)==false) {
				super.addChild(scrollbar.v_scroll);
			}
		} else {
			if (super.contains(scrollbar.v_scroll)==true) {
				super.removeChild(scrollbar.v_scroll);
			}
		}
		scrollbar.v_scroll.enable = enable;
		enable_mouse_wheel = enable;
	}
	
	public void setEnableHScrollBar(boolean enable) {
		if (enable) {
			if (super.contains(scrollbar.h_scroll)==false) {
				super.addChild(scrollbar.h_scroll);
			}
		} else {
			if (super.contains(scrollbar.h_scroll)==true) {
				super.removeChild(scrollbar.h_scroll);
			}
		}
		scrollbar.h_scroll.enable = enable;
	}
	
//	Rectangle view_rect ;
	
	@Override
	public void update() 
	{
		super.update();

	}
	
	@Override
	protected void updateChilds() 
	{
		if (EnableMouseRightScroll) {
			if (isCatchedMouse() && getRoot().isMouseDown(com.g2d.display.event.MouseEvent.BUTTON_RIGHT)) {
				last_mouse_right_down_mouse = new Point2D.Double(
						getMouseX(), 
						getMouseY());
				last_mouse_right_down_pos = new Point2D.Double(
						scrollbar.h_scroll.getValue(),
						scrollbar.v_scroll.getValue());
			}
			else if (getRoot().isMouseHold(com.g2d.display.event.MouseEvent.BUTTON_RIGHT)) {
				if (last_mouse_right_down_mouse != null) {
					double dx = last_mouse_right_down_mouse.x - getMouseX();
					double dy = last_mouse_right_down_mouse.y - getMouseY();
					scrollbar.h_scroll.setValue(last_mouse_right_down_pos.x + dx); 
					scrollbar.v_scroll.setValue(last_mouse_right_down_pos.y + dy); 
				}
			}
			else if (getRoot().isMouseUp(com.g2d.display.event.MouseEvent.BUTTON_RIGHT)) {
				last_mouse_right_down_mouse = null;
			}
		} else {
			last_mouse_right_down_mouse = null;
		}
		UILayout ly = getLayout();
		int sx = ly.BorderSize;
		int sy = ly.BorderSize;
		int sw = getWidth() -(ly.BorderSize<<1);
		int sh = getHeight()-(ly.BorderSize<<1);
		
		Rectangle view_rect = scrollbar.update(this, sx, sy, sw, sh, 
				view_port.getWidth(),  container.local_bounds.width,
				view_port.getHeight(), container.local_bounds.height);
		
		view_port.setBounds(view_rect);
		
		container.setLocation(
				-(int)scrollbar.h_scroll.getValue(), 
				-(int)scrollbar.v_scroll.getValue());
		
		super.updateChilds();
	}
	
//	@Override
//	protected void renderAfter(Graphics2D g) {
//		super.renderAfter(g);
//		if (view_rect!=null) {
//			g.setColor(Color.GREEN);
//			g.draw(view_rect);
//		}
//	}

	public class PanelContainer extends Container
	{
		
		public PanelContainer() 
		{
			this.enable_input = false;
		}
		
		public void update() 
		{
			local_bounds.width = view_port.getWidth();
			local_bounds.height = view_port.getHeight();
			for (UIComponent item : getComonents()) {	
				local_bounds.width  = (int)Math.max(local_bounds.width,  item.x + item.getWidth());
				local_bounds.height = (int)Math.max(local_bounds.height, item.y + item.getHeight());
			}
		}
		
	}
	
}
