package com.mininglamp.currencySys.customReport.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mininglamp.currencySys.common.base.bean.ResponseBean;
import com.mininglamp.currencySys.common.base.controller.BaseController;
import com.mininglamp.currencySys.common.util.StringUtils;
import com.mininglamp.currencySys.customReport.service.CustomReportService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 自定义报表
 * @author ChenXiangyu
 */
@Controller
@RequestMapping(value = "/customReportController")
public class CustomReportController extends BaseController{
	@Autowired
	CustomReportService customReportService;
	
	/**
	 * 维度层级树
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@RequestMapping(value="/getTree_dep",method={RequestMethod.GET,RequestMethod.POST})
	public ResponseBean getTree_dep(HttpServletRequest request){
		ResponseBean responseBean = new ResponseBean();
		Map<String,Object> params = getParamters(request);
		try {
			List<Map> cusTypeList = customReportService.getDataList(params, "getCusTypeTree_dep");
			List<Map> QygmList = customReportService.getDataList(params, "geQygmTree_dep");
			List<Map> coinList = customReportService.getDataList(params, "getCoinTree_dep");
			List<Map> areaList = customReportService.getDataList(params, "getAreaTree_dep");
			List<Map> dateList = customReportService.getDateTreeDep();
			List<Map> orgList  = customReportService.getDataList(params, "getOrgTree_dep");
			List<Map> HylbList = customReportService.getDataList(params, "getHylbTree_dep");
			
			Map<String,Object> resultTree = new HashMap<String,Object>();
			resultTree.put("cusTypeTree", cusTypeList);
			resultTree.put("QygmList", QygmList);
			resultTree.put("coinTree", coinList);
			resultTree.put("areaTree", areaList);
			resultTree.put("dateList", dateList);
			resultTree.put("orgTree", orgList);
			resultTree.put("HylbList", HylbList);
			
			responseBean.setData(resultTree);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseBean;
	}
	
	@ResponseBody
	@RequestMapping(value="getCbs_bdpal")
	public ResponseBean getCbs_bdpal(String sqlId,HttpServletRequest request){
		Map<String,Object> params = getParamters(request);
		ResponseBean responseBean = new ResponseBean();
		Map<String,Object> map = request.getParameterMap();
		params = dealParams(map);
		// 参数过滤
		if (params == null) {
			return responseBean;
		} else if (StringUtils.isNotEmpty(params.get("error-msg"))) {
			responseBean.setData("msg_"+params.get("error-msg"));
			return responseBean;
		}
		try {
			// 遍历数据日期季度列表，循环查询
			List<Map> orgimpalaList = new ArrayList<Map>();
			for (String sjrq : (Set<String>)params.get("filtersjrq_root")) {
				if ("4".equals(sjrq.substring(5, 6))) {
					params.put("sjrq", (Integer.parseInt(sjrq.substring(0, 4))+1)+"0101");
				} else if ("3".equals(sjrq.substring(5, 6))) {
					params.put("sjrq", sjrq.substring(0, 4)+"1001");
				} else if ("2".equals(sjrq.substring(5, 6))) {
					params.put("sjrq", sjrq.substring(0, 4)+"0701");
				} else if ("1".equals(sjrq.substring(5, 6))) {
					params.put("sjrq", sjrq.substring(0, 4)+"0401");
				}
				orgimpalaList.addAll(customReportService.getCbsBdpalList(params));
			}
			
			// ---------------------------------- 返回值处理 START ------------------------------
			// 若无金融机构显示项，则只取上报类型为3的数据
			List<Map> resultList = new ArrayList<Map>();
			if (!"yes".equals(params.get("org_root"))) {
				for (Map info : orgimpalaList) {
					if ("3".equals(info.get("SBLX"))) {
						resultList.add(info);
					}
				}
				// 数据中没有上报类型为3的，取为2的
				if (!orgimpalaList.isEmpty() && resultList.isEmpty()) {
					for (Map info : orgimpalaList) {
						if ("2".equals(info.get("SBLX"))) {
							resultList.add(info);
						}
					}
				}
				// 数据中没有上报类型为2的，取为1的
				if (!orgimpalaList.isEmpty() && resultList.isEmpty()) {
					resultList = orgimpalaList;
				}
			} else {
				resultList = orgimpalaList;
			}
			// 金融机构地区（拼成 市-县 的形式）
			List<Map> areaList = customReportService.getDataList(params, "getAreaTree_dep");
			if ("yes".equals(params.get("area_root"))) {
				for (Map info : resultList) {
					String parent1 = "";
					if (StringUtils.isNotEmpty(info.get("DQDM"))) {
						for (Map area : areaList) {
							if (StringUtils.parseString(info.get("DQDM")).equals(area.get("id")) && !"省".equals(area.get("level"))) {
								for (Map areaP1 : areaList) {
									if (String.valueOf(area.get("pId")).equals(areaP1.get("id"))) {
										parent1 = StringUtils.parseString(areaP1.get("name"));
										break;
									}
								}
								info.put("金融机构地区", parent1+"-"+info.get("金融机构地区"));
							}
						}
					}
				}
			}
			// ---------------------------------- 返回值处理 END ------------------------------
			
			responseBean.setData(resultList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseBean;
	}
	
	/**
	 * 处理多维参数
	 * @param map
	 * @return 若有错误抛出信息，则在"error-msg"项中体现
	 * @author ice
	 * @date 
	 * @time
	 */
    public Map<String,Object> dealParams(Map<String,Object> map){
    	Map<String, Object> params = null;
    	
		try {
			params = new HashMap<String,Object>();
			Set keySet = map.keySet();
			String keyValue = "";
			for (Object keyName : keySet) {
				keyValue=keyValue+keyName;
			}
   
			Map<String, Object> pars = (Map<String, Object>)JSONObject.fromObject(keyValue);
			//行
			Map<String,Object> rows = (Map<String, Object>) pars.get("rows");
			Set keySetRows = rows.keySet();
			//列
			Map<String,Object> column = (Map<String, Object>) pars.get("column");
			Set keySetColumn = column.keySet();
			//度量
			JSONArray value = (JSONArray) pars.get("value");
			
			List<String> listKey = new ArrayList<String>();
			listKey.addAll(keySetColumn);
			listKey.addAll(keySetRows);
			listKey.addAll(value);
			
			for (int i = 0; i < listKey.size(); i++) {
				 params.put(listKey.get(i)+"","yes");
			}
			//过滤
			Map<String,Object> filter = (Map<String, Object>) pars.get("filter");
			List<String> keyFilter = new ArrayList<String>(filter.keySet());  
			for (int i = 0; i < filter.size(); i++) {
				params.put("filter"+keyFilter.get(i)+"", filter.get(keyFilter.get(i)));
			}
			// 查询条件校验（不允许有过滤但不显示的情况出现，若入参出现了这种情况，则返回提示信息【msg_提示信息】）
			if (params.get("cust_root") == null && params.get("filtercust_root") != null) {
				params.put("error-msg", "您选择的有客户类型过滤条件，请在行或列中添加【客户类型】显示项！");
				return params;
			} else if (params.get("qygm_root") == null && params.get("filterqygm_root") != null) {
				params.put("error-msg", "您选择的有企业规模过滤条件，请在行或列中添加【企业规模】显示项！");
				return params;
			} else if (params.get("coin_root") == null && params.get("filtercoin_root") != null) {
				params.put("error-msg", "您选择的有币种过滤条件，请在行或列中添加【币种】显示项！");
				return params;
			} else if (params.get("area_root") == null && params.get("filterarea_root") != null) {
				params.put("error-msg", "您选择的有金融机构地区过滤条件，请在行或列中添加【金融机构地区】显示项！");
				return params;
			} else if (params.get("sjrq_root") == null) {
				params.put("error-msg", "数据日期为必选的显示项，请在行或列中添加【数据日期】项！");
				return params;
			} else if (params.get("org_root") == null && params.get("filterorg_root") != null) {
				params.put("error-msg", "您选择的有金融机构过滤条件，请在行或列中添加【金融机构】显示项！");
				return params;
			} else if (params.get("hylb_root") == null && params.get("filterhylb_root") != null) {
				params.put("error-msg", "您选择的有贷款主体行业类别过滤条件，请在行或列中添加【贷款主体行业类别】显示项！");
				return params;
			}
			//-------------------------过滤start-----------------------------
			// 数据日期
			List<Map> dateList = customReportService.getDateTreeDep();
			if (params.get("filtersjrq_root") != null) {
				Set<String> filterSjrq = new HashSet<String>();
				// 如果传的是日期，则处理成季度
				for (String sjrq : (List<String>)params.get("filtersjrq_root")) {
					if (StringUtils.isNotEmpty(sjrq) && sjrq.length() == 4) {
						for (Map dateInfo : dateList) {
							if (sjrq.equals(dateInfo.get("pId"))) {
								filterSjrq.add(StringUtils.parseString(dateInfo.get("id")));
							}
						}
					} else if (StringUtils.isNotEmpty(sjrq) && sjrq.length() == 6) {
						filterSjrq.add(sjrq);
					}
				}
				params.put("filtersjrq_root", filterSjrq);
			} else {
				// 查出全部的
				Set<String> dates = new HashSet<String>();
				for (Map dateInfo : dateList) {
					if ("2".equals(dateInfo.get("level"))) {
						dates.add(StringUtils.parseString(dateInfo.get("id")));
					}
				}
				params.put("filtersjrq_root", dates);
			}
			// 金融机构
			if(params.get("filterorg_root") != null){
				List<String> datas = (List<String>) params.get("filterorg_root");
				List<Map> orgList  = customReportService.getDataList(params, "getOrgTree_dep");
				List<String> nbjghOne = new ArrayList<String>();
				List<String> nbjghTwo = new ArrayList<String>();
				List<String> nbjghThree = new ArrayList<String>();
				for (String data : datas) {
					for (Map orgInfo : orgList) {
						if (orgInfo.get("id").equals(data)) {
							if ("1".equals(orgInfo.get("level"))) {
								nbjghOne.add(StringUtils.parseString(orgInfo.get("id")));
							} else if ("2".equals(orgInfo.get("level"))) {
								nbjghTwo.add(StringUtils.parseString(orgInfo.get("id")));
							} else if ("3".equals(orgInfo.get("level"))) {
								nbjghThree.add(StringUtils.parseString(orgInfo.get("id")));
							}
						}
					}
				}
				params.put("nbjgh_one", nbjghOne);
				params.put("nbjgh_two", nbjghTwo);
				params.put("nbjgh_three", nbjghThree);
			}
			// 金融机构地区
			List<Map> areaList = customReportService.getDataList(params, "getAreaTree_dep");
			if (params.get("filterarea_root") != null) {
				Set<String> filterArea = new HashSet<String>();
				// 选择上级则需将下级地市也加到参数中
				for (String area : (List<String>)params.get("filterarea_root")) {
					filterArea.add(area);
					if (StringUtils.isNotEmpty(area)) {
						for (Map areaInfo : areaList) {
							if (area.equals(areaInfo.get("id")) && "省".equals(areaInfo.get("level"))) {
								// 取得该省的所有市
								for (Map areaCity : areaList) {
									if (area.equals(areaCity.get("pId"))) {
										filterArea.add(StringUtils.parseString(areaCity.get("id")));
										// 取得该市的所有县区
										for (Map areaTown : areaList) {
											if (StringUtils.parseString(areaCity.get("id")).equals(areaTown.get("pId"))) 
												filterArea.add(StringUtils.parseString(areaTown.get("id"))); 
										}
									}
								}
							} else if (area.equals(areaInfo.get("id")) && "市".equals(areaInfo.get("level"))) {
								// 取得该市的所有县区
								for (Map areaTown : areaList) {
									if (area.equals(areaTown.get("pId"))) 
										filterArea.add(StringUtils.parseString(areaTown.get("id"))); 
								}
							}
						}
					}
				}
				params.put("filterarea_root", filterArea);
			}
			// 客户类型
			List<Map> cusTypeList = customReportService.getDataList(params, "getCusTypeTree_dep");
			if (params.get("filtercust_root") != null) {
				Set<String> filterCust = new HashSet<String>();
				// 根据所选项取得所包含的所有最低级的id
				for (String cust : (List<String>)params.get("filtercust_root")) {
					if (StringUtils.isNotEmpty(cust) && cust.length() == 2) {
						for (Map custInfo : cusTypeList) {
							if (cust.equals(custInfo.get("pId"))) {
								filterCust.add(StringUtils.parseString(custInfo.get("id")));
							}
						}
					} else if (StringUtils.isNotEmpty(cust) && cust.length() == 5) {
						filterCust.add(cust);
					}
				}
				params.put("filtercust_root", filterCust);
			}
			//-------------------------过滤end-----------------------------
		} catch (Exception e) {
			e.printStackTrace();
		}
		return params;
	}
}

