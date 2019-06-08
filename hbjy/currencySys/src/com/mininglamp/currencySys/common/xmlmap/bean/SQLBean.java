package com.mininglamp.currencySys.common.xmlmap.bean;

import java.util.ArrayList;
import java.util.List;

public class SQLBean {
	private String queryType;
	private String sqlid;
	private List<SQLCriteria> sqlCriterias;

	public SQLBean() {
		super();
		// TODO Auto-generated constructor stub
		sqlCriterias = new ArrayList<SQLCriteria>();

	}

	public List<SQLCriteria> getSqlCriterias() {
		return sqlCriterias;
	}

	public void setSqlCriterias(List<SQLCriteria> sqlCriterias) {
		this.sqlCriterias = sqlCriterias;
	}

	private String resultMap;

	public String getResultMap() {
		return resultMap;
	}

	public void setResultMap(String resultMap) {
		this.resultMap = resultMap;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getSqlid() {
		return sqlid;
	}

	public void setSqlid(String sqlid) {
		this.sqlid = sqlid;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	private String paramType;
}
