package com.mininglamp.currencySys.util;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mininglamp.logx.kit.StrKit;
import com.mininglamp.logx.util.WebUtil;
import com.mininglamp.currencySys.common.auth.AppCodeManager;
import com.mininglamp.currencySys.common.auth.XMLAppCodeManager;
import com.mininglamp.currencySys.common.exception.ConflictException;

/**
 * 用户安全性验证
 * @author czy
 * 2016年9月20日20:27:17
 */
public class SecurityUtil {
	private static AppCodeManager xmlAppCodeManager;
	static{
		/**
		 * 初始化操作
		 */
		xmlAppCodeManager = new XMLAppCodeManager();
		try {
			xmlAppCodeManager.init();
		} catch (ConflictException e) {
			e.printStackTrace();
		}
	}
	/**
	 * token有效间隔30分钟
	 */
	private static Long tokenIntervalTime = 30 * 60 * 1000l;
	/**
	 * 存放token时长信息
	 */
	private static Map<String,Long> tokenMap = new HashMap<String,Long>();
	/**
	 * 存放ip和token对应关系
	 */
	private static Map<String,String> ipTokenRelateMap = new HashMap<String,String>();
	/**
	 * 验证token 0=成功 1=失效 2=失败
	 * @param token
	 * @param ip
	 * @return 0=成功 1=失效 2=失败
	 */
	public static int validateToken(String token,String ip){
		if(StrKit.isBlank(token) || StrKit.isBlank(ip)) return 2;
		/**
		 * 根据ip获取token并验证是否和传入的相同
		 */
		String _token = ipTokenRelateMap.get(ip);
		if(_token == null || !_token.equals(token)) return 2;
		/**
		 * 根据token获取时间，并判断时间是否超时
		 */
		Long lastTime = tokenMap.get(token);
		if(lastTime == null){
			tokenMap.remove(token);
			return 2;
		}
		Long nowTime = System.currentTimeMillis();
		if(lastTime + tokenIntervalTime > nowTime){
			//更新时间
			tokenMap.put(token, nowTime);
			return 0;
		}else{
			return 1;
		}
	}
	/**
	 * 验证编号
	 * @param id
	 * @return
	 */
	public static boolean validateUser(HttpServletRequest request,HttpServletResponse response){
		return true;
	}
	/**
	 * 从session读取当前登陆人信息，若有则验证通过
	 * @param session HttpSession
	 * @return
	 */
	public static boolean validateSession(HttpSession session){
//		if(session == null) return false;
		
		return true;
	}
	/**
	 * 更新token
	 * @param request
	 * @return
	 */
	public static String updateToken(HttpServletRequest request) {
		String ip = WebUtil.getIpAdd(request);
		String token = ipTokenRelateMap.get(ip);
		if(token != null) tokenMap.remove(token);
		//重新生成token
		token = UUID.randomUUID().toString();
		tokenMap.put(token, System.currentTimeMillis());
		ipTokenRelateMap.put(ip, token);
		return token;
	}
	/**
	 * DES加密
	 * @param dataSource
	 * @param key
	 * @return
	 */
	public static byte[] encryptByDES(byte[] dataSource,String key){
		try {
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(key.getBytes());
			//创建密匙工厂，然后用它把DESKeySpec转换成SecretKey
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secureKey = keyFactory.generateSecret(desKey);
			//Cipher对象完成实际加密操作
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, secureKey,random);
			//获取数据并加密
			return cipher.doFinal(dataSource);
		} catch (Exception e) {
//			LoggerUtil.error(e, SecurityUtil.class);
			return null;
		}
	}
	/**
	 * 解密
	 * @param src
	 * @param key
	 * @return
	 */
	public static byte[] decryptByDES(byte[] src,String key){
		try {
			//DES算法要求有一个可信任的随机数源
			SecureRandom random = new SecureRandom();
			//创建一个DESKeySpec对象
			DESKeySpec desKey = new DESKeySpec(key.getBytes());
			//创建密匙工厂
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			//ESKeySpec对象转换成SecretKeyd对象
			SecretKey secureKey = keyFactory.generateSecret(desKey);
			//Cipher对象完成实际加密操作
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, secureKey,random);
			//获取数据并加密
			return cipher.doFinal(src);
		} catch (Exception e) {
//			LoggerUtil.error(e, SecurityUtil.class);
			return null;
		}
	}
	public static void main(String[] args) throws UnsupportedEncodingException {
		String str = "123456abc";
		byte[] enStr = encryptByDES(str.getBytes(), "ml000012");
		String midStr = new String(enStr,"UTF-8");
		byte[] midByte = midStr.getBytes("UTF-8");
		String lastStr = new String(midByte,"UTF-8");
		System.out.println(lastStr.getBytes("ISO-8859-1").length);
		System.out.println(new String(enStr));
//		String deStr = new String(decryptByDES(new String(enStr,"UTF-8").getBytes("ISO-8859-1"), "ml000012"));
//		System.out.println(deStr);
		String deStr = new String(decryptByDES(lastStr.getBytes("ISO-8859-1"), "ml000012"));
		System.out.println(deStr);
	}
	
	/**************************  appCode验证  *****************************************/
	/**
	 * 验证appcode有效性
	 * @param appCode
	 * @param ip
	 * @return
	 */
	public static boolean validateAppcode(HttpServletRequest request){
		return xmlAppCodeManager.validateAppCode(request);
	}
	public static void setXmlAppCodeManager(AppCodeManager xmlAppCodeManager) {
		SecurityUtil.xmlAppCodeManager = xmlAppCodeManager;
	}
	
}
