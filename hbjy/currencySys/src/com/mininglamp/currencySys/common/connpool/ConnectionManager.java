package com.mininglamp.currencySys.common.connpool;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;

import com.google.common.io.Resources;

/**
 * 连接池管理
 * @author czy
 * 2015年7月29日17:46:48
 *
 */
public class ConnectionManager {
	private static Logger logger = Logger.getLogger(ConnectionManager.class);
	/**
	 * 常量
	 */
	protected static final int INIT_SIZE=14;//初始化连接个数
	protected static final int INCREAMENT_LENGTH=5;//连接增量
	
	private static String url = "";
    private static String user = "";
    private static String pass = "";
    private static String driverClass="";
    
    private static boolean inited;
    /**
     * 所有连接
     */
	protected static List<Connection> all=new ArrayList<Connection>();
	protected static Queue<Connection> unused=new ArrayBlockingQueue<Connection>(100);
	protected static List<Connection> used=new ArrayList<Connection>();
	public static Map<Connection,Long> connectTimes=new HashMap<Connection,Long>();
	
	private ConnectionManager() {
	}
	/**
	 * 初始化服务
	 */
	public static void initService(){
		//加载配置信息
		loadConfig();
		logger.info("es连接池加载配置信息成功");
		//加载驱动
		try {
			Class.forName(driverClass);
			logger.info("es连接池加载驱动["+driverClass+"]成功");
			//生成连接池
			initConnectPool();
			logger.info("es连接池初始化连接成功");
			inited=true;
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	/**
	 * 获取连接
	 * @return 连接
	 */
	public static synchronized Connection getConnection(){
		Connection conn=null;
		//未使用队列直接取出
		if(!unused.isEmpty()){
			conn=unused.remove();
			used.add(conn);
			if(unused.size()<INCREAMENT_LENGTH/2){
				try {
					increamentConnectPool(all.size(),INCREAMENT_LENGTH);
				} catch (SQLException e) {
					logger.error("es连接池扩容失败", e);
					throw new RuntimeException(e);
				}
			}
		}else{
			//建立新连接
			try {
				increamentConnectPool(all.size(),INCREAMENT_LENGTH);
				conn=unused.remove();
				used.add(conn);
			} catch (SQLException e) {
				logger.error("es连接池扩容失败", e);
				throw new RuntimeException(e);
			}
		}
		/**
		 * 检测连接是否有效，若无效则重新获取
		 */
		try {
			if (conn.isClosed()) {
				all.remove(conn);
				used.remove(conn);
				return getConnection();
			}
		} catch (Exception e) {
			logger.error("es连接获取连接状态异常", e);
			all.remove(conn);
			used.remove(conn);
			return getConnection();
		}
		logger.info("线程["+Thread.currentThread().getName()+"]获取连接["+used.size()+"/"+all.size()+"]-["+conn+"]");
		return conn;
	}
	
	/**
	 * 初始化连接池
	 * @throws SQLException 
	 */
	private static void initConnectPool() throws SQLException{
		increamentConnectPool(0,INIT_SIZE);
//		//获取所有地址
//		String[] urls=url.split(",");
//		int urlLen=urls.length;
//		//初始化连接，按顺序建立
//		for(int i=0;i<INIT_SIZE;i++){
//			Connection connection = (Connection) DriverManager.getConnection(urls[i%urlLen], user, pass);
//			all.add(connection);
//			unused.add(connection);
//		}
	}
	/**
	 * 增加连接
	 * @throws SQLException
	 */
	private static void increamentConnectPool(int startIndex,int length) throws SQLException{
		//获取所有地址
		String[] urls=url.split(",");
		int urlLen=urls.length;
		//初始化连接，按顺序建立
		for(int i=startIndex;i<startIndex+length;i++){
			try{
				Connection connection = (Connection) DriverManager.getConnection(urls[i%urlLen], user, pass);
				all.add(connection);
				unused.add(connection);
			}catch(Exception e){
				logger.warn("连接建立失败:"+urls[i%urlLen], e);
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * 加载配置文件
	 */
	private static void loadConfig(){
		Properties pro=new Properties();
		try {
			pro.load(new InputStreamReader(Resources.getResource("ibatis/es.properties").openStream(),"UTF-8"));
			driverClass=pro.getProperty("es.connection.driverclass");
		    url  =pro.getProperty("es.connection.url");
		    user = pro.getProperty("es.connection.username");
	        pass = pro.getProperty("es.connection.password");
		} catch (FileNotFoundException e) {
			logger.error("es配置文件未找到", e);
			throw new RuntimeException(e);
		} catch (IOException e) {
			logger.error("es配置文件读取失败", e);
			throw new RuntimeException(e);
		}
	}
	/**
	 * 归还连接
	 * @param conn
	 */
	public static void returnConnection(Connection conn){
		try {
			if(conn.isClosed()){
				all.remove(conn);
				used.remove(conn);
			}else{
				used.remove(conn);
				unused.add(conn);
			}
		} catch (SQLException e) {
			logger.error("es连接异常", e);
			throw new RuntimeException(e);
		}
	}
	/**
	 * 释放所有连接
	 * @return 释放结果，所有连接关闭为true
	 */
	public static boolean releaseConnections(){
		boolean flag=true;
		used.clear();
		unused.clear();
		logger.info("es连接状态重置成功");
		int len=all.size();
		for(int i=0;i<len;i++){
			Connection conn=all.get(i);
			try {
				if(!conn.isClosed()){
					conn.close();
				}
			} catch (SQLException e) {
				logger.error("es连接关闭失败", e);
				flag=false;
			}
		}
		if(flag) all.clear();
		return flag;
	}
	/**
	 * 获取是否初始化
	 * @return
	 */
	public static boolean isInitialed(){
		return inited;
	}
}
