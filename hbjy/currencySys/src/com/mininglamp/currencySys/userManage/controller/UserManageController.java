package com.mininglamp.currencySys.userManage.controller;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mininglamp.currencySys.common.base.bean.ResponseBean;
import com.mininglamp.currencySys.common.base.controller.BaseController;
import com.mininglamp.currencySys.common.util.StringUtils;
import com.mininglamp.currencySys.regulationFile.service.RegulationService;
import com.mininglamp.currencySys.user.service.UserService;
import com.mininglamp.currencySys.userManage.service.UserManageService;
import com.mininglamp.currencySys.wildCatReport.service.WildCatReportService;

import net.sf.json.JSONArray;
/**
 * @author zhangpeng
 *
 */
@Controller
@RequestMapping("userManage")
public class UserManageController extends BaseController{
	//注入service对象
	@Autowired
	UserManageService userManageService;
	@Autowired
	UserService userService;
	@Autowired
	RegulationService regulationService;
	@Autowired
	WildCatReportService wildCatReportService;
	
	/**
	 * 用户信息管理：获取DB数据
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/userManageSearch")
	@ResponseBody
	public ResponseBean userManageSearch(HttpServletRequest request,HttpServletResponse response) throws Exception {
		// 获取参数
		Map<String, Object> params = getParamters(request);
		ResponseBean result = new ResponseBean();
		Map dataMap = new HashMap();
		String nbjgh = (String) request.getSession().getAttribute("nbjgh");
		String username = (String) request.getSession().getAttribute("username");
		if(!"admin".equals(username)){
			params.put("nbjgh", nbjgh);
			params.put("username", username);
		}
		//获取数据:用户信息列表
		List<Map> dataList = userManageService.getDataList(params, "getUserManageInfo");
//		//查询单位码表
//		List<Map> dwCodeList = userManageService.getDataList(params, "queryDwCode");
		dataMap.put("dataList", dataList);
//		dataMap.put("dwCodeList", dwCodeList);
		result.setData(dataMap);
		return result;
	}
	
	/**
	 * 用户信息管理：新增数据insert
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/userManageInsert")
	@ResponseBody
	public ResponseBean userManageInsert(HttpServletRequest request,HttpServletResponse response) throws Exception {
		// 获取参数
		Map<String, Object> params = getParamters(request);
		//对密码进行MD5加密
		String MD5Value = UserManageController.encodeMD5((String) params.get("yhPWD"));
		params.put("yhPWD", MD5Value);
		ResponseBean result = new ResponseBean();
		Map dataMap = new HashMap();
		//插入数据:用户信息列表
		int insertTag = userManageService.insertData(params, "insertUserInfo");
		dataMap.put("insertTag", insertTag);
		result.setData(dataMap);
		return result;
	}
	
	/**
	 * 用户信息管理：修改数据update
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/userManageUpdate")
	@ResponseBody
	public ResponseBean userManageUpdate(HttpServletRequest request,HttpServletResponse response) throws Exception {
		// 获取参数
		Map<String, Object> params = getParamters(request);
		//判断是否需要对密码进行加密
		String pwdIsOld = (String) params.get("pwdIsOld");
		if(pwdIsOld.equals("1")){
			//对密码进行MD5加密
			String MD5Value = UserManageController.encodeMD5((String) params.get("yhPWD"));
			params.put("yhPWD", MD5Value);
		}
		ResponseBean result = new ResponseBean();
		Map dataMap = new HashMap();
		//插入数据:用户信息列表
		int updateTag = userManageService.updateData(params, "updateUserInfo");
		dataMap.put("updateTag", updateTag);
		result.setData(dataMap);
		return result;
	}
	
	/**
	 * 用户信息管理：删除数据Delete
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/userManageDelete")
	@ResponseBody
	public ResponseBean userManageDelete(HttpServletRequest request,HttpServletResponse response) throws Exception {
		// 获取参数
		Map<String, Object> params = getParamters(request);
		ResponseBean result = new ResponseBean();
		Map dataMap = new HashMap();
		//插入数据:用户信息列表
		int deleteTag = userManageService.deleteData(params, "deleteUserInfo");
		dataMap.put("deleteTag", deleteTag);
		result.setData(dataMap);
		return result;
	}
	
	/**
	 * 用户信息管理
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getUserAuthority")
	@ResponseBody
	public ResponseBean getUserAuthority(HttpServletRequest request,HttpServletResponse response) throws Exception {
		// 获取参数
		Map<String, Object> params = getParamters(request);
		ResponseBean result = new ResponseBean();
		String userName = (String) params.get("userName");
		String authority = (String) request.getSession().getAttribute("authority");
		if(authority != null && !"all".equals(authority)){
			String[] split = authority.split(",");
			params.put("authority", split);
		}
		// 仅机构级别1、2、3级被分配权限管理页面权限，4、5级不可，即取得可分配权限时，仅1、2级可取得权限管理页面权限
		Map<String, Object> userManageParams = getParamters(request);
		userManageParams.put("yhName", request.getSession().getAttribute("username"));
		List<Map> userManageInfoList = userManageService.getDataList(userManageParams, "getUserManageInfo");
		if (userManageInfoList != null && !userManageInfoList.isEmpty()) {
			String jgjb = StringUtils.parseString(userManageInfoList.get(0).get("DWJB"));
			if ("3".equals(jgjb) || "4".equals(jgjb) || "5".equals(jgjb)) {
				params.put("notAllowAuthority", "yes");
			}
		}
		
		List<Map> list=userManageService.getDataList(params, "getUserAuthority");
		result.setData(list);
		return result;
	}
	
	/**
	 * 用户信息管理：重置密码数据
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/userManageRecharge")
	@ResponseBody
	public ResponseBean getUserManageRecharge(HttpServletRequest request,HttpServletResponse response) throws Exception {
		// 获取参数
		Map<String, Object> params = getParamters(request);
		ResponseBean result = new ResponseBean();
		JSONArray fromObject = JSONArray.fromObject(params.get("rechargeData"));
		params.put("rechargeData", fromObject);
		params.put("PASSWORD", UserManageController.encodeMD5("123456"));
		int updateData = userManageService.updateData(params, "getUserManageRecharge");
		Map dataMap = new HashMap();
		dataMap.put("deleteTag", updateData);
		result.setData(dataMap);
		return result;
	}
	
	/**
	 * 用户信息管理 获取机构树形
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/queryDwCode")
	@ResponseBody
	public ResponseBean queryDwCode(HttpServletRequest request,HttpServletResponse response) throws Exception {
		// 获取参数
		Map<String, Object> params = getParamters(request);
		ResponseBean result = new ResponseBean();
		String nbjgh = (String) request.getSession().getAttribute("nbjgh");
		String level = (String) request.getSession().getAttribute("level");
		if (null==level) {
			level="0";
		}
		String levelTwo=(Integer.parseInt(level)+1)+"";
		String levelThree=(Integer.parseInt(level)+2)+"";
		params.put("nbjgh", nbjgh);
		params.put("level", level);
		params.put("levelTwo", levelTwo);
		params.put("levelThree", levelThree);
		List<Map> dwCodeList = userManageService.getDataList(params, "queryDwCode");
		result.setData(dwCodeList);
		return result;
	}
	
	/**
	 * MD5加密方法
	 * @param yhPWD
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public static String encodeMD5(String yhPWD) {
		try {  
            // 生成一个MD5加密计算摘要  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            // 计算md5函数  
            md.update(yhPWD.getBytes());  
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符  
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值  
            return new BigInteger(1, md.digest()).toString(16);  
        } catch (Exception e) {  
           e.printStackTrace();  
           return null;  
        }  
	}
	
	/**
	 * 个人信息修改
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updateUser")
	@ResponseBody
	public String updateUser(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws Exception {
		// 获取参数
		Map<String, Object> params = getParamters(request);
		try {
			if(params.get("xmm")!=null && !params.get("xmm").equals("")) {
				params.put("xmm", encodeMD5(params.get("xmm").toString()));
			}
			params.put("zh", session.getAttribute("username"));
			userManageService.updateData(params, "updateUser");
			session.setAttribute("name", params.get("xm"));
			session.setAttribute("userssbm", params.get("bm"));
			session.setAttribute("userteleph", params.get("dh"));
		} catch (Exception e) {
			//e.printStackTrace();
			return "no";
		}
		return "ok";
	}
	
	/**
	 * 个人信息查询
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getUser")
	@ResponseBody
	public Map<String,Object> getUser(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws Exception {
		// 获取参数
		Map<String, Object> params = getParamters(request);
		Map<String,Object> map=null;
		try {
			List<Map> dataList = userManageService.getDataList(params, "getUser");
			map = dataList.get(0);
		} catch (Exception e) {
			//e.printStackTrace();
			return map;
		}
		return map;
	}
	
	/**
	 * 根据用户名查询用户的现金从业人员信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getUserPrsnInfoByUsername")
	@ResponseBody
	public Map<String,Object> getUserPrsnInfoByUsername(HttpServletRequest request,HttpServletResponse response) throws Exception {
		// 获取参数
		Map<String, Object> params = getParamters(request);
		Map<String,Object> map=null;
		try {
			List<Map> dataList = userService.getDataList(params, "getUserPrsnInfoByUsername");
			map = dataList.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return map;
		}
		return map;
	}
	
	/**
	 * 保存用户的现金从业人员信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saveUserPrsnInfo")
	@ResponseBody
	public ResponseBean saveUserPrsnInfo(HttpServletRequest request,HttpServletResponse response) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		ResponseBean responseBean = new ResponseBean();
		Map<String, Object> responseParams = new HashMap<String, Object>();
		responseParams.put("result", true);
		responseParams.put("msg", "保存成功！");
		responseBean.setData(responseParams);
		// 获取参数
		Map<String, Object> params = getParamters(request);
		Map<String, Object> saveUserPrsnParams = new HashMap<String, Object>();
		boolean flg = true;
		try {
			saveUserPrsnParams.put("SJRQ", sdf.format(new Date()));
			saveUserPrsnParams.put("NAME", params.get("name") != null ? params.get("name") : "");
			String sex = "";
			if (params.get("sex") != null) {
				if ("1".equals(params.get("sex").toString())) {
					sex = "男";
				} else if ("2".equals(params.get("sex").toString())) {
					sex = "女";
				} else {
					sex = params.get("sex").toString();
				}
			}
			saveUserPrsnParams.put("SEX", sex);
			saveUserPrsnParams.put("CARD_ID", params.get("cardNum") != null ? params.get("cardNum") : "");
			params.put("publishRange", params.get("nbjgh"));
			List<Map> dataList = regulationService.getDataList("selectJgName", params);
			if (dataList != null && !dataList.isEmpty()) {
				saveUserPrsnParams.put("UNIT", dataList.get(0).get("JRJGMC") != null ? dataList.get(0).get("JRJGMC") : "");
				saveUserPrsnParams.put("SSDWTYXXDM", dataList.get(0).get("TYSHXYDM") != null ? dataList.get(0).get("TYSHXYDM") : "");
				saveUserPrsnParams.put("DWJRJGBM", dataList.get(0).get("JRJGBM"));
			} else {
				saveUserPrsnParams.put("UNIT", "");
				saveUserPrsnParams.put("SSDWTYXXDM", "");
			}
			saveUserPrsnParams.put("DWNBJGH", params.get("nbjgh"));
			saveUserPrsnParams.put("GW", params.get("gw") != null ? params.get("gw") : "0");// 岗位:0-管理岗、1-柜员、2-清分人员，默认[管理岗]
			saveUserPrsnParams.put("SFQDHGZS", params.get("sfqdhgzs") != null ? params.get("sfqdhgzs") : "0");// 是否取得反假币培训合格证书:0-否、1-是，默认[否]
			saveUserPrsnParams.put("QDZSSJ", params.get("qdzssj"));
			saveUserPrsnParams.put("ZSSXSJ", params.get("zssxsj"));
			saveUserPrsnParams.put("DQZT", "1");// 当前状态:1-正常从事该工作、2-在本行内不再从事该工作、3-离职，默认[正常从事该工作]
			// 保存现金从业人员信息(若更新失败再保存)
			flg = wildCatReportService.updateData(saveUserPrsnParams, "prsnUpdateDataByCardId") > 0;
			if (!flg) {
				flg = wildCatReportService.insertData(saveUserPrsnParams, "prsnInsertData") > 0;
			}
			// 保存系统用户信息
			if (flg) {
				saveUserPrsnParams.clear();
				saveUserPrsnParams.put("xm", params.get("name") != null ? params.get("name") : "");
				saveUserPrsnParams.put("dh", params.get("teleph") != null ? params.get("teleph") : "");
				saveUserPrsnParams.put("dwbm", params.get("nbjgh"));
				saveUserPrsnParams.put("zh", params.get("yhName"));
				flg = userManageService.updateData(saveUserPrsnParams, "updateUser") > 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			flg = false;
		}
		if (!flg) {
			responseParams.put("result", false);
			responseParams.put("msg", "保存失败，请联系管理员！");
		} else {
			// 从业人员信息输入是在用户第一次登录时保存的，此时要更新用户的登录次数
			if (Integer.parseInt(com.mininglamp.currencySys.common.util.StringUtils.parseString(request.getSession().getAttribute("login_times"))) == 0) {
				params.put("username", request.getSession().getAttribute("username"));
				userService.updateData(params, "updateLoginTimes");
			}
		}
		return responseBean;
	}
	
	/**
	 * 根据内部机构号获取下级机构信息tree
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/selectSubordinateOrgTree")
	@ResponseBody
	public List<Map> selectSubordinateOrgTree(HttpServletRequest request,HttpServletResponse response) throws Exception {
		// 获取参数
		Map<String, Object> params = getParamters(request);
		try {
			List<Map> dataList = userService.getDataList(params, "selectSubordinateOrgTree");
			if (dataList != null && !dataList.isEmpty()) {
				return dataList; 
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Map>();
		}
		return new ArrayList<Map>();
	}
	
	/**
	 * 根据内部机构号获取机构名称
	 * @param request (publishRange:内部机构号)
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/selectJgName")
	@ResponseBody
	public List<Map> selectJgName(HttpServletRequest request,HttpServletResponse response) throws Exception {
		// 获取参数
		Map<String, Object> params = getParamters(request);
		try {
			List<Map> dataList = regulationService.getDataList("selectJgName", params);
			if (dataList != null && !dataList.isEmpty()) {
				return dataList; 
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Map>();
		}
		return new ArrayList<Map>();
	}
	/**
	 * 根据名称获取机构级别
	 * @param request 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/selectJgJb")
	@ResponseBody
	public ResponseBean selectJgJb(HttpServletRequest request,HttpServletResponse response) throws Exception {
		// 获取参数
		ResponseBean responseBean = new ResponseBean();
		Map<String, Object> params = getParamters(request);
		Map<String, Object> responseParams = new HashMap<String, Object>();
		
		try {
			List<Map> dataList = userManageService.getDataList(params, "selectJgJb");
			if (dataList != null && !dataList.isEmpty()) {
				responseParams.put("data",dataList.get(0).get("JGJB"));
				responseParams.put("msg", "查询成功！");
				responseBean.setData(responseParams);
				return responseBean; 
			}
		} catch (Exception e) {
			e.printStackTrace();
			return responseBean;
		}
		return responseBean;
	}
}
