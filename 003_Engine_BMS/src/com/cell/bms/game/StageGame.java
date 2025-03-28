package com.cell.bms.game;


import java.awt.event.KeyEvent;

import com.cell.bms.BMSFile;
import com.cell.bms.BMSPlayer;
import com.cell.bms.PlayerManager;
import com.g2d.Canvas;
import com.g2d.Graphics2D;
import com.g2d.display.DisplayObjectContainer;
import com.g2d.display.Stage;

public class StageGame extends Stage
{
	BMSFile		bms_file;
	BMSPlayer	player;
	BMSLayer	layer;
	
	public StageGame() 
	{
		setSize(Config.STAGE_WIDTH, Config.STAGE_HEIGHT);
	}
	
	@Override
	public void inited(Canvas root, Object[] args) {
		if (args!=null && args.length>0) {
			bms_file = (BMSFile)args[0];
		}
		super.inited(root, args);
	}
	
	public void added(DisplayObjectContainer parent) 
	{
		player	= new BMSPlayer(bms_file);
		layer	= new BMSLayer(player);
		this.addChild(layer);
	}

	public void removed(DisplayObjectContainer parent) {
		bms_file.dispose();
		PlayerManager.mute();
	}
	
	public void update() {
		if (getRoot().isKeyDown(KeyEvent.VK_ESCAPE)) {
			getRoot().changeStage(StageTitle.class);			
			player.stop();
		}
	}
	
	public void render(Graphics2D g) {}
	
}
