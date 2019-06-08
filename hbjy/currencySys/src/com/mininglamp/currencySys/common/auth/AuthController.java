package com.mininglamp.currencySys.common.auth;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mininglamp.currencySys.common.base.CodeConst;
import com.mininglamp.currencySys.common.base.bean.ResponseBean;
import com.mininglamp.currencySys.regulationFile.service.RegulationService;
import com.mininglamp.currencySys.uploadAndDownload.service.impl.UploadAndAownloadServiceImp;
import com.mininglamp.currencySys.user.bean.MemoryData;
import com.mininglamp.currencySys.user.service.UserService;
import com.mininglamp.currencySys.util.SecurityUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import net.sf.json.JSONObject;
/**
 * 权限认证控制器
 * 所有方法必须支持GET，用于跨域请求
 * @author czy
 * 2016年9月21日16:36:16
 */
@Controller
@Api(position=0,value="权限认证")
public class AuthController {
	
	private NamedThreadLocal<Long>  startTimeThreadLocal = new NamedThreadLocal<Long>("StopWatch-StartTime");  
	private String provincialUsers = null;//获取省级用户的内部机构号
	
	@Autowired
	private UserService userService;
	@Autowired
	RegulationService regulationService;
	
	//首页
	@RequestMapping("index")
	public String bankIndex() {
		return "/bankIndex";
	}
		
	@RequestMapping(value = "/login")
	public String login() {
    	return "/login";
	}
	
	//获取配置信息
	public void getprovincialUsers() {
		try {
			Properties prop = new Properties();
			//Class.getResourceAsStream("name") 会指定要加载的资源路径与当前类所在包的路径一致 如果这个name是以 '/' 开头的，那么就会从classpath的根路径下开始查找。
			//getClassLoader().getResourceAsStream("name")  无论要查找的资源前面是否带'/' 都会从classpath的根路径下查找。
			prop.load(UploadAndAownloadServiceImp.class.getClassLoader().getResourceAsStream("ibatis/jdbc.properties"));
			provincialUsers = prop.getProperty("provincial.users");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/signIn",method={RequestMethod.POST,RequestMethod.GET},produces="application/json; charset=UTF-8")
	@ApiOperation(value = "登陆",notes="用于登陆认证获取本系统的访问权限",httpMethod="POST")
	public ResponseBean login(@ApiParam(name="id")String id,@ApiParam(name="password")String password,@ApiParam(name="loginfla")String loginfla,HttpServletRequest request,HttpServletResponse response) throws Exception{
		ResponseBean bean = new ResponseBean();
		HttpSession session = request.getSession();
		/**
		 * 登陆逻辑控制
		 */
		if(StringUtils.isNoneBlank(id) && StringUtils.isNoneBlank(password)){
			Map<String, String> params = new HashMap<String, String>();
			params.put("loginName", id);
			List<Map> userList = userService.getUserList(params, "getUsermanager");
			if(userList != null && !userList.isEmpty()){
				if(("null".equals(userList.get(0).get("NBJGH")) || userList.get(0).get("NBJGH") == null) && !"admin".equals(id)){
					bean.setCode(CodeConst.CODE_FAIL_NBJGH);
					bean.setMessage(CodeConst.msgMap.get(CodeConst.CODE_FAIL_NBJGH));
				}else{
					String pwd = (String) userList.get(0).get("YH_PWD");
					if(pwd.equals(encodeMD5(password))){
						String sessionID = session.getId();
						//记录sessionID 来判断是同一账号只能一处登陆
						if (!MemoryData.getSessionIDMap().containsKey(id)) { //不存在，首次登陆，放入Map
							MemoryData.getSessionIDMap().put(id, sessionID);
						}else if(MemoryData.getSessionIDMap().containsKey(id)&&!StringUtils.equals(sessionID, MemoryData.getSessionIDMap().get(id))){
							if("ok".equals(loginfla)){
								session.removeAttribute("errorLogs");
								//保存用户信息
								session.removeAttribute("username");
								//省级用户的内部机构号
								session.removeAttribute("provincialUsers");
								session.removeAttribute("sessionID");
								session.removeAttribute("userid");
								session.removeAttribute("authority");
								session.removeAttribute("name");
								session.removeAttribute("userteleph");
								session.removeAttribute("userszdwbm");
								session.removeAttribute("userszdwmc");
								session.removeAttribute("userssbm");
								session.removeAttribute("nbjgh");
								session.removeAttribute("userszdwjb");
								session.removeAttribute("authority");
								session.removeAttribute("login_times");
								MemoryData.getSessionIDMap().remove(id);
								MemoryData.getSessionIDMap().put(id, sessionID);
							}else{
								bean.setCode(CodeConst.CODE_CONFIRM_LOGIN);
								bean.setMessage(CodeConst.msgMap.get(CodeConst.CODE_CONFIRM_LOGIN));
							}
						}
					
						//更新token
						String token = SecurityUtil.updateToken(request);
						response.addCookie(new Cookie("Mws-Token", token));
						bean.setData(token);
						
						//获取配置信息
						getprovincialUsers();
						Map<String,Object> map=new HashMap<>();
						map.put("publishRange", userList.get(0).get("NBJGH"));
						List<Map> listMap=regulationService.getDataList("selectBrNoList",map);
						List<Map> listMapTwo=regulationService.getDataList("selectLSBrNoList",map);
						if (listMap.size()!=0) {
							session.setAttribute("snsb","cz");
						}
						if (listMapTwo.size()!=0) {
							session.setAttribute("snls","cz");
						}
						//保存用户信息
						session.setAttribute("username", id);
						//省级用户的内部机构号
						session.setAttribute("provincialUsers", provincialUsers);
						session.setAttribute("sessionID", sessionID);
						session.setAttribute("userid", userList.get(0).get("YH_PWD"));
						session.setAttribute("authority", userList.get(0).get("AUTHORITY"));
						session.setAttribute("name", userList.get(0).get("NAME"));
						session.setAttribute("userteleph", userList.get(0).get("TELEPH"));
						session.setAttribute("userszdwbm", userList.get(0).get("DWBM"));
						session.setAttribute("userszdwmc", userList.get(0).get("DW_NAME"));
						session.setAttribute("userssbm", userList.get(0).get("SSBM"));
						session.setAttribute("nbjgh", userList.get(0).get("NBJGH"));
						session.setAttribute("userszdwjb", userList.get(0).get("DW_JB"));
						session.setAttribute("level", userList.get(0).get("ALEVEL"));
						session.setAttribute("teleph", userList.get(0).get("TELEPH"));
						session.setAttribute("yh_name", userList.get(0).get("YH_NAME"));
						session.setAttribute("login_times", userList.get(0).get("LOGIN_TIMES"));
						session.setAttribute("ssjg", userList.get(0).get("SSJG"));
						session.setAttribute("dwcode", userList.get(0).get("DW_CODE"));
						// 将session信息同步到前台cookie
						response.addCookie(new Cookie("userInfo", JSONObject.fromObject(userList.get(0)).toString()));
						
						// 更新用户的登录次数（若是第一次登录，则不在此处更新，在保存现金从业人员时更新）
						if (Integer.parseInt(com.mininglamp.currencySys.common.util.StringUtils.parseString(userList.get(0).get("LOGIN_TIMES"))) > 0) {
							params.put("username", id);
							userService.updateData(params, "updateLoginTimes");
						}
						
					} else {
						bean.setCode(CodeConst.CODE_FAIL_LOGIN_ID_PWD);
						bean.setMessage(CodeConst.msgMap.get(CodeConst.CODE_FAIL_LOGIN_ID_PWD));
					}
				}
			} else {
				bean.setCode(CodeConst.CODE_FAIL_LOGIN_ID_PWD);
				bean.setMessage(CodeConst.msgMap.get(CodeConst.CODE_FAIL_LOGIN_ID_PWD));
			}
		}else{
			bean.setCode(CodeConst.CODE_FAIL_LOGIN_ID_PWD);
			bean.setMessage(CodeConst.msgMap.get(CodeConst.CODE_FAIL_LOGIN_ID_PWD));
		}
		
		return bean;
	}
	
	@RequestMapping(value="/logout",method={RequestMethod.POST,RequestMethod.GET})
	public void logout(HttpServletRequest request,HttpServletResponse response){
		HttpSession session = request.getSession();
		String SessionId = session.getId();
		String username = (String) session.getAttribute("username");
		String LoginSessionId = MemoryData.getSessionIDMap().get(username);
		//判断SessionId是否相同 相同侧说明同一用户 
		if(SessionId.equals(LoginSessionId)){
			MemoryData.getSessionIDMap().remove(username);
			session.invalidate();//清除 session 中的所有信xi
		}
		// 防止缓存           
		//Forces caches to obtain a new copy of the page from the origin server    
		response.setHeader("Cache-Control","no-cache");     
		//Directs caches not to store the page under any circumstance    
		response.setHeader("Cache-Control","no-store");     
		//HTTP 1.0 backward compatibility     
		response.setHeader("Pragma","no-cache");     
		//Causes the proxy cache to see the page as "stale"    
		response.setDateHeader("Expires", 0); 
		//return "/login";
		try {
			response.sendRedirect(request.getContextPath() + "/login");
		} catch (Exception e) {
			e.printStackTrace();
		}  
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
	 * 获取ip地址
	 * @param request
	 * @return
	 */
	public static String IpAndSource(HttpServletRequest request) {
		/* 需求：记录访客信息，访客IP、来源 */
		String ipAddress = null;
		if (request.getHeader("x-forwarded-for") == null) {
			ipAddress = request.getRemoteAddr();
		} else {
			if (request.getHeader("x-forwarded-for").length() > 15) {
				String[] aStr = request.getHeader("x-forwarded-for").split(",");
				ipAddress = aStr[0];
			} else {
				ipAddress = request.getHeader("x-forwarded-for");
			}
		}

		String terminal = request.getHeader("User-Agent");
		if (terminal.contains("Windows NT")) {
			terminal = "PC";
		} else {
			terminal = "MT";
		}
		String agent=request.getHeader("User-Agent").toLowerCase();
		String browserName = getBrowserName(agent);
		
		return ipAddress +"," + browserName+"," + terminal;
	}
	
	/**
	 * 获取浏览器
	 * @param agent
	 * @return
	 */
	public static String getBrowserName(String agent) {
		if (agent.indexOf("msie 7") > 0) {
			return "ie7";
		} else if (agent.indexOf("msie 8") > 0) {
			return "ie8";
		} else if (agent.indexOf("msie 9") > 0) {
			return "ie9";
		} else if (agent.indexOf("msie 10") > 0) {
			return "ie10";
		} else if (agent.indexOf("msie") > 0) {
			return "ie";
		} else if (agent.indexOf("opera") > 0) {
			return "opera";
		} else if (agent.indexOf("opera") > 0) {
			return "opera";
		} else if (agent.indexOf("firefox") > 0) {
			return "firefox";
		} else if (agent.indexOf("webkit") > 0) {
			return "webkit";
		} else if (agent.indexOf("gecko") > 0 && agent.indexOf("rv:11") > 0) {
			return "ie11";
		} else {
			return "Others";
		}
	}
}
