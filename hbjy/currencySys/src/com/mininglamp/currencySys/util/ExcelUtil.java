package com.mininglamp.currencySys.util;

import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


public class ExcelUtil {
	
	/**
	* 创建excel文档，
	*
	* @param list        数据
	* @param keys        list中map的key数组集合
	* @param columnNames excel的列名
	* //导出格式为07版本的Excel 可以导出1048576行数据
	*/
	public static Workbook createWorkBook(List<Map> list, String[] keys, String columnNames[]) {
		//导出格式为07版本的Excel 可以导出1048576行数据
		long startTime = System.currentTimeMillis(); // 开始时间
		System.out.println("strat execute time: " + startTime);
		Workbook wb = new SXSSFWorkbook(100); // 关键语句  // 在内存当中保持 100 行 , 超过的数据放到硬盘中
		Sheet sheet = null; // 工作表对象
		Row nRow = null; // 行对象
		Cell nCell = null; // 列对象
		int rowNo = 0; // 总行号
		int pageRowNo = 0; // 页行号

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

		// 设置第二种单元格的样式（用于值）
		cs2.setFont(f2);
		cs2.setBorderLeft(CellStyle.BORDER_THIN);
		cs2.setBorderRight(CellStyle.BORDER_THIN);
		cs2.setBorderTop(CellStyle.BORDER_THIN);
		cs2.setBorderBottom(CellStyle.BORDER_THIN);
		cs2.setAlignment(CellStyle.ALIGN_CENTER);

		if(list.size() == 0){
			sheet = wb.createSheet("数据0");
			Row row = sheet.createRow(0);
			for (int i = 0; i < columnNames.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(columnNames[i]);
				cell.setCellStyle(cs);
				sheet.setColumnWidth((short) i, (short) (35.7 * 150));
			}
		}
		
		// 设置每行每列的值
		for (int i = 0; i < list.size(); i++) {
			// 打印1000000条后切换到下个工作表，可根据需要自行拓展，2百万，3百万...数据一样操作，只要不超过1048576就可以
			if (i % 1000000 == 0) {
				System.out.println("Current Sheet:" + i / 1000000);
//				sheet = wb.createSheet("我的第" + (i / 1000000) + "个工作簿");// 建立新的sheet对象
				sheet = wb.createSheet("数据"+(i / 1000000));
				sheet = wb.getSheetAt(i / 1000000); // 动态指定当前的工作表
				pageRowNo = 0; // 每当新建了工作表就将当前工作表的行号重置为0
				Row row = sheet.createRow(0);
				for (int j = 0; j < columnNames.length; j++) {
					nCell = row.createCell(j);
					nCell.setCellValue(columnNames[j]);
					nCell.setCellStyle(cs);
					sheet.setColumnWidth((short) j, (short) (35.7 * 150));
				}
			}
			// Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
			// 创建一行，在页sheet上
			Row row1 = sheet.createRow((i % 1000000) + 1);
			// 在row行上创建一个方格
			for (int j = 0; j < keys.length; j++) {
				Cell cell = row1.createCell(j);
				cell.setCellValue(list.get(i).get(keys[j]) == null ? "" : list.get(i).get(keys[j]).toString());
				cell.setCellStyle(cs2);
			}
		}
		long finishedTime = System.currentTimeMillis(); // 处理完成时间
		System.out.println("处理完成时间: " + (finishedTime - startTime) / 1000 + "m");
		return wb;
	}
	
	
	public static List<String> getDays(String begin,String end) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		Date bd = sdf.parse(begin);
		Date ed = sdf.parse(end);
		Calendar tempStart = Calendar.getInstance();
		tempStart.setTime(bd);
		List<String> result = new ArrayList<String>();
		while(bd.getTime()<=ed.getTime()){
			result.add(sdf.format(tempStart.getTime()));
			tempStart.add(Calendar.DAY_OF_YEAR, 1);
			bd = tempStart.getTime();
		}
		return result;
	}
	
	/**
	* 创建excel文档，
	*
	* @param list        数据
	* @param keys        list中map的key数组集合
	* @param columnNames excel的列名
	* //导出为03版本的 最大导出65536行数据 列256
	*/
	public static Workbook createWorkBookt(List<Map> list, String[] keys, String columnNames[]) {
		//导出为03版本的 最大导出65536行数据 列256
		// 创建excel工作簿
		long startTime = System.currentTimeMillis(); // 开始时间
		Workbook wb = new HSSFWorkbook();
		// 创建第一个sheet（页），并命名
		Sheet sheet = wb.createSheet("数据");
		// 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
		for (int i = 0; i < keys.length; i++) {
			sheet.setColumnWidth((short) i, (short) (35.7 * 150));
		}
		
		// 创建第一行
		Row row = sheet.createRow(0);//sheet.createRow((short) 0);
		
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
		
		// 设置第二种单元格的样式（用于值）
		cs2.setFont(f2);
		cs2.setBorderLeft(CellStyle.BORDER_THIN);
		cs2.setBorderRight(CellStyle.BORDER_THIN);
		cs2.setBorderTop(CellStyle.BORDER_THIN);
		cs2.setBorderBottom(CellStyle.BORDER_THIN);
		cs2.setAlignment(CellStyle.ALIGN_CENTER);
		//设置列名
		for (int i = 0; i < columnNames.length; i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(columnNames[i]);
			cell.setCellStyle(cs);
		}
		//设置每行每列的值
		for (int i = 0; i < list.size(); i++) {
			// Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
			// 创建一行，在页sheet上
			Row row1 = sheet.createRow(i+1);
			// 在row行上创建一个方格
			for (int j = 0; j < keys.length; j++) {
				Cell cell = row1.createCell(j);
				cell.setCellValue(list.get(i).get(keys[j]) == null ? " " : list.get(i).get(keys[j]).toString());
				cell.setCellStyle(cs2);
			}
		}
		long finishedTime = System.currentTimeMillis(); // 处理完成时间
		System.out.println("处理完成时间: " + (finishedTime - startTime) / 1000 + "m");
		return wb;
	}
	
	/**
	 * 导出csv
	 * @param file csv文件(路径+文件名)
	 * @param dataList dataList 数据
	 * @return
	 */
	public static boolean exportCsv(String fileName, List<Map> dataList,String[] keys){
		boolean isSucess=false;
//		FileOutputStream out=null;
//		OutputStreamWriter osw=null;
//		BufferedWriter bw=null;
		try {
			FileWriter writer = new FileWriter(fileName, true);
//			out = new FileOutputStream(file);
//			osw = new OutputStreamWriter(out);
//			bw =new BufferedWriter(osw);
			if(dataList!=null && !dataList.isEmpty()){
				for(int i = 0;i<dataList.size();i++){
					String row = "";
					Map oneData = dataList.get(i);
					for(int j = 0;j<keys.length;j++){
						row += (oneData.get(keys[j]) == null ? "  " : oneData.get(keys[j]).toString())+"  |  ";
					}
					writer.write(row+"\r");
//					bw.append(row).append("\r");
				}
				/*
				for(String data : dataList){
					bw.append(data).append("\r");
				}
				*/
			}
			isSucess=true;
		} catch (Exception e) {
			System.out.println("***********************************************************************************************************");
			System.out.println(e);
			isSucess=false;
		}finally{
			/*
			if(bw!=null){
				try {
					bw.close();
					bw=null;
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
			if(osw!=null){
				try {
					osw.close();
					osw=null;
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
			if(out!=null){
				try {
					out.close();
					out=null;
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
			*/
		}
		return isSucess;
	}
	
}