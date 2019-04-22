package com.net.server;

import com.cell.net.io.Message;
import com.net.Protocol;

public interface ServerMessageHandler<T extends Message>
{
	public void onReceived(ClientSession session, Protocol protocol, T request);
}
