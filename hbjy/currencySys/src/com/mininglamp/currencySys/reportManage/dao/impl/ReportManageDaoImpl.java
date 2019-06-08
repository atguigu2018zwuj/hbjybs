package com.mininglamp.currencySys.reportManage.dao.impl;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mininglamp.currencySys.common.base.dao.OracleBaseDao;
import com.mininglamp.currencySys.reportManage.dao.ReportManageDao;

@Repository("reportManageDao")
public class ReportManageDaoImpl extends OracleBaseDao implements ReportManageDao{

	@Override
	public List<Map> getDataList(Map params, String sqlId) {
		return this.normalSelectList(params, sqlId);
	}

	@Override
	public int updateData(Map params, String sqlId) {
		return this.normalUpdate(params, sqlId);
	}
	
	@Override
	protected String getMapper() {
		return "ReportManageMapper";
	}


}
