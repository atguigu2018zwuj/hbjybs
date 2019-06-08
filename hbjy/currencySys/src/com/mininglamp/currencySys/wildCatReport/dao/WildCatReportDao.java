package com.mininglamp.currencySys.wildCatReport.dao;

import java.util.List;
import java.util.Map;

public interface WildCatReportDao {
	
	public List<Map> getDataList(Map params,String sqlId);
	
	public int deleteData(Map params,String sqlId);
	
	public int updateData(Map params,String sqlId);
	
	public int insertData(Map params,String sqlId);
	
	public List<Map> getDataCount(Map params,String sqlId);
	
}
