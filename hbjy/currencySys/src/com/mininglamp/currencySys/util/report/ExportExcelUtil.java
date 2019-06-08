package com.mininglamp.currencySys.util.report;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.Region;

/**
 * 根据表头模版导出数据
 * 
 * @author JH
 *
 */
public class ExportExcelUtil {

	private static final String String = null;

	/**
	 * 描述：根据文件路径获取项目中的文件 (默认文件路径 WEB-INF->……文件)
	 * 
	 * @param fileDir
	 *            文件路径
	 * @return
	 * @throws Exception
	 */
	public File getExcelDemoFile(String fileDir) throws Exception {
		String classDir = null;
		String fileBaseDir = null;
		File file = null;
		classDir = Thread.currentThread().getContextClassLoader()
				.getResource("/").getPath();
		fileBaseDir = classDir.substring(0, classDir.lastIndexOf("classes"));
		// 读取文件
		file = new File(fileBaseDir + fileDir);
		if (!file.exists()) {
			throw new Exception("模板文件不存在！");
		}
		return file;
	}

	/**
	 * 写入新excel
	 * 
	 * @param file
	 *            文件 、sheetName sheet、date 后台数据、filed 表中列字段、params 参数
	 * @return
	 * @throws Exception
	 */
	public Workbook writeNewExcel(File file, String sheetName, List<Map> data,
			List<String> filed, Map<String, Object> params) throws Exception {
		Workbook wb = null;
		Row row = null;
		Cell cell = null;

		FileInputStream fis = new FileInputStream(file);
		wb = new ImportExcelUtil().getWorkbook(fis, file.getName()); // 获取工作薄
		Sheet sheet = wb.getSheet(sheetName);
		int sheetIndex = wb.getSheetIndex(sheet);
		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			if (i != sheetIndex) {
				wb.removeSheetAt(i);
			}
		}
		Row row0 = sheet.getRow(0);
		// title
		Cell title = row0.getCell(0);
		if (title == null) {
			title = row0.createCell(0);
		}
		title.setCellValue((String) params.get("title"));

		Row row2 = sheet.getRow(2);
		if (filed.size() < 7) {

			// 填报单位
			Cell fillCompany = row2.getCell(0);
			if (fillCompany == null) {
				fillCompany = row2.createCell(0);
			}
			fillCompany.setCellValue((String) params.get("fillCompany"));
			// 报告期
			Cell date = row2.getCell(2);
			if (date == null) {
				date = row2.createCell(2);
			}
			date.setCellValue((String) params.get("date"));
			// 单位
			Cell dw = row2.getCell(3);
			if (dw == null) {
				dw = row2.createCell(3);
			}
			dw.setCellValue((String) params.get("dw"));

		} else {
			// 填报单位
			Cell fillCompany = row2.getCell(1);
			if (fillCompany == null) {
				fillCompany = row2.createCell(1);
			}
			fillCompany.setCellValue((String) params.get("fillCompany"));
			// 报告期
			Cell date = row2.getCell(4);
			if (date == null) {
				date = row2.createCell(4);
			}
			date.setCellValue((String) params.get("date"));
			// 单位
			Cell dw = row2.getCell(6);
			if (dw == null) {
				dw = row2.createCell(6);
			}
			dw.setCellValue((String) params.get("dw"));
		}
		// 循环插入数据
		int lastRow = sheet.getLastRowNum() + 1; // 插入数据的数据ROW
		CellStyle cs = setSimpleCellStyle(wb); // Excel单元格样式
		for (int i = 0; i < data.size(); i++) {
			row = sheet.createRow(lastRow + i); // 创建新的ROW，用于数据插入
			// 按项目实际需求，在该处将对象数据插入到Excel中
			Map<String, Object> map = data.get(i);
			// Cell赋值开始
			// 遍历列数
			for (int k = 0; k < filed.size(); k++) {
				cell = row.createCell(k);
				// 赋列值
				try {
					BigDecimal d = (BigDecimal) map.get(filed.get(k));
					cell.setCellValue(d.doubleValue());
				} catch (Exception e) {
					cell.setCellValue((String) map.get(filed.get(k)));
				}
				// 定义样式
				cell.setCellStyle(cs);
			}
		}
		cs = lastCellStyle(wb);
		// 末尾添加元素
		lastRow = sheet.getLastRowNum() + 1;
		row = sheet.createRow(lastRow);
		// 列
		Cell lastRowCell = row.createCell(0);
		if (filed.size() < 7) {
			CellStyle cs2 = lastCellStyle2(wb);
			// 复核
			lastRowCell.setCellValue("复核："+(String) params.get("fh"));
			lastRowCell.setCellStyle(cs2);
			// 制表
			lastRowCell = row.createCell(1);
			lastRowCell.setCellValue("制表："+(String) params.get("zb"));
			lastRowCell.setCellStyle(cs2);
			// 联系电话
			lastRowCell = row.createCell(2);
			lastRowCell.setCellValue("联系电话："+(String) params.get("lxdh"));
			lastRowCell.setCellStyle(cs2);
		} else {
			// 复核
			lastRowCell.setCellValue("复核：");
			lastRowCell.setCellStyle(cs);
			lastRowCell = row.createCell(1);
			lastRowCell.setCellValue((String) params.get("fh"));

			// 制表
			lastRowCell = row.createCell(2);
			lastRowCell.setCellValue("制表：");
			lastRowCell.setCellStyle(cs);
			lastRowCell = row.createCell(3);
			lastRowCell.setCellValue((String) params.get("zb"));

			// 联系电话
			lastRowCell = row.createCell(4);
			lastRowCell.setCellValue("联系电话：");
			lastRowCell.setCellStyle(cs);
			lastRowCell = row.createCell(5);
			lastRowCell.setCellValue((String) params.get("lxdh"));
		}
		String bz = (String) params.get("bz");
		if (bz != "" && bz != null) {
			row = sheet.createRow(lastRow + 2);
			String[] split = bz.split("，");
			// 备注
			lastRowCell = row.createCell(0);
			lastRowCell.setCellValue("备注：");
			lastRowCell.setCellStyle(cs);
			// 合并单元格
			// sheet.addMergedRegion(new CellRangeAddress(lastRow+3, lastRow+3,
			// 1,filed.size()-1));
			for (int i = 0; i < split.length; i++) {
				// 以逗号换行
				row = sheet.createRow(lastRow + 3 + i);
				lastRowCell = row.createCell(1);
				lastRowCell.setCellValue(split[i]);
			}
		}
		return wb;
	}

	/**
	 * 描述：设置简单的Cell样式
	 * 
	 * @return
	 */
	public CellStyle setSimpleCellStyle(Workbook wb) {
		CellStyle cs = wb.createCellStyle();

		cs.setBorderBottom(CellStyle.BORDER_THIN); // 下边框
		cs.setBorderLeft(CellStyle.BORDER_THIN);// 左边框
		cs.setBorderTop(CellStyle.BORDER_THIN);// 上边框
		cs.setBorderRight(CellStyle.BORDER_THIN);// 右边框
		cs.setAlignment(CellStyle.ALIGN_CENTER); // 居中

		return cs;
	}

	/**
	 * 描述：设置末尾几行Cell样式
	 * 
	 * @return
	 */
	public CellStyle lastCellStyle(Workbook wb) {
		CellStyle cs = wb.createCellStyle();
		cs.setAlignment(CellStyle.ALIGN_RIGHT); // 居右

		return cs;
	}
	/**
	 * 描述：设置末尾几行Cell样式
	 * 
	 * @return
	 */
	public CellStyle lastCellStyle2(Workbook wb) {
		CellStyle cs = wb.createCellStyle();
		cs.setAlignment(CellStyle.ALIGN_CENTER); // 居中
		return cs;
	}

}