package com.mininglamp.currencySys.common.xmlmap.bean;

import java.util.HashMap;
import java.util.Map;

public class ResultMap {
	private String id;

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	private String clazz;

	public ResultMap() {
		resultmaps = new HashMap<String, Prop>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Prop> getResultmaps() {
		return resultmaps;
	}

	public void setResultmaps(Map<String, Prop> resultmaps) {
		this.resultmaps = resultmaps;
	}

	private Map<String, Prop> resultmaps = null;

	public void addMapper(String column, Prop prop) {
		if (column == null || "".equals(column.trim()) || prop == null)
			return;
		resultmaps.put(column, prop);
	}
}
