package com.mininglamp.currencySys.common.interceptor;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.mininglamp.currencySys.user.bean.MemoryData;
import com.mininglamp.currencySys.user.service.UserService;
/**
 * 权限拦截器
 * @author czy
 * 2016年9月19日15:55:36
 */
public class SecurityInterceptor implements HandlerInterceptor{
	private static final Logger logger = Logger.getLogger(SecurityInterceptor.class);
	
	private NamedThreadLocal<Long>  startTimeThreadLocal = new NamedThreadLocal<Long>("StopWatch-StartTime");  
	@Autowired
	private UserService userService;
	
	private List<String> excludeUrls;//不需用拦截的
	
	public List<String> getExcludeUrls() {
		return excludeUrls;
	}

	public void setExcludeUrls(List<String> excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	public void afterCompletion(HttpServletRequest request,HttpServletResponse response, Object object, Exception exception)
			throws Exception {
		long endTime = System.currentTimeMillis();//2、结束时间  
        long beginTime = startTimeThreadLocal.get();//得到线程绑定的局部变量（开始时间）  
        long consumeTime = endTime - beginTime;//3、消耗的时间  
//        if(consumeTime > 500) {//此处认为处理时间超过500毫秒的请求为慢请求  
	        //TODO 记录到日志文件  
	        System.out.println(  
        		String.format("%s consume %d millis", request.getRequestURI(), consumeTime));  
//        }    
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response,Object object, ModelAndView modelAndView) throws Exception {
	}
	/**
	 * 调用controller前调用
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object object) throws Exception {
		long beginTime = System.currentTimeMillis();//1、开始时间  
        startTimeThreadLocal.set(beginTime);//线程绑定变量（该数据只有当前请求的线程可见）
        
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String url = requestUri.substring(contextPath.length());
		logger.debug(url);
		HttpSession session = request.getSession();
		String SessionId = session.getId();
		String username = (String) session.getAttribute("username");
		String LoginSessionId = MemoryData.getSessionIDMap().get(username);
		if(!SessionId.equals(LoginSessionId)){
		    if(!"/currencySys/index".equals(requestUri) && !excludeUrls.contains(url)){
		    	java.io.PrintWriter out = response.getWriter();  
				out.println("<html>");  
			    out.println("<script>");
		    	out.println(new String("alert('您的账号已在其他地方登录或者登录超时！！！');".getBytes("UTF-8"),"UTF-8"));  
		    	out.println("window.open ('/currencySys/login','_top')");  
			    out.println("</script>");  
			    out.println("</html>");
			    out.close();
			    response.setContentType("text/html;charset=UTF-8");// 解决中文乱码
		    }else{
		    	/*response.sendRedirect(request.getContextPath() + "/login");*/
		    }
			return false;
		}
		/*String ip = (String) session.getAttribute("ip");*/
		String authority = (String) session.getAttribute("authority");
		if(StringUtils.isNotBlank(username) && StringUtils.isNotBlank(LoginSessionId) && StringUtils.isNotBlank(authority) /*&& StringUtils.isNotBlank(ip)*/){
			/**
			 * 放行baseController和exclude的请求
			 */
			if(url.indexOf("baseController") > -1||excludeUrls.contains(url)){
				return true;
			}
			
			//四位表名的标识 ps:http://127.0.0.1:8080/currencySys/manaProController/zsycIndex
			int lastIndexOf = requestUri.lastIndexOf("/");
			String tableName = requestUri.substring((lastIndexOf + 1), requestUri.length()-5);
			//判断角色
			if("/currencySys/index".equals(requestUri)){
				return true;
			}else{
				if(SessionId.equals(LoginSessionId)){
					//管理员
					if ("all".equals(authority)) {
						return true;
					}else if(authority.contains(tableName)){
						return true;
					}else{
						/*response.sendRedirect(request.getContextPath() + "/login");
						return false;*/
						return true;
					}
				}else{
					java.io.PrintWriter out = response.getWriter();  
					out.println("<html>");  
				    out.println("<script>");  
				    out.println("window.open ('/currencySys/login','_top')");  
				    out.println("</script>");  
				    out.println("</html>");
				    out.close();
					return false;
				}
			}
		} else {
			MemoryData.getSessionIDMap().remove(username);//清除
			response.sendRedirect(request.getContextPath() + "/login");
			return false;
		}
		
		/**
		 * 验证分为两种
		 * 1、本系统登陆，会存在session维持连接
		 * 2、外部系统调用，需要用token维持连接，并绑定请求IP @Depreted
		 * 3、外部系统调用，Header包含Mws-Appcode
		 *//*
		//没有session则认为为外部调用(内部调用已过滤登陆动作)
//			String token = request.getHeader("Mws-Token");
//			String ip = WebUtil.getIpAdd(request);
//			int tokenNum = SecurityUtil.validateToken(token, ip);
//			if(tokenNum == 0){
//				return true;
//			}
		*//**
		 * 验证appCode
		 *//*
		boolean appCodeFlag = SecurityUtil.validateAppcode(request);
		if(appCodeFlag) return true;
		
		//继续验证用户名和密码是否存在
//		return SecurityUtil.validateUser(request,response);
		return SecurityUtil.validateSession(request.getSession(false));*/
	}
}
