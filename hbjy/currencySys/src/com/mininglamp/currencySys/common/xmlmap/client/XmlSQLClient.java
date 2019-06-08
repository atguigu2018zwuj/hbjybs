package com.mininglamp.currencySys.common.xmlmap.client;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mininglamp.logx.util.CloseUtil;
import com.mininglamp.currencySys.common.connpool.C3P0ConnentionProvider;
import com.mininglamp.currencySys.common.util.DateUtilz;
import com.mininglamp.currencySys.common.xmlmap.SQLMapParser;
import com.mininglamp.currencySys.common.xmlmap.bean.Prop;
import com.mininglamp.currencySys.common.xmlmap.bean.ResultMap;
import com.mininglamp.currencySys.common.xmlmap.bean.SQLBean;
import com.mininglamp.currencySys.common.xmlmap.bean.SQLCriteria;
import com.mininglamp.currencySys.common.xmlmap.exception.ConfigParseException;
import com.mininglamp.currencySys.common.xmlmap.exception.ORMMappingException;


abstract public class XmlSQLClient {
	
	private Logger logger = Logger.getLogger(XmlSQLClient.class);
	protected SQLMapParser sqlMapParser;
	
	private C3P0ConnentionProvider provider;

	public SQLMapParser getSqlMapParser() {
		return sqlMapParser;
	}

	public void setSqlMapParser(SQLMapParser sqlMapParser) {
		this.sqlMapParser = sqlMapParser;
	}

	abstract protected String additionalPageSQL(String sql, String fromSize, String returnSize);

	@SuppressWarnings("unchecked")
	private String buildSQL(SQLBean bean, Object parameter) throws ConfigParseException {
		if (parameter == null)
			throw new ConfigParseException("传入的对象是空的");

		Class clazz = parameter.getClass();
		// SQLBean bean = sqlMapParser.getConf().getSQLBean(queryId);
		List<SQLCriteria> criterias = bean.getSqlCriterias();
		StringBuffer sqlbuf = new StringBuffer();
		for (SQLCriteria cri : criterias) {
			if ("content".equals(cri.getCriteriaType())) {
				sqlbuf.append(cri.getContent());
			} else if ("if".equals(cri.getCriteriaType())) {
				if (parameter instanceof Map) {
					Map<String, Object> fields = (Map<String, Object>) parameter;
					Object fieldObj = fields.get(cri.getField());
					if ("notnull".equals(cri.getValue())) {
						if (fieldObj != null && !"".equals(fieldObj.toString().replace("'", "").trim())) {
							String finalFieldValue = "";
							if (cri.getContent().contains(" in ") || cri.getContent().contains(" like "))
								finalFieldValue = handleSqlInOrLike(cri.getContent(), fieldObj.toString().trim());
							else
								finalFieldValue = handleSql(cri.getContent(), fieldObj);
							sqlbuf.append(finalFieldValue);
						}
					} else if ("notstring".equals(cri.getValue())) {
						if (fieldObj != null && !"".equals(fieldObj.toString().replace("'", "").trim())) {
							String finalFieldValue = handleSqlInOrLike(cri.getContent(), fieldObj.toString().replace("'", "").trim());
							sqlbuf.append(finalFieldValue);
						}

					} else if ("array".equals(cri.getValue())) {
						if (fieldObj != null && !"".equals(fieldObj)) {
							String finalFieldValue = handleSqlArray(cri.getContent(), fieldObj);
							sqlbuf.append(finalFieldValue);
						}

					}else {
						if (fieldObj != null && cri.getValue().equals(fieldObj.toString().replace("'", "").trim())) {
							sqlbuf.append(cri.getContent());
						}
					}
				} else if (parameter instanceof java.lang.String || parameter instanceof java.lang.Integer || parameter instanceof java.lang.Double
						|| parameter instanceof java.lang.Long) {

					String fieldObj = parameter.toString();
					if ("notnull".equals(cri.getValue())) {
						if (fieldObj != null && !"".equals(fieldObj.replace("'", "").trim())) {
							String finalFieldValue = "";
							if (cri.getContent().contains(" in ") || cri.getContent().contains(" like "))
								finalFieldValue = handleSqlInOrLike(cri.getContent(), fieldObj.trim());
							else
								finalFieldValue = handleSql(cri.getContent(), fieldObj.replace("'", "").trim());
							sqlbuf.append(finalFieldValue);
						}
					} else if ("notstring".equals(cri.getValue())) {
						if (fieldObj != null && !"".equals(fieldObj.trim().replace("'", ""))) {
							String finalFieldValue = handleSqlInOrLike(cri.getContent(), fieldObj.replace("'", "").trim());
							sqlbuf.append(finalFieldValue);
						}

					} else {
						if (fieldObj != null && cri.getValue().equals(fieldObj.trim().replace("'", ""))) {
							sqlbuf.append(cri.getContent());
						}
					}
				} else {
					try {
						System.out.println(cri.getField());
						Field field = clazz.getDeclaredField(cri.getField());
						if (!field.isAccessible())
							field.setAccessible(true);
						Object fieldObj = field.get(parameter);
						if ("notnull".equals(cri.getValue())) {
							if (fieldObj != null && !"".equals(fieldObj.toString().replace("'", "").trim())) {
								String finalFieldValue = "";
								if (cri.getContent().contains(" in ") || cri.getContent().contains(" like "))
									finalFieldValue = handleSqlInOrLike(cri.getContent(), fieldObj.toString().trim());
								else
									finalFieldValue = handleSql(cri.getContent(), fieldObj);
								sqlbuf.append(finalFieldValue);
							}
						} else if ("notstring".equals(cri.getValue())) {
							if (fieldObj != null && !"".equals(fieldObj.toString().replace("'", "").trim())) {
								String finalFieldValue = handleSqlInOrLike(cri.getContent(), fieldObj.toString().replace("'", "").trim());
								sqlbuf.append(finalFieldValue);
							}

						} else {
							if (fieldObj != null && cri.getValue().equals(fieldObj.toString().replace("'", "").trim())) {
								sqlbuf.append(cri.getContent());
							}
						}
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
						throw new ConfigParseException("没有这个属性值: " + cri.getField());
					} catch (SecurityException e) {
						e.printStackTrace();
						throw new ConfigParseException("访问权限控制问题: " + cri.getField());
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						throw new ConfigParseException("非法的参数: " + cri.getField());
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						throw new ConfigParseException("非法的存取操作问题: " + cri.getField());
					}
				}
			}
		}
		return sqlbuf.toString();
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryForMapList(String queryId) throws ConfigParseException, ORMMappingException {
		return queryForMapList(queryId, new HashMap());
	}

	public List<Map<String, Object>> queryForMapList(String queryId, Object parameter) throws ConfigParseException, ORMMappingException {
		String sql = "";

		if (queryId == null)
			throw new ConfigParseException("传入的queryId是空的");
		SQLBean bean = sqlMapParser.getConf().getSQLBean(queryId);
		if(bean == null){
			throw new ORMMappingException("查询没有配置相应的resultmap,queriId: " + queryId);
		}
		sql = buildSQL(bean, parameter);
		logger.info("即将执行的QUERY ID: " + queryId);
		return executeSQL(sql);

	}

	public List<Map<String, Object>> queryForMapList(String queryId, Object parameter, Long fromSize, Integer returnSize) throws ConfigParseException{
		if(fromSize == null||fromSize < 0||returnSize == null||returnSize < 0) return null;
		return queryForMapList(queryId,parameter,String.valueOf(fromSize),String.valueOf(returnSize));
	}
	public List<Map<String, Object>> queryForMapList(String queryId, Object parameter, String fromSize, String returnSize)
			throws ConfigParseException {
		String sql = "";

		if (queryId == null)
			throw new ConfigParseException("传入的queryId是空的");
		SQLBean bean = sqlMapParser.getConf().getSQLBean(queryId);
		String firstSQL = buildSQL(bean, parameter);
		sql = additionalPageSQL(firstSQL, fromSize, returnSize);
		logger.info("即将执行的QUERY ID: " + queryId);
		return executeSQL(sql);

	}

	@SuppressWarnings("unchecked")
	public <T> T queryForList(String queryId) throws ORMMappingException {
		T t = (T) queryForList(queryId, new HashMap());
		return t;
	}

	public Object queryForList(String queryId, Object parameter) throws ORMMappingException {

		try {
			if (queryId == null)
				throw new ConfigParseException("传入的queryId是空的");
			SQLBean bean = sqlMapParser.getConf().getSQLBean(queryId);
			if (bean == null)
				throw new ORMMappingException("Queryid = " + queryId + "的查询没有找到");
			if (bean.getResultMap() == null || "".equals(bean.getResultMap()))
				throw new ORMMappingException("查询没有配置相应的resultmap,queriId: " + queryId);
			if ("Map".equals(bean.getResultMap()) || "java.util.Map".equals(bean.getResultMap()))
				throw new ORMMappingException("返回的对象和类型不相匹配,返回对象类型是java.util.Map");
			ResultMap rmap = sqlMapParser.getConf().getResultMap(bean.getResultMap().trim());
			String firstSQL = buildSQL(bean, parameter);
			System.out.println(firstSQL);
			logger.info("即将执行的QUERY ID: " + queryId);
			if (rmap == null)
				return executeSQL(firstSQL);
			else
				return executeSQL(firstSQL, bean, rmap);
		} catch (ConfigParseException e) {
			logger.error("XmlSQLClient类queryForList方法发生异常！");
			e.printStackTrace();
			return null;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public Object queryForList(String queryId, Object parameter, String fromSize, String returnSize) throws ORMMappingException, ConfigParseException {
		String sql = "";
		if (queryId == null)
			throw new ConfigParseException("传入的queryId是空的");
		SQLBean bean = sqlMapParser.getConf().getSQLBean(queryId);
		if (bean == null)
			throw new ORMMappingException("Queryid = " + queryId + "的查询没有找到");
		if (bean.getResultMap() == null || "".equals(bean.getResultMap()))
			throw new ORMMappingException("查询没有配置相应的resultmap,queriId: " + queryId);
		if ("Map".equals(bean.getResultMap()) || "java.util.Map".equals(bean.getResultMap()))
			throw new ORMMappingException("返回的对象和类型不相匹配,返回对象类型是java.util.Map");
		ResultMap rmap = sqlMapParser.getConf().getResultMap(bean.getResultMap().trim());
		String firstSQL = buildSQL(bean, parameter);
		sql = additionalPageSQL(firstSQL, fromSize, returnSize);
		logger.info("即将执行的SQL: " + queryId + ": " + sql);

		return executeSQL(sql, bean, rmap);

	}

	@SuppressWarnings("unchecked")
	private List<Object> executeSQL(String sql, SQLBean bean, ResultMap rmap) throws ORMMappingException {
		List<Object> res = new ArrayList<Object>();
		Connection connection = null;
		Statement stmt = null;
		ResultSet resultSet = null;
		try {
			connection = provider.getConnection();
			logger.info("获取连接,开始执行......");
			stmt = connection.createStatement();
			logger.info("获取连接,并执行结束......开始处理返回结果");
			resultSet = stmt.executeQuery(sql);
			ResultSetMetaData metadata = resultSet.getMetaData();
			int metadatacount = metadata.getColumnCount();
			if ("String".equals(bean.getResultMap()) || "java.lang.String".equals(bean.getResultMap())) {
				if (resultSet.next())
					res.add(resultSet.getString(1));
			} else if ("Integer".equals(bean.getResultMap()) || "java.lang.Integer".equals(bean.getResultMap())) {
				if (resultSet.next())
					res.add(resultSet.getInt(1));
			} else if ("Long".equals(bean.getResultMap()) || "java.lang.Long".equals(bean.getResultMap())) {
				if (resultSet.next())
					res.add(resultSet.getLong(1));
			} else if ("Double".equals(bean.getResultMap()) || "java.lang.Double".equals(bean.getResultMap())) {
				if (resultSet.next())
					res.add(resultSet.getDouble(1));
			} else {
				Map<String, Prop> mapprops = rmap.getResultmaps();
				String mapclass = rmap.getClazz();
				Class clazz = Class.forName(mapclass);
				
				while (resultSet.next()) {
					Object obj = clazz.newInstance();

					for (int i = 0; i < metadatacount; i++) {
						String columnName = metadata.getColumnLabel(i + 1);
						// System.out.println(columnName);
						Prop prop = mapprops.get(columnName);
						// System.out.println(prop.getProperty());
						PropertyDescriptor pd = new PropertyDescriptor(prop.getProperty(), clazz);
						Method getMethod = pd.getWriteMethod();
						if ("String".equals(prop.getColumnType()) || "java.lang.String".equals(prop.getColumnType()))
							getMethod.invoke(obj, resultSet.getString(columnName));
						else if ("Integer".equals(prop.getColumnType()) || "java.lang.Integer".equals(prop.getColumnType()))
							getMethod.invoke(obj, resultSet.getInt(columnName));
						else if ("Long".equals(prop.getColumnType()) || "java.lang.Long".equals(prop.getColumnType()))
							getMethod.invoke(obj, resultSet.getLong(columnName));
						else if ("Double".equals(prop.getColumnType()) || "java.lang.Double".equals(prop.getColumnType()))
							getMethod.invoke(obj, resultSet.getDouble(columnName));
					}
					res.add(obj);
				}
			}
			logger.info("数据包装完成......");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ORMMappingException("SQL执行错误，请联系管理员");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new ORMMappingException("映射类未找到");
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new ORMMappingException("映射类虽然找到，但是构造函数无法执行");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new ORMMappingException("强行访问私有属性未果");
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new ORMMappingException("参数错误");
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new ORMMappingException("reflect 错误");
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (stmt != null)
					stmt.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				logger.error("XmlSQLClient类executeSQL方法发生异常！");
				e.printStackTrace();
			}
		}

		return res;
	}

	private List<Map<String, Object>> executeSQL(String sql) {
		sql = sql.replaceAll("\\t\\n\\r", " ");
		logger.info("拼好要执行的SQL: " + sql);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Connection connection = null;
		Statement stmt = null;
		ResultSet resultSet = null;
		try {

			connection = provider.getConnection();
			logger.info("获取连接,开始执行......");
			stmt = connection.createStatement();
			resultSet = stmt.executeQuery(sql);
			logger.info("获取连接,并执行结束......");
			ResultSetMetaData metadata = resultSet.getMetaData();
			int totalColumns = metadata.getColumnCount();
			int[] types = new int[totalColumns + 1];
			String[] labelNames = new String[totalColumns + 1];
			buildLabelNamesAndTypes(metadata, labelNames, types);

			while (resultSet.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= totalColumns; i++) {
					Object value;
					if (types[i] < Types.BLOB)
						value = resultSet.getObject(i);
					else if (types[i] == Types.CLOB)
						value = handleClob(resultSet.getClob(i));
					else if (types[i] == Types.NCLOB)
						value = handleClob(resultSet.getNClob(i));
					else if (types[i] == Types.BLOB)
						value = handleBlob(resultSet.getBlob(i));
					else
						value = resultSet.getObject(i);
					map.put(labelNames[i], value);
				}
				list.add(map);
			}
		} catch (SQLException e) {
			logger.error("XmlSQLClient类executeSQL方法发生异常！");
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (stmt != null)
					stmt.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				logger.error("XmlSQLClient类executeSQL方法发生异常！");
				e.printStackTrace();
			}
		}

		return list;

	}

	public Long queryLong(String queryId, Object parameter) throws ConfigParseException, ORMMappingException {
		Map<String, Object> result = this.queryForMap(queryId, parameter);
		if (result == null)
			return null;
		else
			return (Long) result.values().toArray()[0];
	}

	public Map<String, Object> queryForMap(String queryId, Object parameter) throws ConfigParseException, ORMMappingException {
		List<Map<String, Object>> result = this.queryForMapList(queryId, parameter);
		if (result == null || result.size() == 0)
			return null;
		else
			return result.get(0);
	}

	@SuppressWarnings("unchecked")
	public Object queryForObject(String queryId, Object parameter) throws ORMMappingException {
		List<Object> result = (List<Object>) this.queryForList(queryId, parameter);
		if (result == null || result.size() == 0)
			return null;
		else
			return result.get(0);
	}
	/**
	 * 处理sql语句，将select * from t where a=?这种sql和参数拼接为完整sql
	 * 
	 * @param sql
	 *            sql
	 * @param paras
	 *            参数
	 * @return sql
	 */
	private String handleSql(String sql, Object paras) {
		if (paras != null) {

			Object value = null;
			if (paras instanceof Date) {
				value = DateUtilz.FormatDate10((Date) paras);
				sql = sql.replaceAll("\\?", "'" + value + "'");
			}

			else if (paras instanceof Integer) {
				value = paras;
				sql = sql.replaceFirst("\\?", "" + value + "");
			} else if (paras instanceof Long) {
				value = paras;
				sql = sql.replaceFirst("\\?", "" + value + "");
			} else if (paras instanceof Double) {
				value = paras;
				sql = sql.replaceFirst("\\?", "" + value + "");
			} else {
				value = paras.toString().replace("'", "").trim();
				sql = sql.replaceFirst("\\?", "'" + value + "'");
			}

		}

		return sql;
	}

	private String handleSqlInOrLike(String sql, Object paras) {
		if (paras != null) {
			String value = paras.toString();
			sql = sql.replaceAll("\\?", value);
		}

		return sql;
	}
	
	private String handleSqlArray(String sql, Object paras) {
		if (paras != null) {
			String[] paramArr = null;
			if(paras instanceof String){
				paramArr = ((String)paras).split(",");
			}else if(paras instanceof String[]){
				paramArr = (String[]) paras;
			}else{
				return "";
			}
			StringBuilder sb=new StringBuilder("");
			for(int i=0;i<paramArr.length;i++){
				String value = paramArr[i];
				sb.append(sql.replaceAll("\\?", value.replace("'", "").trim()));
			}
			return sb.toString();
		}
		
		return sql;
	}

	public C3P0ConnentionProvider getProvider() {
		return provider;
	}

	public void setProvider(C3P0ConnentionProvider provider) {
		this.provider = provider;
	}

	private static final void buildLabelNamesAndTypes(ResultSetMetaData rsmd, String[] labelNames, int[] types) throws SQLException {
		for (int i = 1; i < labelNames.length; i++) {
			labelNames[i] = rsmd.getColumnLabel(i);
			types[i] = rsmd.getColumnType(i);
		}
	}

	public static String handleClob(Clob clob) throws SQLException {
		if (clob == null)
			return null;

		Reader reader = null;
		try {
			reader = clob.getCharacterStream();
			char[] buffer = new char[(int) clob.length()];
			reader.read(buffer);
			return new String(buffer);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			CloseUtil.closeIO(reader);
		}

	}

	public static byte[] handleBlob(Blob blob) throws SQLException {
		if (blob == null)
			return null;

		InputStream is = null;
		try {
			is = blob.getBinaryStream();
			byte[] data = new byte[(int) blob.length()];
			is.read(data);
			is.close();
			return data;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			CloseUtil.closeIO(is);
		}

	}
}
