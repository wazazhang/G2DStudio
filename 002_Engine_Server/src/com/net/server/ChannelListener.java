package com.net.server;

import com.cell.net.io.Message;

public interface ChannelListener 
{
	public void receivedMessage(Channel channel, ClientSession sender, Message message);
	
	public void sessionLeaved(Channel channel, ClientSession session);
	
	public void sessionJoined(Channel channel, ClientSession session);
}
