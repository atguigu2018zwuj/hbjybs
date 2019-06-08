package com.mininglamp.currencySys.util.report;  
  
import java.io.File;  
import java.io.OutputStream;  
import java.net.URLEncoder;  
import java.util.ArrayList;  
import java.util.HashMap;
import java.util.List;  
  
import java.util.Map;

import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
  






import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;  
import org.springframework.stereotype.Controller;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RequestMethod;  
import org.springframework.web.bind.annotation.ResponseBody;
  
/**
 * @author 
 *根据模版 +后台数据 导出excel
 */
@Controller  
@RequestMapping("/excelTest")    
public class ExcelTestControl { 
	
	 /**测试页面
     * @param request  
     * @param response  
     * @throws Exception  
     */ 
	  @RequestMapping(value="Index",method={RequestMethod.GET,RequestMethod.POST})  
	  public String excelIndex(HttpServletRequest request,HttpServletResponse response) {
		return "excelTest/excelTest";
	  }
	
      
	 /**导出测试
     * @param request  
     * @param response  
     * @throws Exception  
     */  
    @RequestMapping(value="exportIndex",method={RequestMethod.GET,RequestMethod.POST})  
    public  String  ajaxUploadExcel(HttpServletRequest request,HttpServletResponse response) throws Exception {  
        OutputStream os = null;    
        Workbook wb = null;    //工作薄  
        try {  
            // 模拟数据库取值  
            List<Map> data = new ArrayList<Map>();  
            Map<String,Object> map=new HashMap<String, Object>();
            map.put("danwei", "银行");
            map.put("t520","1000");
            map.put("t520z","10");
            map.put("t1","2000");
            map.put("t1z","20");
            map.put("h520","3000");
            map.put("h520z","30");
            map.put("h1","4000");
            map.put("h1z","40");
            data.add(map);
            // 定义表中字段 顺序要和模版中表头顺序一致
            List<String> filed=new ArrayList<String>();
            filed.add("danwei");
            filed.add("t520");
            filed.add("t520z");
            filed.add("t1");
            filed.add("t1z");
            filed.add("h520");
            filed.add("h520z");
            filed.add("h1");
            filed.add("h1z");
            //导出Excel文件数据  
            ExportExcelUtil util = new ExportExcelUtil();
            //读取模版文件
            File file =util.getExcelDemoFile("/reportTemplate/bank.xls");  
            //读取模版中的sheet
            String sheetName="bank1";
            //定义 填报单位、报告期
            Map<String,Object> params=new HashMap<String, Object>();
            //填报单位
            params.put("fillCompany", "人民银行郑州中心支行");
            //报告期
            params.put("date", "2017年全年");
            //写入新excel
            wb = util.writeNewExcel(file, sheetName,data,filed,params);   
            String fileName="excel测试.xls"; 
            response.setContentType("application/octet-stream;charset=utf-8");  
            response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes(),"iso-8859-1"));
            os = response.getOutputStream();  
            wb.write(os);    
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        finally{  
            os.flush();  
            os.close();  
        }   
        return null;  
    }  
    
    
    /**  
     * @param request  
     * @param response  
     * @throws Exception  
     */  
    @RequestMapping(value="htmlIndex",method={RequestMethod.GET,RequestMethod.POST})  
    public @ResponseBody String readExcelToHtml(HttpServletRequest request,HttpServletResponse response) throws Exception {  
        OutputStream os = null;    
        Workbook wb = null;    //工作薄  
        String tableHtml=null;
        try {  
            // 模拟数据库取值  
            List<Map> data = new ArrayList<Map>();  
            Map<String,Object> map=new HashMap<String, Object>();
            map.put("danwei", "银行");
            map.put("t520","1000");
            map.put("t520z","10");
            map.put("t1","2000");
            map.put("t1z","20");
            map.put("h520","3000");
            map.put("h520z","30");
            map.put("h1","4000");
            map.put("h1z","40");
            data.add(map);
            // 定义表中字段 顺序要和模版中表头顺序一致
            List<String> filed=new ArrayList<String>();
            filed.add("danwei");
            filed.add("t520");
            filed.add("t520z");
            filed.add("t1");
            filed.add("t1z");
            filed.add("h520");
            filed.add("h520z");
            filed.add("h1");
            filed.add("h1z");
            //导出Excel文件数据  
            ExportExcelUtil util = new ExportExcelUtil();
            //读取模版文件
            File file =util.getExcelDemoFile("/reportTemplate/bank.xls");  
            //读取模版中的sheet
            String sheetName="bank1";
            //定义 填报单位、报告期
            Map<String,Object> params=new HashMap<String, Object>();
            //填报单位
            params.put("fillCompany", "人民银行郑州中心支行");
            //报告期
            params.put("date", "2017年全年");
            //单位
            params.put("dw", "万元");
            //复核
            params.put("fh", "");
            //制表
            params.put("zb", "");
            //联系电话
            params.put("lxdh", "");
            //写入新excel
            wb = util.writeNewExcel(file, sheetName,data,filed,params);   
            tableHtml = POIReadExcelToHtml.getExcelInfo(wb,true);
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return tableHtml;  
    }  
  
  
}  