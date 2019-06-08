package com.mininglamp.currencySys.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.xml.sax.SAXException;
/**
 * 清除sqlsession缓存并重加载
 * @author czy
 * 2016年9月27日9:54:39
 */
public class SqlSessionCache {
	
	private SqlSessionFactory oracleSqlSessionFactory;
	private Resource[] mapperLocations;
	/**
	 * 根XML
	 */
	private Resource configLocation;
	private HashMap<String,Long> fileMapping = new HashMap<String,Long>();
	
	public void refreshMapper(){
		refreshMapper(oracleSqlSessionFactory);
	}
	public String refreshOracleMapper(){
		return refreshMapper(oracleSqlSessionFactory);
	}
	public String refreshMapper(SqlSessionFactory sqlSessionFactory){
		try{
			Configuration configuration=sqlSessionFactory.getConfiguration();
			//扫描文件
			this.scanMapperXml();
			
			int changeCount = this.isChanged();
			if(changeCount > 0){
				//清理
				this.removeConfig(configuration);
				//重加载
				for(Resource mapperLocation:mapperLocations){
					//直接用Resource.getInputStream存在缓存，无法加载最新数据，通过File转换来获取输入流
					XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(new FileInputStream(mapperLocation.getFile()), configuration, mapperLocation.toString(), configuration.getSqlFragments());
					xmlMapperBuilder.parse();
				}
				return "存在修改或新增的SQL配置文件共["+changeCount+"]个，重新加载成功";
			}
			return "未发现修改的SQL配置文件";
		}catch(Exception e){
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	public void setOracleSqlSessionFactory(SqlSessionFactory oracleSqlSessionFactory) {
		this.oracleSqlSessionFactory = oracleSqlSessionFactory;
	}





	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

	/**
	 * 扫描
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private void scanMapperXml() throws IOException{
		List<Resource> resourceList = new ArrayList<Resource>();
		
		//读取根XML
		SAXReader reader = new SAXReader();
		Document doc;
		try {
			reader.setValidation(false);
			reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			doc = reader.read(configLocation.getInputStream());
			Element root = doc.getRootElement();
			Element mappers = root.element("mappers");
			List<Element> elements = mappers.elements();
			for (Element e : elements) {
				resourceList.add(new ClassPathResource(e.attributeValue("resource")));
			}
			
			this.mapperLocations = resourceList.toArray(new Resource[resourceList.size()]);
			
		} catch (DocumentException e1) {
			e1.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 清除缓存
	 */
	private void removeConfig(Configuration configuration) throws Exception{
		Class<?> classConfig = configuration.getClass();
		clearMap(classConfig, configuration,"mappedStatements");
		clearMap(classConfig, configuration,"caches");
		clearMap(classConfig, configuration,"resultMaps");
		clearMap(classConfig, configuration,"parameterMaps");
		clearMap(classConfig, configuration,"keyGenerators");
		clearMap(classConfig, configuration,"sqlFragments");
		
		clearSet(classConfig, configuration,"loadedResources");
	}
	@SuppressWarnings("unchecked")
	private void clearMap(Class<?> classConfig,Configuration configuration,String fieldName) throws Exception{
		Field field = classConfig.getDeclaredField(fieldName);
		field.setAccessible(true);
		Map mapConfig = (Map) field.get(configuration);
		mapConfig.clear();
	}
	@SuppressWarnings("unchecked")
	private void clearSet(Class<?> classConfig,Configuration configuration,String fieldName) throws Exception{
		Field field = classConfig.getDeclaredField(fieldName);
		field.setAccessible(true);
		Set setConfig = (Set) field.get(configuration);
		setConfig.clear();
	}
	/**
	 * 获取文件修改或新增个数
	 * @return
	 * @throws IOException
	 */
	private int isChanged() throws IOException{
		int changeCount = 0;
		for(Resource resource: mapperLocations){
			String resourceName = resource.getFilename();
			boolean addFlag = !fileMapping.containsKey(resourceName);
			
			Long compareFrame = fileMapping.get(resourceName);
			long lastFrame = resource.contentLength() + resource.lastModified();
			boolean modifyFlag = null!=compareFrame&&compareFrame.longValue()!=lastFrame;
			
			if(addFlag || modifyFlag){
				fileMapping.put(resourceName, Long.valueOf(lastFrame));
				changeCount++;
			}
		}
		return changeCount;
	}
	
}
