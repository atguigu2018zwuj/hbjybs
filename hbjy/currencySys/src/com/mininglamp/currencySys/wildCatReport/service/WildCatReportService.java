package com.mininglamp.currencySys.wildCatReport.service;

import java.util.List;
import java.util.Map;

public interface WildCatReportService {

	public List<Map> getDataList(Map params,String sqlId);
	
	public int deleteData(Map params,String sqlId);
	
	public int updateData(Map params,String sqlId);
	
	public int insertData(Map params,String sqlId);
	
	public List<Map> getDataCount(Map params,String sqlId);
	
}
