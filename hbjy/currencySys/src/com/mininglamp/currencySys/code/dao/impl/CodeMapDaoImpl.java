package com.mininglamp.currencySys.code.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mininglamp.currencySys.code.dao.CodeMapDao;
import com.mininglamp.currencySys.common.base.dao.OracleBaseDao;




/**
 * 码表相关
 * @author ccw
 * @time 2017/6/17
 * @QQ 452050507
 */
@Repository("codeMapDao")
public class CodeMapDaoImpl extends OracleBaseDao implements CodeMapDao{
	
	@Override
	protected String getMapper() {
		return "codeMapMapper";
	}

	@Override
	public List<Map> getCodeList(String sqlId) throws Exception {
		Map<Object, Object> params = new HashMap<Object, Object>();
		return this.normalSelectList(params, sqlId);
	}

	@Override
	public List<Map> getAreaList(String sqlId, String pid) throws Exception {
		Map<Object, Object> params = new HashMap<Object, Object>();
		params.put("pid", pid);
		return this.normalSelectList(params, sqlId);
	}

	@Override
	public List<Map> getDataList(String sqlId, Map params) throws Exception {
		return this.normalSelectList(params, sqlId);
	}
	
	
	

}
