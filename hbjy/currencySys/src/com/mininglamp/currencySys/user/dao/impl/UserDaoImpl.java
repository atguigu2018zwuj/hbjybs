package com.mininglamp.currencySys.user.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mininglamp.currencySys.common.base.dao.OracleBaseDao;
import com.mininglamp.currencySys.user.bean.Menu;
import com.mininglamp.currencySys.user.bean.Office;
import com.mininglamp.currencySys.user.bean.Role;
import com.mininglamp.currencySys.user.bean.User;
import com.mininglamp.currencySys.user.dao.UserDao;


@Repository("userMapperDao")
public class UserDaoImpl extends OracleBaseDao implements UserDao{

	@Override
	public List<Map> getDataList(Map<String, Object> params, String sqlId) {
		return this.normalSelectList(params, sqlId);
	}
	
	@Override
	public List<Map> getListBySqlId(Map params, String sqlId) throws Exception {
		return this.normalSelectList(params, sqlId);
	}

	@Override
	public int insertList(Map params, String sqlId) throws Exception {
		return this.normalInsert(params, sqlId);
	}
	
	@Override
	protected String getMapper() {
		return "userMapper";
	}

	@Override
	public User getUser(Map params, String sqlId) throws Exception {
		return this.getSqlSession().selectOne(getMapper() + "." + sqlId, params);
	}

	@Override
	public List<Role> getRoleList(Map params, String sqlId) throws Exception {
		return this.getSqlSession().selectList(getMapper() + "." + sqlId, params);
	}

	@Override
	public List<Menu> getMenuList(Map params, String sqlId) throws Exception {
		return this.getSqlSession().selectList(getMapper() + "." + sqlId, params);
	}

	@Override
	public List<Office> getOfficeList(Map params, String sqlId)
			throws Exception {
		return this.getSqlSession().selectList(getMapper() + "." + sqlId, params);
	}

	@Override
	public int updateData(Map<String, String> params ,String sqlId) {
		return this.normalUpdate(params, sqlId);
	}
}
