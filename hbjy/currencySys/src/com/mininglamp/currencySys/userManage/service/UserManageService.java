/**
 * 
 */
package com.mininglamp.currencySys.userManage.service;

import java.util.List;
import java.util.Map;

/**
 * @author zhangpeng
 *
 */
public interface UserManageService {
		//查询
		public List<Map> getDataList(Map params,String sqlId);
		//插入
		public int insertData(Map map, String sqlid);
		//删除
		public int deleteData(Map map, String sqlid);
		//更新
		public int updateData(Map map, String sqlid);
}
