package com.net.client;


import com.cell.net.io.Message;

public interface ClientChannel {

	public int	getID();
	
	public void		send(Message message);
	
	public ServerSession getSession();
}
