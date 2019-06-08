package com.mininglamp.currencySys.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.mininglamp.currencySys.user.service.UserService;

public class WriteLog {

	static SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
	static SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private static UserService userService;
	
	/**
	 * 插入操作日志
	 */
	public static void insertOperationLog(HttpServletRequest request,HttpServletResponse response,String content){
		Map<String, Object> params = new HashMap<String, Object>();
		String username = (String) request.getSession().getAttribute("username");
		String date = fmt.format(new Date());
		content = username+"在"+ymdhms.format(date)+","+content;
		params.put("NR", content);
		params.put("NAME", username);
		params.put("SJRQ", date);
		try {
			userService.insertList(params, "insertOperationLog");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
