package com.mininglamp.currencySys.uploadAndDownload.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mininglamp.currencySys.common.base.bean.ResponseBean;
import com.mininglamp.currencySys.common.base.controller.BaseController;
import com.mininglamp.currencySys.uploadAndDownload.service.UploadAndAownloadService;
import com.mininglamp.currencySys.uploadAndDownload.service.impl.UploadAndAownloadServiceImp;
import com.mininglamp.currencySys.util.Encodes;
import com.mininglamp.currencySys.util.ExcelUtil;
import com.mininglamp.currencySys.util.CheckProgram.CheckReadExcel;

import net.sf.json.JSONArray;

@Controller
@RequestMapping("/test")
public class UploadAndAownloadController extends BaseController{

	@Autowired
	private UploadAndAownloadService testService;
	
	String jrjgbm = null;
	
	//获取配置信息
	public void getUserAndPass() {
		try {
			Properties prop = new Properties();
			//Class.getResourceAsStream("name") 会指定要加载的资源路径与当前类所在包的路径一致 如果这个name是以 '/' 开头的，那么就会从classpath的根路径下开始查找。
			//getClassLoader().getResourceAsStream("name")  无论要查找的资源前面是否带'/' 都会从classpath的根路径下查找。
			prop.load(UploadAndAownloadServiceImp.class.getClassLoader().getResourceAsStream("ibatis/jdbc.properties"));
			jrjgbm = prop.getProperty("provincial.jrjgbm");
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
	@RequestMapping(value = "/dowm")
	@ResponseBody
	public ResponseBean downExcel(HttpServletRequest request, HttpServletResponse response,String sqlId) throws Exception {
		Map<String, Object> params = getParamters(request);
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd");
		ResponseBean result = new ResponseBean();
		List<Map> dataList = testService.getDataList("test", params);
		String fileName = "acco"+fmt.format(fmt1.parse((String)params.get("beginSjrq"))) + ".xls"; 
		String columnNames[] = {"数据日期", "账户号", "账户名称", "客户内部编码", "开户行金融机构编码","开户行内部机构号", "账户标志", "账户类型", "开户日期", "销户日期","账户状态", "是否开通网上银行", "是否开通手机银行"};
		String keys[] = {"SJRQ", "ZHH", "ZHMC", "KHNBBM", "KHHJRJGBM","KHHNBJGH", "ZHBZ", "ZHLX", "KHRQ", "XHRQ","ZHZT", "SFKTWSYH", "SFKTSJYH"};
		this.export(request, response, fileName, dataList, keys, columnNames,params);
		return result;
	}
	
	/**
	 * 下载err日志
	 * @param request
	 * @param response
	 * @param sqlId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/errLogs")
	@ResponseBody
	public ResponseBean errLogs(HttpServletRequest request, HttpServletResponse response,String sqlId) throws Exception {
		Map<String, Object> params = getParamters(request);
		HttpSession session = request.getSession();
		ResponseBean result = new ResponseBean();
		getUserAndPass();
		String tname = (String) params.get("tname");
		String fileName = "错误日志"+tname + jrjgbm+".txt";
		StringBuffer errorLogs = new StringBuffer().append(session.getAttribute("errorLogs"));
		response.reset();
		// response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment;filename=" + Encodes.urlEncode(fileName));
		BufferedOutputStream buff = null;
		StringBuffer write = new StringBuffer();
		String enter = "\r\n";
		ServletOutputStream outSTr = null;
		try {
			outSTr = response.getOutputStream(); // 建立
			buff = new BufferedOutputStream(outSTr);
			// 把内容写入文件
			write.append(errorLogs);
			write.append(enter);
			buff.write(write.toString().getBytes("UTF-8"));
			buff.flush();
			buff.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("文件下载出错。"+e.getMessage(),e);
            //文件不存在时会在FileinputStream（filePath）读取时异常，进入这里。导致前台页面无响应。这里就进行重定向到原页面，并带上错误标记
            try{
                //request.getCOntextPath()获取项目名，这个必须加上，不然找不到对应path的方法。页面404，这个path对应本类的另一个方法（在下面有）
            	response.sendRedirect(request.getContextPath()+"/error/404.jsp");
            }catch(IOException e1){
                logger.error(e1);
            }
		} finally {
			try {
				buff.close();
				outSTr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param sqlId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getCodeData")
	@ResponseBody
	public ResponseBean getArea(HttpServletRequest request, HttpServletResponse response,String sqlId) throws Exception {
		Map<String, Object> params = getParamters(request);
		ResponseBean result = new ResponseBean();
		long start = System.currentTimeMillis();
		result.setData(testService.getDataList("test", params));
		long end = System.currentTimeMillis();
		System.out.println(end-start+"===");
		return result;
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param sqlId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/updateData")
	@ResponseBody
	public ResponseBean updateData(HttpServletRequest request, HttpServletResponse response,String sqlId) throws Exception {
		Map<String, Object> params = getParamters(request);
		params.put("last_row", JSONArray.fromObject(params.get("last_row")));
		ResponseBean result = new ResponseBean();
		result.setData(testService.updateData("updateData", params));
		return result;
	}
	
	/**
	 * 上传文件
	 * @param request
	 * @param response
	 * @param reportDataFile
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/reportDataInsert")
	@ResponseBody
	public ResponseBean reportDataInsert(HttpServletRequest request,
			HttpServletResponse response,@RequestParam MultipartFile reportDataFile) throws IOException, ParseException{
		long start = System.currentTimeMillis();
		Map<String, Object> params = getParamters(request);
		List<Map> dataList = testService.getDataList("getTable_nbjgh", params);
		params.put("Table_nbjgh", dataList);
		//saveData 对应 mybatis xml 添加 ，  deleteData 对应 删除
		ResponseBean fileParsing = CheckReadExcel.FileParsing(request,reportDataFile,params,testService);
		long end = System.currentTimeMillis();
		System.out.println("导入共用"+(end-start)+"毫秒");
		return fileParsing;
	}
	
	/**
	 * 导出文件
	 * @param request
	 * @param response
	 * @param fileName
	 * @param datalist
	 * @param keys
	 * @param columnNames
	 * @param params 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static void export(HttpServletRequest request, HttpServletResponse response, 
			String fileName, List datalist, String[] keys, String[] columnNames, Map<String, Object> params) throws Exception{
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHssmm");
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Workbook createWorkBook = ExcelUtil.createWorkBook(datalist, keys, columnNames);
		//创建sheet
		createWorkSheet(createWorkBook,(List<Map>)params.get("arrayList"), (String[])params.get("paramKeys"), (String[])params.get("paramNames"));
		createWorkBook.write(os);
		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);
		
		//保存到服务器 
		String realPath = request.getSession().getServletContext().getRealPath("");
		String localFileName = fileName;
		localFileName = localFileName.substring(0, localFileName.length() - 4) + "-" +fmt.format(new Date()) + localFileName.substring(localFileName.length() - 4, localFileName.length());
		String name = new String(localFileName.getBytes(), "UTF-8");
		//导出时  在服务器 生成一个excel文件，再导出
		File file = new File(realPath+"/upLoadFiles/" +name);
		File parentFile = new File(realPath+"/upLoadFiles");
		//判断文件 文件夹
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		if (!file.exists()) {
			file.createNewFile();
		}
		Workbook createWorkBook1 = ExcelUtil.createWorkBook(datalist, keys, columnNames);
		//创建sheet
		createWorkSheet(createWorkBook1,(List<Map>)params.get("arrayList"), (String[])params.get("paramKeys"), (String[])params.get("paramNames"));
		createWorkBook1.write(new FileOutputStream(file));
		
		// 设置response参数，可以打开下载页面
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes(), "ISO-8859-1"));
		ServletOutputStream out = response.getOutputStream();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(out);
			byte[] buff = new byte[2048];
			int bytesRead;
			// Simple read/write loop.
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (final IOException e) {
			throw e;
		} finally {
			if (bis != null)
			bis.close();
			if (bos != null)
			bos.close();
		}
	}
	
	/**
	* 创建Sheet文档，
	 * @param columnNames 
	 * @param keys 
	 * @param datalist 
	*
	* @param list        数据
	* @param keys        list中map的key数组集合
	* @param columnNames excel的列名
	*/
	public static Sheet createWorkSheet(Workbook wb, List<Map> list, String[] keys, String[] columnNames) {
		// 创建第一个sheet（页），并命名
		Sheet sheet = wb.createSheet("参数");
		// 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
		for (int i = 0; i < keys.length; i++) {
			sheet.setColumnWidth((short) i, (short) (35.7 * 160));
		}
		sheet.setDefaultRowHeight((short) 700);
		// 创建第一行
		Row row = sheet.createRow(0);//sheet.createRow((short) 0);
		
		// 创建两种单元格格式
		CellStyle cs = wb.createCellStyle();
		CellStyle cs2 = wb.createCellStyle();
		
		// 创建两种字体
		Font f = wb.createFont();
		Font f2 = wb.createFont();
		
		// 创建第一种字体样式（用于列名）
		f.setFontHeightInPoints((short) 12);
		f.setColor(IndexedColors.BLACK.getIndex());
		f.setBoldweight(Font.BOLDWEIGHT_BOLD);
		
		// 创建第二种字体样式（用于值）
		f2.setFontHeightInPoints((short) 12);
		f2.setColor(IndexedColors.BLACK.getIndex());
		f.setBoldweight(Font.BOLDWEIGHT_BOLD);
		
		// 设置第一种单元格的样式（用于列名）
		cs.setFont(f);
		cs.setBorderLeft(CellStyle.BORDER_THIN);
		cs.setBorderRight(CellStyle.BORDER_THIN);
		cs.setBorderTop(CellStyle.BORDER_THIN);
		cs.setBorderBottom(CellStyle.BORDER_THIN);
		cs.setAlignment(CellStyle.ALIGN_CENTER);
		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// 设置第二种单元格的样式（用于值）
		cs2.setFont(f2);
		cs2.setBorderLeft(CellStyle.BORDER_THIN);
		cs2.setBorderRight(CellStyle.BORDER_THIN);
		cs2.setBorderTop(CellStyle.BORDER_THIN);
		cs2.setBorderBottom(CellStyle.BORDER_THIN);
		cs2.setAlignment(CellStyle.ALIGN_CENTER);
		cs2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
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
				cell.setCellValue(list.get(i).get(keys[j]) == null ? "" : list.get(i).get(keys[j]).toString());
				cell.setCellStyle(cs2);
			}
		}
		return sheet;
	}
	
	/**
	 * 下载文件
	 * @param fileName 文件名（若为空则取filePath的文件名）
	 * @param filePath 文件路径（包含文件名）
	 * @param request
	 * @param response
	 */
	public static void downloadFile(String fileName ,String filePath, HttpServletRequest request, HttpServletResponse response){
		response.setContentType("text/html;charset=utf-8");
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
				
		java.io.BufferedInputStream bis = null;
		java.io.BufferedOutputStream bos = null;
	
		String downLoadPath = filePath;  //注意不同系统的分隔符
		
		try {
			if (StringUtils.isEmpty(fileName)) {
				fileName = (new File(downLoadPath)).getName();
			}
			long fileLength = new File(downLoadPath).length();
			response.setContentType("application/x-msdownload;");
			response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
			response.setHeader("Content-Length", String.valueOf(fileLength));
			bis = new BufferedInputStream(new FileInputStream(downLoadPath));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null)
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (bos != null)
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	/**
	 * 上传文件到指定目录
	 * @param file 要上传的文件
	 * @param fileSavePath 文件保存路径（可为空，默认为tomcat项目发布路径/tempFile）
	 * @param fileName 指定的文件名（若为null，则使用要上传的文件的默认文件名）
	 * @param request 用于获取session的请求对象
	 */
	public static String uploadFile(MultipartFile file, String fileSavePath, String fileName, HttpServletRequest request){
		// 判断文件是否为空  
        if (!file.isEmpty()) {
        	fileSavePath = StringUtils.isNotEmpty(fileSavePath) ? fileSavePath : request.getSession().getServletContext().getRealPath("")+"/tempFile/";
            try {
                File filepath = new File(fileSavePath);
                if (!filepath.exists()) 
                    filepath.mkdirs();
                // 文件保存路径  
                fileSavePath = fileSavePath + (StringUtils.isNotEmpty(fileName) ? fileName : file.getOriginalFilename());  
                // 转存文件  
                file.transferTo(new File(fileSavePath));
                return fileSavePath;
            } catch (Exception e) {  
                e.printStackTrace();
                return null;
            }
        } else {
        	return null;
        }
	}
}

