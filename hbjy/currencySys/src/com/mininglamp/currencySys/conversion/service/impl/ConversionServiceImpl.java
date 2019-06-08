package com.mininglamp.currencySys.conversion.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mininglamp.currencySys.conversion.dao.ConversionDao;
import com.mininglamp.currencySys.conversion.service.ConversionService;


@Service(value = "conversionService")
public class ConversionServiceImpl implements ConversionService{

	@Autowired
	private ConversionDao conversionDao;

	@Override
	public List<Map> getDataList(String sqlId, Map<String, Object> params) {
		return conversionDao.getDataList(sqlId, params);
	}

	@Override
	public int updateGeneratereport(String sqlId, Map<String, Object> params) {
		return conversionDao.updateGeneratereport(sqlId, params);
	}
	
}
