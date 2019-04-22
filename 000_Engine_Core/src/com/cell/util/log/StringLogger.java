package com.cell.util.log;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;

public class StringLogger
{
	final private String prefix;
	private StringBuilder buf = new StringBuilder();
	private PrintWriter out = new PrintWriter(new LogWriter(), true);
	
	public StringLogger(String prefix) 
	{
		this.prefix = prefix;
	}
	public void clear() {
		buf.delete(0, buf.length());
	}
	public void info(String msg) {
		out.println(prefix + msg);
	}
	public void error(String msg) {
		out.println(prefix + msg);
	}
	public void error(String msg, Throwable e) {
		out.println(prefix + msg);
		e.printStackTrace(out);
	}
	public void error(Throwable e) {
		error(e.getMessage(), e);
	}
	
	public String popString() {
		String ret = buf.toString();
		buf.setLength(0);
		return ret;
	}

	public String getString() {
		return buf.toString();
	}
	
	private class LogWriter extends Writer {
		@Override
		public void write(char[] cbuf, int off, int len) throws IOException {
			buf.append(cbuf, off, len);
		}
		@Override
		public void flush() throws IOException {}
		@Override
		public void close() throws IOException {}

	}
}
