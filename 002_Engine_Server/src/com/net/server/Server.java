package com.net.server;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.cell.net.io.ExternalizableFactory;
import com.cell.net.io.Message;

/**
 * @author zhangyifei
 *
 */
public interface Server
{
	 
	public void 					open(int port, ServerListener listener) throws IOException ;
	
//	public void 					close() throws IOException;
	
	public void 					dispose() throws IOException;
	
	
	public void						broadcast(Message message);
	
	public ExternalizableFactory	getMessageFactory();
//	
//	public long 					getSentMessageCount() ;
//	
//	public long 					getReceivedMessageCount ();
//	
//	public long 					getSentBytes();
//	
//	public long 					getReceivedBytes();
//	
//	
//	public long						getHeartBeatSent();
//	
//	public long						getHeartBeatReceived();

	
	public int 						getSessionCount();
	
	public boolean 					hasSession(ClientSession session);
	
	public ClientSession 			getSession(long sessionID);
	
	public Iterator<ClientSession> 	getSessionsIt();
	
	public List<ClientSession> 		getSessions();
	
	public ChannelManager 			getChannelManager();

	
	/**
	 * 添加对指定类型消息监听的Handler
	 * @param <T>
	 * @param cls
	 * @param handler
	 */
	public<T extends Message> 
	void addMessageHandler(Class<T> cls, ServerMessageHandler<T> handler);

	/**
	 * 移除消息监听器
	 * @param <T>
	 * @param cls
	 * @param handler
	 */
	public<T extends Message> 
	void removeMessageHandler(Class<T> cls, ServerMessageHandler<T> handler);

}
