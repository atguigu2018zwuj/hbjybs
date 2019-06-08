/**
 * 
 */
package com.mininglamp.currencySys.userManage.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mininglamp.currencySys.userManage.dao.UserManageDao;
import com.mininglamp.currencySys.userManage.service.UserManageService;

/**
 * @author zhangpeng
 *
 */
@Service("userManageService")
public class UserManageServiceImpl implements UserManageService{
	//注入dao对象
		@Autowired
		UserManageDao userManageDao;
		//查询
		@Override
		public List<Map> getDataList(Map params, String sqlId) {
			return userManageDao.getDataList(params, sqlId);
		}
		//插入
		@Override
		public int insertData(Map map, String sqlid) {
			return userManageDao.insertData(map, sqlid);
		}
		//删除
		@Override
		public int deleteData(Map map, String sqlid) {
			return userManageDao.deleteData(map, sqlid);
		}
		//更新
		@Override
		public int updateData(Map map, String sqlid) {
			return userManageDao.updateData(map, sqlid);
		}
}
