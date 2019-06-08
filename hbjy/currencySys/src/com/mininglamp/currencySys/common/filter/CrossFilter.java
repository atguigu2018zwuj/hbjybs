package com.mininglamp.currencySys.common.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
/**
 * 跨域配置[ie7-不兼容]
 * @author czy
 * 2016年9月23日16:29:18
 */
public class CrossFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET,PUT,DELETE,POST");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setHeader("Access-Control-Max-Age", "1800");
		if("OPTIONS".equals(request.getMethod())){
			response.setStatus(HttpStatus.OK.value());
		}
		chain.doFilter(request, response);
	}

}
