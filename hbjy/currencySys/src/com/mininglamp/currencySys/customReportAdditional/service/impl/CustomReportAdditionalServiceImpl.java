package com.mininglamp.currencySys.customReportAdditional.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.mininglamp.currencySys.customReportAdditional.dao.impl.CustomReportAdditionalDaoImpl;
import com.mininglamp.currencySys.customReportAdditional.service.CustomReportAdditionalService;

@Service(value="customReportAdditionalService")
public class CustomReportAdditionalServiceImpl  implements CustomReportAdditionalService{

	@Autowired
	private CustomReportAdditionalDaoImpl customReportAdditionalDaoImpl;
	
	@Override
	public List<Map> getDataList(String sqlId, Map<String, Object> params) {
		return customReportAdditionalDaoImpl.getDataList(sqlId, params);
	}
	
	//插入
	@Override
	public int insertData(Map map, String sqlid) {
		return customReportAdditionalDaoImpl.insertData(map, sqlid);
	}
	//删除
	@Override
	public int deleteData(Map map, String sqlid) {
		return customReportAdditionalDaoImpl.deleteData(map, sqlid);
	}
	//更新
	@Override
	public int updateData(Map map, String sqlid) {
		return customReportAdditionalDaoImpl.updateData(map, sqlid);
	}
}
