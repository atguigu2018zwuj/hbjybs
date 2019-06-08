/**
 * 
 */
package com.mininglamp.currencySys.jglszcxx.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mininglamp.currencySys.common.base.dao.OracleBaseDao;
import com.mininglamp.currencySys.jglszcxx.dao.JglszcxxDao;

/**
 * @author ChenXiangYu
 *
 */
@Repository("jglszcxxDao")
public class JglszcxxDaoImpl extends OracleBaseDao implements JglszcxxDao{
		// 获取xml映射sql
		@Override
		protected String getMapper() {
			return "JglszcxxMapper";
		}
		//查询一个
		@Override
		public Map getDataOne(Map params, String sqlId) {
			return this.normalSelectOne(params, sqlId);
		}
		//查询
		@Override
		public List<Map> getDataList(Map params, String sqlId) {
			return this.normalSelectList(params, sqlId);
		}
		//插入
		@Override
		public int insertData(Map map, String sqlid) {
			return this.normalInsert(map, sqlid);
		}
		//删除
		@Override
		public int deleteData(Map map, String sqlid) {
			return this.normalDelete(map, sqlid);
		}
		//更新
		@Override
		public int updateData(Map map, String sqlid) {
			return this.normalUpdate(map, sqlid);
		}
}
