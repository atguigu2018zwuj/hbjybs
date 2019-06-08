package com.mininglamp.currencySys.jrjg.service;
import java.util.List;
import java.util.Map;
/**
 * 
 * <p>Description:服务层 </p>
 * @author zrj
 * @date 2019年1月16日
 * @version 1.0
 */
public interface JrjgService {
	public List<Map> getDataList(String sqlId, Map<String, Object> params);

	public Object updateData(String string, Map<String, Object> params);
	
	public int deleteData(String sqlId, Map<String, Object> params);

	public List<Map> getDataCount(String sqlId, Map<String, Object> params);

}
