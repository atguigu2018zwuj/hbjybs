package com.mininglamp.currencySys.common.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;
/**
 * jsonp通用处理
 * @author czy
 * 2016年9月21日16:30:52
 */
@ControllerAdvice(basePackages="com.mininglamp.mws")
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice{
	public JsonpAdvice() {
		super("callback","jsonp");
	}
}
