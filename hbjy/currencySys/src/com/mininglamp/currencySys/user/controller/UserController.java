package com.mininglamp.currencySys.user.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mininglamp.currencySys.common.base.controller.BaseController;
import com.mininglamp.currencySys.user.bean.MemoryData;
import com.mininglamp.currencySys.user.service.UserService;

@Controller
@RequestMapping(value="/login")
public class UserController extends BaseController{

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/getUserData")
	@ResponseBody
	public List<Map<String, Object>> getUserData1(HttpServletRequest request, HttpServletResponse response, String sqlId)
			throws Exception {
		long start = System.currentTimeMillis();
		Map<String, Object> params = getParamters(request);
		HttpSession session = request.getSession();
		String authority = (String) session.getAttribute("authority");
		String SessionId = session.getId();
		String username = (String) session.getAttribute("username");
		String LoginSessionId = MemoryData.getSessionIDMap().get(username);
		//判断SessionId是否相同 相同侧说明同一用户 
		if(SessionId.equals(LoginSessionId)){
			String[] split = null ;
			///如果权限为空 侧说明 session消失
			if(authority == null){
				return null;
			}else{
				if(authority != null && !"all".equals(authority)){
					split = authority.split(",");
					params.put("authority", split);
				}
				List<Map> dataList = userService.getUserList(params,"getUSER_AUTHORITY");
				List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
				for (Map map : dataList) {
					arrayList.add(map);
				}
				List<Map<String, Object>> tree = getTree(arrayList,"0","id");
				long end = System.currentTimeMillis();
				return tree;
			}
		}else{
			return null;
		}
	}
	
	
	/** 
	 * 
	 * @param list 所有元素的平级集合，map包含id和pid 
	 * @param pid 顶级节点的pid，可以为null 
	 * @param idName id位的名称，一般为id或者code 
	 * @return 树 
	 */  
	public static List<Map<String, Object>> getTree(List<Map<String, Object>> list, String pid, String idName) {  
	    List<Map<String, Object>> res = new ArrayList<Map<String,Object>>();  
	    if (CollectionUtils.isNotEmpty(list))  
	        for (Map<String, Object> map : list) {  
	            if(pid == null && map.get("p"+idName) == null || map.get("p"+idName) != null && map.get("p"+idName).equals(pid)){  
	                String id = (String) map.get(idName);  
	                map.put("children", getTree(list, id, idName));  
	                res.add(map);  
	            }  
	        }  
	    return res;  
	}  
	
}
