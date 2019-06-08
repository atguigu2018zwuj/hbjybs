package com.mininglamp.currencySys.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.mininglamp.currencySys.uploadAndDownload.service.impl.UploadAndAownloadServiceImp;

public class JDBCUtils {
	
	static SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
	static SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static String driverName = null; // 驱动
	private static String url = null;
	private static String userName = null;
	private static String pwd = null;

	// 获取配置信息
	public static void getUserAndPass() {
		try {
			Properties prop = new Properties();
			// Class.getResourceAsStream("name") 会指定要加载的资源路径与当前类所在包的路径一致
			// 如果这个name是以 '/' 开头的，那么就会从classpath的根路径下开始查找。
			// getClassLoader().getResourceAsStream("name") 无论要查找的资源前面是否带'/'
			// 都会从classpath的根路径下查找。
			prop.load(UploadAndAownloadServiceImp.class.getClassLoader().getResourceAsStream("ibatis/jdbc.properties"));
			driverName = prop.getProperty("oracle.connection.driverClass");
			url = prop.getProperty("oracle.connection.jdbcUrl");
			userName = prop.getProperty("oracle.connection.username");
			pwd = prop.getProperty("oracle.connection.password");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	//注册驱动
	public static Connection getMysqlConnection() {
		Connection connection = null;
		getUserAndPass();
		try {
			Class.forName(driverName);
			connection = DriverManager.getConnection(url, userName, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * 获取list数据条数
	 * 
	 * @param sql
	 * @return
	 */
	public static int getDataCount(String sql) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Connection connection = null;
		Statement st = null;
		ResultSet rs = null;
		int int1 = 0;
		try {
			connection = getMysqlConnection();
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			rs.next();
			int1 = (int) rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(rs, st, connection);
		}
		return int1;
	}

	/**
	 * 获取list结果集
	 * 
	 * @param sql
	 * @return
	 */
	public static List<Map<String, Object>> findListBySql(String sql) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Connection connection = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			connection = getMysqlConnection();
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount();
			while (rs.next()) {
				Map<String, Object> rowData = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					rowData.put(md.getColumnName(i), rs.getObject(i));
				}
				list.add(rowData);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(rs, st, connection);
		}
		return list;
	}

	/**
	 * 获取list结果集
	 * 
	 * @param sql
	 * @return
	 */
	public static ResultSet findResultSetBySql(String sql) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Connection connection = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			connection = getMysqlConnection();
			st = connection.createStatement();
			rs = st.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (st != null) {
					st.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rs;
	}

	public static List<Map> findListBySql1(String sql) {
		List<Map> list = new ArrayList<Map>();
		Connection connection = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			connection = getMysqlConnection();
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount();
			while (rs.next()) {
				Map<String, Object> rowData = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					rowData.put(md.getColumnName(i), rs.getObject(i));
				}
				list.add(rowData);
				rowData = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(rs, st, connection);
		}
		return list;
	}

	/**
	 * 获取map结果集
	 * 
	 * @param sql
	 * @return
	 */
	public static Map<String, Object> findMapBySql(String sql) {
		Map<String, Object> map = new HashMap<>();
		Connection connection = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			connection = getMysqlConnection();
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					map.put(md.getColumnName(i), rs.getObject(i));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(rs, st, connection);
		}
		return map;
	}

	/**
	 * 关闭资源
	 * @param rs
	 * @param st
	 * @param connection
	 */
	public static void closeResources(ResultSet rs, Statement st, Connection connection) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (st != null) {
				st.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	    
	/**
	 * 在开发过程，SQL语句有可能写错，如果能把运行时出错的 SQL 语句直接打印出来，那对排错非常方便，因为其可以直接拷贝到数据库客户端进行调试。
	 * 
	 * @param sql
	 *            SQL 语句，可以带有 ? 的占位符
	 * @param params
	 *            插入到 SQL 中的参数，可单个可多个可不填
	 * @return 实际 sql 语句
	 */
	public static String printRealSql(String sql, Object[] params) {
		if (params == null || params.length == 0) {
			LoggerUtil.info("The SQL is------------>\n" + sql);
			return sql;
		}

		if (!match(sql, params)) {
			LoggerUtil.info("SQL 语句中的占位符与参数个数不匹配。SQL：" + sql);
			return null;
		}

		int cols = params.length;
		Object[] values = new Object[cols];
		System.arraycopy(params, 0, values, 0, cols);

		for (int i = 0; i < cols; i++) {
			Object value = values[i];
			if (value instanceof Date) {
				values[i] = "'" + value + "'";
			} else if (value instanceof String) {
				values[i] = "'" + value + "'";
			} else if (value instanceof Boolean) {
				values[i] = (Boolean) value ? 1 : 0;
			}
		}

		String statement = String.format(sql.replaceAll("\\?", "%s"), values);

		LoggerUtil.info("The SQL is------------>\n" + statement);

		// ConnectionMgr.addSql(statement); // 用来保存日志

		return statement;
	}

	/**
	 * ? 和参数的实际个数是否匹配
	 * 
	 * @param sql
	 *            SQL 语句，可以带有 ? 的占位符
	 * @param params
	 *            插入到 SQL 中的参数，可单个可多个可不填
	 * @return true 表示为 ? 和参数的实际个数匹配
	 */
	public static boolean match(String sql, Object[] params) {
		if (params == null || params.length == 0)
			return true; // 没有参数，完整输出
		Matcher m = Pattern.compile("(\\?)").matcher(sql);
		int count = 0;
		while (m.find()) {
			count++;
		}
		return count == params.length;
	}
	
	/**
	 * delete 拼接sql 获取参数的list
	 * jdbc多个不确定参数
	 * @param paramList 参数
	 * @param delList 放参数的list
	 * @param delSql sql语句
	 * @param list_TYPE 
	 * @param paramListType  参数类型
	 * @throws ParseException
	 * delete from ASSET_MAN_PRODUCT_ZSYL t where SJRQ = '2018-02-01' and JRJGBM like '%5%' 
	 */
	public static void addStatement(List<Map> paramList, List<Object> delList, StringBuffer delSql, List<Object> list_TYPE, List<Map> paramListType) throws ParseException{
		if(paramList != null && paramList.size() > 0){
			delSql.append(" t where ");
			for (Map<String,Object> map : paramList) {
				for (String s:map.keySet()) {
					System.out.print("key:"+s+"\t");
					System.out.println("value:"+map.get(s));
					if(map.get(s) != null && !"null".equals(map.get(s)) && map.get(s).toString().length() > 0){
						String paramListTypeValue = (String) paramListType.get(0).get(s);
						if("date".equals(paramListTypeValue)){
							delSql.append(s+" = ? and ");
							delList.add(new java.sql.Date(fmt.parse(map.get(s).toString().trim()).getTime()));
						}else if("time".startsWith(paramListTypeValue)){
							delSql.append(s+" = ? and ");
							delList.add(new java.sql.Date(f.parse(map.get(s).toString().trim()).getTime()));
						}else if("inString".startsWith(paramListTypeValue)){
							delSql.append(s+" in ("+map.get(s).toString().trim()+") and ");
						}/*else if("c".startsWith(paramListTypeValue)){
					   }else if("n".startsWith(paramListTypeValue)){
					   }*/else{
						   delSql.append(s+" like ? and ");
						   delList.add("%"+map.get(s).toString().trim()+"%");
					   }
					}
				}
			}
			delSql = delSql.delete(delSql.length() - 4, delSql.length());
		}else{
			delSql.append(" t where 1=1");
		}
		//sql语句输出
		JDBCUtils.printRealSql(delSql.toString(), null);
	}
	
	/**
	 * delete 
	 * 向PreparedStatement中set参数
	 * @param ps PreparedStatement
	 * @param delList 参数的list
	 * @throws SQLException
	 */
	public static void pstSetObject(PreparedStatement ps,List<Object> delList) throws SQLException{
		if(delList != null){
			for(int i = 0; i < delList.size(); i++){
				//+1 是因为有一个确定的参数，如果都为动态不确定的参数，可以多判断一下
				ps.setObject(i + 1, delList.get(i));
			}
		}
	}
	
	/**
	 * insert 拼接sql语句
	 * jdbc多个不确定参数
	 * @param excelList 参数(包含所有要插入的数据值)
	 * @param insList 放参数的list
	 * @param delSql sql语句
	 * @param list_TYPE 
	 * @throws ParseException
	 * INSERT into ASSET_MAN_PRODUCT_ZSYL(CRJGZJDM,NBJGH,JYZHMC,CRJGLX,SJRQ,JYSJ,JCZCLX,JCZCYSXYJEZRMB,LSH,SYQZRJEZRMB,JYDSZHMC,BZ,JCZCYSXYJE,JYZHKHHH,JYRQ,JYDSZHKHHMC,JYZHKHHMC,CRJGZJLX,JYDSZHKHHH,JYFX,SYQZRJE,CRJGJCZCHGBS,CPDM,JYZHH,JYDSZHH,ZCSYQBM,JRJGBM,CRJGJCZCCBBS,CODE) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
	 */
	public static void insertStatement(List<Map> excelList, List<Object> insList, StringBuffer sql, List<Object> list_TYPE) throws ParseException{
		int num = 0;
		for (Map<String,Object> map : excelList) {
			sql.append("(");
			for (String s:map.keySet()) {
                   System.out.print("key:"+s+"\t");
                   System.out.println("value:"+map.get(s));
//                   if(map.get(s) != null && !"null".equals(map.get(s)) && map.get(s).toString().length() > 0){
                	   num ++;	
                	   insList.add(s);
                	   sql.append(s+",");
//                   }
            }
			sql = sql.delete(sql.length() - 1, sql.length());
			sql.append(") values (");
			for (int i = 0; i < num; i++) {
				if(i == (num -1)){
					sql.append("?,");
				}else{
					if("time".equals(list_TYPE.get(i))){
						sql.append("to_date(?,'yyyy-mm-dd hh24:mi:ss'),");
					}else{
						sql.append("?,");
					}
				}
			}
			sql = sql.delete(sql.length() - 1, sql.length());
			sql.append(")");
			//sql语句输出
			JDBCUtils.printRealSql(sql.toString(), null);
			break;
		}
	}
	
	/**
	 * insert
	 * 批量增加数据（每3000条数据主动提交一次）
	 * 向PreparedStatement中批量set参数
	 * @param ps PreparedStatement
	 * @param con 
	 * @param zsyl 
	 * @param excelList 要添加的数据 List<Map>
	 * @param list_TYPEList 
	 * @param List<Map> filed
	 * @throws Exception 
	 */
	public static void psSetObject(PreparedStatement ps,List<Object> insList, int[] zstl, Connection con, List<Map> excelList, List<Object> list_TYPEList) throws Exception{
		for (int i = 0; i < excelList.size(); i++) {
			Map excelParam = excelList.get(i);
			psSetObjectOne(ps, insList, excelParam, list_TYPEList);
			ps.addBatch();
			if (i % 3000 == 0 && i != 0) {
				zstl = ps.executeBatch();
				ps.clearBatch();
				con.commit();
			}
		}
	}
	
	/**
	 * insert
	 * 批量增加数据，并获取所有新增数据的主键值(code)
	 * 向PreparedStatement中批量set参数并执行（主动提交）
	 * @param ps PreparedStatement
	 * @param insertSql 新增数据的sql
	 * @param tableName 标识报表的4位标识码
	 * @param insList 字段集合
	 * @param con 数据库连接
	 * @param excelList 要添加的数据集合
	 * @param list_TYPEList 字段类型集合
	 * @return result 每一条输入插入语句执行后的结果值（格式：影响条数_主键值）
	 * @throws Exception 
	 */
	public static List<String> insertDataBatch(PreparedStatement ps, String insertSql, String tableName, List<Object> insList, Connection con, List<Map> excelList, List<Object> list_TYPEList) throws Exception{
		//所有新增数据的主键值
		List<String> result = new ArrayList<String>();
		// 不自动提交
		con.setAutoCommit(false);
		ResultSet resultSet = null;
		
		for (int i = 0; i < excelList.size(); i++) {
			Map excelParam = excelList.get(i);//要添加的数据
			// 插入数据
			ps = con.prepareStatement(insertSql);
			psSetObjectOne(ps, insList, excelParam, list_TYPEList);
			int count = ps.executeUpdate();
			if (count > 0 && !"jgxx".equals(tableName)) {
				// 获取新增数据的主键值(code)
				ps = con.prepareStatement("select "+StringUtils.upperCase(tableName)+"_CODE.currval CURRVAL from dual");
				resultSet = ps.executeQuery();
				if (resultSet.next()) {
					result.add(count+"_"+resultSet.getInt("CURRVAL"));
				} else {
					result.add(count+"_"+0);
				}
			} else if (count > 0 && "jgxx".equals(tableName)) {
				// HBJYODS.JRJGXX没有code列
				result.add(count+"_0");
			} else {
				result.add("0_0");
			}
		}
		
		if (resultSet != null) {
			resultSet.close();
		}
		con.commit();
		return result;
	}
	
	/**
	 * insert
	 * 增加数据
	 * 向PreparedStatement中set参数
	 * @param ps PreparedStatement
	 * @param insList 字段集合
	 * @param excelParam 要添加的数据
	 * @param list_TYPEList 字段类型集合
	 * @throws Exception
	 */
	public static void psSetObjectOne(PreparedStatement ps,List<Object> insList, Map excelParam, List<Object> list_TYPEList) throws Exception{
		if(insList != null && insList.size() > 0 && excelParam != null && !excelParam.isEmpty()){
			for (int j = 0; j < insList.size(); j++) {
				String colValue = (String) excelParam.get(insList.get(j));
				if(list_TYPEList.size() > j){
					String FIELD_TYPE = (String) list_TYPEList.get(j);
					if("date".equals(FIELD_TYPE)){
						if(colValue == null || colValue == "" || colValue.length() <= 0 || "null".equals(colValue)){
							ps.setNull(j+1,java.sql.Types.DATE);
						}else{
							ps.setDate(j+1, new java.sql.Date(fmt.parse((String) colValue).getTime()));
						}
					}else if("time".startsWith(FIELD_TYPE)){
						if(colValue == null || colValue == "" || colValue.length() <= 0 || "null".equals(colValue)){
							ps.setNull(j+1,java.sql.Types.TIME);
						}else{
							Long astDayOfMonth = DateUtils.astDayOfMonth(f.parse((String) colValue));
							Date format = ymdhms.parse("1970-01-01 00:00:00");
							ps.setString(j+1, ymdhms.format(astDayOfMonth + format.getTime()));
//							ps.setDate(j+1, new java.sql.Date(f.parse((String) colValue).getTime()));
						}
					}else if("c".startsWith(FIELD_TYPE)){
						if(colValue == null || colValue == "" || colValue.length() <= 0 || "null".equals(colValue)){
							ps.setNull(j+1,java.sql.Types.VARCHAR);
						}else{
							ps.setString(j+1, (String) colValue);
						}
					}else if("n".startsWith(FIELD_TYPE)){
						if(colValue == null || colValue == "" || colValue.length() <= 0 || "null".equals(colValue)){
							ps.setNull(j+1,java.sql.Types.NUMERIC);
						}else{
							ps.setDouble(j+1, Double.parseDouble((String) colValue));
						}
					}else{
						if(colValue == null || colValue == "" || colValue.length() <= 0 || "null".equals(colValue)){
							ps.setNull(j+1,java.sql.Types.VARCHAR);
						}else{
							ps.setString(j+1, (String) colValue);
						}
					}
				}else{
					if(colValue == null || colValue == "" || colValue.length() <= 0 || "null".equals(colValue)){
						ps.setNull(j+1,java.sql.Types.VARCHAR);
					}else{
						ps.setString(j+1, (String) colValue);
					}
				}
			}
		}
	}
}
