package com.mininglamp.currencySys.user.bean;

import java.util.HashMap;
import java.util.Map;

public class MemoryData  {

	private static Map<String, String> sessionIDMap = new HashMap<String,String>();

	public static Map<String, String> getSessionIDMap() {
		return sessionIDMap;
	}

	public static void setSessionIDMap(Map<String, String> sessionIDMap) {
		MemoryData .sessionIDMap = sessionIDMap;
	}

	
}
