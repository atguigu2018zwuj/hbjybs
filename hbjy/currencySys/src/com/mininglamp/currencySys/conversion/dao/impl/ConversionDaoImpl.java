package com.mininglamp.currencySys.conversion.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mininglamp.currencySys.common.base.dao.OracleBaseDao;
import com.mininglamp.currencySys.conversion.dao.ConversionDao;

@Repository(value = "conversionDao")
public class ConversionDaoImpl extends OracleBaseDao implements ConversionDao{

	@Override
	protected String getMapper() {
		return "conversion";
	}

	@Override
	public List<Map> getDataList(String sqlId, Map<String, Object> params) {
		return this.normalSelectList(params, sqlId);
	}

	@Override
	public int updateGeneratereport(String sqlId, Map<String, Object> params) {
		return this.normalUpdate(params, sqlId);
	}

}
