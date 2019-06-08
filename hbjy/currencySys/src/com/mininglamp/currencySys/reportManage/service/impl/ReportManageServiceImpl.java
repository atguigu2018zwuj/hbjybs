package com.mininglamp.currencySys.reportManage.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.mininglamp.currencySys.reportManage.dao.ReportManageDao;
import com.mininglamp.currencySys.reportManage.service.ReportManageService;

@Service("reportManageService")
public class ReportManageServiceImpl implements ReportManageService{
	
	@Autowired
	ReportManageDao reportManageDao;
	
	@Override
	public List<Map> getDataList(Map params, String sqlId) {
		return reportManageDao.getDataList(params, sqlId);
	}

	@Override
	public int updateData(Map params, String sqlId) {
		return reportManageDao.updateData(params, sqlId);
	}

}
