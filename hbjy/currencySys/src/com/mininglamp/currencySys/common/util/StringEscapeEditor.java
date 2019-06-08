package com.mininglamp.currencySys.common.util;

import java.beans.PropertyEditorSupport;

import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.JavaScriptUtils;
/**
 * 绑定initBinder，防止XSS共计
 * @author czy
 * 2016年9月19日15:46:01
 */
public class StringEscapeEditor extends PropertyEditorSupport{
	private boolean escapeHTML;
	private boolean escapeJavaScript;
	
	public StringEscapeEditor(){
		super();
	}
	public StringEscapeEditor(boolean escapeHTML,boolean escapeJavaScript){
		super();
		this.escapeHTML = escapeHTML;
		this.escapeJavaScript = escapeJavaScript;
	}
	@Override
	public String getAsText() {
		Object value = getValue();
		return value != null ? value.toString() : "";
	}
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if(text == null){
			setValue(null);
		}else{
			String value = text;
			if(escapeHTML){
				value = HtmlUtils.htmlEscape(value);
			}
			if(escapeJavaScript){
				value = JavaScriptUtils.javaScriptEscape(value);
			}
			setValue(value);
		}
	}
	
	
	
}
