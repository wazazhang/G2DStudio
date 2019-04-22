package com.g2d;


public interface BufferedImage extends Image
{

	public BufferedImage subImage(int x, int y, int width, int height);
}
