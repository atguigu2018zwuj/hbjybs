package com.mininglamp.currencySys.common.base;

import java.util.HashMap;
import java.util.Map;

/**
 * 响应编码常量
 * @author czy
 * 2016年9月19日16:27:33
 */
public class CodeConst {
	/**
	 * 成功
	 */
	public static final String CODE_OK = "1000";
	/**
	 * 错误
	 */
	public static final String CODE_ERROR = "2000";
	/**
	 * 传入参数错误
	 */
	public static final String CODE_ERROR_PARAMETER = "2001";
	public static final String CODE_ERROR_PARAMETER_EMPTY = "2002";
	/**
	 * 失败
	 */
	public static final String CODE_FAIL = "3000";
	public static final String CODE_FAIL_LOGIN = "3101";
	public static final String CODE_FAIL_LOGIN_ID_PWD = "3102";
	public static final String CODE_FAIL_DATA_EMPTY = "3103";
	public static final String CODE_FAIL_NBJGH = "3104";
	/**
	 * 模型
	 */
	public static final String CODE_NO_USER_OR_CUSTOMER = "4001";
	/***
     * 
     */
    public static final String CODE_TOKEN_ERROR = "4002";
    public static final String CODE_TOKEN_EXPIRES = "4003";
	/**
	 * 未知
	 */
	public static final String CODE_UNKNOWN = "9999";
	public static final String CODE_INFO_UNCON = "5555";
	
	/**
	 * 确认登录
	 */
	public static final String CODE_CONFIRM_LOGIN = "6666";
	 
	public static Map<String,String> msgMap = new HashMap<String,String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			this.put(CODE_ERROR_PARAMETER,"传入参数错误");
			this.put(CODE_ERROR_PARAMETER_EMPTY, "上送参数不能为空");
			this.put(CODE_FAIL,"服务执行失败");
			this.put(CODE_FAIL_LOGIN,"登陆失败");
			//this.put(CODE_FAIL_LOGIN_ID_PWD,"登陆失败[用户名或密码错误]");
			this.put(CODE_FAIL_LOGIN_ID_PWD,"错误，错误的用户名或密码!");
			this.put(CODE_INFO_UNCON, "数据正在更新，暂不提供访问数据权限");
			this.put(CODE_NO_USER_OR_CUSTOMER, "没有这个用户或客户");
			this.put(CODE_TOKEN_ERROR, "您没有上送正确的token");
			this.put(CODE_TOKEN_EXPIRES, "您上送的token已超时，请重新获取");
			this.put(CODE_CONFIRM_LOGIN, "当前账户已在他处登录，是否登录");
			this.put(CODE_FAIL_NBJGH, "当前账户内部机构号异常，请联系管理员！！");
			this.put(CODE_OK, "成功");
		}
	};
	/**
	 * 获取编码描述信息
	 * @param code
	 * @return
	 */
	public static String getCodeMessage(String code){
		return msgMap.get(code);
	}
}
