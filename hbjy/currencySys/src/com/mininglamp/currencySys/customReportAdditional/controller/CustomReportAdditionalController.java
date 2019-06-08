package com.mininglamp.currencySys.customReportAdditional.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mininglamp.currencySys.common.base.bean.ResponseBean;
import com.mininglamp.currencySys.common.base.controller.BaseController;
import com.mininglamp.currencySys.common.util.CommonUtil;
import com.mininglamp.currencySys.common.util.StringUtils;
import com.mininglamp.currencySys.customReportAdditional.service.CustomReportAdditionalService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 自定义报表补录表管理
 */
@Controller
@RequestMapping(value = "/cusReportAddController")
public class CustomReportAdditionalController extends BaseController{
	private static final Logger logger = LoggerFactory.getLogger(CustomReportAdditionalController.class);
	
	@Autowired
	private CustomReportAdditionalService customReportAdditionalService;
	
// ---------------------------------------- 数据源管理 START ----------------------------------------
	/**
	 * 数据源查询
	 * @param request
	 * @param response
	 * @param sqlId
	 * @return
	 */
	@RequestMapping(value = "/getDatasourceInfo")
	@ResponseBody
	public List<Map<String, Object>> getDatasourceInfo(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> params = getParamters(request);
		List<Map> dataList = customReportAdditionalService.getDataList("selectDatasource", params);
		List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		for (Map map : dataList) {
			arrayList.add(map);
		}
		return arrayList;
	}
// ---------------------------------------- 数据源管理 END ----------------------------------------
	
// ---------------------------------------- 补录表维护 START ----------------------------------------
	/**
	 * 补录表查询
	 * @param request
	 * @param response
	 * @param sqlId
	 * @return
	 */
	@RequestMapping(value = "/getTableInfo")
	@ResponseBody
	public List<Map<String, Object>> getTableInfo(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> params = getParamters(request);
		List<Map> dataList = customReportAdditionalService.getDataList("selectTable", params);
		List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		for (Map map : dataList) {
			arrayList.add(map);
		}
		return arrayList;
	}
	
	/**
	 * 补录表删除
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/deleteTableInfo")
	@ResponseBody
	public ResponseBean deleteTableInfo(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> params = getParamters(request);
		ResponseBean result = new ResponseBean();
		// 删除表数据失败
		final int errorDeleteInfo = 0;
		// 删除物理表失败
		final int errorDropTable = -1;
		
		// 删除表数据条数
		int deleteInfoCount = 0;
		try {
			params.put("deleted", JSONArray.fromObject(params.get("deleted")));
			JSONArray deleteds = (JSONArray)params.get("deleted");
			deleteInfoCount = customReportAdditionalService.deleteData(params, "deleteTableInfo");
			if (deleteInfoCount > 0) {
				// 删除补录表数据成功，删除对应物理表
				JSONObject deletedObj = null;
				Connection connection = null;
				PreparedStatement pstm = null;
				ResultSet rs = null;
				Map<String, Object> datasourceParams = new HashMap<String, Object>();
				List<Map> datasourceList = null;
				// 删除表sql模板
				String deleteSqlTemplate = "drop table &1.&2";
				String selectSqlTemplate = "select count(1) as tcount from user_tables where table_name = upper('&1')";
				for (Object obj : deleteds) {
					deletedObj = JSONObject.fromObject(obj);
					// 取得数据源信息，获取数据源的用户名
					datasourceParams.put("dname", deletedObj.getString("DATASOURCE"));
					datasourceList = customReportAdditionalService.getDataList("selectDatasource", datasourceParams);
					if (datasourceList == null || datasourceList.isEmpty()) {
						logger.error("数据源【"+deletedObj.getString("DATASOURCE")+"】的数据不存在");
						// 删除表失败
						result.setData(errorDropTable);
						return result;
					}
					connection = CommonUtil.getConnectionByDatasourceName(deletedObj.getString("DATASOURCE"));
					// 查询对应物理表是否存在
					pstm = connection.prepareStatement(selectSqlTemplate.replaceAll("&1", deletedObj.getString("TABLE_NO")));
					rs = pstm.executeQuery();
					if (rs.next()) {
						// 对应物理表存在
						if (rs.getInt("TCOUNT") > 0) {
							try {
								// 删除物理表
								pstm = connection.prepareStatement(
										deleteSqlTemplate.replaceAll("&1", StringUtils.parseString(datasourceList.get(0).get("DBUSER"))).replaceAll("&2", deletedObj.getString("TABLE_NO")));
								pstm.executeQuery();
							} catch (SQLException sqlE) {
								// 删除表失败
								result.setData(errorDropTable);
								return result;
							}
						}
					}
					
					// 关闭资源
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
					if (pstm != null) {
						try {
							pstm.close();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
					if (connection != null) {
						try {
							connection.close();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
			
			result.setData(deleteInfoCount);
		} catch (Exception e) {
			e.printStackTrace();
			// 若删除表数据条数大于0，则可判定为删除物理表失败
			result.setData(deleteInfoCount > 0 ? errorDropTable : errorDeleteInfo);
		}
		return result;
	}
	
	/**
	 * 补录表保存
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/saveTableInfo")
	@ResponseBody
	public ResponseBean saveTableInfo(HttpServletRequest request, HttpServletResponse response){
		Map params = getParamters(request);
		ResponseBean result = null;
		try {
			result = new ResponseBean();
			// TODO 若修改表基本信息，则同步数据配置内容
			int count = 0;
			count = customReportAdditionalService.updateData(params,"updateTableInfo");
			if (count <= 0) {
				count = customReportAdditionalService.insertData(params,"insertTableInfo");
			}
			result.setData(count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 补录表创建
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/createTable")
	@ResponseBody
	public ResponseBean createTable(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> params = getParamters(request);
		ResponseBean result = new ResponseBean();
		Map<String,Object> resultData = new HashMap<String,Object>();
		resultData.put("flg", true);
		resultData.put("msg", "创建补录表成功");
		result.setData(resultData);
		
		Connection connection = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			// 参数校验
			if (StringUtils.isEmpty(params.get("createTableSql")) || StringUtils.isEmpty(params.get("datasource"))) {
				resultData.put("flg", false);
				resultData.put("msg", "【建表语句】或【数据源】为空");
				return result;
			}
			// 创建补录表的所有sql组成的字符串
			String createTableSql = StringUtils.parseString(params.get("createTableSql")).replaceAll("\\n", "");
			
			// 在一个事务中逐条执行补录表创建的sql
			String[] createTableSqlArray = createTableSql.split(";");
			connection = CommonUtil.getConnectionByDatasourceName(StringUtils.parseString(params.get("datasource")));
			connection.setAutoCommit(false);
			for (String sql : createTableSqlArray) {
				pstm = connection.prepareStatement(sql);
				pstm.executeQuery();
			}
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
			resultData.put("flg", false);
			resultData.put("msg", e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			if (pstm != null) {
				try {
					pstm.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.rollback();
					connection.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		return result;
	}
// ---------------------------------------- 补录表维护 END ----------------------------------------
	
// ---------------------------------------- 模板定制 START ----------------------------------------
	/**
	 * 模板查询
	 * @param request
	 * @param response
	 * @param sqlId
	 * @return
	 */
	@RequestMapping(value = "/getTemplateInfo")
	@ResponseBody
	public List<Map<String, Object>> getTemplateInfo(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> params = getParamters(request);
		List<Map> dataList = customReportAdditionalService.getDataList("selectTemplate", params);
		List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		for (Map map : dataList) {
			arrayList.add(map);
		}
		return arrayList;
	}
	
	/**
	 * 模板删除
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/deleteTemplateInfo")
	@ResponseBody
	public ResponseBean deleteTemplateInfo(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> params = getParamters(request);
		ResponseBean result = null;
		try {
			params.put("deleted", JSONArray.fromObject(params.get("deleted")));
			result = new ResponseBean();
			result.setData(customReportAdditionalService.deleteData(params, "deleteTemplateInfo"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 模板保存
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/saveTemplateInfo")
	@ResponseBody
	public ResponseBean saveTemplateInfo(HttpServletRequest request, HttpServletResponse response){
		Map params = getParamters(request);
		ResponseBean result = null;
		try {
			result = new ResponseBean();
			// TODO 若修改模板基本信息，则同步数据配置内容
			int count = 0;
			count = customReportAdditionalService.updateData(params,"updateTemplateInfo");
			if (count <= 0) {
				count = customReportAdditionalService.insertData(params,"insertTemplateInfo");
			}
			result.setData(count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
// ---------------------------------------- 模板定制 END ----------------------------------------
}

