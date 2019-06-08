/**
 * 
 */
package com.mininglamp.currencySys.dataSummary.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mininglamp.currencySys.dataSummary.dao.DataSummaryDao;
import com.mininglamp.currencySys.dataSummary.dao.impl.DataSummaryDaoImpl;
import com.mininglamp.currencySys.dataSummary.service.DataSummaryService;

/**
 * @author zhangpeng
 *
 */
@Service(value = "dataSummaryService")
public class DataSummaryServiceImpl implements DataSummaryService {

	@Autowired
	private DataSummaryDaoImpl dataSummaryDaoImpl;

	@Override
	public Object updateData(String sqlId, Map<String, Object> params) {
		return dataSummaryDaoImpl.updateData(sqlId, params);
	}

	@Override
	public int deleteData(String sqlId, Map<String, Object> params) {
		return dataSummaryDaoImpl.deleteData(sqlId, params);
	}
	
	@Override
	public List<Map> getData(String sqlId, Map<String, Object> params) {
		return dataSummaryDaoImpl.getData(sqlId, params);
	}
}
