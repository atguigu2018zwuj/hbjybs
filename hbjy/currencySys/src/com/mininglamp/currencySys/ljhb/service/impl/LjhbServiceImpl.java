package com.mininglamp.currencySys.ljhb.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mininglamp.currencySys.ljhb.dao.LjhbDao;
import com.mininglamp.currencySys.ljhb.dao.impl.LjhbDaoImpl;
import com.mininglamp.currencySys.ljhb.service.LjhbService;

@Service(value="ljhbService")
public class LjhbServiceImpl implements LjhbService{

	@Autowired
	private LjhbDao ljhbDao;
	
	@Override
	public List<Map> getDataList(String sqlId, Map<String, Object> params) {
		return ljhbDao.getDataList(sqlId, params);
	}

	@Override
	public Object updateData(String sqlId, Map<String, Object> params) {
		return ljhbDao.updateData(sqlId, params);
	}

	@Override
	public int deleteData(String sqlId, Map<String, Object> params) {
		return ljhbDao.deleteData(sqlId, params);
	}

	@Override
	public List<Map> getDataCount(String sqlId, Map<String, Object> params) {
		return ljhbDao.getDataCount(sqlId, params);
	}

}
