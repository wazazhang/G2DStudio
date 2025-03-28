/*******************************************************************************
* Copyright (c) 2007 LuaJ. All rights reserved.
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
******************************************************************************/
package org.luaj.vm;

/**
 * RuntimeException that is thrown and caught in response to a lua error. 
 * This error does not indicate any problem with the normal functioning 
 * of the Lua VM, but rather indicates that the lua script being interpreted
 * has encountered a lua error, eigher via LuaState.error() or lua error() calls. 
 *  
 */
public class LuaErrorException extends RuntimeException {
	
	private static final String DEFAULT_MESSAGE = "lua error";
	
	private Throwable cause;

	/**
	 * Construct a LuaErrorException with the default message. 
	 */
	public LuaErrorException() {
		this(DEFAULT_MESSAGE);
	}

	/**
	 * Construct a LuaErrorException in response to a Throwable that was caught
	 * and with the default message. 
	 */
	public LuaErrorException(Throwable cause) {
		this( DEFAULT_MESSAGE+": "+cause );
		this.cause = cause;
	}

	/**
	 * Construct a LuaErrorException with a specific message.
	 *  
	 * @param message message to supply
	 */
	public LuaErrorException(String message) {
		this(null, message, -1);
	}

	/**
	 * Construct the message around a specific vm and with a particular level of debug info
	 * @param vm
	 * @param message
	 * @param level 0 for no message, >=1 for current call or above, -1 for most recent lua call
	 */
	public LuaErrorException(LuaState vm, String message, int level) {
		super( addLineInfo( vm, message, level ) );
	}

	/** 
	 * Append line info as per level
	 * @param vm
	 * @param message
	 * @param level
	 * @return
	 */
	private static String addLineInfo(LuaState vm, String message, int level) {
		// add position information
		if ( vm == null ) {
			if ( LThread.running != null )
				vm = LThread.running.vm;
			else
				vm = LuaState.mainState;
			if ( vm == null )
				return message;
		}
		if ( level != 0 && message != null ) {
			message = vm.getFileLine(level)+": "+message;
		}
		return vm.luaV_call_errfunc( message );
	}
	
	/** 
	 * Get the cause, if any.
	 */
	public Throwable getCause() {
		return cause;
	}


}
