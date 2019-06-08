package com.mininglamp.currencySys.uploadAndDownload.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mininglamp.currencySys.ManaPro.service.ManaProService;
import com.mininglamp.currencySys.code.service.CodeMapService;
import com.mininglamp.currencySys.common.util.StringUtils;
import com.mininglamp.currencySys.generateMessage.service.GenerateMessageService;
import com.mininglamp.currencySys.insertData.service.InsertDataService;
import com.mininglamp.currencySys.uploadAndDownload.dao.UploadAndAownloadDao;
import com.mininglamp.currencySys.uploadAndDownload.service.UploadAndAownloadService;
import com.mininglamp.currencySys.util.JDBCUtils;
import com.mininglamp.currencySys.util.CheckProgram.CheckReadExcel;

@Service(value="testService")
public class UploadAndAownloadServiceImp implements UploadAndAownloadService{
	private static final Logger logger = LoggerFactory.getLogger(UploadAndAownloadServiceImp.class);
	/** 码表键字段名称（所有码表的键均为该名称） */
	private static final String CODE_TABLE_KEY_FIELD = "CODE_KEY";

	@Autowired
	public  UploadAndAownloadDao testDao;
	@Autowired
	private CodeMapService codeMapService;
	@Autowired
	private GenerateMessageService generateMessageService;
	@Autowired
	private InsertDataService insertDataService;
	@Autowired
	private ManaProService manaProService;
	
	@Override
	public List<Map> getDataList(String sqlId, Map<String, Object> params) {
		return testDao.getDataList(sqlId, params);
	}

	/**
	 * saveTool存放的是 Excel数据 或者是 错误日志
	 */
	@Override
	public StringBuffer saveData(Map<String, Object> params, String path,MultipartFile reportDataFile,HttpServletRequest request) {
		//String savelog = "-2";
		StringBuffer savelog = new StringBuffer();
		if(params.size() > 0){
			if(path == null || path == "" || path.isEmpty()){
				return savelog.append("文件路径为空，请核查！！！");
			}else{
				long start = System.currentTimeMillis();
				String nbjgh = (String) request.getSession().getAttribute("nbjgh");
				params.put("nbjgh", nbjgh);
				List<Map> dataList = testDao.getDataList("getAllAuth", params);
				List<Map> userAuthorityInfo = null;
				try {
					params.put("codeKey", params.get("tableNames"));
					userAuthorityInfo = codeMapService.getDataList("getUserAuthorityInfo", params);
				} catch (Exception e1) {
					logger.error(e1.getMessage(),e1);
				}
				if (userAuthorityInfo != null && !userAuthorityInfo.isEmpty()) {
					params.put("userAuthorityInfo", userAuthorityInfo.get(0));
				}
				// 取得所有配置表中用到的码表的key（除表格jgxx用到的外）
				Map<String,List<String>> dbCodeTableKeys = null;
				try {
					List<Map> dbCodeTableNamesData = codeMapService.getDataList("getDbCodeTableNames", params);
					// 配置表中除jgxx外所有表用到的码表名称
					List<String> dbCodeTableNames = new ArrayList<String>();
					for (Map dbCodeTableNamesDataOne : dbCodeTableNamesData) {
						if (StringUtils.isNotEmpty(dbCodeTableNamesDataOne.get("CONF_TABLE_NAME"))) dbCodeTableNames.add(StringUtils.parseString(dbCodeTableNamesDataOne.get("CONF_TABLE_NAME")));
					}
					dbCodeTableKeys = new HashMap<String,List<String>>();
					Map<String,Object> searchParams = new HashMap<String,Object>();
					 
					for (String dbCodeTableName : dbCodeTableNames) {
						// 排除金融机构编码
						if ("special_finf".equals(dbCodeTableName)) {
							continue;
						}
						searchParams.put("tableName", dbCodeTableName);
						searchParams.put("tableFields", CODE_TABLE_KEY_FIELD);
						List<String> configColumnValueList = new ArrayList<String>(); 
						List<Map> configColumnValueDatas = generateMessageService.getDataList("selectDatasFromCustomTable", searchParams);
						for (Map configColumnValueData : configColumnValueDatas) {
							if (StringUtils.isNotEmpty(configColumnValueData.get(CODE_TABLE_KEY_FIELD))) configColumnValueList.add(StringUtils.parseString(configColumnValueData.get(CODE_TABLE_KEY_FIELD)));
						}
						dbCodeTableKeys.put(dbCodeTableName, configColumnValueList);
					}
				} catch (Exception e1) {
					logger.error(e1.getMessage(),e1);
					return savelog.append("参数异常！！！");
				}
				if (dbCodeTableKeys != null && !dbCodeTableKeys.isEmpty()) {
					params.put("dbCodeTableKeys", dbCodeTableKeys);
				}
				
				HashMap<String, Object> saveTool = CheckReadExcel.uploadTool(path,params,reportDataFile,dataList,request,manaProService);
				params.remove("nbjgh");
				long end = System.currentTimeMillis();
				System.out.println("获取Excel数据、验证共用"+(end-start)+"毫秒");
				if(saveTool != null && saveTool.size() > 0){
					//获取Excel中不符合校验的日志
					@SuppressWarnings("unchecked")
					List<Map> object = (List<Map>) saveTool.get("errorList");
					//判断错误日志的list为否为空 
					if(!object.isEmpty() && object != null){
						savelog = (StringBuffer) object.get(0).get("errorlog");
					}else{
						//获取Excel日志
						List<Map> ExcelList = (List<Map>) saveTool.get("ExcelList");
						params.put("saveTool", ExcelList);
						try {
							//参数
							List<Map> paramList = (List<Map>) saveTool.get("paramList");
							//参数类型
							List<Map> paramListType = (List<Map>) saveTool.get("paramListType");
							if(!paramList.isEmpty() && paramList != null){
								String pl = savePLData(ExcelList,params,paramList,paramListType,request);
								if(pl != null && pl.length() > 0 && pl.startsWith("导入成功")){
									savelog.append(pl);
								}else{
									savelog.append(pl);
								}
							}else{
								String tableNames = (String) params.get("tableNames");
								if("jgxx".equals(tableNames)){
									String pl = savePLData(ExcelList,params,paramList,paramListType,request);
									if(pl != null && pl.length() > 0 && pl.startsWith("导入成功")){
										savelog.append(pl);
									}else{
										savelog.append(pl);
									}
								}else{
									return savelog.append("参数异常！！！");
								}
							}
						} catch (Exception e) {
							System.out.println(e+"在Up..Impl的79行");
							return savelog.append(e);
						}
					}
				}
			}
		}
		return savelog;
	}

	@Override
	public int deleteData(String sqlId, Map<String, Object> params) {
		int deleteData = 0;
		if(params.size() > 0){
			deleteData = testDao.deleteData(sqlId, params);
		}
		return deleteData;
	}

	@Override
	public int updateData(String sqlId, Map<String, Object> params) {
		int updateData = 0;
		if(params.size() > 0){
			try {
				updateData = testDao.updateData(sqlId, params);
			} catch (Exception e) {
				return updateData;
			}
		}
		return updateData;
	}
	
	/**
	 * 批量操作数据  删除，新增
	 * @param excelList
	 * @param params
	 * @param paramList 
	 * @param paramListType 
	 * @return
	 */
	public String savePLData(List<Map> excelList, Map<String, Object> params, List<Map> paramList, List<Map> paramListType,HttpServletRequest request){
		String nbjgh = (String) request.getSession().getAttribute("nbjgh");
		String username = (String) request.getSession().getAttribute("username");
		int[] executeBatch = null;
		String DRVIER = null;
		String URL = null;
		String USERNAMR = null;
		String PASSWORD = null;
		String provincial_users= null;
		try {
			Properties prop = new Properties();
			//Class.getResourceAsStream("name") 会指定要加载的资源路径与当前类所在包的路径一致 如果这个name是以 '/' 开头的，那么就会从classpath的根路径下开始查找。
			//getClassLoader().getResourceAsStream("name")  无论要查找的资源前面是否带'/' 都会从classpath的根路径下查找。
			prop.load(UploadAndAownloadServiceImp.class.getClassLoader().getResourceAsStream("ibatis/jdbc.properties"));
			DRVIER = prop.getProperty("oracle.connection.driverClass");
			URL = prop.getProperty("oracle.connection.jdbcUrl");
			USERNAMR = prop.getProperty("oracle.connection.username");
			PASSWORD = prop.getProperty("oracle.connection.password");
			provincial_users = prop.getProperty("provincial.users");
		} catch (Exception e) {
			System.out.println(e);
			return "数据库连接异常！！！";
		}
		Connection con = null;
	    ResultSet rt = null;
	    PreparedStatement ps = null;
		PreparedStatement ps1 = null;
	    long start;
	    try {
	    	start = System.currentTimeMillis();
			// 加载驱动
			Class.forName(DRVIER);
			con = DriverManager.getConnection(URL, USERNAMR, PASSWORD);
			con.setAutoCommit(false);
			String tableNames = (String) params.get("tableNames");
			//根据文件名前四位 tableNames 来判断是那张表 然后进行删除，增加
			if(tableNames.length() > 0 && tableNames != null){
				try {
					//表字段类型
					List<Object> list_TYPE = new ArrayList<Object>();
					//获取校验表中数据
					List<Map> dataList = (List<Map>) params.get("check_rule_config");
					for (int i = 0; i < dataList.size(); i++) {
						String FIELD_TYPE = (String) dataList.get(i).get("FIELD_TYPE");
						list_TYPE.add(FIELD_TYPE);
					}
					List<Map> list_TYPEList = (List<Map>) params.get("list_TYPEList");
					//获取全表名
					String tname = (String) params.get("tname");
					if(tname.length() > 0 && tname != null){
						//增加
						StringBuffer sql = new StringBuffer().append("INSERT into ");
						sql.append(tname);
						//删除
						StringBuffer delSql = new StringBuffer().append("delete from ");
						delSql.append(tname);
						//放 delete语句 where后面的 ‘？’ 的值
						List<Object> delList = new ArrayList<Object>();
						//放insert语句 表中的字段名
						List<Object> insList = new ArrayList<Object>();
						if(paramList.size() > 0){
							//删除 拼接参数
							if(paramList != null && !paramList.isEmpty()){
								JDBCUtils.addStatement(paramList,delList, delSql,list_TYPE,paramListType);		
								List<Map> Table_nbjgh  = (List<Map>) params.get("Table_nbjgh");
								if(!"admin".equals(username)){
									for (Map map : Table_nbjgh) {
										//当前表的内部机构号  字段
										String tablenbjgh = (String) map.get("TABLE_NBJGH");
										if(tablenbjgh != null && !"null".equals(tablenbjgh) && tablenbjgh.length() > 0){
											delSql.append(" and "+tablenbjgh+" in (select BR_NO from HBJYODS.JRJGXX m start with m.BR_NO = "+nbjgh+" connect by m.SJGLJGNBJGH = prior m.BR_NO )");
											break;
										}
									}
								}
								// 校验code列是否还存在
								if (paramList != null && !paramList.isEmpty() && paramList.get(0).containsKey("code") && StringUtils.isNotEmpty(paramList.get(0).get("code"))) {
									String selectCountByCode = delSql.toString().replace("delete", "select count(1)");
									ps1 = con.prepareStatement(selectCountByCode);
									JDBCUtils.pstSetObject(ps1, delList);
									rt = ps1.executeQuery();
									int count = 0;
									if (rt.next()) {
										count = rt.getInt(1);
									}
									if (count != paramList.get(0).get("code").toString().split(",").length) {
										return "当前文件中参数sheet中的编码列的值已过期，请重新导出！";
									}
								}
								JDBCUtils.printRealSql(delSql.toString(), null);
								ps1 = con.prepareStatement(delSql.toString());
								JDBCUtils.pstSetObject(ps1, delList);
								int executeUpdate = ps1.executeUpdate();
								System.out.println("总共删除"+executeUpdate+"条数据！！");
							}
						}else{
							//判断是否为机构信息 是就放行
							if("jgxx".equals((String) params.get("tableNames"))){
								JDBCUtils.addStatement(paramList,delList, delSql,list_TYPE,paramListType);		
								ps1 = con.prepareStatement(delSql.toString());
								JDBCUtils.pstSetObject(ps1, delList);
								ps1.execute();
							}else{
								return "Excel参数sheet异常！！！";
							}
						}
						//插入 拼接参数
						JDBCUtils.insertStatement(excelList,insList, sql,list_TYPE);	
						// 批量执行（无法取得新增数据的主键）
//						ps = con.prepareStatement(sql.toString());
//						JDBCUtils.psSetObject(ps, insList,executeBatch,con,excelList,list_TYPE);
//						executeBatch = ps.executeBatch();
//						con.commit();
						// 分条执行（能够取得每一条新增数据的主键）
						executeBatch = new int[excelList.size()];// 执行结果（下方代码要用）
						List<String> rsults = JDBCUtils.insertDataBatch(ps,sql.toString(),(String)params.get("tableNames"), insList, con, excelList, list_TYPE);
						// 记录操作日志
						for (int resultIdx = 0; resultIdx < (rsults != null ? rsults.size() : 0); resultIdx++) {
							String[] result = (StringUtils.isNotEmpty(rsults.get(resultIdx)) ? rsults.get(resultIdx) : "0_0").split("_");
							if (Integer.parseInt(result[0]) > 0) {
								executeBatch[resultIdx] = Integer.parseInt(result[0]);
								if (!"0".equals(result[1])) {
									// 新增成功
									insertDataService.insertRecordOperationLog(
											(String)params.get("tableNames"), 
											Integer.parseInt(result[1]), 
											username, "1");
								}
							}
						}
					}else{
						return "表名无效！！！";
					}
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
					con.rollback();
					executeBatch = null;
					return e+"";
				}finally {
					if (ps != null) {
						try {
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						}
					}
					if (con != null) {
						try {
							con.close();
						} catch (SQLException e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						}
					}
				}
			}else{
				return "文件名中四位标识和本业务四位标识不符！！！";
			}
			long end = System.currentTimeMillis();
		    System.out.println("delete,insert共用"+(end-start)+"毫秒");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	    if(executeBatch != null && executeBatch.length > 0){
//	    	tableSjrq=2018-05-02, tname=SPECIAL_PRSN
	    	SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	if("admin".equals(username)){
	    		params.put("nbjgh", provincial_users);
	    	}else{
	    		params.put("nbjgh", nbjgh);
	    	}
	    	params.put("username", username);
	    	params.put("ymdhms", fmt.format(new Date()));
	    	params.put("content", "导入成功  ,共"+excelList.size()+"条数据");
	    	int insertData = testDao.insertData("insertBSGL",params);
	    	
//	    	// 更新打回报表的完成时间（不在此执行，需要等用户确认完成后再完成代办事项）
//	    	Map<String,Object> repulseReportParam = new HashMap<>();
//	    	repulseReportParam.put("sjrq", StringUtils.parseString(params.get("tableSjrq")));
//	    	String tname = StringUtils.parseString(params.get("tname"));
//	    	// 业务编码取表名后四位
//	    	repulseReportParam.put("ywbm", StringUtils.isNotEmpty(tname) ? tname.substring(tname.length() - 4).toLowerCase() : "");
//	    	if (generateMessageService.updateData(repulseReportParam, "updateRepulseReportFinishDate") <= 0) {
//	    		logger.info("数据日【"+repulseReportParam.get("sjrq")+"】的业务【"+repulseReportParam.get("ywbm")+"】无打回信息");
//	    	}
	    	
	    	params = null;
	    	return "导入成功  ,共"+excelList.size()+"条数据";
	    }else{
	    	try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
				return e+"";
			}
	    	return "异常，请重新导入！！！";
	    }
	}

	public static <V> void main(String[] args) {
		String DRVIER = null;
		String URL = null;
		String USERNAMR = null;
		String PASSWORD = null;
		try {
			Properties prop = new Properties();
			//Class.getResourceAsStream("name") 会指定要加载的资源路径与当前类所在包的路径一致 如果这个name是以 '/' 开头的，那么就会从classpath的根路径下开始查找。
			//getClassLoader().getResourceAsStream("name")  无论要查找的资源前面是否带'/' 都会从classpath的根路径下查找。
			prop.load(UploadAndAownloadServiceImp.class.getClassLoader().getResourceAsStream("ibatis/jdbc.properties"));
			DRVIER = prop.getProperty("oracle.connection.driverClass");
			URL = prop.getProperty("oracle.connection.jdbcUrl");
			USERNAMR = prop.getProperty("oracle.connection.username");
			PASSWORD = prop.getProperty("oracle.connection.password");
		} catch (Exception e) {
			System.out.println(e);
		}
		Connection con = null;
	    ResultSet rt = null;
	    PreparedStatement ps = null;
		PreparedStatement ps1 = null;
	    long start;
	    try {
	    	start = System.currentTimeMillis();
			// 加载驱动
			Class.forName(DRVIER);
			con = DriverManager.getConnection(URL, USERNAMR, PASSWORD);
//			con.setAutoCommit(false);
//			String sql = "select 1 from dual";
//			PreparedStatement prepareStatement = con.prepareStatement(sql);
//			ResultSet executeQuery = prepareStatement.executeQuery();
//			ResultSetMetaData metaData = executeQuery.getMetaData();
			System.out.println(con);
//			while (executeQuery.next()) {
//				//System.out.println(executeQuery.getString(1));
//			}
			long end = System.currentTimeMillis();
		    System.out.println("共用"+(end-start)+"毫秒");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally {
			if (ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				}
			}
	}
}
