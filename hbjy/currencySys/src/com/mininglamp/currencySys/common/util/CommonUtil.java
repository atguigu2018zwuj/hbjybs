package com.mininglamp.currencySys.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.mq.jmqi.JmqiException;
import com.mininglamp.currencySys.customReportAdditional.service.CustomReportAdditionalService;
import com.mininglamp.currencySys.jglszcxx.service.JglszcxxService;
import com.mininglamp.currencySys.user.service.UserService;
import com.sun.xml.internal.messaging.saaj.util.Base64;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import spc.webos.endpoint.ESB2;
import spc.webos.endpoint.Executable;


/**
 * 
 * @author falcon
 * 新增通用类工具方法
 * 	add by cxk 2015年3月10日10:34:51
 */

public class CommonUtil {
	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
	private static java.util.logging.Logger logger1 = java.util.logging.Logger.getAnonymousLogger();
	private static long LAST_TIME = System.currentTimeMillis();
	
	/** jdbc.properties配置文件对应的输入流 */ 
	public static final InputStream JDBC_PROPERTIES_IN_STREAM = CommonUtil.class.getClassLoader().getResourceAsStream("ibatis/jdbc.properties");
	public static Properties jdbcProperties = null;
	/** esbApi.properties配置文件对应的输入流 */
	public static final InputStream ESBAPI_PROPERTIES_IN_STREAM = CommonUtil.class.getClassLoader().getResourceAsStream("esbApi/esbApi.properties");
	public static Properties esbApiProperties = null;
    
	/**
     * 随机获取UUID字符串(无中划线,32位)
     * 
     * @return UUID字符串
     */
    public static String getUUID()
    {
        String uuid = UUID.randomUUID().toString();
        return (uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23)
                + uuid.substring(24)).toUpperCase();
    }
    /**
     * 获取系统当前时间
     * add by cxk 2015年3月10日14:57:11
     * @return
     */
    public static String getLocalSysTime(){
		Date now = new Date(); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String timeString = dateFormat.format( now ); 
		return timeString;
	}
	
	
    /**
     * 在0~m之间生成随机数(包括m)
     * 
     */
    public static int getRandomInt(int m)
    {
        if(m < 0)
            return 0;
        float d = (float)Math.random() * m;
        return Math.round(d);
    }
	
    /**
     * 将数组转换为regex分割的字符串
     * @param st 要转化的数组
     * @param regex 分割符
     * @return String
     */
    public static String arrayToString(String[] st, String regex)
    {
        if (st == null)
        {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < st.length; i++)
        {
            if (sb.length() == 0)
            {
                sb.append(st[i]);
            }
            else
            {
                sb.append(regex).append(st[i]);
            }
        }
        return sb.toString();
    }
    
    /**
     * 按字节截取固定长度的字符串
     * 
     * @param s 字符串
     * @param len 长度
     */
    public String interceptStr(String s, int len)
    {
        try
        {
            byte[] b = s.getBytes("GBK");
            if (b.length > len)
            {
                if (b[len] < 0)
                {
                    s = new String(b, 0, --len, "GBK");
                }
                else
                {
                    s = new String(b, 0, len, "GBK");
                }
            }
            
        }
        catch (Exception e)
        {	
//        	System.out.println("String to GBK Error ");
            //logger.error("String to GBK Error ", e);
			throw new RuntimeException(e);
        }
        return s;
    }
    
    /**
     * 求绝对值
     * 
     * @param a int值
     * @return a的绝对值
     */
    public String abs(int a)
    {
        int b = (a < 0) ? -a : a;
        return Integer.toString(b);
    }
    
    /**
     * 将返回的分转换为元
     * 
     * @param 名称 : 含义
     * @return String
     * @exception
     */
    public static String centToYuan(String money)
    {
        if (money == null || "".equals(money))
        {
            return "0.00";
        }
        Pattern p = Pattern.compile("\\D*");
        Matcher m = p.matcher(money);
        boolean b = m.matches();
        
        if (b)
        {
            return money;
        }
        String symbol = "";
        // 负数
        if (money.indexOf("-") == 0)
        {
            symbol = "-";
            money = money.substring(1);
        }
        String yuanStr;
        if (money.indexOf(".") != -1)
        {
            yuanStr = money;
        }
        else if (money.length() == 2)
        {
            yuanStr = "0." + money;
        }
        else if (money.length() == 1)
        {
            yuanStr = "0.0" + money;
        }
        else
        {
            String beforDot;
            String atferDot;
            beforDot = money.substring(0, money.length() - 2);
            atferDot = money.substring(money.length() - 2);
            yuanStr = beforDot + "." + atferDot;
        }
        return symbol + yuanStr;
    }
    
    /**
     * 将返回的元转换为格式为0.00
     * 
     * @param 名称 : 含义
     * @return String
     */
    public String yuanToYuan(String money)
    {
        String yuanStr;
        if (money == null || "".equals(money))
        {
            return "&nbsp;";
        }
        else
        {
            yuanStr = money + ".00";
        }
        return yuanStr;
    }
    
    /**
     * 将数值转换为格式为规定有效小数位的小数 2011-07-18 chenyunhua
     * 
     * 比如 入参为 2.3 和 2 则返回值为 2.30
     * 
     * @param oldValue :要转换的数值
     * 
     * @param floatLenth :要保留的有效小数位
     * 
     * @return String 保留两位邮箱小数后的数值
     */
    public static String TransToFloat(String oldValue, int floatLenth)
    {
        if (oldValue == null || "".equals(oldValue))
        {
            oldValue = "0.00";
        }
        String newValue = String.valueOf((new BigDecimal(oldValue)).setScale(floatLenth, BigDecimal.ROUND_HALF_UP));
        return newValue;
    }
    
    /**
     * 计算字符长度 中文字符2，英文 1
     * 
     * @param chr
     * @return
     * @see [类、类#方法、类#成员]
     */
    private static int byteLength(char chr)
    {
        if (Integer.toHexString(chr).length() == 4)
        {
            return 2;
        }
        else
        {
            return 1;
        }
    }
    
    /**
     * 按指定长度，省略字符串部分字符
     * 
     * @param String 字符串
     * @param length 保留字符串长度
     * @return 省略后的字符串
     */
    public static String subString(String string, int length)
    {
        return subString(string, length, "...");
    }
    
    /**
     * 按指定长度，省略字符串部分字符
     * 
     * @param String 被处理字符串
     * @param length 保留字符串长度
     * @param more 后缀字符串
     * @return 省略后的字符串
     */
    public static String subString(String string, int length, String more)
    {
        if (string.getBytes().length < length)
        {
            return string;
        }
        StringBuffer sb = new StringBuffer();
        int moreLen = more.length();
        int count = 0;
        char[] charArray = string.toCharArray();
        for (int i = 0; i < charArray.length; i++)
        {
            char tempChar = charArray[i];
            count += byteLength(tempChar);
            
            if (count < length - moreLen)
            {
                sb.append(tempChar);
            }
            else if (count == length - moreLen)
            {
                sb.append(tempChar);
                break;
            }
            else if (count > length - moreLen)
            {
                break;
            }
        }
        sb.append(more);
        return sb.toString();
    }
    
    /**
     * 功能: 将金额值(String)转换成long,以分为单位
     * 
     * @param sMoney long 金额值(以元为单位)
     * @return long(以分为单位)
     */
    public static long moneyToLong(String sMoney)
    {
        long DMoney = Math.round(Double.parseDouble(sMoney) * 100);
        return DMoney;
    }
    
    /**
     * 功能：把以分为单位的字符串金额信息转换为以元为单位的金额信息（）
     * 
     * @param money 以分为单位的金额信息
     * @return 以元为单位的金额信息
     * @see [类、类#方法、类#成员]
     */
    public static String moneyString(String money)
    {
        String tempAmount = Integer.parseInt(money) + "";
        int length = tempAmount.length();
        if (tempAmount.length() > 2)
        {
            tempAmount = tempAmount.substring(0, length - 2) + "." + tempAmount.substring(length - 2);
        }
        else
        {
            if (tempAmount.length() == 2)
            {
                tempAmount = "0." + tempAmount;
            }
            else
            {
                tempAmount = "0.0" + tempAmount;
            }
        }
        return tempAmount;
    }
    
    /**
     * 功能：把字符串类型的金额信息（以分为单位）转换为double类型的金额信息（以元为单位）
     * 
     * @param money 字符串类型的金额信息（以分为单位）
     * @return double类型的金额信息（以元为单位）
     * @see [类、类#方法、类#成员]
     */
    public static double moneyStringToDouble(String money)
    {
        return Double.valueOf(moneyString(money));
    }
    /**
     * 转换为日期格式 yyyy-MM-dd
     * add by zqb 
     * 2015年4月9日14:34:58
     * @param newDate
     * @return
     */
    public static String FormatDate10(Date newDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(newDate);
		return date;
	}
    /**
     * 转换为日期格式yyyy-MM-dd hh42:mi:ss
     * add by zqb 
     * 2015年4月9日14:35:21
     * @param newDate
     * @return
     */
    public static String FormatDate19(Date newDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh42:mi:ss");
		String date = dateFormat.format(newDate);
		return date;
	}
    /**
     * 日期转换为String  yyyyMMdd
     * add by zqb 
     * 2015年4月9日14:35:21
     * @param newDate
     * @return
     * @throws ParseException 
     */
    public static String DateToString10(String newDate) throws ParseException{
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
    	Date tmpDate=dateFormat.parse(newDate);
    	SimpleDateFormat dateFormatNew = new SimpleDateFormat("yyyymmdd");
		String date = dateFormatNew.format(tmpDate);
		return date;
    }
    
    /**
     * 在原来的日期基础上增加天数
     */
    public static Date addDays(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + day);
		return c.getTime();
	}
    
    /**
     * 在原来的日期基础上增加天数
     */
    public static String addDays(String date, int day) {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			Date d = sdf.parse(date);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + day);
			return sdf.format(c.getTime());
		} catch (ParseException e) {
			return null;
		}
	}
    /**
	 * 转换数据库中下划线分割字段名为驼峰命名格式
	 * @param orignal 原始字段
	 * @return 驼峰命名字段
	 */
	public static String getBeanProName(String orignal){
		if(orignal==null||"".equals(orignal.trim())){
			return "";
		}
		int len = orignal.length();
		StringBuilder sb = new StringBuilder(len);
		for(int i=0;i<len;i++){
			char c = orignal.charAt(i);
			if(c=='_'){
				if(++i<len){
					sb.append(Character.toUpperCase(orignal.charAt(i)));
				}
			}else{
				sb.append(Character.toLowerCase(c));
			}
		}
		return sb.toString();
	}
	/**
	 * 将从数据库获取的_分割字段名改为驼峰命名
	 * @param dbMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> convertDbPro(Map<String,Object> dbMap){
		if(dbMap == null||dbMap.isEmpty()) return dbMap;
		
		Map<String,Object> mapNew = new HashMap<String,Object>();
		Set<String> keys = dbMap.keySet();
		for (String key : keys) {
			Object value = dbMap.get(key);
			if(value instanceof Map){
				value = convertDbPro((Map<String, Object>) value);
			}
			String _key = getBeanProName(key);
			mapNew.put(_key, value);
		}
		return mapNew;
	}
	/**
	 * 验证是否为空
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isEmpty(Object obj){
		if(obj == null) return true;
		
		if(obj instanceof Object[]){
			Object[] arr = (Object[]) obj;
			return arr.length == 0;
		}
		if(obj instanceof List){
			List list = (List) obj;
			return list.isEmpty();
		}
		if(obj instanceof Map){
			Map map = (Map) obj;
			return map.isEmpty();
		}
		return false;
	}
	
	/**
	 * 发送短信给指定系统用户
	 * @param username 系统用户的用户名
	 * @param msg 短信内容
	 * @param tlrNo 操作柜员号 
	 * @return 短信是否发送成功
	 */
	public static boolean sendMobileMessage(String username, String msg, String tlrNo){
		String txBranch = "";
		String phoneNumber = "";
		String jrjgbm = "";
		
		UserService userService = SpringContextUtils.getBean(UserService.class);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("username", username);
		List<Map> userJrjgxxList = userService.getDataList(param, "getUserJrjgxxByUsername");
		if (userJrjgxxList != null && !userJrjgxxList.isEmpty()) {
			// 电话号码
			phoneNumber = StringUtils.parseString(userJrjgxxList.get(0).get("teleph"));
			String jgjb = StringUtils.parseString(userJrjgxxList.get(0).get("jgjb"));
			// 是否发送短信（1短信发送，0不发送 ）
			String smsnotice = StringUtils.parseString(userJrjgxxList.get(0).get("smsnotice"));
			if (!"1".equals(smsnotice)) {
				// 不发送短信
				logger.warn("指定的用户【"+username+"】设置的发送短信设置为不发送短信！");
				return false;
			}
			
			// 获取发卡机构号
			if ("4".equals(jgjb) || "5".equals(jgjb)) {
				// 此时不发送短信
				logger.error("指定的用户【"+username+"】所在机构不是省联社、市办或联社，不可发送短信！");
				return false;
			} else if ("3".equals(jgjb)) {
				// 只有联社可出钱发送短信
				txBranch = StringUtils.parseString(userJrjgxxList.get(0).get("nbjgh"));
			} else if ("1".equals(jgjb) || "2".equals(jgjb)) {
				// 此时需所属机构的下级联社（jgjb:3）轮流出钱发送短信，即轮流使用下级联社的内部机构号
				JglszcxxService jglszcxxService = SpringContextUtils.getBean(JglszcxxService.class);
				// 查找下级联社中从未出钱发过短信的
				param.put("nbjgh", userJrjgxxList.get(0).get("nbjgh"));
				param.put("type", "dx");
				param.put("never", "true");
				Map neverSendMsgJg = jglszcxxService.getDataOne(param, "getThisTurnJgOfZcByNbjgh");
				if (neverSendMsgJg != null) {
					txBranch = StringUtils.parseString(neverSendMsgJg.get("id"));
					jrjgbm = StringUtils.parseString(neverSendMsgJg.get("jrjgbm"));
				} else {
					// 曾经都出钱发过短信，则查找最早发送短信的机构
					param.put("never", "false");
					param.put("orderFieldJzc", "dxzcsj");
					Map sendMsgJg = jglszcxxService.getDataOne(param, "getThisTurnJgOfZcByNbjgh");
					if (sendMsgJg != null) {
						txBranch = StringUtils.parseString(sendMsgJg.get("id"));
						jrjgbm = StringUtils.parseString(sendMsgJg.get("jrjgbm"));
					} else {
						logger.error("指定的用户【"+username+"】所在机构下没有联社，无法发送短信！");
						return false;
					}
				}
				
				boolean flag = sendMobileMessage(phoneNumber, msg, tlrNo, null, txBranch);
				if (flag) {
					// 发送成功后，更新机构的最后一次出钱发短信的时间
					param.put("nbjgh", txBranch);
					if (jglszcxxService.updateData(param, "updateJglszcxxTimeToNowByNbjgh") <= 0) {
						// 更新失败，插入数据
						param.put("jrjgbm", jrjgbm);
						param.put("type", "dx");
						jglszcxxService.insertData(param, "insertJglszcxx");
					}
				} else {
					logger.warn("发送短信给用户【"+username+"】失败！");
				}
				return flag;
			}
		} else {
			logger.error("未找到指定用户【"+username+"】的所属机构，无法发送短信！");
			return false;
		}
		
		return sendMobileMessage(phoneNumber, msg, tlrNo, null, txBranch);
	}
	
	/**
	 * 发送短信
	 * @param phoneNumber 手机号
	 * @param msg 短信内容
	 * @param tlrNo 操作柜员号（可为null，此时默认取session中包含的当前登录用户的柜员号） 
	 * @param request 用以取session（若操作柜员号为null时，必需）
	 * @param txBranch 短信发卡内部机构号（注意：该机构号涉及到短信收费来源，应为phoneNumber所属人员所在机构的内部机构号）
	 * @return 短信是否发送成功
	 */
	public static boolean sendMobileMessage(String phoneNumber, String msg, String tlrNo, HttpServletRequest request, String txBranch){
		boolean flag = true;
		
		// 参数校验
		if (StringUtils.isEmpty(phoneNumber) || StringUtils.isEmpty(msg) || (tlrNo == null && request == null) || StringUtils.isEmpty(txBranch)) {
			logger.error("method [sendMobileMessage] has wrong params!!!");
			logger.error("method [sendMobileMessage] params : phoneNumber="+phoneNumber+"; msg="+msg+"; tlrNo="+tlrNo+"; request="+request+"; txBranch="+txBranch+";");
			return false;
		}	
		
		// 不是柜员号时传空字符串
		if ("admin".equals(tlrNo) || !tlrNo.matches("^[a-z A-Z 0-9][a-z A-Z 0-9]{6}[a-z A-Z 0-9]$")) {
			tlrNo = "";
		}

		flag = sendMobileMessageTCP(phoneNumber, msg, tlrNo, txBranch);
		return flag;
	}
	
	/**
	 * 发送短信（核心方法）-MQ协议
	 * @deprecated 由于在短信发送平台申请的协议为TCP协议，故此方法暂时无法使用
	 * @param esb2 用于调用短信接口的ESB2对象</br>
	 * 		-- 调用该方法之前需初始化【ESB2】对象：</br>
	 * 			ESB2 esb2 = ESB2.getInstance();</br>
	 * 			esb2.setAsynResponseOnMessage(new AsynResponseOnMessage());</br>
	 * 			esb2.setRequestOnMessage(new BizOnMessage());</br>
	 * 			try {</br>
	 * 				esb2.initESB();</br>
	 * 			} catch (Exception e) {</br>
	 * 				logger.error("ESB2 Object had inited, will not init again");</br>
	 * 			}</br>
	 * 		-- 调用该方法之后需销毁【ESB2】对象：</br>
	 * 			if (esb2 != null) {</br>
	 * 				esb2.destory();</br>
	 * 			}
	 * @param phoneNumber 手机号
	 * @param msg 短信内容
	 * @param tlrNo 操作柜员号 
	 * @param txBranch 短信发卡内部机构号
	 * @return 短信是否发送成功
	 */
	public static boolean sendMobileMessage(ESB2 esb2, String phoneNumber, String msg, String tlrNo, String txBranch){
		boolean flag = true;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("hhmmss");
		
		// 参数校验
		if (StringUtils.isEmpty(phoneNumber) || StringUtils.isEmpty(msg) || StringUtils.isEmpty(txBranch)) {
			logger.error("method [sendMobileMessage] has wrong params!!!");
			logger.error("method [sendMobileMessage] params : phoneNumber="+phoneNumber+"; msg="+msg+"; tlrNo="+tlrNo+"; txBranch="+txBranch+";");
			return false;
		} else if (tlrNo == null) {
			tlrNo = "";
		}
		
		// 请求系统渠道日期
		String chanDate=sdf.format(new Date());		
		try {
			String seqNb = esb2.getSeqNb(); //发起报文流水(报文流水号)
			String dt = sdf.format(new Date());
			String sndTm = sdf1.format(new Date()); 
			Executable exe = new Executable();
			logger.info("method [sendMobileMessage] params : phoneNumber="+phoneNumber+"; msg="+msg+"; tlrNo="+tlrNo+"; txBranch="+txBranch+";");
			// 短信报文
			exe.request=("<transaction>"
					+ "<header>"
					+ "<msg>"
					+ "<sndAppCd>HBB</sndAppCd>"
					+ "<callTyp>SYN</callTyp>"
					+ "<msgCd>SMS.000010020.01</msgCd>"
					+ "<seqNb>"+seqNb+"</seqNb>"
					+ "<sndTm>"+sndTm+"</sndTm>"
					+ "<sndDt>"+dt+"</sndDt>"
					+ "<rcvAppCd>SMS</rcvAppCd>"
					+ "</msg>"
					+ "</header>"
					+ "<body>"
					+ "<request>"
					+ "<smsBizCd>TYZZY001</smsBizCd>"
					+ "<chanDate>"+chanDate+"</chanDate>"
					+ "<chanSeqNo>"+seqNb+"</chanSeqNo>"
					+ "<chanNo>95</chanNo>"
					+ "<telNum>"+phoneNumber+"</telNum>"
					+ "<smsTemplatFlg>0</smsTemplatFlg>"
					+ "<tlrNo>"+tlrNo+"</tlrNo>"
					+ "<smsCntt>"+msg+"</smsCntt>"
					+ "<txBranch>"+txBranch+"</txBranch>"
					+ "</request>"
					+ "</body>"
					+ "</transaction>").getBytes("UTF-8");
			exe.timeout = 60;
			exe.correlationID = (dt + "-" + seqNb).getBytes();
			logger.info("request :" + new String(exe.request,"UTF-8"));
			esb2.execute(exe);
			logger.info("response :" + new String(exe.response,"UTF-8"));
			
			// 获取返回值
			SAXReader reader = new SAXReader();
			Document document = reader.read(new ByteArrayInputStream(exe.response));
			Element root = document.getRootElement();
			// 接口请求状态
			String requestState = root.element("header").element("status").elementText("retCd");
			if (!"000000".equals(requestState)) {
				flag = false;
				logger.error("request to API[sendMobileMessage] failed, get error-code["+requestState+"]!");
			} else {
				// 短信发送结果
				Element responseEle = root.element("body").element("response");
				if (!"000000".equals(responseEle.elementText("retCode"))) {
					logger.error("send massage to mobile["+phoneNumber+"] failed because of ["+responseEle.elementText("retInfo")+"]!");
					flag = false;
				}
			}
		} catch(JmqiException mqe){
			flag = false;
			logger.error("catch JmqiException and reload",mqe);
		} catch (Exception e) {
			flag = false;
			logger.error("catch a Exception",e);
		}
		
		return flag;
	}
	
	/**
	 * 发送短信（核心方法）-TCP协议
	 * @param phoneNumber 手机号
	 * @param msg 短信内容
	 * @param tlrNo 操作柜员号 
	 * @param txBranch 短信发卡内部机构号
	 * @return 短信是否发送成功
	 */
	public static boolean sendMobileMessageTCP(String phoneNumber, String msg, String tlrNo, String txBranch){
		boolean flag = true;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("hhmmss");
		//final String msgServerHost = "11.1.37.50"; //生产短信接口ip
		//final String msgServerHost = "11.0.160.84"; //测试短信接口ip
		//final int msgServerPort = 30001;
		final String msgServerHost = getEsbApiProperties("esb.msg.serverHost");
		int msgServerPort;
		try {
			msgServerPort = Integer.parseInt(getEsbApiProperties("esb.msg.serverPort"));
		} catch (Exception e) {
			msgServerPort = 30001;
		}		
		// 参数校验
		if (StringUtils.isEmpty(phoneNumber) || StringUtils.isEmpty(msg) || StringUtils.isEmpty(txBranch)) {
			logger.error("method [sendMobileMessage] has wrong params!!!");
			logger.error("method [sendMobileMessage] params : phoneNumber="+phoneNumber+"; msg="+msg+"; tlrNo="+tlrNo+"; txBranch="+txBranch+";");
			return false;
		} else if (tlrNo == null) {
			tlrNo = "";
		}
		
		// 请求系统渠道日期
		String chanDate=sdf.format(new Date());		
		try {
			String seqNb = getSeqNb(15); //发起报文流水(报文流水号)
			String dt = sdf.format(new Date());
			String sndTm = sdf1.format(new Date());
			logger.info("method [sendMobileMessage] params : phoneNumber="+phoneNumber+"; msg="+msg+"; tlrNo="+tlrNo+"; txBranch="+txBranch+";");
			// 短信报文
			String request ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<transaction>"
					+ "<header>"
					+ "<msg>"
					+ "<sndAppCd>HBB</sndAppCd>"
					+ "<callTyp>SYN</callTyp>"
					+ "<msgCd>SMS.000010020.01</msgCd>"
					+ "<seqNb>"+seqNb+"</seqNb>"
					+ "<sndTm>"+sndTm+"</sndTm>"
					+ "<sndDt>"+dt+"</sndDt>"
					+ "<rcvAppCd>SMS</rcvAppCd>"
					+ "</msg>"
					+ "</header>"
					+ "<body>"
					+ "<request>"
					+ "<smsBizCd>TYZZY001</smsBizCd>"
					+ "<chanDate>"+chanDate+"</chanDate>"
					+ "<chanSeqNo>"+seqNb+"</chanSeqNo>"
					+ "<chanNo>95</chanNo>"
					+ "<telNum>"+phoneNumber+"</telNum>"
					+ "<smsTemplatFlg>0</smsTemplatFlg>"
					+ "<tlrNo>"+tlrNo+"</tlrNo>"
					+ "<smsCntt>"+msg+"</smsCntt>"
					+ "<txBranch>"+txBranch+"</txBranch>"
					+ "</request>"
					+ "</body>"
					+ "</transaction>";

			logger.info("开始发送短信，request（请求报文） :" + request);
			Socket socket = new Socket(msgServerHost, msgServerPort);
			// 获取输出流，向服务器端发送信息
			OutputStream os = socket.getOutputStream();// 字节输出流
			PrintWriter pw = new PrintWriter(os);// 将输出流包装为打印流
			pw.write(new String(request.getBytes("UTF-8"),"UTF-8"));
			pw.flush();
			// 获取输入流，并读取服务器端的响应信息
			InputStream is = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String info = null;
			StringBuilder result = new StringBuilder();
			while ((info = br.readLine()) != null) {
				result.append(info);
			}
			// 关闭资源
			br.close();
			is.close();
			pw.close();
			os.close();
			socket.close();
			logger.info("短信发送完毕，response（返回报文） :" + result.toString());
			
			// 解析返回报文
			if (StringUtils.isNotEmpty(result.toString())) {
				// 获取返回值
				SAXReader reader = new SAXReader();
				Document document = reader.read(new ByteArrayInputStream(result.toString().getBytes("UTF-8")));
				Element root = document.getRootElement();
				// 接口请求状态
				String requestState = root.element("header").element("status").elementText("retCd");
				String requestStateInfo = root.element("header").element("status").elementText("location");
				requestStateInfo = StringUtils.isNotEmpty(requestStateInfo) ? requestStateInfo : "";
				if (!"000000".equals(requestState)) {
					flag = false;
					logger.error("request to API[sendMobileMessage] failed, get error-code["+requestState+"], get error-info["+requestStateInfo+"]!");
				} else {
					// 短信发送结果
					Element responseEle = root.element("body").element("response");
					if (!"000000".equals(responseEle.elementText("retCode"))) {
						logger.error("send massage to mobile["+phoneNumber+"] failed because of ["+responseEle.elementText("retInfo")+"]!");
						flag = false;
					}
				}
			} else {
				flag = false;
				logger.error("send massage to mobile["+phoneNumber+"] failed because of [response is null]!");
			}
		} catch (Exception e) {
			flag = false;
			logger.error("catch a Exception ["+e.getClass().getName()+"] : ",e.getMessage());
		}
		
		if (flag) {
			logger.info("send massage to mobile["+phoneNumber+"] successed!");
		}
		
		return flag;
	}
	
	/**
	 * 获取时间戳
	 * @param len 时间戳长度
	 * @return 指定长度的时间戳
	 */
	public static String getTimeSN(int len) {
		long time = System.currentTimeMillis();
		if (time <= LAST_TIME) {
			time = LAST_TIME + 1L;
		}
		LAST_TIME = time;
		String str = String.valueOf(time);
		return len > str.length() ? str : str.substring(str.length() - len);
	}

	/**
	 * 获取随机数
	 * @param len 随机数长度
	 * @return 指定长度的随机数
	 */
	public static String random(int len) {
		String random = String.valueOf(Math.random()).substring(2) + String.valueOf(Math.random()).substring(2);

		random = random.replaceAll("E", "").replaceAll("-", "");
		return len > random.length() ? random : random.substring(random.length() - len);
	}

	/**
	 * 获取交易流水号
	 * @param len 流水号长度
	 * @return 指定长度的交易流水号
	 */
	public static String getSeqNb(int len) {
		String seqNb = "A1" + getTimeSN(8) + random(24);
		return seqNb.substring(0, len);
	}
	
	/**
     * 获取OracleConnection对象
     * @return OracleConnection对象
     */
    public static Connection getOracleConnection() {
    	Connection connection = null;
        try {
            Class.forName(getJdbcProperties("oracle.connection.driverClass"));
            connection = DriverManager.getConnection(
            		getJdbcProperties("oracle.connection.jdbcUrl"), 
            		getJdbcProperties("oracle.connection.username"), 
            		getJdbcProperties("oracle.connection.password"));
            logger.info("成功连接数据库");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("class not find !", e);
        } catch (SQLException e) {
            throw new RuntimeException("get connection error!", e);
        }

        return connection;
    }
    
    /**
     * 根据数据源名称获取Connection对象
     * @param datasourceName 表【report_datasource_manager】中数据源名称（DNAME）
     * @return Connection对象
     */
    public static Connection getConnectionByDatasourceName(String datasourceName) {
    	CustomReportAdditionalService service = SpringContextUtils.getBean(CustomReportAdditionalService.class);
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("dname", datasourceName);
		List<Map> datasourceList = service.getDataList("selectDatasource", params);
		if (datasourceList == null || datasourceList.isEmpty() || datasourceList.get(0) == null) {
			throw new RuntimeException("数据源【"+datasourceName+"】的信息未找到");
		}
		Map datasourceInfo = datasourceList.get(0);
		
		Connection connection = null;
		// Oracle数据库
		if ("01".equals(datasourceInfo.get("DRIVER")) && StringUtils.isNotEmpty(datasourceInfo.get("HOST"))
				&& StringUtils.isNotEmpty(datasourceInfo.get("PORT")) && StringUtils.isNotEmpty(datasourceInfo.get("DBNAME"))
				&& StringUtils.isNotEmpty(datasourceInfo.get("DBUSER")) && StringUtils.isNotEmpty(datasourceInfo.get("DBUSERPWD"))) {
			String driver = "oracle.jdbc.driver.OracleDriver";
			// 格式：jdbc:oracle:thin:@localhost:1521:ORCL
			String url = "jdbc:oracle:thin:@" + StringUtils.parseString(datasourceInfo.get("HOST")) + ":"
					+ StringUtils.parseString(datasourceInfo.get("PORT")) + ":"
					+ StringUtils.parseString(datasourceInfo.get("DBNAME"));
			String username = StringUtils.parseString(datasourceInfo.get("DBUSER"));
			String password = Base64.base64Decode(StringUtils.parseString(datasourceInfo.get("DBUSERPWD")));
			try {
	            Class.forName(driver);
	            connection = DriverManager.getConnection(url,username,password);
	            logger.info("成功连接数据库");
	        } catch (ClassNotFoundException e) {
	            throw new RuntimeException("class not find !", e);
	        } catch (SQLException e) {
	        	
	        }

	        return connection;
		} else {
			// 暂时只实现Oracle数据库
			throw new RuntimeException("不支持数据源【"+datasourceName+"】的数据库类型");
		}
    }
    
    /**
     * 获取jdbcProperties文件的值
     * @param key key
     * @return value
     */
	public static String getJdbcProperties(String key) {
		String value = null;
		try {
			if (jdbcProperties == null) {
				jdbcProperties = new Properties();
				// 使用properties对象加载输入流
				jdbcProperties.load(JDBC_PROPERTIES_IN_STREAM);
			}
			logger.debug("jdbcProperties文件内容（key【"+key+"】）："+jdbcProperties);
			//获取key对应的value值
			value = jdbcProperties.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	/**
     * 获取esbApiProperties文件的值
     * @param key key
     * @return value
     */
	public static String getEsbApiProperties(String key) {
		String value = null;
		try {
			if (esbApiProperties == null) {
				esbApiProperties = new Properties();
				// 使用properties对象加载输入流
				esbApiProperties.load(ESBAPI_PROPERTIES_IN_STREAM);
			}
			logger.debug("esbApiProperties文件内容（key【"+key+"】）："+esbApiProperties);
			//获取key对应的value值
			value = esbApiProperties.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	/**
     * List<Map<String, String>>去除【全部】value都为空的Map
     * @param result 包含value都为空的Map的结果集
     * @return value都为空的Map均被去除后的深度复制结果集
     */
    public static List<Map<String, String>> removeEmptyValueMapFromResultAll(List<Map<String, String>> result){
    	logger.debug("参数："+JSONArray.fromObject(result).toString());
    	List<Map<String, String>> resultWithoutEmptyValueMap = new ArrayList<Map<String, String>>();
    	for (Map<String, String> map : result) {
    		// 深度复制后的map
    		Map<String, String> mapNew = new LinkedHashMap<>();
    		boolean isAllEmpty = true;
    		for (String key : map.keySet()) {
    			mapNew.put(key, map.get(key));
    			if (StringUtils.isNotEmpty(map.get(key))) {
    				isAllEmpty = false;
    			} 
    		}
    		if (!isAllEmpty) resultWithoutEmptyValueMap.add(mapNew);
    	}
    	logger.debug("返回值："+JSONArray.fromObject(resultWithoutEmptyValueMap).toString());
    	return resultWithoutEmptyValueMap;
    }
    
    /**
     * List<Map<String, String>>去除【后方】value都为空的Map
     * @param result 包含value都为空的Map的结果集
     * @return 后方value都为空的Map均被去除后的深度复制结果集，从最后一个map开始，遇到第一个不为空的结束
     */
    public static List<Map<String, String>> removeEmptyValueMapFromResultAfter(List<Map<String, String>> result){
    	List<Map<String, String>> resultWithoutEmptyValueMapTemp = new ArrayList<Map<String, String>>();
    	List<Map<String, String>> resultWithoutEmptyValueMap = new ArrayList<Map<String, String>>();
    	// 是否已经遇到了一个不为空的map
    	boolean hasNotEmptyMap = false;
    	for (int mapIx = result.size()-1; mapIx >= 0; mapIx--) {
    		// 深度复制后的map
    		Map<String, String> mapNew = new LinkedHashMap<>();
    		
    		Map<String, String> map = result.get(mapIx);
    		// 若已遇到一个不为空的map，则接下来的map都不再进行不为空的过滤
    		if (!hasNotEmptyMap) {
    			// 是否map所有value都为空
    			boolean isAllEmpty = true;
    			for (String key : map.keySet()) {
    				mapNew.put(key, map.get(key));
    				if (StringUtils.isNotEmpty(map.get(key))) {
    					isAllEmpty = false;
    					hasNotEmptyMap = true;
    				} 
    			}
    			if (!isAllEmpty) {
    				resultWithoutEmptyValueMapTemp.add(mapNew);
    			} else {
    				logger.debug("当前索引【"+mapIx+"】的map的所有value均为空");
    			}
    		} else {
    			logger.debug("自索引【"+mapIx+"】以前（包括该索引）的map将不再进行去空map处理");
    			for (String key : map.keySet()) {
    				mapNew.put(key, map.get(key));
    			}
    			resultWithoutEmptyValueMapTemp.add(map);
    		}
    	}
    	logger.debug("去空map处理后的list结果："+JSONArray.fromObject(resultWithoutEmptyValueMapTemp).toString());
    	
    	// 将顺序改为原结果集的顺序
    	for (int mapIx = resultWithoutEmptyValueMapTemp.size()-1; mapIx >= 0; mapIx--) {
    		resultWithoutEmptyValueMap.add(resultWithoutEmptyValueMapTemp.get(mapIx));
    	}
    	logger.debug("返回结果："+JSONArray.fromObject(resultWithoutEmptyValueMap).toString());
    	return resultWithoutEmptyValueMap;
    }
    
    /**
     * List<Map<String, String>>去除【前方】value都为空的Map
     * @param result 包含value都为空的Map的结果集
     * @return 前方value都为空的Map均被去除后的深度复制结果集，从第一个map开始，遇到第一个不为空的结束
     */
    public static List<Map<String, String>> removeEmptyValueMapFromResultBefore(List<Map<String, String>> result){
    	List<Map<String, String>> resultWithoutEmptyValueMap = new ArrayList<Map<String, String>>();
    	// 是否已经遇到了一个不为空的map
    	boolean hasNotEmptyMap = false;
    	for (int mapIx = 0; mapIx < result.size(); mapIx++) {
    		// 深度复制后的map
    		Map<String, String> mapNew = new LinkedHashMap<>();
    		
    		Map<String, String> map = result.get(mapIx);
    		// 若已遇到一个不为空的map，则接下来的map都不再进行不为空的过滤
    		if (!hasNotEmptyMap) {
    			// 是否map所有value都为空
    			boolean isAllEmpty = true;
    			for (String key : map.keySet()) {
    				mapNew.put(key, map.get(key));
    				if (StringUtils.isNotEmpty(map.get(key))) {
    					isAllEmpty = false;
    					hasNotEmptyMap = true;
    				} 
    			}
    			if (!isAllEmpty) {
    				resultWithoutEmptyValueMap.add(mapNew);
    			} else {
    				logger.debug("当前索引【"+mapIx+"】的map的所有value均为空");
    			}
    		} else {
    			logger.debug("自索引【"+mapIx+"】以后（包括该索引）的map将不再进行去空map处理");
    			for (String key : map.keySet()) {
    				mapNew.put(key, map.get(key));
    			}
    			resultWithoutEmptyValueMap.add(map);
    		}
    	}
    	logger.debug("返回结果："+JSONArray.fromObject(resultWithoutEmptyValueMap).toString());
    	return resultWithoutEmptyValueMap;
    }
    /**
     * 取得<String, String>的map中value不为空的所有项组成的map
     * @param map 包含空value的项的map
     * @return map中value不为空的所有项组成的map
     */
    public static Map<String, String> removeEmptyValueElem(Map<String, String> map){
    	Map<String, String> result = null;
    	if (map instanceof HashMap) {
    		logger.debug("参数类型为【HashMap】，返回的结果被初始化为该类型");
    		result = new HashMap<String, String>();
    	} else if (map instanceof LinkedHashMap) {
    		logger.debug("参数类型为【LinkedHashMap】，返回的结果被初始化为该类型");
    		result = new LinkedHashMap<String, String>();
    	} else if (map instanceof TreeMap) {
    		logger.debug("参数类型为【TreeMap】，返回的结果被初始化为该类型");
    		result = new TreeMap<String, String>();
    	}
    	
    	// 去空
    	for (String key : map.keySet()) {
    		if (StringUtils.isNotEmpty(map.get(key))) {
    			result.put(key, map.get(key));
    		}
    	}
    	logger.debug("返回值："+JSONObject.fromObject(result).toString());
    	return result;
    }
    
    /**
     * 替换map中元素的key（将原始key替换成新的key，若有新的value可将原始value替换为新value）
     * @param map 包含元素的map
     * @param oldKey 原始key
     * @param newKey 新的key
     * @param newValue 新value(可为null，此时为原始值)
     */
    @SuppressWarnings("rawtypes")
	public static void replaceElemKey(Map map, Object oldKey, Object newKey, Object newValue){
    	if (map instanceof LinkedHashMap) {
    		boolean isBefore = true;
    		// 要替换元素key之前的元素
    		LinkedHashMap<Object,Object> beforeCells = new LinkedHashMap<Object,Object>();
    		// 要替换元素key的元素
    		LinkedHashMap<Object,Object> currentCell = new LinkedHashMap<Object,Object>();
    		// 要替换元素key之后的元素
    		LinkedHashMap<Object,Object> afterCells = new LinkedHashMap<Object,Object>();
    		for (Object key : map.keySet()) {
    			if (key.equals(oldKey)) {
    				currentCell.put(newKey, newValue != null ? newValue : map.get(key));
    				isBefore = false;
    			} else if (isBefore) {
    				beforeCells.put(key, map.get(key));
    			} else {
    				afterCells.put(key, map.get(key));
    			}
    		}
    		
    		// 将所有元素再按顺序写入到map中
    		map.clear();
    		for (Object key : beforeCells.keySet()) {
    			map.put(key, beforeCells.get(key));
    		}
    		for (Object key : currentCell.keySet()) {
    			map.put(key, currentCell.get(key));
    		}
    		for (Object key : afterCells.keySet()) {
    			map.put(key, afterCells.get(key));
    		}
    	}
    }
	
//	public static void main(String[] args) throws Exception{
////		String tlrNo = "22020011";
////		String msg = "货币金银采集系统中有您一条未完成的任务，请前往处理！";
////		String phoneNumber = "18336396052";
////		String txBranch = "1622499998";
////		boolean flg = CommonUtil.sendMobileMessage(phoneNumber, msg, tlrNo, null, txBranch);
////		System.out.println("---------------------------- 短信发送"+(flg?"成功！":"失败！"));
//		
////		JDBC_PROPERTIES_IN_STREAM = new BufferedReader(new FileReader("H:\\863\\项目\\河南农信数据报送系统\\svn\\hbjy\\currencySys\\config\\ibatis\\jdbc.properties"));
//		Connection connection = getOracleConnection();
//		PreparedStatement pstm = connection.prepareStatement("select t.* from REPORT_DATASOURCE_MANAGER t");
//		ResultSet rs = pstm.executeQuery();
//		System.out.println("|DNAME	|DRIVER	|HOST	|PORT	|DBNAME	|DBUSER	|DBUSERPWD");
//		while (rs.next()) {
//            System.out.print("|"+rs.getString(1)+"	");
//            System.out.print("|"+rs.getString(2)+"	");
//            System.out.print("|"+rs.getString(3)+"	");
//            System.out.print("|"+rs.getString(4)+"	");
//            System.out.print("|"+rs.getString(5)+"	");
//            System.out.print("|"+rs.getString(6)+"	");
//            System.out.print("|"+rs.getString(7)+"	");
//            System.out.println();
//        }
//	}
}
