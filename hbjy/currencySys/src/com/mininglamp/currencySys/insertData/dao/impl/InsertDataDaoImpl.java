package com.mininglamp.currencySys.insertData.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mininglamp.currencySys.common.base.dao.OracleBaseDao;
import com.mininglamp.currencySys.insertData.dao.InsertDataDao;

@Repository(value="insertDataDaoImpl")
public class InsertDataDaoImpl extends OracleBaseDao implements InsertDataDao{

	@Override
	protected String getMapper() {
		return "insertDataDaoImpl";
	}

	@Override
	public int insertData(String sqlId, Map<String, Object> params) {
		return this.normalInsert(params, sqlId);
	}

	@Override
	public List<Map> getData(String sqlId, Map<String, Object> params) {
		return this.normalSelectList(params, sqlId);
	}

}
