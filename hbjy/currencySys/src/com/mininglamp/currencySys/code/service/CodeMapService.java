package com.mininglamp.currencySys.code.service;

import java.util.List;
import java.util.Map;

/**
 * 码表相关接口
 * @author ccw
 * @time 2017/6/17
 * @QQ 452050507
 */
public interface CodeMapService {

	public List<Map> getCodeList(String sqlId) throws Exception;
	
	//地区编码专用查询
	public List<Map> getAreaList(String sqlId,String pid) throws Exception;
	
	//通用带参数查询
	public List<Map> getDataList(String sqlId,Map params) throws Exception;
}
