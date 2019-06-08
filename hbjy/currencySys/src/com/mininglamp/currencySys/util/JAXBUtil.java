package com.mininglamp.currencySys.util;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
/**
 * JAXB工具
 * @author czy
 * 2015年6月4日18:34:37
 *
 */
public class JAXBUtil {
	@SuppressWarnings("unchecked")
	public static <T> T toObject(Class<T> clazz,String xmlString) throws JAXBException, SAXException, ParserConfigurationException{
		if(StringUtils.isBlank(xmlString)) return null;
		JAXBContext ctx=JAXBContext.newInstance(clazz);
		Unmarshaller um=ctx.createUnmarshaller();
		StringReader reader=new StringReader(xmlString);
		SAXParserFactory sax=SAXParserFactory.newInstance();
		sax.setNamespaceAware(false);
		XMLReader xmlReader=sax.newSAXParser().getXMLReader();
		Source source=new SAXSource(xmlReader, new InputSource(reader));
		
		T t=null;
		try {
			t = (T) um.unmarshal(source);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("ErrXML:"+xmlString);
		}
		return t;
	}
	
}
