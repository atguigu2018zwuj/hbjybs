package com.mininglamp.currencySys.common.aop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;

import com.mininglamp.currencySys.common.util.CommonUtil;


/**
 * 通用处理从数据库中查询的List和Map中属性_命名转换为驼峰命名
 * @author czy
 * 2016年9月28日15:18:49
 */
public class Db2BeanAspect {
	private List<String> includeMethods;
	/**
	 * 修改返回数据--针对场景模块中的返回结果为List的方法
	 * @param jp
	 * @return
	 * @throws java.lang.Throwable
	 */
	@SuppressWarnings("unchecked")
	public Object convertListProp(ProceedingJoinPoint jp) throws java.lang.Throwable{
		Object[] args=jp.getArgs();
		List list=(List) jp.proceed(args);
		//检测
		if(!isValid(jp)) return list;
		
		List newList = new ArrayList();
		if(list != null && !list.isEmpty()){
			if(list.get(0) instanceof Map){
				for (Object  t : list) {
					t = CommonUtil.convertDbPro((Map<String, Object>) t);
					newList.add(t);
				}
				list = newList;
			}
		}
		return list;
	}
	/**
	 * Map属性名转换为驼峰命名
	 * @param jp
	 * @return
	 * @throws java.lang.Throwable
	 */
	@SuppressWarnings("unchecked")
	public Object convertMapProp(ProceedingJoinPoint jp) throws java.lang.Throwable{
		Object[] args=jp.getArgs();
		Map map=(Map) jp.proceed(args);
		//检测
		if(!isValid(jp)) return map;
		
		return CommonUtil.convertDbPro(map);
	}
	public void setIncludeMethods(List<String> includeMethods) {
		this.includeMethods = includeMethods;
	}
	/**
	 * 验证是否在includeMethods中，若有则返回true，否则返回false
	 * @param jp
	 * @return 有则true,否则false
	 */
	private boolean isValid(ProceedingJoinPoint jp){
		if(includeMethods == null||includeMethods.isEmpty()) return false;
		String methodName = jp.getSignature().getName();
		if(includeMethods.contains(methodName)) return true;
		else return false;
	}
}
