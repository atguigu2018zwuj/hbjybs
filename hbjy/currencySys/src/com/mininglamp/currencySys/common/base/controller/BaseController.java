package com.mininglamp.currencySys.common.base.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mininglamp.currencySys.common.util.StringEscapeEditor;
import com.mininglamp.currencySys.insertData.service.InsertDataService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
/**
 * 基本类控制器
 * @author czy
 *
 */
@Controller
@RequestMapping("/baseController")
@Api(value="base",hidden=true,description="基本控制类默认隐藏")
public class BaseController {
	@Autowired
	private InsertDataService insertDataService;
	
	public Logger logger = Logger.getLogger(BaseController.class);
	public String [] posLoans = {"E02","B13","C16","C15","D15","D13"};
	public String [] posManagers = {"B10","C11","D08","E21"};

	public void initBinder(ServletRequestDataBinder binder){
		/**
		 * 自动转换日期
		 */
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),true));
		/**
		 * 防止XSS共计
		 */
		binder.registerCustomEditor(String.class, new StringEscapeEditor(true,false));
		
	}
	/**
	 * 通用jsp跳转方法
	 * 不做权限限制
	 * @param folder
	 * @param jspName
	 * @return
	 */
	@ApiOperation(hidden=true,value="jspjump")
	@RequestMapping("/{folder}/{jspName}")
	public String redirectJsp(@PathVariable String folder,@PathVariable String jspName){
		return "/" + folder + "/" + jspName;
	}
	
	/**
     * 获取参数
     * @return
     */
    public Map<String, Object> getParamters(HttpServletRequest request){
 		Map<String, Object> paras = new HashMap<String, Object>();
 		@SuppressWarnings("unchecked")
		Map<String, String[]> param = request.getParameterMap();
 		Set<String> keyset = param.keySet();
 		for(String key : keyset){
 			if(key.endsWith("[]")){
 				paras.put(key.substring(0, key.length()-2), param.get(key));
 			}else{
 				if(param.get(key)[0]!=null&&param.get(key)[0]!=""){
// 	 			if(param.get(key)[0]!=null && param.get(key)[0]!="" && !"".equals(param.get(key)[0])){
 					paras.put(key, param.get(key)[0]);
 				}
 			}
 		}
 		return paras;
 	}
    
    /**
     * 记录报表每条记录的操作日志
     * @param wjbm 操作的报送表的4位标识码
     * @param recordCode 报表记录的code值
     * @param username 操作者的用户名
     * @param type 操作类型：1-新增；2-修改；3-删除；
     * @return 是否新增记录成功
     */
    public boolean insertRecordOperationLog(String wjbm,int recordCode,String username,String type){
    	return insertDataService.insertRecordOperationLog(wjbm, recordCode, username, type);
    }
}
