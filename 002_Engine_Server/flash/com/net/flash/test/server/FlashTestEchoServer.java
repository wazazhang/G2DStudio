package com.net.flash.test.server;

import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cell.CUtil;
import com.cell.net.io.Message;
import com.cell.util.concurrent.ThreadPool;
import com.net.Protocol;
import com.net.flash.test.Messages.Echo2Request;
import com.net.flash.test.Messages.Echo2Response;
import com.net.flash.test.Messages.EchoNotify;
import com.net.flash.test.Messages.EchoRequest;
import com.net.flash.test.Messages.EchoResponse;
import com.net.server.ClientSession;
import com.net.server.ClientSessionListener;
import com.net.server.Server;
import com.net.server.ServerListener;

public class FlashTestEchoServer implements ServerListener
{
	private static Logger log = LoggerFactory.getLogger(FlashTestEchoServer.class);
	
	private ThreadPool services = new ThreadPool("Flash-Test");
	
	public FlashTestEchoServer() {
		
	}
	
	@Override
	public void init(Server server) {
		
	}
	
	@Override
	public void destory() {
		
	}
	
	@Override
	public ClientSessionListener connected(ClientSession session) {
		log.info("connected " + session.getRemoteAddress());
		return new EchoClientSession(session);
	}
	
	class EchoClientSession implements ClientSessionListener, Runnable
	{
		ClientSession session;
		ScheduledFuture<?> task;
		public EchoClientSession(ClientSession session) {
			this.session = session;
			// 每10秒向客户端发送个消息
			this.task = services.scheduleAtFixedRate(this, 1000, 10000);
		}
		@Override
		public void disconnected(ClientSession session) {
			log.info("disconnected " + session.getRemoteAddress());
			this.task.cancel(false);
		}
		@Override
		public void sentMessage(ClientSession session, Protocol protocol, Message message) {}
		@Override
		public void receivedMessage(ClientSession session, Protocol protocol, Message message) {
			if (message instanceof EchoRequest) {
				session.sendResponse(protocol, new EchoResponse(message.toString() + " ok"));
			}
			else if (message instanceof Echo2Request) {
				session.sendResponse(protocol, new Echo2Response(message.toString() + " ok"));
			}
		}
		public void onError(ClientSession session, Throwable cause){
			
		}
		public void run() {
//			log.info("send notify");
			this.session.send(new EchoNotify("roll " + CUtil.getRandom(1, 100)));
		}
	}
	
}
