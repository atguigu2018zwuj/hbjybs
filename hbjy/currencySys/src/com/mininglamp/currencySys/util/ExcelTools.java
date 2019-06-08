
package com.mininglamp.currencySys.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.CellView;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.lang3.math.NumberUtils;

import com.mininglamp.logx.util.CloseUtil;
import com.mininglamp.currencySys.bean.MultiSheetXlsExpBean;
import com.mininglamp.currencySys.bean.SheetBean;


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
public class ExcelTools {
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
     *
     */
    @SuppressWarnings("unchecked")
	public static void downLoadExcel(HttpServletResponse response,String filename,String[] title_name,
    		String[] col_name, List ls_data, String[] code_col,String list_data_type) {
    	List<String[]> strList = new ArrayList<String[]>();
    	strList.add(title_name);
    	downLoadExcel(response,filename,strList,col_name,ls_data,code_col,list_data_type,null);
    }
    /**
     * 获取列宽度，根据文本长度以及最大值和最小值综合计算
     * @return
     */
    private static int getCellWidth(String val){
    	/**
    	 * 单位256
    	 * 最小14单位
    	 * 最大30单位
    	 */
    	int unit = 256,min = 14,max = 30,len = min;
    	if(val == null) return len*unit;
    	try {
			len = val.getBytes("GBK").length + 4;
			len = Math.min(Math.max(min, len), max);
		} catch (UnsupportedEncodingException e) {
		}
    	return len * unit;
    }
    /**
     * 填充单sheet内容
     * @param workbook
     * @param sheet_name
     * @param title_name
     * @param col_name
     * @param ls_data
     * @param code_col
     * @param list_data_type
     * @param intList
     * @throws RowsExceededException
     * @throws WriteException
     */
    @SuppressWarnings("unchecked")
	public static void sheetFill(WritableWorkbook workbook,String sheetName,int sheetIdx,List<String[]> title_name,
    		String[] col_name, List ls_data, String[] code_col, List<int[]> intList) throws RowsExceededException, WriteException{
    	if(sheetName == null) sheetName = "sheet " + (sheetIdx+1);
    	WritableSheet ws = workbook.createSheet(sheetName, sheetIdx);
        //设置
        Map<String,WritableCellFormat> wcfs = getCellFormat();
        /**
         * 列宽设置
         * 在表格内自动设置
        CellView cv = new CellView();
        cv.setAutosize(false);
        cv.setSize(15*256);
        //有效列数
        int colLen = col_name == null ? 14 : col_name.length;
        for(int i=0;i<colLen;i++){
        	ws.setColumnView(i, cv);
        }
        */
        int rowNum = 0; // 要写的行
        WritableCellFormat titleFormat = wcfs.get("title");
        if (title_name != null) {
        	for(; rowNum<title_name.size(); rowNum++){
        		putTitle(ws, rowNum, title_name.get(rowNum), titleFormat, title_name);// 写入标题
        	}
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
        	Map<String,Object> lineMap = null;
        	if(obj instanceof Map){
        		lineMap = (Map<String, Object>) obj;
        	}
            for (int j = 0; j < col_name.length; j++) {// 写sheet
            	String field = col_name[j];
            	String value = "";
            	if(lineMap!=null){
            		Object valObj = lineMap.get(field);
            		if(valObj != null){
            			value = lineMap.get(field).toString();
            		}
            	}else{
            		Object resultObj = CommonUtil.getValueByField(obj, field);
            		if(resultObj != null) value = objectToString(resultObj);
            		else value = "";
    				value = trunsCodeDmsm1(field, value, null);
            	}
				cells[j] = value;
            }
            putRow(ws, rowNum, cells, wcfs, title_name); // 写入数据
            }

        //首先要合并单元格
        ExcelTools.mergeCell(intList, ws);
    }
    /**
     * 格式生成
     * 注意：每次导出时都需要新建cellformat对象，否则如果作为全局变量保存后，下次导出会报错
     * @return
     */
    private static Map<String, WritableCellFormat> getCellFormat() {
    	Map<String, WritableCellFormat> cellFormatMap = new HashMap<String,WritableCellFormat>();
    	JXLFormat foramt = new JXLFormat();
        WritableCellFormat wcf = foramt.getCellFormat(foramt.bold_font, null);
        WritableCellFormat wcf1 = foramt.getCellFormat(foramt.nobold_font, NumberFormats.TEXT);
        WritableCellFormat wcf2 = foramt.getCellFormat(foramt.nobold_font, NumberFormats.PERCENT_FLOAT);
        WritableCellFormat wcf3 = foramt.getCellFormat(foramt.nobold_font, new NumberFormat("0.00"));
        WritableCellFormat wcf4 = foramt.getCellFormat(foramt.nobold_font, new NumberFormat("0"));
        
        try {
			wcf.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
			wcf.setAlignment(Alignment.CENTRE);
			wcf.setBackground(Colour.LIGHT_GREEN);
			wcf1.setAlignment(Alignment.RIGHT);
			wcf1.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
			wcf2.setAlignment(Alignment.RIGHT);
			wcf2.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
			wcf3.setAlignment(Alignment.RIGHT);
			wcf3.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
			wcf4.setAlignment(Alignment.RIGHT);
			wcf4.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
		} catch (Exception e) {
		}
		cellFormatMap.put("title", wcf);
        cellFormatMap.put("default", wcf1);
        cellFormatMap.put("percent", wcf2);
        cellFormatMap.put("double", wcf3);
        cellFormatMap.put("integer", wcf4);
		return cellFormatMap;
	}

	/**
     * 多sheet统一导出
     * @param response
     * @param multiSheetXlsExpBean
     */
    @SuppressWarnings("unchecked")
	public static void downLoadExcelMultiSheet(HttpServletResponse response,MultiSheetXlsExpBean multiSheetXlsExpBean){
    	String filename = multiSheetXlsExpBean.getFileName();
    	if(null==filename || "".equals(filename)){
    		filename="file";
    	}
    	OutputStream os = null;
        	try {
				os = response.getOutputStream();
				response.reset();// 清空输出流
				response.setHeader("Content-Disposition", "attachment; filename="+new String(filename.getBytes("GBK"),"ISO8859-1")+".xls"); //java.net.URLEncoder.encode(, "UTF-8")
	            response.setContentType("application/msexcel");// 定义输出类型
				//获取文档
	            WritableWorkbook workbook = Workbook.createWorkbook(os);
				/**
				 * 循环放入sheet
				 */
	            List<SheetBean> sheetList = multiSheetXlsExpBean.getSheets();
	            for (SheetBean sheetBean : sheetList) {
					String sheetName = sheetBean.getSheetName();
					int sheetIdx = sheetBean.getSheetIdx();
					List<String[]> title_name = sheetBean.getTitle_name();
					String[] col_name = sheetBean.getCol_name();
					List ls_data = sheetBean.getLs_data();
					String[] code_col = sheetBean.getCode_col();
					List<int[]> intList = sheetBean.getIntList();
					
					sheetFill(workbook, sheetName, sheetIdx, title_name, col_name, ls_data, code_col, intList);
				}
	            workbook.write();
	            workbook.close(); // 一定要关闭, 否则没有保存Excel
			} catch (IOException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}finally{
				CloseUtil.closeIO(os);
			}
    }
    @SuppressWarnings("unchecked")
	public static void downLoadExcel(HttpServletResponse response,String filename, List<String[]> title_name,
    		String[] col_name, List ls_data, String[] code_col,String list_data_type, List<int[]> intList) {
    	MultiSheetXlsExpBean multiSheetXlsExpBean = new MultiSheetXlsExpBean();
    	multiSheetXlsExpBean.setFileName(filename).setSheet(title_name, col_name, ls_data, code_col, intList);
    	downLoadExcelMultiSheet(response,multiSheetXlsExpBean);
    }
    
    
    public static boolean checkIsPercent(List<String[]> titles, int j){
		boolean r = false;
		if(titles == null) return false;
		
		for(String[] title: titles){
			if(title == null) continue;
			String value = title[j];
			
			if(value == null) continue;
			
			if(j < title.length && (value.contains("率") || value.contains("增减") || value.contains("之比"))){
				r = true;
				return r;
			}
		}
		return r;
	}
    /**
     * 获取样式
     * @return
     * @throws WriteException 
     */
    private static Map<String,WritableCellFormat> getFormats() throws WriteException{
    	JXLFormat foramt = new JXLFormat();
    	Map<String,WritableCellFormat> wcfs = new HashMap<String,WritableCellFormat>();
        WritableCellFormat wcf = foramt.getCellFormat(foramt.bold_font, null);
        WritableCellFormat wcf1 = foramt.getCellFormat(foramt.nobold_font, NumberFormats.TEXT);
        WritableCellFormat wcf2 = foramt.getCellFormat(foramt.nobold_font, NumberFormats.PERCENT_FLOAT);
        WritableCellFormat wcf3 = foramt.getCellFormat(foramt.nobold_font, new NumberFormat("0.00"));
        WritableCellFormat wcf4 = foramt.getCellFormat(foramt.nobold_font, new NumberFormat("0"));
        
        WritableFont titleFont = new WritableFont(WritableFont.ARIAL,12,WritableFont.BOLD,false);
        WritableCellFormat titleFormat = foramt.getCellFormat(titleFont, NumberFormats.TEXT);
        titleFormat.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
        titleFormat.setBackground(Colour.GRAY_25);
        try {
			wcf.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
			wcf.setAlignment(Alignment.CENTRE);
			wcf.setBackground(Colour.LIGHT_GREEN);
			wcf1.setAlignment(Alignment.RIGHT);
			wcf2.setAlignment(Alignment.RIGHT);
			wcf3.setAlignment(Alignment.RIGHT);
			wcf4.setAlignment(Alignment.RIGHT);
		} catch (Exception e) {
		}
		wcfs.put("title", titleFormat);
        wcfs.put("default", wcf1);
        wcfs.put("percent", wcf2);
        wcfs.put("double", wcf3);
        wcfs.put("integer", wcf4);
        return wcfs;
    }
    
    @SuppressWarnings("unchecked")
	public static void downLoadExcelNew(HttpServletResponse response,String filename,List<Map<String[], WritableCellFormat>> title_name,
    		String[] col_name, List ls_data, String[] code_col,String list_data_type, List<int[]> intList,String widthAuto) {
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
            /**
             * 2016年4月1日13:19:08
             * czy
             * 修改为正文不加粗
             */
            WritableFont wf = new WritableFont(WritableFont.ARIAL, 11, WritableFont.NO_BOLD, false);
            WritableCellFormat wcf = new WritableCellFormat(wf);
            wcf.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
            wcf.setAlignment(Alignment.CENTRE);
            Map<String,WritableCellFormat> wcfs = getFormats();
            WritableSheet ws = workbook.createSheet("sheet 1", 0);
            
            if(widthAuto == null){
            	for(int i=0;i<26;i++){
            		ws.setColumnView(i, 15);
            	}
            }
            
            int rowNum = 0; // 要写的行

            //if(title != null)putRow(ws,0,title,wcf);
            @SuppressWarnings("unused")
			int ii = 0;
            if (title_name.get(0) != null) {
                rowNum = putTitles(ws, title_name);// 写入标题1
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
            	Map<String,Object> lineMap = null;
            	if(obj instanceof Map){
            		lineMap = (Map<String, Object>) obj;
            	}
                for (int j = 0; j < col_name.length; j++) {// 写sheet
                	String field = col_name[j];
                	String value = "";
                	if(lineMap!=null){
                		Object valObj = lineMap.get(field);
                		if(valObj != null)
                			value = lineMap.get(field).toString();
                	}else{
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
                	}
    				cells[j] = value;
                }
                putRow(ws, rowNum, cells, wcfs, null); // 写入数据
                }

            //首先要合并单元格
            ExcelTools.mergeCell(intList, ws);
            
            
            
            
            
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
     * wcfs.put("title", wcf);
        wcfs.put("default", wcf1);
        wcfs.put("percent", wcf2);
        wcfs.put("double", wcf3);
     */
    private static void putRow(WritableSheet ws, int rowNum, Object[] cells, Map<String,WritableCellFormat> wcfs, List<String[]> title_name) throws RowsExceededException, WriteException {
        for (int j = 0; j < cells.length; j++) {
        	Label cell = null;
        	jxl.write.Number c = null;
        	Object cellObj = cells[j];
        	if(cellObj == null) continue;
        	String valStr = cellObj.toString().trim();
        	if(NumberUtils.isNumber(valStr) && checkIsPercent(title_name, j)){
        		c = new jxl.write.Number(j, rowNum, Double.parseDouble(valStr), wcfs.get("percent"));
        	}else if(NumberUtils.isNumber(valStr) && valStr.contains(".")){
        		c = new jxl.write.Number(j, rowNum, Double.parseDouble(valStr), wcfs.get("double"));
        	}else if(valStr.equals("0") || (NumberUtils.isNumber(valStr) && !valStr.startsWith("0")&&valStr.length() < 16)){
        		c = new jxl.write.Number(j, rowNum, Double.parseDouble(valStr), wcfs.get("integer"));
        	}else{
        		cell = new Label(j, rowNum, valStr, wcfs.get("default"));
        	}
        	if(c != null){
        		ws.addCell(c);
        	}else{
        		ws.addCell(cell);
        	}
        	fitCellWidth(ws,j,valStr);
        }
    }
    /**
     * 标题信息
     * @param ws
     * @param rowNum
     * @param cells
     * @param wcfs
     * @param title_name
     * @throws RowsExceededException
     * @throws WriteException
     */
    private static void putTitle(WritableSheet ws, int rowNum, Object[] cells, WritableCellFormat titleFormat, List<String[]> title_name) throws RowsExceededException, WriteException {
    	for (int j = 0; j < cells.length; j++) {
    		Label cell = null;
    		Object cellObj = cells[j];
    		if(cellObj == null) continue;
    		String valStr = cellObj.toString().trim();
    		cell = new Label(j, rowNum, valStr, titleFormat);
    		
    		ws.addCell(cell);
    		fitCellWidth(ws,j,valStr);
    	}
    }
    /**
     * 列宽自适应
     * @param ws
     * @param colIdx
     * @param valStr
     */
    private static void fitCellWidth(WritableSheet ws,int colIdx,String valStr){
    	//列宽
		CellView cellView = ws.getColumnView(colIdx);
		if(cellView == null) {
			cellView = new CellView();
		}
		int nowSize = cellView.getSize();
		int size = getCellWidth(valStr);
		if(nowSize < size) cellView.setSize(size);
		ws.setColumnView(colIdx, cellView);
    }
    /**
     * 写入标题
     */
    private static int putTitles(WritableSheet ws,  List<Map<String[], WritableCellFormat>> list) throws RowsExceededException, WriteException {
        for (int j = 0; j < list.size(); j++) {
        	for(String[] keyArr:list.get(j).keySet()){
        		for(int i=0;i<keyArr.length;i++){
            		Label cell = new Label(i, j, "" + keyArr[i], list.get(j).get(keyArr));
                  ws.addCell(cell); 
        			
        		}
        	}
        }
        
        return list.size();
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

    /**
     * 合并单元格
     */
    public static void mergeCell(List<int[]> list, WritableSheet ws){
    	if(ws != null && list != null && list.size() > 0){
    		for(int[] a: list){
    			try {
					ws.mergeCells(a[0], a[1], a[2], a[3]);
				} catch (RowsExceededException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				} catch (WriteException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
    		}
    	}
    }
}
