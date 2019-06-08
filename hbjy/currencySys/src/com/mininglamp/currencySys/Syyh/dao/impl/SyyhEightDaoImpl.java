package com.mininglamp.currencySys.Syyh.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mininglamp.currencySys.ManaPro.dao.ManaProDao;
import com.mininglamp.currencySys.Syyh.dao.SyyhEightDao;
import com.mininglamp.currencySys.common.base.dao.OracleBaseDao;

@Repository(value="syyheightDaoImpl")
public class SyyhEightDaoImpl extends OracleBaseDao implements SyyhEightDao{

	@Override
	protected String getMapper() {
		return "syyh";
	}

	@Override
	public List<Map> getDataList(String sqlId, Map<String, Object> params) {
		return this.normalSelectList(params, sqlId);
	}

	@Override
	public int updateData(String sqlId, Map<String, Object> params) {
		return this.normalUpdate(params, sqlId);
	}

	@Override
	public int deleteData(String sqlId, Map<String, Object> params) {
		return this.normalDelete(params, sqlId);
	}

	public List<Map> getDataCount(String sqlId, Map<String, Object> params) {
		return this.normalSelectList(params, sqlId);
	}

}
