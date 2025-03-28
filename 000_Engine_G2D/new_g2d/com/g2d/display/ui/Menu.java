package com.g2d.display.ui;


import com.g2d.Color;
import com.g2d.Font;
import com.g2d.Graphics2D;
import com.g2d.display.DisplayObjectContainer;
import com.g2d.display.event.MouseEvent;
import com.g2d.display.event.MouseListener;
import com.g2d.display.ui.layout.UILayout;
import com.g2d.display.ui.layout.UILayoutManager;
import com.g2d.util.Drawing;

public class Menu extends Window implements MouseListener
{
	int border_size	= 1;

//	-------------------------------------------------------------------------------------------------------------

	public Menu() {
		this(200, new Object[0]);
	}

	public Menu(int width) {
		this(width, new Object[0]);
	}

	public Menu(Object[] objects) {
		this(200, objects);
	}
	
	public Menu(int width, Object[] objects) 
	{
		this.setSize(width, 1);
		
		UILayoutManager.getInstance().setLayout(this);
		for (Object obj : objects) {
			MenuItemButton item = new MenuItemButton("" + obj);
			item.setUserData(obj);
			addMenuItem(item);
			UILayoutManager.getInstance().setLayout(item);
		}
		
		enable_drag = false;
		enable_drag_resize = false;
		this.setTransition(new SimpleAlphaTransition(5));
	}
	
	public Menu(int width, UILayout broad, UILayout button, Object[] objects)
	{
		this.setSize(width, 1);
		
		this.setCustomLayout(broad);
		for (Object obj : objects) {
			MenuItemButton item = new MenuItemButton("" + obj);
			item.setUserData(obj);
			addMenuItem(item);
			item.setCustomLayout(button);
		}
		
		enable_drag = false;
		enable_drag_resize = false;
		this.setTransition(new SimpleAlphaTransition(5));
	}

//	-------------------------------------------------------------------------------------------------------------
	
	public void addMenuItem(MenuItem item)
	{
		UIComponent comp = item.toComponent();
		
		comp.setLocation(border_size, getHeight());
		comp.setSize(getWidth()-(border_size<<1), item.getItemHeight());
		
		this.setSize(getWidth(), getHeight() + item.getItemHeight() + border_size);
		
		this.addComponent(comp);
		
		comp.addEventListener(this);
	}

//	-------------------------------------------------------------------------------------------------------------
	
	public void mouseClick(MouseEvent e) {}
	public void mouseDown(MouseEvent e) {}
	public void mouseUp(MouseEvent e) {
		if (e.source.enable) {
			this.close();
			onClickMenuItem(e, (MenuItem)e.source);
		}
	}
	
	protected void onClickMenuItem(MouseEvent e, MenuItem item)
	{
		System.out.println(item);
	}

//	-------------------------------------------------------------------------------------------------------------
	
	@Override
	public void update() {
		if (getRoot().isMouseDown(MouseEvent.BUTTON_LEFT, MouseEvent.BUTTON_RIGHT)) {
			if (!isCatchedMouse()) {
				this.close();
			}
		}
		super.update();
	}
	
	public void open(DisplayObjectContainer screen) 
	{
		super.open(screen);
		
		if (x + getWidth() > screen.getWidth()) {
			setLocation(x - getWidth(), y);
		}
		if (y + getHeight() > screen.getHeight()) {
			setLocation(x, y - getHeight());
		}
	}
	
	public void show(DisplayObjectContainer screen, int x, int y) {
		this.setLocation(x, y);
		open(screen);
	}
	
	public void show(DisplayObjectContainer screen) {
		this.setLocation(screen.getMouseX(), screen.getMouseY());
		open(screen);
	}
	

//	-------------------------------------------------------------------------------------------------------------
	

	public static interface MenuItem
	{
		public int getItemHeight();
		
		public UIComponent toComponent();

		public Object getUserData();

	}
	
	public static class MenuItemButton extends BaseButton implements MenuItem
	{	
		public Color 				text_color = new Color(0xffffffff);
		public String 				text = "";

		/**文字是否抗锯齿*/
		public boolean	enable_antialiasing	 = false;
		
		public String text_font;
		public int text_anchor = Drawing.TEXT_ANCHOR_HCENTER | Drawing.TEXT_ANCHOR_VCENTER;
		
		public MenuItemButton(String text){
			this.text = text;
		}
		
		public int getItemHeight() {
			return 25;
		}
		public UIComponent toComponent() {
			return this;
		}
		
		@SuppressWarnings("unchecked")
		public Object getUserData(){
			return super.getUserData();
		}
		
		@Override
		public void render(Graphics2D g) {
			super.render(g);
			Font oldf = g.getFont();
			try {
				if (text_font != null) {
					g.setFont(text_font);
				}
				g.setColor(text_color);
				if (enable_antialiasing) {
					boolean flag = g.setFontAntialiasing(enable_antialiasing);
					Drawing.drawStringBorder(g, text, 0, 0, getWidth(), getHeight(), text_anchor);
					g.setFontAntialiasing(flag);
				} else {
					Drawing.drawStringBorder(g, text, 0, 0, getWidth(), getHeight(), text_anchor);
				}
			} finally {
				g.setFont(oldf);
			}
		}
		
		
	}
	
}
