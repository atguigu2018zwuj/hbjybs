package com.mininglamp.currencySys.jrjg.controller;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mininglamp.currencySys.common.base.bean.ResponseBean;
import com.mininglamp.currencySys.common.base.controller.BaseController;
import com.mininglamp.currencySys.common.util.CommonValidateUtil;
import com.mininglamp.currencySys.insertData.service.InsertDataService;
import com.mininglamp.currencySys.jrjg.service.JrjgService;

import net.sf.json.JSONArray;
/**
 * 
 * <p>Description:金融机构现新增业务报表控制层 </p>
 * @author zrj
 * @date 2019年1月16日
 * @version 1.0
 */
@Controller
@RequestMapping(value="/jrjgController")
public class jrjgController extends BaseController {
	SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	private JrjgService jrjgService;
	@Autowired
	private InsertDataService insertDataService;
	
	/**
	 * 查询
	 * @param request
	 * @param response
	 * @param sqlId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getCodeData")
	@ResponseBody
	public HashMap<String, Object> getCodeData(HttpServletRequest request, HttpServletResponse response,String sqlId) throws Exception {
		long start = System.currentTimeMillis();
		HashMap<String, Object> hashMap = null;
		try {
			hashMap = new HashMap<String, Object>();
			Map<String, Object> params = getParamters(request);
			Long page = Long.valueOf((String) params.get("page"));//第几页的数据 
			Long rows = Long.valueOf((String) params.get("rows"));//每页多少条数据  
			params.put("minData", (rows * ( page- 1))+"");
			params.put("maxData", ((page) * rows )+"");
			sqlId = (String) params.get("selectSqlId");
			String nbjgh = (String) request.getSession().getAttribute("nbjgh");
			String username = (String) request.getSession().getAttribute("username");
			if("admin".equals(username)){
				params.put("DataAuthNBJGH", "");
			}else{
				params.put("DataAuthNBJGH", nbjgh);
			}
			List<Map> dataList = jrjgService.getDataList(sqlId, params);
			List<Map> dataCount = jrjgService.getDataCount(sqlId+"Count", params);
			hashMap.put("rows", dataList);
			hashMap.put("total", dataCount.get(0).get("COUNT"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("查询共用"+(end-start)+"毫秒");
		return hashMap;
	}
	
	/**
	 * 更新
	 * @param request
	 * @param response
	 * @param sqlId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/updateData")
	@ResponseBody
	public ResponseBean updateData(HttpServletRequest request, HttpServletResponse response,String sqlId) throws Exception {
		HttpSession session = request.getSession();
		Map<String, Object> params = getParamters(request);
		ResponseBean result = null;
		try {
			result = new ResponseBean();
			//获取当前用户内部机构号
			String nbjgh = (String) session.getAttribute("nbjgh");
			params.put("nbjgh", nbjgh);
			//获取当前用户名
			String username = (String) session.getAttribute("username");
			//获取当前用户可用内部机构号
			List<Map> AllAuth = insertDataService.getData("getAllAuth", params);
			ArrayList<String> nbjghList = new ArrayList<String>();
			for (Map map : AllAuth) {
				nbjghList.add(String.valueOf(map.get("NBJGH")));
			}
			//获取当前表的内部机构号
			List<Map> Table_nbjgh = insertDataService.getData("getTable_nbjgh", params);
			//数据的内部机构号
			String table_nbjgh = (String)params.get(Table_nbjgh.get(0).get("TABLE_NBJGH"));
			if(!"admin".equals(username) && !nbjghList.contains(table_nbjgh)){
				result.setData("您无权录入"+table_nbjgh+"此内部机构号");
				return result;
			}
			params.put("last_row", JSONArray.fromObject(params.get("last_row")));
			//获取当前修改的上报类型值
			String SBLXValue = (String) params.get("SBLX");
			//检验经常性报表  上报类型是否符合当前用户
			String validateInfo = CommonValidateUtil.validateSBLX(SBLXValue,(String)params.get("tableName"), request);
			if (StringUtils.isNotEmpty(validateInfo)) {
				result.setData(validateInfo);
				return result;
			}
			
			sqlId = (String) params.get("UpdateSqlId");
			result.setData(jrjgService.updateData(sqlId, params));
			// 记录操作日志
			if ((int)result.getData() > 0) {
				insertRecordOperationLog((String)params.get("fileName"), Integer.parseInt((String)params.get("CODE")), username, "2");
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(e.toString().contains("ORA-00001")){
				result.setData("违反唯一约束异常！");
			}else{
				result.setData("异常");
			}
		}
		return result;
	}
	
	/**
	 * 删除
	 * @param request
	 * @param response
	 * @param sqlId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteData")
	@ResponseBody
	public ResponseBean deleteData(HttpServletRequest request, HttpServletResponse response,String sqlId) throws Exception {
		Map<String, Object> params = getParamters(request);
		ResponseBean result = null;
		try {
			String username = (String) request.getSession().getAttribute("username");
			params.put("deleted", JSONArray.fromObject(params.get("deleted")));
			result = new ResponseBean();
			sqlId = (String) params.get("delSqlId");
			result.setData(jrjgService.deleteData(sqlId, params));
			// 记录操作日志
			if ((int)result.getData() > 0) {
				JSONArray deleted = JSONArray.fromObject(params.get("deleted"));
				insertRecordOperationLog((String)params.get("fileName"), (deleted.size() > 0 ? deleted.getJSONObject(0).getInt("CODE") : 0), username, "3");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
