package com.net.server;

import java.util.Iterator;

import com.cell.net.io.Message;
import com.net.Protocol;

public interface Channel 
{
	public int						getID();
	
	public Iterator<ClientSession> 	getSessions();
	
	public int 						getSessionCount();
	
	public boolean 					hasSessions();
	
	public boolean 					hasSession(ClientSession session);
	
	public boolean 					join(ClientSession session);
		
	public boolean 					leave(ClientSession session);
			
	public int 						leaveAll();
	
	public int 						send(Message message);
	
	public int 						send(ClientSession sender, Message message);
	
	public int 						sendResponse(ClientSession sender, Protocol request, Message response);
		
	public Server					getServer();
	
	public ChannelListener 			getChannelListener();
	
	public void						dispose();
	
}
