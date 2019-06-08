package com.mininglamp.currencySys.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mininglamp.currencySys.ManaPro.service.ManaProService;


/**
 * 
 * @author falcon
 * 新增通用类工具方法
 * 	add by cxk 2015年3月10日10:34:51
 */

public class CommonValidateUtil {
	private static final Logger logger = LoggerFactory.getLogger(CommonValidateUtil.class);
	
	/**
	 * 检验经常性报表  上报类型是否符合当前用户
	 * @param SBLXValue 上报类型的值
	 * @param tableName 报表名称
	 * @param request 用于获取session信息
	 * @return 若校验错误，则返回错误信息，否则返回null
	 */
	public static String validateSBLX(String SBLXValue,String tableName,HttpServletRequest request){
		// 两张表不校验：SPECIAL_JQSB、SPECIAL_RCYW
		if ("SPECIAL_JQSB".equalsIgnoreCase(tableName) || "SPECIAL_RCYW".equalsIgnoreCase(tableName)) {
			return null;
		}
		if(SBLXValue != null && !"null".equals(SBLXValue) && SBLXValue.length() > 0){
			if("0".equals(SBLXValue)){
				//获取省级用户的内部机构号
				String provincialUsers = (String) request.getSession().getAttribute("provincialUsers");
				//获取当前用户的内部机构号
				String userszdwbm = (String) request.getSession().getAttribute("userszdwbm");
				if(provincialUsers != null && !"null".equals(provincialUsers) && provincialUsers.length() > 0 ){
					if(userszdwbm != null && !"null".equals(userszdwbm) && userszdwbm.length() > 0){
						if(!provincialUsers.equals(userszdwbm)){
							return "【上报类型】不是自己本级或下级";
						}
					}
				}else{
					logger.error("配置文件中【省级用户内部机构号】异常");
					return "配置文件中【省级用户内部机构号】异常";
				}
			}else if(!"1".equals(SBLXValue) && !"2".equals(SBLXValue) && !"3".equals(SBLXValue)){
				return "【上报类型】不是有效编码 ";
			} else {
				// 用户机构级别
				String userJgjb = (String) request.getSession().getAttribute("level");
				// 可以上报下级的、本级的，不能上报上级的
				if (("1".equals(SBLXValue) && !"1".equals(userJgjb) && !"2".equals(userJgjb))
						|| ("2".equals(SBLXValue) && !"1".equals(userJgjb) && !"2".equals(userJgjb) && !"3".equals(userJgjb))) {
					return "【上报类型】不是自己本级或下级";
				}
			}
		}
		return null;
	}
	
	/**
	 * 码表码值参照性校验
	 * @param value 校验的值
	 * @param fieldName 字段中文名
	 * @param isContainConfTable 是否根据配置的表进行参照性校验（1：是；0：否；）
	 * @param confTableName 配置的参照性校验表
	 * @param dbCodeTableKeys 包含所有码表的key的码表信息集合
	 * @return 若校验错误，则返回错误信息，否则返回null
	 */
	public static String validateCodeFormat(String value,String fieldName,String isContainConfTable,String confTableName,Map<String,List<String>> dbCodeTableKeys){
		// 根据配置的表进行参照性校验，且配置表不为空时进行校验（金融机构编码不进行校验）
		if ("1".equals(isContainConfTable) && StringUtils.isNotEmpty(confTableName) && !"special_finf".equals(confTableName) && !"code_sblx".equals(confTableName)) {
			if (!dbCodeTableKeys.containsKey(confTableName) || !dbCodeTableKeys.get(confTableName).contains(value)) {
				return "【"+fieldName.substring(fieldName.lastIndexOf("_")+1, fieldName.length())+"】字段不是有效编码";
			}
		}
		return null;
	}
	
	/**
	 * 权限管理-金融机构、专项日报金融机构基本信息 校验
	 * @param NBJGHValue 
	 * @param params 校验参数（</br>
	 * 		NBJGHValue：要校验的内部机构号；</br>
	 * 		JGJBValue：要校验的机构级别；</br>
	 * 		tableName：HBJYODS.JRJGXX(权限管理-金融机构)、SPECIAL_FINF(专项日报金融机构基本信息)；</br>
	 * 		sjgljgNbjgh：上级管理内部机构号；）
	 * @param request 用于获取session信息
	 * @param manaProService 查询数据用
	 * @return 若校验错误，则返回错误信息，否则返回null
	 * @throws 参数错误
	 */
	public static String validateJRJGXX(Map<String, Object> params, HttpServletRequest request, ManaProService manaProService) throws Exception{
		Map<String, Object> tempParams = new HashMap<String, Object>();
		List<Map> jrjgDateCount = null;
		String NBJGHValue = StringUtils.parseStringWithoutNull(params.get("NBJGHValue"));
		String JGJBValue = StringUtils.parseStringWithoutNull(params.get("JGJBValue"));
		String tableName = StringUtils.parseStringWithoutNull(params.get("tableName"));
		tableName = "jgxx".equals(tableName) ? "HBJYODS.JRJGXX" : ("finf".equals(tableName) ? "SPECIAL_FINF" : tableName);
		String sjgljgNbjgh = StringUtils.parseStringWithoutNull(params.get("sjgljgNbjgh"));
		
		// 参数校验
		if ((StringUtils.isEmpty(NBJGHValue) && StringUtils.isEmpty(JGJBValue)) || StringUtils.isEmpty(tableName)) {
			throw new Exception("权限管理-金融机构、专项日报金融机构基本信息 校验：参数异常！");
		} else {
			if (StringUtils.isNotEmpty(NBJGHValue) && StringUtils.isEmpty(sjgljgNbjgh)) {
				throw new Exception("权限管理-金融机构、专项日报金融机构基本信息 校验：参数异常！");
			}
		}
		
		// 只校验HBJYODS.JRJGXX与SPECIAL_FINF
		if (!"HBJYODS.JRJGXX".equals(tableName) && !"SPECIAL_FINF".equals(tableName)) {
			return null;
		}
		
		// 校验规则
		// 1、不能新增与当前用户同级的机构
		if (StringUtils.isNotEmpty(JGJBValue) && StringUtils.parseString(JGJBValue).equals(request.getSession().getAttribute("level"))) {
			return "您无权添加与您的机构同级的机构";
		}
		// 2、内部机构号不应该与上级管理机构内部机构号相同
		if (StringUtils.isNotEmpty(NBJGHValue)) {
			if (!NBJGHValue.equals(sjgljgNbjgh)) {
				// 3、JRJGXX表内部机构号不应该已存在
				if("HBJYODS.JRJGXX".equals(tableName)){
					tempParams.put("BR_NO_equal", NBJGHValue);
					jrjgDateCount = manaProService.getDataCount("getJrjgxxDataCount", tempParams);
					if (jrjgDateCount == null || jrjgDateCount.isEmpty() || StringUtils.isNotEmpty(jrjgDateCount.get(0).get("count")) 
							|| Integer.parseInt(jrjgDateCount.get(0).get("COUNT").toString()) <= 0) {
						return null;
					} else {
						return "当前内部机构号对应的机构已存在";
					}
				} else {
					return null;
				}
			} else {
				return "内部机构号不能与上级内部机构号相同";
			}
		} else {
			return null;
		}
	}
	
//	public static void main(String[] args) throws Exception{
//	}
}
