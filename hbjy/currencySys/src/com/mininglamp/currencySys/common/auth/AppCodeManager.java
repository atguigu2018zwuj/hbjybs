package com.mininglamp.currencySys.common.auth;

import javax.servlet.http.HttpServletRequest;

import com.mininglamp.currencySys.common.exception.ConflictException;

/**
 * AppCode管理工具类
 * @author czy
 * 2016年9月27日19:37:14
 */
public abstract interface AppCodeManager {
	/**
	 * 初始化
	 * @throws ConflictException 
	 */
	void init() throws ConflictException;
	/**
	 * 验证
	 * @param request
	 * @return
	 */
	boolean validateAppCode(HttpServletRequest request);
}
