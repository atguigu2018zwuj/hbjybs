package com.mininglamp.currencySys.common.xmlmap.bean;

import java.util.HashMap;
import java.util.Map;

/************************
 * 
 * @author 刘振华
 * @date 20150914
 * 
 *       代表一个配置文件
 * 
 * 
 */
public class BatisConfiguration {
	/***************************
	 * key 是sqlmap文件中的select id value 是该id对应的一个封装SQLBean对象
	 */
	private Map<String, SQLBean> conf = null;
	private Map<String, ResultMap> resultmaps = null;

	public BatisConfiguration() {
		super();
		// TODO Auto-generated constructor stub
		conf = new HashMap<String, SQLBean>();
		resultmaps = new HashMap<String, ResultMap>();
	}

	/****************************
	 * 
	 * @param sqlid
	 *            是sqlmap文件中的select id
	 * @param bean
	 *            是该id对应的一个封装SQLBean对象
	 */
	public void addSQLBean(String sqlid, SQLBean bean) {
		if (sqlid == null || "".equals(sqlid.trim()) || bean == null)
			return;
		else
			conf.put(sqlid, bean);
	}

	/***************************
	 * 
	 * @param sqlid
	 *            是sqlmap文件中的select id
	 * @return 根据唯一的sqlid获取一个查询对象
	 */
	public SQLBean getSQLBean(String sqlid) {
		if (sqlid == null || "".equals(sqlid))
			return null;
		return this.conf.get(sqlid);
	}

	public void addResultMap(String mapid, ResultMap rmap) {
		if (mapid == null || "".equals(mapid.trim()) || rmap == null)
			return;
		else
			resultmaps.put(mapid, rmap);
	}

	public ResultMap getResultMap(String mapid) {
		if (mapid == null || "".equals(mapid.trim()))
			return null;
		return this.resultmaps.get(mapid);
	}
}
