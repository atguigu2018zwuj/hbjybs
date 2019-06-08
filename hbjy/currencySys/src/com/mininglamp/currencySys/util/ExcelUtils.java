
package com.mininglamp.currencySys.util;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFeatures;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


/**
 * 数据导入-Excel工具类
 *
 * @author
 * @since 2010.6.7
 * @version 1.0
 *
 */
/**
 * 
 * @author miaozhen
 * //下载excel			
			//调用导出方法
			String filename =bmmc+"__一证多车异常数据统计"+sss;
			String[] title_name = {"序号","驾驶证号","当事人","处理机关","处理机关名称","处理车辆数"};
			String[] col_name = {"xh","jszh","dsr","cljg","cljgmc","clcls"};
			ExcelTools.downLoadExcel(response, filename,title_name,col_name,queryExcelList,null,null);
			
 *
 */
public class ExcelUtils {


    /**
     * 获取工作薄数量
     */

    public int getNumberOfSheets(Workbook book) {
        return book == null ? 0 : book.getNumberOfSheets();
    }

    /**
     * 得到数据
     */
    public String getData(Sheet sheet,int cell, int row) {
        Cell rs = sheet.getCell(cell, row);
        return rs.getContents();
    }

    /**
     * 得到数据
     */
    public String getData(Cell rs) {
        return rs.getContents();
    }

    /**
     * 获取工作薄总行数
     */
    public int getRows(Sheet sheet) {
        return sheet == null ? 0 : sheet.getRows();
    }

    /**
     * 获取最大列数
     */
    public int getColumns(Sheet sheet) {
        return sheet == null ? 0 : sheet.getColumns();
    }

    /**
     * 获取每行单元格数组
     */
    public Cell[] getRows(Sheet sheet, int row) {
        return sheet == null || sheet.getRows() < row ? null : sheet.getRow(row);
    }

    /**
     * 获取每行单元格数组
     */
    public Cell[][] getCells(Sheet sheet, int endrow, int endcol) {
        return getCells(sheet, 0, endrow, 0, endcol);
    }

    /**
     * 获取每行单元格数组
     */
    public Cell[][] getCells(Sheet sheet, int startrow, int endrow, int startcol, int endcol) {
        Cell[][] cellArray = new Cell[endrow - startrow][endcol - startcol];
        int maxRow = this.getRows(sheet);
        int maxCos = this.getColumns(sheet);
        for (int i = startrow; i < endrow && i < maxRow; i++) {
            for (int j = startcol; j < endcol && j < maxCos; j++) {
                cellArray[i - startrow][j - startcol] = sheet.getCell(j, i);
            }
        }
        return cellArray;
    }

    /**
     * 得到行的值
     */
    public Cell[] getColCells(Sheet sheet, int col, int startrow, int endrow) {
        Cell[] cellArray = new Cell[endrow - startrow];
        int maxRow = this.getRows(sheet);
        int maxCos = this.getColumns(sheet);
        if (col <= 0 || col > maxCos || startrow > maxRow || endrow < startrow) {
            return null;
        }
        if (startrow < 0) {
            startrow = 0;
        }
        for (int i = startrow; i < endrow && i < maxRow; i++) {
            cellArray[i - startrow] = sheet.getCell(col, i);
        }
        return cellArray;
    }

    /**
     * 得到列的值
     */
    public Cell[] getRowCells(Sheet sheet, int row, int startcol, int endcol) {
        Cell[] cellArray = new Cell[endcol - startcol];
        int maxRow = this.getRows(sheet);
        int maxCos = this.getColumns(sheet);
        if (row <= 0 || row > maxRow || startcol > maxCos || endcol < startcol) {
            return null;
        }
        if (startcol < 0) {
            startcol = 0;
        }
        for (int i = startcol; i < startcol && i < maxCos; i++) {
            cellArray[i - startcol] = sheet.getCell(i, row);
        }
        return cellArray;
    }

    /**
     * 写入数据到Excel文件
     * filename:下载文件的文件名称(下载的文件名称)
     * title_name：excel第一行标题的名称(字符串数组，第一行内容)
     * col_name：所有属性列的名称
     * ls_data：数据List
     * code_col：需要转换的代码的列的名称,代码类别（dmlb）用逗号分割.例子：(zdlx,8005)
     * list_data_type:list数据的类别。(BEAN或MAP)
     *
     */
    @SuppressWarnings("unchecked")
	public static void downLoadExcel(HttpServletResponse response,String filename, String[] title_name,
    		String[] col_name, List ls_data, String[] code_col,String list_data_type) {
        try {
        	OutputStream os = response.getOutputStream();// 取得输出流
        	response.reset();// 清空输出流
        	if(null==filename || "".equals(filename)){
        		filename="file";
        	}
            //response.setHeader("Content-disposition", "attachment; filename="+filename+".xls");// 设定输出文件头
            response.setHeader("Content-Disposition", "attachment; filename="+new String(filename.getBytes("GBK"),"ISO8859-1")+".xls"); //java.net.URLEncoder.encode(, "UTF-8")
            response.setContentType("application/msexcel");// 定义输出类型
            WritableWorkbook workbook = Workbook.createWorkbook(os);
            /////////////////////////////////////////////////////////////////////
//            String tmptitle = "财务报表"; // 标题
//            WritableSheet wsheet = workbook.createSheet(tmptitle, 0); // sheet名称
//            // 设置excel标题
//            WritableFont wfont = new WritableFont(WritableFont.ARIAL, 16,WritableFont.BOLD, false,UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
//            WritableCellFormat wcfFC = new WritableCellFormat(wfont);
//            wcfFC.setBackground(Colour.AQUA);
//            wsheet.addCell(new Label(1, 0, tmptitle, wcfFC));
//            wfont = new jxl.write.WritableFont(WritableFont.ARIAL, 14,WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
//            wcfFC = new WritableCellFormat(wfont);
            //HashMap hm_code_name = getCodeHashMap(code_col);
            WritableSheet ws = workbook.createSheet("sheet 1", 0);
            ws.setColumnView(19, 0);
//            ws.getSettings().setPassword("123");
            int rowNum = 0; // 要写的行
            if (title_name != null) {
                putRow(ws, 0, title_name);// 写入标题
                rowNum = 1;
            }
            if (ls_data==null || ls_data.size()==0){
            	ls_data= new ArrayList();
            }
            if (col_name==null || col_name.length==0){
            	col_name = new String[0];
            }
            for (int i = 0; i < ls_data.size(); i++, rowNum++) {// 写sheet
            	Object obj = ls_data.get(i);
            	Object[] cells = new Object[col_name.length];//ls_data.get(i);
                for (int j = 0; j < col_name.length; j++) {// 写sheet
                	String field = col_name[j];
                	String value = "";
    				field = field.substring(0, 1).toUpperCase()	+ field.substring(1);
    				String methodStr = null;
    				methodStr = "get" + field;
    				Method method = null;
    				try {
    					method = obj.getClass().getMethod(methodStr);
    					if (method == null) {value="";}
    					Object o = method.invoke(obj);
    					value = objectToString(o);
    					if (value == null) {value="";}
    				} catch (Exception e) {
    					value = "";
    				}
    				value = trunsCodeDmsm1(field, value, null);
    				cells[j] = value;
                }
                putRow(ws, rowNum, cells); // 写入数据
                }
            workbook.write();
            workbook.close(); // 一定要关闭, 否则没有保存Excel
            os.close();
        } catch (RowsExceededException e) {
        	LoggerUtil.error("jxl write RowsExceededException: ",e);
//            System.out.println("jxl write RowsExceededException: " + e.getMessage());
			throw new RuntimeException(e);
        } catch (WriteException e) {
        	LoggerUtil.error("jxl write WriteException: ",e);
//            System.out.println("jxl write WriteException: " + e.getMessage());
			throw new RuntimeException(e);
        } catch (IOException e) {
        	LoggerUtil.error("jxl write file i/o exception!, cause by: ",e);
//            System.out.println("jxl write file i/o exception!, cause by: " + e.getMessage());
			throw new RuntimeException(e);
        }catch (Exception e) {
        	LoggerUtil.error("jxl 未知错误!, cause by: ",e);
//            System.out.println("jxl 未知错误!, cause by: " + e.getMessage());
			throw new RuntimeException(e);
        }finally{
//        	workbook.write();
//          workbook.close(); // 一定要关闭, 否则没有保存Excel
//          os.close();
        }
    }

    @SuppressWarnings("unchecked")
	private static String trunsCodeDmsm1(String col,String val,HashMap hm_code_name){
    	if(null==col || "".equals(col)){
    		return val;
    	}
    	if(null==val || "".equals(val)){
    		return val;
    	}
    	if(null==hm_code_name){
    		return val;
    	}
    	HashMap hm_code_value = (HashMap)hm_code_name.get(col);
    	if(null==hm_code_value){
    		return val;
    	}
    	String t_val = (String)hm_code_value.get(val);
    	if(null==t_val || "".equals(t_val)){
    		return val;
    	}else{
    		return t_val;
    	}
    } 
	/**private static HashMap getCodeHashMap(String[] code_col) {
		/////////////////////////////////////////////////////////////////////
		HashMap hm_code_name = new HashMap();
		if(null==code_col){code_col=new String[0];}
		for(int i=0;i<code_col.length;i++){
			String t_code_col = code_col[i];
			if(null!=t_code_col && !"".equals(t_code_col)){
				String[]arr_code_col = t_code_col.split(",");
				if(null!=arr_code_col && arr_code_col.length==2){
					String zdmc=arr_code_col[0];
					String dmlb=arr_code_col[1];
					List ls_code = CodeTools.getCodeList(dmlb);
					if(null==ls_code){ls_code = new ArrayList();}
					HashMap hm_code_value = new HashMap();
					for(int j=0;j<ls_code.size();j++){
						Code code = (Code)ls_code.get(j);
						hm_code_value.put(code.getDmz(), code.getDmsm1());
					}
					hm_code_name.put(zdmc, hm_code_value);
				}
			}
		}
		return hm_code_name;
		/////////////////////////////////////////////////////////////////////
	}**/

    /**
     * 写入一行数据
     */
    private static void putRow(WritableSheet ws, int rowNum, Object[] cells) throws RowsExceededException, WriteException {
		WritableCellFeatures cellFeatures = null;
		WritableCellFeatures cellFeatures1 = null;
		WritableCellFeatures cellFeatures2 = null;
		
    	
    	if(rowNum == 0){
    		cellFeatures = new WritableCellFeatures();
    		cellFeatures1 = new WritableCellFeatures();
    		cellFeatures2 = new WritableCellFeatures();
			cellFeatures.setComment("日期必填，日期格式以  2015/10/10 例子为准");
			cellFeatures1.setComment("是/否");
			cellFeatures2.setComment("借据编号必填");
    	}
        for (int j = 0; j < cells.length; j++) {
            Label cell = new Label(j, rowNum, "" + cells[j]);
            if(rowNum == 0 && j == 0){
				cell.setCellFeatures(cellFeatures2);
        	}
        	if(rowNum == 0 && j == 1){
				cell.setCellFeatures(cellFeatures);
        	}
        	if(rowNum == 0 && (j == 15 || j == 16 || j == 17 || j == 18)){
				cell.setCellFeatures(cellFeatures1);
        	}
            ws.addCell(cell);
        }
    }
    public static String objectToString(Object obj) {
		String strArray = "";
		if (obj == null) {return null;}
		if(obj == null){strArray = "";}
		else if (obj instanceof String) {
			String value = (String) obj;
			if (value == null || "null".equalsIgnoreCase(value)) {value = "";}
			strArray = value;
		} else if (obj instanceof Date) {
			Date date = (Date) obj;
			if (date == null) {strArray = "";}
			String dateStr = CommonUtil.FormatDate10(date);
			strArray = dateStr;
		} else if (obj instanceof Timestamp) {
			Timestamp date = (Timestamp) obj;
			if (date == null) {strArray = "";}
			String dateStr = CommonUtil.FormatDate10(new Date(date.getTime()));
			strArray = dateStr;
		} else if (obj instanceof Integer) {
			Integer value = (Integer) obj;
			strArray = String.valueOf(value.intValue());
		} else if (obj instanceof Long) {
			Long value = (Long) obj;
			strArray = String.valueOf(value.longValue());
		} else if (obj instanceof Double) {
			Double value = (Double) obj;
			strArray = String.valueOf(value.doubleValue());
		} else if (obj instanceof Float) {
			Float value = (Float) obj;
			strArray = String.valueOf(value.floatValue());
		} else if (obj instanceof Short) {
			Short value = (Short) obj;
			strArray = String.valueOf(value.shortValue());
		} else if (obj instanceof BigDecimal) {
			BigDecimal value = (BigDecimal) obj;
			strArray = String.valueOf(value.doubleValue());
		} else if (obj instanceof Boolean) {
			Boolean value = (Boolean) obj;
			strArray = String.valueOf(value.booleanValue());
		} else {
			strArray = String.valueOf(obj);
		}
		return strArray;
	}

}
