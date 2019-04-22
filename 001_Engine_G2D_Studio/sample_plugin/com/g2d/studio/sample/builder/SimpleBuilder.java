package com.g2d.studio.sample.builder;


import java.io.File;
import java.io.IOException;

import com.cell.gameedit.OutputLoader;
import com.cell.gameedit.output.OutputXmlDir;
import com.g2d.studio.StudioResource;
import com.g2d.studio.cell.gameedit.Builder;
import com.g2d.studio.cpj.CPJResourceType;

public class SimpleBuilder extends SampleBuilder
{
	public SimpleBuilder(String g2d_project_root) throws IOException
	{
		super(g2d_project_root);
	}
	
//	---------------------------------------------------------------------------------------------------------------------------
//
//	---------------------------------------------------------------------------------------------------------------------------
	
//	-------------------------------------------------------------------------------------------------------------------

	@Override
	public com.g2d.studio.io.File findCPJFile(
			com.g2d.studio.io.File file,
			CPJResourceType resType) 
	{
		com.g2d.studio.io.File cpj_file = null;
		com.g2d.studio.io.File xml_file = null;
		
		switch (resType) {
		case ACTOR:
			cpj_file = file.getChildFile("actor.cpj");
			xml_file = file.getChildFile("output").getChildFile("actor.xml");
			break;
		case AVATAR:
			cpj_file = file.getChildFile("item.cpj");
			xml_file = file.getChildFile("output").getChildFile("item.xml");
			break;
		case EFFECT:
			cpj_file = file.getChildFile("effect.cpj");
			xml_file = file.getChildFile("output").getChildFile("effect.xml");
			break;
		case WORLD:
			cpj_file = file.getChildFile("scene.cpj");
			xml_file = file.getChildFile("output").getChildFile("scene.xml");
			break;
		default:
			return null;
		}
		if (cpj_file.exists() && xml_file.exists()) {
			return cpj_file;
		}
		return null;
	}

}
