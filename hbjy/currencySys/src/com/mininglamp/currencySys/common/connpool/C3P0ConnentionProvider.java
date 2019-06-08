package com.mininglamp.currencySys.common.connpool;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.google.common.io.Resources;
import com.mchange.v2.c3p0.ComboPooledDataSource;
  
/** 
 * c3p0连接池管理类 
 * @author mininglamp
 * 
 * 
 */  
public class C3P0ConnentionProvider {  
    private static Logger logger = Logger.getLogger(C3P0ConnentionProvider.class);
    private static final String JDBC_DRIVER = "driverClass";  
    private static final String JDBC_URL = "jdbcUrl";  
    
    private static ComboPooledDataSource dataSourceImpala;  
    /** 
     * 初始化连接池代码块 
     */  
    public C3P0ConnentionProvider(){
        initDBSource();
    }
    /** 
     * 初始化c3p0连接池 
     */  
    private   void initDBSource(){  
        Properties c3p0Pro = new Properties();  
        try {  
            //加载配置文件  
            c3p0Pro.load(new InputStreamReader(Resources.getResource("ibatis/jdbc.impala.properties").openStream(),"UTF-8"));  
        } catch (Exception e) {  
            e.printStackTrace();  
			throw new RuntimeException(e);
        }   
        
        
        try {  
            dataSourceImpala = new ComboPooledDataSource();  
            dataSourceImpala.setUser("");  
            dataSourceImpala.setPassword("");  
            dataSourceImpala.setJdbcUrl(c3p0Pro.getProperty(JDBC_URL));  
            dataSourceImpala.setDriverClass(c3p0Pro.getProperty(JDBC_DRIVER));  
            // 设置初始连接池的大小！  
            dataSourceImpala.setInitialPoolSize(10);  
            // 设置连接池的最小值！   
            dataSourceImpala.setMinPoolSize(10);  
            // 设置连接池的最大值！   
            dataSourceImpala.setMaxPoolSize(80);  
            // 设置连接池中的最大Statements数量！  
            dataSourceImpala.setMaxStatements(50);  
            // 设置连接池的最大空闲时间！  
            dataSourceImpala.setMaxIdleTime(60);
            //设置异步进程数
            dataSourceImpala.setNumHelperThreads(10);
            dataSourceImpala.setTestConnectionOnCheckin(true);
            dataSourceImpala.setUnreturnedConnectionTimeout(600);
            dataSourceImpala.setDebugUnreturnedConnectionStackTraces(true);
            
        } catch ( Exception e) {  
            throw new RuntimeException(e);  
        }  
        /******
        String drverClass = c3p0Pro.getProperty(JDBC_DRIVER);  
        if(drverClass != null){  
            try {  
                //加载驱动类  
                Class.forName(drverClass);  
            } catch (ClassNotFoundException e) {  
                e.printStackTrace();  
            }  
              
        }  
          
        Properties jdbcpropes = new Properties();  
        Properties c3propes = new Properties();  
        for(Object key:c3p0Pro.keySet()){  
            String skey = (String)key;  
            if(skey.startsWith("c3p0.")){  
                c3propes.put(skey, c3p0Pro.getProperty(skey));  
            }else{  
                jdbcpropes.put(skey, c3p0Pro.getProperty(skey));  
            }  
        }  
          
        try {  
            //建立连接池  
            DataSource unPooled = DataSources.unpooledDataSource(c3p0Pro.getProperty(JDBC_URL),jdbcpropes);  
            ds = DataSources.pooledDataSource(unPooled,c3propes);  
              
           
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        
        ****/
        logger.info("初始化连接池结束!");
    }  
      
    /** 
     * 获取数据库连接对象 
     * @return 数据连接对象 
     * @throws SQLException  
     */  
    public  synchronized Connection getConnection() throws SQLException{  
    	
    	Connection conn = null;
         try {
        	 if(dataSourceImpala != null){
        		 conn = dataSourceImpala.getConnection();  
        		 logger.info("连接数: " + dataSourceImpala.getNumConnections());
        	 }
		} catch (Exception e) {
			throw new RuntimeException("无法获得Impala数据源连接");
		}
        return conn;  
        
    }  
    
    
}  