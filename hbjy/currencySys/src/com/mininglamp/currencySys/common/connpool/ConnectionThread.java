package com.mininglamp.currencySys.common.connpool;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * 连接监控
 * @author czy
 * 2015年7月30日16:17:44
 */
public class ConnectionThread extends Thread{
	private static Logger logger = Logger.getLogger(ConnectionThread.class);
	private final long WAIT_TIME=1000*60*10;
    /**
     * 每分钟检测一次
     */
	@Override
	public void run() {
		logger.info("es连接池监控线程启动，检测间隔："+WAIT_TIME+"S.");
		while(true){
			try{
				/**
				 * 查询已使用的连接
				 * 超时过多的放入未使用
				 */
				for(Connection conn:ConnectionManager.used){
					Long startTime=ConnectionManager.connectTimes.get(conn);
					long currentTime=System.currentTimeMillis();
					if(startTime!=null&&startTime>0&&currentTime-startTime>WAIT_TIME){
						try {
							if (!conn.isClosed()) {
								conn.close();
								logger.info("es连接关闭["+conn+"]");
							}
						} catch (SQLException e) {
							logger.error("关闭失败", e);
							throw new RuntimeException(e);
						}
					}
				}
				
				/**
				 * 查询未使用
				 * 过多则释放部分连接
				 */
				int length=ConnectionManager.unused.size();
				for(int i=0;i<length-ConnectionManager.INIT_SIZE-ConnectionManager.INCREAMENT_LENGTH;i++){
					Connection conn=ConnectionManager.unused.remove();
					ConnectionManager.all.remove(conn);
					try {
						if (!conn.isClosed()) {
							conn.close();
							logger.info("es连接关闭["+conn+"]");
						}
						conn=null;
					} catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				}
				/**
				 * 查询所有连接
				 * 保活 发送select to_personset_empcode from test2/person_paper_acc_inn_chg_all2 limit 1
				 */
				
				
				/**
				 * 输出连接情况
				 */
				logger.info("es连接[ 总数 ]："+ConnectionManager.all.size());
				logger.info("es连接[ 正在使用连接 ]："+ConnectionManager.used.size());
				logger.info("es连接[ 未使用连接 ]："+ConnectionManager.unused.size());
				try {
					Thread.sleep(1000*60);
					logger.info("es连接池监控线执行完毕");
				} catch (InterruptedException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}catch(Exception e){
				logger.error("espool监控线程异常", e);
				throw new RuntimeException(e);
			}
		}
		
		
	}
	
	
	
}
