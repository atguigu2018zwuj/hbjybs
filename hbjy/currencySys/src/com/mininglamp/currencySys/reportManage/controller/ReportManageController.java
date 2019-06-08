package com.mininglamp.currencySys.reportManage.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.mininglamp.currencySys.common.base.bean.ResponseBean;
import com.mininglamp.currencySys.common.base.controller.BaseController;
import com.mininglamp.currencySys.common.util.StringUtils;
import com.mininglamp.currencySys.reportManage.service.ReportManageService;
import com.mininglamp.currencySys.user.service.UserService;
@Controller
@RequestMapping("reportManage")
public class ReportManageController extends BaseController{
	
	@Autowired 
	ReportManageService reportManageService;
	@Autowired
	private UserService userService;
	
	@RequestMapping("reportManageIndex")
	public String reportManageIndex() {
		return "reportManage/reportManage";
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
	
	/*
	 * 报送管理数据查询
	 */
	@RequestMapping("reportManageData")
	@ResponseBody
	public List<Map<String, Object>> fileManageData(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws Exception {
		// 当前登录单位编码
		String bsdwBm = (String) session.getAttribute("userszdwbm");
		// 当前登录单位级别
		Object bsdwJb = session.getAttribute("userszdwjb");
		Map<String, Object> params = getParamters(request);
		String bsjg = (String) params.get("bsjg");
		String nbjgh = (String) request.getSession().getAttribute("nbjgh");
		String username = (String) request.getSession().getAttribute("username");
		if(!"admin".equals(username)){
			params.put("nbjgh", nbjgh);
			params.put("username", username);
		}
		params.put("bsdwBm", bsdwBm);
		// 根据用户名取得机构级别
		params.put("loginName", username);
		List<Map> userList = userService.getUserList(params, "getUsermanager");
		int jgjb = 0;// 实际级别从1开始
		if (userList != null && !userList.isEmpty() && StringUtils.isNotEmpty(userList.get(0).get("ALEVEL"))) {
			jgjb = Integer.parseInt(String.valueOf(userList.get(0).get("ALEVEL")));
			// 机构级别：只显示到当前级别及其下面两级
			params.put("level", String.valueOf(jgjb));
			params.put("levelTwo", String.valueOf(jgjb+1));
			params.put("levelThree", String.valueOf(jgjb+2));
		}
		// 报送管理文件数据
		List<Map> reportManageData = reportManageService.getDataList(params, "fileManageData");
		params.put("bsdwBm", "");
		List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		for (Map map : reportManageData) {
			arrayList.add(map);
		} 
		List<Map<String, Object>> tree = getTree(arrayList,jgjb == 0 ? "0" : String.valueOf(userList.get(0).get("SJNBJGH")),"id");
		return tree;
	}
	
	/*@RequestMapping("reportManageData")
	@ResponseBody
	public List<Map> fileManageData(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws Exception {
		// 当前登录单位编码
		String bsdwBm = (String) session.getAttribute("userszdwbm");
		// 当前登录单位级别
		Object bsdwJb = session.getAttribute("userszdwjb");
		Map<String, Object> params = getParamters(request);
		String bsjg = (String) params.get("bsjg");
		params.put("ID", "0");
		params.put("bsdwBm", bsdwBm);
		// 报送管理文件数据
		List<Map> reportManageData = reportManageService.getDataList(params, "fileManageData");
		params.put("bsdwBm", "");
		List<Map> buildTree = buildTree(reportManageData,params); 
		return reportManageData;
	}*/

	/**
	 * 根据sqlid获取码表数据
	 * @param request
	 * @param response
	 * @param sqlId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getCodeData")
	@ResponseBody
	public ResponseBean getArea(HttpServletRequest request,HttpServletResponse response, String sqlId) throws Exception {
		SimpleDateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> params = getParamters(request);
		ResponseBean result = new ResponseBean();
		String[] split = null;
		String authority = (String) request.getSession().getAttribute("authority");
		if(authority != null && !"all".equals(authority)){
			split = authority.split(",");
		}
		params.put("authority", split);
		result.setData(reportManageService.getDataList(params, sqlId));
		return result;
	}
	
	/**
	 * 循环遍历每个父类下面的子类
	 * @param root
	 * @param params2
	 * @return
	 * @throws Exception
	 */
	public List<Map> buildTree(List<Map> root, Map<String, Object> params2) throws Exception {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd");
		ArrayList<Map> arrayList = new ArrayList<Map>();
		for (int i = 0; i < root.size(); i++) {
			params2.put("ID", root.get(i).get("id"));
			String id = (String) root.get(i).get("id");
			params2.put("sign", "sign");
			List<Map> children = reportManageService.getDataList(params2,"fileManageData"); // 查询某节点的子节点（获取的是list）
			if(children.size() > 0){
//				if(id.length() < 2){
					buildTree(children,params2);
//				}
				root.get(i).put("children", children);
				arrayList.add(root.get(i));
			}
		}
		return arrayList;
	}

}
