package com.mininglamp.currencySys.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;

import com.mininglamp.logx.kit.JsonKit;
import com.mininglamp.currencySys.common.base.bean.ResponseBean;


/**
 * 统一请求参数和结果日志记录
 * @author czy
 * 2016年10月9日10:54:48
 */
public class ActionLogAspect {
	
	public ResponseBean log(ProceedingJoinPoint jp) throws java.lang.Throwable{
		Object[] args=jp.getArgs();
		if(args.length > 0)
			System.out.println(JsonKit.toJson(args[0]));
		ResponseBean bean=(ResponseBean) jp.proceed(args);
		System.out.println(JsonKit.toJson(bean));
		return bean;
	}
}
