package com.mininglamp.currencySys.generateMessage.service;

import java.util.List;
import java.util.Map;

public interface GenerateMessageService {

	public List<Map> getDataList(String sqlId, Map<String, Object> params);
	
	public int updateGeneratereport(String sqlId , Map<String, Object> params);
	
	/**
	 * 判断报文是否通过（若报文标识为1、3、5时通过，否则不通过）
	 * @param reportFlag 报文标识（1:生成报文成功;2:生成报文失败;3:校验成功;4:校验失败;5:上报成功;6:上报失败;66:强制性校验错误）
	 * @return 报文是否通过
	 */
	public boolean isReportPass(String reportFlag);
	
	/**
	 * 当前机构的指定代办事项下所有下级机构的待办事项是否都已完成
	 * @param sjrq 数据日期
	 * @param ywbm 报送表的4位标识码
	 * @param jrjgCjxx 当前代办事项的机构层级信息
	 * @return 如果都已完成则返回true，否则返回false
	 */
	public boolean isChildJrjgTodosAllFinished(String sjrq, String ywbm, String jrjgCjxx);
	
	//插入
	public int insertData(Map map, String sqlid);
	//删除
	public int deleteData(Map map, String sqlid);
	//更新
	public int updateData(Map map, String sqlid);
}
