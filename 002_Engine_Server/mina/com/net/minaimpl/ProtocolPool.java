package com.net.minaimpl;



public class ProtocolPool {

	static private ProtocolPool instance = new ProtocolPool();
	static public  ProtocolPool getInstance() {
		return instance;
	}
	private ProtocolPool() {}
	
	public ProtocolImpl createProtocol() {
		return new ProtocolImpl();
	}
}
