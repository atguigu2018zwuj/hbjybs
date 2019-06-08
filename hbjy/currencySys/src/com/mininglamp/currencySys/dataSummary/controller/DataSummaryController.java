/**
 * 
 */
package com.mininglamp.currencySys.dataSummary.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mininglamp.currencySys.common.base.bean.ResponseBean;
import com.mininglamp.currencySys.common.base.controller.BaseController;
import com.mininglamp.currencySys.dataSummary.service.DataSummaryService;
import com.mininglamp.currencySys.insertData.service.InsertDataService;
import com.mininglamp.currencySys.uploadAndDownload.service.impl.UploadAndAownloadServiceImp;

import net.sf.json.JSONArray;

@Controller
@RequestMapping(value = "/dataSummaryController")
public class DataSummaryController extends BaseController {
	@Autowired
	private DataSummaryService dataSummaryService;
	@Autowired
	private InsertDataService insertDataService;

	String jrjgbm = null;
	String nbjgh  = null;
	String username  = null;
	
	public void getUserAndPass() {
		try {
			Properties prop = new Properties();
			//Class.getResourceAsStream("name") 会指定要加载的资源路径与当前类所在包的路径一致 如果这个name是以 '/' 开头的，那么就会从classpath的根路径下开始查找。
			//getClassLoader().getResourceAsStream("name")  无论要查找的资源前面是否带'/' 都会从classpath的根路径下查找。
			prop.load(UploadAndAownloadServiceImp.class.getClassLoader().getResourceAsStream("ibatis/jdbc.properties"));
			jrjgbm = prop.getProperty("provincial.jrjgbm");
			nbjgh = prop.getProperty("provincial.users");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 * 汇总
	 * @param request
	 * @param response
	 * @param sqlId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateData")
	@ResponseBody
	public ResponseBean updateData(HttpServletRequest request, HttpServletResponse response, String sqlId)
			throws Exception {
		/*getUserAndPass();*/
		jrjgbm=(String) request.getSession().getAttribute("dwcode");
		nbjgh=(String) request.getSession().getAttribute("nbjgh");
		username=(String) request.getSession().getAttribute("username");
		String snsh=(String) request.getSession().getAttribute("snsb");
		String snls=(String) request.getSession().getAttribute("snls");
		String ssjg=(String) request.getSession().getAttribute("ssjg");
		String level=(String) request.getSession().getAttribute("level");
		// 前台传来的参数分别是：sqlId：表名；bsrq：报送日期
		Map<String, Object> params = getParamters(request);
		// 修改表名格式为“insertXXXXSummary”
		String SqlName = (String) params.get("sqlId");
		String newSql = SqlName.substring(SqlName.length() - 4);
		StringBuffer sb = new StringBuffer("insert");
		String newSqlId = sb.append(newSql.toUpperCase()).append("Summary").toString();
		StringBuffer nameSfx = new StringBuffer("SYYH_");
		String tableName = nameSfx.append(newSql.toUpperCase()).toString();
		String sblx=null;
		if ("1699999998".equals(nbjgh)) {
			params.put("sblx", "0");
		}
		if (null != snsh) {
			params.put("sblx", "1");
		}
		if (null != snls) {
			params.put("sblx", "2");
		}
		params.put("tableName", tableName);
		// 汇总后结果插入的所有字段（除code列）
		List<Map> insertColumn = dataSummaryService.getData("getInsertColumnWithoutCode", params);
		params.put("insertColumn", insertColumn!=null && !insertColumn.isEmpty() ? insertColumn.get(0).get("COLUMN_NAMES") : "");
		params.put("sqlId", newSqlId);
		params.put("jrjgbm", jrjgbm);
		params.put("nbjgh", nbjgh);
		params.put("ssjg", ssjg);
		ResponseBean result = null;
		try {
			result = new ResponseBean();
			if(!"yes".equals(params.get("ishz"))){
				//查询是否已经汇总过
				List<Map> data = dataSummaryService.getData("getData", params);
				if(data != null && data.size() > 0){
					result.setData("OK");
					return result;
				}
			}
			// 刪除
			dataSummaryService.deleteData("deleteData", params);
			// 汇总
			if (!"SYYH_JRJB".equals(params.get("tableName").toString())) {
				if (level.equals("2")) {
					if (ssjg.contains("洛阳")) {
						params.put("DWMC", ssjg.substring(0,2).concat("市办"));
					}else if(ssjg.contains("周口")) {
						params.put("DWMC", ssjg.substring(0,2).concat("市办"));
					}else if(ssjg.contains("新乡")) {
						params.put("DWMC", ssjg.substring(12,14).concat("市办"));
					}else if(ssjg.contains("三门峡")) {
						params.put("DWMC", ssjg.substring(0,3).concat("市办"));
					}else if(ssjg.contains("驻马店")) {
						params.put("DWMC", ssjg.substring(0,3).concat("市办"));
					}else if(ssjg.contains("漯河")) {
						params.put("DWMC", ssjg.substring(0,2).concat("市办"));
					}else if(ssjg.contains("郑州")) {
						params.put("DWMC", ssjg.substring(0,2).concat("市办"));
					}else if(ssjg.contains("安阳")) {
						params.put("DWMC", ssjg.substring(0,2).concat("市办"));
					}else if(ssjg.contains("开封")) {
						params.put("DWMC", ssjg.substring(0,2).concat("市办"));
					}else if(ssjg.contains("商丘")) {
						params.put("DWMC", ssjg.substring(0,2).concat("市办"));
					}else if(ssjg.contains("焦作")) {
						params.put("DWMC", ssjg.substring(0,2).concat("市办"));
					}else if(ssjg.contains("许昌")) {
						params.put("DWMC", ssjg.substring(0,2).concat("市办"));
					}else if(ssjg.contains("濮阳")) {
						params.put("DWMC", ssjg.substring(0,2).concat("市办"));
					}else if(ssjg.contains("济源")) {
						params.put("DWMC", ssjg.substring(0,2).concat("市办"));
					}else if(ssjg.contains("平顶山")) {
						params.put("DWMC", ssjg.substring(0,3).concat("市办"));
					}else if(ssjg.contains("信阳")) {
						params.put("DWMC", ssjg.substring(0,2).concat("市办"));
					}else if(ssjg.contains("南阳")) {
						params.put("DWMC", ssjg.substring(0,2).concat("市办"));
					}else if(ssjg.contains("鹤壁")) {
						params.put("DWMC", ssjg.substring(0,2).concat("市办"));
					}else {
						params.put("DWMC","市办");
					} 
				}else {
					params.put("DWMC","市办");
				}			
			}
			result.setData(dataSummaryService.updateData(newSqlId, params));
			// 若汇总成功，则更新报送状态
			if (result.getData() != null && (int)result.getData() > 0) {
				Map<String, Object> bsglParam = new HashMap<String, Object>();
				bsglParam.put("SJRQ", params.get("bsrq"));
				bsglParam.put("fileName", newSql);
				bsglParam.put("nbjgh", nbjgh);
				bsglParam.put("USERNAME", username);
				insertDataService.insertData("insertBsglData", bsglParam);
			}
			/*result.setData("1");*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
