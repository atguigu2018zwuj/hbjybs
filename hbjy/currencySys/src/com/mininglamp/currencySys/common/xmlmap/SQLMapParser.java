package com.mininglamp.currencySys.common.xmlmap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.google.common.io.Resources;
import com.mininglamp.currencySys.common.xmlmap.bean.BatisConfiguration;
import com.mininglamp.currencySys.common.xmlmap.bean.Prop;
import com.mininglamp.currencySys.common.xmlmap.bean.ResultMap;
import com.mininglamp.currencySys.common.xmlmap.bean.SQLBean;
import com.mininglamp.currencySys.common.xmlmap.bean.SQLCriteria;
import com.mininglamp.currencySys.common.xmlmap.exception.ConfigParseException;

/***********************
 * 
 * @author liuzhenhua
 * @date 20150914 把 sqlmap配置文件解析成 对象
 * 
 */
public class SQLMapParser {
	private Logger logger = Logger.getLogger(SQLMapParser.class);
	// 总配置文件对象
	private BatisConfiguration conf = null;
	private SAXReader reader = null;
	private String xmlpath;

	public SQLMapParser(String xmlpath) {
		conf = new BatisConfiguration();
		reader = new SAXReader();
		this.xmlpath = xmlpath;

		try {
			List<String> paths = this.parseConfigPath(xmlpath);
			for (String path : paths)
				this.parse(path);
			logger.info("Impala 查询的初始文件" + xmlpath + "解析完毕");
		} catch (ConfigParseException e) {
			logger.error("XmlMapParser类SQLMapParser方法发生异常！");
			e.printStackTrace();
		}
	}

	public BatisConfiguration getConf() {
		return conf;
	}

	@SuppressWarnings("unchecked")
	private List<String> parseConfigPath(String configPath) {
		List<String> paths = new ArrayList<String>();
		try {
			if (configPath == null || "".equals(configPath.trim()))
				configPath = "ibatis/map/default-map.xml";
			Document doc = reader.read(Resources.getResource(configPath).openStream());
			Element root = doc.getRootElement();
			List<Element> elements = root.elements();

			for (Element e : elements) {
				if ("include".equals(e.getName())) {
					paths.add(e.getTextTrim());
				}
			}
		} catch (DocumentException e) {
			logger.error("XmlMapParser类parseConfigPath方法发生异常！");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("XmlMapParser类parseConfigPath方法发生异常！");
			e.printStackTrace();
		}
		return paths;
	}

	@SuppressWarnings("unchecked")
	private void parse(String path) throws ConfigParseException {
		try {
			// String parent
			// =this.getClass().getClassLoader().getResource("").toURI().getPath();
			// logger.info("配置文件的父路径: "+parent);

			Document doc = reader.read(Resources.getResource(path/* "ibatis/map/empmonitor-ibatis-new.xml" */).openStream());
			Element root = doc.getRootElement();

			List<Element> elements = (List<Element>) root.elements();
			for (Element e : elements) {
				if ("select".equals(e.getName())) {
					SQLBean bean = new SQLBean();
					String queryType = e.getName();

					Iterator<Attribute> attrs = e.attributeIterator();
					while (attrs.hasNext()) {
						Attribute a = attrs.next();

						if ("id".equals(a.getName())) {
							bean.setSqlid(a.getText());
						} else if ("resultMap".equals(a.getName())) {
							bean.setResultMap(a.getText());
						} else if ("parameter".equals(a.getName())) {
							bean.setParamType(a.getText());
						}
					}
					Iterator<Element> eles = e.elementIterator();
					while (eles.hasNext()) {
						SQLCriteria criteria = new SQLCriteria();
						Element el = eles.next();
						criteria.setCriteriaType(el.getName());

						if ("content".equals(el.getName())) {
							criteria.setContent(el.getText());
							bean.getSqlCriterias().add(criteria);
							continue;
						} else if ("if".equals(el.getName())) {
							criteria.setContent(el.getText());
							Iterator<Attribute> ifattrs = el.attributeIterator();
							while (ifattrs.hasNext()) {
								Attribute attr = ifattrs.next();

								if ("test".equals(attr.getName()))
									criteria.setField(attr.getText());
								else if ("value".equals(attr.getName()))
									criteria.setValue(attr.getText());
							}
							bean.getSqlCriterias().add(criteria);
						}
					}

					bean.setQueryType(queryType);

					conf.addSQLBean(bean.getSqlid(), bean);
				} else if ("mapper".equals(e.getName())) {
					ResultMap rmap = new ResultMap();
					Iterator<Attribute> attrs = e.attributeIterator();
					while (attrs.hasNext()) {
						Attribute attr = attrs.next();
						if ("id".equals(attr.getName()))
							rmap.setId(attr.getText());
						else if ("class".equals(attr.getName())) {
							rmap.setClazz(attr.getText());
						}
					}
					List<Element> mappers = e.elements();
					for (Element mapper : mappers) {
						if ("resultmap".equals(mapper.getName())) {
							Iterator<Attribute> mapper_attrs = mapper.attributeIterator();
							Prop prop = new Prop();
							while (mapper_attrs.hasNext()) {
								Attribute att = mapper_attrs.next();

								if ("property".equals(att.getName())) {
									prop.setProperty(att.getText());
								} else if ("column".equals(att.getName())) {
									prop.setColumn(att.getText());
								} else if ("columntype".equals(att.getName())) {
									prop.setColumnType(att.getText());
								}

							}
							rmap.addMapper(prop.getColumn(), prop);
						}
					}
					conf.addResultMap(rmap.getId(), rmap);
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new ConfigParseException("在解析配置文件时发生错误： " + path, e);
		} catch (IOException e1) {
			e1.printStackTrace();
			logger.error("XmlMapParser类parse方法发生异常！");
		} finally {

		}

	}

	public String getXmlpath() {
		return xmlpath;
	}

	public void setXmlpath(String xmlpath) {
		this.xmlpath = xmlpath;
	}

}
