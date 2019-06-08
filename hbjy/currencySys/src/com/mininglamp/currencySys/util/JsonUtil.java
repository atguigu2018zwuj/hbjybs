package com.mininglamp.currencySys.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultValueProcessor;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.JSONUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
  
/**
 * <p>Title: </p>
 * <p>Description: 服务端Json处理类</p> 
 * add by cxk 2015年3月25日10:52:19
 */
@SuppressWarnings("unchecked")
public class JsonUtil {  
	
	private static Log log = null;

	@SuppressWarnings("unused")
	private StringBuffer returnStr=new StringBuffer();
	static {
		
		setJsonDataFormat();
		log = LogFactory.getLog(JsonUtil.class);
	}
	
    public static JSONObject getJSONObject(String jsonString) {
        JSONObject jsonObject = null;   
        try {    
        	log.debug("json: " + jsonString);
            jsonObject = JSONObject.fromObject(jsonString);   
        } catch(Exception e) {   
            e.printStackTrace();   
        }
        return jsonObject;
    }

    public static JSONObject getJSONObject(String jsonString, JsonConfig jsonConfig) {
        JSONObject jsonObject = null;   
        try {    
        	log.debug("json: " + jsonString);
            jsonObject = JSONObject.fromObject(jsonString, jsonConfig);   
        } catch(Exception e) {   
            e.printStackTrace();   
        }
        return jsonObject;
    }
    
    
    /**  
     * 把数据对象转换成json字符串  
     * DTO对象形如：{"id" : idValue, "name" : nameValue, ...}  
     * 数组对象形如：[{}, {}, {}, ...]  
     * map对象形如：{key1 : {"id" : idValue, "name" : nameValue, ...}, key2 : {}, ...}  
     * @return  
     */  
    public static String getJSONString(Object object) { 
    	if ( object == null)
    	{
    		return "{}";
    	}
    		
        String jsonString = null;   

        
        if(object != null){   
            if(object instanceof Collection || object instanceof Object[]){   
                jsonString = JSONArray.fromObject(object).toString();   
            }else{   
                jsonString = JSONObject.fromObject(object).toString();   
            }   
        }   
        log.debug("json: " + jsonString);
        return jsonString == null ? "{}" : jsonString;  
    }

    
    /**  
     * 从一个JSON 对象字符格式中得到一个java对象，形如：  
     * {"id" : idValue, "name" : nameValue, "aBean" : {"aBeanId" : aBeanIdValue, ...}}  
     * @param object  
     * @param clazz  
     * @return  
     */  
    public static <T>T getDTO(String jsonString, Class<T> clazz){   
        JSONObject jsonObject = getJSONObject(jsonString);
        return  (T) JSONObject.toBean(jsonObject, clazz);   
    }   

    /**  
     * 从一个JSON 对象字符格式中得到一个java对象，形如：  
     * {"id" : idValue, "name" : nameValue, "aBean" : {"aBeanId" : aBeanIdValue, ...}}  
     * @param object  
     * @param jsonConfig  
     * @return  
     */  
    public static Object getDTO(String jsonString, JsonConfig jsonConfig){   
        JSONObject jsonObject = getJSONObject(jsonString, jsonConfig);
        return JSONObject.toBean(jsonObject, jsonConfig);   
    }
    
    /**  
     * 从一个JSON 对象字符格式中得到一个java对象，其中map是一类的集合，形如：  
     * {"id" : idValue, "name" : nameValue, "aBean" : {"aBeanId" : aBeanIdValue, ...},  
     * map:[{}, {}, ...]}  
     * @param jsonString  
     * @param clazz  
     * @param map 集合属性的类型 (key : 集合属性名, value : 集合属性类型class) eg: ("beansList" : Bean.class)  
     * @return  
     */  
    public static Object getDTO(String jsonString, Class clazz, Map map){   
    	JSONObject jsonObject = getJSONObject(jsonString); 
        return JSONObject.toBean(jsonObject, clazz, map);   
    }   
       
    /**  
     * 从一个JSON数组得到一个java对象数组，形如：  
     * [{"id" : idValue, "name" : nameValue}, {"id" : idValue, "name" : nameValue}, ...]  
     * @param jsonString  
     * @param clazz  
     * @return  
     */  
    public static Object[] getDTOArray(String jsonString, Class clazz){   
    	log.debug("json: " + jsonString);
        JSONArray array = JSONArray.fromObject(jsonString);   
        Object[] obj = new Object[array.size()];   
        for(int i = 0; i < array.size(); i++){   
            JSONObject jsonObject = array.getJSONObject(i);   
            obj[i] = JSONObject.toBean(jsonObject, clazz);   
        }   
        return obj;   
    }   
    
    /**  
     * 从一个JSON数组得到一个java对象数组，形如：  
     * [{"id" : idValue, "name" : nameValue}, {"id" : idValue, "name" : nameValue}, ...]  
     * @param jsonString  
     * @param jsonConfig  
     * @return  
     */  
    public static Object[] getDTOArray(String jsonString, JsonConfig jsonConfig){   
    	log.debug("json: " + jsonString);
        JSONArray array = JSONArray.fromObject(jsonString, jsonConfig);   
        Object[] obj = new Object[array.size()];   
        for(int i = 0; i < array.size(); i++){   
            JSONObject jsonObject = array.getJSONObject(i);   
            obj[i] = JSONObject.toBean(jsonObject, jsonConfig);   
        }   
        return obj;   
    } 
    
    /**  
     * 从一个JSON数组得到一个java对象数组，形如：  
     * [{"id" : idValue, "name" : nameValue}, {"id" : idValue, "name" : nameValue}, ...]  
     * @param jsonString  
     * @param clazz  
     * @param map  
     * @return  
     */  
    public static Object[] getDTOArray(String jsonString, Class clazz, Map map){     
    	log.debug("json: " + jsonString);
        JSONArray array = JSONArray.fromObject(jsonString);   
        Object[] obj = new Object[array.size()];   
        for(int i = 0; i < array.size(); i++){   
            JSONObject jsonObject = array.getJSONObject(i);   
            obj[i] = JSONObject.toBean(jsonObject, clazz, map);   
        }   
        return obj;   
    }   
       
    /**  
     * 从一个JSON数组得到一个java对象集合  
     * @param object  
     * @param clazz  
     * @return  
     */  
    public static List getDTOList(String jsonString, Class clazz){   
    	log.debug("json: " + jsonString);
        JSONArray array = JSONArray.fromObject(jsonString);   
        List list = new ArrayList();   
        for(Iterator iter = array.iterator(); iter.hasNext();){   
            JSONObject jsonObject = (JSONObject)iter.next();   
            list.add(JSONObject.toBean(jsonObject, clazz));   
        }   
        return list;   
    }   
    
    /**  
     * 从一个JSON数组得到一个java对象集合  
     * @param object  
     * @param jsonConfig  
     * @return  
     */ 
	public static List getDTOList(String jsonString, JsonConfig jsonConfig){   
    	log.debug("json: " + jsonString);
        JSONArray array = JSONArray.fromObject(jsonString, jsonConfig);   
        List list = new ArrayList();   
        for(Iterator iter = array.iterator(); iter.hasNext();){   
            JSONObject jsonObject = (JSONObject)iter.next();   
            list.add(JSONObject.toBean(jsonObject, jsonConfig));   
        }   
        return list;   
    } 
    
    /**  
     * 从一个JSON数组得到一个java对象集合，其中对象中包含有集合属性  
     * @param object  
     * @param clazz  
     * @param map 集合属性的类型  
     * @return  
     */  
    public static List getDTOList(String jsonString, Class clazz, Map map){    
    	log.debug("json: " + jsonString);
        JSONArray array = JSONArray.fromObject(jsonString);   
        List list = new ArrayList();   
        for(Iterator iter = array.iterator(); iter.hasNext();){   
            JSONObject jsonObject = (JSONObject)iter.next();   
            list.add(JSONObject.toBean(jsonObject, clazz, map));   
        }   
        return list;   
    }   
       
    /**  
     * 从json HASH表达式中获取一个map，该map支持嵌套功能  
     * 形如：{"id" : "johncon", "name" : "小强"}  
     * 注意commons-collections版本，必须包含org.apache.commons.collections.map.MultiKeyMap  
     * @param jsonString  
     * @return  
     */
    public static Map getMapFromJson(String jsonString) {     
    	JSONObject jsonObject = getJSONObject(jsonString); 
    	return convertJSONObject2Map(jsonObject); 
    }   
    
    /**  
     * 从json HASH表达式中获取一个map，该map支持嵌套功能  
     * 形如：{"id" : "johncon", "name" : "小强"}  
     * 注意commons-collections版本，必须包含org.apache.commons.collections.map.MultiKeyMap  
     * @param jsonString  
     * @param jsonConfig 
     * @return  
     */ 
    public static Map getMapFromJson(String jsonString, JsonConfig jsonConfig) {     
    	JSONObject jsonObject = getJSONObject(jsonString, jsonConfig);   
        return convertJSONObject2Map(jsonObject);   
    } 

    private static Map convertJSONObject2Map(JSONObject jsonObject) {
        Map map = new HashMap();   
        if(jsonObject == null) return map;
        for(Iterator iter = jsonObject.keys(); iter.hasNext();){   
            String key = (String)iter.next();   
            map.put(key, jsonObject.get(key));   
        }   
        return map;   
    }
    
    /**  
     * 从json数组中得到相应java基本类型数组  
     * json形如：["123", "456"]  
     * @param jsonString  
     * @return  
     */  
    public static Object[] getObjectArrayFromJson(String jsonString) { 
    	//LogUtil.debug(JsonUtil.class, "json: " + jsonString);
        JSONArray jsonArray = JSONArray.fromObject(jsonString);   
        return jsonArray.toArray();   
    }   
  
    /**  
     * 从json数组中得到相应java基本类型数组  
     * json形如：["123", "456"]  
     * @param jsonString  
     * @param jsonConfig
     * @return  
     */  
    public static Object[] getObjectArrayFromJson(String jsonString, JsonConfig jsonConfig) { 
    	//LogUtil.debug(JsonUtil.class, "json: " + jsonString);
        JSONArray jsonArray = JSONArray.fromObject(jsonString, jsonConfig);   
        return jsonArray.toArray();   
    }   
    
   
    /**  
     * 根据参数组装返回前台的Json字符串
     * 结果字符串格式为："{\"result\":\"success\",\"msg\":\"客户资料保存成功！\"}" 
     *  
     * @param successOrFail  String
     * @param msg   String
     * @return  String 返回前台的Json格式字符串
     */  
    public static String getJSONRespStr(String successOrFail, String msg) { 
    	
    	String jsonString = "{\"result\":\"fail\",\"msg\":\"返回消息为空！\"}" ;    	
    	if ( !("success".equals(successOrFail) || "fail".equals(successOrFail) ) )
    		return jsonString;
    	if ( msg == null)
    		return jsonString;
    	
    	JSONObject jsonObject = JSONObject.fromObject(jsonString);
    	jsonObject.put("result", successOrFail);
    	jsonObject.put("msg", msg);
    	
    	jsonString = jsonObject.toString();
        //LogUtil.debug(JsonUtil.class, "组装的应答信息: " + jsonString);
        
        return jsonString == null ? "{}" : jsonString;   
    }    
    /**
    * JSON配置，用于配置默认的时间解析器等
    * @return
    */
    public static JsonConfig configJson() {
        return configJson(new String[]{""});
    }

    /**
    * @param excludes 不需要解析的字段
    * @return
    */
    public static JsonConfig configJson(String[] excludes) { 
        return configJson(excludes, null);
    }

    /**
    * @param objectClass 集合中包含的对象的Class
    * @return
    */
    public static JsonConfig configJson(Class objectClass) { 
        return configJson(new String[]{""}, objectClass);
    }
    
	/**
	 * @param excludes 不需要解析的字段
	 * @param objectClass 集合中包含的对象的Class
	 * @return
	 */
	public static JsonConfig configJson(String[] excludes, Class objectClass) {
		JsonConfig jsonConfig = new JsonConfig();
		if (objectClass != null)
			jsonConfig.setRootClass(objectClass);
		jsonConfig.setExcludes(excludes);
		jsonConfig.setIgnoreDefaultExcludes(false);
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor());
		jsonConfig.registerDefaultValueProcessor(Long.class,new NumberProcessor());
		jsonConfig.registerDefaultValueProcessor(Integer.class,new NumberProcessor());
		return jsonConfig;
	}
    
    /**
     * 默认日期格式为："yyyy-MM-dd HH:mm:ss"，如需要其他格式，可通过此方法进行设置
     * 
     * @param datePattern 日期格式，如"yyyy-MM-dd"
     * @return JsonConfig
     */
    public static JsonConfig configJson(String datePattern) {    	
    	JsonConfig jsonConfig = configJson();
    	if(StringUtils.isBlank(datePattern)) return jsonConfig;
    	
    	jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor(datePattern));
    	jsonConfig.registerDefaultValueProcessor(Long.class,new NumberProcessor());
		jsonConfig.registerDefaultValueProcessor(Integer.class,new NumberProcessor());
    	return jsonConfig;
    }
	
    private static void setJsonDataFormat(){   
		//注册日期串的处理格式列表，在Json格式的日期转换到Java.util.Date时使用
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd"}));
    } 
	//json默认会把Number类型的null转换为0，注册此处理器，可使其仍然转换为null
    static class NumberProcessor implements DefaultValueProcessor {    
        public Object getDefaultValue(Class type) {    
            return null;    
        }    
    }
}   
