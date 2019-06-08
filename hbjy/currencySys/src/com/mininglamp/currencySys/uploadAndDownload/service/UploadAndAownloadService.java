package com.mininglamp.currencySys.uploadAndDownload.service;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public interface UploadAndAownloadService {

	public List<Map> getDataList(String sqlId, Map<String, Object> params);
	
	public StringBuffer saveData(Map<String, Object> params, String path, MultipartFile reportDataFile, HttpServletRequest request);
	
	public int deleteData(String sqlId, Map<String, Object> params);
	
	public int updateData(String sqlId, Map<String, Object> params);
}
