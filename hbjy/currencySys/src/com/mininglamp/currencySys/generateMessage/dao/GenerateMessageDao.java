package com.mininglamp.currencySys.generateMessage.dao;

import java.util.List;
import java.util.Map;

public interface GenerateMessageDao {

	public List<Map> getDataList(String string, Map<String, Object> params);

	public int updateGeneratereport(String sqlId, Map<String, Object> params);
	
	//插入
	public int insertData(Map map, String sqlid);
	//删除
	public int deleteData(Map map, String sqlid);
	//更新
	public int updateData(Map map, String sqlid);
}
