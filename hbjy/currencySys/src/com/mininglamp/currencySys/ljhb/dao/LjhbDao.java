package com.mininglamp.currencySys.ljhb.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

public interface LjhbDao {

	public List<Map> getDataList(String string, Map<String, Object> params);
	
	public int updateData(String string, Map<String, Object> params);
	
	public int deleteData(String string, Map<String, Object> params);

	public List<Map> getDataCount(String sqlId, Map<String, Object> params);
}
