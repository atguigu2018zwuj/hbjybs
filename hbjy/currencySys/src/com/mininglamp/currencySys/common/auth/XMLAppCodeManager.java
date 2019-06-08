package com.mininglamp.currencySys.common.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.google.common.io.Resources;
import com.mininglamp.logx.kit.StrKit;
import com.mininglamp.logx.util.WebUtil;
import com.mininglamp.currencySys.common.exception.ConflictException;
/**
 * AppCode管理工具类
 * @author czy
 * 2016年9月27日19:37:14
 */
public class XMLAppCodeManager implements AppCodeManager{
	private Logger logger = Logger.getLogger(XMLAppCodeManager.class);
	private Config config;
	private List<AppCode> appcodeList;
	
	@SuppressWarnings("unchecked")
	@Override
	public void init() throws ConflictException {
		config = new Config();
		appcodeList = new ArrayList<AppCode>();
		
		//读取根XML
		SAXReader reader = new SAXReader();
		Document doc;
		try {
			reader.setValidation(false);
			doc = reader.read(Resources.getResource("appCodes.xml").openStream());
			Element root = doc.getRootElement();
			Element configElement = root.element("config");
			Element listElement = root.element("list");
			List<Element> configElementList = configElement.elements();
			List<Element> appcodeElementList = listElement.elements();
			
			for (Element e : configElementList) {
				String name = e.attributeValue("name");
				if(name.equals("enable")){
					config.setEnable("true".equals(e.getTextTrim()));
				}
			}
			for (Element e : appcodeElementList) {
				AppCode appCode = new AppCode();
				appCode.setCode(e.attributeValue("code"));
				appCode.setName(e.attributeValue("name"));
				String ip = e.attributeValue("ip");
				String ips = e.attributeValue("ips");
				
				if(ip != null && ips != null) throw new ConflictException("AppCode中不可同时配置IP和IPS");
				List<String> ipList = new ArrayList<String>();
				
				if(ip != null){
					ipList.add(ip);
				}else if(ips != null){
					String[] ipArr = ips.split(",");
					for (String string : ipArr) {
						ipList.add(string);
					}
				}
				appCode.setIps(ipList);
				appcodeList.add(appCode);
			}
			logger.info("appCode.xml加载完成");
		} catch (DocumentException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean validateAppCode(HttpServletRequest request) {
		String code = request.getHeader("Mws-Appcode");
		if(!StrKit.isBlank(code)){
			//存在appCode则验证IP
			AppCode appCode = searchAppCode(code);
			if(appCode == null) return false;
			String ip = WebUtil.getIpAdd(request);
			return ip.equals(appCode.getCode());
		}
		
		return false;
	}
	/**
	 * 查找AppCode对象
	 * @param code
	 * @return
	 */
	private AppCode searchAppCode(String code){
		if(code == null) return null;
		for(int i=0,len=appcodeList.size();i<len;i++){
			AppCode appCode = appcodeList.get(i);
			if(appCode != null && code.equals(appCode.getCode())) return appCode;
		}
		return null;
	}
	
	/**
	 * AppCode对象
	 * @author czy
	 * 2016年9月27日19:51:27
	 */
	class AppCode{
		private String code;
		private String name;
		private List<String> ips;
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public List<String> getIps() {
			return ips;
		}
		public void setIps(List<String> ips) {
			this.ips = ips;
		}
	}
	/**
	 * 配置对象
	 * @author czy
	 *
	 */
	class Config{
		private boolean enable;

		public boolean isEnable() {
			return enable;
		}

		public void setEnable(boolean enable) {
			this.enable = enable;
		}
	}
}
