package com.mininglamp.currencySys.reportManage.service;

import java.util.List;
import java.util.Map;

public interface ReportManageService {
	
	public List<Map> getDataList(Map params,String sqlId);
	
	public int updateData(Map params,String sqlId);

}
