package com.mininglamp.currencySys.ManaPro.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

public interface ManaProDao {

	public List<Map> getDataList(String string, Map<String, Object> params);
	
	public int updateData(String string, Map<String, Object> params);
	
	public int deleteData(String string, Map<String, Object> params);
}
