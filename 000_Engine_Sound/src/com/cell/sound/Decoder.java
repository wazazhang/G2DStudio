package com.cell.sound;

import java.io.InputStream;

public abstract class Decoder
{
	abstract public String getName();
	
	abstract public SoundInfo decode(String resource, InputStream in) throws Exception;
	
	
}
