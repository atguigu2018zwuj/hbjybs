package com.mininglamp.currencySys.common.aop;

import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;

import com.mininglamp.currencySys.common.base.bean.PageBean;


/**
 * 统一切面 用于处理返回结果
 * @author czy
 * 2015年12月29日10:53:06
 */
@SuppressWarnings("unchecked")
public class ObjectTranslateAspect {
	/**
	 * 通用列表数据格式转换，参数格式必须为第一个对应transcode
	 * @param jp
	 * @return
	 * @throws java.lang.Throwable
	 */
	public Object changeValue(ProceedingJoinPoint jp) throws java.lang.Throwable{
		Object[] args=jp.getArgs();
		List list=(List) jp.proceed(args);
		if(args.length>0&&args[0] instanceof String){
			/*
			 * service返回controller参数格式转换用
			 */
//			String key=(String) jp.getArgs()[0];
//			translate(list,key);
		}
		return list;
	}
	
	
	/**
	 * 通用列表数据格式转换，参数格式必须为第一个对应transcode
	 * @param jp
	 * @return
	 * @throws java.lang.Throwable
	 */
	public Object changeMapValue(ProceedingJoinPoint jp) throws java.lang.Throwable{
		Object[] args=jp.getArgs();
		Map map=(Map) jp.proceed(args);
		if(args.length>0&&args[0] instanceof String){
			String key=(String) jp.getArgs()[0];
			if(map != null){
				ObjectTranscoder.transcodeMap(key,map);
			}
		}
		return map;
	}
	/**
	 * 通用分页列表数据格式转换，参数格式必须为第二个对应PageBean
	 * @param jp
	 * @return
	 * @throws java.lang.Throwable
	 */
	public Object change4findValuePage(ProceedingJoinPoint jp) throws java.lang.Throwable{
		Object[] args=jp.getArgs();
		List list=(List) jp.proceed(jp.getArgs());
		if(args.length>1 && jp.getArgs()[1] instanceof PageBean){
			PageBean page=(PageBean) jp.getArgs()[1];
			String key=page.getTranscodeId();
			if(key != null)
				translate(list,key);
		}
		return list;
	}
	/**
	 * 格式转换
	 * @param list
	 * @param key
	 */
	private void translate(List list,String key){
		if(list!=null&&key!=null){
			for(Object t:list){
				if(t instanceof java.util.Map){
					ObjectTranscoder.transcodeMap(key, (Map)t);
				}else{
					ObjectTranscoder.transcode(t);
				}
			}
		}
	}
}
