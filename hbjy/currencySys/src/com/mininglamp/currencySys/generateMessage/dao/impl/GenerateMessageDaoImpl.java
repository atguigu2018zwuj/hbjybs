package com.mininglamp.currencySys.generateMessage.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mininglamp.currencySys.common.base.dao.OracleBaseDao;
import com.mininglamp.currencySys.generateMessage.dao.GenerateMessageDao;

@Repository(value="generateMessageDaoImpl")
public class GenerateMessageDaoImpl extends OracleBaseDao implements GenerateMessageDao{

	@Override
	public List<Map> getDataList(String sqlId, Map<String, Object> params) {
		return this.normalSelectList(params, sqlId);
	}

	@Override
	protected String getMapper() {
		return "generateMessage";
	}

	@Override
	public int updateGeneratereport(String sqlId, Map<String, Object> params) {
		return this.normalUpdate(params, sqlId);
	}
	
	//插入
	@Override
	public int insertData(Map map, String sqlid) {
		return this.normalInsert(map, sqlid);
	}
	
	//删除
	@Override
	public int deleteData(Map map, String sqlid) {
		return this.normalDelete(map, sqlid);
	}
	
	//更新
	@Override
	public int updateData(Map map, String sqlid) {
		return this.normalUpdate(map, sqlid);
	}
}
