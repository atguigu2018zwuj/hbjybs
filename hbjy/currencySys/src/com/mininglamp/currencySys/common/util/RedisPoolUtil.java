package com.mininglamp.currencySys.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;

/**
 * Redis连接池工具类
 * 
 * @author zhangxiaobo 2015年11月6日
 */
public class RedisPoolUtil {

	private static Logger logger = LogManager.getLogger(RedisPoolUtil.class);
	private static JedisPool pool = null;
	private static ShardedJedisPool shardedJedisPool = null;

	static {
		Properties prop = new Properties();
		try {
			// 加载配置
			InputStream is = RedisPoolUtil.class.getResourceAsStream("/redis/redis.properties");
			prop.load(is);

			// 读取配置
			String host = prop.getProperty("redis.host");
			int port = Integer.parseInt(prop.getProperty("redis.port", "6379"));
			String pass = prop.getProperty("redis.pass");
			int dbIndex = Integer.parseInt(prop.getProperty("redis.dbIndex", "0"));
			int timeout = Integer.parseInt(prop.getProperty("redis.timeout", "30000"));
			int maxTotal = Integer.parseInt(prop.getProperty("redis.maxTotal", "1000"));
			int maxIdle = Integer.parseInt(prop.getProperty("redis.maxIdle", "100"));
			int minIdle = Integer.parseInt(prop.getProperty("redis.minIdle", "10"));
			int maxWaitMillis = Integer.parseInt(prop.getProperty("redis.maxWaitMillis", "1000"));
			boolean testOnBorrow = Boolean.getBoolean(prop.getProperty("redis.testOnBorrow", "true"));
			boolean testOnReturn = Boolean.getBoolean(prop.getProperty("redis.testOnReturn", "true"));

			// 配置
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(maxTotal);
			config.setMaxIdle(maxIdle);
			config.setMinIdle(minIdle);
			config.setMaxWaitMillis(maxWaitMillis);
			config.setTestOnBorrow(testOnBorrow);
			config.setTestOnReturn(testOnReturn);

			// 实例化连接池
			pool = new JedisPool(config, host, port, timeout, pass, dbIndex);
			
			/**
			 * 分片连接池实例化
			 */
			String master = prop.getProperty("redis.master");
			String slave = prop.getProperty("redis.slave");
			List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
			if(master != null)
				shards.add(new JedisShardInfo(prop.getProperty("redis.master"), port, timeout,"master"));
			if(slave != null)
				shards.add(new JedisShardInfo(prop.getProperty("redis.slave"), port, timeout,"slave"));
			
			shardedJedisPool = new ShardedJedisPool(config, shards);
		} catch (IOException e) {
			logger.error("Init jedis pool error", e);
		}
	}

	private RedisPoolUtil() {

	}
	
	/**
	 * 从连接池中获取Jedis连接
	 * 
	 * @author zhangxiaobo 2015年11月6日
	 */
	public static Jedis getResource() {
		if (pool != null) {
			try {
				return pool.getResource();
			} catch (Exception e) {
				logger.error("get jedis resource error", e);
			}
		}
		return null;
	}

	/**
	 * 归还Jedis连接到连接池中
	 * 
	 * @param jedis
	 * @param broken
	 * @author zhangxiaobo 2015年11月6日
	 */
	public static void returnResource(Jedis jedis, boolean broken) {
		try {
			if(broken) {
				pool.returnBrokenResource(jedis);
			}else {
				pool.returnResource(jedis);
			}
		} catch (Exception e) {
			logger.error("return jedis resource error", e);
			if (jedis != null && jedis.isConnected()) {
				try {
					try {
						jedis.quit();
					} catch (Exception ex) {
						logger.error("jedis quit error", ex);
					}
					jedis.disconnect();
				} catch (Exception ex) {
					logger.error("jedis disconnection error", ex);
				}
			}
		}
	}
	
	/**
	 * 根据异常判断是否为broken resource
	 * 
	 * @param e
	 * @return
	 */
	public static boolean handleException(JedisException e) {
		if (e instanceof JedisConnectionException) {
			logger.error("Redis connection lost.", e);
		} else if (e instanceof JedisDataException) {
			if (e.getMessage() != null
					&& e.getMessage().indexOf("READONLY") != -1) {
				logger.error("Redis connection is read-only.", e);
			} else {
				return false;
			}
		} else {
			logger.error("Jedis exception occured.", e);
		}
		return true;
	}
	public static void main(String[] args) {
		
	}
	/**
	 * 获取分布式连接
	 * @return
	 */
	public static ShardedJedis getShardResource() {
		if (shardedJedisPool != null) {
			try {
				return shardedJedisPool.getResource();
			} catch (Exception e) {
				logger.error("get jedis resource error", e);
			}
		}
		return null;		
	}
}
