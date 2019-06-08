package com.mininglamp.currencySys.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.time.DateUtils;

/**
 * 时间格式化工具类
 * @author ZhangXiaolei(Rayen)
 *
 */
public class DateFormatUtils extends DateUtils{
	
	/**
	 * 日期格式模板化,日期转换字符串
	 * @param date
	 * @param format
	 * @return
	 */
	public static String dateFormat(Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	public static String getDateStr(String format) {
		return new SimpleDateFormat(format).format(new Date());
	}
	
	
	
	/**
	 * 字符串转换日期
	 * @param str
	 * @param format
	 * @return
	 */
	public static Date stringToDate(String str,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if(null != str && !"".equals(str)){
			try {
				return sdf.parse(str);
			} catch (Exception e) {
			}
		}
		return null;
	}
	
	/**
	 * 根据dateNum,返回日期
	 * @param dateNnm	为正则往后，为负则往前，为0则返回new Date()   
	 * @param format	格式化
	 * @return
	 */
	public static String getDateByNum(String date,int dateNnm,String format){
		DateFormat df = new SimpleDateFormat(format);
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(stringToDate(date,format));
		//修改后的日期
		gc.add(5, dateNnm);
		return df.format(gc.getTime());
	}
	
	/**
	 * 得到2个时间的时间差
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static long getDifftime(String beginDate,String endDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date begin = null;
		Date end = null;
		try {
			 begin = sdf.parse(beginDate);
			 end = sdf.parse(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long between  = 0;
		if(begin != null && end != null){
			 between = (end.getTime() - begin.getTime()) / (24*3600*1000);
		}
		
		return between;
	}
	
	/**
	 * 讲指定日期转换成指定的新格式
	 * @author Raven(ZhangXiaolei)
	 * @date 2016-5-10
	 * @describe 
	 * @param date			指定日期
	 * @param formatOld		指定日期的旧格式
	 * @param formatNew		新格式
	 * @return
	 */
	public static String getAssignFormat(String date,String formatOld ,String formatNew){
		SimpleDateFormat sdfOld = new SimpleDateFormat(formatOld);
		SimpleDateFormat sdfNew = new SimpleDateFormat(formatNew);
		
		try {
			Date assignDate = sdfOld.parse(date);
			
			date = sdfNew.format(assignDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
