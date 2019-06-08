package com.mininglamp.currencySys.insertData.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mininglamp.currencySys.ManaPro.service.ManaProService;
import com.mininglamp.currencySys.common.base.bean.ResponseBean;
import com.mininglamp.currencySys.common.base.controller.BaseController;
import com.mininglamp.currencySys.common.util.CommonValidateUtil;
import com.mininglamp.currencySys.common.util.StringUtils;
import com.mininglamp.currencySys.insertData.service.InsertDataService;

import net.sf.json.JSONArray;

@Controller
@RequestMapping(value="/insertDataController")
public class InsertDataController extends BaseController{
	private static final Logger logger = LoggerFactory.getLogger(InsertDataController.class);

	@Autowired
	private InsertDataService insertDataService;
	@Autowired
	private ManaProService manaProService;
	
	/**
	 * 新增数据
	 * @param request
	 * @param response
	 * @param sqlId
	 * @return
	 */
	@RequestMapping(value="insertData")
	@ResponseBody
	public ResponseBean insertData(HttpServletRequest request, HttpServletResponse response,String sqlId){
		Map<String, Object> params = getParamters(request);
		HttpSession session = request.getSession();
		ResponseBean result = new ResponseBean();
		//获取当前用户内部机构号
		String nbjgh = (String) session.getAttribute("nbjgh");
		params.put("nbjgh", nbjgh);
		//获取当前用户名
		String username = (String) session.getAttribute("username");
		params.put("USERNAME", username);
		//获取当前用户机构级别
		String jgjb = (String) session.getAttribute("");
		//获取当前用户可用内部机构号
		List<Map> AllAuth = insertDataService.getData("getAllAuth", params);
		ArrayList<String> nbjghList = new ArrayList<String>();
		for (Map map : AllAuth) {
			nbjghList.add(String.valueOf(map.get("NBJGH")));
		}
		//获取当前表的内部机构号
		List<Map> Table_nbjgh = insertDataService.getData("getTable_nbjgh", params);
        //新增数据的内部机构号
		String table_nbjgh = null;
		if("HBJYODS.JRJGXX".equals(params.get("tableName"))){
			table_nbjgh = (String) params.get("BR_NO");
		}else{
			table_nbjgh = (String)params.get(Table_nbjgh.get(0).get("TABLE_NBJGH"));
		}
		//判断当前数据的内部机构号是否有权限录入
		if("admin".equals(username)){
			data(params, session, result);
		}else{
			if (Table_nbjgh.size()==0 && !"HBJYODS.JRJGXX".equals(params.get("tableName"))) {
				result.setData("您无权录入"+table_nbjgh+"此内部机构号");
				return result;
			}
			if((nbjghList.contains(table_nbjgh) || (null == table_nbjgh && "special_gzhm".equals((String)(Table_nbjgh.get(0)==null?"":Table_nbjgh.get(0).get("TABLENAME")))))
					&& (!"HBJYODS.JRJGXX".equals(params.get("tableName")) && !"special_finf".equals(Table_nbjgh.get(0).get("TABLENAME")))){
				//获取当前修改的上报类型值
				String SBLXValue = (String) params.get("SBLX");
				//检验经常性报表  上报类型是否符合当前用户
				String sblxValidateInfo = CommonValidateUtil.validateSBLX(SBLXValue,(String)Table_nbjgh.get(0).get("TABLENAME"), request);
				if (StringUtils.isNotEmpty(sblxValidateInfo)) {
					result.setData(sblxValidateInfo);
				} else {
					data(params, session, result);
				}
			}else if("HBJYODS.JRJGXX".equals(params.get("tableName")) || "special_finf".equals((String)(Table_nbjgh.get(0)==null?"":Table_nbjgh.get(0).get("TABLENAME")))) {
				Map<String, Object> tempParams = new HashMap<String, Object>();
				try {
					tempParams.put("NBJGHValue", table_nbjgh);
					tempParams.put("JGJBValue", params.get("JGJB"));
					tempParams.put("tableName", params.get("tableName"));
					tempParams.put("sjgljgNbjgh", params.get("SJGLJGNBJGH"));
					String jrjgxxValidateMsg = CommonValidateUtil.validateJRJGXX(tempParams, request, manaProService);
					if (StringUtils.isNotEmpty(jrjgxxValidateMsg)) {
						result.setData(jrjgxxValidateMsg);
					} else {
						data(params, session, result);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				result.setData("您无权录入"+table_nbjgh+"此内部机构号");
			}
		}
		return result;
	}

	/**
	 * 插入数据
	 * @param params
	 * @param session
	 * @param result
	 */
	public void data(Map<String, Object> params, HttpSession session, ResponseBean result) {
		//获取表名
		String tableName = (String) params.get("tableName");
		//获取四位标识
		String fileName = (String) params.get("fileName");
		if(tableName != null ){
			try {
				List<Map> configList = (List<Map>) session.getAttribute(fileName+"dataList");
				ArrayList<Map> configList_bak = new ArrayList<Map>();
				for (Map map : configList) {
					configList_bak.add(map);
				}
				JSONArray fields = JSONArray.fromObject(params.get("last_row"));
				ArrayList<String> arrayList = new ArrayList<String>();
				for (int i = 0; i < fields.size(); i++) {
					if("code".equals(fields.get(i)) || "CODE".equals(fields.get(i))){
						HashMap<Object, Object> hashMap = new HashMap<>();
						hashMap.put("FIELD_ID", "CODE");
						configList_bak.add(hashMap);
					}
					arrayList.add(String.valueOf(fields.get(i)));
				}
				params.put("configList", configList_bak);
				params.put("last_row", arrayList);
				arrayList = null;
				// 若当前业务报表中存在自动生成的字段，则调用其对应的insert方法（此方法暂时不用，更改为汇总时在省、市、县中数据计算自动生成字段）
//				boolean noAutoInput = true;// 当前业务报表中是否不存在自动生成的字段
//				for (Map config : configList) {
//					if (InsertDataService.IS_INPUT_AUTO_TRUE.equals((String)config.get("IS_INPUT_AUTO"))) {
//						noAutoInput = false;
//						break;
//					}
//				}
//				int insertData = 0;
//				if (noAutoInput) {
//					insertData = insertDataService.insertData("insertData", params);
//				} else {
//					insertData = insertDataService.insertData("customInsertData", params);
//				}
				// 将自动生成的字段强制设为null（汇总时省、市、县级再生成）
				for (Map config : configList) {
					if (InsertDataService.IS_INPUT_AUTO_TRUE.equals((String)config.get("IS_INPUT_AUTO"))) {
						params.put((String)config.get("FIELD_ID"), null);
					}
				}
				int insertData = insertDataService.insertData("insertData", params);
				
				if (!fileName.equals("jgxx") && (Integer)params.get("code") != -1) {
					int insertBsglData = insertDataService.insertData("insertBsglData", params);
					// 记录操作日志
					insertRecordOperationLog((String)params.get("fileName"), (int)params.get("code"), (String)params.get("USERNAME"), "1");
				}				
				result.setData(insertData);
			} catch (Exception e) {
				result.setData("异常");
				if(e.toString().contains("ORA-00001")){
					result.setData("违反唯一约束异常！");
				}else{
					result.setData("异常");
				}
				logger.error(e.getMessage(),e);
			} finally {
			}
		}else{
			result.setData("表名异常");
		}
	}
}
