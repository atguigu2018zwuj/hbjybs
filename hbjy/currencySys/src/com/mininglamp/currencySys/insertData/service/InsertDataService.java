package com.mininglamp.currencySys.insertData.service;

import java.util.List;
import java.util.Map;

public interface InsertDataService {
	// 字段配置-是否自动生成：自动生成
	public static final String IS_INPUT_AUTO_TRUE = "1";

	public int insertData(String sqlId, Map<String, Object> params);

	public List<Map> getData(String sqlId, Map<String, Object> params);

	/**
     * 记录报表每条记录的操作日志
     * @param wjbm 操作的报送表的4位标识码
     * @param recordCode 报表记录的code值
     * @param username 操作者的用户名
     * @param type 操作类型：1-新增；2-修改；3-删除；
     * @return 是否新增记录成功
     */
	public boolean insertRecordOperationLog(String wjbm, int recordCode, String username, String type);
}
