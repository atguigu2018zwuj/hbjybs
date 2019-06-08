package com.mininglamp.currencySys.bean;

import java.util.ArrayList;
import java.util.List;
/**
 * excel多sheet导出设置
 * @author czy
 * 2016年11月29日16:37:28
 */
public class MultiSheetXlsExpBean {
	/**
	 * 文件名
	 */
	private String fileName;
	private List<SheetBean> sheets = new ArrayList<SheetBean>();
	
	
	public String getFileName() {
		return fileName;
	}


	public MultiSheetXlsExpBean setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}
	/**
	 * 设置一个sheet
	 * <br/>title_name：excel第一行标题的名称(字符串数组，第一行内容)
     * <br/>col_name：所有属性列的名称
     * <br/>ls_data：数据List
     * <br/>code_col：需要转换的代码的列的名称,代码类别（dmlb）用逗号分割.例子：(zdlx,8005)
     * <br/>list_data_type:list数据的类别。(BEAN或MAP)，合并单元格数组
	 **/
	@SuppressWarnings("unchecked")
	public SheetBean setSheet(List<String[]> title_name,
    		String[] col_name, List ls_data, String[] code_col, List<int[]> intList){
		SheetBean sheet = new SheetBean(title_name,col_name,ls_data,code_col,intList);
		sheets.add(sheet);
		return sheet.setSheetIdx(sheets.size()-1);
	}
	public List<SheetBean> getSheets(){
		return sheets;
	}
}
