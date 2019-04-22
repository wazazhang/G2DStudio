package com.net.server;

import com.cell.net.io.Message;
import com.net.AbstractSession;
import com.net.Protocol;


public interface ClientSession extends AbstractSession
 {
	public String getName();

	public Server getServer();

	public ClientSessionListener getListener();

	public boolean send(Message message);

	public boolean sendResponse(Protocol request, Message response);

	
}
