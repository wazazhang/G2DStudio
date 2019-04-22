package com.cell.net.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

public class HttpPost implements Runnable
{
	String 	host;
	int 	port;
	String 	path;
	String 	params;
	
	HttpPostListener listener;

	String http_response = "";
	
	public HttpPost(URL url)
	{
		this(url.getHost(), url.getPort(), url.getPath(), url.getQuery());
	}
	
	public HttpPost(String host, int port, String path, String params) 
	{
		this.host	= host;
		this.port	= port;
		this.path	= path;
		this.params	= params;
		if (port <= 0) {
			this.port = 80;
		}
	}
	
	public HttpPost(String host, int port, String path, String params, HttpPostListener listener) 
	{
		this(host, port, path, params);
		this.listener = listener;
		new Thread(this).run();
	}

	public String post()
	{
		String ret = "";
		
		try {
			// 建立连接

			InetAddress addr = InetAddress.getByName(host);
			Socket socket = new Socket(addr, port);

			StringBuilder sb = new StringBuilder();
			sb.append("POST " + path + " HTTP/1.0\r\n");
			sb.append("Host: " + host + "\r\n");
			sb.append("Content-Length: " + params.length() + "\r\n");
			sb.append("Content-Type: application/x-www-form-urlencoded\r\n");
			sb.append("\r\n"); // 以空行作为分割
			// 发送数据
			sb.append(params);
			
			// 发送数据头
			OutputStreamWriter sw = new OutputStreamWriter(
					socket.getOutputStream(), "UTF-8");
			sw.write(sb.toString());
			sw.flush();
			
			// 读取返回信息
			BufferedReader rd = new BufferedReader(
					new InputStreamReader(socket.getInputStream(), "UTF-8"));
			boolean isblank = false;
			String line;
			while ((line = rd.readLine()) != null) {
				http_response += (line + "\n");
				if (line.isEmpty()) {
					isblank = true;
				} 
				// 以空行作为分割
				else if (isblank) {
					ret += line + "\n";
				}
			}
			sw.close();
			rd.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
			if (listener!=null) {
				listener.response(this, e.getMessage());
			}
			return e.getMessage();
		}
		
		if (listener!=null) {
			listener.response(this, ret);
		}
		return ret;
	}
	
	public void run() {
		post();
	}
	
	public static interface HttpPostListener
	{
		public void response(HttpPost post, String msg);
	}
}
