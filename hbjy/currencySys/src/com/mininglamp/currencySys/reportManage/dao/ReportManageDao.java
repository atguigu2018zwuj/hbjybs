package com.mininglamp.currencySys.reportManage.dao;

import java.util.List;
import java.util.Map;

public interface ReportManageDao {
	
	public List<Map> getDataList(Map params,String sqlId);
	
	public int updateData(Map params,String sqlId);
	
}
