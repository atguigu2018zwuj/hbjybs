package com.mininglamp.currencySys.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class StringUtils extends org.apache.commons.lang3.StringUtils{
	
	/**
	 * 首字母变大写
	 */
	public static String firstCharToUpperCase(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= 'a' && firstChar <= 'z') {
			char[] arr = str.toCharArray();
			arr[0] -= ('a' - 'A');
			return new String(arr);
		}
		return str;
	}
	
	/**
	 * 首字母变小写
	 */
	public static String firstCharToLowerCase(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= 'A' && firstChar <= 'Z') {
			char[] arr = str.toCharArray();
			arr[0] += ('a' - 'A');
			return new String(arr);
		}
		return str;
	}
	
	/**
	 * 转化换为字符串
	 */
	public static String parseString(Object strObj) {
		 return isNull(strObj) ? null : String.valueOf(strObj);
	}
	
	/**
	 * 转化换为非null的字符串
	 * @return 如果strObj为null，则返回空字符串
	 */
	public static String parseStringWithoutNull(Object strObj) {
		 return isNull(strObj) ? "" : String.valueOf(strObj);
	}
	
	/**
	 * 字符串判空
	 * @param strObj
	 * @return 若字符串为null或emptyString时，返回true；否则返回false；
	 */
	public static boolean isEmpty(Object strObj) {
		 return isEmpty(parseString(strObj));
	}
	
	/**
	 * 字符串判空
	 * @param strObj
	 * @return 若字符串不为null且不为emptyString时，返回true；否则返回false；
	 */
	public static boolean isNotEmpty(Object strObj) {
		 return isNotEmpty(parseString(strObj));
	}
	
	/**
	 * 字符串对象判空
	 * @param strObj 字符串对象
	 * @return 某些特殊情况处理为null（如json解析后出现的JSONNull的情况）
	 */
	public static boolean isNull(Object strObj) {
		 return strObj == null || "null".equals(String.valueOf(strObj));
	}
	
	/**
	 * 压缩GZip
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] gZip(byte[] data) {
		byte[] b = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(bos);
			gzip.write(data);
			gzip.finish();
			gzip.close();
			b = bos.toByteArray();
			bos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return b;
	}

	/**
	 * 解压GZip
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] unGZip(byte[] data) {
		byte[] b = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			GZIPInputStream gzip = new GZIPInputStream(bis);
			byte[] buf = new byte[1024];
			int num = -1;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((num = gzip.read(buf, 0, buf.length)) != -1) {
				baos.write(buf, 0, num);
			}
			b = baos.toByteArray();
			baos.flush();
			baos.close();
			gzip.close();
			bis.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return b;
	}
}
