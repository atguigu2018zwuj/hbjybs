package com.mininglamp.currencySys.zhhb.dao;

import java.util.List;
import java.util.Map;

public interface ZhhbDao {


	public List<Map> getDataList(String string, Map<String, Object> params);
	
	public int updateData(String string, Map<String, Object> params);
	
	public int deleteData(String string, Map<String, Object> params);
}
