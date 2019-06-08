package com.mininglamp.currencySys.util.CheckProgram;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.mininglamp.currencySys.ManaPro.service.ManaProService;
import com.mininglamp.currencySys.common.base.bean.ResponseBean;
import com.mininglamp.currencySys.common.util.CommonValidateUtil;
import com.mininglamp.currencySys.insertData.service.InsertDataService;
import com.mininglamp.currencySys.uploadAndDownload.service.UploadAndAownloadService;
import com.mininglamp.currencySys.util.DateUtils;
import com.mininglamp.currencySys.util.report.ImportExcelUtil;

import net.sf.json.JSONArray;

public class CheckReadExcel {
	/**
	 * 对上传的文件进行解析
	 * @param request 
	 * @param reportDataFile
	 * @param params
	 * @param testService
	 * @return
	 * @throws IOException
	 * @throws ParseException 
	 */
	public static ResponseBean FileParsing(HttpServletRequest request, MultipartFile reportDataFile, Map<String, Object> params, UploadAndAownloadService testService) throws IOException, ParseException{
		ResponseBean result = new ResponseBean();
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat fmt1 = new SimpleDateFormat("yyyyMMdd");
			HashMap<String, Object> map = new HashMap<String ,Object>();
			ArrayList<Map> arrayList = new ArrayList<Map>();
			String realPath = null;  
			String contextPath = request.getContextPath();
			realPath=contextPath+"/upload/";
			String name = reportDataFile.getOriginalFilename();
			String tname = (String) params.get("tname");
			//当前的表名
			String subTname = tname.substring(tname.length()-4);
			//上传文件的表名Ename
			String Ename = name.substring(0, 4);
			String newNname = UUID.randomUUID().toString()+name;//新名字
			File targetFile = new File(realPath,newNname);
			int lastIndexOf = name.lastIndexOf(".");
			//获取文件后缀
			String names = name.substring(lastIndexOf, name.length());
			if(name.length() < 16 || !Ename.equalsIgnoreCase(subTname)){
				map.put("code", "文件名称不对 请核对");
				arrayList.add(map);
				result.setData(arrayList);
				return result;
			}
			//获取表名 文件名前4位
			String tableNames = name.substring(0, 4);
			//String tableNames = name.substring(lastIndexOf-4, lastIndexOf);
			//获取文件名中 数据日期 后8位  tableSjrq
//			String tableSjrq = name.substring(18, 26);
			String tableSjrq = name.substring(4, 12);
			if(tableNames != null && tableNames != "" && tableNames.length() == 4 && tableNames.matches("^[a-zA-Z]*")){
				Pattern pattern = Pattern.compile("[0-9]*");
				if(!pattern.matcher(tableSjrq).matches() && !DataDateCheck.isValidDataFormat(tableSjrq)){
					map.put("code", "文件日期格式不符合！");
					arrayList.add(map);
					result.setData(arrayList);
					return result;
				}
				params.put("tableNames", tableNames);
				params.put("tableSjrq", fmt.format(fmt1.parse(tableSjrq)));
				
				List<Map> DataList;
				try {
					DataList = testService.getDataList("check_rule_config", params);
					if(names.equals(".xls")||names.equals(".xlsb")||names.equals(".xlsx")||names.equals(".xlsm")||names.equals(".xlst")){
						params.put("check_rule_config", DataList);
						if(DataList.size() < 1){
							map.put("code", "配置表异常！");
							arrayList.add(map);
							result.setData(arrayList);
							return result;
						}else{
							StringBuffer saveData = new StringBuffer();
							try {
								//调用Service的逻辑
								saveData = testService.saveData(params, realPath + newNname,reportDataFile,request);
							}catch (Exception e) {
								System.out.println(e);
								map.put("code", "导入异常！");
								arrayList.add(map);
								result.setData(arrayList);
								return result;
							}
							if(saveData.indexOf("导入成功") != -1){
								map.put("code", saveData);
								arrayList.add(map);
								result.setData(arrayList);
							}else{
								String substring = "";
								if(saveData.length() >= 9000){
									substring = saveData.substring(0, 9000);
									substring +="......";
								}else{
									substring = saveData.toString();
								}
								map.put("code", "导入失败！ 错误日志：<br />"+substring);
								//把错误信息放到session 方便下载错误日志
								HttpSession session = request.getSession();
								session.removeAttribute("errorLogs");
								//替换错误日志中br
								session.setAttribute("errorLogs", substring.replace("<br />", "\r\n"));
								arrayList.add(map);
								result.setData(arrayList);
							}
						}
					}else{
						map.put("code", "文件后缀不对，请核对！");
						arrayList.add(map);
						result.setData(arrayList);
					}
				} catch (Exception e) {
					map.put("code", "check_rule_config配置表异常");
					arrayList.add(map);
					result.setData(arrayList);
					return result;
				}
			}else{
				map.put("code", "文件名字不对，请核对！");
				arrayList.add(map);
				result.setData(arrayList);
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setData(e);
		}
		return result;
	}
	
	/**
	 * 获取Excel数据
	 * @param path
	 * @param params
	 * @param reportDataFile 
	 * @param dataList 
	 * @param request 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	public static HashMap<String, Object> uploadTool(String path,Map<String, Object> params, MultipartFile reportDataFile, List<Map> dataList, HttpServletRequest request,ManaProService manaProService) {
		//StringBuffer errLogs = null;
		StringBuffer errLogs = new StringBuffer();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd");
		//用来放Excel数据 和错误日志
		HashMap<String, Object> EAndEmap = new HashMap<String ,Object>();
		//用来接收每个Sheet的值
		ArrayList<Map> arrayList = new ArrayList<Map>();
		//用来接收参数的值
		ArrayList<Map> paramList = new ArrayList<Map>();
		//用来接收参数的类型
		ArrayList<Map> paramListType = new ArrayList<Map>();
		//用来接收错误日志的值
		ArrayList<Map> errorList = new ArrayList<Map>();
		try {
			// 同时支持Excel 2003、2007
//			File excelFile = new File(path); // 创建文件对象
//			FileInputStream is = new FileInputStream(excelFile); // 文件流
//			Workbook workbook = WorkbookFactory.create(is); // 这种方式 Excel
															// 2003/2007/2010
															// 都是可以处理的
			CommonsMultipartFile cf = (CommonsMultipartFile) reportDataFile;
			InputStream file = cf.getInputStream();
			Workbook workbook = WorkbookFactory.create(file);
			int sheetCount = workbook.getNumberOfSheets(); // Sheet的数量
			Sheet sheet1 = workbook.getSheetAt(sheetCount-1);//获取到最后一个sheet
			Row row1 = sheet1.getRow(1);//获取到第二行
			Cell cell1 = row1.getCell(0);//获取第二行第一列
			String tablesjrq = (String) params.get("tableSjrq");
			if(!"jgxx".equals(params.get("tableNames"))) {
//				if(!tablesjrq.equals(cell1.getStringCellValue())) {
//					errLogs.append("导入的表日期  "+tablesjrq+"  与参数sheet页数据日期   "+cell1.getStringCellValue()+"  不相同！请修改与表名日期一致!");
//				}
			
				// 判断文件名中数据日期是否正确（月报为月末，季报为季末，半年报为半年末，年报为年末）
				String markingCode = ((Map)params.get("userAuthorityInfo")).get("MARKINGCODE") != null 
						? ((Map)params.get("userAuthorityInfo")).get("MARKINGCODE").toString() : "";// 1:日报，2:月报，3:季报，4:半年报，5:年报
				if (StringUtils.isNotEmpty(markingCode)) {
					if ("2".equals(markingCode) && !DateUtils.isLastDayOfMonth(fmt1.parse(tablesjrq))) {
						errLogs.append("当前报表为月报，导入的文件名中表日期  "+tablesjrq+" 不是当月最后一天！");
					} else if ("3".equals(markingCode) && !DateUtils.getIsLastDayOfQuarter(tablesjrq)) {
						errLogs.append("当前报表为季报，导入的文件名中表日期  "+tablesjrq+" 不是当前季度最后一天！");
					} else if ("4".equals(markingCode) && !DateUtils.getIsbn(tablesjrq)) {
						errLogs.append("当前报表为半年报，导入的文件名中表日期  "+tablesjrq+" 不是当前半年最后一天！");
					} else if ("5".equals(markingCode) && !DateUtils.getIsCurrYearLast(tablesjrq)) {
						errLogs.append("当前报表为年报，导入的文件名中表日期  "+tablesjrq+" 不是当前年最后一天！");
					}
				}
			}
			
			// 遍历每个Sheet
			if(errLogs == null || errLogs.length() <= 0) {
			for (int s = 0; s < sheetCount; s++) {
				Sheet sheet = workbook.getSheetAt(s);
				//int rowNums = sheet.getPhysicalNumberOfRows(); // 获取总行数
                int rowNums = sheet.getLastRowNum() + 1;
				//数据sheet默认会有一行表头
				if(rowNums>1){
					//遍历每一行
					for (int r = 1; r < rowNums; r++) {
						Row row = sheet.getRow(r);//sheet.getRow(1).getCell(0)
						int cellNum = row.getPhysicalNumberOfCells(); // 获取总列数
						//查看是否为空行
						boolean checkRowNull = isRowEmpty(row);
						if(checkRowNull){
							continue;
						}
						//用来接收没行的值 flied, cellValue
						//HashMap<String, Object> map = new HashMap<String ,Object>();//每行Excel数据
						LinkedHashMap<String, Object> map = new LinkedHashMap<String ,Object>();
						//参数值
						HashMap<String, Object> paraMmap = new HashMap<String ,Object>();
						//参数值类型
						HashMap<String, Object> paraMmapType = new HashMap<String ,Object>();
						JSONArray filedList = JSONArray.fromObject(params.get("fieldAll"));
						int column = 0;
						if(filedList.contains("code") || filedList.contains("CODE")){
							//解决前两列 复选框 ，操作（现这两列已被冻结，前台取不到，故不需特殊处理）
//							column = filedList.size()-3;//有效列数，超出无效。
							column = filedList.size()-1;//有效列数，超出无效。
						}else{
							//解决前两列 复选框 ，操作（现这两列已被冻结，前台取不到，故不需特殊处理）
//							column = filedList.size()-2;//有效列数，超出无效。
							column = filedList.size();//有效列数，超出无效。
						}
						// 遍历每一列
						for (int cellCount = 0; cellCount < column; cellCount++) {
							DataFormatter dataFormatter = new DataFormatter();
							Cell cell = row.getCell(cellCount);
							if(cell!=null) {
							int cellType = cell.getCellType();
							if(cellType != 1){
								cell.setCellType(1); //设成文本格式
							}
							}
							//json字符串转化成集合
							//获取每列的filed
							//解决前两列 复选框 ，操作（现这两列已被冻结，前台取不到，故不需特殊处理）
//							String flied = (String) filedList.get((cellCount+2));
							String flied = (String) filedList.get((cellCount));
							//获取每列的值
							String cellValue = null;
							if("code".equals(flied) || "CODE".equals(flied)){
								cellValue = (String) ImportExcelUtil.getCellValue(cell);
							}else{
								cellValue = (String) ImportExcelUtil.getCellValue(cell,params,cellCount);
							}
							//如果当前sheet个数  s<sheetCount（总sheet个数）
							//sheetCount - 1 表示最后一个sheet 当做参数处理
							if(s < (sheetCount - 1)){
								//入库前校验
								String checkoutBeforeWarehousing = checkoutBeforeWarehousing(flied,cellValue,params,cellCount,r,sheetCount,dataList,request,manaProService);
								//有错误
								if(checkoutBeforeWarehousing != null && checkoutBeforeWarehousing.length() > 0){
									errLogs.append(checkoutBeforeWarehousing);
									int length = errLogs.length();
								}else{
									//每行的列和值添加到 map里面
									map.put(flied, cellValue);
								}
							}else{
								//只读参数sheet页 第二行数据
								if(r == 1){
									if(cellValue != null && cellValue.trim().length() > 0){
										List<Map> object = (List<Map>) params.get("check_rule_config");
										//判断文件名称中的日期，数据sheet的数据日期，参数sheet数据日期  的值是否一致
										if("date".equals(object.get(1).get("FIELD_TYPE"))){
											String tableSjrq = (String) params.get("tableSjrq");
											if(!tableSjrq.equals(cellValue) && !"jgxx".equals(params.get("fileName"))){
												errLogs.append("参数sheet数据日期和文件名中日期不相等");
											}else{
												if(flied==null && flied=="") {
													continue;
												}
												paraMmap.put(flied, cellValue);
												paraMmapType.put(flied, object.get(cellCount).get("FIELD_TYPE"));
											}
										}else{
											if(flied==null && flied=="") {
												continue;
											}
											paraMmap.put(flied, cellValue);
											paraMmapType.put(flied, object.get(cellCount).get("FIELD_TYPE"));
										}
									}
								}
							}
						}	
						//如果当前sheet个数  s<sheetCount（总sheet个数）
						//sheetCount - 1 表示最后一个sheet 当做参数处理
						if(s < (sheetCount - 1)){
							arrayList.add(map);
						}else{
							// 将code列加到删除参数中
							String code = row.getLastCellNum() >= column+1 && row.getCell(column) != null ? row.getCell(column).toString() : "";
							if (StringUtils.isNotEmpty(code)) {
								paraMmap.put("code", code);
								paraMmapType.put("code", "inString");
							}
							if(!paraMmap.isEmpty() && paraMmap!= null){
								paramList.add(paraMmap);
							}
							if(!paraMmapType.isEmpty() && paraMmapType!= null){
								paramListType.add(paraMmapType);	
							}
						}
					}
				}else{
					errLogs.append("第"+(s+1)+"数据sheet中数据为空");
				}
			}
		}
			file.close();//关闭流
			if(errLogs == null || errLogs.length() <= 0){
				//插入数据库
				EAndEmap.put("ExcelList", arrayList);
				//参数
				EAndEmap.put("paramList", paramList);
				//参数类型
				EAndEmap.put("paramListType", paramListType);
			}else{
				HashMap<String, Object> errMap = new HashMap<String ,Object>();
				errMap.put("errorlog", errLogs);
				errorList.add(errMap);
				errMap = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			errLogs.append(e);
			HashMap<String, Object> errMap = new HashMap<String ,Object>();
			errMap.put("errorlog", errLogs);
			errorList.add(errMap);
			errMap = null;
		}
		EAndEmap.put("errorList", errorList);
		return EAndEmap;
	}
	
	/**
	 * 校验每个单元格是否符合规范
	 * @param flied  字段名
	 * @param cellValue 单元格值
	 * @param params  Map<String, Object> params参数
	 * @param cellCount 哪列
	 * @param rowNums 哪行
	 * @param sheetCount 哪个sheet
	 * @param dataList 
	 * @param request 
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings("unused")
	public static String checkoutBeforeWarehousing(String flied,String cellValue, Map<String, Object> params,int cellCount, int rowNums, int sheetCount, List<Map> dataList, HttpServletRequest request,ManaProService manaProService) throws ParseException {
		String username = (String) request.getSession().getAttribute("username");
		String err = "";
		try {
			List<Map> object = (List<Map>) params.get("check_rule_config");
			Map<String,List<String>> dbCodeTableKeys = null;
			if (params.get("dbCodeTableKeys") != null) {
				dbCodeTableKeys = (Map<String,List<String>>)params.get("dbCodeTableKeys");
			} else {
				err += "第"+(rowNums+1)+"行,第"+(cellCount+1)+"列 参数异常！！！<br />";
				return err;
			}
			if(object.size() > cellCount){
				//字段类型
				String FIELD_TYPE = (String) object.get(cellCount).get("FIELD_TYPE");
				//字段 中文名称
				String FIELD_NAME = (String) object.get(cellCount).get("FIELD_NAME");
				//为否可以为空
				String is_null = (String) object.get(cellCount).get("IS_NULL");
				//是否强制校验
				String IS_LOGIC = (String) object.get(cellCount).get("IS_LOGIC");
				//是否使用配置的码表进行参照性校验
				String IS_CONTAIN_CONF_TABLE = (String) object.get(cellCount).get("IS_CONTAIN_CONF_TABLE");
				//配置的码表
				String CONF_TABLE_NAME = (String) object.get(cellCount).get("CONF_TABLE_NAME");
				//是否是自动生成字段
				String IS_INPUT_AUTO = (String) object.get(cellCount).get("IS_INPUT_AUTO");
				//判断文件名称中的日期，数据sheet的数据日期，参数sheet数据日期  的值是否一致
				if("SJRQ".equals(flied) ||  "sjrq".equals(flied)){
					String tableSjrq = (String) params.get("tableSjrq");
					if(!tableSjrq.equals(cellValue)){
						err += "第"+(rowNums+1)+"行,第"+(cellCount+1)+"列  数据日期和文件名中日期不相等<br />";
					}
				}
				//判断最后一个sheet的参数日期与表名是否一致
				
				//如果是admin用户就不做判断
				if(!"admin".equals(username)){
					//判断当前数据的内部机构号 是否在本用户的管辖之内
					if(flied != null && !"null".equals(flied) && flied.length() > 0){
						//获取当前表  内部机构号的字段
						List<Map> Table_nbjgh  = (List<Map>) params.get("Table_nbjgh");
						for (Map map : Table_nbjgh) {
							//当前表的内部机构号  字段
							String tablenbjgh = (String) map.get("TABLE_NBJGH");
							//如果当前字段和 当前表的内部机构号  字段相等
							if(flied.equals(tablenbjgh)){
								ArrayList<String> nbjghList = new ArrayList<String>();
								//当前用户下所有的内部机构号的值
								for (Map map2 : dataList) {
									nbjghList.add(String.valueOf(map2.get("NBJGH")));
								}
								boolean containsValue = nbjghList.contains(cellValue);
								if(!containsValue){
									err += "第"+(rowNums+1)+"行,第"+(cellCount+1)+"列 权限不足不能导入;"+"<br />";
									break;
								}
							}
						}
						
						//检验经常性报表  上报类型是否符合当前用户
						if("SBLX".equals(flied) || "sblx".equals(flied)){
							String sblxValiedateInfo = CommonValidateUtil.validateSBLX(cellValue,(String)params.get("tname"), request);
							if (StringUtils.isNotEmpty(sblxValiedateInfo)) {
								err += ("第"+(rowNums+1)+"行,第"+(cellCount+1)+"列 "+sblxValiedateInfo + "<br />");
							}
						}
						
						// 权限管理-金融机构、专项日报金融机构基本信息：机构级别校验
						if(("jgxx".equals((String)params.get("tableNames")) || "finf".equals((String)params.get("tableNames"))) && ("JGJB".equals(flied) || "jgjb".equals(flied))){
							Map<String, Object> tempParams = new HashMap<String, Object>();
							try {
								tempParams.put("JGJBValue", cellValue);
								tempParams.put("tableName", (String)params.get("tname"));
								String jrjgxxValidateMsg = CommonValidateUtil.validateJRJGXX(tempParams, request, manaProService);
								if (StringUtils.isNotEmpty(jrjgxxValidateMsg)) {
									err += "第"+(rowNums+1)+"行,第"+(cellCount+1)+"列 "+jrjgxxValidateMsg+"<br />";
								}
							} catch (Exception e) {
								e.printStackTrace();
								err += "第"+(rowNums+1)+"行,第"+(cellCount+1)+"列 异常<br />";
							}
						}
					}else{
						err += "第"+(rowNums+1)+"行,第"+(cellCount+1)+"flied字段异常<br />";
					}
				}
				
				//判断Excel单元格是否为空 不为空就不需要判断 字段为否为空
				if("null".equals(cellValue) || cellValue == null || cellValue.isEmpty()){
					//是否为空（1：必填   0：可为空）且不是自动生成字段
					if("1".equals(is_null) && !InsertDataService.IS_INPUT_AUTO_TRUE.equals(IS_INPUT_AUTO)){
						err += "第"+(rowNums+1)+"行,第"+(cellCount+1)+"列 为空;"+"<br />";
					}
				}else{
					boolean dataFieldTypeCheck = FieldTypeCheck.dataFieldTypeCheck(cellValue, FIELD_TYPE);
					if(!dataFieldTypeCheck){
						err += "第"+(rowNums+1)+"行,第"+(cellCount+1)+"列 格式错误;"+"<br />";
					}
					
					// 参照性校验
					String errorMsgCodeFormat = CommonValidateUtil.validateCodeFormat(cellValue, FIELD_NAME, IS_CONTAIN_CONF_TABLE, CONF_TABLE_NAME, dbCodeTableKeys);
					if (StringUtils.isNotEmpty(errorMsgCodeFormat)) err += "第"+(rowNums+1)+"行,第"+(cellCount+1)+"列 "+errorMsgCodeFormat+"<br />";
				}
			}
		} catch (Exception e) {
			err += e;
			e.printStackTrace();
		}
		return err;
	}
	
	// 判断某行为否为空
	public static boolean isRowEmpty(Row row) {
		   if(row != null && !"".equals(row)){
			 for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
			       Cell cell = row.getCell(c);
			       if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && StringUtils.isNotEmpty(cell.getStringCellValue()) && StringUtils.isNotEmpty(cell.getStringCellValue().trim()))
			           return false;
			   }
		}
		return true;
	}
	
	// 判断某行某列有问题
	private int CheckRowError(HSSFCell cell, List<Object> error_num, int rowNum, int cell_num) {
		// 判断各个单元格是否为空
		if (cell == null || cell.equals("") || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			error_num.add("出错啦！请检查第" + (rowNum + 1) + "行第" + (cell_num + 1) + "列。" + "如果您在该行没有数据，建议您选择删除该行，重试！");
			return -1;
		}
		return 0;
	}
	
	// 判断行为空
	public static int CheckRowNull(Row row) {
		int num = 0;
		Iterator<Cell> cellItr = row.iterator();
		while (cellItr.hasNext()) {
			Cell c = cellItr.next();
			if (c.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
				num++;
			}
		}
		return num;
	}
	
	/**
	 * 写入错误文件
	 * @param count
	 * @param str
	 * @param path
	 */
	public static void writeAnErrorFile(int count, String str,String path) {
		String substring = path.substring(0, 10);
	    File f = new File(path+"errorlog.txt");
	    BufferedOutputStream bos=null;
	    OutputStreamWriter writer = null;
	    BufferedWriter bw = null;
	    try {
	        OutputStream os = new FileOutputStream(f);
	        bos=new BufferedOutputStream(os);
	        writer = new OutputStreamWriter(bos);
	        bw = new BufferedWriter(writer);
	        for (int i = 0; i < count; i++) {
	            bw.write(str);
	        }
	        bw.flush();
	        if(f.exists()){
	            f.delete();
	        }
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            bw.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
}
