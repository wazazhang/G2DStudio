package com.g2d.studio.ui.edit.gui;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.cell.CMath;
import com.cell.math.TVector;
import com.g2d.Color;
import com.g2d.Graphics2D;
import com.g2d.Tools;
import com.g2d.annotation.Property;
import com.g2d.display.DisplayObject;
import com.g2d.display.event.MouseEvent;
import com.g2d.display.ui.Container;
import com.g2d.display.ui.layout.UILayout;
import com.g2d.geom.Rectangle;
import com.g2d.geom.Rectangle2D;
import com.g2d.studio.ui.edit.UIEditFileNode;
import com.g2d.studio.ui.edit.UITreeNode;

public class UEScrollPan extends Container implements SavedComponent
{
	@Property("边尺寸")
	public int BorderSize = 4;
	@Property("允许纵向滚动")
	public boolean EnableScrollV = false;
	@Property("允许横向滚动")
	public boolean EnableScrollH = true;
	@Property("是否支持弹性")
	public boolean EnableElasticity = true;
	@Property("是否显示滑动条")
	public boolean ShowSlider = true;
	
//	-------------------------------------------------------------------------------
	private PanelContainer container;
	private Rectangle container_bounds = new Rectangle();
	private Rectangle2D.Double scroll_rect 	= new Rectangle2D.Double();
	private Rectangle2D.Double elasticity_rect = new Rectangle2D.Double();
	private Rectangle view_rect = new Rectangle();

	private TVector last_container_pos;
	private TVector last_mouse_down;
	private TVector prewf_mouse_pos;
	private TVector scroll_speed;
	@Property("滚动惯性加速度")
	public float scroll_acc = 0.9f;
	@Property("弹性系数")
	public float elasticity_time = 4f;
	@Property("滚动条渐隐时间")
	public float scroll_fade_time_max = 20;
	private float scroll_fade_time = 0;
	
	private UILayout default_scroll_v;
	private UILayout default_scroll_h;
	
	@Property("纵向滑动条")
	public UILayout layout_scroll_v;;
	@Property("横向滑动条")
	public UILayout layout_scroll_h;;
	
//	---------------------------------------------------------------------------------
	
	public UEScrollPan() 
	{
		default_scroll_v = new UILayout(
				Tools.readImage("/com/g2d/display/ui/res/scrollv-strip.png"), 
				UILayout.ImageStyle.IMAGE_STYLE_V_036, 
				3);
		default_scroll_h = new UILayout(
				Tools.readImage("/com/g2d/display/ui/res/scrollh-strip.png"), 
				UILayout.ImageStyle.IMAGE_STYLE_H_012, 
				3);
		container = new PanelContainer();
		super.addChild(container);
	}
	
	@Override
	public void onRead(UIEditFileNode edit, Element e, Document doc) throws Exception 
	{
		layout_scroll_v = UITreeNode.readLayout(edit, 
				UITreeNode.findXmlChild(e, "layout_scroll_v"));
		layout_scroll_h = UITreeNode.readLayout(edit, 
				UITreeNode.findXmlChild(e, "layout_scroll_h"));
	}
	
	@Override
	public void onWrite(UIEditFileNode edit, Element e, Document doc) throws Exception 
	{
		if (layout_scroll_v != null) {
			Element rect = doc.createElement("layout_scroll_v");
			UITreeNode.writeLayout(edit, layout_scroll_v, rect);
			e.appendChild(rect);
		}
		if (layout_scroll_h != null) {
			Element rect = doc.createElement("layout_scroll_h");
			UITreeNode.writeLayout(edit, layout_scroll_h, rect);
			e.appendChild(rect);
		}
	}


	/** call getContainer().addChild(DisplayObject child); */
	public boolean addChild(DisplayObject child) {
		return container.addChild(child);
	}
	
	/**  call getContainer().removeChild(DisplayObject child); */
	public boolean removeChild(DisplayObject child) {
		return container.removeChild(child);
	}
	
	
//	------------------------------------------------------------------------------------------------------------------------------
	@Override
	protected void onMouseDown(MouseEvent event) {
		super.onMouseDown(event);
		
	}
	@Override
	protected void onMouseUp(MouseEvent event) {
		super.onMouseUp(event);
	}
	
	@Override
	public void update() 
	{		
		scroll_acc 				= Math.min(1, scroll_acc);
		elasticity_time 		= Math.max(1, elasticity_time);
		scroll_fade_time_max	= Math.max(1, scroll_fade_time_max);
	
		if (getRoot().isMouseDown(MouseEvent.BUTTON_LEFT)) {
			if (!edit_mode.isDragEnable(this) && isCatchedMouse()) {
				this.scroll_fade_time = 0;
				this.prewf_mouse_pos = new TVector(
						getMouseX(),
						getMouseY());
				this.scroll_speed = new TVector(0, 0);
				this.last_mouse_down = new TVector(
						getMouseX(),
						getMouseY());
				this.last_container_pos = new TVector(
						scroll_rect.x, 
						scroll_rect.y);
			}
		}
		if (getRoot().isMouseUp(MouseEvent.BUTTON_LEFT)) {
			if (last_mouse_down != null) {
				this.scroll_fade_time = 0;
				last_mouse_down = null;
				last_container_pos = null;
			}
		}
		
		if (last_mouse_down != null) 
		{
			this.scroll_fade_time = 0;
			double dx = last_mouse_down.x - getMouseX();
			double dy = last_mouse_down.y - getMouseY();
			if (EnableScrollH) {
				scroll_rect.x = last_container_pos.x + dx;
			} else {
				scroll_rect.x = 0;
			}
			if (EnableScrollV) {
				scroll_rect.y = last_container_pos.y + dy;
			} else {
				scroll_rect.y = 0;
			}
			scroll_speed.x = prewf_mouse_pos.x - getMouseX();
			scroll_speed.y = prewf_mouse_pos.y - getMouseY();
			prewf_mouse_pos.x = getMouseX();
			prewf_mouse_pos.y = getMouseY();
			
			elasticity_rect = getTargetViewRect();
		}
		else
		{
			if (scroll_speed != null) 
			{
				this.scroll_fade_time = 0;
				if (EnableScrollH) {
					scroll_rect.x += scroll_speed.x;
				}
				if (EnableScrollV) {
					scroll_rect.y += scroll_speed.y;
				}
				scroll_speed.x *= scroll_acc;
				scroll_speed.y *= scroll_acc;

				elasticity_rect = getTargetViewRect();
				if (Math.abs(scroll_speed.x)<0.1 && 
					Math.abs(scroll_speed.y)<0.1) {
					scroll_speed = null;
				}
				if ((EnableScrollH && isScrollInViewX() != 0) ||
					(EnableScrollV && isScrollInViewY() != 0)) {
					scroll_speed = null;
				}
			}
			else if (elasticity_rect != null)
			{
				this.scroll_fade_time = 0;
				double ddx = (elasticity_rect.x - scroll_rect.x);
				double ddy = (elasticity_rect.y - scroll_rect.y);
				if (Math.abs(ddx)<1 && Math.abs(ddy)<1) {
					scroll_rect.x = elasticity_rect.x;
					scroll_rect.y = elasticity_rect.y;
					elasticity_rect = null;
				} else {
					scroll_rect.x += ddx / elasticity_time;
					scroll_rect.y += ddy / elasticity_time;
				}
			}
		}

		if (!EnableElasticity)
		{
			double esx = container_bounds.x;
			double esy = container_bounds.y;
			double edx = container_bounds.x + container_bounds.width  - scroll_rect.width;
			double edy = container_bounds.y + container_bounds.height - scroll_rect.height;
			if (scroll_rect.x < esx) {
				scroll_rect.x = esx;
			}
			if (scroll_rect.y < esy) {
				scroll_rect.y = esy;
			}
			if (scroll_rect.x > edx) {
				scroll_rect.x = edx;
			}
			if (scroll_rect.y > edy) {
				scroll_rect.y = edy;
			}
		}
		super.update();
	}
	
//	------------------------------------------------------------------------------------------------------------------------------
	private int isScrollInViewX()
	{
		if (scroll_rect.x < container_bounds.x) {
			return -1;
		}
		if (scroll_rect.x + scroll_rect.width > container_bounds.x + container_bounds.width) {
			return 1;
		}
		return 0;
	}
	private int isScrollInViewY()
	{
		if (scroll_rect.y < container_bounds.y) {
			return -1;
		}
		if (scroll_rect.y + scroll_rect.height > container_bounds.y + container_bounds.height) {
			return 1;
		}
		return 0;
	}
	
	private Rectangle2D.Double getTargetViewRect()
	{
		Rectangle2D.Double ret = (Rectangle2D.Double) scroll_rect.getBounds2D();
		if (scroll_rect.x < container_bounds.x) {
			ret.x = container_bounds.x;
		}
		else if (scroll_rect.x + scroll_rect.width > container_bounds.x + container_bounds.width) {
			ret.x = container_bounds.x + container_bounds.width - view_rect.width;
		}
		if (scroll_rect.y < container_bounds.y) {
			ret.y = container_bounds.y;
		}
		else if (scroll_rect.y + scroll_rect.height > container_bounds.y + container_bounds.height) {
			ret.y = container_bounds.y + container_bounds.height - view_rect.height;
		}
		return ret;
	}
	
	@Override
	protected void updateChilds() 
	{
		view_rect.x 		= BorderSize;
		view_rect.y 		= BorderSize;
		view_rect.width	 	= getWidth()  - (BorderSize << 1);
		view_rect.height	= getHeight() - (BorderSize << 1);
		
		scroll_rect.width 	= view_rect.width ;
		scroll_rect.height 	= view_rect.height;

		container.x 		= -scroll_rect.x;
		container.y 		= -scroll_rect.y;
		
		super.updateChilds();
	}
	
	@Override
	protected void renderChilds(Graphics2D g) 
	{
		g.pushClip();
		g.clip(view_rect);
		super.renderChilds(g);
		if (ShowSlider) {
			drawSlider(g);
		}
		g.popClip();
		
	}
	
	protected void drawSlider(Graphics2D g)
	{
		if (scroll_fade_time < scroll_fade_time_max) 
		{
			g.pushComposite();
			
			float alpha = 1 - (scroll_fade_time / scroll_fade_time_max);
			g.setColor(Color.WHITE);
			g.setAlpha(alpha);

			UILayout scroll_v = layout_scroll_v != null ? 
					layout_scroll_v : default_scroll_v;
			UILayout scroll_h = layout_scroll_h != null ? 
					layout_scroll_h : default_scroll_h;
			
			if (EnableScrollH && scroll_h != null)
			{
				Rectangle psize = scroll_h.getPreferredSize();
				double th = psize!=null?psize.getHeight():8;
				double tw = view_rect.width;
				double pt1 = CMath.getPercent(
						scroll_rect.x, 
						container_bounds.x, 
						container_bounds.x+container_bounds.width);
				double pt2 = CMath.getPercent(
						scroll_rect.x + scroll_rect.width, 
						container_bounds.x, 
						container_bounds.x+container_bounds.width);
				double ptw = pt2 - pt1;
				scroll_h.render(g, 
						(int)(view_rect.x + pt1*tw), 
						(int)(view_rect.y + view_rect.height - th), 
						(int)(ptw*tw),
						(int)th);
			}
			if (EnableScrollV && scroll_v != null) 
			{
				Rectangle psize = scroll_v.getPreferredSize();
				double tw = psize!=null?psize.getWidth():8;
				double th = view_rect.height;
				double pt1 = CMath.getPercent(
						scroll_rect.y, 
						container_bounds.y, 
						container_bounds.y+container_bounds.height);
				double pt2 = CMath.getPercent(
						scroll_rect.y + scroll_rect.height, 
						container_bounds.y, 
						container_bounds.y+container_bounds.height);
				double pth = pt2 - pt1;
				scroll_v.render(g, 
						(int)(view_rect.x + view_rect.width - tw), 
						(int)(view_rect.y + pt1 * th), 
						(int)tw,
						(int)(pth*th));
			}
			g.popComposite();
			scroll_fade_time += 1;
		}
		
	}
	
	class PanelContainer extends Container
	{
		
		
		public PanelContainer() 
		{
			this.clip_local_bounds = false;
			this.enable_focus = false;
			this.enable_drag = false;
			setCustomLayout(UILayout.createBlankRect());
			setLayout(UILayout.createBlankRect());
		}
		
		@Override
		public void update()
		{
			super.update();
			int sx = 0;
			int sy = 0;
			int dx = 0;
			int dy = 0;
			for (DisplayObject d : getChilds()) {
				sx = (int)Math.min(sx, d.x);
				sy = (int)Math.min(sy, d.y);
				dx = (int)Math.max(dx, d.x + d.getWidth());
				dy = (int)Math.max(dy, d.y + d.getHeight());
			}
			this.local_bounds.setBounds(sx, sy, 
					Math.max(dx - sx, view_rect.width), 
					Math.max(dy - sy, view_rect.height));
			
			container_bounds = local_bounds.getBounds();
		}
		
		@Override
		public void render(Graphics2D g) {
			
			super.render(g);
			
		}
	}
}
