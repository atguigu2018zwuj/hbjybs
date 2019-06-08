package com.mininglamp.currencySys.customReportAdditional.service;

import java.util.List;
import java.util.Map;

public interface CustomReportAdditionalService {

	public List<Map> getDataList(String sqlId, Map<String, Object> params);
	
	//插入
	public int insertData(Map map, String sqlid);
	//删除
	public int deleteData(Map map, String sqlid);
	//更新
	public int updateData(Map map, String sqlid);
}
