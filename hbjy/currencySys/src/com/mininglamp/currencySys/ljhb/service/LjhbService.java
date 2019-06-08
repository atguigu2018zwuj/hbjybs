package com.mininglamp.currencySys.ljhb.service;

import java.util.List;
import java.util.Map;

public interface LjhbService {

	public List<Map> getDataList(String sqlId, Map<String, Object> params);

	public Object updateData(String string, Map<String, Object> params);
	
	public int deleteData(String sqlId, Map<String, Object> params);

	public List<Map> getDataCount(String sqlId, Map<String, Object> params);
}
