package com.mininglamp.currencySys.code.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mininglamp.currencySys.code.dao.CodeMapDao;
import com.mininglamp.currencySys.code.service.CodeMapService;



@Service("codeMapService")
public class CodeMapServiceImpl implements CodeMapService{
	@Autowired
	private CodeMapDao codeMapDao;

	@Override
	public List<Map> getCodeList(String sqlId) throws Exception {
		return codeMapDao.getCodeList(sqlId);
	}

	@Override
	public List<Map> getAreaList(String sqlId, String pid) throws Exception {
		return codeMapDao.getAreaList(sqlId, pid);
	}

	@Override
	public List<Map> getDataList(String sqlId, Map params) throws Exception {
		return codeMapDao.getDataList(sqlId, params);
	}

}
