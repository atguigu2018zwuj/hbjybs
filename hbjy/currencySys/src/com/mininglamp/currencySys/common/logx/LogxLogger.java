package com.mininglamp.currencySys.common.logx;

import java.util.List;
import java.util.Map;

import com.mininglamp.logx.inter.LoginStatus;
import com.mininglamp.logx.log.Log4jLogger;

public class LogxLogger extends Log4jLogger implements LoginStatus{


	
	/**
	 * 登陆判断
	 */
	@Override
	public boolean isLogin(Map<String, Object> sessionAttrs) {
		if(sessionAttrs.get("userBean") == null) return false;
		else return true;
	}

	@Override
	public String getAppenderName() {
		return "INFO";
	}

	@Override
	protected String handleLine(String line) {
		if(line.contains("[INFO]")&&line.contains("^")){
			return line.substring(32);
		}
		return null;
	}

	@Override
	protected void logLine(String line) {
		getLogger().info(line);
	}

	@Override
	public List<String> getLog(String startTime, String endTime) {
		return getDailyRollingLog(startTime, endTime);
	}

	
	
}
