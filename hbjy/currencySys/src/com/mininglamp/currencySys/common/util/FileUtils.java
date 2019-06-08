package com.mininglamp.currencySys.common.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 文件操作工具类
 * @author ZhangXialei
 *
 */
public class FileUtils {
	
	/**
	 * 返回指定文件的指定值
	 * @param fileName
	 * @param key
	 * @return
	 */
	public static String getValueByKey(String fileName,String key)throws Exception{
		//文件在class的跟路径
		InputStream input = FileUtils.class.getClassLoader().getResourceAsStream(fileName);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(input));
		
		String value = "";
		Properties pro = new Properties();
		pro.load(br);
		value = pro.getProperty(key);
		return value;
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(getValueByKey("solr.properties","solrServer"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
