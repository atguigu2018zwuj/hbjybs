package com.mininglamp.currencySys.common.aop;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

@SuppressWarnings("unchecked")
public class ObjectTranscoder {
	private static File _cfFile = null;
	private static long _lastModified = 0;
	private static final Map<String,List<Property>> classNameMap=new HashMap<String,List<Property>>();
	private static final Map<String,Map<String,String>> translateMap=new HashMap<String,Map<String,String>>();
	private static final Map<String,Map<String,String>> replaceMap=new HashMap<String,Map<String,String>>();
	
		
		//<property name="xx" formatType="numberformat" pattern="0.00"/>
		//<property name="name1" formatType="dateformat" fromPattern="yyyyMMddHHmmss" toPattern="yyyy-MM-dd HH:mm:ss"/>
	static{
		String path=ObjectTranscoder.class.getResource("/transcoder.xml").getPath();
		try {
			String rpath=URLDecoder.decode(path, "utf-8");
			_cfFile=new File(rpath);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	private static void init(){
		synchronized (ObjectTranscoder.class) {
			if (_cfFile.lastModified() != _lastModified) {
				classNameMap.clear();
				translateMap.clear();
				replaceMap.clear();
				SAXReader saxReader = new SAXReader();
				 try {
					Document docTemp = saxReader.read(_cfFile);
					List<Element> xlsNodes = docTemp.selectNodes("//classnames/classname");
					for (Iterator<Element> it = xlsNodes.iterator(); it.hasNext();) {
						Element elmt = it.next();
						String className=elmt.attributeValue("name");
						
						Iterator<Element> itp=elmt.elementIterator("property");
						List<Property> propertyList=new ArrayList<Property>();
						while(itp.hasNext()){
							Element elmp= itp.next();
							Property property=new Property();
							String propertyName=elmp.attributeValue("name");
							property.setName(propertyName);
							String formatType=elmp.attributeValue("formatType");
							property.setFormatType(formatType);
							String codeId=elmp.attributeValue("codeId");
							property.setCodeId(codeId);
							String fromPattern=elmp.attributeValue("fromPattern");
							property.setFromPattern(fromPattern);
							String toPattern=elmp.attributeValue("toPattern");
							property.setToPattern(toPattern);
							String pattern=elmp.attributeValue("pattern");
							property.setPattern(pattern);
							String lineage = elmp.attributeValue("lineage");
							property.setLineage(lineage);
							/*String add=elmp.attributeValue("add");
							property.setAdd(add);
							String divide=elmp.attributeValue("divide");
							property.setDivide(divide);
							String multiply=elmp.attributeValue("multiply");
							property.setMultiply(multiply);
							elmp.*/
							Iterator<Element> operationIte =elmp.elementIterator();
							List<String> operationList=new ArrayList<String>();
							while(operationIte.hasNext()){
								Element operationEle= operationIte.next();
								String operation=operationEle.getName();
								String text=operationEle.getText();
								operationList.add(operation+","+text);
							}
							property.setOperationList(operationList);
							propertyList.add(property);
						}
						classNameMap.put(className, propertyList);
					}
					List<Element> translatesNodes = docTemp.selectNodes("//translates/translate");
					for (Iterator<Element> it = translatesNodes.iterator(); it.hasNext();) {
						Element elmt = it.next();
						String coderid=elmt.attributeValue("id");
						Iterator<Element> itp=elmt.elementIterator("code");
						Map<String,String> codeMap=new HashMap<String,String>();
						while(itp.hasNext()){
							Element elmp= itp.next();
							String key=elmp.attributeValue("key");
							String value=elmp.attributeValue("value");
							codeMap.put(key, value);
						}
						translateMap.put(coderid, codeMap);
					}
					List<Element> replaceNodes = docTemp.selectNodes("//translates/replace");
					for (Iterator<Element> it = replaceNodes.iterator(); it.hasNext();) {
						Element elmt = it.next();
						String coderid=elmt.attributeValue("id");
						Iterator<Element> itp=elmt.elementIterator("code");
						Map<String,String> codeMap=new HashMap<String,String>();
						while(itp.hasNext()){
							Element elmp= itp.next();
							String key=elmp.attributeValue("regex");
							String value=elmp.attributeValue("replacement");
							codeMap.put(key, value==null?"":value);
						}
						replaceMap.put(coderid, codeMap);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					
				}
				_lastModified = _cfFile.lastModified();
			}
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String s="142431198612011217";
		s=ObjectTranscoder.desensitizationIdNumber(s);
		System.out.println(s);
		/*Map map=new HashMap();
		map.put("total_debitamt", "1.234561234E7");
		ObjectTranscoder.transcodeMap("getfakeAmountTop", map);
		System.out.println(map.get("total_debitamt"));*/
		/*AgentFrequentBean bean=new AgentFrequentBean();
		bean.setPersonIdtype("10");
		//ObjectTranscoder.transcode(bean);
		Map m=new HashMap();
		System.out.println(m.getClass().getName());*/
		/*long time=60000;
		System.out.println(time/60000);
		System.out.println(60001/60000);
		if(time/60000>0){
			
			
		}
		Date d=new Date(time);
		SimpleDateFormat formatter = new SimpleDateFormat("mm分ss秒SS毫秒");
        String s= formatter.format(d);
		System.out.println(s);*/
		/*Double b=new Double(189454567896789.123456789);
		BigDecimal bd=new BigDecimal(b);
		System.out.println(b);
		System.out.println(bd);
		try {
			Field field=new ObjectTranscoder().getClass().getDeclaredField("classNameMap");
			System.out.println(field.getType().getName());
			System.out.println(field.getType()==java.util.Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		//System.out.println(formatNumber("0.125456789123456789", "0.00"));
	}
	/**
	 * replace
	 * @param source
	 * @param regex
	 * @param replacement
	 * @return
	 */
	public static String replace(String source,Map<String,String> transMap){
		String newValue=source;
		Set<Entry<String, String>> set=transMap.entrySet();
		Iterator<Entry<String, String>> itea=set.iterator();
		while(itea.hasNext()){
			Entry<String, String> entry=itea.next();
			String regex=entry.getKey();
			String replacement=entry.getValue();
			newValue=newValue.replaceAll(regex, replacement);
		}
		return newValue;
	}
	/**
	 * 屏蔽身份证号敏感信息
	 * @param source
	 * @return
	 */
	public static String desensitizationIdNumber(String source){
		String idNumber=source;
		if(source!=null&&source.length()>14){
			idNumber=idNumber.substring(0, 10)+"****"+idNumber.substring(14, idNumber.length());
		}
		return idNumber;
	}
	/**
	 * 屏蔽账号敏感信息
	 * @param source
	 * @return
	 */
	public static String desensitizationAccount(String source){
		String account = null;
		if(source!=null){
			source=source.trim();
		
			account = source;
			if(source.length()>6){
				account=account.substring(0, account.length()-6)+"****"+account.substring(account.length()-2, account.length());
			}
		}
		return account;
	}
	/**
	 * 格式化时间戳
	 * @param source
	 * @param fromPattern
	 * @param toPattern
	 * @return
	 */
	public static String formatDate(String source,String fromPattern,String toPattern){
		Date d=null;
		String newValue=source;
		if(StringUtils.isNotBlank(source)){
			try {
				d = DateUtils.parseDate(source, fromPattern);
				SimpleDateFormat formatter = new SimpleDateFormat(toPattern);
				newValue=formatter.format(d);
			} catch (ParseException e) {
			}
		}
        return newValue;
	}
	/**
	 * 格式化数字
	 * @param source
	 * @param pattern
	 * @return
	 */
	private static String formatNumber(String source,String pattern,List<String> operationList){
		String newValue=source;
		if(StringUtils.isNotBlank(source)){
			Double dbl=formatNumber(new Double(source), pattern, operationList);
			DecimalFormat format=new DecimalFormat(pattern);
			newValue=format.format(dbl);
		}
		return newValue;
	}
	/**
	 * 数字运算 add +, divide /,multiply *
	 * @param source
	 * @param pattern
	 * @return
	 */
	private static BigDecimal operationNumber(BigDecimal source,List<String> operationList){
		if(operationList!=null&&operationList.size()>0){
			try {
				for(String operation:operationList){
					String s []=operation.split(",");
					String key=s[0];
					String value=s[1];
					BigDecimal bd=new BigDecimal(value);
					Method m=source.getClass().getMethod(key, BigDecimal.class);
					source=(BigDecimal)m.invoke(source, bd);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return source;
	}
	/**
	 * 格式化数字
	 * @param source
	 * @param pattern
	 * @return
	 */
	private static Double formatNumber(Double source,String pattern,List<String> operationList){
		Double newValue=source;
		if(source!=null){
			BigDecimal bd=new BigDecimal(source);
			bd=operationNumber(bd,operationList);
			DecimalFormat format=new DecimalFormat(pattern);
			newValue=Double.valueOf(format.format(bd.doubleValue()));
		}
		return newValue;
	}
	public static void transcodeMap(String transCodeKey,Map map){
		if(StringUtils.isBlank(transCodeKey)){
			return;
		}
		if (_cfFile.lastModified() != _lastModified) {
			init();
		}
		List<Property> propertyList=classNameMap.get(transCodeKey);
		if(propertyList!=null&&propertyList.size()>0){
			for(Property p:propertyList){
				String name=p.getName();
				String formatType=p.getFormatType();
				try{
					Object fieldValue=map.get(name);
					
					if(fieldValue!=null){
						
						Object newValue=fieldValue;
						if(fieldValue.getClass()==java.lang.String.class){
							if(formatType.equals(Property.TRANSLATE)){
								String codeId=p.getCodeId();
								Map<String,String> transMap= translateMap.get(codeId);
								if(transMap.containsKey(fieldValue)){
									newValue=transMap.get(fieldValue);
								}
							}else if(formatType.equals(Property.DATEFORMAT)){
								newValue=formatDate(fieldValue.toString(),p.getFromPattern(),p.getToPattern());
							}else if(formatType.equals(Property.NUMBERFORMAT)){
								newValue=formatNumber(fieldValue.toString(),p.getPattern(),p.getOperationList());
							}else if(formatType.equals(Property.REPLACE)){
								String codeId=p.getCodeId();
								Map<String,String> transMap= replaceMap.get(codeId);
								newValue=replace(fieldValue.toString(),transMap);
							}else if(formatType.equals(Property.DESENSITIZATIONIDNUMBER)){
								newValue=desensitizationIdNumber(fieldValue.toString());
							}else if(formatType.equals(Property.DESENSITIZATIONACCOUNT)){
								newValue=desensitizationAccount(fieldValue.toString());
							}
						}else if(fieldValue instanceof java.lang.Number){
							if(formatType.equals(Property.NUMBERFORMAT)){
								newValue=formatNumber(((Number)fieldValue).doubleValue(),p.getPattern(),p.getOperationList());
							}
						}
						map.put(name, newValue);
					}else{
						map.put(name, "");
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 翻译
	 * @param obj
	 */
	public static void transcode(Object obj){
		if (_cfFile.lastModified() != _lastModified) {
			init();
		}
		String className=obj.getClass().getName();
		List<Property> propertyList=classNameMap.get(className);
		if(propertyList!=null&&propertyList.size()>0){
			for(Property p:propertyList){
				String name=p.getName();
				String formatType=p.getFormatType();
				try{
				    Field field = null;
				    if(p.getLineage() != null&&Property.INHERIT.equals(p.getLineage()))
					    field=obj.getClass().getSuperclass().getDeclaredField(name);
				    else
				        field=obj.getClass().getDeclaredField(name);
					field.setAccessible(true);
					Object fieldValueObj=field.get(obj);
					if(fieldValueObj!=null){
						Object newValue=fieldValueObj;
						if(field.getType()==java.lang.String.class){
							String fieldValue=fieldValueObj==null?null:fieldValueObj.toString();
							if(formatType.equals(Property.TRANSLATE)){
								String codeId=p.getCodeId();
								Map<String,String> transMap= translateMap.get(codeId);
								if(transMap.containsKey(fieldValue)){
									newValue=transMap.get(fieldValue);
								}
							}else if(formatType.equals(Property.DATEFORMAT)){
								newValue=formatDate(fieldValue,p.getFromPattern(),p.getToPattern());
							}else if(formatType.equals(Property.NUMBERFORMAT)){
								newValue=formatNumber(fieldValue,p.getPattern(),p.getOperationList());
							}else if(formatType.equals(Property.REPLACE)){
								String codeId=p.getCodeId();
								Map<String,String> transMap= replaceMap.get(codeId);
								newValue=replace(fieldValue.toString(),transMap);
							}else if(formatType.equals(Property.DESENSITIZATIONIDNUMBER)){
								newValue=desensitizationIdNumber(fieldValue.toString());
							}else if(formatType.equals(Property.DESENSITIZATIONACCOUNT)){
								newValue=desensitizationAccount(fieldValue.toString());
							}
						}else if(fieldValueObj instanceof java.lang.Number){
							if(formatType.equals(Property.NUMBERFORMAT)){
								newValue=formatNumber(((Number)fieldValueObj).doubleValue(),p.getPattern(),p.getOperationList());
							}
						}
						field.set(obj, newValue);
					}else{
						if(field.getType()==java.lang.String.class){
							field.set(obj, "");
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	
	static class Property{
		
		public static final String NUMBERFORMAT="numberformat";
		public static final String DATEFORMAT="dateformat";
		public static final String TRANSLATE="translate";
		public static final String REPLACE="replace";
		public static final String OPERATION="operation";
		public static final String DESENSITIZATIONIDNUMBER="desensitizationIdNumber";//屏蔽身份证号敏感信息
		public static final String DESENSITIZATIONACCOUNT="desensitizationAccount";//屏蔽账号敏感信息
		//add +, divide /,multiply *
		public static final String OPERATION_ADD="add";
		public static final String OPERATION_DIVIDE="divide";
		public static final String OPERATION_MULTIPLY="multiply";
		public static final String INHERIT = "inherit";
		//operation 运算
		public Property(){
			
		}
		private String name;
		private String formatType;
		private String codeId;
		private String fromPattern;
		private String toPattern;
		private String pattern;
		private String lineage;
		private List<String> operationList;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getFormatType() {
			return formatType;
		}
		public void setFormatType(String formatType) {
			this.formatType = formatType;
		}
		public String getCodeId() {
			return codeId;
		}
		public void setCodeId(String codeId) {
			this.codeId = codeId;
		}
		public String getFromPattern() {
			return fromPattern;
		}
		public void setFromPattern(String fromPattern) {
			this.fromPattern = fromPattern;
		}
		public String getToPattern() {
			return toPattern;
		}
		public void setToPattern(String toPattern) {
			this.toPattern = toPattern;
		}
		public String getPattern() {
			return pattern;
		}
		public void setPattern(String pattern) {
			this.pattern = pattern;
		}
		public List<String> getOperationList() {
			return operationList;
		}
		public void setOperationList(List<String> operationList) {
			this.operationList = operationList;
		}
        public String getLineage() {
            return lineage;
        }
        public void setLineage(String lineage) {
            this.lineage = lineage;
        }
		
	}
}
