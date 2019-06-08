/**
 * 
 */
package com.mininglamp.currencySys.dataSummary.dao;

import java.util.List;
import java.util.Map;

/**
 * @author zhangpeng
 *
 */
public interface DataSummaryDao {
	
	public int updateData(String sqlId, Map params);
	
	public int deleteData(String string, Map<String, Object> params);

	public List<Map> getData(String sqlId, Map<String, Object> params);
}
