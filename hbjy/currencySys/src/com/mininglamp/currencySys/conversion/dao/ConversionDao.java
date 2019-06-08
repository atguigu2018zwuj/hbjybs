package com.mininglamp.currencySys.conversion.dao;

import java.util.List;
import java.util.Map;

public interface ConversionDao {

	public List<Map> getDataList(String string, Map<String, Object> params);

	public int updateGeneratereport(String sqlId, Map<String, Object> params);
}
