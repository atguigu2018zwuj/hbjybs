/*package com.mininglamp.currencySys.generateMessage.controller;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.mininglamp.currencySys.common.base.bean.ResponseBean;
import com.mininglamp.currencySys.common.base.controller.BaseController;
import com.mininglamp.currencySys.generateMessage.service.GenerateMessageService;
import com.mininglamp.currencySys.uploadAndDownload.service.impl.UploadAndAownloadServiceImp;
import net.sf.json.JSONArray;
*//**
 * 调用本地脚本
 * @author Administrator
 *
 *//*
@Controller
@RequestMapping(value = "/generateMessageController")
public class GenerateMessageController extends BaseController{
	SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd");
	private String provincial_jrjgbm = null;//金融机构编码
	private String shell_url = null;//脚本路径	
	@Autowired
	private GenerateMessageService generateMessageService;
	
	// 资管产品SPV投资流量信息
	@RequestMapping("gmvIndex")
	public String generateMessageView() {
		return "generateMessage/generateMessageView";
	}
	
	*//**
	 * 获取用户名，密码，ip
	 *//*
	public void getUserAndPass() {
		try {
			Properties prop = new Properties();
			//Class.getResourceAsStream("name") 会指定要加载的资源路径与当前类所在包的路径一致 如果这个name是以 '/' 开头的，那么就会从classpath的根路径下开始查找。
			//getClassLoader().getResourceAsStream("name")  无论要查找的资源前面是否带'/' 都会从classpath的根路径下查找。
			prop.load(UploadAndAownloadServiceImp.class.getClassLoader().getResourceAsStream("ibatis/jdbc.properties"));
			provincial_jrjgbm = prop.getProperty("provincial.jrjgbm");
			shell_url = prop.getProperty("shell.url");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	*//**
	 * 调用本地shell脚本
	 * @param command
	 * @throws IOException
	 *//*
	public static void getRunTime(String[] cmd) throws IOException {
		Process p = Runtime.getRuntime().exec(cmd);  
		try {
			StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");              
			// kick off stderr  
			errorGobbler.start();  
			        
			StreamGobbler outGobbler = new StreamGobbler(p.getInputStream(), "STDOUT");  
			// kick off stdout  
		    outGobbler.start();   
			p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
	} 
	
	*//**
	 * 覆盖生成文本文件
	 * @param filePath
	 * @param content 
	 *//*
	public static void contentToTxt(String filePath, String content) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath), false));
			writer.write(content);
			writer.close();
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
	
	*//** 
	 * 
	 * @param list 所有元素的平级集合，map包含id和pid 
	 * @param pid 顶级节点的pid，可以为null 
	 * @param idName id位的名称，一般为id或者code 
	 * @return 树 
	 *//*  
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
	
	//下载失败文件
	@RequestMapping(value = "/downFlie")
	@ResponseBody
	public void downFlie(HttpServletRequest request, HttpServletResponse resp){
		try {
			Map<String, Object> params = getParamters(request);
			String beginSjrq = fmt.format(fmt1.parse(String.valueOf(params.get("beginSjrq"))));
			getUserAndPass();// 获取ip,用户名,密码
			//表名
			String fileName = String.valueOf(params.get("fileName"));
			//获取四位标识
			String swbs = fileName.substring(fileName.length() - 4,fileName.length());
			//错误文件名
			String errorFileName = "FDBD"+swbs+provincial_jrjgbm+beginSjrq+".dat";
			String realPath = shell_url+"/check/error_"+beginSjrq+"/"+errorFileName;
			// 文件的路径
			String fileUrl = new String(String.valueOf(realPath).getBytes("UTF-8"), "UTF-8");
			// 根据文件名获取 MIME 类型
			String contentDisposition = "attachment;filename="+errorFileName;
			// 输入流
			FileInputStream input = new FileInputStream(fileUrl);
			// 设置头
			resp.setHeader("Content-Type", "multipart/form-data");
			resp.setHeader("Content-Disposition", contentDisposition);
			// 获取绑定了客户端的流
			ServletOutputStream output = resp.getOutputStream();
			// 把输入流中的数据写入到输出流中
			IOUtils.copy(input, output);
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("文件下载出错。"+e.getMessage(),e);
            //文件不存在时会在FileinputStream（filePath）读取时异常，进入这里。导致前台页面无响应。这里就进行重定向到原页面，并带上错误标记
            try{
                //request.getCOntextPath()获取项目名，这个必须加上，不然找不到对应path的方法。页面404，这个path对应本类的另一个方法（在下面有）
            	resp.sendRedirect(request.getContextPath()+"/error/404.jsp");
            }catch(IOException e1){
                logger.error(e1);
            }
		}
	}
	
	*//**
	 * 查询
	 * @param request
	 * @param response
	 * @param sqlId
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping(value = "/getCodeData")
	@ResponseBody
	public List<Map<String, Object>> getUserData(HttpServletRequest request, HttpServletResponse response, String sqlId)
			throws Exception {
		long start = System.currentTimeMillis();
		Map<String, Object> params = getParamters(request);
		List<Map> dataList = generateMessageService.getDataList("getUSER_AUTHORITY", params);
		List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		for (Map map : dataList) {
			arrayList.add(map);
		}
		List<Map<String, Object>> tree = getTree(arrayList,"0","id");
		long end = System.currentTimeMillis();
		return tree;
	}
	
	*//***************************拆分---生成报文 开始**********************************//*
	*//**
	 * 调用shell脚本,命令
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping(value = "/generateMessage")
	@ResponseBody
	public List<Map<String, Object>> generateMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> params = getParamters(request);
		StringBuffer stringBuffer = new StringBuffer();
		List<Map> dataList = null;
		try {
			String beginSjrq = fmt.format(fmt1.parse(String.valueOf(params.get("beginSjrq"))));
			List<Map> treeData = (List<Map>) JSONArray.fromObject(params.get("treeData"));
			//遍历出表名 追加到stringBuffer
			for (int i = 0;i < treeData.size(); i++) {
				stringBuffer.append(new String(String.valueOf(treeData.get(i).get("key")+"\n").getBytes("UTF-8"),"GBK"));
			}
			//获取ip,用户名,密码
			getUserAndPass();
			//覆盖文本文件 table_name.txt
			String path = shell_url+"/script/table_name_dat.txt";
			contentToTxt(path,stringBuffer.toString());
			//用来判断是 生成报文、程序校验、报文上报？
			String id = (String) params.get("id");
			if("scbw".equals(id)){//生成报文
				//运行shell脚本
				String[] command = {"/bin/sh" , "-c" , " sh "+shell_url+"/script/exec_shell.sh 'export_ex.sh "+beginSjrq +" 1'"};
				getRunTime(command);
			}else if("cxjy".equals(id)){//程序校验
				//运行shell脚本
				String[] command = {"/bin/sh" , "-c" , " sh "+shell_url+"/script/exec_shell.sh 'export_ex.sh "+beginSjrq +" 2'"};
				getRunTime(command);
			}else if("bwsb".equals(id)){//报文上报
				//运行shell脚本
				String[] command = {"/bin/sh" , "-c" , " sh "+shell_url+"/script/exec_shell.sh 'export_ex.sh "+beginSjrq +" 4'"};
				getRunTime(command);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			dataList = generateMessageService.getDataList("getUSER_AUTHORITY", params);
		}
		List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		for (Map map : dataList) {
			arrayList.add(map);
		} 
		List<Map<String, Object>> tree = getTree(arrayList,"0","id");
		return tree;
	}
	*//***************************拆分---文生成报 结束**********************************//*
	
	
	*//***************************拆分---报文下载 开始**********************************//*
	//下载文件
	@RequestMapping(value = "/downloadMessage")
	@ResponseBody
	public void downloadMessage(HttpServletRequest request, HttpServletResponse resp){
		try {
			Map<String, Object> params = getParamters(request);
			String beginSjrq = fmt.format(fmt1.parse(String.valueOf(params.get("beginSjrq"))));
			getUserAndPass();// 获取ip,用户名,密码
			List fromObject = JSONArray.fromObject(params.get("params"));
			
			if(fromObject.size() == 1){
				//表名
				String fileName = String.valueOf(fromObject.get(0));
				//获取四位标识
				String swbs = fileName.substring(fileName.length() - 4,fileName.length());
				//错误文件名
				String FileName = swbs+provincial_jrjgbm+beginSjrq+".dat";
				String realPath = shell_url+"/data/report_dat/"+beginSjrq+"/"+FileName;
				// 文件的路径
				String fileUrl = new String(String.valueOf(realPath).getBytes("UTF-8"), "UTF-8");
				// 根据文件名获取 MIME 类型
				String contentDisposition = "attachment;filename="+FileName;
				// 输入流
				FileInputStream input = new FileInputStream(fileUrl);
				// 设置头
				resp.setHeader("Content-Type", "multipart/form-data");
				resp.setHeader("Content-Disposition", contentDisposition);
				// 获取绑定了客户端的流
				ServletOutputStream output = resp.getOutputStream();
				// 把输入流中的数据写入到输出流中
				IOUtils.copy(input, output);
				input.close();
			}else{//下载多文件 zip格式
				FileInputStream input = null;
				String contentType = null;
				String contentDisposition = null;
				//创建文件路径的集合，  
			    List<String> filePath = new ArrayList<String>();  
				for (int i = 0; i < fromObject.size(); i++) {
					String fileName = (String) fromObject.get(i);
					//获取四位标识
					String swbs = fileName.substring(fileName.length() - 4,fileName.length());
					//错误文件名
					String FileName = swbs+provincial_jrjgbm+beginSjrq+".dat";
					String realPath = shell_url+"/data/report_dat/"+beginSjrq+"/"+FileName;
					// 文件的路径
					String fileUrl = new String(String.valueOf(realPath).getBytes("UTF-8"), "UTF-8");
				    filePath.add(fileUrl);  
				}
				resp.setContentType("text/html; charset=UTF-8"); //设置编码字符  
				resp.setContentType("application/x-msdownload"); //设置内容类型为下载类型  
				resp.setHeader("Content-disposition", "attachment;filename="+new String((fromObject.get(0)+"等"+fromObject.size()+"个文件.zip").getBytes("UTF-8"),"iso-8859-1"));//设置下载的文件名称  
			    OutputStream out = resp.getOutputStream();   //创建页面返回方式为输出流，会自动弹出下载框
				String zipBasePath=request.getSession().getServletContext().getRealPath("");  
			    String zipFilePath = zipBasePath+"/upload/zip/temp.zip";  
			    File file = new File(zipBasePath+"/upload/zip/");  
			    //判断文件 文件夹
			    if (!file.exists()) {
			    	file.mkdirs();
			    }
			    //根据临时的zip压缩包路径，创建zip文件  
			    File zip = new File(zipFilePath);  
			    if (!zip.exists()){     
			        zip.createNewFile();     
			    }  
			    FileOutputStream fos = new FileOutputStream(zip);  
			    ZipOutputStream zos = new ZipOutputStream(fos);  
			    //循环读取文件路径集合，获取每一个文件的路径  
			    for(String fp : filePath){  
			        File f = new File(fp);  //根据文件路径创建文件  
			        zipFile(f, zos);  //将每一个文件写入zip文件包内，即进行打包  
			    }  
			    fos.close(); 
			    fos.flush();
			    //将打包后的文件写到客户端，输出的方法同上，使用缓冲流输出  
			    InputStream fis = new BufferedInputStream(new FileInputStream(zipFilePath));  
			    byte[] buff = new byte[4096];  
			    int size = 0;  
			    while((size=fis.read(buff)) != -1){  
			        out.write(buff, 0, size);  
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("文件下载出错。"+e.getMessage(),e);
            //文件不存在时会在FileinputStream（filePath）读取时异常，进入这里。导致前台页面无响应。这里就进行重定向到原页面，并带上错误标记
            try{
                //request.getCOntextPath()获取项目名，这个必须加上，不然找不到对应path的方法。页面404，这个path对应本类的另一个方法（在下面有）
            	resp.sendRedirect(request.getContextPath()+"/error/404.jsp");
            }catch(IOException e1){
                logger.error(e1);
            }
		}
	}
	
	*//***************************拆分---生成下载 结束 **********************************//*
	
	*//**
	 * 判断是否有强制性校验错误
	 * @param request
	 * @param resp
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping(value = "/readFile")
	@ResponseBody
	public ResponseBean readFile(HttpServletRequest request, HttpServletResponse resp) throws Exception{
	    ResponseBean responseBean = new ResponseBean();
		Map<String, Object> params = getParamters(request);
		getUserAndPass();// 获取ip,用户名,密码
		List<Map> treeData = (List<Map>) JSONArray.fromObject(params.get("treeData"));
		//遍历出表名 追加到stringBuffer
		for (int i = 0;i < treeData.size(); i++) {
			String fileName = String.valueOf(treeData.get(i).get("key"));
			//获取四位标识
			String swbs = fileName.substring(fileName.length() - 4,fileName.length());
			//错误文件名
			try {
				//错误文件名
				String beginSjrq = fmt.format(fmt1.parse(String.valueOf(params.get("beginSjrq"))));
				String FileName = "FDBD"+swbs+provincial_jrjgbm+beginSjrq+".dat";
				String realPath = shell_url+"/check/error_"+beginSjrq+"/"+FileName;
				InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(realPath), "GBK");
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader, 10*1024*1024);			//读取数据缓冲流
				String lineText = null;
				while((lineText = bufferedReader.readLine()) != null) {
		              if(lineText.contains("|强制性参照信息校验错误") || lineText.contains("|强制性格式校验错误") || lineText.contains("|强制性内容校验错误") || lineText.contains("|强制性逻辑校验错误")){
		            	  params.put("TABLENAME", swbs);
		            	  int updateGeneratereport = generateMessageService.updateGeneratereport("updateGENERATEREPORT",params);
		            	  responseBean.setData(updateGeneratereport);
		            	  break;
		              }
				}	
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return responseBean;
	}
	
	//封装压缩文件的方法  
	public void zipFile(File inputFile, ZipOutputStream zipoutputStream) {
		try {
			if (inputFile.exists()) { // 判断文件是否存在
				if (inputFile.isFile()) { // 判断是否属于文件，还是文件夹

					// 创建输入流读取文件
					FileInputStream fis = new FileInputStream(inputFile);
					BufferedInputStream bis = new BufferedInputStream(fis);

					// 将文件写入zip内，即将文件进行打包
					ZipEntry ze = new ZipEntry(inputFile.getName()); // 获取文件名
					zipoutputStream.putNextEntry(ze);

					// 写入文件的方法，同上
					byte[] b = new byte[1024];
					long l = 0;
					while (l < inputFile.length()) {
						int j = bis.read(b, 0, 1024);
						l += j;
						zipoutputStream.write(b, 0, j);
					}
					zipoutputStream.closeEntry();
					// 关闭输入输出流
					bis.close();
					fis.close();
				} else { // 如果是文件夹，则使用穷举的方法获取文件，写入zip
					try {
						File[] files = inputFile.listFiles();
						for (int i = 0; i < files.length; i++) {
							zipFile(files[i], zipoutputStream);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
}*/








package com.mininglamp.currencySys.generateMessage.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mininglamp.currencySys.common.base.bean.ResponseBean;
import com.mininglamp.currencySys.common.base.controller.BaseController;
import com.mininglamp.currencySys.common.util.CommonUtil;
import com.mininglamp.currencySys.common.util.StringUtils;
import com.mininglamp.currencySys.generateMessage.service.GenerateMessageService;
import com.mininglamp.currencySys.regulationFile.service.RegulationService;
import com.mininglamp.currencySys.uploadAndDownload.service.impl.UploadAndAownloadServiceImp;
import com.mininglamp.currencySys.userManage.service.UserManageService;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 调用远程脚本
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/generateMessageController")
public class GenerateMessageController extends BaseController{
	
	SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd");
	private Logger logger = LoggerFactory.getLogger(GenerateMessageController.class);
	private String ip = null;//服务器ip
	private String osUsername = null;//用户名
	private String password = null;//密码
	private String provincial_jrjgbm = null;//金融机构编码
	private String shell_url = null;//脚本路径
	
	@Autowired
	private GenerateMessageService generateMessageService;
//	@Autowired 
//	private ReportManageService reportManageService;
	@Autowired
	UserManageService userManageService;
	@Autowired
	RegulationService regulationService;
	
	// 资管产品SPV投资流量信息
	@RequestMapping("gmvIndex")
	public String generateMessageView() {
		return "generateMessage/generateMessageView";
	}
	
	/**
	 * 获取用户名，密码，ip
	 */
	public void getUserAndPass() {
		try {
			Properties prop = new Properties();
			//Class.getResourceAsStream("name") 会指定要加载的资源路径与当前类所在包的路径一致 如果这个name是以 '/' 开头的，那么就会从classpath的根路径下开始查找。
			//getClassLoader().getResourceAsStream("name")  无论要查找的资源前面是否带'/' 都会从classpath的根路径下查找。
			prop.load(UploadAndAownloadServiceImp.class.getClassLoader().getResourceAsStream("ibatis/jdbc.properties"));
			ip = prop.getProperty("liunx.ip");
			osUsername = prop.getProperty("liunx.username");
			password = prop.getProperty("liunx.password");
			provincial_jrjgbm = prop.getProperty("provincial.jrjgbm");
			shell_url = prop.getProperty("shell.url");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
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
	
	/**
	 * 获取报文校验错误日志
	 * @param params 参数（beginSjrq：数据日期（格式：yyyy-MM-dd）；fileName：报表库表名）
	 * @return 报文校验错误日志文件（若取得SCP远程文件失败，则返回null）
	 * @throws Exception 
	 */
	public File getErrorCheckFile(Map<String, Object> params,HttpServletRequest request) throws Exception{
		String beginSjrq = fmt.format(fmt1.parse(String.valueOf(params.get("beginSjrq"))));
		//表名
		String fileName = String.valueOf(params.get("fileName"));
		//获取四位标识
		String swbs = fileName.substring(fileName.length() - 4,fileName.length());
		
		// 开启SCP连接，并拷贝远程的文件scp到本地目录
		getUserAndPass();// 获取ip,用户名,密码
		//创建连接
		Connection conn = new Connection(ip);
		conn.connect();
		boolean isAuthenticated = conn.authenticateWithPassword(osUsername, password);
		if (isAuthenticated == false)
			throw new IOException("Authentication failed.文件scp到数据服务器时发生异常");
		SCPClient client = new SCPClient(conn);
		// 获取服务器存放的路径
		String realPath = request.getSession().getServletContext().getRealPath("/errorCheckFile/");
		File parentFile = new File(realPath);
		//判断文件 文件夹
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		
		//错误文件名
		String errorFileName = "FDBD"+swbs+provincial_jrjgbm+beginSjrq+".dat";
		// 文件的路径
		String fileUrl = new String(String.valueOf(realPath+"/"+errorFileName).getBytes("UTF-8"), "UTF-8");
		// 如果存在文件则先删除
		File oldErrorCheckFile = new File(fileUrl);
		if (oldErrorCheckFile.exists()) {
			oldErrorCheckFile.delete();
		}
		
		// 远程的文件scp到本地目录
		try {
			client.get(shell_url+"/check/error_"+beginSjrq+"/"+errorFileName, realPath);
		} catch (IOException e) {
			logger.warn("取得SCP远程文件【"+shell_url+"/check/error_"+beginSjrq+"/"+errorFileName+"】失败！");
		}
		conn.close();
		// 若没有取得则创建
		File errorCheckFile = new File(fileUrl);
		if (!errorCheckFile.exists()) {
			errorCheckFile.createNewFile();
		}
		return errorCheckFile;
	}
	
	/**
	 * 获取打回的详细信息
	 * @param params 参数（beginSjrq：报送的数据日期；fileName：报送的业务报表名；ywbm：报送的业务编码；）
	 * @param request
	 * @return 与机构对应的机构级别、错误信息、错误数据行code、机构层级信息（key：Jgjb_msg_code_cjxx；value：Jrjgnbjgh；）
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map<String,String> getRepulseReportInfos(Map<String, Object> params,HttpServletRequest request) throws Exception{
		// 与机构对应的机构级别、错误信息、错误数据行code（key：Jgjb-msg-code；value：Jrjgnbjgh；）
		Map<String,String> jgjbMsgCodeJrjgnbjgh = new TreeMap<String,String>();
		// 与机构对应的机构级别、机构层级信息、错误信息、错误数据行code（key：Jgjb_msg_code_cjxx；value：Jrjgnbjgh；）
		Map<String,String> result = new TreeMap<String,String>();
		
		// 报文的错误信息与错误报文code的对应关系（key：错误信息；value：错误数据行code；）
		Map<String,String> msgCode = new HashMap<String,String>();
		// 错误信息-错误报文code与金融机构的对应关系（key：错误信息-错误报文code；value：金融机构内部机构号；）
		Map<String,String> msgCodeJrjgnbjgh = new HashMap<String,String>();
		// 查询参数
		Map<String, Object> searchParams = new HashMap<String,Object>();
		
		// 读取报文校验的日志，并取得错误数据的code
		File errorCheckFile = getErrorCheckFile(params, request);
		RandomAccessFile errorCheckFileAccess = new RandomAccessFile(errorCheckFile,"r");
		String fileLineText = null;
		String errorMsgText = null;
		int lineNum = 0;// 读取的行号，从1开始
		do {
			fileLineText = errorCheckFileAccess.readLine();
			if (StringUtils.isNotEmpty(fileLineText)) {
				lineNum++;
				// 转码
				fileLineText = new String(fileLineText.getBytes("ISO-8859-1"),"GBK");
				// 错误信息行
				if (lineNum%2 == 1) {
					errorMsgText = fileLineText.split("\\|")[3];
					
				// 错误数据code
				} else {
					msgCode.put(errorMsgText, fileLineText);
				}
			}
		} while(StringUtils.isNotEmpty(fileLineText));
		errorCheckFileAccess.close();
		
		// 取得msgCodeJrjgnbjgh
		List<Map> lastOperationLogs = null; 
		for (String msg : msgCode.keySet()) {
			if (StringUtils.isNotEmpty(msgCode.get(msg))) {
				for (String code : msgCode.get(msg).split(",")) {
					if (!msgCodeJrjgnbjgh.containsKey(code)) {
						searchParams.put("wjbm", params.get("ywbm"));
						searchParams.put("recordCode", code);
						lastOperationLogs = generateMessageService.getDataList("getLastOperationLogs", searchParams);
						// 取得报文上报者机构编码
						if (lastOperationLogs != null && !lastOperationLogs.isEmpty()) {
							msgCodeJrjgnbjgh.put(msg+"_"+code, (String)lastOperationLogs.get(0).get("DWBM"));
						}
					}
				}
			}
		}
		
		// 构造result(Jgjb_msg_code)
		searchParams.clear();
		for (Entry<String,String> entry : msgCodeJrjgnbjgh.entrySet()) {
			// 当前msg-code中对应的最低级别的机构
			String minJbJgnbjgh = entry.getValue();
			int minJb = 0;
			// 获取最低级别的机构的级别
			searchParams.put("publishRange", minJbJgnbjgh);
			List<Map> jrjgxx = regulationService.getDataList("selectJgName", searchParams);
			if (jrjgxx != null && !jrjgxx.isEmpty()) {
				minJb = Integer.parseInt(String.valueOf(jrjgxx.get(0).get("JGJB")));
				// 先将最低级别的机构加入结果集中
				jgjbMsgCodeJrjgnbjgh.put(minJb+"_"+entry.getKey(), minJbJgnbjgh);
				// 将最低级别的机构的上级机构（直到市办）加入到结果集中（机构级别：2-市办，3-法人行社（支行），4-网点，5-分理处；）
				for (int jb = 2; jb < minJb; jb++) {
					// 取得对应级别的机构
					searchParams.put("jgjb", String.valueOf(jb));
					searchParams.put("nbjgh", minJbJgnbjgh);
					List<Map> sbjgInfos = generateMessageService.getDataList("getSjjrjgInfo", searchParams);
					if (sbjgInfos != null && !sbjgInfos.isEmpty()) {
						jgjbMsgCodeJrjgnbjgh.put(String.valueOf(jb)+"_"+entry.getKey(), (String)sbjgInfos.get(0).get("BR_NO"));
					}
				}
			}
		}
		
		// 构造result，将机构层级信息加上(Jgjb_msg_code_cjxx)
		// 取得机构层级信息
		Set<String> msgCodes = new HashSet<String>();		// 所有错误数据行code
		for (Entry<String,String> entry : jgjbMsgCodeJrjgnbjgh.entrySet()) {
			msgCodes.add(entry.getKey().substring(entry.getKey().indexOf("_")+1, entry.getKey().length()));
		}
		// 取得cjxx
		for (String msgCodeOne : msgCodes) {
			for (Entry<String,String> entry : jgjbMsgCodeJrjgnbjgh.entrySet()) {
				if (entry.getKey().endsWith(msgCodeOne)) {
					// 每一个msg_code的cjxx
					StringBuilder cjxx = new StringBuilder();
					// 取得当前msg_code下的最低机构级别
					int minJb = 0;
					for (Entry<String,String> msgCodeJrjgnbjghEntry : msgCodeJrjgnbjgh.entrySet()) {
						if (entry.getKey().endsWith(msgCodeJrjgnbjghEntry.getKey())) {
							searchParams.put("publishRange", msgCodeJrjgnbjghEntry.getValue());
							List<Map> jrjgxx = regulationService.getDataList("selectJgName", searchParams);
							if (jrjgxx != null && !jrjgxx.isEmpty()) {
								minJb = Integer.parseInt(String.valueOf(jrjgxx.get(0).get("JGJB")));
							}
						}
						if (minJb > 0) {
							break;
						}
					}
					// 拼接cjxx
					for (Entry<String,String> entry1 : jgjbMsgCodeJrjgnbjgh.entrySet()) {
						// cjxx的机构级别应 >= 当前机构级别 且 <= 当前msg_code下的最低机构级别
						if (entry1.getKey().endsWith(msgCodeOne) 
								&& Integer.parseInt(entry1.getKey().substring(0, entry1.getKey().indexOf("_"))) >= Integer.parseInt(entry.getKey().substring(0, entry.getKey().indexOf("_"))) 
								&& Integer.parseInt(entry1.getKey().substring(0, entry1.getKey().indexOf("_"))) <= minJb) {
							// 查询机构信息
							searchParams.put("publishRange", entry1.getValue());
							List<Map> jrjgxx = regulationService.getDataList("selectJgName", searchParams);
							if (jrjgxx != null && !jrjgxx.isEmpty()) {
								cjxx.append(jrjgxx.get(0).get("JRJGMC"));
								cjxx.append("->");
							}
						}
					}
					result.put(entry.getKey()+"_"+cjxx.toString().replaceAll("-\\>$", ""), entry.getValue());
				}
			}
		}
		
		return result;
	}
	
	/***************************不拆分生成报文 开始**********************************/
	//下载失败文件
	@RequestMapping(value = "/downFlie")
	@ResponseBody
	public void downFlie(HttpServletRequest request, HttpServletResponse resp){
		try {
			Map<String, Object> params = getParamters(request);
			File errorCheckFile = getErrorCheckFile(params, request);
			// 根据文件名获取 MIME 类型
			String contentDisposition = "attachment;filename="+errorCheckFile.getName();
			// 输入流
			FileInputStream input = new FileInputStream(errorCheckFile.getAbsolutePath());
			// 设置头
			resp.setHeader("Content-Type", "multipart/form-data");
			resp.setHeader("Content-Disposition", contentDisposition);
			// 获取绑定了客户端的流
			ServletOutputStream output = resp.getOutputStream();
			// 把输入流中的数据写入到输出流中
			IOUtils.copy(input, output);
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 调用shell脚本,命令
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/createGenerateMessage")
	@ResponseBody
	public List<Map<String, Object>> create(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> params = getParamters(request);
		StringBuffer stringBuffer = new StringBuffer();
		List<Map> dataList = null;
		try {
			String beginSjrq = fmt.format(fmt1.parse(String.valueOf(params.get("beginSjrq"))));
			List<Map> treeData = (List<Map>) JSONArray.fromObject(params.get("treeData"));
			for (int i = 0;i < treeData.size(); i++) {
				if(i == (treeData.size() - 1)){
					stringBuffer.append(new String(String.valueOf(treeData.get(i).get("key")).getBytes("UTF-8"),"GBK"));
				}else{
					stringBuffer.append(new String(String.valueOf(treeData.get(i).get("key")+"\n").getBytes("UTF-8"),"GBK"));
				}
			}
			//获取ip,用户名,密码
			getUserAndPass();
			RemoteShellExecutor executor = new RemoteShellExecutor(ip, osUsername, password);
			//覆盖文本文件 table_name.txt
			int exec = executor.exec("echo '"+stringBuffer.toString()+"' > "+shell_url+"/script/table_name.txt");
			//运行shell脚本
//			executor.exec(". "+shell_url+"/.profile;cd hbjy/script;sh export.sh "+beginSjrq);
			executor.exec("sh "+shell_url+"/script/exec_shell.sh 'export.sh "+beginSjrq+"'");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			dataList = generateMessageService.getDataList("getUSER_AUTHORITY", params);
		}
		List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		for (Map map : dataList) {
			arrayList.add(map);
		} 
		List<Map<String, Object>> tree = getTree(arrayList,"0","id");
		return tree;
	}
	
	/***************************不拆分生成报文 结束**********************************/
	
	/**
	 * 查询
	 * @param request
	 * @param response
	 * @param sqlId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getCodeData")
	@ResponseBody
	public List<Map<String, Object>> getUserData(HttpServletRequest request, HttpServletResponse response, String sqlId)
			throws Exception {
		long start = System.currentTimeMillis();
		Map<String, Object> params = getParamters(request);
		List<Map> dataList = generateMessageService.getDataList("getUSER_AUTHORITY", params);
		List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		for (Map map : dataList) {
			arrayList.add(map);
		}
		List<Map<String, Object>> tree = getTree(arrayList,"0","id");
		long end = System.currentTimeMillis();
		return tree;
	}
	
	/***************************拆分---生成报文 开始**********************************/
	/**
	 * 调用shell脚本,命令
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/generateMessage")
	@ResponseBody
	public List<Map<String, Object>> generateMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> params = getParamters(request);
		StringBuffer stringBuffer = new StringBuffer();
		List<Map> dataList = null;
		try {
			String beginSjrq = fmt.format(fmt1.parse(String.valueOf(params.get("beginSjrq"))));
			List<Map> treeData = (List<Map>) JSONArray.fromObject(params.get("treeData"));
			//遍历出表名 追加到stringBuffer
			for (int i = 0;i < treeData.size(); i++) {
				if(i == (treeData.size() - 1)){
					stringBuffer.append(new String(String.valueOf(treeData.get(i).get("key")).getBytes("UTF-8"),"GBK"));
				}else{
					stringBuffer.append(new String(String.valueOf(treeData.get(i).get("key")+"\n").getBytes("UTF-8"),"GBK"));
				}
			}
			//获取ip,用户名,密码
			getUserAndPass();
			RemoteShellExecutor executor = new RemoteShellExecutor(ip, osUsername, password);
			
			//用来判断是 生成报文、程序校验、报文上报？
			String id = (String) params.get("id");
			//覆盖文本文件 table_name.txt
			executor.exec("echo '"+stringBuffer.toString()+"' > "+shell_url+"/script/table_name_dat.txt");
			if("scbw".equals(id)){//生成报文
				//运行shell脚本
//				executor.exec(". "+shell_url+"/.profile;cd hbjy/script;sh export_ex.sh "+beginSjrq+" 1");
				executor.exec("sh "+shell_url+"/script/exec_shell.sh 'export_ex.sh "+beginSjrq+" 1'");
			}else if("cxjy".equals(id)){//程序校验
				//运行shell脚本
//				executor.exec(". "+shell_url+"/.profile;cd hbjy/script;sh export_ex.sh "+beginSjrq+" 2");
				executor.exec("sh "+shell_url+"/script/exec_shell.sh 'export_ex.sh "+beginSjrq+" 2'");
			}else if("bwsb".equals(id)){//报文上报
				//运行shell脚本
//				executor.exec(". "+shell_url+"/.profile;cd hbjy/script;sh export_ex.sh "+beginSjrq+" 4");
				executor.exec("sh "+shell_url+"/script/exec_shell.sh 'export_ex.sh "+beginSjrq+" 4'");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			dataList = generateMessageService.getDataList("getUSER_AUTHORITY", params);
		}
		List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		for (Map map : dataList) {
			arrayList.add(map);
		} 
		List<Map<String, Object>> tree = getTree(arrayList,"0","id");
		return tree;
	}
	/***************************拆分---文生成报 结束**********************************/
	
	
	/***************************拆分---报文下载 开始**********************************/
	//下载文件
	@RequestMapping(value = "/downloadMessage")
	@ResponseBody
	public void downloadMessage(HttpServletRequest request, HttpServletResponse resp){
		try {
			Map<String, Object> params = getParamters(request);
			String beginSjrq = fmt.format(fmt1.parse(String.valueOf(params.get("beginSjrq"))));
			getUserAndPass();// 获取ip,用户名,密码
			//创建连接
			Connection conn = new Connection(ip);
			conn.connect();
			boolean isAuthenticated = conn.authenticateWithPassword(osUsername, password);
			if (isAuthenticated == false)
				throw new IOException("Authentication failed.文件scp到数据服务器时发生异常");
			SCPClient client = new SCPClient(conn);
			// 获取服务器存放的路径
			String realPath = request.getSession().getServletContext().getRealPath("/upload/zip/");
			File parentFile = new File(realPath);
			//判断文件 文件夹
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			List fromObject = JSONArray.fromObject(params.get("params"));
			
			if(fromObject.size() == 1){
				//表名
				String fileName = String.valueOf(fromObject.get(0));
				//获取四位标识
				String swbs = fileName.substring(fileName.length() - 4,fileName.length());
				//错误文件名
				String FileName = swbs+provincial_jrjgbm+beginSjrq+".dat";
				// 远程的文件scp到本地目录(取带code的)
				client.get(shell_url+"/data/report_dat_rev/"+beginSjrq+"/"+FileName, realPath);
				conn.close();
				// 文件的路径
				String fileUrl = new String(String.valueOf(realPath+"/"+FileName).getBytes("UTF-8"), "UTF-8");
				// 根据文件名获取 MIME 类型
//			String contentType = request.getServletContext().getMimeType(fileUrl);
				String contentDisposition = "attachment;filename="+FileName;
				// 输入流
				FileInputStream input = new FileInputStream(fileUrl);
				// 设置头
				resp.setHeader("Content-Type", "multipart/form-data");
				resp.setHeader("Content-Disposition", contentDisposition);
				// 获取绑定了客户端的流
				ServletOutputStream output = resp.getOutputStream();
				// 把输入流中的数据写入到输出流中
				IOUtils.copy(input, output);
				input.close();
			}else{//下载多文件 zip格式
				FileInputStream input = null;
				String contentType = null;
				String contentDisposition = null;
				//创建文件路径的集合，  
			    List<String> filePath = new ArrayList<String>();  
				for (int i = 0; i < fromObject.size(); i++) {
					String fileName = (String) fromObject.get(i);
					//获取四位标识
					String swbs = fileName.substring(fileName.length() - 4,fileName.length());
					//错误文件名
					String FileName = swbs+provincial_jrjgbm+beginSjrq+".dat";
					// 远程的文件scp到本地目录
					client.get(shell_url+"/data/report_dat/"+beginSjrq+"/"+FileName, realPath);
					// 文件的路径
					String fileUrl = new String(String.valueOf(realPath+"/"+FileName).getBytes("UTF-8"), "UTF-8");
				    filePath.add(fileUrl);  
				}
				conn.close(); 
				resp.setContentType("text/html; charset=UTF-8"); //设置编码字符  
				resp.setContentType("application/x-msdownload"); //设置内容类型为下载类型  
				resp.setHeader("Content-disposition", "attachment;filename="+new String((fromObject.get(0)+"等"+fromObject.size()+"个文件.zip").getBytes("UTF-8"),"iso-8859-1"));//设置下载的文件名称  
			    OutputStream out = resp.getOutputStream();   //创建页面返回方式为输出流，会自动弹出下载框
				String zipBasePath=request.getSession().getServletContext().getRealPath("/upload/zip/");  
			    String zipFilePath = zipBasePath+"temp.zip";  
			    //根据临时的zip压缩包路径，创建zip文件  
			    File zip = new File(zipFilePath);  
			    if (!zip.exists()){     
			        zip.createNewFile();     
			    }  
			    //创建zip文件输出流  
			    FileOutputStream fos = new FileOutputStream(zip);  
			    ZipOutputStream zos = new ZipOutputStream(fos);  
			    //循环读取文件路径集合，获取每一个文件的路径  
			    for(String fp : filePath){  
			        File f = new File(fp);  //根据文件路径创建文件  
			        zipFile(f, zos);  //将每一个文件写入zip文件包内，即进行打包  
			    }  
			    fos.close();  
			    //将打包后的文件写到客户端，输出的方法同上，使用缓冲流输出  
			    InputStream fis = new BufferedInputStream(new FileInputStream(zipFilePath));  
			    byte[] buff = new byte[4096];  
			    int size = 0;  
			    while((size=fis.read(buff)) != -1){  
			        out.write(buff, 0, size);  
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/***************************拆分---生成下载 结束 **********************************/
	
	/**
	 * 判断是否有强制性校验错误
	 * @param request
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/readFile")
	@ResponseBody
	public ResponseBean readFile(HttpServletRequest request, HttpServletResponse resp) throws Exception{
	    ResponseBean responseBean = new ResponseBean();
		Map<String, Object> params = getParamters(request);
		getUserAndPass();// 获取ip,用户名,密码
		//创建连接
		Connection conn = new Connection(ip);
		conn.connect();
		boolean isAuthenticated = conn.authenticateWithPassword(osUsername, password);
		if (isAuthenticated == false)
			throw new IOException("Authentication failed.文件scp到数据服务器时发生异常");
		SCPClient client = new SCPClient(conn);
		// 获取服务器存放的路径
		String realPath = request.getSession().getServletContext().getRealPath("/upload/zip/");
		File parentFile = new File(realPath);
		//判断文件 文件夹
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		List<Map> treeData = (List<Map>) JSONArray.fromObject(params.get("treeData"));
		//遍历出表名 追加到stringBuffer
		for (int i = 0;i < treeData.size(); i++) {
			String fileName = String.valueOf(treeData.get(i).get("key"));
			//获取四位标识
			String swbs = fileName.substring(fileName.length() - 4,fileName.length());
			//错误文件名
			try {
				String beginSjrq = fmt.format(fmt1.parse(String.valueOf(params.get("beginSjrq"))));
				//错误文件名
				String FileName = "FDBD"+swbs+provincial_jrjgbm+beginSjrq+".dat";
				// 远程的文件scp到本地目录
				client.get(shell_url+"/check/error_"+beginSjrq+"/"+FileName, realPath);
				conn.close();
				InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(realPath +"/"+ FileName), "GBK");
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader, 10*1024*1024);			//读取数据缓冲流
				String lineText = null;
				while((lineText = bufferedReader.readLine()) != null) {
		              if(lineText.contains("|强制性参照信息校验错误") || lineText.contains("|强制性格式校验错误") || lineText.contains("|强制性内容校验错误") || lineText.contains("|强制性逻辑校验错误")){
		            	  params.put("TABLENAME", swbs);
		            	  int updateGeneratereport = generateMessageService.updateGeneratereport("updateGENERATEREPORT",params);
		            	  responseBean.setData(updateGeneratereport);
		            	  break;
		              }
				}	
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return responseBean;
	}
	
	//封装压缩文件的方法  
		public void zipFile(File inputFile, ZipOutputStream zipoutputStream) {
			try {
				if (inputFile.exists()) { // 判断文件是否存在
					if (inputFile.isFile()) { // 判断是否属于文件，还是文件夹

						// 创建输入流读取文件
						FileInputStream fis = new FileInputStream(inputFile);
						BufferedInputStream bis = new BufferedInputStream(fis);

						// 将文件写入zip内，即将文件进行打包
						ZipEntry ze = new ZipEntry(inputFile.getName()); // 获取文件名
						zipoutputStream.putNextEntry(ze);

						// 写入文件的方法，同上
						byte[] b = new byte[1024];
						long l = 0;
						while (l < inputFile.length()) {
							int j = bis.read(b, 0, 1024);
							l += j;
							zipoutputStream.write(b, 0, j);
						}
						zipoutputStream.closeEntry();
						// 关闭输入输出流
						bis.close();
						fis.close();
					} else { // 如果是文件夹，则使用穷举的方法获取文件，写入zip
						try {
							File[] files = inputFile.listFiles();
							for (int i = 0; i < files.length; i++) {
								zipFile(files[i], zipoutputStream);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		
	/**
	 * 错误的报文
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/repulseErrorReport")
	@ResponseBody
	public ResponseBean repulseErrorReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		Map<String, Object> params = getParamters(request);
		Map<String, Object> responseParams = new HashMap<String, Object>();
		responseParams.put("result", true);
		responseParams.put("msg", "上报报文已打回！");
		responseBean.setData(responseParams);
		
		// 校验当前报文的状态，若是错误状态才可打回
		if (generateMessageService.isReportPass(StringUtils.parseString(params.get("scbwxx_code")))) {
			responseParams.put("result", false);
			responseParams.put("msg", "当前报文没有问题，不可打回！");
			return responseBean;
		}
		
		// 获取打回的详细信息
		params.put("beginSjrq", params.get("sjrq"));
		params.put("fileName", params.get("bm"));
		Map<String, String> repulseReportInfos = getRepulseReportInfos(params,request);
		// 未找到报文上报者
		if (repulseReportInfos == null || repulseReportInfos.isEmpty()) {
			responseParams.put("result", false);
			responseParams.put("msg", "未查询到错误报文的报文上报者！");
			return responseBean;
		}
		
		// 取得格式为【机构层级_1603099998=[msg_code1,msg_code2]】的中间数据
		Map<String,List<String>> cjxxJrjgnbjghMsgCode = new HashMap<String,List<String>>();
		for (Entry<String,String> entry : repulseReportInfos.entrySet()) {
			// 机构层级_nbjgh
			String cjxxNbjgh = entry.getKey().substring(entry.getKey().lastIndexOf("_")+1, entry.getKey().length())+"_"+entry.getValue();
			// msg_code
			String msgCode = entry.getKey().substring(entry.getKey().indexOf("_")+1, entry.getKey().lastIndexOf("_"));
			// [msg_code1,msg_code2]
			List<String> msgCodes = new ArrayList<String>();
			if (cjxxJrjgnbjghMsgCode.containsKey(cjxxNbjgh)) {
				msgCodes = cjxxJrjgnbjghMsgCode.get(cjxxNbjgh);
				msgCodes.add(msgCode);
			} else {
				msgCodes = new ArrayList<String>();
				msgCodes.add(msgCode);
			}
			cjxxJrjgnbjghMsgCode.put(cjxxNbjgh, msgCodes);
		}
		
		// 取得格式为【机构层级_1603099998={msg1=[code1,code2]}】的中间数据
		Map<String,String> cjxxJrjgnbjghMsgCodeJson = new HashMap<String,String>();
		for (Entry<String,List<String>> entry : cjxxJrjgnbjghMsgCode.entrySet()) {
			// 所有不重复的msg
			Set<String> msgs = new HashSet<String>();
			for (String msgCodeOne : entry.getValue()) {
				msgs.add(msgCodeOne.substring(0, msgCodeOne.indexOf("_")));
			}
			// 格式【msg1=[code1,code2]】
			Map<String,List<String>> msgCodes = new HashMap<String,List<String>>();
			for (String msgOne : msgs) {
				List<String> codes = new ArrayList<String>();
				for (String msgCodeOne : entry.getValue()) {
					if (msgOne.equals(msgCodeOne.substring(0, msgCodeOne.indexOf("_")))) {
						codes.add(msgCodeOne.substring(msgCodeOne.indexOf("_")+1, msgCodeOne.length()));
					}
				}
				msgCodes.put(msgOne, codes);
			}
			cjxxJrjgnbjghMsgCodeJson.put(entry.getKey(), JSONObject.fromObject(msgCodes).toString());
		}
		
		// 将打回的数据插入到打回表中
		for (Entry<String,String> entry : cjxxJrjgnbjghMsgCodeJson.entrySet()) {
			Map<String, Object> sqlParam = new HashMap<String, Object>();
			sqlParam.put("sjrq", StringUtils.parseString(params.get("sjrq")));
			sqlParam.put("ywbm", StringUtils.parseString(params.get("ywbm")));
			sqlParam.put("ywmc", StringUtils.parseString(params.get("ywm")));
			sqlParam.put("tablename", StringUtils.parseString(params.get("bm")));
			sqlParam.put("biaoshi", StringUtils.parseString(params.get("reportFlag")));
			sqlParam.put("biaoshi_desc", StringUtils.parseString(params.get("reportFlagName")));
			sqlParam.put("reporter_code", entry.getKey().split("_")[1]);
			sqlParam.put("reporter_name", entry.getKey().split("_")[0].split("->")[0]);
			sqlParam.put("error_details", entry.getValue());
			sqlParam.put("jrjg_cjxx", entry.getKey().split("_")[0]);
			// 先更新，若未打回过，则新增数据
			if (generateMessageService.updateData(sqlParam, "repulseReportAgain") <= 0) {
				if (generateMessageService.insertData(sqlParam, "insertRepulseReport") <= 0) {
					responseParams.put("result", false);
					responseParams.put("msg", "打回失败，请稍候再试！");
					return responseBean;
				}
			}
		}
		
		return responseBean;
	}
	
	/**
	 * 将报文错误的短信发送给报文上报者
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/sendReportErrorMsg")
	@ResponseBody
	public ResponseBean sendReportErrorMsg(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		Map<String, Object> params = getParamters(request);
		Map<String, Object> responseParams = new HashMap<String, Object>();
		responseParams.put("result", true);
		responseParams.put("msg", "短信发送成功！");
		responseBean.setData(responseParams);
		
		// 校验当前报文的状态，若是错误状态才可打回
		if (generateMessageService.isReportPass(StringUtils.parseString(params.get("scbwxx_code")))) {
			responseParams.put("result", false);
			responseParams.put("msg", "当前报文没有问题，无需发送短信！");
			return responseBean;
		}
		
		// 获取打回的详细信息
		params.put("beginSjrq", params.get("sjrq"));
		params.put("fileName", params.get("bm"));
		Map<String, String> repulseReportInfos = getRepulseReportInfos(params,request);
		// 未找到报文上报者
		if (repulseReportInfos == null || repulseReportInfos.isEmpty()) {
			responseParams.put("result", false);
			responseParams.put("msg", "未查询到错误报文的报文上报者！");
			return responseBean;
		}
		
		// 要给发送短信的人员的用户名集合（另临时存储中间值：报文上报者机构编码）
		Set<String> usernames = new HashSet<String>();
		// 报文上报者上级市办机构编码
		Set<String> sbjgNbjghs = new HashSet<String>();
		for (Entry<String,String> entry : repulseReportInfos.entrySet()) {
			// 取得所有报文上报者上级市办机构信息(取得Jb为2的)
			if ("2".equals(entry.getKey().split("_")[0])) {
				sbjgNbjghs.add(entry.getValue());
			}
		}
		// 根据市办机构编码，取得所有该市办的用户
		Map<String, Object> searchParams = new HashMap<String,Object>();
		for (String sbjgNbjgh : sbjgNbjghs) {
			searchParams.put("DWBM", sbjgNbjgh);
			searchParams.put("SMSNOTICE", "1");
			List<Map> sbUserInfos = userManageService.getDataList(searchParams, "getUserManageInfo");
			for (Map sbUserInfo : sbUserInfos) {
				usernames.add((String)sbUserInfo.get("YH_NAME"));
			}
		}
		if (usernames.isEmpty()) {
			responseParams.put("result", false);
			responseParams.put("msg", "发送短信的目标用户，均不接收短信！");
			return responseBean;
		}
		
//		String msg = "数据日期【"+StringUtils.parseString(params.get("sjrq"))+"】的业务【"+StringUtils.parseString(params.get("ywm"))+"】的报文因【"+StringUtils.parseString(params.get("reportFlagName"))+"】失败，请及时处理！";
//		String msg = "货币金银采集系统中有您一条未完成的任务，请前往处理！";
		String msg = "货币金银采集系统中有您一条未完成任务，报表名称【"+StringUtils.parseString(params.get("ywm"))+"】请前往处理！";
		// 发送短信给报文上报者
		boolean isAllFailed = true;
		for (String username : usernames) {
			if (CommonUtil.sendMobileMessage(username, msg, StringUtils.parseString(request.getSession().getAttribute("username")))) {
				isAllFailed = false;
			}
		}
		// 全部失败则报错
		if (isAllFailed) {
			responseParams.put("result", false);
			responseParams.put("msg", "短信发送失败！");
		}
				
		return responseBean;
	}
	
	/**
	 * 查询用户的代办事项
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getUserTodos")
	@ResponseBody
	public List<Map<String, Object>> getUserTodos(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, Object> params = getParamters(request);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		// 上报者用户名
		searchParams.put("reporterCode", params.get("reporterCode"));
		searchParams.put("isFinished", false);
		List<Map> dataList = generateMessageService.getDataList("selectRepulseReport", searchParams);
		List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		for (Map map : dataList) {
			arrayList.add(map);
		}
		return arrayList;
	}
	
	/**
	 * 查询当前登录用户的代办事项
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getCurrentUserTodos")
	@ResponseBody
	public List<Map<String, Object>> getCurrentUserTodos(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, Object> params = getParamters(request);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		// 上报者用户名设为当前登录用户的用户名
		searchParams.put("reporterCode", request.getSession().getAttribute("nbjgh"));
		searchParams.put("isFinished", false);
		List<Map> dataList = generateMessageService.getDataList("selectRepulseReport", searchParams);
		List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		for (Map map : dataList) {
			// 查询下级待办事项是否已完成
			map.put("isChildTodosAllFinished", generateMessageService.isChildJrjgTodosAllFinished(
					StringUtils.parseStringWithoutNull(map.get("sjrq")), 
					StringUtils.parseStringWithoutNull(map.get("ywbm")),
					StringUtils.parseStringWithoutNull(map.get("jrjg_cjxx"))));
			arrayList.add(map);
		}
		return arrayList;
	}
	
	/**
	 * 获取指定报表的字段信息（按照库中column_id升序显示）
	 * @param request 包含参数（tableName：库表名）
	 * @param response
	 * @return 指定报表的字段信息（按照库中column_id升序显示）
	 * @throws Exception
	 */
	@RequestMapping(value = "/getTableFieldInfo")
	@ResponseBody
	public List<Map<String, Object>> getTableFieldInfo(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, Object> params = getParamters(request);
		List<Map> dataList = generateMessageService.getDataList("getFieldInfoByTableName", params);
		List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		for (Map map : dataList) {
			arrayList.add(map);
		}
		return arrayList;
	}
	
	/**
	 * 获取错误详细信息（将code转换成完整数据）
	 * @param request 包含参数（tableName：库表名；tableFields：表字段（以逗号分隔）；errorDetailsJson：错误详细数据信息（包含记录code）；）
	 * @param response
	 * @return 错误详细数据（包含完整数据）
	 * @throws Exception
	 */
	@RequestMapping(value = "/getErrorDetailsDataInfo")
	@ResponseBody
	public Map<String, List<Map>> getErrorDetailsDataInfo(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 结果集{msg1:[{field1:data1,field2:data2},{field1:data1,field2:data2}]}
		Map<String, List<Map>> result = new HashMap<String, List<Map>>();
		
		Map<String, Object> params = getParamters(request);
		// 库表名
		String tableName = StringUtils.parseStringWithoutNull(params.get("tableName"));
		// 表字段（以逗号分隔）
		String tableFields = StringUtils.parseStringWithoutNull(params.get("tableFields"));
		// 错误详细数据信息（包含记录code）
		JSONObject errorDetails = JSONObject.fromObject(StringUtils.parseStringWithoutNull(params.get("errorDetailsJson")));
		
		// 获取错误详细信息（将code转换成完整数据）
		Map<String, Object> shearchParams = new HashMap<String, Object>();
		for (Object key : errorDetails.keySet()) {
			shearchParams.put("tableName", tableName);
			shearchParams.put("tableFields", tableFields);
			shearchParams.put("codes", StringUtils.join((List<String>)errorDetails.get(key), ","));
			List<Map> dataList = generateMessageService.getDataList("selectDatasFromCustomTable", shearchParams);
			result.put(StringUtils.parseStringWithoutNull(key), dataList);
		}
		return result;
	}
	
	/**
	 * 更新待办事项状态为完成
	 * @param request 包含参数（sjrq：数据日期；ywbm：业务编码，四位标识码；reporterCode：上报者金融机构号；jrjgCjxx：金融机构层级信息；）
	 * @param response
	 * @return 是否更新成功
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateRepulseReportFinishDate")
	@ResponseBody
	public ResponseBean updateRepulseReportFinishDate(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResponseBean responseBean = new ResponseBean();
		Map<String, Object> params = getParamters(request);
		Map<String, Object> responseParams = new HashMap<String, Object>();
		responseParams.put("result", true);
		responseParams.put("msg", "待办事项状态已更新！");
		responseBean.setData(responseParams);
		
		// 参数校验
		if (StringUtils.isEmpty(params.get("sjrq")) || StringUtils.isEmpty(params.get("ywbm")) 
				|| StringUtils.isEmpty(params.get("reporterCode")) || StringUtils.isEmpty(params.get("jrjgCjxx"))) {
			responseParams.put("result", false);
			responseParams.put("msg", "系统异常，请联系管理员！");
			logger.error("更新待办事项状态为完成-参数为空");
			return responseBean;
		} 
		
		// 查询参数
		Map<String, Object> searchParams = new HashMap<String, Object>();
		// 当前机构金融机构层级信息
		String jrjgCjxx = StringUtils.parseString(params.get("jrjgCjxx"));
		
		// 若当前机构的打回所对应的下级机构的打回还未完成，则当前机构不可将打回状态更新为完成
		if (!generateMessageService.isChildJrjgTodosAllFinished(
				StringUtils.parseString(params.get("sjrq")),
				StringUtils.parseString(params.get("ywbm")),
				jrjgCjxx)) {
			responseParams.put("result", false);
			responseParams.put("msg", "当前待办事项需要所有下级机构完成后，才可更新状态为完成！");
			return responseBean;
		}
//		// 当前机构的打回所对应的所有下级机构的打回的机构层级信息
//		List<String> childjrjgCjxxList = new ArrayList<String>();
//		int childJbJgCount = jrjgCjxx.split("-\\>").length-1;// 当前机构的打回所对应的下级机构的打回的数目
//		String tempJrjgCjxx = null;// 临时存储所有下级打回信息的机构层级信息
//		for (int i = 1; i <= childJbJgCount; i++) {
//			tempJrjgCjxx = tempJrjgCjxx == null ? jrjgCjxx.substring(jrjgCjxx.indexOf("->")+2,jrjgCjxx.length()) 
//					: tempJrjgCjxx.substring(tempJrjgCjxx.indexOf("->")+2,tempJrjgCjxx.length());
//			childjrjgCjxxList.add(tempJrjgCjxx);
//		}
//		if (!childjrjgCjxxList.isEmpty()) {
//			searchParams.put("sjrq", StringUtils.parseString(params.get("sjrq")));
//			searchParams.put("ywbm", StringUtils.parseString(params.get("ywbm")));
//			searchParams.put("jrjgCjxxList", childjrjgCjxxList);
//			searchParams.put("isFinished", false);
//			List<Map> dataList = generateMessageService.getDataList("selectRepulseReport", searchParams);
//			if (dataList != null && !dataList.isEmpty()) {
//				responseParams.put("result", false);
//				responseParams.put("msg", "当前待办事项需要所有下级机构完成后，才可更新状态为完成！");
//				return responseBean;
//			}
//		}
		
		// 更新待办事项状态为完成
		searchParams.clear();
		searchParams.put("sjrq", StringUtils.parseString(params.get("sjrq")));
		searchParams.put("ywbm", StringUtils.parseString(params.get("ywbm")));
		searchParams.put("reporter_code", StringUtils.parseString(params.get("reporterCode")));
		searchParams.put("jrjg_cjxx", StringUtils.parseString(params.get("jrjgCjxx")));
		if (generateMessageService.updateData(searchParams, "updateRepulseReportFinishDate") <= 0) {
			responseParams.put("result", false);
			responseParams.put("msg", "待办事项状态更新失败！");
		}
		return responseBean;
	}
}

