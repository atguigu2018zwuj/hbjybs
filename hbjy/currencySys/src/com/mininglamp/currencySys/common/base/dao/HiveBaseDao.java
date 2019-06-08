package com.mininglamp.currencySys.common.base.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
/**
 * 通用mybatis基本持久层-[HIVE]<br/>
 * 提供基本的增删改查方法
 * @author czy
 * 2016年7月1日11:19:41
 */
@SuppressWarnings("unchecked")
@Repository
public abstract class HiveBaseDao extends SqlSessionDaoSupport{
	
	@Autowired
	public void setSqlSessionFactory(@Qualifier(value="hiveSqlSessionFactory") SqlSessionFactory hiveSqlSessionFactory) {
		super.setSqlSessionFactory(hiveSqlSessionFactory);
	}
	
	/**
	 * 通用获取列表
	 * @param map
	 * @param sqlid
	 * @return
	 */
	public List<Map> normalSelectList(Map map, String sqlid) {
		List<Map> list=getSqlSession().selectList(getMapper() + "." + sqlid,map);
		return list;
	}
	/**
	 * 通用获取单条数据
	 * @param map
	 * @param sqlid
	 * @return
	 */
	public Map normalSelectOne(Map map, String sqlid) {
		List<Map> list = normalSelectList(map,sqlid);
		if(list != null && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 通用插入
	 * @param map
	 * @param sqlid
	 * @return
	 */
	public int normalInsert(Map map, String sqlid) {
		int result = 0;
		result = getSqlSession().insert(getMapper() + "." + sqlid, map);
		return result;
	}
	/**
	 * 通用更新
	 * @param map
	 * @param sqlid
	 * @return
	 */
	public int normalUpdate(Map map, String sqlid) {
		int result = getSqlSession().update(getMapper() + "." + sqlid, map);
		return result;
	}
	/**
	 * 通用删除
	 * @param map
	 * @param sqlid
	 * @return
	 */
	public int normalDelete(Map map, String sqlid) {
		int result = getSqlSession().delete(getMapper() + "." + sqlid, map);
		return result;
	}
	protected abstract String getMapper();
}
