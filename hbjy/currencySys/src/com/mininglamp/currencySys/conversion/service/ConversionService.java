package com.mininglamp.currencySys.conversion.service;

import java.util.List;
import java.util.Map;

public interface ConversionService {

	public List<Map> getDataList(String sqlId, Map<String, Object> params);
	
	public int updateGeneratereport(String sqlId , Map<String, Object> params);
}
