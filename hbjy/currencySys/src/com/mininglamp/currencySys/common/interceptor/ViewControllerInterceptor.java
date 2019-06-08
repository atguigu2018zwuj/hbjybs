package com.mininglamp.currencySys.common.interceptor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.mininglamp.currencySys.user.service.UserService;

/**
 * 菜单页面访问拦截器
 * @author 陈翔宇
 */
public class ViewControllerInterceptor implements HandlerInterceptor{
	private static final Logger logger = Logger.getLogger(ViewControllerInterceptor.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss.S");
	@Autowired
	private UserService userService;
	
	/**
	 * 加载完页面后调用，此时记录用户访问菜单的日志
	 */
	@SuppressWarnings("rawtypes")
	public void afterCompletion(HttpServletRequest request,HttpServletResponse response, Object object, Exception exception) throws Exception {
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		String url = requestUri.substring(contextPath.length());
		logger.debug(url);
		if (StringUtils.isEmpty(url)) {
			logger.error("时间【"+sdf.format(new Date())+"】---- 用户【"+username+"】取得的url为空");
			return;
		}
		
		String urlViewKey = url.substring(url.lastIndexOf("/")+1).replace("Index", "");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codeKey", urlViewKey);
		// 包含当前目录及其所有上级目录的名字
		String urlViewName = null;
		// 根据code_key取得所有上级目录
		List<Map> dataList = userService.getDataList(params, "getUserAuthorityParent");
		if (dataList != null && !dataList.isEmpty()) {
			StringBuilder urlViewNameSB = new StringBuilder();
			for (Map data : dataList) {
				urlViewNameSB.append(data.get("CODE_VALUE"));
				urlViewNameSB.append("-");
			}
			urlViewName = urlViewNameSB.toString().replaceAll("\\-$", "");
		}
		logger.info("ViewAccess ====== 时间【"+sdf.format(new Date())+"】---- 用户【"+username+"】访问路径【"+(urlViewName != null && StringUtils.isNotEmpty(urlViewName) ? urlViewName : urlViewKey)+"】");
		// TODO 存储到访问日志表中
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response,Object object, ModelAndView modelAndView) throws Exception {
	}
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object object) throws Exception {
		return true;
	}
}
