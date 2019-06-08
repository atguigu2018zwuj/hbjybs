package com.mininglamp.currencySys.wildCatReport.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mininglamp.currencySys.common.base.dao.OracleBaseDao;
import com.mininglamp.currencySys.wildCatReport.dao.WildCatReportDao;
@Repository("wildCatReportDao")
public class WildCatReportDaoImpl extends OracleBaseDao implements WildCatReportDao{

	@Override
	protected String getMapper() {
		return "WildCatReportMapper";
	}

	@Override
	public List<Map> getDataList(Map params, String sqlId) {
		return this.normalSelectList(params, sqlId);
	}

	@Override
	public int deleteData(Map params, String sqlId) {
		return this.normalDelete(params, sqlId);
	}

	@Override
	public int updateData(Map params, String sqlId) {
		return this.normalUpdate(params, sqlId);
	}

	@Override
	public int insertData(Map params, String sqlId) {
		return this.normalInsert(params, sqlId);
	}

	@Override
	public List<Map> getDataCount(Map params, String sqlId) {
		return this.normalSelectList(params, sqlId);
	}

}
