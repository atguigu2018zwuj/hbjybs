package com.mininglamp.currencySys.zhhb.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.mininglamp.currencySys.zhhb.dao.impl.ZhhbDaoImpl;
import com.mininglamp.currencySys.zhhb.service.ZhhbService;

@Service(value="zhhbService")
public class ZhhbServiceImpl implements ZhhbService {

	@Resource
	private ZhhbDaoImpl zhhbDaoImpl;
	
	@Override
	public List<Map> getDataList(String sqlId, Map<String, Object> params) {
		return zhhbDaoImpl.getDataList(sqlId, params);
	}

	@Override
	public Object updateData(String sqlId, Map<String, Object> params) {
		return zhhbDaoImpl.updateData(sqlId, params);
	}

	@Override
	public int deleteData(String sqlId, Map<String, Object> params) {
		return zhhbDaoImpl.deleteData(sqlId, params);
	}

	@Override
	public List<Map> getDataCount(String sqlId, Map<String, Object> params) {
		return zhhbDaoImpl.getDataCount(sqlId, params);
	}
	
}
