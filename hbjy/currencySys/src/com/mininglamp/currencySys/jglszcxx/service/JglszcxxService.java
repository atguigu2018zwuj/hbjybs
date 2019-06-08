/**
 * 
 */
package com.mininglamp.currencySys.jglszcxx.service;

import java.util.List;
import java.util.Map;

/**
 * @author ChenXiangYu
 *
 */
public interface JglszcxxService {
		//查询一个
		public Map getDataOne(Map params, String sqlId);
		//查询
		public List<Map> getDataList(Map params,String sqlId);
		//插入
		public int insertData(Map map, String sqlid);
		//删除
		public int deleteData(Map map, String sqlid);
		//更新
		public int updateData(Map map, String sqlid);
}
