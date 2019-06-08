package com.mininglamp.currencySys.ManaPro.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.support.json.JSONParser;
import com.mininglamp.currencySys.ManaPro.service.ManaProService;
import com.mininglamp.currencySys.common.base.bean.ResponseBean;
import com.mininglamp.currencySys.common.base.controller.BaseController;
import com.mininglamp.currencySys.insertData.service.InsertDataService;
import com.mininglamp.currencySys.uploadAndDownload.controller.UploadAndAownloadController;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="/manaProController")
public class ManaProCLController extends BaseController{

	SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	private ManaProService manaProService;
	@Autowired
	private InsertDataService insertDataService;
	
	/**
	 * 获取配置表信息
	 * @param request
	 * @param response
	 * @param sqlId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getEditData")
	@ResponseBody
	public void getEditData(HttpServletRequest request, HttpServletResponse response,String sqlId) throws Exception {
		long start = System.currentTimeMillis();
		HttpSession session = request.getSession();
		Map<String, Object> params = getParamters(request);
		List<Map> dataList = null;
		List<Map> codeList = null;
		String editTableName = (String) params.get("editTableName");
		List<Map> attribute = (List<Map>) session.getAttribute(editTableName+"dataList");
		//如果为空 就查询 不会空不查
		if(attribute == null || attribute.isEmpty()){
			try {
				dataList = manaProService.getDataList("getEditData", params);
				//获取那些表有CODE列
				codeList = manaProService.getDataList("getCode", params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			long end = System.currentTimeMillis();
			System.out.println("查询共用"+(end-start)+"毫秒");
			session.setAttribute(editTableName+"dataList", dataList);
			session.setAttribute("codeList", codeList);
		}else{
			System.out.println(editTableName+"---"+attribute.toString());
		}
	}
	
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
			List<Map> dataList = manaProService.getDataList(sqlId, params);
			List<Map> dataCount = manaProService.getDataCount(sqlId+"Count", params);
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
		Map<String, Object> params = getParamters(request);
		ResponseBean result = null;
		HttpSession session = request.getSession();
		System.out.println("updateData .......");
		try {
			result = new ResponseBean();
			//获取当前用户内部机构号
			String nbjgh = (String) session.getAttribute("nbjgh");
			params.put("nbjgh", nbjgh);
			//获取当前用户名
			String username = (String) session.getAttribute("username");
			System.out.println(params.get("fileName"));
			if(!"jgxx".equals(params.get("fileName"))){
				System.out.println("进来了！！");
				//获取当前用户可用内部机构号
				List<Map> AllAuth = insertDataService.getData("getAllAuth", params);
				ArrayList<String> nbjghList = new ArrayList<String>();
				for (Map map : AllAuth) {
					nbjghList.add(String.valueOf(map.get("NBJGH")));
				}
				//获取当前表的内部机构号
				List<Map> Table_nbjgh = insertDataService.getData("getTable_nbjgh", params);
				//新增数据的内部机构号
				String table_nbjgh = (String)params.get(Table_nbjgh.get(0).get("TABLE_NBJGH"));
				if(!"admin".equals(username) && !nbjghList.contains(table_nbjgh)){
					result.setData("您无权录入"+table_nbjgh+"此内部机构号");
					return result;
				}
			}
			params.put("last_row", JSONArray.fromObject(params.get("last_row")));
			sqlId = (String) params.get("UpdateSqlId");
			result.setData(manaProService.updateData(sqlId, params));
			// 记录操作日志
			if ((int)result.getData() > 0 && params.get("CODE") != null) {
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
			params.put("deleted", JSONArray.fromObject(params.get("deleted")));
			result = new ResponseBean();
			sqlId = (String) params.get("delSqlId");
			result.setData(manaProService.deleteData(sqlId, params));
			// 记录操作日志
			if ((int)result.getData() > 0) {
				String username = (String) request.getSession().getAttribute("username");
				JSONArray deleted = JSONArray.fromObject(params.get("deleted"));
				insertRecordOperationLog((String)params.get("fileName"), (deleted.size() > 0 ? deleted.getJSONObject(0).getInt("CODE") : 0), username, "3");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * sgqc导出Excel
	 * @param request
	 * @param response
	 * @param sqlId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/down")
	public void sgqcDownExcel(HttpServletRequest request, HttpServletResponse response,String sqlId) throws Exception {
		long start = System.currentTimeMillis();
		Map<String, Object> params = getParamters(request);
		sqlId = (String) params.get("excelSqlid");
		String nbjgh = (String) request.getSession().getAttribute("nbjgh");
		String username = (String) request.getSession().getAttribute("username");
		if("admin".equals(username)){
			params.put("DataAuthNBJGH", "");
		}else{
			params.put("DataAuthNBJGH", nbjgh);
		}
		List<Map> dataList = manaProService.getDataList(sqlId, params);
		//获取title
		JSONArray fieldValues = JSONArray.fromObject(new String(params.get("fieldValues").toString().getBytes("UTF-8"), "UTF-8"));
		//获取fields
		JSONArray fields = JSONArray.fromObject(params.get("fields"));
		//删除fields前两个字段
		fields.remove(0);
		fields.remove(0);
		//删除fields前两个字段
		fieldValues.remove(0);
		fieldValues.remove(0);
		//转换成数组
		Object[] fieldsArray = fields.toArray(new String[fields.size()]);
		Object[] fieldValuesArray = fieldValues.toArray(new String[fieldValues.size()]);
		//获取文件中文名
		String fileTitle = (String) params.get("fileTitle");
		//文件名称
		String fileName = params.get("fileName")+fmt.format(fmt1.parse((String)params.get("beginSjrq"))) + fileTitle + ".xls"; 
		//数据sheet页title
		String[] columnNames = (String[]) fieldValuesArray;
		//数据sheet页keys
		String[] keys = (String[]) fieldsArray;
		//参数sheet页title
		String[] paramNames = (String[]) fieldValuesArray;
		//数据sheet页keys
		fields.remove(0);
		fields.add(0, "beginSjrq");
		Object[] paramarray = fields.toArray(new String[fields.size()]);
		String paramKeys[] = (String[]) paramarray;
		
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		ArrayList<Map> arrayList = new ArrayList<Map>();
		hashMap = (HashMap<String, Object>) params;
		arrayList.add(hashMap);
		params.put("arrayList", arrayList);
		params.put("paramNames", paramNames);
		params.put("paramKeys", paramKeys);
		UploadAndAownloadController.export(request, response, fileName, dataList, keys, columnNames,params);
		long end = System.currentTimeMillis();
		System.out.println("导出共用"+(end-start)+"毫秒");
	}
	
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/jgxxDown")
	public void jgxxDownExcel(HttpServletRequest request, HttpServletResponse response,String sqlId) throws Exception {
		long start = System.currentTimeMillis();
		Map<String, Object> params = getParamters(request);
		params.put("beginSjrq", fmt1.format(new Date()));
		sqlId = (String) params.get("excelSqlid");
		// 数据权限限制
		String nbjgh = (String) request.getSession().getAttribute("nbjgh");
		String username = (String) request.getSession().getAttribute("username");
		if("admin".equals(username)){
			params.put("DataAuthNBJGH", "");
		}else{
			params.put("DataAuthNBJGH", nbjgh);
		}
		List<Map> dataList = manaProService.getDataList(sqlId, params);
		//获取title
		JSONArray fieldValues = JSONArray.fromObject(new String(params.get("fieldValues").toString().getBytes("UTF-8"), "UTF-8"));
		//获取fields
		JSONArray fields = JSONArray.fromObject(params.get("fields"));
		//删除fields前两个字段
		fields.remove(0);
		fields.remove(0);
		//删除fields前两个字段
		fieldValues.remove(0);
		fieldValues.remove(0);
		//转换成数组
		Object[] fieldsArray = fields.toArray(new String[fields.size()]);
		Object[] fieldValuesArray = fieldValues.toArray(new String[fieldValues.size()]);
		//获取文件中文名
		String fileTitle = (String) params.get("fileTitle");
		//文件名称
		String fileName = params.get("fileName")+fmt.format(fmt1.parse((String)params.get("beginSjrq"))) + fileTitle + ".xls"; 
		//数据sheet页title
		String[] columnNames = (String[]) fieldValuesArray;
		//数据sheet页keys
		String[] keys = (String[]) fieldsArray;
		//参数sheet页title
		String[] paramNames = (String[]) fieldValuesArray;
		//数据sheet页keys
		Object[] paramarray = fields.toArray(new String[fields.size()]);
		String paramKeys[] = (String[]) paramarray;
		
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		ArrayList<Map> arrayList = new ArrayList<Map>();
		hashMap = (HashMap<String, Object>) params;
		arrayList.add(hashMap);
		params.put("arrayList", arrayList);
		params.put("paramNames", paramNames);
		params.put("paramKeys", paramKeys);
		UploadAndAownloadController.export(request, response, fileName, dataList, keys, columnNames,params);
		long end = System.currentTimeMillis();
		System.out.println("导出共用"+(end-start)+"毫秒");
	}
}
