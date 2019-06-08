package com.mininglamp.currencySys.generateMessage.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.mininglamp.currencySys.generateMessage.dao.impl.GenerateMessageDaoImpl;
import com.mininglamp.currencySys.generateMessage.service.GenerateMessageService;

@Service(value="generateMessageService")
public class GenerateMessageServiceImpl  implements GenerateMessageService{

	@Autowired
	private GenerateMessageDaoImpl generateMessageDaoImpl;
	
	@Override
	public List<Map> getDataList(String sqlId, Map<String, Object> params) {
		return generateMessageDaoImpl.getDataList(sqlId, params);
	}
	
	@Override
	public int updateGeneratereport(String sqlId , Map<String, Object> params){
		return generateMessageDaoImpl.updateGeneratereport(sqlId ,params);
	}

	@Override
	public boolean isReportPass(String reportBiaoshi) {
		return !StringUtils.isEmpty(reportBiaoshi) && ("1".equals(reportBiaoshi) || "3".equals(reportBiaoshi) || "5".equals(reportBiaoshi));
	}
	
	@Override
	public boolean isChildJrjgTodosAllFinished(String sjrq, String ywbm, String jrjgCjxx) {
		// 查询参数
		Map<String, Object> searchParams = new HashMap<String, Object>();
		// 当前机构的打回所对应的所有下级机构的打回的机构层级信息
		List<String> childjrjgCjxxList = new ArrayList<String>();
		int childJbJgCount = jrjgCjxx.split("-\\>").length-1;// 当前机构的打回所对应的下级机构的打回的数目
		String tempJrjgCjxx = null;// 临时存储所有下级打回信息的机构层级信息
		for (int i = 1; i <= childJbJgCount; i++) {
			tempJrjgCjxx = tempJrjgCjxx == null ? jrjgCjxx.substring(jrjgCjxx.indexOf("->")+2,jrjgCjxx.length()) 
					: tempJrjgCjxx.substring(tempJrjgCjxx.indexOf("->")+2,tempJrjgCjxx.length());
			childjrjgCjxxList.add(tempJrjgCjxx);
		}
		if (!childjrjgCjxxList.isEmpty()) {
			searchParams.put("sjrq", sjrq);
			searchParams.put("ywbm", ywbm);
			searchParams.put("jrjgCjxxList", childjrjgCjxxList);
			searchParams.put("isFinished", false);
			List<Map> dataList = generateMessageDaoImpl.getDataList("selectRepulseReport", searchParams);
			if (dataList != null && !dataList.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	//插入
	@Override
	public int insertData(Map map, String sqlid) {
		return generateMessageDaoImpl.insertData(map, sqlid);
	}
	//删除
	@Override
	public int deleteData(Map map, String sqlid) {
		return generateMessageDaoImpl.deleteData(map, sqlid);
	}
	//更新
	@Override
	public int updateData(Map map, String sqlid) {
		return generateMessageDaoImpl.updateData(map, sqlid);
	}
}
