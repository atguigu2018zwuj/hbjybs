package com.mininglamp.currencySys.common.base.bean;

import java.io.Serializable;
import java.util.Map;
/**
 * JSON格式入参，所有查询参数放入params属性中
 * @author czy
 * 2016年11月10日11:22:41
 */
public class RequestBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 参数
	 */
	private Map<String,String> params;

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
}
