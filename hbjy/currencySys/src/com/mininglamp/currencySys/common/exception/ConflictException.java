package com.mininglamp.currencySys.common.exception;
/**
 * 冲突异常
 * @author czy
 * 2016年9月27日20:49:58
 */
public class ConflictException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ConflictException() {
		super();
	}
	public ConflictException(String message) {
		super(message);
	}
	public ConflictException(String message,Throwable cause) {
		super(message,cause);
	}
}
