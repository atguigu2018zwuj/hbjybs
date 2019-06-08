package com.mininglamp.currencySys.user.service;

import java.util.List;
import java.util.Map;

import com.mininglamp.currencySys.user.bean.Menu;
import com.mininglamp.currencySys.user.bean.Office;
import com.mininglamp.currencySys.user.bean.Role;
import com.mininglamp.currencySys.user.bean.User;

public interface UserService {
	public List<Map> getDataList(Map<String, Object> params, String sqlId);
	
	public List<Map> getUserList(Map params,String sqlId) throws Exception;
	
	public User getUser(Map params,String sqlId) throws Exception;
	
	public List<Role> getRoleList(Map params,String sqlId) throws Exception;

	public List<Menu> getMenuList(Map params,String sqlId) throws Exception;
	
	public List<Office> getOfficeList(Map params,String sqlId) throws Exception;
	
	public int updateData(Map params, String string);
	
	public int insertList(Map params, String sqlId) throws Exception;
}
