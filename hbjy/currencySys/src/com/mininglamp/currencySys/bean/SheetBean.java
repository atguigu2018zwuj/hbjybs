package com.mininglamp.currencySys.bean;

import java.util.List;
/**
 * 针对导出的sheet信息
 * @author czy
 * 2016年11月29日16:37:13
 */
@SuppressWarnings("unchecked")
public class SheetBean{
	/**
	 * 设置一个sheet
	 * <br/>title_name：excel第一行标题的名称(字符串数组，第一行内容)
     * <br/>col_name：所有属性列的名称
     * <br/>ls_data：数据List
     * <br/>code_col：需要转换的代码的列的名称,代码类别（dmlb）用逗号分割.例子：(zdlx,8005)
	 **/
	public SheetBean(List<String[]> titleName, String[] colName, List lsData,String[] codeCol, List<int[]> intList) {
		this.code_col = codeCol;
		this.col_name = colName;
		this.intList = intList;
		this.ls_data = lsData;
		this.title_name = titleName;
	}
	private List<String[]> title_name;
	private String[] col_name;
	
	private List ls_data;
	private String[] code_col;
	private List<int[]> intList;
	
	private String sheetName;
	private int sheetIdx = 0;
	
	
	public String getSheetName() {
		return sheetName;
	}
	public SheetBean setSheetName(String sheetName) {
		this.sheetName = sheetName;
		return this;
	}
	public int getSheetIdx() {
		return sheetIdx;
	}
	public SheetBean setSheetIdx(int sheetIdx) {
		this.sheetIdx = sheetIdx;
		return this;
	}
	public List<String[]> getTitle_name() {
		return title_name;
	}
	public void setTitle_name(List<String[]> titleName) {
		title_name = titleName;
	}
	public String[] getCol_name() {
		return col_name;
	}
	public void setCol_name(String[] colName) {
		col_name = colName;
	}
	public List getLs_data() {
		return ls_data;
	}
	public void setLs_data(List lsData) {
		ls_data = lsData;
	}
	public String[] getCode_col() {
		return code_col;
	}
	public void setCode_col(String[] codeCol) {
		code_col = codeCol;
	}
	public List<int[]> getIntList() {
		return intList;
	}
	public void setIntList(List<int[]> intList) {
		this.intList = intList;
	}
}