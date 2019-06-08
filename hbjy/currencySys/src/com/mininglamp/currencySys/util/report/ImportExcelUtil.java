package com.mininglamp.currencySys.util.report;  
  
import java.io.IOException;  
import java.io.InputStream;  
import java.text.DecimalFormat;  
import java.text.SimpleDateFormat;  
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;  
import org.apache.poi.ss.usermodel.Cell;  
import org.apache.poi.ss.usermodel.Row;  
import org.apache.poi.ss.usermodel.Sheet;  
import org.apache.poi.ss.usermodel.Workbook;  
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mininglamp.currencySys.insertData.service.InsertDataService;
  
  /**
   * excel 读取配置
   * @author JH
   *
   */
public class ImportExcelUtil {  
      
    private final static String excel2003L =".xls";    //2003- 版本的excel  
    private final static String excel2007U =".xlsx";   //2007+ 版本的excel  
      
    /** 
     * 描述：获取IO流中的数据，组装成List<List<Object>>对象 
     * @param in,fileName 
     * @return 
     * @throws IOException  
     */  
    public  List<List<Object>> getBankListByExcel(InputStream in,String fileName) throws Exception{  
        List<List<Object>> list = null;  
          
        //创建Excel工作薄  
        Workbook work = this.getWorkbook(in,fileName);  
        if(null == work){  
            throw new Exception("创建Excel工作薄为空！");  
        }  
        Sheet sheet = null;  
        Row row = null;  
        Cell cell = null;  
          
        list = new ArrayList<List<Object>>();  
        //遍历Excel中所有的sheet  
        for (int i = 0; i < work.getNumberOfSheets(); i++) {  
            sheet = work.getSheetAt(i);  
            if(sheet==null){continue;}  
              
            //遍历当前sheet中的所有行  
            for (int j = sheet.getFirstRowNum(); j < sheet.getLastRowNum(); j++) {  
                row = sheet.getRow(j);  
                if(row==null||row.getFirstCellNum()==j){continue;}  
                  
                //遍历所有的列  
                List<Object> li = new ArrayList<Object>();  
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {  
                    cell = row.getCell(y);  
                    li.add(this.getCellValue(cell));  
                }  
                list.add(li);  
            }  
        }  
        return list;  
    }  
      
    /** 
     * 描述：根据文件后缀，自适应上传文件的版本  
     * @param inStr,fileName 
     * @return 
     * @throws Exception 
     */  
    public Workbook getWorkbook(InputStream inStr,String fileName) throws Exception{  
        Workbook wb = null;  
        String fileType = fileName.substring(fileName.lastIndexOf("."));  
        if(excel2003L.equals(fileType)){  
            wb = new HSSFWorkbook(inStr);  //2003-  
        }else if(excel2007U.equals(fileType)){ 
        	wb = new XSSFWorkbook(inStr);
        }else{  
            throw new Exception("解析的文件格式有误！");  
        }  
        return wb;  
    }  
    
    /** 
     * 描述：根据文件后缀，自适应上传文件的版本  
     * @param inStr,fileName 
     * @return 
     * @throws Exception 
     */  
    public static Workbook getXlsxWorkbook(InputStream file) throws Exception{  
    	
    	 Workbook wb = null;
         try {
        	 wb = new XSSFWorkbook(file);
         } catch (Exception e) {
        	 wb=null;
         }
        return wb;  
    }  
    
    public static Workbook getXlsWorkbook(InputStream file) throws Exception{  
    	
   	 	Workbook wb = null;
        try {
        	wb = new HSSFWorkbook(file);
        } catch (Exception e) {
        	System.out.println(e);
       	   wb=null;
        }
       return wb;  
   }  
    
    /** 
     * 描述：对表格中数值进行格式化 
     * @param cell 
     * @return 
     */  
    public static  Object getCellValue(Cell cell){  
        Object value = null;  
        DecimalFormat df = new DecimalFormat("0");  //格式化number String字符  
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");  //日期格式化  
        DecimalFormat df2 = new DecimalFormat("0.00");  //格式化数字  
        if(cell == null){
        	return value; 
        }
        switch (cell.getCellType()) {  
        case Cell.CELL_TYPE_STRING:  
            value = cell.getRichStringCellValue().getString();  
            break;  
        case Cell.CELL_TYPE_NUMERIC:  
            if("General".equals(cell.getCellStyle().getDataFormatString())){  
                value = df.format(cell.getNumericCellValue());  
            }else if(HSSFDateUtil.isCellDateFormatted(cell)){ 
            	value = sdf.format(cell.getDateCellValue());
            }else{  
                value = df2.format(cell.getNumericCellValue());  
            }  
            break;  
        case Cell.CELL_TYPE_BOOLEAN:  
            value = cell.getBooleanCellValue();  
            break;  
        case Cell.CELL_TYPE_BLANK:  
            value = "null";  
            break;  
        default:  
            break;  
        }  
        return value;  
    }

    /** 
     * 描述：对表格中数值进行格式化 
     * @param cell 
     * @param params 
     * @return 
     */  
    public static  Object getCellValue(Cell cell, Map<String, Object> params, int cellCount){  
    	boolean fal = false;
    	Object value = null;  
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");  //日期格式化  
    	List<Map> object = (List<Map>) params.get("check_rule_config");
    	//字段类型
		String FIELD_TYPE = (String) object.get(cellCount).get("FIELD_TYPE");
		//字段 中文名称
		String FIELD_NAME = (String) object.get(cellCount).get("FIELD_NAME");
		//为否可以为空
		String is_null = (String) object.get(cellCount).get("IS_NULL");
		//是否强制校验
		String IS_LOGIC = (String) object.get(cellCount).get("IS_LOGIC");
		//是否是自动生成字段
		String IS_INPUT_AUTO = (String) object.get(cellCount).get("IS_INPUT_AUTO");
		// 数字类型校验
		if(FIELD_TYPE.startsWith("n..") || FIELD_TYPE.startsWith("n")){
			if(FIELD_TYPE.startsWith("n..(")){
				fal = true;
			}else{
				fal = false;
			}
		}else{
			fal = false;
		}
		DecimalFormat df = new DecimalFormat("0");
		
		// 自动生成的字段强制设为null
        if(cell == null || "null".equals(cell) || InsertDataService.IS_INPUT_AUTO_TRUE.equals(IS_INPUT_AUTO)){
        	return value; 
        }
        switch (cell.getCellType()) {  
        case Cell.CELL_TYPE_STRING:  
            value = cell.getRichStringCellValue().getString();  
            break;  
        case Cell.CELL_TYPE_NUMERIC:  
            if("General".equals(cell.getCellStyle().getDataFormatString())){  
            	if(fal){
            		value = String.valueOf(cell.getNumericCellValue());
            	}else{
            		value = df.format(cell.getNumericCellValue());
            	}
            }else if(HSSFDateUtil.isCellDateFormatted(cell)){ 
            	value = sdf.format(cell.getDateCellValue());
            }else{  
            	if(fal){
            		value = String.valueOf(cell.getNumericCellValue());
            	}else{
            		value = df.format(cell.getNumericCellValue());
            	} 
            }  
            break;  
        case Cell.CELL_TYPE_BOOLEAN:  
            value = cell.getBooleanCellValue();  
            break;  
        case Cell.CELL_TYPE_BLANK:  
            value = "null";  
            break;  
        default:  
            break;  
        }  
        return value;  
    }

    public static String DecimaltoFormat(int numCountStr, int decimalCountStr){
    	String Expression = "0";
    	/*for (int i = 0; i < numCountStr; i++) {
    		Expression += "0";
		}*/
    	Expression += ".";
    	for (int i = 0; i < decimalCountStr; i++) {
    		Expression += "0";
		}
		return Expression;
    }
}  