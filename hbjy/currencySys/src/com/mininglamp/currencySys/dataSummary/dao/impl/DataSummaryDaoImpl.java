package com.mininglamp.currencySys.dataSummary.dao.impl;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mininglamp.currencySys.common.base.dao.OracleBaseDao;
import com.mininglamp.currencySys.dataSummary.dao.DataSummaryDao;

@Repository("DataSummaryDaoImpl")
public class DataSummaryDaoImpl extends OracleBaseDao implements DataSummaryDao{

	@Override
	protected String getMapper() {
		return "DataSummaryMapper";
	}

	@Override
	public int updateData(String sqlId, Map params) {
		int result = this.normalUpdate(params, sqlId);
		return result;
	}
	
	@Override
	public int deleteData(String sqlId, Map<String, Object> params) {
		return this.normalDelete(params, sqlId);
	}

	@Override
	public List<Map> getData(String sqlId, Map<String, Object> params) {
		return this.normalSelectList(params, sqlId);
	}

}
