/**
 * 
 */
package com.mininglamp.currencySys.jglszcxx.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mininglamp.currencySys.jglszcxx.dao.JglszcxxDao;
import com.mininglamp.currencySys.jglszcxx.service.JglszcxxService;

/**
 * @author ChenXiangYu
 *
 */
@Service("jglszcxxService")
public class JglszcxxServiceImpl implements JglszcxxService{
		@Autowired
		JglszcxxDao jglszcxxDao;
		
		//查询一个
		@Override
		public Map getDataOne(Map params, String sqlId) {
			return jglszcxxDao.getDataOne(params, sqlId);
		}
		//查询
		@Override
		public List<Map> getDataList(Map params, String sqlId) {
			return jglszcxxDao.getDataList(params, sqlId);
		}
		//插入
		@Override
		public int insertData(Map map, String sqlid) {
			return jglszcxxDao.insertData(map, sqlid);
		}
		//删除
		@Override
		public int deleteData(Map map, String sqlid) {
			return jglszcxxDao.deleteData(map, sqlid);
		}
		//更新
		@Override
		public int updateData(Map map, String sqlid) {
			return jglszcxxDao.updateData(map, sqlid);
		}
}
