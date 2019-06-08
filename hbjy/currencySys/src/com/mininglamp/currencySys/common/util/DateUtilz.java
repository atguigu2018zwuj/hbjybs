package com.mininglamp.currencySys.common.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.util.Assert;

import com.mininglamp.logx.kit.StrKit;
import com.mininglamp.logx.util.DateUtil;


/**
 * 描述：Date 类实际上只是一个包裹类, 它包含的是一个长整型数据, 表示的是从GMT(格林尼治标准时间)
 * 1970年1月1日00:00:00这一刻之前或者是之后经历的毫秒数。
 * 
 * @author ZhangXiaolei(Raven)
 * @version 1.9
 * @since 2007-03-09
 */
@SuppressWarnings("unchecked")
public class DateUtilz {
    
    /** Number of milliseconds in a standard second. */
    public static final long MILLIS_PER_SECOND = 1000;
    
    /** Number of milliseconds in a standard minute. */
    public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    
    /** Number of milliseconds in a standard hour. */
    public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
    
    /** Number of milliseconds in a standard day. */
    public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;
    
    /**  默认日期格式yyyy-MM-dd */
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    
    /** 在组装时间时不改变的部分采用此值 */
    public static final int TIME_NO_CHANGE = -1;
    
    /** 表示时间为0 */
    public static final int TIME_IS_ZERO = 0;
	
    
    public static String getBeforeMonth(String format){
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DAY_OF_MONTH,-1);
    	cal.add(Calendar.MONTH, -1);
    	Date date = cal.getTime();
    	return DateFormatUtils.dateFormat(date,format);
    }
    public static String getYesterdayStr(String format){
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DAY_OF_MONTH,-1);
    	Date date = cal.getTime();
    	return DateFormatUtils.dateFormat(date,format);
    }
    //NOTE 获得当前时间
	/**
	 * 获得当前年份
	 * @return 当前年份
	 */
	public static int getCurrentYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}
	
	/**
	 * 获得某个时间的年份
	 * @param date 某个时间
	 * @return 某个时间的年份
	 */
	public static int getSomeYear(java.util.Date date) {
		Calendar cal = dateToCalendar(date);
		return cal.get(Calendar.YEAR);
	}
	
	/**
	 * 获得当前月份
	 * @return 当前月份
	 */
	public static int getCurrentMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH) + 1;
	}
	
	/**
	 * 获得某个时间的月份
	 * @param date 某个时间
	 * @return 某个时间的月份
	 */
	public static int getSomeMonth(java.util.Date date) {
		Calendar cal = dateToCalendar(date);
		return cal.get(Calendar.MONTH) + 1;
	}
	
	/**
	 * 获得当前DAY OF MONTH
	 * @return 当前DAY OF MONTH
	 */
	public static int getCurrentDay() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 获得某个时间的DAY OF MONTH
	 * @param date 某个时间
	 * @return 某个时间的DAY OF MONTH
	 */
	public static int getSomeDay(java.util.Date date) {
		Calendar cal = dateToCalendar(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 获得当前小时
	 * @return 当前小时
	 */
	public static int getCurrentHour() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * 获得某个时间的小时
	 * @param date 某个时间
	 * @return 某个时间的小时
	 */
	public static int getSomeHour(java.util.Date date) {
		Calendar cal = dateToCalendar(date);
		return cal.get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * 获得当前分钟
	 * @return 当前分钟
	 */
	public static int getCurrentMinute() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MINUTE);
	}
	
	/**
	 * 获得某个时间的分钟
	 * @param date 某个时间
	 * @return 某个时间的分钟
	 */
	public static int getSomeMinute(java.util.Date date) {
		Calendar cal = dateToCalendar(date);
		return cal.get(Calendar.MINUTE);
	}
	
	/**
	 * 获得当前秒
	 * @return 当前秒
	 */
	public static int getCurrentSecond() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.SECOND);
	}
	
	/**
	 * 获得某个时间的秒数
	 * @param date 某个时间
	 * @return 某个时间的秒数
	 */
	public static int getSomeSecond(java.util.Date date) {
		Calendar cal = dateToCalendar(date);
		return cal.get(Calendar.SECOND);
	}
	
	/**
	 * 获得当前毫秒
	 * @return 当前毫秒
	 */
	public static int getCurrentMillisecond() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MILLISECOND);
	}
	
	/**
	 * 获得某个时间的毫秒
	 * @param date 某个时间
	 * @return 某个时间的毫秒
	 */
	public static int getSomeMillisecond(java.util.Date date) {
		Calendar cal = dateToCalendar(date);
		return cal.get(Calendar.MILLISECOND);
	}
	
	/**
	 * 获得当前的日期
	 * @return 返回当前的日期
	 */
	public static java.util.Date getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}
	
	/**
	 * 获得昨天日期
	 * @return 返回昨天日期
	 */
	public static java.util.Date getYestodayDate() {
	    return new java.util.Date(System.currentTimeMillis() - MILLIS_PER_DAY);
	}
	
	/**
	 * 获得昨天日期
	 * @param date
	 * @return 返回昨天日期
	 */
	public static java.util.Date getYestodayDate(java.util.Date date) {
	    return new java.util.Date(date.getTime() - MILLIS_PER_DAY);
	}

	/**
	 * 获得明天日期
	 * @return 返回明天日期
	 */
	public static java.util.Date getTomorrowDate() {
		return new java.util.Date(System.currentTimeMillis() + MILLIS_PER_DAY);
	}
	
	/**
	 * 获得指定日期的明天的日期
	 * @param date
	 * @return 返回明天日期
	 */
	public static java.util.Date getTomorrowDate(java.util.Date date) {
		return new java.util.Date(date.getTime() + MILLIS_PER_DAY);
	}
	
	//NOTE 获得指定格式的时间
	
	/**
	 * 获得当前的日期(yyyy-MM-dd)
	 * @return 返回当前的日期
	 */
	public static String getCurrentDateAccurateToDay() {
	    return getCurrentDate(DEFAULT_DATE_PATTERN);
	}
	
	/**
	 * 获得当前的日期(yyyy-MM-dd EE)
	 * @return 返回当前的日期
	 */
	public static String getCurrentDateAccurateToWeek() {
		return getCurrentDate("yyyy-MM-dd EE");
	}
	
	/**
	 * 获得当前的时间(yyyy-MM-dd hh:mm:ss)
	 * @return 返回当前时间
	 */
	public static String getCurrentDateAccurateToSecond() {
	    return getCurrentDate("yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 获得当前的时间(yyyyMMddHHmmssSSS)
	 * @return 返回当前时间
	 */
	public static String getCurrentDateAccurateToMillisecond() {
	    return getCurrentDate("yyyy-MM-dd HH:mm:ss,SSS");
	}
	
	/**
	 * 获得明天日期(yyyy-MM-dd)
	 * @return 返回明天日期
	 */
	public static String getTomorrowStringDate() {
		return getTomorrowDate(DEFAULT_DATE_PATTERN);
	}
	
	/**
	 * 获得昨天日期(yyyy-MM-dd)
	 * @return 返回昨天日期
	 */
	public static String getYestodayStringDate() {
		return getYestodayDate(DEFAULT_DATE_PATTERN);
	}
	
	/**
	 * 按格式获得昨天日期
	 * 例如：getYestodayDate("yyyy-MM-dd");
	 * 
	 * @param pattern
	 * @return 返回昨天日期
	 */
	public static String getYestodayDate(String pattern) {
	    java.util.Date yestodayDate = new java.util.Date(System.currentTimeMillis() - MILLIS_PER_DAY);
	    return dateToString(yestodayDate, pattern);
	}
	
	/**
	 * 按格式获得明天日期
	 * 例如，getTomorrowDate("yyyy-MM-dd");
	 * 
	 * @param pattern
	 * @return 返回明天日期
	 */
	public static String getTomorrowDate(String pattern) {
		java.util.Date tomorrowDate = new java.util.Date(System.currentTimeMillis() + MILLIS_PER_DAY);
		return dateToString(tomorrowDate, pattern);
	}
	
	/**
	 * 返回指定格式的当天日期
	 * 例如：getCurrentDate("yyyy-MM-dd");
	 * 
	 * @param pattern 时间样式
	 * @return 返回指定格式的当天日期
	 */
	public static String getCurrentDate(String pattern) {
		return dateToString(new java.util.Date(), pattern);
	}
	
	//NOTE 转换日期格式
	
	/**
	 * 转换日期格式
	 * @param date
	 * @param oldPattern
	 * @param newPattern
	 * @return 转换格式后的日期
	 */
	public static String getDateByFormat(String date, String oldPattern, String newPattern) {
	    Assert.notNull(date, "指定日期为null");
	    java.util.Date newDate = stringToDate(date, oldPattern);
        return dateToString(newDate, newPattern);
	}
	
	/**
	 * Date转换为String
	 * @param date
	 * @param pattern
	 * @return 字符串格式日期
	 */
	public static String dateToString(java.util.Date date, String pattern) {
	    Assert.notNull(date, "指定日期为null");
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);  
        return sdf.format(date);
	}
	
	/**
	 * String转换为Date
	 * 
	 * 说明：SimpleDateFormat是DateFormat的子类。
	 * 在DateFormat中，也可以调用parse方法来解析日期型的数据，不过不推荐这样做。
	 * 建议还是采用SimpleDateFormat类中parse(Strng source, ParsePosition positon)来解析。
	 * 具体原因，因为DateFormat中的parse方法有bug且不支持定位解析。
	 * 
	 * @param date
	 * @return 分析成功则返回分析得到的日期，失败则返回null
	 */
	public static java.util.Date stringToDate(String date, String pattern) {
	    return stringToDate(date, pattern, Locale.CHINA);
	}
	
	/**
	 * String转换为Date
	 * 
	 * @param date
	 * @param pattern
	 * @param locale
	 * @return 分析成功则返回分析得到的日期，失败则返回null
	 */
	public static java.util.Date stringToDate(String date, String pattern, Locale locale) {
		SimpleDateFormat formate = new SimpleDateFormat(pattern, locale);
	    java.util.Date newDate = formate.parse(date, new ParsePosition(0));
	    Assert.notNull(newDate, "日期可能与所给格式不匹配");
	    return newDate;
	}
	
	/**
	 * String转换为Calendar
	 * @param date
	 * @param pattern
	 * @return Calendar
	 */
	public static Calendar stringToCalendar(String date, String pattern) {
		Assert.notNull(date, "指定日期为null");
		java.util.Date newDate = stringToDate(date, pattern);
	    return dateToCalendar(newDate);
	}
	
	/**
	 * Calendar转Date
	 * @param cal
	 * @return Date
	 */
	public static java.util.Date calendarToDate(Calendar cal) {
		return cal.getTime();
	}
	
	/**
	 * Date转Calendar
	 * @param date
	 * @return Calendar
	 */
	public static Calendar dateToCalendar(java.util.Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	
	/**
	 * java.util.Date转java.sql.Date
	 * @param date
	 * @return java.sql.Date
	 */
	public static java.sql.Date utilDateToSqlDate(java.util.Date date) {
		return new java.sql.Date(date.getTime());
	}
	
	/**
	 * java.sql.Date转java.util.Date
	 * @param date
	 * @return java.util.Date
	 */
	public static java.util.Date sqlDateToUtilDate(java.sql.Date date) {
		return new java.util.Date(date.getTime());
	}
	
	//NOTE 获得特定的日期
	
	/**
	 * 获得某一日期的yyyy-MM-dd 00:00:00形式
	 * @param date 日期
	 * @return Date
	 */
	public static java.util.Date getSomeDateAccurateToMinHour(java.util.Date date) {
		 Map map = new  HashMap();
		 map.put(new Integer(Calendar.HOUR_OF_DAY), new Integer(TIME_IS_ZERO));
		 map.put(new Integer(Calendar.MINUTE), new Integer(TIME_IS_ZERO));
		 map.put(new Integer(Calendar.SECOND), new Integer(TIME_IS_ZERO));
		 return getSomeDate(date, map);
	}

	
	/**
	 * 获得某一日期的yyyy-MM-dd 23:59:59形式
	 * @param date 日期
	 * @return Date
	 */
	public static java.util.Date getSomeDateAccurateToMaxHour(java.util.Date date) {
		 Map map = new  HashMap();
		 map.put(new Integer(Calendar.HOUR_OF_DAY), new Integer(23));
		 map.put(new Integer(Calendar.MINUTE), new Integer(59));
		 map.put(new Integer(Calendar.SECOND), new Integer(59));
		 return getSomeDate(date, map);
	}
	
	/**
	 * 获得某一日期的yyyy-MM-01 00:00:00形式
	 * @param date 日期
	 * @return Date
	 */
	public static java.util.Date getSomeDateAccurateToMinMonth(java.util.Date date) {
		 Map map = new  HashMap();
		 map.put(new Integer(Calendar.DAY_OF_MONTH), new Integer(1));
		 map.put(new Integer(Calendar.HOUR_OF_DAY), new Integer(TIME_IS_ZERO));
		 map.put(new Integer(Calendar.MINUTE), new Integer(TIME_IS_ZERO));
		 map.put(new Integer(Calendar.SECOND), new Integer(TIME_IS_ZERO));
		 return getSomeDate(date, map);
	}
	
	/**
	 * 获得某一日期的yyyy-MM-dd(dd为此月最后一天) 23:59:59形式
	 * @param date 日期
	 * @return Date
	 */
	public static java.util.Date getSomeDateAccurateToMaxMonth(java.util.Date date) {
		 Map map = new  HashMap();
		 map.put(new Integer(Calendar.DAY_OF_MONTH), new Integer(getDaysOfMonth(date)));
		 map.put(new Integer(Calendar.HOUR_OF_DAY), new Integer(23));
		 map.put(new Integer(Calendar.MINUTE), new Integer(59));
		 map.put(new Integer(Calendar.SECOND), new Integer(59));
		 return getSomeDate(date, map);
	}
	
	/**
	 * 根据日期组装新的日期
	 * @param date
	 * @param calendar
	 * @return Date
	 */
	public static java.util.Date getSomeDate(java.util.Date date, Map calendar) {
		Calendar cal = dateToCalendar(date);

		Iterator it = calendar.entrySet().iterator();     
		while(it.hasNext()) {     
		    Entry entry = (Entry)it.next();     
		    Integer key = (Integer)entry.getKey();     
		    Integer value = (Integer)entry.getValue();   
		    if(key.intValue() == Calendar.MONTH) {
		    	cal.set(key.intValue(), value.intValue() - 1);
		    }
		    else {
		    	cal.set(key.intValue(), value.intValue());
		    }
		}
		return calendarToDate(cal);
	}
	
	/**
	 * 计算某年第几天的日期
	 * @param year 年份
	 * @param dayOfYear 一年中的第几天
	 * @return 某年第几天的日期
	 */
	public static String getDateWithDayOfYear(int year, int dayOfYear) {
	    SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
	    Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.YEAR, year);
	    cal.set(Calendar.DAY_OF_YEAR, dayOfYear);
	    return df.format(cal.getTime());
	}
	
	/**
	 * 获得当前星期中某一天的日期
	 * @param dayOfWeek 当前星期中的某一天
	 * @return 当前星期中某一天的日期
	 */
	public static String getDateWithDayOfCurrentWeek(int dayOfWeek) {
		checkWeek(dayOfWeek);
		
		SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
	    Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
	    if(dayOfWeek == Calendar.SUNDAY) {
	    	cal.add(Calendar.DATE, 7);
	    }
	    return df.format(cal.getTime());
	}
	
	//NOTE 时期计算
	/**
	 * 获得某个月的天数
	 * @param date 日期
	 * @return 某个月的天数
	 */
	public static int getDaysOfMonth(java.util.Date date) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.YEAR, getSomeYear(date));
		cal.set(Calendar.MONTH, getSomeMonth(date) - 1);
		
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 返回某年的天数
	 * @param year
	 * @return 某年的天数
	 */
	public static int getDaysOfYear(int year) {
	    return isLeapYear(year) ? 366 : 365 ;
	}
	
	/**
	 * 日期计算，当前日期加或减掉相应的小时数
	 * @param date 日期
	 * @param hour 小时数
	 * @return 返回计算后的日期 
	 */
	public static java.util.Date calculateWithHour(java.util.Date date, int hour) { 
	    Calendar cal = dateToCalendar(date);
	    cal.set(Calendar.HOUR_OF_DAY, hour);
	    return cal.getTime();  
	}
	
	/**  
     *  日期计算，当前日期加或减掉相应的天数   
     *  @param date 日期  
     *  @param day 天数  
     *  @return 返回计算后的日期  
     */  
	public static java.util.Date calculateWithDay(java.util.Date date, int day) { 
		Calendar cal = dateToCalendar(date);
	    cal.setTimeInMillis(cal.getTimeInMillis() + day * MILLIS_PER_DAY);
	    return cal.getTime();  
	}
	
	/**
	 * 日期计算，当前日期加或减掉相应的月  
	 * @param date 日期
	 * @param month 月份数
	 * @return 返回计算后的日期 
	 */
	public static java.util.Date calculateWithMonth(java.util.Date date, int month) {    
		Calendar cal = dateToCalendar(date);
	    cal.add(Calendar.MONTH, month);
	    return cal.getTime();  
	}
	
	/**
	 * 日期计算，当前日期加或减掉相应的年数  
	 * @param date 日期
	 * @param year 年数
	 * @return 返回计算后的日期 
	 */
	public static java.util.Date calculateWithYear(java.util.Date date, int year) {    
		Calendar cal = dateToCalendar(date);
	    cal.set(Calendar.YEAR, year);
	    return cal.getTime();  
	}
	
	/**
	 * 日期计算，当前日期加或减掉相应的小时数 
	 * @param date 日期
	 * @param hour 小时数
	 * @return 返回计算后的日期 
	 */
	public static String calculateWithHour(String date, int hour) {
		java.util.Date temp = calculateWithHour(stringToDate(date, DEFAULT_DATE_PATTERN), hour);
		return dateToString(temp, DEFAULT_DATE_PATTERN);
	}
	
	/**
	 * 日期计算，当前日期加或减掉相应的天数  
	 * @param date 日期
	 * @param day 天数
	 * @return 返回计算后的日期 
	 */
	public static String calculateWithDay(String date, int day) {
		java.util.Date temp = calculateWithDay(stringToDate(date, DEFAULT_DATE_PATTERN), day);
		return dateToString(temp, DEFAULT_DATE_PATTERN);
	}
	
	/**
	 * 日期计算，当前日期加或减掉相应的月数  
	 * @param date 日期
	 * @param month 月数
	 * @return 返回计算后的日期 
	 */
	public static String calculateWithMonth(String date, int month) {
		java.util.Date temp = calculateWithMonth(stringToDate(date, DEFAULT_DATE_PATTERN), month);
		return dateToString(temp, DEFAULT_DATE_PATTERN);
	}
	
	/**
	 * 日期计算，当前日期加或减掉相应的年数  
	 * @param date 日期
	 * @param year 年数
	 * @return 返回计算后的日期 
	 */
	public static String calculateWithYear(String date, int year) {
		java.util.Date temp = calculateWithYear(stringToDate(date, DEFAULT_DATE_PATTERN), year);
		return dateToString(temp, DEFAULT_DATE_PATTERN);
	}
	
	/**
	 * 计算两天之间的天数，日期格式为yyyy-MM-dd
	 * Calculates the number of days between two calendar days in a manner
	 * which is independent of the Calendar type used.
	 *
	 * @param date1    The first date.
	 * @param date2    The second date.
	 *
	 * @return      The number of days between the two dates.  Zero is
	 *              returned if the dates are the same, one if the dates are
	 *              adjacent, etc.  The order of the dates
	 *              does not matter, the value returned is always >= 0.
	 *              If Calendar types of d1 and d2
	 *              are different, the result may not be accurate.
	 */
	public static int getBetweenDays(java.util.Date date1, java.util.Date date2) {
		
		Calendar d1 = dateToCalendar(date1);
		Calendar d2 = dateToCalendar(date2);
	    if(d1.after(d2)) {  // swap dates so that d1 is start and d2 is end
	        Calendar swap = d1;
	        d1 = d2;
	        d2 = swap;
	    }
	    int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
	    int y2 = d2.get(Calendar.YEAR);
	    if(d1.get(Calendar.YEAR) != y2) {
	        d1 = (Calendar) d1.clone();
	        do {
	            days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
	            d1.add(Calendar.YEAR, 1);
	        } while (d1.get(Calendar.YEAR) != y2);
	    }
	    return days;
	}
	
	
	/**
	 * 计算两天之间的天数，日期格式为yyyy-MM-dd
	 * Calculates the number of days between two calendar days in a manner
	 * which is independent of the Calendar type used.
	 *
	 * @param date1    The first date.
	 * @param date2    The second date.
	 *
	 * @return      The number of days between the two dates.  Zero is
	 *              returned if the dates are the same, one if the dates are
	 *              adjacent, etc.  The order of the dates
	 *              does not matter, the value returned is always >= 0.
	 *              If Calendar types of d1 and d2
	 *              are different, the result may not be accurate.
	 */
	public static int getBetweenDaysByNegative(java.util.Date date1, java.util.Date date2) {
		
		Calendar d1 = dateToCalendar(date1);
		Calendar d2 = dateToCalendar(date2);
	    if(d1.after(d2)) {  // swap dates so that d1 is start and d2 is end
	        Calendar swap = d1;
	        d1 = d2;
	        d2 = swap;
	        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
		    int y2 = d2.get(Calendar.YEAR);
		    if(d1.get(Calendar.YEAR) != y2) {
		        d1 = (Calendar) d1.clone();
		        do {
		            days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
		            d1.add(Calendar.YEAR, 1);
		        } while (d1.get(Calendar.YEAR) != y2);
		    }
		    return -days;
	    }else{
	    	int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
		    int y2 = d2.get(Calendar.YEAR);
		    if(d1.get(Calendar.YEAR) != y2) {
		        d1 = (Calendar) d1.clone();
		        do {
		            days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
		            d1.add(Calendar.YEAR, 1);
		        } while (d1.get(Calendar.YEAR) != y2);
		    }
		    return days;
	    }
	}
	
	/**
	 * 计算两个时间之间的分钟数，小时格式为 HH:mm
	 * @param hour1
	 * @param hour2
	 * @return 两时间分钟之差
	 */
	public static int getBetweenMinute(java.util.Date date1, java.util.Date date2) {
		return (int)(getBetweenMillis(date1, date2) / MILLIS_PER_MINUTE);
	}
	
	/**
	 * 计算两个时间之间的分钟数，小时格式为 HH:mm
	 * @param hour1
	 * @param hour2
	 * @return 两时间分钟之差
	 */
	public static int getBetweenSecond(java.util.Date date1, java.util.Date date2) {
		return (int)(getBetweenMillis(date1, date2) / MILLIS_PER_SECOND);
	}
	
	/**
	 * 计算两个小时之间的毫秒数
	 * @param date1
	 * @param date2
	 * @return 两时间毫秒之差
	 */
	public static long getBetweenMillis(java.util.Date date1, java.util.Date date2) {
		Calendar d1 = dateToCalendar(date1);
		Calendar d2 = dateToCalendar(date2);
	    if(d1.after(d2)) {  // swap dates so that d1 is start and d2 is end
	        Calendar swap = d1;
	        d1 = d2;
	        d2 = swap;
	    }
	    return d2.getTimeInMillis() - d1.getTimeInMillis();
	}

	//NOTE 判断日期
	/**
	 * 判断是否为闰年
	 * @param year
	 * @return true是闰年，false不是闰年
	 */
	public static boolean isLeapYear(int year) {
	    
	    boolean isLeapYear = false;
	    int gregorianCutoverYear = 1582;//1582 年定的 Gregorian Calendar 才再增加百年不潤四百年潤的規則
	    if( year >= gregorianCutoverYear ) {
	        if((year%4 == 0) && ((year%100 != 0) || (year%400 == 0))) {
	            isLeapYear = true;
	        }
	    }
	    else{
	        if(year%4 == 0) {
	            isLeapYear = true;
	        }
	    }    
	    return isLeapYear;
	}
	
	/**
	 * 检查年份是否合法
	 * @param year 年份
	 */
	public static void checkYear(int year) {
		Assert.isTrue( (year>10000 || year<1000), "您输入的年份越界，必须在1000~9999之间" );
	}
	
	/**
	 * 检测月份是否合法
	 * @param month 月份
	 */
	public static void checkMonth(int month) {
		Assert.isTrue( (month<1 || month>12), "您输入的月份不合法，必须在1~12之间" );
	}
	
	/**
	 * 检测星期是否合法
	 * @param week 月份
	 */
	public static void checkWeek(int week) {
		Assert.isTrue( (week<1 || week>7), "您输入的星期不合法，必须在1~7之间" );
	}
	
	/**
	 * 检查日子是否合法
	 * @param date 日期
	 */
	public static void checkDayOfMonth(java.util.Date date) {
		int day = getSomeDay(date);
		Assert.isTrue( (day<1), "您输入的日期不合法，日必须大于零" );
		Assert.isTrue( (day>getDaysOfMonth(date)), "您输入的日期不合法，日必须小于当月最大天数" );
	}
	
	/**
	 * 检查日子是否合法
	 * @param year 日期
	 * @param dayOfYear 一年中的第几天
	 */
	public static void checkDayOfYear(int year, int dayOfYear) {
		Assert.isTrue( (dayOfYear<1), "您输入的日期不合法，日必须大于零" );
		Assert.isTrue( (dayOfYear>getDaysOfYear(year)), "您输入的日期不合法，日必须小于当年最大天数" );
	}
	
	/**
	 * 比较时间的新旧
	 * @param oldDate 旧时间
	 * @param newDate 新时间
	 * @param pattern 时间样式
	 * @return true 如果newDate比oldDate新
	 */
	public static boolean compareBothDate(String oldDate, String newDate, String pattern) {
		Calendar c1 = stringToCalendar(oldDate, pattern);
		Calendar c2 = stringToCalendar(newDate, pattern);
		return (c2.getTimeInMillis() - c1.getTimeInMillis()) >=0;
	}
	
	/**
	 * 获得当前时间是一周的第几天
	 * @param date
	 * @return 周一到周日分别是 1-7
	 */
	public static int getDayOfweek(){
		int week=0;
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        week=calendar.get(Calendar.DAY_OF_WEEK);
        week = week - 1;
        if(week==0){
        	week = 7;
        }
        return week;
	}
	
	/**
	 * 获得当前的时间(yyyyMMdd)
	 * @return 返回当前时间
	 */
	public static String getLittleDate() {
	    return getCurrentDate("yyyyMMdd");
	}
	
	/**
	 * 获得当前的时间(yyyyMMddHHmmss)
	 * @return 返回当前时间
	 */
	public static String getToSecond() {
	    return getCurrentDate("yyyyMMddHHmmss");
	}
	//NOTE 换算时间
	
	/**
	 * 根据毫秒数换算成天、时、分、秒
	 * @param mss   毫秒
	 * @param model 格式 1:x天x小时  0:x-x:x:x
	 * @return 时间
	 */
	public static String getDay(Long mss,int model){
		String day = String.valueOf(mss / (1000 * 60 * 60 * 24));
		String hour = String.valueOf((mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
		String minutes = String.valueOf((mss % (1000 * 60 * 60)) / (1000 * 60));
		String seconds = String.valueOf((mss % (1000 * 60)) / 1000);
		if(model==1){
			return day+"天 "+hour+"小时";
		}else{
			return day+" "+hour+":"+minutes+":"+seconds;
		}
		
	}
	
	/**
	 * 返回指定日志、格式的上月月末日期
	 * @author Raven(ZhangXiaolei)
	 * @date 2016-8-19
	 * @describe 
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static String getLastMonthEnd(String dateStr,String pattern){
		Date date = DateUtilz.stringToDate(dateStr, pattern);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Integer year = cal.get(Calendar.YEAR);
		Integer month = cal.get(Calendar.MONTH)+1;
		String monthBegin = "";
		if(month>=10){
			 monthBegin = year+""+month+"01";
		}else{
			 monthBegin = year+"0"+month+"01";
		}
		return dateToString(DateUtilz.getYestodayDate(stringToDate(monthBegin, pattern)), pattern);
	}
	
	public static String FormatDate10(Date newDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(newDate);
		return date;
	}
	
	public static String compareDateGetMin(String date1,String date2,String date3,String pattern){
		List<Date> list = new ArrayList<Date>();
		list.add(DateUtilz.stringToDate(date1, pattern));
		list.add(DateUtilz.stringToDate(date2, pattern));
		list.add(DateUtilz.stringToDate(date3, pattern));
		
		Date minDate = null;
		
		for (int i = 0; i < list.size(); i++) {
			if(minDate== null){
				minDate = list.get(i);
			}else{
				if(minDate.getTime() > list.get(i).getTime()){
					minDate = list.get(i);
				}
			}
		}
		return DateUtilz.dateToString(minDate, pattern);
	}
	
	/**
	 * 获得某个时间的月份
	 * @param date 某个时间
	 * @return 某个时间的月份
	 */
	public static String getBeforeMonth(String date,String pattern) {
		if(StrKit.isBlank(date)){
			date = DateUtil.dateToString(new Date(),pattern);
		}
		if(StrKit.isBlank(pattern)){
			throw new RuntimeException("时间格式不能为null");
		}
		Calendar cal = dateToCalendar(stringToDate(date, pattern));
		cal.add(Calendar.MONTH,-1);
		return dateToString(cal.getTime(), pattern);
	}
	
	/**
	 * 测试用
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(getBeforeMonth("20161217","yyyyMMdd"));
		
		
	}
}
