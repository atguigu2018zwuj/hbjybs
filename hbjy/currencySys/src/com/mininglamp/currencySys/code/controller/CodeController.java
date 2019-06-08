package com.mininglamp.currencySys.code.controller;
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
import com.mininglamp.currencySys.code.service.CodeMapService;
import com.mininglamp.currencySys.common.base.bean.ResponseBean;
import com.mininglamp.currencySys.common.base.controller.BaseController;
import com.mininglamp.currencySys.common.util.StringUtils;
import com.mininglamp.currencySys.userManage.service.UserManageService;

import net.sf.json.JSONArray;



/**
 * 码表用
 * @author Cheng
 * @time 2017/8/22
 * @QQ 452050507
 */
@Controller
@RequestMapping(value="/code")
public class CodeController extends BaseController{

	@Autowired
	private CodeMapService codeMapService;
	@Autowired
	UserManageService userManageService;
	
	/**
	 * 根据sqlid获取码表数据 编辑通用
	 * @param request
	 * @param response
	 * @param sqlId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getData")
	@ResponseBody
	public JSONArray getData(HttpServletRequest request, HttpServletResponse response,String sqlId) throws Exception {
		Map<String, Object> params = getParamters(request);
		HttpSession session = request.getSession();
		String nbjgh = (String) session.getAttribute("nbjgh");
		params.put("nbjgh", nbjgh);
		List<Map> dataList = codeMapService.getDataList(sqlId, params);
		JSONArray fromObject = JSONArray.fromObject(dataList);
		return fromObject;
	}
	
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
	public ResponseBean getArea(HttpServletRequest request, HttpServletResponse response,String sqlId) throws Exception {
		Map<String, Object> params = getParamters(request);
		ResponseBean result = new ResponseBean();
		result.setData(codeMapService.getDataList(sqlId, params));
		return result;
	}
	
	public static List<Map<String, Object>> getTree(List<Map<String, Object>> list, String pid, String idName) {  
	    List<Map<String, Object>> res = new ArrayList<Map<String,Object>>();  
	    if (CollectionUtils.isNotEmpty(list))  
	        for (Map<String, Object> map : list) {  
	            if(pid == null && map.get("p"+idName) == null || map.get("p"+idName) != null && map.get("p"+idName).equals(pid)){  
	                String id = (String) map.get(idName);  
	                List<Map<String, Object>> tree = getTree(list, id, idName);
	                map.put("children", tree);  
	                if(!tree.isEmpty()){
	                	map.put("state", "closed");
	                }
	                res.add(map);  
	            }  
	        }  
	    return res;  
	} 
	
	/**
	 * 获取hbjyods.jrjgxx
	 * @param request
	 * @param response
	 * @param sqlId
	 * @return 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getJg")
	@ResponseBody
	public JSONArray getJg(HttpServletRequest request, HttpServletResponse response,String sqlId) throws Exception {
		Map<String, Object> params = getParamters(request);
		ResponseBean result = new ResponseBean();
		// 只能看到当前用户的机构及其下级机构的数据（若取不到当前用户的内部机构号，默认从省联社开始查询）
		params.put("topNbjgh", StringUtils.isNotEmpty(request.getSession().getAttribute("nbjgh")) ? request.getSession().getAttribute("nbjgh") : "1699999998");
		List<Map> dataList = codeMapService.getDataList(sqlId, params);
		List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		String treePid = null;
		for (Map map : dataList) {
			arrayList.add(map);
			if (params.get("topNbjgh").equals(map.get("id"))) {
				treePid = StringUtils.parseString(map.get("pid"));
			}
		} 
		
		List<Map<String, Object>> tree = getTree(arrayList,treePid,"id");
		JSONArray fromObject = JSONArray.fromObject(tree);
		return fromObject;
	}
	
	/**
	 * 取得报表配置信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getCodeConfigInfo")
	@ResponseBody
	public ResponseBean getCodeConfigInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, Object> params = getParamters(request);
		ResponseBean result = new ResponseBean();
		result.setData(codeMapService.getDataList("getUserAuthorityInfo", params));
		return result;
	}
}
