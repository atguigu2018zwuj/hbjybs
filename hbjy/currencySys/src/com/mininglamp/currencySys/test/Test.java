package com.mininglamp.currencySys.test;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import org.slf4j.Logger;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class Test {
	@SuppressWarnings("resource")
//	public static void main(String[] args) throws IOException {
//		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMDDhhmmss");
//		String now = dateFormat.format(new Date());
//		// 导出文件路径
//		String basePath = "C:/";
//		// 文件名
//		String exportFileName = "数据_" + now + ".xlsx";
//		String[] cellTitle = { "序号", };
//		// 需要导出的数据
//		List<String[]> dataList = new ArrayList<String[]>();
//		dataList.add(new String[] { "东邪" });
//		dataList.add(new String[] { "西毒" });
//		dataList.add(new String[] { "南帝" });
//		dataList.add(new String[] { "北丐" });
//		dataList.add(new String[] { "中神通" });
//		dataList.add(new String[] { "中神通" });
//		for (int i = 0; i < 115536; i++) {
//			dataList.add(new String[] { i + "" });
//		}
//		// 声明一个工作薄
//		XSSFWorkbook workBook = null;
//		workBook = new XSSFWorkbook();
//		// 生成一个表格
//		XSSFSheet sheet = workBook.createSheet();
//		workBook.setSheetName(0, "学生信息");
//		// 创建表格标题行 第一行
//		XSSFRow titleRow = sheet.createRow(0);
//		for (int i = 0; i < cellTitle.length; i++) {
//			titleRow.createCell(i).setCellValue(cellTitle[i]);
//		}
//		// 插入需导出的数据
//		for (int i = 0; i < dataList.size(); i++) {
//			XSSFRow row = sheet.createRow(i + 1);
//			row.createCell(0).setCellValue(i + 1);
//			row.createCell(1).setCellValue(dataList.get(i)[0]);
//		}
//		File file = new File(basePath + exportFileName);
//		// 文件输出流
//		FileOutputStream outStream = new FileOutputStream(file);
//		workBook.write(outStream);
//		outStream.flush();
//		outStream.close();
//		System.out.println("导出2007文件成功！文件导出路径：--" + basePath + exportFileName);
//	}
//	
	/**  
     * @param args  
     *//*   
   public static void main(String[] args) {   
      System. out .println( " 内存信息 :" + toMemoryInfo ());   
   }   
   
   *//**  
     * 获取当前 jvm 的内存信息  
     *  
     * @return  
     *//*   
   public static String toMemoryInfo() {   
   
      Runtime currRuntime = Runtime.getRuntime ();   
      int nFreeMemory = ( int ) (currRuntime.freeMemory() / 1024 / 1024);   
      int nTotalMemory = ( int ) (currRuntime.totalMemory() / 1024 / 1024);   
      return nFreeMemory + "M/" + nTotalMemory + "M(free/total)" ;   
   } */
	/** 
	 * 
	 * @param list 所有元素的平级集合，map包含id和pid 
	 * @param pid 顶级节点的pid，可以为null 
	 * @param idName id位的名称，一般为id或者code 
	 * @return 树 
	 */  
	public static List<Map<String, Object>> getTree(List<Map<String, Object>> list, String pid, String idName) {  
	    List<Map<String, Object>> res = new ArrayList<Map<String,Object>>();  
	    if (CollectionUtils.isNotEmpty(list))  
	        for (Map<String, Object> map : list) {  
	            if(pid == null && map.get("p"+idName) == null || map.get("p"+idName) != null && map.get("p"+idName).equals(pid)){  
	                String id = (String) map.get(idName);  
	                map.put("children", getTree(list, id, idName));  
	                res.add(map);  
	            }  
	        }  
	    return res;  
	}  
	
	public static String reverse(String originStr) {  
        if(originStr == null || originStr.length() <= 1)   
            return originStr;  
        return reverse(originStr.substring(1)) + originStr.charAt(0);  
    }
	
	//通过socket传输到短息平台
		public static boolean socketFile(String IP,int point,String localfile) throws Exception{
			System.out.println("进入[socket]方法中...");
	        Socket socket = null;
	        OutputStream os = null;
	        PrintWriter pw = null;
	        InputStream is = null;
	        BufferedReader br= null;
	        try {
	          try {
	            socket = new Socket();
	            socket.connect(new InetSocketAddress(IP, point),10 * 1000);
	            os = socket.getOutputStream();  
	            pw = new PrintWriter(new OutputStreamWriter(os,"GBK"),true);
				//pw = new PrintWriter(os);
				//is = socket.getInputStream();
				//br = new BufferedReader(new InputStreamReader(is));
				pw.write(localfile);
				pw.flush();
				System.out.println("已发送xml["+localfile+"]");
	            } finally{
	            	if (br != null)
	            		br.close();
	            	if (is != null)
	            		is.close();
	                if (pw != null)
	                	pw.close();
	                if (os != null)
	            		os.close();
	                if (socket != null)
	                    socket.close();
	        }
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("发送失败"+e.getMessage());
	        }
	        System.out.println("结束");
			return true;
		}
		
	public static void main(String[] args) throws Exception {
		ScheduledExecutorService scheduledExecutor = ScheduledExecutor();
		 try {
			 System.out.println("sssssssssss");
			 Test tm = new Test();  
			 tm.jdbcex(true);
			 scheduledExecutor.shutdown();
			 scheduledExecutor.awaitTermination(5, TimeUnit.SECONDS);
		} catch (Exception e) {
			 // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。  
			e.printStackTrace();
	        scheduledExecutor.shutdownNow(); 
		}
	     
	     
		/*socketFile("192.168.136.166",22,"sdfhkshdfjkshf");*/
//		Test tm = new Test();  
//	    tm.jdbcex(true);  
		/*List<String> pcodes = new ArrayList<>();  
		pcodes.add(null);  
		List<Map<String, Object>> list = new ArrayList<>();  
		for (int i = 0, len = 10; i < len; i++){  
		    Map<String, Object> map = new HashedMap();  
		    map.put("code", "code" + i);  
		    map.put("name", "name" + i);  
		    map.put("pcode", pcodes.get(0));  
		    pcodes.add("code" + i);  
		    Collections.shuffle(pcodes);  
		    list.add(map);
		}  
		System.out.println(list); 
		System.out.println(getTree(list, null, "code")); */
		/*List<Map<String, Object>> findListBySql = BaseDao.findListBySql("select * from SPECIAL_GZHM");
		System.out.println(findListBySql.toString());*/
	}

	public static ScheduledExecutorService ScheduledExecutor() {
		Runnable runnable = new Runnable() {  
	            public void run() {  
	                // task to run goes here  
	                System.out.println("Hello !!");  
	            }  
	        };  
	        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();  
	        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间  
	        service.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);
	        return service;
	}  
	
	
	public void jdbcex(boolean isClose) throws InstantiationException, IllegalAccessException,   
	            ClassNotFoundException, SQLException, IOException, InterruptedException {  
	          
	    String xlsFile = "f:/poiSXXFSBigData1.xlsx";     //输出文件  
	    //内存中只创建100个对象，写临时文件，当超过100条，就将内存中不用的对象释放。  
	    Workbook wb = new SXSSFWorkbook(100);           //关键语句  
	    Sheet sheet = null;     //工作表对象  
	    Row nRow = null;        //行对象  
	    Cell nCell = null;      //列对象  
	  
	    //使用jdbc链接数据库  
	    Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();    
	    String url = "jdbc:oracle:thin:@192.168.136.166:1521:ORCL";  
	    String user = "hbjy";  
	    String password = "hbjy123";  
	    //获取数据库连接  
	    Connection conn = DriverManager.getConnection(url, user,password);     
	    Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);     
	    String sql = "select * from SPECIAL_PRSN";   //100万测试数据  
	    ResultSet rs = stmt.executeQuery(sql);    
	      
	    ResultSetMetaData rsmd = rs.getMetaData();  
	    long  startTime = System.currentTimeMillis();   //开始时间  
	    System.out.println("strat execute time: " + startTime);  
	          
	    int rowNo = 0;      //总行号  
	    int pageRowNo = 0;  //页行号  
	    
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
	          
	    while(rs.next()) {  
	        //打印300000条后切换到下个工作表，可根据需要自行拓展，2百万，3百万...数据一样操作，只要不超过1048576就可以  
	        if(rowNo%300000==0){  
	            System.out.println("Current Sheet:" + rowNo/300000);  
	            sheet = wb.createSheet("我的第"+(rowNo/300000)+"个工作簿");//建立新的sheet对象  
	            sheet = wb.getSheetAt(rowNo/300000);        //动态指定当前的工作表  
	            pageRowNo = 0;      //每当新建了工作表就将当前工作表的行号重置为0 
	            for (int i = 0; i < rsmd.getColumnCount(); i++) {
	    			sheet.setColumnWidth((short) i, (short) (35.7 * 150));
	    		}
	        }  
	        rowNo++;  
	        nRow = sheet.createRow(pageRowNo++);    //新建行对象  
	  
	        // 打印每行，每行有6列数据   rsmd.getColumnCount()==6 --- 列属性的个数  
	        for(int j=0;j<rsmd.getColumnCount();j++){  
	            nCell = nRow.createCell(j);  
	            nCell.setCellValue(rs.getString(j+1));  
	        }  
	              
	        if(rowNo%10000==0){  
	            System.out.println("row no: " + rowNo);  
	        }  
//	      Thread.sleep(1);    //休息一下，防止对CPU占用，其实影响不大  
	    }  
	          
	    long finishedTime = System.currentTimeMillis(); //处理完成时间  
	    System.out.println("finished execute  time: " + (finishedTime - startTime)/1000 + "m");  
	          
	    FileOutputStream fOut = new FileOutputStream(xlsFile);  
	    wb.write(fOut);  
	    fOut.flush();       //刷新缓冲区  
	    fOut.close();  
	          
	    long stopTime = System.currentTimeMillis();     //写文件时间  
	    System.out.println("write xlsx file time: " + (stopTime - startTime)/1000 + "m");  
	          
	    if(isClose){  
	        this.close(rs, stmt, conn);  
	    }  
	}  
	      
	//执行关闭流的操作  
	private void close(ResultSet rs, Statement stmt, Connection conn ) throws SQLException{  
	    rs.close();     
	    stmt.close();     
	    conn.close();   
	}  
}