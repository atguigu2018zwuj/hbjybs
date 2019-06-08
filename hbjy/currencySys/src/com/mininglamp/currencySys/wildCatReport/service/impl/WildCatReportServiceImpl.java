package com.mininglamp.currencySys.wildCatReport.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mininglamp.currencySys.wildCatReport.dao.WildCatReportDao;
import com.mininglamp.currencySys.wildCatReport.service.WildCatReportService;

@Service("wildCatReportService")
public class WildCatReportServiceImpl implements WildCatReportService{
	
	@Autowired
	WildCatReportDao wildCatReportDao;
	
	@Override
	public List<Map> getDataList(Map params, String sqlId) {
		return wildCatReportDao.getDataList(params, sqlId);
	}

	@Override
	public int deleteData(Map params, String sqlId) {
		return wildCatReportDao.deleteData(params, sqlId);
	}

	@Override
	public int updateData(Map params, String sqlId) {
		return wildCatReportDao.updateData(params, sqlId);
	}

	@Override
	public int insertData(Map params, String sqlId) {
		return wildCatReportDao.insertData(params, sqlId);
	}

	@Override
	public List<Map> getDataCount(Map params, String sqlId) {
		return wildCatReportDao.getDataCount(params, sqlId);
	}

}
