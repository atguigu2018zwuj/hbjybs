package com.mininglamp.currencySys.insertData.dao;

import java.util.List;
import java.util.Map;

public interface InsertDataDao {

	public int insertData(String string, Map<String, Object> params);
	
	public List<Map> getData(String string, Map<String, Object> params);
}
