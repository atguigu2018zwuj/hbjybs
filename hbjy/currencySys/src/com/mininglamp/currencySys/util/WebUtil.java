package com.mininglamp.currencySys.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

/**
 * 
 * @author falcon
 *	创建工具方法
 *	add by cxk 2015年3月10日10:25:09
 */


public class WebUtil
{
	public static void main(String[] args) {
//		 System.out.println(WebUtil.getIpAdd());
	}
    /**
     * 取得HttpSession的简化方法.
     */
    public static HttpSession getSession()
    {
        return ServletActionContext.getRequest().getSession();
    }
    
    /**
     * 取得HttpRequest的简化方法.
     */
    public static HttpServletRequest getRequest()
    {
        return ServletActionContext.getRequest();
    }
    
    /**
     * 取得HttpResponse的简化方法.
     */
    public static HttpServletResponse getResponse()
    {
        return ServletActionContext.getResponse();
    }
    
    /**
     * 获取HttpSession Attribute的简化方法.
     * 
     * @param keyStr
     * @return Object
     */
    public static Object getSessionAttribute(String keyStr)
    {
        return getSession().getAttribute(keyStr);
    }
    
    /**
     * 在非action中获取HttpSession Attribute(使用DWR调用的方法)
     * 
     * @param keyStr
     * @param request
     * @return Object
     * @author l00137148
     */
    public static Object getSessionAttribute(String keyStr, HttpServletRequest request)
    {
        return request.getSession().getAttribute(keyStr);
    }
    
    /**
     * 设置HttpSession Attribute的简化方法.
     * 
     * @param keyStr
     * @param object
     */
    public static void setSessionAttribute(String keyStr, Object object)
    {
        getSession().setAttribute(keyStr, object);
    }
    
    /**
     * 设置HttpSession Attribute的简化方法.
     * 
     * @param keyStr
     * @param object
     */
    public static void setSessionAttribute(HttpSession session, String keyStr, Object object)
    {
        session.setAttribute(keyStr, object);
    }
    
    /**
     * 移除HttpSession Attribute的简化方法.
     * 
     * @param keyStr
     */
    public static void removeSessionAttribute(String keyStr)
    {
        getSession().removeAttribute(keyStr);
    }
    
    /**
     * 获取客户端IP地址
     * 
     * @return String 客户端IP地址
     */
    public static String getIpAddress()
    {
        return getRequest().getRemoteAddr();
    }

    /**
     * 获取客户端IP地址  使用反向代理，获取真实的客户端IP
     * @return String 客户端IP地址
     */
    public static String getIpAdd()
    {
        String ip = getRequest().getHeader("x-forwarded-for");
        //add begin yKF53986 2012/01/12 R003C11L12n01 
        //在反向代理 X-Forwarded-For中获取客户端真实的IP地址
        if (null != ip && 0 < ip.length() && !"unknown".equalsIgnoreCase(ip))
        {
            String[] ipArr = ip.split(",");
            for (int i = 0; i < ipArr.length; i++)
            {
                if (!"unknown".equalsIgnoreCase(ipArr[i]) && !"".equals(ipArr[i].trim()))
                {
                    ip = ipArr[i];
                    break;
                }
            }
        }
        //add begin yKF53986 2012/01/12 R003C11L12n01 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = getRequest().getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = getRequest().getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = getRequest().getRemoteAddr();
        }
        return ip;
    }

    public static String getIpAdd(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		// add begin yKF53986 2012/01/12 R003C11L12n01
		// 在反向代理 X-Forwarded-For中获取客户端真实的IP地址
		if (null != ip && 0 < ip.length() && !"unknown".equalsIgnoreCase(ip)) {
			String[] ipArr = ip.split(",");
			for (int i = 0; i < ipArr.length; i++) {
				if (!"unknown".equalsIgnoreCase(ipArr[i])
						&& !"".equals(ipArr[i].trim())) {
					ip = ipArr[i];
					break;
				}
			}
		}
		// add begin yKF53986 2012/01/12 R003C11L12n01
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
    
    @SuppressWarnings("unchecked")
	public static String getRequestParameters(HttpServletRequest request,String sep){
		if(request == null) return null;
		StringBuilder param= new StringBuilder();
		for (Enumeration<String> eh = request.getParameterNames(); eh.hasMoreElements();) {
			String parName = eh.nextElement();
			Object parValueObj = request.getParameter(parName);
			String parValue = "";
			if(parValueObj != null)	parValue = parValueObj.toString();
			
			if(parName!=null) parName = parName.replaceAll(sep, "_-_");
			if(parValue!=null) parValue = parValue.replaceAll(sep, "_-_");
			
			param.append(parName+"="+parValue+sep);
		}
		if(param.length()>0) param = param.deleteCharAt(param.length()-1);
		
		return param.toString();
	}
    
    
    
}
