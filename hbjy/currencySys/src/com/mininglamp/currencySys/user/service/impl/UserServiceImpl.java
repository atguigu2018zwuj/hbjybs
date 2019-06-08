package com.mininglamp.currencySys.user.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mininglamp.currencySys.user.bean.Menu;
import com.mininglamp.currencySys.user.bean.Office;
import com.mininglamp.currencySys.user.bean.Role;
import com.mininglamp.currencySys.user.bean.User;
import com.mininglamp.currencySys.user.dao.UserDao;
import com.mininglamp.currencySys.user.service.UserService;


@Service("userService")
public class UserServiceImpl implements UserService{
	@Autowired
	private UserDao userDao;
	
	@Override
	public List<Map> getDataList(Map<String, Object> params, String sqlId) {
		return userDao.getDataList(params, sqlId);
	}

	@Override
	public List<Map> getUserList(Map params, String sqlId) throws Exception {
		return userDao.getListBySqlId(params, sqlId);
	}

	@Override
	public User getUser(Map params, String sqlId) throws Exception {
		return userDao.getUser(params, sqlId);
	}

	@Override
	public List<Role> getRoleList(Map params, String sqlId) throws Exception {
		return userDao.getRoleList(params, sqlId);
	}

	@Override
	public List<Menu> getMenuList(Map params, String sqlId) throws Exception {
		return userDao.getMenuList(params, sqlId);
	}

	@Override
	public List<Office> getOfficeList(Map params, String sqlId)
			throws Exception {
		return userDao.getOfficeList(params, sqlId);
	}

	@Override
	public int updateData(Map params, String string) {
		return userDao.updateData(params,string);
	}

	@Override
	public int insertList(Map params, String sqlId) throws Exception {
		return userDao.insertList(params, sqlId);
	}


}
