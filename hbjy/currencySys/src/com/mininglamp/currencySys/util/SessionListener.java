package com.mininglamp.currencySys.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 监听session创建和销毁
 * @author Administrator
 *
 */
public class SessionListener implements HttpSessionListener,ServletContextListener,ServletRequestListener{
	
	/**
	 * 保存所有有效session信息
	 */
	private static Set<HttpSession> sessions = new HashSet<HttpSession>(); 
	
	/**
	 * 缓存在线数量值
	 */
	public static List<Map<String,Long>> cachOnLineSession = new ArrayList<Map<String,Long>>();
	
	/**
	 * 缓存登陆数量值
	 */
	public static List<Map<String,Long>> cachLoginSession = new ArrayList<Map<String,Long>>();
	/**
	 * 是否加载日志字典
	 */
	private boolean loadLogdic = false;
	/**
	 * 创建session
	 */
	@Override
	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		HttpSession session = httpSessionEvent.getSession();
		sessions.add(session);
		calculationCach(session,true);
		if(!loadLogdic){
//			LogManage.saveDicToServletContext(session.getServletContext());
			loadLogdic = true;
		}
	}
	
	/**
	 * 销毁session
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		HttpSession session = httpSessionEvent.getSession();
		sessions.remove(session);
		calculationCach(session,false);
	}
	
	/**
	 * 计算缓存数据
	 */
	public synchronized static void calculationCach(HttpSession session, boolean isAdd){
		long currenttime = System.currentTimeMillis();
		
		Map<String,Long> map = new HashMap<String,Long>();
		map.put("time", currenttime);
		map.put("num", Long.valueOf(sessions.size()+""));
		cachOnLineSession.add(map);
		Map<String,Long> ms = new HashMap<String,Long>();
		ms.put("time", currenttime);
		ms.put("num", (long)getLoginSessionsInfo().size());
		cachLoginSession.add(ms);
		
		removeOverTime(cachOnLineSession, currenttime);
		removeOverTime(cachLoginSession, currenttime);
	}
	
	/**
	 * @param list
	 * @return 去除超时的缓存数据
	 */
	public static List<Map<String,Long>> removeOverTime(List<Map<String,Long>> list, long currenttime){
		Iterator<Map<String, Long>> it = list.iterator();
		while(it.hasNext()){
			Map<String,Long> map = it.next();
			if((currenttime-map.get("time")) >= 24*60*60*1000){
				it.remove();
			}
		}
		return list;
	}
	
	/**
	 * 获取session
	 * @return
	 */
	public static  Set<HttpSession> getAllSessions (){
		return sessions;
	}
	/**
	 * 所有session属性
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Map> getAllSessionsInfo(){
		List<Map> list=new ArrayList<Map>();
		for (HttpSession session : sessions) {
			try {
				Map<String, Object> map = new HashMap<String, Object>();
				Enumeration enumeration = session.getAttributeNames();
				Map<String, Object> attrMap = new HashMap<String, Object>();
				while (enumeration.hasMoreElements()) {
					Object obj = enumeration.nextElement();
					if (obj instanceof String) {
						attrMap.put(obj.toString(), session.getAttribute(obj
								.toString()));
					}
				}
				map.put("attrs", attrMap);
				map.put("sessionId", session.getId());
				map.put("createTime", session.getCreationTime());
				map.put("lastAccessedTime", session.getLastAccessedTime());
				map.put("maxInactiveInterval", session.getMaxInactiveInterval());
				map.put("time", new Date().getTime());
				list.add(map);
			} catch (Exception e) {
				sessions.remove(session);
				LoggerUtil.error("获取session信息错误", e);
				throw new RuntimeException(e);
			}
		}
		return list;
	}
	/**
	 * 登陆session属性
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Map> getLoginSessionsInfo(){
		List<Map> list=getAllSessionsInfo();
		List<Map> resultList=new ArrayList<Map>();
		for (Map map : list) {
			if(map.get("attrs")!=null&&((Map)map.get("attrs")).get("userBean")!=null) resultList.add(map);
		}
		return resultList;
	}
	/**
	 * 失效指定session
	 * @param idArr id数组
	 * @return
	 */
	public static boolean invalidateSessions (String[] idArr){
		try {
			for (HttpSession session : sessions) {
				if (Arrays.binarySearch(idArr, session.getId()) > -1) {
					session.invalidate();
					sessions.remove(session);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
	}
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
	}
	@Override
	public void requestDestroyed(ServletRequestEvent requestEvent) {
	}
	@Override
	public void requestInitialized(ServletRequestEvent requestEvent) {
		try{
			HttpServletRequest request = (HttpServletRequest) requestEvent.getServletRequest();
			HttpSession session = request.getSession(true);
			if(session.getAttribute("ip") == null) {
				String ip = WebUtil.getIpAdd(request);
				session.setAttribute("ip", ip);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
