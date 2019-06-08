package com.mininglamp.currencySys.regulationFile.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mininglamp.currencySys.announcement.bean.AnnouncementBean;
import com.mininglamp.currencySys.common.base.dao.OracleBaseDao;
import com.mininglamp.currencySys.regulationFile.bean.RegulationBean;
import com.mininglamp.currencySys.regulationFile.dao.RegulationDao;

/**
 * <p>Description:数据访问层实现类 </p>
 * @author zrj
 * @date 2019年2月21日
 * @version 1.0
 */
@Repository(value="regulationDao")
public class RegulationDaoImpl extends OracleBaseDao implements RegulationDao {

	@Override
	public List<Map> getDataList(String sqlId, Map<String, Object> params) {
		return this.normalSelectList(params, sqlId);
	}

	@Override
	public int updateData(String sqlId, Map<String, Object> params) {
		return this.normalUpdate(params, sqlId);
	}

	@Override
	public int deleteData(String sqlId, Map<String, Object> params) {
		return this.normalDelete(params, sqlId);
	}

	public List<Map> getDataCount(String sqlId, Map<String, Object> params) {
		return this.normalSelectList(params, sqlId);
	}
	@Override
	public List<RegulationBean> getDataList(String string, RegulationBean params) {
		
		return this.regulationSelectObjectList(params, string);
	}

	@Override
	protected String getMapper() {
		
		return "RegulationMapper";
	}

}
