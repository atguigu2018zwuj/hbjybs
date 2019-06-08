package com.mininglamp.currencySys.customReport.service;

import java.util.List;
import java.util.Map;

public interface CustomReportService {

	public List<Map> getDataList(Map<String, Object> params, String sqlId);
	
	/**
	 * 查询自定义报表（hbjyods.cbs_bdpal）的数据
	 * @param params
	 * @return 数据列表
	 */
	public List<Map> getCbsBdpalList(Map<String, Object> params);
	
	/**
	 * 获取数据日期维度树
	 * @return
	 */
	public List<Map> getDateTreeDep();
	
	public int updateGeneratereport(Map<String, Object> params, String sqlId);
	
	//插入
	public int insertData(Map map, String sqlid);
	//删除
	public int deleteData(Map map, String sqlid);
	//更新
	public int updateData(Map map, String sqlid);
}
