package com.mininglamp.currencySys.uploadAndDownload.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mininglamp.currencySys.common.base.dao.OracleBaseDao;
import com.mininglamp.currencySys.uploadAndDownload.dao.UploadAndAownloadDao;

@Repository(value="testDao")
public class UploadAndAownloadDaoImp extends OracleBaseDao implements UploadAndAownloadDao{

	@Override
	public List<Map> getDataList(String sqlId, Map<String, Object> params) {
		return this.normalSelectList(params, sqlId);
	}

	@Override
	protected String getMapper() {
		return "testDaoImp";
	}

	@Override
	public int saveData(String sqlId, Map<String, Object> params) {
		return this.normalInsert(params, sqlId);
	}

	@Override
	public int deleteData(String sqlId, Map<String, Object> params) {
		return this.normalDelete(params, sqlId);
	}
	
	@Override
	public int updateData(String sqlId, Map<String, Object> params) {
		return this.normalUpdate(params, sqlId);
	}

	@Override
	public int insertData(String sqlId, Map<String, Object> params) {
		return this.normalInsert(params, sqlId);
	}

}
