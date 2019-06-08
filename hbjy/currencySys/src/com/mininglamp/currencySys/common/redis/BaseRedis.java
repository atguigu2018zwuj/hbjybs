package com.mininglamp.currencySys.common.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Tuple;

/**
 * Redis缓存层基类，定义公共方法
 * 
 * @author zhangxiaobo 2015年11月10日
 * 
 */
public interface BaseRedis {

	/**
	 * 获取redis中hash的所有值
	 * 
	 * @param key
	 * @return
	 */
	public List<String> hvals(String key);

	/**
	 * 获取redis中hash值
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public String hget(String key, String field);
	
	/**
	 * 保存数据到redis的hash中
	 * 
	 * @param key
	 * @param field
	 * @param value
	 */
	public void hset(String key, String field, String value);

	/**
	 * 保存数据到redis的hash中
	 * 
	 * @param key
	 * @param map
	 */
	public void hset(String key, Map<String, String> map);
	/**************
	 * 删除key<->hashmap中的某一项field
	 * @param key
	 * @param field
	 */
	public void hdel(String key,String field);
	/**
	 * 保存数据到redis的hash中,处理集合
	 * @param key
	 * @param value
	 */
	public void setByte(byte[] key,byte [] value);
	public byte[] getByte(byte[] key);
	/**
	 * 增加hash类型中的数值
	 * @param key
	 * @param field
	 * @param value
	 */
	public void hincr(String key, String field, long value);

	/**
	 * 获取有序集合(Sorted set)中，指定成员的分数
	 * @param key
	 * @param keyStr
	 * @return
	 */
	public Double zscore(String key,String member);
	
	/**
	 * 对有序集合内的成员增加计分
	 * @param key
	 * @param score
	 * @param member
	 */
	public void zincrby(String key, Double score, String member);
	
	/**
	 * 添加指定成员到指定有序集合(Sorted set)
	 * @param key
	 * @param score
	 * @param member
	 */
	public void zadd(String key,Double score,String member);
	
	/**
	 * 返回有序集合(Sorted Sets)中，指定区间内的成员，其中成员位置按score值递减(从大到下)来排列
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<Tuple> zrevreangeWithScores(String key, long start, long end);
	
	/**
	 * 删除key
	 * @param key
	 */
	public void del(String key);
	
	/**
	 * 向存于 key 的列表的尾部插入所指定的值。如果key不存在，那么会创建一个空的列表然后在进行 push操作
	 * @param key
	 * @param strings
	 * @return
	 */
	public Long rpush(String key,String ...strings);
	public <T> long rpush(String key,List<T> value);
	public <T> long rpush(String key,List<T> value,int expire);
	
	public <T> List<T> range(String key,Class<T> beanClass,int start,int end);
	
	/**
	 * 向存于 key 的列表的头插入所指定的值。如果key不存在，那么会创建一个空的列表然后在进行 push操作
	 * @param key
	 * @param strings
	 * @return
	 */
	public Long lpush(String key,String ...strings);
	
	/**
	 * 返回存储在  key 的列表里指定范围内的元素
	 * start 和 end偏移量都是基于0的下标，即list的第一个元素下标是0(list的表头),第二个元素下标是1,依次类推。
	 * start 和 end可以为负数，表示从list尾部开始计数 
	 * @param key
	 * @param start    
	 * @param end
	 * @return
	 */
	public List<String> lrange(String key,long start,long end);
	
	/**
	 * 从存于key的列表移除前count次出现的值为value的元素.
	 * count > 0 :从头往尾移除值为value的元素count次.
	 * count < 0 :从尾往头移除值为value的元素count次.
	 * count = 0 :移除所有值为value的元素.
	 * @param key
	 * @param count
	 * @param value
	 * @return
	 */
	public Long lrem(String key,long count,String value);
}
