package com.mininglamp.currencySys.announcement.bean;
public class ResultPojo {
	
	public static final int CODE_SUCCES=200;//执行成功
	public static final int CODE_SUCCES_ANOTHER=202;//执行成功，但需要另一种处理结果
	public static final int CODE_FAILURE=400;//执行失败
	public static final int CODE_FAILURE_ANOTHER=500;//执行失败
	public static final int CODE_NO_LOGIN=404;

	private int code;
	
	private String msg;
	
	private Object data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public static int getCodeSucces() {
		return CODE_SUCCES;
	}

	public static int getCodeFailure() {
		return CODE_FAILURE;
	}
	
	
	
}
