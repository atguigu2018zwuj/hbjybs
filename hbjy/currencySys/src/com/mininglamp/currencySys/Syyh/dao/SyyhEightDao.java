package com.mininglamp.currencySys.Syyh.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

public interface SyyhEightDao {

	public List<Map> getDataList(String string, Map<String, Object> params);
	
	public int updateData(String string, Map<String, Object> params);
	
	public int deleteData(String string, Map<String, Object> params);
}
