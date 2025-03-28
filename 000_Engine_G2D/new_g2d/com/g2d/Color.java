package com.g2d;

import java.io.Serializable;

public class Color implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public final static Color white     	= new Color(255, 255, 255);
    public final static Color WHITE			= white;
    public final static Color lightGray		= new Color(192, 192, 192);
    public final static Color LIGHT_GRAY 	= lightGray;
    public final static Color gray      	= new Color(128, 128, 128);
    public final static Color GRAY 			= gray;
    public final static Color darkGray  	= new Color(64, 64, 64);
    public final static Color DARK_GRAY 	= darkGray;
    public final static Color black 		= new Color(0, 0, 0);
    public final static Color BLACK 		= black;
    public final static Color red     		= new Color(255, 0, 0);
    public final static Color RED 			= red;
    public final static Color pink			= new Color(255, 175, 175);
    public final static Color PINK 			= pink;
    public final static Color orange 		= new Color(255, 200, 0);
    public final static Color ORANGE 		= orange;
    public final static Color yellow 		= new Color(255, 255, 0);
    public final static Color YELLOW 		= yellow;
    public final static Color green 		= new Color(0, 255, 0);
    public final static Color GREEN 		= green;
    public final static Color magenta		= new Color(255, 0, 255);
    public final static Color MAGENTA 		= magenta;
    public final static Color cyan 			= new Color(0, 255, 255);
    public final static Color CYAN 			= cyan;
    public final static Color blue 			= new Color(0, 0, 255);
    public final static Color BLUE 			= blue;

//	-----------------------------------------------------------------------------------------------------------------
    
    private int		value_argb;

    private float 	value_argb_f[] = new float[4];

    public Color(int r, int g, int b) 
    {
        this(r, g, b, 255);
    }
    
    public Color(int argb) 
    {
		this.set(argb);
    }
    
    public Color(int r, int g, int b, int a) 
	{
    	 this(r/255f, g/255f, b/255f, a/255f);
	}
    
    public Color(float r, float g, float b) {
       this(r, g, b, 1f);
    }

    public Color(float r, float g, float b, float a)
    {
    	this.set(r, g, b, a);
    }

    public Color(String hex) {
    	long argb = Long.parseLong(hex, 16);
    	set((int)argb); 
    }
    
    private void set(int argb) 
    {
    	this.value_argb 		= argb;
    	this.value_argb_f[0] 	= ((argb >> 24) & 0xff) / 255f;
    	this.value_argb_f[1] 	= ((argb >> 16) & 0xff) / 255f;
    	this.value_argb_f[2] 	= ((argb >> 8)  & 0xff) / 255f;
    	this.value_argb_f[3] 	= ((argb >> 0)  & 0xff) / 255f;
    }
    
    private void set(float r, float g, float b, float a) 
    {
    	if (r >= 0 && r <= 1f && 
    		g >= 0 && g <= 1f &&
    		b >= 0 && b <= 1f && 
    		a >= 0 && a <= 1f) 
    	{
    		long lv = 
    	    		((long)(a * 0xff)<<24) | 
    	    		((long)(r * 0xff)<<16) | 
    	    		((long)(g * 0xff)<<8) | 
    	    		((long)(b * 0xff)<<0);
    	    	value_argb = (int)(0xffffffff & lv);
    	    	value_argb_f[0] = a;
    	    	value_argb_f[1] = r;
    	    	value_argb_f[2] = g;
    	    	value_argb_f[3] = b;
    	} else {
    		throw new IllegalArgumentException("rgba{" + r + "," + g + "," + b + "," + a + "}");
    	}
    }
    
    public void setAlpha(float a) {
    	set(value_argb_f[1], value_argb_f[2], value_argb_f[3], a);
    }

	public void setRed(float r) {
    	set(r, value_argb_f[2], value_argb_f[3], value_argb_f[0]);
	}

	public void setGreen(float g) {
    	set(value_argb_f[1], g, value_argb_f[3], value_argb_f[0]);
	}

	public void setBlue(float b) {
    	set(value_argb_f[1], value_argb_f[2], b, value_argb_f[0]);
	}
  
	public float getAlpha() {
		return value_argb_f[0];
	}
	public float getRed() {
		return value_argb_f[1];
	}

	public float getGreen() {
		return value_argb_f[2];
	}

	public float getBlue() {
		return value_argb_f[3];
	}
	
	public String toHexString() {
		return Long.toHexString(0x00ffffffffL & getARGB());
	}
	public String toString() {
		return toHexString();
	}
	public int getARGB() {
		return value_argb;
	}

	public int hashCode() {
		return value_argb;
	}


}
