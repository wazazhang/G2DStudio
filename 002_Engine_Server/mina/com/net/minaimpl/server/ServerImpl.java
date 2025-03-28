package com.net.minaimpl.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

import com.cell.CUtil;
import com.cell.net.io.ExternalizableFactory;
import com.cell.net.io.Message;
import com.net.Protocol;
import com.net.minaimpl.ProtocolImpl;
import com.net.minaimpl.ProtocolPool;
import com.net.minaimpl.SessionAttributeKey;
import com.net.server.Channel;
import com.net.server.ChannelManager;
import com.net.server.ClientSession;
import com.net.server.ServerMessageHandler;


public class ServerImpl extends AbstractServer
{
	final private ChannelManager		channel_manager;

	protected ConcurrentHashMap<Class<?>, AtomicLong>	cmessage_received_count = 
		new ConcurrentHashMap<Class<?>, AtomicLong>();
	protected ConcurrentHashMap<Class<?>, AtomicLong>	cmessage_sent_count = 
		new ConcurrentHashMap<Class<?>, AtomicLong>();
	
//	private ReentrantLock session_lock = new ReentrantLock();
	
	private int max_session_count = 0;
	
//	----------------------------------------------------------------------------------------------------------------------
	
	public ServerImpl() 
	{
		this(Thread.currentThread().getContextClassLoader(), null,
				Runtime.getRuntime().availableProcessors() + 1, 
				Integer.MAX_VALUE, 
				Integer.MAX_VALUE, 
				0);
	}

	/**
	 * @param ioProcessCount
	 * @param sessionWriteIdleTimeSeconds	多长时间内没有发送数据，断掉链接(秒)
	 * @param sessionReadIdleTimeSeconds	多长时间内没有接受数据，断掉链接(秒)
	 * @param keepalive_interval_sec		心跳间隔，0表示不使用心跳机制
	 */
	public ServerImpl(
			int ioProcessCount, 
			int sessionWriteIdleTimeSeconds,
			int sessionReadIdleTimeSeconds,
			int keepalive_interval_sec) 
	{
		this(Thread.currentThread().getContextClassLoader(), null,
				ioProcessCount, 
				sessionWriteIdleTimeSeconds, 
				sessionReadIdleTimeSeconds,
				keepalive_interval_sec);
	}
	
	/**
	 * @param class_loader
	 * @param externalizable_factory
	 * @param ioProcessCount
	 * @param sessionWriteIdleTimeSeconds	多长时间内没有发送数据，断掉链接(秒)
	 * @param sessionReadIdleTimeSeconds	多长时间内没有接受数据，断掉链接(秒)
	 * @param keepalive_interval_sec		心跳间隔，0表示不使用心跳机制
	 */
	public ServerImpl(
			ClassLoader 			class_loader,
			ExternalizableFactory 	externalizable_factory,
			int 					ioProcessCount, 
			int 					sessionWriteIdleTimeSeconds,
			int 					sessionReadIdleTimeSeconds,
			int 					keepalive_interval_sec) 
	{
		this(class_loader, externalizable_factory, null, null,
				ioProcessCount, 
				sessionWriteIdleTimeSeconds, 
				sessionReadIdleTimeSeconds, 
				keepalive_interval_sec);
	}
	
	/**
	 * @param class_loader
	 * @param externalizable_factory
	 * @param acceptor_pool
	 * @param io_processor_pool
	 * @param io_processor_count
	 * @param sessionWriteIdleTimeSeconds	多长时间内没有发送数据，断掉链接(秒)
	 * @param sessionReadIdleTimeSeconds	多长时间内没有接受数据，断掉链接(秒)
	 * @param keepalive_interval_sec		心跳间隔，0表示不使用心跳机制
	 * @param close_on_error
	 */
	public ServerImpl(
			ClassLoader 			class_loader,
			ExternalizableFactory 	externalizable_factory,
			Executor 				acceptor_pool,
			Executor 				io_processor_pool,
			int						io_processor_count,
			int 					sessionWriteIdleTimeSeconds,
			int 					sessionReadIdleTimeSeconds, 
			int 					keepalive_interval_sec) 
	{
		super(class_loader, externalizable_factory, 
				acceptor_pool, 
				io_processor_pool,
				io_processor_count, 
				sessionWriteIdleTimeSeconds, 
				sessionReadIdleTimeSeconds, 
				keepalive_interval_sec);
		this.channel_manager = new ChannelManagerImpl(this);
	}	
//	----------------------------------------------------------------------------------------------------------------------

//	-----------------------------------------------------------------------------------------------------------------------

	public ChannelManager getChannelManager() {
		return channel_manager;
	}
	
	private ClientSessionImpl getBindSession(IoSession session)
	{
		return (ClientSessionImpl)session.getAttribute(SessionAttributeKey.CLIENT_SESSION);
	}
	
	/**
	 * 设置最大连结数，0表示无限
	 * @param count
	 */
	public void setMaxSession(int count)
	{
		this.max_session_count = count;
	}
	
	public int getMaxSession()
	{
		return max_session_count;
	}
	
//	class SessionIterator implements Iterator<ClientSession>
//	{
//		public SessionIterator() {
//			Acceptor.getManagedSessions().containsKey(SessionAttributeKey.CLIENT_SESSION);
//		}
//		
//		@Override
//		public boolean hasNext() {
//			// TODO Auto-generated method stub
//			return false;
//		}
//		@Override
//		public ClientSession next() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		@Override
//		public void remove() {
//			// TODO Auto-generated method stub
//			
//		}
//		
//	}
	
	public List<ClientSession> getSessions() {
		ArrayList<ClientSession> sessions = new ArrayList<ClientSession>(
				Acceptor.getManagedSessionCount());
		for (IoSession session : Acceptor.getManagedSessions().values()) {
			ClientSessionImpl client = getBindSession(session);
			if (client != null) {
				sessions.add(client);
			}
		}
		return sessions;
	}
	
	public Iterator<ClientSession> getSessionsIt() {
		return getSessions().iterator();
	}
	
	public ClientSession getSession(long sessionID) {
		IoSession session = Acceptor.getManagedSessions().get(sessionID);
		ClientSessionImpl client = getBindSession(session);
		return client;
	}
	
	public ClientSession getSession(Object object){
		for (IoSession session : Acceptor.getManagedSessions().values()){
			ClientSessionImpl client = getBindSession(session);
			if (object.equals(client)) {
				return client;
			}
		}
		return null;
	}
	
	public boolean hasSession(ClientSession session) {
		return Acceptor.getManagedSessions().containsKey(session.getID());
	}
	
	public int getSessionCount() {
		return Acceptor.getManagedSessionCount();
	}
	
	public void broadcast(Message message){
		ProtocolImpl p = ProtocolPool.getInstance().createProtocol();
		p.Protocol	= Protocol.PROTOCOL_SESSION_MESSAGE;
		p.message	= message;
		Set<WriteFuture> futures = Acceptor.broadcast(p);
//		for (WriteFuture future : futures) {
//			future.awaitUninterruptibly();
//		}
	}
	
//	-----------------------------------------------------------------------------------------------------------------------

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
//		super.exceptionCaught(session, cause);
		ClientSessionImpl client = getBindSession(session);
		if (client != null && client.Listener != null){
			client.Listener.onError(client, cause);
		} else {
			super.exceptionCaught(session, cause);
		}
	}

	
	
	public void sessionOpened(IoSession session) throws Exception {
		log.debug("sessionOpened : " + session);
		try {
			if (max_session_count > 0) {
				int ccount = Acceptor.getManagedSessionCount();
				int maxcount = max_session_count;
				if (ccount > maxcount) {
					WriteFuture future = write(session, null, Protocol.PROTOCOL_SESSION_MESSAGE, 0, 0, ProtocolImpl.SYSTEM_NOTIFY_SERVER_FULL);
					session.close(false);
					return;
				}
			}
			ClientSessionImpl client = new ClientSessionImpl(session, this);
			client.setListener(SrvListener.connected(client));
			session.setAttribute(SessionAttributeKey.CLIENT_SESSION, client);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void sessionClosed(IoSession session) throws Exception {
		log.debug("sessionClosed : " + session);
		ClientSessionImpl client = (ClientSessionImpl)session.removeAttribute(SessionAttributeKey.CLIENT_SESSION);
		if (client != null) {
			try {
				Iterator<Channel> channels = channel_manager.getChannels();
				while (channels.hasNext()) {
					try {
						channels.next().leave(client);
					} catch (Throwable e) {
						log.error(e.getMessage(), e);
					}
				}
			} catch (Throwable e) {
				log.error(e.getMessage(), e);
			}
			try {
				if (client.Listener != null) {
					client.Listener.disconnected(client);
				}
			} catch (Throwable e) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
	public void messageReceived(final IoSession session, final Object message) throws Exception 
	{
		//System.out.println(Thread.currentThread().getName() + " messageReceived");
		if (message instanceof Protocol)
		{
			Protocol header = (Protocol)message;
			
			recordMessageCount(cmessage_received_count, header);
			
			ClientSessionImpl client = getBindSession(session);
			if (client != null && client.Listener != null)
			{
				switch (header.getProtocol())
				{
				case Protocol.PROTOCOL_CHANNEL_MESSAGE:
					ChannelImpl channel = (ChannelImpl)channel_manager.getChannel(header.getChannelID());
					if (channel != null) {
						channel.getChannelListener().receivedMessage(channel, client, header.getMessage());
					}
					break;
					
				case Protocol.PROTOCOL_SESSION_MESSAGE:
					client.Listener.receivedMessage(client, header, header.getMessage());
					break;
//				case Protocol.PROTOCOL_CHANNEL_JOIN_S2C:
//					break;
//				case Protocol.PROTOCOL_CHANNEL_LEAVE_S2C:
//					break;
				default:
					log.error("unknow message : " + session + " : " + message);
				}
				handleMessage(client, header);
			}
		}
		else
		{
			log.error("bad message type : " + session + " : " + message);
		}
		
	}
	
	@Override
	public void messageSent(IoSession session, Object message) throws Exception 
	{
		if (message instanceof Protocol) {
			Protocol header = (Protocol) message;
			recordMessageCount(cmessage_sent_count, header);
			ClientSessionImpl client = getBindSession(session);
			if (client != null && client.Listener != null) {
				client.Listener.sentMessage(client, header, header.getMessage());
			}
		} else {
			log.error("bad message type : " + session + " : " + message);
		}
	}

    protected long recordMessageCount(Map<Class<?>, AtomicLong> map, Protocol msg) {
		if (map != null && msg.getMessage() != null) {
			AtomicLong idx = map.get(msg.getMessage().getClass());
			if (idx == null) {
				idx = new AtomicLong(0);
				map.put(msg.getMessage().getClass(), idx);
			}
			return idx.incrementAndGet();
		}
		return 0;
    }
	

	public String getMessageIOStats()
	{
		StringBuilder lines = new StringBuilder();
		lines.append("[I/O message count]\n");
		if (cmessage_received_count!=null) {
			lines.append("[Received]\n");
			CUtil.toStatusLines(80, cmessage_received_count, lines);
			CUtil.toStatusSeparator(lines);
		}
		if (cmessage_sent_count!=null) {
			lines.append("[Sent]\n");
			CUtil.toStatusLines(80, cmessage_sent_count, lines);
			CUtil.toStatusSeparator(lines);
		}
		return lines.toString();
	}
	
	
	final protected ConcurrentHashMap<Class<?>, HashSet<ServerMessageHandler<?>>> handlers = 
		new ConcurrentHashMap<Class<?>, HashSet<ServerMessageHandler<?>>>();
	
	public<T extends Message>
	void addMessageHandler(Class<T> cls, ServerMessageHandler<T> handler) {
		HashSet<ServerMessageHandler<?>> handleset = handlers.get(cls);
		if (handleset == null) {
			handleset = new HashSet<ServerMessageHandler<?>>();
			handlers.put(cls, handleset);
		}
		handleset.add(handler);
	}
	public<T extends Message>
	void removeMessageHandler(Class<T> cls, ServerMessageHandler<T> handler) {
		HashSet<ServerMessageHandler<?>> handleset = handlers.get(cls);
		if (handleset != null) {
			handleset.remove(handler);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" }) 
	void handleMessage(ClientSession session, Protocol protocol) {
		Message msg = protocol.getMessage();
		if (msg != null) {
			Class<?> cls = msg.getClass();
			HashSet<ServerMessageHandler<?>> handleset = handlers.get(cls);
			if (handleset!=null && !handleset.isEmpty()) {
				for (ServerMessageHandler sh : handleset) {
					sh.onReceived(session, protocol, msg);
				}
			}
		}
	}
}
