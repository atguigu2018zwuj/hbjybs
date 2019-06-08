package com.mininglamp.currencySys.code.dao;

import java.util.List;
import java.util.Map;

/**
 * 所有码表接口
 * @author Cheng
 * @time 2017/8/22
 * @QQ 452050507
 */
public interface CodeMapDao {
	
	public List<Map> getCodeList(String sqlId) throws Exception;
	
	//地区编码专用查询
	public List<Map> getAreaList(String sqlId,String pid) throws Exception;
	
	//通用带参数查询
	public List<Map> getDataList(String sqlId,Map params) throws Exception;

}
