package com.mininglamp.currencySys.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 堆缓存常量
 * add by ice 2017年6月19日20:51:36
 */

public class StaticMap {
	//堆中存放 少量的缓存数据
	
    //贷款存量
//	public static HashMap<String, List<Map<String,Object>>> staticHashMapMore = new HashMap<String, List<Map<String,Object>>>();
//	
//	/**
//	 * 贷款存量-清除存在堆中的统计结果
//	 */
//	public static void removestaticHashMapAssistPoorCount(String keyStr)
//	{
//		staticHashMapMore.remove(keyStr);
//	}
	
	   //贷款存量
		public static HashMap<String, List<Map>> staticHashMapMore = new HashMap<String, List<Map>>();
		
		//融资云图
		public static HashMap<String, List<Map>> staticHashMapRz = new HashMap<String, List<Map>>();
		public static HashMap<String, List<Map>> staticHashMapAreaTree = new HashMap<String, List<Map>>();
		//融资-搜索结果
		public static HashMap<String, List<Map>> staticHashMapConsultResult = new HashMap<String, List<Map>>();
		
		/** 贷款存量-清除存在堆中的统计结果*/
		public static void removestaticHashMapMore(String keyStr)
		{
			staticHashMapMore.remove(keyStr);
		}
		
		/** 融资云图*/
		public static void removestaticHashMapRz(String keyStr)
		{
			staticHashMapRz.remove(keyStr);
		}
		
		/**地区tree*/
		public static void removestaticHashMapAreaTree(String keyStr)
		{
			staticHashMapAreaTree.remove(keyStr);
		}
		
		/**融资-搜索结果*/
		public static void removeStaticHashMapConsultResult(String keyStr)
		{
			staticHashMapConsultResult.remove(keyStr);
		}
		
	
}
