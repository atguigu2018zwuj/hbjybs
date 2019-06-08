package com.mininglamp.currencySys.common.base.dao;

import java.util.List;
import java.util.Map;
/**
 * 通用mybatis持久层操作
 * @author czy
 *
 */
@SuppressWarnings("unchecked")
public interface MybatisBaseDaoInter {
	/**
	 * 通用获取列表
	 * @param map
	 * @param sqlid
	 * @return
	 */
	
	public List<Map> normalSelectList(Map map, String sqlid) ;
	/**
	 * 通用获取单条数据
	 * @param map
	 * @param sqlid
	 * @return
	 */
	public Map normalSelectOne(Map map, String sqlid) ;
	/**
	 * 通用插入
	 * @param map
	 * @param sqlid
	 * @return
	 */
	public int normalInsert(Map map, String sqlid) ;
	/**
	 * 通用更新
	 * @param map
	 * @param sqlid
	 * @return
	 */
	public int normalUpdate(Map map, String sqlid);
	/**
	 * 通用删除
	 * @param map
	 * @param sqlid
	 * @return
	 */
	public int normalDelete(Map map, String sqlid);
}
