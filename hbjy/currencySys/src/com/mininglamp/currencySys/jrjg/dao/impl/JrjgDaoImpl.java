package com.mininglamp.currencySys.jrjg.dao.impl;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

import com.mininglamp.currencySys.announcement.bean.AnnouncementBean;
import com.mininglamp.currencySys.common.base.dao.OracleBaseDao;
import com.mininglamp.currencySys.jrjg.dao.JrjgDao;
/**
 * 
 * <p>Description:数据层实现类 </p>
 * @author zrj
 * @date 2019年1月16日
 * @version 1.0
 */
@Repository(value="jrjgDao")
public class JrjgDaoImpl extends OracleBaseDao implements JrjgDao {

	@Override
	protected String getMapper() {
		return "JrjgReportMapper";
	}

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
	public List<AnnouncementBean> getDataList(String string, AnnouncementBean params) {
		// TODO Auto-generated method stub
		return this.normalSelectObjectList(params, string);
	}

}
