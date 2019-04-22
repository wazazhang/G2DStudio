package com.net.client.service;

import com.cell.net.io.Message;

public interface NotifyListener<N extends Message>
{
	public void notify(BasicNetService service, N notify);
}
