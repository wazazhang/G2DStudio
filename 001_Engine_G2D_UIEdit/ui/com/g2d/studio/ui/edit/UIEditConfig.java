package com.g2d.studio.ui.edit;

import com.cell.util.Config;
import com.cell.util.anno.ConfigField;
import com.cell.util.anno.ConfigSeparator;

public class UIEditConfig extends Config
{
	@ConfigField("默认字体")
	public static String 	UI_DEFAULT_FONT 		= null;
	@ConfigField("启用默认字体后，是否锁定所有字体")
	public static boolean 	UI_FONT_FAMILY_FIXED	= false;
	@ConfigField("默认字体颜色")
	public static int UI_DEFAULT_TEXT_COLOR = 0xffffffff;
	@ConfigField("默认字体颜色(焦点)")
	public static int UI_DEFAULT_TEXT_COLOR_FOCUS = 0xffffffff;
	
//	----------------------------------------------------------
	@ConfigSeparator("默认样式")
//	----------------------------------------------------------

	////////////////////////////////////////////////////////////////////////////////////
	@ConfigField("默认样式-根节点")
	public static String UI_DEFAULT_LAYOUT_ROOT = 
		"/com/g2d/studio/ui/edit/res/form.png, " +
		"IMAGE_STYLE_ALL_9, 40";

	////////////////////////////////////////////////////////////////////////////////////
	@ConfigField("默认样式-按钮UP")
	public static String UI_DEFAULT_LAYOUT_BUTTON_U = 
		"/com/g2d/studio/ui/edit/res/btn1-u.png, " +
		"IMAGE_STYLE_ALL_9, 10";

	@ConfigField("默认样式-按钮DOWN")
	public static String UI_DEFAULT_LAYOUT_BUTTON_D = 
		"/com/g2d/studio/ui/edit/res/btn1-d.png, " +
		"IMAGE_STYLE_ALL_9, 10";
	
	////////////////////////////////////////////////////////////////////////////////////
	@ConfigField("默认样式-进度条底")
	public static String UI_DEFAULT_LAYOUT_GAUGE_D = 
		"/com/g2d/studio/ui/edit/res/button1-d.png, " +
		"IMAGE_STYLE_ALL_9, 10";

	@ConfigField("默认样式-进度条上层")
	public static String UI_DEFAULT_LAYOUT_GAUGE_U = 
		"/com/g2d/studio/ui/edit/res/button1-u.png, " +
		"IMAGE_STYLE_ALL_9, 10";
	////////////////////////////////////////////////////////////////////////////////////
	@ConfigField("默认样式-CheckBox")
	public static String UI_DEFAULT_LAYOUT_CHECKBOX= 
			"/com/g2d/studio/ui/edit/res/textbox.png, " +
					"IMAGE_STYLE_ALL_9, 10";
	@ConfigField("默认样式-CheckBox checkimage")
	public static String UI_DEFAULT_IMAGE_CHECKBOX_CHECKED= 
			"/com/g2d/studio/ui/edit/res/checkbox-checkimage.png";
	@ConfigField("默认样式-CheckBox uncheckimage")
	public static String UI_DEFAULT_IMAGE_CHECKBOX_UNCHECKED= 
			"/com/g2d/studio/ui/edit/res/checkbox-uncheckimage.png";
	////////////////////////////////////////////////////////////////////////////////////
	
	@ConfigField("默认样式-ImageBox")
	public static String UI_DEFAULT_LAYOUT_IMAGEBOX= ", NULL, 10";

	////////////////////////////////////////////////////////////////////////////////////
	@ConfigField("默认样式-Label")
	public static String UI_DEFAULT_LAYOUT_LABEL= ", NULL, 10";

	////////////////////////////////////////////////////////////////////////////////////
	@ConfigField("默认样式-Canvas")
	public static String UI_DEFAULT_LAYOUT_CANVAS= 
		"/com/g2d/studio/ui/edit/res/panel.png, " +
		"IMAGE_STYLE_ALL_9, 10";

	////////////////////////////////////////////////////////////////////////////////////
	@ConfigField("默认样式-TextInput")
	public static String UI_DEFAULT_LAYOUT_TEXTINPUT= 
		"/com/g2d/studio/ui/edit/res/textbox.png, " +
		"IMAGE_STYLE_ALL_9, 10";

	@ConfigField("默认样式-TextBox")
	public static String UI_DEFAULT_LAYOUT_TEXTBOX= 
		"/com/g2d/studio/ui/edit/res/textbox.png, " +
		"IMAGE_STYLE_ALL_9, 10";

	////////////////////////////////////////////////////////////////////////////////////
	@ConfigField("默认样式-ScrollPan")
	public static String UI_DEFAULT_LAYOUT_SCROLLPAN = 
			"/com/g2d/studio/ui/edit/res/textbox.png, " +
					"IMAGE_STYLE_ALL_9, 10";

	////////////////////////////////////////////////////////////////////////////////////
//	----------------------------------------------------------
	@ConfigSeparator("G2D资源")
//	----------------------------------------------------------

	@ConfigField("G2D目录[project.g2d.save]位置")
	public static String G2D_SAVE_PATH = "";
	
	@ConfigField("G2D目录[effects]位置")
	public static String G2D_EFFECTS_PATH = "";

//	----------------------------------------------------------
	@ConfigSeparator("扩展")
//	----------------------------------------------------------
	@ConfigField("文本工具相对路径")
	public static String EXT_TEXT_STYLE_TOOLS_PATH = "";
	
	public static void load(String arg) {
		Config.load(UIEditConfig.class, arg);
	}
}
