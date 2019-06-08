package com.mininglamp.currencySys.common.util;

import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 安全类，加解密
 * 
 * @author zhangxiaobo
 * 
 */
public class Securitys {
	public static final Logger LOGGER = Logger.getLogger(Securitys.class
			.getName());
	public static final String HEX = "0123456789abcdef";
	public static final String CHARACTER = "utf8";

	public static class Aes {

		public static final String Algorithm = "AES";

		/**
		 * 加密
		 * 
		 * @param content
		 * @param password
		 * @return
		 */
		public static byte[] encrypt(String content, String password) {
			try {
				KeyGenerator kgen = KeyGenerator.getInstance(Algorithm);
				kgen.init(128, new SecureRandom(password.getBytes()));
				SecretKey secretKey = kgen.generateKey();
				byte[] enCodeFormat = secretKey.getEncoded();
				SecretKeySpec key = new SecretKeySpec(enCodeFormat, Algorithm);
				Cipher cipher = Cipher.getInstance(Algorithm);// 创建密码器
				byte[] byteContent = content.getBytes(CHARACTER);
				cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
				byte[] result = cipher.doFinal(byteContent);
				return result;
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "encrypt exception", e);
			}
			return null;
		}

		/**
		 * 解密
		 * 
		 * @param content
		 * @param password
		 * @return
		 */
		public static String decrypt(byte[] content, String password) {
			try {
				KeyGenerator kgen = KeyGenerator.getInstance(Algorithm);
				kgen.init(128, new SecureRandom(password.getBytes()));
				SecretKey secretKey = kgen.generateKey();
				byte[] enCodeFormat = secretKey.getEncoded();
				SecretKeySpec key = new SecretKeySpec(enCodeFormat, Algorithm);
				Cipher cipher = Cipher.getInstance(Algorithm);// 创建密码器
				cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
				byte[] result = cipher.doFinal(content);
				return new String(result, CHARACTER);
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "decrypt exception", e);
			}
			return null;
		}

		public static void test() {
			String content = "http://21.32.3.70:80/hbpost/bho/log/access.action?url=http://21.6.11.6/easyloan&time=20151210105700&ip=21.32.95.154&gateway=20.32.95.2&mac=54EE7541678E&os=XPSP3&user=Administrator&ie=ie7&bho=v1.0&_=1449716422548";
			String password = "123456";
			// 加密
			System.out.println("加密前：" + content);
			System.out.println("长度：" + content.length());
			byte[] encryptResult = encrypt(content, password);
			String hex = bytesToHexString(encryptResult);
			System.out.println("加密后：" + hex);
			System.out.println("长度：" + hex.length());
			// 解密
			String decryptResult = decrypt(hexStringToBytes(hex), password);
			System.out.println("解密后：" + decryptResult);
			System.out.println("长度：" + decryptResult.length());
		}

	}

	/**
	 * Convert byte[] to hex
	 * string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	 * 
	 * @param src
	 *            byte[] data
	 * @return hex string
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * Convert hex string to byte[]
	 * 
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		// return Byte.valueOf(c+"", 16);
		return (byte) HEX.indexOf(c);
	}

	public static void main(String[] args) {
//		Aes.test();
		String[] ass=new String[]{"123"};
		if(ass instanceof Object[]){
			System.out.println("ll");
		}
	}
}
