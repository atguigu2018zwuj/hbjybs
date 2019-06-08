package com.mininglamp.currencySys.common.redis.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisException;

import com.mininglamp.logx.kit.JsonKit;
import com.mininglamp.currencySys.common.redis.BaseRedis;
import com.mininglamp.currencySys.common.util.RedisPoolUtil;
import com.mininglamp.currencySys.util.JsonUtil;

/**
 * 数据层基类，实现公共方法
 * 
 * @author zhangxiaobo 2015年11月4日
 * 
 */
@Repository("baseRedis")
public class BaseRedisImpl implements BaseRedis {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public List<String> hvals(String key) {
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = RedisPoolUtil.getResource();
			return jedis.hvals(key);
		} catch (JedisException e) {
			this.loggerError("", e);
			broken = RedisPoolUtil.handleException(e);
			return null;
		} catch (Exception e) {
			this.loggerError("get hash vals error, key=" + key, e);
			return null;
		} finally {
			RedisPoolUtil.returnResource(jedis, broken);
		}
	}

	
	public String hget(String key, String field) {
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = RedisPoolUtil.getResource();
			return jedis.hget(key, field);
		} catch (JedisException e) {
			this.loggerError("", e);
			broken = RedisPoolUtil.handleException(e);
			return null;
		} catch (Exception e) {
			this.loggerError("get hash value error, key=" + key + " field=" + field, e);
			return null;
		} finally {
			RedisPoolUtil.returnResource(jedis, broken);
		}
	}
//	public static void main(String[] args) {
//		BaseRedisImpl base = new BaseRedisImpl();
////		base.hset("a", "b", "c");
////		System.out.println(base.hget("a", "b"));
//		ShardedJedis jedis = RedisPoolUtil.getShardResource();
//		ResponseBean bean = new ResponseBean();
//		bean.setData(new Date());
//		String val = JsonKit.toJson(bean);
//		jedis.hset("a", "b", val);
//		System.out.println(jedis.hget("a", "b"));
//	}
	
	public void hset(String key, String field, String value) {
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = RedisPoolUtil.getResource();
			jedis.hset(key, field, value);
		} catch (JedisException e) {
			this.loggerError("", e);
			broken = RedisPoolUtil.handleException(e);
		} catch (Exception e) {
			this.loggerError("set hash error", e);
		} finally {
			RedisPoolUtil.returnResource(jedis, broken);
		}
	}

	
	public void hset(String key, Map<String, String> map) {
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = RedisPoolUtil.getResource();
			jedis.hmset(key, map);
		} catch (JedisException e) {
			this.loggerError("", e);
			broken = RedisPoolUtil.handleException(e);
		} catch (Exception e) {
			this.loggerError("set hash error", e);
		} finally {
			RedisPoolUtil.returnResource(jedis, broken);
		}
	}
	
	public void setByte(byte[] key,byte [] value){
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = RedisPoolUtil.getResource();
			jedis.set(key, value);
		} catch (JedisException e) {
			this.loggerError("", e);
			broken = RedisPoolUtil.handleException(e);
		} catch (Exception e) {
			this.loggerError("hincr error", e);
		} finally {
			RedisPoolUtil.returnResource(jedis, broken);
		}
	}
	
	public byte[] getByte(byte[] key){
		Jedis jedis = null;
		boolean broken = false;
		byte[] in  = null;
		try {
			jedis = RedisPoolUtil.getResource();
			in = jedis.get(key);
			return in;
		} catch (JedisException e) {
			this.loggerError("", e);
			broken = RedisPoolUtil.handleException(e);
			return null;
		} catch (Exception e) {
			this.loggerError("hincr error", e);
		} finally {
			RedisPoolUtil.returnResource(jedis, broken);
		}
		return null;
	}
	
	public void hincr(String key, String field, long value) {
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = RedisPoolUtil.getResource();
			jedis.hincrBy(key, field, value);
		} catch (JedisException e) {
			this.loggerError("", e);
			broken = RedisPoolUtil.handleException(e);
		} catch (Exception e) {
			this.loggerError("hincr error", e);
		} finally {
			RedisPoolUtil.returnResource(jedis, broken);
		}
	}
	
	
	public Double zscore(String key,String member){
		Jedis jedis = null;
		boolean broken = false;
		Double score = 0.0;
		try {
			jedis = RedisPoolUtil.getResource();
			score = jedis.zscore(key, member);
			if(score == null){
				return 0.0;
			}
			return score;
		} catch (JedisException e) {
			this.loggerError("", e);
			broken = RedisPoolUtil.handleException(e);
			return null;
		} catch (Exception e) {
			this.loggerError("zscore SortedSets error", e);
			return null;
		} finally {
			RedisPoolUtil.returnResource(jedis, broken);
		}
	}
	
	
	public void zincrby(String key, Double score, String member) {
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = RedisPoolUtil.getResource();
			jedis.zincrby(key, score, member);
		} catch (JedisException e) {
			this.loggerError("",e);
			broken = RedisPoolUtil.handleException(e);
		} catch (Exception e){
			this.loggerError("zincrby SortedSets error", e);
		} finally {
			RedisPoolUtil.returnResource(jedis, broken);
		}
	}

	
	public void zadd(String key,Double score,String member){
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = RedisPoolUtil.getResource();
			jedis.zadd(key, score, member);
		} catch (JedisException e) {
			this.loggerError("",e);
			broken = RedisPoolUtil.handleException(e);
		} catch (Exception e){
			this.loggerError("zadd SortedSets error", e);
		} finally {
			RedisPoolUtil.returnResource(jedis, broken);
		}
	}
	
	
	public Set<Tuple> zrevreangeWithScores(String key, long start, long end){
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = RedisPoolUtil.getResource();
			return jedis.zrevrangeWithScores(key, start, end);
		} catch (JedisException e) {
			this.loggerError("",e);
			broken = RedisPoolUtil.handleException(e);
			return null;
		} catch (Exception e){
			this.loggerError(" get zrevreangeWithScores SortedSets error", e);
			return null;
		} finally {
			RedisPoolUtil.returnResource(jedis, broken);
		}
	}
	
	
	public void del(String key) {
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = RedisPoolUtil.getResource();
			jedis.del(key);
		} catch (JedisException e) {
			this.loggerError("",e);
			broken = RedisPoolUtil.handleException(e);
		} catch (Exception e){
			this.loggerError("del key error", e);
		} finally {
			RedisPoolUtil.returnResource(jedis, broken);
		}
	}
	
	
	public Long rpush(String key, String... strings) {
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = RedisPoolUtil.getResource();
			return jedis.rpush(key, strings);
		} catch (JedisException e) {
			this.loggerError("",e);
			broken = RedisPoolUtil.handleException(e);
			return null;
		} catch (Exception e){
			this.loggerError("rpush list error", e);
			return null;
		} finally {
			RedisPoolUtil.returnResource(jedis, broken);
		}
	}
	
	
	public Long lpush(String key, String... strings) {
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = RedisPoolUtil.getResource();
			return jedis.lpush(key, strings);
		} catch (JedisException e) {
			this.loggerError("",e);
			broken = RedisPoolUtil.handleException(e);
			return null;
		} catch (Exception e){
			this.loggerError("lpush list error", e);
			return null;
		} finally {
			RedisPoolUtil.returnResource(jedis, broken);
		}
	}
	
	public List<String> lrange(String key,long start,long end) {
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = RedisPoolUtil.getResource();
			return jedis.lrange(key, start, end);
		} catch (JedisException e) {
			this.loggerError("", e);
			broken = RedisPoolUtil.handleException(e);
			return null;
		} catch (Exception e) {
			this.loggerError("get lrange list vals error, key=" + key, e);
			return null;
		} finally {
			RedisPoolUtil.returnResource(jedis, broken);
		}
	}
	
	
	
	public Long lrem(String key,long count,String value){
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = RedisPoolUtil.getResource();
			return jedis.lrem(key, count, value);
		} catch (JedisException e) {
			this.loggerError("", e);
			broken = RedisPoolUtil.handleException(e);
			return null;
		} catch (Exception e) {
			this.loggerError("get lrange list vals error, key=" + key, e);
			return null;
		} finally {
			RedisPoolUtil.returnResource(jedis, broken);
		}
	}
	
	
	public <T> long rpush(String key, List<T> value) {
		Jedis jedis = null;
		boolean broken = false;
		long result=0;
		try {
			jedis = RedisPoolUtil.getResource();
			for(int i=0;i<value.size();i++){
				Object t=value.get(i);
				result=jedis.rpush(key, JsonKit.toJson(t));
			}
		} catch (JedisException e) {
			this.loggerError("",e);
			broken = RedisPoolUtil.handleException(e);
			return 0;
		} catch (Exception e){
			logger.error(e.getMessage());
		}  finally {
			RedisPoolUtil.returnResource(jedis, broken);
		}
		return result;
	}
	
	
	public <T> long rpush(String key, List<T> value,int expire) {
		Jedis jedis = null;
		boolean broken = false;
		long result=0;
		try {
			jedis = RedisPoolUtil.getResource();
			for(int i=0;i<value.size();i++){
				if(i==0){
					jedis.expire(key, expire);
				}
				Object t=value.get(i);
				result=jedis.rpush(key, JsonKit.toJson(t));
			}
		} catch (Exception e){
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			RedisPoolUtil.returnResource(jedis, broken);
		}
		return result;
	}
	
	public <T> List<T> range(String key,Class<T> beanClass,int start,int end){
		Jedis jedis = null;
		List<T> result=null;
		List<String> list=null;
		boolean broken = false;
		try {
			jedis = RedisPoolUtil.getResource();
			list=jedis.lrange(key, start, end);
			if(list!=null&&list.size()>0){
				result=new ArrayList<T>(list.size());
				for(String s:list){
					result.add(JsonUtil.getDTO(s, beanClass));
				}
			}
		} catch (Exception e){
			logger.error(e.getMessage());
		} finally {
			RedisPoolUtil.returnResource(jedis, broken);
			if(list!=null){
				list.clear();
			}
		}
		return result;
	}
	
	
	public void loggerError(String msg) {
		logger.error(msg);
	}

	public void loggerError(String msg, Throwable e) {
		logger.error(msg, e);
	}


    @Override
    public void hdel(String key, String field) {
        // TODO Auto-generated method stub
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = RedisPoolUtil.getResource();
            jedis.hdel(key, field);
        } catch (JedisException e) {
            this.loggerError("", e);
            broken = RedisPoolUtil.handleException(e);
        } catch (Exception e) {
            this.loggerError("delete hash error", e);
        } finally {
            RedisPoolUtil.returnResource(jedis, broken);
        }
    }	
}
