package com.mininglamp.currencySys.util;

public class HashUtil {
	public static int addhash(String args) {
		byte[] bys = args.getBytes();
		int r = 0;
		for (int i = 0; i < bys.length; i++) {
			r = r * 31 + bys[i];
		}
		return r;
	}
	
	public static void main(String args[]) {
		System.out.println("130632197710201531".hashCode() % 200);
		System.out.println(Math.abs(HashUtil.addhash("130632197710201531") % 200));
	}
}
