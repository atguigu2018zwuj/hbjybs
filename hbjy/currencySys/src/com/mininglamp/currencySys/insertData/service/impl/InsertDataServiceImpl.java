package com.mininglamp.currencySys.insertData.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mininglamp.currencySys.insertData.dao.impl.InsertDataDaoImpl;
import com.mininglamp.currencySys.insertData.service.InsertDataService;

@Service(value="insertDataService")
public class InsertDataServiceImpl implements InsertDataService{
	private Logger logger = LoggerFactory.getLogger(InsertDataServiceImpl.class);

	@Autowired
	private InsertDataDaoImpl insertDataDaoImpl;
	
	@Override
	public int insertData(String sqlId, Map<String, Object> params) {
		return insertDataDaoImpl.insertData(sqlId, params);
	}
	
	@Override
	public List<Map> getData(String sqlId, Map<String, Object> params) {
		return insertDataDaoImpl.getData(sqlId, params);
	}
	
	@Override
    public boolean insertRecordOperationLog(String wjbm,int recordCode,String username,String type){
    	if (StringUtils.isEmpty(wjbm)) {
    		logger.error("记录报表每条记录的操作日志-参数错误：wjbm为空");
    		return false;
    	} else if (StringUtils.isEmpty(username)) {
    		logger.error("记录报表每条记录的操作日志-参数错误：username为空");
    		return false;
    	} else if (!"1".equals(type) && !"2".equals(type) && !"3".equals(type)) {
    		logger.error("记录报表每条记录的操作日志-参数错误：type的值未定义");
    		return false;
    	}
    	Map<String,Object> param = new HashMap<String,Object>();
    	param.put("wjbm", wjbm);
    	param.put("recordCode", recordCode);
    	param.put("username", username);
    	param.put("type", type);
    	int insertResult = 0;
    	try {
    		insertResult = this.insertData("insertRecordsOperationLogData", param);
    	} catch (Exception e) {
    		logger.error("记录报表数据操作日志异常",e);
    		return false;
    	}
    	if (insertResult <= 0) {
    		logger.warn("记录报表数据操作日志失败");
    		return false;
    	}
    	return true;
    }
}
