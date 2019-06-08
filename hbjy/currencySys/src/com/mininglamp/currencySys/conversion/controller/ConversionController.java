/*package com.mininglamp.currencySys.conversion.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mininglamp.currencySys.common.base.controller.BaseController;
import com.mininglamp.currencySys.conversion.service.ConversionService;
import com.mininglamp.currencySys.generateMessage.controller.GenerateMessageController;
import com.mininglamp.currencySys.generateMessage.controller.RemoteShellExecutor;
import com.mininglamp.currencySys.uploadAndDownload.service.impl.UploadAndAownloadServiceImp;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import net.sf.json.JSONArray;

*//**
 * 调用本地脚本
 * @author Administrator
 *
 *//*
@Controller
@RequestMapping(value = "/conversionController")
public class ConversionController extends BaseController{

	@Autowired
	private ConversionService conversionService;
	
	SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd");
	private String provincial_jrjgbm = null;//金融机构编码
	private String shell_url = null;//脚本路径
	
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
	
	*//***************************下载失败文件开始**********************************//*
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
		}
	}
	
	*//***************************下载失败文件 结束**********************************//*
	
	*//**
	 * 数据文件查询
	 * @param request
	 * @param response
	 * @param sqlId
	 * @return
	 * @throws Exception
	 *//*
	@SuppressWarnings("finally")
	@RequestMapping(value = "/getConversionData")
	@ResponseBody
	public List<Map<String, Object>> getUserData(HttpServletRequest request, HttpServletResponse response, String sqlId){
		long start = System.currentTimeMillis();
		Map<String, Object> params = getParamters(request);
		String fileName = null;
		getUserAndPass();// 获取ip,用户名,密码
		try {
			String beginSjrq = fmt.format(fmt1.parse(String.valueOf(params.get("beginSjrq"))));
			String ywm = (String) params.get("ywm");
			//文件位置
			String realPath = shell_url+"/data/"+beginSjrq+".txt";
			File file = new File(realPath);
			
//			if(ywm != null && !"null".equals(ywm)){
				if(file.exists()){
					fileName = FileUtils.readFileToString(file, "UTF-8");
				}else{
					String[] command = {"/bin/sh" , "-c" , " sh "+shell_url+"/script/exec_shell.sh 'import.sh "+beginSjrq +" 1'"};
					GenerateMessageController.getRunTime(command);
					if(file.exists()){
						fileName = FileUtils.readFileToString(file, "UTF-8");
					}else{
						fileName = "-1";//解压失败
					}
				}
//			}else{
				
//			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
			if("-2".equals(fileName)){
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("文件不存在", "文件不存在");
				arrayList.add(hashMap);
				hashMap = null;
				return arrayList;
			}else if("-1".equals(fileName)){
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("解压失败", "解压失败");
				arrayList.add(hashMap);
				hashMap = null;
				return arrayList;
			}else if(fileName == null || "null".equals(fileName)){
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("异常", "异常");
				arrayList.add(hashMap);
				hashMap = null;
				return arrayList;
			}else {
				conversion(params, arrayList, fileName);
				List<Map<String, Object>> tree = getTree(arrayList,"0","id");
				long end = System.currentTimeMillis();
				arrayList = null;
				return tree;
			}
		}
	}
	
	*//**
	 * 数据转换查询
	 * @param request
	 * @param response
	 * @param sqlId
	 * @return
	 * @throws Exception
	 *//*
	@SuppressWarnings("finally")
	@RequestMapping(value = "/getTransformation")
	@ResponseBody
	public List<Map<String, Object>> getTransformation(HttpServletRequest request, HttpServletResponse response, String sqlId)
			throws Exception {
		long start = System.currentTimeMillis();
		Map<String, Object> params = getParamters(request);
		List<Map> dataList = conversionService.getDataList("getTransformation", params);
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
	@SuppressWarnings("finally")
	@RequestMapping(value = "/warehousingConversion")
	@ResponseBody
	public List<Map<String, Object>> generateMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> params = getParamters(request);
		StringBuffer stringBuffer = new StringBuffer();
		String beginSjrq =null;
		List<Map> dataList = null;
		//用来判断是 生成报文、程序校验、报文上报？
		String id = (String) params.get("id");
		try {
			beginSjrq = fmt.format(fmt1.parse(String.valueOf(params.get("beginSjrq"))));
			List<Map> treeData = (List<Map>) JSONArray.fromObject(params.get("treeData"));
			//遍历出表名 追加到stringBuffer
			for (int i = 0;i < treeData.size(); i++) {
				if("sjrk".equals(id)){
					stringBuffer.append(new String(String.valueOf(treeData.get(i).get("text")+"\n").getBytes("UTF-8"),"GBK"));
				}else if("sjzh".equals(id)){
					stringBuffer.append(new String(String.valueOf(treeData.get(i).get("key")+"\n").getBytes("UTF-8"),"GBK"));
				}
			}
			//获取ip,用户名,密码
			getUserAndPass();
			if("sjrk".equals(id)){//入库
				//覆盖文本文件 file.txt
				String path = shell_url+"/script/file.txt";
				GenerateMessageController.contentToTxt(path,stringBuffer.toString());
				//运行shell脚本
				String[] command = {"/bin/sh" , "-c" , " sh "+shell_url+"/script/exec_shell.sh 'import.sh "+beginSjrq +" 2'"};
				GenerateMessageController.getRunTime(command);
			}else if("sjzh".equals(id)){//转换
				//覆盖文本文件 table_name.txt
				String path = shell_url+"/script/table_name.txt";
				GenerateMessageController.contentToTxt(path,stringBuffer.toString());
				//运行shell脚本
				String[] command = {"/bin/sh" , "-c" , " sh "+shell_url+"/script/exec_shell.sh 'convert.sh "+beginSjrq +" 2'"};
				GenerateMessageController.getRunTime(command);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
			if("sjrk".equals(id)){
				String realPath = shell_url+"/data/"+beginSjrq+".txt";
				File file = new File(realPath);
				String fileName = FileUtils.readFileToString(file, "UTF-8");
				conversion(params, arrayList, fileName);
			}else if("sjzh".equals(id)){
				dataList = conversionService.getDataList("getTransformation", params);
				for (Map map : dataList) {
					arrayList.add(map);
				} 
			}
			List<Map<String, Object>> tree = getTree(arrayList,"0","id");
			arrayList = null;
			long end = System.currentTimeMillis();
			return tree;
		}
		
	}
	*//***************************拆分---文生成报 结束**********************************//*

	public void conversion(Map<String, Object> params, List<Map<String, Object>> arrayList, String fileName) {
		List<Map> dataList;
		dataList = conversionService.getDataList("getBs", params);
		String[] split = fileName.split("\n");
		boolean fal = false;
		//获取入库时 业务名的 模糊查询
		String ywm = (String) params.get("ywm");
		for (int i = 0; i < split.length; i++) {
			String fileNa = split[i].substring(0,split[i].length()-4);
			if(ywm != null && !"null".equals(ywm)){
				if(fileNa.contains(ywm) && ywm != null){
					fal = true;
				}else{
					continue;
				}
			}else{
				fal = true;
			}
			if(fal){
				HashMap<String, Object> Map = new HashMap<String, Object>();
				for (int j = 0; j < dataList.size(); j++) {
					String key = (String) dataList.get(j).get("key");
					if(key.equals(fileNa)){
						Map.put("scbwxx", dataList.get(j).get("scbwxx"));
						break;
					}
				}
				Map.put("text", fileNa);//去除后四位   .txt
				Map.put("id", (i+2)+"");
				Map.put("pid", "1");
				Map.put("key", null);
				arrayList.add(Map);
				Map=null;
			}
		}
		HashMap<String, Object> Parent = new HashMap<String, Object>();
		Parent.put("text", "全部");
		Parent.put("key", "全部");
		Parent.put("id", "1");
		Parent.put("pid", "0");
		Parent.put("scbwxx", "");
		arrayList.add(Parent);
	}
	
}*/



package com.mininglamp.currencySys.conversion.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mininglamp.currencySys.common.base.controller.BaseController;
import com.mininglamp.currencySys.conversion.service.ConversionService;
import com.mininglamp.currencySys.generateMessage.controller.RemoteShellExecutor;
import com.mininglamp.currencySys.uploadAndDownload.service.impl.UploadAndAownloadServiceImp;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import net.sf.json.JSONArray;

/**
 * 调用远程脚本
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/conversionController")
public class ConversionController extends BaseController{

	@Autowired
	private ConversionService conversionService;
	
	SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd");
	private String ip = null;//服务器ip
	private String osUsername = null;//用户名
	private String password = null;//密码
	private String provincial_jrjgbm = null;//金融机构编码
	private String shell_url = null;//脚本路径
	
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
	
	/***************************下载失败文件开始**********************************/
	//下载失败文件
	@RequestMapping(value = "/downFlie")
	@ResponseBody
	public void downFlie(HttpServletRequest request, HttpServletResponse resp){
		try {
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
			String realPath = request.getSession().getServletContext().getRealPath("/errorCheckFile/");
			File parentFile = new File(realPath);
			//判断文件 文件夹
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			//表名
			String fileName = String.valueOf(params.get("fileName"));
			//获取四位标识
			String swbs = fileName.substring(fileName.length() - 4,fileName.length());
			//错误文件名
			String errorFileName = "FDBD"+swbs+provincial_jrjgbm+fmt.format(fmt1.parse(String.valueOf(params.get("beginSjrq"))))+".dat";
			// 远程的文件scp到本地目录
			client.get(shell_url+"/check/error_"+fmt.format(fmt1.parse(String.valueOf(params.get("beginSjrq"))))+"/"+errorFileName, realPath);
			conn.close();
			// 文件的路径
			String fileUrl = new String(String.valueOf(realPath+"/"+errorFileName).getBytes("UTF-8"), "UTF-8");
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
		}
	}
	
	/***************************下载失败文件 结束**********************************/
	
	/**
	 * 数据文件查询
	 * @param request
	 * @param response
	 * @param sqlId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value = "/getConversionData")
	@ResponseBody
	public List<Map<String, Object>> getUserData(HttpServletRequest request, HttpServletResponse response, String sqlId){
		long start = System.currentTimeMillis();
		Map<String, Object> params = getParamters(request);
		String fileName = null;
		getUserAndPass();// 获取ip,用户名,密码
		try {
			String beginSjrq = fmt.format(fmt1.parse(String.valueOf(params.get("beginSjrq"))));
			String ywm = (String) params.get("ywm");
			if(ywm != null && !"null".equals(ywm) && !"".equals(ywm)){
				//创建连接
				Connection conn = new Connection(ip);
				conn.connect();
				boolean isAuthenticated = conn.authenticateWithPassword(osUsername, password);
				if (isAuthenticated == false)
					throw new IOException("Authentication failed.文件scp到数据服务器时发生异常");
				SCPClient client = new SCPClient(conn);
				// 获取服务器存放的路径
				String realPath = request.getSession().getServletContext().getRealPath("/dateFile/");
				File parentFile = new File(realPath);
				//判断文件 文件夹
				if (!parentFile.exists()) {
					parentFile.mkdirs();
				}
				// 远程的文件scp到本地目录
				client.get(shell_url+"/tmp/"+beginSjrq+".txt", realPath);
				conn.close();
				File file = new File(realPath+"/"+beginSjrq+".txt");
				fileName = FileUtils.readFileToString(file, "UTF-8");
			}else{
				RemoteShellExecutor executor = new RemoteShellExecutor(ip, osUsername, password);
				String exec = executor.exec1("sh "+shell_url+"/script/exec_shell.sh 'import.sh "+beginSjrq+" 1'");
				//判断返回值内容  得出文件存不存在 
				/*if(exec != null && exec.contains("code is 0")){*/
				if(exec.contains("code is 0")&&(!exec.contains("not exists"))){
					//创建连接
					Connection conn = new Connection(ip);
					conn.connect();
					boolean isAuthenticated = conn.authenticateWithPassword(osUsername, password);
					if (isAuthenticated == false)
						throw new IOException("Authentication failed.文件scp到数据服务器时发生异常");
					SCPClient client = new SCPClient(conn);
					// 获取服务器存放的路径
					String realPath = request.getSession().getServletContext().getRealPath("/dateFile/");
					File parentFile = new File(realPath);
					//判断文件 文件夹
					if (!parentFile.exists()) {
						parentFile.mkdirs();
					}
					// 远程的文件scp到本地目录
					client.get(shell_url+"/tmp/"+beginSjrq+".txt", realPath);
					conn.close();
					File file = new File(realPath+"/"+beginSjrq+".txt");
					fileName = FileUtils.readFileToString(file, "UTF-8");
				}else if(exec != null && exec.contains("code is 1") && exec.contains("ERROR")){
					fileName = "-1";//解压失败
				}else{
					fileName = "-2";//文件不存在时
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
			if("-2".equals(fileName)){
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("文件不存在", "文件不存在");
				arrayList.add(hashMap);
				hashMap = null;
				return arrayList;
			}else if("-1".equals(fileName)){
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("解压失败", "解压失败");
				arrayList.add(hashMap);
				hashMap = null;
				return arrayList;
			}else if(fileName == null || "null".equals(fileName)){
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("异常", "异常");
				arrayList.add(hashMap);
				hashMap = null;
				return arrayList;
			}else {
				conversion(params, arrayList, fileName);
				List<Map<String, Object>> tree = getTree(arrayList,"0","id");
				long end = System.currentTimeMillis();
				arrayList = null;
				return tree;
			}
		}
	}
	
	/**
	 * 数据转换查询
	 * 数据转换初始化查询数据
	 * @param request
	 * @param response
	 * @param sqlId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value = "/getTransformation")
	@ResponseBody
	public List<Map<String, Object>> getTransformation(HttpServletRequest request, HttpServletResponse response, String sqlId)
			throws Exception {
		long start = System.currentTimeMillis();
		Map<String, Object> params = getParamters(request);
		List<Map> dataList = conversionService.getDataList("getTransformation", params);
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
	@SuppressWarnings("finally")
	@RequestMapping(value = "/warehousingConversion")
	@ResponseBody
	public List<Map<String, Object>> generateMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> params = getParamters(request);
		StringBuffer stringBuffer = new StringBuffer();
		String beginSjrq =null;
		List<Map> dataList = null;
		//用来判断是 生成报文、程序校验、报文上报？
		String id = (String) params.get("id");
		try {
			beginSjrq = fmt.format(fmt1.parse(String.valueOf(params.get("beginSjrq"))));
			List<Map> treeData = (List<Map>) JSONArray.fromObject(params.get("treeData"));
			//遍历出表名 追加到stringBuffer
			for (int i = 0;i < treeData.size(); i++) {
				// 排除父节点
				if ("0".equals(String.valueOf(treeData.get(i).get("pid")))) {
					continue;
				}
				if(i == (treeData.size() - 1)){
					if("sjrk".equals(id)){
						stringBuffer.append(new String(String.valueOf(treeData.get(i).get("text")).getBytes("UTF-8"),"GBK"));
					}else if("sjzh".equals(id)){
						stringBuffer.append(new String(String.valueOf(treeData.get(i).get("key")).getBytes("UTF-8"),"GBK"));
					}
				}else{
					if("sjrk".equals(id)){
						stringBuffer.append(new String(String.valueOf(treeData.get(i).get("text")+"\n").getBytes("UTF-8"),"GBK"));
					}else if("sjzh".equals(id)){
						stringBuffer.append(new String(String.valueOf(treeData.get(i).get("key")+"\n").getBytes("UTF-8"),"GBK"));
					}
				}
			}
			//获取ip,用户名,密码
			getUserAndPass();
			RemoteShellExecutor executor = new RemoteShellExecutor(ip, osUsername, password);
			
			if("sjrk".equals(id)){//入库
				//覆盖文本文件 file.txt
				executor.exec("echo '"+stringBuffer.toString()+"' > "+shell_url+"/script/file.txt");
				//运行shell脚本
//				executor.exec(". "+shell_url+"/.profile;cd hbjy/script;sh import.sh "+beginSjrq+" 2");
				executor.exec("sh "+shell_url+"/script/exec_shell.sh 'import.sh "+beginSjrq+" 2'");
				/*executor.exec("sh "+shell_url+"/script/import.sh "+beginSjrq+" 2");*/
			}else if("sjzh".equals(id)){//转换
				//覆盖文本文件 table_name.txt
				executor.exec("echo '"+stringBuffer.toString()+"' > "+shell_url+"/script/table_name.txt");
				//运行shell脚本
//				executor.exec(". "+shell_url+"/.profile;cd hbjy/script;sh convert.sh "+beginSjrq+" 2");
				executor.exec("sh "+shell_url+"/script/exec_shell.sh 'convert.sh "+beginSjrq+" 2'");
				/*executor.exec("sh "+shell_url+"/script/convert.sh "+beginSjrq+" 2'");*/
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
			if("sjrk".equals(id)){
				String realPath = request.getSession().getServletContext().getRealPath("/dateFile/");
				File file = new File(realPath+"/"+beginSjrq+".txt");
				String fileName = FileUtils.readFileToString(file, "UTF-8");
				conversion(params, arrayList, fileName);
			}else if("sjzh".equals(id)){
				dataList = conversionService.getDataList("getTransformation", params);
				for (Map map : dataList) {
					arrayList.add(map);
				} 
			}
			List<Map<String, Object>> tree = getTree(arrayList,"0","id");
			arrayList = null;
			long end = System.currentTimeMillis();
			return tree;
		}
		
	}
	/***************************拆分---文生成报 结束**********************************/

	public void conversion(Map<String, Object> params, List<Map<String, Object>> arrayList, String fileName) {
		List<Map> dataList;
		dataList = conversionService.getDataList("getBs", params);
		String[] split = fileName.split("\n");
		boolean fal = false;
		//获取入库时 业务名的 模糊查询
		String ywm = (String) params.get("ywm");
		for (int i = 0; i < split.length; i++) {
			String fileNa = split[i].substring(0,split[i].length()-4);
			if(ywm != null && !"null".equals(ywm)){
				if(fileNa.contains(ywm) && ywm != null){
					fal = true;
				}else{
					continue;
				}
			}else{
				fal = true;
			}
			if(fal){
				HashMap<String, Object> Map = new HashMap<String, Object>();
				for (int j = 0; j < dataList.size(); j++) {
					String key = (String) dataList.get(j).get("key");
					if(key.equals(fileNa)){
						Map.put("scbwxx", dataList.get(j).get("scbwxx"));
						break;
					}
				}
				Map.put("text", fileNa);//去除后四位   .txt
				Map.put("id", (i+2)+"");
				Map.put("pid", "1");
				Map.put("key", null);
				arrayList.add(Map);
				Map=null;
			}
		}
		HashMap<String, Object> Parent = new HashMap<String, Object>();
		Parent.put("text", "全部");
		Parent.put("key", "全部");
		Parent.put("id", "1");
		Parent.put("pid", "0");
		Parent.put("scbwxx", "");
		arrayList.add(Parent);
	}
	
}
