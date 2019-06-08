package com.mininglamp.currencySys.uploadAndDownload.dao;
import java.util.List;
import java.util.Map;
public interface UploadAndAownloadDao{

	public List<Map> getDataList(String string, Map<String, Object> params);
	
	public int saveData(String string, Map<String, Object> params);
	
	public int deleteData(String string, Map<String, Object> params);
	
	public int updateData(String string, Map<String, Object> params);

	public int insertData(String string, Map<String, Object> params);
}
