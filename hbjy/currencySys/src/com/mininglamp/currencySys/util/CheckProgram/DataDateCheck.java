package com.mininglamp.currencySys.util.CheckProgram;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期类型根式正确性验证
 * @author lhl
 * @date 2017年2月13日
 * @version 1.0
 */
public class DataDateCheck {

	/**
	 * 数据日期格式是否正确
	 * @param dataDate:数据日期
	 * @throws 
	 * @return
	 */
	public  static boolean isValidDataFormat(String dataDate) {
		String datePattern1 = "\\d{4}-\\d{2}-\\d{2}";
		String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))"
				+ "[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"
				+ "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"
				+ "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?("
				+ "(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"
				+ "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
		if ((dataDate != null)) {
			Pattern pattern = Pattern.compile(datePattern1);
			Matcher match = pattern.matcher(dataDate);
			if (match.matches()) {
				pattern = Pattern.compile(datePattern2);
				match = pattern.matcher(dataDate);
				return match.matches();
			} else {
				return false;
			}
		}
		return false;
	}
	
	/**
	 * 数据时间是否正确 
	 * 格式：HH:mm:ss
	 * @param args
	 * @throws Exception
	 */
	public static boolean isValidDataTime(String dataTime){
		dataTime = dataTime;
		String format =  "^(?:[01]\\d|2[0-3])(?::[0-5]\\d){2}$";
		if ((dataTime != null)) {
			Pattern pattern = Pattern.compile(format);
			Matcher match = pattern.matcher(dataTime);
			return match.matches();
		}
		return false;
	}

	/**
	 * 数据日期是否是最后一天
	 * @param str:数据日期
	 * @throws 
	 * @return
	 */
	public static  boolean isLastDayOfMonth(String str)  {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1));
		if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 比较 date1 是否 小于 date2
	 * @param date1  当前字段日期
	 * @param date2   要比较字段日期
	 * @return 成立返回 true 不成立返回false
	 */
	public static boolean DateCompareBefore(String date1 ,String date2){
		SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
		Calendar dateCal1 = Calendar.getInstance();
		Calendar dateCal2 = Calendar.getInstance();
		try {
			if(!date1.isEmpty() && DataDateCheck.isValidDataFormat(date1) &&
					!date2.isEmpty() && DataDateCheck.isValidDataFormat(date2)){
				dateCal1.setTime(SDF.parse(date1));
				dateCal2.setTime(SDF.parse(date2));
				if(dateCal1.compareTo(dateCal2) == -1){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	/**
	 * 比较 date1 是否 大于 date2
	 * @param date1   当前字段日期
	 * @param date2   要比较字段日期
	 * @return 成立返回 true 不成立返回false
	 */
	public static boolean DateCompareAfter(String date1 ,String date2){
		SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
		Calendar dateCal1 = Calendar.getInstance();
		Calendar dateCal2 = Calendar.getInstance();
		try {
			if(!date1.isEmpty() && DataDateCheck.isValidDataFormat(date1) &&
					!date2.isEmpty() && DataDateCheck.isValidDataFormat(date2)){
				dateCal1.setTime(SDF.parse(date1));
				dateCal2.setTime(SDF.parse(date2));
				if(dateCal1.compareTo(dateCal2) == 1){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 比较 date1 是否 等于 date2
	 * @param date1   当前字段日期
	 * @param date2   要比较字段日期
	 * @return 成立返回 true 不成立返回false
	 */
	public static boolean DateCompareEquals(String date1 ,String date2){
		SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
		Calendar dateCal1 = Calendar.getInstance();
		Calendar dateCal2 = Calendar.getInstance();
		try {
			if(!date1.isEmpty() && DataDateCheck.isValidDataFormat(date1) &&
					!date2.isEmpty() && DataDateCheck.isValidDataFormat(date2)){
				dateCal1.setTime(SDF.parse(date1));
				dateCal2.setTime(SDF.parse(date2));
				if(dateCal1.compareTo(dateCal2) == 0){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	public static void main(String[] args) throws ParseException{
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-M-dd");
		SimpleDateFormat fmt2 = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat fmt1 = new SimpleDateFormat("yyyyMMdd");
		System.out.println(fmt.format(fmt2.parse("2017/10/10")));
	}
	
}
