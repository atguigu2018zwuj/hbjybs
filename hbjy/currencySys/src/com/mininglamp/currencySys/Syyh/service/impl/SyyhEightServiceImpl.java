package com.mininglamp.currencySys.Syyh.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mininglamp.currencySys.Syyh.dao.impl.SyyhEightDaoImpl;
import com.mininglamp.currencySys.Syyh.service.SyyhEightService;

@Service(value="syyhEightService")
public class SyyhEightServiceImpl implements SyyhEightService{

	@Autowired
	private SyyhEightDaoImpl syyhEightDaoImpl;
	
	@Override
	public List<Map> getDataList(String sqlId, Map<String, Object> params) {
		return syyhEightDaoImpl.getDataList(sqlId, params);
	}

	@Override
	public Object updateData(String sqlId, Map<String, Object> params) {
		return syyhEightDaoImpl.updateData(sqlId, params);
	}

	@Override
	public int deleteData(String sqlId, Map<String, Object> params) {
		return syyhEightDaoImpl.deleteData(sqlId, params);
	}

	@Override
	public List<Map> getDataCount(String sqlId, Map<String, Object> params) {
		return syyhEightDaoImpl.getDataCount(sqlId, params);
	}
	
}
