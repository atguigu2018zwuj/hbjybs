package com.mininglamp.currencySys.util.CheckProgram;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;


/**
 * 数据类型正则校验
 * @author lihaolong
 * @date 2017年5月16日
 * @version 1.0
 */
public class FieldTypeCheck {

	/**
	 * 数据字段长度校验
	 * @param dataField 数据字段值
	 * @param expressionStr 字段类型的表达式
	 * @return
	 */
	public static boolean dataFieldTypeCheck(String dataField,String expressionStr ) {

		/* n..?  	最长?位数字型
		 * c..? 	最长?位字符型
		 * n?   	?位数字定长型
		 * c?    	?位字符定长型
		 * n..(a,b)	最长a位数字型，其中b位小数
		 */
		dataField = dataField;
		expressionStr = expressionStr;             					//去除空格处理
		
		if(!expressionStr.startsWith("n") && !expressionStr.startsWith("c") && !expressionStr.equals("date") && !expressionStr.equals("time")){
			return false;
		}
		
		if(("date").equals(expressionStr)){   								//日期类型格式校验
			if(DataDateCheck.isValidDataFormat(dataField)){
				return true;
			}
		}else if(("time").equals(expressionStr)){   						//日期类型格式校验
			if(DataDateCheck.isValidDataTime(dataField)){
				return true;
			}
		}else if(expressionStr.startsWith("c")){
			if(expressionStr.startsWith("c..") && StringUtils.isNumeric(expressionStr.substring(3))){
				if(dataField.length() <= Integer.parseInt((expressionStr.substring(3)))){
					return true;
				}
			}else {
				if(!StringUtils.isNumeric(expressionStr.substring(1))){
					return false;
				}
				if(dataField.length() == Integer.parseInt((expressionStr.substring(1)))){
					return true;
				}
			}
		}else if(expressionStr.startsWith("n")){							// 数字类型校验
			if(expressionStr.startsWith("n..")){
				if(expressionStr.startsWith("n..(")){
					String numCountStr = expressionStr.substring(expressionStr.indexOf("(") + 1,expressionStr.indexOf(","));
					String decimalCountStr = expressionStr.substring(expressionStr.indexOf(",") + 1,expressionStr.indexOf(")"));
					if(!StringUtils.isNumeric(numCountStr) || !StringUtils.isNumeric(decimalCountStr)){
						return false;
					}
					String datePattern = "^(\\-?)[0-9]\\d{0,"+ numCountStr +"}(\\.\\d{1,"+ decimalCountStr +"})?$";
					Pattern pattern = Pattern.compile(datePattern);
					Matcher match = pattern.matcher(dataField);
					if(match.matches() && dataField.length() - 1 <= Integer.parseInt(numCountStr)){
						return true;
					}
				} else {
					if(!StringUtils.isNumeric(expressionStr.substring(3))){
						return false;
					}
					if(StringUtils.isNumeric(dataField) &&
							dataField.length()<=Integer.parseInt((expressionStr.substring(3)))){
						return true;
					}
				}
			} else {
				if(!StringUtils.isNumeric(expressionStr.substring(1))){
					return false;
				}
				if(StringUtils.isNumeric(dataField) &&
						dataField.length()==Integer.parseInt((expressionStr.substring(1)))){
					return true;
				}
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println(dataFieldTypeCheck("11", "c2"));
	}
}
