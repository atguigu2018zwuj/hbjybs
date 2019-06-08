package com.mininglamp.currencySys.customReport.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mininglamp.currencySys.customReport.dao.impl.CustomReportDaoImpl;
import com.mininglamp.currencySys.customReport.service.CustomReportService;

@Service(value="customReportService")
public class CustomReportServiceImpl  implements CustomReportService{

	@Autowired
	private CustomReportDaoImpl customReportDaoImpl;
	
	@Override
	public List<Map> getDataList(Map<String, Object> params, String sqlId) {
		return customReportDaoImpl.getDataList(sqlId, params);
	}
	
	@Override
	public List<Map> getCbsBdpalList(Map<String, Object> params) {
		// 先删除一次表
		try{
			customReportDaoImpl.updateData(params, "dropTempCbsDwmcOne");
		}catch(Exception e){System.out.println(e.getMessage());}
		try{
			customReportDaoImpl.updateData(params, "dropTempcbsDwmcTwo");
		}catch(Exception e){System.out.println(e.getMessage());}
		try{
			customReportDaoImpl.updateData(params, "dropTempCbsDwmcThree");
		}catch(Exception e){System.out.println(e.getMessage());}
		// 先创建临时表
		int count1 = customReportDaoImpl.updateData(params, "createTempCbsDwmcOne");
		int count2 = customReportDaoImpl.updateData(params, "createTempcbsDwmcTwo");
		int count3 = customReportDaoImpl.updateData(params, "createTempCbsDwmcThree");
		List<Map> results = null;
		try {
			results = customReportDaoImpl.getDataList("getCbs_bdpal", params);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 最后删除临时表
			if (count1 > 0) {
				customReportDaoImpl.updateData(params, "dropTempCbsDwmcOne");
			}
			if (count2 > 0) {
				customReportDaoImpl.updateData(params, "dropTempcbsDwmcTwo");
			}
			if (count3 > 0) {
				customReportDaoImpl.updateData(params, "dropTempCbsDwmcThree");
			}
		}
		
		return results;
	}
	
	@Override
	public List<Map> getDateTreeDep() {
		List<Map> dateTree = new ArrayList<Map>();
		Map<String,Object> params = new HashMap<String,Object>();
		List<Map> dateList = customReportDaoImpl.getDataList("getDateTree_dep", params);
		// 数据日期
		if (dateList != null && !dateList.isEmpty()) {
			// 由数据日期yyyymm取得层级树：yyyy-yyyyQn-yyyymm
			Map dateTreeNode = null;
			String sjrqYM = null;// 数据日期年月
			String sjrqYQ = null;// 数据日期季度
			StringBuilder node1 = null;// node按照 id*name*level*pId 的形式拼成的字符串
			StringBuilder node2 = null;// node按照 id*name*level*pId 的形式拼成的字符串
			StringBuilder node3 = null;// node按照 id*name*level*pId 的形式拼成的字符串
			Set<String> tempSet = new HashSet<String>();
			for (Map date : dateList) {
				sjrqYM = date.get("jioyrq_ym").toString();
				sjrqYQ = sjrqYM.substring(0, 4)+'Q'+((Integer.parseInt(sjrqYM.substring(4,6))-1)/3+1);
				// level=3
//				node3 = new StringBuilder("");
//				node3.append(sjrqYM);
//				node3.append("*");
//				node3.append(sjrqYM);
//				node3.append("*3*");
//				node3.append(sjrqYQ);
				// level=2
				node2 = new StringBuilder("");
				node2.append(sjrqYQ);
				node2.append("*");
				node2.append(sjrqYQ);
				node2.append("*2*");
				node2.append(sjrqYM.substring(0,4));
				// level=3
				node1 = new StringBuilder("");
				node1.append(sjrqYM.substring(0,4));
				node1.append("*");
				node1.append(sjrqYM.substring(0,4));
				node1.append("*1*sjrq_root");
//				tempSet.add(node3.toString());
				tempSet.add(node2.toString());
				tempSet.add(node1.toString());
			}
			tempSet.add("sjrq_root*数据日期*0*0");// 顶级节点
			// 取出去重后的node
			for (String node : tempSet) {
				dateTreeNode = new HashMap();
				dateTreeNode.put("id", node.split("\\*")[0]);
				dateTreeNode.put("name", node.split("\\*")[1]);
				dateTreeNode.put("level", node.split("\\*")[2]);
				dateTreeNode.put("pId", node.split("\\*")[3]);
				dateTree.add(dateTreeNode);
			}
		}
		return dateTree;
	}
	
	@Override
	public int updateGeneratereport(Map<String, Object> params, String sqlId){
		return customReportDaoImpl.updateGeneratereport(sqlId ,params);
	}
	
	//插入
	@Override
	public int insertData(Map map, String sqlid) {
		return customReportDaoImpl.insertData(map, sqlid);
	}
	//删除
	@Override
	public int deleteData(Map map, String sqlid) {
		return customReportDaoImpl.deleteData(map, sqlid);
	}
	//更新
	@Override
	public int updateData(Map map, String sqlid) {
		return customReportDaoImpl.updateData(map, sqlid);
	}
}
