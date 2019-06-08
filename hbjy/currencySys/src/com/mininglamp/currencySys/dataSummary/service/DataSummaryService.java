/**
 * 
 */
package com.mininglamp.currencySys.dataSummary.service;

import java.util.List;
import java.util.Map;

/**
 * @author zhangpeng
 *
 */
public interface DataSummaryService {

	public Object updateData(String string, Map<String, Object> params);
	
	public int deleteData(String sqlId, Map<String, Object> params);

	public List<Map> getData(String sqlId, Map<String, Object> params);
}
