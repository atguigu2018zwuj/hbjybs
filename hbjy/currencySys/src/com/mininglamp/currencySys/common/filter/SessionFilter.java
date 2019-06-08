package com.mininglamp.currencySys.common.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mininglamp.currencySys.user.bean.MemoryData;


public class SessionFilter implements Filter{

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession();
		// 登陆url
		String loginUrl = httpRequest.getContextPath() + "/login";
		String logoutUrl = httpRequest.getContextPath() + "/logout";
		String url = httpRequest.getRequestURI();
		String username = (String) session.getAttribute("username");
		// 超时处理，ajax请求超时设置超时状态，页面请求超时则返回提示并重定向
		if (!loginUrl.equals(url) && !logoutUrl.equals(url) && username == null && !url.contains(".")) {
			isAjxa(request, response, chain, httpRequest, httpResponse, loginUrl);
		} else {
			String SessionId = session.getId();
			String LoginSessionId = MemoryData.getSessionIDMap().get(username);
			if(SessionId.equals(LoginSessionId) || loginUrl.equals(url) || logoutUrl.equals(url) || url.contains(".")){
				chain.doFilter(request, response);
			}else{
				isAjxa(request, response, chain, httpRequest, httpResponse, loginUrl);
			}
		}
	}

	/**
	 * 判断是否为ajxa 然后分情况返回
	 * @param request
	 * @param response
	 * @param chain
	 * @param httpRequest
	 * @param httpResponse
	 * @param loginUrl
	 * @throws IOException
	 * @throws ServletException
	 */
	public void isAjxa(ServletRequest request, ServletResponse response, FilterChain chain,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse, String loginUrl)
			throws IOException, ServletException {
		// 判断是否为ajax请求
		if (httpRequest.getHeader("x-requested-with") != null && httpRequest.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
			httpResponse.addHeader("sessionstatus", "timeOut");
			httpResponse.addHeader("loginPath", loginUrl);
			chain.doFilter(request, response);// 不可少，否则请求会出错
		} else {
			String str = "<html><script language='javascript'>alert('会话过期,请重新登录');"
					+ "window.top.location.href='"
					+ loginUrl
					+ "';</script></html>";
			response.setContentType("text/html;charset=UTF-8");// 解决中文乱码
			try {
				PrintWriter writer = response.getWriter();
				writer.write(str);
				writer.flush();
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}
