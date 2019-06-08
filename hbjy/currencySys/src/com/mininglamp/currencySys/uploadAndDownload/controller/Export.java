package com.mininglamp.currencySys.uploadAndDownload.controller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mininglamp.currencySys.common.base.controller.BaseController;
import com.mininglamp.currencySys.uploadAndDownload.service.UploadAndAownloadService;
import com.mininglamp.currencySys.uploadAndDownload.service.impl.UploadAndAownloadServiceImp;
import com.mininglamp.currencySys.util.Encodes;

import net.sf.json.JSONArray;
/**
 * 导出Excel
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/export")
public class Export extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(Export.class); 
	
	private SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
	private SimpleDateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat ymdhms = new SimpleDateFormat("yyyyMMddHHMMSS");
	private static String driverName = null; // 驱动
	private static String url = null;
	private static String userName = null;
	private static String pwd = null;

	@Autowired
	private UploadAndAownloadService testService;
	
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

	/**
	 * 导出Excel
	 * @param request
	 * @param response
	 * @param sqlId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/down")
	public void downExcel(HttpServletRequest request, HttpServletResponse response,String sqlId) throws Exception {
		long start = System.currentTimeMillis();
		//String str =  URLDecoder.decode(filetitle, "UTF-8");
		Map<String, Object> params = getParamters(request);
		//导出选中数据增加代码
		String suffix=null;
		String subName=(String) params.get("fileName");
		if (subName.length()>4) {
			String preffix=subName.substring(0, 4);
			suffix=subName.substring(4, subName.length()-1);
			params.put("fileName", preffix);
		}else {
			params.put("fileName", subName);
		}
		HttpSession session = request.getSession();
		String nbjgh = null;
		String username = (String) session.getAttribute("username");
		//判断当前用户是否为admin 是 则查询全部 否则查询权限之内的
		if(!"admin".equals(username)){
			nbjgh = (String) session.getAttribute("nbjgh");
		}
		sqlId = (String) params.get("excelSqlid");
		//获取title
		JSONArray fieldValues = JSONArray.fromObject(new String(params.get("fieldValues").toString().getBytes("UTF-8"), "UTF-8"));
		//获取fields
		JSONArray fields = JSONArray.fromObject(params.get("fields"));
		if(fields.contains("code") || fields.contains("CODE")){
			//去除code列
			fields.remove(fields.size() - 1);
		}
		if(fieldValues.contains("编码")){
			//去除code列
			fieldValues.remove(fieldValues.size() - 1);
		}
		//删除fields前两个字段（由于全选、操作已被冻结，故取不到这两列，无需删除）
//		fields.remove(0);
//		fields.remove(0);
		//删除fieldValues前两个字段
		fieldValues.remove(0);
		fieldValues.remove(0);
		//转换成数组
		Object[] fieldsArray = fields.toArray(new String[fields.size()]);
		Object[] fieldValuesArray = fieldValues.toArray(new String[fieldValues.size()]);
		//获取文件中文名
		String fileTitle = (String) params.get("fileTitle");
		String beginSjrq = (String) params.get("beginSjrq");
		if(beginSjrq == null){
			beginSjrq = (String) params.get("SJRQ");
			params.put("beginSjrq", beginSjrq);
		}
		//文件名称
		String fileName = params.get("fileName")+fmt.format(fmt1.parse(beginSjrq)) + fileTitle + ".xls";
		//String fileName = params.get("fileName")+fileTitle + ".xls";
		//sheet页title
		String[] columnNames = (String[]) fieldValuesArray;
		//数据sheet页keys
		String[] keys = (String[]) fieldsArray;
		fields.remove(0);
		fields.add(0, "beginSjrq");
		Object[] paramarray = fields.toArray(new String[fields.size()]);
		//参数sheet页keys
		String paramKeys[] = (String[]) paramarray;
		
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		ArrayList<Map> arrayList = new ArrayList<Map>();
		hashMap = (HashMap<String, Object>) params;
		arrayList.add(hashMap);
		//拼接收SQL语句
		//获取四位标识
		String name = (String) params.get("fileName");
		//获取配置表信息
		List<Map> configure = (List<Map>) session.getAttribute(name+"dataList");
		StringBuffer sql = new  StringBuffer("select ");
		//获取表名
		String tableName = (String) params.get("tableName");
		//获取当前表的 内部机构号是什么
		params.put("tname", tableName);
		List<Map> dataList = testService.getDataList("getTable_nbjgh", params);
		//拼接查询sql语句
		SplicingSQLByCode(sql,configure,keys,arrayList,tableName,nbjgh,String.valueOf(dataList.get(0).get("TABLE_NBJGH")),suffix);
		logger.info("报表导出，导出查询的拼接后的sql -- ："+sql);
		
		jdbcex(request, response, keys, columnNames,arrayList,columnNames,paramKeys,fileName,sql.toString(),suffix);
		long end = System.currentTimeMillis();
		System.out.println("导出共用"+(end-start)+"毫秒");
	}
	
	/**
	 * 拼接sql
	 * @param sql
	 * @param configure 配置表list
	 * @param keys 表字段名称
	 * @param arrayList 参数
	 * @param tableName 表名
	 * @param nbjgh 当前用户的内部机构号
	 * @param TABLE_NBJGH 
	 */
	public void SplicingSQL(StringBuffer sql, List<Map> configure, String[] keys, ArrayList<Map> arrayList, String tableName, String nbjgh, String TABLE_NBJGH) {
		//遍历字段  获取字段类型 填充查询字段
		for (int i = 0; i < keys.length; i++) {
			if(i < configure.size()){
				String FIELD_TYPE = (String) configure.get(i).get("FIELD_TYPE");
				if("date".equals(FIELD_TYPE) || "DATE".equals(FIELD_TYPE)){
					sql.append(" to_char(T."+keys[i]+",'yyyy-mm-dd') as "+keys[i]);
				}else if("time".equals(FIELD_TYPE) || "TIME".equals(FIELD_TYPE)){
					sql.append(" to_char(T."+keys[i]+",'HH24:MI:SS') as "+keys[i]);
				}else{
					sql.append(" T."+keys[i]+" as "+keys[i]);
				}
				sql.append(" , ");
			}else{
				
			}
		}
		sql = sql.delete(sql.length() - 2, sql.length());
		sql.append(" from "+tableName+" T where 1=1 ");
		
		//遍历字段  获取字段类型 填充where条件后的数据
		for (int i = 0; i < keys.length; i++) {
			//根据配置表  去除code列
			if(i < configure.size()){
				String FIELD_TYPE = (String) configure.get(i).get("FIELD_TYPE");
				String columnValue = (String) arrayList.get(0).get(keys[i]);
				if((columnValue != null && !"null".equals(columnValue) && columnValue != "" && !"".equals(columnValue) ) || "SJRQ".equals(keys[i])){
					if("date".equals(FIELD_TYPE) || "DATE".equals(FIELD_TYPE)){
						//目前系统中 查询时有两个字段  一个是SJRQ ；另一个beginSjrq  日期条件必选 所以说这两个肯定有一个是有值的
						String sjrq = (String) arrayList.get(0).get(keys[i]);//keys[i] 应该是  SJRQ
						if(sjrq == null || "null".equals(sjrq)){
							sjrq = (String) arrayList.get(0).get("beginSjrq");
						}
						sql.append(" and "+keys[i]+" = to_date('"+sjrq+"','yyyy-mm-dd') ");
					}else{
						//sql.append(" and "+keys[i]+" = '"+arrayList.get(0).get(keys[i])+"'");
						sql.append(" and "+keys[i]+" like '%"+arrayList.get(0).get(keys[i])+"%'");
					}
				}
			}
		}
		
		if(nbjgh != null){
			if(TABLE_NBJGH != null && !"null".equals(TABLE_NBJGH)){
				sql.append(" and T."+TABLE_NBJGH+" in ("
						+"select BR_NO from HBJYODS.JRJGXX m "
						+"start with m.BR_NO = '"+nbjgh+"' connect by m.SJGLJGNBJGH = prior m.BR_NO)");
			}else{
				// （gzhm）冠字号文件信息表 没有内部机构号，不需要查询
				if (!"special_gzhm".equalsIgnoreCase(tableName)) {
					try {
						throw new Exception("参数异常");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	/**
	 * 拼接sql
	 * @param sql
	 * @param configure 配置表list
	 * @param keys 表字段名称
	 * @param arrayList 参数
	 * @param tableName 表名
	 * @param nbjgh 当前用户的内部机构号
	 * @param TABLE_NBJGH 
	 */
	public void SplicingSQLByCode(StringBuffer sql, List<Map> configure, String[] keys, ArrayList<Map> arrayList, String tableName, String nbjgh, String TABLE_NBJGH,String suffix) {
		//遍历字段  获取字段类型 填充查询字段
		for (int i = 0; i < keys.length; i++) {
			if(i < configure.size()){
				String FIELD_TYPE = (String) configure.get(i).get("FIELD_TYPE");
				if("date".equals(FIELD_TYPE) || "DATE".equals(FIELD_TYPE)){
					sql.append(" to_char(T."+keys[i]+",'yyyy-mm-dd') as "+keys[i]);
				}else if("time".equals(FIELD_TYPE) || "TIME".equals(FIELD_TYPE)){
					sql.append(" to_char(T."+keys[i]+",'HH24:MI:SS') as "+keys[i]);
				}else{
					sql.append(" T."+keys[i]+" as "+keys[i]);
				}
				sql.append(" , ");
			}else{
				
			}
		}
		sql = sql.delete(sql.length() - 2, sql.length());
		sql.append(" from "+tableName+" T where 1=1 ");
		
		//遍历字段  获取字段类型 填充where条件后的数据
		for (int i = 0; i < keys.length; i++) {
			//根据配置表  去除code列
			if(i < configure.size()){
				String FIELD_TYPE = (String) configure.get(i).get("FIELD_TYPE");
				String columnValue = (String) arrayList.get(0).get(keys[i]);
				if((columnValue != null && !"null".equals(columnValue) && columnValue != "" && !"".equals(columnValue) ) || "SJRQ".equals(keys[i])){
					if("date".equals(FIELD_TYPE) || "DATE".equals(FIELD_TYPE)){
						//目前系统中 查询时有两个字段  一个是SJRQ ；另一个beginSjrq  日期条件必选 所以说这两个肯定有一个是有值的
						String sjrq = (String) arrayList.get(0).get(keys[i]);//keys[i] 应该是  SJRQ
						if(sjrq == null || "null".equals(sjrq)){
							sjrq = (String) arrayList.get(0).get("beginSjrq");
						}
						sql.append(" and "+keys[i]+" = to_date('"+sjrq+"','yyyy-mm-dd') ");
					}else{
						//sql.append(" and "+keys[i]+" = '"+arrayList.get(0).get(keys[i])+"'");
						sql.append(" and "+keys[i]+" like '%"+arrayList.get(0).get(keys[i])+"%'");
					}
				}
			}
		}
		if (suffix !=null) {
			sql.append(" and T.CODE in ("+suffix+")");
		}		
		if(nbjgh != null){
			if(TABLE_NBJGH != null && !"null".equals(TABLE_NBJGH)){
				sql.append(" and T."+TABLE_NBJGH+" in ("
						+"select BR_NO from HBJYODS.JRJGXX m "
						+"start with m.BR_NO = '"+nbjgh+"' connect by m.SJGLJGNBJGH = prior m.BR_NO)");
			}else{
				// （gzhm）冠字号文件信息表 没有内部机构号，不需要查询
				if (!"special_gzhm".equalsIgnoreCase(tableName)) {
					try {
						throw new Exception("参数异常");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	/**
	 * 读数据库数据生成Excel
	 * @param request
	 * @param response
	 * @param keys
	 * @param columnNames
	 * @param arrayList
	 * @param paramNames
	 * @param paramKeys
	 * @param fileName
	 * @param sql
	 */
	@RequestMapping("jdbcex")
	public static void jdbcex(HttpServletRequest request, HttpServletResponse response, String[] keys,
			String columnNames[], ArrayList<Map> arrayList, String[] paramNames, String[] paramKeys, String fileName, String sql,String suffix)
			{

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		FileInputStream input = null;
		try {
			getUserAndPass();
			//获取服务器URL 
			String realPath = request.getSession().getServletContext().getRealPath("");
			File parentFile = new File(realPath+"/upLoadFiles");
			//判断文件 文件夹 是否存在
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			String fileUrl=realPath+"/upLoadFiles/" +ymdhms.format(new Date())+"----" +fileName;
			//URLEncoder.encode(onename, "UTF-8");
			//String fileUrl = new String(onename.getBytes("utf-8"), "ISO-8859-1");// 输出文件
			//fileUrl = new String(fileUrl.getBytes(), "UTF-8");
			// 内存中只创建100个对象，写临时文件，当超过100条，就将内存中不用的对象释放。
			Workbook wb = new SXSSFWorkbook(100); // 关键语句
			// 创建两种单元格格式
			CellStyle cs = wb.createCellStyle();
			CellStyle cs2 = wb.createCellStyle();
			
			// 创建两种字体
			Font f = wb.createFont();
			Font f2 = wb.createFont();

			// 创建第一种字体样式（用于列名）
			f.setFontHeightInPoints((short) 10);
			f.setColor(IndexedColors.BLACK.getIndex());
			f.setBoldweight(Font.BOLDWEIGHT_BOLD);

			// 创建第二种字体样式（用于值）
			f2.setFontHeightInPoints((short) 10);
			f2.setColor(IndexedColors.BLACK.getIndex());

			// 设置第一种单元格的样式（用于列名）
			cs.setFont(f);
			cs.setBorderLeft(CellStyle.BORDER_THIN);
			cs.setBorderRight(CellStyle.BORDER_THIN);
			cs.setBorderTop(CellStyle.BORDER_THIN);
			cs.setBorderBottom(CellStyle.BORDER_THIN);
			cs.setAlignment(CellStyle.ALIGN_CENTER);
			cs.setDataFormat(wb.createDataFormat().getFormat("@"));
			
			// 设置第二种单元格的样式（用于值）
			cs2.setFont(f2);
			cs2.setBorderLeft(CellStyle.BORDER_THIN);
			cs2.setBorderRight(CellStyle.BORDER_THIN);
			cs2.setBorderTop(CellStyle.BORDER_THIN);
			cs2.setBorderBottom(CellStyle.BORDER_THIN);
			cs2.setAlignment(CellStyle.ALIGN_CENTER);
			cs2.setDataFormat(wb.createDataFormat().getFormat("@"));
			Sheet sheet = null; // 工作表对象
			Row nRow = null; // 行对象
			Cell nCell = null; // 列对象

			// 使用jdbc链接数据库
			Class.forName(driverName).newInstance();
			// 获取数据库连接
			conn = DriverManager.getConnection(url, userName, pwd);
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(sql);
			System.out.println("sql语句--->"+sql);

			ResultSetMetaData rsmd = rs.getMetaData();
			long startTime = System.currentTimeMillis(); // 开始时间
			System.out.println("strat execute time: " + startTime);

			int rowNo = 0; // 总行号
			int pageRowNo = 0; // 页行号

			//获取查询数据总条数
			rs.last();
			int Count = rs.getRow();
			if(Count > 0){
				rs.beforeFirst();
				while (rs.next()) {
					// 打印300000条后切换到下个工作表，可根据需要自行拓展，2百万，3百万...数据一样操作，只要不超过1048576就可以
					if (rowNo % 300000 == 0) {
						System.out.println("Current Sheet:" + rowNo / 300000);
						sheet = wb.createSheet("数据" + (rowNo / 300000));// 建立新的sheet对象
						sheet = wb.getSheetAt(rowNo / 300000); // 动态指定当前的工作表
						pageRowNo = 0; // 每当新建了工作表就将当前工作表的行号重置为0
						Row row = sheet.createRow(0);
						for (int j = 0; j < columnNames.length; j++) {
							nCell = row.createCell(j);
							nCell.setCellValue(columnNames[j]);
							nCell.setCellStyle(cs);
							sheet.setColumnWidth((short) j, (short) (35.7 * 150));
							sheet.setDefaultColumnStyle(j, cs);
						}
					}
					rowNo++;
					nRow = sheet.createRow((pageRowNo++) + 1); // 新建行对象
					//System.out.println("rsmd.getColumnCount()"+rsmd.getColumnCount());
					//System.out.println("rsmd.toString()"+rsmd.toString());
					//System.out.println("rs.toString()"+rs.toString());
					// 打印每行，每行有N列数据 rsmd.getColumnCount()==N --- 列属性的个数
					for (int j = 0; j < rsmd.getColumnCount(); j++) {
						nCell = nRow.createCell(j);
						/*String resultData=null;
						if (null !=rs.getString(j + 1)) {
							if (rs.getString(j + 1).toString().equals("R01")) {
								resultData="100元";
							}else if(rs.getString(j + 1).toString().equals("R02")) {
								resultData="50元";
							}else if(rs.getString(j + 1).toString().equals("R03")) {
								resultData="20元";
							}else if(rs.getString(j + 1).toString().equals("R04")) {
								resultData="10元";
							}else if(rs.getString(j + 1).toString().equals("R05")) {
								resultData="5元";
							}else if(rs.getString(j + 1).toString().equals("R06")) {
								resultData="1元";
							}else if(rs.getString(j + 1).toString().equals("R07")) {
								resultData="5角";
							}else if(rs.getString(j + 1).toString().equals("R08")) {
								resultData="1角";
							}else if(rs.getString(j + 1).toString().equals("R09")) {
								resultData="2元";
							}else if(rs.getString(j + 1).toString().equals("R10")) {
								resultData="2角";
							}else if(rs.getString(j + 1).toString().equals("R11")) {
								resultData="分币";
							}else if(rs.getString(j + 1).toString().equals("W")) {
								resultData="省级";
							}else if(rs.getString(j + 1).toString().equals("X")) {
								resultData="地级市";
							}else if(rs.getString(j + 1).toString().equals("Y")) {
								resultData="县级";
							}else if(rs.getString(j + 1).toString().equals("Z")) {
								resultData="乡级";
							}
							else {
								resultData=rs.getString(j + 1).toString();
							}
						}*/
						nCell.setCellValue(rs.getString(j + 1) == null ? "" : rs.getString(j + 1).toString());
						//nCell.setCellValue(rs.getString(j + 1) == null ? "" : resultData);
						System.out.println("rs.getString(j + 1)"+rs.getString(j + 1));
						nCell.setCellStyle(cs2);
					}
					if (rowNo % 10000 == 0) {
						System.out.println("row no: " + rowNo);
					}
					// Thread.sleep(1); //休息一下，防止对CPU占用，其实影响不大
				}
			}else{
				sheet = wb.createSheet("数据" + (rowNo / 300000));// 建立新的sheet对象
				sheet = wb.getSheetAt(rowNo / 300000); // 动态指定当前的工作表
				Row row = sheet.createRow(0);
				for (int j = 0; j < columnNames.length; j++) {
					nCell = row.createCell(j);
					nCell.setCellValue(columnNames[j]);
					nCell.setCellStyle(cs);
					sheet.setColumnWidth((short) j, (short) (35.7 * 150));
					sheet.setDefaultColumnStyle(j, cs);
				}
			}
			
			//创建参数sheet
			sheet = wb.createSheet("参数");// 建立新的sheet对象
			sheet = wb.getSheetAt(rowNo / 300000 + 1); // 动态指定当前的工作表
			Row row = sheet.createRow(0);
			// 设置列名
			for (int i = 0; i < paramNames.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(paramNames[i]);
				cell.setCellStyle(cs);
				sheet.setColumnWidth((short) i, (short) (35.7 * 150));
			}
			// 设置code列
			if (sql.indexOf("T.CODE in") >= 0) {
				Cell cell = row.createCell(paramNames.length);
				cell.setCellValue("编码");
				cell.setCellStyle(cs);
				sheet.setColumnWidth((short) paramNames.length, (short) (35.7 * 150));
			}
			// 设置每行每列的值
			for (int i = 0; i < arrayList.size(); i++) {
				// Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
				// 创建一行，在页sheet上
				Row row1 = sheet.createRow(i + 1);
				// 在row行上创建一个方格
				for (int j = 0; j < paramKeys.length; j++) {
					Cell cell = row1.createCell(j);
					System.out.println("arrayList.get(i)"+""+i+":"+arrayList.get(i));
					System.out.println(arrayList.get(i).get(paramKeys[j]));
					cell.setCellValue(arrayList.get(i).get(paramKeys[j]) == null ? "" : arrayList.get(i).get(paramKeys[j]).toString());
					cell.setCellStyle(cs2);
					sheet.setDefaultColumnStyle(j, cs);
				}
				// 设置code列
				if (sql.indexOf("T.CODE in") >= 0) {
					Cell cell = row1.createCell(paramKeys.length);
					System.out.println("arrayList.get(i)"+""+i+":"+arrayList.get(i));
					System.out.println(suffix);
					cell.setCellValue(suffix == null ? "" : suffix);
					cell.setCellStyle(cs2);
					sheet.setDefaultColumnStyle(paramKeys.length, cs);
				}
			}
			long finishedTime = System.currentTimeMillis(); // 处理完成时间
			System.out.println("finished execute  time: " + (finishedTime - startTime) / 1000 + "m");

			FileOutputStream fOut = new FileOutputStream(fileUrl);
			wb.write(fOut);
			fOut.flush(); // 刷新缓冲区
			fOut.close();

			long stopTime = System.currentTimeMillis(); // 写文件时间
			System.out.println("write xls file time: " + (stopTime - startTime) / 1000 + "m");

			// 输入流 
			input = new FileInputStream(fileUrl);
			// 设置response参数，可以打开下载页面
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			//response.setHeader("Content-Disposition","attachment;filename=" + new String(fileName.getBytes(), "ISO-8859-1"));
			response.setHeader("Content-Disposition","attachment;filename=" + Encodes.urlEncode(fileName));
			// 获取绑定了客户端的流
			ServletOutputStream output = response.getOutputStream();
			// 把输入流中的数据写入到输出流中
			IOUtils.copy(input, output);
		} catch (Exception e) {
			e.printStackTrace();
            //文件不存在时会在FileinputStream（filePath）读取时异常，进入这里。导致前台页面无响应。这里就进行重定向到原页面，并带上错误标记
            try{
                //request.getCOntextPath()获取项目名，这个必须加上，不然找不到对应path的方法。页面404，这个path对应本类的另一个方法（在下面有）
            	response.sendRedirect(request.getContextPath()+"/error/404.jsp");
            }catch(IOException e1){
            	e1.printStackTrace();
            }
		}finally {
			// 执行关闭流的操作
			try {
				if(input != null){
					input.close();
				}
				if(rs != null){
					rs.close();
				}
				if(stmt != null){
					stmt.close();
				}
				if(conn != null){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
