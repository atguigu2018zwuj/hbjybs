package com.mininglamp.currencySys.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.mininglamp.logx.http.Const;

//import com.mininglamp.sycount.common.log.bean.LogDic;
import com.mininglamp.ws.client.Constants;
import com.mininglamp.ws.client.bean.UserBean;


public class LoggerUtil {
	protected static Logger	logger	= LoggerFactory.getLogger("errorLog");

	public static void debug(String msg) {
		logger.debug(msg);
	}

	public static void debug(String msg, Object obj) {
		logger.debug(msg, obj);
	}

	public static void info(String msg) {
		logger.info(msg);
	}

	public static void info(String msg, Object obj) {
		logger.info(msg, obj);
	}

	public static void warn(String msg) {
		logger.warn(msg);
	}

	public static void warn(String msg, Object obj) {
		logger.warn(msg, obj);
	}

	public static void error(String msg) {
		logger.error(msg);
	}

	public static void error(String msg, Object obj) {
		logger.error(msg, obj);
	}
	/**
	 * 记录日志操作版本1
	 * @param session
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public static void logParse(HttpSession session,HttpServletRequest request) {
		if(session==null||request==null) return;
		UserBean userBean=(UserBean) session.getAttribute("userBean");
		if(userBean==null) return;
		String sortName="";
		if(userBean.getOrganisationBean()!=null){
			sortName=userBean.getOrganisationBean().getSortName();
		}
		StringBuilder param = new StringBuilder("");
		for (Enumeration<String> eh = request.getParameterNames(); eh.hasMoreElements();) {
			String parName = eh.nextElement();
			Object parValue = request.getParameter(parName);
			param.append(parName+"="+parValue+"@");
		}
		String paramstr = "";
		if(param.length()>0){
			paramstr = param.substring(0, param.length()-1);
		}
		logger.info(userBean.getUserId()+"^"+userBean.getUserName()+"^"+sortName+"^"+request.getRemoteAddr()+"^"+Constants.browser(request.getHeader("user-agent"))+
				"^"+request.getRequestURI()+"^"+Constants.logUuid()+"^"+paramstr+"^"+Constants.logDateTime()+"^"+Constants.VERSION_NUM+"^"+Constants.VERSION_DATE);
		
	}
	
	/**
	 * 记录日志操作版本2
	 * @param session
	 * @param request
	 */
//	@SuppressWarnings("unchecked")
//	public static void logParse(HttpSession session,HttpServletRequest request, String resultVal) {
//		if(session==null||request==null) return;
//		String uuid = "";
//		if(request.getAttribute("actionid")==null){
//			uuid = Constants.logUuid();
//			request.setAttribute("actionid", uuid);
//		}else{
//			uuid = (String)request.getAttribute("actionid");
//		}
//		
//		StringBuilder param = new StringBuilder("");
//		for (Enumeration<String> eh = request.getParameterNames(); eh.hasMoreElements();) {
//			String parName = eh.nextElement();
//			Object parValue = request.getParameter(parName);
//			param.append(parName+"="+parValue+"`");
//		}
//		String paramstr = "";
//		if(param.length()>0){
//			paramstr = param.substring(0, param.length()-1);
//		}
//		paramstr = filterStr(paramstr);
//		
//		String url=request.getContextPath()+request.getServletPath();
//		if(url.indexOf("?") != -1){
//			url = url.substring(0, url.indexOf("?"));
//		}
//		boolean sessionValid = true;
//		try{
//			session.getAttribute("userBean");
//		}catch(Exception e){
//			sessionValid = false;
//		}
//		if(!sessionValid){
//			logDefaultInfo(request, session, url, paramstr);
//			return;
//		}
//		
//		LogDic log=getLogDicByUrl(url);
//		
//		UserBean userBean=(UserBean) session.getAttribute("userBean");
//		String userId = request.getParameter("userBean.userId");
//		String parms = request.getParameter("parms");
//		if(userBean==null&&userId==null&&parms==null) return;
//		String sortName="";
//		if(userBean!=null&&userBean.getOrganisationBean()!=null){
//			sortName=userBean.getOrganisationBean().getSortName();
//		}
//		if(log==null) return;
//		String logstr = null;
//		
//		if(userBean != null){
//			logstr = userBean.getUserId()+"^"+userBean.getUserName()+"^"+sortName+"^"+request.getRemoteAddr()+"^"+Constants.browser(request.getHeader("user-agent"))+
//			"^"+request.getRequestURI()+"^"+(log==null?"":log.getActioncode())+"^"+paramstr+"^"+uuid+"^"+Constants.logDateTime()+"^"+session.getId()+"^"+(resultVal==null?"":resultVal)+"^"+Constants.VERSION_NUM_2+"^"+Constants.VERSION_DATE;
//		}else if(userId != null){
////			logstr = userId+"^"+""+"^"+""+"^"+request.getRemoteAddr()+"^"+Constants.browser(request.getHeader("user-agent"))+
////			"^"+request.getRequestURI()+"^"+(log==null?"":log.getActioncode())+"^"+paramstr+"^"+uuid+"^"+Constants.logDateTime()+"^"+session.getId()+"^"+(resultVal==null?"":resultVal)+"^"+Constants.VERSION_NUM_2+"^"+Constants.VERSION_DATE;
//			logstr = handleLogArr(userId,"","",request.getRemoteAddr(),Constants.browser(request.getHeader("user-agent")),
//			request.getRequestURI(),(log==null?"":log.getActioncode()),paramstr,uuid,Constants.logDateTime(),session.getId(),(resultVal==null?"":resultVal),Constants.VERSION_NUM_2,Constants.VERSION_DATE);
//		}else{
////			logstr = ""+"^"+""+"^"+""+"^"+request.getRemoteAddr()+"^"+Constants.browser(request.getHeader("user-agent"))+
////			"^"+request.getRequestURI()+"^"+(log==null?"":log.getActioncode())+"^"+paramstr+"^"+uuid+"^"+Constants.logDateTime()+"^"+session.getId()+"^"+(resultVal==null?"":resultVal)+"^"+Constants.VERSION_NUM_2+"^"+Constants.VERSION_DATE;
//			logstr = handleLogArr("","","",request.getRemoteAddr(),Constants.browser(request.getHeader("user-agent")),
//			request.getRequestURI(),(log==null?"":log.getActioncode()),paramstr,uuid,Constants.logDateTime(),session.getId(),resultVal,Constants.VERSION_NUM_2,Constants.VERSION_DATE);
//		}
//		logger.info(formatReturn(logstr));
//		
//	}
	private static void logDefaultInfo(HttpServletRequest request,HttpSession session,String url,String paramstr){
		String uuid = (String) request.getAttribute("actionid");
		String logstr = ""+"^"+""+"^"+""+"^"+request.getRemoteAddr()+"^"+Constants.browser(request.getHeader("user-agent"))+
		"^"+request.getRequestURI()+"^"+"2"+"^"+paramstr+"^"+uuid+"^"+Constants.logDateTime()+"^"+session.getId()+"^"+""+"^"+Constants.VERSION_NUM_2+"^"+Constants.VERSION_DATE;
		logger.info(formatReturn(logstr));
	}
	/**
	 * 记录日志操作版本错误输出版
	 * @param session
	 * @param request
	 */
//	@SuppressWarnings("unchecked")
//	public static void logParseError(HttpSession session,HttpServletRequest request, String resultVal) {
//		if(session==null||request==null) return;
//		String uuid = "";
//		if(request.getAttribute("actionid")==null){
//			uuid = Constants.logUuid();
//			request.setAttribute("actionid", uuid);
//		}else{
//			uuid = (String)request.getAttribute("actionid");
//		}
//		
//		StringBuilder param = new StringBuilder("");
//		for (Enumeration<String> eh = request.getParameterNames(); eh.hasMoreElements();) {
//			String parName = eh.nextElement();
//			Object parValue = request.getParameter(parName);
//			param.append(parName+"="+parValue+"`");
//		}
//		String paramstr = "";
//		if(param.length()>0){
//			paramstr = param.substring(0, param.length()-1);
//		}
//		
//		String url=request.getContextPath()+request.getServletPath();
//		if(url.indexOf("?") != -1){
//			url = url.substring(0, url.indexOf("?"));
//		}
//		boolean sessionValid = true;
//		try{
//			session.getAttribute("userBean");
//		}catch(Exception e){
//			sessionValid = false;
//		}
//		if(!sessionValid){
//			logDefaultInfoError(request, session, url, paramstr);
//			return;
//		}
//		
//		LogDic log=getLogDicByUrl(url);
//		
//		UserBean userBean=(UserBean) session.getAttribute("userBean");
//		String userId = request.getParameter("userBean.userId");
//		if(userBean==null&&userId==null) return;
//		String sortName="";
//		if(userBean!=null&&userBean.getOrganisationBean()!=null){
//			sortName=userBean.getOrganisationBean().getSortName();
//		}
//		if(log==null) return;
//		String logstr = null;
//		
//		if(userBean != null){
////			logstr = userBean.getUserId()+"^"+userBean.getUserName()+"^"+sortName+"^"+request.getRemoteAddr()+"^"+Constants.browser(request.getHeader("user-agent"))+
////			"^"+request.getRequestURI()+"^"+(log==null?"":log.getActioncode())+"^"+paramstr+"^"+uuid+"^"+Constants.logDateTime()+"^"+session.getId()+"^"+(resultVal==null?"":resultVal)+"^"+Constants.VERSION_NUM_2+"^"+Constants.VERSION_DATE;
//			logstr = handleLogArr(userBean.getUserId(),userBean.getUserName(),sortName,request.getRemoteAddr(),Constants.browser(request.getHeader("user-agent")),
//					request.getRequestURI(),(log==null?"":log.getActioncode()),paramstr,uuid,Constants.logDateTime(),session.getId(),resultVal,Constants.VERSION_NUM_2,Constants.VERSION_DATE);
//		}else if(userId != null){
////			logstr = userId+"^"+""+"^"+""+"^"+request.getRemoteAddr()+"^"+Constants.browser(request.getHeader("user-agent"))+
////			"^"+request.getRequestURI()+"^"+(log==null?"":log.getActioncode())+"^"+paramstr+"^"+uuid+"^"+Constants.logDateTime()+"^"+session.getId()+"^"+(resultVal==null?"":resultVal)+"^"+Constants.VERSION_NUM_2+"^"+Constants.VERSION_DATE;
//			logstr = handleLogArr(userId,"","",request.getRemoteAddr(),Constants.browser(request.getHeader("user-agent")),
//			request.getRequestURI(),(log==null?"":log.getActioncode()),paramstr,uuid,Constants.logDateTime(),session.getId(),resultVal,Constants.VERSION_NUM_2,Constants.VERSION_DATE);
//		}else{
////			logstr = ""+"^"+""+"^"+""+"^"+request.getRemoteAddr()+"^"+Constants.browser(request.getHeader("user-agent"))+
////			"^"+request.getRequestURI()+"^"+(log==null?"":log.getActioncode())+"^"+paramstr+"^"+uuid+"^"+Constants.logDateTime()+"^"+session.getId()+"^"+(resultVal==null?"":resultVal)+"^"+Constants.VERSION_NUM_2+"^"+Constants.VERSION_DATE;
//			logstr = handleLogArr("","","",request.getRemoteAddr(),Constants.browser(request.getHeader("user-agent")),
//			request.getRequestURI(),(log==null?"":log.getActioncode()),paramstr,uuid,Constants.logDateTime(),session.getId(),resultVal,Constants.VERSION_NUM_2,Constants.VERSION_DATE);
//		}
//		logger.error(formatReturn(logstr));
//		
//	}
	private static void logDefaultInfoError(HttpServletRequest request,HttpSession session,String url,String paramstr){
		String uuid = (String) request.getAttribute("actionid");
//		String logstr = ""+"^"+""+"^"+""+"^"+request.getRemoteAddr()+"^"+Constants.browser(request.getHeader("user-agent"))+
//		"^"+request.getRequestURI()+"^"+"2"+"^"+paramstr+"^"+uuid+"^"+Constants.logDateTime()+"^"+session.getId()+"^"+""+"^"+Constants.VERSION_NUM_2+"^"+Constants.VERSION_DATE;
		String logstr = handleLogArr("","","",request.getRemoteAddr(),Constants.browser(request.getHeader("user-agent")),
		request.getRequestURI(),"2",paramstr,uuid,Constants.logDateTime(),session.getId(),"",Constants.VERSION_NUM_2,Constants.VERSION_DATE);
		logger.error(formatReturn(logstr));
	}
	
	/**
	 * 根据url获取日志字典对象
	 * @param session
	 * @param url
	 * @return
	 */
//	public static LogDic getLogDicByUrl(String url){
////		return LogManage.getLogDic(url);
//		return null;
//	}
	/**
	 * 过滤日志内容
	 * 部分特殊字符会导致日志格式异常，需要清理，
	 * 此处为统一处理
	 * 后续针对各个字段进行处理，去除^和`
	 * @param str
	 * @return
	 */
	public static String formatReturn(String str){
		if(str == null) return null;
		return str.replaceAll("(\r\n|\r|\n|\n\r)", " ");
	}
	public static void main(String[] args) {
		String a="mininglamp^开发人\r员^河北省分行^0:0:0:0:0:0:0:1^chrome/41^/authmgr/rightManage/getResInfoList.action^ea18ba718fb0406a82ce7d235a14f0ef^^20160317193305^1^20151010";
		System.out.println(formatReturn(a));
	}
	/**
	 * 日志内容处理
	 * @param strings
	 * @return
	 */
	public static String handleLogArr(String ...strings){
		if(strings == null) return "";
		StringBuilder sb = new StringBuilder();
		for (String string : strings) {
			sb.append(filterStr(string));
			sb.append(Const.LOG_COLUMN_SEPARATOR);
		}
		try{
			if(sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
		}catch(Exception e){
			e.printStackTrace();
		}
		return sb.toString();
	}
	/**
	 * Filter the log column to remove invalid character.
	 * @param str
	 * @return
	 */
	public static String filterStr(String str){
		return str == null?null:str.replaceAll("\\"+Const.LOG_COLUMN_SEPARATOR, Const.LOG_COLUMN_SHIFT_STR);
	}
}
