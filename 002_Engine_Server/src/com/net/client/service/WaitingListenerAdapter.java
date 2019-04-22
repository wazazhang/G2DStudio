package com.net.client.service;

import com.cell.net.io.Message;

public class WaitingListenerAdapter implements WaitingListener<Message, Message>
{
	public void response(BasicNetService service, Message request, Message response){}
	
	/**
	 * @param service
	 * @param request
	 * @param send_time 该消息的发送时间
	 */
	public void timeout(BasicNetService service, Message request, long send_time){}
}
