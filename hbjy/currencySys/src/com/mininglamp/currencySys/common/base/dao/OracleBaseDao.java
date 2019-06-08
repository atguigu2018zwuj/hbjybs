package com.mininglamp.currencySys.common.base.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mininglamp.currencySys.announcement.bean.AnnouncementBean;
import com.mininglamp.currencySys.regulationFile.bean.RegulationBean;
/**
 * 通用mybatis基本持久层<br/>
 * 提供基本的增删改查方法
 * @author czy
 * 2016年7月1日11:19:41
 */
@SuppressWarnings("unchecked")
@Repository
public abstract class OracleBaseDao extends SqlSessionDaoSupport{
	
	@Autowired
	public void setSqlSessionFactory(@Qualifier(value="oracleSqlSessionFactory") SqlSessionFactory oracleSqlSessionFactory) {
		super.setSqlSessionFactory(oracleSqlSessionFactory);
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
	
	public int normalInsertList(List<Map> map, String sqlid) {
		int result = getSqlSession().insert(getMapper() + "." + sqlid, map);
		//int result = getSqlSession().delete(getMapper() + "." + sqlid, map);
		return result;
	}
	protected abstract String getMapper();
	
	/**
	 * 公告查询返回集合
	 * @param map
	 * @param sqlid
	 * @return
	 */
	public List<AnnouncementBean> normalSelectObjectList(Object map, String sqlid) {
		List<AnnouncementBean> list=getSqlSession().selectList(getMapper() + "." + sqlid,map);
		return list;
	}
	
	/**
	 * 公告查询返回集合
	 * @param map
	 * @param sqlid
	 * @return
	 */
	public List<RegulationBean> regulationSelectObjectList(Object map, String sqlid) {
		List<RegulationBean> list=getSqlSession().selectList(getMapper() + "." + sqlid,map);
		return list;
	}
	/**
	 * 传入list参数
	 * @param map
	 * @param sqlid
	 * @return
	 */
	public List<Map> messageSelectObjectList(List list, String sqlid) {
		List<Map> result=getSqlSession().selectList(getMapper() + "." + sqlid,list);
		return result;
	}
}
