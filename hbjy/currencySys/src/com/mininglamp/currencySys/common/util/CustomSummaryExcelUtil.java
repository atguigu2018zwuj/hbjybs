package com.mininglamp.currencySys.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.Collator;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 自定义汇总
 * @author 陈翔宇
 */
public class CustomSummaryExcelUtil {
	private static final Logger logger = LoggerFactory.getLogger(CustomSummaryExcelUtil.class);
	/** 正则表达式：浮点数 */
	private static final String REG_DOUBLE_NUMBER = "^-?[1-9]\\d*(\\.\\d*)?|-?0(\\.\\d*)?$";
	/** 正则表达式：简单的公式（如：【(H7*2)/(H7*2+I7)*100】或【F7*0.1+J7*0.1+L7*0.45+N7*0.35】） */
	private static final String REG_SIMPLE_FORMULA = "^\\(*(([A-Z]+\\d+)|(-?[1-9]\\d*(\\.\\d*)?|-?0(\\.\\d*[1-9]\\d*)?))(\\)*[\\+ \\- \\* /]\\(*(([A-Z]+\\d+)|(-?[1-9]\\d*(\\.\\d*)?|-?0(\\.\\d*[1-9]\\d*)?))\\)*)+";
	/** 正则表达式：函数公式（如：【RANK(O8,$O$7:$O$14,0)】或【SUM(M8:O8)】） */
	private static final String REG_FUNCTION_FORMULA = "[A-Z]+\\(.+\\)";
	/** 正则表达式：加和函数公式（如：【SUM(M1:M8)】或【SUM(M8:O8)】） */
	private static final String REG_FUNCTION_SUM_FORMULA = "SUM\\(.+\\)";
	/** 操作完成代码：成功执行 */
	private static final String FLG_CODE_SUCCESS = "0000";
	/** 操作完成代码：起始行编号、结束行编号不在合理范围内 */
	private static final String FLG_CODE_START_END_ROWNW_ERROR = "0001";
	/** 行号范围方向：自上而下指定行范围 */
	private static final String ROWNM_DIRECTION_FORWARD = "FORWARD";
	/** 行号范围方向：自下而上指定行范围 */
	private static final String ROWNM_DIRECTION_BACKWARD = "BACKWARD";
	/** 行号范围方向：默认行范围 */
	private static final String ROWNM_DIRECTION_NONE = "NONE";
	
	/**
	 * 汇总Excel
	 * @param summaryParams 汇总参数（详见方法 CustomSummaryExcelUtil.summary(JSONObject)）
	 * @return 是否汇总成功
	 */
	public static String summaryExcel(String summaryParamsStr){
		if (StringUtils.isNotEmpty(summaryParamsStr)) {
			JSONObject summaryParams = null;
			try {
				summaryParams = JSONObject.fromObject(summaryParamsStr);
				// 参数校验(参数均为必填)
				if (StringUtils.isEmpty(summaryParams.getString("templatePath")) 
						|| (summaryParams.getJSONArray("filePathList") == null || summaryParams.getJSONArray("filePathList").isEmpty())) {
					return null;
				}
				return summaryExcel(summaryParams);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}
	
	/**
	 * 汇总Excel
	 * @param summaryParams 汇总参数（详见方法 CustomSummaryExcelUtil.summary(JSONObject)）
	 * @return 输出到的文件的路径
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static String summaryExcel(JSONObject summaryParams) throws Exception{
		// 汇总EXCEL
    	List<Map<String,String>> result = summary(summaryParams);
    	System.out.println(JSONArray.fromObject(result).toString());
    	// 将结果写入excel
    	return writeToExcel(
    			result, 
    			summaryParams.getString("templatePath"), 
    			summaryParams.get("verticalSummaryList") != null ? summaryParams.getJSONArray("verticalSummaryList") : new ArrayList<String>(),
    			StringUtils.isNotEmpty(summaryParams.get("summaryType")) ? summaryParams.getString("summaryType") : "ROW_PUSH",
    			summaryParams.get("simpleSummaryOption") != null ? summaryParams.getJSONObject("simpleSummaryOption") : new JSONObject());
	}
	
	/**
     * 汇总数据(不写入Excel)
     * @param summaryParams 汇总参数（详见参数{@link com.mininglamp.currencySys.common.util.CustomSummaryExcelUtil#summary(String, List, List, List, List, String, JSONObject)}）
     * @return 汇总后的结果集
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public static List<Map<String,String>> summary(JSONObject summaryParams) throws Exception{
    	// 参数获取
    	// 模板路径
    	String templatePath = summaryParams.getString("templatePath");
    	// 汇总方式类型
    	String summaryType = StringUtils.parseString(summaryParams.get("summaryType"));
    	// 简单汇总的选项
    	JSONObject simpleSummaryOption = summaryParams.get("simpleSummaryOption") != null ? summaryParams.getJSONObject("simpleSummaryOption") : new JSONObject();
    	// 报表路径
    	List<String> filePathList = summaryParams.getJSONArray("filePathList");
    	// 需要纵向汇总的列
    	List<String> verticalSummaryList = summaryParams.get("verticalSummaryList") != null ? summaryParams.getJSONArray("verticalSummaryList") : new ArrayList<String>();
    	// 需要纵向加和的行的公式
    	List<String> verticalFormulaList = summaryParams.get("verticalFormulaList") != null ? summaryParams.getJSONArray("verticalFormulaList") : new ArrayList<String>();
    	// 需要横向汇总的列的公式
    	List<String> horizontalSummaryList = summaryParams.get("horizontalSummaryList") != null ? summaryParams.getJSONArray("horizontalSummaryList") : new ArrayList<String>();
    	
    	// 参数处理
    	// 将纵向汇总的列按照字典顺序排序
    	Collections.sort(verticalSummaryList, new Comparator<String>() {  
    		@Override  
    		public int compare(String o1, String o2) {  
    			Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);  
    			return com.compare(o1, o2);  
    		}
    	});
    	// 将纵向加和的行的公式按照数值降序排序
    	Collections.sort(verticalFormulaList, new Comparator<String>() {  
    		@Override  
    		public int compare(String o1, String o2) {
    			if (StringUtils.isEmpty(o1.split("=")[0]) && StringUtils.isEmpty(o2.split("=")[0])) {
    				return 0;
    			} else if (StringUtils.isEmpty(o1.split("=")[0])) {
    				return 1;
    			} else if (StringUtils.isEmpty(o2.split("=")[0])) {
    				return -1;
    			} else {
    				return Integer.parseInt(o2.split("=")[0]) - Integer.parseInt(o1.split("=")[0]);
    			}
    		}
    	});
    	
    	return summary(templatePath,filePathList,verticalSummaryList,verticalFormulaList,horizontalSummaryList,summaryType,simpleSummaryOption);
    }
	
	/**
     * 汇总数据(不写入Excel)
     * @param templatePath 模板路径
     * @param filePathList 包含所有要汇总的的excel的路径
     * @param verticalSummaryList 包含所有需要纵向汇总的列
     * @param verticalFormulaList 包含所有需要纵向加和的行的公式（支持两种格式：o=SUM(m:n)、o=SUM(m,n,p,……)。其中【m、n、o、p】都是行号，第一种为第o行的数据由从第m行到第n行求和；第二种为第o行的数据由SUM中的所有行号的行的加和；）
     * @param horizontalSummaryList 包含所有需要横向汇总的列的公式
     * @param summaryType 汇总方式类型（类型描述(可为null，默认为ROW_PUSH)：<br/>
     * 		●ROW_PUSH：多行归总汇总(详见方法[{@link com.mininglamp.currencySys.common.util.CustomSummaryExcelUtil#summarySimpleRowPush(List, List, int, List)}])；<br/>
     * 		●CELL_SUM：各单元格加总汇总(详见方法[{@link com.mininglamp.currencySys.common.util.CustomSummaryExcelUtil#summarySimpleCellSum(List, int, int, List, int, List)}])；）
     * @param simpleSummaryOption 简单汇总的选项(若summaryType选择CELL_SUM时，必填。详见方法[{@link com.mininglamp.currencySys.common.util.CustomSummaryExcelUtil#summarySimpleCellSum(List, int, int, List, int, List)}])：<br/>
     * 		●dataStartRowNum：CELL_SUM-数据行开始行号；<br/>
     * 		●dataEndRowNum：CELL_SUM-数据行结束行号；）
     * @return 汇总后的结果集
     * @throws Exception
     */
    public static List<Map<String,String>> summary(String templatePath, List<String> filePathList, List<String> verticalSummaryList, List<String> verticalFormulaList, List<String> horizontalSummaryList, String summaryType, JSONObject simpleSummaryOption) throws Exception{
    	// 包含所有已汇总数据的结果集
    	List<Map<String,String>> result = CommonUtil.removeEmptyValueMapFromResultAfter(readExcel(templatePath));
    	// 表头结束行号
    	int headerEndRowNum = result.size();
    	// 全部结果集的第一个数据行的行号（包含表头的行号）
    	int firstDataRowNm = 1;
    	// 全部结果集的最后一行数据行的行号（包含表头的行号）
    	int lastDataRowNm = 1;
    	
    	// 遍历所有报表路径，将报表中所有数据行都加入到result中，行数由第一个数据行的行号以此向下
    	summaryType = StringUtils.isNotEmpty(summaryType) ? summaryType : "ROW_PUSH";
    	// 根据汇总方式类型进行简单汇总
    	String flg = null;
    	switch (summaryType) {
    		case "ROW_PUSH":
    			// 临时使用开始行号与结束行号作为参数，第一个数据行的行号默认表头最后一行行号加1
    			if (StringUtils.isEmpty(simpleSummaryOption.get("direction")) || ROWNM_DIRECTION_NONE.equals(simpleSummaryOption.getString("direction"))) {
    				firstDataRowNm = 1;
    			} else {
        			// 若summaryType选择ROW_PUSH，且选择行范围指定时必填，行范围从1开始，为指定行范围方向下的行范围，如【自下而上的1~2】即为倒数第1~2行
    				if (simpleSummaryOption.get("dataStartRowNum") == null || simpleSummaryOption.get("dataEndRowNum") == null) return result;
    				firstDataRowNm = simpleSummaryOption.getInt("dataStartRowNum");
    				lastDataRowNm = simpleSummaryOption.getInt("dataEndRowNum");
    			}
    			flg = summarySimpleRowPush(result, filePathList, headerEndRowNum, firstDataRowNm, lastDataRowNm, StringUtils.parseString(simpleSummaryOption.get("direction")));
    			// 真正赋值
    			firstDataRowNm = headerEndRowNum + 1;
    			lastDataRowNm = result.size();
    			break;
    		case "CELL_SUM":
    			// 若summaryType选择CELL_SUM时必填
    			if (simpleSummaryOption.get("dataStartRowNum") == null || simpleSummaryOption.get("dataEndRowNum") == null) return result;
    			// 真正赋值
    			firstDataRowNm = simpleSummaryOption.getInt("dataStartRowNum");
    			lastDataRowNm = simpleSummaryOption.getInt("dataEndRowNum");
    			flg = summarySimpleCellSum(result, firstDataRowNm, lastDataRowNm, filePathList);
    			break;
    		default:
    			return result;
    	}
    	if (!FLG_CODE_SUCCESS.equals(flg)) {
    		switch (flg) {
    			case FLG_CODE_START_END_ROWNW_ERROR:
    				Map<String,String> map = new LinkedHashMap<String,String>();
    				map.put("A1", "起始行编号、结束行编号不在合理范围内，请修正后再试！");
    				result.clear();
    				result.add(map);
    				return result;
    		}
    	}
    	
    	// 遍历所有单元格进行横向公式计算
    	horizontalFormulaSummary(result, firstDataRowNm, lastDataRowNm, horizontalSummaryList);
    	
    	// 处理纵向加和的行的公式（o=SUM(m:n)、o=SUM(m,n,p,……)）
    	verticalFormulaSummary(result, verticalFormulaList);
    	
    	// 处理纵向加和(将加和放到对应列最后一行数据的下一行的单元格中)
    	verticalSummary(result, firstDataRowNm, lastDataRowNm, verticalSummaryList, summaryType);
    	
    	return result;
    }
    
	/**
     * 根据行index、列index获取该单元格的对应编号（A1、B1、AA1、AAA1、……）
     * @param rowNum 行index（从0开始）
     * @param colIx 列index（从0开始）
     * @return
     */
    public static String getCellNo(int rowNum, int colIx){
    	// 用于获取单元格编号的char
    	String noChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        // 取得表格编号
        StringBuilder cellNo = new StringBuilder("");
        float stringSize = Float.valueOf(noChars.length());
        if (colIx/(stringSize*stringSize+stringSize) >= 1) {//702-18278
            // 三位
        	int shang = 0;
        	int yuShu = 0;
        	if (colIx > 1377) {
        		shang = new Double(Math.floor((colIx-Math.pow(stringSize, 2)-stringSize)/(stringSize*stringSize))).intValue();
        		yuShu = new Double(Math.floor((colIx-Math.pow(stringSize, 2)-stringSize)%(stringSize*stringSize)+stringSize)).intValue();
        	} else {
        		shang = new Double(Math.floor((colIx-Math.pow(stringSize, 2)-stringSize)/(stringSize*stringSize+stringSize))).intValue();
        		yuShu = new Double(Math.floor((colIx-Math.pow(stringSize, 2))%(stringSize*stringSize+stringSize))).intValue();
        	}
        	cellNo.append(noChars.charAt(shang));// 除以26^2的商
        		cellNo.append(noChars.charAt(new Double(Math.floor((yuShu-Math.pow(stringSize, 1))/stringSize)).intValue()));// 除以26^2的余数再除以26的商
        		cellNo.append(noChars.charAt(new Double(yuShu%stringSize).intValue()));// 除以26^2的余数再除以26的余数
        } else if (colIx/stringSize >= 1) {//26-701
        	// 两位
        	cellNo.append(noChars.charAt(new Double(Math.floor((colIx-Math.pow(stringSize, 1))/stringSize)).intValue()));// 除以26的商
        	cellNo.append(noChars.charAt(new Double(colIx%stringSize).intValue()));// 除以26的余数
        } else {
        	// 一位
        	cellNo.append(noChars.charAt(colIx/1));
        }
        cellNo.append(rowNum+1);
        
        return cellNo.toString();
    }
    
    /**
     * 根据单元格的对应编号获取列索引
     * @param cellNo 单元格的对应编号（A1、B1、AA1、AAA1、……）
     * @return
     */
    public static int getColIndexFromCellNo(String cellNo){
    	// 取得列编号
    	String colNo = StringUtils.isNotEmpty(cellNo) ? cellNo.replaceAll("\\d+", "") : "";
    	// 用于获取单元格编号的char
    	String noChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	List<Integer> indexOfChar = new ArrayList<Integer>();
    	for (int i = 0; i < colNo.length(); i++) {
    		indexOfChar.add(noChars.indexOf(colNo.charAt(i)));
    	}
    	int index = 0;
    	switch (indexOfChar.size()) {
    		case 1:
    			index = indexOfChar.get(0);
    			break;
    		case 2:
    			index = (indexOfChar.get(0) + 1) * noChars.length() + indexOfChar.get(1);
    			break;
    		case 3:
    			index = (indexOfChar.get(0) + 1) * noChars.length() * noChars.length() + (indexOfChar.get(1) + 1) * noChars.length() + indexOfChar.get(2);
    			break;
    		default:
    			logger.error("暂不支持的编号");
    	}
    	return index;
    }
    
    /**
     * 表头父子关系并绑定单元格编号（第一个sheet的全部内容）
     * @param result 要返回的结果信息数组
     * @param sheet 要处理的sheet页
     */
    public static void bindingCellNo(List<Map<String,String>> result,Sheet sheet){
    	// 将所有合并的单元格放在list中
    	List<CellRangeAddress> mergerCellList = new ArrayList<CellRangeAddress>(); 
    	for (int mergerIx = 0; mergerIx < sheet.getNumMergedRegions(); mergerIx++) {
    		mergerCellList.add(sheet.getMergedRegion(mergerIx));
    	}
    	
    	// 处理当前页，循环读取每一行
        for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row xssfRow = sheet.getRow(rowNum);
            if (xssfRow == null) {
            	continue;
            }
            int minColIx = xssfRow.getFirstCellNum();
            int maxColIx = xssfRow.getLastCellNum();
            // 根据编号排序
            Map<String,String> row = new LinkedHashMap<String,String>();
            for (int colIx = minColIx; colIx < maxColIx; colIx++) {
                Cell cell = xssfRow.getCell(colIx);
                if (cell == null) {
                    continue;
                }
                // -------------------------- 获取表头父子关系并绑定表头编号（形如：A1、B1） START --------------------------
                // 取得表格编号
                String cellNo = getCellNo(rowNum, colIx);
                
                // 判定是否是斜线表头(斜线表头只保存标识列的表头名，不保存标识行的表头名)
                if (cell.toString().matches("^\\s*[\\u4e00-\\u9fa5 a-z A-Z 0-9]+(\\n[\\u4e00-\\u9fa5 a-z A-Z 0-9]+)+")) {
                	// 取得标识列的表头名(一般是最后一行用空白符分隔开的内容)
                	String[] contents = cell.toString().split("\\n");
                	String[] colHeads = ((contents != null && contents.length > 0) ? contents[contents.length-1] : "").trim().replaceAll("\\s+", "#|#").split("#\\|#");
                	for (CellRangeAddress ca : mergerCellList) {
                		if (colIx == ca.getFirstColumn() && rowNum == ca.getFirstRow() && colHeads.length == (ca.getLastColumn()-ca.getFirstColumn()+1)) {
                			// 一个colHead对应一列
                			for (int colHeadIx = 0; colHeadIx < colHeads.length; colHeadIx++) {
                				// 将合并列的所有单元格标号拼接为 A1_A2_A3 的格式
                				StringBuilder colNo = new StringBuilder("");   
                				for (int rowNm = ca.getFirstRow(); rowNm <= ca.getLastRow(); rowNm++) {
                					colNo.append(getCellNo(rowNm, colIx+colHeadIx));
                					colNo.append("_");
                				}
                				row.put(colNo.length() > 0 ? colNo.deleteCharAt(colNo.length()-1).toString() : "", colHeads[colHeadIx]);
                			}
                			break;
                		}
                	}
                } else {
                	// 若单元格值为公式则在之前拼上等号
                	String cellValue = null;
                	if (StringUtils.isEmpty(cell.toString())) {
                		cellValue = "";
                	} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                		cellValue = excelFormulaHandle(cell.getCellFormula(), cell.getNumericCellValue(), rowNum, "PRE_PROCESS");
                	} else {
                		cellValue = cell.toString();
                	}
                	// 判断是否是合并的单元格
                	boolean isMergedCell = false;
                	for (CellRangeAddress ca : mergerCellList) {
                		if (colIx == ca.getFirstColumn() && rowNum == ca.getFirstRow()) {
                			// 是合并的单元格
                			isMergedCell = true;
                			// 将合并的所有单元格标号拼接为 A1_B1_C1_A2_B2_C2 的格式
                			StringBuilder colNo = new StringBuilder("");
                			for (int colNm = ca.getFirstColumn(); colNm <= ca.getLastColumn(); colNm++) {
                				for (int rowNm = ca.getFirstRow(); rowNm <= ca.getLastRow(); rowNm++) {
                					colNo.append(getCellNo(rowNm, colNm));
                					colNo.append("_");
                				}
                			}
                			row.put(colNo.length() > 0 ? colNo.deleteCharAt(colNo.length()-1).toString() : "", cellValue);
                			break;
                		}
                	}
                	// 不是合并的单元格
                	if (!isMergedCell) row.put(cellNo, cellValue);
                }
                // -------------------------- 获取表头父子关系并绑定表头编号（形如：A1、B1） END --------------------------
            }
            result.add(row);
        }
        
        // 将中间的空行进行填充（只填充一个A*）
        for (int rowIx = 0; rowIx < result.size(); rowIx++) {
        	if (result.get(rowIx).isEmpty()) {
        		result.get(rowIx).put("A"+(rowIx+1), "");
        	} else if (Integer.parseInt(result.get(rowIx).keySet().iterator().next().replaceAll("[A-Z]+", "").split("_")[0]) > rowIx+1) {
        		// 当前行之前有一空行，填充内容并添加到此行之前
        		Map<String,String> map = new LinkedHashMap<String,String>();
        		map.put("A"+(rowIx+1), "");
        		result.add(rowIx, map);
        	} else if (Integer.parseInt(result.get(rowIx).keySet().iterator().next().replaceAll("[A-Z]+", "").split("_")[0]) < rowIx+1) {
        		// 当前行的前一行是多余的，删除之
        		result.remove(result.get(rowIx));
        	}
        }
    }
    
    /**
     * 改造poi默认的toString（）方法如下
     * @Title: getStringVal 
     * @Description: 1.对于不熟悉的类型，或者为空则返回""控制串
     *               2.如果是数字，则修改单元格类型为String，然后返回String，这样就保证数字不被格式化了
     * @param @param cell
     * @param @return    设定文件 
     * @return String    返回类型 
     * @throws
     */
    public static String getStringVal(HSSFCell cell) {
        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_BOOLEAN:
            return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
        case Cell.CELL_TYPE_FORMULA:
            return cell.getCellFormula();
        case Cell.CELL_TYPE_NUMERIC:
            cell.setCellType(Cell.CELL_TYPE_STRING);
            return cell.getStringCellValue();
        case Cell.CELL_TYPE_STRING:
            return cell.getStringCellValue();
        default:	
            return "";
        }
    }
    
	/**
     * read读取excel并进行标记单元格编号及其对应内容（第一个sheet的全部内容）
     * @Description: 处理Xlsx文件或Xls文件
     * @param @param path excel文件路径
     * @param @return
     * @param @throws Exception    设定文件 
     * @return List<Map<String,String>>    返回类型<br/>
     * 
     * 1.先用InputStream获取excel文件的io流
     * 2.然后穿件一个内存中的excel文件HSSFWorkbook类型对象，这个对象表示了整个excel文件。
     * 3.对这个excel文件的每页做循环处理
     * 4.对每页中每行做循环处理
     * 5.对每行中的每个单元格做处理，获取这个单元格的值
     * 6.把这行的结果添加到一个List数组中
     * 7.把每行的结果添加到最后的总结果中
     * 8.解析完以后就获取了一个List<List<String>>类型的对象了
     */
    public static List<Map<String,String>> readExcel(String path) throws Exception {
        List<Map<String,String>> result = new ArrayList<Map<String,String>>();
        try {
        	// xls
        	InputStream is = new FileInputStream(path);
        	HSSFWorkbook workbook = new HSSFWorkbook(is);
        	int size = workbook.getNumberOfSheets();
        	// 仅取第一页
        	for (int numSheet = 0; numSheet < size; numSheet++) {
        		// HSSFSheet 标识某一页
        		HSSFSheet hssfSheet = workbook.getSheetAt(numSheet);
        		if (hssfSheet == null) {
        			continue;
        		}
        		bindingCellNo(result,hssfSheet);
        		break;
        	}
        } catch (OfficeXmlFileException e) {
        	// 不是xls文件，使用xlsx的解析器
        	// xlsx
        	InputStream is = new FileInputStream(path);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
            // 仅取第一页
            for (XSSFSheet xssfSheet : xssfWorkbook) {
                if (xssfSheet == null) {
                    continue;
                }
                bindingCellNo(result,xssfSheet);
                break;
            }
        }
        return result;
    }
    
    /**
     * 输出到excel（文件名：模板文件名+"_汇总"+时间戳）
     * @param result 包含所有已汇总数据的结果集
     * @param templatePath 模板文件路径
     * @param verticalSummary 需要纵向汇总的列
     * @return 输出到的文件的路径
     * @throws Exception
     */
    public static String writeToExcel(List<Map<String,String>> result, String templatePath, List<String> verticalSummary, String summaryType, JSONObject simpleSummaryOption) throws Exception{
    	NumberFormat nf = NumberFormat.getInstance(); 
		nf.setGroupingUsed(false);//非科学计数法显示
    	
    	// 包含所有已汇总数据的结果集
    	List<Map<String,String>> templateResult = CommonUtil.removeEmptyValueMapFromResultAfter(readExcel(templatePath));
    	// 第一个数据行的行号
    	int firstDataRowNm = 1;
    	// 最后一行数据行的行号
    	int lastDataRowNm = 1;
    	
    	summaryType = StringUtils.isNotEmpty(summaryType) ? summaryType : "ROW_PUSH";
    	// 根据汇总方式类型取得第一个数据行的行号、最后一行数据行的行号
    	switch (summaryType) {
    		case "ROW_PUSH":
    			firstDataRowNm = templateResult.size()+1;
    			lastDataRowNm = result.size();
    			break;
    		case "CELL_SUM":
    			firstDataRowNm = StringUtils.isNotEmpty(simpleSummaryOption.get("dataStartRowNum")) ? simpleSummaryOption.getInt("dataStartRowNum") : 1;
    			lastDataRowNm = StringUtils.isNotEmpty(simpleSummaryOption.get("dataEndRowNum")) ? 
    					(verticalSummary != null && !verticalSummary.isEmpty() ? simpleSummaryOption.getInt("dataEndRowNum")+1 : simpleSummaryOption.getInt("dataEndRowNum")) : result.size();
    			break;
    		default:
    			firstDataRowNm = 1;
    			lastDataRowNm = result.size();
    	}
    	
    	// 汇总后的表格(该写法不可动，否则会出现读不了头的奇怪问题)
    	File excelFile = new File(templatePath.indexOf(".xlsx") > 0 ? 
    			templatePath.replace(".xlsx", "_汇总"+(new Date()).getTime()+".xlsx") 
    			: templatePath.replace(".xls", "_汇总"+(new Date()).getTime()+".xls"));
    	if (!excelFile.exists()) {
    		excelFile.createNewFile();
    	}
    	// 获取excel工作簿
    	try {
        	// xls
    		InputStream fileIs = new FileInputStream(templatePath);
        	HSSFWorkbook workbook = new HSSFWorkbook(fileIs);
        	// 单元格样式（仅边框）
        	CellStyle cellStyleNomal = workbook.createCellStyle();
        	cellStyleNomal.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框  
        	cellStyleNomal.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框  
        	cellStyleNomal.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框  
        	cellStyleNomal.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框 
        	// 单元格样式（边框、居中）
        	CellStyle cellStyleCenter = workbook.createCellStyle();
        	cellStyleCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框  
        	cellStyleCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框  
        	cellStyleCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框  
        	cellStyleCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        	cellStyleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
        	cellStyleCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        	// 获取工作表sheet
        	HSSFSheet sheet=workbook.getSheetAt(0);
        	// 计算公式
        	sheet.setForceFormulaRecalculation(true);
        	// 获取结果集中第一个数据行的行号对应的行
            for (int rowIx = firstDataRowNm-1; rowIx < lastDataRowNm; rowIx++) {
            	HSSFRow nrow = null;
            	// 若当前写的单元格所在的行在模板文件已有，则从模板文件获取该行
            	if (rowIx <= templateResult.size()-1) {
            		nrow = sheet.getRow(rowIx);
            		nrow = nrow != null ? nrow : sheet.createRow(rowIx);
            	} else {
            		// 否则，创建行
            		nrow = sheet.createRow(rowIx);
            	}
            	int cellIx = 0;
            	// 有纵向加和且为数据最后一行
        		for (String cellNo : result.get(rowIx).keySet()) {
        			HSSFCell ncell = null;
        			// 若当前写的单元格在模板文件已有，则从模板文件获取该单元格
        			ncell = nrow.getCell((verticalSummary != null && !verticalSummary.isEmpty() && rowIx == (result.size()-1)) ? getColIndexFromCellNo(cellNo) : cellIx);
        			// 当前写的单元格是否存在在模板文件中
        			boolean isNcellExist = ncell != null;
                	// 获取不到则创建
        			if (!isNcellExist) {
        				ncell=nrow.createCell(
        						(verticalSummary != null && !verticalSummary.isEmpty() && rowIx == (result.size()-1)) ? getColIndexFromCellNo(cellNo) : cellIx);
        				// 设置单元格样式（样式只能使用固定样式，因为POI要求单元格的样式与单元格只能同属于一个工作簿，汇总文件的样式无法应用于模板文件。）
                		ncell.setCellStyle(cellStyleNomal);
                	}
        			
        			cellIx++;
        			// 若可转为double则值为数字，则不显示科学计数法
            		try{
            			Double tempValue = Double.valueOf(result.get(rowIx).get(cellNo));
            			ncell.setCellValue(tempValue);
            			// 解决excel显示科学计数法的问题（暂时在MSExcel2013中未发现此类问题，故下方代码暂时先注释掉）
//            			if (tempValue.toString().indexOf("E") >= 0) {
//            				// 当前显示为科学计数法，则显示为string
//            				ncell.setCellValue(nf.format(tempValue));
//            			} else {
//            				// 当前显示不为科学计数法，则显示为number
//            				ncell.setCellValue(tempValue);
//            			}
            		}catch(NumberFormatException e){
            			// 非数值类型
            			// 是公式则设置单元格的公式为当前公式
            			if (result.get(rowIx).get(cellNo).startsWith("=")) {
            				ncell.setCellFormula(result.get(rowIx).get(cellNo).replace("=", ""));
            			} else {
            				ncell.setCellValue(result.get(rowIx).get(cellNo));
            			}
            		}
            		
            		// 若单元格是创建的，则添加需要合并的单元格
            		if (!isNcellExist && StringUtils.isNotEmpty(cellNo) && cellNo.split("_").length > 1) {
            			// 取得合并的单元格第一个单元格的编号和最后一个单元格的编号（由于编号绑定方式为从上到下、从左到右的逐行遍历方式，故第一个单元格为编码第一段，最后一个单元格为编码最后一段）
            			String firstCellNo = cellNo.split("_")[0];
            			String lastCellNo = cellNo.split("_")[cellNo.split("_").length-1];
            			CellRangeAddress region = new CellRangeAddress(
            					Integer.parseInt(firstCellNo.replaceAll("[A-Z]+", ""))-1, 
            					Integer.parseInt(lastCellNo.replaceAll("[A-Z]+", ""))-1, 
            					getColIndexFromCellNo(firstCellNo), 
            					getColIndexFromCellNo(lastCellNo));
            			sheet.addMergedRegion(region);
            			ncell.setCellStyle(cellStyleCenter);
            		}
        		}
            }
        	// 将结果集内容写入excelFile中
            FileOutputStream out=new FileOutputStream(excelFile);
            out.flush();
            workbook.write(out);
            out.close();
        } catch (OfficeXmlFileException e) {
        	// 不是xls文件，使用xlsx的解析器
        	// xlsx
        	InputStream fileIs = new FileInputStream(templatePath);
        	XSSFWorkbook workbook = new XSSFWorkbook(fileIs);
        	// 单元格样式（仅边框）
        	CellStyle cellStyleNomal = workbook.createCellStyle();
        	cellStyleNomal.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框  
        	cellStyleNomal.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框  
        	cellStyleNomal.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框  
        	cellStyleNomal.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框 
        	// 单元格样式（边框、居中）
        	CellStyle cellStyleCenter = workbook.createCellStyle();
        	cellStyleCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框  
        	cellStyleCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框  
        	cellStyleCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框  
        	cellStyleCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        	cellStyleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
        	cellStyleCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        	// 获取工作表sheet
        	XSSFSheet sheet=workbook.getSheetAt(0);
        	// 计算公式
        	sheet.setForceFormulaRecalculation(true);
        	// 获取结果集中第一个数据行的行号对应的行
            for (int rowIx = firstDataRowNm-1; rowIx < lastDataRowNm; rowIx++) {
            	XSSFRow nrow = null;
            	// 若当前写的单元格所在的行在模板文件已有，则从模板文件获取该行
            	if (rowIx <= templateResult.size()-1) {
            		nrow = sheet.getRow(rowIx);
            	} else {
            		// 否则，创建行
            		nrow = sheet.createRow(rowIx);
            	}
            	// 写入数据
            	int cellIx = 0;
            	for (String cellNo : result.get(rowIx).keySet()){
            		XSSFCell ncell = null;
        			// 若当前写的单元格在模板文件已有，则从模板文件获取该单元格
        			ncell = nrow.getCell((verticalSummary != null && !verticalSummary.isEmpty() && rowIx == (result.size()-1)) ? getColIndexFromCellNo(cellNo) : cellIx);
                	// 获取不到则创建
        			if (ncell == null) {
        				ncell=nrow.createCell(
        						(verticalSummary != null && !verticalSummary.isEmpty() && rowIx == (result.size()-1)) ? getColIndexFromCellNo(cellNo) : cellIx);
        				// 设置单元格样式（样式只能使用固定样式，因为POI要求单元格的样式与单元格只能同属于一个工作簿，汇总文件的样式无法应用于模板文件。）
                		ncell.setCellStyle(cellStyleNomal);
                	}
        			
        			cellIx++;
            		// 若可转为double则值为数字，则不显示科学计数法
            		try{
            			Double tempValue = Double.valueOf(result.get(rowIx).get(cellNo));
            			ncell.setCellValue(tempValue);
            		}catch(NumberFormatException e1){
            			// 非数值类型
            			// 是公式则设置单元格的公式为当前公式
            			if (result.get(rowIx).get(cellNo).startsWith("=")) {
            				ncell.setCellFormula(result.get(rowIx).get(cellNo).replace("=", ""));
            			} else {
            				ncell.setCellValue(result.get(rowIx).get(cellNo));
            			}
            		}
            		
            		// 添加需要合并的单元格
            		if (StringUtils.isNotEmpty(cellNo) && cellNo.split("_").length > 1) {
            			// 取得合并的单元格第一个单元格的编号和最后一个单元格的编号（由于编号绑定方式为从上到下、从左到右的逐行遍历方式，故第一个单元格为编码第一段，最后一个单元格为编码最后一段）
            			String firstCellNo = cellNo.split("_")[0];
            			String lastCellNo = cellNo.split("_")[cellNo.split("_").length-1];
            			CellRangeAddress region = new CellRangeAddress(
            					Integer.parseInt(firstCellNo.replaceAll("[A-Z]+", ""))-1, 
            					Integer.parseInt(lastCellNo.replaceAll("[A-Z]+", ""))-1, 
            					getColIndexFromCellNo(firstCellNo), 
            					getColIndexFromCellNo(lastCellNo));
            			sheet.addMergedRegion(region);
            			ncell.setCellStyle(cellStyleCenter);
            		}
            	}
            }
        	// 将结果集内容写入excelFile中
            FileOutputStream out=new FileOutputStream(excelFile);
            out.flush();
            workbook.write(out);
            out.close();
        }
    	return excelFile.getPath();
    }
    
    /**
     * 取得 仅最后一行表头，无父类 的表头信息
     * @param templateHeaders 包含所有表头单元格编号及值的结果
     * @return 单元格编号为字典排序的结果，如：{"B2":"金融机构编码","C2":"内部机构号","D2":"许可证号"}
     */
    public static Map<String, String> getTemplateHeadersWithoutParent(List<Map<String, String>> templateHeaders){
    	Map<String, String> templateHeadersWithoutParent = new TreeMap<String, String>();
    	
    	// 先去除参数中的空行
    	List<Map<String, String>> templateHeadersWithoutEmptyRow = CommonUtil.removeEmptyValueMapFromResultAfter(templateHeaders);
    	
		// 取表头最后一行，并去除值为空的项
		if (templateHeadersWithoutEmptyRow != null && !templateHeadersWithoutEmptyRow.isEmpty()) {
			Map<String, String> templateHeadersOld = templateHeadersWithoutEmptyRow.get(templateHeadersWithoutEmptyRow.size()-1);
			for (String key : templateHeadersOld.keySet()) {
				if (StringUtils.isNotEmpty(templateHeadersOld.get(key))) {
					templateHeadersWithoutParent.put(key, templateHeadersOld.get(key));
				}
			}
			// 将合并的表头也添加到表头列表中
			// 从表头第一行开始遍历
			Map<String, String> templateHeaderInfo = getTemplateHeaderInfo(templateHeaders);
			for (int mapIx = (Integer.parseInt(templateHeaderInfo.get("firstRowNum"))-1); mapIx < templateHeadersWithoutEmptyRow.size(); mapIx++) {
				for (String key : templateHeadersWithoutEmptyRow.get(mapIx).keySet()) {
					// 合并的单元格
					if (StringUtils.isNotEmpty(key) && key.split("_").length > 1) {
						// 包含模板最后一行
						String[] cellNos = key.split("_");
						StringBuilder cellNoLastRow = new StringBuilder(""); 
						for (String cellNo : cellNos) {
							// 取得纯纵向合并的单元格的最后一行的单元格编号
							if (cellNo.replaceAll("[A-Z]+", "").equals(String.valueOf(templateHeadersWithoutEmptyRow.size()))) {
								cellNoLastRow.append(cellNo);
								cellNoLastRow.append("_");
							}
						}
						// 当前单元格不包含最后一行
						if (cellNoLastRow.length() > 0) {
							// 针对多行、多列合并的，cellNo为该合并单元格最后一行所有编号的合集
							templateHeadersWithoutParent.put(cellNoLastRow.substring(0, cellNoLastRow.length()-1), templateHeadersWithoutEmptyRow.get(mapIx).get(key));
						}
					}
				}
			}
		} else {
			logger.warn("参数templateHeaders中没有有效的行");
		}
		logger.debug("返回值："+JSONObject.fromObject(templateHeadersWithoutParent).toString());
    	return templateHeadersWithoutParent;
    }
    
    /**
     * 取得表头每一层级的已拼上父类项的子项的结果集
     * @param templateHeaders 包含所有表头单元格编号及值的结果
     * @return 已拼上父类项的子项的结果集
     */
    public static List<Map<String, String>> getEachLevelHeaderWithParentInfo(List<Map<String, String>> templateHeaders) {
    	// 用于临时保存结果，最后需要输出为Map
    	List<Map<String, String>> tempResult = new ArrayList<Map<String, String>>();
    	
    	Map<String, String> templateHeaderInfo = getTemplateHeaderInfo(templateHeaders);
    	// 第一行的行号(非索引)
    	int firstRowNum = Integer.parseInt(templateHeaderInfo.get("firstRowNum"));
    	// 最后一行行号(非索引)
    	int lastRowNum = Integer.parseInt(templateHeaderInfo.get("lastRowNum"));
    	
    	// 有父类
		if ((lastRowNum - firstRowNum + 1) >= 2) {
			tempResult.add(getTemplateHeadersWithoutParent(templateHeaders));
				
			// 临时存储每一行的处理结果
			Map<String, String> tempWithParent = null;
			for (int tempReslutIx = 0; tempReslutIx < tempResult.size(); tempReslutIx++) {
				// （父类项）从倒数第二行逐行遍历
				int headerIx = lastRowNum - 2 - tempReslutIx;
				// 遍历到表头第一行结束
				if (headerIx+1 < firstRowNum) break;
				Map<String, String> templateHeader = templateHeaders.get(headerIx);
				
				tempWithParent = new TreeMap<String, String>();
				Map<String, String> tempReslutMap = tempResult.get(tempReslutIx);
				
				// 根据最后一行表头找到对应父类，并拼上父类的汉字到最前面
				for (String cCellNo : tempReslutMap.keySet()) {
					for (String pCellNo : templateHeader.keySet()) {
						// pCellNo是cCellNo的父类
						if (StringUtils.isNotEmpty(pCellNo) && StringUtils.isNotEmpty(templateHeader.get(pCellNo)) // 值不为空即不是合并单元格的副单元格
								&& !pCellNo.equals(cCellNo) // 不是自己
								// 不包含最后一行表头的单元格编号
								&& !isInclude(pCellNo,cCellNo.split("-")[0],"ALL")
								// 包含最后一行表头的单元格列号
								&& isInclude(pCellNo,cCellNo.split("-")[0],"COL")) {
							tempWithParent.put(pCellNo+"-"+cCellNo, templateHeader.get(pCellNo)+"-"+tempReslutMap.get(cCellNo));
						} else {
							tempWithParent.put(cCellNo, tempReslutMap.get(cCellNo));
						}
					}
				}
				
				logger.debug("当前子项行【"+(headerIx+2)+"】父类查找结果："+JSONObject.fromObject(tempWithParent).toString());
				if (!tempWithParent.isEmpty()) {
					// 滤掉多余的项
					Map<String, String> tempMap = new TreeMap<String, String>();
					// 取得当前层级数（即key中用"-"分隔后，最多被分隔成几个部分）
					int level = 0;
					for (String key : tempWithParent.keySet()) {
						if (key.split("-").length > level) {
							level = key.split("-").length;
						}
					}
					// 将单个的单元格编号的key取出放到tempMap中
					for (String key : tempWithParent.keySet()) {
						if (key.split("-").length < level) {
							tempMap.put(key, tempWithParent.get(key));
							tempWithParent.put(key, null);
						}
					}
					// 遍历单个的单元格编号的key，将已有父类项、又有单独子项的单元格编号的单独子项去除（如：有B2，又有B1_C1_D1_E1-B2的，去除前者，保留后者）
					for (String key : tempMap.keySet()) {
						boolean isExist = false;
						for (String keyWithParent : tempWithParent.keySet()) {
							if (StringUtils.isNotEmpty(keyWithParent)  
									// 判定是否有父类项的key已经包含子项的情况（若子项不包含"-"，即单独编号的，取父类项的最后一个"-"分隔后的部分与子项比较；若包含包含"-"，即包含前一级父类项编号的，直接判定父类项是否结束与子项key）
									&& ((key.indexOf("-") < 0 && keyWithParent.replaceAll("\\d+", "").split("-")[keyWithParent.split("-").length-1].equals(key.replaceAll("\\d+", ""))) 
											|| (key.indexOf("-") >= 0 && keyWithParent.replaceAll("\\d+", "").endsWith(key.replaceAll("\\d+", "")))) 
									&& StringUtils.isNotEmpty(tempWithParent.get(keyWithParent))) {
								isExist = true;
								break;
							}
						}
						if (!isExist) tempWithParent.put(key, tempMap.get(key));
					}
					
					tempResult.add(CommonUtil.removeEmptyValueElem(tempWithParent));
				} else {
					logger.warn("当前子项行号【"+(headerIx+2)+"】的行为发现符合要求的结果");
				}
			}
		} else {
			logger.warn("当前参数的结果集的表头无父类项");
		}
		
		logger.debug("返回结果："+JSONArray.fromObject(tempResult).toString());
		return tempResult;
    }
    
    /**
     * 取得 有父类 的表头信息
     * @param templateHeaders 包含所有表头单元格编号及值的结果
     * @return 单元格编号为字典排序的结果，如：{"B2":"金融机构-金融机构编码","C2":"金融机构-内部机构号","D2":"金融机构-许可证号"}
     */
    public static Map<String, String> getTemplateHeadersWithParent(List<Map<String, String>> templateHeaders){
    	Map<String, String> templateHeadersWithParent = new TreeMap<String, String>();
    	
		List<Map<String, String>> tempResult = getEachLevelHeaderWithParentInfo(templateHeaders);
		
		// 将tempResult中的结果转存到最终结果中
		if (!tempResult.isEmpty()) {
			Map<String,String> tempResultOne = tempResult.get(tempResult.size()-1);
			logger.debug("最终父类项的结果："+JSONObject.fromObject(tempResultOne).toString());
			for (String key : tempResultOne.keySet()) {
				templateHeadersWithParent.put(key.indexOf("-") == -1 ? key : key.split("-")[key.split("-").length-1], tempResultOne.get(key));
			}
		} else {
			logger.warn("未取得父类项结果");
		}
		logger.debug("返回值："+JSONObject.fromObject(templateHeadersWithParent).toString());
    	return templateHeadersWithParent;
    }
    
    /**
     * 取得 除最后一行的纯父类 的表头信息
     * @param templateHeaders 包含所有表头单元格编号及值的结果
     * @return 单元格编号为字典排序的结果，如：{"B1_C1_D1_E1_F1_G1":"金融机构","C6_D6_E6_F6_G6_H6_I6-E7_F7_G7":"柜面现金收入-企业"}
     */
    public static Map<String, String> getTemplateHeaderParents(List<Map<String, String>> templateHeaders){
    	Map<String, String> templateHeaderParents = new TreeMap<String, String>();
    	
    	List<Map<String, String>> tempResult = getEachLevelHeaderWithParentInfo(templateHeaders);
    	
    	// 将tempResult中的结果转存到最终结果中
		if (!tempResult.isEmpty()) {
			Map<String,String> tempResultOne = tempResult.get(tempResult.size()-1);
			logger.debug("最终父类项的结果："+JSONObject.fromObject(tempResultOne).toString());
			for (String key : tempResultOne.keySet()) {
				if (key.indexOf("-") >= 0) {
					String[] keyCellNos = key.split("-");
					String[] valueCellNos = tempResultOne.get(key).split("-");
					for (int keyIx = 1; keyIx < keyCellNos.length; keyIx++) {
						// 父类项的key
						StringBuilder parentKey = new StringBuilder("");
						// 父类项的value
						StringBuilder parentValue = new StringBuilder("");
						for (int keyOneIx = 0; keyOneIx < keyIx; keyOneIx++) {
							parentKey.append(keyCellNos[keyOneIx]);
							parentKey.append("-");
							parentValue.append(valueCellNos[keyOneIx]);
							parentValue.append("-");
						}
						templateHeaderParents.put(parentKey.substring(0, parentKey.length()-1), parentValue.substring(0, parentValue.length()-1));
					}
				}
			}
		} else {
			logger.warn("未取得父类项结果");
		}
		logger.debug("返回值："+JSONObject.fromObject(templateHeaderParents).toString());
    	return templateHeaderParents;
    }
    
    /**
     * 取得表头信息
     * @param templateHeaders 包含所有表头单元格编号及值的结果
     * @return 表头信息（说明：<br/>
     * 		firstRowNum：第一行的行号(非索引)；<br/>
     * 		lastRowNum：最后一行行号(非索引)；）
     */
    public static Map<String, String> getTemplateHeaderInfo(List<Map<String, String>> templateHeaders){
    	Map<String, String> templateHeaderInfo = null;
    	if (templateHeaders != null && !templateHeaders.isEmpty()) {
    		// 先从最后一行开始去除后方的所有空行，到不为空行为止
        	List<Map<String, String>> templateHeadersWithoutEmptyRow = CommonUtil.removeEmptyValueMapFromResultAfter(templateHeaders);
    		
    		templateHeaderInfo = new HashMap<String, String>();
    		templateHeaderInfo.put("lastRowNum", templateHeadersWithoutEmptyRow.get(templateHeadersWithoutEmptyRow.size()-1).keySet().toArray()[0].toString().replaceAll("[A-Z]+", ""));
    		
    		// 从最后一行开始向上逐行遍历
    		for (int rowIx = Integer.parseInt(templateHeaderInfo.get("lastRowNum"))-1; rowIx >= 1; rowIx--) {
    			// 子项行
    			Map<String, String> childRow = templateHeadersWithoutEmptyRow.get(rowIx);
    			// 父类项行
    			Map<String, String> parentRow = templateHeadersWithoutEmptyRow.get(rowIx-1);
    			// 表头内父类项行的size应等于子项行，大于、小于均可判为不是表头
    			if (parentRow.size() != childRow.size()) {
    				templateHeaderInfo.put("firstRowNum",String.valueOf(rowIx+1));
    				logger.debug("表头内父类项行的size不等于子项行，此时判定表头第一行行号为："+templateHeaderInfo.get("firstRowNum"));
    				break;
    			}
    			for (String cCellNo : childRow.keySet()) {
    				if (StringUtils.isEmpty(childRow.get(cCellNo))) {
    					continue;
    				}
    				boolean hasParent = false;
    				for (String pCellNo : parentRow.keySet()) {
        				if (StringUtils.isNotEmpty(parentRow.get(pCellNo)) // 子项、父类项都不为空
        						// 父类项包含子项的编号
        						&& ((cCellNo.indexOf("_") == -1 && Arrays.asList(pCellNo.replaceAll("\\d+", "").split("_")).contains(cCellNo.replaceAll("\\d+", ""))) 
        								|| (cCellNo.indexOf("_") >= 0 && pCellNo.replaceAll("\\d+", "").indexOf(cCellNo.replaceAll("\\d+", "")) >= 0))) {
        					hasParent = true;
        					break;
        				}
        			}
    				if (!hasParent) {
     					// 是否是父类项是纵向合并的情况（此时也会被判定为子项的编号不被任何父类项包含）
     					for (int mergeCellRowIx = rowIx-1; mergeCellRowIx >= 0; mergeCellRowIx--) {
     						Map<String,String> mergeCellRow = templateHeadersWithoutEmptyRow.get(mergeCellRowIx);
     						for (String pCellNo : mergeCellRow.keySet()) {
     							// 是否包含子项
     							if (StringUtils.isNotEmpty(mergeCellRow.get(pCellNo)) 
     									// 父类项包含子项的编号
     	        						&& ((cCellNo.indexOf("_") == -1 && Arrays.asList(pCellNo.replaceAll("\\d+", "").split("_")).contains(cCellNo.replaceAll("\\d+", ""))) 
     	        								|| (cCellNo.indexOf("_") >= 0 && pCellNo.replaceAll("\\d+", "").indexOf(StringUtils.join(new HashSet<String>(Arrays.asList(cCellNo.replaceAll("\\d+", "").split("_"))), "_")) >= 0))) {
     								hasParent = true;
     								break;
     							}
     						}
     						if (hasParent) {
     							break;
     						} else {
     							// 是否存在一个子项无父类项
     							boolean isHasOneChildNotHaveParent = false;
     							for (String pCellNo : mergeCellRow.keySet()) {
     								if (pCellNo.replaceAll("\\d+", "").split("_")[0].equals(cCellNo.replaceAll("\\d+", "").split("_")[0])
     										&& pCellNo.split("_").length < cCellNo.split("_").length) {
     									// 找到了一个当前不是当前子项的父类项的 子项第一个单元格相对应的单元格，即确定了当前子类不存在父类
     									isHasOneChildNotHaveParent = true;
     									break;
     								}
     							}
     							if (isHasOneChildNotHaveParent) break;
     						}
     					}
     					if (!hasParent) {
     						templateHeaderInfo.put("firstRowNum",String.valueOf(rowIx+1));
     						logger.debug("经判定，当前子项编号【"+cCellNo+"】无父类项，此时判定表头第一行行号为："+templateHeaderInfo.get("firstRowNum"));
     						break;
     					}
    				}
    			}
    			if (StringUtils.isNotEmpty(templateHeaderInfo.get("firstRowNum"))) break;
    		}
    		
    		templateHeaderInfo.put("firstRowNum", StringUtils.isNotEmpty(templateHeaderInfo.get("firstRowNum")) ? templateHeaderInfo.get("firstRowNum") : "1");
    	} else {
    		logger.error("参数【templateHeaders】为空");
    	}
    	
    	logger.debug("返回值："+JSONObject.fromObject(templateHeaderInfo).toString());
    	return templateHeaderInfo;
    }
    
    /**
     * 简单汇总-多行归总汇总：将所有汇总文件的所有行直接复制进最终汇总表中
     * @param result 包含所有已汇总数据的结果集
     * @param filePathList 包含所有要汇总的的excel的路径
     * @param headerEndRowNum 表头结束行号
     * @param dataStartRowNum 每一个汇总文件的一个数据行的行号（不包含表头）
     * @param dataEndRowNum 每一个汇总文件的最后一个数据行的行号（不包含表头）
     * @param direction startRowNum、endRowNum所适用的方向（FORWARD：自上而下；BACKWARD：自下而上）
     * @throws Exception
     */
    private static String summarySimpleRowPush(List<Map<String,String>> result, List<String> filePathList, int headerEndRowNum, int dataStartRowNum, int dataEndRowNum, String direction) throws Exception{
    	// 遍历所有报表路径，将报表中所有数据行都加入到result中，行数由第一个数据行的行号以此向下
    	for (String filePathOne : filePathList) {
    		List<Map<String,String>> filePathOneResult = CommonUtil.removeEmptyValueMapFromResultAfter(readExcel(filePathOne));
    			
    		// TODO 当前代码有一个问题：方向是自下而上时，会把备注信息算上。该问题解决需要获取数据行的实际开始行号与结束行号
			// 为进行正向遍历，需转换数据的开始行号与结束行号：行范围从1开始，为指定行范围方向下的行范围，如【自下而上的1~2】即为倒数第1~2行、【自上而下的1~2】即为第1~2行加表头最后一行行号
    		int startRowNum = 0;
    		int endRowNum = 0;
			if (ROWNM_DIRECTION_FORWARD.equals(direction)) {
				startRowNum = headerEndRowNum + dataStartRowNum;
				endRowNum = headerEndRowNum + dataEndRowNum;
			} else if (ROWNM_DIRECTION_BACKWARD.equals(direction)) {
				startRowNum = filePathOneResult.size() - dataEndRowNum + 1;
				endRowNum = filePathOneResult.size() - dataStartRowNum + 1;
			} else {
	    		// 不指定行范围，默认全部，即开始行号为表头最后一行加1，结束行号为当前文件最后一行行号
				startRowNum = headerEndRowNum + 1;
				endRowNum = filePathOneResult.size();
			}
    		
    		// 读取报表从第一个数据行的行号向下的所有行
    		for (int rowIx = startRowNum-1; rowIx < endRowNum; rowIx++) {
    			// 处理好后的行
    			Map<String,String> summaryRow = new LinkedHashMap<String,String>();
    			Map<String,String> row = filePathOneResult.get(rowIx);
    			
    			// 若整个行（数据行）都是空，则停止读取
    			boolean isAllCellEmpty = true;
    			for (String cellNo : row.keySet()) {
    				if (StringUtils.isNotEmpty(row.get(cellNo))) {
    					isAllCellEmpty = false;
    					break;
    				}
    			}
    			if (isAllCellEmpty) break; 
    			
            	for (String cellNo : row.keySet()) {
            		// 单元格值
            		String valueStr = row.get(cellNo);
            		// 汇总后列号需要考虑合并的情况
            		StringBuilder cellNoNew = null;
            		if (StringUtils.isNotEmpty(cellNo) && cellNo.split("_").length > 1) {
            			cellNoNew = new StringBuilder("");
            			// 所有原始行号
            			String[] rowNumOlds = cellNo.replaceAll("[A-Z]+", "").split("_");
            			// 所有原始列号
            			String[] colNos = cellNo.replaceAll("\\d+", "").split("_");
            			// 取得原始最小行号（所有合并单元格的最小行号就是当前单元格所在的行号）
            			int minRowNumOld = rowIx+1;
            			for (int colNoIx = 0; colNoIx < colNos.length; colNoIx++) {
            				// 单个单元格编号=原始列号+((原始行号-原始最小行号)+应当设置的对应行号)
            				cellNoNew.append(colNos[colNoIx]);
            				cellNoNew.append(String.valueOf((Integer.parseInt(rowNumOlds[colNoIx])-minRowNumOld)+result.size()+1));
            				cellNoNew.append("_");
            			}
            			cellNoNew = new StringBuilder(cellNoNew.substring(0, cellNoNew.length()-1));
            		} else {
            			cellNoNew = new StringBuilder(cellNo.replaceAll("\\d+", String.valueOf(result.size()+1)));
            		}
            		summaryRow.put(cellNoNew.toString(), valueStr);
            	}
            	result.add(summaryRow);
            }
    	}
    	
    	if (StringUtils.isNotEmpty(direction)) {
	    	// 为适应当前开始行号、结束行号，处理合并单元格及其包含的其他单元格
	    	// 1、处理结果集中包含的合并的单元格为适合当前开始行号、结束行号的合并单元格编号
	    	Map<String,List<String>> needDealMergerCell = new HashMap<String,List<String>>();
	    	for (int rowMergerIx = headerEndRowNum; rowMergerIx < result.size(); rowMergerIx++) {
	    		Map<String,String> rowMerger = result.get(rowMergerIx);
	    		for (String cellNoMerger : rowMerger.keySet()) {
	    			if (cellNoMerger.split("_").length > 1) {
	    				// 需要合并的单元格的新编号集
	    		    	List<String> nowMergerCellNos = new ArrayList<String>();
	    				// 将该合并的单元格编号中所有当前行的编号添加到该单元格新编号集中
	    				for (String currentRowCellNoMergerOne : cellNoMerger.split("_")) {
	    					if (currentRowCellNoMergerOne.replaceAll("[A-Z]+", "").equals(String.valueOf(rowMergerIx+1))) {
	    						nowMergerCellNos.add(currentRowCellNoMergerOne);
	    					}
	    				}
	    				// 将最终集合中的合并单元格所包含的集合添加到单元格新编号集中
	    				for (int rowIx = headerEndRowNum; rowIx < result.size(); rowIx++) {
	    					Map<String,String> row = result.get(rowIx);
	    					for (String cellNo : row.keySet()) {
	    						if (StringUtils.isEmpty(row.get(cellNo)) && cellNoMerger.indexOf(cellNo) >= 0) {
	    							// 第一列
		    						if ((cellNo.replaceAll("\\d+", "").equals(nowMergerCellNos.get(0).replaceAll("\\d+", "")) && Integer.parseInt(cellNo.replaceAll("[A-Z]+", "")) == Integer.parseInt(nowMergerCellNos.get(nowMergerCellNos.size()-1).replaceAll("[A-Z]+", ""))+1)
		    								// 第一行
		    								|| (cellNo.replaceAll("[A-Z]+", "").equals(nowMergerCellNos.get(0).replaceAll("[A-Z]+", "")) && getColIndexFromCellNo(cellNo) == getColIndexFromCellNo(nowMergerCellNos.get(nowMergerCellNos.size()-1)) + 1)
		    								// 除第一行与第一列外的其他
		    								|| (Integer.parseInt(cellNo.replaceAll("[A-Z]+", "")) == Integer.parseInt(nowMergerCellNos.get(nowMergerCellNos.size()-1).replaceAll("[A-Z]+", ""))+1 && getColIndexFromCellNo(cellNo) == getColIndexFromCellNo(nowMergerCellNos.get(nowMergerCellNos.size()-1))+1)) {
	    								nowMergerCellNos.add(cellNo);
	    							} else {
	        							break;
	        						}
	    						}
	    					}
	    				}
	    				// 加入行号0
	    				nowMergerCellNos.add(0, String.valueOf(rowMergerIx+1));
	    				// 加入单元格值1
	    				nowMergerCellNos.add(1, rowMerger.get(cellNoMerger));
	    				// 需要合并的单元格的新编号集从2开始
	    				needDealMergerCell.put(cellNoMerger, nowMergerCellNos);
	    			}
	    		}
	    	}
			// 将新的单元格编号修改到原始编号上，需要保持与原顺序号相同
	    	for (String needDealMergerCellOne : needDealMergerCell.keySet()) {
	    		List<String> nowMergerCellNos = needDealMergerCell.get(needDealMergerCellOne);
	    		int rowNum = Integer.parseInt(nowMergerCellNos.get(0));
	    		Map<String,String> rowMerger = result.get(rowNum-1);
	    		CommonUtil.replaceElemKey(rowMerger, needDealMergerCellOne, String.join("_", nowMergerCellNos.subList(2, nowMergerCellNos.size())), null);
	    	}
	    	// TODO 2、处理结果集中不包含的合并单元格所含有的单个集合为一个新的合并单元格
    	}
    	return FLG_CODE_SUCCESS;
    }
    
    /**
     * 简单汇总-各单元格加总汇总：将所有汇总文件的指定单元格的值得加和设置到最终汇总表中的对应单元格上
     * @param result 包含所有已汇总数据的结果集
     * @param startRowNum 起始行编号（非索引）
     * @param endRowNum 结束行编号（非索引）
     * @param filePathList 包含所有要汇总的的excel的路径
     * @throws Exception 
     */
    private static String summarySimpleCellSum(List<Map<String,String>> result, int startRowNum, int endRowNum, List<String> filePathList) throws Exception{
		// 参数校验
		// 若起始行编号、结束行编号不在指定范围内，则直接返回
		if (startRowNum < 1 || endRowNum > result.size() || startRowNum > endRowNum) {
			logger.error("起始行编号、结束行编号不在指定范围内，应当的编号范围：[1~"+result.size()+"]，实际["+startRowNum+"~"+endRowNum+"]");
			result = new ArrayList<Map<String,String>>();
			return FLG_CODE_START_END_ROWNW_ERROR;
		}
		
		// 取得模板中在数据行中所有为空的单元格，并加入结果集中
		List<Map<String,String>> needSumCells = new ArrayList<Map<String,String>>();
		for (int rowIx = (startRowNum-1); rowIx <= (endRowNum-1); rowIx++) {
			Map<String,String> row = result.get(rowIx);
			// 需要加和的单元格的行
			Map<String,String> needSumCellRow = new LinkedHashMap<String,String>();
			for (String cellNo : row.keySet()) {
				if (StringUtils.isEmpty(row.get(cellNo))) {
					needSumCellRow.put(cellNo, "");
				}
			}
			needSumCells.add(needSumCellRow);
		}
		
		// 遍历空单元格，设置值为所有汇总文件的该单元格的加和
		for (String filePathOne : filePathList) {
			List<Map<String,String>> filePathOneResult = readExcel(filePathOne);
			for (int rowIx = 0; rowIx < needSumCells.size(); rowIx++) {
				Map<String,String> row = needSumCells.get(rowIx);
				// 模板文件、汇总文件中的行索引
				int realRowIx = rowIx + startRowNum - 1;
				for (String cellNo : row.keySet()) {
					String value = StringUtils.isNotEmpty(result.get(realRowIx).get(cellNo)) ? result.get(realRowIx).get(cellNo) : "0";
					// 汇总文件的当前单元格的值
					Double sumValue = null;
					try {
						sumValue = Double.parseDouble(filePathOneResult.get(realRowIx).get(cellNo));
					} catch (Exception e) {
						sumValue = 0D;
					}
					value = String.valueOf(Double.parseDouble(value) + sumValue);
					result.get(realRowIx).put(cellNo, value);
				}
			}
		}
		return FLG_CODE_SUCCESS;
    }
    
    /**
     * 判断编号cellNo1的单元格是否包含编号cellNo2的单元格
     * @param cellNo1 判断中 包含者 单元格编号
     * @param cellNo2 判断中 被包含者 单元格编号
     * @param type 包含类型（ALL：整个单元格（合并或未合并）；ROW：单元格连贯行；COL：单元格连贯列；默认ALL）
     * @return cellNo1是否包含cellNo2的单元格编号
     */
    private static boolean isInclude(String cellNo1, String cellNo2, String type){
    	// 如果包含者长度比被包含者长度短，则cellNo1不包含cellNo2的单元格编号
    	if (StringUtils.isEmpty(cellNo1) || StringUtils.isEmpty(cellNo2)) {
    		return false;
    	} else if (cellNo1.equals(cellNo2)) {
    		return true;
    	}
    	
    	// 包含类型默认all
    	type = String.valueOf(type);
    	switch (type) {
    		case "ROW":
    			if (cellNo2.split("_").length == 1) {
    				return new HashSet<String>(Arrays.asList(cellNo1.replaceAll("[A-Z]+", "").split("_"))).contains(cellNo2.replaceAll("[A-Z]+", ""));
    			} else {
    				// 被包含者有多个编号
    				boolean isInclude = true;
    				Set<String> cellNo1RowNos = new HashSet<String>(Arrays.asList(cellNo1.replaceAll("[A-Z]+", "").split("_")));
    				Set<String> cellNo2RowNos = new HashSet<String>(Arrays.asList(cellNo2.replaceAll("[A-Z]+", "").split("_")));
    				for (String cellNo2RowNo : cellNo2RowNos) {
    					if (!cellNo1RowNos.contains(cellNo2RowNo)) {
    						isInclude = false;
    						break;
    					}
    				}
    				return isInclude;
    			}
    		case "COL":
    			if (cellNo2.split("_").length == 1) {
    				return new HashSet<String>(Arrays.asList(cellNo1.replaceAll("\\d+", "").split("_"))).contains(cellNo2.replaceAll("\\d+", ""));
    			} else {
    				// 被包含者有多个编号
    				boolean isInclude = true;
    				Set<String> cellNo1RowNos = new HashSet<String>(Arrays.asList(cellNo1.replaceAll("\\d+", "").split("_")));
    				Set<String> cellNo2RowNos = new HashSet<String>(Arrays.asList(cellNo2.replaceAll("\\d+", "").split("_")));
    				for (String cellNo2RowNo : cellNo2RowNos) {
    					if (!cellNo1RowNos.contains(cellNo2RowNo)) {
    						isInclude = false;
    						break;
    					}
    				}
    				return isInclude;
    			}
			default:
			if (cellNo2.split("_").length == 1) {
				// 被包含者只有一个编号
				return Arrays.asList(cellNo1.split("_")).contains(cellNo2);
			} else {
				// 被包含者有多个编号
				boolean isInclude = true;
				Set<String> cellNo1RowNos = new HashSet<String>(Arrays.asList(cellNo1.split("_")));
				Set<String> cellNo2RowNos = new HashSet<String>(Arrays.asList(cellNo2.split("_")));
				for (String cellNo2RowNo : cellNo2RowNos) {
					if (!cellNo1RowNos.contains(cellNo2RowNo)) {
						isInclude = false;
						break;
					}
				}
				return isInclude;
			}
    	}
    }
    
    /**
     * 对结果集进行横向汇总公式计算
     * @param result 处理的结果集
     * @param firstDataRowNm 数据开始行号
     * @param lastDataRowNm 数据结束行号
     * @param horizontalSummaryList 横向汇总公式集合
     */
    private static void horizontalFormulaSummary(List<Map<String,String>> result, int firstDataRowNm, int lastDataRowNm,List<String> horizontalSummaryList){
    	ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine se = manager.getEngineByName("js");
		
    	// 遍历所有单元格进行横向公式计算
		for (int rowIx = firstDataRowNm-1; rowIx < lastDataRowNm; rowIx++) {
			Map<String,String> row = result.get(rowIx);
			for (String cellNo : row.keySet()) {
				String valueStr = row.get(cellNo);
				// 计算横向汇总公式
				String horizontalSummaryFormula = null;
    			// 获取公式
				// 判定是否是横向汇总公式的结果列
    			for (String horizontalSummaryOne : horizontalSummaryList) {
    				if (cellNo.replaceAll("\\d+", "").equals(horizontalSummaryOne.split("=")[0])) {
						horizontalSummaryFormula = horizontalSummaryOne.split("=")[1];
						break;
    				}
    			}
    			
    			// 单元格的值即是公式
    			if (StringUtils.isEmpty(horizontalSummaryFormula)) {
    				valueStr = excelFormulaHandle(valueStr, 0, rowIx, "MAIN_PROCESS");
    				if (StringUtils.isNotEmpty(valueStr)) {
//        				// 计算汇总公式而不保留公式在导出的文件中（该方式已被保留公式，利用excel运算取代）
//        				horizontalSummaryFormula = valueStr.replace("=", "");
    				}
    			}
        		
        		// 有公式则进行计算
        		if (StringUtils.isNotEmpty(horizontalSummaryFormula)) {
            		// 横向公式计算
            		// 取得所有=号后的字母组
            		Matcher m = Pattern.compile("[A-Z]+").matcher(horizontalSummaryFormula);
            		while (m.find()) {
            			// 将所有=号后字母组替换为对应列的值
            			String cellValue = row.get(m.group()+(rowIx+1));
            			cellValue = StringUtils.isNotEmpty(cellValue) && cellValue.matches(REG_DOUBLE_NUMBER) ? cellValue : "0";
            			horizontalSummaryFormula = horizontalSummaryFormula.replaceAll(m.group(), cellValue);
            		}
            		// 计算横向汇总公式的结果
            		try {
            			valueStr = String.valueOf(se.eval(horizontalSummaryFormula));
            		} catch (Exception e) {
            			valueStr = "0";
            		}
        		}
        		// 更新原始结果集
        		row.put(cellNo, valueStr);
			}
		}
		
		// 针对数据行外的未被处理的公式，进行处理（这种情况暂时只会出现在各单元格加总汇总方式中）
		for (int rowIx = 0; rowIx < result.size(); rowIx++) {
			Map<String,String> row = result.get(rowIx);
			for (String cellNo : row.keySet()) {
				String valueStr = row.get(cellNo);
				// 单元格的值即是公式
				valueStr = excelFormulaHandle(valueStr, 0, rowIx, "MAIN_PROCESS");
				if (StringUtils.isNotEmpty(valueStr)) {
					row.put(cellNo, valueStr);
				}
			}
		}
    }
    
    /**
     * excel公式处理
     * @param cellFormula 单元格中公式
     * @param cellNumericValue 单元格中数值（若为主处理，此项可填为0）
     * @param rowIx 行索引
     * @param type 处理类型（PRE_PROCESS：预处理；MAIN_PROCESS：主处理；）
     * @return 处理后的公式
     */
    private static String excelFormulaHandle(String cellFormula, double cellNumericValue, int rowIx, String type){
    	ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine se = manager.getEngineByName("js");
		String cellValueStr = null;
		
    	switch(type) {
    		case "PRE_PROCESS":
    			// 预处理
    			String cellValue = null;
    			if (cellFormula.matches(REG_SIMPLE_FORMULA) || cellFormula.matches(REG_FUNCTION_SUM_FORMULA)) {
        			// 简单公式，将单元格编号改为纯列号(如：F7*0.1+J7*0.1+L7*0.45+N7*0.35 -> F*0.1+J*0.1+L*0.45+N*0.35)
        			cellValue = "="+cellFormula;
        			Matcher m = Pattern.compile("[A-Z]+\\d+").matcher(cellValue);
        			List<String> tempMatcher = new ArrayList<String>();
            		while (m.find()) {
            			tempMatcher.add(m.group());
            		}
            		// 将长度长的排到前面，先替换长度长的，以避免替换错误
            		Collections.sort(tempMatcher, new Comparator<String>() {
						@Override
						public int compare(String o1, String o2) {
							return o1.length() - o2.length();
						}
					});
            		for (String matcher : tempMatcher) {
            			if (matcher.replaceAll("[A-Z]+", "").equals(String.valueOf(rowIx+1))) {
            				cellValue = cellValue.replaceAll(matcher, matcher.replaceAll("\\d+", "")+"{&ROW}");
            			} else {
            				if (!cellFormula.matches(REG_FUNCTION_SUM_FORMULA)) {
            					// 若公式是跨行公式，则将行号替换为相对于当前cell行号的值（&ROW代表当前行号，通过减[matcher行在当前cell行之前]或加[matcher行在当前cell行之后]表示）
            					cellValue = cellValue.replaceAll(matcher, matcher.replaceAll("\\d+", "")
            							+"{&ROW"+String.valueOf(Integer.parseInt(matcher.replaceAll("[A-Z]+", "")) - (rowIx+1))+"}");
            				}
            			}
            		}
        		} else if (cellFormula.matches(REG_FUNCTION_FORMULA)) {
        			// 特殊函数公式，在前方拼上等号
        			cellValue = "="+cellFormula;
        		} else {
        			// 其他公式，只取值
        			try {
        				cellValue = String.valueOf(cellNumericValue);
        			} catch (Exception e) {
        				cellValue = "0";
        			}
        		}
    			cellValueStr = cellValue;
    			break;
    		case "MAIN_PROCESS":
    			// 主处理
    			if (cellFormula.startsWith("=") && (cellFormula.replace("=", "").matches(REG_FUNCTION_SUM_FORMULA) || !cellFormula.replace("=", "").matches(REG_FUNCTION_FORMULA))) {
					// 保留公式在导出的文件中
	    			// 跨行的公式项的处理（{&ROW-2}或{&ROW+5}）
					Matcher m = Pattern.compile("\\{&ROW[\\+ \\-]\\d+\\}").matcher(cellFormula);
	    			List<String> tempMatcher = new ArrayList<String>();
	        		while (m.find()) {
	        			tempMatcher.add(m.group());
	        		}
					if (tempMatcher != null && !tempMatcher.isEmpty()) {
						// 是跨行的公式项
						for (String matcher : tempMatcher) {
							String rowNo = String.valueOf(rowIx+1);
							try {
								rowNo = String.valueOf(se.eval(matcher.replaceAll("[\\{ \\}]","").replaceAll("&ROW", String.valueOf(rowIx+1))));
							} catch (Exception e) {
								logger.error("因【"+e.getMessage()+"】的错误，将跨行公式的行号设为了当前行号",e);
							}
							cellFormula = cellFormula.replace(matcher, rowNo);
						}
					}
					// 非跨行的公式项
					cellFormula = cellFormula.replaceAll("\\{&ROW\\}", String.valueOf(rowIx+1));
//    				// 计算汇总公式而不保留公式在导出的文件中（该方式已被保留公式，利用excel运算取代）
//    				cellFormula = cellFormula.replaceAll("{&ROW}", "");
    			}
    			cellValueStr = cellFormula;
				break;
    	}
    	
    	return cellValueStr;
    }
    
    /**
     * 对结果集进行纵向汇总公式计算
     * @param result 处理的结果集
     * @param verticalFormulaList 纵向汇总公式集合
     */
    private static void verticalFormulaSummary(List<Map<String,String>> result, List<String> verticalFormulaList){
    	ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine se = manager.getEngineByName("js");
		
    	// 处理纵向加和的行的公式（o=SUM(m:n)、o=SUM(m,n,p,……)）
    	for (int formulaIx = 0; formulaIx < verticalFormulaList.size(); formulaIx++) {
    		String formulaOld = verticalFormulaList.get(formulaIx);
    		// 括号中间的内容：【m:n】或【m,n,p,……】
    		String formulaOfToStr = formulaOld.substring(formulaOld.indexOf("(")+1, formulaOld.indexOf(")"));
    		// 判定纵向汇总的行的公式是否可识别
    		if (!formulaOld.matches("^[0-9]\\d*=[A-Z]+\\(\\d+(,\\d+)*\\)$") 
    				&& !(formulaOld.matches("^[0-9]\\d*=[A-Z]+\\(\\d+:\\d+\\)$") && Integer.parseInt(formulaOfToStr.split(":")[1]) >= Integer.parseInt(formulaOfToStr.split(":")[0]))) {
    			logger.warn("当前纵向汇总的行的公式无法识别："+formulaOld);
				continue;
    		}
    		
    		// 获取运算符
    		String operator = null;
			if (formulaOld.indexOf("SUM") >= 0) {
				operator = "+";
			}
			if (StringUtils.isEmpty(operator)) {
				logger.warn("当前纵向汇总的行的公式中的运算方法不支持："+formulaOld);
				continue;
			}
			// 获取运算公式
    		StringBuilder formulaTemp = new StringBuilder("");
    		// 运算公式
    		String formula = null;
    		// 拼上等号及等号左边的内容
    		formulaTemp.append(formulaOld.split("=")[0]);
    		formulaTemp.append("=");
    		// 拼上等号右边的内容
    		if (formulaOld.indexOf(":") >= 0) {
    			// 开始行号
    			int startRowNo = Integer.parseInt(formulaOfToStr.split(":")[0]);
    			// 开始列号
    			int endRowNo = Integer.parseInt(formulaOfToStr.split(":")[1]);
    			// 拼上对应的行号及运算符
    			for (int rowNo = startRowNo; rowNo <= endRowNo; rowNo++) {
    				formulaTemp.append("{");
    				formulaTemp.append(String.valueOf(rowNo));
    				formulaTemp.append("}");
    				formulaTemp.append(operator);
    			}
    		} else if (formulaOld.indexOf(",") >= 0) {
    			for (String rowNo : formulaOfToStr.split(",")) {
    				formulaTemp.append("{");
    				formulaTemp.append(rowNo);
    				formulaTemp.append("}");
    				formulaTemp.append(operator);
    			}
    		}
    		formula = formulaTemp.substring(0, formulaTemp.length()-1);
    		
    		// 等号左边的内容，即结果行号
    		int formulaResultRowNo = Integer.parseInt(formulaOld.split("=")[0]);
    		try{
	    		// 当前行是否无一个值被成功运算
	    		boolean isAllFailed = true;
	    		for (String cellNo : result.get(formulaResultRowNo-1).keySet()) {
	    			// 单元格列号
	    			String cellColNo = cellNo.replaceAll("\\d+", "");
	    			// 单元格值
	    			String cellValue = result.get(formulaResultRowNo-1).get(cellNo);
	    			// 等号右侧运算式
	    			StringBuilder operatorFormula = new StringBuilder(formula.split("=")[1]);
	    			// 所有为空或是数字的值都需进行运算得值
	    			if (StringUtils.isEmpty(cellValue) || cellValue.matches(REG_DOUBLE_NUMBER)) {
	    				// 构造运算式
	    				for (int rowNoStrIx = 0; rowNoStrIx < operatorFormula.toString().split("["+operator+"]").length; rowNoStrIx++) {
	    					// 行号
	    					String rowNo = operatorFormula.toString().split("["+operator+"]")[rowNoStrIx];
	    					// 运算式项的值
	    					String formulaElemValue = result.get(Integer.parseInt(rowNo.replaceAll("[{,}]+", ""))-1).get(cellColNo+rowNo.replaceAll("[{,}]+", ""));
	    					operatorFormula.replace(
	    							operatorFormula.indexOf(rowNo),
	    							operatorFormula.indexOf(rowNo)+rowNo.length(),
	    							formulaElemValue != null ? formulaElemValue : "");
	    							// 将运算不了的均设0（现在先设定为空字符串）
	//    							StringUtils.isNotEmpty(formulaElemValue) && formulaElemValue.matches(REG_DOUBLE_NUMBER) ? formulaElemValue : "0");
	    				}
	    				try {
	    					cellValue = String.valueOf(se.eval(operatorFormula.toString()));
	    				} catch (Exception e) {
	    					logger.debug("当前列【"+cellColNo+"】的指定行【"+operatorFormula.toString().split("["+operator+"]")+"】包含非数值型字符串，无法进行运算，结果值设为空字符串");
	    					cellValue = "";
	    				}
	    				// 判断是否运算成功
	    				if (StringUtils.isNotEmpty(cellValue) && !"0".equals(cellValue)) {
	    					isAllFailed = false;
	    				}
	    				result.get(formulaResultRowNo-1).put(cellNo, cellValue);
	    			}
	    		}
	    		// 若当前公式全部都未执行成功且不是最后一个公式，则将当前公式移动到最后面
	    		if (isAllFailed && formulaIx < verticalFormulaList.size() - 1) {
	    			verticalFormulaList.remove(formulaOld);
	    			verticalFormulaList.add(formulaOld);
	    			formulaIx--;
	    		}
    		} catch (IndexOutOfBoundsException e) {
    			logger.error("当前公式【"+formulaOld+"】中的行号不存在，无法执行，该公式将被忽略");
    			continue;
        	}
    	}
    }
    
    /**
     * 对结果集进行纵向加总汇总计算
     * @param result 处理的结果集
     * @param firstDataRowNm 数据开始行号
     * @param lastDataRowNm 数据结束行号
     * @param verticalSummaryList 需要纵向加总汇总的列号
     * @param summaryType 简单汇总方式类型（类型描述(不可为空)：<br/>
     * 		●ROW_PUSH：多行归总汇总(详见方法[{@link com.mininglamp.currencySys.common.util.CustomSummaryExcelUtil#summarySimpleRowPush(List, List, int, List)}])；<br/>
     * 		●CELL_SUM：各单元格加总汇总(详见方法[{@link com.mininglamp.currencySys.common.util.CustomSummaryExcelUtil#summarySimpleCellSum(List, int, int, List, int, List)}])；）
     */
	private static void verticalSummary(List<Map<String,String>> result, int firstDataRowNm, int lastDataRowNm, List<String> verticalSummaryList,String summaryType){
		// 处理纵向加和(将加和放到对应列最后一行数据的下一行的单元格中)
    	Map<String,String> sumRow = new LinkedHashMap<String,String>();
    	for (String verticalSummaryOne : verticalSummaryList) {
    		// 当前列加和
    		double sumValue = 0;
    		for (int rowIx = firstDataRowNm-1; rowIx < lastDataRowNm; rowIx++) {
    			Map<String,String> row = result.get(rowIx);
    			Double cellDoubleValue = null;
    			try{
    				// 取得每一数据行对应单元格的值
    				cellDoubleValue = Double.valueOf(row.get(verticalSummaryOne+(rowIx+1)));
    			}catch(NumberFormatException e){
    				// 不是数字
    				break;
    			} catch (Exception e) {
    				logger.error("Double.valueOfException---cellNo："+verticalSummaryOne+(rowIx+1),e);
    			}
    			sumValue += cellDoubleValue;
            }
    		sumRow.put(verticalSummaryOne+(lastDataRowNm+1), String.valueOf(sumValue));
    	}
    	// 设置结果到结果集
    	if (sumRow != null && !sumRow.isEmpty()) {
	    	if ("CELL_SUM".equals(summaryType)) {
	    		// 各单元格加总汇总，根据单元格编号将结果设置到结果集中
	    		boolean isAllNotSet = true;
	    		for (String cellNo : sumRow.keySet()) {
	    			int rowIx = Integer.parseInt(cellNo.replaceAll("[A-Z]+", "")) - 1;
	    			// 若加总要显示的行不在结果集中，则加上
	    			if (rowIx > result.size()-1) {
	    				for (int newTimes = 1; newTimes <= rowIx-(result.size()-1); newTimes++) {
	    					Map<String,String> map = new LinkedHashMap<String,String>();
	    					result.add(map);
	    				}
	    			}
    				result.get(rowIx).put(cellNo, sumRow.get(cellNo));
    				isAllNotSet = false;
	    		}
	    		// 都没有设置上，则将结果添加的结果集的最后一行
	    		if (isAllNotSet) {
	    			result.add(sumRow);
	    		}
	    	} else {
	    		// 多行归总汇总，将结果添加的结果集的最后一行
	    		result.add(sumRow);
	    	}
    	}
	}
    
	public static void main(String[] args){
		// 汇总测试
		// 全部
//		String json = "{\"templatePath\":\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件3\\\\测试.xls\",\"filePathList\":[\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件3\\\\汇总文件1.xls\"],\"verticalSummaryList\":[],\"horizontalSummaryList\":[]}";
//		String json = "{\"templatePath\":\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格.xlsx\",\"filePathList\":[\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格_1.xlsx\",\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格_2.xlsx\"],\"verticalSummaryList\":[\"D\",\"E\",\"G\",\"H\",\"I\",\"K\",\"M\"],\"verticalFormulaList\":[\"14=SUM(7:13)\",\"145=SUM(15,19)\"],\"horizontalSummaryList\":[\"F=(D*2)/(D*2+E)*100\",\"J=(H*2)/(H*2+I)*100\"]}";
		// 包含分、子行求和
//		String json = "{\"templatePath\":\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\分、子项求和\\\\测试文件2\\\\银行业金融机构对公现金业务统计表（按存款人类别）.xls\",\"filePathList\":[\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\分、子项求和\\\\测试文件2\\\\银行业金融机构对公现金业务统计表（按存款人类别）_1.xls\"],\"verticalFormulaList\":[\"9=SUM(10:13)\",\"15=SUM(7,8,9,14)\"]}";
//		String json = "{\"templatePath\":\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\分、子项求和\\\\测试文件1\\\\24-年度发行基金调拨计划.xls\",\"filePathList\":[\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\分、子项求和\\\\测试文件1\\\\24-年度发行基金调拨计划_1.xls\",\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\分、子项求和\\\\测试文件1\\\\24-年度发行基金调拨计划_2.xls\"],\"verticalFormulaList\":[\"11=SUM(25:100)\",\"15=SUM(12:14)\",\"16=SUM(24,25)\"],\"horizontalSummaryList\":[\"J=D+E+F+G+H+I-C\"]}";
//		String json = "{\"templatePath\":\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格.xlsx\",\"filePathList\":[\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格_1.xlsx\"],\"verticalSummaryList\":[\"D\",\"E\",\"H\",\"I\",\"G\",\"K\",\"M\"],\"verticalFormulaList\":[\"7=SUM(8:12)\",\"14=SUM(8,10,12,13)\"],\"horizontalSummaryList\":[],\"summaryType\":\"ROW_PUSH\"}";
		
		// 不进行纵向汇总
//		String json = "{\"templatePath\":\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格.xlsx\",\"filePathList\":[\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格_1.xlsx\",\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格_2.xlsx\"],\"verticalSummaryList\":[],\"horizontalSummaryList\":[\"F=(D*2)/(D*2+E)*100\",\"J=(H*2)/(H*2+I)*100\",\"L=K/G*100\",\"N=M/G*100\",\"O=F*0.1+J*0.1+L*0.45+N*0.35\"]}";
		// 不进行横向汇总
//		String json = "{\"templatePath\":\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格.xlsx\",\"filePathList\":[\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格_1.xlsx\",\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格_2.xlsx\"],\"verticalSummaryList\":[\"D\",\"E\",\"G\",\"H\",\"I\",\"K\",\"M\"],\"horizontalSummaryList\":[]}";

		// 各单元格加总汇总
//		String json = "{\"templatePath\":\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\各单元格加总汇总\\\\测试文件1\\\\测试.xls\",\"filePathList\":[\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\各单元格加总汇总\\\\测试文件1\\\\汇总文件1.xls\",\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\各单元格加总汇总\\\\测试文件1\\\\汇总文件2.xls\"],\"verticalSummaryList\":[],\"horizontalSummaryList\":[],\"summaryType\":\"CELL_SUM\"}";
//		String json = "{\"templatePath\":\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\各单元格加总汇总\\\\测试文件2\\\\银行业金融机构现金库存券别统计表.xls\",\"filePathList\":[\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\各单元格加总汇总\\\\测试文件2\\\\银行业金融机构现金库存券别统计表_鹤壁.xls\",\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\各单元格加总汇总\\\\测试文件2\\\\银行业金融机构现金库存券别统计表_开封.xls\",\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\各单元格加总汇总\\\\测试文件2\\\\银行业金融机构现金库存券别统计表_南阳.xls\",\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\各单元格加总汇总\\\\测试文件2\\\\银行业金融机构现金库存券别统计表_信阳.xls\",\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\各单元格加总汇总\\\\测试文件2\\\\银行业金融机构现金库存券别统计表_许昌.xls\",\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\各单元格加总汇总\\\\测试文件2\\\\银行业金融机构现金库存券别统计表_郑州.xls\"],\"verticalSummaryList\":[\"B\",\"C\",\"D\",\"F\",\"G\",\"H\",\"J\",\"K\",\"L\",\"O\",\"P\"],\"verticalFormulaList\":[\"16=SUM(7,8,9)\"],\"horizontalSummaryList\":[\"N=B+F+J\"],\"summaryType\":\"CELL_SUM\",\"simpleSummaryOption\":{\"dataStartRowNum\":6,\"dataEndRowNum\":16}}";
		// 多行归总汇总(指定汇总行范围)
//		String json = "{\"templatePath\":\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格.xlsx\",\"filePathList\":[\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格_1.xlsx\",\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格_2.xlsx\"],\"verticalSummaryList\":[\"D\",\"E\",\"H\",\"I\",\"G\",\"K\",\"M\"],\"summaryType\":\"ROW_PUSH\",\"simpleSummaryOption\":{\"direction\":\"FORWARD\",\"dataStartRowNum\":2,\"dataEndRowNum\":5}}";
//		String json = "{\"templatePath\":\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格.xlsx\",\"filePathList\":[\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格_1.xlsx\",\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格_2.xlsx\"],\"verticalSummaryList\":[\"D\",\"E\",\"H\",\"I\",\"G\",\"K\",\"M\"],\"summaryType\":\"ROW_PUSH\",\"simpleSummaryOption\":{\"direction\":\"BACKWARD\",\"dataStartRowNum\":1,\"dataEndRowNum\":5}}";
//		String json = "{\"templatePath\":\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格.xlsx\",\"filePathList\":[\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格_1.xlsx\",\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格_2.xlsx\"],\"verticalSummaryList\":[\"D\",\"E\",\"H\",\"I\",\"G\",\"K\",\"M\"],\"summaryType\":\"ROW_PUSH\",\"simpleSummaryOption\":{\"dataStartRowNum\":1,\"dataEndRowNum\":1}}";
//		String json = "{\"templatePath\":\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格.xlsx\",\"filePathList\":[\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格_1.xlsx\",\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格_2.xlsx\"],\"verticalSummaryList\":[\"D\",\"E\",\"H\",\"I\",\"G\",\"K\",\"M\"],\"summaryType\":\"ROW_PUSH\",\"simpleSummaryOption\":{}}";
//		String json = "{\"templatePath\":\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格.xlsx\",\"filePathList\":[\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格_1.xlsx\",\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格_2.xlsx\"],\"verticalSummaryList\":[\"D\",\"E\",\"H\",\"I\",\"G\",\"K\",\"M\"],\"summaryType\":\"ROW_PUSH\"}";
		// 数据单文件汇总
		String json = "{\"templatePath\":\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\测试文件\\\\测试文件1\\\\发行库缴存款情况\\\\发行库缴存款情况.xlsx\",\"filePathList\":[\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\测试文件\\\\测试文件1\\\\发行库缴存款情况\\\\发行库缴存款情况2.xlsx\",\"H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\测试文件\\\\测试文件1\\\\发行库缴存款情况\\\\发行库缴存款情况3.xlsx\"],\"verticalSummaryList\":[],\"verticalFormulaList\":[\"79=SUM(7,11,15,19,23,27,31,35,39,43,47,51,55,59,63,67,71,75)\",\"80=SUM(8,12,16,20,24,28,32,36,40,44,48,52,56,60,64,68,72,76)\",\"81=SUM(9,13,17,21,25,29,33,37,41,45,49,53,57,61,65,69,73,77)\",\"82=SUM(10,14,18,22,26,30,34,38,42,46,50,54,58,62,66,70,74,78)\"],\"horizontalSummaryList\":[],\"summaryType\":\"ROW_PUSH\",\"simpleSummaryOption\":{\"dataStartRowNum\":\"\",\"dataEndRowNum\":\"\"}}";
		if (CustomSummaryExcelUtil.summaryExcel(json) != null) {
			System.out.println("------------------------------- 汇总完毕！---------------------------------");
		} else {
			System.out.println("------------------------------- 汇总失败！---------------------------------");
		}
		
		// 其他测试
//		List<Map<String, String>> templateHeaders = null;
//		try {
////			templateHeaders = CustomSummaryExcelUtil.readExcel("H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\测试文件1\\\\金融机构信息维护.xls");
//			templateHeaders = CustomSummaryExcelUtil.readExcel("H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格.xlsx");
////			templateHeaders = CustomSummaryExcelUtil.readExcel("H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\测试文件3\\\\测试.xls");
////			templateHeaders = CustomSummaryExcelUtil.readExcel("H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\多行归总汇总\\\\测试文件2\\\\汇总类客户信息数据表格 - 副本.xlsx");
//			
////			templateHeaders = CustomSummaryExcelUtil.readExcel("H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\分、子项求和\\\\测试文件1\\\\24-年度发行基金调拨计划.xls");
////			templateHeaders = CustomSummaryExcelUtil.readExcel("H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\每日临时\\\\自定义报表\\\\自定义汇总\\\\分、子项求和\\\\测试文件2\\\\银行业金融机构对公现金业务统计表（按存款人类别）.xls");
////			templateHeaders = CustomSummaryExcelUtil.readExcel("H:\\\\863\\\\项目\\\\河南农信数据报送系统\\\\其他文档\\\\报表汇总\\\\36-金融机构现金收支流向情况统计表.xls");
//		} catch (Exception e) {
//			// Auto-generated catch block
//			e.printStackTrace();
//		}
////		Map<String, String> temp = CustomSummaryExcelUtil.getTemplateHeaderParents(templateHeaders);
//		Map<String, String> temp = CustomSummaryExcelUtil.getTemplateHeadersWithParent(templateHeaders);
////		boolean temp = CustomSummaryExcelUtil.isInclude("G3_G4_G5_H3_H4_H5_I3_I4_I5_J3_J4_J5_K3_K4_K5","H4_I4_J4","ROW");
////		System.out.println(temp);
////		Map<String, String> temp = CustomSummaryExcelUtil.getTemplateHeadersWithoutParent(templateHeaders);
////		Map<String, String> temp = CustomSummaryExcelUtil.getTemplateHeaderInfo(templateHeaders);
//		System.out.println(JSONObject.fromObject(temp).toString());
	}
}
